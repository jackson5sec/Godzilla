/*     */ package com.google.common.collect;
/*     */ 
/*     */ import com.google.common.annotations.Beta;
/*     */ import com.google.common.annotations.GwtIncompatible;
/*     */ import com.google.common.base.Function;
/*     */ import com.google.common.base.Preconditions;
/*     */ import com.google.common.primitives.Ints;
/*     */ import com.google.errorprone.annotations.CanIgnoreReturnValue;
/*     */ import com.google.errorprone.annotations.concurrent.LazyInit;
/*     */ import java.io.Serializable;
/*     */ import java.util.Collections;
/*     */ import java.util.Comparator;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.NoSuchElementException;
/*     */ import java.util.Set;
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
/*     */ @Beta
/*     */ @GwtIncompatible
/*     */ public final class ImmutableRangeSet<C extends Comparable>
/*     */   extends AbstractRangeSet<C>
/*     */   implements Serializable
/*     */ {
/*  53 */   private static final ImmutableRangeSet<Comparable<?>> EMPTY = new ImmutableRangeSet(
/*  54 */       ImmutableList.of());
/*     */   
/*  56 */   private static final ImmutableRangeSet<Comparable<?>> ALL = new ImmutableRangeSet(
/*  57 */       ImmutableList.of((Range)Range.all()));
/*     */ 
/*     */   
/*     */   private final transient ImmutableList<Range<C>> ranges;
/*     */ 
/*     */   
/*     */   @LazyInit
/*     */   private transient ImmutableRangeSet<C> complement;
/*     */ 
/*     */   
/*     */   public static <E extends Comparable<? super E>> Collector<Range<E>, ?, ImmutableRangeSet<E>> toImmutableRangeSet() {
/*  68 */     return CollectCollectors.toImmutableRangeSet();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static <C extends Comparable> ImmutableRangeSet<C> of() {
/*  74 */     return (ImmutableRangeSet)EMPTY;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <C extends Comparable> ImmutableRangeSet<C> of(Range<C> range) {
/*  82 */     Preconditions.checkNotNull(range);
/*  83 */     if (range.isEmpty())
/*  84 */       return of(); 
/*  85 */     if (range.equals(Range.all())) {
/*  86 */       return all();
/*     */     }
/*  88 */     return new ImmutableRangeSet<>(ImmutableList.of(range));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static <C extends Comparable> ImmutableRangeSet<C> all() {
/*  95 */     return (ImmutableRangeSet)ALL;
/*     */   }
/*     */ 
/*     */   
/*     */   public static <C extends Comparable> ImmutableRangeSet<C> copyOf(RangeSet<C> rangeSet) {
/* 100 */     Preconditions.checkNotNull(rangeSet);
/* 101 */     if (rangeSet.isEmpty())
/* 102 */       return of(); 
/* 103 */     if (rangeSet.encloses((Range)Range.all())) {
/* 104 */       return all();
/*     */     }
/*     */     
/* 107 */     if (rangeSet instanceof ImmutableRangeSet) {
/* 108 */       ImmutableRangeSet<C> immutableRangeSet = (ImmutableRangeSet<C>)rangeSet;
/* 109 */       if (!immutableRangeSet.isPartialView()) {
/* 110 */         return immutableRangeSet;
/*     */       }
/*     */     } 
/* 113 */     return new ImmutableRangeSet<>(ImmutableList.copyOf(rangeSet.asRanges()));
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
/*     */   public static <C extends Comparable<?>> ImmutableRangeSet<C> copyOf(Iterable<Range<C>> ranges) {
/* 125 */     return (new Builder<>()).addAll(ranges).build();
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
/*     */   public static <C extends Comparable<?>> ImmutableRangeSet<C> unionOf(Iterable<Range<C>> ranges) {
/* 137 */     return (ImmutableRangeSet)copyOf(TreeRangeSet.create(ranges));
/*     */   }
/*     */   
/*     */   ImmutableRangeSet(ImmutableList<Range<C>> ranges) {
/* 141 */     this.ranges = ranges;
/*     */   }
/*     */   
/*     */   private ImmutableRangeSet(ImmutableList<Range<C>> ranges, ImmutableRangeSet<C> complement) {
/* 145 */     this.ranges = ranges;
/* 146 */     this.complement = complement;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean intersects(Range<C> otherRange) {
/* 154 */     int ceilingIndex = SortedLists.binarySearch(this.ranges, 
/*     */         
/* 156 */         (Function)Range.lowerBoundFn(), otherRange.lowerBound, 
/*     */         
/* 158 */         Ordering.natural(), SortedLists.KeyPresentBehavior.ANY_PRESENT, SortedLists.KeyAbsentBehavior.NEXT_HIGHER);
/*     */ 
/*     */     
/* 161 */     if (ceilingIndex < this.ranges.size() && ((Range<C>)this.ranges
/* 162 */       .get(ceilingIndex)).isConnected(otherRange) && 
/* 163 */       !((Range<C>)this.ranges.get(ceilingIndex)).intersection(otherRange).isEmpty()) {
/* 164 */       return true;
/*     */     }
/* 166 */     return (ceilingIndex > 0 && ((Range<C>)this.ranges
/* 167 */       .get(ceilingIndex - 1)).isConnected(otherRange) && 
/* 168 */       !((Range<C>)this.ranges.get(ceilingIndex - 1)).intersection(otherRange).isEmpty());
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean encloses(Range<C> otherRange) {
/* 174 */     int index = SortedLists.binarySearch(this.ranges, 
/*     */         
/* 176 */         (Function)Range.lowerBoundFn(), otherRange.lowerBound, 
/*     */         
/* 178 */         Ordering.natural(), SortedLists.KeyPresentBehavior.ANY_PRESENT, SortedLists.KeyAbsentBehavior.NEXT_LOWER);
/*     */ 
/*     */     
/* 181 */     return (index != -1 && ((Range<C>)this.ranges.get(index)).encloses(otherRange));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Range<C> rangeContaining(C value) {
/* 187 */     int index = SortedLists.binarySearch(this.ranges, 
/*     */         
/* 189 */         (Function)Range.lowerBoundFn(), 
/* 190 */         Cut.belowValue(value), 
/* 191 */         Ordering.natural(), SortedLists.KeyPresentBehavior.ANY_PRESENT, SortedLists.KeyAbsentBehavior.NEXT_LOWER);
/*     */ 
/*     */     
/* 194 */     if (index != -1) {
/* 195 */       Range<C> range = this.ranges.get(index);
/* 196 */       return range.contains(value) ? range : null;
/*     */     } 
/* 198 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   public Range<C> span() {
/* 203 */     if (this.ranges.isEmpty()) {
/* 204 */       throw new NoSuchElementException();
/*     */     }
/* 206 */     return (Range)Range.create(((Range)this.ranges.get(0)).lowerBound, ((Range)this.ranges.get(this.ranges.size() - 1)).upperBound);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isEmpty() {
/* 211 */     return this.ranges.isEmpty();
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
/*     */   public void add(Range<C> range) {
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
/*     */   public void addAll(RangeSet<C> other) {
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
/*     */   public void addAll(Iterable<Range<C>> other) {
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
/*     */   public void remove(Range<C> range) {
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
/*     */   public void removeAll(RangeSet<C> other) {
/* 271 */     throw new UnsupportedOperationException();
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
/*     */   public void removeAll(Iterable<Range<C>> other) {
/* 283 */     throw new UnsupportedOperationException();
/*     */   }
/*     */ 
/*     */   
/*     */   public ImmutableSet<Range<C>> asRanges() {
/* 288 */     if (this.ranges.isEmpty()) {
/* 289 */       return ImmutableSet.of();
/*     */     }
/* 291 */     return new RegularImmutableSortedSet<>(this.ranges, (Comparator)Range.rangeLexOrdering());
/*     */   }
/*     */ 
/*     */   
/*     */   public ImmutableSet<Range<C>> asDescendingSetOfRanges() {
/* 296 */     if (this.ranges.isEmpty()) {
/* 297 */       return ImmutableSet.of();
/*     */     }
/* 299 */     return new RegularImmutableSortedSet<>(this.ranges.reverse(), Range.<Comparable<?>>rangeLexOrdering().reverse());
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
/*     */   private final class ComplementRanges
/*     */     extends ImmutableList<Range<C>>
/*     */   {
/* 314 */     private final boolean positiveBoundedBelow = ((Range)ImmutableRangeSet.this.ranges.get(0)).hasLowerBound();
/* 315 */     private final boolean positiveBoundedAbove = ((Range)Iterables.<Range>getLast(ImmutableRangeSet.this.ranges)).hasUpperBound();
/*     */     ComplementRanges() {
/* 317 */       int size = ImmutableRangeSet.this.ranges.size() - 1;
/* 318 */       if (this.positiveBoundedBelow) {
/* 319 */         size++;
/*     */       }
/* 321 */       if (this.positiveBoundedAbove) {
/* 322 */         size++;
/*     */       }
/* 324 */       this.size = size;
/*     */     }
/*     */     private final int size;
/*     */     
/*     */     public int size() {
/* 329 */       return this.size;
/*     */     }
/*     */     
/*     */     public Range<C> get(int index) {
/*     */       Cut<C> lowerBound, upperBound;
/* 334 */       Preconditions.checkElementIndex(index, this.size);
/*     */ 
/*     */       
/* 337 */       if (this.positiveBoundedBelow) {
/* 338 */         lowerBound = (index == 0) ? Cut.<C>belowAll() : ((Range)ImmutableRangeSet.this.ranges.get(index - 1)).upperBound;
/*     */       } else {
/* 340 */         lowerBound = ((Range)ImmutableRangeSet.this.ranges.get(index)).upperBound;
/*     */       } 
/*     */ 
/*     */       
/* 344 */       if (this.positiveBoundedAbove && index == this.size - 1) {
/* 345 */         upperBound = Cut.aboveAll();
/*     */       } else {
/* 347 */         upperBound = ((Range)ImmutableRangeSet.this.ranges.get(index + (this.positiveBoundedBelow ? 0 : 1))).lowerBound;
/*     */       } 
/*     */       
/* 350 */       return (Range)Range.create(lowerBound, upperBound);
/*     */     }
/*     */ 
/*     */     
/*     */     boolean isPartialView() {
/* 355 */       return true;
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public ImmutableRangeSet<C> complement() {
/* 361 */     ImmutableRangeSet<C> result = this.complement;
/* 362 */     if (result != null)
/* 363 */       return result; 
/* 364 */     if (this.ranges.isEmpty())
/* 365 */       return this.complement = all(); 
/* 366 */     if (this.ranges.size() == 1 && ((Range)this.ranges.get(0)).equals(Range.all())) {
/* 367 */       return this.complement = of();
/*     */     }
/* 369 */     ImmutableList<Range<C>> complementRanges = new ComplementRanges();
/* 370 */     result = this.complement = new ImmutableRangeSet(complementRanges, this);
/*     */     
/* 372 */     return result;
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
/*     */   public ImmutableRangeSet<C> union(RangeSet<C> other) {
/* 384 */     return (ImmutableRangeSet)unionOf((Iterable)Iterables.concat(asRanges(), other.asRanges()));
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
/*     */   public ImmutableRangeSet<C> intersection(RangeSet<C> other) {
/* 397 */     RangeSet<C> copy = (RangeSet)TreeRangeSet.create(this);
/* 398 */     copy.removeAll(other.complement());
/* 399 */     return copyOf(copy);
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
/*     */   public ImmutableRangeSet<C> difference(RangeSet<C> other) {
/* 411 */     RangeSet<C> copy = (RangeSet)TreeRangeSet.create(this);
/* 412 */     copy.removeAll(other);
/* 413 */     return copyOf(copy);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private ImmutableList<Range<C>> intersectRanges(final Range<C> range) {
/*     */     final int fromIndex, toIndex;
/* 421 */     if (this.ranges.isEmpty() || range.isEmpty())
/* 422 */       return ImmutableList.of(); 
/* 423 */     if (range.encloses(span())) {
/* 424 */       return this.ranges;
/*     */     }
/*     */ 
/*     */     
/* 428 */     if (range.hasLowerBound()) {
/*     */       
/* 430 */       fromIndex = SortedLists.binarySearch(this.ranges, 
/*     */           
/* 432 */           (Function)Range.upperBoundFn(), range.lowerBound, SortedLists.KeyPresentBehavior.FIRST_AFTER, SortedLists.KeyAbsentBehavior.NEXT_HIGHER);
/*     */     
/*     */     }
/*     */     else {
/*     */       
/* 437 */       fromIndex = 0;
/*     */     } 
/*     */ 
/*     */     
/* 441 */     if (range.hasUpperBound()) {
/*     */       
/* 443 */       toIndex = SortedLists.binarySearch(this.ranges, 
/*     */           
/* 445 */           (Function)Range.lowerBoundFn(), range.upperBound, SortedLists.KeyPresentBehavior.FIRST_PRESENT, SortedLists.KeyAbsentBehavior.NEXT_HIGHER);
/*     */     
/*     */     }
/*     */     else {
/*     */       
/* 450 */       toIndex = this.ranges.size();
/*     */     } 
/* 452 */     final int length = toIndex - fromIndex;
/* 453 */     if (length == 0) {
/* 454 */       return ImmutableList.of();
/*     */     }
/* 456 */     return new ImmutableList<Range<C>>()
/*     */       {
/*     */         public int size() {
/* 459 */           return length;
/*     */         }
/*     */ 
/*     */         
/*     */         public Range<C> get(int index) {
/* 464 */           Preconditions.checkElementIndex(index, length);
/* 465 */           if (index == 0 || index == length - 1) {
/* 466 */             return ((Range<C>)ImmutableRangeSet.this.ranges.get(index + fromIndex)).intersection(range);
/*     */           }
/* 468 */           return ImmutableRangeSet.this.ranges.get(index + fromIndex);
/*     */         }
/*     */ 
/*     */ 
/*     */         
/*     */         boolean isPartialView() {
/* 474 */           return true;
/*     */         }
/*     */       };
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ImmutableRangeSet<C> subRangeSet(Range<C> range) {
/* 483 */     if (!isEmpty()) {
/* 484 */       Range<C> span = span();
/* 485 */       if (range.encloses(span))
/* 486 */         return this; 
/* 487 */       if (range.isConnected(span)) {
/* 488 */         return new ImmutableRangeSet(intersectRanges(range));
/*     */       }
/*     */     } 
/* 491 */     return of();
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
/*     */   public ImmutableSortedSet<C> asSet(DiscreteDomain<C> domain) {
/* 514 */     Preconditions.checkNotNull(domain);
/* 515 */     if (isEmpty()) {
/* 516 */       return ImmutableSortedSet.of();
/*     */     }
/* 518 */     Range<C> span = span().canonical(domain);
/* 519 */     if (!span.hasLowerBound())
/*     */     {
/*     */       
/* 522 */       throw new IllegalArgumentException("Neither the DiscreteDomain nor this range set are bounded below");
/*     */     }
/* 524 */     if (!span.hasUpperBound()) {
/*     */       try {
/* 526 */         domain.maxValue();
/* 527 */       } catch (NoSuchElementException e) {
/* 528 */         throw new IllegalArgumentException("Neither the DiscreteDomain nor this range set are bounded above");
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/* 533 */     return new AsSet(domain);
/*     */   }
/*     */   
/*     */   private final class AsSet extends ImmutableSortedSet<C> { private final DiscreteDomain<C> domain;
/*     */     private transient Integer size;
/*     */     
/*     */     AsSet(DiscreteDomain<C> domain) {
/* 540 */       super(Ordering.natural());
/* 541 */       this.domain = domain;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public int size() {
/* 549 */       Integer result = this.size;
/* 550 */       if (result == null) {
/* 551 */         long total = 0L;
/* 552 */         for (UnmodifiableIterator<Range<C>> unmodifiableIterator = ImmutableRangeSet.this.ranges.iterator(); unmodifiableIterator.hasNext(); ) { Range<C> range = unmodifiableIterator.next();
/* 553 */           total += ContiguousSet.<C>create(range, this.domain).size();
/* 554 */           if (total >= 2147483647L) {
/*     */             break;
/*     */           } }
/*     */         
/* 558 */         result = this.size = Integer.valueOf(Ints.saturatedCast(total));
/*     */       } 
/* 560 */       return result.intValue();
/*     */     }
/*     */ 
/*     */     
/*     */     public UnmodifiableIterator<C> iterator() {
/* 565 */       return new AbstractIterator<C>() {
/* 566 */           final Iterator<Range<C>> rangeItr = ImmutableRangeSet.this.ranges.iterator();
/* 567 */           Iterator<C> elemItr = Iterators.emptyIterator();
/*     */ 
/*     */           
/*     */           protected C computeNext() {
/* 571 */             while (!this.elemItr.hasNext()) {
/* 572 */               if (this.rangeItr.hasNext()) {
/* 573 */                 this.elemItr = ContiguousSet.<C>create(this.rangeItr.next(), ImmutableRangeSet.AsSet.this.domain).iterator(); continue;
/*     */               } 
/* 575 */               return endOfData();
/*     */             } 
/*     */             
/* 578 */             return this.elemItr.next();
/*     */           }
/*     */         };
/*     */     }
/*     */ 
/*     */     
/*     */     @GwtIncompatible("NavigableSet")
/*     */     public UnmodifiableIterator<C> descendingIterator() {
/* 586 */       return new AbstractIterator<C>() {
/* 587 */           final Iterator<Range<C>> rangeItr = ImmutableRangeSet.this.ranges.reverse().iterator();
/* 588 */           Iterator<C> elemItr = Iterators.emptyIterator();
/*     */ 
/*     */           
/*     */           protected C computeNext() {
/* 592 */             while (!this.elemItr.hasNext()) {
/* 593 */               if (this.rangeItr.hasNext()) {
/* 594 */                 this.elemItr = ContiguousSet.<C>create(this.rangeItr.next(), ImmutableRangeSet.AsSet.this.domain).descendingIterator(); continue;
/*     */               } 
/* 596 */               return endOfData();
/*     */             } 
/*     */             
/* 599 */             return this.elemItr.next();
/*     */           }
/*     */         };
/*     */     }
/*     */     
/*     */     ImmutableSortedSet<C> subSet(Range<C> range) {
/* 605 */       return ImmutableRangeSet.this.subRangeSet(range).asSet(this.domain);
/*     */     }
/*     */ 
/*     */     
/*     */     ImmutableSortedSet<C> headSetImpl(C toElement, boolean inclusive) {
/* 610 */       return subSet((Range)Range.upTo((Comparable<?>)toElement, BoundType.forBoolean(inclusive)));
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     ImmutableSortedSet<C> subSetImpl(C fromElement, boolean fromInclusive, C toElement, boolean toInclusive) {
/* 616 */       if (!fromInclusive && !toInclusive && Range.compareOrThrow((Comparable)fromElement, (Comparable)toElement) == 0) {
/* 617 */         return ImmutableSortedSet.of();
/*     */       }
/* 619 */       return subSet(
/* 620 */           (Range)Range.range((Comparable<?>)fromElement, 
/* 621 */             BoundType.forBoolean(fromInclusive), (Comparable<?>)toElement, 
/* 622 */             BoundType.forBoolean(toInclusive)));
/*     */     }
/*     */ 
/*     */     
/*     */     ImmutableSortedSet<C> tailSetImpl(C fromElement, boolean inclusive) {
/* 627 */       return subSet((Range)Range.downTo((Comparable<?>)fromElement, BoundType.forBoolean(inclusive)));
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean contains(Object o) {
/* 632 */       if (o == null) {
/* 633 */         return false;
/*     */       }
/*     */       
/*     */       try {
/* 637 */         Comparable comparable = (Comparable)o;
/* 638 */         return ImmutableRangeSet.this.contains(comparable);
/* 639 */       } catch (ClassCastException e) {
/* 640 */         return false;
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     int indexOf(Object target) {
/* 646 */       if (contains(target)) {
/*     */         
/* 648 */         Comparable comparable = (Comparable)target;
/* 649 */         long total = 0L;
/* 650 */         for (UnmodifiableIterator<Range<C>> unmodifiableIterator = ImmutableRangeSet.this.ranges.iterator(); unmodifiableIterator.hasNext(); ) { Range<C> range = unmodifiableIterator.next();
/* 651 */           if (range.contains((C)comparable)) {
/* 652 */             return Ints.saturatedCast(total + ContiguousSet.<C>create(range, this.domain).indexOf(comparable));
/*     */           }
/* 654 */           total += ContiguousSet.<C>create(range, this.domain).size(); }
/*     */ 
/*     */         
/* 657 */         throw new AssertionError("impossible");
/*     */       } 
/* 659 */       return -1;
/*     */     }
/*     */ 
/*     */     
/*     */     ImmutableSortedSet<C> createDescendingSet() {
/* 664 */       return new DescendingImmutableSortedSet<>(this);
/*     */     }
/*     */ 
/*     */     
/*     */     boolean isPartialView() {
/* 669 */       return ImmutableRangeSet.this.ranges.isPartialView();
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString() {
/* 674 */       return ImmutableRangeSet.this.ranges.toString();
/*     */     }
/*     */ 
/*     */     
/*     */     Object writeReplace() {
/* 679 */       return new ImmutableRangeSet.AsSetSerializedForm<>(ImmutableRangeSet.this.ranges, this.domain);
/*     */     } }
/*     */ 
/*     */   
/*     */   private static class AsSetSerializedForm<C extends Comparable> implements Serializable {
/*     */     private final ImmutableList<Range<C>> ranges;
/*     */     private final DiscreteDomain<C> domain;
/*     */     
/*     */     AsSetSerializedForm(ImmutableList<Range<C>> ranges, DiscreteDomain<C> domain) {
/* 688 */       this.ranges = ranges;
/* 689 */       this.domain = domain;
/*     */     }
/*     */     
/*     */     Object readResolve() {
/* 693 */       return (new ImmutableRangeSet<>(this.ranges)).asSet(this.domain);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   boolean isPartialView() {
/* 704 */     return this.ranges.isPartialView();
/*     */   }
/*     */ 
/*     */   
/*     */   public static <C extends Comparable<?>> Builder<C> builder() {
/* 709 */     return new Builder<>();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static class Builder<C extends Comparable<?>>
/*     */   {
/* 721 */     private final List<Range<C>> ranges = Lists.newArrayList();
/*     */ 
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
/*     */     public Builder<C> add(Range<C> range) {
/* 734 */       Preconditions.checkArgument(!range.isEmpty(), "range must not be empty, but was %s", range);
/* 735 */       this.ranges.add(range);
/* 736 */       return this;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     @CanIgnoreReturnValue
/*     */     public Builder<C> addAll(RangeSet<C> ranges) {
/* 746 */       return addAll(ranges.asRanges());
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
/*     */     public Builder<C> addAll(Iterable<Range<C>> ranges) {
/* 758 */       for (Range<C> range : ranges) {
/* 759 */         add(range);
/*     */       }
/* 761 */       return this;
/*     */     }
/*     */     
/*     */     @CanIgnoreReturnValue
/*     */     Builder<C> combine(Builder<C> builder) {
/* 766 */       addAll(builder.ranges);
/* 767 */       return this;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public ImmutableRangeSet<C> build() {
/* 777 */       ImmutableList.Builder<Range<C>> mergedRangesBuilder = new ImmutableList.Builder<>(this.ranges.size());
/* 778 */       Collections.sort(this.ranges, (Comparator)Range.rangeLexOrdering());
/* 779 */       PeekingIterator<Range<C>> peekingItr = Iterators.peekingIterator(this.ranges.iterator());
/* 780 */       while (peekingItr.hasNext()) {
/* 781 */         Range<C> range = peekingItr.next();
/* 782 */         while (peekingItr.hasNext()) {
/* 783 */           Range<C> nextRange = peekingItr.peek();
/* 784 */           if (range.isConnected(nextRange)) {
/* 785 */             Preconditions.checkArgument(range
/* 786 */                 .intersection(nextRange).isEmpty(), "Overlapping ranges not permitted but found %s overlapping %s", range, nextRange);
/*     */ 
/*     */ 
/*     */             
/* 790 */             range = range.span(peekingItr.next());
/*     */           } 
/*     */         } 
/*     */ 
/*     */         
/* 795 */         mergedRangesBuilder.add(range);
/*     */       } 
/* 797 */       ImmutableList<Range<C>> mergedRanges = mergedRangesBuilder.build();
/* 798 */       if (mergedRanges.isEmpty())
/* 799 */         return (ImmutableRangeSet)ImmutableRangeSet.of(); 
/* 800 */       if (mergedRanges.size() == 1 && (
/* 801 */         (Range)Iterables.<Range>getOnlyElement((Iterable)mergedRanges)).equals(Range.all())) {
/* 802 */         return (ImmutableRangeSet)ImmutableRangeSet.all();
/*     */       }
/* 804 */       return (ImmutableRangeSet)new ImmutableRangeSet<>(mergedRanges);
/*     */     }
/*     */   }
/*     */   
/*     */   private static final class SerializedForm<C extends Comparable>
/*     */     implements Serializable {
/*     */     private final ImmutableList<Range<C>> ranges;
/*     */     
/*     */     SerializedForm(ImmutableList<Range<C>> ranges) {
/* 813 */       this.ranges = ranges;
/*     */     }
/*     */     
/*     */     Object readResolve() {
/* 817 */       if (this.ranges.isEmpty())
/* 818 */         return ImmutableRangeSet.of(); 
/* 819 */       if (this.ranges.equals(ImmutableList.of(Range.all()))) {
/* 820 */         return ImmutableRangeSet.all();
/*     */       }
/* 822 */       return new ImmutableRangeSet<>(this.ranges);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   Object writeReplace() {
/* 828 */     return new SerializedForm<>(this.ranges);
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\google\common\collect\ImmutableRangeSet.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */