/*    */ package com.google.common.collect;
/*    */ 
/*    */ import com.google.common.annotations.GwtCompatible;
/*    */ import com.google.common.base.Preconditions;
/*    */ import java.util.Comparator;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ @GwtCompatible
/*    */ final class SortedIterables
/*    */ {
/*    */   public static boolean hasSameComparator(Comparator<?> comparator, Iterable<?> elements) {
/*    */     Comparator<?> comparator2;
/* 37 */     Preconditions.checkNotNull(comparator);
/* 38 */     Preconditions.checkNotNull(elements);
/*    */     
/* 40 */     if (elements instanceof SortedSet) {
/* 41 */       comparator2 = comparator((SortedSet)elements);
/* 42 */     } else if (elements instanceof SortedIterable) {
/* 43 */       comparator2 = ((SortedIterable)elements).comparator();
/*    */     } else {
/* 45 */       return false;
/*    */     } 
/* 47 */     return comparator.equals(comparator2);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public static <E> Comparator<? super E> comparator(SortedSet<E> sortedSet) {
/* 53 */     Comparator<? super E> result = sortedSet.comparator();
/* 54 */     if (result == null) {
/* 55 */       result = Ordering.natural();
/*    */     }
/* 57 */     return result;
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\google\common\collect\SortedIterables.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */