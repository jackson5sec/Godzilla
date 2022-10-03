/*     */ package com.google.common.hash;
/*     */ 
/*     */ import com.google.common.base.Charsets;
/*     */ import com.google.common.base.Preconditions;
/*     */ import com.google.common.primitives.Ints;
/*     */ import com.google.common.primitives.UnsignedBytes;
/*     */ import com.google.errorprone.annotations.CanIgnoreReturnValue;
/*     */ import com.google.errorprone.annotations.Immutable;
/*     */ import java.io.Serializable;
/*     */ import java.nio.ByteBuffer;
/*     */ import java.nio.ByteOrder;
/*     */ import java.nio.charset.Charset;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
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
/*     */ final class Murmur3_32HashFunction
/*     */   extends AbstractHashFunction
/*     */   implements Serializable
/*     */ {
/*  55 */   static final HashFunction MURMUR3_32 = new Murmur3_32HashFunction(0);
/*     */   
/*  57 */   static final HashFunction GOOD_FAST_HASH_32 = new Murmur3_32HashFunction(Hashing.GOOD_FAST_HASH_SEED);
/*     */   
/*     */   private static final int CHUNK_SIZE = 4;
/*     */   
/*     */   private static final int C1 = -862048943;
/*     */   
/*     */   private static final int C2 = 461845907;
/*     */   private final int seed;
/*     */   private static final long serialVersionUID = 0L;
/*     */   
/*     */   Murmur3_32HashFunction(int seed) {
/*  68 */     this.seed = seed;
/*     */   }
/*     */ 
/*     */   
/*     */   public int bits() {
/*  73 */     return 32;
/*     */   }
/*     */ 
/*     */   
/*     */   public Hasher newHasher() {
/*  78 */     return new Murmur3_32Hasher(this.seed);
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/*  83 */     return "Hashing.murmur3_32(" + this.seed + ")";
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean equals(Object object) {
/*  88 */     if (object instanceof Murmur3_32HashFunction) {
/*  89 */       Murmur3_32HashFunction other = (Murmur3_32HashFunction)object;
/*  90 */       return (this.seed == other.seed);
/*     */     } 
/*  92 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public int hashCode() {
/*  97 */     return getClass().hashCode() ^ this.seed;
/*     */   }
/*     */ 
/*     */   
/*     */   public HashCode hashInt(int input) {
/* 102 */     int k1 = mixK1(input);
/* 103 */     int h1 = mixH1(this.seed, k1);
/*     */     
/* 105 */     return fmix(h1, 4);
/*     */   }
/*     */ 
/*     */   
/*     */   public HashCode hashLong(long input) {
/* 110 */     int low = (int)input;
/* 111 */     int high = (int)(input >>> 32L);
/*     */     
/* 113 */     int k1 = mixK1(low);
/* 114 */     int h1 = mixH1(this.seed, k1);
/*     */     
/* 116 */     k1 = mixK1(high);
/* 117 */     h1 = mixH1(h1, k1);
/*     */     
/* 119 */     return fmix(h1, 8);
/*     */   }
/*     */ 
/*     */   
/*     */   public HashCode hashUnencodedChars(CharSequence input) {
/* 124 */     int h1 = this.seed;
/*     */ 
/*     */     
/* 127 */     for (int i = 1; i < input.length(); i += 2) {
/* 128 */       int k1 = input.charAt(i - 1) | input.charAt(i) << 16;
/* 129 */       k1 = mixK1(k1);
/* 130 */       h1 = mixH1(h1, k1);
/*     */     } 
/*     */ 
/*     */     
/* 134 */     if ((input.length() & 0x1) == 1) {
/* 135 */       int k1 = input.charAt(input.length() - 1);
/* 136 */       k1 = mixK1(k1);
/* 137 */       h1 ^= k1;
/*     */     } 
/*     */     
/* 140 */     return fmix(h1, 2 * input.length());
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public HashCode hashString(CharSequence input, Charset charset) {
/* 146 */     if (Charsets.UTF_8.equals(charset)) {
/* 147 */       int utf16Length = input.length();
/* 148 */       int h1 = this.seed;
/* 149 */       int i = 0;
/* 150 */       int len = 0;
/*     */ 
/*     */       
/* 153 */       while (i + 4 <= utf16Length) {
/* 154 */         char c0 = input.charAt(i);
/* 155 */         char c1 = input.charAt(i + 1);
/* 156 */         char c2 = input.charAt(i + 2);
/* 157 */         char c3 = input.charAt(i + 3);
/* 158 */         if (c0 < '' && c1 < '' && c2 < '' && c3 < '') {
/* 159 */           int j = c0 | c1 << 8 | c2 << 16 | c3 << 24;
/* 160 */           j = mixK1(j);
/* 161 */           h1 = mixH1(h1, j);
/* 162 */           i += 4;
/* 163 */           len += 4;
/*     */         } 
/*     */       } 
/*     */ 
/*     */ 
/*     */       
/* 169 */       long buffer = 0L;
/* 170 */       int shift = 0;
/* 171 */       for (; i < utf16Length; i++) {
/* 172 */         char c = input.charAt(i);
/* 173 */         if (c < '') {
/* 174 */           buffer |= c << shift;
/* 175 */           shift += 8;
/* 176 */           len++;
/* 177 */         } else if (c < 'ࠀ') {
/* 178 */           buffer |= charToTwoUtf8Bytes(c) << shift;
/* 179 */           shift += 16;
/* 180 */           len += 2;
/* 181 */         } else if (c < '?' || c > '?') {
/* 182 */           buffer |= charToThreeUtf8Bytes(c) << shift;
/* 183 */           shift += 24;
/* 184 */           len += 3;
/*     */         } else {
/* 186 */           int codePoint = Character.codePointAt(input, i);
/* 187 */           if (codePoint == c)
/*     */           {
/* 189 */             return hashBytes(input.toString().getBytes(charset));
/*     */           }
/* 191 */           i++;
/* 192 */           buffer |= codePointToFourUtf8Bytes(codePoint) << shift;
/* 193 */           len += 4;
/*     */         } 
/*     */         
/* 196 */         if (shift >= 32) {
/* 197 */           int j = mixK1((int)buffer);
/* 198 */           h1 = mixH1(h1, j);
/* 199 */           buffer >>>= 32L;
/* 200 */           shift -= 32;
/*     */         } 
/*     */       } 
/*     */       
/* 204 */       int k1 = mixK1((int)buffer);
/* 205 */       h1 ^= k1;
/* 206 */       return fmix(h1, len);
/*     */     } 
/* 208 */     return hashBytes(input.toString().getBytes(charset));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public HashCode hashBytes(byte[] input, int off, int len) {
/* 214 */     Preconditions.checkPositionIndexes(off, off + len, input.length);
/* 215 */     int h1 = this.seed;
/*     */     int i;
/* 217 */     for (i = 0; i + 4 <= len; i += 4) {
/* 218 */       int j = mixK1(getIntLittleEndian(input, off + i));
/* 219 */       h1 = mixH1(h1, j);
/*     */     } 
/*     */     
/* 222 */     int k1 = 0;
/* 223 */     for (int shift = 0; i < len; i++, shift += 8) {
/* 224 */       k1 ^= UnsignedBytes.toInt(input[off + i]) << shift;
/*     */     }
/* 226 */     h1 ^= mixK1(k1);
/* 227 */     return fmix(h1, len);
/*     */   }
/*     */   
/*     */   private static int getIntLittleEndian(byte[] input, int offset) {
/* 231 */     return Ints.fromBytes(input[offset + 3], input[offset + 2], input[offset + 1], input[offset]);
/*     */   }
/*     */   
/*     */   private static int mixK1(int k1) {
/* 235 */     k1 *= -862048943;
/* 236 */     k1 = Integer.rotateLeft(k1, 15);
/* 237 */     k1 *= 461845907;
/* 238 */     return k1;
/*     */   }
/*     */   
/*     */   private static int mixH1(int h1, int k1) {
/* 242 */     h1 ^= k1;
/* 243 */     h1 = Integer.rotateLeft(h1, 13);
/* 244 */     h1 = h1 * 5 + -430675100;
/* 245 */     return h1;
/*     */   }
/*     */ 
/*     */   
/*     */   private static HashCode fmix(int h1, int length) {
/* 250 */     h1 ^= length;
/* 251 */     h1 ^= h1 >>> 16;
/* 252 */     h1 *= -2048144789;
/* 253 */     h1 ^= h1 >>> 13;
/* 254 */     h1 *= -1028477387;
/* 255 */     h1 ^= h1 >>> 16;
/* 256 */     return HashCode.fromInt(h1);
/*     */   }
/*     */   
/*     */   @CanIgnoreReturnValue
/*     */   private static final class Murmur3_32Hasher extends AbstractHasher {
/*     */     private int h1;
/*     */     private long buffer;
/*     */     private int shift;
/*     */     private int length;
/*     */     private boolean isDone;
/*     */     
/*     */     Murmur3_32Hasher(int seed) {
/* 268 */       this.h1 = seed;
/* 269 */       this.length = 0;
/* 270 */       this.isDone = false;
/*     */     }
/*     */ 
/*     */     
/*     */     private void update(int nBytes, long update) {
/* 275 */       this.buffer |= (update & 0xFFFFFFFFL) << this.shift;
/* 276 */       this.shift += nBytes * 8;
/* 277 */       this.length += nBytes;
/*     */       
/* 279 */       if (this.shift >= 32) {
/* 280 */         this.h1 = Murmur3_32HashFunction.mixH1(this.h1, Murmur3_32HashFunction.mixK1((int)this.buffer));
/* 281 */         this.buffer >>>= 32L;
/* 282 */         this.shift -= 32;
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public Hasher putByte(byte b) {
/* 288 */       update(1, (b & 0xFF));
/* 289 */       return this;
/*     */     }
/*     */ 
/*     */     
/*     */     public Hasher putBytes(byte[] bytes, int off, int len) {
/* 294 */       Preconditions.checkPositionIndexes(off, off + len, bytes.length);
/*     */       int i;
/* 296 */       for (i = 0; i + 4 <= len; i += 4) {
/* 297 */         update(4, Murmur3_32HashFunction.getIntLittleEndian(bytes, off + i));
/*     */       }
/* 299 */       for (; i < len; i++) {
/* 300 */         putByte(bytes[off + i]);
/*     */       }
/* 302 */       return this;
/*     */     }
/*     */ 
/*     */     
/*     */     public Hasher putBytes(ByteBuffer buffer) {
/* 307 */       ByteOrder bo = buffer.order();
/* 308 */       buffer.order(ByteOrder.LITTLE_ENDIAN);
/* 309 */       while (buffer.remaining() >= 4) {
/* 310 */         putInt(buffer.getInt());
/*     */       }
/* 312 */       while (buffer.hasRemaining()) {
/* 313 */         putByte(buffer.get());
/*     */       }
/* 315 */       buffer.order(bo);
/* 316 */       return this;
/*     */     }
/*     */ 
/*     */     
/*     */     public Hasher putInt(int i) {
/* 321 */       update(4, i);
/* 322 */       return this;
/*     */     }
/*     */ 
/*     */     
/*     */     public Hasher putLong(long l) {
/* 327 */       update(4, (int)l);
/* 328 */       update(4, l >>> 32L);
/* 329 */       return this;
/*     */     }
/*     */ 
/*     */     
/*     */     public Hasher putChar(char c) {
/* 334 */       update(2, c);
/* 335 */       return this;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public Hasher putString(CharSequence input, Charset charset) {
/* 341 */       if (Charsets.UTF_8.equals(charset)) {
/* 342 */         int utf16Length = input.length();
/* 343 */         int i = 0;
/*     */ 
/*     */         
/* 346 */         while (i + 4 <= utf16Length) {
/* 347 */           char c0 = input.charAt(i);
/* 348 */           char c1 = input.charAt(i + 1);
/* 349 */           char c2 = input.charAt(i + 2);
/* 350 */           char c3 = input.charAt(i + 3);
/* 351 */           if (c0 < '' && c1 < '' && c2 < '' && c3 < '') {
/* 352 */             update(4, (c0 | c1 << 8 | c2 << 16 | c3 << 24));
/* 353 */             i += 4;
/*     */           } 
/*     */         } 
/*     */ 
/*     */ 
/*     */         
/* 359 */         for (; i < utf16Length; i++) {
/* 360 */           char c = input.charAt(i);
/* 361 */           if (c < '') {
/* 362 */             update(1, c);
/* 363 */           } else if (c < 'ࠀ') {
/* 364 */             update(2, Murmur3_32HashFunction.charToTwoUtf8Bytes(c));
/* 365 */           } else if (c < '?' || c > '?') {
/* 366 */             update(3, Murmur3_32HashFunction.charToThreeUtf8Bytes(c));
/*     */           } else {
/* 368 */             int codePoint = Character.codePointAt(input, i);
/* 369 */             if (codePoint == c) {
/*     */               
/* 371 */               putBytes(input.subSequence(i, utf16Length).toString().getBytes(charset));
/* 372 */               return this;
/*     */             } 
/* 374 */             i++;
/* 375 */             update(4, Murmur3_32HashFunction.codePointToFourUtf8Bytes(codePoint));
/*     */           } 
/*     */         } 
/* 378 */         return this;
/*     */       } 
/* 380 */       return super.putString(input, charset);
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public HashCode hash() {
/* 386 */       Preconditions.checkState(!this.isDone);
/* 387 */       this.isDone = true;
/* 388 */       this.h1 ^= Murmur3_32HashFunction.mixK1((int)this.buffer);
/* 389 */       return Murmur3_32HashFunction.fmix(this.h1, this.length);
/*     */     }
/*     */   }
/*     */   
/*     */   private static long codePointToFourUtf8Bytes(int codePoint) {
/* 394 */     return (0xF0L | (codePoint >>> 18)) & 0xFFL | (0x80L | (0x3F & codePoint >>> 12)) << 8L | (0x80L | (0x3F & codePoint >>> 6)) << 16L | (0x80L | (0x3F & codePoint)) << 24L;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static long charToThreeUtf8Bytes(char c) {
/* 401 */     return ((0x1E0 | c >>> 12) & 0xFF | (0x80 | 0x3F & c >>> 6) << 8 | (0x80 | 0x3F & c) << 16);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static long charToTwoUtf8Bytes(char c) {
/* 407 */     return ((0x3C0 | c >>> 6) & 0xFF | (0x80 | 0x3F & c) << 8);
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\google\common\hash\Murmur3_32HashFunction.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */