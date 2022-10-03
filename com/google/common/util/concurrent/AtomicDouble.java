/*     */ package com.google.common.util.concurrent;
/*     */ 
/*     */ import com.google.common.annotations.GwtIncompatible;
/*     */ import com.google.errorprone.annotations.CanIgnoreReturnValue;
/*     */ import com.google.j2objc.annotations.ReflectionSupport;
/*     */ import java.io.IOException;
/*     */ import java.io.ObjectInputStream;
/*     */ import java.io.ObjectOutputStream;
/*     */ import java.io.Serializable;
/*     */ import java.util.concurrent.atomic.AtomicLongFieldUpdater;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
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
/*     */ @ReflectionSupport(ReflectionSupport.Level.FULL)
/*     */ public class AtomicDouble
/*     */   extends Number
/*     */   implements Serializable
/*     */ {
/*     */   private static final long serialVersionUID = 0L;
/*     */   private volatile transient long value;
/*  64 */   private static final AtomicLongFieldUpdater<AtomicDouble> updater = AtomicLongFieldUpdater.newUpdater(AtomicDouble.class, "value");
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public AtomicDouble(double initialValue) {
/*  72 */     this.value = Double.doubleToRawLongBits(initialValue);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public AtomicDouble() {}
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final double get() {
/*  86 */     return Double.longBitsToDouble(this.value);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final void set(double newValue) {
/*  95 */     long next = Double.doubleToRawLongBits(newValue);
/*  96 */     this.value = next;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final void lazySet(double newValue) {
/* 105 */     long next = Double.doubleToRawLongBits(newValue);
/* 106 */     updater.lazySet(this, next);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final double getAndSet(double newValue) {
/* 116 */     long next = Double.doubleToRawLongBits(newValue);
/* 117 */     return Double.longBitsToDouble(updater.getAndSet(this, next));
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
/*     */   public final boolean compareAndSet(double expect, double update) {
/* 130 */     return updater.compareAndSet(this, Double.doubleToRawLongBits(expect), Double.doubleToRawLongBits(update));
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
/*     */   public final boolean weakCompareAndSet(double expect, double update) {
/* 147 */     return updater.weakCompareAndSet(this, 
/* 148 */         Double.doubleToRawLongBits(expect), Double.doubleToRawLongBits(update));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @CanIgnoreReturnValue
/*     */   public final double getAndAdd(double delta) {
/*     */     while (true) {
/* 160 */       long current = this.value;
/* 161 */       double currentVal = Double.longBitsToDouble(current);
/* 162 */       double nextVal = currentVal + delta;
/* 163 */       long next = Double.doubleToRawLongBits(nextVal);
/* 164 */       if (updater.compareAndSet(this, current, next)) {
/* 165 */         return currentVal;
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
/*     */   @CanIgnoreReturnValue
/*     */   public final double addAndGet(double delta) {
/*     */     while (true) {
/* 179 */       long current = this.value;
/* 180 */       double currentVal = Double.longBitsToDouble(current);
/* 181 */       double nextVal = currentVal + delta;
/* 182 */       long next = Double.doubleToRawLongBits(nextVal);
/* 183 */       if (updater.compareAndSet(this, current, next)) {
/* 184 */         return nextVal;
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
/* 196 */     return Double.toString(get());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int intValue() {
/* 205 */     return (int)get();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public long longValue() {
/* 214 */     return (long)get();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public float floatValue() {
/* 223 */     return (float)get();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public double doubleValue() {
/* 229 */     return get();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void writeObject(ObjectOutputStream s) throws IOException {
/* 238 */     s.defaultWriteObject();
/*     */     
/* 240 */     s.writeDouble(get());
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private void readObject(ObjectInputStream s) throws IOException, ClassNotFoundException {
/* 246 */     s.defaultReadObject();
/*     */     
/* 248 */     set(s.readDouble());
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\google\commo\\util\concurrent\AtomicDouble.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */