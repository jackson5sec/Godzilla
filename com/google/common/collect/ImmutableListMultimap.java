/*     */ package com.google.common.collect;
/*     */ 
/*     */ import com.google.common.annotations.Beta;
/*     */ import com.google.common.annotations.GwtCompatible;
/*     */ import com.google.common.annotations.GwtIncompatible;
/*     */ import com.google.common.base.Preconditions;
/*     */ import com.google.errorprone.annotations.CanIgnoreReturnValue;
/*     */ import com.google.errorprone.annotations.concurrent.LazyInit;
/*     */ import com.google.j2objc.annotations.RetainedWith;
/*     */ import java.io.IOException;
/*     */ import java.io.InvalidObjectException;
/*     */ import java.io.ObjectInputStream;
/*     */ import java.io.ObjectOutputStream;
/*     */ import java.util.Collection;
/*     */ import java.util.Comparator;
/*     */ import java.util.List;
/*     */ import java.util.Map;
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
/*     */ 
/*     */ 
/*     */ @GwtCompatible(serializable = true, emulated = true)
/*     */ public class ImmutableListMultimap<K, V>
/*     */   extends ImmutableMultimap<K, V>
/*     */   implements ListMultimap<K, V>
/*     */ {
/*     */   @LazyInit
/*     */   @RetainedWith
/*     */   private transient ImmutableListMultimap<V, K> inverse;
/*     */   @GwtIncompatible
/*     */   private static final long serialVersionUID = 0L;
/*     */   
/*     */   public static <T, K, V> Collector<T, ?, ImmutableListMultimap<K, V>> toImmutableListMultimap(Function<? super T, ? extends K> keyFunction, Function<? super T, ? extends V> valueFunction) {
/*  85 */     Preconditions.checkNotNull(keyFunction, "keyFunction");
/*  86 */     Preconditions.checkNotNull(valueFunction, "valueFunction");
/*  87 */     return Collector.of(ImmutableListMultimap::builder, (builder, t) -> builder.put(keyFunction.apply(t), valueFunction.apply(t)), Builder::combine, Builder::build, new Collector.Characteristics[0]);
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
/*     */   public static <T, K, V> Collector<T, ?, ImmutableListMultimap<K, V>> flatteningToImmutableListMultimap(Function<? super T, ? extends K> keyFunction, Function<? super T, ? extends Stream<? extends V>> valuesFunction) {
/* 129 */     Preconditions.checkNotNull(keyFunction);
/* 130 */     Preconditions.checkNotNull(valuesFunction);
/* 131 */     return Collectors.collectingAndThen(
/* 132 */         Multimaps.flatteningToMultimap(input -> Preconditions.checkNotNull(keyFunction.apply(input)), input -> ((Stream)valuesFunction.apply(input)).peek(Preconditions::checkNotNull), 
/*     */ 
/*     */           
/* 135 */           MultimapBuilder.linkedHashKeys().arrayListValues()::build), ImmutableListMultimap::copyOf);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <K, V> ImmutableListMultimap<K, V> of() {
/* 143 */     return EmptyImmutableListMultimap.INSTANCE;
/*     */   }
/*     */ 
/*     */   
/*     */   public static <K, V> ImmutableListMultimap<K, V> of(K k1, V v1) {
/* 148 */     Builder<K, V> builder = builder();
/* 149 */     builder.put(k1, v1);
/* 150 */     return builder.build();
/*     */   }
/*     */ 
/*     */   
/*     */   public static <K, V> ImmutableListMultimap<K, V> of(K k1, V v1, K k2, V v2) {
/* 155 */     Builder<K, V> builder = builder();
/* 156 */     builder.put(k1, v1);
/* 157 */     builder.put(k2, v2);
/* 158 */     return builder.build();
/*     */   }
/*     */ 
/*     */   
/*     */   public static <K, V> ImmutableListMultimap<K, V> of(K k1, V v1, K k2, V v2, K k3, V v3) {
/* 163 */     Builder<K, V> builder = builder();
/* 164 */     builder.put(k1, v1);
/* 165 */     builder.put(k2, v2);
/* 166 */     builder.put(k3, v3);
/* 167 */     return builder.build();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static <K, V> ImmutableListMultimap<K, V> of(K k1, V v1, K k2, V v2, K k3, V v3, K k4, V v4) {
/* 173 */     Builder<K, V> builder = builder();
/* 174 */     builder.put(k1, v1);
/* 175 */     builder.put(k2, v2);
/* 176 */     builder.put(k3, v3);
/* 177 */     builder.put(k4, v4);
/* 178 */     return builder.build();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static <K, V> ImmutableListMultimap<K, V> of(K k1, V v1, K k2, V v2, K k3, V v3, K k4, V v4, K k5, V v5) {
/* 184 */     Builder<K, V> builder = builder();
/* 185 */     builder.put(k1, v1);
/* 186 */     builder.put(k2, v2);
/* 187 */     builder.put(k3, v3);
/* 188 */     builder.put(k4, v4);
/* 189 */     builder.put(k5, v5);
/* 190 */     return builder.build();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <K, V> Builder<K, V> builder() {
/* 200 */     return new Builder<>();
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
/*     */   public static final class Builder<K, V>
/*     */     extends ImmutableMultimap.Builder<K, V>
/*     */   {
/*     */     @CanIgnoreReturnValue
/*     */     public Builder<K, V> put(K key, V value) {
/* 232 */       super.put(key, value);
/* 233 */       return this;
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
/* 244 */       super.put(entry);
/* 245 */       return this;
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
/* 257 */       super.putAll(entries);
/* 258 */       return this;
/*     */     }
/*     */ 
/*     */     
/*     */     @CanIgnoreReturnValue
/*     */     public Builder<K, V> putAll(K key, Iterable<? extends V> values) {
/* 264 */       super.putAll(key, values);
/* 265 */       return this;
/*     */     }
/*     */ 
/*     */     
/*     */     @CanIgnoreReturnValue
/*     */     public Builder<K, V> putAll(K key, V... values) {
/* 271 */       super.putAll(key, values);
/* 272 */       return this;
/*     */     }
/*     */ 
/*     */     
/*     */     @CanIgnoreReturnValue
/*     */     public Builder<K, V> putAll(Multimap<? extends K, ? extends V> multimap) {
/* 278 */       super.putAll(multimap);
/* 279 */       return this;
/*     */     }
/*     */ 
/*     */     
/*     */     @CanIgnoreReturnValue
/*     */     Builder<K, V> combine(ImmutableMultimap.Builder<K, V> other) {
/* 285 */       super.combine(other);
/* 286 */       return this;
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
/* 297 */       super.orderKeysBy(keyComparator);
/* 298 */       return this;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     @CanIgnoreReturnValue
/*     */     public Builder<K, V> orderValuesBy(Comparator<? super V> valueComparator) {
/* 309 */       super.orderValuesBy(valueComparator);
/* 310 */       return this;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public ImmutableListMultimap<K, V> build() {
/* 316 */       return (ImmutableListMultimap<K, V>)super.build();
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
/*     */   public static <K, V> ImmutableListMultimap<K, V> copyOf(Multimap<? extends K, ? extends V> multimap) {
/* 333 */     if (multimap.isEmpty()) {
/* 334 */       return of();
/*     */     }
/*     */ 
/*     */     
/* 338 */     if (multimap instanceof ImmutableListMultimap) {
/*     */       
/* 340 */       ImmutableListMultimap<K, V> kvMultimap = (ImmutableListMultimap)multimap;
/* 341 */       if (!kvMultimap.isPartialView()) {
/* 342 */         return kvMultimap;
/*     */       }
/*     */     } 
/*     */     
/* 346 */     return fromMapEntries(multimap.asMap().entrySet(), (Comparator<? super V>)null);
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
/*     */   public static <K, V> ImmutableListMultimap<K, V> copyOf(Iterable<? extends Map.Entry<? extends K, ? extends V>> entries) {
/* 360 */     return (new Builder<>()).putAll(entries).build();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static <K, V> ImmutableListMultimap<K, V> fromMapEntries(Collection<? extends Map.Entry<? extends K, ? extends Collection<? extends V>>> mapEntries, Comparator<? super V> valueComparator) {
/* 367 */     if (mapEntries.isEmpty()) {
/* 368 */       return of();
/*     */     }
/*     */     
/* 371 */     ImmutableMap.Builder<K, ImmutableList<V>> builder = new ImmutableMap.Builder<>(mapEntries.size());
/* 372 */     int size = 0;
/*     */     
/* 374 */     for (Map.Entry<? extends K, ? extends Collection<? extends V>> entry : mapEntries) {
/* 375 */       K key = entry.getKey();
/* 376 */       Collection<? extends V> values = entry.getValue();
/*     */ 
/*     */ 
/*     */       
/* 380 */       ImmutableList<V> list = (valueComparator == null) ? ImmutableList.<V>copyOf(values) : ImmutableList.<V>sortedCopyOf(valueComparator, values);
/* 381 */       if (!list.isEmpty()) {
/* 382 */         builder.put(key, list);
/* 383 */         size += list.size();
/*     */       } 
/*     */     } 
/*     */     
/* 387 */     return new ImmutableListMultimap<>(builder.build(), size);
/*     */   }
/*     */   
/*     */   ImmutableListMultimap(ImmutableMap<K, ImmutableList<V>> map, int size) {
/* 391 */     super((ImmutableMap)map, size);
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
/*     */   public ImmutableList<V> get(K key) {
/* 404 */     ImmutableList<V> list = (ImmutableList<V>)this.map.get(key);
/* 405 */     return (list == null) ? ImmutableList.<V>of() : list;
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
/*     */   public ImmutableListMultimap<V, K> inverse() {
/* 421 */     ImmutableListMultimap<V, K> result = this.inverse;
/* 422 */     return (result == null) ? (this.inverse = invert()) : result;
/*     */   }
/*     */   
/*     */   private ImmutableListMultimap<V, K> invert() {
/* 426 */     Builder<V, K> builder = builder();
/* 427 */     for (UnmodifiableIterator<Map.Entry<K, V>> unmodifiableIterator = entries().iterator(); unmodifiableIterator.hasNext(); ) { Map.Entry<K, V> entry = unmodifiableIterator.next();
/* 428 */       builder.put(entry.getValue(), entry.getKey()); }
/*     */     
/* 430 */     ImmutableListMultimap<V, K> invertedMultimap = builder.build();
/* 431 */     invertedMultimap.inverse = this;
/* 432 */     return invertedMultimap;
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
/*     */   public ImmutableList<V> removeAll(Object key) {
/* 445 */     throw new UnsupportedOperationException();
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
/*     */   public ImmutableList<V> replaceValues(K key, Iterable<? extends V> values) {
/* 458 */     throw new UnsupportedOperationException();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @GwtIncompatible
/*     */   private void writeObject(ObjectOutputStream stream) throws IOException {
/* 467 */     stream.defaultWriteObject();
/* 468 */     Serialization.writeMultimap(this, stream);
/*     */   }
/*     */   @GwtIncompatible
/*     */   private void readObject(ObjectInputStream stream) throws IOException, ClassNotFoundException {
/*     */     ImmutableMap<Object, ImmutableList<Object>> tmpMap;
/* 473 */     stream.defaultReadObject();
/* 474 */     int keyCount = stream.readInt();
/* 475 */     if (keyCount < 0) {
/* 476 */       throw new InvalidObjectException("Invalid key count " + keyCount);
/*     */     }
/* 478 */     ImmutableMap.Builder<Object, ImmutableList<Object>> builder = ImmutableMap.builder();
/* 479 */     int tmpSize = 0;
/*     */     
/* 481 */     for (int i = 0; i < keyCount; i++) {
/* 482 */       Object key = stream.readObject();
/* 483 */       int valueCount = stream.readInt();
/* 484 */       if (valueCount <= 0) {
/* 485 */         throw new InvalidObjectException("Invalid value count " + valueCount);
/*     */       }
/*     */       
/* 488 */       ImmutableList.Builder<Object> valuesBuilder = ImmutableList.builder();
/* 489 */       for (int j = 0; j < valueCount; j++) {
/* 490 */         valuesBuilder.add(stream.readObject());
/*     */       }
/* 492 */       builder.put(key, valuesBuilder.build());
/* 493 */       tmpSize += valueCount;
/*     */     } 
/*     */ 
/*     */     
/*     */     try {
/* 498 */       tmpMap = builder.build();
/* 499 */     } catch (IllegalArgumentException e) {
/* 500 */       throw (InvalidObjectException)(new InvalidObjectException(e.getMessage())).initCause(e);
/*     */     } 
/*     */     
/* 503 */     ImmutableMultimap.FieldSettersHolder.MAP_FIELD_SETTER.set(this, tmpMap);
/* 504 */     ImmutableMultimap.FieldSettersHolder.SIZE_FIELD_SETTER.set(this, tmpSize);
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\google\common\collect\ImmutableListMultimap.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */