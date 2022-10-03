/*     */ package com.google.common.hash;
/*     */ 
/*     */ import com.google.common.annotations.Beta;
/*     */ import com.google.common.base.Preconditions;
/*     */ import com.google.errorprone.annotations.Immutable;
/*     */ import java.security.Key;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.zip.Adler32;
/*     */ import java.util.zip.CRC32;
/*     */ import java.util.zip.Checksum;
/*     */ import javax.crypto.spec.SecretKeySpec;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
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
/*     */ public final class Hashing
/*     */ {
/*     */   public static HashFunction goodFastHash(int minimumBits) {
/*  65 */     int bits = checkPositiveAndMakeMultipleOf32(minimumBits);
/*     */     
/*  67 */     if (bits == 32) {
/*  68 */       return Murmur3_32HashFunction.GOOD_FAST_HASH_32;
/*     */     }
/*  70 */     if (bits <= 128) {
/*  71 */       return Murmur3_128HashFunction.GOOD_FAST_HASH_128;
/*     */     }
/*     */ 
/*     */     
/*  75 */     int hashFunctionsNeeded = (bits + 127) / 128;
/*  76 */     HashFunction[] hashFunctions = new HashFunction[hashFunctionsNeeded];
/*  77 */     hashFunctions[0] = Murmur3_128HashFunction.GOOD_FAST_HASH_128;
/*  78 */     int seed = GOOD_FAST_HASH_SEED;
/*  79 */     for (int i = 1; i < hashFunctionsNeeded; i++) {
/*  80 */       seed += 1500450271;
/*  81 */       hashFunctions[i] = murmur3_128(seed);
/*     */     } 
/*  83 */     return new ConcatenatedHashFunction(hashFunctions);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  91 */   static final int GOOD_FAST_HASH_SEED = (int)System.currentTimeMillis();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static HashFunction murmur3_32(int seed) {
/* 101 */     return new Murmur3_32HashFunction(seed);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static HashFunction murmur3_32() {
/* 112 */     return Murmur3_32HashFunction.MURMUR3_32;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static HashFunction murmur3_128(int seed) {
/* 123 */     return new Murmur3_128HashFunction(seed);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static HashFunction murmur3_128() {
/* 134 */     return Murmur3_128HashFunction.MURMUR3_128;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static HashFunction sipHash24() {
/* 144 */     return SipHashFunction.SIP_HASH_24;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static HashFunction sipHash24(long k0, long k1) {
/* 154 */     return new SipHashFunction(2, 4, k0, k1);
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
/*     */   @Deprecated
/*     */   public static HashFunction md5() {
/* 171 */     return Md5Holder.MD5;
/*     */   }
/*     */   
/*     */   private static class Md5Holder {
/* 175 */     static final HashFunction MD5 = new MessageDigestHashFunction("MD5", "Hashing.md5()");
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
/*     */   @Deprecated
/*     */   public static HashFunction sha1() {
/* 192 */     return Sha1Holder.SHA_1;
/*     */   }
/*     */   
/*     */   private static class Sha1Holder {
/* 196 */     static final HashFunction SHA_1 = new MessageDigestHashFunction("SHA-1", "Hashing.sha1()");
/*     */   }
/*     */ 
/*     */   
/*     */   public static HashFunction sha256() {
/* 201 */     return Sha256Holder.SHA_256;
/*     */   }
/*     */   
/*     */   private static class Sha256Holder {
/* 205 */     static final HashFunction SHA_256 = new MessageDigestHashFunction("SHA-256", "Hashing.sha256()");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static HashFunction sha384() {
/* 215 */     return Sha384Holder.SHA_384;
/*     */   }
/*     */   
/*     */   private static class Sha384Holder {
/* 219 */     static final HashFunction SHA_384 = new MessageDigestHashFunction("SHA-384", "Hashing.sha384()");
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static HashFunction sha512() {
/* 225 */     return Sha512Holder.SHA_512;
/*     */   }
/*     */   
/*     */   private static class Sha512Holder {
/* 229 */     static final HashFunction SHA_512 = new MessageDigestHashFunction("SHA-512", "Hashing.sha512()");
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
/*     */   public static HashFunction hmacMd5(Key key) {
/* 243 */     return new MacHashFunction("HmacMD5", key, hmacToString("hmacMd5", key));
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
/*     */   public static HashFunction hmacMd5(byte[] key) {
/* 256 */     return hmacMd5(new SecretKeySpec((byte[])Preconditions.checkNotNull(key), "HmacMD5"));
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
/*     */   public static HashFunction hmacSha1(Key key) {
/* 269 */     return new MacHashFunction("HmacSHA1", key, hmacToString("hmacSha1", key));
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
/*     */   public static HashFunction hmacSha1(byte[] key) {
/* 282 */     return hmacSha1(new SecretKeySpec((byte[])Preconditions.checkNotNull(key), "HmacSHA1"));
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
/*     */   public static HashFunction hmacSha256(Key key) {
/* 295 */     return new MacHashFunction("HmacSHA256", key, hmacToString("hmacSha256", key));
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
/*     */   public static HashFunction hmacSha256(byte[] key) {
/* 308 */     return hmacSha256(new SecretKeySpec((byte[])Preconditions.checkNotNull(key), "HmacSHA256"));
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
/*     */   public static HashFunction hmacSha512(Key key) {
/* 321 */     return new MacHashFunction("HmacSHA512", key, hmacToString("hmacSha512", key));
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
/*     */   public static HashFunction hmacSha512(byte[] key) {
/* 334 */     return hmacSha512(new SecretKeySpec((byte[])Preconditions.checkNotNull(key), "HmacSHA512"));
/*     */   }
/*     */   
/*     */   private static String hmacToString(String methodName, Key key) {
/* 338 */     return String.format("Hashing.%s(Key[algorithm=%s, format=%s])", new Object[] { methodName, key
/*     */           
/* 340 */           .getAlgorithm(), key.getFormat() });
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
/*     */   public static HashFunction crc32c() {
/* 354 */     return Crc32cHashFunction.CRC_32_C;
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
/*     */   public static HashFunction crc32() {
/* 370 */     return ChecksumType.CRC_32.hashFunction;
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
/*     */   public static HashFunction adler32() {
/* 386 */     return ChecksumType.ADLER_32.hashFunction;
/*     */   }
/*     */   
/*     */   @Immutable
/*     */   enum ChecksumType implements ImmutableSupplier<Checksum> {
/* 391 */     CRC_32("Hashing.crc32()")
/*     */     {
/*     */       public Checksum get() {
/* 394 */         return new CRC32();
/*     */       }
/*     */     },
/* 397 */     ADLER_32("Hashing.adler32()")
/*     */     {
/*     */       public Checksum get() {
/* 400 */         return new Adler32();
/*     */       }
/*     */     };
/*     */     
/*     */     public final HashFunction hashFunction;
/*     */     
/*     */     ChecksumType(String toString) {
/* 407 */       this.hashFunction = new ChecksumHashFunction(this, 32, toString);
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
/*     */   public static HashFunction farmHashFingerprint64() {
/* 430 */     return FarmHashFingerprint64.FARMHASH_FINGERPRINT_64;
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
/*     */   public static int consistentHash(HashCode hashCode, int buckets) {
/* 465 */     return consistentHash(hashCode.padToLong(), buckets);
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
/*     */   public static int consistentHash(long input, int buckets) {
/* 500 */     Preconditions.checkArgument((buckets > 0), "buckets must be positive: %s", buckets);
/* 501 */     LinearCongruentialGenerator generator = new LinearCongruentialGenerator(input);
/* 502 */     int candidate = 0;
/*     */ 
/*     */ 
/*     */     
/*     */     while (true) {
/* 507 */       int next = (int)((candidate + 1) / generator.nextDouble());
/* 508 */       if (next >= 0 && next < buckets) {
/* 509 */         candidate = next; continue;
/*     */       }  break;
/* 511 */     }  return candidate;
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
/*     */   public static HashCode combineOrdered(Iterable<HashCode> hashCodes) {
/* 526 */     Iterator<HashCode> iterator = hashCodes.iterator();
/* 527 */     Preconditions.checkArgument(iterator.hasNext(), "Must be at least 1 hash code to combine.");
/* 528 */     int bits = ((HashCode)iterator.next()).bits();
/* 529 */     byte[] resultBytes = new byte[bits / 8];
/* 530 */     for (HashCode hashCode : hashCodes) {
/* 531 */       byte[] nextBytes = hashCode.asBytes();
/* 532 */       Preconditions.checkArgument((nextBytes.length == resultBytes.length), "All hashcodes must have the same bit length.");
/*     */       
/* 534 */       for (int i = 0; i < nextBytes.length; i++) {
/* 535 */         resultBytes[i] = (byte)(resultBytes[i] * 37 ^ nextBytes[i]);
/*     */       }
/*     */     } 
/* 538 */     return HashCode.fromBytesNoCopy(resultBytes);
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
/*     */   public static HashCode combineUnordered(Iterable<HashCode> hashCodes) {
/* 551 */     Iterator<HashCode> iterator = hashCodes.iterator();
/* 552 */     Preconditions.checkArgument(iterator.hasNext(), "Must be at least 1 hash code to combine.");
/* 553 */     byte[] resultBytes = new byte[((HashCode)iterator.next()).bits() / 8];
/* 554 */     for (HashCode hashCode : hashCodes) {
/* 555 */       byte[] nextBytes = hashCode.asBytes();
/* 556 */       Preconditions.checkArgument((nextBytes.length == resultBytes.length), "All hashcodes must have the same bit length.");
/*     */       
/* 558 */       for (int i = 0; i < nextBytes.length; i++) {
/* 559 */         resultBytes[i] = (byte)(resultBytes[i] + nextBytes[i]);
/*     */       }
/*     */     } 
/* 562 */     return HashCode.fromBytesNoCopy(resultBytes);
/*     */   }
/*     */ 
/*     */   
/*     */   static int checkPositiveAndMakeMultipleOf32(int bits) {
/* 567 */     Preconditions.checkArgument((bits > 0), "Number of bits must be positive");
/* 568 */     return bits + 31 & 0xFFFFFFE0;
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
/*     */   public static HashFunction concatenating(HashFunction first, HashFunction second, HashFunction... rest) {
/* 584 */     List<HashFunction> list = new ArrayList<>();
/* 585 */     list.add(first);
/* 586 */     list.add(second);
/* 587 */     list.addAll(Arrays.asList(rest));
/* 588 */     return new ConcatenatedHashFunction(list.<HashFunction>toArray(new HashFunction[0]));
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
/*     */   public static HashFunction concatenating(Iterable<HashFunction> hashFunctions) {
/* 602 */     Preconditions.checkNotNull(hashFunctions);
/*     */     
/* 604 */     List<HashFunction> list = new ArrayList<>();
/* 605 */     for (HashFunction hashFunction : hashFunctions) {
/* 606 */       list.add(hashFunction);
/*     */     }
/* 608 */     Preconditions.checkArgument((list.size() > 0), "number of hash functions (%s) must be > 0", list.size());
/* 609 */     return new ConcatenatedHashFunction(list.<HashFunction>toArray(new HashFunction[0]));
/*     */   }
/*     */   
/*     */   private static final class ConcatenatedHashFunction
/*     */     extends AbstractCompositeHashFunction {
/*     */     private ConcatenatedHashFunction(HashFunction... functions) {
/* 615 */       super(functions);
/* 616 */       for (HashFunction function : functions) {
/* 617 */         Preconditions.checkArgument(
/* 618 */             (function.bits() % 8 == 0), "the number of bits (%s) in hashFunction (%s) must be divisible by 8", function
/*     */             
/* 620 */             .bits(), function);
/*     */       }
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     HashCode makeHash(Hasher[] hashers) {
/* 627 */       byte[] bytes = new byte[bits() / 8];
/* 628 */       int i = 0;
/* 629 */       for (Hasher hasher : hashers) {
/* 630 */         HashCode newHash = hasher.hash();
/* 631 */         i += newHash.writeBytesTo(bytes, i, newHash.bits() / 8);
/*     */       } 
/* 633 */       return HashCode.fromBytesNoCopy(bytes);
/*     */     }
/*     */ 
/*     */     
/*     */     public int bits() {
/* 638 */       int bitSum = 0;
/* 639 */       for (HashFunction function : this.functions) {
/* 640 */         bitSum += function.bits();
/*     */       }
/* 642 */       return bitSum;
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean equals(Object object) {
/* 647 */       if (object instanceof ConcatenatedHashFunction) {
/* 648 */         ConcatenatedHashFunction other = (ConcatenatedHashFunction)object;
/* 649 */         return Arrays.equals((Object[])this.functions, (Object[])other.functions);
/*     */       } 
/* 651 */       return false;
/*     */     }
/*     */ 
/*     */     
/*     */     public int hashCode() {
/* 656 */       return Arrays.hashCode((Object[])this.functions);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static final class LinearCongruentialGenerator
/*     */   {
/*     */     private long state;
/*     */ 
/*     */     
/*     */     public LinearCongruentialGenerator(long seed) {
/* 668 */       this.state = seed;
/*     */     }
/*     */     
/*     */     public double nextDouble() {
/* 672 */       this.state = 2862933555777941757L * this.state + 1L;
/* 673 */       return ((int)(this.state >>> 33L) + 1) / 2.147483648E9D;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\google\common\hash\Hashing.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */