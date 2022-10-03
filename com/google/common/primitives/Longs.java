/*     */ package com.google.common.primitives;
/*     */ 
/*     */ import com.google.common.annotations.Beta;
/*     */ import com.google.common.annotations.GwtCompatible;
/*     */ import com.google.common.base.Converter;
/*     */ import com.google.common.base.Preconditions;
/*     */ import java.io.Serializable;
/*     */ import java.util.AbstractList;
/*     */ import java.util.Arrays;
/*     */ import java.util.Collection;
/*     */ import java.util.Collections;
/*     */ import java.util.Comparator;
/*     */ import java.util.List;
/*     */ import java.util.RandomAccess;
/*     */ import java.util.Spliterator;
/*     */ import java.util.Spliterators;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @GwtCompatible
/*     */ public final class Longs
/*     */ {
/*     */   public static final int BYTES = 8;
/*     */   public static final long MAX_POWER_OF_TWO = 4611686018427387904L;
/*     */   
/*     */   public static int hashCode(long value) {
/*  79 */     return (int)(value ^ value >>> 32L);
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
/*     */   public static int compare(long a, long b) {
/*  95 */     return (a < b) ? -1 : ((a > b) ? 1 : 0);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static boolean contains(long[] array, long target) {
/* 106 */     for (long value : array) {
/* 107 */       if (value == target) {
/* 108 */         return true;
/*     */       }
/*     */     } 
/* 111 */     return false;
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
/*     */   public static int indexOf(long[] array, long target) {
/* 123 */     return indexOf(array, target, 0, array.length);
/*     */   }
/*     */ 
/*     */   
/*     */   private static int indexOf(long[] array, long target, int start, int end) {
/* 128 */     for (int i = start; i < end; i++) {
/* 129 */       if (array[i] == target) {
/* 130 */         return i;
/*     */       }
/*     */     } 
/* 133 */     return -1;
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
/*     */   public static int indexOf(long[] array, long[] target) {
/* 147 */     Preconditions.checkNotNull(array, "array");
/* 148 */     Preconditions.checkNotNull(target, "target");
/* 149 */     if (target.length == 0) {
/* 150 */       return 0;
/*     */     }
/*     */ 
/*     */     
/* 154 */     for (int i = 0; i < array.length - target.length + 1; i++) {
/* 155 */       int j = 0; while (true) { if (j < target.length) {
/* 156 */           if (array[i + j] != target[j])
/*     */             break;  j++;
/*     */           continue;
/*     */         } 
/* 160 */         return i; }
/*     */     
/* 162 */     }  return -1;
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
/*     */   public static int lastIndexOf(long[] array, long target) {
/* 174 */     return lastIndexOf(array, target, 0, array.length);
/*     */   }
/*     */ 
/*     */   
/*     */   private static int lastIndexOf(long[] array, long target, int start, int end) {
/* 179 */     for (int i = end - 1; i >= start; i--) {
/* 180 */       if (array[i] == target) {
/* 181 */         return i;
/*     */       }
/*     */     } 
/* 184 */     return -1;
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
/*     */   public static long min(long... array) {
/* 196 */     Preconditions.checkArgument((array.length > 0));
/* 197 */     long min = array[0];
/* 198 */     for (int i = 1; i < array.length; i++) {
/* 199 */       if (array[i] < min) {
/* 200 */         min = array[i];
/*     */       }
/*     */     } 
/* 203 */     return min;
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
/*     */   public static long max(long... array) {
/* 215 */     Preconditions.checkArgument((array.length > 0));
/* 216 */     long max = array[0];
/* 217 */     for (int i = 1; i < array.length; i++) {
/* 218 */       if (array[i] > max) {
/* 219 */         max = array[i];
/*     */       }
/*     */     } 
/* 222 */     return max;
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
/*     */   @Beta
/*     */   public static long constrainToRange(long value, long min, long max) {
/* 240 */     Preconditions.checkArgument((min <= max), "min (%s) must be less than or equal to max (%s)", min, max);
/* 241 */     return Math.min(Math.max(value, min), max);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static long[] concat(long[]... arrays) {
/* 252 */     int length = 0;
/* 253 */     for (long[] array : arrays) {
/* 254 */       length += array.length;
/*     */     }
/* 256 */     long[] result = new long[length];
/* 257 */     int pos = 0;
/* 258 */     for (long[] array : arrays) {
/* 259 */       System.arraycopy(array, 0, result, pos, array.length);
/* 260 */       pos += array.length;
/*     */     } 
/* 262 */     return result;
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
/*     */   public static byte[] toByteArray(long value) {
/* 278 */     byte[] result = new byte[8];
/* 279 */     for (int i = 7; i >= 0; i--) {
/* 280 */       result[i] = (byte)(int)(value & 0xFFL);
/* 281 */       value >>= 8L;
/*     */     } 
/* 283 */     return result;
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
/*     */   public static long fromByteArray(byte[] bytes) {
/* 298 */     Preconditions.checkArgument((bytes.length >= 8), "array too small: %s < %s", bytes.length, 8);
/* 299 */     return fromBytes(bytes[0], bytes[1], bytes[2], bytes[3], bytes[4], bytes[5], bytes[6], bytes[7]);
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
/*     */   public static long fromBytes(byte b1, byte b2, byte b3, byte b4, byte b5, byte b6, byte b7, byte b8) {
/* 311 */     return (b1 & 0xFFL) << 56L | (b2 & 0xFFL) << 48L | (b3 & 0xFFL) << 40L | (b4 & 0xFFL) << 32L | (b5 & 0xFFL) << 24L | (b6 & 0xFFL) << 16L | (b7 & 0xFFL) << 8L | b8 & 0xFFL;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static final class AsciiDigits
/*     */   {
/*     */     private static final byte[] asciiDigits;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     static {
/* 331 */       byte[] result = new byte[128];
/* 332 */       Arrays.fill(result, (byte)-1); int i;
/* 333 */       for (i = 0; i <= 9; i++) {
/* 334 */         result[48 + i] = (byte)i;
/*     */       }
/* 336 */       for (i = 0; i <= 26; i++) {
/* 337 */         result[65 + i] = (byte)(10 + i);
/* 338 */         result[97 + i] = (byte)(10 + i);
/*     */       } 
/* 340 */       asciiDigits = result;
/*     */     }
/*     */     
/*     */     static int digit(char c) {
/* 344 */       return (c < '') ? asciiDigits[c] : -1;
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
/*     */   @Beta
/*     */   public static Long tryParse(String string) {
/* 366 */     return tryParse(string, 10);
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
/*     */   @Beta
/*     */   public static Long tryParse(String string, int radix) {
/* 390 */     if (((String)Preconditions.checkNotNull(string)).isEmpty()) {
/* 391 */       return null;
/*     */     }
/* 393 */     if (radix < 2 || radix > 36) {
/* 394 */       throw new IllegalArgumentException("radix must be between MIN_RADIX and MAX_RADIX but was " + radix);
/*     */     }
/*     */     
/* 397 */     boolean negative = (string.charAt(0) == '-');
/* 398 */     int index = negative ? 1 : 0;
/* 399 */     if (index == string.length()) {
/* 400 */       return null;
/*     */     }
/* 402 */     int digit = AsciiDigits.digit(string.charAt(index++));
/* 403 */     if (digit < 0 || digit >= radix) {
/* 404 */       return null;
/*     */     }
/* 406 */     long accum = -digit;
/*     */     
/* 408 */     long cap = Long.MIN_VALUE / radix;
/*     */     
/* 410 */     while (index < string.length()) {
/* 411 */       digit = AsciiDigits.digit(string.charAt(index++));
/* 412 */       if (digit < 0 || digit >= radix || accum < cap) {
/* 413 */         return null;
/*     */       }
/* 415 */       accum *= radix;
/* 416 */       if (accum < Long.MIN_VALUE + digit) {
/* 417 */         return null;
/*     */       }
/* 419 */       accum -= digit;
/*     */     } 
/*     */     
/* 422 */     if (negative)
/* 423 */       return Long.valueOf(accum); 
/* 424 */     if (accum == Long.MIN_VALUE) {
/* 425 */       return null;
/*     */     }
/* 427 */     return Long.valueOf(-accum);
/*     */   }
/*     */   
/*     */   private static final class LongConverter
/*     */     extends Converter<String, Long> implements Serializable {
/* 432 */     static final LongConverter INSTANCE = new LongConverter();
/*     */     private static final long serialVersionUID = 1L;
/*     */     
/*     */     protected Long doForward(String value) {
/* 436 */       return Long.decode(value);
/*     */     }
/*     */ 
/*     */     
/*     */     protected String doBackward(Long value) {
/* 441 */       return value.toString();
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString() {
/* 446 */       return "Longs.stringConverter()";
/*     */     }
/*     */     
/*     */     private Object readResolve() {
/* 450 */       return INSTANCE;
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
/*     */   @Beta
/*     */   public static Converter<String, Long> stringConverter() {
/* 469 */     return LongConverter.INSTANCE;
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
/*     */   public static long[] ensureCapacity(long[] array, int minLength, int padding) {
/* 486 */     Preconditions.checkArgument((minLength >= 0), "Invalid minLength: %s", minLength);
/* 487 */     Preconditions.checkArgument((padding >= 0), "Invalid padding: %s", padding);
/* 488 */     return (array.length < minLength) ? Arrays.copyOf(array, minLength + padding) : array;
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
/*     */   public static String join(String separator, long... array) {
/* 500 */     Preconditions.checkNotNull(separator);
/* 501 */     if (array.length == 0) {
/* 502 */       return "";
/*     */     }
/*     */ 
/*     */     
/* 506 */     StringBuilder builder = new StringBuilder(array.length * 10);
/* 507 */     builder.append(array[0]);
/* 508 */     for (int i = 1; i < array.length; i++) {
/* 509 */       builder.append(separator).append(array[i]);
/*     */     }
/* 511 */     return builder.toString();
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
/*     */   public static Comparator<long[]> lexicographicalComparator() {
/* 528 */     return LexicographicalComparator.INSTANCE;
/*     */   }
/*     */   
/*     */   private enum LexicographicalComparator implements Comparator<long[]> {
/* 532 */     INSTANCE;
/*     */ 
/*     */     
/*     */     public int compare(long[] left, long[] right) {
/* 536 */       int minLength = Math.min(left.length, right.length);
/* 537 */       for (int i = 0; i < minLength; i++) {
/* 538 */         int result = Longs.compare(left[i], right[i]);
/* 539 */         if (result != 0) {
/* 540 */           return result;
/*     */         }
/*     */       } 
/* 543 */       return left.length - right.length;
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString() {
/* 548 */       return "Longs.lexicographicalComparator()";
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void sortDescending(long[] array) {
/* 558 */     Preconditions.checkNotNull(array);
/* 559 */     sortDescending(array, 0, array.length);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void sortDescending(long[] array, int fromIndex, int toIndex) {
/* 569 */     Preconditions.checkNotNull(array);
/* 570 */     Preconditions.checkPositionIndexes(fromIndex, toIndex, array.length);
/* 571 */     Arrays.sort(array, fromIndex, toIndex);
/* 572 */     reverse(array, fromIndex, toIndex);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void reverse(long[] array) {
/* 582 */     Preconditions.checkNotNull(array);
/* 583 */     reverse(array, 0, array.length);
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
/*     */   public static void reverse(long[] array, int fromIndex, int toIndex) {
/* 597 */     Preconditions.checkNotNull(array);
/* 598 */     Preconditions.checkPositionIndexes(fromIndex, toIndex, array.length);
/* 599 */     for (int i = fromIndex, j = toIndex - 1; i < j; i++, j--) {
/* 600 */       long tmp = array[i];
/* 601 */       array[i] = array[j];
/* 602 */       array[j] = tmp;
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
/*     */   public static long[] toArray(Collection<? extends Number> collection) {
/* 620 */     if (collection instanceof LongArrayAsList) {
/* 621 */       return ((LongArrayAsList)collection).toLongArray();
/*     */     }
/*     */     
/* 624 */     Object[] boxedArray = collection.toArray();
/* 625 */     int len = boxedArray.length;
/* 626 */     long[] array = new long[len];
/* 627 */     for (int i = 0; i < len; i++)
/*     */     {
/* 629 */       array[i] = ((Number)Preconditions.checkNotNull(boxedArray[i])).longValue();
/*     */     }
/* 631 */     return array;
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
/*     */   public static List<Long> asList(long... backingArray) {
/* 650 */     if (backingArray.length == 0) {
/* 651 */       return Collections.emptyList();
/*     */     }
/* 653 */     return new LongArrayAsList(backingArray);
/*     */   }
/*     */   
/*     */   @GwtCompatible
/*     */   private static class LongArrayAsList extends AbstractList<Long> implements RandomAccess, Serializable {
/*     */     final long[] array;
/*     */     final int start;
/*     */     final int end;
/*     */     private static final long serialVersionUID = 0L;
/*     */     
/*     */     LongArrayAsList(long[] array) {
/* 664 */       this(array, 0, array.length);
/*     */     }
/*     */     
/*     */     LongArrayAsList(long[] array, int start, int end) {
/* 668 */       this.array = array;
/* 669 */       this.start = start;
/* 670 */       this.end = end;
/*     */     }
/*     */ 
/*     */     
/*     */     public int size() {
/* 675 */       return this.end - this.start;
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean isEmpty() {
/* 680 */       return false;
/*     */     }
/*     */ 
/*     */     
/*     */     public Long get(int index) {
/* 685 */       Preconditions.checkElementIndex(index, size());
/* 686 */       return Long.valueOf(this.array[this.start + index]);
/*     */     }
/*     */ 
/*     */     
/*     */     public Spliterator.OfLong spliterator() {
/* 691 */       return Spliterators.spliterator(this.array, this.start, this.end, 0);
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public boolean contains(Object target) {
/* 697 */       return (target instanceof Long && Longs.indexOf(this.array, ((Long)target).longValue(), this.start, this.end) != -1);
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public int indexOf(Object target) {
/* 703 */       if (target instanceof Long) {
/* 704 */         int i = Longs.indexOf(this.array, ((Long)target).longValue(), this.start, this.end);
/* 705 */         if (i >= 0) {
/* 706 */           return i - this.start;
/*     */         }
/*     */       } 
/* 709 */       return -1;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public int lastIndexOf(Object target) {
/* 715 */       if (target instanceof Long) {
/* 716 */         int i = Longs.lastIndexOf(this.array, ((Long)target).longValue(), this.start, this.end);
/* 717 */         if (i >= 0) {
/* 718 */           return i - this.start;
/*     */         }
/*     */       } 
/* 721 */       return -1;
/*     */     }
/*     */ 
/*     */     
/*     */     public Long set(int index, Long element) {
/* 726 */       Preconditions.checkElementIndex(index, size());
/* 727 */       long oldValue = this.array[this.start + index];
/*     */       
/* 729 */       this.array[this.start + index] = ((Long)Preconditions.checkNotNull(element)).longValue();
/* 730 */       return Long.valueOf(oldValue);
/*     */     }
/*     */ 
/*     */     
/*     */     public List<Long> subList(int fromIndex, int toIndex) {
/* 735 */       int size = size();
/* 736 */       Preconditions.checkPositionIndexes(fromIndex, toIndex, size);
/* 737 */       if (fromIndex == toIndex) {
/* 738 */         return Collections.emptyList();
/*     */       }
/* 740 */       return new LongArrayAsList(this.array, this.start + fromIndex, this.start + toIndex);
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean equals(Object object) {
/* 745 */       if (object == this) {
/* 746 */         return true;
/*     */       }
/* 748 */       if (object instanceof LongArrayAsList) {
/* 749 */         LongArrayAsList that = (LongArrayAsList)object;
/* 750 */         int size = size();
/* 751 */         if (that.size() != size) {
/* 752 */           return false;
/*     */         }
/* 754 */         for (int i = 0; i < size; i++) {
/* 755 */           if (this.array[this.start + i] != that.array[that.start + i]) {
/* 756 */             return false;
/*     */           }
/*     */         } 
/* 759 */         return true;
/*     */       } 
/* 761 */       return super.equals(object);
/*     */     }
/*     */ 
/*     */     
/*     */     public int hashCode() {
/* 766 */       int result = 1;
/* 767 */       for (int i = this.start; i < this.end; i++) {
/* 768 */         result = 31 * result + Longs.hashCode(this.array[i]);
/*     */       }
/* 770 */       return result;
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString() {
/* 775 */       StringBuilder builder = new StringBuilder(size() * 10);
/* 776 */       builder.append('[').append(this.array[this.start]);
/* 777 */       for (int i = this.start + 1; i < this.end; i++) {
/* 778 */         builder.append(", ").append(this.array[i]);
/*     */       }
/* 780 */       return builder.append(']').toString();
/*     */     }
/*     */     
/*     */     long[] toLongArray() {
/* 784 */       return Arrays.copyOfRange(this.array, this.start, this.end);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\google\common\primitives\Longs.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */