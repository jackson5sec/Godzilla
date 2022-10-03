/*     */ package com.google.common.math;
/*     */ 
/*     */ import com.google.common.annotations.GwtCompatible;
/*     */ import com.google.common.annotations.GwtIncompatible;
/*     */ import com.google.common.annotations.VisibleForTesting;
/*     */ import com.google.common.base.Preconditions;
/*     */ import com.google.common.primitives.Booleans;
/*     */ import com.google.errorprone.annotations.CanIgnoreReturnValue;
/*     */ import java.math.BigInteger;
/*     */ import java.math.RoundingMode;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @GwtCompatible(emulated = true)
/*     */ public final class DoubleMath
/*     */ {
/*     */   private static final double MIN_INT_AS_DOUBLE = -2.147483648E9D;
/*     */   private static final double MAX_INT_AS_DOUBLE = 2.147483647E9D;
/*     */   private static final double MIN_LONG_AS_DOUBLE = -9.223372036854776E18D;
/*     */   private static final double MAX_LONG_AS_DOUBLE_PLUS_ONE = 9.223372036854776E18D;
/*     */   
/*     */   @GwtIncompatible
/*     */   static double roundIntermediate(double x, RoundingMode mode) {
/*     */     double z;
/*  56 */     if (!DoubleUtils.isFinite(x)) {
/*  57 */       throw new ArithmeticException("input is infinite or NaN");
/*     */     }
/*  59 */     switch (mode) {
/*     */       case UNNECESSARY:
/*  61 */         MathPreconditions.checkRoundingUnnecessary(isMathematicalInteger(x));
/*  62 */         return x;
/*     */       
/*     */       case FLOOR:
/*  65 */         if (x >= 0.0D || isMathematicalInteger(x)) {
/*  66 */           return x;
/*     */         }
/*  68 */         return ((long)x - 1L);
/*     */ 
/*     */       
/*     */       case CEILING:
/*  72 */         if (x <= 0.0D || isMathematicalInteger(x)) {
/*  73 */           return x;
/*     */         }
/*  75 */         return ((long)x + 1L);
/*     */ 
/*     */       
/*     */       case DOWN:
/*  79 */         return x;
/*     */       
/*     */       case UP:
/*  82 */         if (isMathematicalInteger(x)) {
/*  83 */           return x;
/*     */         }
/*  85 */         return ((long)x + ((x > 0.0D) ? 1L : -1L));
/*     */ 
/*     */       
/*     */       case HALF_EVEN:
/*  89 */         return Math.rint(x);
/*     */ 
/*     */       
/*     */       case HALF_UP:
/*  93 */         z = Math.rint(x);
/*  94 */         if (Math.abs(x - z) == 0.5D) {
/*  95 */           return x + Math.copySign(0.5D, x);
/*     */         }
/*  97 */         return z;
/*     */ 
/*     */ 
/*     */ 
/*     */       
/*     */       case HALF_DOWN:
/* 103 */         z = Math.rint(x);
/* 104 */         if (Math.abs(x - z) == 0.5D) {
/* 105 */           return x;
/*     */         }
/* 107 */         return z;
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/* 112 */     throw new AssertionError();
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
/*     */   @GwtIncompatible
/*     */   public static int roundToInt(double x, RoundingMode mode) {
/* 132 */     double z = roundIntermediate(x, mode);
/* 133 */     MathPreconditions.checkInRangeForRoundingInputs(((z > -2.147483649E9D)) & ((z < 2.147483648E9D)), x, mode);
/*     */     
/* 135 */     return (int)z;
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
/*     */   @GwtIncompatible
/*     */   public static long roundToLong(double x, RoundingMode mode) {
/* 157 */     double z = roundIntermediate(x, mode);
/* 158 */     MathPreconditions.checkInRangeForRoundingInputs(((-9.223372036854776E18D - z < 1.0D)) & ((z < 9.223372036854776E18D)), x, mode);
/*     */     
/* 160 */     return (long)z;
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
/*     */   @GwtIncompatible
/*     */   public static BigInteger roundToBigInteger(double x, RoundingMode mode) {
/* 184 */     x = roundIntermediate(x, mode);
/* 185 */     if ((((-9.223372036854776E18D - x < 1.0D) ? 1 : 0) & ((x < 9.223372036854776E18D) ? 1 : 0)) != 0) {
/* 186 */       return BigInteger.valueOf((long)x);
/*     */     }
/* 188 */     int exponent = Math.getExponent(x);
/* 189 */     long significand = DoubleUtils.getSignificand(x);
/* 190 */     BigInteger result = BigInteger.valueOf(significand).shiftLeft(exponent - 52);
/* 191 */     return (x < 0.0D) ? result.negate() : result;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @GwtIncompatible
/*     */   public static boolean isPowerOfTwo(double x) {
/* 200 */     if (x > 0.0D && DoubleUtils.isFinite(x)) {
/* 201 */       long significand = DoubleUtils.getSignificand(x);
/* 202 */       return ((significand & significand - 1L) == 0L);
/*     */     } 
/* 204 */     return false;
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
/*     */   public static double log2(double x) {
/* 224 */     return Math.log(x) / LN_2;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @GwtIncompatible
/*     */   public static int log2(double x, RoundingMode mode) {
/*     */     boolean bool1;
/*     */     int j, i;
/*     */     boolean increment;
/*     */     double xScaled;
/* 239 */     Preconditions.checkArgument((x > 0.0D && DoubleUtils.isFinite(x)), "x must be positive and finite");
/* 240 */     int exponent = Math.getExponent(x);
/* 241 */     if (!DoubleUtils.isNormal(x)) {
/* 242 */       return log2(x * 4.503599627370496E15D, mode) - 52;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/* 247 */     switch (mode) {
/*     */       case UNNECESSARY:
/* 249 */         MathPreconditions.checkRoundingUnnecessary(isPowerOfTwo(x));
/*     */       
/*     */       case FLOOR:
/* 252 */         bool1 = false;
/*     */         break;
/*     */       case CEILING:
/* 255 */         bool1 = !isPowerOfTwo(x);
/*     */         break;
/*     */       case DOWN:
/* 258 */         j = ((exponent < 0) ? 1 : 0) & (!isPowerOfTwo(x) ? 1 : 0);
/*     */         break;
/*     */       case UP:
/* 261 */         i = ((exponent >= 0) ? 1 : 0) & (!isPowerOfTwo(x) ? 1 : 0);
/*     */         break;
/*     */       case HALF_EVEN:
/*     */       case HALF_UP:
/*     */       case HALF_DOWN:
/* 266 */         xScaled = DoubleUtils.scaleNormalize(x);
/*     */ 
/*     */         
/* 269 */         increment = (xScaled * xScaled > 2.0D);
/*     */         break;
/*     */       default:
/* 272 */         throw new AssertionError();
/*     */     } 
/* 274 */     return increment ? (exponent + 1) : exponent;
/*     */   }
/*     */   
/* 277 */   private static final double LN_2 = Math.log(2.0D);
/*     */ 
/*     */   
/*     */   @VisibleForTesting
/*     */   static final int MAX_FACTORIAL = 170;
/*     */ 
/*     */ 
/*     */   
/*     */   @GwtIncompatible
/*     */   public static boolean isMathematicalInteger(double x) {
/* 287 */     return (DoubleUtils.isFinite(x) && (x == 0.0D || 52 - 
/*     */       
/* 289 */       Long.numberOfTrailingZeros(DoubleUtils.getSignificand(x)) <= Math.getExponent(x)));
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
/*     */   public static double factorial(int n) {
/* 302 */     MathPreconditions.checkNonNegative("n", n);
/* 303 */     if (n > 170) {
/* 304 */       return Double.POSITIVE_INFINITY;
/*     */     }
/*     */ 
/*     */     
/* 308 */     double accum = 1.0D;
/* 309 */     for (int i = 1 + (n & 0xFFFFFFF0); i <= n; i++) {
/* 310 */       accum *= i;
/*     */     }
/* 312 */     return accum * everySixteenthFactorial[n >> 4];
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @VisibleForTesting
/* 319 */   static final double[] everySixteenthFactorial = new double[] { 1.0D, 2.0922789888E13D, 2.631308369336935E35D, 1.2413915592536073E61D, 1.2688693218588417E89D, 7.156945704626381E118D, 9.916779348709496E149D, 1.974506857221074E182D, 3.856204823625804E215D, 5.5502938327393044E249D, 4.7147236359920616E284D };
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static boolean fuzzyEquals(double a, double b, double tolerance) {
/* 360 */     MathPreconditions.checkNonNegative("tolerance", tolerance);
/* 361 */     return (Math.copySign(a - b, 1.0D) <= tolerance || a == b || (
/*     */ 
/*     */       
/* 364 */       Double.isNaN(a) && Double.isNaN(b)));
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
/*     */   public static int fuzzyCompare(double a, double b, double tolerance) {
/* 381 */     if (fuzzyEquals(a, b, tolerance))
/* 382 */       return 0; 
/* 383 */     if (a < b)
/* 384 */       return -1; 
/* 385 */     if (a > b) {
/* 386 */       return 1;
/*     */     }
/* 388 */     return Booleans.compare(Double.isNaN(a), Double.isNaN(b));
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
/*     */   @Deprecated
/*     */   @GwtIncompatible
/*     */   public static double mean(double... values) {
/* 408 */     Preconditions.checkArgument((values.length > 0), "Cannot take mean of 0 values");
/* 409 */     long count = 1L;
/* 410 */     double mean = checkFinite(values[0]);
/* 411 */     for (int index = 1; index < values.length; index++) {
/* 412 */       checkFinite(values[index]);
/* 413 */       count++;
/*     */       
/* 415 */       mean += (values[index] - mean) / count;
/*     */     } 
/* 417 */     return mean;
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
/*     */   @Deprecated
/*     */   public static double mean(int... values) {
/* 434 */     Preconditions.checkArgument((values.length > 0), "Cannot take mean of 0 values");
/*     */ 
/*     */ 
/*     */     
/* 438 */     long sum = 0L;
/* 439 */     for (int index = 0; index < values.length; index++) {
/* 440 */       sum += values[index];
/*     */     }
/* 442 */     return sum / values.length;
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
/*     */   @Deprecated
/*     */   public static double mean(long... values) {
/* 460 */     Preconditions.checkArgument((values.length > 0), "Cannot take mean of 0 values");
/* 461 */     long count = 1L;
/* 462 */     double mean = values[0];
/* 463 */     for (int index = 1; index < values.length; index++) {
/* 464 */       count++;
/*     */       
/* 466 */       mean += (values[index] - mean) / count;
/*     */     } 
/* 468 */     return mean;
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
/*     */   @Deprecated
/*     */   @GwtIncompatible
/*     */   public static double mean(Iterable<? extends Number> values) {
/* 488 */     return mean(values.iterator());
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
/*     */   @Deprecated
/*     */   @GwtIncompatible
/*     */   public static double mean(Iterator<? extends Number> values) {
/* 508 */     Preconditions.checkArgument(values.hasNext(), "Cannot take mean of 0 values");
/* 509 */     long count = 1L;
/* 510 */     double mean = checkFinite(((Number)values.next()).doubleValue());
/* 511 */     while (values.hasNext()) {
/* 512 */       double value = checkFinite(((Number)values.next()).doubleValue());
/* 513 */       count++;
/*     */       
/* 515 */       mean += (value - mean) / count;
/*     */     } 
/* 517 */     return mean;
/*     */   }
/*     */   
/*     */   @GwtIncompatible
/*     */   @CanIgnoreReturnValue
/*     */   private static double checkFinite(double argument) {
/* 523 */     Preconditions.checkArgument(DoubleUtils.isFinite(argument));
/* 524 */     return argument;
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\google\common\math\DoubleMath.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */