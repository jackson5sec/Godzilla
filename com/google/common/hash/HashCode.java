/*     */ package com.google.common.hash;
/*     */ 
/*     */ import com.google.common.annotations.Beta;
/*     */ import com.google.common.base.Preconditions;
/*     */ import com.google.common.primitives.Ints;
/*     */ import com.google.common.primitives.UnsignedInts;
/*     */ import com.google.errorprone.annotations.CanIgnoreReturnValue;
/*     */ import java.io.Serializable;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
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
/*     */ public abstract class HashCode
/*     */ {
/*     */   public abstract int bits();
/*     */   
/*     */   public abstract int asInt();
/*     */   
/*     */   public abstract long asLong();
/*     */   
/*     */   public abstract long padToLong();
/*     */   
/*     */   public abstract byte[] asBytes();
/*     */   
/*     */   @CanIgnoreReturnValue
/*     */   public int writeBytesTo(byte[] dest, int offset, int maxLength) {
/*  87 */     maxLength = Ints.min(new int[] { maxLength, bits() / 8 });
/*  88 */     Preconditions.checkPositionIndexes(offset, offset + maxLength, dest.length);
/*  89 */     writeBytesToImpl(dest, offset, maxLength);
/*  90 */     return maxLength;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   abstract void writeBytesToImpl(byte[] paramArrayOfbyte, int paramInt1, int paramInt2);
/*     */ 
/*     */ 
/*     */   
/*     */   byte[] getBytesInternal() {
/* 101 */     return asBytes();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   abstract boolean equalsSameBits(HashCode paramHashCode);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static HashCode fromInt(int hash) {
/* 117 */     return new IntHashCode(hash);
/*     */   }
/*     */   
/*     */   private static final class IntHashCode extends HashCode implements Serializable {
/*     */     final int hash;
/*     */     
/*     */     IntHashCode(int hash) {
/* 124 */       this.hash = hash;
/*     */     }
/*     */     private static final long serialVersionUID = 0L;
/*     */     
/*     */     public int bits() {
/* 129 */       return 32;
/*     */     }
/*     */ 
/*     */     
/*     */     public byte[] asBytes() {
/* 134 */       return new byte[] { (byte)this.hash, (byte)(this.hash >> 8), (byte)(this.hash >> 16), (byte)(this.hash >> 24) };
/*     */     }
/*     */ 
/*     */     
/*     */     public int asInt() {
/* 139 */       return this.hash;
/*     */     }
/*     */ 
/*     */     
/*     */     public long asLong() {
/* 144 */       throw new IllegalStateException("this HashCode only has 32 bits; cannot create a long");
/*     */     }
/*     */ 
/*     */     
/*     */     public long padToLong() {
/* 149 */       return UnsignedInts.toLong(this.hash);
/*     */     }
/*     */ 
/*     */     
/*     */     void writeBytesToImpl(byte[] dest, int offset, int maxLength) {
/* 154 */       for (int i = 0; i < maxLength; i++) {
/* 155 */         dest[offset + i] = (byte)(this.hash >> i * 8);
/*     */       }
/*     */     }
/*     */ 
/*     */     
/*     */     boolean equalsSameBits(HashCode that) {
/* 161 */       return (this.hash == that.asInt());
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
/*     */   public static HashCode fromLong(long hash) {
/* 174 */     return new LongHashCode(hash);
/*     */   }
/*     */   
/*     */   private static final class LongHashCode extends HashCode implements Serializable { final long hash;
/*     */     private static final long serialVersionUID = 0L;
/*     */     
/*     */     LongHashCode(long hash) {
/* 181 */       this.hash = hash;
/*     */     }
/*     */ 
/*     */     
/*     */     public int bits() {
/* 186 */       return 64;
/*     */     }
/*     */ 
/*     */     
/*     */     public byte[] asBytes() {
/* 191 */       return new byte[] { (byte)(int)this.hash, (byte)(int)(this.hash >> 8L), (byte)(int)(this.hash >> 16L), (byte)(int)(this.hash >> 24L), (byte)(int)(this.hash >> 32L), (byte)(int)(this.hash >> 40L), (byte)(int)(this.hash >> 48L), (byte)(int)(this.hash >> 56L) };
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
/*     */     
/*     */     public int asInt() {
/* 205 */       return (int)this.hash;
/*     */     }
/*     */ 
/*     */     
/*     */     public long asLong() {
/* 210 */       return this.hash;
/*     */     }
/*     */ 
/*     */     
/*     */     public long padToLong() {
/* 215 */       return this.hash;
/*     */     }
/*     */ 
/*     */     
/*     */     void writeBytesToImpl(byte[] dest, int offset, int maxLength) {
/* 220 */       for (int i = 0; i < maxLength; i++) {
/* 221 */         dest[offset + i] = (byte)(int)(this.hash >> i * 8);
/*     */       }
/*     */     }
/*     */ 
/*     */     
/*     */     boolean equalsSameBits(HashCode that) {
/* 227 */       return (this.hash == that.asLong());
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
/*     */   public static HashCode fromBytes(byte[] bytes) {
/* 240 */     Preconditions.checkArgument((bytes.length >= 1), "A HashCode must contain at least 1 byte.");
/* 241 */     return fromBytesNoCopy((byte[])bytes.clone());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static HashCode fromBytesNoCopy(byte[] bytes) {
/* 249 */     return new BytesHashCode(bytes);
/*     */   }
/*     */   
/*     */   private static final class BytesHashCode extends HashCode implements Serializable { final byte[] bytes;
/*     */     private static final long serialVersionUID = 0L;
/*     */     
/*     */     BytesHashCode(byte[] bytes) {
/* 256 */       this.bytes = (byte[])Preconditions.checkNotNull(bytes);
/*     */     }
/*     */ 
/*     */     
/*     */     public int bits() {
/* 261 */       return this.bytes.length * 8;
/*     */     }
/*     */ 
/*     */     
/*     */     public byte[] asBytes() {
/* 266 */       return (byte[])this.bytes.clone();
/*     */     }
/*     */ 
/*     */     
/*     */     public int asInt() {
/* 271 */       Preconditions.checkState((this.bytes.length >= 4), "HashCode#asInt() requires >= 4 bytes (it only has %s bytes).", this.bytes.length);
/*     */ 
/*     */ 
/*     */       
/* 275 */       return this.bytes[0] & 0xFF | (this.bytes[1] & 0xFF) << 8 | (this.bytes[2] & 0xFF) << 16 | (this.bytes[3] & 0xFF) << 24;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public long asLong() {
/* 283 */       Preconditions.checkState((this.bytes.length >= 8), "HashCode#asLong() requires >= 8 bytes (it only has %s bytes).", this.bytes.length);
/*     */ 
/*     */ 
/*     */       
/* 287 */       return padToLong();
/*     */     }
/*     */ 
/*     */     
/*     */     public long padToLong() {
/* 292 */       long retVal = (this.bytes[0] & 0xFF);
/* 293 */       for (int i = 1; i < Math.min(this.bytes.length, 8); i++) {
/* 294 */         retVal |= (this.bytes[i] & 0xFFL) << i * 8;
/*     */       }
/* 296 */       return retVal;
/*     */     }
/*     */ 
/*     */     
/*     */     void writeBytesToImpl(byte[] dest, int offset, int maxLength) {
/* 301 */       System.arraycopy(this.bytes, 0, dest, offset, maxLength);
/*     */     }
/*     */ 
/*     */     
/*     */     byte[] getBytesInternal() {
/* 306 */       return this.bytes;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     boolean equalsSameBits(HashCode that) {
/*     */       int j;
/* 313 */       if (this.bytes.length != (that.getBytesInternal()).length) {
/* 314 */         return false;
/*     */       }
/*     */       
/* 317 */       boolean areEqual = true;
/* 318 */       for (int i = 0; i < this.bytes.length; i++) {
/* 319 */         j = areEqual & ((this.bytes[i] == that.getBytesInternal()[i]) ? 1 : 0);
/*     */       }
/* 321 */       return j;
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
/*     */   public static HashCode fromString(String string) {
/* 338 */     Preconditions.checkArgument(
/* 339 */         (string.length() >= 2), "input string (%s) must have at least 2 characters", string);
/* 340 */     Preconditions.checkArgument(
/* 341 */         (string.length() % 2 == 0), "input string (%s) must have an even number of characters", string);
/*     */ 
/*     */ 
/*     */     
/* 345 */     byte[] bytes = new byte[string.length() / 2];
/* 346 */     for (int i = 0; i < string.length(); i += 2) {
/* 347 */       int ch1 = decode(string.charAt(i)) << 4;
/* 348 */       int ch2 = decode(string.charAt(i + 1));
/* 349 */       bytes[i / 2] = (byte)(ch1 + ch2);
/*     */     } 
/* 351 */     return fromBytesNoCopy(bytes);
/*     */   }
/*     */   
/*     */   private static int decode(char ch) {
/* 355 */     if (ch >= '0' && ch <= '9') {
/* 356 */       return ch - 48;
/*     */     }
/* 358 */     if (ch >= 'a' && ch <= 'f') {
/* 359 */       return ch - 97 + 10;
/*     */     }
/* 361 */     throw new IllegalArgumentException("Illegal hexadecimal character: " + ch);
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
/*     */   public final boolean equals(Object object) {
/* 373 */     if (object instanceof HashCode) {
/* 374 */       HashCode that = (HashCode)object;
/* 375 */       return (bits() == that.bits() && equalsSameBits(that));
/*     */     } 
/* 377 */     return false;
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
/*     */   public final int hashCode() {
/* 389 */     if (bits() >= 32) {
/* 390 */       return asInt();
/*     */     }
/*     */     
/* 393 */     byte[] bytes = getBytesInternal();
/* 394 */     int val = bytes[0] & 0xFF;
/* 395 */     for (int i = 1; i < bytes.length; i++) {
/* 396 */       val |= (bytes[i] & 0xFF) << i * 8;
/*     */     }
/* 398 */     return val;
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
/*     */   public final String toString() {
/* 414 */     byte[] bytes = getBytesInternal();
/* 415 */     StringBuilder sb = new StringBuilder(2 * bytes.length);
/* 416 */     for (byte b : bytes) {
/* 417 */       sb.append(hexDigits[b >> 4 & 0xF]).append(hexDigits[b & 0xF]);
/*     */     }
/* 419 */     return sb.toString();
/*     */   }
/*     */   
/* 422 */   private static final char[] hexDigits = "0123456789abcdef".toCharArray();
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\google\common\hash\HashCode.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */