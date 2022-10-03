/*     */ package com.google.common.hash;
/*     */ 
/*     */ import com.google.common.primitives.UnsignedBytes;
/*     */ import com.google.errorprone.annotations.Immutable;
/*     */ import java.io.Serializable;
/*     */ import java.nio.ByteBuffer;
/*     */ import java.nio.ByteOrder;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @Immutable
/*     */ final class Murmur3_128HashFunction
/*     */   extends AbstractHashFunction
/*     */   implements Serializable
/*     */ {
/*  45 */   static final HashFunction MURMUR3_128 = new Murmur3_128HashFunction(0);
/*     */   
/*  47 */   static final HashFunction GOOD_FAST_HASH_128 = new Murmur3_128HashFunction(Hashing.GOOD_FAST_HASH_SEED);
/*     */   
/*     */   private final int seed;
/*     */   
/*     */   private static final long serialVersionUID = 0L;
/*     */   
/*     */   Murmur3_128HashFunction(int seed) {
/*  54 */     this.seed = seed;
/*     */   }
/*     */ 
/*     */   
/*     */   public int bits() {
/*  59 */     return 128;
/*     */   }
/*     */ 
/*     */   
/*     */   public Hasher newHasher() {
/*  64 */     return new Murmur3_128Hasher(this.seed);
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/*  69 */     return "Hashing.murmur3_128(" + this.seed + ")";
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean equals(Object object) {
/*  74 */     if (object instanceof Murmur3_128HashFunction) {
/*  75 */       Murmur3_128HashFunction other = (Murmur3_128HashFunction)object;
/*  76 */       return (this.seed == other.seed);
/*     */     } 
/*  78 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public int hashCode() {
/*  83 */     return getClass().hashCode() ^ this.seed;
/*     */   }
/*     */   
/*     */   private static final class Murmur3_128Hasher extends AbstractStreamingHasher {
/*     */     private static final int CHUNK_SIZE = 16;
/*     */     private static final long C1 = -8663945395140668459L;
/*     */     private static final long C2 = 5545529020109919103L;
/*     */     private long h1;
/*     */     private long h2;
/*     */     private int length;
/*     */     
/*     */     Murmur3_128Hasher(int seed) {
/*  95 */       super(16);
/*  96 */       this.h1 = seed;
/*  97 */       this.h2 = seed;
/*  98 */       this.length = 0;
/*     */     }
/*     */ 
/*     */     
/*     */     protected void process(ByteBuffer bb) {
/* 103 */       long k1 = bb.getLong();
/* 104 */       long k2 = bb.getLong();
/* 105 */       bmix64(k1, k2);
/* 106 */       this.length += 16;
/*     */     }
/*     */     
/*     */     private void bmix64(long k1, long k2) {
/* 110 */       this.h1 ^= mixK1(k1);
/*     */       
/* 112 */       this.h1 = Long.rotateLeft(this.h1, 27);
/* 113 */       this.h1 += this.h2;
/* 114 */       this.h1 = this.h1 * 5L + 1390208809L;
/*     */       
/* 116 */       this.h2 ^= mixK2(k2);
/*     */       
/* 118 */       this.h2 = Long.rotateLeft(this.h2, 31);
/* 119 */       this.h2 += this.h1;
/* 120 */       this.h2 = this.h2 * 5L + 944331445L;
/*     */     }
/*     */ 
/*     */     
/*     */     protected void processRemaining(ByteBuffer bb) {
/* 125 */       long k1 = 0L;
/* 126 */       long k2 = 0L;
/* 127 */       this.length += bb.remaining();
/* 128 */       switch (bb.remaining()) {
/*     */         case 15:
/* 130 */           k2 ^= UnsignedBytes.toInt(bb.get(14)) << 48L;
/*     */         case 14:
/* 132 */           k2 ^= UnsignedBytes.toInt(bb.get(13)) << 40L;
/*     */         case 13:
/* 134 */           k2 ^= UnsignedBytes.toInt(bb.get(12)) << 32L;
/*     */         case 12:
/* 136 */           k2 ^= UnsignedBytes.toInt(bb.get(11)) << 24L;
/*     */         case 11:
/* 138 */           k2 ^= UnsignedBytes.toInt(bb.get(10)) << 16L;
/*     */         case 10:
/* 140 */           k2 ^= UnsignedBytes.toInt(bb.get(9)) << 8L;
/*     */         case 9:
/* 142 */           k2 ^= UnsignedBytes.toInt(bb.get(8));
/*     */         case 8:
/* 144 */           k1 ^= bb.getLong();
/*     */           break;
/*     */         case 7:
/* 147 */           k1 ^= UnsignedBytes.toInt(bb.get(6)) << 48L;
/*     */         case 6:
/* 149 */           k1 ^= UnsignedBytes.toInt(bb.get(5)) << 40L;
/*     */         case 5:
/* 151 */           k1 ^= UnsignedBytes.toInt(bb.get(4)) << 32L;
/*     */         case 4:
/* 153 */           k1 ^= UnsignedBytes.toInt(bb.get(3)) << 24L;
/*     */         case 3:
/* 155 */           k1 ^= UnsignedBytes.toInt(bb.get(2)) << 16L;
/*     */         case 2:
/* 157 */           k1 ^= UnsignedBytes.toInt(bb.get(1)) << 8L;
/*     */         case 1:
/* 159 */           k1 ^= UnsignedBytes.toInt(bb.get(0));
/*     */           break;
/*     */         default:
/* 162 */           throw new AssertionError("Should never get here.");
/*     */       } 
/* 164 */       this.h1 ^= mixK1(k1);
/* 165 */       this.h2 ^= mixK2(k2);
/*     */     }
/*     */ 
/*     */     
/*     */     public HashCode makeHash() {
/* 170 */       this.h1 ^= this.length;
/* 171 */       this.h2 ^= this.length;
/*     */       
/* 173 */       this.h1 += this.h2;
/* 174 */       this.h2 += this.h1;
/*     */       
/* 176 */       this.h1 = fmix64(this.h1);
/* 177 */       this.h2 = fmix64(this.h2);
/*     */       
/* 179 */       this.h1 += this.h2;
/* 180 */       this.h2 += this.h1;
/*     */       
/* 182 */       return HashCode.fromBytesNoCopy(
/* 183 */           ByteBuffer.wrap(new byte[16])
/* 184 */           .order(ByteOrder.LITTLE_ENDIAN)
/* 185 */           .putLong(this.h1)
/* 186 */           .putLong(this.h2)
/* 187 */           .array());
/*     */     }
/*     */     
/*     */     private static long fmix64(long k) {
/* 191 */       k ^= k >>> 33L;
/* 192 */       k *= -49064778989728563L;
/* 193 */       k ^= k >>> 33L;
/* 194 */       k *= -4265267296055464877L;
/* 195 */       k ^= k >>> 33L;
/* 196 */       return k;
/*     */     }
/*     */     
/*     */     private static long mixK1(long k1) {
/* 200 */       k1 *= -8663945395140668459L;
/* 201 */       k1 = Long.rotateLeft(k1, 31);
/* 202 */       k1 *= 5545529020109919103L;
/* 203 */       return k1;
/*     */     }
/*     */     
/*     */     private static long mixK2(long k2) {
/* 207 */       k2 *= 5545529020109919103L;
/* 208 */       k2 = Long.rotateLeft(k2, 33);
/* 209 */       k2 *= -8663945395140668459L;
/* 210 */       return k2;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\google\common\hash\Murmur3_128HashFunction.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */