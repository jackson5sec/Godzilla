/*    */ package com.google.common.base;
/*    */ 
/*    */ import com.google.common.annotations.GwtCompatible;
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
/*    */ @GwtCompatible(serializable = true)
/*    */ final class PairwiseEquivalence<T>
/*    */   extends Equivalence<Iterable<T>>
/*    */   implements Serializable
/*    */ {
/*    */   final Equivalence<? super T> elementEquivalence;
/*    */   private static final long serialVersionUID = 1L;
/*    */   
/*    */   PairwiseEquivalence(Equivalence<? super T> elementEquivalence) {
/* 28 */     this.elementEquivalence = Preconditions.<Equivalence<? super T>>checkNotNull(elementEquivalence);
/*    */   }
/*    */ 
/*    */   
/*    */   protected boolean doEquivalent(Iterable<T> iterableA, Iterable<T> iterableB) {
/* 33 */     Iterator<T> iteratorA = iterableA.iterator();
/* 34 */     Iterator<T> iteratorB = iterableB.iterator();
/*    */     
/* 36 */     while (iteratorA.hasNext() && iteratorB.hasNext()) {
/* 37 */       if (!this.elementEquivalence.equivalent(iteratorA.next(), iteratorB.next())) {
/* 38 */         return false;
/*    */       }
/*    */     } 
/*    */     
/* 42 */     return (!iteratorA.hasNext() && !iteratorB.hasNext());
/*    */   }
/*    */ 
/*    */   
/*    */   protected int doHash(Iterable<T> iterable) {
/* 47 */     int hash = 78721;
/* 48 */     for (T element : iterable) {
/* 49 */       hash = hash * 24943 + this.elementEquivalence.hash(element);
/*    */     }
/* 51 */     return hash;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean equals(Object object) {
/* 56 */     if (object instanceof PairwiseEquivalence) {
/* 57 */       PairwiseEquivalence<?> that = (PairwiseEquivalence)object;
/* 58 */       return this.elementEquivalence.equals(that.elementEquivalence);
/*    */     } 
/*    */     
/* 61 */     return false;
/*    */   }
/*    */ 
/*    */   
/*    */   public int hashCode() {
/* 66 */     return this.elementEquivalence.hashCode() ^ 0x46A3EB07;
/*    */   }
/*    */ 
/*    */   
/*    */   public String toString() {
/* 71 */     return this.elementEquivalence + ".pairwise()";
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\google\common\base\PairwiseEquivalence.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */