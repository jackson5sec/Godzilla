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
/*     */ @GwtCompatible(emulated = true)
/*     */ public final class Floats
/*     */ {
/*     */   public static final int BYTES = 4;
/*     */   
/*     */   public static int hashCode(float value) {
/*  72 */     return Float.valueOf(value).hashCode();
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
/*     */   public static int compare(float a, float b) {
/*  89 */     return Float.compare(a, b);
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
/*     */   public static boolean isFinite(float value) {
/* 101 */     return (Float.NEGATIVE_INFINITY < value && value < Float.POSITIVE_INFINITY);
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
/*     */   public static boolean contains(float[] array, float target) {
/* 113 */     for (float value : array) {
/* 114 */       if (value == target) {
/* 115 */         return true;
/*     */       }
/*     */     } 
/* 118 */     return false;
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
/*     */   public static int indexOf(float[] array, float target) {
/* 131 */     return indexOf(array, target, 0, array.length);
/*     */   }
/*     */ 
/*     */   
/*     */   private static int indexOf(float[] array, float target, int start, int end) {
/* 136 */     for (int i = start; i < end; i++) {
/* 137 */       if (array[i] == target) {
/* 138 */         return i;
/*     */       }
/*     */     } 
/* 141 */     return -1;
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
/*     */   public static int indexOf(float[] array, float[] target) {
/* 157 */     Preconditions.checkNotNull(array, "array");
/* 158 */     Preconditions.checkNotNull(target, "target");
/* 159 */     if (target.length == 0) {
/* 160 */       return 0;
/*     */     }
/*     */ 
/*     */     
/* 164 */     for (int i = 0; i < array.length - target.length + 1; i++) {
/* 165 */       int j = 0; while (true) { if (j < target.length) {
/* 166 */           if (array[i + j] != target[j])
/*     */             break;  j++;
/*     */           continue;
/*     */         } 
/* 170 */         return i; }
/*     */     
/* 172 */     }  return -1;
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
/*     */   public static int lastIndexOf(float[] array, float target) {
/* 185 */     return lastIndexOf(array, target, 0, array.length);
/*     */   }
/*     */ 
/*     */   
/*     */   private static int lastIndexOf(float[] array, float target, int start, int end) {
/* 190 */     for (int i = end - 1; i >= start; i--) {
/* 191 */       if (array[i] == target) {
/* 192 */         return i;
/*     */       }
/*     */     } 
/* 195 */     return -1;
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
/*     */   public static float min(float... array) {
/* 208 */     Preconditions.checkArgument((array.length > 0));
/* 209 */     float min = array[0];
/* 210 */     for (int i = 1; i < array.length; i++) {
/* 211 */       min = Math.min(min, array[i]);
/*     */     }
/* 213 */     return min;
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
/*     */   public static float max(float... array) {
/* 226 */     Preconditions.checkArgument((array.length > 0));
/* 227 */     float max = array[0];
/* 228 */     for (int i = 1; i < array.length; i++) {
/* 229 */       max = Math.max(max, array[i]);
/*     */     }
/* 231 */     return max;
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
/*     */   public static float constrainToRange(float value, float min, float max) {
/* 249 */     Preconditions.checkArgument((min <= max), "min (%s) must be less than or equal to max (%s)", Float.valueOf(min), Float.valueOf(max));
/* 250 */     return Math.min(Math.max(value, min), max);
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
/*     */   public static float[] concat(float[]... arrays) {
/* 262 */     int length = 0;
/* 263 */     for (float[] array : arrays) {
/* 264 */       length += array.length;
/*     */     }
/* 266 */     float[] result = new float[length];
/* 267 */     int pos = 0;
/* 268 */     for (float[] array : arrays) {
/* 269 */       System.arraycopy(array, 0, result, pos, array.length);
/* 270 */       pos += array.length;
/*     */     } 
/* 272 */     return result;
/*     */   }
/*     */   
/*     */   private static final class FloatConverter
/*     */     extends Converter<String, Float> implements Serializable {
/* 277 */     static final FloatConverter INSTANCE = new FloatConverter();
/*     */     private static final long serialVersionUID = 1L;
/*     */     
/*     */     protected Float doForward(String value) {
/* 281 */       return Float.valueOf(value);
/*     */     }
/*     */ 
/*     */     
/*     */     protected String doBackward(Float value) {
/* 286 */       return value.toString();
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString() {
/* 291 */       return "Floats.stringConverter()";
/*     */     }
/*     */     
/*     */     private Object readResolve() {
/* 295 */       return INSTANCE;
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
/*     */   public static Converter<String, Float> stringConverter() {
/* 309 */     return FloatConverter.INSTANCE;
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
/*     */   public static float[] ensureCapacity(float[] array, int minLength, int padding) {
/* 326 */     Preconditions.checkArgument((minLength >= 0), "Invalid minLength: %s", minLength);
/* 327 */     Preconditions.checkArgument((padding >= 0), "Invalid padding: %s", padding);
/* 328 */     return (array.length < minLength) ? Arrays.copyOf(array, minLength + padding) : array;
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
/*     */   public static String join(String separator, float... array) {
/* 344 */     Preconditions.checkNotNull(separator);
/* 345 */     if (array.length == 0) {
/* 346 */       return "";
/*     */     }
/*     */ 
/*     */     
/* 350 */     StringBuilder builder = new StringBuilder(array.length * 12);
/* 351 */     builder.append(array[0]);
/* 352 */     for (int i = 1; i < array.length; i++) {
/* 353 */       builder.append(separator).append(array[i]);
/*     */     }
/* 355 */     return builder.toString();
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
/*     */   public static Comparator<float[]> lexicographicalComparator() {
/* 372 */     return LexicographicalComparator.INSTANCE;
/*     */   }
/*     */   
/*     */   private enum LexicographicalComparator implements Comparator<float[]> {
/* 376 */     INSTANCE;
/*     */ 
/*     */     
/*     */     public int compare(float[] left, float[] right) {
/* 380 */       int minLength = Math.min(left.length, right.length);
/* 381 */       for (int i = 0; i < minLength; i++) {
/* 382 */         int result = Float.compare(left[i], right[i]);
/* 383 */         if (result != 0) {
/* 384 */           return result;
/*     */         }
/*     */       } 
/* 387 */       return left.length - right.length;
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString() {
/* 392 */       return "Floats.lexicographicalComparator()";
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
/*     */   public static void sortDescending(float[] array) {
/* 405 */     Preconditions.checkNotNull(array);
/* 406 */     sortDescending(array, 0, array.length);
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
/*     */   public static void sortDescending(float[] array, int fromIndex, int toIndex) {
/* 419 */     Preconditions.checkNotNull(array);
/* 420 */     Preconditions.checkPositionIndexes(fromIndex, toIndex, array.length);
/* 421 */     Arrays.sort(array, fromIndex, toIndex);
/* 422 */     reverse(array, fromIndex, toIndex);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void reverse(float[] array) {
/* 432 */     Preconditions.checkNotNull(array);
/* 433 */     reverse(array, 0, array.length);
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
/*     */   public static void reverse(float[] array, int fromIndex, int toIndex) {
/* 447 */     Preconditions.checkNotNull(array);
/* 448 */     Preconditions.checkPositionIndexes(fromIndex, toIndex, array.length);
/* 449 */     for (int i = fromIndex, j = toIndex - 1; i < j; i++, j--) {
/* 450 */       float tmp = array[i];
/* 451 */       array[i] = array[j];
/* 452 */       array[j] = tmp;
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
/*     */   public static float[] toArray(Collection<? extends Number> collection) {
/* 470 */     if (collection instanceof FloatArrayAsList) {
/* 471 */       return ((FloatArrayAsList)collection).toFloatArray();
/*     */     }
/*     */     
/* 474 */     Object[] boxedArray = collection.toArray();
/* 475 */     int len = boxedArray.length;
/* 476 */     float[] array = new float[len];
/* 477 */     for (int i = 0; i < len; i++)
/*     */     {
/* 479 */       array[i] = ((Number)Preconditions.checkNotNull(boxedArray[i])).floatValue();
/*     */     }
/* 481 */     return array;
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
/*     */   public static List<Float> asList(float... backingArray) {
/* 500 */     if (backingArray.length == 0) {
/* 501 */       return Collections.emptyList();
/*     */     }
/* 503 */     return new FloatArrayAsList(backingArray);
/*     */   }
/*     */   
/*     */   @GwtCompatible
/*     */   private static class FloatArrayAsList extends AbstractList<Float> implements RandomAccess, Serializable {
/*     */     final float[] array;
/*     */     final int start;
/*     */     final int end;
/*     */     private static final long serialVersionUID = 0L;
/*     */     
/*     */     FloatArrayAsList(float[] array) {
/* 514 */       this(array, 0, array.length);
/*     */     }
/*     */     
/*     */     FloatArrayAsList(float[] array, int start, int end) {
/* 518 */       this.array = array;
/* 519 */       this.start = start;
/* 520 */       this.end = end;
/*     */     }
/*     */ 
/*     */     
/*     */     public int size() {
/* 525 */       return this.end - this.start;
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean isEmpty() {
/* 530 */       return false;
/*     */     }
/*     */ 
/*     */     
/*     */     public Float get(int index) {
/* 535 */       Preconditions.checkElementIndex(index, size());
/* 536 */       return Float.valueOf(this.array[this.start + index]);
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public boolean contains(Object target) {
/* 542 */       return (target instanceof Float && Floats.indexOf(this.array, ((Float)target).floatValue(), this.start, this.end) != -1);
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public int indexOf(Object target) {
/* 548 */       if (target instanceof Float) {
/* 549 */         int i = Floats.indexOf(this.array, ((Float)target).floatValue(), this.start, this.end);
/* 550 */         if (i >= 0) {
/* 551 */           return i - this.start;
/*     */         }
/*     */       } 
/* 554 */       return -1;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public int lastIndexOf(Object target) {
/* 560 */       if (target instanceof Float) {
/* 561 */         int i = Floats.lastIndexOf(this.array, ((Float)target).floatValue(), this.start, this.end);
/* 562 */         if (i >= 0) {
/* 563 */           return i - this.start;
/*     */         }
/*     */       } 
/* 566 */       return -1;
/*     */     }
/*     */ 
/*     */     
/*     */     public Float set(int index, Float element) {
/* 571 */       Preconditions.checkElementIndex(index, size());
/* 572 */       float oldValue = this.array[this.start + index];
/*     */       
/* 574 */       this.array[this.start + index] = ((Float)Preconditions.checkNotNull(element)).floatValue();
/* 575 */       return Float.valueOf(oldValue);
/*     */     }
/*     */ 
/*     */     
/*     */     public List<Float> subList(int fromIndex, int toIndex) {
/* 580 */       int size = size();
/* 581 */       Preconditions.checkPositionIndexes(fromIndex, toIndex, size);
/* 582 */       if (fromIndex == toIndex) {
/* 583 */         return Collections.emptyList();
/*     */       }
/* 585 */       return new FloatArrayAsList(this.array, this.start + fromIndex, this.start + toIndex);
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean equals(Object object) {
/* 590 */       if (object == this) {
/* 591 */         return true;
/*     */       }
/* 593 */       if (object instanceof FloatArrayAsList) {
/* 594 */         FloatArrayAsList that = (FloatArrayAsList)object;
/* 595 */         int size = size();
/* 596 */         if (that.size() != size) {
/* 597 */           return false;
/*     */         }
/* 599 */         for (int i = 0; i < size; i++) {
/* 600 */           if (this.array[this.start + i] != that.array[that.start + i]) {
/* 601 */             return false;
/*     */           }
/*     */         } 
/* 604 */         return true;
/*     */       } 
/* 606 */       return super.equals(object);
/*     */     }
/*     */ 
/*     */     
/*     */     public int hashCode() {
/* 611 */       int result = 1;
/* 612 */       for (int i = this.start; i < this.end; i++) {
/* 613 */         result = 31 * result + Floats.hashCode(this.array[i]);
/*     */       }
/* 615 */       return result;
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString() {
/* 620 */       StringBuilder builder = new StringBuilder(size() * 12);
/* 621 */       builder.append('[').append(this.array[this.start]);
/* 622 */       for (int i = this.start + 1; i < this.end; i++) {
/* 623 */         builder.append(", ").append(this.array[i]);
/*     */       }
/* 625 */       return builder.append(']').toString();
/*     */     }
/*     */     
/*     */     float[] toFloatArray() {
/* 629 */       return Arrays.copyOfRange(this.array, this.start, this.end);
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
/*     */   @GwtIncompatible
/*     */   public static Float tryParse(String string) {
/* 654 */     if (Doubles.FLOATING_POINT_PATTERN.matcher(string).matches()) {
/*     */       
/*     */       try {
/*     */         
/* 658 */         return Float.valueOf(Float.parseFloat(string));
/* 659 */       } catch (NumberFormatException numberFormatException) {}
/*     */     }
/*     */ 
/*     */ 
/*     */     
/* 664 */     return null;
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\google\common\primitives\Floats.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */