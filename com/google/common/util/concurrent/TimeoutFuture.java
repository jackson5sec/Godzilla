/*     */ package com.google.common.util.concurrent;
/*     */ 
/*     */ import com.google.common.annotations.GwtIncompatible;
/*     */ import com.google.common.base.Preconditions;
/*     */ import java.util.concurrent.Future;
/*     */ import java.util.concurrent.ScheduledExecutorService;
/*     */ import java.util.concurrent.ScheduledFuture;
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
/*     */ @GwtIncompatible
/*     */ final class TimeoutFuture<V>
/*     */   extends FluentFuture.TrustedFuture<V>
/*     */ {
/*     */   private ListenableFuture<V> delegateRef;
/*     */   private ScheduledFuture<?> timer;
/*     */   
/*     */   static <V> ListenableFuture<V> create(ListenableFuture<V> delegate, long time, TimeUnit unit, ScheduledExecutorService scheduledExecutor) {
/*  43 */     TimeoutFuture<V> result = new TimeoutFuture<>(delegate);
/*  44 */     Fire<V> fire = new Fire<>(result);
/*  45 */     result.timer = scheduledExecutor.schedule(fire, time, unit);
/*  46 */     delegate.addListener(fire, MoreExecutors.directExecutor());
/*  47 */     return result;
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
/*     */   private TimeoutFuture(ListenableFuture<V> delegate) {
/*  78 */     this.delegateRef = (ListenableFuture<V>)Preconditions.checkNotNull(delegate);
/*     */   }
/*     */   
/*     */   private static final class Fire<V>
/*     */     implements Runnable {
/*     */     TimeoutFuture<V> timeoutFutureRef;
/*     */     
/*     */     Fire(TimeoutFuture<V> timeoutFuture) {
/*  86 */       this.timeoutFutureRef = timeoutFuture;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public void run() {
/*  93 */       TimeoutFuture<V> timeoutFuture = this.timeoutFutureRef;
/*  94 */       if (timeoutFuture == null) {
/*     */         return;
/*     */       }
/*  97 */       ListenableFuture<V> delegate = timeoutFuture.delegateRef;
/*  98 */       if (delegate == null) {
/*     */         return;
/*     */       }
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
/* 114 */       this.timeoutFutureRef = null;
/* 115 */       if (delegate.isDone()) {
/* 116 */         timeoutFuture.setFuture(delegate);
/*     */       } else {
/*     */         try {
/* 119 */           ScheduledFuture<?> timer = timeoutFuture.timer;
/* 120 */           String message = "Timed out";
/* 121 */           if (timer != null) {
/* 122 */             long overDelayMs = Math.abs(timer.getDelay(TimeUnit.MILLISECONDS));
/* 123 */             if (overDelayMs > 10L) {
/* 124 */               message = message + " (timeout delayed by " + overDelayMs + " ms after scheduled time)";
/*     */             }
/*     */           } 
/* 127 */           timeoutFuture.timer = null;
/* 128 */           timeoutFuture.setException(new TimeoutFuture.TimeoutFutureException(message + ": " + delegate));
/*     */         } finally {
/* 130 */           delegate.cancel(true);
/*     */         } 
/*     */       } 
/*     */     }
/*     */   }
/*     */   
/*     */   private static final class TimeoutFutureException extends TimeoutException {
/*     */     private TimeoutFutureException(String message) {
/* 138 */       super(message);
/*     */     }
/*     */ 
/*     */     
/*     */     public synchronized Throwable fillInStackTrace() {
/* 143 */       setStackTrace(new StackTraceElement[0]);
/* 144 */       return this;
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   protected String pendingToString() {
/* 150 */     ListenableFuture<? extends V> localInputFuture = this.delegateRef;
/* 151 */     ScheduledFuture<?> localTimer = this.timer;
/* 152 */     if (localInputFuture != null) {
/* 153 */       String message = "inputFuture=[" + localInputFuture + "]";
/* 154 */       if (localTimer != null) {
/* 155 */         long delay = localTimer.getDelay(TimeUnit.MILLISECONDS);
/*     */         
/* 157 */         if (delay > 0L) {
/* 158 */           message = message + ", remaining delay=[" + delay + " ms]";
/*     */         }
/*     */       } 
/* 161 */       return message;
/*     */     } 
/* 163 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void afterDone() {
/* 168 */     maybePropagateCancellationTo(this.delegateRef);
/*     */     
/* 170 */     Future<?> localTimer = this.timer;
/*     */ 
/*     */ 
/*     */     
/* 174 */     if (localTimer != null) {
/* 175 */       localTimer.cancel(false);
/*     */     }
/*     */     
/* 178 */     this.delegateRef = null;
/* 179 */     this.timer = null;
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\google\commo\\util\concurrent\TimeoutFuture.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */