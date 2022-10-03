/*      */ package org.mozilla.javascript;
/*      */ 
/*      */ import java.math.BigInteger;
/*      */ 
/*      */ class DToA {
/*      */   static final int DTOSTR_STANDARD = 0;
/*      */   static final int DTOSTR_STANDARD_EXPONENTIAL = 1;
/*      */   static final int DTOSTR_FIXED = 2;
/*      */   static final int DTOSTR_EXPONENTIAL = 3;
/*      */   static final int DTOSTR_PRECISION = 4;
/*      */   private static final int Frac_mask = 1048575;
/*      */   private static final int Exp_shift = 20;
/*      */   private static final int Exp_msk1 = 1048576;
/*      */   private static final long Frac_maskL = 4503599627370495L;
/*      */   private static final int Exp_shiftL = 52;
/*      */   private static final long Exp_msk1L = 4503599627370496L;
/*      */   private static final int Bias = 1023;
/*      */   private static final int P = 53;
/*      */   private static final int Exp_shift1 = 20;
/*      */   private static final int Exp_mask = 2146435072;
/*      */   private static final int Exp_mask_shifted = 2047;
/*      */   private static final int Bndry_mask = 1048575;
/*      */   private static final int Log2P = 1;
/*      */   private static final int Sign_bit = -2147483648;
/*      */   private static final int Exp_11 = 1072693248;
/*      */   private static final int Ten_pmax = 22;
/*      */   private static final int Quick_max = 14;
/*      */   private static final int Bletch = 16;
/*      */   private static final int Frac_mask1 = 1048575;
/*      */   private static final int Int_max = 14;
/*      */   private static final int n_bigtens = 5;
/*      */   
/*      */   private static char BASEDIGIT(int digit) {
/*   34 */     return (char)((digit >= 10) ? (87 + digit) : (48 + digit));
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
/*   72 */   private static final double[] tens = new double[] { 1.0D, 10.0D, 100.0D, 1000.0D, 10000.0D, 100000.0D, 1000000.0D, 1.0E7D, 1.0E8D, 1.0E9D, 1.0E10D, 1.0E11D, 1.0E12D, 1.0E13D, 1.0E14D, 1.0E15D, 1.0E16D, 1.0E17D, 1.0E18D, 1.0E19D, 1.0E20D, 1.0E21D, 1.0E22D };
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*   78 */   private static final double[] bigtens = new double[] { 1.0E16D, 1.0E32D, 1.0E64D, 1.0E128D, 1.0E256D };
/*      */ 
/*      */ 
/*      */   
/*      */   private static int lo0bits(int y) {
/*   83 */     int x = y;
/*      */     
/*   85 */     if ((x & 0x7) != 0) {
/*   86 */       if ((x & 0x1) != 0)
/*   87 */         return 0; 
/*   88 */       if ((x & 0x2) != 0) {
/*   89 */         return 1;
/*      */       }
/*   91 */       return 2;
/*      */     } 
/*   93 */     int k = 0;
/*   94 */     if ((x & 0xFFFF) == 0) {
/*   95 */       k = 16;
/*   96 */       x >>>= 16;
/*      */     } 
/*   98 */     if ((x & 0xFF) == 0) {
/*   99 */       k += 8;
/*  100 */       x >>>= 8;
/*      */     } 
/*  102 */     if ((x & 0xF) == 0) {
/*  103 */       k += 4;
/*  104 */       x >>>= 4;
/*      */     } 
/*  106 */     if ((x & 0x3) == 0) {
/*  107 */       k += 2;
/*  108 */       x >>>= 2;
/*      */     } 
/*  110 */     if ((x & 0x1) == 0) {
/*  111 */       k++;
/*  112 */       x >>>= 1;
/*  113 */       if ((x & 0x1) == 0)
/*  114 */         return 32; 
/*      */     } 
/*  116 */     return k;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private static int hi0bits(int x) {
/*  122 */     int k = 0;
/*      */     
/*  124 */     if ((x & 0xFFFF0000) == 0) {
/*  125 */       k = 16;
/*  126 */       x <<= 16;
/*      */     } 
/*  128 */     if ((x & 0xFF000000) == 0) {
/*  129 */       k += 8;
/*  130 */       x <<= 8;
/*      */     } 
/*  132 */     if ((x & 0xF0000000) == 0) {
/*  133 */       k += 4;
/*  134 */       x <<= 4;
/*      */     } 
/*  136 */     if ((x & 0xC0000000) == 0) {
/*  137 */       k += 2;
/*  138 */       x <<= 2;
/*      */     } 
/*  140 */     if ((x & Integer.MIN_VALUE) == 0) {
/*  141 */       k++;
/*  142 */       if ((x & 0x40000000) == 0)
/*  143 */         return 32; 
/*      */     } 
/*  145 */     return k;
/*      */   }
/*      */ 
/*      */   
/*      */   private static void stuffBits(byte[] bits, int offset, int val) {
/*  150 */     bits[offset] = (byte)(val >> 24);
/*  151 */     bits[offset + 1] = (byte)(val >> 16);
/*  152 */     bits[offset + 2] = (byte)(val >> 8);
/*  153 */     bits[offset + 3] = (byte)val;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static BigInteger d2b(double d, int[] e, int[] bits) {
/*      */     byte[] dbl_bits;
/*      */     int i, k;
/*  163 */     long dBits = Double.doubleToLongBits(d);
/*  164 */     int d0 = (int)(dBits >>> 32L);
/*  165 */     int d1 = (int)dBits;
/*      */     
/*  167 */     int z = d0 & 0xFFFFF;
/*  168 */     d0 &= Integer.MAX_VALUE;
/*      */     int de;
/*  170 */     if ((de = d0 >>> 20) != 0)
/*  171 */       z |= 0x100000; 
/*      */     int y;
/*  173 */     if ((y = d1) != 0) {
/*  174 */       dbl_bits = new byte[8];
/*  175 */       k = lo0bits(y);
/*  176 */       y >>>= k;
/*  177 */       if (k != 0) {
/*  178 */         stuffBits(dbl_bits, 4, y | z << 32 - k);
/*  179 */         z >>= k;
/*      */       } else {
/*      */         
/*  182 */         stuffBits(dbl_bits, 4, y);
/*  183 */       }  stuffBits(dbl_bits, 0, z);
/*  184 */       i = (z != 0) ? 2 : 1;
/*      */     }
/*      */     else {
/*      */       
/*  188 */       dbl_bits = new byte[4];
/*  189 */       k = lo0bits(z);
/*  190 */       z >>>= k;
/*  191 */       stuffBits(dbl_bits, 0, z);
/*  192 */       k += 32;
/*  193 */       i = 1;
/*      */     } 
/*  195 */     if (de != 0) {
/*  196 */       e[0] = de - 1023 - 52 + k;
/*  197 */       bits[0] = 53 - k;
/*      */     } else {
/*      */       
/*  200 */       e[0] = de - 1023 - 52 + 1 + k;
/*  201 */       bits[0] = 32 * i - hi0bits(z);
/*      */     } 
/*  203 */     return new BigInteger(dbl_bits);
/*      */   }
/*      */   static String JS_dtobasestr(int base, double d) {
/*      */     boolean negative;
/*      */     String intDigits;
/*  208 */     if (2 > base || base > 36) {
/*  209 */       throw new IllegalArgumentException("Bad base: " + base);
/*      */     }
/*      */     
/*  212 */     if (Double.isNaN(d))
/*  213 */       return "NaN"; 
/*  214 */     if (Double.isInfinite(d))
/*  215 */       return (d > 0.0D) ? "Infinity" : "-Infinity"; 
/*  216 */     if (d == 0.0D)
/*      */     {
/*  218 */       return "0";
/*      */     }
/*      */ 
/*      */     
/*  222 */     if (d >= 0.0D) {
/*  223 */       negative = false;
/*      */     } else {
/*  225 */       negative = true;
/*  226 */       d = -d;
/*      */     } 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  232 */     double dfloor = Math.floor(d);
/*  233 */     long lfloor = (long)dfloor;
/*  234 */     if (lfloor == dfloor) {
/*      */       
/*  236 */       intDigits = Long.toString(negative ? -lfloor : lfloor, base);
/*      */     } else {
/*      */       
/*  239 */       long mantissa, floorBits = Double.doubleToLongBits(dfloor);
/*  240 */       int exp = (int)(floorBits >> 52L) & 0x7FF;
/*      */       
/*  242 */       if (exp == 0) {
/*  243 */         mantissa = (floorBits & 0xFFFFFFFFFFFFFL) << 1L;
/*      */       } else {
/*  245 */         mantissa = floorBits & 0xFFFFFFFFFFFFFL | 0x10000000000000L;
/*      */       } 
/*  247 */       if (negative) {
/*  248 */         mantissa = -mantissa;
/*      */       }
/*  250 */       exp -= 1075;
/*  251 */       BigInteger x = BigInteger.valueOf(mantissa);
/*  252 */       if (exp > 0) {
/*  253 */         x = x.shiftLeft(exp);
/*  254 */       } else if (exp < 0) {
/*  255 */         x = x.shiftRight(-exp);
/*      */       } 
/*  257 */       intDigits = x.toString(base);
/*      */     } 
/*      */     
/*  260 */     if (d == dfloor)
/*      */     {
/*  262 */       return intDigits;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  271 */     StringBuilder buffer = new StringBuilder();
/*  272 */     buffer.append(intDigits).append('.');
/*  273 */     double df = d - dfloor;
/*      */     
/*  275 */     long dBits = Double.doubleToLongBits(d);
/*  276 */     int word0 = (int)(dBits >> 32L);
/*  277 */     int word1 = (int)dBits;
/*      */     
/*  279 */     int[] e = new int[1];
/*  280 */     int[] bbits = new int[1];
/*      */     
/*  282 */     BigInteger b = d2b(df, e, bbits);
/*      */ 
/*      */ 
/*      */     
/*  286 */     int s2 = -(word0 >>> 20 & 0x7FF);
/*  287 */     if (s2 == 0)
/*  288 */       s2 = -1; 
/*  289 */     s2 += 1076;
/*      */ 
/*      */     
/*  292 */     BigInteger mlo = BigInteger.valueOf(1L);
/*  293 */     BigInteger mhi = mlo;
/*  294 */     if (word1 == 0 && (word0 & 0xFFFFF) == 0 && (word0 & 0x7FE00000) != 0) {
/*      */ 
/*      */ 
/*      */       
/*  298 */       s2++;
/*  299 */       mhi = BigInteger.valueOf(2L);
/*      */     } 
/*      */     
/*  302 */     b = b.shiftLeft(e[0] + s2);
/*  303 */     BigInteger s = BigInteger.valueOf(1L);
/*  304 */     s = s.shiftLeft(s2);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  310 */     BigInteger bigBase = BigInteger.valueOf(base);
/*      */     
/*  312 */     boolean done = false;
/*      */     do {
/*  314 */       b = b.multiply(bigBase);
/*  315 */       BigInteger[] divResult = b.divideAndRemainder(s);
/*  316 */       b = divResult[1];
/*  317 */       int digit = (char)divResult[0].intValue();
/*  318 */       if (mlo == mhi) {
/*  319 */         mlo = mhi = mlo.multiply(bigBase);
/*      */       } else {
/*  321 */         mlo = mlo.multiply(bigBase);
/*  322 */         mhi = mhi.multiply(bigBase);
/*      */       } 
/*      */ 
/*      */       
/*  326 */       int j = b.compareTo(mlo);
/*      */       
/*  328 */       BigInteger delta = s.subtract(mhi);
/*  329 */       int j1 = (delta.signum() <= 0) ? 1 : b.compareTo(delta);
/*      */       
/*  331 */       if (j1 == 0 && (word1 & 0x1) == 0) {
/*  332 */         if (j > 0)
/*  333 */           digit++; 
/*  334 */         done = true;
/*      */       }
/*  336 */       else if (j < 0 || (j == 0 && (word1 & 0x1) == 0)) {
/*  337 */         if (j1 > 0) {
/*      */ 
/*      */           
/*  340 */           b = b.shiftLeft(1);
/*  341 */           j1 = b.compareTo(s);
/*  342 */           if (j1 > 0)
/*      */           {
/*  344 */             digit++; } 
/*      */         } 
/*  346 */         done = true;
/*  347 */       } else if (j1 > 0) {
/*  348 */         digit++;
/*  349 */         done = true;
/*      */       } 
/*      */       
/*  352 */       buffer.append(BASEDIGIT(digit));
/*  353 */     } while (!done);
/*      */     
/*  355 */     return buffer.toString();
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
/*      */   static int word0(double d) {
/*  396 */     long dBits = Double.doubleToLongBits(d);
/*  397 */     return (int)(dBits >> 32L);
/*      */   }
/*      */ 
/*      */   
/*      */   static double setWord0(double d, int i) {
/*  402 */     long dBits = Double.doubleToLongBits(d);
/*  403 */     dBits = i << 32L | dBits & 0xFFFFFFFFL;
/*  404 */     return Double.longBitsToDouble(dBits);
/*      */   }
/*      */ 
/*      */   
/*      */   static int word1(double d) {
/*  409 */     long dBits = Double.doubleToLongBits(d);
/*  410 */     return (int)dBits;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   static BigInteger pow5mult(BigInteger b, int k) {
/*  417 */     return b.multiply(BigInteger.valueOf(5L).pow(k));
/*      */   }
/*      */ 
/*      */   
/*      */   static boolean roundOff(StringBuilder buf) {
/*  422 */     int i = buf.length();
/*  423 */     while (i != 0) {
/*  424 */       i--;
/*  425 */       char c = buf.charAt(i);
/*  426 */       if (c != '9') {
/*  427 */         buf.setCharAt(i, (char)(c + 1));
/*  428 */         buf.setLength(i + 1);
/*  429 */         return false;
/*      */       } 
/*      */     } 
/*  432 */     buf.setLength(0);
/*  433 */     return true;
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
/*      */   static int JS_dtoa(double d, int mode, boolean biasUp, int ndigits, boolean[] sign, StringBuilder buf) {
/*      */     int b2, b5, s2, s5;
/*      */     char dig;
/*      */     double d2;
/*      */     boolean denorm;
/*  487 */     int[] be = new int[1];
/*  488 */     int[] bbits = new int[1];
/*      */ 
/*      */ 
/*      */     
/*  492 */     if ((word0(d) & Integer.MIN_VALUE) != 0) {
/*      */       
/*  494 */       sign[0] = true;
/*      */       
/*  496 */       d = setWord0(d, word0(d) & Integer.MAX_VALUE);
/*      */     } else {
/*      */       
/*  499 */       sign[0] = false;
/*      */     } 
/*  501 */     if ((word0(d) & 0x7FF00000) == 2146435072) {
/*      */       
/*  503 */       buf.append((word1(d) == 0 && (word0(d) & 0xFFFFF) == 0) ? "Infinity" : "NaN");
/*  504 */       return 9999;
/*      */     } 
/*  506 */     if (d == 0.0D) {
/*      */       
/*  508 */       buf.setLength(0);
/*  509 */       buf.append('0');
/*  510 */       return 1;
/*      */     } 
/*      */     
/*  513 */     BigInteger b = d2b(d, be, bbits); int i;
/*  514 */     if ((i = word0(d) >>> 20 & 0x7FF) != 0) {
/*  515 */       d2 = setWord0(d, word0(d) & 0xFFFFF | 0x3FF00000);
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
/*  537 */       i -= 1023;
/*  538 */       denorm = false;
/*      */     }
/*      */     else {
/*      */       
/*  542 */       i = bbits[0] + be[0] + 1074;
/*  543 */       long x = (i > 32) ? (word0(d) << 64 - i | (word1(d) >>> i - 32)) : (word1(d) << 32 - i);
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*  548 */       d2 = setWord0(x, word0(x) - 32505856);
/*  549 */       i -= 1075;
/*  550 */       denorm = true;
/*      */     } 
/*      */     
/*  553 */     double ds = (d2 - 1.5D) * 0.289529654602168D + 0.1760912590558D + i * 0.301029995663981D;
/*  554 */     int k = (int)ds;
/*  555 */     if (ds < 0.0D && ds != k)
/*  556 */       k--; 
/*  557 */     boolean k_check = true;
/*  558 */     if (k >= 0 && k <= 22) {
/*  559 */       if (d < tens[k])
/*  560 */         k--; 
/*  561 */       k_check = false;
/*      */     } 
/*      */ 
/*      */     
/*  565 */     int j = bbits[0] - i - 1;
/*      */     
/*  567 */     if (j >= 0) {
/*  568 */       b2 = 0;
/*  569 */       s2 = j;
/*      */     } else {
/*      */       
/*  572 */       b2 = -j;
/*  573 */       s2 = 0;
/*      */     } 
/*  575 */     if (k >= 0) {
/*  576 */       b5 = 0;
/*  577 */       s5 = k;
/*  578 */       s2 += k;
/*      */     } else {
/*      */       
/*  581 */       b2 -= k;
/*  582 */       b5 = -k;
/*  583 */       s5 = 0;
/*      */     } 
/*      */ 
/*      */     
/*  587 */     if (mode < 0 || mode > 9)
/*  588 */       mode = 0; 
/*  589 */     boolean try_quick = true;
/*  590 */     if (mode > 5) {
/*  591 */       mode -= 4;
/*  592 */       try_quick = false;
/*      */     } 
/*  594 */     boolean leftright = true;
/*  595 */     int ilim1 = 0, ilim = ilim1;
/*  596 */     switch (mode) {
/*      */       case 0:
/*      */       case 1:
/*  599 */         ilim = ilim1 = -1;
/*  600 */         i = 18;
/*  601 */         ndigits = 0;
/*      */         break;
/*      */       case 2:
/*  604 */         leftright = false;
/*      */       
/*      */       case 4:
/*  607 */         if (ndigits <= 0)
/*  608 */           ndigits = 1; 
/*  609 */         ilim = ilim1 = i = ndigits;
/*      */         break;
/*      */       case 3:
/*  612 */         leftright = false;
/*      */       
/*      */       case 5:
/*  615 */         i = ndigits + k + 1;
/*  616 */         ilim = i;
/*  617 */         ilim1 = i - 1;
/*  618 */         if (i <= 0) {
/*  619 */           i = 1;
/*      */         }
/*      */         break;
/*      */     } 
/*      */ 
/*      */     
/*  625 */     boolean fast_failed = false;
/*  626 */     if (ilim >= 0 && ilim <= 14 && try_quick) {
/*      */ 
/*      */ 
/*      */       
/*  630 */       i = 0;
/*  631 */       d2 = d;
/*  632 */       int k0 = k;
/*  633 */       int ilim0 = ilim;
/*  634 */       int ieps = 2;
/*      */       
/*  636 */       if (k > 0) {
/*  637 */         ds = tens[k & 0xF];
/*  638 */         j = k >> 4;
/*  639 */         if ((j & 0x10) != 0) {
/*      */           
/*  641 */           j &= 0xF;
/*  642 */           d /= bigtens[4];
/*  643 */           ieps++;
/*      */         } 
/*  645 */         for (; j != 0; j >>= 1, i++) {
/*  646 */           if ((j & 0x1) != 0) {
/*  647 */             ieps++;
/*  648 */             ds *= bigtens[i];
/*      */           } 
/*  650 */         }  d /= ds;
/*      */       } else {
/*  652 */         int j1; if ((j1 = -k) != 0) {
/*  653 */           d *= tens[j1 & 0xF];
/*  654 */           for (j = j1 >> 4; j != 0; j >>= 1, i++) {
/*  655 */             if ((j & 0x1) != 0) {
/*  656 */               ieps++;
/*  657 */               d *= bigtens[i];
/*      */             } 
/*      */           } 
/*      */         } 
/*  661 */       }  if (k_check && d < 1.0D && ilim > 0) {
/*  662 */         if (ilim1 <= 0) {
/*  663 */           fast_failed = true;
/*      */         } else {
/*  665 */           ilim = ilim1;
/*  666 */           k--;
/*  667 */           d *= 10.0D;
/*  668 */           ieps++;
/*      */         } 
/*      */       }
/*      */ 
/*      */ 
/*      */       
/*  674 */       double eps = ieps * d + 7.0D;
/*  675 */       eps = setWord0(eps, word0(eps) - 54525952);
/*  676 */       if (ilim == 0) {
/*  677 */         BigInteger bigInteger1 = null, bigInteger2 = bigInteger1;
/*  678 */         d -= 5.0D;
/*  679 */         if (d > eps) {
/*  680 */           buf.append('1');
/*  681 */           k++;
/*  682 */           return k + 1;
/*      */         } 
/*  684 */         if (d < -eps) {
/*  685 */           buf.setLength(0);
/*  686 */           buf.append('0');
/*  687 */           return 1;
/*      */         } 
/*  689 */         fast_failed = true;
/*      */       } 
/*  691 */       if (!fast_failed) {
/*  692 */         fast_failed = true;
/*  693 */         if (leftright) {
/*      */ 
/*      */ 
/*      */           
/*  697 */           eps = 0.5D / tens[ilim - 1] - eps;
/*  698 */           i = 0; while (true) {
/*  699 */             long L = (long)d;
/*  700 */             d -= L;
/*  701 */             buf.append((char)(int)(48L + L));
/*  702 */             if (d < eps) {
/*  703 */               return k + 1;
/*      */             }
/*  705 */             if (1.0D - d < eps) {
/*      */               char lastCh;
/*      */               
/*      */               while (true) {
/*  709 */                 lastCh = buf.charAt(buf.length() - 1);
/*  710 */                 buf.setLength(buf.length() - 1);
/*  711 */                 if (lastCh != '9')
/*  712 */                   break;  if (buf.length() == 0) {
/*  713 */                   k++;
/*  714 */                   lastCh = '0';
/*      */                   break;
/*      */                 } 
/*      */               } 
/*  718 */               buf.append((char)(lastCh + 1));
/*  719 */               return k + 1;
/*      */             } 
/*  721 */             if (++i >= ilim)
/*      */               break; 
/*  723 */             eps *= 10.0D;
/*  724 */             d *= 10.0D;
/*      */           }
/*      */         
/*      */         } else {
/*      */           
/*  729 */           eps *= tens[ilim - 1];
/*  730 */           for (i = 1;; i++, d *= 10.0D) {
/*  731 */             long L = (long)d;
/*  732 */             d -= L;
/*  733 */             buf.append((char)(int)(48L + L));
/*  734 */             if (i == ilim) {
/*  735 */               if (d > 0.5D + eps) {
/*      */                 char lastCh;
/*      */                 
/*      */                 while (true) {
/*  739 */                   lastCh = buf.charAt(buf.length() - 1);
/*  740 */                   buf.setLength(buf.length() - 1);
/*  741 */                   if (lastCh != '9')
/*  742 */                     break;  if (buf.length() == 0) {
/*  743 */                     k++;
/*  744 */                     lastCh = '0';
/*      */                     break;
/*      */                   } 
/*      */                 } 
/*  748 */                 buf.append((char)(lastCh + 1));
/*  749 */                 return k + 1;
/*      */               } 
/*      */               
/*  752 */               if (d < 0.5D - eps) {
/*  753 */                 stripTrailingZeroes(buf);
/*      */ 
/*      */                 
/*  756 */                 return k + 1;
/*      */               } 
/*      */               break;
/*      */             } 
/*      */           } 
/*      */         } 
/*      */       } 
/*  763 */       if (fast_failed) {
/*  764 */         buf.setLength(0);
/*  765 */         d = d2;
/*  766 */         k = k0;
/*  767 */         ilim = ilim0;
/*      */       } 
/*      */     } 
/*      */ 
/*      */ 
/*      */     
/*  773 */     if (be[0] >= 0 && k <= 14) {
/*      */       
/*  775 */       ds = tens[k];
/*  776 */       if (ndigits < 0 && ilim <= 0) {
/*  777 */         BigInteger bigInteger1 = null, bigInteger2 = bigInteger1;
/*  778 */         if (ilim < 0 || d < 5.0D * ds || (!biasUp && d == 5.0D * ds)) {
/*  779 */           buf.setLength(0);
/*  780 */           buf.append('0');
/*  781 */           return 1;
/*      */         } 
/*  783 */         buf.append('1');
/*  784 */         k++;
/*  785 */         return k + 1;
/*      */       } 
/*  787 */       for (i = 1;; i++) {
/*  788 */         long L = (long)(d / ds);
/*  789 */         d -= L * ds;
/*  790 */         buf.append((char)(int)(48L + L));
/*  791 */         if (i == ilim) {
/*  792 */           d += d;
/*  793 */           if (d > ds || (d == ds && ((L & 0x1L) != 0L || biasUp))) {
/*      */             char lastCh;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */             
/*      */             while (true) {
/*  804 */               lastCh = buf.charAt(buf.length() - 1);
/*  805 */               buf.setLength(buf.length() - 1);
/*  806 */               if (lastCh != '9')
/*  807 */                 break;  if (buf.length() == 0) {
/*  808 */                 k++;
/*  809 */                 lastCh = '0';
/*      */                 break;
/*      */               } 
/*      */             } 
/*  813 */             buf.append((char)(lastCh + 1));
/*      */           } 
/*      */           break;
/*      */         } 
/*  817 */         d *= 10.0D;
/*  818 */         if (d == 0.0D)
/*      */           break; 
/*      */       } 
/*  821 */       return k + 1;
/*      */     } 
/*      */     
/*  824 */     int m2 = b2;
/*  825 */     int m5 = b5;
/*  826 */     BigInteger mlo = null, mhi = mlo;
/*  827 */     if (leftright) {
/*  828 */       if (mode < 2) {
/*  829 */         i = denorm ? (be[0] + 1075) : (54 - bbits[0]);
/*      */       
/*      */       }
/*      */       else {
/*      */         
/*  834 */         j = ilim - 1;
/*  835 */         if (m5 >= j) {
/*  836 */           m5 -= j;
/*      */         } else {
/*  838 */           s5 += j -= m5;
/*  839 */           b5 += j;
/*  840 */           m5 = 0;
/*      */         } 
/*  842 */         if ((i = ilim) < 0) {
/*  843 */           m2 -= i;
/*  844 */           i = 0;
/*      */         } 
/*      */       } 
/*      */       
/*  848 */       b2 += i;
/*  849 */       s2 += i;
/*  850 */       mhi = BigInteger.valueOf(1L);
/*      */     } 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  856 */     if (m2 > 0 && s2 > 0) {
/*  857 */       i = (m2 < s2) ? m2 : s2;
/*  858 */       b2 -= i;
/*  859 */       m2 -= i;
/*  860 */       s2 -= i;
/*      */     } 
/*      */ 
/*      */     
/*  864 */     if (b5 > 0) {
/*  865 */       if (leftright) {
/*  866 */         if (m5 > 0) {
/*  867 */           mhi = pow5mult(mhi, m5);
/*  868 */           BigInteger b1 = mhi.multiply(b);
/*  869 */           b = b1;
/*      */         } 
/*  871 */         if ((j = b5 - m5) != 0) {
/*  872 */           b = pow5mult(b, j);
/*      */         }
/*      */       } else {
/*  875 */         b = pow5mult(b, b5);
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*  880 */     BigInteger S = BigInteger.valueOf(1L);
/*  881 */     if (s5 > 0) {
/*  882 */       S = pow5mult(S, s5);
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*  887 */     boolean spec_case = false;
/*  888 */     if (mode < 2 && 
/*  889 */       word1(d) == 0 && (word0(d) & 0xFFFFF) == 0 && (word0(d) & 0x7FE00000) != 0) {
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*  894 */       b2++;
/*  895 */       s2++;
/*  896 */       spec_case = true;
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
/*  907 */     byte[] S_bytes = S.toByteArray();
/*  908 */     int S_hiWord = 0;
/*  909 */     for (int idx = 0; idx < 4; idx++) {
/*  910 */       S_hiWord <<= 8;
/*  911 */       if (idx < S_bytes.length)
/*  912 */         S_hiWord |= S_bytes[idx] & 0xFF; 
/*      */     } 
/*  914 */     if ((i = ((s5 != 0) ? (32 - hi0bits(S_hiWord)) : 1) + s2 & 0x1F) != 0) {
/*  915 */       i = 32 - i;
/*      */     }
/*  917 */     if (i > 4) {
/*  918 */       i -= 4;
/*  919 */       b2 += i;
/*  920 */       m2 += i;
/*  921 */       s2 += i;
/*      */     }
/*  923 */     else if (i < 4) {
/*  924 */       i += 28;
/*  925 */       b2 += i;
/*  926 */       m2 += i;
/*  927 */       s2 += i;
/*      */     } 
/*      */     
/*  930 */     if (b2 > 0)
/*  931 */       b = b.shiftLeft(b2); 
/*  932 */     if (s2 > 0) {
/*  933 */       S = S.shiftLeft(s2);
/*      */     }
/*      */     
/*  936 */     if (k_check && 
/*  937 */       b.compareTo(S) < 0) {
/*  938 */       k--;
/*  939 */       b = b.multiply(BigInteger.valueOf(10L));
/*  940 */       if (leftright)
/*  941 */         mhi = mhi.multiply(BigInteger.valueOf(10L)); 
/*  942 */       ilim = ilim1;
/*      */     } 
/*      */ 
/*      */ 
/*      */     
/*  947 */     if (ilim <= 0 && mode > 2) {
/*      */ 
/*      */       
/*  950 */       if (ilim < 0 || (i = b.compareTo(S = S.multiply(BigInteger.valueOf(5L)))) < 0 || (i == 0 && !biasUp)) {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */         
/*  958 */         buf.setLength(0);
/*  959 */         buf.append('0');
/*  960 */         return 1;
/*      */       } 
/*      */ 
/*      */       
/*  964 */       buf.append('1');
/*  965 */       k++;
/*  966 */       return k + 1;
/*      */     } 
/*  968 */     if (leftright) {
/*  969 */       if (m2 > 0) {
/*  970 */         mhi = mhi.shiftLeft(m2);
/*      */       }
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*  976 */       mlo = mhi;
/*  977 */       if (spec_case) {
/*  978 */         mhi = mlo;
/*  979 */         mhi = mhi.shiftLeft(1);
/*      */       } 
/*      */ 
/*      */ 
/*      */       
/*  984 */       for (i = 1;; i++) {
/*  985 */         BigInteger[] divResult = b.divideAndRemainder(S);
/*  986 */         b = divResult[1];
/*  987 */         dig = (char)(divResult[0].intValue() + 48);
/*      */ 
/*      */ 
/*      */         
/*  991 */         j = b.compareTo(mlo);
/*      */         
/*  993 */         BigInteger delta = S.subtract(mhi);
/*  994 */         int j1 = (delta.signum() <= 0) ? 1 : b.compareTo(delta);
/*      */         
/*  996 */         if (j1 == 0 && mode == 0 && (word1(d) & 0x1) == 0) {
/*  997 */           if (dig == '9') {
/*  998 */             buf.append('9');
/*  999 */             if (roundOff(buf)) {
/* 1000 */               k++;
/* 1001 */               buf.append('1');
/*      */             } 
/* 1003 */             return k + 1;
/*      */           } 
/*      */           
/* 1006 */           if (j > 0)
/* 1007 */             dig = (char)(dig + 1); 
/* 1008 */           buf.append(dig);
/* 1009 */           return k + 1;
/*      */         } 
/* 1011 */         if (j < 0 || (j == 0 && mode == 0 && (word1(d) & 0x1) == 0)) {
/*      */ 
/*      */ 
/*      */ 
/*      */           
/* 1016 */           if (j1 > 0) {
/*      */ 
/*      */             
/* 1019 */             b = b.shiftLeft(1);
/* 1020 */             j1 = b.compareTo(S);
/* 1021 */             dig = (char)(dig + 1); if ((j1 > 0 || (j1 == 0 && ((dig & 0x1) == 1 || biasUp))) && dig == '9') {
/*      */               
/* 1023 */               buf.append('9');
/* 1024 */               if (roundOff(buf)) {
/* 1025 */                 k++;
/* 1026 */                 buf.append('1');
/*      */               } 
/* 1028 */               return k + 1;
/*      */             } 
/*      */           } 
/*      */           
/* 1032 */           buf.append(dig);
/* 1033 */           return k + 1;
/*      */         } 
/* 1035 */         if (j1 > 0) {
/* 1036 */           if (dig == '9') {
/*      */ 
/*      */ 
/*      */             
/* 1040 */             buf.append('9');
/* 1041 */             if (roundOff(buf)) {
/* 1042 */               k++;
/* 1043 */               buf.append('1');
/*      */             } 
/* 1045 */             return k + 1;
/*      */           } 
/* 1047 */           buf.append((char)(dig + 1));
/* 1048 */           return k + 1;
/*      */         } 
/* 1050 */         buf.append(dig);
/* 1051 */         if (i == ilim)
/*      */           break; 
/* 1053 */         b = b.multiply(BigInteger.valueOf(10L));
/* 1054 */         if (mlo == mhi) {
/* 1055 */           mlo = mhi = mhi.multiply(BigInteger.valueOf(10L));
/*      */         } else {
/* 1057 */           mlo = mlo.multiply(BigInteger.valueOf(10L));
/* 1058 */           mhi = mhi.multiply(BigInteger.valueOf(10L));
/*      */         } 
/*      */       } 
/*      */     } else {
/*      */       
/* 1063 */       for (i = 1;; i++) {
/*      */         
/* 1065 */         BigInteger[] divResult = b.divideAndRemainder(S);
/* 1066 */         b = divResult[1];
/* 1067 */         dig = (char)(divResult[0].intValue() + 48);
/* 1068 */         buf.append(dig);
/* 1069 */         if (i >= ilim)
/*      */           break; 
/* 1071 */         b = b.multiply(BigInteger.valueOf(10L));
/*      */       } 
/*      */     } 
/*      */ 
/*      */     
/* 1076 */     b = b.shiftLeft(1);
/* 1077 */     j = b.compareTo(S);
/* 1078 */     if (j > 0 || (j == 0 && ((dig & 0x1) == 1 || biasUp))) {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 1087 */       if (roundOff(buf)) {
/* 1088 */         k++;
/* 1089 */         buf.append('1');
/* 1090 */         return k + 1;
/*      */       } 
/*      */     } else {
/*      */       
/* 1094 */       stripTrailingZeroes(buf);
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
/* 1108 */     return k + 1;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static void stripTrailingZeroes(StringBuilder buf) {
/* 1116 */     int bl = buf.length();
/* 1117 */     while (bl-- > 0 && buf.charAt(bl) == '0');
/*      */ 
/*      */     
/* 1120 */     buf.setLength(bl + 1);
/*      */   }
/*      */ 
/*      */   
/* 1124 */   private static final int[] dtoaModes = new int[] { 0, 0, 3, 2, 2 };
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   static void JS_dtostr(StringBuilder buffer, int mode, int precision, double d) {
/* 1135 */     boolean[] sign = new boolean[1];
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1141 */     if (mode == 2 && (d >= 1.0E21D || d <= -1.0E21D)) {
/* 1142 */       mode = 0;
/*      */     }
/* 1144 */     int decPt = JS_dtoa(d, dtoaModes[mode], (mode >= 2), precision, sign, buffer);
/* 1145 */     int nDigits = buffer.length();
/*      */ 
/*      */     
/* 1148 */     if (decPt != 9999) {
/* 1149 */       boolean exponentialNotation = false;
/* 1150 */       int minNDigits = 0;
/*      */ 
/*      */       
/* 1153 */       switch (mode) {
/*      */         case 0:
/* 1155 */           if (decPt < -5 || decPt > 21) {
/* 1156 */             exponentialNotation = true; break;
/*      */           } 
/* 1158 */           minNDigits = decPt;
/*      */           break;
/*      */         
/*      */         case 2:
/* 1162 */           if (precision >= 0) {
/* 1163 */             minNDigits = decPt + precision; break;
/*      */           } 
/* 1165 */           minNDigits = decPt;
/*      */           break;
/*      */ 
/*      */         
/*      */         case 3:
/* 1170 */           minNDigits = precision;
/*      */         
/*      */         case 1:
/* 1173 */           exponentialNotation = true;
/*      */           break;
/*      */ 
/*      */         
/*      */         case 4:
/* 1178 */           minNDigits = precision;
/* 1179 */           if (decPt < -5 || decPt > precision) {
/* 1180 */             exponentialNotation = true;
/*      */           }
/*      */           break;
/*      */       } 
/*      */       
/* 1185 */       if (nDigits < minNDigits) {
/* 1186 */         int p = minNDigits;
/* 1187 */         nDigits = minNDigits;
/*      */         do {
/* 1189 */           buffer.append('0');
/* 1190 */         } while (buffer.length() != p);
/*      */       } 
/*      */       
/* 1193 */       if (exponentialNotation) {
/*      */         
/* 1195 */         if (nDigits != 1) {
/* 1196 */           buffer.insert(1, '.');
/*      */         }
/* 1198 */         buffer.append('e');
/* 1199 */         if (decPt - 1 >= 0)
/* 1200 */           buffer.append('+'); 
/* 1201 */         buffer.append(decPt - 1);
/*      */       }
/* 1203 */       else if (decPt != nDigits) {
/*      */ 
/*      */         
/* 1206 */         if (decPt > 0) {
/*      */           
/* 1208 */           buffer.insert(decPt, '.');
/*      */         } else {
/*      */           
/* 1211 */           for (int i = 0; i < 1 - decPt; i++)
/* 1212 */             buffer.insert(0, '0'); 
/* 1213 */           buffer.insert(1, '.');
/*      */         } 
/*      */       } 
/*      */     } 
/*      */ 
/*      */     
/* 1219 */     if (sign[0] && (word0(d) != Integer.MIN_VALUE || word1(d) != 0) && ((word0(d) & 0x7FF00000) != 2146435072 || (word1(d) == 0 && (word0(d) & 0xFFFFF) == 0)))
/*      */     {
/*      */ 
/*      */       
/* 1223 */       buffer.insert(0, '-');
/*      */     }
/*      */   }
/*      */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\mozilla\javascript\DToA.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */