/*     */ package com.google.common.collect;
/*     */ 
/*     */ import com.google.common.annotations.GwtCompatible;
/*     */ import com.google.common.base.MoreObjects;
/*     */ import com.google.common.base.Preconditions;
/*     */ import com.google.common.base.Predicate;
/*     */ import com.google.common.base.Predicates;
/*     */ import java.util.Collection;
/*     */ import java.util.Collections;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
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
/*     */ class FilteredEntryMultimap<K, V>
/*     */   extends AbstractMultimap<K, V>
/*     */   implements FilteredMultimap<K, V>
/*     */ {
/*     */   final Multimap<K, V> unfiltered;
/*     */   final Predicate<? super Map.Entry<K, V>> predicate;
/*     */   
/*     */   FilteredEntryMultimap(Multimap<K, V> unfiltered, Predicate<? super Map.Entry<K, V>> predicate) {
/*  50 */     this.unfiltered = (Multimap<K, V>)Preconditions.checkNotNull(unfiltered);
/*  51 */     this.predicate = (Predicate<? super Map.Entry<K, V>>)Preconditions.checkNotNull(predicate);
/*     */   }
/*     */ 
/*     */   
/*     */   public Multimap<K, V> unfiltered() {
/*  56 */     return this.unfiltered;
/*     */   }
/*     */ 
/*     */   
/*     */   public Predicate<? super Map.Entry<K, V>> entryPredicate() {
/*  61 */     return this.predicate;
/*     */   }
/*     */ 
/*     */   
/*     */   public int size() {
/*  66 */     return entries().size();
/*     */   }
/*     */   
/*     */   private boolean satisfies(K key, V value) {
/*  70 */     return this.predicate.apply(Maps.immutableEntry(key, value));
/*     */   }
/*     */   
/*     */   final class ValuePredicate implements Predicate<V> {
/*     */     private final K key;
/*     */     
/*     */     ValuePredicate(K key) {
/*  77 */       this.key = key;
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean apply(V value) {
/*  82 */       return FilteredEntryMultimap.this.satisfies(this.key, value);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   static <E> Collection<E> filterCollection(Collection<E> collection, Predicate<? super E> predicate) {
/*  88 */     if (collection instanceof Set) {
/*  89 */       return Sets.filter((Set<E>)collection, predicate);
/*     */     }
/*  91 */     return Collections2.filter(collection, predicate);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean containsKey(Object key) {
/*  97 */     return (asMap().get(key) != null);
/*     */   }
/*     */ 
/*     */   
/*     */   public Collection<V> removeAll(Object key) {
/* 102 */     return (Collection<V>)MoreObjects.firstNonNull(asMap().remove(key), unmodifiableEmptyCollection());
/*     */   }
/*     */ 
/*     */   
/*     */   Collection<V> unmodifiableEmptyCollection() {
/* 107 */     return (this.unfiltered instanceof SetMultimap) ? 
/* 108 */       Collections.<V>emptySet() : 
/* 109 */       Collections.<V>emptyList();
/*     */   }
/*     */ 
/*     */   
/*     */   public void clear() {
/* 114 */     entries().clear();
/*     */   }
/*     */ 
/*     */   
/*     */   public Collection<V> get(K key) {
/* 119 */     return filterCollection(this.unfiltered.get(key), new ValuePredicate(key));
/*     */   }
/*     */ 
/*     */   
/*     */   Collection<Map.Entry<K, V>> createEntries() {
/* 124 */     return filterCollection(this.unfiltered.entries(), this.predicate);
/*     */   }
/*     */ 
/*     */   
/*     */   Collection<V> createValues() {
/* 129 */     return new FilteredMultimapValues<>(this);
/*     */   }
/*     */ 
/*     */   
/*     */   Iterator<Map.Entry<K, V>> entryIterator() {
/* 134 */     throw new AssertionError("should never be called");
/*     */   }
/*     */ 
/*     */   
/*     */   Map<K, Collection<V>> createAsMap() {
/* 139 */     return new AsMap();
/*     */   }
/*     */ 
/*     */   
/*     */   Set<K> createKeySet() {
/* 144 */     return asMap().keySet();
/*     */   }
/*     */   
/*     */   boolean removeEntriesIf(Predicate<? super Map.Entry<K, Collection<V>>> predicate) {
/* 148 */     Iterator<Map.Entry<K, Collection<V>>> entryIterator = this.unfiltered.asMap().entrySet().iterator();
/* 149 */     boolean changed = false;
/* 150 */     while (entryIterator.hasNext()) {
/* 151 */       Map.Entry<K, Collection<V>> entry = entryIterator.next();
/* 152 */       K key = entry.getKey();
/* 153 */       Collection<V> collection = filterCollection(entry.getValue(), new ValuePredicate(key));
/* 154 */       if (!collection.isEmpty() && predicate.apply(Maps.immutableEntry(key, collection))) {
/* 155 */         if (collection.size() == ((Collection)entry.getValue()).size()) {
/* 156 */           entryIterator.remove();
/*     */         } else {
/* 158 */           collection.clear();
/*     */         } 
/* 160 */         changed = true;
/*     */       } 
/*     */     } 
/* 163 */     return changed;
/*     */   }
/*     */   
/*     */   class AsMap
/*     */     extends Maps.ViewCachingAbstractMap<K, Collection<V>>
/*     */   {
/*     */     public boolean containsKey(Object key) {
/* 170 */       return (get(key) != null);
/*     */     }
/*     */ 
/*     */     
/*     */     public void clear() {
/* 175 */       FilteredEntryMultimap.this.clear();
/*     */     }
/*     */ 
/*     */     
/*     */     public Collection<V> get(Object key) {
/* 180 */       Collection<V> result = (Collection<V>)FilteredEntryMultimap.this.unfiltered.asMap().get(key);
/* 181 */       if (result == null) {
/* 182 */         return null;
/*     */       }
/*     */       
/* 185 */       K k = (K)key;
/* 186 */       result = FilteredEntryMultimap.filterCollection(result, new FilteredEntryMultimap.ValuePredicate(k));
/* 187 */       return result.isEmpty() ? null : result;
/*     */     }
/*     */ 
/*     */     
/*     */     public Collection<V> remove(Object key) {
/* 192 */       Collection<V> collection = (Collection<V>)FilteredEntryMultimap.this.unfiltered.asMap().get(key);
/* 193 */       if (collection == null) {
/* 194 */         return null;
/*     */       }
/*     */       
/* 197 */       K k = (K)key;
/* 198 */       List<V> result = Lists.newArrayList();
/* 199 */       Iterator<V> itr = collection.iterator();
/* 200 */       while (itr.hasNext()) {
/* 201 */         V v = itr.next();
/* 202 */         if (FilteredEntryMultimap.this.satisfies(k, v)) {
/* 203 */           itr.remove();
/* 204 */           result.add(v);
/*     */         } 
/*     */       } 
/* 207 */       if (result.isEmpty())
/* 208 */         return null; 
/* 209 */       if (FilteredEntryMultimap.this.unfiltered instanceof SetMultimap) {
/* 210 */         return Collections.unmodifiableSet(Sets.newLinkedHashSet(result));
/*     */       }
/* 212 */       return Collections.unmodifiableList(result);
/*     */     }
/*     */ 
/*     */     
/*     */     Set<K> createKeySet() {
/*     */       class KeySetImpl
/*     */         extends Maps.KeySet<K, Collection<V>>
/*     */       {
/*     */         KeySetImpl() {
/* 221 */           super(FilteredEntryMultimap.AsMap.this);
/*     */         }
/*     */ 
/*     */         
/*     */         public boolean removeAll(Collection<?> c) {
/* 226 */           return FilteredEntryMultimap.this.removeEntriesIf(Maps.keyPredicateOnEntries(Predicates.in(c)));
/*     */         }
/*     */ 
/*     */         
/*     */         public boolean retainAll(Collection<?> c) {
/* 231 */           return FilteredEntryMultimap.this.removeEntriesIf(Maps.keyPredicateOnEntries(Predicates.not(Predicates.in(c))));
/*     */         }
/*     */ 
/*     */         
/*     */         public boolean remove(Object o) {
/* 236 */           return (FilteredEntryMultimap.AsMap.this.remove(o) != null);
/*     */         }
/*     */       };
/* 239 */       return new KeySetImpl();
/*     */     }
/*     */ 
/*     */     
/*     */     Set<Map.Entry<K, Collection<V>>> createEntrySet() {
/*     */       class EntrySetImpl
/*     */         extends Maps.EntrySet<K, Collection<V>>
/*     */       {
/*     */         Map<K, Collection<V>> map() {
/* 248 */           return FilteredEntryMultimap.AsMap.this;
/*     */         }
/*     */ 
/*     */         
/*     */         public Iterator<Map.Entry<K, Collection<V>>> iterator() {
/* 253 */           return new AbstractIterator<Map.Entry<K, Collection<V>>>() {
/* 254 */               final Iterator<Map.Entry<K, Collection<V>>> backingIterator = FilteredEntryMultimap.this.unfiltered
/* 255 */                 .asMap().entrySet().iterator();
/*     */ 
/*     */               
/*     */               protected Map.Entry<K, Collection<V>> computeNext() {
/* 259 */                 while (this.backingIterator.hasNext()) {
/* 260 */                   Map.Entry<K, Collection<V>> entry = this.backingIterator.next();
/* 261 */                   K key = entry.getKey();
/*     */                   
/* 263 */                   Collection<V> collection = FilteredEntryMultimap.filterCollection(entry.getValue(), new FilteredEntryMultimap.ValuePredicate(key));
/* 264 */                   if (!collection.isEmpty()) {
/* 265 */                     return Maps.immutableEntry(key, collection);
/*     */                   }
/*     */                 } 
/* 268 */                 return endOfData();
/*     */               }
/*     */             };
/*     */         }
/*     */ 
/*     */         
/*     */         public boolean removeAll(Collection<?> c) {
/* 275 */           return FilteredEntryMultimap.this.removeEntriesIf(Predicates.in(c));
/*     */         }
/*     */ 
/*     */         
/*     */         public boolean retainAll(Collection<?> c) {
/* 280 */           return FilteredEntryMultimap.this.removeEntriesIf(Predicates.not(Predicates.in(c)));
/*     */         }
/*     */ 
/*     */         
/*     */         public int size() {
/* 285 */           return Iterators.size(iterator());
/*     */         }
/*     */       };
/* 288 */       return new EntrySetImpl();
/*     */     }
/*     */     
/*     */     Collection<Collection<V>> createValues() {
/*     */       class ValuesImpl
/*     */         extends Maps.Values<K, Collection<V>>
/*     */       {
/*     */         ValuesImpl() {
/* 296 */           super(FilteredEntryMultimap.AsMap.this);
/*     */         }
/*     */ 
/*     */         
/*     */         public boolean remove(Object o) {
/* 301 */           if (o instanceof Collection) {
/* 302 */             Collection<?> c = (Collection)o;
/*     */             
/* 304 */             Iterator<Map.Entry<K, Collection<V>>> entryIterator = FilteredEntryMultimap.this.unfiltered.asMap().entrySet().iterator();
/* 305 */             while (entryIterator.hasNext()) {
/* 306 */               Map.Entry<K, Collection<V>> entry = entryIterator.next();
/* 307 */               K key = entry.getKey();
/*     */               
/* 309 */               Collection<V> collection = FilteredEntryMultimap.filterCollection(entry.getValue(), new FilteredEntryMultimap.ValuePredicate(key));
/* 310 */               if (!collection.isEmpty() && c.equals(collection)) {
/* 311 */                 if (collection.size() == ((Collection)entry.getValue()).size()) {
/* 312 */                   entryIterator.remove();
/*     */                 } else {
/* 314 */                   collection.clear();
/*     */                 } 
/* 316 */                 return true;
/*     */               } 
/*     */             } 
/*     */           } 
/* 320 */           return false;
/*     */         }
/*     */ 
/*     */         
/*     */         public boolean removeAll(Collection<?> c) {
/* 325 */           return FilteredEntryMultimap.this.removeEntriesIf(Maps.valuePredicateOnEntries(Predicates.in(c)));
/*     */         }
/*     */ 
/*     */         
/*     */         public boolean retainAll(Collection<?> c) {
/* 330 */           return FilteredEntryMultimap.this.removeEntriesIf(Maps.valuePredicateOnEntries(Predicates.not(Predicates.in(c))));
/*     */         }
/*     */       };
/* 333 */       return new ValuesImpl();
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   Multiset<K> createKeys() {
/* 339 */     return new Keys();
/*     */   }
/*     */   
/*     */   class Keys
/*     */     extends Multimaps.Keys<K, V> {
/*     */     Keys() {
/* 345 */       super(FilteredEntryMultimap.this);
/*     */     }
/*     */ 
/*     */     
/*     */     public int remove(Object key, int occurrences) {
/* 350 */       CollectPreconditions.checkNonnegative(occurrences, "occurrences");
/* 351 */       if (occurrences == 0) {
/* 352 */         return count(key);
/*     */       }
/* 354 */       Collection<V> collection = (Collection<V>)FilteredEntryMultimap.this.unfiltered.asMap().get(key);
/* 355 */       if (collection == null) {
/* 356 */         return 0;
/*     */       }
/*     */       
/* 359 */       K k = (K)key;
/* 360 */       int oldCount = 0;
/* 361 */       Iterator<V> itr = collection.iterator();
/* 362 */       while (itr.hasNext()) {
/* 363 */         V v = itr.next();
/*     */         
/* 365 */         oldCount++;
/* 366 */         if (FilteredEntryMultimap.this.satisfies(k, v) && oldCount <= occurrences) {
/* 367 */           itr.remove();
/*     */         }
/*     */       } 
/*     */       
/* 371 */       return oldCount;
/*     */     }
/*     */ 
/*     */     
/*     */     public Set<Multiset.Entry<K>> entrySet() {
/* 376 */       return new Multisets.EntrySet<K>()
/*     */         {
/*     */           Multiset<K> multiset()
/*     */           {
/* 380 */             return FilteredEntryMultimap.Keys.this;
/*     */           }
/*     */ 
/*     */           
/*     */           public Iterator<Multiset.Entry<K>> iterator() {
/* 385 */             return FilteredEntryMultimap.Keys.this.entryIterator();
/*     */           }
/*     */ 
/*     */           
/*     */           public int size() {
/* 390 */             return FilteredEntryMultimap.this.keySet().size();
/*     */           }
/*     */           
/*     */           private boolean removeEntriesIf(final Predicate<? super Multiset.Entry<K>> predicate) {
/* 394 */             return FilteredEntryMultimap.this.removeEntriesIf(new Predicate<Map.Entry<K, Collection<V>>>()
/*     */                 {
/*     */                   public boolean apply(Map.Entry<K, Collection<V>> entry)
/*     */                   {
/* 398 */                     return predicate.apply(
/* 399 */                         Multisets.immutableEntry(entry.getKey(), ((Collection)entry.getValue()).size()));
/*     */                   }
/*     */                 });
/*     */           }
/*     */ 
/*     */           
/*     */           public boolean removeAll(Collection<?> c) {
/* 406 */             return removeEntriesIf(Predicates.in(c));
/*     */           }
/*     */ 
/*     */           
/*     */           public boolean retainAll(Collection<?> c) {
/* 411 */             return removeEntriesIf(Predicates.not(Predicates.in(c)));
/*     */           }
/*     */         };
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\google\common\collect\FilteredEntryMultimap.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */