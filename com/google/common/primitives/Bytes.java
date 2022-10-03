/*     */ package com.google.common.primitives;
/*     */ 
/*     */ import com.google.common.annotations.GwtCompatible;
/*     */ import com.google.common.base.Preconditions;
/*     */ import java.io.Serializable;
/*     */ import java.util.AbstractList;
/*     */ import java.util.Arrays;
/*     */ import java.util.Collection;
/*     */ import java.util.Collections;
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
/*     */ @GwtCompatible
/*     */ public final class Bytes
/*     */ {
/*     */   public static int hashCode(byte value) {
/*  60 */     return value;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static boolean contains(byte[] array, byte target) {
/*  71 */     for (byte value : array) {
/*  72 */       if (value == target) {
/*  73 */         return true;
/*     */       }
/*     */     } 
/*  76 */     return false;
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
/*     */   public static int indexOf(byte[] array, byte target) {
/*  88 */     return indexOf(array, target, 0, array.length);
/*     */   }
/*     */ 
/*     */   
/*     */   private static int indexOf(byte[] array, byte target, int start, int end) {
/*  93 */     for (int i = start; i < end; i++) {
/*  94 */       if (array[i] == target) {
/*  95 */         return i;
/*     */       }
/*     */     } 
/*  98 */     return -1;
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
/*     */   public static int indexOf(byte[] array, byte[] target) {
/* 112 */     Preconditions.checkNotNull(array, "array");
/* 113 */     Preconditions.checkNotNull(target, "target");
/* 114 */     if (target.length == 0) {
/* 115 */       return 0;
/*     */     }
/*     */ 
/*     */     
/* 119 */     for (int i = 0; i < array.length - target.length + 1; i++) {
/* 120 */       int j = 0; while (true) { if (j < target.length) {
/* 121 */           if (array[i + j] != target[j])
/*     */             break;  j++;
/*     */           continue;
/*     */         } 
/* 125 */         return i; }
/*     */     
/* 127 */     }  return -1;
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
/*     */   public static int lastIndexOf(byte[] array, byte target) {
/* 139 */     return lastIndexOf(array, target, 0, array.length);
/*     */   }
/*     */ 
/*     */   
/*     */   private static int lastIndexOf(byte[] array, byte target, int start, int end) {
/* 144 */     for (int i = end - 1; i >= start; i--) {
/* 145 */       if (array[i] == target) {
/* 146 */         return i;
/*     */       }
/*     */     } 
/* 149 */     return -1;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static byte[] concat(byte[]... arrays) {
/* 160 */     int length = 0;
/* 161 */     for (byte[] array : arrays) {
/* 162 */       length += array.length;
/*     */     }
/* 164 */     byte[] result = new byte[length];
/* 165 */     int pos = 0;
/* 166 */     for (byte[] array : arrays) {
/* 167 */       System.arraycopy(array, 0, result, pos, array.length);
/* 168 */       pos += array.length;
/*     */     } 
/* 170 */     return result;
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
/*     */   public static byte[] ensureCapacity(byte[] array, int minLength, int padding) {
/* 187 */     Preconditions.checkArgument((minLength >= 0), "Invalid minLength: %s", minLength);
/* 188 */     Preconditions.checkArgument((padding >= 0), "Invalid padding: %s", padding);
/* 189 */     return (array.length < minLength) ? Arrays.copyOf(array, minLength + padding) : array;
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
/*     */   public static byte[] toArray(Collection<? extends Number> collection) {
/* 206 */     if (collection instanceof ByteArrayAsList) {
/* 207 */       return ((ByteArrayAsList)collection).toByteArray();
/*     */     }
/*     */     
/* 210 */     Object[] boxedArray = collection.toArray();
/* 211 */     int len = boxedArray.length;
/* 212 */     byte[] array = new byte[len];
/* 213 */     for (int i = 0; i < len; i++)
/*     */     {
/* 215 */       array[i] = ((Number)Preconditions.checkNotNull(boxedArray[i])).byteValue();
/*     */     }
/* 217 */     return array;
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
/*     */   public static List<Byte> asList(byte... backingArray) {
/* 233 */     if (backingArray.length == 0) {
/* 234 */       return Collections.emptyList();
/*     */     }
/* 236 */     return new ByteArrayAsList(backingArray);
/*     */   }
/*     */   
/*     */   @GwtCompatible
/*     */   private static class ByteArrayAsList extends AbstractList<Byte> implements RandomAccess, Serializable {
/*     */     final byte[] array;
/*     */     final int start;
/*     */     final int end;
/*     */     private static final long serialVersionUID = 0L;
/*     */     
/*     */     ByteArrayAsList(byte[] array) {
/* 247 */       this(array, 0, array.length);
/*     */     }
/*     */     
/*     */     ByteArrayAsList(byte[] array, int start, int end) {
/* 251 */       this.array = array;
/* 252 */       this.start = start;
/* 253 */       this.end = end;
/*     */     }
/*     */ 
/*     */     
/*     */     public int size() {
/* 258 */       return this.end - this.start;
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean isEmpty() {
/* 263 */       return false;
/*     */     }
/*     */ 
/*     */     
/*     */     public Byte get(int index) {
/* 268 */       Preconditions.checkElementIndex(index, size());
/* 269 */       return Byte.valueOf(this.array[this.start + index]);
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public boolean contains(Object target) {
/* 275 */       return (target instanceof Byte && Bytes.indexOf(this.array, ((Byte)target).byteValue(), this.start, this.end) != -1);
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public int indexOf(Object target) {
/* 281 */       if (target instanceof Byte) {
/* 282 */         int i = Bytes.indexOf(this.array, ((Byte)target).byteValue(), this.start, this.end);
/* 283 */         if (i >= 0) {
/* 284 */           return i - this.start;
/*     */         }
/*     */       } 
/* 287 */       return -1;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public int lastIndexOf(Object target) {
/* 293 */       if (target instanceof Byte) {
/* 294 */         int i = Bytes.lastIndexOf(this.array, ((Byte)target).byteValue(), this.start, this.end);
/* 295 */         if (i >= 0) {
/* 296 */           return i - this.start;
/*     */         }
/*     */       } 
/* 299 */       return -1;
/*     */     }
/*     */ 
/*     */     
/*     */     public Byte set(int index, Byte element) {
/* 304 */       Preconditions.checkElementIndex(index, size());
/* 305 */       byte oldValue = this.array[this.start + index];
/*     */       
/* 307 */       this.array[this.start + index] = ((Byte)Preconditions.checkNotNull(element)).byteValue();
/* 308 */       return Byte.valueOf(oldValue);
/*     */     }
/*     */ 
/*     */     
/*     */     public List<Byte> subList(int fromIndex, int toIndex) {
/* 313 */       int size = size();
/* 314 */       Preconditions.checkPositionIndexes(fromIndex, toIndex, size);
/* 315 */       if (fromIndex == toIndex) {
/* 316 */         return Collections.emptyList();
/*     */       }
/* 318 */       return new ByteArrayAsList(this.array, this.start + fromIndex, this.start + toIndex);
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean equals(Object object) {
/* 323 */       if (object == this) {
/* 324 */         return true;
/*     */       }
/* 326 */       if (object instanceof ByteArrayAsList) {
/* 327 */         ByteArrayAsList that = (ByteArrayAsList)object;
/* 328 */         int size = size();
/* 329 */         if (that.size() != size) {
/* 330 */           return false;
/*     */         }
/* 332 */         for (int i = 0; i < size; i++) {
/* 333 */           if (this.array[this.start + i] != that.array[that.start + i]) {
/* 334 */             return false;
/*     */           }
/*     */         } 
/* 337 */         return true;
/*     */       } 
/* 339 */       return super.equals(object);
/*     */     }
/*     */ 
/*     */     
/*     */     public int hashCode() {
/* 344 */       int result = 1;
/* 345 */       for (int i = this.start; i < this.end; i++) {
/* 346 */         result = 31 * result + Bytes.hashCode(this.array[i]);
/*     */       }
/* 348 */       return result;
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString() {
/* 353 */       StringBuilder builder = new StringBuilder(size() * 5);
/* 354 */       builder.append('[').append(this.array[this.start]);
/* 355 */       for (int i = this.start + 1; i < this.end; i++) {
/* 356 */         builder.append(", ").append(this.array[i]);
/*     */       }
/* 358 */       return builder.append(']').toString();
/*     */     }
/*     */     
/*     */     byte[] toByteArray() {
/* 362 */       return Arrays.copyOfRange(this.array, this.start, this.end);
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
/*     */   public static void reverse(byte[] array) {
/* 375 */     Preconditions.checkNotNull(array);
/* 376 */     reverse(array, 0, array.length);
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
/*     */   public static void reverse(byte[] array, int fromIndex, int toIndex) {
/* 390 */     Preconditions.checkNotNull(array);
/* 391 */     Preconditions.checkPositionIndexes(fromIndex, toIndex, array.length);
/* 392 */     for (int i = fromIndex, j = toIndex - 1; i < j; i++, j--) {
/* 393 */       byte tmp = array[i];
/* 394 */       array[i] = array[j];
/* 395 */       array[j] = tmp;
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\google\common\primitives\Bytes.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */