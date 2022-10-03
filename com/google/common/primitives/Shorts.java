/*     */ package com.google.common.primitives;
/*     */ 
/*     */ import com.google.common.annotations.Beta;
/*     */ import com.google.common.annotations.GwtCompatible;
/*     */ import com.google.common.annotations.GwtIncompatible;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
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
/*     */ public final class Shorts
/*     */ {
/*     */   public static final int BYTES = 2;
/*     */   public static final short MAX_POWER_OF_TWO = 16384;
/*     */   
/*     */   public static int hashCode(short value) {
/*  74 */     return value;
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
/*     */   public static short checkedCast(long value) {
/*  86 */     short result = (short)(int)value;
/*  87 */     Preconditions.checkArgument((result == value), "Out of range: %s", value);
/*  88 */     return result;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static short saturatedCast(long value) {
/*  99 */     if (value > 32767L) {
/* 100 */       return Short.MAX_VALUE;
/*     */     }
/* 102 */     if (value < -32768L) {
/* 103 */       return Short.MIN_VALUE;
/*     */     }
/* 105 */     return (short)(int)value;
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
/*     */   public static int compare(short a, short b) {
/* 121 */     return a - b;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static boolean contains(short[] array, short target) {
/* 132 */     for (short value : array) {
/* 133 */       if (value == target) {
/* 134 */         return true;
/*     */       }
/*     */     } 
/* 137 */     return false;
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
/*     */   public static int indexOf(short[] array, short target) {
/* 149 */     return indexOf(array, target, 0, array.length);
/*     */   }
/*     */ 
/*     */   
/*     */   private static int indexOf(short[] array, short target, int start, int end) {
/* 154 */     for (int i = start; i < end; i++) {
/* 155 */       if (array[i] == target) {
/* 156 */         return i;
/*     */       }
/*     */     } 
/* 159 */     return -1;
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
/*     */   public static int indexOf(short[] array, short[] target) {
/* 173 */     Preconditions.checkNotNull(array, "array");
/* 174 */     Preconditions.checkNotNull(target, "target");
/* 175 */     if (target.length == 0) {
/* 176 */       return 0;
/*     */     }
/*     */ 
/*     */     
/* 180 */     for (int i = 0; i < array.length - target.length + 1; i++) {
/* 181 */       int j = 0; while (true) { if (j < target.length) {
/* 182 */           if (array[i + j] != target[j])
/*     */             break;  j++;
/*     */           continue;
/*     */         } 
/* 186 */         return i; }
/*     */     
/* 188 */     }  return -1;
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
/*     */   public static int lastIndexOf(short[] array, short target) {
/* 200 */     return lastIndexOf(array, target, 0, array.length);
/*     */   }
/*     */ 
/*     */   
/*     */   private static int lastIndexOf(short[] array, short target, int start, int end) {
/* 205 */     for (int i = end - 1; i >= start; i--) {
/* 206 */       if (array[i] == target) {
/* 207 */         return i;
/*     */       }
/*     */     } 
/* 210 */     return -1;
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
/*     */   public static short min(short... array) {
/* 222 */     Preconditions.checkArgument((array.length > 0));
/* 223 */     short min = array[0];
/* 224 */     for (int i = 1; i < array.length; i++) {
/* 225 */       if (array[i] < min) {
/* 226 */         min = array[i];
/*     */       }
/*     */     } 
/* 229 */     return min;
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
/*     */   public static short max(short... array) {
/* 241 */     Preconditions.checkArgument((array.length > 0));
/* 242 */     short max = array[0];
/* 243 */     for (int i = 1; i < array.length; i++) {
/* 244 */       if (array[i] > max) {
/* 245 */         max = array[i];
/*     */       }
/*     */     } 
/* 248 */     return max;
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
/*     */   public static short constrainToRange(short value, short min, short max) {
/* 266 */     Preconditions.checkArgument((min <= max), "min (%s) must be less than or equal to max (%s)", min, max);
/* 267 */     return (value < min) ? min : ((value < max) ? value : max);
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
/*     */   public static short[] concat(short[]... arrays) {
/* 279 */     int length = 0;
/* 280 */     for (short[] array : arrays) {
/* 281 */       length += array.length;
/*     */     }
/* 283 */     short[] result = new short[length];
/* 284 */     int pos = 0;
/* 285 */     for (short[] array : arrays) {
/* 286 */       System.arraycopy(array, 0, result, pos, array.length);
/* 287 */       pos += array.length;
/*     */     } 
/* 289 */     return result;
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
/*     */   @GwtIncompatible
/*     */   public static byte[] toByteArray(short value) {
/* 303 */     return new byte[] { (byte)(value >> 8), (byte)value };
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
/*     */   @GwtIncompatible
/*     */   public static short fromByteArray(byte[] bytes) {
/* 318 */     Preconditions.checkArgument((bytes.length >= 2), "array too small: %s < %s", bytes.length, 2);
/* 319 */     return fromBytes(bytes[0], bytes[1]);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @GwtIncompatible
/*     */   public static short fromBytes(byte b1, byte b2) {
/* 330 */     return (short)(b1 << 8 | b2 & 0xFF);
/*     */   }
/*     */   
/*     */   private static final class ShortConverter
/*     */     extends Converter<String, Short> implements Serializable {
/* 335 */     static final ShortConverter INSTANCE = new ShortConverter();
/*     */     private static final long serialVersionUID = 1L;
/*     */     
/*     */     protected Short doForward(String value) {
/* 339 */       return Short.decode(value);
/*     */     }
/*     */ 
/*     */     
/*     */     protected String doBackward(Short value) {
/* 344 */       return value.toString();
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString() {
/* 349 */       return "Shorts.stringConverter()";
/*     */     }
/*     */     
/*     */     private Object readResolve() {
/* 353 */       return INSTANCE;
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
/*     */   public static Converter<String, Short> stringConverter() {
/* 372 */     return ShortConverter.INSTANCE;
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
/*     */   public static short[] ensureCapacity(short[] array, int minLength, int padding) {
/* 389 */     Preconditions.checkArgument((minLength >= 0), "Invalid minLength: %s", minLength);
/* 390 */     Preconditions.checkArgument((padding >= 0), "Invalid padding: %s", padding);
/* 391 */     return (array.length < minLength) ? Arrays.copyOf(array, minLength + padding) : array;
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
/*     */   public static String join(String separator, short... array) {
/* 404 */     Preconditions.checkNotNull(separator);
/* 405 */     if (array.length == 0) {
/* 406 */       return "";
/*     */     }
/*     */ 
/*     */     
/* 410 */     StringBuilder builder = new StringBuilder(array.length * 6);
/* 411 */     builder.append(array[0]);
/* 412 */     for (int i = 1; i < array.length; i++) {
/* 413 */       builder.append(separator).append(array[i]);
/*     */     }
/* 415 */     return builder.toString();
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
/*     */   public static Comparator<short[]> lexicographicalComparator() {
/* 432 */     return LexicographicalComparator.INSTANCE;
/*     */   }
/*     */   
/*     */   private enum LexicographicalComparator implements Comparator<short[]> {
/* 436 */     INSTANCE;
/*     */ 
/*     */     
/*     */     public int compare(short[] left, short[] right) {
/* 440 */       int minLength = Math.min(left.length, right.length);
/* 441 */       for (int i = 0; i < minLength; i++) {
/* 442 */         int result = Shorts.compare(left[i], right[i]);
/* 443 */         if (result != 0) {
/* 444 */           return result;
/*     */         }
/*     */       } 
/* 447 */       return left.length - right.length;
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString() {
/* 452 */       return "Shorts.lexicographicalComparator()";
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void sortDescending(short[] array) {
/* 462 */     Preconditions.checkNotNull(array);
/* 463 */     sortDescending(array, 0, array.length);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void sortDescending(short[] array, int fromIndex, int toIndex) {
/* 473 */     Preconditions.checkNotNull(array);
/* 474 */     Preconditions.checkPositionIndexes(fromIndex, toIndex, array.length);
/* 475 */     Arrays.sort(array, fromIndex, toIndex);
/* 476 */     reverse(array, fromIndex, toIndex);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void reverse(short[] array) {
/* 486 */     Preconditions.checkNotNull(array);
/* 487 */     reverse(array, 0, array.length);
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
/*     */   public static void reverse(short[] array, int fromIndex, int toIndex) {
/* 501 */     Preconditions.checkNotNull(array);
/* 502 */     Preconditions.checkPositionIndexes(fromIndex, toIndex, array.length);
/* 503 */     for (int i = fromIndex, j = toIndex - 1; i < j; i++, j--) {
/* 504 */       short tmp = array[i];
/* 505 */       array[i] = array[j];
/* 506 */       array[j] = tmp;
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
/*     */   public static short[] toArray(Collection<? extends Number> collection) {
/* 524 */     if (collection instanceof ShortArrayAsList) {
/* 525 */       return ((ShortArrayAsList)collection).toShortArray();
/*     */     }
/*     */     
/* 528 */     Object[] boxedArray = collection.toArray();
/* 529 */     int len = boxedArray.length;
/* 530 */     short[] array = new short[len];
/* 531 */     for (int i = 0; i < len; i++)
/*     */     {
/* 533 */       array[i] = ((Number)Preconditions.checkNotNull(boxedArray[i])).shortValue();
/*     */     }
/* 535 */     return array;
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
/*     */   public static List<Short> asList(short... backingArray) {
/* 551 */     if (backingArray.length == 0) {
/* 552 */       return Collections.emptyList();
/*     */     }
/* 554 */     return new ShortArrayAsList(backingArray);
/*     */   }
/*     */   
/*     */   @GwtCompatible
/*     */   private static class ShortArrayAsList extends AbstractList<Short> implements RandomAccess, Serializable {
/*     */     final short[] array;
/*     */     final int start;
/*     */     final int end;
/*     */     private static final long serialVersionUID = 0L;
/*     */     
/*     */     ShortArrayAsList(short[] array) {
/* 565 */       this(array, 0, array.length);
/*     */     }
/*     */     
/*     */     ShortArrayAsList(short[] array, int start, int end) {
/* 569 */       this.array = array;
/* 570 */       this.start = start;
/* 571 */       this.end = end;
/*     */     }
/*     */ 
/*     */     
/*     */     public int size() {
/* 576 */       return this.end - this.start;
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean isEmpty() {
/* 581 */       return false;
/*     */     }
/*     */ 
/*     */     
/*     */     public Short get(int index) {
/* 586 */       Preconditions.checkElementIndex(index, size());
/* 587 */       return Short.valueOf(this.array[this.start + index]);
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public boolean contains(Object target) {
/* 593 */       return (target instanceof Short && Shorts.indexOf(this.array, ((Short)target).shortValue(), this.start, this.end) != -1);
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public int indexOf(Object target) {
/* 599 */       if (target instanceof Short) {
/* 600 */         int i = Shorts.indexOf(this.array, ((Short)target).shortValue(), this.start, this.end);
/* 601 */         if (i >= 0) {
/* 602 */           return i - this.start;
/*     */         }
/*     */       } 
/* 605 */       return -1;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public int lastIndexOf(Object target) {
/* 611 */       if (target instanceof Short) {
/* 612 */         int i = Shorts.lastIndexOf(this.array, ((Short)target).shortValue(), this.start, this.end);
/* 613 */         if (i >= 0) {
/* 614 */           return i - this.start;
/*     */         }
/*     */       } 
/* 617 */       return -1;
/*     */     }
/*     */ 
/*     */     
/*     */     public Short set(int index, Short element) {
/* 622 */       Preconditions.checkElementIndex(index, size());
/* 623 */       short oldValue = this.array[this.start + index];
/*     */       
/* 625 */       this.array[this.start + index] = ((Short)Preconditions.checkNotNull(element)).shortValue();
/* 626 */       return Short.valueOf(oldValue);
/*     */     }
/*     */ 
/*     */     
/*     */     public List<Short> subList(int fromIndex, int toIndex) {
/* 631 */       int size = size();
/* 632 */       Preconditions.checkPositionIndexes(fromIndex, toIndex, size);
/* 633 */       if (fromIndex == toIndex) {
/* 634 */         return Collections.emptyList();
/*     */       }
/* 636 */       return new ShortArrayAsList(this.array, this.start + fromIndex, this.start + toIndex);
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean equals(Object object) {
/* 641 */       if (object == this) {
/* 642 */         return true;
/*     */       }
/* 644 */       if (object instanceof ShortArrayAsList) {
/* 645 */         ShortArrayAsList that = (ShortArrayAsList)object;
/* 646 */         int size = size();
/* 647 */         if (that.size() != size) {
/* 648 */           return false;
/*     */         }
/* 650 */         for (int i = 0; i < size; i++) {
/* 651 */           if (this.array[this.start + i] != that.array[that.start + i]) {
/* 652 */             return false;
/*     */           }
/*     */         } 
/* 655 */         return true;
/*     */       } 
/* 657 */       return super.equals(object);
/*     */     }
/*     */ 
/*     */     
/*     */     public int hashCode() {
/* 662 */       int result = 1;
/* 663 */       for (int i = this.start; i < this.end; i++) {
/* 664 */         result = 31 * result + Shorts.hashCode(this.array[i]);
/*     */       }
/* 666 */       return result;
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString() {
/* 671 */       StringBuilder builder = new StringBuilder(size() * 6);
/* 672 */       builder.append('[').append(this.array[this.start]);
/* 673 */       for (int i = this.start + 1; i < this.end; i++) {
/* 674 */         builder.append(", ").append(this.array[i]);
/*     */       }
/* 676 */       return builder.append(']').toString();
/*     */     }
/*     */     
/*     */     short[] toShortArray() {
/* 680 */       return Arrays.copyOfRange(this.array, this.start, this.end);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\google\common\primitives\Shorts.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */