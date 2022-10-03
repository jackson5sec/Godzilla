/*     */ package com.google.common.cache;
/*     */ 
/*     */ import com.google.common.annotations.GwtCompatible;
/*     */ import java.io.IOException;
/*     */ import java.io.ObjectInputStream;
/*     */ import java.io.ObjectOutputStream;
/*     */ import java.io.Serializable;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
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
/*     */ final class LongAdder
/*     */   extends Striped64
/*     */   implements Serializable, LongAddable
/*     */ {
/*     */   private static final long serialVersionUID = 7249069246863182397L;
/*     */   
/*     */   final long fn(long v, long x) {
/*  49 */     return v + x;
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
/*     */   public void add(long x) {
/*  68 */     boolean uncontended = true; Striped64.Cell[] as; long b, v; int[] hc; Striped64.Cell a; int n;
/*  69 */     if (((as = this.cells) != null || !casBase(b = this.base, b + x)) && ((hc = threadHashCode.get()) == null || as == null || (n = as.length) < 1 || (a = as[n - 1 & hc[0]]) == null || 
/*     */ 
/*     */ 
/*     */       
/*  73 */       !(uncontended = a.cas(v = a.value, v + x)))) retryUpdate(x, hc, uncontended);
/*     */   
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void increment() {
/*  80 */     add(1L);
/*     */   }
/*     */ 
/*     */   
/*     */   public void decrement() {
/*  85 */     add(-1L);
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
/*     */   public long sum() {
/*  97 */     long sum = this.base;
/*  98 */     Striped64.Cell[] as = this.cells;
/*  99 */     if (as != null) {
/* 100 */       int n = as.length;
/* 101 */       for (int i = 0; i < n; i++) {
/* 102 */         Striped64.Cell a = as[i];
/* 103 */         if (a != null) sum += a.value; 
/*     */       } 
/*     */     } 
/* 106 */     return sum;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void reset() {
/* 116 */     internalReset(0L);
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
/*     */   public long sumThenReset() {
/* 128 */     long sum = this.base;
/* 129 */     Striped64.Cell[] as = this.cells;
/* 130 */     this.base = 0L;
/* 131 */     if (as != null) {
/* 132 */       int n = as.length;
/* 133 */       for (int i = 0; i < n; i++) {
/* 134 */         Striped64.Cell a = as[i];
/* 135 */         if (a != null) {
/* 136 */           sum += a.value;
/* 137 */           a.value = 0L;
/*     */         } 
/*     */       } 
/*     */     } 
/* 141 */     return sum;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String toString() {
/* 151 */     return Long.toString(sum());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public long longValue() {
/* 161 */     return sum();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public int intValue() {
/* 167 */     return (int)sum();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public float floatValue() {
/* 173 */     return (float)sum();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public double doubleValue() {
/* 179 */     return sum();
/*     */   }
/*     */   
/*     */   private void writeObject(ObjectOutputStream s) throws IOException {
/* 183 */     s.defaultWriteObject();
/* 184 */     s.writeLong(sum());
/*     */   }
/*     */   
/*     */   private void readObject(ObjectInputStream s) throws IOException, ClassNotFoundException {
/* 188 */     s.defaultReadObject();
/* 189 */     this.busy = 0;
/* 190 */     this.cells = null;
/* 191 */     this.base = s.readLong();
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\google\common\cache\LongAdder.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */