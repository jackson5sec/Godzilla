/*     */ package com.google.common.math;
/*     */ 
/*     */ import com.google.common.annotations.Beta;
/*     */ import com.google.common.annotations.GwtIncompatible;
/*     */ import com.google.common.base.Preconditions;
/*     */ import com.google.common.primitives.Doubles;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
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
/*     */ public final class PairedStatsAccumulator
/*     */ {
/*  38 */   private final StatsAccumulator xStats = new StatsAccumulator();
/*  39 */   private final StatsAccumulator yStats = new StatsAccumulator();
/*  40 */   private double sumOfProductsOfDeltas = 0.0D;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void add(double x, double y) {
/*  55 */     this.xStats.add(x);
/*  56 */     if (Doubles.isFinite(x) && Doubles.isFinite(y)) {
/*  57 */       if (this.xStats.count() > 1L) {
/*  58 */         this.sumOfProductsOfDeltas += (x - this.xStats.mean()) * (y - this.yStats.mean());
/*     */       }
/*     */     } else {
/*  61 */       this.sumOfProductsOfDeltas = Double.NaN;
/*     */     } 
/*  63 */     this.yStats.add(y);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addAll(PairedStats values) {
/*  71 */     if (values.count() == 0L) {
/*     */       return;
/*     */     }
/*     */     
/*  75 */     this.xStats.addAll(values.xStats());
/*  76 */     if (this.yStats.count() == 0L) {
/*  77 */       this.sumOfProductsOfDeltas = values.sumOfProductsOfDeltas();
/*     */     
/*     */     }
/*     */     else {
/*     */       
/*  82 */       this.sumOfProductsOfDeltas += values
/*  83 */         .sumOfProductsOfDeltas() + (values
/*  84 */         .xStats().mean() - this.xStats.mean()) * (values
/*  85 */         .yStats().mean() - this.yStats.mean()) * values
/*  86 */         .count();
/*     */     } 
/*  88 */     this.yStats.addAll(values.yStats());
/*     */   }
/*     */ 
/*     */   
/*     */   public PairedStats snapshot() {
/*  93 */     return new PairedStats(this.xStats.snapshot(), this.yStats.snapshot(), this.sumOfProductsOfDeltas);
/*     */   }
/*     */ 
/*     */   
/*     */   public long count() {
/*  98 */     return this.xStats.count();
/*     */   }
/*     */ 
/*     */   
/*     */   public Stats xStats() {
/* 103 */     return this.xStats.snapshot();
/*     */   }
/*     */ 
/*     */   
/*     */   public Stats yStats() {
/* 108 */     return this.yStats.snapshot();
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
/* 126 */     Preconditions.checkState((count() != 0L));
/* 127 */     return this.sumOfProductsOfDeltas / count();
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
/*     */   public final double sampleCovariance() {
/* 144 */     Preconditions.checkState((count() > 1L));
/* 145 */     return this.sumOfProductsOfDeltas / (count() - 1L);
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
/*     */   public final double pearsonsCorrelationCoefficient() {
/* 165 */     Preconditions.checkState((count() > 1L));
/* 166 */     if (Double.isNaN(this.sumOfProductsOfDeltas)) {
/* 167 */       return Double.NaN;
/*     */     }
/* 169 */     double xSumOfSquaresOfDeltas = this.xStats.sumOfSquaresOfDeltas();
/* 170 */     double ySumOfSquaresOfDeltas = this.yStats.sumOfSquaresOfDeltas();
/* 171 */     Preconditions.checkState((xSumOfSquaresOfDeltas > 0.0D));
/* 172 */     Preconditions.checkState((ySumOfSquaresOfDeltas > 0.0D));
/*     */ 
/*     */ 
/*     */     
/* 176 */     double productOfSumsOfSquaresOfDeltas = ensurePositive(xSumOfSquaresOfDeltas * ySumOfSquaresOfDeltas);
/* 177 */     return ensureInUnitRange(this.sumOfProductsOfDeltas / Math.sqrt(productOfSumsOfSquaresOfDeltas));
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
/*     */   public final LinearTransformation leastSquaresFit() {
/* 212 */     Preconditions.checkState((count() > 1L));
/* 213 */     if (Double.isNaN(this.sumOfProductsOfDeltas)) {
/* 214 */       return LinearTransformation.forNaN();
/*     */     }
/* 216 */     double xSumOfSquaresOfDeltas = this.xStats.sumOfSquaresOfDeltas();
/* 217 */     if (xSumOfSquaresOfDeltas > 0.0D) {
/* 218 */       if (this.yStats.sumOfSquaresOfDeltas() > 0.0D) {
/* 219 */         return LinearTransformation.mapping(this.xStats.mean(), this.yStats.mean())
/* 220 */           .withSlope(this.sumOfProductsOfDeltas / xSumOfSquaresOfDeltas);
/*     */       }
/* 222 */       return LinearTransformation.horizontal(this.yStats.mean());
/*     */     } 
/*     */     
/* 225 */     Preconditions.checkState((this.yStats.sumOfSquaresOfDeltas() > 0.0D));
/* 226 */     return LinearTransformation.vertical(this.xStats.mean());
/*     */   }
/*     */ 
/*     */   
/*     */   private double ensurePositive(double value) {
/* 231 */     if (value > 0.0D) {
/* 232 */       return value;
/*     */     }
/* 234 */     return Double.MIN_VALUE;
/*     */   }
/*     */ 
/*     */   
/*     */   private static double ensureInUnitRange(double value) {
/* 239 */     if (value >= 1.0D) {
/* 240 */       return 1.0D;
/*     */     }
/* 242 */     if (value <= -1.0D) {
/* 243 */       return -1.0D;
/*     */     }
/* 245 */     return value;
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\google\common\math\PairedStatsAccumulator.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */