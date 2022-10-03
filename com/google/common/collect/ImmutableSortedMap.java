/*     */ package com.google.common.collect;
/*     */ 
/*     */ import com.google.common.annotations.Beta;
/*     */ import com.google.common.annotations.GwtCompatible;
/*     */ import com.google.common.base.Preconditions;
/*     */ import com.google.errorprone.annotations.CanIgnoreReturnValue;
/*     */ import java.util.AbstractMap;
/*     */ import java.util.Arrays;
/*     */ import java.util.Collection;
/*     */ import java.util.Comparator;
/*     */ import java.util.Iterator;
/*     */ import java.util.Map;
/*     */ import java.util.NavigableMap;
/*     */ import java.util.NavigableSet;
/*     */ import java.util.Set;
/*     */ import java.util.SortedMap;
/*     */ import java.util.Spliterator;
/*     */ import java.util.TreeMap;
/*     */ import java.util.function.BiConsumer;
/*     */ import java.util.function.BinaryOperator;
/*     */ import java.util.function.Consumer;
/*     */ import java.util.function.Function;
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
/*     */ @GwtCompatible(serializable = true, emulated = true)
/*     */ public final class ImmutableSortedMap<K, V>
/*     */   extends ImmutableSortedMapFauxverideShim<K, V>
/*     */   implements NavigableMap<K, V>
/*     */ {
/*     */   public static <T, K, V> Collector<T, ?, ImmutableSortedMap<K, V>> toImmutableSortedMap(Comparator<? super K> comparator, Function<? super T, ? extends K> keyFunction, Function<? super T, ? extends V> valueFunction) {
/*  80 */     return CollectCollectors.toImmutableSortedMap(comparator, keyFunction, valueFunction);
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
/*     */   public static <T, K, V> Collector<T, ?, ImmutableSortedMap<K, V>> toImmutableSortedMap(Comparator<? super K> comparator, Function<? super T, ? extends K> keyFunction, Function<? super T, ? extends V> valueFunction, BinaryOperator<V> mergeFunction) {
/*  99 */     Preconditions.checkNotNull(comparator);
/* 100 */     Preconditions.checkNotNull(keyFunction);
/* 101 */     Preconditions.checkNotNull(valueFunction);
/* 102 */     Preconditions.checkNotNull(mergeFunction);
/* 103 */     return Collectors.collectingAndThen(
/* 104 */         Collectors.toMap(keyFunction, valueFunction, mergeFunction, () -> new TreeMap<>(comparator)), ImmutableSortedMap::copyOfSorted);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 113 */   private static final Comparator<Comparable> NATURAL_ORDER = Ordering.natural();
/*     */   
/* 115 */   private static final ImmutableSortedMap<Comparable, Object> NATURAL_EMPTY_MAP = new ImmutableSortedMap(
/*     */       
/* 117 */       ImmutableSortedSet.emptySet(Ordering.natural()), ImmutableList.of());
/*     */   
/*     */   static <K, V> ImmutableSortedMap<K, V> emptyMap(Comparator<? super K> comparator) {
/* 120 */     if (Ordering.<Comparable>natural().equals(comparator)) {
/* 121 */       return of();
/*     */     }
/* 123 */     return new ImmutableSortedMap<>(
/* 124 */         ImmutableSortedSet.emptySet(comparator), ImmutableList.of());
/*     */   }
/*     */   
/*     */   private final transient RegularImmutableSortedSet<K> keySet;
/*     */   private final transient ImmutableList<V> valueList;
/*     */   private transient ImmutableSortedMap<K, V> descendingMap;
/*     */   private static final long serialVersionUID = 0L;
/*     */   
/*     */   public static <K, V> ImmutableSortedMap<K, V> of() {
/* 133 */     return (ImmutableSortedMap)NATURAL_EMPTY_MAP;
/*     */   }
/*     */ 
/*     */   
/*     */   public static <K extends Comparable<? super K>, V> ImmutableSortedMap<K, V> of(K k1, V v1) {
/* 138 */     return of(Ordering.natural(), k1, v1);
/*     */   }
/*     */ 
/*     */   
/*     */   private static <K, V> ImmutableSortedMap<K, V> of(Comparator<? super K> comparator, K k1, V v1) {
/* 143 */     return new ImmutableSortedMap<>(new RegularImmutableSortedSet<>(
/* 144 */           ImmutableList.of(k1), (Comparator<? super K>)Preconditions.checkNotNull(comparator)), 
/* 145 */         ImmutableList.of(v1));
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
/*     */   public static <K extends Comparable<? super K>, V> ImmutableSortedMap<K, V> of(K k1, V v1, K k2, V v2) {
/* 157 */     return ofEntries((Map.Entry<K, V>[])new Map.Entry[] { entryOf(k1, v1), entryOf(k2, v2) });
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
/*     */   public static <K extends Comparable<? super K>, V> ImmutableSortedMap<K, V> of(K k1, V v1, K k2, V v2, K k3, V v3) {
/* 169 */     return ofEntries((Map.Entry<K, V>[])new Map.Entry[] { entryOf(k1, v1), entryOf(k2, v2), entryOf(k3, v3) });
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
/*     */   public static <K extends Comparable<? super K>, V> ImmutableSortedMap<K, V> of(K k1, V v1, K k2, V v2, K k3, V v3, K k4, V v4) {
/* 181 */     return ofEntries((Map.Entry<K, V>[])new Map.Entry[] { entryOf(k1, v1), entryOf(k2, v2), entryOf(k3, v3), entryOf(k4, v4) });
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
/*     */   public static <K extends Comparable<? super K>, V> ImmutableSortedMap<K, V> of(K k1, V v1, K k2, V v2, K k3, V v3, K k4, V v4, K k5, V v5) {
/* 193 */     return ofEntries((Map.Entry<K, V>[])new Map.Entry[] {
/* 194 */           entryOf(k1, v1), entryOf(k2, v2), entryOf(k3, v3), entryOf(k4, v4), entryOf(k5, v5)
/*     */         });
/*     */   }
/*     */   
/*     */   private static <K extends Comparable<? super K>, V> ImmutableSortedMap<K, V> ofEntries(Map.Entry<K, V>... entries) {
/* 199 */     return fromEntries(Ordering.natural(), false, entries, entries.length);
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
/*     */ 
/*     */ 
/*     */   
/*     */   public static <K, V> ImmutableSortedMap<K, V> copyOf(Map<? extends K, ? extends V> map) {
/* 221 */     Ordering<K> naturalOrder = (Ordering)NATURAL_ORDER;
/* 222 */     return copyOfInternal(map, naturalOrder);
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
/*     */   public static <K, V> ImmutableSortedMap<K, V> copyOf(Map<? extends K, ? extends V> map, Comparator<? super K> comparator) {
/* 238 */     return copyOfInternal(map, (Comparator<? super K>)Preconditions.checkNotNull(comparator));
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
/*     */   @Beta
/*     */   public static <K, V> ImmutableSortedMap<K, V> copyOf(Iterable<? extends Map.Entry<? extends K, ? extends V>> entries) {
/* 258 */     Ordering<K> naturalOrder = (Ordering)NATURAL_ORDER;
/* 259 */     return copyOf(entries, naturalOrder);
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
/*     */   @Beta
/*     */   public static <K, V> ImmutableSortedMap<K, V> copyOf(Iterable<? extends Map.Entry<? extends K, ? extends V>> entries, Comparator<? super K> comparator) {
/* 274 */     return fromEntries((Comparator<? super K>)Preconditions.checkNotNull(comparator), false, entries);
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
/*     */   public static <K, V> ImmutableSortedMap<K, V> copyOfSorted(SortedMap<K, ? extends V> map) {
/*     */     Comparator<Comparable> comparator1;
/* 289 */     Comparator<? super K> comparator = map.comparator();
/* 290 */     if (comparator == null)
/*     */     {
/*     */       
/* 293 */       comparator1 = NATURAL_ORDER;
/*     */     }
/* 295 */     if (map instanceof ImmutableSortedMap) {
/*     */ 
/*     */ 
/*     */       
/* 299 */       ImmutableSortedMap<K, V> kvMap = (ImmutableSortedMap)map;
/* 300 */       if (!kvMap.isPartialView()) {
/* 301 */         return kvMap;
/*     */       }
/*     */     } 
/* 304 */     return fromEntries((Comparator)comparator1, true, map.entrySet());
/*     */   }
/*     */ 
/*     */   
/*     */   private static <K, V> ImmutableSortedMap<K, V> copyOfInternal(Map<? extends K, ? extends V> map, Comparator<? super K> comparator) {
/* 309 */     boolean sameComparator = false;
/* 310 */     if (map instanceof SortedMap) {
/* 311 */       SortedMap<?, ?> sortedMap = (SortedMap<?, ?>)map;
/* 312 */       Comparator<?> comparator2 = sortedMap.comparator();
/*     */       
/* 314 */       sameComparator = (comparator2 == null) ? ((comparator == NATURAL_ORDER)) : comparator.equals(comparator2);
/*     */     } 
/*     */     
/* 317 */     if (sameComparator && map instanceof ImmutableSortedMap) {
/*     */ 
/*     */ 
/*     */       
/* 321 */       ImmutableSortedMap<K, V> kvMap = (ImmutableSortedMap)map;
/* 322 */       if (!kvMap.isPartialView()) {
/* 323 */         return kvMap;
/*     */       }
/*     */     } 
/* 326 */     return fromEntries(comparator, sameComparator, map.entrySet());
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
/*     */   private static <K, V> ImmutableSortedMap<K, V> fromEntries(Comparator<? super K> comparator, boolean sameComparator, Iterable<? extends Map.Entry<? extends K, ? extends V>> entries) {
/* 341 */     Map.Entry[] arrayOfEntry = Iterables.<Map.Entry>toArray((Iterable)entries, (Map.Entry[])EMPTY_ENTRY_ARRAY);
/* 342 */     return fromEntries(comparator, sameComparator, (Map.Entry<K, V>[])arrayOfEntry, arrayOfEntry.length);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static <K, V> ImmutableSortedMap<K, V> fromEntries(final Comparator<? super K> comparator, boolean sameComparator, Map.Entry<K, V>[] entryArray, int size) {
/* 350 */     switch (size) {
/*     */       case 0:
/* 352 */         return emptyMap(comparator);
/*     */       case 1:
/* 354 */         return of(comparator, entryArray[0]
/* 355 */             .getKey(), entryArray[0].getValue());
/*     */     } 
/* 357 */     Object[] keys = new Object[size];
/* 358 */     Object[] values = new Object[size];
/* 359 */     if (sameComparator) {
/*     */       
/* 361 */       for (int i = 0; i < size; i++) {
/* 362 */         Object key = entryArray[i].getKey();
/* 363 */         Object value = entryArray[i].getValue();
/* 364 */         CollectPreconditions.checkEntryNotNull(key, value);
/* 365 */         keys[i] = key;
/* 366 */         values[i] = value;
/*     */       }
/*     */     
/*     */     }
/*     */     else {
/*     */       
/* 372 */       Arrays.sort(entryArray, 0, size, new Comparator<Map.Entry<K, V>>()
/*     */           {
/*     */ 
/*     */ 
/*     */             
/*     */             public int compare(Map.Entry<K, V> e1, Map.Entry<K, V> e2)
/*     */             {
/* 379 */               return comparator.compare(e1.getKey(), e2.getKey());
/*     */             }
/*     */           });
/* 382 */       K prevKey = entryArray[0].getKey();
/* 383 */       keys[0] = prevKey;
/* 384 */       values[0] = entryArray[0].getValue();
/* 385 */       CollectPreconditions.checkEntryNotNull(keys[0], values[0]);
/* 386 */       for (int i = 1; i < size; i++) {
/* 387 */         K key = entryArray[i].getKey();
/* 388 */         V value = entryArray[i].getValue();
/* 389 */         CollectPreconditions.checkEntryNotNull(key, value);
/* 390 */         keys[i] = key;
/* 391 */         values[i] = value;
/* 392 */         checkNoConflict(
/* 393 */             (comparator.compare(prevKey, key) != 0), "key", entryArray[i - 1], entryArray[i]);
/* 394 */         prevKey = key;
/*     */       } 
/*     */     } 
/* 397 */     return new ImmutableSortedMap<>(new RegularImmutableSortedSet<>(new RegularImmutableList<>(keys), comparator), new RegularImmutableList<>(values));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <K extends Comparable<?>, V> Builder<K, V> naturalOrder() {
/* 408 */     return new Builder<>(Ordering.natural());
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
/*     */   public static <K, V> Builder<K, V> orderedBy(Comparator<K> comparator) {
/* 420 */     return new Builder<>(comparator);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <K extends Comparable<?>, V> Builder<K, V> reverseOrder() {
/* 428 */     return new Builder<>(Ordering.<Comparable>natural().reverse());
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
/*     */   public static class Builder<K, V>
/*     */     extends ImmutableMap.Builder<K, V>
/*     */   {
/*     */     private final Comparator<? super K> comparator;
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
/*     */     public Builder(Comparator<? super K> comparator) {
/* 461 */       this.comparator = (Comparator<? super K>)Preconditions.checkNotNull(comparator);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     @CanIgnoreReturnValue
/*     */     public Builder<K, V> put(K key, V value) {
/* 472 */       super.put(key, value);
/* 473 */       return this;
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
/*     */     @CanIgnoreReturnValue
/*     */     public Builder<K, V> put(Map.Entry<? extends K, ? extends V> entry) {
/* 486 */       super.put(entry);
/* 487 */       return this;
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
/*     */     @CanIgnoreReturnValue
/*     */     public Builder<K, V> putAll(Map<? extends K, ? extends V> map) {
/* 500 */       super.putAll(map);
/* 501 */       return this;
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
/*     */     @CanIgnoreReturnValue
/*     */     @Beta
/*     */     public Builder<K, V> putAll(Iterable<? extends Map.Entry<? extends K, ? extends V>> entries) {
/* 516 */       super.putAll(entries);
/* 517 */       return this;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     @Deprecated
/*     */     @CanIgnoreReturnValue
/*     */     @Beta
/*     */     public Builder<K, V> orderEntriesByValue(Comparator<? super V> valueComparator) {
/* 531 */       throw new UnsupportedOperationException("Not available on ImmutableSortedMap.Builder");
/*     */     }
/*     */ 
/*     */     
/*     */     Builder<K, V> combine(ImmutableMap.Builder<K, V> other) {
/* 536 */       super.combine(other);
/* 537 */       return this;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public ImmutableSortedMap<K, V> build() {
/* 548 */       switch (this.size) {
/*     */         case 0:
/* 550 */           return ImmutableSortedMap.emptyMap(this.comparator);
/*     */         case 1:
/* 552 */           return ImmutableSortedMap.of(this.comparator, this.entries[0].getKey(), this.entries[0].getValue());
/*     */       } 
/* 554 */       return ImmutableSortedMap.fromEntries(this.comparator, false, this.entries, this.size);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   ImmutableSortedMap(RegularImmutableSortedSet<K> keySet, ImmutableList<V> valueList) {
/* 564 */     this(keySet, valueList, (ImmutableSortedMap<K, V>)null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   ImmutableSortedMap(RegularImmutableSortedSet<K> keySet, ImmutableList<V> valueList, ImmutableSortedMap<K, V> descendingMap) {
/* 571 */     this.keySet = keySet;
/* 572 */     this.valueList = valueList;
/* 573 */     this.descendingMap = descendingMap;
/*     */   }
/*     */ 
/*     */   
/*     */   public int size() {
/* 578 */     return this.valueList.size();
/*     */   }
/*     */ 
/*     */   
/*     */   public void forEach(BiConsumer<? super K, ? super V> action) {
/* 583 */     Preconditions.checkNotNull(action);
/* 584 */     ImmutableList<K> keyList = this.keySet.asList();
/* 585 */     for (int i = 0; i < size(); i++) {
/* 586 */       action.accept(keyList.get(i), this.valueList.get(i));
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public V get(Object key) {
/* 592 */     int index = this.keySet.indexOf(key);
/* 593 */     return (index == -1) ? null : this.valueList.get(index);
/*     */   }
/*     */ 
/*     */   
/*     */   boolean isPartialView() {
/* 598 */     return (this.keySet.isPartialView() || this.valueList.isPartialView());
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public ImmutableSet<Map.Entry<K, V>> entrySet() {
/* 604 */     return super.entrySet();
/*     */   }
/*     */ 
/*     */   
/*     */   ImmutableSet<Map.Entry<K, V>> createEntrySet() {
/*     */     class EntrySet
/*     */       extends ImmutableMapEntrySet<K, V>
/*     */     {
/*     */       public UnmodifiableIterator<Map.Entry<K, V>> iterator() {
/* 613 */         return asList().iterator();
/*     */       }
/*     */ 
/*     */       
/*     */       public Spliterator<Map.Entry<K, V>> spliterator() {
/* 618 */         return asList().spliterator();
/*     */       }
/*     */ 
/*     */       
/*     */       public void forEach(Consumer<? super Map.Entry<K, V>> action) {
/* 623 */         asList().forEach(action);
/*     */       }
/*     */ 
/*     */       
/*     */       ImmutableList<Map.Entry<K, V>> createAsList() {
/* 628 */         return new ImmutableAsList<Map.Entry<K, V>>()
/*     */           {
/*     */             public Map.Entry<K, V> get(int index) {
/* 631 */               return new AbstractMap.SimpleImmutableEntry<>(ImmutableSortedMap.this
/* 632 */                   .keySet.asList().get(index), (V)ImmutableSortedMap.this.valueList.get(index));
/*     */             }
/*     */ 
/*     */             
/*     */             public Spliterator<Map.Entry<K, V>> spliterator() {
/* 637 */               return CollectSpliterators.indexed(
/* 638 */                   size(), 1297, this::get);
/*     */             }
/*     */ 
/*     */             
/*     */             ImmutableCollection<Map.Entry<K, V>> delegateCollection() {
/* 643 */               return ImmutableSortedMap.EntrySet.this;
/*     */             }
/*     */           };
/*     */       }
/*     */ 
/*     */       
/*     */       ImmutableMap<K, V> map() {
/* 650 */         return ImmutableSortedMap.this;
/*     */       }
/*     */     };
/* 653 */     return isEmpty() ? ImmutableSet.<Map.Entry<K, V>>of() : new EntrySet();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public ImmutableSortedSet<K> keySet() {
/* 659 */     return this.keySet;
/*     */   }
/*     */ 
/*     */   
/*     */   ImmutableSet<K> createKeySet() {
/* 664 */     throw new AssertionError("should never be called");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ImmutableCollection<V> values() {
/* 673 */     return this.valueList;
/*     */   }
/*     */ 
/*     */   
/*     */   ImmutableCollection<V> createValues() {
/* 678 */     throw new AssertionError("should never be called");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Comparator<? super K> comparator() {
/* 688 */     return keySet().comparator();
/*     */   }
/*     */ 
/*     */   
/*     */   public K firstKey() {
/* 693 */     return keySet().first();
/*     */   }
/*     */ 
/*     */   
/*     */   public K lastKey() {
/* 698 */     return keySet().last();
/*     */   }
/*     */   
/*     */   private ImmutableSortedMap<K, V> getSubMap(int fromIndex, int toIndex) {
/* 702 */     if (fromIndex == 0 && toIndex == size())
/* 703 */       return this; 
/* 704 */     if (fromIndex == toIndex) {
/* 705 */       return emptyMap(comparator());
/*     */     }
/* 707 */     return new ImmutableSortedMap(this.keySet
/* 708 */         .getSubSet(fromIndex, toIndex), this.valueList.subList(fromIndex, toIndex));
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
/*     */   public ImmutableSortedMap<K, V> headMap(K toKey) {
/* 723 */     return headMap(toKey, false);
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
/*     */   public ImmutableSortedMap<K, V> headMap(K toKey, boolean inclusive) {
/* 739 */     return getSubMap(0, this.keySet.headIndex((K)Preconditions.checkNotNull(toKey), inclusive));
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
/*     */   public ImmutableSortedMap<K, V> subMap(K fromKey, K toKey) {
/* 754 */     return subMap(fromKey, true, toKey, false);
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
/*     */   public ImmutableSortedMap<K, V> subMap(K fromKey, boolean fromInclusive, K toKey, boolean toInclusive) {
/* 773 */     Preconditions.checkNotNull(fromKey);
/* 774 */     Preconditions.checkNotNull(toKey);
/* 775 */     Preconditions.checkArgument(
/* 776 */         (comparator().compare(fromKey, toKey) <= 0), "expected fromKey <= toKey but %s > %s", fromKey, toKey);
/*     */ 
/*     */ 
/*     */     
/* 780 */     return headMap(toKey, toInclusive).tailMap(fromKey, fromInclusive);
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
/*     */   public ImmutableSortedMap<K, V> tailMap(K fromKey) {
/* 794 */     return tailMap(fromKey, true);
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
/*     */   public ImmutableSortedMap<K, V> tailMap(K fromKey, boolean inclusive) {
/* 810 */     return getSubMap(this.keySet.tailIndex((K)Preconditions.checkNotNull(fromKey), inclusive), size());
/*     */   }
/*     */ 
/*     */   
/*     */   public Map.Entry<K, V> lowerEntry(K key) {
/* 815 */     return headMap(key, false).lastEntry();
/*     */   }
/*     */ 
/*     */   
/*     */   public K lowerKey(K key) {
/* 820 */     return Maps.keyOrNull(lowerEntry(key));
/*     */   }
/*     */ 
/*     */   
/*     */   public Map.Entry<K, V> floorEntry(K key) {
/* 825 */     return headMap(key, true).lastEntry();
/*     */   }
/*     */ 
/*     */   
/*     */   public K floorKey(K key) {
/* 830 */     return Maps.keyOrNull(floorEntry(key));
/*     */   }
/*     */ 
/*     */   
/*     */   public Map.Entry<K, V> ceilingEntry(K key) {
/* 835 */     return tailMap(key, true).firstEntry();
/*     */   }
/*     */ 
/*     */   
/*     */   public K ceilingKey(K key) {
/* 840 */     return Maps.keyOrNull(ceilingEntry(key));
/*     */   }
/*     */ 
/*     */   
/*     */   public Map.Entry<K, V> higherEntry(K key) {
/* 845 */     return tailMap(key, false).firstEntry();
/*     */   }
/*     */ 
/*     */   
/*     */   public K higherKey(K key) {
/* 850 */     return Maps.keyOrNull(higherEntry(key));
/*     */   }
/*     */ 
/*     */   
/*     */   public Map.Entry<K, V> firstEntry() {
/* 855 */     return isEmpty() ? null : entrySet().asList().get(0);
/*     */   }
/*     */ 
/*     */   
/*     */   public Map.Entry<K, V> lastEntry() {
/* 860 */     return isEmpty() ? null : entrySet().asList().get(size() - 1);
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
/*     */   public final Map.Entry<K, V> pollFirstEntry() {
/* 873 */     throw new UnsupportedOperationException();
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
/*     */   public final Map.Entry<K, V> pollLastEntry() {
/* 886 */     throw new UnsupportedOperationException();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ImmutableSortedMap<K, V> descendingMap() {
/* 893 */     ImmutableSortedMap<K, V> result = this.descendingMap;
/* 894 */     if (result == null) {
/* 895 */       if (isEmpty()) {
/* 896 */         return result = emptyMap(Ordering.from(comparator()).reverse());
/*     */       }
/* 898 */       return 
/*     */         
/* 900 */         result = new ImmutableSortedMap((RegularImmutableSortedSet<K>)this.keySet.descendingSet(), this.valueList.reverse(), this);
/*     */     } 
/*     */     
/* 903 */     return result;
/*     */   }
/*     */ 
/*     */   
/*     */   public ImmutableSortedSet<K> navigableKeySet() {
/* 908 */     return this.keySet;
/*     */   }
/*     */ 
/*     */   
/*     */   public ImmutableSortedSet<K> descendingKeySet() {
/* 913 */     return this.keySet.descendingSet();
/*     */   }
/*     */ 
/*     */   
/*     */   private static class SerializedForm
/*     */     extends ImmutableMap.SerializedForm
/*     */   {
/*     */     private final Comparator<Object> comparator;
/*     */     
/*     */     private static final long serialVersionUID = 0L;
/*     */ 
/*     */     
/*     */     SerializedForm(ImmutableSortedMap<?, ?> sortedMap) {
/* 926 */       super(sortedMap);
/* 927 */       this.comparator = (Comparator)sortedMap.comparator();
/*     */     }
/*     */ 
/*     */     
/*     */     Object readResolve() {
/* 932 */       ImmutableSortedMap.Builder<Object, Object> builder = new ImmutableSortedMap.Builder<>(this.comparator);
/* 933 */       return createMap(builder);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   Object writeReplace() {
/* 941 */     return new SerializedForm(this);
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\google\common\collect\ImmutableSortedMap.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */