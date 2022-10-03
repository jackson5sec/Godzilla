/*     */ package org.springframework.util.concurrent;
/*     */ 
/*     */ import java.util.ArrayDeque;
/*     */ import java.util.Queue;
/*     */ import org.springframework.lang.Nullable;
/*     */ import org.springframework.util.Assert;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ListenableFutureCallbackRegistry<T>
/*     */ {
/*  39 */   private final Queue<SuccessCallback<? super T>> successCallbacks = new ArrayDeque<>(1);
/*     */   
/*  41 */   private final Queue<FailureCallback> failureCallbacks = new ArrayDeque<>(1);
/*     */   
/*  43 */   private State state = State.NEW;
/*     */   
/*     */   @Nullable
/*     */   private Object result;
/*     */   
/*  48 */   private final Object mutex = new Object();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addCallback(ListenableFutureCallback<? super T> callback) {
/*  56 */     Assert.notNull(callback, "'callback' must not be null");
/*  57 */     synchronized (this.mutex) {
/*  58 */       switch (this.state) {
/*     */         case NEW:
/*  60 */           this.successCallbacks.add(callback);
/*  61 */           this.failureCallbacks.add(callback);
/*     */           break;
/*     */         case SUCCESS:
/*  64 */           notifySuccess(callback);
/*     */           break;
/*     */         case FAILURE:
/*  67 */           notifyFailure(callback);
/*     */           break;
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private void notifySuccess(SuccessCallback<? super T> callback) {
/*     */     try {
/*  76 */       callback.onSuccess((T)this.result);
/*     */     }
/*  78 */     catch (Throwable throwable) {}
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private void notifyFailure(FailureCallback callback) {
/*  84 */     Assert.state(this.result instanceof Throwable, "No Throwable result for failure state");
/*     */     try {
/*  86 */       callback.onFailure((Throwable)this.result);
/*     */     }
/*  88 */     catch (Throwable throwable) {}
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addSuccessCallback(SuccessCallback<? super T> callback) {
/*  99 */     Assert.notNull(callback, "'callback' must not be null");
/* 100 */     synchronized (this.mutex) {
/* 101 */       switch (this.state) {
/*     */         case NEW:
/* 103 */           this.successCallbacks.add(callback);
/*     */           break;
/*     */         case SUCCESS:
/* 106 */           notifySuccess(callback);
/*     */           break;
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addFailureCallback(FailureCallback callback) {
/* 118 */     Assert.notNull(callback, "'callback' must not be null");
/* 119 */     synchronized (this.mutex) {
/* 120 */       switch (this.state) {
/*     */         case NEW:
/* 122 */           this.failureCallbacks.add(callback);
/*     */           break;
/*     */         case FAILURE:
/* 125 */           notifyFailure(callback);
/*     */           break;
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void success(@Nullable T result) {
/* 137 */     synchronized (this.mutex) {
/* 138 */       this.state = State.SUCCESS;
/* 139 */       this.result = result;
/*     */       SuccessCallback<? super T> callback;
/* 141 */       while ((callback = this.successCallbacks.poll()) != null) {
/* 142 */         notifySuccess(callback);
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void failure(Throwable ex) {
/* 153 */     synchronized (this.mutex) {
/* 154 */       this.state = State.FAILURE;
/* 155 */       this.result = ex;
/*     */       FailureCallback callback;
/* 157 */       while ((callback = this.failureCallbacks.poll()) != null)
/* 158 */         notifyFailure(callback); 
/*     */     } 
/*     */   }
/*     */   
/*     */   private enum State
/*     */   {
/* 164 */     NEW, SUCCESS, FAILURE;
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\springframewor\\util\concurrent\ListenableFutureCallbackRegistry.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */