/*     */ package com.google.common.util.concurrent;
/*     */ 
/*     */ import com.google.common.annotations.GwtCompatible;
/*     */ import com.google.common.base.Preconditions;
/*     */ import com.google.common.collect.ImmutableCollection;
/*     */ import java.util.concurrent.Callable;
/*     */ import java.util.concurrent.Executor;
/*     */ import java.util.concurrent.RejectedExecutionException;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @GwtCompatible
/*     */ final class CombinedFuture<V>
/*     */   extends AggregateFuture<Object, V>
/*     */ {
/*     */   CombinedFuture(ImmutableCollection<? extends ListenableFuture<?>> futures, boolean allMustSucceed, Executor listenerExecutor, AsyncCallable<V> callable) {
/*  38 */     init(new CombinedFutureRunningState(futures, allMustSucceed, new AsyncCallableInterruptibleTask(callable, listenerExecutor)));
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
/*     */   CombinedFuture(ImmutableCollection<? extends ListenableFuture<?>> futures, boolean allMustSucceed, Executor listenerExecutor, Callable<V> callable) {
/*  50 */     init(new CombinedFutureRunningState(futures, allMustSucceed, new CallableInterruptibleTask(callable, listenerExecutor)));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private final class CombinedFutureRunningState
/*     */     extends AggregateFuture<Object, V>.RunningState
/*     */   {
/*     */     private CombinedFuture.CombinedFutureInterruptibleTask task;
/*     */ 
/*     */     
/*     */     CombinedFutureRunningState(ImmutableCollection<? extends ListenableFuture<?>> futures, boolean allMustSucceed, CombinedFuture.CombinedFutureInterruptibleTask task) {
/*  62 */       super(futures, allMustSucceed, false);
/*  63 */       this.task = task;
/*     */     }
/*     */ 
/*     */     
/*     */     void collectOneValue(boolean allMustSucceed, int index, Object returnValue) {}
/*     */ 
/*     */     
/*     */     void handleAllCompleted() {
/*  71 */       CombinedFuture.CombinedFutureInterruptibleTask localTask = this.task;
/*  72 */       if (localTask != null) {
/*  73 */         localTask.execute();
/*     */       } else {
/*  75 */         Preconditions.checkState(CombinedFuture.this.isDone());
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     void releaseResourcesAfterFailure() {
/*  81 */       super.releaseResourcesAfterFailure();
/*  82 */       this.task = null;
/*     */     }
/*     */ 
/*     */     
/*     */     void interruptTask() {
/*  87 */       CombinedFuture.CombinedFutureInterruptibleTask localTask = this.task;
/*  88 */       if (localTask != null)
/*  89 */         localTask.interruptTask(); 
/*     */     }
/*     */   }
/*     */   
/*     */   private abstract class CombinedFutureInterruptibleTask<T>
/*     */     extends InterruptibleTask<T>
/*     */   {
/*     */     private final Executor listenerExecutor;
/*     */     boolean thrownByExecute = true;
/*     */     
/*     */     public CombinedFutureInterruptibleTask(Executor listenerExecutor) {
/* 100 */       this.listenerExecutor = (Executor)Preconditions.checkNotNull(listenerExecutor);
/*     */     }
/*     */ 
/*     */     
/*     */     final boolean isDone() {
/* 105 */       return CombinedFuture.this.isDone();
/*     */     }
/*     */     
/*     */     final void execute() {
/*     */       try {
/* 110 */         this.listenerExecutor.execute(this);
/* 111 */       } catch (RejectedExecutionException e) {
/* 112 */         if (this.thrownByExecute) {
/* 113 */           CombinedFuture.this.setException(e);
/*     */         }
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     final void afterRanInterruptibly(T result, Throwable error) {
/* 120 */       if (error != null) {
/* 121 */         if (error instanceof java.util.concurrent.ExecutionException) {
/* 122 */           CombinedFuture.this.setException(error.getCause());
/* 123 */         } else if (error instanceof java.util.concurrent.CancellationException) {
/* 124 */           CombinedFuture.this.cancel(false);
/*     */         } else {
/* 126 */           CombinedFuture.this.setException(error);
/*     */         } 
/*     */       } else {
/* 129 */         setValue(result);
/*     */       } 
/*     */     }
/*     */     
/*     */     abstract void setValue(T param1T);
/*     */   }
/*     */   
/*     */   private final class AsyncCallableInterruptibleTask
/*     */     extends CombinedFutureInterruptibleTask<ListenableFuture<V>>
/*     */   {
/*     */     private final AsyncCallable<V> callable;
/*     */     
/*     */     public AsyncCallableInterruptibleTask(AsyncCallable<V> callable, Executor listenerExecutor) {
/* 142 */       super(listenerExecutor);
/* 143 */       this.callable = (AsyncCallable<V>)Preconditions.checkNotNull(callable);
/*     */     }
/*     */ 
/*     */     
/*     */     ListenableFuture<V> runInterruptibly() throws Exception {
/* 148 */       this.thrownByExecute = false;
/* 149 */       ListenableFuture<V> result = this.callable.call();
/* 150 */       return (ListenableFuture<V>)Preconditions.checkNotNull(result, "AsyncCallable.call returned null instead of a Future. Did you mean to return immediateFuture(null)? %s", this.callable);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     void setValue(ListenableFuture<V> value) {
/* 159 */       CombinedFuture.this.setFuture(value);
/*     */     }
/*     */ 
/*     */     
/*     */     String toPendingString() {
/* 164 */       return this.callable.toString();
/*     */     }
/*     */   }
/*     */   
/*     */   private final class CallableInterruptibleTask
/*     */     extends CombinedFutureInterruptibleTask<V> {
/*     */     private final Callable<V> callable;
/*     */     
/*     */     public CallableInterruptibleTask(Callable<V> callable, Executor listenerExecutor) {
/* 173 */       super(listenerExecutor);
/* 174 */       this.callable = (Callable<V>)Preconditions.checkNotNull(callable);
/*     */     }
/*     */ 
/*     */     
/*     */     V runInterruptibly() throws Exception {
/* 179 */       this.thrownByExecute = false;
/* 180 */       return this.callable.call();
/*     */     }
/*     */ 
/*     */     
/*     */     void setValue(V value) {
/* 185 */       CombinedFuture.this.set(value);
/*     */     }
/*     */ 
/*     */     
/*     */     String toPendingString() {
/* 190 */       return this.callable.toString();
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\google\commo\\util\concurrent\CombinedFuture.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */