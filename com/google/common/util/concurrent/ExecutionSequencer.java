/*     */ package com.google.common.util.concurrent;
/*     */ 
/*     */ import com.google.common.annotations.Beta;
/*     */ import com.google.common.base.Preconditions;
/*     */ import java.util.concurrent.Callable;
/*     */ import java.util.concurrent.Executor;
/*     */ import java.util.concurrent.atomic.AtomicReference;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
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
/*     */ public final class ExecutionSequencer
/*     */ {
/*     */   public static ExecutionSequencer create() {
/*  48 */     return new ExecutionSequencer();
/*     */   }
/*     */   
/*     */   enum RunningState {
/*  52 */     NOT_RUN,
/*  53 */     CANCELLED,
/*  54 */     STARTED;
/*     */   }
/*     */ 
/*     */   
/*  58 */   private final AtomicReference<ListenableFuture<Object>> ref = new AtomicReference<>(
/*  59 */       Futures.immediateFuture(null));
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public <T> ListenableFuture<T> submit(final Callable<T> callable, Executor executor) {
/*  69 */     Preconditions.checkNotNull(callable);
/*  70 */     return submitAsync(new AsyncCallable<T>()
/*     */         {
/*     */           public ListenableFuture<T> call() throws Exception
/*     */           {
/*  74 */             return Futures.immediateFuture(callable.call());
/*     */           }
/*     */ 
/*     */           
/*     */           public String toString() {
/*  79 */             return callable.toString();
/*     */           }
/*     */         },  executor);
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
/*     */   public <T> ListenableFuture<T> submitAsync(final AsyncCallable<T> callable, final Executor executor) {
/*  94 */     Preconditions.checkNotNull(callable);
/*  95 */     final AtomicReference<RunningState> runningState = new AtomicReference<>(RunningState.NOT_RUN);
/*  96 */     AsyncCallable<T> task = new AsyncCallable<T>()
/*     */       {
/*     */         public ListenableFuture<T> call() throws Exception
/*     */         {
/* 100 */           if (!runningState.compareAndSet(ExecutionSequencer.RunningState.NOT_RUN, ExecutionSequencer.RunningState.STARTED)) {
/* 101 */             return Futures.immediateCancelledFuture();
/*     */           }
/* 103 */           return callable.call();
/*     */         }
/*     */ 
/*     */         
/*     */         public String toString() {
/* 108 */           return callable.toString();
/*     */         }
/*     */       };
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 122 */     final SettableFuture<Object> newFuture = SettableFuture.create();
/*     */     
/* 124 */     final ListenableFuture<?> oldFuture = this.ref.getAndSet(newFuture);
/*     */ 
/*     */ 
/*     */     
/* 128 */     final ListenableFuture<T> taskFuture = Futures.submitAsync(task, new Executor()
/*     */         {
/*     */           
/*     */           public void execute(Runnable runnable)
/*     */           {
/* 133 */             oldFuture.addListener(runnable, executor);
/*     */           }
/*     */         });
/*     */     
/* 137 */     final ListenableFuture<T> outputFuture = Futures.nonCancellationPropagating(taskFuture);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 143 */     Runnable listener = new Runnable()
/*     */       {
/*     */         public void run()
/*     */         {
/* 147 */           if (taskFuture.isDone() || (outputFuture
/*     */ 
/*     */ 
/*     */             
/* 151 */             .isCancelled() && runningState.compareAndSet(ExecutionSequencer.RunningState.NOT_RUN, ExecutionSequencer.RunningState.CANCELLED)))
/*     */           {
/*     */ 
/*     */             
/* 155 */             newFuture.setFuture(oldFuture);
/*     */           }
/*     */         }
/*     */       };
/*     */ 
/*     */ 
/*     */     
/* 162 */     outputFuture.addListener(listener, MoreExecutors.directExecutor());
/* 163 */     taskFuture.addListener(listener, MoreExecutors.directExecutor());
/*     */     
/* 165 */     return outputFuture;
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\google\commo\\util\concurrent\ExecutionSequencer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */