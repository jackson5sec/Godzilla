/*     */ package org.springframework.util.concurrent;
/*     */ 
/*     */ import java.util.concurrent.CompletableFuture;
/*     */ import java.util.concurrent.CompletionStage;
/*     */ import java.util.concurrent.ExecutionException;
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
/*     */ public class CompletableToListenableFutureAdapter<T>
/*     */   implements ListenableFuture<T>
/*     */ {
/*     */   private final CompletableFuture<T> completableFuture;
/*  38 */   private final ListenableFutureCallbackRegistry<T> callbacks = new ListenableFutureCallbackRegistry<>();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public CompletableToListenableFutureAdapter(CompletionStage<T> completionStage) {
/*  46 */     this(completionStage.toCompletableFuture());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public CompletableToListenableFutureAdapter(CompletableFuture<T> completableFuture) {
/*  53 */     this.completableFuture = completableFuture;
/*  54 */     this.completableFuture.whenComplete((result, ex) -> {
/*     */           if (ex != null) {
/*     */             this.callbacks.failure(ex);
/*     */           } else {
/*     */             this.callbacks.success((T)result);
/*     */           } 
/*     */         });
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addCallback(ListenableFutureCallback<? super T> callback) {
/*  67 */     this.callbacks.addCallback(callback);
/*     */   }
/*     */ 
/*     */   
/*     */   public void addCallback(SuccessCallback<? super T> successCallback, FailureCallback failureCallback) {
/*  72 */     this.callbacks.addSuccessCallback(successCallback);
/*  73 */     this.callbacks.addFailureCallback(failureCallback);
/*     */   }
/*     */ 
/*     */   
/*     */   public CompletableFuture<T> completable() {
/*  78 */     return this.completableFuture;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean cancel(boolean mayInterruptIfRunning) {
/*  84 */     return this.completableFuture.cancel(mayInterruptIfRunning);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isCancelled() {
/*  89 */     return this.completableFuture.isCancelled();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isDone() {
/*  94 */     return this.completableFuture.isDone();
/*     */   }
/*     */ 
/*     */   
/*     */   public T get() throws InterruptedException, ExecutionException {
/*  99 */     return this.completableFuture.get();
/*     */   }
/*     */ 
/*     */   
/*     */   public T get(long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
/* 104 */     return this.completableFuture.get(timeout, unit);
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\springframewor\\util\concurrent\CompletableToListenableFutureAdapter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */