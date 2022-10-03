/*     */ package com.google.common.hash;
/*     */ 
/*     */ import com.google.common.annotations.VisibleForTesting;
/*     */ import com.google.common.base.Preconditions;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ final class FarmHashFingerprint64
/*     */   extends AbstractNonStreamingHashFunction
/*     */ {
/*  42 */   static final HashFunction FARMHASH_FINGERPRINT_64 = new FarmHashFingerprint64();
/*     */   
/*     */   private static final long K0 = -4348849565147123417L;
/*     */   
/*     */   private static final long K1 = -5435081209227447693L;
/*     */   
/*     */   private static final long K2 = -7286425919675154353L;
/*     */   
/*     */   public HashCode hashBytes(byte[] input, int off, int len) {
/*  51 */     Preconditions.checkPositionIndexes(off, off + len, input.length);
/*  52 */     return HashCode.fromLong(fingerprint(input, off, len));
/*     */   }
/*     */ 
/*     */   
/*     */   public int bits() {
/*  57 */     return 64;
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/*  62 */     return "Hashing.farmHashFingerprint64()";
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @VisibleForTesting
/*     */   static long fingerprint(byte[] bytes, int offset, int length) {
/*  69 */     if (length <= 32) {
/*  70 */       if (length <= 16) {
/*  71 */         return hashLength0to16(bytes, offset, length);
/*     */       }
/*  73 */       return hashLength17to32(bytes, offset, length);
/*     */     } 
/*  75 */     if (length <= 64) {
/*  76 */       return hashLength33To64(bytes, offset, length);
/*     */     }
/*  78 */     return hashLength65Plus(bytes, offset, length);
/*     */   }
/*     */ 
/*     */   
/*     */   private static long shiftMix(long val) {
/*  83 */     return val ^ val >>> 47L;
/*     */   }
/*     */   
/*     */   private static long hashLength16(long u, long v, long mul) {
/*  87 */     long a = (u ^ v) * mul;
/*  88 */     a ^= a >>> 47L;
/*  89 */     long b = (v ^ a) * mul;
/*  90 */     b ^= b >>> 47L;
/*  91 */     b *= mul;
/*  92 */     return b;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static void weakHashLength32WithSeeds(byte[] bytes, int offset, long seedA, long seedB, long[] output) {
/* 102 */     long part1 = LittleEndianByteArray.load64(bytes, offset);
/* 103 */     long part2 = LittleEndianByteArray.load64(bytes, offset + 8);
/* 104 */     long part3 = LittleEndianByteArray.load64(bytes, offset + 16);
/* 105 */     long part4 = LittleEndianByteArray.load64(bytes, offset + 24);
/*     */     
/* 107 */     seedA += part1;
/* 108 */     seedB = Long.rotateRight(seedB + seedA + part4, 21);
/* 109 */     long c = seedA;
/* 110 */     seedA += part2;
/* 111 */     seedA += part3;
/* 112 */     seedB += Long.rotateRight(seedA, 44);
/* 113 */     output[0] = seedA + part4;
/* 114 */     output[1] = seedB + c;
/*     */   }
/*     */   
/*     */   private static long hashLength0to16(byte[] bytes, int offset, int length) {
/* 118 */     if (length >= 8) {
/* 119 */       long mul = -7286425919675154353L + (length * 2);
/* 120 */       long a = LittleEndianByteArray.load64(bytes, offset) + -7286425919675154353L;
/* 121 */       long b = LittleEndianByteArray.load64(bytes, offset + length - 8);
/* 122 */       long c = Long.rotateRight(b, 37) * mul + a;
/* 123 */       long d = (Long.rotateRight(a, 25) + b) * mul;
/* 124 */       return hashLength16(c, d, mul);
/*     */     } 
/* 126 */     if (length >= 4) {
/* 127 */       long mul = -7286425919675154353L + (length * 2);
/* 128 */       long a = LittleEndianByteArray.load32(bytes, offset) & 0xFFFFFFFFL;
/* 129 */       return hashLength16(length + (a << 3L), LittleEndianByteArray.load32(bytes, offset + length - 4) & 0xFFFFFFFFL, mul);
/*     */     } 
/* 131 */     if (length > 0) {
/* 132 */       byte a = bytes[offset];
/* 133 */       byte b = bytes[offset + (length >> 1)];
/* 134 */       byte c = bytes[offset + length - 1];
/* 135 */       int y = (a & 0xFF) + ((b & 0xFF) << 8);
/* 136 */       int z = length + ((c & 0xFF) << 2);
/* 137 */       return shiftMix(y * -7286425919675154353L ^ z * -4348849565147123417L) * -7286425919675154353L;
/*     */     } 
/* 139 */     return -7286425919675154353L;
/*     */   }
/*     */   
/*     */   private static long hashLength17to32(byte[] bytes, int offset, int length) {
/* 143 */     long mul = -7286425919675154353L + (length * 2);
/* 144 */     long a = LittleEndianByteArray.load64(bytes, offset) * -5435081209227447693L;
/* 145 */     long b = LittleEndianByteArray.load64(bytes, offset + 8);
/* 146 */     long c = LittleEndianByteArray.load64(bytes, offset + length - 8) * mul;
/* 147 */     long d = LittleEndianByteArray.load64(bytes, offset + length - 16) * -7286425919675154353L;
/* 148 */     return hashLength16(
/* 149 */         Long.rotateRight(a + b, 43) + Long.rotateRight(c, 30) + d, a + Long.rotateRight(b + -7286425919675154353L, 18) + c, mul);
/*     */   }
/*     */   
/*     */   private static long hashLength33To64(byte[] bytes, int offset, int length) {
/* 153 */     long mul = -7286425919675154353L + (length * 2);
/* 154 */     long a = LittleEndianByteArray.load64(bytes, offset) * -7286425919675154353L;
/* 155 */     long b = LittleEndianByteArray.load64(bytes, offset + 8);
/* 156 */     long c = LittleEndianByteArray.load64(bytes, offset + length - 8) * mul;
/* 157 */     long d = LittleEndianByteArray.load64(bytes, offset + length - 16) * -7286425919675154353L;
/* 158 */     long y = Long.rotateRight(a + b, 43) + Long.rotateRight(c, 30) + d;
/* 159 */     long z = hashLength16(y, a + Long.rotateRight(b + -7286425919675154353L, 18) + c, mul);
/* 160 */     long e = LittleEndianByteArray.load64(bytes, offset + 16) * mul;
/* 161 */     long f = LittleEndianByteArray.load64(bytes, offset + 24);
/* 162 */     long g = (y + LittleEndianByteArray.load64(bytes, offset + length - 32)) * mul;
/* 163 */     long h = (z + LittleEndianByteArray.load64(bytes, offset + length - 24)) * mul;
/* 164 */     return hashLength16(
/* 165 */         Long.rotateRight(e + f, 43) + Long.rotateRight(g, 30) + h, e + Long.rotateRight(f + a, 18) + g, mul);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static long hashLength65Plus(byte[] bytes, int offset, int length) {
/* 172 */     int seed = 81;
/*     */     
/* 174 */     long x = 81L;
/*     */     
/* 176 */     long y = 2480279821605975764L;
/* 177 */     long z = shiftMix(y * -7286425919675154353L + 113L) * -7286425919675154353L;
/* 178 */     long[] v = new long[2];
/* 179 */     long[] w = new long[2];
/* 180 */     x = x * -7286425919675154353L + LittleEndianByteArray.load64(bytes, offset);
/*     */ 
/*     */     
/* 183 */     int end = offset + (length - 1) / 64 * 64;
/* 184 */     int last64offset = end + (length - 1 & 0x3F) - 63;
/*     */     while (true) {
/* 186 */       x = Long.rotateRight(x + y + v[0] + LittleEndianByteArray.load64(bytes, offset + 8), 37) * -5435081209227447693L;
/* 187 */       y = Long.rotateRight(y + v[1] + LittleEndianByteArray.load64(bytes, offset + 48), 42) * -5435081209227447693L;
/* 188 */       x ^= w[1];
/* 189 */       y += v[0] + LittleEndianByteArray.load64(bytes, offset + 40);
/* 190 */       z = Long.rotateRight(z + w[0], 33) * -5435081209227447693L;
/* 191 */       weakHashLength32WithSeeds(bytes, offset, v[1] * -5435081209227447693L, x + w[0], v);
/* 192 */       weakHashLength32WithSeeds(bytes, offset + 32, z + w[1], y + LittleEndianByteArray.load64(bytes, offset + 16), w);
/* 193 */       long tmp = x;
/* 194 */       x = z;
/* 195 */       z = tmp;
/* 196 */       offset += 64;
/* 197 */       if (offset == end) {
/* 198 */         long mul = -5435081209227447693L + ((z & 0xFFL) << 1L);
/*     */         
/* 200 */         offset = last64offset;
/* 201 */         w[0] = w[0] + (length - 1 & 0x3F);
/* 202 */         v[0] = v[0] + w[0];
/* 203 */         w[0] = w[0] + v[0];
/* 204 */         x = Long.rotateRight(x + y + v[0] + LittleEndianByteArray.load64(bytes, offset + 8), 37) * mul;
/* 205 */         y = Long.rotateRight(y + v[1] + LittleEndianByteArray.load64(bytes, offset + 48), 42) * mul;
/* 206 */         x ^= w[1] * 9L;
/* 207 */         y += v[0] * 9L + LittleEndianByteArray.load64(bytes, offset + 40);
/* 208 */         z = Long.rotateRight(z + w[0], 33) * mul;
/* 209 */         weakHashLength32WithSeeds(bytes, offset, v[1] * mul, x + w[0], v);
/* 210 */         weakHashLength32WithSeeds(bytes, offset + 32, z + w[1], y + LittleEndianByteArray.load64(bytes, offset + 16), w);
/* 211 */         return hashLength16(
/* 212 */             hashLength16(v[0], w[0], mul) + shiftMix(y) * -4348849565147123417L + x, 
/* 213 */             hashLength16(v[1], w[1], mul) + z, mul);
/*     */       } 
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\google\common\hash\FarmHashFingerprint64.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */