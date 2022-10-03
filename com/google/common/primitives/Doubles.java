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
/*     */ import java.util.Spliterator;
/*     */ import java.util.Spliterators;
/*     */ import java.util.regex.Pattern;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
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
/*     */ public final class Doubles
/*     */ {
/*     */   public static final int BYTES = 8;
/*     */   
/*     */   public static int hashCode(double value) {
/*  73 */     return Double.valueOf(value).hashCode();
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
/*     */   public static int compare(double a, double b) {
/*  94 */     return Double.compare(a, b);
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
/*     */   public static boolean isFinite(double value) {
/* 106 */     return (Double.NEGATIVE_INFINITY < value && value < Double.POSITIVE_INFINITY);
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
/*     */   public static boolean contains(double[] array, double target) {
/* 118 */     for (double value : array) {
/* 119 */       if (value == target) {
/* 120 */         return true;
/*     */       }
/*     */     } 
/* 123 */     return false;
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
/*     */   public static int indexOf(double[] array, double target) {
/* 136 */     return indexOf(array, target, 0, array.length);
/*     */   }
/*     */ 
/*     */   
/*     */   private static int indexOf(double[] array, double target, int start, int end) {
/* 141 */     for (int i = start; i < end; i++) {
/* 142 */       if (array[i] == target) {
/* 143 */         return i;
/*     */       }
/*     */     } 
/* 146 */     return -1;
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
/*     */   public static int indexOf(double[] array, double[] target) {
/* 162 */     Preconditions.checkNotNull(array, "array");
/* 163 */     Preconditions.checkNotNull(target, "target");
/* 164 */     if (target.length == 0) {
/* 165 */       return 0;
/*     */     }
/*     */ 
/*     */     
/* 169 */     for (int i = 0; i < array.length - target.length + 1; i++) {
/* 170 */       int j = 0; while (true) { if (j < target.length) {
/* 171 */           if (array[i + j] != target[j])
/*     */             break;  j++;
/*     */           continue;
/*     */         } 
/* 175 */         return i; }
/*     */     
/* 177 */     }  return -1;
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
/*     */   public static int lastIndexOf(double[] array, double target) {
/* 190 */     return lastIndexOf(array, target, 0, array.length);
/*     */   }
/*     */ 
/*     */   
/*     */   private static int lastIndexOf(double[] array, double target, int start, int end) {
/* 195 */     for (int i = end - 1; i >= start; i--) {
/* 196 */       if (array[i] == target) {
/* 197 */         return i;
/*     */       }
/*     */     } 
/* 200 */     return -1;
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
/*     */   public static double min(double... array) {
/* 213 */     Preconditions.checkArgument((array.length > 0));
/* 214 */     double min = array[0];
/* 215 */     for (int i = 1; i < array.length; i++) {
/* 216 */       min = Math.min(min, array[i]);
/*     */     }
/* 218 */     return min;
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
/*     */   public static double max(double... array) {
/* 231 */     Preconditions.checkArgument((array.length > 0));
/* 232 */     double max = array[0];
/* 233 */     for (int i = 1; i < array.length; i++) {
/* 234 */       max = Math.max(max, array[i]);
/*     */     }
/* 236 */     return max;
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
/*     */   public static double constrainToRange(double value, double min, double max) {
/* 254 */     Preconditions.checkArgument((min <= max), "min (%s) must be less than or equal to max (%s)", Double.valueOf(min), Double.valueOf(max));
/* 255 */     return Math.min(Math.max(value, min), max);
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
/*     */   public static double[] concat(double[]... arrays) {
/* 267 */     int length = 0;
/* 268 */     for (double[] array : arrays) {
/* 269 */       length += array.length;
/*     */     }
/* 271 */     double[] result = new double[length];
/* 272 */     int pos = 0;
/* 273 */     for (double[] array : arrays) {
/* 274 */       System.arraycopy(array, 0, result, pos, array.length);
/* 275 */       pos += array.length;
/*     */     } 
/* 277 */     return result;
/*     */   }
/*     */   
/*     */   private static final class DoubleConverter
/*     */     extends Converter<String, Double> implements Serializable {
/* 282 */     static final DoubleConverter INSTANCE = new DoubleConverter();
/*     */     private static final long serialVersionUID = 1L;
/*     */     
/*     */     protected Double doForward(String value) {
/* 286 */       return Double.valueOf(value);
/*     */     }
/*     */ 
/*     */     
/*     */     protected String doBackward(Double value) {
/* 291 */       return value.toString();
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString() {
/* 296 */       return "Doubles.stringConverter()";
/*     */     }
/*     */     
/*     */     private Object readResolve() {
/* 300 */       return INSTANCE;
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
/*     */   @Beta
/*     */   public static Converter<String, Double> stringConverter() {
/* 314 */     return DoubleConverter.INSTANCE;
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
/*     */   public static double[] ensureCapacity(double[] array, int minLength, int padding) {
/* 331 */     Preconditions.checkArgument((minLength >= 0), "Invalid minLength: %s", minLength);
/* 332 */     Preconditions.checkArgument((padding >= 0), "Invalid padding: %s", padding);
/* 333 */     return (array.length < minLength) ? Arrays.copyOf(array, minLength + padding) : array;
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
/*     */   public static String join(String separator, double... array) {
/* 349 */     Preconditions.checkNotNull(separator);
/* 350 */     if (array.length == 0) {
/* 351 */       return "";
/*     */     }
/*     */ 
/*     */     
/* 355 */     StringBuilder builder = new StringBuilder(array.length * 12);
/* 356 */     builder.append(array[0]);
/* 357 */     for (int i = 1; i < array.length; i++) {
/* 358 */       builder.append(separator).append(array[i]);
/*     */     }
/* 360 */     return builder.toString();
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
/*     */   public static Comparator<double[]> lexicographicalComparator() {
/* 377 */     return LexicographicalComparator.INSTANCE;
/*     */   }
/*     */   
/*     */   private enum LexicographicalComparator implements Comparator<double[]> {
/* 381 */     INSTANCE;
/*     */ 
/*     */     
/*     */     public int compare(double[] left, double[] right) {
/* 385 */       int minLength = Math.min(left.length, right.length);
/* 386 */       for (int i = 0; i < minLength; i++) {
/* 387 */         int result = Double.compare(left[i], right[i]);
/* 388 */         if (result != 0) {
/* 389 */           return result;
/*     */         }
/*     */       } 
/* 392 */       return left.length - right.length;
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString() {
/* 397 */       return "Doubles.lexicographicalComparator()";
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
/*     */   public static void sortDescending(double[] array) {
/* 410 */     Preconditions.checkNotNull(array);
/* 411 */     sortDescending(array, 0, array.length);
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
/*     */   public static void sortDescending(double[] array, int fromIndex, int toIndex) {
/* 424 */     Preconditions.checkNotNull(array);
/* 425 */     Preconditions.checkPositionIndexes(fromIndex, toIndex, array.length);
/* 426 */     Arrays.sort(array, fromIndex, toIndex);
/* 427 */     reverse(array, fromIndex, toIndex);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void reverse(double[] array) {
/* 437 */     Preconditions.checkNotNull(array);
/* 438 */     reverse(array, 0, array.length);
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
/*     */   public static void reverse(double[] array, int fromIndex, int toIndex) {
/* 452 */     Preconditions.checkNotNull(array);
/* 453 */     Preconditions.checkPositionIndexes(fromIndex, toIndex, array.length);
/* 454 */     for (int i = fromIndex, j = toIndex - 1; i < j; i++, j--) {
/* 455 */       double tmp = array[i];
/* 456 */       array[i] = array[j];
/* 457 */       array[j] = tmp;
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
/*     */   public static double[] toArray(Collection<? extends Number> collection) {
/* 475 */     if (collection instanceof DoubleArrayAsList) {
/* 476 */       return ((DoubleArrayAsList)collection).toDoubleArray();
/*     */     }
/*     */     
/* 479 */     Object[] boxedArray = collection.toArray();
/* 480 */     int len = boxedArray.length;
/* 481 */     double[] array = new double[len];
/* 482 */     for (int i = 0; i < len; i++)
/*     */     {
/* 484 */       array[i] = ((Number)Preconditions.checkNotNull(boxedArray[i])).doubleValue();
/*     */     }
/* 486 */     return array;
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
/*     */   public static List<Double> asList(double... backingArray) {
/* 508 */     if (backingArray.length == 0) {
/* 509 */       return Collections.emptyList();
/*     */     }
/* 511 */     return new DoubleArrayAsList(backingArray);
/*     */   }
/*     */   
/*     */   @GwtCompatible
/*     */   private static class DoubleArrayAsList extends AbstractList<Double> implements RandomAccess, Serializable {
/*     */     final double[] array;
/*     */     final int start;
/*     */     final int end;
/*     */     private static final long serialVersionUID = 0L;
/*     */     
/*     */     DoubleArrayAsList(double[] array) {
/* 522 */       this(array, 0, array.length);
/*     */     }
/*     */     
/*     */     DoubleArrayAsList(double[] array, int start, int end) {
/* 526 */       this.array = array;
/* 527 */       this.start = start;
/* 528 */       this.end = end;
/*     */     }
/*     */ 
/*     */     
/*     */     public int size() {
/* 533 */       return this.end - this.start;
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean isEmpty() {
/* 538 */       return false;
/*     */     }
/*     */ 
/*     */     
/*     */     public Double get(int index) {
/* 543 */       Preconditions.checkElementIndex(index, size());
/* 544 */       return Double.valueOf(this.array[this.start + index]);
/*     */     }
/*     */ 
/*     */     
/*     */     public Spliterator.OfDouble spliterator() {
/* 549 */       return Spliterators.spliterator(this.array, this.start, this.end, 0);
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public boolean contains(Object target) {
/* 555 */       return (target instanceof Double && Doubles
/* 556 */         .indexOf(this.array, ((Double)target).doubleValue(), this.start, this.end) != -1);
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public int indexOf(Object target) {
/* 562 */       if (target instanceof Double) {
/* 563 */         int i = Doubles.indexOf(this.array, ((Double)target).doubleValue(), this.start, this.end);
/* 564 */         if (i >= 0) {
/* 565 */           return i - this.start;
/*     */         }
/*     */       } 
/* 568 */       return -1;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public int lastIndexOf(Object target) {
/* 574 */       if (target instanceof Double) {
/* 575 */         int i = Doubles.lastIndexOf(this.array, ((Double)target).doubleValue(), this.start, this.end);
/* 576 */         if (i >= 0) {
/* 577 */           return i - this.start;
/*     */         }
/*     */       } 
/* 580 */       return -1;
/*     */     }
/*     */ 
/*     */     
/*     */     public Double set(int index, Double element) {
/* 585 */       Preconditions.checkElementIndex(index, size());
/* 586 */       double oldValue = this.array[this.start + index];
/*     */       
/* 588 */       this.array[this.start + index] = ((Double)Preconditions.checkNotNull(element)).doubleValue();
/* 589 */       return Double.valueOf(oldValue);
/*     */     }
/*     */ 
/*     */     
/*     */     public List<Double> subList(int fromIndex, int toIndex) {
/* 594 */       int size = size();
/* 595 */       Preconditions.checkPositionIndexes(fromIndex, toIndex, size);
/* 596 */       if (fromIndex == toIndex) {
/* 597 */         return Collections.emptyList();
/*     */       }
/* 599 */       return new DoubleArrayAsList(this.array, this.start + fromIndex, this.start + toIndex);
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean equals(Object object) {
/* 604 */       if (object == this) {
/* 605 */         return true;
/*     */       }
/* 607 */       if (object instanceof DoubleArrayAsList) {
/* 608 */         DoubleArrayAsList that = (DoubleArrayAsList)object;
/* 609 */         int size = size();
/* 610 */         if (that.size() != size) {
/* 611 */           return false;
/*     */         }
/* 613 */         for (int i = 0; i < size; i++) {
/* 614 */           if (this.array[this.start + i] != that.array[that.start + i]) {
/* 615 */             return false;
/*     */           }
/*     */         } 
/* 618 */         return true;
/*     */       } 
/* 620 */       return super.equals(object);
/*     */     }
/*     */ 
/*     */     
/*     */     public int hashCode() {
/* 625 */       int result = 1;
/* 626 */       for (int i = this.start; i < this.end; i++) {
/* 627 */         result = 31 * result + Doubles.hashCode(this.array[i]);
/*     */       }
/* 629 */       return result;
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString() {
/* 634 */       StringBuilder builder = new StringBuilder(size() * 12);
/* 635 */       builder.append('[').append(this.array[this.start]);
/* 636 */       for (int i = this.start + 1; i < this.end; i++) {
/* 637 */         builder.append(", ").append(this.array[i]);
/*     */       }
/* 639 */       return builder.append(']').toString();
/*     */     }
/*     */     
/*     */     double[] toDoubleArray() {
/* 643 */       return Arrays.copyOfRange(this.array, this.start, this.end);
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
/*     */   @GwtIncompatible
/* 657 */   static final Pattern FLOATING_POINT_PATTERN = fpPattern();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @GwtIncompatible
/*     */   private static Pattern fpPattern() {
/* 668 */     String decimal = "(?:\\d+#(?:\\.\\d*#)?|\\.\\d+#)";
/* 669 */     String completeDec = decimal + "(?:[eE][+-]?\\d+#)?[fFdD]?";
/* 670 */     String hex = "(?:[0-9a-fA-F]+#(?:\\.[0-9a-fA-F]*#)?|\\.[0-9a-fA-F]+#)";
/* 671 */     String completeHex = "0[xX]" + hex + "[pP][+-]?\\d+#[fFdD]?";
/* 672 */     String fpPattern = "[+-]?(?:NaN|Infinity|" + completeDec + "|" + completeHex + ")";
/*     */     
/* 674 */     fpPattern = fpPattern.replace("#", "+");
/*     */ 
/*     */ 
/*     */     
/* 678 */     return 
/*     */       
/* 680 */       Pattern.compile(fpPattern);
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
/*     */   @GwtIncompatible
/*     */   public static Double tryParse(String string) {
/* 702 */     if (FLOATING_POINT_PATTERN.matcher(string).matches()) {
/*     */       
/*     */       try {
/*     */         
/* 706 */         return Double.valueOf(Double.parseDouble(string));
/* 707 */       } catch (NumberFormatException numberFormatException) {}
/*     */     }
/*     */ 
/*     */ 
/*     */     
/* 712 */     return null;
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\google\common\primitives\Doubles.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */