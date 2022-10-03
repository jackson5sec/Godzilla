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
/*     */ class DiyFp
/*     */ {
/*     */   private long f;
/*     */   private int e;
/*     */   static final int kSignificandSize = 64;
/*     */   static final long kUint64MSB = -9223372036854775808L;
/*     */   
/*     */   DiyFp() {
/*  48 */     this.f = 0L;
/*  49 */     this.e = 0;
/*     */   }
/*     */   
/*     */   DiyFp(long f, int e) {
/*  53 */     this.f = f;
/*  54 */     this.e = e;
/*     */   }
/*     */ 
/*     */   
/*     */   private static boolean uint64_gte(long a, long b) {
/*  59 */     if (a != b) { if ((((a > b) ? 1 : 0) ^ ((a < 0L) ? 1 : 0) ^ ((b < 0L) ? 1 : 0)) != 0); return false; }
/*     */   
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   void subtract(DiyFp other) {
/*  67 */     assert this.e == other.e;
/*  68 */     assert uint64_gte(this.f, other.f);
/*  69 */     this.f -= other.f;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static DiyFp minus(DiyFp a, DiyFp b) {
/*  76 */     DiyFp result = new DiyFp(a.f, a.e);
/*  77 */     result.subtract(b);
/*  78 */     return result;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   void multiply(DiyFp other) {
/*  88 */     long kM32 = 4294967295L;
/*  89 */     long a = this.f >>> 32L;
/*  90 */     long b = this.f & 0xFFFFFFFFL;
/*  91 */     long c = other.f >>> 32L;
/*  92 */     long d = other.f & 0xFFFFFFFFL;
/*  93 */     long ac = a * c;
/*  94 */     long bc = b * c;
/*  95 */     long ad = a * d;
/*  96 */     long bd = b * d;
/*  97 */     long tmp = (bd >>> 32L) + (ad & 0xFFFFFFFFL) + (bc & 0xFFFFFFFFL);
/*     */ 
/*     */     
/* 100 */     tmp += 2147483648L;
/* 101 */     long result_f = ac + (ad >>> 32L) + (bc >>> 32L) + (tmp >>> 32L);
/* 102 */     this.e += other.e + 64;
/* 103 */     this.f = result_f;
/*     */   }
/*     */ 
/*     */   
/*     */   static DiyFp times(DiyFp a, DiyFp b) {
/* 108 */     DiyFp result = new DiyFp(a.f, a.e);
/* 109 */     result.multiply(b);
/* 110 */     return result;
/*     */   }
/*     */   
/*     */   void normalize() {
/* 114 */     assert this.f != 0L;
/* 115 */     long f = this.f;
/* 116 */     int e = this.e;
/*     */ 
/*     */ 
/*     */     
/* 120 */     long k10MSBits = -18014398509481984L;
/* 121 */     while ((f & 0xFFC0000000000000L) == 0L) {
/* 122 */       f <<= 10L;
/* 123 */       e -= 10;
/*     */     } 
/* 125 */     while ((f & Long.MIN_VALUE) == 0L) {
/* 126 */       f <<= 1L;
/* 127 */       e--;
/*     */     } 
/* 129 */     this.f = f;
/* 130 */     this.e = e;
/*     */   }
/*     */   
/*     */   static DiyFp normalize(DiyFp a) {
/* 134 */     DiyFp result = new DiyFp(a.f, a.e);
/* 135 */     result.normalize();
/* 136 */     return result;
/*     */   }
/*     */   
/* 139 */   long f() { return this.f; } int e() {
/* 140 */     return this.e;
/*     */   }
/* 142 */   void setF(long new_value) { this.f = new_value; } void setE(int new_value) {
/* 143 */     this.e = new_value;
/*     */   }
/*     */   
/*     */   public String toString() {
/* 147 */     return "[DiyFp f:" + this.f + ", e:" + this.e + "]";
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\mozilla\javascript\v8dtoa\DiyFp.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */