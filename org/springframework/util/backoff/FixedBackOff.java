/*     */ package org.springframework.util.backoff;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class FixedBackOff
/*     */   implements BackOff
/*     */ {
/*     */   public static final long DEFAULT_INTERVAL = 5000L;
/*     */   public static final long UNLIMITED_ATTEMPTS = 9223372036854775807L;
/*  38 */   private long interval = 5000L;
/*     */   
/*  40 */   private long maxAttempts = Long.MAX_VALUE;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public FixedBackOff(long interval, long maxAttempts) {
/*  56 */     this.interval = interval;
/*  57 */     this.maxAttempts = maxAttempts;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setInterval(long interval) {
/*  65 */     this.interval = interval;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public long getInterval() {
/*  72 */     return this.interval;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setMaxAttempts(long maxAttempts) {
/*  79 */     this.maxAttempts = maxAttempts;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public long getMaxAttempts() {
/*  86 */     return this.maxAttempts;
/*     */   }
/*     */ 
/*     */   
/*     */   public BackOffExecution start() {
/*  91 */     return new FixedBackOffExecution();
/*     */   }
/*     */   
/*     */   public FixedBackOff() {}
/*     */   
/*     */   private class FixedBackOffExecution implements BackOffExecution {
/*  97 */     private long currentAttempts = 0L;
/*     */ 
/*     */     
/*     */     public long nextBackOff() {
/* 101 */       this.currentAttempts++;
/* 102 */       if (this.currentAttempts <= FixedBackOff.this.getMaxAttempts()) {
/* 103 */         return FixedBackOff.this.getInterval();
/*     */       }
/*     */       
/* 106 */       return -1L;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public String toString() {
/* 113 */       String attemptValue = (FixedBackOff.this.maxAttempts == Long.MAX_VALUE) ? "unlimited" : String.valueOf(FixedBackOff.this.maxAttempts);
/* 114 */       return "FixedBackOff{interval=" + FixedBackOff.this.interval + ", currentAttempts=" + this.currentAttempts + ", maxAttempts=" + attemptValue + '}';
/*     */     }
/*     */     
/*     */     private FixedBackOffExecution() {}
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\springframewor\\util\backoff\FixedBackOff.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */