/*    */ package com.google.common.collect;
/*    */ 
/*    */ import com.google.common.annotations.GwtCompatible;
/*    */ import java.io.Serializable;
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
/*    */ final class NullsLastOrdering<T>
/*    */   extends Ordering<T>
/*    */   implements Serializable
/*    */ {
/*    */   final Ordering<? super T> ordering;
/*    */   private static final long serialVersionUID = 0L;
/*    */   
/*    */   NullsLastOrdering(Ordering<? super T> ordering) {
/* 29 */     this.ordering = ordering;
/*    */   }
/*    */ 
/*    */   
/*    */   public int compare(T left, T right) {
/* 34 */     if (left == right) {
/* 35 */       return 0;
/*    */     }
/* 37 */     if (left == null) {
/* 38 */       return 1;
/*    */     }
/* 40 */     if (right == null) {
/* 41 */       return -1;
/*    */     }
/* 43 */     return this.ordering.compare(left, right);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public <S extends T> Ordering<S> reverse() {
/* 49 */     return this.ordering.<T>reverse().nullsFirst();
/*    */   }
/*    */ 
/*    */   
/*    */   public <S extends T> Ordering<S> nullsFirst() {
/* 54 */     return this.ordering.nullsFirst();
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public <S extends T> Ordering<S> nullsLast() {
/* 60 */     return this;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean equals(Object object) {
/* 65 */     if (object == this) {
/* 66 */       return true;
/*    */     }
/* 68 */     if (object instanceof NullsLastOrdering) {
/* 69 */       NullsLastOrdering<?> that = (NullsLastOrdering)object;
/* 70 */       return this.ordering.equals(that.ordering);
/*    */     } 
/* 72 */     return false;
/*    */   }
/*    */ 
/*    */   
/*    */   public int hashCode() {
/* 77 */     return this.ordering.hashCode() ^ 0xC9177248;
/*    */   }
/*    */ 
/*    */   
/*    */   public String toString() {
/* 82 */     return this.ordering + ".nullsLast()";
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\google\common\collect\NullsLastOrdering.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */