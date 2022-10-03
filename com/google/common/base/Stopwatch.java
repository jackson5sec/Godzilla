/*     */ package com.google.common.base;
/*     */ 
/*     */ import com.google.common.annotations.GwtCompatible;
/*     */ import com.google.common.annotations.GwtIncompatible;
/*     */ import com.google.errorprone.annotations.CanIgnoreReturnValue;
/*     */ import java.time.Duration;
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
/*     */ @GwtCompatible(emulated = true)
/*     */ public final class Stopwatch
/*     */ {
/*     */   private final Ticker ticker;
/*     */   private boolean isRunning;
/*     */   private long elapsedNanos;
/*     */   private long startTick;
/*     */   
/*     */   public static Stopwatch createUnstarted() {
/*  96 */     return new Stopwatch();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Stopwatch createUnstarted(Ticker ticker) {
/* 105 */     return new Stopwatch(ticker);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Stopwatch createStarted() {
/* 114 */     return (new Stopwatch()).start();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Stopwatch createStarted(Ticker ticker) {
/* 123 */     return (new Stopwatch(ticker)).start();
/*     */   }
/*     */   
/*     */   Stopwatch() {
/* 127 */     this.ticker = Ticker.systemTicker();
/*     */   }
/*     */   
/*     */   Stopwatch(Ticker ticker) {
/* 131 */     this.ticker = Preconditions.<Ticker>checkNotNull(ticker, "ticker");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isRunning() {
/* 139 */     return this.isRunning;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @CanIgnoreReturnValue
/*     */   public Stopwatch start() {
/* 150 */     Preconditions.checkState(!this.isRunning, "This stopwatch is already running.");
/* 151 */     this.isRunning = true;
/* 152 */     this.startTick = this.ticker.read();
/* 153 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @CanIgnoreReturnValue
/*     */   public Stopwatch stop() {
/* 165 */     long tick = this.ticker.read();
/* 166 */     Preconditions.checkState(this.isRunning, "This stopwatch is already stopped.");
/* 167 */     this.isRunning = false;
/* 168 */     this.elapsedNanos += tick - this.startTick;
/* 169 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @CanIgnoreReturnValue
/*     */   public Stopwatch reset() {
/* 179 */     this.elapsedNanos = 0L;
/* 180 */     this.isRunning = false;
/* 181 */     return this;
/*     */   }
/*     */   
/*     */   private long elapsedNanos() {
/* 185 */     return this.isRunning ? (this.ticker.read() - this.startTick + this.elapsedNanos) : this.elapsedNanos;
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
/*     */   public long elapsed(TimeUnit desiredUnit) {
/* 202 */     return desiredUnit.convert(elapsedNanos(), TimeUnit.NANOSECONDS);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @GwtIncompatible
/*     */   public Duration elapsed() {
/* 214 */     return Duration.ofNanos(elapsedNanos());
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String toString() {
/* 220 */     long nanos = elapsedNanos();
/*     */     
/* 222 */     TimeUnit unit = chooseUnit(nanos);
/* 223 */     double value = nanos / TimeUnit.NANOSECONDS.convert(1L, unit);
/*     */ 
/*     */     
/* 226 */     return Platform.formatCompact4Digits(value) + " " + abbreviate(unit);
/*     */   }
/*     */   
/*     */   private static TimeUnit chooseUnit(long nanos) {
/* 230 */     if (TimeUnit.DAYS.convert(nanos, TimeUnit.NANOSECONDS) > 0L) {
/* 231 */       return TimeUnit.DAYS;
/*     */     }
/* 233 */     if (TimeUnit.HOURS.convert(nanos, TimeUnit.NANOSECONDS) > 0L) {
/* 234 */       return TimeUnit.HOURS;
/*     */     }
/* 236 */     if (TimeUnit.MINUTES.convert(nanos, TimeUnit.NANOSECONDS) > 0L) {
/* 237 */       return TimeUnit.MINUTES;
/*     */     }
/* 239 */     if (TimeUnit.SECONDS.convert(nanos, TimeUnit.NANOSECONDS) > 0L) {
/* 240 */       return TimeUnit.SECONDS;
/*     */     }
/* 242 */     if (TimeUnit.MILLISECONDS.convert(nanos, TimeUnit.NANOSECONDS) > 0L) {
/* 243 */       return TimeUnit.MILLISECONDS;
/*     */     }
/* 245 */     if (TimeUnit.MICROSECONDS.convert(nanos, TimeUnit.NANOSECONDS) > 0L) {
/* 246 */       return TimeUnit.MICROSECONDS;
/*     */     }
/* 248 */     return TimeUnit.NANOSECONDS;
/*     */   }
/*     */   
/*     */   private static String abbreviate(TimeUnit unit) {
/* 252 */     switch (unit) {
/*     */       case NANOSECONDS:
/* 254 */         return "ns";
/*     */       case MICROSECONDS:
/* 256 */         return "μs";
/*     */       case MILLISECONDS:
/* 258 */         return "ms";
/*     */       case SECONDS:
/* 260 */         return "s";
/*     */       case MINUTES:
/* 262 */         return "min";
/*     */       case HOURS:
/* 264 */         return "h";
/*     */       case DAYS:
/* 266 */         return "d";
/*     */     } 
/* 268 */     throw new AssertionError();
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\google\common\base\Stopwatch.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */