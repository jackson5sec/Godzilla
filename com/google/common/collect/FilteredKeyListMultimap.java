/*    */ package com.google.common.collect;
/*    */ 
/*    */ import com.google.common.annotations.GwtCompatible;
/*    */ import com.google.common.base.Predicate;
/*    */ import java.util.Collection;
/*    */ import java.util.List;
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
/*    */ final class FilteredKeyListMultimap<K, V>
/*    */   extends FilteredKeyMultimap<K, V>
/*    */   implements ListMultimap<K, V>
/*    */ {
/*    */   FilteredKeyListMultimap(ListMultimap<K, V> unfiltered, Predicate<? super K> keyPredicate) {
/* 33 */     super(unfiltered, keyPredicate);
/*    */   }
/*    */ 
/*    */   
/*    */   public ListMultimap<K, V> unfiltered() {
/* 38 */     return (ListMultimap<K, V>)super.unfiltered();
/*    */   }
/*    */ 
/*    */   
/*    */   public List<V> get(K key) {
/* 43 */     return (List<V>)super.get(key);
/*    */   }
/*    */ 
/*    */   
/*    */   public List<V> removeAll(Object key) {
/* 48 */     return (List<V>)super.removeAll(key);
/*    */   }
/*    */ 
/*    */   
/*    */   public List<V> replaceValues(K key, Iterable<? extends V> values) {
/* 53 */     return (List<V>)super.replaceValues(key, values);
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\google\common\collect\FilteredKeyListMultimap.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */