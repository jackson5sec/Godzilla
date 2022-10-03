/*     */ package com.google.common.collect;
/*     */ 
/*     */ import com.google.common.annotations.GwtCompatible;
/*     */ import com.google.errorprone.annotations.CanIgnoreReturnValue;
/*     */ import java.util.Collection;
/*     */ import java.util.Collections;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
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
/*     */ abstract class AbstractSetMultimap<K, V>
/*     */   extends AbstractMapBasedMultimap<K, V>
/*     */   implements SetMultimap<K, V>
/*     */ {
/*     */   private static final long serialVersionUID = 7431625294878419160L;
/*     */   
/*     */   protected AbstractSetMultimap(Map<K, Collection<V>> map) {
/*  44 */     super(map);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   Set<V> createUnmodifiableEmptyCollection() {
/*  52 */     return Collections.emptySet();
/*     */   }
/*     */ 
/*     */   
/*     */   <E> Collection<E> unmodifiableCollectionSubclass(Collection<E> collection) {
/*  57 */     return Collections.unmodifiableSet((Set<? extends E>)collection);
/*     */   }
/*     */ 
/*     */   
/*     */   Collection<V> wrapCollection(K key, Collection<V> collection) {
/*  62 */     return new AbstractMapBasedMultimap.WrappedSet(this, key, (Set<V>)collection);
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
/*     */   public Set<V> get(K key) {
/*  75 */     return (Set<V>)super.get(key);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Set<Map.Entry<K, V>> entries() {
/*  86 */     return (Set<Map.Entry<K, V>>)super.entries();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @CanIgnoreReturnValue
/*     */   public Set<V> removeAll(Object key) {
/*  98 */     return (Set<V>)super.removeAll(key);
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
/*     */   @CanIgnoreReturnValue
/*     */   public Set<V> replaceValues(K key, Iterable<? extends V> values) {
/* 112 */     return (Set<V>)super.replaceValues(key, values);
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
/* 123 */     return super.asMap();
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
/*     */   @CanIgnoreReturnValue
/*     */   public boolean put(K key, V value) {
/* 137 */     return super.put(key, value);
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
/* 148 */     return super.equals(object);
/*     */   }
/*     */   
/*     */   abstract Set<V> createCollection();
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\google\common\collect\AbstractSetMultimap.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */