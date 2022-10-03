/*    */ package com.google.common.collect;
/*    */ 
/*    */ import com.google.common.annotations.GwtCompatible;
/*    */ import java.io.Serializable;
/*    */ import java.util.Comparator;
/*    */ import java.util.Iterator;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ @GwtCompatible(serializable = true)
/*    */ final class LexicographicalOrdering<T>
/*    */   extends Ordering<Iterable<T>>
/*    */   implements Serializable
/*    */ {
/*    */   final Comparator<? super T> elementOrder;
/*    */   private static final long serialVersionUID = 0L;
/*    */   
/*    */   LexicographicalOrdering(Comparator<? super T> elementOrder) {
/* 31 */     this.elementOrder = elementOrder;
/*    */   }
/*    */ 
/*    */   
/*    */   public int compare(Iterable<T> leftIterable, Iterable<T> rightIterable) {
/* 36 */     Iterator<T> left = leftIterable.iterator();
/* 37 */     Iterator<T> right = rightIterable.iterator();
/* 38 */     while (left.hasNext()) {
/* 39 */       if (!right.hasNext()) {
/* 40 */         return 1;
/*    */       }
/* 42 */       int result = this.elementOrder.compare(left.next(), right.next());
/* 43 */       if (result != 0) {
/* 44 */         return result;
/*    */       }
/*    */     } 
/* 47 */     if (right.hasNext()) {
/* 48 */       return -1;
/*    */     }
/* 50 */     return 0;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean equals(Object object) {
/* 55 */     if (object == this) {
/* 56 */       return true;
/*    */     }
/* 58 */     if (object instanceof LexicographicalOrdering) {
/* 59 */       LexicographicalOrdering<?> that = (LexicographicalOrdering)object;
/* 60 */       return this.elementOrder.equals(that.elementOrder);
/*    */     } 
/* 62 */     return false;
/*    */   }
/*    */ 
/*    */   
/*    */   public int hashCode() {
/* 67 */     return this.elementOrder.hashCode() ^ 0x7BB78CF5;
/*    */   }
/*    */ 
/*    */   
/*    */   public String toString() {
/* 72 */     return this.elementOrder + ".lexicographical()";
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\google\common\collect\LexicographicalOrdering.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */