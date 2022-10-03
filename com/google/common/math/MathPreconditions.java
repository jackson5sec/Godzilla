/*     */ package com.google.common.math;
/*     */ 
/*     */ import com.google.common.annotations.GwtCompatible;
/*     */ import com.google.errorprone.annotations.CanIgnoreReturnValue;
/*     */ import java.math.BigInteger;
/*     */ import java.math.RoundingMode;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
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
/*     */ @CanIgnoreReturnValue
/*     */ final class MathPreconditions
/*     */ {
/*     */   static int checkPositive(String role, int x) {
/*  32 */     if (x <= 0) {
/*  33 */       throw new IllegalArgumentException(role + " (" + x + ") must be > 0");
/*     */     }
/*  35 */     return x;
/*     */   }
/*     */   
/*     */   static long checkPositive(String role, long x) {
/*  39 */     if (x <= 0L) {
/*  40 */       throw new IllegalArgumentException(role + " (" + x + ") must be > 0");
/*     */     }
/*  42 */     return x;
/*     */   }
/*     */   
/*     */   static BigInteger checkPositive(String role, BigInteger x) {
/*  46 */     if (x.signum() <= 0) {
/*  47 */       throw new IllegalArgumentException(role + " (" + x + ") must be > 0");
/*     */     }
/*  49 */     return x;
/*     */   }
/*     */   
/*     */   static int checkNonNegative(String role, int x) {
/*  53 */     if (x < 0) {
/*  54 */       throw new IllegalArgumentException(role + " (" + x + ") must be >= 0");
/*     */     }
/*  56 */     return x;
/*     */   }
/*     */   
/*     */   static long checkNonNegative(String role, long x) {
/*  60 */     if (x < 0L) {
/*  61 */       throw new IllegalArgumentException(role + " (" + x + ") must be >= 0");
/*     */     }
/*  63 */     return x;
/*     */   }
/*     */   
/*     */   static BigInteger checkNonNegative(String role, BigInteger x) {
/*  67 */     if (x.signum() < 0) {
/*  68 */       throw new IllegalArgumentException(role + " (" + x + ") must be >= 0");
/*     */     }
/*  70 */     return x;
/*     */   }
/*     */   
/*     */   static double checkNonNegative(String role, double x) {
/*  74 */     if (x < 0.0D) {
/*  75 */       throw new IllegalArgumentException(role + " (" + x + ") must be >= 0");
/*     */     }
/*  77 */     return x;
/*     */   }
/*     */   
/*     */   static void checkRoundingUnnecessary(boolean condition) {
/*  81 */     if (!condition) {
/*  82 */       throw new ArithmeticException("mode was UNNECESSARY, but rounding was necessary");
/*     */     }
/*     */   }
/*     */   
/*     */   static void checkInRangeForRoundingInputs(boolean condition, double input, RoundingMode mode) {
/*  87 */     if (!condition) {
/*  88 */       throw new ArithmeticException("rounded value is out of range for input " + input + " and rounding mode " + mode);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   static void checkNoOverflow(boolean condition, String methodName, int a, int b) {
/*  94 */     if (!condition) {
/*  95 */       throw new ArithmeticException("overflow: " + methodName + "(" + a + ", " + b + ")");
/*     */     }
/*     */   }
/*     */   
/*     */   static void checkNoOverflow(boolean condition, String methodName, long a, long b) {
/* 100 */     if (!condition)
/* 101 */       throw new ArithmeticException("overflow: " + methodName + "(" + a + ", " + b + ")"); 
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\google\common\math\MathPreconditions.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */