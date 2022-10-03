/*      */ package com.google.common.collect;
/*      */ 
/*      */ import com.google.common.annotations.Beta;
/*      */ import com.google.common.annotations.GwtCompatible;
/*      */ import com.google.common.annotations.GwtIncompatible;
/*      */ import com.google.common.base.Function;
/*      */ import com.google.common.base.Preconditions;
/*      */ import com.google.common.base.Predicate;
/*      */ import com.google.common.base.Predicates;
/*      */ import com.google.common.base.Supplier;
/*      */ import com.google.errorprone.annotations.CanIgnoreReturnValue;
/*      */ import com.google.j2objc.annotations.Weak;
/*      */ import java.io.IOException;
/*      */ import java.io.ObjectInputStream;
/*      */ import java.io.ObjectOutputStream;
/*      */ import java.io.Serializable;
/*      */ import java.util.AbstractCollection;
/*      */ import java.util.Collection;
/*      */ import java.util.Collections;
/*      */ import java.util.Comparator;
/*      */ import java.util.HashSet;
/*      */ import java.util.Iterator;
/*      */ import java.util.List;
/*      */ import java.util.Map;
/*      */ import java.util.NavigableSet;
/*      */ import java.util.NoSuchElementException;
/*      */ import java.util.Set;
/*      */ import java.util.SortedSet;
/*      */ import java.util.Spliterator;
/*      */ import java.util.function.Consumer;
/*      */ import java.util.function.Function;
/*      */ import java.util.function.Supplier;
/*      */ import java.util.stream.Collector;
/*      */ import java.util.stream.Stream;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ @GwtCompatible(emulated = true)
/*      */ public final class Multimaps
/*      */ {
/*      */   @Beta
/*      */   public static <T, K, V, M extends Multimap<K, V>> Collector<T, ?, M> toMultimap(Function<? super T, ? extends K> keyFunction, Function<? super T, ? extends V> valueFunction, Supplier<M> multimapSupplier) {
/*  112 */     Preconditions.checkNotNull(keyFunction);
/*  113 */     Preconditions.checkNotNull(valueFunction);
/*  114 */     Preconditions.checkNotNull(multimapSupplier);
/*  115 */     return (Collector)Collector.of(multimapSupplier, (multimap, input) -> multimap.put(keyFunction.apply(input), valueFunction.apply(input)), (multimap1, multimap2) -> { multimap1.putAll(multimap2); return multimap1; }new Collector.Characteristics[0]);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Beta
/*      */   public static <T, K, V, M extends Multimap<K, V>> Collector<T, ?, M> flatteningToMultimap(Function<? super T, ? extends K> keyFunction, Function<? super T, ? extends Stream<? extends V>> valueFunction, Supplier<M> multimapSupplier) {
/*  162 */     Preconditions.checkNotNull(keyFunction);
/*  163 */     Preconditions.checkNotNull(valueFunction);
/*  164 */     Preconditions.checkNotNull(multimapSupplier);
/*  165 */     return (Collector)Collector.of(multimapSupplier, (multimap, input) -> { K key = keyFunction.apply(input); Collection<V> valuesForKey = multimap.get(key); ((Stream)valueFunction.apply(input)).forEachOrdered(valuesForKey::add); }(multimap1, multimap2) -> { multimap1.putAll(multimap2); return multimap1; }new Collector.Characteristics[0]);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static <K, V> Multimap<K, V> newMultimap(Map<K, Collection<V>> map, Supplier<? extends Collection<V>> factory) {
/*  216 */     return new CustomMultimap<>(map, factory);
/*      */   }
/*      */   private static class CustomMultimap<K, V> extends AbstractMapBasedMultimap<K, V> { transient Supplier<? extends Collection<V>> factory;
/*      */     @GwtIncompatible
/*      */     private static final long serialVersionUID = 0L;
/*      */     
/*      */     CustomMultimap(Map<K, Collection<V>> map, Supplier<? extends Collection<V>> factory) {
/*  223 */       super(map);
/*  224 */       this.factory = (Supplier<? extends Collection<V>>)Preconditions.checkNotNull(factory);
/*      */     }
/*      */ 
/*      */     
/*      */     Set<K> createKeySet() {
/*  229 */       return createMaybeNavigableKeySet();
/*      */     }
/*      */ 
/*      */     
/*      */     Map<K, Collection<V>> createAsMap() {
/*  234 */       return createMaybeNavigableAsMap();
/*      */     }
/*      */ 
/*      */     
/*      */     protected Collection<V> createCollection() {
/*  239 */       return (Collection<V>)this.factory.get();
/*      */     }
/*      */ 
/*      */     
/*      */     <E> Collection<E> unmodifiableCollectionSubclass(Collection<E> collection) {
/*  244 */       if (collection instanceof NavigableSet)
/*  245 */         return Sets.unmodifiableNavigableSet((NavigableSet<E>)collection); 
/*  246 */       if (collection instanceof SortedSet)
/*  247 */         return Collections.unmodifiableSortedSet((SortedSet<E>)collection); 
/*  248 */       if (collection instanceof Set)
/*  249 */         return Collections.unmodifiableSet((Set<? extends E>)collection); 
/*  250 */       if (collection instanceof List) {
/*  251 */         return Collections.unmodifiableList((List<? extends E>)collection);
/*      */       }
/*  253 */       return Collections.unmodifiableCollection(collection);
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     Collection<V> wrapCollection(K key, Collection<V> collection) {
/*  259 */       if (collection instanceof List)
/*  260 */         return wrapList(key, (List<V>)collection, null); 
/*  261 */       if (collection instanceof NavigableSet)
/*  262 */         return new AbstractMapBasedMultimap.WrappedNavigableSet(this, key, (NavigableSet<V>)collection, null); 
/*  263 */       if (collection instanceof SortedSet)
/*  264 */         return new AbstractMapBasedMultimap.WrappedSortedSet(this, key, (SortedSet<V>)collection, null); 
/*  265 */       if (collection instanceof Set) {
/*  266 */         return new AbstractMapBasedMultimap.WrappedSet(this, key, (Set<V>)collection);
/*      */       }
/*  268 */       return new AbstractMapBasedMultimap.WrappedCollection(this, key, collection, null);
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     @GwtIncompatible
/*      */     private void writeObject(ObjectOutputStream stream) throws IOException {
/*  278 */       stream.defaultWriteObject();
/*  279 */       stream.writeObject(this.factory);
/*  280 */       stream.writeObject(backingMap());
/*      */     }
/*      */ 
/*      */     
/*      */     @GwtIncompatible
/*      */     private void readObject(ObjectInputStream stream) throws IOException, ClassNotFoundException {
/*  286 */       stream.defaultReadObject();
/*  287 */       this.factory = (Supplier<? extends Collection<V>>)stream.readObject();
/*  288 */       Map<K, Collection<V>> map = (Map<K, Collection<V>>)stream.readObject();
/*  289 */       setMap(map);
/*      */     } }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static <K, V> ListMultimap<K, V> newListMultimap(Map<K, Collection<V>> map, Supplier<? extends List<V>> factory) {
/*  328 */     return new CustomListMultimap<>(map, factory);
/*      */   }
/*      */   private static class CustomListMultimap<K, V> extends AbstractListMultimap<K, V> { transient Supplier<? extends List<V>> factory;
/*      */     @GwtIncompatible
/*      */     private static final long serialVersionUID = 0L;
/*      */     
/*      */     CustomListMultimap(Map<K, Collection<V>> map, Supplier<? extends List<V>> factory) {
/*  335 */       super(map);
/*  336 */       this.factory = (Supplier<? extends List<V>>)Preconditions.checkNotNull(factory);
/*      */     }
/*      */ 
/*      */     
/*      */     Set<K> createKeySet() {
/*  341 */       return createMaybeNavigableKeySet();
/*      */     }
/*      */ 
/*      */     
/*      */     Map<K, Collection<V>> createAsMap() {
/*  346 */       return createMaybeNavigableAsMap();
/*      */     }
/*      */ 
/*      */     
/*      */     protected List<V> createCollection() {
/*  351 */       return (List<V>)this.factory.get();
/*      */     }
/*      */ 
/*      */     
/*      */     @GwtIncompatible
/*      */     private void writeObject(ObjectOutputStream stream) throws IOException {
/*  357 */       stream.defaultWriteObject();
/*  358 */       stream.writeObject(this.factory);
/*  359 */       stream.writeObject(backingMap());
/*      */     }
/*      */ 
/*      */     
/*      */     @GwtIncompatible
/*      */     private void readObject(ObjectInputStream stream) throws IOException, ClassNotFoundException {
/*  365 */       stream.defaultReadObject();
/*  366 */       this.factory = (Supplier<? extends List<V>>)stream.readObject();
/*  367 */       Map<K, Collection<V>> map = (Map<K, Collection<V>>)stream.readObject();
/*  368 */       setMap(map);
/*      */     } }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static <K, V> SetMultimap<K, V> newSetMultimap(Map<K, Collection<V>> map, Supplier<? extends Set<V>> factory) {
/*  406 */     return new CustomSetMultimap<>(map, factory);
/*      */   }
/*      */   private static class CustomSetMultimap<K, V> extends AbstractSetMultimap<K, V> { transient Supplier<? extends Set<V>> factory;
/*      */     @GwtIncompatible
/*      */     private static final long serialVersionUID = 0L;
/*      */     
/*      */     CustomSetMultimap(Map<K, Collection<V>> map, Supplier<? extends Set<V>> factory) {
/*  413 */       super(map);
/*  414 */       this.factory = (Supplier<? extends Set<V>>)Preconditions.checkNotNull(factory);
/*      */     }
/*      */ 
/*      */     
/*      */     Set<K> createKeySet() {
/*  419 */       return createMaybeNavigableKeySet();
/*      */     }
/*      */ 
/*      */     
/*      */     Map<K, Collection<V>> createAsMap() {
/*  424 */       return createMaybeNavigableAsMap();
/*      */     }
/*      */ 
/*      */     
/*      */     protected Set<V> createCollection() {
/*  429 */       return (Set<V>)this.factory.get();
/*      */     }
/*      */ 
/*      */     
/*      */     <E> Collection<E> unmodifiableCollectionSubclass(Collection<E> collection) {
/*  434 */       if (collection instanceof NavigableSet)
/*  435 */         return Sets.unmodifiableNavigableSet((NavigableSet<E>)collection); 
/*  436 */       if (collection instanceof SortedSet) {
/*  437 */         return Collections.unmodifiableSortedSet((SortedSet<E>)collection);
/*      */       }
/*  439 */       return Collections.unmodifiableSet((Set<? extends E>)collection);
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     Collection<V> wrapCollection(K key, Collection<V> collection) {
/*  445 */       if (collection instanceof NavigableSet)
/*  446 */         return new AbstractMapBasedMultimap.WrappedNavigableSet(this, key, (NavigableSet<V>)collection, null); 
/*  447 */       if (collection instanceof SortedSet) {
/*  448 */         return new AbstractMapBasedMultimap.WrappedSortedSet(this, key, (SortedSet<V>)collection, null);
/*      */       }
/*  450 */       return new AbstractMapBasedMultimap.WrappedSet(this, key, (Set<V>)collection);
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     @GwtIncompatible
/*      */     private void writeObject(ObjectOutputStream stream) throws IOException {
/*  457 */       stream.defaultWriteObject();
/*  458 */       stream.writeObject(this.factory);
/*  459 */       stream.writeObject(backingMap());
/*      */     }
/*      */ 
/*      */     
/*      */     @GwtIncompatible
/*      */     private void readObject(ObjectInputStream stream) throws IOException, ClassNotFoundException {
/*  465 */       stream.defaultReadObject();
/*  466 */       this.factory = (Supplier<? extends Set<V>>)stream.readObject();
/*  467 */       Map<K, Collection<V>> map = (Map<K, Collection<V>>)stream.readObject();
/*  468 */       setMap(map);
/*      */     } }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static <K, V> SortedSetMultimap<K, V> newSortedSetMultimap(Map<K, Collection<V>> map, Supplier<? extends SortedSet<V>> factory) {
/*  506 */     return new CustomSortedSetMultimap<>(map, factory);
/*      */   }
/*      */   private static class CustomSortedSetMultimap<K, V> extends AbstractSortedSetMultimap<K, V> { transient Supplier<? extends SortedSet<V>> factory;
/*      */     transient Comparator<? super V> valueComparator;
/*      */     @GwtIncompatible
/*      */     private static final long serialVersionUID = 0L;
/*      */     
/*      */     CustomSortedSetMultimap(Map<K, Collection<V>> map, Supplier<? extends SortedSet<V>> factory) {
/*  514 */       super(map);
/*  515 */       this.factory = (Supplier<? extends SortedSet<V>>)Preconditions.checkNotNull(factory);
/*  516 */       this.valueComparator = ((SortedSet<V>)factory.get()).comparator();
/*      */     }
/*      */ 
/*      */     
/*      */     Set<K> createKeySet() {
/*  521 */       return createMaybeNavigableKeySet();
/*      */     }
/*      */ 
/*      */     
/*      */     Map<K, Collection<V>> createAsMap() {
/*  526 */       return createMaybeNavigableAsMap();
/*      */     }
/*      */ 
/*      */     
/*      */     protected SortedSet<V> createCollection() {
/*  531 */       return (SortedSet<V>)this.factory.get();
/*      */     }
/*      */ 
/*      */     
/*      */     public Comparator<? super V> valueComparator() {
/*  536 */       return this.valueComparator;
/*      */     }
/*      */ 
/*      */     
/*      */     @GwtIncompatible
/*      */     private void writeObject(ObjectOutputStream stream) throws IOException {
/*  542 */       stream.defaultWriteObject();
/*  543 */       stream.writeObject(this.factory);
/*  544 */       stream.writeObject(backingMap());
/*      */     }
/*      */ 
/*      */     
/*      */     @GwtIncompatible
/*      */     private void readObject(ObjectInputStream stream) throws IOException, ClassNotFoundException {
/*  550 */       stream.defaultReadObject();
/*  551 */       this.factory = (Supplier<? extends SortedSet<V>>)stream.readObject();
/*  552 */       this.valueComparator = ((SortedSet<V>)this.factory.get()).comparator();
/*  553 */       Map<K, Collection<V>> map = (Map<K, Collection<V>>)stream.readObject();
/*  554 */       setMap(map);
/*      */     } }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @CanIgnoreReturnValue
/*      */   public static <K, V, M extends Multimap<K, V>> M invertFrom(Multimap<? extends V, ? extends K> source, M dest) {
/*  575 */     Preconditions.checkNotNull(dest);
/*  576 */     for (Map.Entry<? extends V, ? extends K> entry : source.entries()) {
/*  577 */       dest.put(entry.getValue(), entry.getKey());
/*      */     }
/*  579 */     return dest;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static <K, V> Multimap<K, V> synchronizedMultimap(Multimap<K, V> multimap) {
/*  615 */     return Synchronized.multimap(multimap, null);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static <K, V> Multimap<K, V> unmodifiableMultimap(Multimap<K, V> delegate) {
/*  630 */     if (delegate instanceof UnmodifiableMultimap || delegate instanceof ImmutableMultimap) {
/*  631 */       return delegate;
/*      */     }
/*  633 */     return new UnmodifiableMultimap<>(delegate);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Deprecated
/*      */   public static <K, V> Multimap<K, V> unmodifiableMultimap(ImmutableMultimap<K, V> delegate) {
/*  644 */     return (Multimap<K, V>)Preconditions.checkNotNull(delegate);
/*      */   }
/*      */   
/*      */   private static class UnmodifiableMultimap<K, V> extends ForwardingMultimap<K, V> implements Serializable {
/*      */     final Multimap<K, V> delegate;
/*      */     transient Collection<Map.Entry<K, V>> entries;
/*      */     transient Multiset<K> keys;
/*      */     transient Set<K> keySet;
/*      */     transient Collection<V> values;
/*      */     transient Map<K, Collection<V>> map;
/*      */     private static final long serialVersionUID = 0L;
/*      */     
/*      */     UnmodifiableMultimap(Multimap<K, V> delegate) {
/*  657 */       this.delegate = (Multimap<K, V>)Preconditions.checkNotNull(delegate);
/*      */     }
/*      */ 
/*      */     
/*      */     protected Multimap<K, V> delegate() {
/*  662 */       return this.delegate;
/*      */     }
/*      */ 
/*      */     
/*      */     public void clear() {
/*  667 */       throw new UnsupportedOperationException();
/*      */     }
/*      */ 
/*      */     
/*      */     public Map<K, Collection<V>> asMap() {
/*  672 */       Map<K, Collection<V>> result = this.map;
/*  673 */       if (result == null)
/*      */       {
/*      */         
/*  676 */         result = this.map = Collections.<K, V>unmodifiableMap(
/*  677 */             Maps.transformValues(this.delegate
/*  678 */               .asMap(), new Function<Collection<V>, Collection<V>>()
/*      */               {
/*      */                 public Collection<V> apply(Collection<V> collection)
/*      */                 {
/*  682 */                   return Multimaps.unmodifiableValueCollection(collection);
/*      */                 }
/*      */               }));
/*      */       }
/*  686 */       return result;
/*      */     }
/*      */ 
/*      */     
/*      */     public Collection<Map.Entry<K, V>> entries() {
/*  691 */       Collection<Map.Entry<K, V>> result = this.entries;
/*  692 */       if (result == null) {
/*  693 */         this.entries = result = Multimaps.unmodifiableEntries(this.delegate.entries());
/*      */       }
/*  695 */       return result;
/*      */     }
/*      */ 
/*      */     
/*      */     public Collection<V> get(K key) {
/*  700 */       return Multimaps.unmodifiableValueCollection(this.delegate.get(key));
/*      */     }
/*      */ 
/*      */     
/*      */     public Multiset<K> keys() {
/*  705 */       Multiset<K> result = this.keys;
/*  706 */       if (result == null) {
/*  707 */         this.keys = result = Multisets.unmodifiableMultiset(this.delegate.keys());
/*      */       }
/*  709 */       return result;
/*      */     }
/*      */ 
/*      */     
/*      */     public Set<K> keySet() {
/*  714 */       Set<K> result = this.keySet;
/*  715 */       if (result == null) {
/*  716 */         this.keySet = result = Collections.unmodifiableSet(this.delegate.keySet());
/*      */       }
/*  718 */       return result;
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean put(K key, V value) {
/*  723 */       throw new UnsupportedOperationException();
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean putAll(K key, Iterable<? extends V> values) {
/*  728 */       throw new UnsupportedOperationException();
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean putAll(Multimap<? extends K, ? extends V> multimap) {
/*  733 */       throw new UnsupportedOperationException();
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean remove(Object key, Object value) {
/*  738 */       throw new UnsupportedOperationException();
/*      */     }
/*      */ 
/*      */     
/*      */     public Collection<V> removeAll(Object key) {
/*  743 */       throw new UnsupportedOperationException();
/*      */     }
/*      */ 
/*      */     
/*      */     public Collection<V> replaceValues(K key, Iterable<? extends V> values) {
/*  748 */       throw new UnsupportedOperationException();
/*      */     }
/*      */ 
/*      */     
/*      */     public Collection<V> values() {
/*  753 */       Collection<V> result = this.values;
/*  754 */       if (result == null) {
/*  755 */         this.values = result = Collections.unmodifiableCollection(this.delegate.values());
/*      */       }
/*  757 */       return result;
/*      */     }
/*      */   }
/*      */   
/*      */   private static class UnmodifiableListMultimap<K, V>
/*      */     extends UnmodifiableMultimap<K, V> implements ListMultimap<K, V> {
/*      */     private static final long serialVersionUID = 0L;
/*      */     
/*      */     UnmodifiableListMultimap(ListMultimap<K, V> delegate) {
/*  766 */       super(delegate);
/*      */     }
/*      */ 
/*      */     
/*      */     public ListMultimap<K, V> delegate() {
/*  771 */       return (ListMultimap<K, V>)super.delegate();
/*      */     }
/*      */ 
/*      */     
/*      */     public List<V> get(K key) {
/*  776 */       return Collections.unmodifiableList(delegate().get(key));
/*      */     }
/*      */ 
/*      */     
/*      */     public List<V> removeAll(Object key) {
/*  781 */       throw new UnsupportedOperationException();
/*      */     }
/*      */ 
/*      */     
/*      */     public List<V> replaceValues(K key, Iterable<? extends V> values) {
/*  786 */       throw new UnsupportedOperationException();
/*      */     }
/*      */   }
/*      */   
/*      */   private static class UnmodifiableSetMultimap<K, V>
/*      */     extends UnmodifiableMultimap<K, V> implements SetMultimap<K, V> {
/*      */     private static final long serialVersionUID = 0L;
/*      */     
/*      */     UnmodifiableSetMultimap(SetMultimap<K, V> delegate) {
/*  795 */       super(delegate);
/*      */     }
/*      */ 
/*      */     
/*      */     public SetMultimap<K, V> delegate() {
/*  800 */       return (SetMultimap<K, V>)super.delegate();
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public Set<V> get(K key) {
/*  809 */       return Collections.unmodifiableSet(delegate().get(key));
/*      */     }
/*      */ 
/*      */     
/*      */     public Set<Map.Entry<K, V>> entries() {
/*  814 */       return Maps.unmodifiableEntrySet(delegate().entries());
/*      */     }
/*      */ 
/*      */     
/*      */     public Set<V> removeAll(Object key) {
/*  819 */       throw new UnsupportedOperationException();
/*      */     }
/*      */ 
/*      */     
/*      */     public Set<V> replaceValues(K key, Iterable<? extends V> values) {
/*  824 */       throw new UnsupportedOperationException();
/*      */     }
/*      */   }
/*      */   
/*      */   private static class UnmodifiableSortedSetMultimap<K, V>
/*      */     extends UnmodifiableSetMultimap<K, V> implements SortedSetMultimap<K, V> {
/*      */     private static final long serialVersionUID = 0L;
/*      */     
/*      */     UnmodifiableSortedSetMultimap(SortedSetMultimap<K, V> delegate) {
/*  833 */       super(delegate);
/*      */     }
/*      */ 
/*      */     
/*      */     public SortedSetMultimap<K, V> delegate() {
/*  838 */       return (SortedSetMultimap<K, V>)super.delegate();
/*      */     }
/*      */ 
/*      */     
/*      */     public SortedSet<V> get(K key) {
/*  843 */       return Collections.unmodifiableSortedSet(delegate().get(key));
/*      */     }
/*      */ 
/*      */     
/*      */     public SortedSet<V> removeAll(Object key) {
/*  848 */       throw new UnsupportedOperationException();
/*      */     }
/*      */ 
/*      */     
/*      */     public SortedSet<V> replaceValues(K key, Iterable<? extends V> values) {
/*  853 */       throw new UnsupportedOperationException();
/*      */     }
/*      */ 
/*      */     
/*      */     public Comparator<? super V> valueComparator() {
/*  858 */       return delegate().valueComparator();
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static <K, V> SetMultimap<K, V> synchronizedSetMultimap(SetMultimap<K, V> multimap) {
/*  875 */     return Synchronized.setMultimap(multimap, null);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static <K, V> SetMultimap<K, V> unmodifiableSetMultimap(SetMultimap<K, V> delegate) {
/*  890 */     if (delegate instanceof UnmodifiableSetMultimap || delegate instanceof ImmutableSetMultimap) {
/*  891 */       return delegate;
/*      */     }
/*  893 */     return new UnmodifiableSetMultimap<>(delegate);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Deprecated
/*      */   public static <K, V> SetMultimap<K, V> unmodifiableSetMultimap(ImmutableSetMultimap<K, V> delegate) {
/*  905 */     return (SetMultimap<K, V>)Preconditions.checkNotNull(delegate);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static <K, V> SortedSetMultimap<K, V> synchronizedSortedSetMultimap(SortedSetMultimap<K, V> multimap) {
/*  921 */     return Synchronized.sortedSetMultimap(multimap, null);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static <K, V> SortedSetMultimap<K, V> unmodifiableSortedSetMultimap(SortedSetMultimap<K, V> delegate) {
/*  937 */     if (delegate instanceof UnmodifiableSortedSetMultimap) {
/*  938 */       return delegate;
/*      */     }
/*  940 */     return new UnmodifiableSortedSetMultimap<>(delegate);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static <K, V> ListMultimap<K, V> synchronizedListMultimap(ListMultimap<K, V> multimap) {
/*  952 */     return Synchronized.listMultimap(multimap, null);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static <K, V> ListMultimap<K, V> unmodifiableListMultimap(ListMultimap<K, V> delegate) {
/*  967 */     if (delegate instanceof UnmodifiableListMultimap || delegate instanceof ImmutableListMultimap) {
/*  968 */       return delegate;
/*      */     }
/*  970 */     return new UnmodifiableListMultimap<>(delegate);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Deprecated
/*      */   public static <K, V> ListMultimap<K, V> unmodifiableListMultimap(ImmutableListMultimap<K, V> delegate) {
/*  982 */     return (ListMultimap<K, V>)Preconditions.checkNotNull(delegate);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static <V> Collection<V> unmodifiableValueCollection(Collection<V> collection) {
/*  994 */     if (collection instanceof SortedSet)
/*  995 */       return Collections.unmodifiableSortedSet((SortedSet<V>)collection); 
/*  996 */     if (collection instanceof Set)
/*  997 */       return Collections.unmodifiableSet((Set<? extends V>)collection); 
/*  998 */     if (collection instanceof List) {
/*  999 */       return Collections.unmodifiableList((List<? extends V>)collection);
/*      */     }
/* 1001 */     return Collections.unmodifiableCollection(collection);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static <K, V> Collection<Map.Entry<K, V>> unmodifiableEntries(Collection<Map.Entry<K, V>> entries) {
/* 1014 */     if (entries instanceof Set) {
/* 1015 */       return Maps.unmodifiableEntrySet((Set<Map.Entry<K, V>>)entries);
/*      */     }
/* 1017 */     return new Maps.UnmodifiableEntries<>(Collections.unmodifiableCollection(entries));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Beta
/*      */   public static <K, V> Map<K, List<V>> asMap(ListMultimap<K, V> multimap) {
/* 1030 */     return (Map)multimap.asMap();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Beta
/*      */   public static <K, V> Map<K, Set<V>> asMap(SetMultimap<K, V> multimap) {
/* 1043 */     return (Map)multimap.asMap();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Beta
/*      */   public static <K, V> Map<K, SortedSet<V>> asMap(SortedSetMultimap<K, V> multimap) {
/* 1056 */     return (Map)multimap.asMap();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Beta
/*      */   public static <K, V> Map<K, Collection<V>> asMap(Multimap<K, V> multimap) {
/* 1067 */     return multimap.asMap();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static <K, V> SetMultimap<K, V> forMap(Map<K, V> map) {
/* 1086 */     return new MapMultimap<>(map);
/*      */   }
/*      */   
/*      */   private static class MapMultimap<K, V>
/*      */     extends AbstractMultimap<K, V> implements SetMultimap<K, V>, Serializable {
/*      */     final Map<K, V> map;
/*      */     private static final long serialVersionUID = 7845222491160860175L;
/*      */     
/*      */     MapMultimap(Map<K, V> map) {
/* 1095 */       this.map = (Map<K, V>)Preconditions.checkNotNull(map);
/*      */     }
/*      */ 
/*      */     
/*      */     public int size() {
/* 1100 */       return this.map.size();
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean containsKey(Object key) {
/* 1105 */       return this.map.containsKey(key);
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean containsValue(Object value) {
/* 1110 */       return this.map.containsValue(value);
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean containsEntry(Object key, Object value) {
/* 1115 */       return this.map.entrySet().contains(Maps.immutableEntry(key, value));
/*      */     }
/*      */ 
/*      */     
/*      */     public Set<V> get(final K key) {
/* 1120 */       return new Sets.ImprovedAbstractSet<V>()
/*      */         {
/*      */           public Iterator<V> iterator() {
/* 1123 */             return new Iterator<V>()
/*      */               {
/*      */                 int i;
/*      */                 
/*      */                 public boolean hasNext() {
/* 1128 */                   return (this.i == 0 && Multimaps.MapMultimap.this.map.containsKey(key));
/*      */                 }
/*      */ 
/*      */                 
/*      */                 public V next() {
/* 1133 */                   if (!hasNext()) {
/* 1134 */                     throw new NoSuchElementException();
/*      */                   }
/* 1136 */                   this.i++;
/* 1137 */                   return (V)Multimaps.MapMultimap.this.map.get(key);
/*      */                 }
/*      */ 
/*      */                 
/*      */                 public void remove() {
/* 1142 */                   CollectPreconditions.checkRemove((this.i == 1));
/* 1143 */                   this.i = -1;
/* 1144 */                   Multimaps.MapMultimap.this.map.remove(key);
/*      */                 }
/*      */               };
/*      */           }
/*      */ 
/*      */           
/*      */           public int size() {
/* 1151 */             return Multimaps.MapMultimap.this.map.containsKey(key) ? 1 : 0;
/*      */           }
/*      */         };
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean put(K key, V value) {
/* 1158 */       throw new UnsupportedOperationException();
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean putAll(K key, Iterable<? extends V> values) {
/* 1163 */       throw new UnsupportedOperationException();
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean putAll(Multimap<? extends K, ? extends V> multimap) {
/* 1168 */       throw new UnsupportedOperationException();
/*      */     }
/*      */ 
/*      */     
/*      */     public Set<V> replaceValues(K key, Iterable<? extends V> values) {
/* 1173 */       throw new UnsupportedOperationException();
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean remove(Object key, Object value) {
/* 1178 */       return this.map.entrySet().remove(Maps.immutableEntry(key, value));
/*      */     }
/*      */ 
/*      */     
/*      */     public Set<V> removeAll(Object key) {
/* 1183 */       Set<V> values = new HashSet<>(2);
/* 1184 */       if (!this.map.containsKey(key)) {
/* 1185 */         return values;
/*      */       }
/* 1187 */       values.add(this.map.remove(key));
/* 1188 */       return values;
/*      */     }
/*      */ 
/*      */     
/*      */     public void clear() {
/* 1193 */       this.map.clear();
/*      */     }
/*      */ 
/*      */     
/*      */     Set<K> createKeySet() {
/* 1198 */       return this.map.keySet();
/*      */     }
/*      */ 
/*      */     
/*      */     Collection<V> createValues() {
/* 1203 */       return this.map.values();
/*      */     }
/*      */ 
/*      */     
/*      */     public Set<Map.Entry<K, V>> entries() {
/* 1208 */       return this.map.entrySet();
/*      */     }
/*      */ 
/*      */     
/*      */     Collection<Map.Entry<K, V>> createEntries() {
/* 1213 */       throw new AssertionError("unreachable");
/*      */     }
/*      */ 
/*      */     
/*      */     Multiset<K> createKeys() {
/* 1218 */       return new Multimaps.Keys<>(this);
/*      */     }
/*      */ 
/*      */     
/*      */     Iterator<Map.Entry<K, V>> entryIterator() {
/* 1223 */       return this.map.entrySet().iterator();
/*      */     }
/*      */ 
/*      */     
/*      */     Map<K, Collection<V>> createAsMap() {
/* 1228 */       return new Multimaps.AsMap<>(this);
/*      */     }
/*      */ 
/*      */     
/*      */     public int hashCode() {
/* 1233 */       return this.map.hashCode();
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static <K, V1, V2> Multimap<K, V2> transformValues(Multimap<K, V1> fromMultimap, Function<? super V1, V2> function) {
/* 1282 */     Preconditions.checkNotNull(function);
/* 1283 */     Maps.EntryTransformer<K, V1, V2> transformer = Maps.asEntryTransformer(function);
/* 1284 */     return transformEntries(fromMultimap, transformer);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static <K, V1, V2> ListMultimap<K, V2> transformValues(ListMultimap<K, V1> fromMultimap, Function<? super V1, V2> function) {
/* 1329 */     Preconditions.checkNotNull(function);
/* 1330 */     Maps.EntryTransformer<K, V1, V2> transformer = Maps.asEntryTransformer(function);
/* 1331 */     return transformEntries(fromMultimap, transformer);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static <K, V1, V2> Multimap<K, V2> transformEntries(Multimap<K, V1> fromMap, Maps.EntryTransformer<? super K, ? super V1, V2> transformer) {
/* 1387 */     return new TransformedEntriesMultimap<>(fromMap, transformer);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static <K, V1, V2> ListMultimap<K, V2> transformEntries(ListMultimap<K, V1> fromMap, Maps.EntryTransformer<? super K, ? super V1, V2> transformer) {
/* 1440 */     return new TransformedEntriesListMultimap<>(fromMap, transformer);
/*      */   }
/*      */   
/*      */   private static class TransformedEntriesMultimap<K, V1, V2>
/*      */     extends AbstractMultimap<K, V2>
/*      */   {
/*      */     final Multimap<K, V1> fromMultimap;
/*      */     final Maps.EntryTransformer<? super K, ? super V1, V2> transformer;
/*      */     
/*      */     TransformedEntriesMultimap(Multimap<K, V1> fromMultimap, Maps.EntryTransformer<? super K, ? super V1, V2> transformer) {
/* 1450 */       this.fromMultimap = (Multimap<K, V1>)Preconditions.checkNotNull(fromMultimap);
/* 1451 */       this.transformer = (Maps.EntryTransformer<? super K, ? super V1, V2>)Preconditions.checkNotNull(transformer);
/*      */     }
/*      */     
/*      */     Collection<V2> transform(K key, Collection<V1> values) {
/* 1455 */       Function<? super V1, V2> function = Maps.asValueToValueFunction(this.transformer, key);
/* 1456 */       if (values instanceof List) {
/* 1457 */         return Lists.transform((List<V1>)values, function);
/*      */       }
/* 1459 */       return Collections2.transform(values, function);
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     Map<K, Collection<V2>> createAsMap() {
/* 1465 */       return Maps.transformEntries(this.fromMultimap
/* 1466 */           .asMap(), (Maps.EntryTransformer)new Maps.EntryTransformer<K, Collection<Collection<V1>>, Collection<Collection<V2>>>()
/*      */           {
/*      */             public Collection<V2> transformEntry(K key, Collection<V1> value)
/*      */             {
/* 1470 */               return Multimaps.TransformedEntriesMultimap.this.transform(key, value);
/*      */             }
/*      */           });
/*      */     }
/*      */ 
/*      */     
/*      */     public void clear() {
/* 1477 */       this.fromMultimap.clear();
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean containsKey(Object key) {
/* 1482 */       return this.fromMultimap.containsKey(key);
/*      */     }
/*      */ 
/*      */     
/*      */     Collection<Map.Entry<K, V2>> createEntries() {
/* 1487 */       return new AbstractMultimap.Entries(this);
/*      */     }
/*      */ 
/*      */     
/*      */     Iterator<Map.Entry<K, V2>> entryIterator() {
/* 1492 */       return Iterators.transform(this.fromMultimap
/* 1493 */           .entries().iterator(), Maps.asEntryToEntryFunction(this.transformer));
/*      */     }
/*      */ 
/*      */     
/*      */     public Collection<V2> get(K key) {
/* 1498 */       return transform(key, this.fromMultimap.get(key));
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean isEmpty() {
/* 1503 */       return this.fromMultimap.isEmpty();
/*      */     }
/*      */ 
/*      */     
/*      */     Set<K> createKeySet() {
/* 1508 */       return this.fromMultimap.keySet();
/*      */     }
/*      */ 
/*      */     
/*      */     Multiset<K> createKeys() {
/* 1513 */       return this.fromMultimap.keys();
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean put(K key, V2 value) {
/* 1518 */       throw new UnsupportedOperationException();
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean putAll(K key, Iterable<? extends V2> values) {
/* 1523 */       throw new UnsupportedOperationException();
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean putAll(Multimap<? extends K, ? extends V2> multimap) {
/* 1528 */       throw new UnsupportedOperationException();
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     public boolean remove(Object key, Object value) {
/* 1534 */       return get((K)key).remove(value);
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     public Collection<V2> removeAll(Object key) {
/* 1540 */       return transform((K)key, this.fromMultimap.removeAll(key));
/*      */     }
/*      */ 
/*      */     
/*      */     public Collection<V2> replaceValues(K key, Iterable<? extends V2> values) {
/* 1545 */       throw new UnsupportedOperationException();
/*      */     }
/*      */ 
/*      */     
/*      */     public int size() {
/* 1550 */       return this.fromMultimap.size();
/*      */     }
/*      */ 
/*      */     
/*      */     Collection<V2> createValues() {
/* 1555 */       return Collections2.transform(this.fromMultimap
/* 1556 */           .entries(), Maps.asEntryToValueFunction(this.transformer));
/*      */     }
/*      */   }
/*      */   
/*      */   private static final class TransformedEntriesListMultimap<K, V1, V2>
/*      */     extends TransformedEntriesMultimap<K, V1, V2>
/*      */     implements ListMultimap<K, V2>
/*      */   {
/*      */     TransformedEntriesListMultimap(ListMultimap<K, V1> fromMultimap, Maps.EntryTransformer<? super K, ? super V1, V2> transformer) {
/* 1565 */       super(fromMultimap, transformer);
/*      */     }
/*      */ 
/*      */     
/*      */     List<V2> transform(K key, Collection<V1> values) {
/* 1570 */       return Lists.transform((List)values, Maps.asValueToValueFunction(this.transformer, key));
/*      */     }
/*      */ 
/*      */     
/*      */     public List<V2> get(K key) {
/* 1575 */       return transform(key, this.fromMultimap.get(key));
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     public List<V2> removeAll(Object key) {
/* 1581 */       return transform((K)key, this.fromMultimap.removeAll(key));
/*      */     }
/*      */ 
/*      */     
/*      */     public List<V2> replaceValues(K key, Iterable<? extends V2> values) {
/* 1586 */       throw new UnsupportedOperationException();
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static <K, V> ImmutableListMultimap<K, V> index(Iterable<V> values, Function<? super V, K> keyFunction) {
/* 1627 */     return index(values.iterator(), keyFunction);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static <K, V> ImmutableListMultimap<K, V> index(Iterator<V> values, Function<? super V, K> keyFunction) {
/* 1668 */     Preconditions.checkNotNull(keyFunction);
/* 1669 */     ImmutableListMultimap.Builder<K, V> builder = ImmutableListMultimap.builder();
/* 1670 */     while (values.hasNext()) {
/* 1671 */       V value = values.next();
/* 1672 */       Preconditions.checkNotNull(value, values);
/* 1673 */       builder.put((K)keyFunction.apply(value), value);
/*      */     } 
/* 1675 */     return builder.build();
/*      */   }
/*      */   
/*      */   static class Keys<K, V> extends AbstractMultiset<K> { @Weak
/*      */     final Multimap<K, V> multimap;
/*      */     
/*      */     Keys(Multimap<K, V> multimap) {
/* 1682 */       this.multimap = multimap;
/*      */     }
/*      */ 
/*      */     
/*      */     Iterator<Multiset.Entry<K>> entryIterator() {
/* 1687 */       return new TransformedIterator<Map.Entry<K, Collection<V>>, Multiset.Entry<K>>(this.multimap
/* 1688 */           .asMap().entrySet().iterator())
/*      */         {
/*      */           Multiset.Entry<K> transform(final Map.Entry<K, Collection<V>> backingEntry) {
/* 1691 */             return new Multisets.AbstractEntry<K>()
/*      */               {
/*      */                 public K getElement() {
/* 1694 */                   return (K)backingEntry.getKey();
/*      */                 }
/*      */ 
/*      */                 
/*      */                 public int getCount() {
/* 1699 */                   return ((Collection)backingEntry.getValue()).size();
/*      */                 }
/*      */               };
/*      */           }
/*      */         };
/*      */     }
/*      */ 
/*      */     
/*      */     public Spliterator<K> spliterator() {
/* 1708 */       return CollectSpliterators.map(this.multimap.entries().spliterator(), Map.Entry::getKey);
/*      */     }
/*      */ 
/*      */     
/*      */     public void forEach(Consumer<? super K> consumer) {
/* 1713 */       Preconditions.checkNotNull(consumer);
/* 1714 */       this.multimap.entries().forEach(entry -> consumer.accept(entry.getKey()));
/*      */     }
/*      */ 
/*      */     
/*      */     int distinctElements() {
/* 1719 */       return this.multimap.asMap().size();
/*      */     }
/*      */ 
/*      */     
/*      */     public int size() {
/* 1724 */       return this.multimap.size();
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean contains(Object element) {
/* 1729 */       return this.multimap.containsKey(element);
/*      */     }
/*      */ 
/*      */     
/*      */     public Iterator<K> iterator() {
/* 1734 */       return Maps.keyIterator(this.multimap.entries().iterator());
/*      */     }
/*      */ 
/*      */     
/*      */     public int count(Object element) {
/* 1739 */       Collection<V> values = Maps.<Collection<V>>safeGet(this.multimap.asMap(), element);
/* 1740 */       return (values == null) ? 0 : values.size();
/*      */     }
/*      */ 
/*      */     
/*      */     public int remove(Object element, int occurrences) {
/* 1745 */       CollectPreconditions.checkNonnegative(occurrences, "occurrences");
/* 1746 */       if (occurrences == 0) {
/* 1747 */         return count(element);
/*      */       }
/*      */       
/* 1750 */       Collection<V> values = Maps.<Collection<V>>safeGet(this.multimap.asMap(), element);
/*      */       
/* 1752 */       if (values == null) {
/* 1753 */         return 0;
/*      */       }
/*      */       
/* 1756 */       int oldCount = values.size();
/* 1757 */       if (occurrences >= oldCount) {
/* 1758 */         values.clear();
/*      */       } else {
/* 1760 */         Iterator<V> iterator = values.iterator();
/* 1761 */         for (int i = 0; i < occurrences; i++) {
/* 1762 */           iterator.next();
/* 1763 */           iterator.remove();
/*      */         } 
/*      */       } 
/* 1766 */       return oldCount;
/*      */     }
/*      */ 
/*      */     
/*      */     public void clear() {
/* 1771 */       this.multimap.clear();
/*      */     }
/*      */ 
/*      */     
/*      */     public Set<K> elementSet() {
/* 1776 */       return this.multimap.keySet();
/*      */     }
/*      */ 
/*      */     
/*      */     Iterator<K> elementIterator() {
/* 1781 */       throw new AssertionError("should never be called");
/*      */     } }
/*      */ 
/*      */   
/*      */   static abstract class Entries<K, V>
/*      */     extends AbstractCollection<Map.Entry<K, V>>
/*      */   {
/*      */     abstract Multimap<K, V> multimap();
/*      */     
/*      */     public int size() {
/* 1791 */       return multimap().size();
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean contains(Object o) {
/* 1796 */       if (o instanceof Map.Entry) {
/* 1797 */         Map.Entry<?, ?> entry = (Map.Entry<?, ?>)o;
/* 1798 */         return multimap().containsEntry(entry.getKey(), entry.getValue());
/*      */       } 
/* 1800 */       return false;
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean remove(Object o) {
/* 1805 */       if (o instanceof Map.Entry) {
/* 1806 */         Map.Entry<?, ?> entry = (Map.Entry<?, ?>)o;
/* 1807 */         return multimap().remove(entry.getKey(), entry.getValue());
/*      */       } 
/* 1809 */       return false;
/*      */     }
/*      */ 
/*      */     
/*      */     public void clear() {
/* 1814 */       multimap().clear();
/*      */     }
/*      */   }
/*      */   
/*      */   static final class AsMap<K, V> extends Maps.ViewCachingAbstractMap<K, Collection<V>> {
/*      */     @Weak
/*      */     private final Multimap<K, V> multimap;
/*      */     
/*      */     AsMap(Multimap<K, V> multimap) {
/* 1823 */       this.multimap = (Multimap<K, V>)Preconditions.checkNotNull(multimap);
/*      */     }
/*      */ 
/*      */     
/*      */     public int size() {
/* 1828 */       return this.multimap.keySet().size();
/*      */     }
/*      */ 
/*      */     
/*      */     protected Set<Map.Entry<K, Collection<V>>> createEntrySet() {
/* 1833 */       return new EntrySet();
/*      */     }
/*      */     
/*      */     void removeValuesForKey(Object key) {
/* 1837 */       this.multimap.keySet().remove(key);
/*      */     }
/*      */     
/*      */     class EntrySet
/*      */       extends Maps.EntrySet<K, Collection<V>>
/*      */     {
/*      */       Map<K, Collection<V>> map() {
/* 1844 */         return Multimaps.AsMap.this;
/*      */       }
/*      */ 
/*      */       
/*      */       public Iterator<Map.Entry<K, Collection<V>>> iterator() {
/* 1849 */         return Maps.asMapEntryIterator(Multimaps.AsMap.this
/* 1850 */             .multimap.keySet(), new Function<K, Collection<V>>()
/*      */             {
/*      */               public Collection<V> apply(K key)
/*      */               {
/* 1854 */                 return Multimaps.AsMap.this.multimap.get(key);
/*      */               }
/*      */             });
/*      */       }
/*      */ 
/*      */       
/*      */       public boolean remove(Object o) {
/* 1861 */         if (!contains(o)) {
/* 1862 */           return false;
/*      */         }
/* 1864 */         Map.Entry<?, ?> entry = (Map.Entry<?, ?>)o;
/* 1865 */         Multimaps.AsMap.this.removeValuesForKey(entry.getKey());
/* 1866 */         return true;
/*      */       }
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     public Collection<V> get(Object key) {
/* 1873 */       return containsKey(key) ? this.multimap.get((K)key) : null;
/*      */     }
/*      */ 
/*      */     
/*      */     public Collection<V> remove(Object key) {
/* 1878 */       return containsKey(key) ? this.multimap.removeAll(key) : null;
/*      */     }
/*      */ 
/*      */     
/*      */     public Set<K> keySet() {
/* 1883 */       return this.multimap.keySet();
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean isEmpty() {
/* 1888 */       return this.multimap.isEmpty();
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean containsKey(Object key) {
/* 1893 */       return this.multimap.containsKey(key);
/*      */     }
/*      */ 
/*      */     
/*      */     public void clear() {
/* 1898 */       this.multimap.clear();
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static <K, V> Multimap<K, V> filterKeys(Multimap<K, V> unfiltered, Predicate<? super K> keyPredicate) {
/* 1931 */     if (unfiltered instanceof SetMultimap)
/* 1932 */       return filterKeys((SetMultimap<K, V>)unfiltered, keyPredicate); 
/* 1933 */     if (unfiltered instanceof ListMultimap)
/* 1934 */       return filterKeys((ListMultimap<K, V>)unfiltered, keyPredicate); 
/* 1935 */     if (unfiltered instanceof FilteredKeyMultimap) {
/* 1936 */       FilteredKeyMultimap<K, V> prev = (FilteredKeyMultimap<K, V>)unfiltered;
/* 1937 */       return new FilteredKeyMultimap<>(prev.unfiltered, 
/* 1938 */           Predicates.and(prev.keyPredicate, keyPredicate));
/* 1939 */     }  if (unfiltered instanceof FilteredMultimap) {
/* 1940 */       FilteredMultimap<K, V> prev = (FilteredMultimap<K, V>)unfiltered;
/* 1941 */       return filterFiltered(prev, (Predicate)Maps.keyPredicateOnEntries(keyPredicate));
/*      */     } 
/* 1943 */     return new FilteredKeyMultimap<>(unfiltered, keyPredicate);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static <K, V> SetMultimap<K, V> filterKeys(SetMultimap<K, V> unfiltered, Predicate<? super K> keyPredicate) {
/* 1976 */     if (unfiltered instanceof FilteredKeySetMultimap) {
/* 1977 */       FilteredKeySetMultimap<K, V> prev = (FilteredKeySetMultimap<K, V>)unfiltered;
/* 1978 */       return new FilteredKeySetMultimap<>(prev
/* 1979 */           .unfiltered(), Predicates.and(prev.keyPredicate, keyPredicate));
/* 1980 */     }  if (unfiltered instanceof FilteredSetMultimap) {
/* 1981 */       FilteredSetMultimap<K, V> prev = (FilteredSetMultimap<K, V>)unfiltered;
/* 1982 */       return filterFiltered(prev, (Predicate)Maps.keyPredicateOnEntries(keyPredicate));
/*      */     } 
/* 1984 */     return new FilteredKeySetMultimap<>(unfiltered, keyPredicate);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static <K, V> ListMultimap<K, V> filterKeys(ListMultimap<K, V> unfiltered, Predicate<? super K> keyPredicate) {
/* 2017 */     if (unfiltered instanceof FilteredKeyListMultimap) {
/* 2018 */       FilteredKeyListMultimap<K, V> prev = (FilteredKeyListMultimap<K, V>)unfiltered;
/* 2019 */       return new FilteredKeyListMultimap<>(prev
/* 2020 */           .unfiltered(), Predicates.and(prev.keyPredicate, keyPredicate));
/*      */     } 
/* 2022 */     return new FilteredKeyListMultimap<>(unfiltered, keyPredicate);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static <K, V> Multimap<K, V> filterValues(Multimap<K, V> unfiltered, Predicate<? super V> valuePredicate) {
/* 2055 */     return filterEntries(unfiltered, (Predicate)Maps.valuePredicateOnEntries(valuePredicate));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static <K, V> SetMultimap<K, V> filterValues(SetMultimap<K, V> unfiltered, Predicate<? super V> valuePredicate) {
/* 2087 */     return filterEntries(unfiltered, (Predicate)Maps.valuePredicateOnEntries(valuePredicate));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static <K, V> Multimap<K, V> filterEntries(Multimap<K, V> unfiltered, Predicate<? super Map.Entry<K, V>> entryPredicate) {
/* 2117 */     Preconditions.checkNotNull(entryPredicate);
/* 2118 */     if (unfiltered instanceof SetMultimap) {
/* 2119 */       return filterEntries((SetMultimap<K, V>)unfiltered, entryPredicate);
/*      */     }
/* 2121 */     return (unfiltered instanceof FilteredMultimap) ? 
/* 2122 */       filterFiltered((FilteredMultimap<K, V>)unfiltered, entryPredicate) : new FilteredEntryMultimap<>(
/* 2123 */         (Multimap<K, V>)Preconditions.checkNotNull(unfiltered), entryPredicate);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static <K, V> SetMultimap<K, V> filterEntries(SetMultimap<K, V> unfiltered, Predicate<? super Map.Entry<K, V>> entryPredicate) {
/* 2153 */     Preconditions.checkNotNull(entryPredicate);
/* 2154 */     return (unfiltered instanceof FilteredSetMultimap) ? 
/* 2155 */       filterFiltered((FilteredSetMultimap<K, V>)unfiltered, entryPredicate) : new FilteredEntrySetMultimap<>(
/* 2156 */         (SetMultimap<K, V>)Preconditions.checkNotNull(unfiltered), entryPredicate);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static <K, V> Multimap<K, V> filterFiltered(FilteredMultimap<K, V> multimap, Predicate<? super Map.Entry<K, V>> entryPredicate) {
/* 2168 */     Predicate<Map.Entry<K, V>> predicate = Predicates.and(multimap.entryPredicate(), entryPredicate);
/* 2169 */     return new FilteredEntryMultimap<>(multimap.unfiltered(), predicate);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static <K, V> SetMultimap<K, V> filterFiltered(FilteredSetMultimap<K, V> multimap, Predicate<? super Map.Entry<K, V>> entryPredicate) {
/* 2181 */     Predicate<Map.Entry<K, V>> predicate = Predicates.and(multimap.entryPredicate(), entryPredicate);
/* 2182 */     return new FilteredEntrySetMultimap<>(multimap.unfiltered(), predicate);
/*      */   }
/*      */   
/*      */   static boolean equalsImpl(Multimap<?, ?> multimap, Object object) {
/* 2186 */     if (object == multimap) {
/* 2187 */       return true;
/*      */     }
/* 2189 */     if (object instanceof Multimap) {
/* 2190 */       Multimap<?, ?> that = (Multimap<?, ?>)object;
/* 2191 */       return multimap.asMap().equals(that.asMap());
/*      */     } 
/* 2193 */     return false;
/*      */   }
/*      */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\google\common\collect\Multimaps.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */