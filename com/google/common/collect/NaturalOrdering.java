/*    */ package com.google.common.collect;
/*    */ 
/*    */ import com.google.common.annotations.GwtCompatible;
/*    */ import com.google.common.base.Preconditions;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ @GwtCompatible(serializable = true)
/*    */ final class NaturalOrdering
/*    */   extends Ordering<Comparable>
/*    */   implements Serializable
/*    */ {
/* 29 */   static final NaturalOrdering INSTANCE = new NaturalOrdering();
/*    */   
/*    */   private transient Ordering<Comparable> nullsFirst;
/*    */   private transient Ordering<Comparable> nullsLast;
/*    */   private static final long serialVersionUID = 0L;
/*    */   
/*    */   public int compare(Comparable<Comparable> left, Comparable right) {
/* 36 */     Preconditions.checkNotNull(left);
/* 37 */     Preconditions.checkNotNull(right);
/* 38 */     return left.compareTo(right);
/*    */   }
/*    */ 
/*    */   
/*    */   public <S extends Comparable> Ordering<S> nullsFirst() {
/* 43 */     Ordering<Comparable> result = this.nullsFirst;
/* 44 */     if (result == null) {
/* 45 */       result = this.nullsFirst = super.<Comparable>nullsFirst();
/*    */     }
/* 47 */     return (Ordering)result;
/*    */   }
/*    */ 
/*    */   
/*    */   public <S extends Comparable> Ordering<S> nullsLast() {
/* 52 */     Ordering<Comparable> result = this.nullsLast;
/* 53 */     if (result == null) {
/* 54 */       result = this.nullsLast = super.<Comparable>nullsLast();
/*    */     }
/* 56 */     return (Ordering)result;
/*    */   }
/*    */ 
/*    */   
/*    */   public <S extends Comparable> Ordering<S> reverse() {
/* 61 */     return ReverseNaturalOrdering.INSTANCE;
/*    */   }
/*    */ 
/*    */   
/*    */   private Object readResolve() {
/* 66 */     return INSTANCE;
/*    */   }
/*    */ 
/*    */   
/*    */   public String toString() {
/* 71 */     return "Ordering.natural()";
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\google\common\collect\NaturalOrdering.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */