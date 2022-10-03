/*     */ package com.google.common.collect;
/*     */ 
/*     */ import com.google.common.annotations.Beta;
/*     */ import com.google.common.annotations.GwtCompatible;
/*     */ import com.google.common.annotations.VisibleForTesting;
/*     */ import com.google.common.base.Preconditions;
/*     */ import com.google.errorprone.annotations.CanIgnoreReturnValue;
/*     */ import com.google.errorprone.annotations.concurrent.LazyInit;
/*     */ import com.google.j2objc.annotations.RetainedWith;
/*     */ import java.io.Serializable;
/*     */ import java.util.AbstractMap;
/*     */ import java.util.Arrays;
/*     */ import java.util.Collection;
/*     */ import java.util.Comparator;
/*     */ import java.util.EnumMap;
/*     */ import java.util.Iterator;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import java.util.Spliterator;
/*     */ import java.util.Spliterators;
/*     */ import java.util.function.BiFunction;
/*     */ import java.util.function.BinaryOperator;
/*     */ import java.util.function.Function;
/*     */ import java.util.function.Supplier;
/*     */ import java.util.stream.Collector;
/*     */ import java.util.stream.Collectors;
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
/*     */ @GwtCompatible(serializable = true, emulated = true)
/*     */ public abstract class ImmutableMap<K, V>
/*     */   implements Map<K, V>, Serializable
/*     */ {
/*     */   public static <T, K, V> Collector<T, ?, ImmutableMap<K, V>> toImmutableMap(Function<? super T, ? extends K> keyFunction, Function<? super T, ? extends V> valueFunction) {
/*  82 */     return CollectCollectors.toImmutableMap(keyFunction, valueFunction);
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
/*     */   public static <T, K, V> Collector<T, ?, ImmutableMap<K, V>> toImmutableMap(Function<? super T, ? extends K> keyFunction, Function<? super T, ? extends V> valueFunction, BinaryOperator<V> mergeFunction) {
/*  99 */     Preconditions.checkNotNull(keyFunction);
/* 100 */     Preconditions.checkNotNull(valueFunction);
/* 101 */     Preconditions.checkNotNull(mergeFunction);
/* 102 */     return Collectors.collectingAndThen(
/* 103 */         Collectors.toMap(keyFunction, valueFunction, mergeFunction, java.util.LinkedHashMap::new), ImmutableMap::copyOf);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <K, V> ImmutableMap<K, V> of() {
/* 114 */     return (ImmutableMap)RegularImmutableMap.EMPTY;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <K, V> ImmutableMap<K, V> of(K k1, V v1) {
/* 123 */     return ImmutableBiMap.of(k1, v1);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <K, V> ImmutableMap<K, V> of(K k1, V v1, K k2, V v2) {
/* 132 */     return RegularImmutableMap.fromEntries((Map.Entry<K, V>[])new Map.Entry[] { entryOf(k1, v1), entryOf(k2, v2) });
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <K, V> ImmutableMap<K, V> of(K k1, V v1, K k2, V v2, K k3, V v3) {
/* 141 */     return RegularImmutableMap.fromEntries((Map.Entry<K, V>[])new Map.Entry[] { entryOf(k1, v1), entryOf(k2, v2), entryOf(k3, v3) });
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <K, V> ImmutableMap<K, V> of(K k1, V v1, K k2, V v2, K k3, V v3, K k4, V v4) {
/* 150 */     return RegularImmutableMap.fromEntries((Map.Entry<K, V>[])new Map.Entry[] {
/* 151 */           entryOf(k1, v1), entryOf(k2, v2), entryOf(k3, v3), entryOf(k4, v4)
/*     */         });
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <K, V> ImmutableMap<K, V> of(K k1, V v1, K k2, V v2, K k3, V v3, K k4, V v4, K k5, V v5) {
/* 161 */     return RegularImmutableMap.fromEntries((Map.Entry<K, V>[])new Map.Entry[] {
/* 162 */           entryOf(k1, v1), entryOf(k2, v2), entryOf(k3, v3), entryOf(k4, v4), entryOf(k5, v5)
/*     */         });
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
/*     */   static <K, V> Map.Entry<K, V> entryOf(K key, V value) {
/* 175 */     CollectPreconditions.checkEntryNotNull(key, value);
/* 176 */     return new AbstractMap.SimpleImmutableEntry<>(key, value);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <K, V> Builder<K, V> builder() {
/* 184 */     return new Builder<>();
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
/*     */   @Beta
/*     */   public static <K, V> Builder<K, V> builderWithExpectedSize(int expectedSize) {
/* 201 */     CollectPreconditions.checkNonnegative(expectedSize, "expectedSize");
/* 202 */     return new Builder<>(expectedSize);
/*     */   }
/*     */ 
/*     */   
/*     */   static void checkNoConflict(boolean safe, String conflictDescription, Map.Entry<?, ?> entry1, Map.Entry<?, ?> entry2) {
/* 207 */     if (!safe) {
/* 208 */       throw conflictException(conflictDescription, entry1, entry2);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   static IllegalArgumentException conflictException(String conflictDescription, Object entry1, Object entry2) {
/* 214 */     return new IllegalArgumentException("Multiple entries with same " + conflictDescription + ": " + entry1 + " and " + entry2);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static class Builder<K, V>
/*     */   {
/*     */     Comparator<? super V> valueComparator;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     Map.Entry<K, V>[] entries;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     int size;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     boolean entriesUsed;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public Builder() {
/* 258 */       this(4);
/*     */     }
/*     */ 
/*     */     
/*     */     Builder(int initialCapacity) {
/* 263 */       this.entries = (Map.Entry<K, V>[])new Map.Entry[initialCapacity];
/* 264 */       this.size = 0;
/* 265 */       this.entriesUsed = false;
/*     */     }
/*     */     
/*     */     private void ensureCapacity(int minCapacity) {
/* 269 */       if (minCapacity > this.entries.length) {
/* 270 */         this
/* 271 */           .entries = Arrays.<Map.Entry<K, V>>copyOf(this.entries, 
/* 272 */             ImmutableCollection.Builder.expandedCapacity(this.entries.length, minCapacity));
/* 273 */         this.entriesUsed = false;
/*     */       } 
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     @CanIgnoreReturnValue
/*     */     public Builder<K, V> put(K key, V value) {
/* 283 */       ensureCapacity(this.size + 1);
/* 284 */       Map.Entry<K, V> entry = ImmutableMap.entryOf(key, value);
/*     */       
/* 286 */       this.entries[this.size++] = entry;
/* 287 */       return this;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     @CanIgnoreReturnValue
/*     */     public Builder<K, V> put(Map.Entry<? extends K, ? extends V> entry) {
/* 298 */       return put(entry.getKey(), entry.getValue());
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     @CanIgnoreReturnValue
/*     */     public Builder<K, V> putAll(Map<? extends K, ? extends V> map) {
/* 309 */       return putAll(map.entrySet());
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     @CanIgnoreReturnValue
/*     */     @Beta
/*     */     public Builder<K, V> putAll(Iterable<? extends Map.Entry<? extends K, ? extends V>> entries) {
/* 322 */       if (entries instanceof Collection) {
/* 323 */         ensureCapacity(this.size + ((Collection)entries).size());
/*     */       }
/* 325 */       for (Map.Entry<? extends K, ? extends V> entry : entries) {
/* 326 */         put(entry);
/*     */       }
/* 328 */       return this;
/*     */     }
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
/*     */     @CanIgnoreReturnValue
/*     */     @Beta
/*     */     public Builder<K, V> orderEntriesByValue(Comparator<? super V> valueComparator) {
/* 344 */       Preconditions.checkState((this.valueComparator == null), "valueComparator was already set");
/* 345 */       this.valueComparator = (Comparator<? super V>)Preconditions.checkNotNull(valueComparator, "valueComparator");
/* 346 */       return this;
/*     */     }
/*     */     
/*     */     @CanIgnoreReturnValue
/*     */     Builder<K, V> combine(Builder<K, V> other) {
/* 351 */       Preconditions.checkNotNull(other);
/* 352 */       ensureCapacity(this.size + other.size);
/* 353 */       System.arraycopy(other.entries, 0, this.entries, this.size, other.size);
/* 354 */       this.size += other.size;
/* 355 */       return this;
/*     */     }
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
/*     */     public ImmutableMap<K, V> build() {
/* 378 */       if (this.valueComparator != null) {
/* 379 */         if (this.entriesUsed) {
/* 380 */           this.entries = Arrays.<Map.Entry<K, V>>copyOf(this.entries, this.size);
/*     */         }
/* 382 */         Arrays.sort(this.entries, 0, this.size, 
/* 383 */             Ordering.<V>from(this.valueComparator).onResultOf(Maps.valueFunction()));
/*     */       } 
/* 385 */       switch (this.size) {
/*     */         case 0:
/* 387 */           return ImmutableMap.of();
/*     */         case 1:
/* 389 */           return ImmutableMap.of(this.entries[0].getKey(), this.entries[0].getValue());
/*     */       } 
/* 391 */       this.entriesUsed = true;
/* 392 */       return RegularImmutableMap.fromEntryArray(this.size, this.entries);
/*     */     }
/*     */ 
/*     */     
/*     */     @VisibleForTesting
/*     */     ImmutableMap<K, V> buildJdkBacked() {
/* 398 */       Preconditions.checkState((this.valueComparator == null), "buildJdkBacked is only for testing; can't use valueComparator");
/*     */       
/* 400 */       switch (this.size) {
/*     */         case 0:
/* 402 */           return ImmutableMap.of();
/*     */         case 1:
/* 404 */           return ImmutableMap.of(this.entries[0].getKey(), this.entries[0].getValue());
/*     */       } 
/* 406 */       this.entriesUsed = true;
/* 407 */       return JdkBackedImmutableMap.create(this.size, this.entries);
/*     */     }
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
/*     */   public static <K, V> ImmutableMap<K, V> copyOf(Map<? extends K, ? extends V> map) {
/* 425 */     if (map instanceof ImmutableMap && !(map instanceof java.util.SortedMap)) {
/*     */       
/* 427 */       ImmutableMap<K, V> kvMap = (ImmutableMap)map;
/* 428 */       if (!kvMap.isPartialView()) {
/* 429 */         return kvMap;
/*     */       }
/* 431 */     } else if (map instanceof EnumMap) {
/*     */       
/* 433 */       ImmutableMap<K, V> kvMap = (ImmutableMap)copyOfEnumMap((EnumMap)map);
/* 434 */       return kvMap;
/*     */     } 
/* 436 */     return copyOf(map.entrySet());
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
/*     */   @Beta
/*     */   public static <K, V> ImmutableMap<K, V> copyOf(Iterable<? extends Map.Entry<? extends K, ? extends V>> entries) {
/*     */     Map.Entry<K, V> onlyEntry;
/* 451 */     Map.Entry[] arrayOfEntry = Iterables.<Map.Entry>toArray((Iterable)entries, (Map.Entry[])EMPTY_ENTRY_ARRAY);
/* 452 */     switch (arrayOfEntry.length) {
/*     */       case 0:
/* 454 */         return of();
/*     */       case 1:
/* 456 */         onlyEntry = arrayOfEntry[0];
/* 457 */         return of(onlyEntry.getKey(), onlyEntry.getValue());
/*     */     } 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 463 */     return RegularImmutableMap.fromEntries((Map.Entry<K, V>[])arrayOfEntry);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static <K extends Enum<K>, V> ImmutableMap<K, V> copyOfEnumMap(EnumMap<K, ? extends V> original) {
/* 469 */     EnumMap<K, V> copy = new EnumMap<>(original);
/* 470 */     for (Map.Entry<?, ?> entry : copy.entrySet()) {
/* 471 */       CollectPreconditions.checkEntryNotNull(entry.getKey(), entry.getValue());
/*     */     }
/* 473 */     return ImmutableEnumMap.asImmutable(copy); } @LazyInit
/*     */   private transient ImmutableSet<Map.Entry<K, V>> entrySet; @LazyInit
/*     */   @RetainedWith
/* 476 */   private transient ImmutableSet<K> keySet; static final Map.Entry<?, ?>[] EMPTY_ENTRY_ARRAY = (Map.Entry<?, ?>[])new Map.Entry[0]; @LazyInit
/*     */   @RetainedWith
/*     */   private transient ImmutableCollection<V> values; @LazyInit
/*     */   private transient ImmutableSetMultimap<K, V> multimapView;
/*     */   
/*     */   static abstract class IteratorBasedImmutableMap<K, V> extends ImmutableMap<K, V> { Spliterator<Map.Entry<K, V>> entrySpliterator() {
/* 482 */       return Spliterators.spliterator(
/* 483 */           entryIterator(), 
/* 484 */           size(), 1297);
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     ImmutableSet<K> createKeySet() {
/* 490 */       return new ImmutableMapKeySet<>(this);
/*     */     }
/*     */ 
/*     */     
/*     */     ImmutableSet<Map.Entry<K, V>> createEntrySet() {
/*     */       class EntrySetImpl
/*     */         extends ImmutableMapEntrySet<K, V>
/*     */       {
/*     */         ImmutableMap<K, V> map() {
/* 499 */           return ImmutableMap.IteratorBasedImmutableMap.this;
/*     */         }
/*     */ 
/*     */         
/*     */         public UnmodifiableIterator<Map.Entry<K, V>> iterator() {
/* 504 */           return ImmutableMap.IteratorBasedImmutableMap.this.entryIterator();
/*     */         }
/*     */       };
/* 507 */       return new EntrySetImpl();
/*     */     }
/*     */ 
/*     */     
/*     */     ImmutableCollection<V> createValues() {
/* 512 */       return new ImmutableMapValues<>(this);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     abstract UnmodifiableIterator<Map.Entry<K, V>> entryIterator(); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   @CanIgnoreReturnValue
/*     */   public final V put(K k, V v) {
/* 528 */     throw new UnsupportedOperationException();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   @CanIgnoreReturnValue
/*     */   public final V putIfAbsent(K key, V value) {
/* 541 */     throw new UnsupportedOperationException();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   public final boolean replace(K key, V oldValue, V newValue) {
/* 553 */     throw new UnsupportedOperationException();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   public final V replace(K key, V value) {
/* 565 */     throw new UnsupportedOperationException();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   public final V computeIfAbsent(K key, Function<? super K, ? extends V> mappingFunction) {
/* 577 */     throw new UnsupportedOperationException();
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
/*     */   @Deprecated
/*     */   public final V computeIfPresent(K key, BiFunction<? super K, ? super V, ? extends V> remappingFunction) {
/* 590 */     throw new UnsupportedOperationException();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   public final V compute(K key, BiFunction<? super K, ? super V, ? extends V> remappingFunction) {
/* 602 */     throw new UnsupportedOperationException();
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
/*     */   @Deprecated
/*     */   public final V merge(K key, V value, BiFunction<? super V, ? super V, ? extends V> remappingFunction) {
/* 615 */     throw new UnsupportedOperationException();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   public final void putAll(Map<? extends K, ? extends V> map) {
/* 627 */     throw new UnsupportedOperationException();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   public final void replaceAll(BiFunction<? super K, ? super V, ? extends V> function) {
/* 639 */     throw new UnsupportedOperationException();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   public final V remove(Object o) {
/* 651 */     throw new UnsupportedOperationException();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   public final boolean remove(Object key, Object value) {
/* 663 */     throw new UnsupportedOperationException();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   public final void clear() {
/* 675 */     throw new UnsupportedOperationException();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isEmpty() {
/* 680 */     return (size() == 0);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean containsKey(Object key) {
/* 685 */     return (get(key) != null);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean containsValue(Object value) {
/* 690 */     return values().contains(value);
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
/*     */   public final V getOrDefault(Object key, V defaultValue) {
/* 704 */     V result = get(key);
/* 705 */     return (result != null) ? result : defaultValue;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ImmutableSet<Map.Entry<K, V>> entrySet() {
/* 716 */     ImmutableSet<Map.Entry<K, V>> result = this.entrySet;
/* 717 */     return (result == null) ? (this.entrySet = createEntrySet()) : result;
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
/*     */   public ImmutableSet<K> keySet() {
/* 730 */     ImmutableSet<K> result = this.keySet;
/* 731 */     return (result == null) ? (this.keySet = createKeySet()) : result;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   UnmodifiableIterator<K> keyIterator() {
/* 742 */     final UnmodifiableIterator<Map.Entry<K, V>> entryIterator = entrySet().iterator();
/* 743 */     return new UnmodifiableIterator<K>()
/*     */       {
/*     */         public boolean hasNext() {
/* 746 */           return entryIterator.hasNext();
/*     */         }
/*     */ 
/*     */         
/*     */         public K next() {
/* 751 */           return (K)((Map.Entry)entryIterator.next()).getKey();
/*     */         }
/*     */       };
/*     */   }
/*     */   
/*     */   Spliterator<K> keySpliterator() {
/* 757 */     return CollectSpliterators.map(entrySet().spliterator(), Map.Entry::getKey);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ImmutableCollection<V> values() {
/* 768 */     ImmutableCollection<V> result = this.values;
/* 769 */     return (result == null) ? (this.values = createValues()) : result;
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
/*     */   
/*     */   public ImmutableSetMultimap<K, V> asMultimap() {
/* 788 */     if (isEmpty()) {
/* 789 */       return ImmutableSetMultimap.of();
/*     */     }
/* 791 */     ImmutableSetMultimap<K, V> result = this.multimapView;
/* 792 */     return (result == null) ? (this
/*     */       
/* 794 */       .multimapView = new ImmutableSetMultimap<>(new MapViewOfValuesAsSingletonSets(), size(), null)) : result;
/*     */   }
/*     */ 
/*     */   
/*     */   private final class MapViewOfValuesAsSingletonSets
/*     */     extends IteratorBasedImmutableMap<K, ImmutableSet<V>>
/*     */   {
/*     */     private MapViewOfValuesAsSingletonSets() {}
/*     */     
/*     */     public int size() {
/* 804 */       return ImmutableMap.this.size();
/*     */     }
/*     */ 
/*     */     
/*     */     ImmutableSet<K> createKeySet() {
/* 809 */       return ImmutableMap.this.keySet();
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean containsKey(Object key) {
/* 814 */       return ImmutableMap.this.containsKey(key);
/*     */     }
/*     */ 
/*     */     
/*     */     public ImmutableSet<V> get(Object key) {
/* 819 */       V outerValue = (V)ImmutableMap.this.get(key);
/* 820 */       return (outerValue == null) ? null : ImmutableSet.<V>of(outerValue);
/*     */     }
/*     */ 
/*     */     
/*     */     boolean isPartialView() {
/* 825 */       return ImmutableMap.this.isPartialView();
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public int hashCode() {
/* 831 */       return ImmutableMap.this.hashCode();
/*     */     }
/*     */ 
/*     */     
/*     */     boolean isHashCodeFast() {
/* 836 */       return ImmutableMap.this.isHashCodeFast();
/*     */     }
/*     */ 
/*     */     
/*     */     UnmodifiableIterator<Map.Entry<K, ImmutableSet<V>>> entryIterator() {
/* 841 */       final Iterator<Map.Entry<K, V>> backingIterator = ImmutableMap.this.entrySet().iterator();
/* 842 */       return new UnmodifiableIterator<Map.Entry<K, ImmutableSet<V>>>()
/*     */         {
/*     */           public boolean hasNext() {
/* 845 */             return backingIterator.hasNext();
/*     */           }
/*     */ 
/*     */           
/*     */           public Map.Entry<K, ImmutableSet<V>> next() {
/* 850 */             final Map.Entry<K, V> backingEntry = backingIterator.next();
/* 851 */             return (Map.Entry)new AbstractMapEntry<K, ImmutableSet<ImmutableSet<V>>>()
/*     */               {
/*     */                 public K getKey() {
/* 854 */                   return (K)backingEntry.getKey();
/*     */                 }
/*     */ 
/*     */                 
/*     */                 public ImmutableSet<V> getValue() {
/* 859 */                   return ImmutableSet.of((V)backingEntry.getValue());
/*     */                 }
/*     */               };
/*     */           }
/*     */         };
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean equals(Object object) {
/* 869 */     return Maps.equalsImpl(this, object);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 876 */     return Sets.hashCodeImpl(entrySet());
/*     */   }
/*     */   
/*     */   boolean isHashCodeFast() {
/* 880 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 885 */     return Maps.toStringImpl(this);
/*     */   }
/*     */ 
/*     */   
/*     */   static class SerializedForm
/*     */     implements Serializable
/*     */   {
/*     */     private final Object[] keys;
/*     */     
/*     */     private final Object[] values;
/*     */     private static final long serialVersionUID = 0L;
/*     */     
/*     */     SerializedForm(ImmutableMap<?, ?> map) {
/* 898 */       this.keys = new Object[map.size()];
/* 899 */       this.values = new Object[map.size()];
/* 900 */       int i = 0;
/* 901 */       for (UnmodifiableIterator<Map.Entry<?, ?>> unmodifiableIterator = map.entrySet().iterator(); unmodifiableIterator.hasNext(); ) { Map.Entry<?, ?> entry = unmodifiableIterator.next();
/* 902 */         this.keys[i] = entry.getKey();
/* 903 */         this.values[i] = entry.getValue();
/* 904 */         i++; }
/*     */     
/*     */     }
/*     */     
/*     */     Object readResolve() {
/* 909 */       ImmutableMap.Builder<Object, Object> builder = new ImmutableMap.Builder<>(this.keys.length);
/* 910 */       return createMap(builder);
/*     */     }
/*     */     
/*     */     Object createMap(ImmutableMap.Builder<Object, Object> builder) {
/* 914 */       for (int i = 0; i < this.keys.length; i++) {
/* 915 */         builder.put(this.keys[i], this.values[i]);
/*     */       }
/* 917 */       return builder.build();
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   Object writeReplace() {
/* 924 */     return new SerializedForm(this);
/*     */   }
/*     */   
/*     */   public abstract V get(Object paramObject);
/*     */   
/*     */   abstract ImmutableSet<Map.Entry<K, V>> createEntrySet();
/*     */   
/*     */   abstract ImmutableSet<K> createKeySet();
/*     */   
/*     */   abstract ImmutableCollection<V> createValues();
/*     */   
/*     */   abstract boolean isPartialView();
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\google\common\collect\ImmutableMap.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */