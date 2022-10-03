/*    */ package com.google.common.util.concurrent;
/*    */ 
/*    */ import com.google.common.annotations.Beta;
/*    */ import com.google.common.annotations.GwtIncompatible;
/*    */ import com.google.common.base.Preconditions;
/*    */ import com.google.errorprone.annotations.CanIgnoreReturnValue;
/*    */ import java.util.concurrent.Callable;
/*    */ import java.util.concurrent.ExecutionException;
/*    */ import java.util.concurrent.TimeUnit;
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
/*    */ @Beta
/*    */ @CanIgnoreReturnValue
/*    */ @GwtIncompatible
/*    */ public final class FakeTimeLimiter
/*    */   implements TimeLimiter
/*    */ {
/*    */   public <T> T newProxy(T target, Class<T> interfaceType, long timeoutDuration, TimeUnit timeoutUnit) {
/* 43 */     Preconditions.checkNotNull(target);
/* 44 */     Preconditions.checkNotNull(interfaceType);
/* 45 */     Preconditions.checkNotNull(timeoutUnit);
/* 46 */     return target;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public <T> T callWithTimeout(Callable<T> callable, long timeoutDuration, TimeUnit timeoutUnit) throws ExecutionException {
/* 52 */     Preconditions.checkNotNull(callable);
/* 53 */     Preconditions.checkNotNull(timeoutUnit);
/*    */     try {
/* 55 */       return callable.call();
/* 56 */     } catch (RuntimeException e) {
/* 57 */       throw new UncheckedExecutionException(e);
/* 58 */     } catch (Exception e) {
/* 59 */       throw new ExecutionException(e);
/* 60 */     } catch (Error e) {
/* 61 */       throw new ExecutionError(e);
/* 62 */     } catch (Throwable e) {
/*    */ 
/*    */       
/* 65 */       throw new ExecutionException(e);
/*    */     } 
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public <T> T callUninterruptiblyWithTimeout(Callable<T> callable, long timeoutDuration, TimeUnit timeoutUnit) throws ExecutionException {
/* 72 */     return callWithTimeout(callable, timeoutDuration, timeoutUnit);
/*    */   }
/*    */ 
/*    */   
/*    */   public void runWithTimeout(Runnable runnable, long timeoutDuration, TimeUnit timeoutUnit) {
/* 77 */     Preconditions.checkNotNull(runnable);
/* 78 */     Preconditions.checkNotNull(timeoutUnit);
/*    */     try {
/* 80 */       runnable.run();
/* 81 */     } catch (RuntimeException e) {
/* 82 */       throw new UncheckedExecutionException(e);
/* 83 */     } catch (Error e) {
/* 84 */       throw new ExecutionError(e);
/* 85 */     } catch (Throwable e) {
/*    */ 
/*    */       
/* 88 */       throw new UncheckedExecutionException(e);
/*    */     } 
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public void runUninterruptiblyWithTimeout(Runnable runnable, long timeoutDuration, TimeUnit timeoutUnit) {
/* 95 */     runWithTimeout(runnable, timeoutDuration, timeoutUnit);
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\google\commo\\util\concurrent\FakeTimeLimiter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */