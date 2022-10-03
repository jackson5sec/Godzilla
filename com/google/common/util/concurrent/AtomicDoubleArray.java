/*     */ package com.google.common.util.concurrent;
/*     */ 
/*     */ import com.google.common.annotations.GwtIncompatible;
/*     */ import com.google.common.primitives.ImmutableLongArray;
/*     */ import com.google.errorprone.annotations.CanIgnoreReturnValue;
/*     */ import java.io.IOException;
/*     */ import java.io.ObjectInputStream;
/*     */ import java.io.ObjectOutputStream;
/*     */ import java.io.Serializable;
/*     */ import java.util.concurrent.atomic.AtomicLongArray;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @GwtIncompatible
/*     */ public class AtomicDoubleArray
/*     */   implements Serializable
/*     */ {
/*     */   private static final long serialVersionUID = 0L;
/*     */   private transient AtomicLongArray longs;
/*     */   
/*     */   public AtomicDoubleArray(int length) {
/*  60 */     this.longs = new AtomicLongArray(length);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public AtomicDoubleArray(double[] array) {
/*  71 */     int len = array.length;
/*  72 */     long[] longArray = new long[len];
/*  73 */     for (int i = 0; i < len; i++) {
/*  74 */       longArray[i] = Double.doubleToRawLongBits(array[i]);
/*     */     }
/*  76 */     this.longs = new AtomicLongArray(longArray);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final int length() {
/*  85 */     return this.longs.length();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final double get(int i) {
/*  95 */     return Double.longBitsToDouble(this.longs.get(i));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final void set(int i, double newValue) {
/* 105 */     long next = Double.doubleToRawLongBits(newValue);
/* 106 */     this.longs.set(i, next);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final void lazySet(int i, double newValue) {
/* 116 */     long next = Double.doubleToRawLongBits(newValue);
/* 117 */     this.longs.lazySet(i, next);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final double getAndSet(int i, double newValue) {
/* 128 */     long next = Double.doubleToRawLongBits(newValue);
/* 129 */     return Double.longBitsToDouble(this.longs.getAndSet(i, next));
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
/*     */   public final boolean compareAndSet(int i, double expect, double update) {
/* 143 */     return this.longs.compareAndSet(i, Double.doubleToRawLongBits(expect), Double.doubleToRawLongBits(update));
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
/*     */   public final boolean weakCompareAndSet(int i, double expect, double update) {
/* 161 */     return this.longs.weakCompareAndSet(i, Double.doubleToRawLongBits(expect), Double.doubleToRawLongBits(update));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @CanIgnoreReturnValue
/*     */   public final double getAndAdd(int i, double delta) {
/*     */     while (true) {
/* 174 */       long current = this.longs.get(i);
/* 175 */       double currentVal = Double.longBitsToDouble(current);
/* 176 */       double nextVal = currentVal + delta;
/* 177 */       long next = Double.doubleToRawLongBits(nextVal);
/* 178 */       if (this.longs.compareAndSet(i, current, next)) {
/* 179 */         return currentVal;
/*     */       }
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
/*     */   @CanIgnoreReturnValue
/*     */   public double addAndGet(int i, double delta) {
/*     */     while (true) {
/* 194 */       long current = this.longs.get(i);
/* 195 */       double currentVal = Double.longBitsToDouble(current);
/* 196 */       double nextVal = currentVal + delta;
/* 197 */       long next = Double.doubleToRawLongBits(nextVal);
/* 198 */       if (this.longs.compareAndSet(i, current, next)) {
/* 199 */         return nextVal;
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String toString() {
/* 211 */     int iMax = length() - 1;
/* 212 */     if (iMax == -1) {
/* 213 */       return "[]";
/*     */     }
/*     */ 
/*     */     
/* 217 */     StringBuilder b = new StringBuilder(19 * (iMax + 1));
/* 218 */     b.append('[');
/* 219 */     for (int i = 0;; i++) {
/* 220 */       b.append(Double.longBitsToDouble(this.longs.get(i)));
/* 221 */       if (i == iMax) {
/* 222 */         return b.append(']').toString();
/*     */       }
/* 224 */       b.append(',').append(' ');
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void writeObject(ObjectOutputStream s) throws IOException {
/* 235 */     s.defaultWriteObject();
/*     */ 
/*     */     
/* 238 */     int length = length();
/* 239 */     s.writeInt(length);
/*     */ 
/*     */     
/* 242 */     for (int i = 0; i < length; i++) {
/* 243 */       s.writeDouble(get(i));
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private void readObject(ObjectInputStream s) throws IOException, ClassNotFoundException {
/* 250 */     s.defaultReadObject();
/*     */     
/* 252 */     int length = s.readInt();
/* 253 */     ImmutableLongArray.Builder builder = ImmutableLongArray.builder();
/* 254 */     for (int i = 0; i < length; i++) {
/* 255 */       builder.add(Double.doubleToRawLongBits(s.readDouble()));
/*     */     }
/* 257 */     this.longs = new AtomicLongArray(builder.build().toArray());
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\google\commo\\util\concurrent\AtomicDoubleArray.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */