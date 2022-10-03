/*     */ package com.google.common.collect;
/*     */ 
/*     */ import com.google.common.annotations.GwtCompatible;
/*     */ import com.google.errorprone.annotations.CanIgnoreReturnValue;
/*     */ import java.util.Collection;
/*     */ import java.util.Collections;
/*     */ import java.util.Map;
/*     */ import java.util.NavigableSet;
/*     */ import java.util.Set;
/*     */ import java.util.SortedSet;
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
/*     */ abstract class AbstractSortedSetMultimap<K, V>
/*     */   extends AbstractSetMultimap<K, V>
/*     */   implements SortedSetMultimap<K, V>
/*     */ {
/*     */   private static final long serialVersionUID = 430848587173315748L;
/*     */   
/*     */   protected AbstractSortedSetMultimap(Map<K, Collection<V>> map) {
/*  44 */     super(map);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   SortedSet<V> createUnmodifiableEmptyCollection() {
/*  52 */     return unmodifiableCollectionSubclass(createCollection());
/*     */   }
/*     */ 
/*     */   
/*     */   <E> SortedSet<E> unmodifiableCollectionSubclass(Collection<E> collection) {
/*  57 */     if (collection instanceof NavigableSet) {
/*  58 */       return Sets.unmodifiableNavigableSet((NavigableSet<E>)collection);
/*     */     }
/*  60 */     return Collections.unmodifiableSortedSet((SortedSet<E>)collection);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   Collection<V> wrapCollection(K key, Collection<V> collection) {
/*  66 */     if (collection instanceof NavigableSet) {
/*  67 */       return new AbstractMapBasedMultimap.WrappedNavigableSet(this, key, (NavigableSet<V>)collection, null);
/*     */     }
/*  69 */     return new AbstractMapBasedMultimap.WrappedSortedSet(this, key, (SortedSet<V>)collection, null);
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
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public SortedSet<V> get(K key) {
/*  87 */     return (SortedSet<V>)super.get(key);
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
/*     */   public SortedSet<V> removeAll(Object key) {
/* 100 */     return (SortedSet<V>)super.removeAll(key);
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
/*     */   
/*     */   @CanIgnoreReturnValue
/*     */   public SortedSet<V> replaceValues(K key, Iterable<? extends V> values) {
/* 116 */     return (SortedSet<V>)super.replaceValues(key, values);
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
/*     */ 
/*     */ 
/*     */   
/*     */   public Map<K, Collection<V>> asMap() {
/* 133 */     return super.asMap();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Collection<V> values() {
/* 144 */     return super.values();
/*     */   }
/*     */   
/*     */   abstract SortedSet<V> createCollection();
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\google\common\collect\AbstractSortedSetMultimap.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */