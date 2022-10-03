/*     */ package com.google.common.primitives;
/*     */ 
/*     */ import com.google.common.annotations.Beta;
/*     */ import com.google.common.annotations.GwtCompatible;
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
/*     */ @GwtCompatible
/*     */ public final class Booleans
/*     */ {
/*     */   private enum BooleanComparator
/*     */     implements Comparator<Boolean>
/*     */   {
/*  50 */     TRUE_FIRST(1, "Booleans.trueFirst()"),
/*  51 */     FALSE_FIRST(-1, "Booleans.falseFirst()");
/*     */     
/*     */     private final int trueValue;
/*     */     private final String toString;
/*     */     
/*     */     BooleanComparator(int trueValue, String toString) {
/*  57 */       this.trueValue = trueValue;
/*  58 */       this.toString = toString;
/*     */     }
/*     */ 
/*     */     
/*     */     public int compare(Boolean a, Boolean b) {
/*  63 */       int aVal = a.booleanValue() ? this.trueValue : 0;
/*  64 */       int bVal = b.booleanValue() ? this.trueValue : 0;
/*  65 */       return bVal - aVal;
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString() {
/*  70 */       return this.toString;
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
/*     */   public static Comparator<Boolean> trueFirst() {
/*  84 */     return BooleanComparator.TRUE_FIRST;
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
/*     */   public static Comparator<Boolean> falseFirst() {
/*  97 */     return BooleanComparator.FALSE_FIRST;
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
/*     */   public static int hashCode(boolean value) {
/* 110 */     return value ? 1231 : 1237;
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
/*     */   public static int compare(boolean a, boolean b) {
/* 127 */     return (a == b) ? 0 : (a ? 1 : -1);
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
/*     */   public static boolean contains(boolean[] array, boolean target) {
/* 142 */     for (boolean value : array) {
/* 143 */       if (value == target) {
/* 144 */         return true;
/*     */       }
/*     */     } 
/* 147 */     return false;
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
/*     */   public static int indexOf(boolean[] array, boolean target) {
/* 162 */     return indexOf(array, target, 0, array.length);
/*     */   }
/*     */ 
/*     */   
/*     */   private static int indexOf(boolean[] array, boolean target, int start, int end) {
/* 167 */     for (int i = start; i < end; i++) {
/* 168 */       if (array[i] == target) {
/* 169 */         return i;
/*     */       }
/*     */     } 
/* 172 */     return -1;
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
/*     */   public static int indexOf(boolean[] array, boolean[] target) {
/* 186 */     Preconditions.checkNotNull(array, "array");
/* 187 */     Preconditions.checkNotNull(target, "target");
/* 188 */     if (target.length == 0) {
/* 189 */       return 0;
/*     */     }
/*     */ 
/*     */     
/* 193 */     for (int i = 0; i < array.length - target.length + 1; i++) {
/* 194 */       int j = 0; while (true) { if (j < target.length) {
/* 195 */           if (array[i + j] != target[j])
/*     */             break;  j++;
/*     */           continue;
/*     */         } 
/* 199 */         return i; }
/*     */     
/* 201 */     }  return -1;
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
/*     */   public static int lastIndexOf(boolean[] array, boolean target) {
/* 213 */     return lastIndexOf(array, target, 0, array.length);
/*     */   }
/*     */ 
/*     */   
/*     */   private static int lastIndexOf(boolean[] array, boolean target, int start, int end) {
/* 218 */     for (int i = end - 1; i >= start; i--) {
/* 219 */       if (array[i] == target) {
/* 220 */         return i;
/*     */       }
/*     */     } 
/* 223 */     return -1;
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
/*     */   public static boolean[] concat(boolean[]... arrays) {
/* 235 */     int length = 0;
/* 236 */     for (boolean[] array : arrays) {
/* 237 */       length += array.length;
/*     */     }
/* 239 */     boolean[] result = new boolean[length];
/* 240 */     int pos = 0;
/* 241 */     for (boolean[] array : arrays) {
/* 242 */       System.arraycopy(array, 0, result, pos, array.length);
/* 243 */       pos += array.length;
/*     */     } 
/* 245 */     return result;
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
/*     */   public static boolean[] ensureCapacity(boolean[] array, int minLength, int padding) {
/* 262 */     Preconditions.checkArgument((minLength >= 0), "Invalid minLength: %s", minLength);
/* 263 */     Preconditions.checkArgument((padding >= 0), "Invalid padding: %s", padding);
/* 264 */     return (array.length < minLength) ? Arrays.copyOf(array, minLength + padding) : array;
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
/*     */   public static String join(String separator, boolean... array) {
/* 277 */     Preconditions.checkNotNull(separator);
/* 278 */     if (array.length == 0) {
/* 279 */       return "";
/*     */     }
/*     */ 
/*     */     
/* 283 */     StringBuilder builder = new StringBuilder(array.length * 7);
/* 284 */     builder.append(array[0]);
/* 285 */     for (int i = 1; i < array.length; i++) {
/* 286 */       builder.append(separator).append(array[i]);
/*     */     }
/* 288 */     return builder.toString();
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
/*     */   public static Comparator<boolean[]> lexicographicalComparator() {
/* 305 */     return LexicographicalComparator.INSTANCE;
/*     */   }
/*     */   
/*     */   private enum LexicographicalComparator implements Comparator<boolean[]> {
/* 309 */     INSTANCE;
/*     */ 
/*     */     
/*     */     public int compare(boolean[] left, boolean[] right) {
/* 313 */       int minLength = Math.min(left.length, right.length);
/* 314 */       for (int i = 0; i < minLength; i++) {
/* 315 */         int result = Booleans.compare(left[i], right[i]);
/* 316 */         if (result != 0) {
/* 317 */           return result;
/*     */         }
/*     */       } 
/* 320 */       return left.length - right.length;
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString() {
/* 325 */       return "Booleans.lexicographicalComparator()";
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
/*     */   public static boolean[] toArray(Collection<Boolean> collection) {
/* 344 */     if (collection instanceof BooleanArrayAsList) {
/* 345 */       return ((BooleanArrayAsList)collection).toBooleanArray();
/*     */     }
/*     */     
/* 348 */     Object[] boxedArray = collection.toArray();
/* 349 */     int len = boxedArray.length;
/* 350 */     boolean[] array = new boolean[len];
/* 351 */     for (int i = 0; i < len; i++)
/*     */     {
/* 353 */       array[i] = ((Boolean)Preconditions.checkNotNull(boxedArray[i])).booleanValue();
/*     */     }
/* 355 */     return array;
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
/*     */   public static List<Boolean> asList(boolean... backingArray) {
/* 371 */     if (backingArray.length == 0) {
/* 372 */       return Collections.emptyList();
/*     */     }
/* 374 */     return new BooleanArrayAsList(backingArray);
/*     */   }
/*     */   
/*     */   @GwtCompatible
/*     */   private static class BooleanArrayAsList extends AbstractList<Boolean> implements RandomAccess, Serializable {
/*     */     final boolean[] array;
/*     */     final int start;
/*     */     final int end;
/*     */     private static final long serialVersionUID = 0L;
/*     */     
/*     */     BooleanArrayAsList(boolean[] array) {
/* 385 */       this(array, 0, array.length);
/*     */     }
/*     */     
/*     */     BooleanArrayAsList(boolean[] array, int start, int end) {
/* 389 */       this.array = array;
/* 390 */       this.start = start;
/* 391 */       this.end = end;
/*     */     }
/*     */ 
/*     */     
/*     */     public int size() {
/* 396 */       return this.end - this.start;
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean isEmpty() {
/* 401 */       return false;
/*     */     }
/*     */ 
/*     */     
/*     */     public Boolean get(int index) {
/* 406 */       Preconditions.checkElementIndex(index, size());
/* 407 */       return Boolean.valueOf(this.array[this.start + index]);
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public boolean contains(Object target) {
/* 413 */       return (target instanceof Boolean && Booleans
/* 414 */         .indexOf(this.array, ((Boolean)target).booleanValue(), this.start, this.end) != -1);
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public int indexOf(Object target) {
/* 420 */       if (target instanceof Boolean) {
/* 421 */         int i = Booleans.indexOf(this.array, ((Boolean)target).booleanValue(), this.start, this.end);
/* 422 */         if (i >= 0) {
/* 423 */           return i - this.start;
/*     */         }
/*     */       } 
/* 426 */       return -1;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public int lastIndexOf(Object target) {
/* 432 */       if (target instanceof Boolean) {
/* 433 */         int i = Booleans.lastIndexOf(this.array, ((Boolean)target).booleanValue(), this.start, this.end);
/* 434 */         if (i >= 0) {
/* 435 */           return i - this.start;
/*     */         }
/*     */       } 
/* 438 */       return -1;
/*     */     }
/*     */ 
/*     */     
/*     */     public Boolean set(int index, Boolean element) {
/* 443 */       Preconditions.checkElementIndex(index, size());
/* 444 */       boolean oldValue = this.array[this.start + index];
/*     */       
/* 446 */       this.array[this.start + index] = ((Boolean)Preconditions.checkNotNull(element)).booleanValue();
/* 447 */       return Boolean.valueOf(oldValue);
/*     */     }
/*     */ 
/*     */     
/*     */     public List<Boolean> subList(int fromIndex, int toIndex) {
/* 452 */       int size = size();
/* 453 */       Preconditions.checkPositionIndexes(fromIndex, toIndex, size);
/* 454 */       if (fromIndex == toIndex) {
/* 455 */         return Collections.emptyList();
/*     */       }
/* 457 */       return new BooleanArrayAsList(this.array, this.start + fromIndex, this.start + toIndex);
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean equals(Object object) {
/* 462 */       if (object == this) {
/* 463 */         return true;
/*     */       }
/* 465 */       if (object instanceof BooleanArrayAsList) {
/* 466 */         BooleanArrayAsList that = (BooleanArrayAsList)object;
/* 467 */         int size = size();
/* 468 */         if (that.size() != size) {
/* 469 */           return false;
/*     */         }
/* 471 */         for (int i = 0; i < size; i++) {
/* 472 */           if (this.array[this.start + i] != that.array[that.start + i]) {
/* 473 */             return false;
/*     */           }
/*     */         } 
/* 476 */         return true;
/*     */       } 
/* 478 */       return super.equals(object);
/*     */     }
/*     */ 
/*     */     
/*     */     public int hashCode() {
/* 483 */       int result = 1;
/* 484 */       for (int i = this.start; i < this.end; i++) {
/* 485 */         result = 31 * result + Booleans.hashCode(this.array[i]);
/*     */       }
/* 487 */       return result;
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString() {
/* 492 */       StringBuilder builder = new StringBuilder(size() * 7);
/* 493 */       builder.append(this.array[this.start] ? "[true" : "[false");
/* 494 */       for (int i = this.start + 1; i < this.end; i++) {
/* 495 */         builder.append(this.array[i] ? ", true" : ", false");
/*     */       }
/* 497 */       return builder.append(']').toString();
/*     */     }
/*     */     
/*     */     boolean[] toBooleanArray() {
/* 501 */       return Arrays.copyOfRange(this.array, this.start, this.end);
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
/*     */   @Beta
/*     */   public static int countTrue(boolean... values) {
/* 514 */     int count = 0;
/* 515 */     for (boolean value : values) {
/* 516 */       if (value) {
/* 517 */         count++;
/*     */       }
/*     */     } 
/* 520 */     return count;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void reverse(boolean[] array) {
/* 530 */     Preconditions.checkNotNull(array);
/* 531 */     reverse(array, 0, array.length);
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
/*     */   public static void reverse(boolean[] array, int fromIndex, int toIndex) {
/* 545 */     Preconditions.checkNotNull(array);
/* 546 */     Preconditions.checkPositionIndexes(fromIndex, toIndex, array.length);
/* 547 */     for (int i = fromIndex, j = toIndex - 1; i < j; i++, j--) {
/* 548 */       boolean tmp = array[i];
/* 549 */       array[i] = array[j];
/* 550 */       array[j] = tmp;
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\google\common\primitives\Booleans.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */