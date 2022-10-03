/*    */ package com.google.common.collect;
/*    */ 
/*    */ import com.google.common.annotations.GwtCompatible;
/*    */ import com.google.common.base.Objects;
/*    */ import com.google.common.base.Preconditions;
/*    */ import com.google.common.base.Predicate;
/*    */ import com.google.common.base.Predicates;
/*    */ import com.google.j2objc.annotations.Weak;
/*    */ import java.util.AbstractCollection;
/*    */ import java.util.Collection;
/*    */ import java.util.Iterator;
/*    */ import java.util.Map;
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
/*    */ final class FilteredMultimapValues<K, V>
/*    */   extends AbstractCollection<V>
/*    */ {
/*    */   @Weak
/*    */   private final FilteredMultimap<K, V> multimap;
/*    */   
/*    */   FilteredMultimapValues(FilteredMultimap<K, V> multimap) {
/* 40 */     this.multimap = (FilteredMultimap<K, V>)Preconditions.checkNotNull(multimap);
/*    */   }
/*    */ 
/*    */   
/*    */   public Iterator<V> iterator() {
/* 45 */     return Maps.valueIterator(this.multimap.entries().iterator());
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean contains(Object o) {
/* 50 */     return this.multimap.containsValue(o);
/*    */   }
/*    */ 
/*    */   
/*    */   public int size() {
/* 55 */     return this.multimap.size();
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean remove(Object o) {
/* 60 */     Predicate<? super Map.Entry<K, V>> entryPredicate = this.multimap.entryPredicate();
/* 61 */     Iterator<Map.Entry<K, V>> unfilteredItr = this.multimap.unfiltered().entries().iterator();
/* 62 */     while (unfilteredItr.hasNext()) {
/* 63 */       Map.Entry<K, V> entry = unfilteredItr.next();
/* 64 */       if (entryPredicate.apply(entry) && Objects.equal(entry.getValue(), o)) {
/* 65 */         unfilteredItr.remove();
/* 66 */         return true;
/*    */       } 
/*    */     } 
/* 69 */     return false;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean removeAll(Collection<?> c) {
/* 74 */     return Iterables.removeIf(this.multimap
/* 75 */         .unfiltered().entries(), 
/*    */         
/* 77 */         Predicates.and(this.multimap
/* 78 */           .entryPredicate(), Maps.valuePredicateOnEntries(Predicates.in(c))));
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean retainAll(Collection<?> c) {
/* 83 */     return Iterables.removeIf(this.multimap
/* 84 */         .unfiltered().entries(), 
/*    */         
/* 86 */         Predicates.and(this.multimap
/* 87 */           .entryPredicate(), 
/* 88 */           Maps.valuePredicateOnEntries(Predicates.not(Predicates.in(c)))));
/*    */   }
/*    */ 
/*    */   
/*    */   public void clear() {
/* 93 */     this.multimap.clear();
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\google\common\collect\FilteredMultimapValues.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */