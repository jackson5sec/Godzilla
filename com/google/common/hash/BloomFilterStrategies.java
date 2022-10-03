/*     */ package com.google.common.hash;
/*     */ 
/*     */ import com.google.common.base.Preconditions;
/*     */ import com.google.common.math.LongMath;
/*     */ import com.google.common.primitives.Ints;
/*     */ import com.google.common.primitives.Longs;
/*     */ import java.math.RoundingMode;
/*     */ import java.util.Arrays;
/*     */ import java.util.concurrent.atomic.AtomicLongArray;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ enum BloomFilterStrategies
/*     */   implements BloomFilter.Strategy
/*     */ {
/*  45 */   MURMUR128_MITZ_32
/*     */   {
/*     */     public <T> boolean put(T object, Funnel<? super T> funnel, int numHashFunctions, LockFreeBitArray bits)
/*     */     {
/*  49 */       long bitSize = bits.bitSize();
/*  50 */       long hash64 = Hashing.murmur3_128().<T>hashObject(object, funnel).asLong();
/*  51 */       int hash1 = (int)hash64;
/*  52 */       int hash2 = (int)(hash64 >>> 32L);
/*     */       
/*  54 */       boolean bitsChanged = false;
/*  55 */       for (int i = 1; i <= numHashFunctions; i++) {
/*  56 */         int combinedHash = hash1 + i * hash2;
/*     */         
/*  58 */         if (combinedHash < 0) {
/*  59 */           combinedHash ^= 0xFFFFFFFF;
/*     */         }
/*  61 */         bitsChanged |= bits.set(combinedHash % bitSize);
/*     */       } 
/*  63 */       return bitsChanged;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public <T> boolean mightContain(T object, Funnel<? super T> funnel, int numHashFunctions, LockFreeBitArray bits) {
/*  69 */       long bitSize = bits.bitSize();
/*  70 */       long hash64 = Hashing.murmur3_128().<T>hashObject(object, funnel).asLong();
/*  71 */       int hash1 = (int)hash64;
/*  72 */       int hash2 = (int)(hash64 >>> 32L);
/*     */       
/*  74 */       for (int i = 1; i <= numHashFunctions; i++) {
/*  75 */         int combinedHash = hash1 + i * hash2;
/*     */         
/*  77 */         if (combinedHash < 0) {
/*  78 */           combinedHash ^= 0xFFFFFFFF;
/*     */         }
/*  80 */         if (!bits.get(combinedHash % bitSize)) {
/*  81 */           return false;
/*     */         }
/*     */       } 
/*  84 */       return true;
/*     */     }
/*     */   },
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  93 */   MURMUR128_MITZ_64
/*     */   {
/*     */     public <T> boolean put(T object, Funnel<? super T> funnel, int numHashFunctions, LockFreeBitArray bits)
/*     */     {
/*  97 */       long bitSize = bits.bitSize();
/*  98 */       byte[] bytes = Hashing.murmur3_128().<T>hashObject(object, funnel).getBytesInternal();
/*  99 */       long hash1 = lowerEight(bytes);
/* 100 */       long hash2 = upperEight(bytes);
/*     */       
/* 102 */       boolean bitsChanged = false;
/* 103 */       long combinedHash = hash1;
/* 104 */       for (int i = 0; i < numHashFunctions; i++) {
/*     */         
/* 106 */         bitsChanged |= bits.set((combinedHash & Long.MAX_VALUE) % bitSize);
/* 107 */         combinedHash += hash2;
/*     */       } 
/* 109 */       return bitsChanged;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public <T> boolean mightContain(T object, Funnel<? super T> funnel, int numHashFunctions, LockFreeBitArray bits) {
/* 115 */       long bitSize = bits.bitSize();
/* 116 */       byte[] bytes = Hashing.murmur3_128().<T>hashObject(object, funnel).getBytesInternal();
/* 117 */       long hash1 = lowerEight(bytes);
/* 118 */       long hash2 = upperEight(bytes);
/*     */       
/* 120 */       long combinedHash = hash1;
/* 121 */       for (int i = 0; i < numHashFunctions; i++) {
/*     */         
/* 123 */         if (!bits.get((combinedHash & Long.MAX_VALUE) % bitSize)) {
/* 124 */           return false;
/*     */         }
/* 126 */         combinedHash += hash2;
/*     */       } 
/* 128 */       return true;
/*     */     }
/*     */     
/*     */     private long lowerEight(byte[] bytes) {
/* 132 */       return Longs.fromBytes(bytes[7], bytes[6], bytes[5], bytes[4], bytes[3], bytes[2], bytes[1], bytes[0]);
/*     */     }
/*     */ 
/*     */     
/*     */     private long upperEight(byte[] bytes) {
/* 137 */       return Longs.fromBytes(bytes[15], bytes[14], bytes[13], bytes[12], bytes[11], bytes[10], bytes[9], bytes[8]);
/*     */     }
/*     */   };
/*     */ 
/*     */ 
/*     */   
/*     */   static final class LockFreeBitArray
/*     */   {
/*     */     private static final int LONG_ADDRESSABLE_BITS = 6;
/*     */ 
/*     */     
/*     */     final AtomicLongArray data;
/*     */     
/*     */     private final LongAddable bitCount;
/*     */ 
/*     */     
/*     */     LockFreeBitArray(long bits) {
/* 154 */       this(new long[Ints.checkedCast(LongMath.divide(bits, 64L, RoundingMode.CEILING))]);
/*     */     }
/*     */ 
/*     */     
/*     */     LockFreeBitArray(long[] data) {
/* 159 */       Preconditions.checkArgument((data.length > 0), "data length is zero!");
/* 160 */       this.data = new AtomicLongArray(data);
/* 161 */       this.bitCount = LongAddables.create();
/* 162 */       long bitCount = 0L;
/* 163 */       for (long value : data) {
/* 164 */         bitCount += Long.bitCount(value);
/*     */       }
/* 166 */       this.bitCount.add(bitCount);
/*     */     }
/*     */     
/*     */     boolean set(long bitIndex) {
/*     */       long oldValue, newValue;
/* 171 */       if (get(bitIndex)) {
/* 172 */         return false;
/*     */       }
/*     */       
/* 175 */       int longIndex = (int)(bitIndex >>> 6L);
/* 176 */       long mask = 1L << (int)bitIndex;
/*     */ 
/*     */ 
/*     */       
/*     */       do {
/* 181 */         oldValue = this.data.get(longIndex);
/* 182 */         newValue = oldValue | mask;
/* 183 */         if (oldValue == newValue) {
/* 184 */           return false;
/*     */         }
/* 186 */       } while (!this.data.compareAndSet(longIndex, oldValue, newValue));
/*     */ 
/*     */       
/* 189 */       this.bitCount.increment();
/* 190 */       return true;
/*     */     }
/*     */     
/*     */     boolean get(long bitIndex) {
/* 194 */       return ((this.data.get((int)(bitIndex >>> 6L)) & 1L << (int)bitIndex) != 0L);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public static long[] toPlainArray(AtomicLongArray atomicLongArray) {
/* 203 */       long[] array = new long[atomicLongArray.length()];
/* 204 */       for (int i = 0; i < array.length; i++) {
/* 205 */         array[i] = atomicLongArray.get(i);
/*     */       }
/* 207 */       return array;
/*     */     }
/*     */ 
/*     */     
/*     */     long bitSize() {
/* 212 */       return this.data.length() * 64L;
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
/*     */     long bitCount() {
/* 224 */       return this.bitCount.sum();
/*     */     }
/*     */     
/*     */     LockFreeBitArray copy() {
/* 228 */       return new LockFreeBitArray(toPlainArray(this.data));
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
/*     */     void putAll(LockFreeBitArray other) {
/* 241 */       Preconditions.checkArgument(
/* 242 */           (this.data.length() == other.data.length()), "BitArrays must be of equal length (%s != %s)", this.data
/*     */           
/* 244 */           .length(), other.data
/* 245 */           .length());
/* 246 */       for (int i = 0; i < this.data.length(); i++) {
/* 247 */         long ourLongOld, ourLongNew, otherLong = other.data.get(i);
/*     */ 
/*     */ 
/*     */         
/* 251 */         boolean changedAnyBits = true;
/*     */         do {
/* 253 */           ourLongOld = this.data.get(i);
/* 254 */           ourLongNew = ourLongOld | otherLong;
/* 255 */           if (ourLongOld == ourLongNew) {
/* 256 */             changedAnyBits = false;
/*     */             break;
/*     */           } 
/* 259 */         } while (!this.data.compareAndSet(i, ourLongOld, ourLongNew));
/*     */         
/* 261 */         if (changedAnyBits) {
/* 262 */           int bitsAdded = Long.bitCount(ourLongNew) - Long.bitCount(ourLongOld);
/* 263 */           this.bitCount.add(bitsAdded);
/*     */         } 
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean equals(Object o) {
/* 270 */       if (o instanceof LockFreeBitArray) {
/* 271 */         LockFreeBitArray lockFreeBitArray = (LockFreeBitArray)o;
/*     */         
/* 273 */         return Arrays.equals(toPlainArray(this.data), toPlainArray(lockFreeBitArray.data));
/*     */       } 
/* 275 */       return false;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public int hashCode() {
/* 281 */       return Arrays.hashCode(toPlainArray(this.data));
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\google\common\hash\BloomFilterStrategies.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */