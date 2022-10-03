/*     */ package com.google.common.hash;
/*     */ 
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
/*     */ final class LongAdder
/*     */   extends Striped64
/*     */   implements Serializable, LongAddable
/*     */ {
/*     */   private static final long serialVersionUID = 7249069246863182397L;
/*     */   
/*     */   final long fn(long v, long x) {
/*  47 */     return v + x;
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
/*  66 */     boolean uncontended = true; Striped64.Cell[] as; long b, v; int[] hc; Striped64.Cell a; int n;
/*  67 */     if (((as = this.cells) != null || !casBase(b = this.base, b + x)) && ((hc = threadHashCode.get()) == null || as == null || (n = as.length) < 1 || (a = as[n - 1 & hc[0]]) == null || 
/*     */ 
/*     */ 
/*     */       
/*  71 */       !(uncontended = a.cas(v = a.value, v + x)))) retryUpdate(x, hc, uncontended);
/*     */   
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void increment() {
/*  78 */     add(1L);
/*     */   }
/*     */ 
/*     */   
/*     */   public void decrement() {
/*  83 */     add(-1L);
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
/*  95 */     long sum = this.base;
/*  96 */     Striped64.Cell[] as = this.cells;
/*  97 */     if (as != null) {
/*  98 */       int n = as.length;
/*  99 */       for (int i = 0; i < n; i++) {
/* 100 */         Striped64.Cell a = as[i];
/* 101 */         if (a != null) sum += a.value; 
/*     */       } 
/*     */     } 
/* 104 */     return sum;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void reset() {
/* 114 */     internalReset(0L);
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
/* 126 */     long sum = this.base;
/* 127 */     Striped64.Cell[] as = this.cells;
/* 128 */     this.base = 0L;
/* 129 */     if (as != null) {
/* 130 */       int n = as.length;
/* 131 */       for (int i = 0; i < n; i++) {
/* 132 */         Striped64.Cell a = as[i];
/* 133 */         if (a != null) {
/* 134 */           sum += a.value;
/* 135 */           a.value = 0L;
/*     */         } 
/*     */       } 
/*     */     } 
/* 139 */     return sum;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String toString() {
/* 149 */     return Long.toString(sum());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public long longValue() {
/* 159 */     return sum();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public int intValue() {
/* 165 */     return (int)sum();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public float floatValue() {
/* 171 */     return (float)sum();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public double doubleValue() {
/* 177 */     return sum();
/*     */   }
/*     */   
/*     */   private void writeObject(ObjectOutputStream s) throws IOException {
/* 181 */     s.defaultWriteObject();
/* 182 */     s.writeLong(sum());
/*     */   }
/*     */   
/*     */   private void readObject(ObjectInputStream s) throws IOException, ClassNotFoundException {
/* 186 */     s.defaultReadObject();
/* 187 */     this.busy = 0;
/* 188 */     this.cells = null;
/* 189 */     this.base = s.readLong();
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\google\common\hash\LongAdder.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */