/*     */ package com.google.common.util.concurrent;
/*     */ 
/*     */ import com.google.common.annotations.GwtIncompatible;
/*     */ import com.google.common.base.Preconditions;
/*     */ import com.google.common.base.Throwables;
/*     */ import com.google.common.collect.ImmutableList;
/*     */ import com.google.errorprone.annotations.CanIgnoreReturnValue;
/*     */ import java.util.Collection;
/*     */ import java.util.List;
/*     */ import java.util.concurrent.Callable;
/*     */ import java.util.concurrent.ExecutionException;
/*     */ import java.util.concurrent.ExecutorService;
/*     */ import java.util.concurrent.Executors;
/*     */ import java.util.concurrent.Future;
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
/*     */ @CanIgnoreReturnValue
/*     */ @GwtIncompatible
/*     */ abstract class WrappingExecutorService
/*     */   implements ExecutorService
/*     */ {
/*     */   private final ExecutorService delegate;
/*     */   
/*     */   protected WrappingExecutorService(ExecutorService delegate) {
/*  49 */     this.delegate = (ExecutorService)Preconditions.checkNotNull(delegate);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected abstract <T> Callable<T> wrapTask(Callable<T> paramCallable);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected Runnable wrapTask(Runnable command) {
/*  63 */     final Callable<Object> wrapped = wrapTask(Executors.callable(command, null));
/*  64 */     return new Runnable()
/*     */       {
/*     */         public void run() {
/*     */           try {
/*  68 */             wrapped.call();
/*  69 */           } catch (Exception e) {
/*  70 */             Throwables.throwIfUnchecked(e);
/*  71 */             throw new RuntimeException(e);
/*     */           } 
/*     */         }
/*     */       };
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private <T> ImmutableList<Callable<T>> wrapTasks(Collection<? extends Callable<T>> tasks) {
/*  83 */     ImmutableList.Builder<Callable<T>> builder = ImmutableList.builder();
/*  84 */     for (Callable<T> task : tasks) {
/*  85 */       builder.add(wrapTask(task));
/*     */     }
/*  87 */     return builder.build();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public final void execute(Runnable command) {
/*  93 */     this.delegate.execute(wrapTask(command));
/*     */   }
/*     */ 
/*     */   
/*     */   public final <T> Future<T> submit(Callable<T> task) {
/*  98 */     return this.delegate.submit(wrapTask((Callable<T>)Preconditions.checkNotNull(task)));
/*     */   }
/*     */ 
/*     */   
/*     */   public final Future<?> submit(Runnable task) {
/* 103 */     return this.delegate.submit(wrapTask(task));
/*     */   }
/*     */ 
/*     */   
/*     */   public final <T> Future<T> submit(Runnable task, T result) {
/* 108 */     return this.delegate.submit(wrapTask(task), result);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public final <T> List<Future<T>> invokeAll(Collection<? extends Callable<T>> tasks) throws InterruptedException {
/* 114 */     return this.delegate.invokeAll((Collection<? extends Callable<T>>)wrapTasks(tasks));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final <T> List<Future<T>> invokeAll(Collection<? extends Callable<T>> tasks, long timeout, TimeUnit unit) throws InterruptedException {
/* 121 */     return this.delegate.invokeAll((Collection<? extends Callable<T>>)wrapTasks(tasks), timeout, unit);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public final <T> T invokeAny(Collection<? extends Callable<T>> tasks) throws InterruptedException, ExecutionException {
/* 127 */     return this.delegate.invokeAny((Collection<? extends Callable<T>>)wrapTasks(tasks));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public final <T> T invokeAny(Collection<? extends Callable<T>> tasks, long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
/* 133 */     return this.delegate.invokeAny((Collection<? extends Callable<T>>)wrapTasks(tasks), timeout, unit);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final void shutdown() {
/* 140 */     this.delegate.shutdown();
/*     */   }
/*     */ 
/*     */   
/*     */   public final List<Runnable> shutdownNow() {
/* 145 */     return this.delegate.shutdownNow();
/*     */   }
/*     */ 
/*     */   
/*     */   public final boolean isShutdown() {
/* 150 */     return this.delegate.isShutdown();
/*     */   }
/*     */ 
/*     */   
/*     */   public final boolean isTerminated() {
/* 155 */     return this.delegate.isTerminated();
/*     */   }
/*     */ 
/*     */   
/*     */   public final boolean awaitTermination(long timeout, TimeUnit unit) throws InterruptedException {
/* 160 */     return this.delegate.awaitTermination(timeout, unit);
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\google\commo\\util\concurrent\WrappingExecutorService.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */