/*     */ package com.google.common.collect;
/*     */ 
/*     */ import com.google.common.annotations.GwtCompatible;
/*     */ import com.google.common.base.Preconditions;
/*     */ import java.io.Serializable;
/*     */ import java.util.Iterator;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @GwtCompatible(serializable = true)
/*     */ final class ReverseOrdering<T>
/*     */   extends Ordering<T>
/*     */   implements Serializable
/*     */ {
/*     */   final Ordering<? super T> forwardOrder;
/*     */   private static final long serialVersionUID = 0L;
/*     */   
/*     */   ReverseOrdering(Ordering<? super T> forwardOrder) {
/*  32 */     this.forwardOrder = (Ordering<? super T>)Preconditions.checkNotNull(forwardOrder);
/*     */   }
/*     */ 
/*     */   
/*     */   public int compare(T a, T b) {
/*  37 */     return this.forwardOrder.compare(b, a);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public <S extends T> Ordering<S> reverse() {
/*  43 */     return (Ordering)this.forwardOrder;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public <E extends T> E min(E a, E b) {
/*  50 */     return this.forwardOrder.max(a, b);
/*     */   }
/*     */ 
/*     */   
/*     */   public <E extends T> E min(E a, E b, E c, E... rest) {
/*  55 */     return this.forwardOrder.max(a, b, c, rest);
/*     */   }
/*     */ 
/*     */   
/*     */   public <E extends T> E min(Iterator<E> iterator) {
/*  60 */     return this.forwardOrder.max(iterator);
/*     */   }
/*     */ 
/*     */   
/*     */   public <E extends T> E min(Iterable<E> iterable) {
/*  65 */     return this.forwardOrder.max(iterable);
/*     */   }
/*     */ 
/*     */   
/*     */   public <E extends T> E max(E a, E b) {
/*  70 */     return this.forwardOrder.min(a, b);
/*     */   }
/*     */ 
/*     */   
/*     */   public <E extends T> E max(E a, E b, E c, E... rest) {
/*  75 */     return this.forwardOrder.min(a, b, c, rest);
/*     */   }
/*     */ 
/*     */   
/*     */   public <E extends T> E max(Iterator<E> iterator) {
/*  80 */     return this.forwardOrder.min(iterator);
/*     */   }
/*     */ 
/*     */   
/*     */   public <E extends T> E max(Iterable<E> iterable) {
/*  85 */     return this.forwardOrder.min(iterable);
/*     */   }
/*     */ 
/*     */   
/*     */   public int hashCode() {
/*  90 */     return -this.forwardOrder.hashCode();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean equals(Object object) {
/*  95 */     if (object == this) {
/*  96 */       return true;
/*     */     }
/*  98 */     if (object instanceof ReverseOrdering) {
/*  99 */       ReverseOrdering<?> that = (ReverseOrdering)object;
/* 100 */       return this.forwardOrder.equals(that.forwardOrder);
/*     */     } 
/* 102 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 107 */     return this.forwardOrder + ".reverse()";
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\google\common\collect\ReverseOrdering.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */