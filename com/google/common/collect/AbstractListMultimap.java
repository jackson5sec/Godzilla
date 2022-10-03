/*     */ package com.google.common.collect;
/*     */ 
/*     */ import com.google.common.annotations.GwtCompatible;
/*     */ import com.google.errorprone.annotations.CanIgnoreReturnValue;
/*     */ import java.util.Collection;
/*     */ import java.util.Collections;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @GwtCompatible
/*     */ abstract class AbstractListMultimap<K, V>
/*     */   extends AbstractMapBasedMultimap<K, V>
/*     */   implements ListMultimap<K, V>
/*     */ {
/*     */   private static final long serialVersionUID = 6588350623831699109L;
/*     */   
/*     */   protected AbstractListMultimap(Map<K, Collection<V>> map) {
/*  44 */     super(map);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   List<V> createUnmodifiableEmptyCollection() {
/*  52 */     return Collections.emptyList();
/*     */   }
/*     */ 
/*     */   
/*     */   <E> Collection<E> unmodifiableCollectionSubclass(Collection<E> collection) {
/*  57 */     return Collections.unmodifiableList((List<? extends E>)collection);
/*     */   }
/*     */ 
/*     */   
/*     */   Collection<V> wrapCollection(K key, Collection<V> collection) {
/*  62 */     return wrapList(key, (List<V>)collection, (AbstractMapBasedMultimap<K, V>.WrappedCollection)null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public List<V> get(K key) {
/*  76 */     return (List<V>)super.get(key);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @CanIgnoreReturnValue
/*     */   public List<V> removeAll(Object key) {
/*  89 */     return (List<V>)super.removeAll(key);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @CanIgnoreReturnValue
/*     */   public List<V> replaceValues(K key, Iterable<? extends V> values) {
/* 102 */     return (List<V>)super.replaceValues(key, values);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @CanIgnoreReturnValue
/*     */   public boolean put(K key, V value) {
/* 115 */     return super.put(key, value);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Map<K, Collection<V>> asMap() {
/* 126 */     return super.asMap();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean equals(Object object) {
/* 137 */     return super.equals(object);
/*     */   }
/*     */   
/*     */   abstract List<V> createCollection();
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\google\common\collect\AbstractListMultimap.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */