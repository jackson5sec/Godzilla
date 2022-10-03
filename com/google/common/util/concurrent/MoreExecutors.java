/*     */ package com.google.common.util.concurrent;
/*     */ 
/*     */ import com.google.common.annotations.Beta;
/*     */ import com.google.common.annotations.GwtCompatible;
/*     */ import com.google.common.annotations.GwtIncompatible;
/*     */ import com.google.common.annotations.VisibleForTesting;
/*     */ import com.google.common.base.Preconditions;
/*     */ import com.google.common.base.Supplier;
/*     */ import com.google.common.base.Throwables;
/*     */ import com.google.common.collect.Lists;
/*     */ import com.google.common.collect.Queues;
/*     */ import com.google.errorprone.annotations.CanIgnoreReturnValue;
/*     */ import com.google.errorprone.annotations.concurrent.GuardedBy;
/*     */ import java.lang.reflect.InvocationTargetException;
/*     */ import java.util.Collection;
/*     */ import java.util.Collections;
/*     */ import java.util.List;
/*     */ import java.util.concurrent.BlockingQueue;
/*     */ import java.util.concurrent.Callable;
/*     */ import java.util.concurrent.Delayed;
/*     */ import java.util.concurrent.ExecutionException;
/*     */ import java.util.concurrent.Executor;
/*     */ import java.util.concurrent.ExecutorService;
/*     */ import java.util.concurrent.Executors;
/*     */ import java.util.concurrent.Future;
/*     */ import java.util.concurrent.RejectedExecutionException;
/*     */ import java.util.concurrent.ScheduledExecutorService;
/*     */ import java.util.concurrent.ScheduledFuture;
/*     */ import java.util.concurrent.ScheduledThreadPoolExecutor;
/*     */ import java.util.concurrent.ThreadFactory;
/*     */ import java.util.concurrent.ThreadPoolExecutor;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @GwtCompatible(emulated = true)
/*     */ public final class MoreExecutors
/*     */ {
/*     */   @Beta
/*     */   @GwtIncompatible
/*     */   public static ExecutorService getExitingExecutorService(ThreadPoolExecutor executor, long terminationTimeout, TimeUnit timeUnit) {
/*  85 */     return (new Application()).getExitingExecutorService(executor, terminationTimeout, timeUnit);
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Beta
/*     */   @GwtIncompatible
/*     */   public static ExecutorService getExitingExecutorService(ThreadPoolExecutor executor) {
/* 104 */     return (new Application()).getExitingExecutorService(executor);
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Beta
/*     */   @GwtIncompatible
/*     */   public static ScheduledExecutorService getExitingScheduledExecutorService(ScheduledThreadPoolExecutor executor, long terminationTimeout, TimeUnit timeUnit) {
/* 125 */     return (new Application())
/* 126 */       .getExitingScheduledExecutorService(executor, terminationTimeout, timeUnit);
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Beta
/*     */   @GwtIncompatible
/*     */   public static ScheduledExecutorService getExitingScheduledExecutorService(ScheduledThreadPoolExecutor executor) {
/* 146 */     return (new Application()).getExitingScheduledExecutorService(executor);
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Beta
/*     */   @GwtIncompatible
/*     */   public static void addDelayedShutdownHook(ExecutorService service, long terminationTimeout, TimeUnit timeUnit) {
/* 165 */     (new Application()).addDelayedShutdownHook(service, terminationTimeout, timeUnit);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @GwtIncompatible
/*     */   @VisibleForTesting
/*     */   static class Application
/*     */   {
/*     */     final ExecutorService getExitingExecutorService(ThreadPoolExecutor executor, long terminationTimeout, TimeUnit timeUnit) {
/* 175 */       MoreExecutors.useDaemonThreadFactory(executor);
/* 176 */       ExecutorService service = Executors.unconfigurableExecutorService(executor);
/* 177 */       addDelayedShutdownHook(executor, terminationTimeout, timeUnit);
/* 178 */       return service;
/*     */     }
/*     */     
/*     */     final ExecutorService getExitingExecutorService(ThreadPoolExecutor executor) {
/* 182 */       return getExitingExecutorService(executor, 120L, TimeUnit.SECONDS);
/*     */     }
/*     */ 
/*     */     
/*     */     final ScheduledExecutorService getExitingScheduledExecutorService(ScheduledThreadPoolExecutor executor, long terminationTimeout, TimeUnit timeUnit) {
/* 187 */       MoreExecutors.useDaemonThreadFactory(executor);
/* 188 */       ScheduledExecutorService service = Executors.unconfigurableScheduledExecutorService(executor);
/* 189 */       addDelayedShutdownHook(executor, terminationTimeout, timeUnit);
/* 190 */       return service;
/*     */     }
/*     */ 
/*     */     
/*     */     final ScheduledExecutorService getExitingScheduledExecutorService(ScheduledThreadPoolExecutor executor) {
/* 195 */       return getExitingScheduledExecutorService(executor, 120L, TimeUnit.SECONDS);
/*     */     }
/*     */ 
/*     */     
/*     */     final void addDelayedShutdownHook(final ExecutorService service, final long terminationTimeout, final TimeUnit timeUnit) {
/* 200 */       Preconditions.checkNotNull(service);
/* 201 */       Preconditions.checkNotNull(timeUnit);
/* 202 */       addShutdownHook(
/* 203 */           MoreExecutors.newThread("DelayedShutdownHook-for-" + service, new Runnable()
/*     */             {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */               
/*     */               public void run()
/*     */               {
/*     */                 try {
/* 214 */                   service.shutdown();
/* 215 */                   service.awaitTermination(terminationTimeout, timeUnit);
/* 216 */                 } catch (InterruptedException interruptedException) {}
/*     */               }
/*     */             }));
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     @VisibleForTesting
/*     */     void addShutdownHook(Thread hook) {
/* 225 */       Runtime.getRuntime().addShutdownHook(hook);
/*     */     }
/*     */   }
/*     */   
/*     */   @GwtIncompatible
/*     */   private static void useDaemonThreadFactory(ThreadPoolExecutor executor) {
/* 231 */     executor.setThreadFactory((new ThreadFactoryBuilder())
/*     */         
/* 233 */         .setDaemon(true)
/* 234 */         .setThreadFactory(executor.getThreadFactory())
/* 235 */         .build());
/*     */   }
/*     */   
/*     */   @GwtIncompatible
/*     */   private static final class DirectExecutorService
/*     */     extends AbstractListeningExecutorService
/*     */   {
/* 242 */     private final Object lock = new Object();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     @GuardedBy("lock")
/* 251 */     private int runningTasks = 0;
/*     */ 
/*     */     
/*     */     @GuardedBy("lock")
/*     */     private boolean shutdown = false;
/*     */ 
/*     */     
/*     */     public void execute(Runnable command) {
/* 259 */       startTask();
/*     */       try {
/* 261 */         command.run();
/*     */       } finally {
/* 263 */         endTask();
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean isShutdown() {
/* 269 */       synchronized (this.lock) {
/* 270 */         return this.shutdown;
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public void shutdown() {
/* 276 */       synchronized (this.lock) {
/* 277 */         this.shutdown = true;
/* 278 */         if (this.runningTasks == 0) {
/* 279 */           this.lock.notifyAll();
/*     */         }
/*     */       } 
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public List<Runnable> shutdownNow() {
/* 287 */       shutdown();
/* 288 */       return Collections.emptyList();
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean isTerminated() {
/* 293 */       synchronized (this.lock) {
/* 294 */         return (this.shutdown && this.runningTasks == 0);
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean awaitTermination(long timeout, TimeUnit unit) throws InterruptedException {
/* 300 */       long nanos = unit.toNanos(timeout);
/* 301 */       synchronized (this.lock) {
/*     */         while (true) {
/* 303 */           if (this.shutdown && this.runningTasks == 0)
/* 304 */             return true; 
/* 305 */           if (nanos <= 0L) {
/* 306 */             return false;
/*     */           }
/* 308 */           long now = System.nanoTime();
/* 309 */           TimeUnit.NANOSECONDS.timedWait(this.lock, nanos);
/* 310 */           nanos -= System.nanoTime() - now;
/*     */         } 
/*     */       } 
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     private void startTask() {
/* 322 */       synchronized (this.lock) {
/* 323 */         if (this.shutdown) {
/* 324 */           throw new RejectedExecutionException("Executor already shutdown");
/*     */         }
/* 326 */         this.runningTasks++;
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     private void endTask() {
/* 332 */       synchronized (this.lock) {
/* 333 */         int numRunning = --this.runningTasks;
/* 334 */         if (numRunning == 0) {
/* 335 */           this.lock.notifyAll();
/*     */         }
/*     */       } 
/*     */     }
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
/*     */     private DirectExecutorService() {}
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
/*     */ 
/*     */ 
/*     */   
/*     */   @GwtIncompatible
/*     */   public static ListeningExecutorService newDirectExecutorService() {
/* 368 */     return new DirectExecutorService();
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
/*     */   public static Executor directExecutor() {
/* 392 */     return DirectExecutor.INSTANCE;
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
/*     */   @Beta
/*     */   @GwtIncompatible
/*     */   public static Executor newSequentialExecutor(Executor delegate) {
/* 439 */     return new SequentialExecutor(delegate);
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @GwtIncompatible
/*     */   public static ListeningExecutorService listeningDecorator(ExecutorService delegate) {
/* 459 */     return (delegate instanceof ListeningExecutorService) ? (ListeningExecutorService)delegate : ((delegate instanceof ScheduledExecutorService) ? new ScheduledListeningDecorator((ScheduledExecutorService)delegate) : new ListeningDecorator(delegate));
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
/*     */   @GwtIncompatible
/*     */   public static ListeningScheduledExecutorService listeningDecorator(ScheduledExecutorService delegate) {
/* 485 */     return (delegate instanceof ListeningScheduledExecutorService) ? (ListeningScheduledExecutorService)delegate : new ScheduledListeningDecorator(delegate);
/*     */   }
/*     */   
/*     */   @GwtIncompatible
/*     */   private static class ListeningDecorator
/*     */     extends AbstractListeningExecutorService
/*     */   {
/*     */     private final ExecutorService delegate;
/*     */     
/*     */     ListeningDecorator(ExecutorService delegate) {
/* 495 */       this.delegate = (ExecutorService)Preconditions.checkNotNull(delegate);
/*     */     }
/*     */ 
/*     */     
/*     */     public final boolean awaitTermination(long timeout, TimeUnit unit) throws InterruptedException {
/* 500 */       return this.delegate.awaitTermination(timeout, unit);
/*     */     }
/*     */ 
/*     */     
/*     */     public final boolean isShutdown() {
/* 505 */       return this.delegate.isShutdown();
/*     */     }
/*     */ 
/*     */     
/*     */     public final boolean isTerminated() {
/* 510 */       return this.delegate.isTerminated();
/*     */     }
/*     */ 
/*     */     
/*     */     public final void shutdown() {
/* 515 */       this.delegate.shutdown();
/*     */     }
/*     */ 
/*     */     
/*     */     public final List<Runnable> shutdownNow() {
/* 520 */       return this.delegate.shutdownNow();
/*     */     }
/*     */ 
/*     */     
/*     */     public final void execute(Runnable command) {
/* 525 */       this.delegate.execute(command);
/*     */     }
/*     */   }
/*     */   
/*     */   @GwtIncompatible
/*     */   private static final class ScheduledListeningDecorator
/*     */     extends ListeningDecorator
/*     */     implements ListeningScheduledExecutorService {
/*     */     final ScheduledExecutorService delegate;
/*     */     
/*     */     ScheduledListeningDecorator(ScheduledExecutorService delegate) {
/* 536 */       super(delegate);
/* 537 */       this.delegate = (ScheduledExecutorService)Preconditions.checkNotNull(delegate);
/*     */     }
/*     */ 
/*     */     
/*     */     public ListenableScheduledFuture<?> schedule(Runnable command, long delay, TimeUnit unit) {
/* 542 */       TrustedListenableFutureTask<Void> task = TrustedListenableFutureTask.create(command, null);
/* 543 */       ScheduledFuture<?> scheduled = this.delegate.schedule(task, delay, unit);
/* 544 */       return new ListenableScheduledTask(task, scheduled);
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public <V> ListenableScheduledFuture<V> schedule(Callable<V> callable, long delay, TimeUnit unit) {
/* 550 */       TrustedListenableFutureTask<V> task = TrustedListenableFutureTask.create(callable);
/* 551 */       ScheduledFuture<?> scheduled = this.delegate.schedule(task, delay, unit);
/* 552 */       return new ListenableScheduledTask<>(task, scheduled);
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public ListenableScheduledFuture<?> scheduleAtFixedRate(Runnable command, long initialDelay, long period, TimeUnit unit) {
/* 558 */       NeverSuccessfulListenableFutureTask task = new NeverSuccessfulListenableFutureTask(command);
/* 559 */       ScheduledFuture<?> scheduled = this.delegate.scheduleAtFixedRate(task, initialDelay, period, unit);
/* 560 */       return new ListenableScheduledTask(task, scheduled);
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public ListenableScheduledFuture<?> scheduleWithFixedDelay(Runnable command, long initialDelay, long delay, TimeUnit unit) {
/* 566 */       NeverSuccessfulListenableFutureTask task = new NeverSuccessfulListenableFutureTask(command);
/*     */       
/* 568 */       ScheduledFuture<?> scheduled = this.delegate.scheduleWithFixedDelay(task, initialDelay, delay, unit);
/* 569 */       return new ListenableScheduledTask(task, scheduled);
/*     */     }
/*     */     
/*     */     private static final class ListenableScheduledTask<V>
/*     */       extends ForwardingListenableFuture.SimpleForwardingListenableFuture<V>
/*     */       implements ListenableScheduledFuture<V>
/*     */     {
/*     */       private final ScheduledFuture<?> scheduledDelegate;
/*     */       
/*     */       public ListenableScheduledTask(ListenableFuture<V> listenableDelegate, ScheduledFuture<?> scheduledDelegate) {
/* 579 */         super(listenableDelegate);
/* 580 */         this.scheduledDelegate = scheduledDelegate;
/*     */       }
/*     */ 
/*     */       
/*     */       public boolean cancel(boolean mayInterruptIfRunning) {
/* 585 */         boolean cancelled = super.cancel(mayInterruptIfRunning);
/* 586 */         if (cancelled)
/*     */         {
/* 588 */           this.scheduledDelegate.cancel(mayInterruptIfRunning);
/*     */         }
/*     */ 
/*     */         
/* 592 */         return cancelled;
/*     */       }
/*     */ 
/*     */       
/*     */       public long getDelay(TimeUnit unit) {
/* 597 */         return this.scheduledDelegate.getDelay(unit);
/*     */       }
/*     */ 
/*     */       
/*     */       public int compareTo(Delayed other) {
/* 602 */         return this.scheduledDelegate.compareTo(other);
/*     */       }
/*     */     }
/*     */     
/*     */     @GwtIncompatible
/*     */     private static final class NeverSuccessfulListenableFutureTask
/*     */       extends AbstractFuture.TrustedFuture<Void> implements Runnable {
/*     */       private final Runnable delegate;
/*     */       
/*     */       public NeverSuccessfulListenableFutureTask(Runnable delegate) {
/* 612 */         this.delegate = (Runnable)Preconditions.checkNotNull(delegate);
/*     */       }
/*     */ 
/*     */       
/*     */       public void run() {
/*     */         try {
/* 618 */           this.delegate.run();
/* 619 */         } catch (Throwable t) {
/* 620 */           setException(t);
/* 621 */           throw Throwables.propagate(t);
/*     */         } 
/*     */       }
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
/*     */   @GwtIncompatible
/*     */   static <T> T invokeAnyImpl(ListeningExecutorService executorService, Collection<? extends Callable<T>> tasks, boolean timed, long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
/* 650 */     Preconditions.checkNotNull(executorService);
/* 651 */     Preconditions.checkNotNull(unit);
/* 652 */     int ntasks = tasks.size();
/* 653 */     Preconditions.checkArgument((ntasks > 0));
/* 654 */     List<Future<T>> futures = Lists.newArrayListWithCapacity(ntasks);
/* 655 */     BlockingQueue<Future<T>> futureQueue = Queues.newLinkedBlockingQueue();
/* 656 */     long timeoutNanos = unit.toNanos(timeout);
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
/*     */   @GwtIncompatible
/*     */   private static <T> ListenableFuture<T> submitAndAddQueueListener(ListeningExecutorService executorService, Callable<T> task, final BlockingQueue<Future<T>> queue) {
/* 727 */     final ListenableFuture<T> future = executorService.submit(task);
/* 728 */     future.addListener(new Runnable()
/*     */         {
/*     */           public void run()
/*     */           {
/* 732 */             queue.add(future);
/*     */           }
/*     */         }, 
/* 735 */         directExecutor());
/* 736 */     return future;
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
/*     */   @Beta
/*     */   @GwtIncompatible
/*     */   public static ThreadFactory platformThreadFactory() {
/* 750 */     if (!isAppEngine()) {
/* 751 */       return Executors.defaultThreadFactory();
/*     */     }
/*     */     try {
/* 754 */       return 
/* 755 */         (ThreadFactory)Class.forName("com.google.appengine.api.ThreadManager")
/* 756 */         .getMethod("currentRequestThreadFactory", new Class[0])
/* 757 */         .invoke(null, new Object[0]);
/* 758 */     } catch (IllegalAccessException|ClassNotFoundException|NoSuchMethodException e) {
/* 759 */       throw new RuntimeException("Couldn't invoke ThreadManager.currentRequestThreadFactory", e);
/* 760 */     } catch (InvocationTargetException e) {
/* 761 */       throw Throwables.propagate(e.getCause());
/*     */     } 
/*     */   }
/*     */   
/*     */   @GwtIncompatible
/*     */   private static boolean isAppEngine() {
/* 767 */     if (System.getProperty("com.google.appengine.runtime.environment") == null) {
/* 768 */       return false;
/*     */     }
/*     */     
/*     */     try {
/* 772 */       return 
/*     */         
/* 774 */         (Class.forName("com.google.apphosting.api.ApiProxy").getMethod("getCurrentEnvironment", new Class[0]).invoke(null, new Object[0]) != null);
/*     */     }
/* 776 */     catch (ClassNotFoundException e) {
/*     */       
/* 778 */       return false;
/* 779 */     } catch (InvocationTargetException e) {
/*     */       
/* 781 */       return false;
/* 782 */     } catch (IllegalAccessException e) {
/*     */       
/* 784 */       return false;
/* 785 */     } catch (NoSuchMethodException e) {
/*     */       
/* 787 */       return false;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @GwtIncompatible
/*     */   static Thread newThread(String name, Runnable runnable) {
/* 797 */     Preconditions.checkNotNull(name);
/* 798 */     Preconditions.checkNotNull(runnable);
/* 799 */     Thread result = platformThreadFactory().newThread(runnable);
/*     */     try {
/* 801 */       result.setName(name);
/* 802 */     } catch (SecurityException securityException) {}
/*     */ 
/*     */     
/* 805 */     return result;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @GwtIncompatible
/*     */   static Executor renamingDecorator(final Executor executor, final Supplier<String> nameSupplier) {
/* 825 */     Preconditions.checkNotNull(executor);
/* 826 */     Preconditions.checkNotNull(nameSupplier);
/* 827 */     if (isAppEngine())
/*     */     {
/* 829 */       return executor;
/*     */     }
/* 831 */     return new Executor()
/*     */       {
/*     */         public void execute(Runnable command) {
/* 834 */           executor.execute(Callables.threadRenaming(command, nameSupplier));
/*     */         }
/*     */       };
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @GwtIncompatible
/*     */   static ExecutorService renamingDecorator(ExecutorService service, final Supplier<String> nameSupplier) {
/* 854 */     Preconditions.checkNotNull(service);
/* 855 */     Preconditions.checkNotNull(nameSupplier);
/* 856 */     if (isAppEngine())
/*     */     {
/* 858 */       return service;
/*     */     }
/* 860 */     return new WrappingExecutorService(service)
/*     */       {
/*     */         protected <T> Callable<T> wrapTask(Callable<T> callable) {
/* 863 */           return Callables.threadRenaming(callable, nameSupplier);
/*     */         }
/*     */ 
/*     */         
/*     */         protected Runnable wrapTask(Runnable command) {
/* 868 */           return Callables.threadRenaming(command, nameSupplier);
/*     */         }
/*     */       };
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @GwtIncompatible
/*     */   static ScheduledExecutorService renamingDecorator(ScheduledExecutorService service, final Supplier<String> nameSupplier) {
/* 888 */     Preconditions.checkNotNull(service);
/* 889 */     Preconditions.checkNotNull(nameSupplier);
/* 890 */     if (isAppEngine())
/*     */     {
/* 892 */       return service;
/*     */     }
/* 894 */     return new WrappingScheduledExecutorService(service)
/*     */       {
/*     */         protected <T> Callable<T> wrapTask(Callable<T> callable) {
/* 897 */           return Callables.threadRenaming(callable, nameSupplier);
/*     */         }
/*     */ 
/*     */         
/*     */         protected Runnable wrapTask(Runnable command) {
/* 902 */           return Callables.threadRenaming(command, nameSupplier);
/*     */         }
/*     */       };
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
/*     */   @Beta
/*     */   @CanIgnoreReturnValue
/*     */   @GwtIncompatible
/*     */   public static boolean shutdownAndAwaitTermination(ExecutorService service, long timeout, TimeUnit unit) {
/* 937 */     long halfTimeoutNanos = unit.toNanos(timeout) / 2L;
/*     */     
/* 939 */     service.shutdown();
/*     */     
/*     */     try {
/* 942 */       if (!service.awaitTermination(halfTimeoutNanos, TimeUnit.NANOSECONDS)) {
/*     */         
/* 944 */         service.shutdownNow();
/*     */         
/* 946 */         service.awaitTermination(halfTimeoutNanos, TimeUnit.NANOSECONDS);
/*     */       } 
/* 948 */     } catch (InterruptedException ie) {
/*     */       
/* 950 */       Thread.currentThread().interrupt();
/*     */       
/* 952 */       service.shutdownNow();
/*     */     } 
/* 954 */     return service.isTerminated();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static Executor rejectionPropagatingExecutor(final Executor delegate, final AbstractFuture<?> future) {
/* 965 */     Preconditions.checkNotNull(delegate);
/* 966 */     Preconditions.checkNotNull(future);
/* 967 */     if (delegate == directExecutor())
/*     */     {
/* 969 */       return delegate;
/*     */     }
/* 971 */     return new Executor()
/*     */       {
/*     */         boolean thrownFromDelegate = true;
/*     */         
/*     */         public void execute(final Runnable command) {
/*     */           try {
/* 977 */             delegate.execute(new Runnable()
/*     */                 {
/*     */                   public void run()
/*     */                   {
/* 981 */                     MoreExecutors.null.this.thrownFromDelegate = false;
/* 982 */                     command.run();
/*     */                   }
/*     */                 });
/* 985 */           } catch (RejectedExecutionException e) {
/* 986 */             if (this.thrownFromDelegate)
/*     */             {
/* 988 */               future.setException(e);
/*     */             }
/*     */           } 
/*     */         }
/*     */       };
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\google\commo\\util\concurrent\MoreExecutors.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */