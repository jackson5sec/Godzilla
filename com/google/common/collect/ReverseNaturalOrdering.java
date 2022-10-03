/*    */ package com.google.common.collect;
/*    */ 
/*    */ import com.google.common.annotations.GwtCompatible;
/*    */ import com.google.common.base.Preconditions;
/*    */ import java.io.Serializable;
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
/*    */ 
/*    */ 
/*    */ @GwtCompatible(serializable = true)
/*    */ final class ReverseNaturalOrdering
/*    */   extends Ordering<Comparable>
/*    */   implements Serializable
/*    */ {
/* 29 */   static final ReverseNaturalOrdering INSTANCE = new ReverseNaturalOrdering();
/*    */   private static final long serialVersionUID = 0L;
/*    */   
/*    */   public int compare(Comparable left, Comparable<Comparable> right) {
/* 33 */     Preconditions.checkNotNull(left);
/* 34 */     if (left == right) {
/* 35 */       return 0;
/*    */     }
/*    */     
/* 38 */     return right.compareTo(left);
/*    */   }
/*    */ 
/*    */   
/*    */   public <S extends Comparable> Ordering<S> reverse() {
/* 43 */     return Ordering.natural();
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public <E extends Comparable> E min(E a, E b) {
/* 50 */     return (E)NaturalOrdering.INSTANCE.max(a, b);
/*    */   }
/*    */ 
/*    */   
/*    */   public <E extends Comparable> E min(E a, E b, E c, E... rest) {
/* 55 */     return (E)NaturalOrdering.INSTANCE.max(a, b, c, (Object[])rest);
/*    */   }
/*    */ 
/*    */   
/*    */   public <E extends Comparable> E min(Iterator<E> iterator) {
/* 60 */     return (E)NaturalOrdering.INSTANCE.max(iterator);
/*    */   }
/*    */ 
/*    */   
/*    */   public <E extends Comparable> E min(Iterable<E> iterable) {
/* 65 */     return (E)NaturalOrdering.INSTANCE.max(iterable);
/*    */   }
/*    */ 
/*    */   
/*    */   public <E extends Comparable> E max(E a, E b) {
/* 70 */     return (E)NaturalOrdering.INSTANCE.min(a, b);
/*    */   }
/*    */ 
/*    */   
/*    */   public <E extends Comparable> E max(E a, E b, E c, E... rest) {
/* 75 */     return (E)NaturalOrdering.INSTANCE.min(a, b, c, (Object[])rest);
/*    */   }
/*    */ 
/*    */   
/*    */   public <E extends Comparable> E max(Iterator<E> iterator) {
/* 80 */     return (E)NaturalOrdering.INSTANCE.min(iterator);
/*    */   }
/*    */ 
/*    */   
/*    */   public <E extends Comparable> E max(Iterable<E> iterable) {
/* 85 */     return (E)NaturalOrdering.INSTANCE.min(iterable);
/*    */   }
/*    */ 
/*    */   
/*    */   private Object readResolve() {
/* 90 */     return INSTANCE;
/*    */   }
/*    */ 
/*    */   
/*    */   public String toString() {
/* 95 */     return "Ordering.natural().reverse()";
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\google\common\collect\ReverseNaturalOrdering.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */