/*    */ package com.google.common.collect;
/*    */ 
/*    */ import com.google.common.annotations.GwtCompatible;
/*    */ import com.google.common.annotations.GwtIncompatible;
/*    */ import java.util.Comparator;
/*    */ import java.util.Spliterator;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ @GwtCompatible(emulated = true)
/*    */ final class ImmutableSortedAsList<E>
/*    */   extends RegularImmutableAsList<E>
/*    */   implements SortedIterable<E>
/*    */ {
/*    */   ImmutableSortedAsList(ImmutableSortedSet<E> backingSet, ImmutableList<E> backingList) {
/* 34 */     super(backingSet, backingList);
/*    */   }
/*    */ 
/*    */   
/*    */   ImmutableSortedSet<E> delegateCollection() {
/* 39 */     return (ImmutableSortedSet<E>)super.delegateCollection();
/*    */   }
/*    */ 
/*    */   
/*    */   public Comparator<? super E> comparator() {
/* 44 */     return delegateCollection().comparator();
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   @GwtIncompatible
/*    */   public int indexOf(Object target) {
/* 53 */     int index = delegateCollection().indexOf(target);
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */     
/* 60 */     return (index >= 0 && get(index).equals(target)) ? index : -1;
/*    */   }
/*    */ 
/*    */   
/*    */   @GwtIncompatible
/*    */   public int lastIndexOf(Object target) {
/* 66 */     return indexOf(target);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public boolean contains(Object target) {
/* 72 */     return (indexOf(target) >= 0);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   @GwtIncompatible
/*    */   ImmutableList<E> subListUnchecked(int fromIndex, int toIndex) {
/* 83 */     ImmutableList<E> parentSubList = super.subListUnchecked(fromIndex, toIndex);
/* 84 */     return (new RegularImmutableSortedSet<>(parentSubList, comparator())).asList();
/*    */   }
/*    */ 
/*    */   
/*    */   public Spliterator<E> spliterator() {
/* 89 */     return CollectSpliterators.indexed(
/* 90 */         size(), 1301, 
/*    */         
/* 92 */         delegateList()::get, 
/* 93 */         comparator());
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\google\common\collect\ImmutableSortedAsList.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */