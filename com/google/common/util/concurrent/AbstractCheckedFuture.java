/*     */ package com.google.common.util.concurrent;
/*     */ 
/*     */ import com.google.common.annotations.Beta;
/*     */ import com.google.common.annotations.GwtIncompatible;
/*     */ import com.google.errorprone.annotations.CanIgnoreReturnValue;
/*     */ import java.util.concurrent.CancellationException;
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
/*     */ @Deprecated
/*     */ @Beta
/*     */ @GwtIncompatible
/*     */ public abstract class AbstractCheckedFuture<V, X extends Exception>
/*     */   extends ForwardingListenableFuture.SimpleForwardingListenableFuture<V>
/*     */   implements CheckedFuture<V, X>
/*     */ {
/*     */   protected AbstractCheckedFuture(ListenableFuture<V> delegate) {
/*  49 */     super(delegate);
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
/*     */   protected abstract X mapException(Exception paramException);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
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
/*     */   public V checkedGet() throws X {
/*     */     try {
/*  84 */       return get();
/*  85 */     } catch (InterruptedException e) {
/*  86 */       Thread.currentThread().interrupt();
/*  87 */       throw mapException(e);
/*  88 */     } catch (CancellationException|java.util.concurrent.ExecutionException e) {
/*  89 */       throw mapException(e);
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
/*     */   @CanIgnoreReturnValue
/*     */   public V checkedGet(long timeout, TimeUnit unit) throws TimeoutException, X {
/*     */     try {
/* 110 */       return get(timeout, unit);
/* 111 */     } catch (InterruptedException e) {
/* 112 */       Thread.currentThread().interrupt();
/* 113 */       throw mapException(e);
/* 114 */     } catch (CancellationException|java.util.concurrent.ExecutionException e) {
/* 115 */       throw mapException(e);
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\google\commo\\util\concurrent\AbstractCheckedFuture.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */