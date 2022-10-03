/*     */ package com.google.common.math;
/*     */ 
/*     */ import com.google.common.annotations.Beta;
/*     */ import com.google.common.annotations.GwtIncompatible;
/*     */ import com.google.common.base.Preconditions;
/*     */ import com.google.common.primitives.Doubles;
/*     */ import java.util.Iterator;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
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
/*     */ public final class StatsAccumulator
/*     */ {
/*  41 */   private long count = 0L;
/*  42 */   private double mean = 0.0D;
/*  43 */   private double sumOfSquaresOfDeltas = 0.0D;
/*  44 */   private double min = Double.NaN;
/*  45 */   private double max = Double.NaN;
/*     */ 
/*     */   
/*     */   public void add(double value) {
/*  49 */     if (this.count == 0L) {
/*  50 */       this.count = 1L;
/*  51 */       this.mean = value;
/*  52 */       this.min = value;
/*  53 */       this.max = value;
/*  54 */       if (!Doubles.isFinite(value)) {
/*  55 */         this.sumOfSquaresOfDeltas = Double.NaN;
/*     */       }
/*     */     } else {
/*  58 */       this.count++;
/*  59 */       if (Doubles.isFinite(value) && Doubles.isFinite(this.mean)) {
/*     */         
/*  61 */         double delta = value - this.mean;
/*  62 */         this.mean += delta / this.count;
/*  63 */         this.sumOfSquaresOfDeltas += delta * (value - this.mean);
/*     */       } else {
/*  65 */         this.mean = calculateNewMeanNonFinite(this.mean, value);
/*  66 */         this.sumOfSquaresOfDeltas = Double.NaN;
/*     */       } 
/*  68 */       this.min = Math.min(this.min, value);
/*  69 */       this.max = Math.max(this.max, value);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addAll(Iterable<? extends Number> values) {
/*  80 */     for (Number value : values) {
/*  81 */       add(value.doubleValue());
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addAll(Iterator<? extends Number> values) {
/*  92 */     while (values.hasNext()) {
/*  93 */       add(((Number)values.next()).doubleValue());
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addAll(double... values) {
/* 103 */     for (double value : values) {
/* 104 */       add(value);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addAll(int... values) {
/* 114 */     for (int value : values) {
/* 115 */       add(value);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addAll(long... values) {
/* 126 */     for (long value : values) {
/* 127 */       add(value);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addAll(Stats values) {
/* 136 */     if (values.count() == 0L) {
/*     */       return;
/*     */     }
/*     */     
/* 140 */     if (this.count == 0L) {
/* 141 */       this.count = values.count();
/* 142 */       this.mean = values.mean();
/* 143 */       this.sumOfSquaresOfDeltas = values.sumOfSquaresOfDeltas();
/* 144 */       this.min = values.min();
/* 145 */       this.max = values.max();
/*     */     } else {
/* 147 */       this.count += values.count();
/* 148 */       if (Doubles.isFinite(this.mean) && Doubles.isFinite(values.mean())) {
/*     */         
/* 150 */         double delta = values.mean() - this.mean;
/* 151 */         this.mean += delta * values.count() / this.count;
/* 152 */         this.sumOfSquaresOfDeltas += values
/* 153 */           .sumOfSquaresOfDeltas() + delta * (values.mean() - this.mean) * values.count();
/*     */       } else {
/* 155 */         this.mean = calculateNewMeanNonFinite(this.mean, values.mean());
/* 156 */         this.sumOfSquaresOfDeltas = Double.NaN;
/*     */       } 
/* 158 */       this.min = Math.min(this.min, values.min());
/* 159 */       this.max = Math.max(this.max, values.max());
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public Stats snapshot() {
/* 165 */     return new Stats(this.count, this.mean, this.sumOfSquaresOfDeltas, this.min, this.max);
/*     */   }
/*     */ 
/*     */   
/*     */   public long count() {
/* 170 */     return this.count;
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
/*     */   public double mean() {
/* 192 */     Preconditions.checkState((this.count != 0L));
/* 193 */     return this.mean;
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
/*     */   public final double sum() {
/* 209 */     return this.mean * this.count;
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
/*     */   public final double populationVariance() {
/* 228 */     Preconditions.checkState((this.count != 0L));
/* 229 */     if (Double.isNaN(this.sumOfSquaresOfDeltas)) {
/* 230 */       return Double.NaN;
/*     */     }
/* 232 */     if (this.count == 1L) {
/* 233 */       return 0.0D;
/*     */     }
/* 235 */     return DoubleUtils.ensureNonNegative(this.sumOfSquaresOfDeltas) / this.count;
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
/*     */   public final double populationStandardDeviation() {
/* 255 */     return Math.sqrt(populationVariance());
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
/*     */   public final double sampleVariance() {
/* 275 */     Preconditions.checkState((this.count > 1L));
/* 276 */     if (Double.isNaN(this.sumOfSquaresOfDeltas)) {
/* 277 */       return Double.NaN;
/*     */     }
/* 279 */     return DoubleUtils.ensureNonNegative(this.sumOfSquaresOfDeltas) / (this.count - 1L);
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
/*     */   public final double sampleStandardDeviation() {
/* 301 */     return Math.sqrt(sampleVariance());
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
/*     */   public double min() {
/* 318 */     Preconditions.checkState((this.count != 0L));
/* 319 */     return this.min;
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
/*     */   public double max() {
/* 336 */     Preconditions.checkState((this.count != 0L));
/* 337 */     return this.max;
/*     */   }
/*     */   
/*     */   double sumOfSquaresOfDeltas() {
/* 341 */     return this.sumOfSquaresOfDeltas;
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
/*     */   static double calculateNewMeanNonFinite(double previousMean, double value) {
/* 363 */     if (Doubles.isFinite(previousMean))
/*     */     {
/* 365 */       return value; } 
/* 366 */     if (Doubles.isFinite(value) || previousMean == value)
/*     */     {
/* 368 */       return previousMean;
/*     */     }
/*     */     
/* 371 */     return Double.NaN;
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\google\common\math\StatsAccumulator.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */