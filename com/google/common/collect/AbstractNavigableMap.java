/*     */ package com.google.common.collect;
/*     */ 
/*     */ import com.google.common.annotations.GwtIncompatible;
/*     */ import java.util.Iterator;
/*     */ import java.util.Map;
/*     */ import java.util.NavigableMap;
/*     */ import java.util.NavigableSet;
/*     */ import java.util.NoSuchElementException;
/*     */ import java.util.Set;
/*     */ import java.util.SortedMap;
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
/*     */ @GwtIncompatible
/*     */ abstract class AbstractNavigableMap<K, V>
/*     */   extends Maps.IteratorBasedAbstractMap<K, V>
/*     */   implements NavigableMap<K, V>
/*     */ {
/*     */   public abstract V get(Object paramObject);
/*     */   
/*     */   public Map.Entry<K, V> firstEntry() {
/*  43 */     return Iterators.<Map.Entry<K, V>>getNext(entryIterator(), null);
/*     */   }
/*     */ 
/*     */   
/*     */   public Map.Entry<K, V> lastEntry() {
/*  48 */     return Iterators.<Map.Entry<K, V>>getNext(descendingEntryIterator(), null);
/*     */   }
/*     */ 
/*     */   
/*     */   public Map.Entry<K, V> pollFirstEntry() {
/*  53 */     return Iterators.<Map.Entry<K, V>>pollNext(entryIterator());
/*     */   }
/*     */ 
/*     */   
/*     */   public Map.Entry<K, V> pollLastEntry() {
/*  58 */     return Iterators.<Map.Entry<K, V>>pollNext(descendingEntryIterator());
/*     */   }
/*     */ 
/*     */   
/*     */   public K firstKey() {
/*  63 */     Map.Entry<K, V> entry = firstEntry();
/*  64 */     if (entry == null) {
/*  65 */       throw new NoSuchElementException();
/*     */     }
/*  67 */     return entry.getKey();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public K lastKey() {
/*  73 */     Map.Entry<K, V> entry = lastEntry();
/*  74 */     if (entry == null) {
/*  75 */       throw new NoSuchElementException();
/*     */     }
/*  77 */     return entry.getKey();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Map.Entry<K, V> lowerEntry(K key) {
/*  83 */     return headMap(key, false).lastEntry();
/*     */   }
/*     */ 
/*     */   
/*     */   public Map.Entry<K, V> floorEntry(K key) {
/*  88 */     return headMap(key, true).lastEntry();
/*     */   }
/*     */ 
/*     */   
/*     */   public Map.Entry<K, V> ceilingEntry(K key) {
/*  93 */     return tailMap(key, true).firstEntry();
/*     */   }
/*     */ 
/*     */   
/*     */   public Map.Entry<K, V> higherEntry(K key) {
/*  98 */     return tailMap(key, false).firstEntry();
/*     */   }
/*     */ 
/*     */   
/*     */   public K lowerKey(K key) {
/* 103 */     return Maps.keyOrNull(lowerEntry(key));
/*     */   }
/*     */ 
/*     */   
/*     */   public K floorKey(K key) {
/* 108 */     return Maps.keyOrNull(floorEntry(key));
/*     */   }
/*     */ 
/*     */   
/*     */   public K ceilingKey(K key) {
/* 113 */     return Maps.keyOrNull(ceilingEntry(key));
/*     */   }
/*     */ 
/*     */   
/*     */   public K higherKey(K key) {
/* 118 */     return Maps.keyOrNull(higherEntry(key));
/*     */   }
/*     */ 
/*     */   
/*     */   abstract Iterator<Map.Entry<K, V>> descendingEntryIterator();
/*     */   
/*     */   public SortedMap<K, V> subMap(K fromKey, K toKey) {
/* 125 */     return subMap(fromKey, true, toKey, false);
/*     */   }
/*     */ 
/*     */   
/*     */   public SortedMap<K, V> headMap(K toKey) {
/* 130 */     return headMap(toKey, false);
/*     */   }
/*     */ 
/*     */   
/*     */   public SortedMap<K, V> tailMap(K fromKey) {
/* 135 */     return tailMap(fromKey, true);
/*     */   }
/*     */ 
/*     */   
/*     */   public NavigableSet<K> navigableKeySet() {
/* 140 */     return new Maps.NavigableKeySet<>(this);
/*     */   }
/*     */ 
/*     */   
/*     */   public Set<K> keySet() {
/* 145 */     return navigableKeySet();
/*     */   }
/*     */ 
/*     */   
/*     */   public NavigableSet<K> descendingKeySet() {
/* 150 */     return descendingMap().navigableKeySet();
/*     */   }
/*     */ 
/*     */   
/*     */   public NavigableMap<K, V> descendingMap() {
/* 155 */     return new DescendingMap();
/*     */   }
/*     */   
/*     */   private final class DescendingMap
/*     */     extends Maps.DescendingMap<K, V> {
/*     */     NavigableMap<K, V> forward() {
/* 161 */       return AbstractNavigableMap.this;
/*     */     }
/*     */     private DescendingMap() {}
/*     */     
/*     */     Iterator<Map.Entry<K, V>> entryIterator() {
/* 166 */       return AbstractNavigableMap.this.descendingEntryIterator();
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\google\common\collect\AbstractNavigableMap.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */