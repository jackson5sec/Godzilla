/*     */ package com.google.common.collect;
/*     */ 
/*     */ import com.google.common.annotations.Beta;
/*     */ import com.google.common.annotations.GwtIncompatible;
/*     */ import com.google.common.base.MoreObjects;
/*     */ import com.google.common.base.Preconditions;
/*     */ import com.google.common.base.Predicate;
/*     */ import com.google.common.base.Predicates;
/*     */ import java.util.AbstractMap;
/*     */ import java.util.Collection;
/*     */ import java.util.Collections;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.NavigableMap;
/*     */ import java.util.NoSuchElementException;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @Beta
/*     */ @GwtIncompatible
/*     */ public final class TreeRangeMap<K extends Comparable, V>
/*     */   implements RangeMap<K, V>
/*     */ {
/*     */   private final NavigableMap<Cut<K>, RangeMapEntry<K, V>> entriesByLowerBound;
/*     */   
/*     */   public static <K extends Comparable, V> TreeRangeMap<K, V> create() {
/*  58 */     return new TreeRangeMap<>();
/*     */   }
/*     */   
/*     */   private TreeRangeMap() {
/*  62 */     this.entriesByLowerBound = Maps.newTreeMap();
/*     */   }
/*     */   
/*     */   private static final class RangeMapEntry<K extends Comparable, V>
/*     */     extends AbstractMapEntry<Range<K>, V> {
/*     */     private final Range<K> range;
/*     */     private final V value;
/*     */     
/*     */     RangeMapEntry(Cut<K> lowerBound, Cut<K> upperBound, V value) {
/*  71 */       this(Range.create(lowerBound, upperBound), value);
/*     */     }
/*     */     
/*     */     RangeMapEntry(Range<K> range, V value) {
/*  75 */       this.range = range;
/*  76 */       this.value = value;
/*     */     }
/*     */ 
/*     */     
/*     */     public Range<K> getKey() {
/*  81 */       return this.range;
/*     */     }
/*     */ 
/*     */     
/*     */     public V getValue() {
/*  86 */       return this.value;
/*     */     }
/*     */     
/*     */     public boolean contains(K value) {
/*  90 */       return this.range.contains(value);
/*     */     }
/*     */     
/*     */     Cut<K> getLowerBound() {
/*  94 */       return this.range.lowerBound;
/*     */     }
/*     */     
/*     */     Cut<K> getUpperBound() {
/*  98 */       return this.range.upperBound;
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public V get(K key) {
/* 104 */     Map.Entry<Range<K>, V> entry = getEntry(key);
/* 105 */     return (entry == null) ? null : entry.getValue();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Map.Entry<Range<K>, V> getEntry(K key) {
/* 111 */     Map.Entry<Cut<K>, RangeMapEntry<K, V>> mapEntry = this.entriesByLowerBound.floorEntry(Cut.belowValue(key));
/* 112 */     if (mapEntry != null && ((RangeMapEntry)mapEntry.getValue()).contains(key)) {
/* 113 */       return mapEntry.getValue();
/*     */     }
/* 115 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void put(Range<K> range, V value) {
/* 122 */     if (!range.isEmpty()) {
/* 123 */       Preconditions.checkNotNull(value);
/* 124 */       remove(range);
/* 125 */       this.entriesByLowerBound.put(range.lowerBound, new RangeMapEntry<>(range, value));
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void putCoalescing(Range<K> range, V value) {
/* 131 */     if (this.entriesByLowerBound.isEmpty()) {
/* 132 */       put(range, value);
/*     */       
/*     */       return;
/*     */     } 
/* 136 */     Range<K> coalescedRange = coalescedRange(range, (V)Preconditions.checkNotNull(value));
/* 137 */     put(coalescedRange, value);
/*     */   }
/*     */ 
/*     */   
/*     */   private Range<K> coalescedRange(Range<K> range, V value) {
/* 142 */     Range<K> coalescedRange = range;
/*     */     
/* 144 */     Map.Entry<Cut<K>, RangeMapEntry<K, V>> lowerEntry = this.entriesByLowerBound.lowerEntry(range.lowerBound);
/* 145 */     coalescedRange = coalesce(coalescedRange, value, lowerEntry);
/*     */ 
/*     */     
/* 148 */     Map.Entry<Cut<K>, RangeMapEntry<K, V>> higherEntry = this.entriesByLowerBound.floorEntry(range.upperBound);
/* 149 */     coalescedRange = coalesce(coalescedRange, value, higherEntry);
/*     */     
/* 151 */     return coalescedRange;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static <K extends Comparable, V> Range<K> coalesce(Range<K> range, V value, Map.Entry<Cut<K>, RangeMapEntry<K, V>> entry) {
/* 157 */     if (entry != null && ((RangeMapEntry)entry
/* 158 */       .getValue()).getKey().isConnected(range) && ((RangeMapEntry)entry
/* 159 */       .getValue()).getValue().equals(value)) {
/* 160 */       return range.span(((RangeMapEntry)entry.getValue()).getKey());
/*     */     }
/* 162 */     return range;
/*     */   }
/*     */ 
/*     */   
/*     */   public void putAll(RangeMap<K, V> rangeMap) {
/* 167 */     for (Map.Entry<Range<K>, V> entry : (Iterable<Map.Entry<Range<K>, V>>)rangeMap.asMapOfRanges().entrySet()) {
/* 168 */       put(entry.getKey(), entry.getValue());
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public void clear() {
/* 174 */     this.entriesByLowerBound.clear();
/*     */   }
/*     */ 
/*     */   
/*     */   public Range<K> span() {
/* 179 */     Map.Entry<Cut<K>, RangeMapEntry<K, V>> firstEntry = this.entriesByLowerBound.firstEntry();
/* 180 */     Map.Entry<Cut<K>, RangeMapEntry<K, V>> lastEntry = this.entriesByLowerBound.lastEntry();
/* 181 */     if (firstEntry == null) {
/* 182 */       throw new NoSuchElementException();
/*     */     }
/* 184 */     return Range.create(
/* 185 */         (((RangeMapEntry)firstEntry.getValue()).getKey()).lowerBound, (((RangeMapEntry)lastEntry.getValue()).getKey()).upperBound);
/*     */   }
/*     */   
/*     */   private void putRangeMapEntry(Cut<K> lowerBound, Cut<K> upperBound, V value) {
/* 189 */     this.entriesByLowerBound.put(lowerBound, new RangeMapEntry<>(lowerBound, upperBound, value));
/*     */   }
/*     */ 
/*     */   
/*     */   public void remove(Range<K> rangeToRemove) {
/* 194 */     if (rangeToRemove.isEmpty()) {
/*     */       return;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 203 */     Map.Entry<Cut<K>, RangeMapEntry<K, V>> mapEntryBelowToTruncate = this.entriesByLowerBound.lowerEntry(rangeToRemove.lowerBound);
/* 204 */     if (mapEntryBelowToTruncate != null) {
/*     */       
/* 206 */       RangeMapEntry<K, V> rangeMapEntry = mapEntryBelowToTruncate.getValue();
/* 207 */       if (rangeMapEntry.getUpperBound().compareTo(rangeToRemove.lowerBound) > 0) {
/*     */         
/* 209 */         if (rangeMapEntry.getUpperBound().compareTo(rangeToRemove.upperBound) > 0)
/*     */         {
/*     */           
/* 212 */           putRangeMapEntry(rangeToRemove.upperBound, rangeMapEntry
/*     */               
/* 214 */               .getUpperBound(), (V)((RangeMapEntry)mapEntryBelowToTruncate
/* 215 */               .getValue()).getValue());
/*     */         }
/*     */         
/* 218 */         putRangeMapEntry(rangeMapEntry
/* 219 */             .getLowerBound(), rangeToRemove.lowerBound, (V)((RangeMapEntry)mapEntryBelowToTruncate
/*     */             
/* 221 */             .getValue()).getValue());
/*     */       } 
/*     */     } 
/*     */ 
/*     */     
/* 226 */     Map.Entry<Cut<K>, RangeMapEntry<K, V>> mapEntryAboveToTruncate = this.entriesByLowerBound.lowerEntry(rangeToRemove.upperBound);
/* 227 */     if (mapEntryAboveToTruncate != null) {
/*     */       
/* 229 */       RangeMapEntry<K, V> rangeMapEntry = mapEntryAboveToTruncate.getValue();
/* 230 */       if (rangeMapEntry.getUpperBound().compareTo(rangeToRemove.upperBound) > 0)
/*     */       {
/*     */         
/* 233 */         putRangeMapEntry(rangeToRemove.upperBound, rangeMapEntry
/*     */             
/* 235 */             .getUpperBound(), (V)((RangeMapEntry)mapEntryAboveToTruncate
/* 236 */             .getValue()).getValue());
/*     */       }
/*     */     } 
/* 239 */     this.entriesByLowerBound.subMap(rangeToRemove.lowerBound, rangeToRemove.upperBound).clear();
/*     */   }
/*     */ 
/*     */   
/*     */   public Map<Range<K>, V> asMapOfRanges() {
/* 244 */     return new AsMapOfRanges(this.entriesByLowerBound.values());
/*     */   }
/*     */ 
/*     */   
/*     */   public Map<Range<K>, V> asDescendingMapOfRanges() {
/* 249 */     return new AsMapOfRanges(this.entriesByLowerBound.descendingMap().values());
/*     */   }
/*     */   
/*     */   private final class AsMapOfRanges
/*     */     extends Maps.IteratorBasedAbstractMap<Range<K>, V>
/*     */   {
/*     */     final Iterable<Map.Entry<Range<K>, V>> entryIterable;
/*     */     
/*     */     AsMapOfRanges(Iterable<TreeRangeMap.RangeMapEntry<K, V>> entryIterable) {
/* 258 */       this.entryIterable = (Iterable)entryIterable;
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean containsKey(Object key) {
/* 263 */       return (get(key) != null);
/*     */     }
/*     */ 
/*     */     
/*     */     public V get(Object key) {
/* 268 */       if (key instanceof Range) {
/* 269 */         Range<?> range = (Range)key;
/* 270 */         TreeRangeMap.RangeMapEntry<K, V> rangeMapEntry = (TreeRangeMap.RangeMapEntry<K, V>)TreeRangeMap.this.entriesByLowerBound.get(range.lowerBound);
/* 271 */         if (rangeMapEntry != null && rangeMapEntry.getKey().equals(range)) {
/* 272 */           return rangeMapEntry.getValue();
/*     */         }
/*     */       } 
/* 275 */       return null;
/*     */     }
/*     */ 
/*     */     
/*     */     public int size() {
/* 280 */       return TreeRangeMap.this.entriesByLowerBound.size();
/*     */     }
/*     */ 
/*     */     
/*     */     Iterator<Map.Entry<Range<K>, V>> entryIterator() {
/* 285 */       return this.entryIterable.iterator();
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public RangeMap<K, V> subRangeMap(Range<K> subRange) {
/* 291 */     if (subRange.equals(Range.all())) {
/* 292 */       return this;
/*     */     }
/* 294 */     return new SubRangeMap(subRange);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private RangeMap<K, V> emptySubRangeMap() {
/* 300 */     return EMPTY_SUB_RANGE_MAP;
/*     */   }
/*     */   
/* 303 */   private static final RangeMap EMPTY_SUB_RANGE_MAP = new RangeMap<Comparable, Object>()
/*     */     {
/*     */       public Object get(Comparable key)
/*     */       {
/* 307 */         return null;
/*     */       }
/*     */ 
/*     */       
/*     */       public Map.Entry<Range, Object> getEntry(Comparable key) {
/* 312 */         return null;
/*     */       }
/*     */ 
/*     */       
/*     */       public Range span() {
/* 317 */         throw new NoSuchElementException();
/*     */       }
/*     */ 
/*     */       
/*     */       public void put(Range range, Object value) {
/* 322 */         Preconditions.checkNotNull(range);
/* 323 */         throw new IllegalArgumentException("Cannot insert range " + range + " into an empty subRangeMap");
/*     */       }
/*     */ 
/*     */ 
/*     */       
/*     */       public void putCoalescing(Range range, Object value) {
/* 329 */         Preconditions.checkNotNull(range);
/* 330 */         throw new IllegalArgumentException("Cannot insert range " + range + " into an empty subRangeMap");
/*     */       }
/*     */ 
/*     */ 
/*     */       
/*     */       public void putAll(RangeMap rangeMap) {
/* 336 */         if (!rangeMap.asMapOfRanges().isEmpty()) {
/* 337 */           throw new IllegalArgumentException("Cannot putAll(nonEmptyRangeMap) into an empty subRangeMap");
/*     */         }
/*     */       }
/*     */ 
/*     */ 
/*     */       
/*     */       public void clear() {}
/*     */ 
/*     */       
/*     */       public void remove(Range range) {
/* 347 */         Preconditions.checkNotNull(range);
/*     */       }
/*     */ 
/*     */       
/*     */       public Map<Range, Object> asMapOfRanges() {
/* 352 */         return Collections.emptyMap();
/*     */       }
/*     */ 
/*     */       
/*     */       public Map<Range, Object> asDescendingMapOfRanges() {
/* 357 */         return Collections.emptyMap();
/*     */       }
/*     */ 
/*     */       
/*     */       public RangeMap subRangeMap(Range range) {
/* 362 */         Preconditions.checkNotNull(range);
/* 363 */         return this;
/*     */       }
/*     */     };
/*     */   
/*     */   private class SubRangeMap
/*     */     implements RangeMap<K, V> {
/*     */     private final Range<K> subRange;
/*     */     
/*     */     SubRangeMap(Range<K> subRange) {
/* 372 */       this.subRange = subRange;
/*     */     }
/*     */ 
/*     */     
/*     */     public V get(K key) {
/* 377 */       return this.subRange.contains(key) ? TreeRangeMap.this.get(key) : null;
/*     */     }
/*     */ 
/*     */     
/*     */     public Map.Entry<Range<K>, V> getEntry(K key) {
/* 382 */       if (this.subRange.contains(key)) {
/* 383 */         Map.Entry<Range<K>, V> entry = TreeRangeMap.this.getEntry(key);
/* 384 */         if (entry != null) {
/* 385 */           return Maps.immutableEntry(((Range<K>)entry.getKey()).intersection(this.subRange), entry.getValue());
/*     */         }
/*     */       } 
/* 388 */       return null;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public Range<K> span() {
/*     */       Cut<K> lowerBound, upperBound;
/* 395 */       Map.Entry<Cut<K>, TreeRangeMap.RangeMapEntry<K, V>> lowerEntry = TreeRangeMap.this.entriesByLowerBound.floorEntry(this.subRange.lowerBound);
/* 396 */       if (lowerEntry != null && ((TreeRangeMap.RangeMapEntry)lowerEntry
/* 397 */         .getValue()).getUpperBound().compareTo(this.subRange.lowerBound) > 0) {
/* 398 */         lowerBound = this.subRange.lowerBound;
/*     */       } else {
/* 400 */         lowerBound = (Cut<K>)TreeRangeMap.this.entriesByLowerBound.ceilingKey(this.subRange.lowerBound);
/* 401 */         if (lowerBound == null || lowerBound.compareTo(this.subRange.upperBound) >= 0) {
/* 402 */           throw new NoSuchElementException();
/*     */         }
/*     */       } 
/*     */ 
/*     */ 
/*     */       
/* 408 */       Map.Entry<Cut<K>, TreeRangeMap.RangeMapEntry<K, V>> upperEntry = TreeRangeMap.this.entriesByLowerBound.lowerEntry(this.subRange.upperBound);
/* 409 */       if (upperEntry == null)
/* 410 */         throw new NoSuchElementException(); 
/* 411 */       if (((TreeRangeMap.RangeMapEntry)upperEntry.getValue()).getUpperBound().compareTo(this.subRange.upperBound) >= 0) {
/* 412 */         upperBound = this.subRange.upperBound;
/*     */       } else {
/* 414 */         upperBound = ((TreeRangeMap.RangeMapEntry)upperEntry.getValue()).getUpperBound();
/*     */       } 
/* 416 */       return Range.create(lowerBound, upperBound);
/*     */     }
/*     */ 
/*     */     
/*     */     public void put(Range<K> range, V value) {
/* 421 */       Preconditions.checkArgument(this.subRange
/* 422 */           .encloses(range), "Cannot put range %s into a subRangeMap(%s)", range, this.subRange);
/* 423 */       TreeRangeMap.this.put(range, value);
/*     */     }
/*     */ 
/*     */     
/*     */     public void putCoalescing(Range<K> range, V value) {
/* 428 */       if (TreeRangeMap.this.entriesByLowerBound.isEmpty() || range.isEmpty() || !this.subRange.encloses(range)) {
/* 429 */         put(range, value);
/*     */         
/*     */         return;
/*     */       } 
/* 433 */       Range<K> coalescedRange = TreeRangeMap.this.coalescedRange(range, (V)Preconditions.checkNotNull(value));
/*     */       
/* 435 */       put(coalescedRange.intersection(this.subRange), value);
/*     */     }
/*     */ 
/*     */     
/*     */     public void putAll(RangeMap<K, V> rangeMap) {
/* 440 */       if (rangeMap.asMapOfRanges().isEmpty()) {
/*     */         return;
/*     */       }
/* 443 */       Range<K> span = rangeMap.span();
/* 444 */       Preconditions.checkArgument(this.subRange
/* 445 */           .encloses(span), "Cannot putAll rangeMap with span %s into a subRangeMap(%s)", span, this.subRange);
/*     */ 
/*     */ 
/*     */       
/* 449 */       TreeRangeMap.this.putAll(rangeMap);
/*     */     }
/*     */ 
/*     */     
/*     */     public void clear() {
/* 454 */       TreeRangeMap.this.remove(this.subRange);
/*     */     }
/*     */ 
/*     */     
/*     */     public void remove(Range<K> range) {
/* 459 */       if (range.isConnected(this.subRange)) {
/* 460 */         TreeRangeMap.this.remove(range.intersection(this.subRange));
/*     */       }
/*     */     }
/*     */ 
/*     */     
/*     */     public RangeMap<K, V> subRangeMap(Range<K> range) {
/* 466 */       if (!range.isConnected(this.subRange)) {
/* 467 */         return TreeRangeMap.this.emptySubRangeMap();
/*     */       }
/* 469 */       return TreeRangeMap.this.subRangeMap(range.intersection(this.subRange));
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public Map<Range<K>, V> asMapOfRanges() {
/* 475 */       return new SubRangeMapAsMap();
/*     */     }
/*     */ 
/*     */     
/*     */     public Map<Range<K>, V> asDescendingMapOfRanges() {
/* 480 */       return new SubRangeMapAsMap()
/*     */         {
/*     */           Iterator<Map.Entry<Range<K>, V>> entryIterator()
/*     */           {
/* 484 */             if (TreeRangeMap.SubRangeMap.this.subRange.isEmpty()) {
/* 485 */               return Iterators.emptyIterator();
/*     */             }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */             
/* 492 */             final Iterator<TreeRangeMap.RangeMapEntry<K, V>> backingItr = TreeRangeMap.this.entriesByLowerBound.headMap(TreeRangeMap.SubRangeMap.this.subRange.upperBound, false).descendingMap().values().iterator();
/* 493 */             return new AbstractIterator<Map.Entry<Range<K>, V>>()
/*     */               {
/*     */                 protected Map.Entry<Range<K>, V> computeNext()
/*     */                 {
/* 497 */                   if (backingItr.hasNext()) {
/* 498 */                     TreeRangeMap.RangeMapEntry<K, V> entry = backingItr.next();
/* 499 */                     if (entry.getUpperBound().compareTo(TreeRangeMap.SubRangeMap.this.subRange.lowerBound) <= 0) {
/* 500 */                       return endOfData();
/*     */                     }
/* 502 */                     return Maps.immutableEntry(entry.getKey().intersection(TreeRangeMap.SubRangeMap.this.subRange), entry.getValue());
/*     */                   } 
/* 504 */                   return endOfData();
/*     */                 }
/*     */               };
/*     */           }
/*     */         };
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean equals(Object o) {
/* 513 */       if (o instanceof RangeMap) {
/* 514 */         RangeMap<?, ?> rangeMap = (RangeMap<?, ?>)o;
/* 515 */         return asMapOfRanges().equals(rangeMap.asMapOfRanges());
/*     */       } 
/* 517 */       return false;
/*     */     }
/*     */ 
/*     */     
/*     */     public int hashCode() {
/* 522 */       return asMapOfRanges().hashCode();
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString() {
/* 527 */       return asMapOfRanges().toString();
/*     */     }
/*     */     
/*     */     class SubRangeMapAsMap
/*     */       extends AbstractMap<Range<K>, V>
/*     */     {
/*     */       public boolean containsKey(Object key) {
/* 534 */         return (get(key) != null);
/*     */       }
/*     */ 
/*     */       
/*     */       public V get(Object key) {
/*     */         try {
/* 540 */           if (key instanceof Range) {
/*     */             
/* 542 */             Range<K> r = (Range<K>)key;
/* 543 */             if (!TreeRangeMap.SubRangeMap.this.subRange.encloses(r) || r.isEmpty()) {
/* 544 */               return null;
/*     */             }
/* 546 */             TreeRangeMap.RangeMapEntry<K, V> candidate = null;
/* 547 */             if (r.lowerBound.compareTo(TreeRangeMap.SubRangeMap.this.subRange.lowerBound) == 0) {
/*     */ 
/*     */               
/* 550 */               Map.Entry<Cut<K>, TreeRangeMap.RangeMapEntry<K, V>> entry = TreeRangeMap.this.entriesByLowerBound.floorEntry(r.lowerBound);
/* 551 */               if (entry != null) {
/* 552 */                 candidate = entry.getValue();
/*     */               }
/*     */             } else {
/* 555 */               candidate = (TreeRangeMap.RangeMapEntry<K, V>)TreeRangeMap.this.entriesByLowerBound.get(r.lowerBound);
/*     */             } 
/*     */             
/* 558 */             if (candidate != null && candidate
/* 559 */               .getKey().isConnected(TreeRangeMap.SubRangeMap.this.subRange) && candidate
/* 560 */               .getKey().intersection(TreeRangeMap.SubRangeMap.this.subRange).equals(r)) {
/* 561 */               return candidate.getValue();
/*     */             }
/*     */           } 
/* 564 */         } catch (ClassCastException e) {
/* 565 */           return null;
/*     */         } 
/* 567 */         return null;
/*     */       }
/*     */ 
/*     */       
/*     */       public V remove(Object key) {
/* 572 */         V value = get(key);
/* 573 */         if (value != null) {
/*     */           
/* 575 */           Range<K> range = (Range<K>)key;
/* 576 */           TreeRangeMap.this.remove(range);
/* 577 */           return value;
/*     */         } 
/* 579 */         return null;
/*     */       }
/*     */ 
/*     */       
/*     */       public void clear() {
/* 584 */         TreeRangeMap.SubRangeMap.this.clear();
/*     */       }
/*     */       
/*     */       private boolean removeEntryIf(Predicate<? super Map.Entry<Range<K>, V>> predicate) {
/* 588 */         List<Range<K>> toRemove = Lists.newArrayList();
/* 589 */         for (Map.Entry<Range<K>, V> entry : entrySet()) {
/* 590 */           if (predicate.apply(entry)) {
/* 591 */             toRemove.add(entry.getKey());
/*     */           }
/*     */         } 
/* 594 */         for (Range<K> range : toRemove) {
/* 595 */           TreeRangeMap.this.remove(range);
/*     */         }
/* 597 */         return !toRemove.isEmpty();
/*     */       }
/*     */ 
/*     */       
/*     */       public Set<Range<K>> keySet() {
/* 602 */         return (Set)new Maps.KeySet<Range<Range<K>>, V>(this)
/*     */           {
/*     */             public boolean remove(Object o) {
/* 605 */               return (TreeRangeMap.SubRangeMap.SubRangeMapAsMap.this.remove(o) != null);
/*     */             }
/*     */ 
/*     */             
/*     */             public boolean retainAll(Collection<?> c) {
/* 610 */               return TreeRangeMap.SubRangeMap.SubRangeMapAsMap.this.removeEntryIf(Predicates.compose(Predicates.not(Predicates.in(c)), Maps.keyFunction()));
/*     */             }
/*     */           };
/*     */       }
/*     */ 
/*     */       
/*     */       public Set<Map.Entry<Range<K>, V>> entrySet() {
/* 617 */         return (Set)new Maps.EntrySet<Range<Range<K>>, V>()
/*     */           {
/*     */             Map<Range<K>, V> map() {
/* 620 */               return TreeRangeMap.SubRangeMap.SubRangeMapAsMap.this;
/*     */             }
/*     */ 
/*     */             
/*     */             public Iterator<Map.Entry<Range<K>, V>> iterator() {
/* 625 */               return TreeRangeMap.SubRangeMap.SubRangeMapAsMap.this.entryIterator();
/*     */             }
/*     */ 
/*     */             
/*     */             public boolean retainAll(Collection<?> c) {
/* 630 */               return TreeRangeMap.SubRangeMap.SubRangeMapAsMap.this.removeEntryIf(Predicates.not(Predicates.in(c)));
/*     */             }
/*     */ 
/*     */             
/*     */             public int size() {
/* 635 */               return Iterators.size(iterator());
/*     */             }
/*     */ 
/*     */             
/*     */             public boolean isEmpty() {
/* 640 */               return !iterator().hasNext();
/*     */             }
/*     */           };
/*     */       }
/*     */       
/*     */       Iterator<Map.Entry<Range<K>, V>> entryIterator() {
/* 646 */         if (TreeRangeMap.SubRangeMap.this.subRange.isEmpty()) {
/* 647 */           return Iterators.emptyIterator();
/*     */         }
/*     */         
/* 650 */         Cut<K> cutToStart = (Cut<K>)MoreObjects.firstNonNull(TreeRangeMap.this
/* 651 */             .entriesByLowerBound.floorKey(TreeRangeMap.SubRangeMap.this.subRange.lowerBound), TreeRangeMap.SubRangeMap.this.subRange.lowerBound);
/*     */         
/* 653 */         final Iterator<TreeRangeMap.RangeMapEntry<K, V>> backingItr = TreeRangeMap.this.entriesByLowerBound.tailMap(cutToStart, true).values().iterator();
/* 654 */         return new AbstractIterator<Map.Entry<Range<K>, V>>()
/*     */           {
/*     */             protected Map.Entry<Range<K>, V> computeNext()
/*     */             {
/* 658 */               while (backingItr.hasNext()) {
/* 659 */                 TreeRangeMap.RangeMapEntry<K, V> entry = backingItr.next();
/* 660 */                 if (entry.getLowerBound().compareTo(TreeRangeMap.SubRangeMap.this.subRange.upperBound) >= 0)
/* 661 */                   return endOfData(); 
/* 662 */                 if (entry.getUpperBound().compareTo(TreeRangeMap.SubRangeMap.this.subRange.lowerBound) > 0)
/*     */                 {
/* 664 */                   return Maps.immutableEntry(entry.getKey().intersection(TreeRangeMap.SubRangeMap.this.subRange), entry.getValue());
/*     */                 }
/*     */               } 
/* 667 */               return endOfData();
/*     */             }
/*     */           };
/*     */       }
/*     */ 
/*     */       
/*     */       public Collection<V> values() {
/* 674 */         return new Maps.Values<Range<Range<K>>, V>(this)
/*     */           {
/*     */             public boolean removeAll(Collection<?> c) {
/* 677 */               return TreeRangeMap.SubRangeMap.SubRangeMapAsMap.this.removeEntryIf(Predicates.compose(Predicates.in(c), Maps.valueFunction()));
/*     */             }
/*     */ 
/*     */             
/*     */             public boolean retainAll(Collection<?> c) {
/* 682 */               return TreeRangeMap.SubRangeMap.SubRangeMapAsMap.this.removeEntryIf(Predicates.compose(Predicates.not(Predicates.in(c)), Maps.valueFunction()));
/*     */             }
/*     */           };
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean equals(Object o) {
/* 691 */     if (o instanceof RangeMap) {
/* 692 */       RangeMap<?, ?> rangeMap = (RangeMap<?, ?>)o;
/* 693 */       return asMapOfRanges().equals(rangeMap.asMapOfRanges());
/*     */     } 
/* 695 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 700 */     return asMapOfRanges().hashCode();
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 705 */     return this.entriesByLowerBound.values().toString();
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\google\common\collect\TreeRangeMap.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */