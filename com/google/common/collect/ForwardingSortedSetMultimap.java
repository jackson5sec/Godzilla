/*    */ package com.google.common.collect;
/*    */ 
/*    */ import com.google.common.annotations.GwtCompatible;
/*    */ import java.util.Collection;
/*    */ import java.util.Comparator;
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
/*    */ public abstract class ForwardingSortedSetMultimap<K, V>
/*    */   extends ForwardingSetMultimap<K, V>
/*    */   implements SortedSetMultimap<K, V>
/*    */ {
/*    */   public SortedSet<V> get(K key) {
/* 48 */     return delegate().get(key);
/*    */   }
/*    */ 
/*    */   
/*    */   public SortedSet<V> removeAll(Object key) {
/* 53 */     return delegate().removeAll(key);
/*    */   }
/*    */ 
/*    */   
/*    */   public SortedSet<V> replaceValues(K key, Iterable<? extends V> values) {
/* 58 */     return delegate().replaceValues(key, values);
/*    */   }
/*    */ 
/*    */   
/*    */   public Comparator<? super V> valueComparator() {
/* 63 */     return delegate().valueComparator();
/*    */   }
/*    */   
/*    */   protected abstract SortedSetMultimap<K, V> delegate();
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\google\common\collect\ForwardingSortedSetMultimap.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */