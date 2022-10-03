/*     */ package com.google.common.util.concurrent;
/*     */ 
/*     */ import com.google.common.annotations.Beta;
/*     */ import com.google.common.annotations.GwtIncompatible;
/*     */ import com.google.common.base.Preconditions;
/*     */ import com.google.errorprone.annotations.CanIgnoreReturnValue;
/*     */ import com.google.errorprone.annotations.ForOverride;
/*     */ import com.google.errorprone.annotations.concurrent.GuardedBy;
/*     */ import java.util.concurrent.Executor;
/*     */ import java.util.concurrent.TimeUnit;
/*     */ import java.util.concurrent.TimeoutException;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @Beta
/*     */ @GwtIncompatible
/*     */ public abstract class AbstractService
/*     */   implements Service
/*     */ {
/*  53 */   private static final ListenerCallQueue.Event<Service.Listener> STARTING_EVENT = new ListenerCallQueue.Event<Service.Listener>()
/*     */     {
/*     */       public void call(Service.Listener listener)
/*     */       {
/*  57 */         listener.starting();
/*     */       }
/*     */ 
/*     */       
/*     */       public String toString() {
/*  62 */         return "starting()";
/*     */       }
/*     */     };
/*  65 */   private static final ListenerCallQueue.Event<Service.Listener> RUNNING_EVENT = new ListenerCallQueue.Event<Service.Listener>()
/*     */     {
/*     */       public void call(Service.Listener listener)
/*     */       {
/*  69 */         listener.running();
/*     */       }
/*     */ 
/*     */       
/*     */       public String toString() {
/*  74 */         return "running()";
/*     */       }
/*     */     };
/*     */   
/*  78 */   private static final ListenerCallQueue.Event<Service.Listener> STOPPING_FROM_STARTING_EVENT = stoppingEvent(Service.State.STARTING);
/*     */   
/*  80 */   private static final ListenerCallQueue.Event<Service.Listener> STOPPING_FROM_RUNNING_EVENT = stoppingEvent(Service.State.RUNNING);
/*     */ 
/*     */   
/*  83 */   private static final ListenerCallQueue.Event<Service.Listener> TERMINATED_FROM_NEW_EVENT = terminatedEvent(Service.State.NEW);
/*     */   
/*  85 */   private static final ListenerCallQueue.Event<Service.Listener> TERMINATED_FROM_STARTING_EVENT = terminatedEvent(Service.State.STARTING);
/*     */   
/*  87 */   private static final ListenerCallQueue.Event<Service.Listener> TERMINATED_FROM_RUNNING_EVENT = terminatedEvent(Service.State.RUNNING);
/*     */   
/*  89 */   private static final ListenerCallQueue.Event<Service.Listener> TERMINATED_FROM_STOPPING_EVENT = terminatedEvent(Service.State.STOPPING);
/*     */   
/*     */   private static ListenerCallQueue.Event<Service.Listener> terminatedEvent(final Service.State from) {
/*  92 */     return new ListenerCallQueue.Event<Service.Listener>()
/*     */       {
/*     */         public void call(Service.Listener listener) {
/*  95 */           listener.terminated(from);
/*     */         }
/*     */ 
/*     */         
/*     */         public String toString() {
/* 100 */           return "terminated({from = " + from + "})";
/*     */         }
/*     */       };
/*     */   }
/*     */   
/*     */   private static ListenerCallQueue.Event<Service.Listener> stoppingEvent(final Service.State from) {
/* 106 */     return new ListenerCallQueue.Event<Service.Listener>()
/*     */       {
/*     */         public void call(Service.Listener listener) {
/* 109 */           listener.stopping(from);
/*     */         }
/*     */ 
/*     */         
/*     */         public String toString() {
/* 114 */           return "stopping({from = " + from + "})";
/*     */         }
/*     */       };
/*     */   }
/*     */   
/* 119 */   private final Monitor monitor = new Monitor();
/*     */   
/* 121 */   private final Monitor.Guard isStartable = new IsStartableGuard();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private final class IsStartableGuard
/*     */     extends Monitor.Guard
/*     */   {
/*     */     public boolean isSatisfied() {
/* 131 */       return (AbstractService.this.state() == Service.State.NEW);
/*     */     }
/*     */   }
/*     */   
/* 135 */   private final Monitor.Guard isStoppable = new IsStoppableGuard();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private final class IsStoppableGuard
/*     */     extends Monitor.Guard
/*     */   {
/*     */     public boolean isSatisfied() {
/* 145 */       return (AbstractService.this.state().compareTo(Service.State.RUNNING) <= 0);
/*     */     }
/*     */   }
/*     */   
/* 149 */   private final Monitor.Guard hasReachedRunning = new HasReachedRunningGuard();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private final class HasReachedRunningGuard
/*     */     extends Monitor.Guard
/*     */   {
/*     */     public boolean isSatisfied() {
/* 159 */       return (AbstractService.this.state().compareTo(Service.State.RUNNING) >= 0);
/*     */     }
/*     */   }
/*     */   
/* 163 */   private final Monitor.Guard isStopped = new IsStoppedGuard();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private final class IsStoppedGuard
/*     */     extends Monitor.Guard
/*     */   {
/*     */     public boolean isSatisfied() {
/* 173 */       return AbstractService.this.state().isTerminal();
/*     */     }
/*     */   }
/*     */ 
/*     */   
/* 178 */   private final ListenerCallQueue<Service.Listener> listeners = new ListenerCallQueue<>();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 189 */   private volatile StateSnapshot snapshot = new StateSnapshot(Service.State.NEW);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @ForOverride
/*     */   protected void doCancelStart() {}
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @CanIgnoreReturnValue
/*     */   public final Service startAsync() {
/* 244 */     if (this.monitor.enterIf(this.isStartable)) {
/*     */       try {
/* 246 */         this.snapshot = new StateSnapshot(Service.State.STARTING);
/* 247 */         enqueueStartingEvent();
/* 248 */         doStart();
/* 249 */       } catch (Throwable startupFailure) {
/* 250 */         notifyFailed(startupFailure);
/*     */       } finally {
/* 252 */         this.monitor.leave();
/* 253 */         dispatchListenerEvents();
/*     */       } 
/*     */     } else {
/* 256 */       throw new IllegalStateException("Service " + this + " has already been started");
/*     */     } 
/* 258 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   @CanIgnoreReturnValue
/*     */   public final Service stopAsync() {
/* 264 */     if (this.monitor.enterIf(this.isStoppable)) {
/*     */       try {
/* 266 */         Service.State previous = state();
/* 267 */         switch (previous) {
/*     */           case NEW:
/* 269 */             this.snapshot = new StateSnapshot(Service.State.TERMINATED);
/* 270 */             enqueueTerminatedEvent(Service.State.NEW);
/*     */             break;
/*     */           case STARTING:
/* 273 */             this.snapshot = new StateSnapshot(Service.State.STARTING, true, null);
/* 274 */             enqueueStoppingEvent(Service.State.STARTING);
/* 275 */             doCancelStart();
/*     */             break;
/*     */           case RUNNING:
/* 278 */             this.snapshot = new StateSnapshot(Service.State.STOPPING);
/* 279 */             enqueueStoppingEvent(Service.State.RUNNING);
/* 280 */             doStop();
/*     */             break;
/*     */           
/*     */           case STOPPING:
/*     */           case TERMINATED:
/*     */           case FAILED:
/* 286 */             throw new AssertionError("isStoppable is incorrectly implemented, saw: " + previous);
/*     */         } 
/* 288 */       } catch (Throwable shutdownFailure) {
/* 289 */         notifyFailed(shutdownFailure);
/*     */       } finally {
/* 291 */         this.monitor.leave();
/* 292 */         dispatchListenerEvents();
/*     */       } 
/*     */     }
/* 295 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public final void awaitRunning() {
/* 300 */     this.monitor.enterWhenUninterruptibly(this.hasReachedRunning);
/*     */     try {
/* 302 */       checkCurrentState(Service.State.RUNNING);
/*     */     } finally {
/* 304 */       this.monitor.leave();
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public final void awaitRunning(long timeout, TimeUnit unit) throws TimeoutException {
/* 310 */     if (this.monitor.enterWhenUninterruptibly(this.hasReachedRunning, timeout, unit)) {
/*     */       try {
/* 312 */         checkCurrentState(Service.State.RUNNING);
/*     */       } finally {
/* 314 */         this.monitor.leave();
/*     */       
/*     */       }
/*     */     
/*     */     }
/*     */     else {
/*     */       
/* 321 */       throw new TimeoutException("Timed out waiting for " + this + " to reach the RUNNING state.");
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public final void awaitTerminated() {
/* 327 */     this.monitor.enterWhenUninterruptibly(this.isStopped);
/*     */     try {
/* 329 */       checkCurrentState(Service.State.TERMINATED);
/*     */     } finally {
/* 331 */       this.monitor.leave();
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public final void awaitTerminated(long timeout, TimeUnit unit) throws TimeoutException {
/* 337 */     if (this.monitor.enterWhenUninterruptibly(this.isStopped, timeout, unit)) {
/*     */       try {
/* 339 */         checkCurrentState(Service.State.TERMINATED);
/*     */       } finally {
/* 341 */         this.monitor.leave();
/*     */       
/*     */       }
/*     */     
/*     */     }
/*     */     else {
/*     */       
/* 348 */       throw new TimeoutException("Timed out waiting for " + this + " to reach a terminal state. Current state: " + 
/*     */ 
/*     */ 
/*     */ 
/*     */           
/* 353 */           state());
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   @GuardedBy("monitor")
/*     */   private void checkCurrentState(Service.State expected) {
/* 360 */     Service.State actual = state();
/* 361 */     if (actual != expected) {
/* 362 */       if (actual == Service.State.FAILED)
/*     */       {
/* 364 */         throw new IllegalStateException("Expected the service " + this + " to be " + expected + ", but the service has FAILED", 
/*     */             
/* 366 */             failureCause());
/*     */       }
/* 368 */       throw new IllegalStateException("Expected the service " + this + " to be " + expected + ", but was " + actual);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected final void notifyStarted() {
/* 380 */     this.monitor.enter();
/*     */ 
/*     */     
/*     */     try {
/* 384 */       if (this.snapshot.state != Service.State.STARTING) {
/* 385 */         IllegalStateException failure = new IllegalStateException("Cannot notifyStarted() when the service is " + this.snapshot.state);
/*     */ 
/*     */         
/* 388 */         notifyFailed(failure);
/* 389 */         throw failure;
/*     */       } 
/*     */       
/* 392 */       if (this.snapshot.shutdownWhenStartupFinishes) {
/* 393 */         this.snapshot = new StateSnapshot(Service.State.STOPPING);
/*     */ 
/*     */         
/* 396 */         doStop();
/*     */       } else {
/* 398 */         this.snapshot = new StateSnapshot(Service.State.RUNNING);
/* 399 */         enqueueRunningEvent();
/*     */       } 
/*     */     } finally {
/* 402 */       this.monitor.leave();
/* 403 */       dispatchListenerEvents();
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected final void notifyStopped() {
/* 416 */     this.monitor.enter();
/*     */     try {
/* 418 */       Service.State previous = state();
/* 419 */       switch (previous) {
/*     */         case NEW:
/*     */         case TERMINATED:
/*     */         case FAILED:
/* 423 */           throw new IllegalStateException("Cannot notifyStopped() when the service is " + previous);
/*     */         case STARTING:
/*     */         case RUNNING:
/*     */         case STOPPING:
/* 427 */           this.snapshot = new StateSnapshot(Service.State.TERMINATED);
/* 428 */           enqueueTerminatedEvent(previous);
/*     */           break;
/*     */       } 
/*     */     } finally {
/* 432 */       this.monitor.leave();
/* 433 */       dispatchListenerEvents();
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected final void notifyFailed(Throwable cause) {
/* 443 */     Preconditions.checkNotNull(cause);
/*     */     
/* 445 */     this.monitor.enter();
/*     */     try {
/* 447 */       Service.State previous = state();
/* 448 */       switch (previous) {
/*     */         case NEW:
/*     */         case TERMINATED:
/* 451 */           throw new IllegalStateException("Failed while in state:" + previous, cause);
/*     */         case STARTING:
/*     */         case RUNNING:
/*     */         case STOPPING:
/* 455 */           this.snapshot = new StateSnapshot(Service.State.FAILED, false, cause);
/* 456 */           enqueueFailedEvent(previous, cause);
/*     */           break;
/*     */       } 
/*     */ 
/*     */ 
/*     */     
/*     */     } finally {
/* 463 */       this.monitor.leave();
/* 464 */       dispatchListenerEvents();
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public final boolean isRunning() {
/* 470 */     return (state() == Service.State.RUNNING);
/*     */   }
/*     */ 
/*     */   
/*     */   public final Service.State state() {
/* 475 */     return this.snapshot.externalState();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public final Throwable failureCause() {
/* 481 */     return this.snapshot.failureCause();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public final void addListener(Service.Listener listener, Executor executor) {
/* 487 */     this.listeners.addListener(listener, executor);
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 492 */     return getClass().getSimpleName() + " [" + state() + "]";
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void dispatchListenerEvents() {
/* 500 */     if (!this.monitor.isOccupiedByCurrentThread()) {
/* 501 */       this.listeners.dispatch();
/*     */     }
/*     */   }
/*     */   
/*     */   private void enqueueStartingEvent() {
/* 506 */     this.listeners.enqueue(STARTING_EVENT);
/*     */   }
/*     */   
/*     */   private void enqueueRunningEvent() {
/* 510 */     this.listeners.enqueue(RUNNING_EVENT);
/*     */   }
/*     */   
/*     */   private void enqueueStoppingEvent(Service.State from) {
/* 514 */     if (from == Service.State.STARTING) {
/* 515 */       this.listeners.enqueue(STOPPING_FROM_STARTING_EVENT);
/* 516 */     } else if (from == Service.State.RUNNING) {
/* 517 */       this.listeners.enqueue(STOPPING_FROM_RUNNING_EVENT);
/*     */     } else {
/* 519 */       throw new AssertionError();
/*     */     } 
/*     */   }
/*     */   
/*     */   private void enqueueTerminatedEvent(Service.State from) {
/* 524 */     switch (from) {
/*     */       case NEW:
/* 526 */         this.listeners.enqueue(TERMINATED_FROM_NEW_EVENT);
/*     */         break;
/*     */       case STARTING:
/* 529 */         this.listeners.enqueue(TERMINATED_FROM_STARTING_EVENT);
/*     */         break;
/*     */       case RUNNING:
/* 532 */         this.listeners.enqueue(TERMINATED_FROM_RUNNING_EVENT);
/*     */         break;
/*     */       case STOPPING:
/* 535 */         this.listeners.enqueue(TERMINATED_FROM_STOPPING_EVENT);
/*     */         break;
/*     */       case TERMINATED:
/*     */       case FAILED:
/* 539 */         throw new AssertionError();
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private void enqueueFailedEvent(final Service.State from, final Throwable cause) {
/* 545 */     this.listeners.enqueue(new ListenerCallQueue.Event<Service.Listener>()
/*     */         {
/*     */           public void call(Service.Listener listener)
/*     */           {
/* 549 */             listener.failed(from, cause);
/*     */           }
/*     */ 
/*     */           
/*     */           public String toString() {
/* 554 */             return "failed({from = " + from + ", cause = " + cause + "})";
/*     */           }
/*     */         });
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @ForOverride
/*     */   protected abstract void doStart();
/*     */ 
/*     */ 
/*     */   
/*     */   @ForOverride
/*     */   protected abstract void doStop();
/*     */ 
/*     */ 
/*     */   
/*     */   private static final class StateSnapshot
/*     */   {
/*     */     final Service.State state;
/*     */     
/*     */     final boolean shutdownWhenStartupFinishes;
/*     */     
/*     */     final Throwable failure;
/*     */ 
/*     */     
/*     */     StateSnapshot(Service.State internalState) {
/* 581 */       this(internalState, false, null);
/*     */     }
/*     */ 
/*     */     
/*     */     StateSnapshot(Service.State internalState, boolean shutdownWhenStartupFinishes, Throwable failure) {
/* 586 */       Preconditions.checkArgument((!shutdownWhenStartupFinishes || internalState == Service.State.STARTING), "shutdownWhenStartupFinishes can only be set if state is STARTING. Got %s instead.", internalState);
/*     */ 
/*     */ 
/*     */       
/* 590 */       Preconditions.checkArgument(((((failure != null) ? 1 : 0) ^ ((internalState == Service.State.FAILED) ? 1 : 0)) == 0), "A failure cause should be set if and only if the state is failed.  Got %s and %s instead.", internalState, failure);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 596 */       this.state = internalState;
/* 597 */       this.shutdownWhenStartupFinishes = shutdownWhenStartupFinishes;
/* 598 */       this.failure = failure;
/*     */     }
/*     */ 
/*     */     
/*     */     Service.State externalState() {
/* 603 */       if (this.shutdownWhenStartupFinishes && this.state == Service.State.STARTING) {
/* 604 */         return Service.State.STOPPING;
/*     */       }
/* 606 */       return this.state;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     Throwable failureCause() {
/* 612 */       Preconditions.checkState((this.state == Service.State.FAILED), "failureCause() is only valid if the service has failed, service is %s", this.state);
/*     */ 
/*     */ 
/*     */       
/* 616 */       return this.failure;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\google\commo\\util\concurrent\AbstractService.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */