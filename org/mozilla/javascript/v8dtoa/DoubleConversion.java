/*    */ package org.mozilla.javascript.v8dtoa;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public final class DoubleConversion
/*    */ {
/*    */   private static final long kSignMask = -9223372036854775808L;
/*    */   private static final long kExponentMask = 9218868437227405312L;
/*    */   private static final long kSignificandMask = 4503599627370495L;
/*    */   private static final long kHiddenBit = 4503599627370496L;
/*    */   private static final int kPhysicalSignificandSize = 52;
/*    */   private static final int kSignificandSize = 53;
/*    */   private static final int kExponentBias = 1075;
/*    */   private static final int kDenormalExponent = -1074;
/*    */   
/*    */   private static int exponent(long d64) {
/* 47 */     if (isDenormal(d64)) {
/* 48 */       return -1074;
/*    */     }
/* 50 */     int biased_e = (int)((d64 & 0x7FF0000000000000L) >> 52L);
/* 51 */     return biased_e - 1075;
/*    */   }
/*    */   
/*    */   private static long significand(long d64) {
/* 55 */     long significand = d64 & 0xFFFFFFFFFFFFFL;
/* 56 */     if (!isDenormal(d64)) {
/* 57 */       return significand + 4503599627370496L;
/*    */     }
/* 59 */     return significand;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   private static boolean isDenormal(long d64) {
/* 65 */     return ((d64 & 0x7FF0000000000000L) == 0L);
/*    */   }
/*    */   
/*    */   private static int sign(long d64) {
/* 69 */     return ((d64 & Long.MIN_VALUE) == 0L) ? 1 : -1;
/*    */   }
/*    */   
/*    */   public static int doubleToInt32(double x) {
/* 73 */     int i = (int)x;
/* 74 */     if (i == x) {
/* 75 */       return i;
/*    */     }
/* 77 */     long d64 = Double.doubleToLongBits(x);
/* 78 */     int exponent = exponent(d64);
/* 79 */     if (exponent <= -53 || exponent > 31) {
/* 80 */       return 0;
/*    */     }
/* 82 */     long s = significand(d64);
/* 83 */     return sign(d64) * (int)((exponent < 0) ? (s >> -exponent) : (s << exponent));
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\mozilla\javascript\v8dtoa\DoubleConversion.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */