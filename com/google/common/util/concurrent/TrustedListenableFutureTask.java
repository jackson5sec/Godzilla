/*     */ package com.google.common.util.concurrent;
/*     */ 
/*     */ import com.google.common.annotations.GwtCompatible;
/*     */ import com.google.common.base.Preconditions;
/*     */ import java.util.concurrent.Callable;
/*     */ import java.util.concurrent.Executors;
/*     */ import java.util.concurrent.RunnableFuture;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
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
/*     */ class TrustedListenableFutureTask<V>
/*     */   extends FluentFuture.TrustedFuture<V>
/*     */   implements RunnableFuture<V>
/*     */ {
/*     */   private volatile InterruptibleTask<?> task;
/*     */   
/*     */   static <V> TrustedListenableFutureTask<V> create(AsyncCallable<V> callable) {
/*  37 */     return new TrustedListenableFutureTask<>(callable);
/*     */   }
/*     */   
/*     */   static <V> TrustedListenableFutureTask<V> create(Callable<V> callable) {
/*  41 */     return new TrustedListenableFutureTask<>(callable);
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
/*     */   static <V> TrustedListenableFutureTask<V> create(Runnable runnable, V result) {
/*  54 */     return new TrustedListenableFutureTask<>(Executors.callable(runnable, result));
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
/*     */   TrustedListenableFutureTask(Callable<V> callable) {
/*  67 */     this.task = new TrustedFutureInterruptibleTask(callable);
/*     */   }
/*     */   
/*     */   TrustedListenableFutureTask(AsyncCallable<V> callable) {
/*  71 */     this.task = new TrustedFutureInterruptibleAsyncTask(callable);
/*     */   }
/*     */ 
/*     */   
/*     */   public void run() {
/*  76 */     InterruptibleTask<?> localTask = this.task;
/*  77 */     if (localTask != null) {
/*  78 */       localTask.run();
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  84 */     this.task = null;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void afterDone() {
/*  89 */     super.afterDone();
/*     */     
/*  91 */     if (wasInterrupted()) {
/*  92 */       InterruptibleTask<?> localTask = this.task;
/*  93 */       if (localTask != null) {
/*  94 */         localTask.interruptTask();
/*     */       }
/*     */     } 
/*     */     
/*  98 */     this.task = null;
/*     */   }
/*     */ 
/*     */   
/*     */   protected String pendingToString() {
/* 103 */     InterruptibleTask<?> localTask = this.task;
/* 104 */     if (localTask != null) {
/* 105 */       return "task=[" + localTask + "]";
/*     */     }
/* 107 */     return super.pendingToString();
/*     */   }
/*     */   
/*     */   private final class TrustedFutureInterruptibleTask
/*     */     extends InterruptibleTask<V> {
/*     */     private final Callable<V> callable;
/*     */     
/*     */     TrustedFutureInterruptibleTask(Callable<V> callable) {
/* 115 */       this.callable = (Callable<V>)Preconditions.checkNotNull(callable);
/*     */     }
/*     */ 
/*     */     
/*     */     final boolean isDone() {
/* 120 */       return TrustedListenableFutureTask.this.isDone();
/*     */     }
/*     */ 
/*     */     
/*     */     V runInterruptibly() throws Exception {
/* 125 */       return this.callable.call();
/*     */     }
/*     */ 
/*     */     
/*     */     void afterRanInterruptibly(V result, Throwable error) {
/* 130 */       if (error == null) {
/* 131 */         TrustedListenableFutureTask.this.set(result);
/*     */       } else {
/* 133 */         TrustedListenableFutureTask.this.setException(error);
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     String toPendingString() {
/* 139 */       return this.callable.toString();
/*     */     }
/*     */   }
/*     */   
/*     */   private final class TrustedFutureInterruptibleAsyncTask
/*     */     extends InterruptibleTask<ListenableFuture<V>>
/*     */   {
/*     */     private final AsyncCallable<V> callable;
/*     */     
/*     */     TrustedFutureInterruptibleAsyncTask(AsyncCallable<V> callable) {
/* 149 */       this.callable = (AsyncCallable<V>)Preconditions.checkNotNull(callable);
/*     */     }
/*     */ 
/*     */     
/*     */     final boolean isDone() {
/* 154 */       return TrustedListenableFutureTask.this.isDone();
/*     */     }
/*     */ 
/*     */     
/*     */     ListenableFuture<V> runInterruptibly() throws Exception {
/* 159 */       return (ListenableFuture<V>)Preconditions.checkNotNull(this.callable
/* 160 */           .call(), "AsyncCallable.call returned null instead of a Future. Did you mean to return immediateFuture(null)? %s", this.callable);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     void afterRanInterruptibly(ListenableFuture<V> result, Throwable error) {
/* 168 */       if (error == null) {
/* 169 */         TrustedListenableFutureTask.this.setFuture(result);
/*     */       } else {
/* 171 */         TrustedListenableFutureTask.this.setException(error);
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     String toPendingString() {
/* 177 */       return this.callable.toString();
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\google\commo\\util\concurrent\TrustedListenableFutureTask.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */