/*      */ package com.google.common.math;
/*      */ 
/*      */ import com.google.common.annotations.Beta;
/*      */ import com.google.common.annotations.GwtCompatible;
/*      */ import com.google.common.annotations.GwtIncompatible;
/*      */ import com.google.common.annotations.VisibleForTesting;
/*      */ import com.google.common.base.Preconditions;
/*      */ import com.google.common.primitives.UnsignedLongs;
/*      */ import java.math.RoundingMode;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ @GwtCompatible(emulated = true)
/*      */ public final class LongMath
/*      */ {
/*      */   @VisibleForTesting
/*      */   static final long MAX_SIGNED_POWER_OF_TWO = 4611686018427387904L;
/*      */   @VisibleForTesting
/*      */   static final long MAX_POWER_OF_SQRT2_UNSIGNED = -5402926248376769404L;
/*      */   
/*      */   @Beta
/*      */   public static long ceilingPowerOfTwo(long x) {
/*   67 */     MathPreconditions.checkPositive("x", x);
/*   68 */     if (x > 4611686018427387904L) {
/*   69 */       throw new ArithmeticException("ceilingPowerOfTwo(" + x + ") is not representable as a long");
/*      */     }
/*   71 */     return 1L << -Long.numberOfLeadingZeros(x - 1L);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Beta
/*      */   public static long floorPowerOfTwo(long x) {
/*   83 */     MathPreconditions.checkPositive("x", x);
/*      */ 
/*      */ 
/*      */     
/*   87 */     return 1L << 63 - Long.numberOfLeadingZeros(x);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static boolean isPowerOfTwo(long x) {
/*   97 */     return ((x > 0L)) & (((x & x - 1L) == 0L));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @VisibleForTesting
/*      */   static int lessThanBranchFree(long x, long y) {
/*  108 */     return (int)((x - y ^ 0xFFFFFFFFFFFFFFFFL ^ 0xFFFFFFFFFFFFFFFFL) >>> 63L);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static int log2(long x, RoundingMode mode) {
/*      */     int leadingZeros;
/*      */     long cmp;
/*      */     int logFloor;
/*  121 */     MathPreconditions.checkPositive("x", x);
/*  122 */     switch (mode) {
/*      */       case UNNECESSARY:
/*  124 */         MathPreconditions.checkRoundingUnnecessary(isPowerOfTwo(x));
/*      */       
/*      */       case DOWN:
/*      */       case FLOOR:
/*  128 */         return 63 - Long.numberOfLeadingZeros(x);
/*      */       
/*      */       case UP:
/*      */       case CEILING:
/*  132 */         return 64 - Long.numberOfLeadingZeros(x - 1L);
/*      */ 
/*      */       
/*      */       case HALF_DOWN:
/*      */       case HALF_UP:
/*      */       case HALF_EVEN:
/*  138 */         leadingZeros = Long.numberOfLeadingZeros(x);
/*  139 */         cmp = -5402926248376769404L >>> leadingZeros;
/*      */         
/*  141 */         logFloor = 63 - leadingZeros;
/*  142 */         return logFloor + lessThanBranchFree(cmp, x);
/*      */     } 
/*      */     
/*  145 */     throw new AssertionError("impossible");
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @GwtIncompatible
/*      */   public static int log10(long x, RoundingMode mode) {
/*  163 */     MathPreconditions.checkPositive("x", x);
/*  164 */     int logFloor = log10Floor(x);
/*  165 */     long floorPow = powersOf10[logFloor];
/*  166 */     switch (mode) {
/*      */       case UNNECESSARY:
/*  168 */         MathPreconditions.checkRoundingUnnecessary((x == floorPow));
/*      */       
/*      */       case DOWN:
/*      */       case FLOOR:
/*  172 */         return logFloor;
/*      */       case UP:
/*      */       case CEILING:
/*  175 */         return logFloor + lessThanBranchFree(floorPow, x);
/*      */       
/*      */       case HALF_DOWN:
/*      */       case HALF_UP:
/*      */       case HALF_EVEN:
/*  180 */         return logFloor + lessThanBranchFree(halfPowersOf10[logFloor], x);
/*      */     } 
/*  182 */     throw new AssertionError();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @GwtIncompatible
/*      */   static int log10Floor(long x) {
/*  195 */     int y = maxLog10ForLeadingZeros[Long.numberOfLeadingZeros(x)];
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  200 */     return y - lessThanBranchFree(x, powersOf10[y]);
/*      */   }
/*      */ 
/*      */   
/*      */   @VisibleForTesting
/*  205 */   static final byte[] maxLog10ForLeadingZeros = new byte[] { 19, 18, 18, 18, 18, 17, 17, 17, 16, 16, 16, 15, 15, 15, 15, 14, 14, 14, 13, 13, 13, 12, 12, 12, 12, 11, 11, 11, 10, 10, 10, 9, 9, 9, 9, 8, 8, 8, 7, 7, 7, 6, 6, 6, 6, 5, 5, 5, 4, 4, 4, 3, 3, 3, 3, 2, 2, 2, 1, 1, 1, 0, 0, 0 };
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @GwtIncompatible
/*      */   @VisibleForTesting
/*  213 */   static final long[] powersOf10 = new long[] { 1L, 10L, 100L, 1000L, 10000L, 100000L, 1000000L, 10000000L, 100000000L, 1000000000L, 10000000000L, 100000000000L, 1000000000000L, 10000000000000L, 100000000000000L, 1000000000000000L, 10000000000000000L, 100000000000000000L, 1000000000000000000L };
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @GwtIncompatible
/*      */   @VisibleForTesting
/*  238 */   static final long[] halfPowersOf10 = new long[] { 3L, 31L, 316L, 3162L, 31622L, 316227L, 3162277L, 31622776L, 316227766L, 3162277660L, 31622776601L, 316227766016L, 3162277660168L, 31622776601683L, 316227766016837L, 3162277660168379L, 31622776601683793L, 316227766016837933L, 3162277660168379331L };
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @VisibleForTesting
/*      */   static final long FLOOR_SQRT_MAX_LONG = 3037000499L;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @GwtIncompatible
/*      */   public static long pow(long b, int k) {
/*  269 */     MathPreconditions.checkNonNegative("exponent", k);
/*  270 */     if (-2L <= b && b <= 2L) {
/*  271 */       switch ((int)b) {
/*      */         case 0:
/*  273 */           return (k == 0) ? 1L : 0L;
/*      */         case 1:
/*  275 */           return 1L;
/*      */         case -1:
/*  277 */           return ((k & 0x1) == 0) ? 1L : -1L;
/*      */         case 2:
/*  279 */           return (k < 64) ? (1L << k) : 0L;
/*      */         case -2:
/*  281 */           if (k < 64) {
/*  282 */             return ((k & 0x1) == 0) ? (1L << k) : -(1L << k);
/*      */           }
/*  284 */           return 0L;
/*      */       } 
/*      */       
/*  287 */       throw new AssertionError();
/*      */     } 
/*      */     
/*  290 */     for (long accum = 1L;; k >>= 1) {
/*  291 */       switch (k) {
/*      */         case 0:
/*  293 */           return accum;
/*      */         case 1:
/*  295 */           return accum * b;
/*      */       } 
/*  297 */       accum *= ((k & 0x1) == 0) ? 1L : b;
/*  298 */       b *= b;
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @GwtIncompatible
/*      */   public static long sqrt(long x, RoundingMode mode) {
/*      */     long sqrtFloor, halfSquare;
/*  313 */     MathPreconditions.checkNonNegative("x", x);
/*  314 */     if (fitsInInt(x)) {
/*  315 */       return IntMath.sqrt((int)x, mode);
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  332 */     long guess = (long)Math.sqrt(x);
/*      */     
/*  334 */     long guessSquared = guess * guess;
/*      */ 
/*      */     
/*  337 */     switch (mode) {
/*      */       case UNNECESSARY:
/*  339 */         MathPreconditions.checkRoundingUnnecessary((guessSquared == x));
/*  340 */         return guess;
/*      */       case DOWN:
/*      */       case FLOOR:
/*  343 */         if (x < guessSquared) {
/*  344 */           return guess - 1L;
/*      */         }
/*  346 */         return guess;
/*      */       case UP:
/*      */       case CEILING:
/*  349 */         if (x > guessSquared) {
/*  350 */           return guess + 1L;
/*      */         }
/*  352 */         return guess;
/*      */       case HALF_DOWN:
/*      */       case HALF_UP:
/*      */       case HALF_EVEN:
/*  356 */         sqrtFloor = guess - ((x < guessSquared) ? 1L : 0L);
/*  357 */         halfSquare = sqrtFloor * sqrtFloor + sqrtFloor;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */         
/*  369 */         return sqrtFloor + lessThanBranchFree(halfSquare, x);
/*      */     } 
/*  371 */     throw new AssertionError();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @GwtIncompatible
/*      */   public static long divide(long p, long q, RoundingMode mode) {
/*      */     boolean increment;
/*      */     long absRem, cmpRemToHalfDivisor;
/*  385 */     Preconditions.checkNotNull(mode);
/*  386 */     long div = p / q;
/*  387 */     long rem = p - q * div;
/*      */     
/*  389 */     if (rem == 0L) {
/*  390 */       return div;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  400 */     int signum = 0x1 | (int)((p ^ q) >> 63L);
/*      */     
/*  402 */     switch (mode) {
/*      */       case UNNECESSARY:
/*  404 */         MathPreconditions.checkRoundingUnnecessary((rem == 0L));
/*      */       
/*      */       case DOWN:
/*  407 */         increment = false;
/*      */         break;
/*      */       case UP:
/*  410 */         increment = true;
/*      */         break;
/*      */       case CEILING:
/*  413 */         increment = (signum > 0);
/*      */         break;
/*      */       case FLOOR:
/*  416 */         increment = (signum < 0);
/*      */         break;
/*      */       case HALF_DOWN:
/*      */       case HALF_UP:
/*      */       case HALF_EVEN:
/*  421 */         absRem = Math.abs(rem);
/*  422 */         cmpRemToHalfDivisor = absRem - Math.abs(q) - absRem;
/*      */ 
/*      */         
/*  425 */         if (cmpRemToHalfDivisor == 0L) {
/*  426 */           int i = ((mode == RoundingMode.HALF_UP) ? 1 : 0) | ((mode == RoundingMode.HALF_EVEN) ? 1 : 0) & (((div & 0x1L) != 0L) ? 1 : 0); break;
/*      */         } 
/*  428 */         increment = (cmpRemToHalfDivisor > 0L);
/*      */         break;
/*      */       
/*      */       default:
/*  432 */         throw new AssertionError();
/*      */     } 
/*  434 */     return increment ? (div + signum) : div;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @GwtIncompatible
/*      */   public static int mod(long x, int m) {
/*  458 */     return (int)mod(x, m);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @GwtIncompatible
/*      */   public static long mod(long x, long m) {
/*  481 */     if (m <= 0L) {
/*  482 */       throw new ArithmeticException("Modulus must be positive");
/*      */     }
/*  484 */     long result = x % m;
/*  485 */     return (result >= 0L) ? result : (result + m);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static long gcd(long a, long b) {
/*  500 */     MathPreconditions.checkNonNegative("a", a);
/*  501 */     MathPreconditions.checkNonNegative("b", b);
/*  502 */     if (a == 0L)
/*      */     {
/*      */       
/*  505 */       return b; } 
/*  506 */     if (b == 0L) {
/*  507 */       return a;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  513 */     int aTwos = Long.numberOfTrailingZeros(a);
/*  514 */     a >>= aTwos;
/*  515 */     int bTwos = Long.numberOfTrailingZeros(b);
/*  516 */     b >>= bTwos;
/*  517 */     while (a != b) {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*  525 */       long delta = a - b;
/*      */       
/*  527 */       long minDeltaOrZero = delta & delta >> 63L;
/*      */ 
/*      */       
/*  530 */       a = delta - minDeltaOrZero - minDeltaOrZero;
/*      */ 
/*      */       
/*  533 */       b += minDeltaOrZero;
/*  534 */       a >>= Long.numberOfTrailingZeros(a);
/*      */     } 
/*  536 */     return a << Math.min(aTwos, bTwos);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @GwtIncompatible
/*      */   public static long checkedAdd(long a, long b) {
/*  546 */     long result = a + b;
/*  547 */     MathPreconditions.checkNoOverflow((((a ^ b) < 0L)) | (((a ^ result) >= 0L)), "checkedAdd", a, b);
/*  548 */     return result;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @GwtIncompatible
/*      */   public static long checkedSubtract(long a, long b) {
/*  558 */     long result = a - b;
/*  559 */     MathPreconditions.checkNoOverflow((((a ^ b) >= 0L)) | (((a ^ result) >= 0L)), "checkedSubtract", a, b);
/*  560 */     return result;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static long checkedMultiply(long a, long b) {
/*  574 */     int leadingZeros = Long.numberOfLeadingZeros(a) + Long.numberOfLeadingZeros(a ^ 0xFFFFFFFFFFFFFFFFL) + Long.numberOfLeadingZeros(b) + Long.numberOfLeadingZeros(b ^ 0xFFFFFFFFFFFFFFFFL);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  585 */     if (leadingZeros > 65) {
/*  586 */       return a * b;
/*      */     }
/*  588 */     MathPreconditions.checkNoOverflow((leadingZeros >= 64), "checkedMultiply", a, b);
/*  589 */     MathPreconditions.checkNoOverflow(((a >= 0L)) | ((b != Long.MIN_VALUE)), "checkedMultiply", a, b);
/*  590 */     long result = a * b;
/*  591 */     MathPreconditions.checkNoOverflow((a == 0L || result / a == b), "checkedMultiply", a, b);
/*  592 */     return result;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @GwtIncompatible
/*      */   public static long checkedPow(long b, int k) {
/*  603 */     MathPreconditions.checkNonNegative("exponent", k);
/*  604 */     if ((((b >= -2L) ? 1 : 0) & ((b <= 2L) ? 1 : 0)) != 0) {
/*  605 */       switch ((int)b) {
/*      */         case 0:
/*  607 */           return (k == 0) ? 1L : 0L;
/*      */         case 1:
/*  609 */           return 1L;
/*      */         case -1:
/*  611 */           return ((k & 0x1) == 0) ? 1L : -1L;
/*      */         case 2:
/*  613 */           MathPreconditions.checkNoOverflow((k < 63), "checkedPow", b, k);
/*  614 */           return 1L << k;
/*      */         case -2:
/*  616 */           MathPreconditions.checkNoOverflow((k < 64), "checkedPow", b, k);
/*  617 */           return ((k & 0x1) == 0) ? (1L << k) : (-1L << k);
/*      */       } 
/*  619 */       throw new AssertionError();
/*      */     } 
/*      */     
/*  622 */     long accum = 1L;
/*      */     while (true) {
/*  624 */       switch (k) {
/*      */         case 0:
/*  626 */           return accum;
/*      */         case 1:
/*  628 */           return checkedMultiply(accum, b);
/*      */       } 
/*  630 */       if ((k & 0x1) != 0) {
/*  631 */         accum = checkedMultiply(accum, b);
/*      */       }
/*  633 */       k >>= 1;
/*  634 */       if (k > 0) {
/*  635 */         MathPreconditions.checkNoOverflow((-3037000499L <= b && b <= 3037000499L), "checkedPow", b, k);
/*      */         
/*  637 */         b *= b;
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Beta
/*      */   public static long saturatedAdd(long a, long b) {
/*  651 */     long naiveSum = a + b;
/*  652 */     if (((((a ^ b) < 0L) ? 1 : 0) | (((a ^ naiveSum) >= 0L) ? 1 : 0)) != 0)
/*      */     {
/*      */       
/*  655 */       return naiveSum;
/*      */     }
/*      */     
/*  658 */     return Long.MAX_VALUE + (naiveSum >>> 63L ^ 0x1L);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Beta
/*      */   public static long saturatedSubtract(long a, long b) {
/*  669 */     long naiveDifference = a - b;
/*  670 */     if (((((a ^ b) >= 0L) ? 1 : 0) | (((a ^ naiveDifference) >= 0L) ? 1 : 0)) != 0)
/*      */     {
/*      */       
/*  673 */       return naiveDifference;
/*      */     }
/*      */     
/*  676 */     return Long.MAX_VALUE + (naiveDifference >>> 63L ^ 0x1L);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Beta
/*      */   public static long saturatedMultiply(long a, long b) {
/*  692 */     int leadingZeros = Long.numberOfLeadingZeros(a) + Long.numberOfLeadingZeros(a ^ 0xFFFFFFFFFFFFFFFFL) + Long.numberOfLeadingZeros(b) + Long.numberOfLeadingZeros(b ^ 0xFFFFFFFFFFFFFFFFL);
/*  693 */     if (leadingZeros > 65) {
/*  694 */       return a * b;
/*      */     }
/*      */     
/*  697 */     long limit = Long.MAX_VALUE + ((a ^ b) >>> 63L);
/*  698 */     if ((((leadingZeros < 64) ? 1 : 0) | ((a < 0L) ? 1 : 0) & ((b == Long.MIN_VALUE) ? 1 : 0)) != 0)
/*      */     {
/*  700 */       return limit;
/*      */     }
/*  702 */     long result = a * b;
/*  703 */     if (a == 0L || result / a == b) {
/*  704 */       return result;
/*      */     }
/*  706 */     return limit;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Beta
/*      */   public static long saturatedPow(long b, int k) {
/*  717 */     MathPreconditions.checkNonNegative("exponent", k);
/*  718 */     if ((((b >= -2L) ? 1 : 0) & ((b <= 2L) ? 1 : 0)) != 0) {
/*  719 */       switch ((int)b) {
/*      */         case 0:
/*  721 */           return (k == 0) ? 1L : 0L;
/*      */         case 1:
/*  723 */           return 1L;
/*      */         case -1:
/*  725 */           return ((k & 0x1) == 0) ? 1L : -1L;
/*      */         case 2:
/*  727 */           if (k >= 63) {
/*  728 */             return Long.MAX_VALUE;
/*      */           }
/*  730 */           return 1L << k;
/*      */         case -2:
/*  732 */           if (k >= 64) {
/*  733 */             return Long.MAX_VALUE + (k & 0x1);
/*      */           }
/*  735 */           return ((k & 0x1) == 0) ? (1L << k) : (-1L << k);
/*      */       } 
/*  737 */       throw new AssertionError();
/*      */     } 
/*      */     
/*  740 */     long accum = 1L;
/*      */     
/*  742 */     long limit = Long.MAX_VALUE + (b >>> 63L & (k & 0x1));
/*      */     while (true) {
/*  744 */       switch (k) {
/*      */         case 0:
/*  746 */           return accum;
/*      */         case 1:
/*  748 */           return saturatedMultiply(accum, b);
/*      */       } 
/*  750 */       if ((k & 0x1) != 0) {
/*  751 */         accum = saturatedMultiply(accum, b);
/*      */       }
/*  753 */       k >>= 1;
/*  754 */       if (k > 0) {
/*  755 */         if ((((-3037000499L > b) ? 1 : 0) | ((b > 3037000499L) ? 1 : 0)) != 0) {
/*  756 */           return limit;
/*      */         }
/*  758 */         b *= b;
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @GwtIncompatible
/*      */   public static long factorial(int n) {
/*  774 */     MathPreconditions.checkNonNegative("n", n);
/*  775 */     return (n < factorials.length) ? factorials[n] : Long.MAX_VALUE;
/*      */   }
/*      */   
/*  778 */   static final long[] factorials = new long[] { 1L, 1L, 2L, 6L, 24L, 120L, 720L, 5040L, 40320L, 362880L, 3628800L, 39916800L, 479001600L, 6227020800L, 87178291200L, 1307674368000L, 20922789888000L, 355687428096000L, 6402373705728000L, 121645100408832000L, 2432902008176640000L };
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static long binomial(int n, int k) {
/*  809 */     MathPreconditions.checkNonNegative("n", n);
/*  810 */     MathPreconditions.checkNonNegative("k", k);
/*  811 */     Preconditions.checkArgument((k <= n), "k (%s) > n (%s)", k, n);
/*  812 */     if (k > n >> 1) {
/*  813 */       k = n - k;
/*      */     }
/*  815 */     switch (k) {
/*      */       case 0:
/*  817 */         return 1L;
/*      */       case 1:
/*  819 */         return n;
/*      */     } 
/*  821 */     if (n < factorials.length)
/*  822 */       return factorials[n] / factorials[k] * factorials[n - k]; 
/*  823 */     if (k >= biggestBinomials.length || n > biggestBinomials[k])
/*  824 */       return Long.MAX_VALUE; 
/*  825 */     if (k < biggestSimpleBinomials.length && n <= biggestSimpleBinomials[k]) {
/*      */       
/*  827 */       long l = n--;
/*  828 */       for (int j = 2; j <= k; n--, j++) {
/*  829 */         l *= n;
/*  830 */         l /= j;
/*      */       } 
/*  832 */       return l;
/*      */     } 
/*  834 */     int nBits = log2(n, RoundingMode.CEILING);
/*      */     
/*  836 */     long result = 1L;
/*  837 */     long numerator = n--;
/*  838 */     long denominator = 1L;
/*      */     
/*  840 */     int numeratorBits = nBits;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  848 */     for (int i = 2; i <= k; i++, n--) {
/*  849 */       if (numeratorBits + nBits < 63) {
/*      */         
/*  851 */         numerator *= n;
/*  852 */         denominator *= i;
/*  853 */         numeratorBits += nBits;
/*      */       }
/*      */       else {
/*      */         
/*  857 */         result = multiplyFraction(result, numerator, denominator);
/*  858 */         numerator = n;
/*  859 */         denominator = i;
/*  860 */         numeratorBits = nBits;
/*      */       } 
/*      */     } 
/*  863 */     return multiplyFraction(result, numerator, denominator);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   static long multiplyFraction(long x, long numerator, long denominator) {
/*  870 */     if (x == 1L) {
/*  871 */       return numerator / denominator;
/*      */     }
/*  873 */     long commonDivisor = gcd(x, denominator);
/*  874 */     x /= commonDivisor;
/*  875 */     denominator /= commonDivisor;
/*      */ 
/*      */     
/*  878 */     return x * numerator / denominator;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  884 */   static final int[] biggestBinomials = new int[] { Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE, 3810779, 121977, 16175, 4337, 1733, 887, 534, 361, 265, 206, 169, 143, 125, 111, 101, 94, 88, 83, 79, 76, 74, 72, 70, 69, 68, 67, 67, 66, 66, 66, 66 };
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @VisibleForTesting
/*  926 */   static final int[] biggestSimpleBinomials = new int[] { Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE, 2642246, 86251, 11724, 3218, 1313, 684, 419, 287, 214, 169, 139, 119, 105, 95, 87, 81, 76, 73, 70, 68, 66, 64, 63, 62, 62, 61, 61, 61 };
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static final int SIEVE_30 = -545925251;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   static boolean fitsInInt(long x) {
/*  963 */     return ((int)x == x);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static long mean(long x, long y) {
/*  976 */     return (x & y) + ((x ^ y) >> 1L);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @GwtIncompatible
/*      */   @Beta
/*      */   public static boolean isPrime(long n) {
/* 1003 */     if (n < 2L) {
/* 1004 */       MathPreconditions.checkNonNegative("n", n);
/* 1005 */       return false;
/*      */     } 
/* 1007 */     if (n == 2L || n == 3L || n == 5L || n == 7L || n == 11L || n == 13L) {
/* 1008 */       return true;
/*      */     }
/*      */     
/* 1011 */     if ((0xDF75D77D & 1 << (int)(n % 30L)) != 0) {
/* 1012 */       return false;
/*      */     }
/* 1014 */     if (n % 7L == 0L || n % 11L == 0L || n % 13L == 0L) {
/* 1015 */       return false;
/*      */     }
/* 1017 */     if (n < 289L) {
/* 1018 */       return true;
/*      */     }
/*      */     
/* 1021 */     for (long[] baseSet : millerRabinBaseSets) {
/* 1022 */       if (n <= baseSet[0]) {
/* 1023 */         for (int i = 1; i < baseSet.length; i++) {
/* 1024 */           if (!MillerRabinTester.test(baseSet[i], n)) {
/* 1025 */             return false;
/*      */           }
/*      */         } 
/* 1028 */         return true;
/*      */       } 
/*      */     } 
/* 1031 */     throw new AssertionError();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/* 1041 */   private static final long[][] millerRabinBaseSets = new long[][] { { 291830L, 126401071349994536L }, { 885594168L, 725270293939359937L, 3569819667048198375L }, { 273919523040L, 15L, 7363882082L, 992620450144556L }, { 47636622961200L, 2L, 2570940L, 211991001L, 3749873356L }, { 7999252175582850L, 2L, 4130806001517L, 149795463772692060L, 186635894390467037L, 3967304179347715805L }, { 585226005592931976L, 2L, 123635709730000L, 9233062284813009L, 43835965440333360L, 761179012939631437L, 1263739024124850375L }, { Long.MAX_VALUE, 2L, 325L, 9375L, 28178L, 450775L, 9780504L, 1795265022L } };
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private enum MillerRabinTester
/*      */   {
/* 1068 */     SMALL
/*      */     {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*      */       long mulMod(long a, long b, long m)
/*      */       {
/* 1077 */         return a * b % m;
/*      */       }
/*      */ 
/*      */       
/*      */       long squareMod(long a, long m) {
/* 1082 */         return a * a % m;
/*      */       }
/*      */     },
/*      */     
/* 1086 */     LARGE
/*      */     {
/*      */       private long plusMod(long a, long b, long m) {
/* 1089 */         return (a >= m - b) ? (a + b - m) : (a + b);
/*      */       }
/*      */ 
/*      */       
/*      */       private long times2ToThe32Mod(long a, long m) {
/* 1094 */         int remainingPowersOf2 = 32;
/*      */         while (true) {
/* 1096 */           int shift = Math.min(remainingPowersOf2, Long.numberOfLeadingZeros(a));
/*      */ 
/*      */           
/* 1099 */           a = UnsignedLongs.remainder(a << shift, m);
/* 1100 */           remainingPowersOf2 -= shift;
/* 1101 */           if (remainingPowersOf2 <= 0)
/* 1102 */             return a; 
/*      */         } 
/*      */       }
/*      */       
/*      */       long mulMod(long a, long b, long m) {
/* 1107 */         long aHi = a >>> 32L;
/* 1108 */         long bHi = b >>> 32L;
/* 1109 */         long aLo = a & 0xFFFFFFFFL;
/* 1110 */         long bLo = b & 0xFFFFFFFFL;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */         
/* 1120 */         long result = times2ToThe32Mod(aHi * bHi, m);
/* 1121 */         result += aHi * bLo;
/* 1122 */         if (result < 0L) {
/* 1123 */           result = UnsignedLongs.remainder(result, m);
/*      */         }
/*      */         
/* 1126 */         result += aLo * bHi;
/* 1127 */         result = times2ToThe32Mod(result, m);
/* 1128 */         return plusMod(result, UnsignedLongs.remainder(aLo * bLo, m), m);
/*      */       }
/*      */ 
/*      */       
/*      */       long squareMod(long a, long m) {
/* 1133 */         long aHi = a >>> 32L;
/* 1134 */         long aLo = a & 0xFFFFFFFFL;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */         
/* 1143 */         long result = times2ToThe32Mod(aHi * aHi, m);
/* 1144 */         long hiLo = aHi * aLo * 2L;
/* 1145 */         if (hiLo < 0L) {
/* 1146 */           hiLo = UnsignedLongs.remainder(hiLo, m);
/*      */         }
/*      */         
/* 1149 */         result += hiLo;
/* 1150 */         result = times2ToThe32Mod(result, m);
/* 1151 */         return plusMod(result, UnsignedLongs.remainder(aLo * aLo, m), m);
/*      */       }
/*      */     };
/*      */ 
/*      */ 
/*      */     
/*      */     static boolean test(long base, long n) {
/* 1158 */       return ((n <= 3037000499L) ? SMALL : LARGE).testWitness(base, n);
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     private long powMod(long a, long p, long m) {
/* 1169 */       long res = 1L;
/* 1170 */       for (; p != 0L; p >>= 1L) {
/* 1171 */         if ((p & 0x1L) != 0L) {
/* 1172 */           res = mulMod(res, a, m);
/*      */         }
/* 1174 */         a = squareMod(a, m);
/*      */       } 
/* 1176 */       return res;
/*      */     }
/*      */ 
/*      */     
/*      */     private boolean testWitness(long base, long n) {
/* 1181 */       int r = Long.numberOfTrailingZeros(n - 1L);
/* 1182 */       long d = n - 1L >> r;
/* 1183 */       base %= n;
/* 1184 */       if (base == 0L) {
/* 1185 */         return true;
/*      */       }
/*      */       
/* 1188 */       long a = powMod(base, d, n);
/*      */ 
/*      */ 
/*      */       
/* 1192 */       if (a == 1L) {
/* 1193 */         return true;
/*      */       }
/* 1195 */       int j = 0;
/* 1196 */       while (a != n - 1L) {
/* 1197 */         if (++j == r) {
/* 1198 */           return false;
/*      */         }
/* 1200 */         a = squareMod(a, n);
/*      */       } 
/* 1202 */       return true;
/*      */     }
/*      */     
/*      */     abstract long mulMod(long param1Long1, long param1Long2, long param1Long3);
/*      */     
/*      */     abstract long squareMod(long param1Long1, long param1Long2);
/*      */   }
/*      */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\google\common\math\LongMath.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */