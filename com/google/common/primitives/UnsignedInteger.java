/*     */ package com.google.common.primitives;
/*     */ 
/*     */ import com.google.common.annotations.GwtCompatible;
/*     */ import com.google.common.annotations.GwtIncompatible;
/*     */ import com.google.common.base.Preconditions;
/*     */ import java.math.BigInteger;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @GwtCompatible(emulated = true)
/*     */ public final class UnsignedInteger
/*     */   extends Number
/*     */   implements Comparable<UnsignedInteger>
/*     */ {
/*  43 */   public static final UnsignedInteger ZERO = fromIntBits(0);
/*  44 */   public static final UnsignedInteger ONE = fromIntBits(1);
/*  45 */   public static final UnsignedInteger MAX_VALUE = fromIntBits(-1);
/*     */   
/*     */   private final int value;
/*     */ 
/*     */   
/*     */   private UnsignedInteger(int value) {
/*  51 */     this.value = value & 0xFFFFFFFF;
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
/*     */   public static UnsignedInteger fromIntBits(int bits) {
/*  67 */     return new UnsignedInteger(bits);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static UnsignedInteger valueOf(long value) {
/*  75 */     Preconditions.checkArgument(((value & 0xFFFFFFFFL) == value), "value (%s) is outside the range for an unsigned integer value", value);
/*     */ 
/*     */ 
/*     */     
/*  79 */     return fromIntBits((int)value);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static UnsignedInteger valueOf(BigInteger value) {
/*  89 */     Preconditions.checkNotNull(value);
/*  90 */     Preconditions.checkArgument((value
/*  91 */         .signum() >= 0 && value.bitLength() <= 32), "value (%s) is outside the range for an unsigned integer value", value);
/*     */ 
/*     */     
/*  94 */     return fromIntBits(value.intValue());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static UnsignedInteger valueOf(String string) {
/* 105 */     return valueOf(string, 10);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static UnsignedInteger valueOf(String string, int radix) {
/* 116 */     return fromIntBits(UnsignedInts.parseUnsignedInt(string, radix));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public UnsignedInteger plus(UnsignedInteger val) {
/* 126 */     return fromIntBits(this.value + ((UnsignedInteger)Preconditions.checkNotNull(val)).value);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public UnsignedInteger minus(UnsignedInteger val) {
/* 136 */     return fromIntBits(this.value - ((UnsignedInteger)Preconditions.checkNotNull(val)).value);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @GwtIncompatible
/*     */   public UnsignedInteger times(UnsignedInteger val) {
/* 148 */     return fromIntBits(this.value * ((UnsignedInteger)Preconditions.checkNotNull(val)).value);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public UnsignedInteger dividedBy(UnsignedInteger val) {
/* 158 */     return fromIntBits(UnsignedInts.divide(this.value, ((UnsignedInteger)Preconditions.checkNotNull(val)).value));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public UnsignedInteger mod(UnsignedInteger val) {
/* 168 */     return fromIntBits(UnsignedInts.remainder(this.value, ((UnsignedInteger)Preconditions.checkNotNull(val)).value));
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
/*     */   public int intValue() {
/* 180 */     return this.value;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public long longValue() {
/* 186 */     return UnsignedInts.toLong(this.value);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public float floatValue() {
/* 195 */     return (float)longValue();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public double doubleValue() {
/* 204 */     return longValue();
/*     */   }
/*     */ 
/*     */   
/*     */   public BigInteger bigIntegerValue() {
/* 209 */     return BigInteger.valueOf(longValue());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int compareTo(UnsignedInteger other) {
/* 219 */     Preconditions.checkNotNull(other);
/* 220 */     return UnsignedInts.compare(this.value, other.value);
/*     */   }
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 225 */     return this.value;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean equals(Object obj) {
/* 230 */     if (obj instanceof UnsignedInteger) {
/* 231 */       UnsignedInteger other = (UnsignedInteger)obj;
/* 232 */       return (this.value == other.value);
/*     */     } 
/* 234 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String toString() {
/* 240 */     return toString(10);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String toString(int radix) {
/* 249 */     return UnsignedInts.toString(this.value, radix);
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\google\common\primitives\UnsignedInteger.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */