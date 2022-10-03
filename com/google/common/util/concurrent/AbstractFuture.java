/*      */ package com.google.common.util.concurrent;
/*      */ 
/*      */ import com.google.common.annotations.Beta;
/*      */ import com.google.common.annotations.GwtCompatible;
/*      */ import com.google.common.base.Preconditions;
/*      */ import com.google.common.base.Throwables;
/*      */ import com.google.common.util.concurrent.internal.InternalFutureFailureAccess;
/*      */ import com.google.common.util.concurrent.internal.InternalFutures;
/*      */ import com.google.errorprone.annotations.CanIgnoreReturnValue;
/*      */ import com.google.errorprone.annotations.ForOverride;
/*      */ import com.google.j2objc.annotations.ReflectionSupport;
/*      */ import java.lang.reflect.Field;
/*      */ import java.security.AccessController;
/*      */ import java.security.PrivilegedActionException;
/*      */ import java.security.PrivilegedExceptionAction;
/*      */ import java.util.Locale;
/*      */ import java.util.concurrent.CancellationException;
/*      */ import java.util.concurrent.ExecutionException;
/*      */ import java.util.concurrent.Executor;
/*      */ import java.util.concurrent.Future;
/*      */ import java.util.concurrent.ScheduledFuture;
/*      */ import java.util.concurrent.TimeUnit;
/*      */ import java.util.concurrent.TimeoutException;
/*      */ import java.util.concurrent.atomic.AtomicReferenceFieldUpdater;
/*      */ import java.util.concurrent.locks.LockSupport;
/*      */ import java.util.logging.Level;
/*      */ import java.util.logging.Logger;
/*      */ import sun.misc.Unsafe;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ @GwtCompatible(emulated = true)
/*      */ @ReflectionSupport(ReflectionSupport.Level.FULL)
/*      */ public abstract class AbstractFuture<V>
/*      */   extends InternalFutureFailureAccess
/*      */   implements ListenableFuture<V>
/*      */ {
/*      */   static {
/*      */     AtomicHelper helper;
/*      */   }
/*      */   
/*   72 */   private static final boolean GENERATE_CANCELLATION_CAUSES = Boolean.parseBoolean(
/*   73 */       System.getProperty("guava.concurrent.generate_cancellation_cause", "false"));
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   static interface Trusted<V>
/*      */     extends ListenableFuture<V> {}
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   static abstract class TrustedFuture<V>
/*      */     extends AbstractFuture<V>
/*      */     implements Trusted<V>
/*      */   {
/*      */     @CanIgnoreReturnValue
/*      */     public final V get() throws InterruptedException, ExecutionException {
/*   90 */       return super.get();
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     @CanIgnoreReturnValue
/*      */     public final V get(long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
/*   97 */       return super.get(timeout, unit);
/*      */     }
/*      */ 
/*      */     
/*      */     public final boolean isDone() {
/*  102 */       return super.isDone();
/*      */     }
/*      */ 
/*      */     
/*      */     public final boolean isCancelled() {
/*  107 */       return super.isCancelled();
/*      */     }
/*      */ 
/*      */     
/*      */     public final void addListener(Runnable listener, Executor executor) {
/*  112 */       super.addListener(listener, executor);
/*      */     }
/*      */ 
/*      */     
/*      */     @CanIgnoreReturnValue
/*      */     public final boolean cancel(boolean mayInterruptIfRunning) {
/*  118 */       return super.cancel(mayInterruptIfRunning);
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*  123 */   private static final Logger log = Logger.getLogger(AbstractFuture.class.getName());
/*      */ 
/*      */   
/*      */   private static final long SPIN_THRESHOLD_NANOS = 1000L;
/*      */ 
/*      */   
/*      */   private static final AtomicHelper ATOMIC_HELPER;
/*      */ 
/*      */   
/*      */   static {
/*  133 */     Throwable thrownUnsafeFailure = null;
/*  134 */     Throwable thrownAtomicReferenceFieldUpdaterFailure = null;
/*      */     
/*      */     try {
/*  137 */       helper = new UnsafeAtomicHelper();
/*  138 */     } catch (Throwable unsafeFailure) {
/*  139 */       thrownUnsafeFailure = unsafeFailure;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*      */       try {
/*  150 */         helper = new SafeAtomicHelper(AtomicReferenceFieldUpdater.newUpdater(Waiter.class, Thread.class, "thread"), AtomicReferenceFieldUpdater.newUpdater(Waiter.class, Waiter.class, "next"), AtomicReferenceFieldUpdater.newUpdater(AbstractFuture.class, Waiter.class, "waiters"), AtomicReferenceFieldUpdater.newUpdater(AbstractFuture.class, Listener.class, "listeners"), AtomicReferenceFieldUpdater.newUpdater(AbstractFuture.class, Object.class, "value"));
/*  151 */       } catch (Throwable atomicReferenceFieldUpdaterFailure) {
/*      */ 
/*      */ 
/*      */ 
/*      */         
/*  156 */         thrownAtomicReferenceFieldUpdaterFailure = atomicReferenceFieldUpdaterFailure;
/*  157 */         helper = new SynchronizedHelper();
/*      */       } 
/*      */     } 
/*  160 */     ATOMIC_HELPER = helper;
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  165 */     Class<?> ensureLoaded = LockSupport.class;
/*      */ 
/*      */ 
/*      */     
/*  169 */     if (thrownAtomicReferenceFieldUpdaterFailure != null) {
/*  170 */       log.log(Level.SEVERE, "UnsafeAtomicHelper is broken!", thrownUnsafeFailure);
/*  171 */       log.log(Level.SEVERE, "SafeAtomicHelper is broken!", thrownAtomicReferenceFieldUpdaterFailure);
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   private static final class Waiter
/*      */   {
/*  178 */     static final Waiter TOMBSTONE = new Waiter(false);
/*      */ 
/*      */     
/*      */     volatile Thread thread;
/*      */ 
/*      */     
/*      */     volatile Waiter next;
/*      */ 
/*      */     
/*      */     Waiter(boolean unused) {}
/*      */ 
/*      */     
/*      */     Waiter() {
/*  191 */       AbstractFuture.ATOMIC_HELPER.putThread(this, Thread.currentThread());
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     void setNext(Waiter next) {
/*  197 */       AbstractFuture.ATOMIC_HELPER.putNext(this, next);
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     void unpark() {
/*  204 */       Thread w = this.thread;
/*  205 */       if (w != null) {
/*  206 */         this.thread = null;
/*  207 */         LockSupport.unpark(w);
/*      */       } 
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void removeWaiter(Waiter node) {
/*  224 */     node.thread = null;
/*      */     
/*      */     label22: while (true) {
/*  227 */       Waiter pred = null;
/*  228 */       Waiter curr = this.waiters;
/*  229 */       if (curr == Waiter.TOMBSTONE) {
/*      */         return;
/*      */       }
/*      */       
/*  233 */       while (curr != null) {
/*  234 */         Waiter succ = curr.next;
/*  235 */         if (curr.thread != null) {
/*  236 */           pred = curr;
/*  237 */         } else if (pred != null) {
/*  238 */           pred.next = succ;
/*  239 */           if (pred.thread == null) {
/*      */             continue label22;
/*      */           }
/*  242 */         } else if (!ATOMIC_HELPER.casWaiters(this, curr, succ)) {
/*      */           continue label22;
/*      */         } 
/*  245 */         curr = succ;
/*      */       } 
/*      */       break;
/*      */     } 
/*      */   }
/*      */   
/*      */   private static final class Listener
/*      */   {
/*  253 */     static final Listener TOMBSTONE = new Listener(null, null);
/*      */     
/*      */     final Runnable task;
/*      */     
/*      */     final Executor executor;
/*      */     Listener next;
/*      */     
/*      */     Listener(Runnable task, Executor executor) {
/*  261 */       this.task = task;
/*  262 */       this.executor = executor;
/*      */     }
/*      */   }
/*      */   private volatile Object value;
/*      */   private volatile Listener listeners;
/*  267 */   private static final Object NULL = new Object();
/*      */   private volatile Waiter waiters;
/*      */   
/*      */   private static final class Failure {
/*  271 */     static final Failure FALLBACK_INSTANCE = new Failure(new Throwable("Failure occurred while trying to finish a future.")
/*      */         {
/*      */           
/*      */           public synchronized Throwable fillInStackTrace()
/*      */           {
/*  276 */             return this;
/*      */           }
/*      */         });
/*      */ 
/*      */     
/*      */     Failure(Throwable exception) {
/*  282 */       this.exception = (Throwable)Preconditions.checkNotNull(exception);
/*      */     }
/*      */     
/*      */     final Throwable exception; }
/*      */   
/*      */   private static final class Cancellation { static final Cancellation CAUSELESS_INTERRUPTED;
/*      */     static final Cancellation CAUSELESS_CANCELLED;
/*      */     final boolean wasInterrupted;
/*      */     final Throwable cause;
/*      */     
/*      */     static {
/*  293 */       if (AbstractFuture.GENERATE_CANCELLATION_CAUSES) {
/*  294 */         CAUSELESS_CANCELLED = null;
/*  295 */         CAUSELESS_INTERRUPTED = null;
/*      */       } else {
/*  297 */         CAUSELESS_CANCELLED = new Cancellation(false, null);
/*  298 */         CAUSELESS_INTERRUPTED = new Cancellation(true, null);
/*      */       } 
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     Cancellation(boolean wasInterrupted, Throwable cause) {
/*  306 */       this.wasInterrupted = wasInterrupted;
/*  307 */       this.cause = cause;
/*      */     } }
/*      */ 
/*      */   
/*      */   private static final class SetFuture<V>
/*      */     implements Runnable {
/*      */     final AbstractFuture<V> owner;
/*      */     final ListenableFuture<? extends V> future;
/*      */     
/*      */     SetFuture(AbstractFuture<V> owner, ListenableFuture<? extends V> future) {
/*  317 */       this.owner = owner;
/*  318 */       this.future = future;
/*      */     }
/*      */ 
/*      */     
/*      */     public void run() {
/*  323 */       if (this.owner.value != this) {
/*      */         return;
/*      */       }
/*      */       
/*  327 */       Object valueToSet = AbstractFuture.getFutureValue(this.future);
/*  328 */       if (AbstractFuture.ATOMIC_HELPER.casValue(this.owner, this, valueToSet)) {
/*  329 */         AbstractFuture.complete(this.owner);
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @CanIgnoreReturnValue
/*      */   public V get(long timeout, TimeUnit unit) throws InterruptedException, TimeoutException, ExecutionException {
/*  400 */     long timeoutNanos = unit.toNanos(timeout);
/*  401 */     long remainingNanos = timeoutNanos;
/*  402 */     if (Thread.interrupted()) {
/*  403 */       throw new InterruptedException();
/*      */     }
/*  405 */     Object localValue = this.value;
/*  406 */     if ((((localValue != null) ? 1 : 0) & (!(localValue instanceof SetFuture) ? 1 : 0)) != 0) {
/*  407 */       return getDoneValue(localValue);
/*      */     }
/*      */     
/*  410 */     long endNanos = (remainingNanos > 0L) ? (System.nanoTime() + remainingNanos) : 0L;
/*      */     
/*  412 */     if (remainingNanos >= 1000L) {
/*  413 */       Waiter oldHead = this.waiters;
/*  414 */       if (oldHead != Waiter.TOMBSTONE) {
/*  415 */         Waiter node = new Waiter();
/*      */         label77: while (true) {
/*  417 */           node.setNext(oldHead);
/*  418 */           if (ATOMIC_HELPER.casWaiters(this, oldHead, node)) {
/*      */             do {
/*  420 */               LockSupport.parkNanos(this, remainingNanos);
/*      */               
/*  422 */               if (Thread.interrupted()) {
/*  423 */                 removeWaiter(node);
/*  424 */                 throw new InterruptedException();
/*      */               } 
/*      */ 
/*      */ 
/*      */               
/*  429 */               localValue = this.value;
/*  430 */               if ((((localValue != null) ? 1 : 0) & (!(localValue instanceof SetFuture) ? 1 : 0)) != 0) {
/*  431 */                 return getDoneValue(localValue);
/*      */               }
/*      */ 
/*      */               
/*  435 */               remainingNanos = endNanos - System.nanoTime();
/*  436 */             } while (remainingNanos >= 1000L);
/*      */             
/*  438 */             removeWaiter(node);
/*      */             
/*      */             break;
/*      */           } 
/*      */           
/*  443 */           oldHead = this.waiters;
/*  444 */           if (oldHead == Waiter.TOMBSTONE)
/*      */             break label77; 
/*      */         } 
/*      */       } else {
/*  448 */         return getDoneValue(this.value);
/*      */       } 
/*      */     } 
/*      */     
/*  452 */     while (remainingNanos > 0L) {
/*  453 */       localValue = this.value;
/*  454 */       if ((((localValue != null) ? 1 : 0) & (!(localValue instanceof SetFuture) ? 1 : 0)) != 0) {
/*  455 */         return getDoneValue(localValue);
/*      */       }
/*  457 */       if (Thread.interrupted()) {
/*  458 */         throw new InterruptedException();
/*      */       }
/*  460 */       remainingNanos = endNanos - System.nanoTime();
/*      */     } 
/*      */     
/*  463 */     String futureToString = toString();
/*  464 */     String unitString = unit.toString().toLowerCase(Locale.ROOT);
/*  465 */     String message = "Waited " + timeout + " " + unit.toString().toLowerCase(Locale.ROOT);
/*      */     
/*  467 */     if (remainingNanos + 1000L < 0L) {
/*      */       
/*  469 */       message = message + " (plus ";
/*  470 */       long overWaitNanos = -remainingNanos;
/*  471 */       long overWaitUnits = unit.convert(overWaitNanos, TimeUnit.NANOSECONDS);
/*  472 */       long overWaitLeftoverNanos = overWaitNanos - unit.toNanos(overWaitUnits);
/*  473 */       boolean shouldShowExtraNanos = (overWaitUnits == 0L || overWaitLeftoverNanos > 1000L);
/*      */       
/*  475 */       if (overWaitUnits > 0L) {
/*  476 */         message = message + overWaitUnits + " " + unitString;
/*  477 */         if (shouldShowExtraNanos) {
/*  478 */           message = message + ",";
/*      */         }
/*  480 */         message = message + " ";
/*      */       } 
/*  482 */       if (shouldShowExtraNanos) {
/*  483 */         message = message + overWaitLeftoverNanos + " nanoseconds ";
/*      */       }
/*      */       
/*  486 */       message = message + "delay)";
/*      */     } 
/*      */ 
/*      */ 
/*      */     
/*  491 */     if (isDone()) {
/*  492 */       throw new TimeoutException(message + " but future completed as timeout expired");
/*      */     }
/*  494 */     throw new TimeoutException(message + " for " + futureToString);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @CanIgnoreReturnValue
/*      */   public V get() throws InterruptedException, ExecutionException {
/*  508 */     if (Thread.interrupted()) {
/*  509 */       throw new InterruptedException();
/*      */     }
/*  511 */     Object localValue = this.value;
/*  512 */     if ((((localValue != null) ? 1 : 0) & (!(localValue instanceof SetFuture) ? 1 : 0)) != 0) {
/*  513 */       return getDoneValue(localValue);
/*      */     }
/*  515 */     Waiter oldHead = this.waiters;
/*  516 */     if (oldHead != Waiter.TOMBSTONE) {
/*  517 */       Waiter node = new Waiter();
/*      */       do {
/*  519 */         node.setNext(oldHead);
/*  520 */         if (ATOMIC_HELPER.casWaiters(this, oldHead, node)) {
/*      */           while (true) {
/*      */             
/*  523 */             LockSupport.park(this);
/*      */             
/*  525 */             if (Thread.interrupted()) {
/*  526 */               removeWaiter(node);
/*  527 */               throw new InterruptedException();
/*      */             } 
/*      */ 
/*      */             
/*  531 */             localValue = this.value;
/*  532 */             if ((((localValue != null) ? 1 : 0) & (!(localValue instanceof SetFuture) ? 1 : 0)) != 0) {
/*  533 */               return getDoneValue(localValue);
/*      */             }
/*      */           } 
/*      */         }
/*  537 */         oldHead = this.waiters;
/*  538 */       } while (oldHead != Waiter.TOMBSTONE);
/*      */     } 
/*      */ 
/*      */     
/*  542 */     return getDoneValue(this.value);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private V getDoneValue(Object obj) throws ExecutionException {
/*  549 */     if (obj instanceof Cancellation)
/*  550 */       throw cancellationExceptionWithCause("Task was cancelled.", ((Cancellation)obj).cause); 
/*  551 */     if (obj instanceof Failure)
/*  552 */       throw new ExecutionException(((Failure)obj).exception); 
/*  553 */     if (obj == NULL) {
/*  554 */       return null;
/*      */     }
/*      */     
/*  557 */     V asV = (V)obj;
/*  558 */     return asV;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean isDone() {
/*  564 */     Object localValue = this.value;
/*  565 */     return ((localValue != null)) & (!(localValue instanceof SetFuture));
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean isCancelled() {
/*  570 */     Object localValue = this.value;
/*  571 */     return localValue instanceof Cancellation;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @CanIgnoreReturnValue
/*      */   public boolean cancel(boolean mayInterruptIfRunning) {
/*  590 */     Object localValue = this.value;
/*  591 */     boolean rValue = false;
/*  592 */     if ((((localValue == null) ? 1 : 0) | localValue instanceof SetFuture) != 0) {
/*      */ 
/*      */       
/*  595 */       Object valueToSet = GENERATE_CANCELLATION_CAUSES ? new Cancellation(mayInterruptIfRunning, new CancellationException("Future.cancel() was called.")) : (mayInterruptIfRunning ? Cancellation.CAUSELESS_INTERRUPTED : Cancellation.CAUSELESS_CANCELLED);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*  602 */       AbstractFuture<?> abstractFuture = this;
/*      */       do {
/*  604 */         while (ATOMIC_HELPER.casValue(abstractFuture, localValue, valueToSet)) {
/*  605 */           rValue = true;
/*      */ 
/*      */           
/*  608 */           if (mayInterruptIfRunning) {
/*  609 */             abstractFuture.interruptTask();
/*      */           }
/*  611 */           complete(abstractFuture);
/*  612 */           if (localValue instanceof SetFuture) {
/*      */ 
/*      */             
/*  615 */             ListenableFuture<?> futureToPropagateTo = ((SetFuture)localValue).future;
/*  616 */             if (futureToPropagateTo instanceof Trusted) {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */               
/*  624 */               AbstractFuture<?> trusted = (AbstractFuture)futureToPropagateTo;
/*  625 */               localValue = trusted.value;
/*  626 */               if ((((localValue == null) ? 1 : 0) | localValue instanceof SetFuture) != 0) {
/*  627 */                 abstractFuture = trusted;
/*      */                 continue;
/*      */               } 
/*      */               // Byte code: goto -> 190
/*      */             } 
/*  632 */             futureToPropagateTo.cancel(mayInterruptIfRunning);
/*      */             
/*      */             break;
/*      */           } 
/*      */           // Byte code: goto -> 190
/*      */         } 
/*  638 */         localValue = abstractFuture.value;
/*  639 */       } while (localValue instanceof SetFuture);
/*      */     } 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  647 */     return rValue;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected void interruptTask() {}
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected final boolean wasInterrupted() {
/*  670 */     Object localValue = this.value;
/*  671 */     return (localValue instanceof Cancellation && ((Cancellation)localValue).wasInterrupted);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void addListener(Runnable listener, Executor executor) {
/*  681 */     Preconditions.checkNotNull(listener, "Runnable was null.");
/*  682 */     Preconditions.checkNotNull(executor, "Executor was null.");
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  692 */     if (!isDone()) {
/*  693 */       Listener oldHead = this.listeners;
/*  694 */       if (oldHead != Listener.TOMBSTONE) {
/*  695 */         Listener newNode = new Listener(listener, executor);
/*      */         do {
/*  697 */           newNode.next = oldHead;
/*  698 */           if (ATOMIC_HELPER.casListeners(this, oldHead, newNode)) {
/*      */             return;
/*      */           }
/*  701 */           oldHead = this.listeners;
/*  702 */         } while (oldHead != Listener.TOMBSTONE);
/*      */       } 
/*      */     } 
/*      */ 
/*      */     
/*  707 */     executeListener(listener, executor);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @CanIgnoreReturnValue
/*      */   protected boolean set(V value) {
/*  724 */     Object valueToSet = (value == null) ? NULL : value;
/*  725 */     if (ATOMIC_HELPER.casValue(this, null, valueToSet)) {
/*  726 */       complete(this);
/*  727 */       return true;
/*      */     } 
/*  729 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @CanIgnoreReturnValue
/*      */   protected boolean setException(Throwable throwable) {
/*  746 */     Object valueToSet = new Failure((Throwable)Preconditions.checkNotNull(throwable));
/*  747 */     if (ATOMIC_HELPER.casValue(this, null, valueToSet)) {
/*  748 */       complete(this);
/*  749 */       return true;
/*      */     } 
/*  751 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Beta
/*      */   @CanIgnoreReturnValue
/*      */   protected boolean setFuture(ListenableFuture<? extends V> future) {
/*  783 */     Preconditions.checkNotNull(future);
/*  784 */     Object localValue = this.value;
/*  785 */     if (localValue == null) {
/*  786 */       if (future.isDone()) {
/*  787 */         Object value = getFutureValue(future);
/*  788 */         if (ATOMIC_HELPER.casValue(this, null, value)) {
/*  789 */           complete(this);
/*  790 */           return true;
/*      */         } 
/*  792 */         return false;
/*      */       } 
/*  794 */       SetFuture<V> valueToSet = new SetFuture<>(this, future);
/*  795 */       if (ATOMIC_HELPER.casValue(this, null, valueToSet)) {
/*      */ 
/*      */         
/*      */         try {
/*  799 */           future.addListener(valueToSet, DirectExecutor.INSTANCE);
/*  800 */         } catch (Throwable t) {
/*      */           Failure failure;
/*      */ 
/*      */ 
/*      */           
/*      */           try {
/*  806 */             failure = new Failure(t);
/*  807 */           } catch (Throwable oomMostLikely) {
/*  808 */             failure = Failure.FALLBACK_INSTANCE;
/*      */           } 
/*      */           
/*  811 */           boolean bool = ATOMIC_HELPER.casValue(this, valueToSet, failure);
/*      */         } 
/*  813 */         return true;
/*      */       } 
/*  815 */       localValue = this.value;
/*      */     } 
/*      */ 
/*      */     
/*  819 */     if (localValue instanceof Cancellation)
/*      */     {
/*  821 */       future.cancel(((Cancellation)localValue).wasInterrupted);
/*      */     }
/*  823 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static Object getFutureValue(ListenableFuture<?> future) {
/*  833 */     if (future instanceof Trusted) {
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*  838 */       Object v = ((AbstractFuture)future).value;
/*  839 */       if (v instanceof Cancellation) {
/*      */ 
/*      */ 
/*      */         
/*  843 */         Cancellation c = (Cancellation)v;
/*  844 */         if (c.wasInterrupted) {
/*  845 */           v = (c.cause != null) ? new Cancellation(false, c.cause) : Cancellation.CAUSELESS_CANCELLED;
/*      */         }
/*      */       } 
/*      */ 
/*      */ 
/*      */       
/*  851 */       return v;
/*      */     } 
/*  853 */     if (future instanceof InternalFutureFailureAccess) {
/*      */       
/*  855 */       Throwable throwable = InternalFutures.tryInternalFastPathGetFailure((InternalFutureFailureAccess)future);
/*  856 */       if (throwable != null) {
/*  857 */         return new Failure(throwable);
/*      */       }
/*      */     } 
/*  860 */     boolean wasCancelled = future.isCancelled();
/*      */     
/*  862 */     if (((!GENERATE_CANCELLATION_CAUSES ? 1 : 0) & wasCancelled) != 0) {
/*  863 */       return Cancellation.CAUSELESS_CANCELLED;
/*      */     }
/*      */     
/*      */     try {
/*  867 */       Object v = getUninterruptibly(future);
/*  868 */       if (wasCancelled) {
/*  869 */         return new Cancellation(false, new IllegalArgumentException("get() did not throw CancellationException, despite reporting isCancelled() == true: " + future));
/*      */       }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*  876 */       return (v == null) ? NULL : v;
/*  877 */     } catch (ExecutionException exception) {
/*  878 */       if (wasCancelled) {
/*  879 */         return new Cancellation(false, new IllegalArgumentException("get() did not throw CancellationException, despite reporting isCancelled() == true: " + future, exception));
/*      */       }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*  887 */       return new Failure(exception.getCause());
/*  888 */     } catch (CancellationException cancellation) {
/*  889 */       if (!wasCancelled) {
/*  890 */         return new Failure(new IllegalArgumentException("get() threw CancellationException, despite reporting isCancelled() == false: " + future, cancellation));
/*      */       }
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*  896 */       return new Cancellation(false, cancellation);
/*  897 */     } catch (Throwable t) {
/*  898 */       return new Failure(t);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static <V> V getUninterruptibly(Future<V> future) throws ExecutionException {
/*  907 */     boolean interrupted = false;
/*      */     
/*      */     while (true) {
/*      */       try {
/*  911 */         return future.get();
/*  912 */       } catch (InterruptedException e) {
/*      */ 
/*      */       
/*      */       } finally {
/*      */         
/*  917 */         if (interrupted) {
/*  918 */           Thread.currentThread().interrupt();
/*      */         }
/*      */       } 
/*      */     } 
/*      */   }
/*      */   
/*      */   private static void complete(AbstractFuture<?> future) {
/*  925 */     Listener next = null;
/*      */     
/*      */     label17: while (true) {
/*  928 */       future.releaseWaiters();
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*  934 */       future.afterDone();
/*      */       
/*  936 */       next = future.clearListeners(next);
/*  937 */       future = null;
/*  938 */       while (next != null) {
/*  939 */         Listener curr = next;
/*  940 */         next = next.next;
/*  941 */         Runnable task = curr.task;
/*  942 */         if (task instanceof SetFuture) {
/*  943 */           SetFuture<?> setFuture = (SetFuture)task;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */           
/*  949 */           future = setFuture.owner;
/*  950 */           if (future.value == setFuture) {
/*  951 */             Object valueToSet = getFutureValue(setFuture.future);
/*  952 */             if (ATOMIC_HELPER.casValue(future, setFuture, valueToSet)) {
/*      */               continue label17;
/*      */             }
/*      */           } 
/*      */           continue;
/*      */         } 
/*  958 */         executeListener(task, curr.executor);
/*      */       } 
/*      */       break;
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Beta
/*      */   @ForOverride
/*      */   protected void afterDone() {}
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected final Throwable tryInternalFastPathGetFailure() {
/* 1004 */     if (this instanceof Trusted) {
/* 1005 */       Object obj = this.value;
/* 1006 */       if (obj instanceof Failure) {
/* 1007 */         return ((Failure)obj).exception;
/*      */       }
/*      */     } 
/* 1010 */     return null;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   final void maybePropagateCancellationTo(Future<?> related) {
/* 1018 */     if ((((related != null) ? 1 : 0) & isCancelled()) != 0) {
/* 1019 */       related.cancel(wasInterrupted());
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private void releaseWaiters() {
/*      */     while (true) {
/* 1027 */       Waiter head = this.waiters;
/* 1028 */       if (ATOMIC_HELPER.casWaiters(this, head, Waiter.TOMBSTONE)) {
/* 1029 */         for (Waiter currentWaiter = head; currentWaiter != null; currentWaiter = currentWaiter.next) {
/* 1030 */           currentWaiter.unpark();
/*      */         }
/*      */         return;
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private Listener clearListeners(Listener onto) {
/*      */     while (true) {
/* 1047 */       Listener head = this.listeners;
/* 1048 */       if (ATOMIC_HELPER.casListeners(this, head, Listener.TOMBSTONE)) {
/* 1049 */         Listener reversedList = onto;
/* 1050 */         while (head != null) {
/* 1051 */           Listener tmp = head;
/* 1052 */           head = head.next;
/* 1053 */           tmp.next = reversedList;
/* 1054 */           reversedList = tmp;
/*      */         } 
/* 1056 */         return reversedList;
/*      */       } 
/*      */     } 
/*      */   }
/*      */   
/*      */   public String toString() {
/* 1062 */     StringBuilder builder = (new StringBuilder()).append(super.toString()).append("[status=");
/* 1063 */     if (isCancelled()) {
/* 1064 */       builder.append("CANCELLED");
/* 1065 */     } else if (isDone()) {
/* 1066 */       addDoneString(builder);
/*      */     } else {
/*      */       String pendingDescription;
/*      */       try {
/* 1070 */         pendingDescription = pendingToString();
/* 1071 */       } catch (RuntimeException e) {
/*      */ 
/*      */         
/* 1074 */         pendingDescription = "Exception thrown from implementation: " + e.getClass();
/*      */       } 
/*      */ 
/*      */       
/* 1078 */       if (pendingDescription != null && !pendingDescription.isEmpty()) {
/* 1079 */         builder.append("PENDING, info=[").append(pendingDescription).append("]");
/* 1080 */       } else if (isDone()) {
/* 1081 */         addDoneString(builder);
/*      */       } else {
/* 1083 */         builder.append("PENDING");
/*      */       } 
/*      */     } 
/* 1086 */     return builder.append("]").toString();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected String pendingToString() {
/* 1096 */     Object localValue = this.value;
/* 1097 */     if (localValue instanceof SetFuture)
/* 1098 */       return "setFuture=[" + userObjectToString(((SetFuture)localValue).future) + "]"; 
/* 1099 */     if (this instanceof ScheduledFuture) {
/* 1100 */       return "remaining delay=[" + ((ScheduledFuture)this)
/* 1101 */         .getDelay(TimeUnit.MILLISECONDS) + " ms]";
/*      */     }
/*      */     
/* 1104 */     return null;
/*      */   }
/*      */   
/*      */   private void addDoneString(StringBuilder builder) {
/*      */     try {
/* 1109 */       V value = getUninterruptibly(this);
/* 1110 */       builder.append("SUCCESS, result=[").append(userObjectToString(value)).append("]");
/* 1111 */     } catch (ExecutionException e) {
/* 1112 */       builder.append("FAILURE, cause=[").append(e.getCause()).append("]");
/* 1113 */     } catch (CancellationException e) {
/* 1114 */       builder.append("CANCELLED");
/* 1115 */     } catch (RuntimeException e) {
/* 1116 */       builder.append("UNKNOWN, cause=[").append(e.getClass()).append(" thrown from get()]");
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private String userObjectToString(Object o) {
/* 1126 */     if (o == this) {
/* 1127 */       return "this future";
/*      */     }
/* 1129 */     return String.valueOf(o);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static void executeListener(Runnable runnable, Executor executor) {
/*      */     try {
/* 1138 */       executor.execute(runnable);
/* 1139 */     } catch (RuntimeException e) {
/*      */ 
/*      */ 
/*      */       
/* 1143 */       log.log(Level.SEVERE, "RuntimeException while executing runnable " + runnable + " with executor " + executor, e);
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   private static abstract class AtomicHelper
/*      */   {
/*      */     private AtomicHelper() {}
/*      */ 
/*      */     
/*      */     abstract void putThread(AbstractFuture.Waiter param1Waiter, Thread param1Thread);
/*      */ 
/*      */     
/*      */     abstract void putNext(AbstractFuture.Waiter param1Waiter1, AbstractFuture.Waiter param1Waiter2);
/*      */ 
/*      */     
/*      */     abstract boolean casWaiters(AbstractFuture<?> param1AbstractFuture, AbstractFuture.Waiter param1Waiter1, AbstractFuture.Waiter param1Waiter2);
/*      */ 
/*      */     
/*      */     abstract boolean casListeners(AbstractFuture<?> param1AbstractFuture, AbstractFuture.Listener param1Listener1, AbstractFuture.Listener param1Listener2);
/*      */ 
/*      */     
/*      */     abstract boolean casValue(AbstractFuture<?> param1AbstractFuture, Object param1Object1, Object param1Object2);
/*      */   }
/*      */ 
/*      */   
/*      */   private static final class UnsafeAtomicHelper
/*      */     extends AtomicHelper
/*      */   {
/*      */     static final Unsafe UNSAFE;
/*      */     static final long LISTENERS_OFFSET;
/*      */     static final long WAITERS_OFFSET;
/*      */     static final long VALUE_OFFSET;
/*      */     static final long WAITER_THREAD_OFFSET;
/*      */     static final long WAITER_NEXT_OFFSET;
/*      */     
/*      */     private UnsafeAtomicHelper() {}
/*      */     
/*      */     static {
/* 1182 */       Unsafe unsafe = null;
/*      */       try {
/* 1184 */         unsafe = Unsafe.getUnsafe();
/* 1185 */       } catch (SecurityException tryReflectionInstead) {
/*      */         
/*      */         try {
/* 1188 */           unsafe = AccessController.<Unsafe>doPrivileged(new PrivilegedExceptionAction<Unsafe>()
/*      */               {
/*      */                 public Unsafe run() throws Exception
/*      */                 {
/* 1192 */                   Class<Unsafe> k = Unsafe.class;
/* 1193 */                   for (Field f : k.getDeclaredFields()) {
/* 1194 */                     f.setAccessible(true);
/* 1195 */                     Object x = f.get((Object)null);
/* 1196 */                     if (k.isInstance(x)) {
/* 1197 */                       return k.cast(x);
/*      */                     }
/*      */                   } 
/* 1200 */                   throw new NoSuchFieldError("the Unsafe");
/*      */                 }
/*      */               });
/* 1203 */         } catch (PrivilegedActionException e) {
/* 1204 */           throw new RuntimeException("Could not initialize intrinsics", e.getCause());
/*      */         } 
/*      */       } 
/*      */       try {
/* 1208 */         Class<?> abstractFuture = AbstractFuture.class;
/* 1209 */         WAITERS_OFFSET = unsafe.objectFieldOffset(abstractFuture.getDeclaredField("waiters"));
/* 1210 */         LISTENERS_OFFSET = unsafe.objectFieldOffset(abstractFuture.getDeclaredField("listeners"));
/* 1211 */         VALUE_OFFSET = unsafe.objectFieldOffset(abstractFuture.getDeclaredField("value"));
/* 1212 */         WAITER_THREAD_OFFSET = unsafe.objectFieldOffset(AbstractFuture.Waiter.class.getDeclaredField("thread"));
/* 1213 */         WAITER_NEXT_OFFSET = unsafe.objectFieldOffset(AbstractFuture.Waiter.class.getDeclaredField("next"));
/* 1214 */         UNSAFE = unsafe;
/* 1215 */       } catch (Exception e) {
/* 1216 */         Throwables.throwIfUnchecked(e);
/* 1217 */         throw new RuntimeException(e);
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     void putThread(AbstractFuture.Waiter waiter, Thread newValue) {
/* 1223 */       UNSAFE.putObject(waiter, WAITER_THREAD_OFFSET, newValue);
/*      */     }
/*      */ 
/*      */     
/*      */     void putNext(AbstractFuture.Waiter waiter, AbstractFuture.Waiter newValue) {
/* 1228 */       UNSAFE.putObject(waiter, WAITER_NEXT_OFFSET, newValue);
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     boolean casWaiters(AbstractFuture<?> future, AbstractFuture.Waiter expect, AbstractFuture.Waiter update) {
/* 1234 */       return UNSAFE.compareAndSwapObject(future, WAITERS_OFFSET, expect, update);
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     boolean casListeners(AbstractFuture<?> future, AbstractFuture.Listener expect, AbstractFuture.Listener update) {
/* 1240 */       return UNSAFE.compareAndSwapObject(future, LISTENERS_OFFSET, expect, update);
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     boolean casValue(AbstractFuture<?> future, Object expect, Object update) {
/* 1246 */       return UNSAFE.compareAndSwapObject(future, VALUE_OFFSET, expect, update);
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   private static final class SafeAtomicHelper
/*      */     extends AtomicHelper
/*      */   {
/*      */     final AtomicReferenceFieldUpdater<AbstractFuture.Waiter, Thread> waiterThreadUpdater;
/*      */     
/*      */     final AtomicReferenceFieldUpdater<AbstractFuture.Waiter, AbstractFuture.Waiter> waiterNextUpdater;
/*      */     
/*      */     final AtomicReferenceFieldUpdater<AbstractFuture, AbstractFuture.Waiter> waitersUpdater;
/*      */     
/*      */     final AtomicReferenceFieldUpdater<AbstractFuture, AbstractFuture.Listener> listenersUpdater;
/*      */     final AtomicReferenceFieldUpdater<AbstractFuture, Object> valueUpdater;
/*      */     
/*      */     SafeAtomicHelper(AtomicReferenceFieldUpdater<AbstractFuture.Waiter, Thread> waiterThreadUpdater, AtomicReferenceFieldUpdater<AbstractFuture.Waiter, AbstractFuture.Waiter> waiterNextUpdater, AtomicReferenceFieldUpdater<AbstractFuture, AbstractFuture.Waiter> waitersUpdater, AtomicReferenceFieldUpdater<AbstractFuture, AbstractFuture.Listener> listenersUpdater, AtomicReferenceFieldUpdater<AbstractFuture, Object> valueUpdater) {
/* 1264 */       this.waiterThreadUpdater = waiterThreadUpdater;
/* 1265 */       this.waiterNextUpdater = waiterNextUpdater;
/* 1266 */       this.waitersUpdater = waitersUpdater;
/* 1267 */       this.listenersUpdater = listenersUpdater;
/* 1268 */       this.valueUpdater = valueUpdater;
/*      */     }
/*      */ 
/*      */     
/*      */     void putThread(AbstractFuture.Waiter waiter, Thread newValue) {
/* 1273 */       this.waiterThreadUpdater.lazySet(waiter, newValue);
/*      */     }
/*      */ 
/*      */     
/*      */     void putNext(AbstractFuture.Waiter waiter, AbstractFuture.Waiter newValue) {
/* 1278 */       this.waiterNextUpdater.lazySet(waiter, newValue);
/*      */     }
/*      */ 
/*      */     
/*      */     boolean casWaiters(AbstractFuture<?> future, AbstractFuture.Waiter expect, AbstractFuture.Waiter update) {
/* 1283 */       return this.waitersUpdater.compareAndSet(future, expect, update);
/*      */     }
/*      */ 
/*      */     
/*      */     boolean casListeners(AbstractFuture<?> future, AbstractFuture.Listener expect, AbstractFuture.Listener update) {
/* 1288 */       return this.listenersUpdater.compareAndSet(future, expect, update);
/*      */     }
/*      */ 
/*      */     
/*      */     boolean casValue(AbstractFuture<?> future, Object expect, Object update) {
/* 1293 */       return this.valueUpdater.compareAndSet(future, expect, update);
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private static final class SynchronizedHelper
/*      */     extends AtomicHelper
/*      */   {
/*      */     private SynchronizedHelper() {}
/*      */ 
/*      */     
/*      */     void putThread(AbstractFuture.Waiter waiter, Thread newValue) {
/* 1306 */       waiter.thread = newValue;
/*      */     }
/*      */ 
/*      */     
/*      */     void putNext(AbstractFuture.Waiter waiter, AbstractFuture.Waiter newValue) {
/* 1311 */       waiter.next = newValue;
/*      */     }
/*      */ 
/*      */     
/*      */     boolean casWaiters(AbstractFuture<?> future, AbstractFuture.Waiter expect, AbstractFuture.Waiter update) {
/* 1316 */       synchronized (future) {
/* 1317 */         if (future.waiters == expect) {
/* 1318 */           future.waiters = update;
/* 1319 */           return true;
/*      */         } 
/* 1321 */         return false;
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     boolean casListeners(AbstractFuture<?> future, AbstractFuture.Listener expect, AbstractFuture.Listener update) {
/* 1327 */       synchronized (future) {
/* 1328 */         if (future.listeners == expect) {
/* 1329 */           future.listeners = update;
/* 1330 */           return true;
/*      */         } 
/* 1332 */         return false;
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     boolean casValue(AbstractFuture<?> future, Object expect, Object update) {
/* 1338 */       synchronized (future) {
/* 1339 */         if (future.value == expect) {
/* 1340 */           future.value = update;
/* 1341 */           return true;
/*      */         } 
/* 1343 */         return false;
/*      */       } 
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   private static CancellationException cancellationExceptionWithCause(String message, Throwable cause) {
/* 1350 */     CancellationException exception = new CancellationException(message);
/* 1351 */     exception.initCause(cause);
/* 1352 */     return exception;
/*      */   }
/*      */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\google\commo\\util\concurrent\AbstractFuture.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */