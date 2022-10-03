/*    */ package com.google.common.collect;
/*    */ 
/*    */ import com.google.common.annotations.GwtIncompatible;
/*    */ import java.util.NavigableSet;
/*    */ import java.util.Set;
/*    */ import java.util.SortedSet;
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
/*    */ @GwtIncompatible
/*    */ final class DescendingImmutableSortedMultiset<E>
/*    */   extends ImmutableSortedMultiset<E>
/*    */ {
/*    */   private final transient ImmutableSortedMultiset<E> forward;
/*    */   
/*    */   DescendingImmutableSortedMultiset(ImmutableSortedMultiset<E> forward) {
/* 31 */     this.forward = forward;
/*    */   }
/*    */ 
/*    */   
/*    */   public int count(Object element) {
/* 36 */     return this.forward.count(element);
/*    */   }
/*    */ 
/*    */   
/*    */   public Multiset.Entry<E> firstEntry() {
/* 41 */     return this.forward.lastEntry();
/*    */   }
/*    */ 
/*    */   
/*    */   public Multiset.Entry<E> lastEntry() {
/* 46 */     return this.forward.firstEntry();
/*    */   }
/*    */ 
/*    */   
/*    */   public int size() {
/* 51 */     return this.forward.size();
/*    */   }
/*    */ 
/*    */   
/*    */   public ImmutableSortedSet<E> elementSet() {
/* 56 */     return this.forward.elementSet().descendingSet();
/*    */   }
/*    */ 
/*    */   
/*    */   Multiset.Entry<E> getEntry(int index) {
/* 61 */     return this.forward.entrySet().asList().reverse().get(index);
/*    */   }
/*    */ 
/*    */   
/*    */   public ImmutableSortedMultiset<E> descendingMultiset() {
/* 66 */     return this.forward;
/*    */   }
/*    */ 
/*    */   
/*    */   public ImmutableSortedMultiset<E> headMultiset(E upperBound, BoundType boundType) {
/* 71 */     return this.forward.tailMultiset(upperBound, boundType).descendingMultiset();
/*    */   }
/*    */ 
/*    */   
/*    */   public ImmutableSortedMultiset<E> tailMultiset(E lowerBound, BoundType boundType) {
/* 76 */     return this.forward.headMultiset(lowerBound, boundType).descendingMultiset();
/*    */   }
/*    */ 
/*    */   
/*    */   boolean isPartialView() {
/* 81 */     return this.forward.isPartialView();
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\google\common\collect\DescendingImmutableSortedMultiset.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */