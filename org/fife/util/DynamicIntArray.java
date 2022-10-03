/*     */ package org.fife.util;
/*     */ 
/*     */ import java.io.Serializable;
/*     */ import java.util.Arrays;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class DynamicIntArray
/*     */   implements Serializable
/*     */ {
/*     */   private int[] data;
/*     */   private int size;
/*     */   
/*     */   public DynamicIntArray() {
/*  42 */     this(10);
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
/*     */   public DynamicIntArray(int initialCapacity) {
/*  54 */     if (initialCapacity < 0) {
/*  55 */       throw new IllegalArgumentException("Illegal initialCapacity: " + initialCapacity);
/*     */     }
/*     */     
/*  58 */     this.data = new int[initialCapacity];
/*  59 */     this.size = 0;
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
/*     */   public DynamicIntArray(int[] intArray) {
/*  73 */     this.size = intArray.length;
/*  74 */     int capacity = (int)Math.min(this.size * 110L / 100L, 2147483647L);
/*  75 */     this.data = new int[capacity];
/*  76 */     System.arraycopy(intArray, 0, this.data, 0, this.size);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void add(int value) {
/*  86 */     ensureCapacity(this.size + 1);
/*  87 */     this.data[this.size++] = value;
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
/*     */   public void add(int index, int[] intArray) {
/* 106 */     if (index > this.size) {
/* 107 */       throwException2(index);
/*     */     }
/* 109 */     int addCount = intArray.length;
/* 110 */     ensureCapacity(this.size + addCount);
/* 111 */     int moveCount = this.size - index;
/* 112 */     if (moveCount > 0) {
/* 113 */       System.arraycopy(this.data, index, this.data, index + addCount, moveCount);
/*     */     }
/* 115 */     System.arraycopy(intArray, 0, this.data, index, addCount);
/* 116 */     this.size += addCount;
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
/*     */   public void add(int index, int value) {
/* 133 */     if (index > this.size) {
/* 134 */       throwException2(index);
/*     */     }
/* 136 */     ensureCapacity(this.size + 1);
/* 137 */     System.arraycopy(this.data, index, this.data, index + 1, this.size - index);
/* 138 */     this.data[index] = value;
/* 139 */     this.size++;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void clear() {
/* 148 */     this.size = 0;
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
/*     */   public boolean contains(int integer) {
/* 160 */     for (int i = 0; i < this.size; i++) {
/* 161 */       if (this.data[i] == integer) {
/* 162 */         return true;
/*     */       }
/*     */     } 
/* 165 */     return false;
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
/*     */   public void decrement(int from, int to) {
/* 177 */     for (int i = from; i < to; i++) {
/* 178 */       this.data[i] = this.data[i] - 1;
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
/*     */   private void ensureCapacity(int minCapacity) {
/* 191 */     int oldCapacity = this.data.length;
/* 192 */     if (minCapacity > oldCapacity) {
/* 193 */       int[] oldData = this.data;
/*     */ 
/*     */       
/* 196 */       int newCapacity = oldCapacity * 3 / 2 + 1;
/* 197 */       if (newCapacity < minCapacity) {
/* 198 */         newCapacity = minCapacity;
/*     */       }
/* 200 */       this.data = new int[newCapacity];
/* 201 */       System.arraycopy(oldData, 0, this.data, 0, this.size);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void fill(int value) {
/* 212 */     Arrays.fill(this.data, value);
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
/*     */   public int get(int index) {
/* 227 */     if (index >= this.size) {
/* 228 */       throwException(index);
/*     */     }
/* 230 */     return this.data[index];
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
/*     */   public int getUnsafe(int index) {
/* 244 */     return this.data[index];
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getSize() {
/* 254 */     return this.size;
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
/*     */   public void increment(int from, int to) {
/* 266 */     for (int i = from; i < to; i++) {
/* 267 */       this.data[i] = this.data[i] + 1;
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public void insertRange(int offs, int count, int value) {
/* 273 */     if (offs > this.size) {
/* 274 */       throwException2(offs);
/*     */     }
/* 276 */     ensureCapacity(this.size + count);
/* 277 */     System.arraycopy(this.data, offs, this.data, offs + count, this.size - offs);
/* 278 */     if (value != 0) {
/* 279 */       Arrays.fill(this.data, offs, offs + count, value);
/*     */     }
/* 281 */     this.size += count;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isEmpty() {
/* 291 */     return (this.size == 0);
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
/*     */   public void remove(int index) {
/* 304 */     if (index >= this.size) {
/* 305 */       throwException(index);
/*     */     }
/* 307 */     int toMove = this.size - index - 1;
/* 308 */     if (toMove > 0) {
/* 309 */       System.arraycopy(this.data, index + 1, this.data, index, toMove);
/*     */     }
/* 311 */     this.size--;
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
/*     */   public void removeRange(int fromIndex, int toIndex) {
/* 326 */     if (fromIndex >= this.size || toIndex > this.size) {
/* 327 */       throwException3(fromIndex, toIndex);
/*     */     }
/* 329 */     int moveCount = this.size - toIndex;
/* 330 */     System.arraycopy(this.data, toIndex, this.data, fromIndex, moveCount);
/* 331 */     this.size -= toIndex - fromIndex;
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
/*     */   public void set(int index, int value) {
/* 346 */     if (index >= this.size) {
/* 347 */       throwException(index);
/*     */     }
/* 349 */     this.data[index] = value;
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
/*     */   public void setUnsafe(int index, int value) {
/* 363 */     this.data[index] = value;
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
/*     */   private void throwException(int index) {
/* 381 */     throw new IndexOutOfBoundsException("Index " + index + " not in valid range [0-" + (this.size - 1) + "]");
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
/*     */   private void throwException2(int index) {
/* 399 */     throw new IndexOutOfBoundsException("Index " + index + ", not in range [0-" + this.size + "]");
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
/*     */   private void throwException3(int fromIndex, int toIndex) {
/* 418 */     throw new IndexOutOfBoundsException("Index range [" + fromIndex + ", " + toIndex + "] not in valid range [0-" + (this.size - 1) + "]");
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\fif\\util\DynamicIntArray.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */