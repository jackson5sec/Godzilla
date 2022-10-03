/*     */ package com.google.common.math;
/*     */ 
/*     */ import com.google.common.annotations.GwtIncompatible;
/*     */ import com.google.common.annotations.VisibleForTesting;
/*     */ import com.google.common.base.Preconditions;
/*     */ import java.math.BigInteger;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
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
/*     */ final class DoubleUtils
/*     */ {
/*     */   static final long SIGNIFICAND_MASK = 4503599627370495L;
/*     */   static final long EXPONENT_MASK = 9218868437227405312L;
/*     */   static final long SIGN_MASK = -9223372036854775808L;
/*     */   static final int SIGNIFICAND_BITS = 52;
/*     */   static final int EXPONENT_BIAS = 1023;
/*     */   static final long IMPLICIT_BIT = 4503599627370496L;
/*     */   @VisibleForTesting
/*     */   static final long ONE_BITS = 4607182418800017408L;
/*     */   
/*     */   static double nextDown(double d) {
/*  40 */     return -Math.nextUp(-d);
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
/*     */   static long getSignificand(double d) {
/*  63 */     Preconditions.checkArgument(isFinite(d), "not a normal value");
/*  64 */     int exponent = Math.getExponent(d);
/*  65 */     long bits = Double.doubleToRawLongBits(d);
/*  66 */     bits &= 0xFFFFFFFFFFFFFL;
/*  67 */     return (exponent == -1023) ? (bits << 1L) : (bits | 0x10000000000000L);
/*     */   }
/*     */   
/*     */   static boolean isFinite(double d) {
/*  71 */     return (Math.getExponent(d) <= 1023);
/*     */   }
/*     */   
/*     */   static boolean isNormal(double d) {
/*  75 */     return (Math.getExponent(d) >= -1022);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static double scaleNormalize(double x) {
/*  83 */     long significand = Double.doubleToRawLongBits(x) & 0xFFFFFFFFFFFFFL;
/*  84 */     return Double.longBitsToDouble(significand | 0x3FF0000000000000L);
/*     */   }
/*     */ 
/*     */   
/*     */   static double bigToDouble(BigInteger x) {
/*  89 */     BigInteger absX = x.abs();
/*  90 */     int exponent = absX.bitLength() - 1;
/*     */     
/*  92 */     if (exponent < 63)
/*  93 */       return x.longValue(); 
/*  94 */     if (exponent > 1023) {
/*  95 */       return x.signum() * Double.POSITIVE_INFINITY;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 106 */     int shift = exponent - 52 - 1;
/* 107 */     long twiceSignifFloor = absX.shiftRight(shift).longValue();
/* 108 */     long signifFloor = twiceSignifFloor >> 1L;
/* 109 */     signifFloor &= 0xFFFFFFFFFFFFFL;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 117 */     boolean increment = ((twiceSignifFloor & 0x1L) != 0L && ((signifFloor & 0x1L) != 0L || absX.getLowestSetBit() < shift));
/* 118 */     long signifRounded = increment ? (signifFloor + 1L) : signifFloor;
/* 119 */     long bits = (exponent + 1023) << 52L;
/* 120 */     bits += signifRounded;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 127 */     bits |= x.signum() & Long.MIN_VALUE;
/* 128 */     return Double.longBitsToDouble(bits);
/*     */   }
/*     */ 
/*     */   
/*     */   static double ensureNonNegative(double value) {
/* 133 */     Preconditions.checkArgument(!Double.isNaN(value));
/* 134 */     if (value > 0.0D) {
/* 135 */       return value;
/*     */     }
/* 137 */     return 0.0D;
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\google\common\math\DoubleUtils.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */