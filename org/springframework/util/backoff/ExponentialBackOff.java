/*     */ package org.springframework.util.backoff;
/*     */ 
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ExponentialBackOff
/*     */   implements BackOff
/*     */ {
/*     */   public static final long DEFAULT_INITIAL_INTERVAL = 2000L;
/*     */   public static final double DEFAULT_MULTIPLIER = 1.5D;
/*     */   public static final long DEFAULT_MAX_INTERVAL = 30000L;
/*     */   public static final long DEFAULT_MAX_ELAPSED_TIME = 9223372036854775807L;
/*  78 */   private long initialInterval = 2000L;
/*     */   
/*  80 */   private double multiplier = 1.5D;
/*     */   
/*  82 */   private long maxInterval = 30000L;
/*     */   
/*  84 */   private long maxElapsedTime = Long.MAX_VALUE;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ExponentialBackOff(long initialInterval, double multiplier) {
/* 103 */     checkMultiplier(multiplier);
/* 104 */     this.initialInterval = initialInterval;
/* 105 */     this.multiplier = multiplier;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setInitialInterval(long initialInterval) {
/* 113 */     this.initialInterval = initialInterval;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public long getInitialInterval() {
/* 120 */     return this.initialInterval;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setMultiplier(double multiplier) {
/* 127 */     checkMultiplier(multiplier);
/* 128 */     this.multiplier = multiplier;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public double getMultiplier() {
/* 135 */     return this.multiplier;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setMaxInterval(long maxInterval) {
/* 142 */     this.maxInterval = maxInterval;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public long getMaxInterval() {
/* 149 */     return this.maxInterval;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setMaxElapsedTime(long maxElapsedTime) {
/* 157 */     this.maxElapsedTime = maxElapsedTime;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public long getMaxElapsedTime() {
/* 165 */     return this.maxElapsedTime;
/*     */   }
/*     */ 
/*     */   
/*     */   public BackOffExecution start() {
/* 170 */     return new ExponentialBackOffExecution();
/*     */   }
/*     */   
/*     */   private void checkMultiplier(double multiplier) {
/* 174 */     Assert.isTrue((multiplier >= 1.0D), () -> "Invalid multiplier '" + multiplier + "'. Should be greater than or equal to 1. A multiplier of 1 is equivalent to a fixed interval.");
/*     */   }
/*     */   
/*     */   public ExponentialBackOff() {}
/*     */   
/*     */   private class ExponentialBackOffExecution
/*     */     implements BackOffExecution {
/* 181 */     private long currentInterval = -1L;
/*     */     
/* 183 */     private long currentElapsedTime = 0L;
/*     */ 
/*     */     
/*     */     public long nextBackOff() {
/* 187 */       if (this.currentElapsedTime >= ExponentialBackOff.this.maxElapsedTime) {
/* 188 */         return -1L;
/*     */       }
/*     */       
/* 191 */       long nextInterval = computeNextInterval();
/* 192 */       this.currentElapsedTime += nextInterval;
/* 193 */       return nextInterval;
/*     */     }
/*     */     
/*     */     private long computeNextInterval() {
/* 197 */       long maxInterval = ExponentialBackOff.this.getMaxInterval();
/* 198 */       if (this.currentInterval >= maxInterval) {
/* 199 */         return maxInterval;
/*     */       }
/* 201 */       if (this.currentInterval < 0L) {
/* 202 */         long initialInterval = ExponentialBackOff.this.getInitialInterval();
/* 203 */         this.currentInterval = Math.min(initialInterval, maxInterval);
/*     */       } else {
/*     */         
/* 206 */         this.currentInterval = multiplyInterval(maxInterval);
/*     */       } 
/* 208 */       return this.currentInterval;
/*     */     }
/*     */     
/*     */     private long multiplyInterval(long maxInterval) {
/* 212 */       long i = this.currentInterval;
/* 213 */       i = (long)(i * ExponentialBackOff.this.getMultiplier());
/* 214 */       return Math.min(i, maxInterval);
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public String toString() {
/* 220 */       StringBuilder sb = new StringBuilder("ExponentialBackOff{");
/* 221 */       sb.append("currentInterval=").append((this.currentInterval < 0L) ? "n/a" : (this.currentInterval + "ms"));
/* 222 */       sb.append(", multiplier=").append(ExponentialBackOff.this.getMultiplier());
/* 223 */       sb.append('}');
/* 224 */       return sb.toString();
/*     */     }
/*     */     
/*     */     private ExponentialBackOffExecution() {}
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\springframewor\\util\backoff\ExponentialBackOff.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */