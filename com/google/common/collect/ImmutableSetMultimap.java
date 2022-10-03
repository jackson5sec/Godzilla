/*     */ package com.google.common.collect;
/*     */ 
/*     */ import com.google.common.annotations.Beta;
/*     */ import com.google.common.annotations.GwtCompatible;
/*     */ import com.google.common.annotations.GwtIncompatible;
/*     */ import com.google.common.base.MoreObjects;
/*     */ import com.google.common.base.Preconditions;
/*     */ import com.google.errorprone.annotations.CanIgnoreReturnValue;
/*     */ import com.google.errorprone.annotations.concurrent.LazyInit;
/*     */ import com.google.j2objc.annotations.RetainedWith;
/*     */ import com.google.j2objc.annotations.Weak;
/*     */ import java.io.IOException;
/*     */ import java.io.InvalidObjectException;
/*     */ import java.io.ObjectInputStream;
/*     */ import java.io.ObjectOutputStream;
/*     */ import java.util.Arrays;
/*     */ import java.util.Collection;
/*     */ import java.util.Comparator;
/*     */ import java.util.Iterator;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import java.util.function.Function;
/*     */ import java.util.stream.Collector;
/*     */ import java.util.stream.Collectors;
/*     */ import java.util.stream.Stream;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
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
/*     */ public class ImmutableSetMultimap<K, V>
/*     */   extends ImmutableMultimap<K, V>
/*     */   implements SetMultimap<K, V>
/*     */ {
/*     */   private final transient ImmutableSet<V> emptySet;
/*     */   @LazyInit
/*     */   @RetainedWith
/*     */   private transient ImmutableSetMultimap<V, K> inverse;
/*     */   private transient ImmutableSet<Map.Entry<K, V>> entries;
/*     */   @GwtIncompatible
/*     */   private static final long serialVersionUID = 0L;
/*     */   
/*     */   public static <T, K, V> Collector<T, ?, ImmutableSetMultimap<K, V>> toImmutableSetMultimap(Function<? super T, ? extends K> keyFunction, Function<? super T, ? extends V> valueFunction) {
/*  89 */     Preconditions.checkNotNull(keyFunction, "keyFunction");
/*  90 */     Preconditions.checkNotNull(valueFunction, "valueFunction");
/*  91 */     return Collector.of(ImmutableSetMultimap::builder, (builder, t) -> builder.put(keyFunction.apply(t), valueFunction.apply(t)), Builder::combine, Builder::build, new Collector.Characteristics[0]);
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <T, K, V> Collector<T, ?, ImmutableSetMultimap<K, V>> flatteningToImmutableSetMultimap(Function<? super T, ? extends K> keyFunction, Function<? super T, ? extends Stream<? extends V>> valuesFunction) {
/* 142 */     Preconditions.checkNotNull(keyFunction);
/* 143 */     Preconditions.checkNotNull(valuesFunction);
/* 144 */     return Collectors.collectingAndThen(
/* 145 */         Multimaps.flatteningToMultimap(input -> Preconditions.checkNotNull(keyFunction.apply(input)), input -> ((Stream)valuesFunction.apply(input)).peek(Preconditions::checkNotNull), 
/*     */ 
/*     */           
/* 148 */           MultimapBuilder.linkedHashKeys().linkedHashSetValues()::build), ImmutableSetMultimap::copyOf);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <K, V> ImmutableSetMultimap<K, V> of() {
/* 156 */     return EmptyImmutableSetMultimap.INSTANCE;
/*     */   }
/*     */ 
/*     */   
/*     */   public static <K, V> ImmutableSetMultimap<K, V> of(K k1, V v1) {
/* 161 */     Builder<K, V> builder = builder();
/* 162 */     builder.put(k1, v1);
/* 163 */     return builder.build();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <K, V> ImmutableSetMultimap<K, V> of(K k1, V v1, K k2, V v2) {
/* 171 */     Builder<K, V> builder = builder();
/* 172 */     builder.put(k1, v1);
/* 173 */     builder.put(k2, v2);
/* 174 */     return builder.build();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <K, V> ImmutableSetMultimap<K, V> of(K k1, V v1, K k2, V v2, K k3, V v3) {
/* 182 */     Builder<K, V> builder = builder();
/* 183 */     builder.put(k1, v1);
/* 184 */     builder.put(k2, v2);
/* 185 */     builder.put(k3, v3);
/* 186 */     return builder.build();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <K, V> ImmutableSetMultimap<K, V> of(K k1, V v1, K k2, V v2, K k3, V v3, K k4, V v4) {
/* 195 */     Builder<K, V> builder = builder();
/* 196 */     builder.put(k1, v1);
/* 197 */     builder.put(k2, v2);
/* 198 */     builder.put(k3, v3);
/* 199 */     builder.put(k4, v4);
/* 200 */     return builder.build();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <K, V> ImmutableSetMultimap<K, V> of(K k1, V v1, K k2, V v2, K k3, V v3, K k4, V v4, K k5, V v5) {
/* 209 */     Builder<K, V> builder = builder();
/* 210 */     builder.put(k1, v1);
/* 211 */     builder.put(k2, v2);
/* 212 */     builder.put(k3, v3);
/* 213 */     builder.put(k4, v4);
/* 214 */     builder.put(k5, v5);
/* 215 */     return builder.build();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <K, V> Builder<K, V> builder() {
/* 222 */     return new Builder<>();
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
/*     */   public static final class Builder<K, V>
/*     */     extends ImmutableMultimap.Builder<K, V>
/*     */   {
/*     */     Collection<V> newMutableValueCollection() {
/* 255 */       return Platform.preservesInsertionOrderOnAddsSet();
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     @CanIgnoreReturnValue
/*     */     public Builder<K, V> put(K key, V value) {
/* 262 */       super.put(key, value);
/* 263 */       return this;
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
/* 274 */       super.put(entry);
/* 275 */       return this;
/*     */     }
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
/* 287 */       super.putAll(entries);
/* 288 */       return this;
/*     */     }
/*     */ 
/*     */     
/*     */     @CanIgnoreReturnValue
/*     */     public Builder<K, V> putAll(K key, Iterable<? extends V> values) {
/* 294 */       super.putAll(key, values);
/* 295 */       return this;
/*     */     }
/*     */ 
/*     */     
/*     */     @CanIgnoreReturnValue
/*     */     public Builder<K, V> putAll(K key, V... values) {
/* 301 */       return putAll(key, Arrays.asList(values));
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     @CanIgnoreReturnValue
/*     */     public Builder<K, V> putAll(Multimap<? extends K, ? extends V> multimap) {
/* 308 */       for (Map.Entry<? extends K, ? extends Collection<? extends V>> entry : (Iterable<Map.Entry<? extends K, ? extends Collection<? extends V>>>)multimap.asMap().entrySet()) {
/* 309 */         putAll(entry.getKey(), entry.getValue());
/*     */       }
/* 311 */       return this;
/*     */     }
/*     */ 
/*     */     
/*     */     @CanIgnoreReturnValue
/*     */     Builder<K, V> combine(ImmutableMultimap.Builder<K, V> other) {
/* 317 */       super.combine(other);
/* 318 */       return this;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     @CanIgnoreReturnValue
/*     */     public Builder<K, V> orderKeysBy(Comparator<? super K> keyComparator) {
/* 329 */       super.orderKeysBy(keyComparator);
/* 330 */       return this;
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
/*     */     @CanIgnoreReturnValue
/*     */     public Builder<K, V> orderValuesBy(Comparator<? super V> valueComparator) {
/* 347 */       super.orderValuesBy(valueComparator);
/* 348 */       return this;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public ImmutableSetMultimap<K, V> build() {
/* 354 */       Collection<Map.Entry<K, Collection<V>>> mapEntries = this.builderMap.entrySet();
/* 355 */       if (this.keyComparator != null) {
/* 356 */         mapEntries = Ordering.<K>from(this.keyComparator).onKeys().immutableSortedCopy(mapEntries);
/*     */       }
/* 358 */       return ImmutableSetMultimap.fromMapEntries(mapEntries, this.valueComparator);
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
/*     */   public static <K, V> ImmutableSetMultimap<K, V> copyOf(Multimap<? extends K, ? extends V> multimap) {
/* 376 */     return copyOf(multimap, (Comparator<? super V>)null);
/*     */   }
/*     */ 
/*     */   
/*     */   private static <K, V> ImmutableSetMultimap<K, V> copyOf(Multimap<? extends K, ? extends V> multimap, Comparator<? super V> valueComparator) {
/* 381 */     Preconditions.checkNotNull(multimap);
/* 382 */     if (multimap.isEmpty() && valueComparator == null) {
/* 383 */       return of();
/*     */     }
/*     */     
/* 386 */     if (multimap instanceof ImmutableSetMultimap) {
/*     */       
/* 388 */       ImmutableSetMultimap<K, V> kvMultimap = (ImmutableSetMultimap)multimap;
/* 389 */       if (!kvMultimap.isPartialView()) {
/* 390 */         return kvMultimap;
/*     */       }
/*     */     } 
/*     */     
/* 394 */     return fromMapEntries(multimap.asMap().entrySet(), valueComparator);
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
/*     */   public static <K, V> ImmutableSetMultimap<K, V> copyOf(Iterable<? extends Map.Entry<? extends K, ? extends V>> entries) {
/* 409 */     return (new Builder<>()).putAll(entries).build();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static <K, V> ImmutableSetMultimap<K, V> fromMapEntries(Collection<? extends Map.Entry<? extends K, ? extends Collection<? extends V>>> mapEntries, Comparator<? super V> valueComparator) {
/* 416 */     if (mapEntries.isEmpty()) {
/* 417 */       return of();
/*     */     }
/*     */     
/* 420 */     ImmutableMap.Builder<K, ImmutableSet<V>> builder = new ImmutableMap.Builder<>(mapEntries.size());
/* 421 */     int size = 0;
/*     */     
/* 423 */     for (Map.Entry<? extends K, ? extends Collection<? extends V>> entry : mapEntries) {
/* 424 */       K key = entry.getKey();
/* 425 */       Collection<? extends V> values = entry.getValue();
/* 426 */       ImmutableSet<V> set = valueSet(valueComparator, values);
/* 427 */       if (!set.isEmpty()) {
/* 428 */         builder.put(key, set);
/* 429 */         size += set.size();
/*     */       } 
/*     */     } 
/*     */     
/* 433 */     return new ImmutableSetMultimap<>(builder.build(), size, valueComparator);
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
/*     */   ImmutableSetMultimap(ImmutableMap<K, ImmutableSet<V>> map, int size, Comparator<? super V> valueComparator) {
/* 446 */     super((ImmutableMap)map, size);
/* 447 */     this.emptySet = emptySet(valueComparator);
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
/*     */   public ImmutableSet<V> get(K key) {
/* 460 */     ImmutableSet<V> set = (ImmutableSet<V>)this.map.get(key);
/* 461 */     return (ImmutableSet<V>)MoreObjects.firstNonNull(set, this.emptySet);
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
/*     */   public ImmutableSetMultimap<V, K> inverse() {
/* 475 */     ImmutableSetMultimap<V, K> result = this.inverse;
/* 476 */     return (result == null) ? (this.inverse = invert()) : result;
/*     */   }
/*     */   
/*     */   private ImmutableSetMultimap<V, K> invert() {
/* 480 */     Builder<V, K> builder = builder();
/* 481 */     for (UnmodifiableIterator<Map.Entry<K, V>> unmodifiableIterator = entries().iterator(); unmodifiableIterator.hasNext(); ) { Map.Entry<K, V> entry = unmodifiableIterator.next();
/* 482 */       builder.put(entry.getValue(), entry.getKey()); }
/*     */     
/* 484 */     ImmutableSetMultimap<V, K> invertedMultimap = builder.build();
/* 485 */     invertedMultimap.inverse = this;
/* 486 */     return invertedMultimap;
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
/*     */   public ImmutableSet<V> removeAll(Object key) {
/* 499 */     throw new UnsupportedOperationException();
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
/*     */   public ImmutableSet<V> replaceValues(K key, Iterable<? extends V> values) {
/* 512 */     throw new UnsupportedOperationException();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ImmutableSet<Map.Entry<K, V>> entries() {
/* 523 */     ImmutableSet<Map.Entry<K, V>> result = this.entries;
/* 524 */     return (result == null) ? (this.entries = new EntrySet<>(this)) : result;
/*     */   }
/*     */   
/*     */   private static final class EntrySet<K, V> extends ImmutableSet<Map.Entry<K, V>> { @Weak
/*     */     private final transient ImmutableSetMultimap<K, V> multimap;
/*     */     
/*     */     EntrySet(ImmutableSetMultimap<K, V> multimap) {
/* 531 */       this.multimap = multimap;
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean contains(Object object) {
/* 536 */       if (object instanceof Map.Entry) {
/* 537 */         Map.Entry<?, ?> entry = (Map.Entry<?, ?>)object;
/* 538 */         return this.multimap.containsEntry(entry.getKey(), entry.getValue());
/*     */       } 
/* 540 */       return false;
/*     */     }
/*     */ 
/*     */     
/*     */     public int size() {
/* 545 */       return this.multimap.size();
/*     */     }
/*     */ 
/*     */     
/*     */     public UnmodifiableIterator<Map.Entry<K, V>> iterator() {
/* 550 */       return this.multimap.entryIterator();
/*     */     }
/*     */ 
/*     */     
/*     */     boolean isPartialView() {
/* 555 */       return false;
/*     */     } }
/*     */ 
/*     */ 
/*     */   
/*     */   private static <V> ImmutableSet<V> valueSet(Comparator<? super V> valueComparator, Collection<? extends V> values) {
/* 561 */     return (valueComparator == null) ? 
/* 562 */       ImmutableSet.<V>copyOf(values) : 
/* 563 */       ImmutableSortedSet.<V>copyOf(valueComparator, values);
/*     */   }
/*     */   
/*     */   private static <V> ImmutableSet<V> emptySet(Comparator<? super V> valueComparator) {
/* 567 */     return (valueComparator == null) ? 
/* 568 */       ImmutableSet.<V>of() : 
/* 569 */       ImmutableSortedSet.<V>emptySet(valueComparator);
/*     */   }
/*     */ 
/*     */   
/*     */   private static <V> ImmutableSet.Builder<V> valuesBuilder(Comparator<? super V> valueComparator) {
/* 574 */     return (valueComparator == null) ? new ImmutableSet.Builder<>() : new ImmutableSortedSet.Builder<>(valueComparator);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @GwtIncompatible
/*     */   private void writeObject(ObjectOutputStream stream) throws IOException {
/* 585 */     stream.defaultWriteObject();
/* 586 */     stream.writeObject(valueComparator());
/* 587 */     Serialization.writeMultimap(this, stream);
/*     */   }
/*     */ 
/*     */   
/*     */   Comparator<? super V> valueComparator() {
/* 592 */     return (this.emptySet instanceof ImmutableSortedSet) ? ((ImmutableSortedSet<V>)this.emptySet)
/* 593 */       .comparator() : null;
/*     */   }
/*     */ 
/*     */   
/*     */   @GwtIncompatible
/*     */   private static final class SetFieldSettersHolder
/*     */   {
/* 600 */     static final Serialization.FieldSetter<ImmutableSetMultimap> EMPTY_SET_FIELD_SETTER = Serialization.getFieldSetter(ImmutableSetMultimap.class, "emptySet");
/*     */   }
/*     */ 
/*     */   
/*     */   @GwtIncompatible
/*     */   private void readObject(ObjectInputStream stream) throws IOException, ClassNotFoundException {
/*     */     ImmutableMap<Object, ImmutableSet<Object>> tmpMap;
/* 607 */     stream.defaultReadObject();
/* 608 */     Comparator<Object> valueComparator = (Comparator<Object>)stream.readObject();
/* 609 */     int keyCount = stream.readInt();
/* 610 */     if (keyCount < 0) {
/* 611 */       throw new InvalidObjectException("Invalid key count " + keyCount);
/*     */     }
/* 613 */     ImmutableMap.Builder<Object, ImmutableSet<Object>> builder = ImmutableMap.builder();
/* 614 */     int tmpSize = 0;
/*     */     
/* 616 */     for (int i = 0; i < keyCount; i++) {
/* 617 */       Object key = stream.readObject();
/* 618 */       int valueCount = stream.readInt();
/* 619 */       if (valueCount <= 0) {
/* 620 */         throw new InvalidObjectException("Invalid value count " + valueCount);
/*     */       }
/*     */       
/* 623 */       ImmutableSet.Builder<Object> valuesBuilder = valuesBuilder(valueComparator);
/* 624 */       for (int j = 0; j < valueCount; j++) {
/* 625 */         valuesBuilder.add(stream.readObject());
/*     */       }
/* 627 */       ImmutableSet<Object> valueSet = valuesBuilder.build();
/* 628 */       if (valueSet.size() != valueCount) {
/* 629 */         throw new InvalidObjectException("Duplicate key-value pairs exist for key " + key);
/*     */       }
/* 631 */       builder.put(key, valueSet);
/* 632 */       tmpSize += valueCount;
/*     */     } 
/*     */ 
/*     */     
/*     */     try {
/* 637 */       tmpMap = builder.build();
/* 638 */     } catch (IllegalArgumentException e) {
/* 639 */       throw (InvalidObjectException)(new InvalidObjectException(e.getMessage())).initCause(e);
/*     */     } 
/*     */     
/* 642 */     ImmutableMultimap.FieldSettersHolder.MAP_FIELD_SETTER.set(this, tmpMap);
/* 643 */     ImmutableMultimap.FieldSettersHolder.SIZE_FIELD_SETTER.set(this, tmpSize);
/* 644 */     SetFieldSettersHolder.EMPTY_SET_FIELD_SETTER.set(this, emptySet(valueComparator));
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\google\common\collect\ImmutableSetMultimap.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */