/*     */ package com.google.common.util.concurrent;
/*     */ 
/*     */ import com.google.common.annotations.GwtIncompatible;
/*     */ import com.google.common.math.LongMath;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
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
/*     */ abstract class SmoothRateLimiter
/*     */   extends RateLimiter
/*     */ {
/*     */   double storedPermits;
/*     */   double maxPermits;
/*     */   double stableIntervalMicros;
/*     */   
/*     */   static final class SmoothWarmingUp
/*     */     extends SmoothRateLimiter
/*     */   {
/*     */     private final long warmupPeriodMicros;
/*     */     private double slope;
/*     */     private double thresholdPermits;
/*     */     private double coldFactor;
/*     */     
/*     */     SmoothWarmingUp(RateLimiter.SleepingStopwatch stopwatch, long warmupPeriod, TimeUnit timeUnit, double coldFactor) {
/* 217 */       super(stopwatch);
/* 218 */       this.warmupPeriodMicros = timeUnit.toMicros(warmupPeriod);
/* 219 */       this.coldFactor = coldFactor;
/*     */     }
/*     */ 
/*     */     
/*     */     void doSetRate(double permitsPerSecond, double stableIntervalMicros) {
/* 224 */       double oldMaxPermits = this.maxPermits;
/* 225 */       double coldIntervalMicros = stableIntervalMicros * this.coldFactor;
/* 226 */       this.thresholdPermits = 0.5D * this.warmupPeriodMicros / stableIntervalMicros;
/* 227 */       this.maxPermits = this.thresholdPermits + 2.0D * this.warmupPeriodMicros / (stableIntervalMicros + coldIntervalMicros);
/*     */       
/* 229 */       this.slope = (coldIntervalMicros - stableIntervalMicros) / (this.maxPermits - this.thresholdPermits);
/* 230 */       if (oldMaxPermits == Double.POSITIVE_INFINITY) {
/*     */         
/* 232 */         this.storedPermits = 0.0D;
/*     */       } else {
/* 234 */         this.storedPermits = (oldMaxPermits == 0.0D) ? this.maxPermits : (this.storedPermits * this.maxPermits / oldMaxPermits);
/*     */       } 
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     long storedPermitsToWaitTime(double storedPermits, double permitsToTake) {
/* 243 */       double availablePermitsAboveThreshold = storedPermits - this.thresholdPermits;
/* 244 */       long micros = 0L;
/*     */       
/* 246 */       if (availablePermitsAboveThreshold > 0.0D) {
/* 247 */         double permitsAboveThresholdToTake = Math.min(availablePermitsAboveThreshold, permitsToTake);
/*     */ 
/*     */ 
/*     */         
/* 251 */         double length = permitsToTime(availablePermitsAboveThreshold) + permitsToTime(availablePermitsAboveThreshold - permitsAboveThresholdToTake);
/* 252 */         micros = (long)(permitsAboveThresholdToTake * length / 2.0D);
/* 253 */         permitsToTake -= permitsAboveThresholdToTake;
/*     */       } 
/*     */       
/* 256 */       micros += (long)(this.stableIntervalMicros * permitsToTake);
/* 257 */       return micros;
/*     */     }
/*     */     
/*     */     private double permitsToTime(double permits) {
/* 261 */       return this.stableIntervalMicros + permits * this.slope;
/*     */     }
/*     */ 
/*     */     
/*     */     double coolDownIntervalMicros() {
/* 266 */       return this.warmupPeriodMicros / this.maxPermits;
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static final class SmoothBursty
/*     */     extends SmoothRateLimiter
/*     */   {
/*     */     final double maxBurstSeconds;
/*     */ 
/*     */ 
/*     */     
/*     */     SmoothBursty(RateLimiter.SleepingStopwatch stopwatch, double maxBurstSeconds) {
/* 281 */       super(stopwatch);
/* 282 */       this.maxBurstSeconds = maxBurstSeconds;
/*     */     }
/*     */ 
/*     */     
/*     */     void doSetRate(double permitsPerSecond, double stableIntervalMicros) {
/* 287 */       double oldMaxPermits = this.maxPermits;
/* 288 */       this.maxPermits = this.maxBurstSeconds * permitsPerSecond;
/* 289 */       if (oldMaxPermits == Double.POSITIVE_INFINITY) {
/*     */         
/* 291 */         this.storedPermits = this.maxPermits;
/*     */       } else {
/* 293 */         this.storedPermits = (oldMaxPermits == 0.0D) ? 0.0D : (this.storedPermits * this.maxPermits / oldMaxPermits);
/*     */       } 
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     long storedPermitsToWaitTime(double storedPermits, double permitsToTake) {
/* 302 */       return 0L;
/*     */     }
/*     */ 
/*     */     
/*     */     double coolDownIntervalMicros() {
/* 307 */       return this.stableIntervalMicros;
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
/*     */ 
/*     */   
/* 327 */   private long nextFreeTicketMicros = 0L;
/*     */   
/*     */   private SmoothRateLimiter(RateLimiter.SleepingStopwatch stopwatch) {
/* 330 */     super(stopwatch);
/*     */   }
/*     */ 
/*     */   
/*     */   final void doSetRate(double permitsPerSecond, long nowMicros) {
/* 335 */     resync(nowMicros);
/* 336 */     double stableIntervalMicros = TimeUnit.SECONDS.toMicros(1L) / permitsPerSecond;
/* 337 */     this.stableIntervalMicros = stableIntervalMicros;
/* 338 */     doSetRate(permitsPerSecond, stableIntervalMicros);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   final double doGetRate() {
/* 345 */     return TimeUnit.SECONDS.toMicros(1L) / this.stableIntervalMicros;
/*     */   }
/*     */ 
/*     */   
/*     */   final long queryEarliestAvailable(long nowMicros) {
/* 350 */     return this.nextFreeTicketMicros;
/*     */   }
/*     */ 
/*     */   
/*     */   final long reserveEarliestAvailable(int requiredPermits, long nowMicros) {
/* 355 */     resync(nowMicros);
/* 356 */     long returnValue = this.nextFreeTicketMicros;
/* 357 */     double storedPermitsToSpend = Math.min(requiredPermits, this.storedPermits);
/* 358 */     double freshPermits = requiredPermits - storedPermitsToSpend;
/*     */     
/* 360 */     long waitMicros = storedPermitsToWaitTime(this.storedPermits, storedPermitsToSpend) + (long)(freshPermits * this.stableIntervalMicros);
/*     */ 
/*     */     
/* 363 */     this.nextFreeTicketMicros = LongMath.saturatedAdd(this.nextFreeTicketMicros, waitMicros);
/* 364 */     this.storedPermits -= storedPermitsToSpend;
/* 365 */     return returnValue;
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
/*     */   void resync(long nowMicros) {
/* 385 */     if (nowMicros > this.nextFreeTicketMicros) {
/* 386 */       double newPermits = (nowMicros - this.nextFreeTicketMicros) / coolDownIntervalMicros();
/* 387 */       this.storedPermits = Math.min(this.maxPermits, this.storedPermits + newPermits);
/* 388 */       this.nextFreeTicketMicros = nowMicros;
/*     */     } 
/*     */   }
/*     */   
/*     */   abstract void doSetRate(double paramDouble1, double paramDouble2);
/*     */   
/*     */   abstract long storedPermitsToWaitTime(double paramDouble1, double paramDouble2);
/*     */   
/*     */   abstract double coolDownIntervalMicros();
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\google\commo\\util\concurrent\SmoothRateLimiter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */