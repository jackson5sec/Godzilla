/*    */ package com.google.common.util.concurrent;
/*    */ 
/*    */ import com.google.common.annotations.GwtIncompatible;
/*    */ import com.google.errorprone.annotations.CanIgnoreReturnValue;
/*    */ import java.util.concurrent.Callable;
/*    */ import java.util.concurrent.ExecutorService;
/*    */ import java.util.concurrent.Future;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ @CanIgnoreReturnValue
/*    */ @GwtIncompatible
/*    */ public abstract class ForwardingListeningExecutorService
/*    */   extends ForwardingExecutorService
/*    */   implements ListeningExecutorService
/*    */ {
/*    */   public <T> ListenableFuture<T> submit(Callable<T> task) {
/* 42 */     return delegate().submit(task);
/*    */   }
/*    */ 
/*    */   
/*    */   public ListenableFuture<?> submit(Runnable task) {
/* 47 */     return delegate().submit(task);
/*    */   }
/*    */ 
/*    */   
/*    */   public <T> ListenableFuture<T> submit(Runnable task, T result) {
/* 52 */     return delegate().submit(task, result);
/*    */   }
/*    */   
/*    */   protected abstract ListeningExecutorService delegate();
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\google\commo\\util\concurrent\ForwardingListeningExecutorService.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */