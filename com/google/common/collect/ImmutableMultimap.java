/*     */ package com.google.common.collect;
/*     */ 
/*     */ import com.google.common.annotations.Beta;
/*     */ import com.google.common.annotations.GwtCompatible;
/*     */ import com.google.common.annotations.GwtIncompatible;
/*     */ import com.google.common.base.Preconditions;
/*     */ import com.google.errorprone.annotations.CanIgnoreReturnValue;
/*     */ import com.google.j2objc.annotations.Weak;
/*     */ import java.io.Serializable;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.Collection;
/*     */ import java.util.Comparator;
/*     */ import java.util.Iterator;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import java.util.Spliterator;
/*     */ import java.util.function.BiConsumer;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @GwtCompatible(emulated = true)
/*     */ public abstract class ImmutableMultimap<K, V>
/*     */   extends BaseImmutableMultimap<K, V>
/*     */   implements Serializable
/*     */ {
/*     */   final transient ImmutableMap<K, ? extends ImmutableCollection<V>> map;
/*     */   final transient int size;
/*     */   private static final long serialVersionUID = 0L;
/*     */   
/*     */   public static <K, V> ImmutableMultimap<K, V> of() {
/*  74 */     return ImmutableListMultimap.of();
/*     */   }
/*     */ 
/*     */   
/*     */   public static <K, V> ImmutableMultimap<K, V> of(K k1, V v1) {
/*  79 */     return ImmutableListMultimap.of(k1, v1);
/*     */   }
/*     */ 
/*     */   
/*     */   public static <K, V> ImmutableMultimap<K, V> of(K k1, V v1, K k2, V v2) {
/*  84 */     return ImmutableListMultimap.of(k1, v1, k2, v2);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <K, V> ImmutableMultimap<K, V> of(K k1, V v1, K k2, V v2, K k3, V v3) {
/*  92 */     return ImmutableListMultimap.of(k1, v1, k2, v2, k3, v3);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <K, V> ImmutableMultimap<K, V> of(K k1, V v1, K k2, V v2, K k3, V v3, K k4, V v4) {
/* 100 */     return ImmutableListMultimap.of(k1, v1, k2, v2, k3, v3, k4, v4);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <K, V> ImmutableMultimap<K, V> of(K k1, V v1, K k2, V v2, K k3, V v3, K k4, V v4, K k5, V v5) {
/* 109 */     return ImmutableListMultimap.of(k1, v1, k2, v2, k3, v3, k4, v4, k5, v5);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <K, V> Builder<K, V> builder() {
/* 119 */     return new Builder<>();
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
/*     */   {
/* 151 */     Map<K, Collection<V>> builderMap = Platform.preservesInsertionOrderOnPutsMap(); Comparator<? super K> keyComparator;
/*     */     Comparator<? super V> valueComparator;
/*     */     
/*     */     Collection<V> newMutableValueCollection() {
/* 155 */       return new ArrayList<>();
/*     */     }
/*     */ 
/*     */     
/*     */     @CanIgnoreReturnValue
/*     */     public Builder<K, V> put(K key, V value) {
/* 161 */       CollectPreconditions.checkEntryNotNull(key, value);
/* 162 */       Collection<V> valueCollection = this.builderMap.get(key);
/* 163 */       if (valueCollection == null) {
/* 164 */         this.builderMap.put(key, valueCollection = newMutableValueCollection());
/*     */       }
/* 166 */       valueCollection.add(value);
/* 167 */       return this;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     @CanIgnoreReturnValue
/*     */     public Builder<K, V> put(Map.Entry<? extends K, ? extends V> entry) {
/* 177 */       return put(entry.getKey(), entry.getValue());
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     @CanIgnoreReturnValue
/*     */     @Beta
/*     */     public Builder<K, V> putAll(Iterable<? extends Map.Entry<? extends K, ? extends V>> entries) {
/* 188 */       for (Map.Entry<? extends K, ? extends V> entry : entries) {
/* 189 */         put(entry);
/*     */       }
/* 191 */       return this;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     @CanIgnoreReturnValue
/*     */     public Builder<K, V> putAll(K key, Iterable<? extends V> values) {
/* 202 */       if (key == null) {
/* 203 */         throw new NullPointerException("null key in entry: null=" + Iterables.toString(values));
/*     */       }
/* 205 */       Collection<V> valueCollection = this.builderMap.get(key);
/* 206 */       if (valueCollection != null) {
/* 207 */         for (V value : values) {
/* 208 */           CollectPreconditions.checkEntryNotNull(key, value);
/* 209 */           valueCollection.add(value);
/*     */         } 
/* 211 */         return this;
/*     */       } 
/* 213 */       Iterator<? extends V> valuesItr = values.iterator();
/* 214 */       if (!valuesItr.hasNext()) {
/* 215 */         return this;
/*     */       }
/* 217 */       valueCollection = newMutableValueCollection();
/* 218 */       while (valuesItr.hasNext()) {
/* 219 */         V value = valuesItr.next();
/* 220 */         CollectPreconditions.checkEntryNotNull(key, value);
/* 221 */         valueCollection.add(value);
/*     */       } 
/* 223 */       this.builderMap.put(key, valueCollection);
/* 224 */       return this;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     @CanIgnoreReturnValue
/*     */     public Builder<K, V> putAll(K key, V... values) {
/* 235 */       return putAll(key, Arrays.asList(values));
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
/*     */     public Builder<K, V> putAll(Multimap<? extends K, ? extends V> multimap) {
/* 249 */       for (Map.Entry<? extends K, ? extends Collection<? extends V>> entry : (Iterable<Map.Entry<? extends K, ? extends Collection<? extends V>>>)multimap.asMap().entrySet()) {
/* 250 */         putAll(entry.getKey(), entry.getValue());
/*     */       }
/* 252 */       return this;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     @CanIgnoreReturnValue
/*     */     public Builder<K, V> orderKeysBy(Comparator<? super K> keyComparator) {
/* 262 */       this.keyComparator = (Comparator<? super K>)Preconditions.checkNotNull(keyComparator);
/* 263 */       return this;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     @CanIgnoreReturnValue
/*     */     public Builder<K, V> orderValuesBy(Comparator<? super V> valueComparator) {
/* 273 */       this.valueComparator = (Comparator<? super V>)Preconditions.checkNotNull(valueComparator);
/* 274 */       return this;
/*     */     }
/*     */     
/*     */     @CanIgnoreReturnValue
/*     */     Builder<K, V> combine(Builder<K, V> other) {
/* 279 */       for (Map.Entry<K, Collection<V>> entry : other.builderMap.entrySet()) {
/* 280 */         putAll(entry.getKey(), entry.getValue());
/*     */       }
/* 282 */       return this;
/*     */     }
/*     */ 
/*     */     
/*     */     public ImmutableMultimap<K, V> build() {
/* 287 */       Collection<Map.Entry<K, Collection<V>>> mapEntries = this.builderMap.entrySet();
/* 288 */       if (this.keyComparator != null) {
/* 289 */         mapEntries = Ordering.<K>from(this.keyComparator).onKeys().immutableSortedCopy(mapEntries);
/*     */       }
/* 291 */       return ImmutableListMultimap.fromMapEntries(mapEntries, this.valueComparator);
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
/*     */   public static <K, V> ImmutableMultimap<K, V> copyOf(Multimap<? extends K, ? extends V> multimap) {
/* 306 */     if (multimap instanceof ImmutableMultimap) {
/*     */       
/* 308 */       ImmutableMultimap<K, V> kvMultimap = (ImmutableMultimap)multimap;
/* 309 */       if (!kvMultimap.isPartialView()) {
/* 310 */         return kvMultimap;
/*     */       }
/*     */     } 
/* 313 */     return ImmutableListMultimap.copyOf(multimap);
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
/*     */   public static <K, V> ImmutableMultimap<K, V> copyOf(Iterable<? extends Map.Entry<? extends K, ? extends V>> entries) {
/* 327 */     return ImmutableListMultimap.copyOf(entries);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @GwtIncompatible
/*     */   static class FieldSettersHolder
/*     */   {
/* 339 */     static final Serialization.FieldSetter<ImmutableMultimap> MAP_FIELD_SETTER = Serialization.getFieldSetter(ImmutableMultimap.class, "map");
/*     */     
/* 341 */     static final Serialization.FieldSetter<ImmutableMultimap> SIZE_FIELD_SETTER = Serialization.getFieldSetter(ImmutableMultimap.class, "size");
/*     */   }
/*     */   
/*     */   ImmutableMultimap(ImmutableMap<K, ? extends ImmutableCollection<V>> map, int size) {
/* 345 */     this.map = map;
/* 346 */     this.size = size;
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
/*     */   @Deprecated
/*     */   @CanIgnoreReturnValue
/*     */   public ImmutableCollection<V> removeAll(Object key) {
/* 361 */     throw new UnsupportedOperationException();
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
/*     */   public ImmutableCollection<V> replaceValues(K key, Iterable<? extends V> values) {
/* 374 */     throw new UnsupportedOperationException();
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
/*     */   public void clear() {
/* 386 */     throw new UnsupportedOperationException();
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   @CanIgnoreReturnValue
/*     */   public boolean put(K key, V value) {
/* 415 */     throw new UnsupportedOperationException();
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
/*     */   public boolean putAll(K key, Iterable<? extends V> values) {
/* 428 */     throw new UnsupportedOperationException();
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
/*     */   public boolean putAll(Multimap<? extends K, ? extends V> multimap) {
/* 441 */     throw new UnsupportedOperationException();
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
/*     */   public boolean remove(Object key, Object value) {
/* 454 */     throw new UnsupportedOperationException();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   boolean isPartialView() {
/* 464 */     return this.map.isPartialView();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean containsKey(Object key) {
/* 471 */     return this.map.containsKey(key);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean containsValue(Object value) {
/* 476 */     return (value != null && super.containsValue(value));
/*     */   }
/*     */ 
/*     */   
/*     */   public int size() {
/* 481 */     return this.size;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ImmutableSet<K> keySet() {
/* 492 */     return this.map.keySet();
/*     */   }
/*     */ 
/*     */   
/*     */   Set<K> createKeySet() {
/* 497 */     throw new AssertionError("unreachable");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ImmutableMap<K, Collection<V>> asMap() {
/* 507 */     return (ImmutableMap)this.map;
/*     */   }
/*     */ 
/*     */   
/*     */   Map<K, Collection<V>> createAsMap() {
/* 512 */     throw new AssertionError("should never be called");
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public ImmutableCollection<Map.Entry<K, V>> entries() {
/* 518 */     return (ImmutableCollection<Map.Entry<K, V>>)super.entries();
/*     */   }
/*     */ 
/*     */   
/*     */   ImmutableCollection<Map.Entry<K, V>> createEntries() {
/* 523 */     return new EntryCollection<>(this);
/*     */   }
/*     */   private static class EntryCollection<K, V> extends ImmutableCollection<Map.Entry<K, V>> { @Weak
/*     */     final ImmutableMultimap<K, V> multimap;
/*     */     private static final long serialVersionUID = 0L;
/*     */     
/*     */     EntryCollection(ImmutableMultimap<K, V> multimap) {
/* 530 */       this.multimap = multimap;
/*     */     }
/*     */ 
/*     */     
/*     */     public UnmodifiableIterator<Map.Entry<K, V>> iterator() {
/* 535 */       return this.multimap.entryIterator();
/*     */     }
/*     */ 
/*     */     
/*     */     boolean isPartialView() {
/* 540 */       return this.multimap.isPartialView();
/*     */     }
/*     */ 
/*     */     
/*     */     public int size() {
/* 545 */       return this.multimap.size();
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean contains(Object object) {
/* 550 */       if (object instanceof Map.Entry) {
/* 551 */         Map.Entry<?, ?> entry = (Map.Entry<?, ?>)object;
/* 552 */         return this.multimap.containsEntry(entry.getKey(), entry.getValue());
/*     */       } 
/* 554 */       return false;
/*     */     } }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   UnmodifiableIterator<Map.Entry<K, V>> entryIterator() {
/* 562 */     return new UnmodifiableIterator<Map.Entry<K, V>>() {
/* 563 */         final Iterator<? extends Map.Entry<K, ? extends ImmutableCollection<V>>> asMapItr = ImmutableMultimap.this.map
/* 564 */           .entrySet().iterator();
/* 565 */         K currentKey = null;
/* 566 */         Iterator<V> valueItr = Iterators.emptyIterator();
/*     */ 
/*     */         
/*     */         public boolean hasNext() {
/* 570 */           return (this.valueItr.hasNext() || this.asMapItr.hasNext());
/*     */         }
/*     */ 
/*     */         
/*     */         public Map.Entry<K, V> next() {
/* 575 */           if (!this.valueItr.hasNext()) {
/* 576 */             Map.Entry<K, ? extends ImmutableCollection<V>> entry = this.asMapItr.next();
/* 577 */             this.currentKey = entry.getKey();
/* 578 */             this.valueItr = ((ImmutableCollection<V>)entry.getValue()).iterator();
/*     */           } 
/* 580 */           return Maps.immutableEntry(this.currentKey, this.valueItr.next());
/*     */         }
/*     */       };
/*     */   }
/*     */ 
/*     */   
/*     */   Spliterator<Map.Entry<K, V>> entrySpliterator() {
/* 587 */     return CollectSpliterators.flatMap(
/* 588 */         asMap().entrySet().spliterator(), keyToValueCollectionEntry -> { K key = (K)keyToValueCollectionEntry.getKey(); Collection<V> valueCollection = (Collection<V>)keyToValueCollectionEntry.getValue(); return CollectSpliterators.map(valueCollection.spliterator(), ()); }0x40 | ((this instanceof SetMultimap) ? 1 : 0), 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 596 */         size());
/*     */   }
/*     */ 
/*     */   
/*     */   public void forEach(BiConsumer<? super K, ? super V> action) {
/* 601 */     Preconditions.checkNotNull(action);
/* 602 */     asMap()
/* 603 */       .forEach((key, valueCollection) -> valueCollection.forEach(()));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ImmutableMultiset<K> keys() {
/* 614 */     return (ImmutableMultiset<K>)super.keys();
/*     */   }
/*     */ 
/*     */   
/*     */   ImmutableMultiset<K> createKeys() {
/* 619 */     return new Keys();
/*     */   }
/*     */ 
/*     */   
/*     */   class Keys
/*     */     extends ImmutableMultiset<K>
/*     */   {
/*     */     public boolean contains(Object object) {
/* 627 */       return ImmutableMultimap.this.containsKey(object);
/*     */     }
/*     */ 
/*     */     
/*     */     public int count(Object element) {
/* 632 */       Collection<V> values = (Collection<V>)ImmutableMultimap.this.map.get(element);
/* 633 */       return (values == null) ? 0 : values.size();
/*     */     }
/*     */ 
/*     */     
/*     */     public ImmutableSet<K> elementSet() {
/* 638 */       return ImmutableMultimap.this.keySet();
/*     */     }
/*     */ 
/*     */     
/*     */     public int size() {
/* 643 */       return ImmutableMultimap.this.size();
/*     */     }
/*     */ 
/*     */     
/*     */     Multiset.Entry<K> getEntry(int index) {
/* 648 */       Map.Entry<K, ? extends Collection<V>> entry = ImmutableMultimap.this.map.entrySet().asList().get(index);
/* 649 */       return Multisets.immutableEntry(entry.getKey(), ((Collection)entry.getValue()).size());
/*     */     }
/*     */ 
/*     */     
/*     */     boolean isPartialView() {
/* 654 */       return true;
/*     */     }
/*     */ 
/*     */     
/*     */     @GwtIncompatible
/*     */     Object writeReplace() {
/* 660 */       return new ImmutableMultimap.KeysSerializedForm(ImmutableMultimap.this);
/*     */     }
/*     */   }
/*     */   
/*     */   @GwtIncompatible
/*     */   private static final class KeysSerializedForm implements Serializable {
/*     */     final ImmutableMultimap<?, ?> multimap;
/*     */     
/*     */     KeysSerializedForm(ImmutableMultimap<?, ?> multimap) {
/* 669 */       this.multimap = multimap;
/*     */     }
/*     */     
/*     */     Object readResolve() {
/* 673 */       return this.multimap.keys();
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ImmutableCollection<V> values() {
/* 683 */     return (ImmutableCollection<V>)super.values();
/*     */   }
/*     */ 
/*     */   
/*     */   ImmutableCollection<V> createValues() {
/* 688 */     return new Values<>(this);
/*     */   }
/*     */ 
/*     */   
/*     */   UnmodifiableIterator<V> valueIterator() {
/* 693 */     return new UnmodifiableIterator<V>() {
/* 694 */         Iterator<? extends ImmutableCollection<V>> valueCollectionItr = ImmutableMultimap.this.map.values().iterator();
/* 695 */         Iterator<V> valueItr = Iterators.emptyIterator();
/*     */ 
/*     */         
/*     */         public boolean hasNext() {
/* 699 */           return (this.valueItr.hasNext() || this.valueCollectionItr.hasNext());
/*     */         }
/*     */ 
/*     */         
/*     */         public V next() {
/* 704 */           if (!this.valueItr.hasNext()) {
/* 705 */             this.valueItr = ((ImmutableCollection<V>)this.valueCollectionItr.next()).iterator();
/*     */           }
/* 707 */           return this.valueItr.next();
/*     */         }
/*     */       };
/*     */   }
/*     */   public abstract ImmutableCollection<V> get(K paramK);
/*     */   public abstract ImmutableMultimap<V, K> inverse();
/*     */   private static final class Values<K, V> extends ImmutableCollection<V> { @Weak
/*     */     private final transient ImmutableMultimap<K, V> multimap;
/*     */     Values(ImmutableMultimap<K, V> multimap) {
/* 716 */       this.multimap = multimap;
/*     */     }
/*     */     private static final long serialVersionUID = 0L;
/*     */     
/*     */     public boolean contains(Object object) {
/* 721 */       return this.multimap.containsValue(object);
/*     */     }
/*     */ 
/*     */     
/*     */     public UnmodifiableIterator<V> iterator() {
/* 726 */       return this.multimap.valueIterator();
/*     */     }
/*     */ 
/*     */     
/*     */     @GwtIncompatible
/*     */     int copyIntoArray(Object[] dst, int offset) {
/* 732 */       for (UnmodifiableIterator<ImmutableCollection<V>> unmodifiableIterator = this.multimap.map.values().iterator(); unmodifiableIterator.hasNext(); ) { ImmutableCollection<V> valueCollection = unmodifiableIterator.next();
/* 733 */         offset = valueCollection.copyIntoArray(dst, offset); }
/*     */       
/* 735 */       return offset;
/*     */     }
/*     */ 
/*     */     
/*     */     public int size() {
/* 740 */       return this.multimap.size();
/*     */     }
/*     */ 
/*     */     
/*     */     boolean isPartialView() {
/* 745 */       return true;
/*     */     } }
/*     */ 
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\google\common\collect\ImmutableMultimap.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */