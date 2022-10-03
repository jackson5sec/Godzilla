/*     */ package org.springframework.util.concurrent;
/*     */ 
/*     */ import java.util.concurrent.Callable;
/*     */ import java.util.concurrent.CompletableFuture;
/*     */ import java.util.concurrent.ExecutionException;
/*     */ import java.util.concurrent.FutureTask;
/*     */ import org.springframework.lang.Nullable;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ListenableFutureTask<T>
/*     */   extends FutureTask<T>
/*     */   implements ListenableFuture<T>
/*     */ {
/*  35 */   private final ListenableFutureCallbackRegistry<T> callbacks = new ListenableFutureCallbackRegistry<>();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ListenableFutureTask(Callable<T> callable) {
/*  44 */     super(callable);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ListenableFutureTask(Runnable runnable, @Nullable T result) {
/*  55 */     super(runnable, result);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void addCallback(ListenableFutureCallback<? super T> callback) {
/*  61 */     this.callbacks.addCallback(callback);
/*     */   }
/*     */ 
/*     */   
/*     */   public void addCallback(SuccessCallback<? super T> successCallback, FailureCallback failureCallback) {
/*  66 */     this.callbacks.addSuccessCallback(successCallback);
/*  67 */     this.callbacks.addFailureCallback(failureCallback);
/*     */   }
/*     */ 
/*     */   
/*     */   public CompletableFuture<T> completable() {
/*  72 */     CompletableFuture<T> completable = new DelegatingCompletableFuture<>(this);
/*  73 */     this.callbacks.addSuccessCallback(completable::complete);
/*  74 */     this.callbacks.addFailureCallback(completable::completeExceptionally);
/*  75 */     return completable;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected void done() {
/*     */     Throwable cause;
/*     */     try {
/*  83 */       T result = get();
/*  84 */       this.callbacks.success(result);
/*     */       
/*     */       return;
/*  87 */     } catch (InterruptedException ex) {
/*  88 */       Thread.currentThread().interrupt();
/*     */       
/*     */       return;
/*  91 */     } catch (ExecutionException ex) {
/*  92 */       cause = ex.getCause();
/*  93 */       if (cause == null) {
/*  94 */         cause = ex;
/*     */       }
/*     */     }
/*  97 */     catch (Throwable ex) {
/*  98 */       cause = ex;
/*     */     } 
/* 100 */     this.callbacks.failure(cause);
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\springframewor\\util\concurrent\ListenableFutureTask.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */