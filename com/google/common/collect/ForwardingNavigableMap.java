/*     */ package com.google.common.collect;
/*     */ 
/*     */ import com.google.common.annotations.Beta;
/*     */ import com.google.common.annotations.GwtIncompatible;
/*     */ import java.util.Iterator;
/*     */ import java.util.Map;
/*     */ import java.util.NavigableMap;
/*     */ import java.util.NavigableSet;
/*     */ import java.util.NoSuchElementException;
/*     */ import java.util.SortedMap;
/*     */ import java.util.function.BiFunction;
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
/*     */ public abstract class ForwardingNavigableMap<K, V>
/*     */   extends ForwardingSortedMap<K, V>
/*     */   implements NavigableMap<K, V>
/*     */ {
/*     */   public Map.Entry<K, V> lowerEntry(K key) {
/*  69 */     return delegate().lowerEntry(key);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected Map.Entry<K, V> standardLowerEntry(K key) {
/*  78 */     return headMap(key, false).lastEntry();
/*     */   }
/*     */ 
/*     */   
/*     */   public K lowerKey(K key) {
/*  83 */     return delegate().lowerKey(key);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected K standardLowerKey(K key) {
/*  92 */     return Maps.keyOrNull(lowerEntry(key));
/*     */   }
/*     */ 
/*     */   
/*     */   public Map.Entry<K, V> floorEntry(K key) {
/*  97 */     return delegate().floorEntry(key);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected Map.Entry<K, V> standardFloorEntry(K key) {
/* 106 */     return headMap(key, true).lastEntry();
/*     */   }
/*     */ 
/*     */   
/*     */   public K floorKey(K key) {
/* 111 */     return delegate().floorKey(key);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected K standardFloorKey(K key) {
/* 120 */     return Maps.keyOrNull(floorEntry(key));
/*     */   }
/*     */ 
/*     */   
/*     */   public Map.Entry<K, V> ceilingEntry(K key) {
/* 125 */     return delegate().ceilingEntry(key);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected Map.Entry<K, V> standardCeilingEntry(K key) {
/* 134 */     return tailMap(key, true).firstEntry();
/*     */   }
/*     */ 
/*     */   
/*     */   public K ceilingKey(K key) {
/* 139 */     return delegate().ceilingKey(key);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected K standardCeilingKey(K key) {
/* 148 */     return Maps.keyOrNull(ceilingEntry(key));
/*     */   }
/*     */ 
/*     */   
/*     */   public Map.Entry<K, V> higherEntry(K key) {
/* 153 */     return delegate().higherEntry(key);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected Map.Entry<K, V> standardHigherEntry(K key) {
/* 162 */     return tailMap(key, false).firstEntry();
/*     */   }
/*     */ 
/*     */   
/*     */   public K higherKey(K key) {
/* 167 */     return delegate().higherKey(key);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected K standardHigherKey(K key) {
/* 176 */     return Maps.keyOrNull(higherEntry(key));
/*     */   }
/*     */ 
/*     */   
/*     */   public Map.Entry<K, V> firstEntry() {
/* 181 */     return delegate().firstEntry();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected Map.Entry<K, V> standardFirstEntry() {
/* 190 */     return Iterables.<Map.Entry<K, V>>getFirst(entrySet(), null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected K standardFirstKey() {
/* 199 */     Map.Entry<K, V> entry = firstEntry();
/* 200 */     if (entry == null) {
/* 201 */       throw new NoSuchElementException();
/*     */     }
/* 203 */     return entry.getKey();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Map.Entry<K, V> lastEntry() {
/* 209 */     return delegate().lastEntry();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected Map.Entry<K, V> standardLastEntry() {
/* 218 */     return Iterables.<Map.Entry<K, V>>getFirst(descendingMap().entrySet(), null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected K standardLastKey() {
/* 226 */     Map.Entry<K, V> entry = lastEntry();
/* 227 */     if (entry == null) {
/* 228 */       throw new NoSuchElementException();
/*     */     }
/* 230 */     return entry.getKey();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Map.Entry<K, V> pollFirstEntry() {
/* 236 */     return delegate().pollFirstEntry();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected Map.Entry<K, V> standardPollFirstEntry() {
/* 245 */     return Iterators.<Map.Entry<K, V>>pollNext(entrySet().iterator());
/*     */   }
/*     */ 
/*     */   
/*     */   public Map.Entry<K, V> pollLastEntry() {
/* 250 */     return delegate().pollLastEntry();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected Map.Entry<K, V> standardPollLastEntry() {
/* 259 */     return Iterators.<Map.Entry<K, V>>pollNext(descendingMap().entrySet().iterator());
/*     */   }
/*     */ 
/*     */   
/*     */   public NavigableMap<K, V> descendingMap() {
/* 264 */     return delegate().descendingMap();
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
/*     */   @Beta
/*     */   protected class StandardDescendingMap
/*     */     extends Maps.DescendingMap<K, V>
/*     */   {
/*     */     NavigableMap<K, V> forward() {
/* 285 */       return ForwardingNavigableMap.this;
/*     */     }
/*     */ 
/*     */     
/*     */     public void replaceAll(BiFunction<? super K, ? super V, ? extends V> function) {
/* 290 */       forward().replaceAll(function);
/*     */     }
/*     */ 
/*     */     
/*     */     protected Iterator<Map.Entry<K, V>> entryIterator() {
/* 295 */       return new Iterator<Map.Entry<K, V>>() {
/* 296 */           private Map.Entry<K, V> toRemove = null;
/* 297 */           private Map.Entry<K, V> nextOrNull = ForwardingNavigableMap.StandardDescendingMap.this.forward().lastEntry();
/*     */ 
/*     */           
/*     */           public boolean hasNext() {
/* 301 */             return (this.nextOrNull != null);
/*     */           }
/*     */ 
/*     */           
/*     */           public Map.Entry<K, V> next() {
/* 306 */             if (!hasNext()) {
/* 307 */               throw new NoSuchElementException();
/*     */             }
/*     */             try {
/* 310 */               return this.nextOrNull;
/*     */             } finally {
/* 312 */               this.toRemove = this.nextOrNull;
/* 313 */               this.nextOrNull = ForwardingNavigableMap.StandardDescendingMap.this.forward().lowerEntry(this.nextOrNull.getKey());
/*     */             } 
/*     */           }
/*     */ 
/*     */           
/*     */           public void remove() {
/* 319 */             CollectPreconditions.checkRemove((this.toRemove != null));
/* 320 */             ForwardingNavigableMap.StandardDescendingMap.this.forward().remove(this.toRemove.getKey());
/* 321 */             this.toRemove = null;
/*     */           }
/*     */         };
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public NavigableSet<K> navigableKeySet() {
/* 329 */     return delegate().navigableKeySet();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Beta
/*     */   protected class StandardNavigableKeySet
/*     */     extends Maps.NavigableKeySet<K, V>
/*     */   {
/*     */     public StandardNavigableKeySet() {
/* 344 */       super(ForwardingNavigableMap.this);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public NavigableSet<K> descendingKeySet() {
/* 350 */     return delegate().descendingKeySet();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Beta
/*     */   protected NavigableSet<K> standardDescendingKeySet() {
/* 362 */     return descendingMap().navigableKeySet();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected SortedMap<K, V> standardSubMap(K fromKey, K toKey) {
/* 372 */     return subMap(fromKey, true, toKey, false);
/*     */   }
/*     */ 
/*     */   
/*     */   public NavigableMap<K, V> subMap(K fromKey, boolean fromInclusive, K toKey, boolean toInclusive) {
/* 377 */     return delegate().subMap(fromKey, fromInclusive, toKey, toInclusive);
/*     */   }
/*     */ 
/*     */   
/*     */   public NavigableMap<K, V> headMap(K toKey, boolean inclusive) {
/* 382 */     return delegate().headMap(toKey, inclusive);
/*     */   }
/*     */ 
/*     */   
/*     */   public NavigableMap<K, V> tailMap(K fromKey, boolean inclusive) {
/* 387 */     return delegate().tailMap(fromKey, inclusive);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected SortedMap<K, V> standardHeadMap(K toKey) {
/* 396 */     return headMap(toKey, false);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected SortedMap<K, V> standardTailMap(K fromKey) {
/* 405 */     return tailMap(fromKey, true);
/*     */   }
/*     */   
/*     */   protected abstract NavigableMap<K, V> delegate();
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\google\common\collect\ForwardingNavigableMap.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */