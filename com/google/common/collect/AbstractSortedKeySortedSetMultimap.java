/*    */ package com.google.common.collect;
/*    */ 
/*    */ import com.google.common.annotations.GwtCompatible;
/*    */ import java.util.Collection;
/*    */ import java.util.Map;
/*    */ import java.util.Set;
/*    */ import java.util.SortedMap;
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
/*    */ @GwtCompatible
/*    */ abstract class AbstractSortedKeySortedSetMultimap<K, V>
/*    */   extends AbstractSortedSetMultimap<K, V>
/*    */ {
/*    */   AbstractSortedKeySortedSetMultimap(SortedMap<K, Collection<V>> map) {
/* 37 */     super(map);
/*    */   }
/*    */ 
/*    */   
/*    */   public SortedMap<K, Collection<V>> asMap() {
/* 42 */     return (SortedMap<K, Collection<V>>)super.asMap();
/*    */   }
/*    */ 
/*    */   
/*    */   SortedMap<K, Collection<V>> backingMap() {
/* 47 */     return (SortedMap<K, Collection<V>>)super.backingMap();
/*    */   }
/*    */ 
/*    */   
/*    */   public SortedSet<K> keySet() {
/* 52 */     return (SortedSet<K>)super.keySet();
/*    */   }
/*    */ 
/*    */   
/*    */   Set<K> createKeySet() {
/* 57 */     return createMaybeNavigableKeySet();
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\google\common\collect\AbstractSortedKeySortedSetMultimap.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */