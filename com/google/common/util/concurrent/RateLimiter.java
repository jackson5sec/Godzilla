/*     */ package com.google.common.util.concurrent;
/*     */ 
/*     */ import com.google.common.annotations.Beta;
/*     */ import com.google.common.annotations.GwtIncompatible;
/*     */ import com.google.common.annotations.VisibleForTesting;
/*     */ import com.google.common.base.Preconditions;
/*     */ import com.google.common.base.Stopwatch;
/*     */ import com.google.errorprone.annotations.CanIgnoreReturnValue;
/*     */ import java.util.Locale;
/*     */ import java.util.concurrent.TimeUnit;
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
/*     */ 
/*     */ @Beta
/*     */ @GwtIncompatible
/*     */ public abstract class RateLimiter
/*     */ {
/*     */   private final SleepingStopwatch stopwatch;
/*     */   private volatile Object mutexDoNotUseDirectly;
/*     */   
/*     */   public static RateLimiter create(double permitsPerSecond) {
/* 128 */     return create(permitsPerSecond, SleepingStopwatch.createFromSystemTimer());
/*     */   }
/*     */   
/*     */   @VisibleForTesting
/*     */   static RateLimiter create(double permitsPerSecond, SleepingStopwatch stopwatch) {
/* 133 */     RateLimiter rateLimiter = new SmoothRateLimiter.SmoothBursty(stopwatch, 1.0D);
/* 134 */     rateLimiter.setRate(permitsPerSecond);
/* 135 */     return rateLimiter;
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
/*     */   public static RateLimiter create(double permitsPerSecond, long warmupPeriod, TimeUnit unit) {
/* 163 */     Preconditions.checkArgument((warmupPeriod >= 0L), "warmupPeriod must not be negative: %s", warmupPeriod);
/* 164 */     return create(permitsPerSecond, warmupPeriod, unit, 3.0D, 
/* 165 */         SleepingStopwatch.createFromSystemTimer());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @VisibleForTesting
/*     */   static RateLimiter create(double permitsPerSecond, long warmupPeriod, TimeUnit unit, double coldFactor, SleepingStopwatch stopwatch) {
/* 175 */     RateLimiter rateLimiter = new SmoothRateLimiter.SmoothWarmingUp(stopwatch, warmupPeriod, unit, coldFactor);
/* 176 */     rateLimiter.setRate(permitsPerSecond);
/* 177 */     return rateLimiter;
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
/*     */   private Object mutex() {
/* 190 */     Object mutex = this.mutexDoNotUseDirectly;
/* 191 */     if (mutex == null) {
/* 192 */       synchronized (this) {
/* 193 */         mutex = this.mutexDoNotUseDirectly;
/* 194 */         if (mutex == null) {
/* 195 */           this.mutexDoNotUseDirectly = mutex = new Object();
/*     */         }
/*     */       } 
/*     */     }
/* 199 */     return mutex;
/*     */   }
/*     */   
/*     */   RateLimiter(SleepingStopwatch stopwatch) {
/* 203 */     this.stopwatch = (SleepingStopwatch)Preconditions.checkNotNull(stopwatch);
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
/*     */   public final void setRate(double permitsPerSecond) {
/* 225 */     Preconditions.checkArgument((permitsPerSecond > 0.0D && 
/* 226 */         !Double.isNaN(permitsPerSecond)), "rate must be positive");
/* 227 */     synchronized (mutex()) {
/* 228 */       doSetRate(permitsPerSecond, this.stopwatch.readMicros());
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   abstract void doSetRate(double paramDouble, long paramLong);
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final double getRate() {
/* 241 */     synchronized (mutex()) {
/* 242 */       return doGetRate();
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   abstract double doGetRate();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @CanIgnoreReturnValue
/*     */   public double acquire() {
/* 259 */     return acquire(1);
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
/*     */   @CanIgnoreReturnValue
/*     */   public double acquire(int permits) {
/* 273 */     long microsToWait = reserve(permits);
/* 274 */     this.stopwatch.sleepMicrosUninterruptibly(microsToWait);
/* 275 */     return 1.0D * microsToWait / TimeUnit.SECONDS.toMicros(1L);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   final long reserve(int permits) {
/* 285 */     checkPermits(permits);
/* 286 */     synchronized (mutex()) {
/* 287 */       return reserveAndGetWaitLength(permits, this.stopwatch.readMicros());
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
/*     */   public boolean tryAcquire(long timeout, TimeUnit unit) {
/* 304 */     return tryAcquire(1, timeout, unit);
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
/*     */   public boolean tryAcquire(int permits) {
/* 318 */     return tryAcquire(permits, 0L, TimeUnit.MICROSECONDS);
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
/*     */   public boolean tryAcquire() {
/* 331 */     return tryAcquire(1, 0L, TimeUnit.MICROSECONDS);
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
/*     */   public boolean tryAcquire(int permits, long timeout, TimeUnit unit) {
/* 346 */     long microsToWait, timeoutMicros = Math.max(unit.toMicros(timeout), 0L);
/* 347 */     checkPermits(permits);
/*     */     
/* 349 */     synchronized (mutex()) {
/* 350 */       long nowMicros = this.stopwatch.readMicros();
/* 351 */       if (!canAcquire(nowMicros, timeoutMicros)) {
/* 352 */         return false;
/*     */       }
/* 354 */       microsToWait = reserveAndGetWaitLength(permits, nowMicros);
/*     */     } 
/*     */     
/* 357 */     this.stopwatch.sleepMicrosUninterruptibly(microsToWait);
/* 358 */     return true;
/*     */   }
/*     */   
/*     */   private boolean canAcquire(long nowMicros, long timeoutMicros) {
/* 362 */     return (queryEarliestAvailable(nowMicros) - timeoutMicros <= nowMicros);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   final long reserveAndGetWaitLength(int permits, long nowMicros) {
/* 371 */     long momentAvailable = reserveEarliestAvailable(permits, nowMicros);
/* 372 */     return Math.max(momentAvailable - nowMicros, 0L);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   abstract long queryEarliestAvailable(long paramLong);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   abstract long reserveEarliestAvailable(int paramInt, long paramLong);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String toString() {
/* 394 */     return String.format(Locale.ROOT, "RateLimiter[stableRate=%3.1fqps]", new Object[] { Double.valueOf(getRate()) });
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static abstract class SleepingStopwatch
/*     */   {
/*     */     protected abstract long readMicros();
/*     */ 
/*     */ 
/*     */     
/*     */     protected abstract void sleepMicrosUninterruptibly(long param1Long);
/*     */ 
/*     */ 
/*     */     
/*     */     public static SleepingStopwatch createFromSystemTimer() {
/* 411 */       return new SleepingStopwatch() {
/* 412 */           final Stopwatch stopwatch = Stopwatch.createStarted();
/*     */ 
/*     */           
/*     */           protected long readMicros() {
/* 416 */             return this.stopwatch.elapsed(TimeUnit.MICROSECONDS);
/*     */           }
/*     */ 
/*     */           
/*     */           protected void sleepMicrosUninterruptibly(long micros) {
/* 421 */             if (micros > 0L) {
/* 422 */               Uninterruptibles.sleepUninterruptibly(micros, TimeUnit.MICROSECONDS);
/*     */             }
/*     */           }
/*     */         };
/*     */     }
/*     */   }
/*     */   
/*     */   private static void checkPermits(int permits) {
/* 430 */     Preconditions.checkArgument((permits > 0), "Requested permits (%s) must be positive", permits);
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\google\commo\\util\concurrent\RateLimiter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */