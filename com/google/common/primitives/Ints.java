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
/*     */ @GwtCompatible
/*     */ public final class Ints
/*     */ {
/*     */   public static final int BYTES = 4;
/*     */   public static final int MAX_POWER_OF_TWO = 1073741824;
/*     */   
/*     */   public static int hashCode(int value) {
/*  75 */     return value;
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
/*     */   public static int checkedCast(long value) {
/*  87 */     int result = (int)value;
/*  88 */     Preconditions.checkArgument((result == value), "Out of range: %s", value);
/*  89 */     return result;
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
/*     */   public static int saturatedCast(long value) {
/* 101 */     if (value > 2147483647L) {
/* 102 */       return Integer.MAX_VALUE;
/*     */     }
/* 104 */     if (value < -2147483648L) {
/* 105 */       return Integer.MIN_VALUE;
/*     */     }
/* 107 */     return (int)value;
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
/*     */   public static int compare(int a, int b) {
/* 123 */     return (a < b) ? -1 : ((a > b) ? 1 : 0);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static boolean contains(int[] array, int target) {
/* 134 */     for (int value : array) {
/* 135 */       if (value == target) {
/* 136 */         return true;
/*     */       }
/*     */     } 
/* 139 */     return false;
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
/*     */   public static int indexOf(int[] array, int target) {
/* 151 */     return indexOf(array, target, 0, array.length);
/*     */   }
/*     */ 
/*     */   
/*     */   private static int indexOf(int[] array, int target, int start, int end) {
/* 156 */     for (int i = start; i < end; i++) {
/* 157 */       if (array[i] == target) {
/* 158 */         return i;
/*     */       }
/*     */     } 
/* 161 */     return -1;
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
/*     */   public static int indexOf(int[] array, int[] target) {
/* 175 */     Preconditions.checkNotNull(array, "array");
/* 176 */     Preconditions.checkNotNull(target, "target");
/* 177 */     if (target.length == 0) {
/* 178 */       return 0;
/*     */     }
/*     */ 
/*     */     
/* 182 */     for (int i = 0; i < array.length - target.length + 1; i++) {
/* 183 */       int j = 0; while (true) { if (j < target.length) {
/* 184 */           if (array[i + j] != target[j])
/*     */             break;  j++;
/*     */           continue;
/*     */         } 
/* 188 */         return i; }
/*     */     
/* 190 */     }  return -1;
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
/*     */   public static int lastIndexOf(int[] array, int target) {
/* 202 */     return lastIndexOf(array, target, 0, array.length);
/*     */   }
/*     */ 
/*     */   
/*     */   private static int lastIndexOf(int[] array, int target, int start, int end) {
/* 207 */     for (int i = end - 1; i >= start; i--) {
/* 208 */       if (array[i] == target) {
/* 209 */         return i;
/*     */       }
/*     */     } 
/* 212 */     return -1;
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
/*     */   public static int min(int... array) {
/* 224 */     Preconditions.checkArgument((array.length > 0));
/* 225 */     int min = array[0];
/* 226 */     for (int i = 1; i < array.length; i++) {
/* 227 */       if (array[i] < min) {
/* 228 */         min = array[i];
/*     */       }
/*     */     } 
/* 231 */     return min;
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
/*     */   public static int max(int... array) {
/* 243 */     Preconditions.checkArgument((array.length > 0));
/* 244 */     int max = array[0];
/* 245 */     for (int i = 1; i < array.length; i++) {
/* 246 */       if (array[i] > max) {
/* 247 */         max = array[i];
/*     */       }
/*     */     } 
/* 250 */     return max;
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
/*     */   public static int constrainToRange(int value, int min, int max) {
/* 268 */     Preconditions.checkArgument((min <= max), "min (%s) must be less than or equal to max (%s)", min, max);
/* 269 */     return Math.min(Math.max(value, min), max);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static int[] concat(int[]... arrays) {
/* 280 */     int length = 0;
/* 281 */     for (int[] array : arrays) {
/* 282 */       length += array.length;
/*     */     }
/* 284 */     int[] result = new int[length];
/* 285 */     int pos = 0;
/* 286 */     for (int[] array : arrays) {
/* 287 */       System.arraycopy(array, 0, result, pos, array.length);
/* 288 */       pos += array.length;
/*     */     } 
/* 290 */     return result;
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
/*     */   public static byte[] toByteArray(int value) {
/* 303 */     return new byte[] { (byte)(value >> 24), (byte)(value >> 16), (byte)(value >> 8), (byte)value };
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
/*     */   public static int fromByteArray(byte[] bytes) {
/* 320 */     Preconditions.checkArgument((bytes.length >= 4), "array too small: %s < %s", bytes.length, 4);
/* 321 */     return fromBytes(bytes[0], bytes[1], bytes[2], bytes[3]);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static int fromBytes(byte b1, byte b2, byte b3, byte b4) {
/* 331 */     return b1 << 24 | (b2 & 0xFF) << 16 | (b3 & 0xFF) << 8 | b4 & 0xFF;
/*     */   }
/*     */   
/*     */   private static final class IntConverter
/*     */     extends Converter<String, Integer> implements Serializable {
/* 336 */     static final IntConverter INSTANCE = new IntConverter();
/*     */     private static final long serialVersionUID = 1L;
/*     */     
/*     */     protected Integer doForward(String value) {
/* 340 */       return Integer.decode(value);
/*     */     }
/*     */ 
/*     */     
/*     */     protected String doBackward(Integer value) {
/* 345 */       return value.toString();
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString() {
/* 350 */       return "Ints.stringConverter()";
/*     */     }
/*     */     
/*     */     private Object readResolve() {
/* 354 */       return INSTANCE;
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
/*     */   public static Converter<String, Integer> stringConverter() {
/* 373 */     return IntConverter.INSTANCE;
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
/*     */   public static int[] ensureCapacity(int[] array, int minLength, int padding) {
/* 390 */     Preconditions.checkArgument((minLength >= 0), "Invalid minLength: %s", minLength);
/* 391 */     Preconditions.checkArgument((padding >= 0), "Invalid padding: %s", padding);
/* 392 */     return (array.length < minLength) ? Arrays.copyOf(array, minLength + padding) : array;
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
/*     */   public static String join(String separator, int... array) {
/* 404 */     Preconditions.checkNotNull(separator);
/* 405 */     if (array.length == 0) {
/* 406 */       return "";
/*     */     }
/*     */ 
/*     */     
/* 410 */     StringBuilder builder = new StringBuilder(array.length * 5);
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
/*     */   public static Comparator<int[]> lexicographicalComparator() {
/* 431 */     return LexicographicalComparator.INSTANCE;
/*     */   }
/*     */   
/*     */   private enum LexicographicalComparator implements Comparator<int[]> {
/* 435 */     INSTANCE;
/*     */ 
/*     */     
/*     */     public int compare(int[] left, int[] right) {
/* 439 */       int minLength = Math.min(left.length, right.length);
/* 440 */       for (int i = 0; i < minLength; i++) {
/* 441 */         int result = Ints.compare(left[i], right[i]);
/* 442 */         if (result != 0) {
/* 443 */           return result;
/*     */         }
/*     */       } 
/* 446 */       return left.length - right.length;
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString() {
/* 451 */       return "Ints.lexicographicalComparator()";
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void sortDescending(int[] array) {
/* 461 */     Preconditions.checkNotNull(array);
/* 462 */     sortDescending(array, 0, array.length);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void sortDescending(int[] array, int fromIndex, int toIndex) {
/* 472 */     Preconditions.checkNotNull(array);
/* 473 */     Preconditions.checkPositionIndexes(fromIndex, toIndex, array.length);
/* 474 */     Arrays.sort(array, fromIndex, toIndex);
/* 475 */     reverse(array, fromIndex, toIndex);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void reverse(int[] array) {
/* 485 */     Preconditions.checkNotNull(array);
/* 486 */     reverse(array, 0, array.length);
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
/*     */   public static void reverse(int[] array, int fromIndex, int toIndex) {
/* 500 */     Preconditions.checkNotNull(array);
/* 501 */     Preconditions.checkPositionIndexes(fromIndex, toIndex, array.length);
/* 502 */     for (int i = fromIndex, j = toIndex - 1; i < j; i++, j--) {
/* 503 */       int tmp = array[i];
/* 504 */       array[i] = array[j];
/* 505 */       array[j] = tmp;
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
/*     */   public static int[] toArray(Collection<? extends Number> collection) {
/* 523 */     if (collection instanceof IntArrayAsList) {
/* 524 */       return ((IntArrayAsList)collection).toIntArray();
/*     */     }
/*     */     
/* 527 */     Object[] boxedArray = collection.toArray();
/* 528 */     int len = boxedArray.length;
/* 529 */     int[] array = new int[len];
/* 530 */     for (int i = 0; i < len; i++)
/*     */     {
/* 532 */       array[i] = ((Number)Preconditions.checkNotNull(boxedArray[i])).intValue();
/*     */     }
/* 534 */     return array;
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
/*     */   public static List<Integer> asList(int... backingArray) {
/* 553 */     if (backingArray.length == 0) {
/* 554 */       return Collections.emptyList();
/*     */     }
/* 556 */     return new IntArrayAsList(backingArray);
/*     */   }
/*     */   
/*     */   @GwtCompatible
/*     */   private static class IntArrayAsList extends AbstractList<Integer> implements RandomAccess, Serializable {
/*     */     final int[] array;
/*     */     final int start;
/*     */     final int end;
/*     */     private static final long serialVersionUID = 0L;
/*     */     
/*     */     IntArrayAsList(int[] array) {
/* 567 */       this(array, 0, array.length);
/*     */     }
/*     */     
/*     */     IntArrayAsList(int[] array, int start, int end) {
/* 571 */       this.array = array;
/* 572 */       this.start = start;
/* 573 */       this.end = end;
/*     */     }
/*     */ 
/*     */     
/*     */     public int size() {
/* 578 */       return this.end - this.start;
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean isEmpty() {
/* 583 */       return false;
/*     */     }
/*     */ 
/*     */     
/*     */     public Integer get(int index) {
/* 588 */       Preconditions.checkElementIndex(index, size());
/* 589 */       return Integer.valueOf(this.array[this.start + index]);
/*     */     }
/*     */ 
/*     */     
/*     */     public Spliterator.OfInt spliterator() {
/* 594 */       return Spliterators.spliterator(this.array, this.start, this.end, 0);
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public boolean contains(Object target) {
/* 600 */       return (target instanceof Integer && Ints.indexOf(this.array, ((Integer)target).intValue(), this.start, this.end) != -1);
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public int indexOf(Object target) {
/* 606 */       if (target instanceof Integer) {
/* 607 */         int i = Ints.indexOf(this.array, ((Integer)target).intValue(), this.start, this.end);
/* 608 */         if (i >= 0) {
/* 609 */           return i - this.start;
/*     */         }
/*     */       } 
/* 612 */       return -1;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public int lastIndexOf(Object target) {
/* 618 */       if (target instanceof Integer) {
/* 619 */         int i = Ints.lastIndexOf(this.array, ((Integer)target).intValue(), this.start, this.end);
/* 620 */         if (i >= 0) {
/* 621 */           return i - this.start;
/*     */         }
/*     */       } 
/* 624 */       return -1;
/*     */     }
/*     */ 
/*     */     
/*     */     public Integer set(int index, Integer element) {
/* 629 */       Preconditions.checkElementIndex(index, size());
/* 630 */       int oldValue = this.array[this.start + index];
/*     */       
/* 632 */       this.array[this.start + index] = ((Integer)Preconditions.checkNotNull(element)).intValue();
/* 633 */       return Integer.valueOf(oldValue);
/*     */     }
/*     */ 
/*     */     
/*     */     public List<Integer> subList(int fromIndex, int toIndex) {
/* 638 */       int size = size();
/* 639 */       Preconditions.checkPositionIndexes(fromIndex, toIndex, size);
/* 640 */       if (fromIndex == toIndex) {
/* 641 */         return Collections.emptyList();
/*     */       }
/* 643 */       return new IntArrayAsList(this.array, this.start + fromIndex, this.start + toIndex);
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean equals(Object object) {
/* 648 */       if (object == this) {
/* 649 */         return true;
/*     */       }
/* 651 */       if (object instanceof IntArrayAsList) {
/* 652 */         IntArrayAsList that = (IntArrayAsList)object;
/* 653 */         int size = size();
/* 654 */         if (that.size() != size) {
/* 655 */           return false;
/*     */         }
/* 657 */         for (int i = 0; i < size; i++) {
/* 658 */           if (this.array[this.start + i] != that.array[that.start + i]) {
/* 659 */             return false;
/*     */           }
/*     */         } 
/* 662 */         return true;
/*     */       } 
/* 664 */       return super.equals(object);
/*     */     }
/*     */ 
/*     */     
/*     */     public int hashCode() {
/* 669 */       int result = 1;
/* 670 */       for (int i = this.start; i < this.end; i++) {
/* 671 */         result = 31 * result + Ints.hashCode(this.array[i]);
/*     */       }
/* 673 */       return result;
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString() {
/* 678 */       StringBuilder builder = new StringBuilder(size() * 5);
/* 679 */       builder.append('[').append(this.array[this.start]);
/* 680 */       for (int i = this.start + 1; i < this.end; i++) {
/* 681 */         builder.append(", ").append(this.array[i]);
/*     */       }
/* 683 */       return builder.append(']').toString();
/*     */     }
/*     */     
/*     */     int[] toIntArray() {
/* 687 */       return Arrays.copyOfRange(this.array, this.start, this.end);
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
/*     */   @Beta
/*     */   public static Integer tryParse(String string) {
/* 711 */     return tryParse(string, 10);
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
/*     */   public static Integer tryParse(String string, int radix) {
/* 735 */     Long result = Longs.tryParse(string, radix);
/* 736 */     if (result == null || result.longValue() != result.intValue()) {
/* 737 */       return null;
/*     */     }
/* 739 */     return Integer.valueOf(result.intValue());
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\google\common\primitives\Ints.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */