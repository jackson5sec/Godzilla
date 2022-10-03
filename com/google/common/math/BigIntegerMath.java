/*     */ package com.google.common.math;
/*     */ 
/*     */ import com.google.common.annotations.Beta;
/*     */ import com.google.common.annotations.GwtCompatible;
/*     */ import com.google.common.annotations.GwtIncompatible;
/*     */ import com.google.common.annotations.VisibleForTesting;
/*     */ import com.google.common.base.Preconditions;
/*     */ import java.math.BigDecimal;
/*     */ import java.math.BigInteger;
/*     */ import java.math.RoundingMode;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
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
/*     */ public final class BigIntegerMath
/*     */ {
/*     */   @VisibleForTesting
/*     */   static final int SQRT2_PRECOMPUTE_THRESHOLD = 256;
/*     */   
/*     */   @Beta
/*     */   public static BigInteger ceilingPowerOfTwo(BigInteger x) {
/*  59 */     return BigInteger.ZERO.setBit(log2(x, RoundingMode.CEILING));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Beta
/*     */   public static BigInteger floorPowerOfTwo(BigInteger x) {
/*  71 */     return BigInteger.ZERO.setBit(log2(x, RoundingMode.FLOOR));
/*     */   }
/*     */ 
/*     */   
/*     */   public static boolean isPowerOfTwo(BigInteger x) {
/*  76 */     Preconditions.checkNotNull(x);
/*  77 */     return (x.signum() > 0 && x.getLowestSetBit() == x.bitLength() - 1);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static int log2(BigInteger x, RoundingMode mode) {
/*     */     BigInteger x2;
/*     */     int logX2Floor;
/*  90 */     MathPreconditions.checkPositive("x", (BigInteger)Preconditions.checkNotNull(x));
/*  91 */     int logFloor = x.bitLength() - 1;
/*  92 */     switch (mode) {
/*     */       case UNNECESSARY:
/*  94 */         MathPreconditions.checkRoundingUnnecessary(isPowerOfTwo(x));
/*     */       case DOWN:
/*     */       case FLOOR:
/*  97 */         return logFloor;
/*     */       
/*     */       case UP:
/*     */       case CEILING:
/* 101 */         return isPowerOfTwo(x) ? logFloor : (logFloor + 1);
/*     */       
/*     */       case HALF_DOWN:
/*     */       case HALF_UP:
/*     */       case HALF_EVEN:
/* 106 */         if (logFloor < 256) {
/*     */           
/* 108 */           BigInteger halfPower = SQRT2_PRECOMPUTED_BITS.shiftRight(256 - logFloor);
/* 109 */           if (x.compareTo(halfPower) <= 0) {
/* 110 */             return logFloor;
/*     */           }
/* 112 */           return logFloor + 1;
/*     */         } 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 119 */         x2 = x.pow(2);
/* 120 */         logX2Floor = x2.bitLength() - 1;
/* 121 */         return (logX2Floor < 2 * logFloor + 1) ? logFloor : (logFloor + 1);
/*     */     } 
/*     */     
/* 124 */     throw new AssertionError();
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
/*     */   @VisibleForTesting
/* 136 */   static final BigInteger SQRT2_PRECOMPUTED_BITS = new BigInteger("16a09e667f3bcc908b2fb1366ea957d3e3adec17512775099da2f590b0667322a", 16);
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
/*     */   public static int log10(BigInteger x, RoundingMode mode) {
/*     */     BigInteger x2, halfPowerSquared;
/* 149 */     MathPreconditions.checkPositive("x", x);
/* 150 */     if (fitsInLong(x)) {
/* 151 */       return LongMath.log10(x.longValue(), mode);
/*     */     }
/*     */     
/* 154 */     int approxLog10 = (int)(log2(x, RoundingMode.FLOOR) * LN_2 / LN_10);
/* 155 */     BigInteger approxPow = BigInteger.TEN.pow(approxLog10);
/* 156 */     int approxCmp = approxPow.compareTo(x);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 163 */     if (approxCmp > 0) {
/*     */ 
/*     */       
/*     */       do {
/*     */ 
/*     */ 
/*     */         
/* 170 */         approxLog10--;
/* 171 */         approxPow = approxPow.divide(BigInteger.TEN);
/* 172 */         approxCmp = approxPow.compareTo(x);
/* 173 */       } while (approxCmp > 0);
/*     */     } else {
/* 175 */       BigInteger nextPow = BigInteger.TEN.multiply(approxPow);
/* 176 */       int nextCmp = nextPow.compareTo(x);
/* 177 */       while (nextCmp <= 0) {
/* 178 */         approxLog10++;
/* 179 */         approxPow = nextPow;
/* 180 */         approxCmp = nextCmp;
/* 181 */         nextPow = BigInteger.TEN.multiply(approxPow);
/* 182 */         nextCmp = nextPow.compareTo(x);
/*     */       } 
/*     */     } 
/*     */     
/* 186 */     int floorLog = approxLog10;
/* 187 */     BigInteger floorPow = approxPow;
/* 188 */     int floorCmp = approxCmp;
/*     */     
/* 190 */     switch (mode) {
/*     */       case UNNECESSARY:
/* 192 */         MathPreconditions.checkRoundingUnnecessary((floorCmp == 0));
/*     */       
/*     */       case DOWN:
/*     */       case FLOOR:
/* 196 */         return floorLog;
/*     */       
/*     */       case UP:
/*     */       case CEILING:
/* 200 */         return floorPow.equals(x) ? floorLog : (floorLog + 1);
/*     */ 
/*     */       
/*     */       case HALF_DOWN:
/*     */       case HALF_UP:
/*     */       case HALF_EVEN:
/* 206 */         x2 = x.pow(2);
/* 207 */         halfPowerSquared = floorPow.pow(2).multiply(BigInteger.TEN);
/* 208 */         return (x2.compareTo(halfPowerSquared) <= 0) ? floorLog : (floorLog + 1);
/*     */     } 
/* 210 */     throw new AssertionError();
/*     */   }
/*     */ 
/*     */   
/* 214 */   private static final double LN_10 = Math.log(10.0D);
/* 215 */   private static final double LN_2 = Math.log(2.0D);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @GwtIncompatible
/*     */   public static BigInteger sqrt(BigInteger x, RoundingMode mode) {
/*     */     int sqrtFloorInt;
/*     */     boolean sqrtFloorIsExact;
/*     */     BigInteger halfSquare;
/* 227 */     MathPreconditions.checkNonNegative("x", x);
/* 228 */     if (fitsInLong(x)) {
/* 229 */       return BigInteger.valueOf(LongMath.sqrt(x.longValue(), mode));
/*     */     }
/* 231 */     BigInteger sqrtFloor = sqrtFloor(x);
/* 232 */     switch (mode) {
/*     */       case UNNECESSARY:
/* 234 */         MathPreconditions.checkRoundingUnnecessary(sqrtFloor.pow(2).equals(x));
/*     */       case DOWN:
/*     */       case FLOOR:
/* 237 */         return sqrtFloor;
/*     */       case UP:
/*     */       case CEILING:
/* 240 */         sqrtFloorInt = sqrtFloor.intValue();
/*     */ 
/*     */         
/* 243 */         sqrtFloorIsExact = (sqrtFloorInt * sqrtFloorInt == x.intValue() && sqrtFloor.pow(2).equals(x));
/* 244 */         return sqrtFloorIsExact ? sqrtFloor : sqrtFloor.add(BigInteger.ONE);
/*     */       case HALF_DOWN:
/*     */       case HALF_UP:
/*     */       case HALF_EVEN:
/* 248 */         halfSquare = sqrtFloor.pow(2).add(sqrtFloor);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 254 */         return (halfSquare.compareTo(x) >= 0) ? sqrtFloor : sqrtFloor.add(BigInteger.ONE);
/*     */     } 
/* 256 */     throw new AssertionError();
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
/*     */   @GwtIncompatible
/*     */   private static BigInteger sqrtFloor(BigInteger x) {
/*     */     BigInteger sqrt0;
/* 282 */     int log2 = log2(x, RoundingMode.FLOOR);
/* 283 */     if (log2 < 1023) {
/* 284 */       sqrt0 = sqrtApproxWithDoubles(x);
/*     */     } else {
/* 286 */       int shift = log2 - 52 & 0xFFFFFFFE;
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 291 */       sqrt0 = sqrtApproxWithDoubles(x.shiftRight(shift)).shiftLeft(shift >> 1);
/*     */     } 
/* 293 */     BigInteger sqrt1 = sqrt0.add(x.divide(sqrt0)).shiftRight(1);
/* 294 */     if (sqrt0.equals(sqrt1)) {
/* 295 */       return sqrt0;
/*     */     }
/*     */     while (true) {
/* 298 */       sqrt0 = sqrt1;
/* 299 */       sqrt1 = sqrt0.add(x.divide(sqrt0)).shiftRight(1);
/* 300 */       if (sqrt1.compareTo(sqrt0) >= 0)
/* 301 */         return sqrt0; 
/*     */     } 
/*     */   }
/*     */   @GwtIncompatible
/*     */   private static BigInteger sqrtApproxWithDoubles(BigInteger x) {
/* 306 */     return DoubleMath.roundToBigInteger(Math.sqrt(DoubleUtils.bigToDouble(x)), RoundingMode.HALF_EVEN);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @GwtIncompatible
/*     */   public static BigInteger divide(BigInteger p, BigInteger q, RoundingMode mode) {
/* 318 */     BigDecimal pDec = new BigDecimal(p);
/* 319 */     BigDecimal qDec = new BigDecimal(q);
/* 320 */     return pDec.divide(qDec, 0, mode).toBigIntegerExact();
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
/*     */   public static BigInteger factorial(int n) {
/* 336 */     MathPreconditions.checkNonNegative("n", n);
/*     */ 
/*     */     
/* 339 */     if (n < LongMath.factorials.length) {
/* 340 */       return BigInteger.valueOf(LongMath.factorials[n]);
/*     */     }
/*     */ 
/*     */     
/* 344 */     int approxSize = IntMath.divide(n * IntMath.log2(n, RoundingMode.CEILING), 64, RoundingMode.CEILING);
/* 345 */     ArrayList<BigInteger> bignums = new ArrayList<>(approxSize);
/*     */ 
/*     */     
/* 348 */     int startingNumber = LongMath.factorials.length;
/* 349 */     long product = LongMath.factorials[startingNumber - 1];
/*     */     
/* 351 */     int shift = Long.numberOfTrailingZeros(product);
/* 352 */     product >>= shift;
/*     */ 
/*     */     
/* 355 */     int productBits = LongMath.log2(product, RoundingMode.FLOOR) + 1;
/* 356 */     int bits = LongMath.log2(startingNumber, RoundingMode.FLOOR) + 1;
/*     */     
/* 358 */     int nextPowerOfTwo = 1 << bits - 1;
/*     */     
/*     */     long num;
/* 361 */     for (num = startingNumber; num <= n; num++) {
/*     */       
/* 363 */       if ((num & nextPowerOfTwo) != 0L) {
/* 364 */         nextPowerOfTwo <<= 1;
/* 365 */         bits++;
/*     */       } 
/*     */       
/* 368 */       int tz = Long.numberOfTrailingZeros(num);
/* 369 */       long normalizedNum = num >> tz;
/* 370 */       shift += tz;
/*     */       
/* 372 */       int normalizedBits = bits - tz;
/*     */       
/* 374 */       if (normalizedBits + productBits >= 64) {
/* 375 */         bignums.add(BigInteger.valueOf(product));
/* 376 */         product = 1L;
/* 377 */         productBits = 0;
/*     */       } 
/* 379 */       product *= normalizedNum;
/* 380 */       productBits = LongMath.log2(product, RoundingMode.FLOOR) + 1;
/*     */     } 
/*     */     
/* 383 */     if (product > 1L) {
/* 384 */       bignums.add(BigInteger.valueOf(product));
/*     */     }
/*     */     
/* 387 */     return listProduct(bignums).shiftLeft(shift);
/*     */   }
/*     */   
/*     */   static BigInteger listProduct(List<BigInteger> nums) {
/* 391 */     return listProduct(nums, 0, nums.size());
/*     */   }
/*     */   
/*     */   static BigInteger listProduct(List<BigInteger> nums, int start, int end) {
/* 395 */     switch (end - start) {
/*     */       case 0:
/* 397 */         return BigInteger.ONE;
/*     */       case 1:
/* 399 */         return nums.get(start);
/*     */       case 2:
/* 401 */         return ((BigInteger)nums.get(start)).multiply(nums.get(start + 1));
/*     */       case 3:
/* 403 */         return ((BigInteger)nums.get(start)).multiply(nums.get(start + 1)).multiply(nums.get(start + 2));
/*     */     } 
/*     */     
/* 406 */     int m = end + start >>> 1;
/* 407 */     return listProduct(nums, start, m).multiply(listProduct(nums, m, end));
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
/*     */   public static BigInteger binomial(int n, int k) {
/* 420 */     MathPreconditions.checkNonNegative("n", n);
/* 421 */     MathPreconditions.checkNonNegative("k", k);
/* 422 */     Preconditions.checkArgument((k <= n), "k (%s) > n (%s)", k, n);
/* 423 */     if (k > n >> 1) {
/* 424 */       k = n - k;
/*     */     }
/* 426 */     if (k < LongMath.biggestBinomials.length && n <= LongMath.biggestBinomials[k]) {
/* 427 */       return BigInteger.valueOf(LongMath.binomial(n, k));
/*     */     }
/*     */     
/* 430 */     BigInteger accum = BigInteger.ONE;
/*     */     
/* 432 */     long numeratorAccum = n;
/* 433 */     long denominatorAccum = 1L;
/*     */     
/* 435 */     int bits = LongMath.log2(n, RoundingMode.CEILING);
/*     */     
/* 437 */     int numeratorBits = bits;
/*     */     
/* 439 */     for (int i = 1; i < k; i++) {
/* 440 */       int p = n - i;
/* 441 */       int q = i + 1;
/*     */ 
/*     */ 
/*     */       
/* 445 */       if (numeratorBits + bits >= 63) {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 451 */         accum = accum.multiply(BigInteger.valueOf(numeratorAccum)).divide(BigInteger.valueOf(denominatorAccum));
/* 452 */         numeratorAccum = p;
/* 453 */         denominatorAccum = q;
/* 454 */         numeratorBits = bits;
/*     */       } else {
/*     */         
/* 457 */         numeratorAccum *= p;
/* 458 */         denominatorAccum *= q;
/* 459 */         numeratorBits += bits;
/*     */       } 
/*     */     } 
/* 462 */     return accum
/* 463 */       .multiply(BigInteger.valueOf(numeratorAccum))
/* 464 */       .divide(BigInteger.valueOf(denominatorAccum));
/*     */   }
/*     */ 
/*     */   
/*     */   @GwtIncompatible
/*     */   static boolean fitsInLong(BigInteger x) {
/* 470 */     return (x.bitLength() <= 63);
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\google\common\math\BigIntegerMath.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */