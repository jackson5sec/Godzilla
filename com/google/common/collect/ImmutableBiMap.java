/*     */ package com.google.common.collect;
/*     */ 
/*     */ import com.google.common.annotations.Beta;
/*     */ import com.google.common.annotations.GwtCompatible;
/*     */ import com.google.common.annotations.VisibleForTesting;
/*     */ import com.google.common.base.Preconditions;
/*     */ import com.google.errorprone.annotations.CanIgnoreReturnValue;
/*     */ import java.util.Arrays;
/*     */ import java.util.Collection;
/*     */ import java.util.Comparator;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import java.util.function.Function;
/*     */ import java.util.stream.Collector;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
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
/*     */ public abstract class ImmutableBiMap<K, V>
/*     */   extends ImmutableBiMapFauxverideShim<K, V>
/*     */   implements BiMap<K, V>
/*     */ {
/*     */   public static <T, K, V> Collector<T, ?, ImmutableBiMap<K, V>> toImmutableBiMap(Function<? super T, ? extends K> keyFunction, Function<? super T, ? extends V> valueFunction) {
/*  59 */     return CollectCollectors.toImmutableBiMap(keyFunction, valueFunction);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <K, V> ImmutableBiMap<K, V> of() {
/*  66 */     return (ImmutableBiMap)RegularImmutableBiMap.EMPTY;
/*     */   }
/*     */ 
/*     */   
/*     */   public static <K, V> ImmutableBiMap<K, V> of(K k1, V v1) {
/*  71 */     return new SingletonImmutableBiMap<>(k1, v1);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <K, V> ImmutableBiMap<K, V> of(K k1, V v1, K k2, V v2) {
/*  80 */     return RegularImmutableBiMap.fromEntries((Map.Entry<K, V>[])new Map.Entry[] { entryOf(k1, v1), entryOf(k2, v2) });
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <K, V> ImmutableBiMap<K, V> of(K k1, V v1, K k2, V v2, K k3, V v3) {
/*  89 */     return RegularImmutableBiMap.fromEntries((Map.Entry<K, V>[])new Map.Entry[] { entryOf(k1, v1), entryOf(k2, v2), entryOf(k3, v3) });
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <K, V> ImmutableBiMap<K, V> of(K k1, V v1, K k2, V v2, K k3, V v3, K k4, V v4) {
/*  98 */     return RegularImmutableBiMap.fromEntries((Map.Entry<K, V>[])new Map.Entry[] {
/*  99 */           entryOf(k1, v1), entryOf(k2, v2), entryOf(k3, v3), entryOf(k4, v4)
/*     */         });
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <K, V> ImmutableBiMap<K, V> of(K k1, V v1, K k2, V v2, K k3, V v3, K k4, V v4, K k5, V v5) {
/* 109 */     return RegularImmutableBiMap.fromEntries((Map.Entry<K, V>[])new Map.Entry[] {
/* 110 */           entryOf(k1, v1), entryOf(k2, v2), entryOf(k3, v3), entryOf(k4, v4), entryOf(k5, v5)
/*     */         });
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <K, V> Builder<K, V> builder() {
/* 120 */     return new Builder<>();
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
/* 137 */     CollectPreconditions.checkNonnegative(expectedSize, "expectedSize");
/* 138 */     return new Builder<>(expectedSize);
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
/*     */   public static final class Builder<K, V>
/*     */     extends ImmutableMap.Builder<K, V>
/*     */   {
/*     */     public Builder() {}
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     Builder(int size) {
/* 178 */       super(size);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     @CanIgnoreReturnValue
/*     */     public Builder<K, V> put(K key, V value) {
/* 188 */       super.put(key, value);
/* 189 */       return this;
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
/*     */     public Builder<K, V> put(Map.Entry<? extends K, ? extends V> entry) {
/* 201 */       super.put(entry);
/* 202 */       return this;
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
/*     */     public Builder<K, V> putAll(Map<? extends K, ? extends V> map) {
/* 214 */       super.putAll(map);
/* 215 */       return this;
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
/*     */     @Beta
/*     */     public Builder<K, V> putAll(Iterable<? extends Map.Entry<? extends K, ? extends V>> entries) {
/* 229 */       super.putAll(entries);
/* 230 */       return this;
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
/*     */     @CanIgnoreReturnValue
/*     */     @Beta
/*     */     public Builder<K, V> orderEntriesByValue(Comparator<? super V> valueComparator) {
/* 247 */       super.orderEntriesByValue(valueComparator);
/* 248 */       return this;
/*     */     }
/*     */ 
/*     */     
/*     */     @CanIgnoreReturnValue
/*     */     Builder<K, V> combine(ImmutableMap.Builder<K, V> builder) {
/* 254 */       super.combine(builder);
/* 255 */       return this;
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
/*     */     public ImmutableBiMap<K, V> build() {
/* 267 */       switch (this.size) {
/*     */         case 0:
/* 269 */           return ImmutableBiMap.of();
/*     */         case 1:
/* 271 */           return ImmutableBiMap.of(this.entries[0].getKey(), this.entries[0].getValue());
/*     */       } 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 280 */       if (this.valueComparator != null) {
/* 281 */         if (this.entriesUsed) {
/* 282 */           this.entries = Arrays.<Map.Entry<K, V>>copyOf(this.entries, this.size);
/*     */         }
/* 284 */         Arrays.sort(this.entries, 0, this.size, 
/*     */ 
/*     */ 
/*     */             
/* 288 */             Ordering.<V>from(this.valueComparator).onResultOf(Maps.valueFunction()));
/*     */       } 
/* 290 */       this.entriesUsed = true;
/* 291 */       return RegularImmutableBiMap.fromEntryArray(this.size, this.entries);
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     @VisibleForTesting
/*     */     ImmutableBiMap<K, V> buildJdkBacked() {
/* 298 */       Preconditions.checkState((this.valueComparator == null), "buildJdkBacked is for tests only, doesn't support orderEntriesByValue");
/*     */ 
/*     */       
/* 301 */       switch (this.size) {
/*     */         case 0:
/* 303 */           return ImmutableBiMap.of();
/*     */         case 1:
/* 305 */           return ImmutableBiMap.of(this.entries[0].getKey(), this.entries[0].getValue());
/*     */       } 
/* 307 */       this.entriesUsed = true;
/* 308 */       return RegularImmutableBiMap.fromEntryArray(this.size, this.entries);
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
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <K, V> ImmutableBiMap<K, V> copyOf(Map<? extends K, ? extends V> map) {
/* 330 */     if (map instanceof ImmutableBiMap) {
/*     */       
/* 332 */       ImmutableBiMap<K, V> bimap = (ImmutableBiMap)map;
/*     */ 
/*     */       
/* 335 */       if (!bimap.isPartialView()) {
/* 336 */         return bimap;
/*     */       }
/*     */     } 
/* 339 */     return copyOf(map.entrySet());
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
/*     */   public static <K, V> ImmutableBiMap<K, V> copyOf(Iterable<? extends Map.Entry<? extends K, ? extends V>> entries) {
/*     */     Map.Entry<K, V> entry;
/* 355 */     Map.Entry[] arrayOfEntry = Iterables.<Map.Entry>toArray((Iterable)entries, (Map.Entry[])EMPTY_ENTRY_ARRAY);
/* 356 */     switch (arrayOfEntry.length) {
/*     */       case 0:
/* 358 */         return of();
/*     */       case 1:
/* 360 */         entry = arrayOfEntry[0];
/* 361 */         return of(entry.getKey(), entry.getValue());
/*     */     } 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 367 */     return RegularImmutableBiMap.fromEntries((Map.Entry<K, V>[])arrayOfEntry);
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
/*     */   public ImmutableSet<V> values() {
/* 387 */     return inverse().keySet();
/*     */   }
/*     */ 
/*     */   
/*     */   final ImmutableSet<V> createValues() {
/* 392 */     throw new AssertionError("should never be called");
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
/*     */   public V forcePut(K key, V value) {
/* 405 */     throw new UnsupportedOperationException();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static class SerializedForm
/*     */     extends ImmutableMap.SerializedForm
/*     */   {
/*     */     private static final long serialVersionUID = 0L;
/*     */ 
/*     */ 
/*     */     
/*     */     SerializedForm(ImmutableBiMap<?, ?> bimap) {
/* 418 */       super(bimap);
/*     */     }
/*     */ 
/*     */     
/*     */     Object readResolve() {
/* 423 */       ImmutableBiMap.Builder<Object, Object> builder = new ImmutableBiMap.Builder<>();
/* 424 */       return createMap(builder);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   Object writeReplace() {
/* 432 */     return new SerializedForm(this);
/*     */   }
/*     */   
/*     */   public abstract ImmutableBiMap<V, K> inverse();
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\google\common\collect\ImmutableBiMap.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */