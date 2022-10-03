/*     */ package org.mozilla.javascript.v8dtoa;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class FastDtoa
/*     */ {
/*     */   static final int kFastDtoaMaximalLength = 17;
/*     */   static final int minimal_target_exponent = -60;
/*     */   static final int maximal_target_exponent = -32;
/*     */   static final int kTen4 = 10000;
/*     */   static final int kTen5 = 100000;
/*     */   static final int kTen6 = 1000000;
/*     */   static final int kTen7 = 10000000;
/*     */   static final int kTen8 = 100000000;
/*     */   static final int kTen9 = 1000000000;
/*     */   
/*     */   static boolean roundWeed(FastDtoaBuilder buffer, long distance_too_high_w, long unsafe_interval, long rest, long ten_kappa, long unit) {
/*  69 */     long small_distance = distance_too_high_w - unit;
/*  70 */     long big_distance = distance_too_high_w + unit;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 141 */     while (rest < small_distance && unsafe_interval - rest >= ten_kappa && (rest + ten_kappa < small_distance || small_distance - rest >= rest + ten_kappa - small_distance)) {
/*     */ 
/*     */       
/* 144 */       buffer.decreaseLast();
/* 145 */       rest += ten_kappa;
/*     */     } 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 151 */     if (rest < big_distance && unsafe_interval - rest >= ten_kappa && (rest + ten_kappa < big_distance || big_distance - rest > rest + ten_kappa - big_distance))
/*     */     {
/*     */ 
/*     */       
/* 155 */       return false;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 163 */     return (2L * unit <= rest && rest <= unsafe_interval - 4L * unit);
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
/*     */   static long biggestPowerTen(int number, int number_bits) {
/* 183 */     switch (number_bits)
/*     */     { case 30:
/*     */       case 31:
/*     */       case 32:
/* 187 */         if (1000000000 <= number)
/* 188 */         { int i = 1000000000;
/* 189 */           int j = 9;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */           
/* 276 */           return i << 32L | 0xFFFFFFFFL & j; } case 27: case 28: case 29: if (100000000 <= number) { int i = 100000000; int j = 8; return i << 32L | 0xFFFFFFFFL & j; } case 24: case 25: case 26: if (10000000 <= number) { int i = 10000000; int j = 7; return i << 32L | 0xFFFFFFFFL & j; } case 20: case 21: case 22: case 23: if (1000000 <= number) { int i = 1000000; int j = 6; return i << 32L | 0xFFFFFFFFL & j; } case 17: case 18: case 19: if (100000 <= number) { int i = 100000; int j = 5; return i << 32L | 0xFFFFFFFFL & j; } case 14: case 15: case 16: if (10000 <= number) { int i = 10000; int j = 4; return i << 32L | 0xFFFFFFFFL & j; } case 10: case 11: case 12: case 13: if (1000 <= number) { int i = 1000; int j = 3; return i << 32L | 0xFFFFFFFFL & j; } case 7: case 8: case 9: if (100 <= number) { int i = 100; int j = 2; return i << 32L | 0xFFFFFFFFL & j; } case 4: case 5: case 6: if (10 <= number) { int i = 10; int j = 1; return i << 32L | 0xFFFFFFFFL & j; } case 1: case 2: case 3: if (1 <= number) { int i = 1; int j = 0; return i << 32L | 0xFFFFFFFFL & j; } case 0: power = 0; exponent = -1; return power << 32L | 0xFFFFFFFFL & exponent; }  int power = 0; int exponent = 0; return power << 32L | 0xFFFFFFFFL & exponent;
/*     */   }
/*     */ 
/*     */   
/*     */   private static boolean uint64_lte(long a, long b) {
/* 281 */     if (a != b) { if ((((a < b) ? 1 : 0) ^ ((a < 0L) ? 1 : 0) ^ ((b < 0L) ? 1 : 0)) != 0); return false; }
/*     */   
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static boolean digitGen(DiyFp low, DiyFp w, DiyFp high, FastDtoaBuilder buffer, int mk) {
/* 331 */     assert low.e() == w.e() && w.e() == high.e();
/* 332 */     assert uint64_lte(low.f() + 1L, high.f() - 1L);
/* 333 */     assert -60 <= w.e() && w.e() <= -32;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 345 */     long unit = 1L;
/* 346 */     DiyFp too_low = new DiyFp(low.f() - unit, low.e());
/* 347 */     DiyFp too_high = new DiyFp(high.f() + unit, high.e());
/*     */ 
/*     */     
/* 350 */     DiyFp unsafe_interval = DiyFp.minus(too_high, too_low);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 358 */     DiyFp one = new DiyFp(1L << -w.e(), w.e());
/*     */     
/* 360 */     int integrals = (int)(too_high.f() >>> -one.e() & 0xFFFFFFFFL);
/*     */     
/* 362 */     long fractionals = too_high.f() & one.f() - 1L;
/* 363 */     long result = biggestPowerTen(integrals, 64 - -one.e());
/* 364 */     int divider = (int)(result >>> 32L & 0xFFFFFFFFL);
/* 365 */     int divider_exponent = (int)(result & 0xFFFFFFFFL);
/* 366 */     int kappa = divider_exponent + 1;
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 371 */     while (kappa > 0) {
/* 372 */       int digit = integrals / divider;
/* 373 */       buffer.append((char)(48 + digit));
/* 374 */       integrals %= divider;
/* 375 */       kappa--;
/*     */ 
/*     */       
/* 378 */       long rest = (integrals << -one.e()) + fractionals;
/*     */ 
/*     */ 
/*     */       
/* 382 */       if (rest < unsafe_interval.f()) {
/*     */ 
/*     */         
/* 385 */         buffer.point = buffer.end - mk + kappa;
/* 386 */         return roundWeed(buffer, DiyFp.minus(too_high, w).f(), unsafe_interval.f(), rest, divider << -one.e(), unit);
/*     */       } 
/*     */ 
/*     */       
/* 390 */       divider /= 10;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     while (true) {
/* 408 */       fractionals *= 5L;
/* 409 */       unit *= 5L;
/* 410 */       unsafe_interval.setF(unsafe_interval.f() * 5L);
/* 411 */       unsafe_interval.setE(unsafe_interval.e() + 1);
/* 412 */       one.setF(one.f() >>> 1L);
/* 413 */       one.setE(one.e() + 1);
/*     */       
/* 415 */       int digit = (int)(fractionals >>> -one.e() & 0xFFFFFFFFL);
/* 416 */       buffer.append((char)(48 + digit));
/* 417 */       fractionals &= one.f() - 1L;
/* 418 */       kappa--;
/* 419 */       if (fractionals < unsafe_interval.f()) {
/* 420 */         buffer.point = buffer.end - mk + kappa;
/* 421 */         return roundWeed(buffer, DiyFp.minus(too_high, w).f() * unit, unsafe_interval.f(), fractionals, one.f(), unit);
/*     */       } 
/*     */     } 
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
/*     */   static boolean grisu3(double v, FastDtoaBuilder buffer) {
/* 440 */     long bits = Double.doubleToLongBits(v);
/* 441 */     DiyFp w = DoubleHelper.asNormalizedDiyFp(bits);
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 446 */     DiyFp boundary_minus = new DiyFp(), boundary_plus = new DiyFp();
/* 447 */     DoubleHelper.normalizedBoundaries(bits, boundary_minus, boundary_plus);
/* 448 */     assert boundary_plus.e() == w.e();
/* 449 */     DiyFp ten_mk = new DiyFp();
/* 450 */     int mk = CachedPowers.getCachedPower(w.e() + 64, -60, -32, ten_mk);
/*     */     
/* 452 */     assert -60 <= w.e() + ten_mk.e() + 64 && -32 >= w.e() + ten_mk.e() + 64;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 465 */     DiyFp scaled_w = DiyFp.times(w, ten_mk);
/* 466 */     assert scaled_w.e() == boundary_plus.e() + ten_mk.e() + 64;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 473 */     DiyFp scaled_boundary_minus = DiyFp.times(boundary_minus, ten_mk);
/* 474 */     DiyFp scaled_boundary_plus = DiyFp.times(boundary_plus, ten_mk);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 482 */     return digitGen(scaled_boundary_minus, scaled_w, scaled_boundary_plus, buffer, mk);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static boolean dtoa(double v, FastDtoaBuilder buffer) {
/* 488 */     assert v > 0.0D;
/* 489 */     assert !Double.isNaN(v);
/* 490 */     assert !Double.isInfinite(v);
/*     */     
/* 492 */     return grisu3(v, buffer);
/*     */   }
/*     */   
/*     */   public static String numberToString(double v) {
/* 496 */     FastDtoaBuilder buffer = new FastDtoaBuilder();
/* 497 */     return numberToString(v, buffer) ? buffer.format() : null;
/*     */   }
/*     */   
/*     */   public static boolean numberToString(double v, FastDtoaBuilder buffer) {
/* 501 */     buffer.reset();
/* 502 */     if (v < 0.0D) {
/* 503 */       buffer.append('-');
/* 504 */       v = -v;
/*     */     } 
/* 506 */     return dtoa(v, buffer);
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\mozilla\javascript\v8dtoa\FastDtoa.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */