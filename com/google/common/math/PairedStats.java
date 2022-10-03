/*     */ package com.google.common.math;
/*     */ 
/*     */ import com.google.common.annotations.Beta;
/*     */ import com.google.common.annotations.GwtIncompatible;
/*     */ import com.google.common.base.MoreObjects;
/*     */ import com.google.common.base.Objects;
/*     */ import com.google.common.base.Preconditions;
/*     */ import java.io.Serializable;
/*     */ import java.nio.ByteBuffer;
/*     */ import java.nio.ByteOrder;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
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
/*     */ public final class PairedStats
/*     */   implements Serializable
/*     */ {
/*     */   private final Stats xStats;
/*     */   private final Stats yStats;
/*     */   private final double sumOfProductsOfDeltas;
/*     */   private static final int BYTES = 88;
/*     */   private static final long serialVersionUID = 0L;
/*     */   
/*     */   PairedStats(Stats xStats, Stats yStats, double sumOfProductsOfDeltas) {
/*  61 */     this.xStats = xStats;
/*  62 */     this.yStats = yStats;
/*  63 */     this.sumOfProductsOfDeltas = sumOfProductsOfDeltas;
/*     */   }
/*     */ 
/*     */   
/*     */   public long count() {
/*  68 */     return this.xStats.count();
/*     */   }
/*     */ 
/*     */   
/*     */   public Stats xStats() {
/*  73 */     return this.xStats;
/*     */   }
/*     */ 
/*     */   
/*     */   public Stats yStats() {
/*  78 */     return this.yStats;
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
/*     */   public double populationCovariance() {
/*  96 */     Preconditions.checkState((count() != 0L));
/*  97 */     return this.sumOfProductsOfDeltas / count();
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
/*     */   public double sampleCovariance() {
/* 114 */     Preconditions.checkState((count() > 1L));
/* 115 */     return this.sumOfProductsOfDeltas / (count() - 1L);
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
/*     */   public double pearsonsCorrelationCoefficient() {
/* 135 */     Preconditions.checkState((count() > 1L));
/* 136 */     if (Double.isNaN(this.sumOfProductsOfDeltas)) {
/* 137 */       return Double.NaN;
/*     */     }
/* 139 */     double xSumOfSquaresOfDeltas = xStats().sumOfSquaresOfDeltas();
/* 140 */     double ySumOfSquaresOfDeltas = yStats().sumOfSquaresOfDeltas();
/* 141 */     Preconditions.checkState((xSumOfSquaresOfDeltas > 0.0D));
/* 142 */     Preconditions.checkState((ySumOfSquaresOfDeltas > 0.0D));
/*     */ 
/*     */ 
/*     */     
/* 146 */     double productOfSumsOfSquaresOfDeltas = ensurePositive(xSumOfSquaresOfDeltas * ySumOfSquaresOfDeltas);
/* 147 */     return ensureInUnitRange(this.sumOfProductsOfDeltas / Math.sqrt(productOfSumsOfSquaresOfDeltas));
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
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public LinearTransformation leastSquaresFit() {
/* 182 */     Preconditions.checkState((count() > 1L));
/* 183 */     if (Double.isNaN(this.sumOfProductsOfDeltas)) {
/* 184 */       return LinearTransformation.forNaN();
/*     */     }
/* 186 */     double xSumOfSquaresOfDeltas = this.xStats.sumOfSquaresOfDeltas();
/* 187 */     if (xSumOfSquaresOfDeltas > 0.0D) {
/* 188 */       if (this.yStats.sumOfSquaresOfDeltas() > 0.0D) {
/* 189 */         return LinearTransformation.mapping(this.xStats.mean(), this.yStats.mean())
/* 190 */           .withSlope(this.sumOfProductsOfDeltas / xSumOfSquaresOfDeltas);
/*     */       }
/* 192 */       return LinearTransformation.horizontal(this.yStats.mean());
/*     */     } 
/*     */     
/* 195 */     Preconditions.checkState((this.yStats.sumOfSquaresOfDeltas() > 0.0D));
/* 196 */     return LinearTransformation.vertical(this.xStats.mean());
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
/*     */   public boolean equals(Object obj) {
/* 217 */     if (obj == null) {
/* 218 */       return false;
/*     */     }
/* 220 */     if (getClass() != obj.getClass()) {
/* 221 */       return false;
/*     */     }
/* 223 */     PairedStats other = (PairedStats)obj;
/* 224 */     return (this.xStats.equals(other.xStats) && this.yStats
/* 225 */       .equals(other.yStats) && 
/* 226 */       Double.doubleToLongBits(this.sumOfProductsOfDeltas) == Double.doubleToLongBits(other.sumOfProductsOfDeltas));
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
/* 237 */     return Objects.hashCode(new Object[] { this.xStats, this.yStats, Double.valueOf(this.sumOfProductsOfDeltas) });
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 242 */     if (count() > 0L) {
/* 243 */       return MoreObjects.toStringHelper(this)
/* 244 */         .add("xStats", this.xStats)
/* 245 */         .add("yStats", this.yStats)
/* 246 */         .add("populationCovariance", populationCovariance())
/* 247 */         .toString();
/*     */     }
/* 249 */     return MoreObjects.toStringHelper(this)
/* 250 */       .add("xStats", this.xStats)
/* 251 */       .add("yStats", this.yStats)
/* 252 */       .toString();
/*     */   }
/*     */ 
/*     */   
/*     */   double sumOfProductsOfDeltas() {
/* 257 */     return this.sumOfProductsOfDeltas;
/*     */   }
/*     */   
/*     */   private static double ensurePositive(double value) {
/* 261 */     if (value > 0.0D) {
/* 262 */       return value;
/*     */     }
/* 264 */     return Double.MIN_VALUE;
/*     */   }
/*     */ 
/*     */   
/*     */   private static double ensureInUnitRange(double value) {
/* 269 */     if (value >= 1.0D) {
/* 270 */       return 1.0D;
/*     */     }
/* 272 */     if (value <= -1.0D) {
/* 273 */       return -1.0D;
/*     */     }
/* 275 */     return value;
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
/*     */   public byte[] toByteArray() {
/* 290 */     ByteBuffer buffer = ByteBuffer.allocate(88).order(ByteOrder.LITTLE_ENDIAN);
/* 291 */     this.xStats.writeTo(buffer);
/* 292 */     this.yStats.writeTo(buffer);
/* 293 */     buffer.putDouble(this.sumOfProductsOfDeltas);
/* 294 */     return buffer.array();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static PairedStats fromByteArray(byte[] byteArray) {
/* 305 */     Preconditions.checkNotNull(byteArray);
/* 306 */     Preconditions.checkArgument((byteArray.length == 88), "Expected PairedStats.BYTES = %s, got %s", 88, byteArray.length);
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 311 */     ByteBuffer buffer = ByteBuffer.wrap(byteArray).order(ByteOrder.LITTLE_ENDIAN);
/* 312 */     Stats xStats = Stats.readFrom(buffer);
/* 313 */     Stats yStats = Stats.readFrom(buffer);
/* 314 */     double sumOfProductsOfDeltas = buffer.getDouble();
/* 315 */     return new PairedStats(xStats, yStats, sumOfProductsOfDeltas);
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\google\common\math\PairedStats.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */