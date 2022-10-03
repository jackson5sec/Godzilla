/*     */ package com.google.common.collect;
/*     */ 
/*     */ import com.google.common.annotations.Beta;
/*     */ import com.google.common.annotations.GwtIncompatible;
/*     */ import com.google.common.annotations.VisibleForTesting;
/*     */ import com.google.common.base.MoreObjects;
/*     */ import com.google.common.base.Preconditions;
/*     */ import java.io.Serializable;
/*     */ import java.util.Collection;
/*     */ import java.util.Comparator;
/*     */ import java.util.Iterator;
/*     */ import java.util.Map;
/*     */ import java.util.NavigableMap;
/*     */ import java.util.NoSuchElementException;
/*     */ import java.util.Set;
/*     */ import java.util.TreeMap;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
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
/*     */ public class TreeRangeSet<C extends Comparable<?>>
/*     */   extends AbstractRangeSet<C>
/*     */   implements Serializable
/*     */ {
/*     */   @VisibleForTesting
/*     */   final NavigableMap<Cut<C>, Range<C>> rangesByLowerBound;
/*     */   private transient Set<Range<C>> asRanges;
/*     */   private transient Set<Range<C>> asDescendingSetOfRanges;
/*     */   private transient RangeSet<C> complement;
/*     */   
/*     */   public static <C extends Comparable<?>> TreeRangeSet<C> create() {
/*  51 */     return new TreeRangeSet<>(new TreeMap<>());
/*     */   }
/*     */ 
/*     */   
/*     */   public static <C extends Comparable<?>> TreeRangeSet<C> create(RangeSet<C> rangeSet) {
/*  56 */     TreeRangeSet<C> result = create();
/*  57 */     result.addAll(rangeSet);
/*  58 */     return result;
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
/*     */   public static <C extends Comparable<?>> TreeRangeSet<C> create(Iterable<Range<C>> ranges) {
/*  71 */     TreeRangeSet<C> result = create();
/*  72 */     result.addAll(ranges);
/*  73 */     return result;
/*     */   }
/*     */   
/*     */   private TreeRangeSet(NavigableMap<Cut<C>, Range<C>> rangesByLowerCut) {
/*  77 */     this.rangesByLowerBound = rangesByLowerCut;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Set<Range<C>> asRanges() {
/*  85 */     Set<Range<C>> result = this.asRanges;
/*  86 */     return (result == null) ? (this.asRanges = new AsRanges(this.rangesByLowerBound.values())) : result;
/*     */   }
/*     */ 
/*     */   
/*     */   public Set<Range<C>> asDescendingSetOfRanges() {
/*  91 */     Set<Range<C>> result = this.asDescendingSetOfRanges;
/*  92 */     return (result == null) ? (this
/*  93 */       .asDescendingSetOfRanges = new AsRanges(this.rangesByLowerBound.descendingMap().values())) : result;
/*     */   }
/*     */   
/*     */   final class AsRanges
/*     */     extends ForwardingCollection<Range<C>>
/*     */     implements Set<Range<C>> {
/*     */     final Collection<Range<C>> delegate;
/*     */     
/*     */     AsRanges(Collection<Range<C>> delegate) {
/* 102 */       this.delegate = delegate;
/*     */     }
/*     */ 
/*     */     
/*     */     protected Collection<Range<C>> delegate() {
/* 107 */       return this.delegate;
/*     */     }
/*     */ 
/*     */     
/*     */     public int hashCode() {
/* 112 */       return Sets.hashCodeImpl(this);
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean equals(Object o) {
/* 117 */       return Sets.equalsImpl(this, o);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public Range<C> rangeContaining(C value) {
/* 123 */     Preconditions.checkNotNull(value);
/* 124 */     Map.Entry<Cut<C>, Range<C>> floorEntry = this.rangesByLowerBound.floorEntry((Cut)Cut.belowValue((Comparable)value));
/* 125 */     if (floorEntry != null && ((Range)floorEntry.getValue()).contains(value)) {
/* 126 */       return floorEntry.getValue();
/*     */     }
/*     */     
/* 129 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean intersects(Range<C> range) {
/* 135 */     Preconditions.checkNotNull(range);
/* 136 */     Map.Entry<Cut<C>, Range<C>> ceilingEntry = this.rangesByLowerBound.ceilingEntry(range.lowerBound);
/* 137 */     if (ceilingEntry != null && ((Range)ceilingEntry
/* 138 */       .getValue()).isConnected(range) && 
/* 139 */       !((Range)ceilingEntry.getValue()).intersection(range).isEmpty()) {
/* 140 */       return true;
/*     */     }
/* 142 */     Map.Entry<Cut<C>, Range<C>> priorEntry = this.rangesByLowerBound.lowerEntry(range.lowerBound);
/* 143 */     return (priorEntry != null && ((Range)priorEntry
/* 144 */       .getValue()).isConnected(range) && 
/* 145 */       !((Range)priorEntry.getValue()).intersection(range).isEmpty());
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean encloses(Range<C> range) {
/* 150 */     Preconditions.checkNotNull(range);
/* 151 */     Map.Entry<Cut<C>, Range<C>> floorEntry = this.rangesByLowerBound.floorEntry(range.lowerBound);
/* 152 */     return (floorEntry != null && ((Range)floorEntry.getValue()).encloses(range));
/*     */   }
/*     */   
/*     */   private Range<C> rangeEnclosing(Range<C> range) {
/* 156 */     Preconditions.checkNotNull(range);
/* 157 */     Map.Entry<Cut<C>, Range<C>> floorEntry = this.rangesByLowerBound.floorEntry(range.lowerBound);
/* 158 */     return (floorEntry != null && ((Range)floorEntry.getValue()).encloses(range)) ? floorEntry
/* 159 */       .getValue() : null;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Range<C> span() {
/* 165 */     Map.Entry<Cut<C>, Range<C>> firstEntry = this.rangesByLowerBound.firstEntry();
/* 166 */     Map.Entry<Cut<C>, Range<C>> lastEntry = this.rangesByLowerBound.lastEntry();
/* 167 */     if (firstEntry == null) {
/* 168 */       throw new NoSuchElementException();
/*     */     }
/* 170 */     return Range.create(((Range)firstEntry.getValue()).lowerBound, ((Range)lastEntry.getValue()).upperBound);
/*     */   }
/*     */ 
/*     */   
/*     */   public void add(Range<C> rangeToAdd) {
/* 175 */     Preconditions.checkNotNull(rangeToAdd);
/*     */     
/* 177 */     if (rangeToAdd.isEmpty()) {
/*     */       return;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/* 183 */     Cut<C> lbToAdd = rangeToAdd.lowerBound;
/* 184 */     Cut<C> ubToAdd = rangeToAdd.upperBound;
/*     */     
/* 186 */     Map.Entry<Cut<C>, Range<C>> entryBelowLB = this.rangesByLowerBound.lowerEntry(lbToAdd);
/* 187 */     if (entryBelowLB != null) {
/*     */       
/* 189 */       Range<C> rangeBelowLB = entryBelowLB.getValue();
/* 190 */       if (rangeBelowLB.upperBound.compareTo(lbToAdd) >= 0) {
/*     */         
/* 192 */         if (rangeBelowLB.upperBound.compareTo(ubToAdd) >= 0)
/*     */         {
/* 194 */           ubToAdd = rangeBelowLB.upperBound;
/*     */         }
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 200 */         lbToAdd = rangeBelowLB.lowerBound;
/*     */       } 
/*     */     } 
/*     */     
/* 204 */     Map.Entry<Cut<C>, Range<C>> entryBelowUB = this.rangesByLowerBound.floorEntry(ubToAdd);
/* 205 */     if (entryBelowUB != null) {
/*     */       
/* 207 */       Range<C> rangeBelowUB = entryBelowUB.getValue();
/* 208 */       if (rangeBelowUB.upperBound.compareTo(ubToAdd) >= 0)
/*     */       {
/* 210 */         ubToAdd = rangeBelowUB.upperBound;
/*     */       }
/*     */     } 
/*     */ 
/*     */     
/* 215 */     this.rangesByLowerBound.subMap(lbToAdd, ubToAdd).clear();
/*     */     
/* 217 */     replaceRangeWithSameLowerBound(Range.create(lbToAdd, ubToAdd));
/*     */   }
/*     */ 
/*     */   
/*     */   public void remove(Range<C> rangeToRemove) {
/* 222 */     Preconditions.checkNotNull(rangeToRemove);
/*     */     
/* 224 */     if (rangeToRemove.isEmpty()) {
/*     */       return;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 231 */     Map.Entry<Cut<C>, Range<C>> entryBelowLB = this.rangesByLowerBound.lowerEntry(rangeToRemove.lowerBound);
/* 232 */     if (entryBelowLB != null) {
/*     */       
/* 234 */       Range<C> rangeBelowLB = entryBelowLB.getValue();
/* 235 */       if (rangeBelowLB.upperBound.compareTo(rangeToRemove.lowerBound) >= 0) {
/*     */         
/* 237 */         if (rangeToRemove.hasUpperBound() && rangeBelowLB.upperBound
/* 238 */           .compareTo(rangeToRemove.upperBound) >= 0)
/*     */         {
/* 240 */           replaceRangeWithSameLowerBound(
/* 241 */               Range.create(rangeToRemove.upperBound, rangeBelowLB.upperBound));
/*     */         }
/* 243 */         replaceRangeWithSameLowerBound(
/* 244 */             Range.create(rangeBelowLB.lowerBound, rangeToRemove.lowerBound));
/*     */       } 
/*     */     } 
/*     */     
/* 248 */     Map.Entry<Cut<C>, Range<C>> entryBelowUB = this.rangesByLowerBound.floorEntry(rangeToRemove.upperBound);
/* 249 */     if (entryBelowUB != null) {
/*     */       
/* 251 */       Range<C> rangeBelowUB = entryBelowUB.getValue();
/* 252 */       if (rangeToRemove.hasUpperBound() && rangeBelowUB.upperBound
/* 253 */         .compareTo(rangeToRemove.upperBound) >= 0)
/*     */       {
/* 255 */         replaceRangeWithSameLowerBound(
/* 256 */             Range.create(rangeToRemove.upperBound, rangeBelowUB.upperBound));
/*     */       }
/*     */     } 
/*     */     
/* 260 */     this.rangesByLowerBound.subMap(rangeToRemove.lowerBound, rangeToRemove.upperBound).clear();
/*     */   }
/*     */   
/*     */   private void replaceRangeWithSameLowerBound(Range<C> range) {
/* 264 */     if (range.isEmpty()) {
/* 265 */       this.rangesByLowerBound.remove(range.lowerBound);
/*     */     } else {
/* 267 */       this.rangesByLowerBound.put(range.lowerBound, range);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public RangeSet<C> complement() {
/* 275 */     RangeSet<C> result = this.complement;
/* 276 */     return (result == null) ? (this.complement = new Complement()) : result;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @VisibleForTesting
/*     */   static final class RangesByUpperBound<C extends Comparable<?>>
/*     */     extends AbstractNavigableMap<Cut<C>, Range<C>>
/*     */   {
/*     */     private final NavigableMap<Cut<C>, Range<C>> rangesByLowerBound;
/*     */     
/*     */     private final Range<Cut<C>> upperBoundWindow;
/*     */ 
/*     */     
/*     */     RangesByUpperBound(NavigableMap<Cut<C>, Range<C>> rangesByLowerBound) {
/* 291 */       this.rangesByLowerBound = rangesByLowerBound;
/* 292 */       this.upperBoundWindow = Range.all();
/*     */     }
/*     */ 
/*     */     
/*     */     private RangesByUpperBound(NavigableMap<Cut<C>, Range<C>> rangesByLowerBound, Range<Cut<C>> upperBoundWindow) {
/* 297 */       this.rangesByLowerBound = rangesByLowerBound;
/* 298 */       this.upperBoundWindow = upperBoundWindow;
/*     */     }
/*     */     
/*     */     private NavigableMap<Cut<C>, Range<C>> subMap(Range<Cut<C>> window) {
/* 302 */       if (window.isConnected(this.upperBoundWindow)) {
/* 303 */         return new RangesByUpperBound(this.rangesByLowerBound, window.intersection(this.upperBoundWindow));
/*     */       }
/* 305 */       return ImmutableSortedMap.of();
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public NavigableMap<Cut<C>, Range<C>> subMap(Cut<C> fromKey, boolean fromInclusive, Cut<C> toKey, boolean toInclusive) {
/* 312 */       return subMap(
/* 313 */           Range.range(fromKey, 
/* 314 */             BoundType.forBoolean(fromInclusive), toKey, 
/* 315 */             BoundType.forBoolean(toInclusive)));
/*     */     }
/*     */ 
/*     */     
/*     */     public NavigableMap<Cut<C>, Range<C>> headMap(Cut<C> toKey, boolean inclusive) {
/* 320 */       return subMap(Range.upTo(toKey, BoundType.forBoolean(inclusive)));
/*     */     }
/*     */ 
/*     */     
/*     */     public NavigableMap<Cut<C>, Range<C>> tailMap(Cut<C> fromKey, boolean inclusive) {
/* 325 */       return subMap(Range.downTo(fromKey, BoundType.forBoolean(inclusive)));
/*     */     }
/*     */ 
/*     */     
/*     */     public Comparator<? super Cut<C>> comparator() {
/* 330 */       return Ordering.natural();
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean containsKey(Object key) {
/* 335 */       return (get(key) != null);
/*     */     }
/*     */ 
/*     */     
/*     */     public Range<C> get(Object key) {
/* 340 */       if (key instanceof Cut) {
/*     */         
/*     */         try {
/* 343 */           Cut<C> cut = (Cut<C>)key;
/* 344 */           if (!this.upperBoundWindow.contains(cut)) {
/* 345 */             return null;
/*     */           }
/* 347 */           Map.Entry<Cut<C>, Range<C>> candidate = this.rangesByLowerBound.lowerEntry(cut);
/* 348 */           if (candidate != null && ((Range)candidate.getValue()).upperBound.equals(cut)) {
/* 349 */             return candidate.getValue();
/*     */           }
/* 351 */         } catch (ClassCastException e) {
/* 352 */           return null;
/*     */         } 
/*     */       }
/* 355 */       return null;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     Iterator<Map.Entry<Cut<C>, Range<C>>> entryIterator() {
/*     */       final Iterator<Range<C>> backingItr;
/* 365 */       if (!this.upperBoundWindow.hasLowerBound()) {
/* 366 */         backingItr = this.rangesByLowerBound.values().iterator();
/*     */       } else {
/*     */         
/* 369 */         Map.Entry<Cut<C>, Range<C>> lowerEntry = this.rangesByLowerBound.lowerEntry(this.upperBoundWindow.lowerEndpoint());
/* 370 */         if (lowerEntry == null) {
/* 371 */           backingItr = this.rangesByLowerBound.values().iterator();
/* 372 */         } else if (this.upperBoundWindow.lowerBound.isLessThan(((Range)lowerEntry.getValue()).upperBound)) {
/* 373 */           backingItr = this.rangesByLowerBound.tailMap(lowerEntry.getKey(), true).values().iterator();
/*     */         
/*     */         }
/*     */         else {
/*     */ 
/*     */           
/* 379 */           backingItr = this.rangesByLowerBound.tailMap(this.upperBoundWindow.lowerEndpoint(), true).values().iterator();
/*     */         } 
/*     */       } 
/* 382 */       return new AbstractIterator<Map.Entry<Cut<C>, Range<C>>>()
/*     */         {
/*     */           protected Map.Entry<Cut<C>, Range<C>> computeNext() {
/* 385 */             if (!backingItr.hasNext()) {
/* 386 */               return endOfData();
/*     */             }
/* 388 */             Range<C> range = backingItr.next();
/* 389 */             if (TreeRangeSet.RangesByUpperBound.this.upperBoundWindow.upperBound.isLessThan(range.upperBound)) {
/* 390 */               return endOfData();
/*     */             }
/* 392 */             return Maps.immutableEntry(range.upperBound, range);
/*     */           }
/*     */         };
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     Iterator<Map.Entry<Cut<C>, Range<C>>> descendingEntryIterator() {
/*     */       Collection<Range<C>> candidates;
/* 401 */       if (this.upperBoundWindow.hasUpperBound()) {
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 406 */         candidates = this.rangesByLowerBound.headMap(this.upperBoundWindow.upperEndpoint(), false).descendingMap().values();
/*     */       } else {
/* 408 */         candidates = this.rangesByLowerBound.descendingMap().values();
/*     */       } 
/* 410 */       final PeekingIterator<Range<C>> backingItr = Iterators.peekingIterator(candidates.iterator());
/* 411 */       if (backingItr.hasNext() && this.upperBoundWindow.upperBound
/* 412 */         .isLessThan(((Range)backingItr.peek()).upperBound)) {
/* 413 */         backingItr.next();
/*     */       }
/* 415 */       return new AbstractIterator<Map.Entry<Cut<C>, Range<C>>>()
/*     */         {
/*     */           protected Map.Entry<Cut<C>, Range<C>> computeNext() {
/* 418 */             if (!backingItr.hasNext()) {
/* 419 */               return endOfData();
/*     */             }
/* 421 */             Range<C> range = backingItr.next();
/* 422 */             return TreeRangeSet.RangesByUpperBound.this.upperBoundWindow.lowerBound.isLessThan(range.upperBound) ? 
/* 423 */               Maps.<Cut<C>, Range<C>>immutableEntry(range.upperBound, range) : 
/* 424 */               endOfData();
/*     */           }
/*     */         };
/*     */     }
/*     */ 
/*     */     
/*     */     public int size() {
/* 431 */       if (this.upperBoundWindow.equals(Range.all())) {
/* 432 */         return this.rangesByLowerBound.size();
/*     */       }
/* 434 */       return Iterators.size(entryIterator());
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean isEmpty() {
/* 439 */       return this.upperBoundWindow.equals(Range.all()) ? this.rangesByLowerBound
/* 440 */         .isEmpty() : (
/* 441 */         !entryIterator().hasNext());
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static final class ComplementRangesByLowerBound<C extends Comparable<?>>
/*     */     extends AbstractNavigableMap<Cut<C>, Range<C>>
/*     */   {
/*     */     private final NavigableMap<Cut<C>, Range<C>> positiveRangesByLowerBound;
/*     */     
/*     */     private final NavigableMap<Cut<C>, Range<C>> positiveRangesByUpperBound;
/*     */     
/*     */     private final Range<Cut<C>> complementLowerBoundWindow;
/*     */ 
/*     */     
/*     */     ComplementRangesByLowerBound(NavigableMap<Cut<C>, Range<C>> positiveRangesByLowerBound) {
/* 458 */       this(positiveRangesByLowerBound, Range.all());
/*     */     }
/*     */ 
/*     */     
/*     */     private ComplementRangesByLowerBound(NavigableMap<Cut<C>, Range<C>> positiveRangesByLowerBound, Range<Cut<C>> window) {
/* 463 */       this.positiveRangesByLowerBound = positiveRangesByLowerBound;
/* 464 */       this.positiveRangesByUpperBound = new TreeRangeSet.RangesByUpperBound<>(positiveRangesByLowerBound);
/* 465 */       this.complementLowerBoundWindow = window;
/*     */     }
/*     */     
/*     */     private NavigableMap<Cut<C>, Range<C>> subMap(Range<Cut<C>> subWindow) {
/* 469 */       if (!this.complementLowerBoundWindow.isConnected(subWindow)) {
/* 470 */         return ImmutableSortedMap.of();
/*     */       }
/* 472 */       subWindow = subWindow.intersection(this.complementLowerBoundWindow);
/* 473 */       return new ComplementRangesByLowerBound(this.positiveRangesByLowerBound, subWindow);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public NavigableMap<Cut<C>, Range<C>> subMap(Cut<C> fromKey, boolean fromInclusive, Cut<C> toKey, boolean toInclusive) {
/* 480 */       return subMap(
/* 481 */           Range.range(fromKey, 
/* 482 */             BoundType.forBoolean(fromInclusive), toKey, 
/* 483 */             BoundType.forBoolean(toInclusive)));
/*     */     }
/*     */ 
/*     */     
/*     */     public NavigableMap<Cut<C>, Range<C>> headMap(Cut<C> toKey, boolean inclusive) {
/* 488 */       return subMap(Range.upTo(toKey, BoundType.forBoolean(inclusive)));
/*     */     }
/*     */ 
/*     */     
/*     */     public NavigableMap<Cut<C>, Range<C>> tailMap(Cut<C> fromKey, boolean inclusive) {
/* 493 */       return subMap(Range.downTo(fromKey, BoundType.forBoolean(inclusive)));
/*     */     }
/*     */ 
/*     */     
/*     */     public Comparator<? super Cut<C>> comparator() {
/* 498 */       return Ordering.natural();
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
/*     */     Iterator<Map.Entry<Cut<C>, Range<C>>> entryIterator() {
/*     */       Collection<Range<C>> positiveRanges;
/*     */       final Cut<C> firstComplementRangeLowerBound;
/* 513 */       if (this.complementLowerBoundWindow.hasLowerBound()) {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 519 */         positiveRanges = this.positiveRangesByUpperBound.tailMap(this.complementLowerBoundWindow.lowerEndpoint(), (this.complementLowerBoundWindow.lowerBoundType() == BoundType.CLOSED)).values();
/*     */       } else {
/* 521 */         positiveRanges = this.positiveRangesByUpperBound.values();
/*     */       } 
/*     */       
/* 524 */       final PeekingIterator<Range<C>> positiveItr = Iterators.peekingIterator(positiveRanges.iterator());
/*     */       
/* 526 */       if (this.complementLowerBoundWindow.contains((Cut)Cut.belowAll()) && (
/* 527 */         !positiveItr.hasNext() || ((Range)positiveItr.peek()).lowerBound != Cut.belowAll())) {
/* 528 */         firstComplementRangeLowerBound = Cut.belowAll();
/* 529 */       } else if (positiveItr.hasNext()) {
/* 530 */         firstComplementRangeLowerBound = ((Range)positiveItr.next()).upperBound;
/*     */       } else {
/* 532 */         return Iterators.emptyIterator();
/*     */       } 
/* 534 */       return new AbstractIterator<Map.Entry<Cut<C>, Range<C>>>() {
/* 535 */           Cut<C> nextComplementRangeLowerBound = firstComplementRangeLowerBound;
/*     */           
/*     */           protected Map.Entry<Cut<C>, Range<C>> computeNext() {
/*     */             Range<C> negativeRange;
/* 539 */             if (TreeRangeSet.ComplementRangesByLowerBound.this.complementLowerBoundWindow.upperBound.isLessThan(this.nextComplementRangeLowerBound) || this.nextComplementRangeLowerBound == 
/* 540 */               Cut.aboveAll()) {
/* 541 */               return endOfData();
/*     */             }
/*     */             
/* 544 */             if (positiveItr.hasNext()) {
/* 545 */               Range<C> positiveRange = positiveItr.next();
/* 546 */               negativeRange = Range.create(this.nextComplementRangeLowerBound, positiveRange.lowerBound);
/* 547 */               this.nextComplementRangeLowerBound = positiveRange.upperBound;
/*     */             } else {
/* 549 */               negativeRange = Range.create(this.nextComplementRangeLowerBound, (Cut)Cut.aboveAll());
/* 550 */               this.nextComplementRangeLowerBound = Cut.aboveAll();
/*     */             } 
/* 552 */             return Maps.immutableEntry(negativeRange.lowerBound, negativeRange);
/*     */           }
/*     */         };
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
/*     */     Iterator<Map.Entry<Cut<C>, Range<C>>> descendingEntryIterator() {
/* 570 */       Cut<C> cut, startingPoint = this.complementLowerBoundWindow.hasUpperBound() ? this.complementLowerBoundWindow.upperEndpoint() : (Cut)Cut.<Comparable>aboveAll();
/*     */ 
/*     */       
/* 573 */       boolean inclusive = (this.complementLowerBoundWindow.hasUpperBound() && this.complementLowerBoundWindow.upperBoundType() == BoundType.CLOSED);
/*     */       
/* 575 */       final PeekingIterator<Range<C>> positiveItr = Iterators.peekingIterator(this.positiveRangesByUpperBound
/*     */           
/* 577 */           .headMap(startingPoint, inclusive)
/* 578 */           .descendingMap()
/* 579 */           .values()
/* 580 */           .iterator());
/*     */       
/* 582 */       if (positiveItr.hasNext())
/*     */       
/*     */       { 
/*     */         
/* 586 */         cut = (((Range)positiveItr.peek()).upperBound == Cut.aboveAll()) ? ((Range)positiveItr.next()).lowerBound : this.positiveRangesByLowerBound.higherKey(((Range)positiveItr.peek()).upperBound); }
/* 587 */       else { if (!this.complementLowerBoundWindow.contains((Cut)Cut.belowAll()) || this.positiveRangesByLowerBound
/* 588 */           .containsKey(Cut.belowAll())) {
/* 589 */           return Iterators.emptyIterator();
/*     */         }
/* 591 */         cut = this.positiveRangesByLowerBound.higherKey((Cut)Cut.belowAll()); }
/*     */ 
/*     */       
/* 594 */       final Cut<C> firstComplementRangeUpperBound = (Cut<C>)MoreObjects.firstNonNull(cut, Cut.aboveAll());
/* 595 */       return new AbstractIterator<Map.Entry<Cut<C>, Range<C>>>() {
/* 596 */           Cut<C> nextComplementRangeUpperBound = firstComplementRangeUpperBound;
/*     */ 
/*     */           
/*     */           protected Map.Entry<Cut<C>, Range<C>> computeNext() {
/* 600 */             if (this.nextComplementRangeUpperBound == Cut.belowAll())
/* 601 */               return endOfData(); 
/* 602 */             if (positiveItr.hasNext()) {
/* 603 */               Range<C> positiveRange = positiveItr.next();
/*     */               
/* 605 */               Range<C> negativeRange = Range.create(positiveRange.upperBound, this.nextComplementRangeUpperBound);
/* 606 */               this.nextComplementRangeUpperBound = positiveRange.lowerBound;
/* 607 */               if (TreeRangeSet.ComplementRangesByLowerBound.this.complementLowerBoundWindow.lowerBound.isLessThan(negativeRange.lowerBound)) {
/* 608 */                 return Maps.immutableEntry(negativeRange.lowerBound, negativeRange);
/*     */               }
/* 610 */             } else if (TreeRangeSet.ComplementRangesByLowerBound.this.complementLowerBoundWindow.lowerBound.isLessThan(Cut.belowAll())) {
/* 611 */               Range<C> negativeRange = Range.create((Cut)Cut.belowAll(), this.nextComplementRangeUpperBound);
/* 612 */               this.nextComplementRangeUpperBound = Cut.belowAll();
/* 613 */               return Maps.immutableEntry((Cut)Cut.belowAll(), negativeRange);
/*     */             } 
/* 615 */             return endOfData();
/*     */           }
/*     */         };
/*     */     }
/*     */ 
/*     */     
/*     */     public int size() {
/* 622 */       return Iterators.size(entryIterator());
/*     */     }
/*     */ 
/*     */     
/*     */     public Range<C> get(Object key) {
/* 627 */       if (key instanceof Cut) {
/*     */         
/*     */         try {
/* 630 */           Cut<C> cut = (Cut<C>)key;
/*     */           
/* 632 */           Map.Entry<Cut<C>, Range<C>> firstEntry = tailMap(cut, true).firstEntry();
/* 633 */           if (firstEntry != null && ((Cut)firstEntry.getKey()).equals(cut)) {
/* 634 */             return firstEntry.getValue();
/*     */           }
/* 636 */         } catch (ClassCastException e) {
/* 637 */           return null;
/*     */         } 
/*     */       }
/* 640 */       return null;
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean containsKey(Object key) {
/* 645 */       return (get(key) != null);
/*     */     }
/*     */   }
/*     */   
/*     */   private final class Complement extends TreeRangeSet<C> {
/*     */     Complement() {
/* 651 */       super(new TreeRangeSet.ComplementRangesByLowerBound<>(TreeRangeSet.this.rangesByLowerBound));
/*     */     }
/*     */ 
/*     */     
/*     */     public void add(Range<C> rangeToAdd) {
/* 656 */       TreeRangeSet.this.remove(rangeToAdd);
/*     */     }
/*     */ 
/*     */     
/*     */     public void remove(Range<C> rangeToRemove) {
/* 661 */       TreeRangeSet.this.add(rangeToRemove);
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean contains(C value) {
/* 666 */       return !TreeRangeSet.this.contains((Comparable)value);
/*     */     }
/*     */ 
/*     */     
/*     */     public RangeSet<C> complement() {
/* 671 */       return TreeRangeSet.this;
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static final class SubRangeSetRangesByLowerBound<C extends Comparable<?>>
/*     */     extends AbstractNavigableMap<Cut<C>, Range<C>>
/*     */   {
/*     */     private final Range<Cut<C>> lowerBoundWindow;
/*     */ 
/*     */ 
/*     */     
/*     */     private final Range<C> restriction;
/*     */ 
/*     */     
/*     */     private final NavigableMap<Cut<C>, Range<C>> rangesByLowerBound;
/*     */ 
/*     */     
/*     */     private final NavigableMap<Cut<C>, Range<C>> rangesByUpperBound;
/*     */ 
/*     */ 
/*     */     
/*     */     private SubRangeSetRangesByLowerBound(Range<Cut<C>> lowerBoundWindow, Range<C> restriction, NavigableMap<Cut<C>, Range<C>> rangesByLowerBound) {
/* 696 */       this.lowerBoundWindow = (Range<Cut<C>>)Preconditions.checkNotNull(lowerBoundWindow);
/* 697 */       this.restriction = (Range<C>)Preconditions.checkNotNull(restriction);
/* 698 */       this.rangesByLowerBound = (NavigableMap<Cut<C>, Range<C>>)Preconditions.checkNotNull(rangesByLowerBound);
/* 699 */       this.rangesByUpperBound = new TreeRangeSet.RangesByUpperBound<>(rangesByLowerBound);
/*     */     }
/*     */     
/*     */     private NavigableMap<Cut<C>, Range<C>> subMap(Range<Cut<C>> window) {
/* 703 */       if (!window.isConnected(this.lowerBoundWindow)) {
/* 704 */         return ImmutableSortedMap.of();
/*     */       }
/* 706 */       return new SubRangeSetRangesByLowerBound(this.lowerBoundWindow
/* 707 */           .intersection(window), this.restriction, this.rangesByLowerBound);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public NavigableMap<Cut<C>, Range<C>> subMap(Cut<C> fromKey, boolean fromInclusive, Cut<C> toKey, boolean toInclusive) {
/* 714 */       return subMap(
/* 715 */           Range.range(fromKey, 
/*     */             
/* 717 */             BoundType.forBoolean(fromInclusive), toKey, 
/*     */             
/* 719 */             BoundType.forBoolean(toInclusive)));
/*     */     }
/*     */ 
/*     */     
/*     */     public NavigableMap<Cut<C>, Range<C>> headMap(Cut<C> toKey, boolean inclusive) {
/* 724 */       return subMap(Range.upTo(toKey, BoundType.forBoolean(inclusive)));
/*     */     }
/*     */ 
/*     */     
/*     */     public NavigableMap<Cut<C>, Range<C>> tailMap(Cut<C> fromKey, boolean inclusive) {
/* 729 */       return subMap(Range.downTo(fromKey, BoundType.forBoolean(inclusive)));
/*     */     }
/*     */ 
/*     */     
/*     */     public Comparator<? super Cut<C>> comparator() {
/* 734 */       return Ordering.natural();
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean containsKey(Object key) {
/* 739 */       return (get(key) != null);
/*     */     }
/*     */ 
/*     */     
/*     */     public Range<C> get(Object key) {
/* 744 */       if (key instanceof Cut) {
/*     */         
/*     */         try {
/* 747 */           Cut<C> cut = (Cut<C>)key;
/* 748 */           if (!this.lowerBoundWindow.contains(cut) || cut
/* 749 */             .compareTo(this.restriction.lowerBound) < 0 || cut
/* 750 */             .compareTo(this.restriction.upperBound) >= 0)
/* 751 */             return null; 
/* 752 */           if (cut.equals(this.restriction.lowerBound)) {
/*     */             
/* 754 */             Range<C> candidate = Maps.<Range<C>>valueOrNull(this.rangesByLowerBound.floorEntry(cut));
/* 755 */             if (candidate != null && candidate.upperBound.compareTo(this.restriction.lowerBound) > 0) {
/* 756 */               return candidate.intersection(this.restriction);
/*     */             }
/*     */           } else {
/* 759 */             Range<C> result = this.rangesByLowerBound.get(cut);
/* 760 */             if (result != null) {
/* 761 */               return result.intersection(this.restriction);
/*     */             }
/*     */           } 
/* 764 */         } catch (ClassCastException e) {
/* 765 */           return null;
/*     */         } 
/*     */       }
/* 768 */       return null;
/*     */     }
/*     */     
/*     */     Iterator<Map.Entry<Cut<C>, Range<C>>> entryIterator() {
/*     */       final Iterator<Range<C>> completeRangeItr;
/* 773 */       if (this.restriction.isEmpty()) {
/* 774 */         return Iterators.emptyIterator();
/*     */       }
/*     */       
/* 777 */       if (this.lowerBoundWindow.upperBound.isLessThan(this.restriction.lowerBound))
/* 778 */         return Iterators.emptyIterator(); 
/* 779 */       if (this.lowerBoundWindow.lowerBound.isLessThan(this.restriction.lowerBound)) {
/*     */ 
/*     */         
/* 782 */         completeRangeItr = this.rangesByUpperBound.tailMap(this.restriction.lowerBound, false).values().iterator();
/*     */ 
/*     */ 
/*     */       
/*     */       }
/*     */       else {
/*     */ 
/*     */ 
/*     */         
/* 791 */         completeRangeItr = this.rangesByLowerBound.tailMap(this.lowerBoundWindow.lowerBound.endpoint(), (this.lowerBoundWindow.lowerBoundType() == BoundType.CLOSED)).values().iterator();
/*     */       } 
/*     */ 
/*     */       
/* 795 */       final Cut<Cut<C>> upperBoundOnLowerBounds = (Cut<Cut<C>>)Ordering.<Comparable>natural().min(this.lowerBoundWindow.upperBound, Cut.belowValue(this.restriction.upperBound));
/* 796 */       return new AbstractIterator<Map.Entry<Cut<C>, Range<C>>>()
/*     */         {
/*     */           protected Map.Entry<Cut<C>, Range<C>> computeNext() {
/* 799 */             if (!completeRangeItr.hasNext()) {
/* 800 */               return endOfData();
/*     */             }
/* 802 */             Range<C> nextRange = completeRangeItr.next();
/* 803 */             if (upperBoundOnLowerBounds.isLessThan(nextRange.lowerBound)) {
/* 804 */               return endOfData();
/*     */             }
/* 806 */             nextRange = nextRange.intersection(TreeRangeSet.SubRangeSetRangesByLowerBound.this.restriction);
/* 807 */             return Maps.immutableEntry(nextRange.lowerBound, nextRange);
/*     */           }
/*     */         };
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     Iterator<Map.Entry<Cut<C>, Range<C>>> descendingEntryIterator() {
/* 815 */       if (this.restriction.isEmpty()) {
/* 816 */         return Iterators.emptyIterator();
/*     */       }
/*     */ 
/*     */       
/* 820 */       Cut<Cut<C>> upperBoundOnLowerBounds = (Cut<Cut<C>>)Ordering.<Comparable>natural().min(this.lowerBoundWindow.upperBound, Cut.belowValue(this.restriction.upperBound));
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 828 */       final Iterator<Range<C>> completeRangeItr = this.rangesByLowerBound.headMap(upperBoundOnLowerBounds.endpoint(), (upperBoundOnLowerBounds.typeAsUpperBound() == BoundType.CLOSED)).descendingMap().values().iterator();
/* 829 */       return new AbstractIterator<Map.Entry<Cut<C>, Range<C>>>()
/*     */         {
/*     */           protected Map.Entry<Cut<C>, Range<C>> computeNext() {
/* 832 */             if (!completeRangeItr.hasNext()) {
/* 833 */               return endOfData();
/*     */             }
/* 835 */             Range<C> nextRange = completeRangeItr.next();
/* 836 */             if (TreeRangeSet.SubRangeSetRangesByLowerBound.this.restriction.lowerBound.compareTo(nextRange.upperBound) >= 0) {
/* 837 */               return endOfData();
/*     */             }
/* 839 */             nextRange = nextRange.intersection(TreeRangeSet.SubRangeSetRangesByLowerBound.this.restriction);
/* 840 */             if (TreeRangeSet.SubRangeSetRangesByLowerBound.this.lowerBoundWindow.contains(nextRange.lowerBound)) {
/* 841 */               return Maps.immutableEntry(nextRange.lowerBound, nextRange);
/*     */             }
/* 843 */             return endOfData();
/*     */           }
/*     */         };
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public int size() {
/* 851 */       return Iterators.size(entryIterator());
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public RangeSet<C> subRangeSet(Range<C> view) {
/* 857 */     return view.equals(Range.all()) ? this : new SubRangeSet(view);
/*     */   }
/*     */   
/*     */   private final class SubRangeSet extends TreeRangeSet<C> {
/*     */     private final Range<C> restriction;
/*     */     
/*     */     SubRangeSet(Range<C> restriction) {
/* 864 */       super(new TreeRangeSet.SubRangeSetRangesByLowerBound<>(
/*     */             
/* 866 */             Range.all(), restriction, TreeRangeSet.this.rangesByLowerBound, null));
/* 867 */       this.restriction = restriction;
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean encloses(Range<C> range) {
/* 872 */       if (!this.restriction.isEmpty() && this.restriction.encloses(range)) {
/* 873 */         Range<C> enclosing = TreeRangeSet.this.rangeEnclosing(range);
/* 874 */         return (enclosing != null && !enclosing.intersection(this.restriction).isEmpty());
/*     */       } 
/* 876 */       return false;
/*     */     }
/*     */ 
/*     */     
/*     */     public Range<C> rangeContaining(C value) {
/* 881 */       if (!this.restriction.contains(value)) {
/* 882 */         return null;
/*     */       }
/* 884 */       Range<C> result = TreeRangeSet.this.rangeContaining(value);
/* 885 */       return (result == null) ? null : result.intersection(this.restriction);
/*     */     }
/*     */ 
/*     */     
/*     */     public void add(Range<C> rangeToAdd) {
/* 890 */       Preconditions.checkArgument(this.restriction
/* 891 */           .encloses(rangeToAdd), "Cannot add range %s to subRangeSet(%s)", rangeToAdd, this.restriction);
/*     */ 
/*     */ 
/*     */       
/* 895 */       super.add(rangeToAdd);
/*     */     }
/*     */ 
/*     */     
/*     */     public void remove(Range<C> rangeToRemove) {
/* 900 */       if (rangeToRemove.isConnected(this.restriction)) {
/* 901 */         TreeRangeSet.this.remove(rangeToRemove.intersection(this.restriction));
/*     */       }
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean contains(C value) {
/* 907 */       return (this.restriction.contains(value) && TreeRangeSet.this.contains((Comparable)value));
/*     */     }
/*     */ 
/*     */     
/*     */     public void clear() {
/* 912 */       TreeRangeSet.this.remove(this.restriction);
/*     */     }
/*     */ 
/*     */     
/*     */     public RangeSet<C> subRangeSet(Range<C> view) {
/* 917 */       if (view.encloses(this.restriction))
/* 918 */         return this; 
/* 919 */       if (view.isConnected(this.restriction)) {
/* 920 */         return new SubRangeSet(this.restriction.intersection(view));
/*     */       }
/* 922 */       return (RangeSet)ImmutableRangeSet.of();
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\google\common\collect\TreeRangeSet.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */