/*     */ package com.google.common.collect;
/*     */ 
/*     */ import com.google.common.annotations.Beta;
/*     */ import com.google.common.annotations.GwtIncompatible;
/*     */ import com.google.common.base.Function;
/*     */ import com.google.common.base.Preconditions;
/*     */ import com.google.errorprone.annotations.CanIgnoreReturnValue;
/*     */ import java.io.Serializable;
/*     */ import java.util.Collections;
/*     */ import java.util.Comparator;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.NoSuchElementException;
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
/*     */ @Beta
/*     */ @GwtIncompatible
/*     */ public class ImmutableRangeMap<K extends Comparable<?>, V>
/*     */   implements RangeMap<K, V>, Serializable
/*     */ {
/*  47 */   private static final ImmutableRangeMap<Comparable<?>, Object> EMPTY = new ImmutableRangeMap(
/*  48 */       ImmutableList.of(), ImmutableList.of());
/*     */ 
/*     */   
/*     */   private final transient ImmutableList<Range<K>> ranges;
/*     */ 
/*     */   
/*     */   private final transient ImmutableList<V> values;
/*     */   
/*     */   private static final long serialVersionUID = 0L;
/*     */ 
/*     */   
/*     */   public static <T, K extends Comparable<? super K>, V> Collector<T, ?, ImmutableRangeMap<K, V>> toImmutableRangeMap(Function<? super T, Range<K>> keyFunction, Function<? super T, ? extends V> valueFunction) {
/*  60 */     return CollectCollectors.toImmutableRangeMap(keyFunction, valueFunction);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static <K extends Comparable<?>, V> ImmutableRangeMap<K, V> of() {
/*  66 */     return (ImmutableRangeMap)EMPTY;
/*     */   }
/*     */ 
/*     */   
/*     */   public static <K extends Comparable<?>, V> ImmutableRangeMap<K, V> of(Range<K> range, V value) {
/*  71 */     return new ImmutableRangeMap<>(ImmutableList.of(range), ImmutableList.of(value));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static <K extends Comparable<?>, V> ImmutableRangeMap<K, V> copyOf(RangeMap<K, ? extends V> rangeMap) {
/*  77 */     if (rangeMap instanceof ImmutableRangeMap) {
/*  78 */       return (ImmutableRangeMap)rangeMap;
/*     */     }
/*  80 */     Map<Range<K>, ? extends V> map = rangeMap.asMapOfRanges();
/*  81 */     ImmutableList.Builder<Range<K>> rangesBuilder = new ImmutableList.Builder<>(map.size());
/*  82 */     ImmutableList.Builder<V> valuesBuilder = new ImmutableList.Builder<>(map.size());
/*  83 */     for (Map.Entry<Range<K>, ? extends V> entry : map.entrySet()) {
/*  84 */       rangesBuilder.add(entry.getKey());
/*  85 */       valuesBuilder.add(entry.getValue());
/*     */     } 
/*  87 */     return new ImmutableRangeMap<>(rangesBuilder.build(), valuesBuilder.build());
/*     */   }
/*     */ 
/*     */   
/*     */   public static <K extends Comparable<?>, V> Builder<K, V> builder() {
/*  92 */     return new Builder<>();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static final class Builder<K extends Comparable<?>, V>
/*     */   {
/* 104 */     private final List<Map.Entry<Range<K>, V>> entries = Lists.newArrayList();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     @CanIgnoreReturnValue
/*     */     public Builder<K, V> put(Range<K> range, V value) {
/* 114 */       Preconditions.checkNotNull(range);
/* 115 */       Preconditions.checkNotNull(value);
/* 116 */       Preconditions.checkArgument(!range.isEmpty(), "Range must not be empty, but was %s", range);
/* 117 */       this.entries.add(Maps.immutableEntry(range, value));
/* 118 */       return this;
/*     */     }
/*     */ 
/*     */     
/*     */     @CanIgnoreReturnValue
/*     */     public Builder<K, V> putAll(RangeMap<K, ? extends V> rangeMap) {
/* 124 */       for (Map.Entry<Range<K>, ? extends V> entry : (Iterable<Map.Entry<Range<K>, ? extends V>>)rangeMap.asMapOfRanges().entrySet()) {
/* 125 */         put(entry.getKey(), entry.getValue());
/*     */       }
/* 127 */       return this;
/*     */     }
/*     */     
/*     */     @CanIgnoreReturnValue
/*     */     Builder<K, V> combine(Builder<K, V> builder) {
/* 132 */       this.entries.addAll(builder.entries);
/* 133 */       return this;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public ImmutableRangeMap<K, V> build() {
/* 143 */       Collections.sort(this.entries, Range.<Comparable<?>>rangeLexOrdering().onKeys());
/* 144 */       ImmutableList.Builder<Range<K>> rangesBuilder = new ImmutableList.Builder<>(this.entries.size());
/* 145 */       ImmutableList.Builder<V> valuesBuilder = new ImmutableList.Builder<>(this.entries.size());
/* 146 */       for (int i = 0; i < this.entries.size(); i++) {
/* 147 */         Range<K> range = (Range<K>)((Map.Entry)this.entries.get(i)).getKey();
/* 148 */         if (i > 0) {
/* 149 */           Range<K> prevRange = (Range<K>)((Map.Entry)this.entries.get(i - 1)).getKey();
/* 150 */           if (range.isConnected(prevRange) && !range.intersection(prevRange).isEmpty()) {
/* 151 */             throw new IllegalArgumentException("Overlapping ranges: range " + prevRange + " overlaps with entry " + range);
/*     */           }
/*     */         } 
/*     */         
/* 155 */         rangesBuilder.add(range);
/* 156 */         valuesBuilder.add((V)((Map.Entry)this.entries.get(i)).getValue());
/*     */       } 
/* 158 */       return new ImmutableRangeMap<>(rangesBuilder.build(), valuesBuilder.build());
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   ImmutableRangeMap(ImmutableList<Range<K>> ranges, ImmutableList<V> values) {
/* 166 */     this.ranges = ranges;
/* 167 */     this.values = values;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public V get(K key) {
/* 173 */     int index = SortedLists.binarySearch(this.ranges, 
/*     */         
/* 175 */         (Function)Range.lowerBoundFn(), 
/* 176 */         Cut.belowValue(key), SortedLists.KeyPresentBehavior.ANY_PRESENT, SortedLists.KeyAbsentBehavior.NEXT_LOWER);
/*     */ 
/*     */     
/* 179 */     if (index == -1) {
/* 180 */       return null;
/*     */     }
/* 182 */     Range<K> range = this.ranges.get(index);
/* 183 */     return range.contains(key) ? this.values.get(index) : null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Map.Entry<Range<K>, V> getEntry(K key) {
/* 190 */     int index = SortedLists.binarySearch(this.ranges, 
/*     */         
/* 192 */         (Function)Range.lowerBoundFn(), 
/* 193 */         Cut.belowValue(key), SortedLists.KeyPresentBehavior.ANY_PRESENT, SortedLists.KeyAbsentBehavior.NEXT_LOWER);
/*     */ 
/*     */     
/* 196 */     if (index == -1) {
/* 197 */       return null;
/*     */     }
/* 199 */     Range<K> range = this.ranges.get(index);
/* 200 */     return range.contains(key) ? Maps.<Range<K>, V>immutableEntry(range, this.values.get(index)) : null;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Range<K> span() {
/* 206 */     if (this.ranges.isEmpty()) {
/* 207 */       throw new NoSuchElementException();
/*     */     }
/* 209 */     Range<K> firstRange = this.ranges.get(0);
/* 210 */     Range<K> lastRange = this.ranges.get(this.ranges.size() - 1);
/* 211 */     return Range.create(firstRange.lowerBound, lastRange.upperBound);
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
/*     */   public void put(Range<K> range, V value) {
/* 223 */     throw new UnsupportedOperationException();
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
/*     */   public void putCoalescing(Range<K> range, V value) {
/* 235 */     throw new UnsupportedOperationException();
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
/*     */   public void putAll(RangeMap<K, V> rangeMap) {
/* 247 */     throw new UnsupportedOperationException();
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
/* 259 */     throw new UnsupportedOperationException();
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
/*     */   public void remove(Range<K> range) {
/* 271 */     throw new UnsupportedOperationException();
/*     */   }
/*     */ 
/*     */   
/*     */   public ImmutableMap<Range<K>, V> asMapOfRanges() {
/* 276 */     if (this.ranges.isEmpty()) {
/* 277 */       return ImmutableMap.of();
/*     */     }
/*     */     
/* 280 */     RegularImmutableSortedSet<Range<K>> rangeSet = new RegularImmutableSortedSet<>(this.ranges, (Comparator)Range.rangeLexOrdering());
/* 281 */     return new ImmutableSortedMap<>(rangeSet, this.values);
/*     */   }
/*     */ 
/*     */   
/*     */   public ImmutableMap<Range<K>, V> asDescendingMapOfRanges() {
/* 286 */     if (this.ranges.isEmpty()) {
/* 287 */       return ImmutableMap.of();
/*     */     }
/*     */     
/* 290 */     RegularImmutableSortedSet<Range<K>> rangeSet = new RegularImmutableSortedSet<>(this.ranges.reverse(), Range.<Comparable<?>>rangeLexOrdering().reverse());
/* 291 */     return new ImmutableSortedMap<>(rangeSet, this.values.reverse());
/*     */   }
/*     */ 
/*     */   
/*     */   public ImmutableRangeMap<K, V> subRangeMap(final Range<K> range) {
/* 296 */     if (((Range)Preconditions.checkNotNull(range)).isEmpty())
/* 297 */       return of(); 
/* 298 */     if (this.ranges.isEmpty() || range.encloses(span())) {
/* 299 */       return this;
/*     */     }
/*     */     
/* 302 */     int lowerIndex = SortedLists.binarySearch(this.ranges, 
/*     */         
/* 304 */         Range.upperBoundFn(), range.lowerBound, SortedLists.KeyPresentBehavior.FIRST_AFTER, SortedLists.KeyAbsentBehavior.NEXT_HIGHER);
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 309 */     int upperIndex = SortedLists.binarySearch(this.ranges, 
/*     */         
/* 311 */         Range.lowerBoundFn(), range.upperBound, SortedLists.KeyPresentBehavior.ANY_PRESENT, SortedLists.KeyAbsentBehavior.NEXT_HIGHER);
/*     */ 
/*     */ 
/*     */     
/* 315 */     if (lowerIndex >= upperIndex) {
/* 316 */       return of();
/*     */     }
/* 318 */     final int off = lowerIndex;
/* 319 */     final int len = upperIndex - lowerIndex;
/* 320 */     ImmutableList<Range<K>> subRanges = new ImmutableList<Range<K>>()
/*     */       {
/*     */         public int size()
/*     */         {
/* 324 */           return len;
/*     */         }
/*     */ 
/*     */         
/*     */         public Range<K> get(int index) {
/* 329 */           Preconditions.checkElementIndex(index, len);
/* 330 */           if (index == 0 || index == len - 1) {
/* 331 */             return ((Range<K>)ImmutableRangeMap.this.ranges.get(index + off)).intersection(range);
/*     */           }
/* 333 */           return ImmutableRangeMap.this.ranges.get(index + off);
/*     */         }
/*     */ 
/*     */ 
/*     */         
/*     */         boolean isPartialView() {
/* 339 */           return true;
/*     */         }
/*     */       };
/* 342 */     final ImmutableRangeMap<K, V> outer = this;
/* 343 */     return new ImmutableRangeMap<K, V>(subRanges, this.values.subList(lowerIndex, upperIndex))
/*     */       {
/*     */         public ImmutableRangeMap<K, V> subRangeMap(Range<K> subRange) {
/* 346 */           if (range.isConnected(subRange)) {
/* 347 */             return outer.subRangeMap(subRange.intersection(range));
/*     */           }
/* 349 */           return ImmutableRangeMap.of();
/*     */         }
/*     */       };
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 357 */     return asMapOfRanges().hashCode();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean equals(Object o) {
/* 362 */     if (o instanceof RangeMap) {
/* 363 */       RangeMap<?, ?> rangeMap = (RangeMap<?, ?>)o;
/* 364 */       return asMapOfRanges().equals(rangeMap.asMapOfRanges());
/*     */     } 
/* 366 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 371 */     return asMapOfRanges().toString();
/*     */   }
/*     */ 
/*     */   
/*     */   private static class SerializedForm<K extends Comparable<?>, V>
/*     */     implements Serializable
/*     */   {
/*     */     private final ImmutableMap<Range<K>, V> mapOfRanges;
/*     */     
/*     */     private static final long serialVersionUID = 0L;
/*     */     
/*     */     SerializedForm(ImmutableMap<Range<K>, V> mapOfRanges) {
/* 383 */       this.mapOfRanges = mapOfRanges;
/*     */     }
/*     */     
/*     */     Object readResolve() {
/* 387 */       if (this.mapOfRanges.isEmpty()) {
/* 388 */         return ImmutableRangeMap.of();
/*     */       }
/* 390 */       return createRangeMap();
/*     */     }
/*     */ 
/*     */     
/*     */     Object createRangeMap() {
/* 395 */       ImmutableRangeMap.Builder<K, V> builder = new ImmutableRangeMap.Builder<>();
/* 396 */       for (UnmodifiableIterator<Map.Entry<Range<K>, V>> unmodifiableIterator = this.mapOfRanges.entrySet().iterator(); unmodifiableIterator.hasNext(); ) { Map.Entry<Range<K>, V> entry = unmodifiableIterator.next();
/* 397 */         builder.put(entry.getKey(), entry.getValue()); }
/*     */       
/* 399 */       return builder.build();
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   Object writeReplace() {
/* 406 */     return new SerializedForm<>(asMapOfRanges());
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\google\common\collect\ImmutableRangeMap.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */