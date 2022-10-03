/*     */ package com.google.common.math;
/*     */ 
/*     */ import com.google.common.annotations.Beta;
/*     */ import com.google.common.annotations.GwtIncompatible;
/*     */ import com.google.common.base.Preconditions;
/*     */ import com.google.errorprone.annotations.concurrent.LazyInit;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
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
/*     */ public abstract class LinearTransformation
/*     */ {
/*     */   public static LinearTransformationBuilder mapping(double x1, double y1) {
/*  46 */     Preconditions.checkArgument((DoubleUtils.isFinite(x1) && DoubleUtils.isFinite(y1)));
/*  47 */     return new LinearTransformationBuilder(x1, y1);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static final class LinearTransformationBuilder
/*     */   {
/*     */     private final double x1;
/*     */ 
/*     */ 
/*     */     
/*     */     private final double y1;
/*     */ 
/*     */ 
/*     */     
/*     */     private LinearTransformationBuilder(double x1, double y1) {
/*  64 */       this.x1 = x1;
/*  65 */       this.y1 = y1;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public LinearTransformation and(double x2, double y2) {
/*  75 */       Preconditions.checkArgument((DoubleUtils.isFinite(x2) && DoubleUtils.isFinite(y2)));
/*  76 */       if (x2 == this.x1) {
/*  77 */         Preconditions.checkArgument((y2 != this.y1));
/*  78 */         return new LinearTransformation.VerticalLinearTransformation(this.x1);
/*     */       } 
/*  80 */       return withSlope((y2 - this.y1) / (x2 - this.x1));
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public LinearTransformation withSlope(double slope) {
/*  90 */       Preconditions.checkArgument(!Double.isNaN(slope));
/*  91 */       if (DoubleUtils.isFinite(slope)) {
/*  92 */         double yIntercept = this.y1 - this.x1 * slope;
/*  93 */         return new LinearTransformation.RegularLinearTransformation(slope, yIntercept);
/*     */       } 
/*  95 */       return new LinearTransformation.VerticalLinearTransformation(this.x1);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static LinearTransformation vertical(double x) {
/* 105 */     Preconditions.checkArgument(DoubleUtils.isFinite(x));
/* 106 */     return new VerticalLinearTransformation(x);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static LinearTransformation horizontal(double y) {
/* 114 */     Preconditions.checkArgument(DoubleUtils.isFinite(y));
/* 115 */     double slope = 0.0D;
/* 116 */     return new RegularLinearTransformation(slope, y);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static LinearTransformation forNaN() {
/* 126 */     return NaNLinearTransformation.INSTANCE;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public abstract boolean isVertical();
/*     */ 
/*     */ 
/*     */   
/*     */   public abstract boolean isHorizontal();
/*     */ 
/*     */ 
/*     */   
/*     */   public abstract double slope();
/*     */ 
/*     */ 
/*     */   
/*     */   public abstract double transform(double paramDouble);
/*     */ 
/*     */ 
/*     */   
/*     */   public abstract LinearTransformation inverse();
/*     */ 
/*     */ 
/*     */   
/*     */   private static final class RegularLinearTransformation
/*     */     extends LinearTransformation
/*     */   {
/*     */     final double slope;
/*     */ 
/*     */     
/*     */     final double yIntercept;
/*     */ 
/*     */     
/*     */     @LazyInit
/*     */     LinearTransformation inverse;
/*     */ 
/*     */ 
/*     */     
/*     */     RegularLinearTransformation(double slope, double yIntercept) {
/* 167 */       this.slope = slope;
/* 168 */       this.yIntercept = yIntercept;
/* 169 */       this.inverse = null;
/*     */     }
/*     */     
/*     */     RegularLinearTransformation(double slope, double yIntercept, LinearTransformation inverse) {
/* 173 */       this.slope = slope;
/* 174 */       this.yIntercept = yIntercept;
/* 175 */       this.inverse = inverse;
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean isVertical() {
/* 180 */       return false;
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean isHorizontal() {
/* 185 */       return (this.slope == 0.0D);
/*     */     }
/*     */ 
/*     */     
/*     */     public double slope() {
/* 190 */       return this.slope;
/*     */     }
/*     */ 
/*     */     
/*     */     public double transform(double x) {
/* 195 */       return x * this.slope + this.yIntercept;
/*     */     }
/*     */ 
/*     */     
/*     */     public LinearTransformation inverse() {
/* 200 */       LinearTransformation result = this.inverse;
/* 201 */       return (result == null) ? (this.inverse = createInverse()) : result;
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString() {
/* 206 */       return String.format("y = %g * x + %g", new Object[] { Double.valueOf(this.slope), Double.valueOf(this.yIntercept) });
/*     */     }
/*     */     
/*     */     private LinearTransformation createInverse() {
/* 210 */       if (this.slope != 0.0D) {
/* 211 */         return new RegularLinearTransformation(1.0D / this.slope, -1.0D * this.yIntercept / this.slope, this);
/*     */       }
/* 213 */       return new LinearTransformation.VerticalLinearTransformation(this.yIntercept, this);
/*     */     }
/*     */   }
/*     */   
/*     */   private static final class VerticalLinearTransformation
/*     */     extends LinearTransformation
/*     */   {
/*     */     final double x;
/*     */     @LazyInit
/*     */     LinearTransformation inverse;
/*     */     
/*     */     VerticalLinearTransformation(double x) {
/* 225 */       this.x = x;
/* 226 */       this.inverse = null;
/*     */     }
/*     */     
/*     */     VerticalLinearTransformation(double x, LinearTransformation inverse) {
/* 230 */       this.x = x;
/* 231 */       this.inverse = inverse;
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean isVertical() {
/* 236 */       return true;
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean isHorizontal() {
/* 241 */       return false;
/*     */     }
/*     */ 
/*     */     
/*     */     public double slope() {
/* 246 */       throw new IllegalStateException();
/*     */     }
/*     */ 
/*     */     
/*     */     public double transform(double x) {
/* 251 */       throw new IllegalStateException();
/*     */     }
/*     */ 
/*     */     
/*     */     public LinearTransformation inverse() {
/* 256 */       LinearTransformation result = this.inverse;
/* 257 */       return (result == null) ? (this.inverse = createInverse()) : result;
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString() {
/* 262 */       return String.format("x = %g", new Object[] { Double.valueOf(this.x) });
/*     */     }
/*     */     
/*     */     private LinearTransformation createInverse() {
/* 266 */       return new LinearTransformation.RegularLinearTransformation(0.0D, this.x, this);
/*     */     }
/*     */   }
/*     */   
/*     */   private static final class NaNLinearTransformation
/*     */     extends LinearTransformation {
/* 272 */     static final NaNLinearTransformation INSTANCE = new NaNLinearTransformation();
/*     */ 
/*     */     
/*     */     public boolean isVertical() {
/* 276 */       return false;
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean isHorizontal() {
/* 281 */       return false;
/*     */     }
/*     */ 
/*     */     
/*     */     public double slope() {
/* 286 */       return Double.NaN;
/*     */     }
/*     */ 
/*     */     
/*     */     public double transform(double x) {
/* 291 */       return Double.NaN;
/*     */     }
/*     */ 
/*     */     
/*     */     public LinearTransformation inverse() {
/* 296 */       return this;
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString() {
/* 301 */       return "NaN";
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\google\common\math\LinearTransformation.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */