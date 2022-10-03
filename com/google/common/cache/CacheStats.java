/*     */ package com.google.common.cache;
/*     */ 
/*     */ import com.google.common.annotations.GwtCompatible;
/*     */ import com.google.common.base.MoreObjects;
/*     */ import com.google.common.base.Objects;
/*     */ import com.google.common.base.Preconditions;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @GwtCompatible
/*     */ public final class CacheStats
/*     */ {
/*     */   private final long hitCount;
/*     */   private final long missCount;
/*     */   private final long loadSuccessCount;
/*     */   private final long loadExceptionCount;
/*     */   private final long totalLoadTime;
/*     */   private final long evictionCount;
/*     */   
/*     */   public CacheStats(long hitCount, long missCount, long loadSuccessCount, long loadExceptionCount, long totalLoadTime, long evictionCount) {
/*  83 */     Preconditions.checkArgument((hitCount >= 0L));
/*  84 */     Preconditions.checkArgument((missCount >= 0L));
/*  85 */     Preconditions.checkArgument((loadSuccessCount >= 0L));
/*  86 */     Preconditions.checkArgument((loadExceptionCount >= 0L));
/*  87 */     Preconditions.checkArgument((totalLoadTime >= 0L));
/*  88 */     Preconditions.checkArgument((evictionCount >= 0L));
/*     */     
/*  90 */     this.hitCount = hitCount;
/*  91 */     this.missCount = missCount;
/*  92 */     this.loadSuccessCount = loadSuccessCount;
/*  93 */     this.loadExceptionCount = loadExceptionCount;
/*  94 */     this.totalLoadTime = totalLoadTime;
/*  95 */     this.evictionCount = evictionCount;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public long requestCount() {
/* 103 */     return this.hitCount + this.missCount;
/*     */   }
/*     */ 
/*     */   
/*     */   public long hitCount() {
/* 108 */     return this.hitCount;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public double hitRate() {
/* 117 */     long requestCount = requestCount();
/* 118 */     return (requestCount == 0L) ? 1.0D : (this.hitCount / requestCount);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public long missCount() {
/* 128 */     return this.missCount;
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
/*     */   public double missRate() {
/* 141 */     long requestCount = requestCount();
/* 142 */     return (requestCount == 0L) ? 0.0D : (this.missCount / requestCount);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public long loadCount() {
/* 151 */     return this.loadSuccessCount + this.loadExceptionCount;
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
/*     */   public long loadSuccessCount() {
/* 165 */     return this.loadSuccessCount;
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
/*     */   public long loadExceptionCount() {
/* 179 */     return this.loadExceptionCount;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public double loadExceptionRate() {
/* 188 */     long totalLoadCount = this.loadSuccessCount + this.loadExceptionCount;
/* 189 */     return (totalLoadCount == 0L) ? 0.0D : (this.loadExceptionCount / totalLoadCount);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public long totalLoadTime() {
/* 199 */     return this.totalLoadTime;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public double averageLoadPenalty() {
/* 207 */     long totalLoadCount = this.loadSuccessCount + this.loadExceptionCount;
/* 208 */     return (totalLoadCount == 0L) ? 0.0D : (this.totalLoadTime / totalLoadCount);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public long evictionCount() {
/* 216 */     return this.evictionCount;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public CacheStats minus(CacheStats other) {
/* 225 */     return new CacheStats(
/* 226 */         Math.max(0L, this.hitCount - other.hitCount), 
/* 227 */         Math.max(0L, this.missCount - other.missCount), 
/* 228 */         Math.max(0L, this.loadSuccessCount - other.loadSuccessCount), 
/* 229 */         Math.max(0L, this.loadExceptionCount - other.loadExceptionCount), 
/* 230 */         Math.max(0L, this.totalLoadTime - other.totalLoadTime), 
/* 231 */         Math.max(0L, this.evictionCount - other.evictionCount));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public CacheStats plus(CacheStats other) {
/* 241 */     return new CacheStats(this.hitCount + other.hitCount, this.missCount + other.missCount, this.loadSuccessCount + other.loadSuccessCount, this.loadExceptionCount + other.loadExceptionCount, this.totalLoadTime + other.totalLoadTime, this.evictionCount + other.evictionCount);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 252 */     return Objects.hashCode(new Object[] {
/* 253 */           Long.valueOf(this.hitCount), Long.valueOf(this.missCount), Long.valueOf(this.loadSuccessCount), Long.valueOf(this.loadExceptionCount), Long.valueOf(this.totalLoadTime), Long.valueOf(this.evictionCount)
/*     */         });
/*     */   }
/*     */   
/*     */   public boolean equals(Object object) {
/* 258 */     if (object instanceof CacheStats) {
/* 259 */       CacheStats other = (CacheStats)object;
/* 260 */       return (this.hitCount == other.hitCount && this.missCount == other.missCount && this.loadSuccessCount == other.loadSuccessCount && this.loadExceptionCount == other.loadExceptionCount && this.totalLoadTime == other.totalLoadTime && this.evictionCount == other.evictionCount);
/*     */     } 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 267 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 272 */     return MoreObjects.toStringHelper(this)
/* 273 */       .add("hitCount", this.hitCount)
/* 274 */       .add("missCount", this.missCount)
/* 275 */       .add("loadSuccessCount", this.loadSuccessCount)
/* 276 */       .add("loadExceptionCount", this.loadExceptionCount)
/* 277 */       .add("totalLoadTime", this.totalLoadTime)
/* 278 */       .add("evictionCount", this.evictionCount)
/* 279 */       .toString();
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\google\common\cache\CacheStats.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */