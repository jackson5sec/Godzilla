/*     */ package com.google.common.hash;
/*     */ 
/*     */ import com.google.common.annotations.Beta;
/*     */ import com.google.common.annotations.VisibleForTesting;
/*     */ import com.google.common.base.Objects;
/*     */ import com.google.common.base.Preconditions;
/*     */ import com.google.common.base.Predicate;
/*     */ import com.google.common.math.DoubleMath;
/*     */ import com.google.common.primitives.SignedBytes;
/*     */ import com.google.common.primitives.UnsignedBytes;
/*     */ import com.google.errorprone.annotations.CanIgnoreReturnValue;
/*     */ import java.io.DataInputStream;
/*     */ import java.io.DataOutputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.OutputStream;
/*     */ import java.io.Serializable;
/*     */ import java.math.RoundingMode;
/*     */ import java.util.stream.Collector;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @Beta
/*     */ public final class BloomFilter<T>
/*     */   implements Predicate<T>, Serializable
/*     */ {
/*     */   private final BloomFilterStrategies.LockFreeBitArray bits;
/*     */   private final int numHashFunctions;
/*     */   private final Funnel<? super T> funnel;
/*     */   private final Strategy strategy;
/*     */   
/*     */   private BloomFilter(BloomFilterStrategies.LockFreeBitArray bits, int numHashFunctions, Funnel<? super T> funnel, Strategy strategy) {
/* 115 */     Preconditions.checkArgument((numHashFunctions > 0), "numHashFunctions (%s) must be > 0", numHashFunctions);
/* 116 */     Preconditions.checkArgument((numHashFunctions <= 255), "numHashFunctions (%s) must be <= 255", numHashFunctions);
/*     */     
/* 118 */     this.bits = (BloomFilterStrategies.LockFreeBitArray)Preconditions.checkNotNull(bits);
/* 119 */     this.numHashFunctions = numHashFunctions;
/* 120 */     this.funnel = (Funnel<? super T>)Preconditions.checkNotNull(funnel);
/* 121 */     this.strategy = (Strategy)Preconditions.checkNotNull(strategy);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public BloomFilter<T> copy() {
/* 131 */     return new BloomFilter(this.bits.copy(), this.numHashFunctions, this.funnel, this.strategy);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean mightContain(T object) {
/* 139 */     return this.strategy.mightContain(object, this.funnel, this.numHashFunctions, this.bits);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   public boolean apply(T input) {
/* 149 */     return mightContain(input);
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
/*     */   @CanIgnoreReturnValue
/*     */   public boolean put(T object) {
/* 165 */     return this.strategy.put(object, this.funnel, this.numHashFunctions, this.bits);
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
/*     */   public double expectedFpp() {
/* 181 */     return Math.pow(this.bits.bitCount() / bitSize(), this.numHashFunctions);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public long approximateElementCount() {
/* 192 */     long bitSize = this.bits.bitSize();
/* 193 */     long bitCount = this.bits.bitCount();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 201 */     double fractionOfBitsSet = bitCount / bitSize;
/* 202 */     return DoubleMath.roundToLong(
/* 203 */         -Math.log1p(-fractionOfBitsSet) * bitSize / this.numHashFunctions, RoundingMode.HALF_UP);
/*     */   }
/*     */ 
/*     */   
/*     */   @VisibleForTesting
/*     */   long bitSize() {
/* 209 */     return this.bits.bitSize();
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
/*     */   public boolean isCompatible(BloomFilter<T> that) {
/* 228 */     Preconditions.checkNotNull(that);
/* 229 */     return (this != that && this.numHashFunctions == that.numHashFunctions && 
/*     */       
/* 231 */       bitSize() == that.bitSize() && this.strategy
/* 232 */       .equals(that.strategy) && this.funnel
/* 233 */       .equals(that.funnel));
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
/*     */   public void putAll(BloomFilter<T> that) {
/* 246 */     Preconditions.checkNotNull(that);
/* 247 */     Preconditions.checkArgument((this != that), "Cannot combine a BloomFilter with itself.");
/* 248 */     Preconditions.checkArgument((this.numHashFunctions == that.numHashFunctions), "BloomFilters must have the same number of hash functions (%s != %s)", this.numHashFunctions, that.numHashFunctions);
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 253 */     Preconditions.checkArgument(
/* 254 */         (bitSize() == that.bitSize()), "BloomFilters must have the same size underlying bit arrays (%s != %s)", 
/*     */         
/* 256 */         bitSize(), that
/* 257 */         .bitSize());
/* 258 */     Preconditions.checkArgument(this.strategy
/* 259 */         .equals(that.strategy), "BloomFilters must have equal strategies (%s != %s)", this.strategy, that.strategy);
/*     */ 
/*     */ 
/*     */     
/* 263 */     Preconditions.checkArgument(this.funnel
/* 264 */         .equals(that.funnel), "BloomFilters must have equal funnels (%s != %s)", this.funnel, that.funnel);
/*     */ 
/*     */ 
/*     */     
/* 268 */     this.bits.putAll(that.bits);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean equals(Object object) {
/* 273 */     if (object == this) {
/* 274 */       return true;
/*     */     }
/* 276 */     if (object instanceof BloomFilter) {
/* 277 */       BloomFilter<?> that = (BloomFilter)object;
/* 278 */       return (this.numHashFunctions == that.numHashFunctions && this.funnel
/* 279 */         .equals(that.funnel) && this.bits
/* 280 */         .equals(that.bits) && this.strategy
/* 281 */         .equals(that.strategy));
/*     */     } 
/* 283 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 288 */     return Objects.hashCode(new Object[] { Integer.valueOf(this.numHashFunctions), this.funnel, this.strategy, this.bits });
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
/*     */   public static <T> Collector<T, ?, BloomFilter<T>> toBloomFilter(Funnel<? super T> funnel, long expectedInsertions) {
/* 314 */     return toBloomFilter(funnel, expectedInsertions, 0.03D);
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
/*     */   public static <T> Collector<T, ?, BloomFilter<T>> toBloomFilter(Funnel<? super T> funnel, long expectedInsertions, double fpp) {
/* 341 */     Preconditions.checkNotNull(funnel);
/* 342 */     Preconditions.checkArgument((expectedInsertions >= 0L), "Expected insertions (%s) must be >= 0", expectedInsertions);
/*     */     
/* 344 */     Preconditions.checkArgument((fpp > 0.0D), "False positive probability (%s) must be > 0.0", Double.valueOf(fpp));
/* 345 */     Preconditions.checkArgument((fpp < 1.0D), "False positive probability (%s) must be < 1.0", Double.valueOf(fpp));
/* 346 */     return (Collector)Collector.of(() -> create(funnel, expectedInsertions, fpp), BloomFilter::put, (bf1, bf2) -> { bf1.putAll(bf2); return bf1; }new Collector.Characteristics[] { Collector.Characteristics.UNORDERED, Collector.Characteristics.CONCURRENT });
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
/*     */   public static <T> BloomFilter<T> create(Funnel<? super T> funnel, int expectedInsertions, double fpp) {
/* 379 */     return create(funnel, expectedInsertions, fpp);
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
/*     */   public static <T> BloomFilter<T> create(Funnel<? super T> funnel, long expectedInsertions, double fpp) {
/* 405 */     return create(funnel, expectedInsertions, fpp, BloomFilterStrategies.MURMUR128_MITZ_64);
/*     */   }
/*     */ 
/*     */   
/*     */   @VisibleForTesting
/*     */   static <T> BloomFilter<T> create(Funnel<? super T> funnel, long expectedInsertions, double fpp, Strategy strategy) {
/* 411 */     Preconditions.checkNotNull(funnel);
/* 412 */     Preconditions.checkArgument((expectedInsertions >= 0L), "Expected insertions (%s) must be >= 0", expectedInsertions);
/*     */     
/* 414 */     Preconditions.checkArgument((fpp > 0.0D), "False positive probability (%s) must be > 0.0", Double.valueOf(fpp));
/* 415 */     Preconditions.checkArgument((fpp < 1.0D), "False positive probability (%s) must be < 1.0", Double.valueOf(fpp));
/* 416 */     Preconditions.checkNotNull(strategy);
/*     */     
/* 418 */     if (expectedInsertions == 0L) {
/* 419 */       expectedInsertions = 1L;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 426 */     long numBits = optimalNumOfBits(expectedInsertions, fpp);
/* 427 */     int numHashFunctions = optimalNumOfHashFunctions(expectedInsertions, numBits);
/*     */     try {
/* 429 */       return new BloomFilter<>(new BloomFilterStrategies.LockFreeBitArray(numBits), numHashFunctions, funnel, strategy);
/* 430 */     } catch (IllegalArgumentException e) {
/* 431 */       throw new IllegalArgumentException("Could not create BloomFilter of " + numBits + " bits", e);
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <T> BloomFilter<T> create(Funnel<? super T> funnel, int expectedInsertions) {
/* 455 */     return create(funnel, expectedInsertions);
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
/*     */   public static <T> BloomFilter<T> create(Funnel<? super T> funnel, long expectedInsertions) {
/* 479 */     return create(funnel, expectedInsertions, 0.03D);
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
/*     */   @VisibleForTesting
/*     */   static int optimalNumOfHashFunctions(long n, long m) {
/* 506 */     return Math.max(1, (int)Math.round(m / n * Math.log(2.0D)));
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
/*     */   @VisibleForTesting
/*     */   static long optimalNumOfBits(long n, double p) {
/* 521 */     if (p == 0.0D) {
/* 522 */       p = Double.MIN_VALUE;
/*     */     }
/* 524 */     return (long)(-n * Math.log(p) / Math.log(2.0D) * Math.log(2.0D));
/*     */   }
/*     */   
/*     */   private Object writeReplace() {
/* 528 */     return new SerialForm<>(this);
/*     */   }
/*     */   
/*     */   private static class SerialForm<T> implements Serializable { final long[] data;
/*     */     final int numHashFunctions;
/*     */     final Funnel<? super T> funnel;
/*     */     final BloomFilter.Strategy strategy;
/*     */     private static final long serialVersionUID = 1L;
/*     */     
/*     */     SerialForm(BloomFilter<T> bf) {
/* 538 */       this.data = BloomFilterStrategies.LockFreeBitArray.toPlainArray(bf.bits.data);
/* 539 */       this.numHashFunctions = bf.numHashFunctions;
/* 540 */       this.funnel = bf.funnel;
/* 541 */       this.strategy = bf.strategy;
/*     */     }
/*     */     
/*     */     Object readResolve() {
/* 545 */       return new BloomFilter(new BloomFilterStrategies.LockFreeBitArray(this.data), this.numHashFunctions, this.funnel, this.strategy);
/*     */     } }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void writeTo(OutputStream out) throws IOException {
/* 564 */     DataOutputStream dout = new DataOutputStream(out);
/* 565 */     dout.writeByte(SignedBytes.checkedCast(this.strategy.ordinal()));
/* 566 */     dout.writeByte(UnsignedBytes.checkedCast(this.numHashFunctions));
/* 567 */     dout.writeInt(this.bits.data.length());
/* 568 */     for (int i = 0; i < this.bits.data.length(); i++) {
/* 569 */       dout.writeLong(this.bits.data.get(i));
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
/*     */   public static <T> BloomFilter<T> readFrom(InputStream in, Funnel<? super T> funnel) throws IOException {
/* 586 */     Preconditions.checkNotNull(in, "InputStream");
/* 587 */     Preconditions.checkNotNull(funnel, "Funnel");
/* 588 */     int strategyOrdinal = -1;
/* 589 */     int numHashFunctions = -1;
/* 590 */     int dataLength = -1;
/*     */     try {
/* 592 */       DataInputStream din = new DataInputStream(in);
/*     */ 
/*     */ 
/*     */       
/* 596 */       strategyOrdinal = din.readByte();
/* 597 */       numHashFunctions = UnsignedBytes.toInt(din.readByte());
/* 598 */       dataLength = din.readInt();
/*     */       
/* 600 */       Strategy strategy = BloomFilterStrategies.values()[strategyOrdinal];
/* 601 */       long[] data = new long[dataLength];
/* 602 */       for (int i = 0; i < data.length; i++) {
/* 603 */         data[i] = din.readLong();
/*     */       }
/* 605 */       return new BloomFilter<>(new BloomFilterStrategies.LockFreeBitArray(data), numHashFunctions, funnel, strategy);
/* 606 */     } catch (RuntimeException e) {
/* 607 */       String message = "Unable to deserialize BloomFilter from InputStream. strategyOrdinal: " + strategyOrdinal + " numHashFunctions: " + numHashFunctions + " dataLength: " + dataLength;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 615 */       throw new IOException(message, e);
/*     */     } 
/*     */   }
/*     */   
/*     */   static interface Strategy extends Serializable {
/*     */     <T> boolean put(T param1T, Funnel<? super T> param1Funnel, int param1Int, BloomFilterStrategies.LockFreeBitArray param1LockFreeBitArray);
/*     */     
/*     */     <T> boolean mightContain(T param1T, Funnel<? super T> param1Funnel, int param1Int, BloomFilterStrategies.LockFreeBitArray param1LockFreeBitArray);
/*     */     
/*     */     int ordinal();
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\google\common\hash\BloomFilter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */