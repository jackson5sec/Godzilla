/*     */ package com.google.common.primitives;
/*     */ 
/*     */ import com.google.common.annotations.Beta;
/*     */ import com.google.common.annotations.GwtCompatible;
/*     */ import com.google.common.annotations.GwtIncompatible;
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
/*     */ @GwtCompatible(emulated = true)
/*     */ public final class Chars
/*     */ {
/*     */   public static final int BYTES = 2;
/*     */   
/*     */   public static int hashCode(char value) {
/*  69 */     return value;
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
/*     */   public static char checkedCast(long value) {
/*  81 */     char result = (char)(int)value;
/*  82 */     Preconditions.checkArgument((result == value), "Out of range: %s", value);
/*  83 */     return result;
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
/*     */   public static char saturatedCast(long value) {
/*  95 */     if (value > 65535L) {
/*  96 */       return Character.MAX_VALUE;
/*     */     }
/*  98 */     if (value < 0L) {
/*  99 */       return Character.MIN_VALUE;
/*     */     }
/* 101 */     return (char)(int)value;
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
/*     */   public static int compare(char a, char b) {
/* 117 */     return a - b;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static boolean contains(char[] array, char target) {
/* 128 */     for (char value : array) {
/* 129 */       if (value == target) {
/* 130 */         return true;
/*     */       }
/*     */     } 
/* 133 */     return false;
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
/*     */   public static int indexOf(char[] array, char target) {
/* 145 */     return indexOf(array, target, 0, array.length);
/*     */   }
/*     */ 
/*     */   
/*     */   private static int indexOf(char[] array, char target, int start, int end) {
/* 150 */     for (int i = start; i < end; i++) {
/* 151 */       if (array[i] == target) {
/* 152 */         return i;
/*     */       }
/*     */     } 
/* 155 */     return -1;
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
/*     */   public static int indexOf(char[] array, char[] target) {
/* 169 */     Preconditions.checkNotNull(array, "array");
/* 170 */     Preconditions.checkNotNull(target, "target");
/* 171 */     if (target.length == 0) {
/* 172 */       return 0;
/*     */     }
/*     */ 
/*     */     
/* 176 */     for (int i = 0; i < array.length - target.length + 1; i++) {
/* 177 */       int j = 0; while (true) { if (j < target.length) {
/* 178 */           if (array[i + j] != target[j])
/*     */             break;  j++;
/*     */           continue;
/*     */         } 
/* 182 */         return i; }
/*     */     
/* 184 */     }  return -1;
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
/*     */   public static int lastIndexOf(char[] array, char target) {
/* 196 */     return lastIndexOf(array, target, 0, array.length);
/*     */   }
/*     */ 
/*     */   
/*     */   private static int lastIndexOf(char[] array, char target, int start, int end) {
/* 201 */     for (int i = end - 1; i >= start; i--) {
/* 202 */       if (array[i] == target) {
/* 203 */         return i;
/*     */       }
/*     */     } 
/* 206 */     return -1;
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
/*     */   public static char min(char... array) {
/* 218 */     Preconditions.checkArgument((array.length > 0));
/* 219 */     char min = array[0];
/* 220 */     for (int i = 1; i < array.length; i++) {
/* 221 */       if (array[i] < min) {
/* 222 */         min = array[i];
/*     */       }
/*     */     } 
/* 225 */     return min;
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
/*     */   public static char max(char... array) {
/* 237 */     Preconditions.checkArgument((array.length > 0));
/* 238 */     char max = array[0];
/* 239 */     for (int i = 1; i < array.length; i++) {
/* 240 */       if (array[i] > max) {
/* 241 */         max = array[i];
/*     */       }
/*     */     } 
/* 244 */     return max;
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
/*     */   public static char constrainToRange(char value, char min, char max) {
/* 262 */     Preconditions.checkArgument((min <= max), "min (%s) must be less than or equal to max (%s)", min, max);
/* 263 */     return (value < min) ? min : ((value < max) ? value : max);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static char[] concat(char[]... arrays) {
/* 274 */     int length = 0;
/* 275 */     for (char[] array : arrays) {
/* 276 */       length += array.length;
/*     */     }
/* 278 */     char[] result = new char[length];
/* 279 */     int pos = 0;
/* 280 */     for (char[] array : arrays) {
/* 281 */       System.arraycopy(array, 0, result, pos, array.length);
/* 282 */       pos += array.length;
/*     */     } 
/* 284 */     return result;
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
/*     */   public static byte[] toByteArray(char value) {
/* 298 */     return new byte[] { (byte)(value >> 8), (byte)value };
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
/*     */   public static char fromByteArray(byte[] bytes) {
/* 313 */     Preconditions.checkArgument((bytes.length >= 2), "array too small: %s < %s", bytes.length, 2);
/* 314 */     return fromBytes(bytes[0], bytes[1]);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @GwtIncompatible
/*     */   public static char fromBytes(byte b1, byte b2) {
/* 325 */     return (char)(b1 << 8 | b2 & 0xFF);
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
/*     */   public static char[] ensureCapacity(char[] array, int minLength, int padding) {
/* 342 */     Preconditions.checkArgument((minLength >= 0), "Invalid minLength: %s", minLength);
/* 343 */     Preconditions.checkArgument((padding >= 0), "Invalid padding: %s", padding);
/* 344 */     return (array.length < minLength) ? Arrays.copyOf(array, minLength + padding) : array;
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
/*     */   public static String join(String separator, char... array) {
/* 356 */     Preconditions.checkNotNull(separator);
/* 357 */     int len = array.length;
/* 358 */     if (len == 0) {
/* 359 */       return "";
/*     */     }
/*     */     
/* 362 */     StringBuilder builder = new StringBuilder(len + separator.length() * (len - 1));
/* 363 */     builder.append(array[0]);
/* 364 */     for (int i = 1; i < len; i++) {
/* 365 */       builder.append(separator).append(array[i]);
/*     */     }
/* 367 */     return builder.toString();
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
/*     */   public static Comparator<char[]> lexicographicalComparator() {
/* 385 */     return LexicographicalComparator.INSTANCE;
/*     */   }
/*     */   
/*     */   private enum LexicographicalComparator implements Comparator<char[]> {
/* 389 */     INSTANCE;
/*     */ 
/*     */     
/*     */     public int compare(char[] left, char[] right) {
/* 393 */       int minLength = Math.min(left.length, right.length);
/* 394 */       for (int i = 0; i < minLength; i++) {
/* 395 */         int result = Chars.compare(left[i], right[i]);
/* 396 */         if (result != 0) {
/* 397 */           return result;
/*     */         }
/*     */       } 
/* 400 */       return left.length - right.length;
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString() {
/* 405 */       return "Chars.lexicographicalComparator()";
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
/*     */   public static char[] toArray(Collection<Character> collection) {
/* 422 */     if (collection instanceof CharArrayAsList) {
/* 423 */       return ((CharArrayAsList)collection).toCharArray();
/*     */     }
/*     */     
/* 426 */     Object[] boxedArray = collection.toArray();
/* 427 */     int len = boxedArray.length;
/* 428 */     char[] array = new char[len];
/* 429 */     for (int i = 0; i < len; i++)
/*     */     {
/* 431 */       array[i] = ((Character)Preconditions.checkNotNull(boxedArray[i])).charValue();
/*     */     }
/* 433 */     return array;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void sortDescending(char[] array) {
/* 442 */     Preconditions.checkNotNull(array);
/* 443 */     sortDescending(array, 0, array.length);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void sortDescending(char[] array, int fromIndex, int toIndex) {
/* 453 */     Preconditions.checkNotNull(array);
/* 454 */     Preconditions.checkPositionIndexes(fromIndex, toIndex, array.length);
/* 455 */     Arrays.sort(array, fromIndex, toIndex);
/* 456 */     reverse(array, fromIndex, toIndex);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void reverse(char[] array) {
/* 466 */     Preconditions.checkNotNull(array);
/* 467 */     reverse(array, 0, array.length);
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
/*     */   public static void reverse(char[] array, int fromIndex, int toIndex) {
/* 481 */     Preconditions.checkNotNull(array);
/* 482 */     Preconditions.checkPositionIndexes(fromIndex, toIndex, array.length);
/* 483 */     for (int i = fromIndex, j = toIndex - 1; i < j; i++, j--) {
/* 484 */       char tmp = array[i];
/* 485 */       array[i] = array[j];
/* 486 */       array[j] = tmp;
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
/*     */   public static List<Character> asList(char... backingArray) {
/* 503 */     if (backingArray.length == 0) {
/* 504 */       return Collections.emptyList();
/*     */     }
/* 506 */     return new CharArrayAsList(backingArray);
/*     */   }
/*     */   
/*     */   @GwtCompatible
/*     */   private static class CharArrayAsList extends AbstractList<Character> implements RandomAccess, Serializable {
/*     */     final char[] array;
/*     */     final int start;
/*     */     final int end;
/*     */     private static final long serialVersionUID = 0L;
/*     */     
/*     */     CharArrayAsList(char[] array) {
/* 517 */       this(array, 0, array.length);
/*     */     }
/*     */     
/*     */     CharArrayAsList(char[] array, int start, int end) {
/* 521 */       this.array = array;
/* 522 */       this.start = start;
/* 523 */       this.end = end;
/*     */     }
/*     */ 
/*     */     
/*     */     public int size() {
/* 528 */       return this.end - this.start;
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean isEmpty() {
/* 533 */       return false;
/*     */     }
/*     */ 
/*     */     
/*     */     public Character get(int index) {
/* 538 */       Preconditions.checkElementIndex(index, size());
/* 539 */       return Character.valueOf(this.array[this.start + index]);
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public boolean contains(Object target) {
/* 545 */       return (target instanceof Character && Chars
/* 546 */         .indexOf(this.array, ((Character)target).charValue(), this.start, this.end) != -1);
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public int indexOf(Object target) {
/* 552 */       if (target instanceof Character) {
/* 553 */         int i = Chars.indexOf(this.array, ((Character)target).charValue(), this.start, this.end);
/* 554 */         if (i >= 0) {
/* 555 */           return i - this.start;
/*     */         }
/*     */       } 
/* 558 */       return -1;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public int lastIndexOf(Object target) {
/* 564 */       if (target instanceof Character) {
/* 565 */         int i = Chars.lastIndexOf(this.array, ((Character)target).charValue(), this.start, this.end);
/* 566 */         if (i >= 0) {
/* 567 */           return i - this.start;
/*     */         }
/*     */       } 
/* 570 */       return -1;
/*     */     }
/*     */ 
/*     */     
/*     */     public Character set(int index, Character element) {
/* 575 */       Preconditions.checkElementIndex(index, size());
/* 576 */       char oldValue = this.array[this.start + index];
/*     */       
/* 578 */       this.array[this.start + index] = ((Character)Preconditions.checkNotNull(element)).charValue();
/* 579 */       return Character.valueOf(oldValue);
/*     */     }
/*     */ 
/*     */     
/*     */     public List<Character> subList(int fromIndex, int toIndex) {
/* 584 */       int size = size();
/* 585 */       Preconditions.checkPositionIndexes(fromIndex, toIndex, size);
/* 586 */       if (fromIndex == toIndex) {
/* 587 */         return Collections.emptyList();
/*     */       }
/* 589 */       return new CharArrayAsList(this.array, this.start + fromIndex, this.start + toIndex);
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean equals(Object object) {
/* 594 */       if (object == this) {
/* 595 */         return true;
/*     */       }
/* 597 */       if (object instanceof CharArrayAsList) {
/* 598 */         CharArrayAsList that = (CharArrayAsList)object;
/* 599 */         int size = size();
/* 600 */         if (that.size() != size) {
/* 601 */           return false;
/*     */         }
/* 603 */         for (int i = 0; i < size; i++) {
/* 604 */           if (this.array[this.start + i] != that.array[that.start + i]) {
/* 605 */             return false;
/*     */           }
/*     */         } 
/* 608 */         return true;
/*     */       } 
/* 610 */       return super.equals(object);
/*     */     }
/*     */ 
/*     */     
/*     */     public int hashCode() {
/* 615 */       int result = 1;
/* 616 */       for (int i = this.start; i < this.end; i++) {
/* 617 */         result = 31 * result + Chars.hashCode(this.array[i]);
/*     */       }
/* 619 */       return result;
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString() {
/* 624 */       StringBuilder builder = new StringBuilder(size() * 3);
/* 625 */       builder.append('[').append(this.array[this.start]);
/* 626 */       for (int i = this.start + 1; i < this.end; i++) {
/* 627 */         builder.append(", ").append(this.array[i]);
/*     */       }
/* 629 */       return builder.append(']').toString();
/*     */     }
/*     */     
/*     */     char[] toCharArray() {
/* 633 */       return Arrays.copyOfRange(this.array, this.start, this.end);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\google\common\primitives\Chars.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */