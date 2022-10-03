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
/*     */ public class DoubleHelper
/*     */ {
/*     */   static final long kSignMask = -9223372036854775808L;
/*     */   static final long kExponentMask = 9218868437227405312L;
/*     */   static final long kSignificandMask = 4503599627370495L;
/*     */   static final long kHiddenBit = 4503599627370496L;
/*     */   private static final int kSignificandSize = 52;
/*     */   private static final int kExponentBias = 1075;
/*     */   private static final int kDenormalExponent = -1074;
/*     */   
/*     */   static DiyFp asDiyFp(long d64) {
/*  42 */     assert !isSpecial(d64);
/*  43 */     return new DiyFp(significand(d64), exponent(d64));
/*     */   }
/*     */ 
/*     */   
/*     */   static DiyFp asNormalizedDiyFp(long d64) {
/*  48 */     long f = significand(d64);
/*  49 */     int e = exponent(d64);
/*     */     
/*  51 */     assert f != 0L;
/*     */ 
/*     */     
/*  54 */     while ((f & 0x10000000000000L) == 0L) {
/*  55 */       f <<= 1L;
/*  56 */       e--;
/*     */     } 
/*     */     
/*  59 */     f <<= 11L;
/*  60 */     e -= 11;
/*  61 */     return new DiyFp(f, e);
/*     */   }
/*     */   
/*     */   static int exponent(long d64) {
/*  65 */     if (isDenormal(d64)) return -1074;
/*     */     
/*  67 */     int biased_e = (int)((d64 & 0x7FF0000000000000L) >>> 52L & 0xFFFFFFFFL);
/*  68 */     return biased_e - 1075;
/*     */   }
/*     */   
/*     */   static long significand(long d64) {
/*  72 */     long significand = d64 & 0xFFFFFFFFFFFFFL;
/*  73 */     if (!isDenormal(d64)) {
/*  74 */       return significand + 4503599627370496L;
/*     */     }
/*  76 */     return significand;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   static boolean isDenormal(long d64) {
/*  82 */     return ((d64 & 0x7FF0000000000000L) == 0L);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   static boolean isSpecial(long d64) {
/*  88 */     return ((d64 & 0x7FF0000000000000L) == 9218868437227405312L);
/*     */   }
/*     */   
/*     */   static boolean isNan(long d64) {
/*  92 */     return ((d64 & 0x7FF0000000000000L) == 9218868437227405312L && (d64 & 0xFFFFFFFFFFFFFL) != 0L);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   static boolean isInfinite(long d64) {
/*  98 */     return ((d64 & 0x7FF0000000000000L) == 9218868437227405312L && (d64 & 0xFFFFFFFFFFFFFL) == 0L);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   static int sign(long d64) {
/* 104 */     return ((d64 & Long.MIN_VALUE) == 0L) ? 1 : -1;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static void normalizedBoundaries(long d64, DiyFp m_minus, DiyFp m_plus) {
/* 112 */     DiyFp v = asDiyFp(d64);
/* 113 */     boolean significand_is_zero = (v.f() == 4503599627370496L);
/* 114 */     m_plus.setF((v.f() << 1L) + 1L);
/* 115 */     m_plus.setE(v.e() - 1);
/* 116 */     m_plus.normalize();
/* 117 */     if (significand_is_zero && v.e() != -1074) {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 124 */       m_minus.setF((v.f() << 2L) - 1L);
/* 125 */       m_minus.setE(v.e() - 2);
/*     */     } else {
/* 127 */       m_minus.setF((v.f() << 1L) - 1L);
/* 128 */       m_minus.setE(v.e() - 1);
/*     */     } 
/* 130 */     m_minus.setF(m_minus.f() << m_minus.e() - m_plus.e());
/* 131 */     m_minus.setE(m_plus.e());
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\mozilla\javascript\v8dtoa\DoubleHelper.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */