/*     */ package com.google.common.collect;
/*     */ 
/*     */ import com.google.common.annotations.GwtCompatible;
/*     */ import com.google.common.base.Function;
/*     */ import com.google.common.base.Preconditions;
/*     */ import com.google.common.base.Predicate;
/*     */ import java.io.Serializable;
/*     */ import java.util.Comparator;
/*     */ import java.util.Iterator;
/*     */ import java.util.SortedSet;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @GwtCompatible
/*     */ public final class Range<C extends Comparable>
/*     */   extends RangeGwtSerializationDependencies
/*     */   implements Predicate<C>, Serializable
/*     */ {
/*     */   static class LowerBoundFn
/*     */     implements Function<Range, Cut>
/*     */   {
/* 123 */     static final LowerBoundFn INSTANCE = new LowerBoundFn();
/*     */ 
/*     */     
/*     */     public Cut apply(Range range) {
/* 127 */       return range.lowerBound;
/*     */     }
/*     */   }
/*     */   
/*     */   static class UpperBoundFn implements Function<Range, Cut> {
/* 132 */     static final UpperBoundFn INSTANCE = new UpperBoundFn();
/*     */ 
/*     */     
/*     */     public Cut apply(Range range) {
/* 136 */       return range.upperBound;
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   static <C extends Comparable<?>> Function<Range<C>, Cut<C>> lowerBoundFn() {
/* 142 */     return LowerBoundFn.INSTANCE;
/*     */   }
/*     */ 
/*     */   
/*     */   static <C extends Comparable<?>> Function<Range<C>, Cut<C>> upperBoundFn() {
/* 147 */     return UpperBoundFn.INSTANCE;
/*     */   }
/*     */   
/*     */   static <C extends Comparable<?>> Ordering<Range<C>> rangeLexOrdering() {
/* 151 */     return (Ordering)RangeLexOrdering.INSTANCE;
/*     */   }
/*     */   
/*     */   static <C extends Comparable<?>> Range<C> create(Cut<C> lowerBound, Cut<C> upperBound) {
/* 155 */     return (Range)new Range<>(lowerBound, upperBound);
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
/*     */   public static <C extends Comparable<?>> Range<C> open(C lower, C upper) {
/* 168 */     return create((Cut)Cut.aboveValue((Comparable)lower), (Cut)Cut.belowValue((Comparable)upper));
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
/*     */   public static <C extends Comparable<?>> Range<C> closed(C lower, C upper) {
/* 180 */     return create((Cut)Cut.belowValue((Comparable)lower), (Cut)Cut.aboveValue((Comparable)upper));
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
/*     */   public static <C extends Comparable<?>> Range<C> closedOpen(C lower, C upper) {
/* 192 */     return create((Cut)Cut.belowValue((Comparable)lower), (Cut)Cut.belowValue((Comparable)upper));
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
/*     */   public static <C extends Comparable<?>> Range<C> openClosed(C lower, C upper) {
/* 204 */     return create((Cut)Cut.aboveValue((Comparable)lower), (Cut)Cut.aboveValue((Comparable)upper));
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
/*     */   public static <C extends Comparable<?>> Range<C> range(C lower, BoundType lowerType, C upper, BoundType upperType) {
/* 217 */     Preconditions.checkNotNull(lowerType);
/* 218 */     Preconditions.checkNotNull(upperType);
/*     */ 
/*     */     
/* 221 */     Cut<C> lowerBound = (lowerType == BoundType.OPEN) ? (Cut)Cut.<Comparable>aboveValue((Comparable)lower) : (Cut)Cut.<Comparable>belowValue((Comparable)lower);
/*     */     
/* 223 */     Cut<C> upperBound = (upperType == BoundType.OPEN) ? (Cut)Cut.<Comparable>belowValue((Comparable)upper) : (Cut)Cut.<Comparable>aboveValue((Comparable)upper);
/* 224 */     return create(lowerBound, upperBound);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <C extends Comparable<?>> Range<C> lessThan(C endpoint) {
/* 233 */     return create((Cut)Cut.belowAll(), (Cut)Cut.belowValue((Comparable)endpoint));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <C extends Comparable<?>> Range<C> atMost(C endpoint) {
/* 242 */     return create((Cut)Cut.belowAll(), (Cut)Cut.aboveValue((Comparable)endpoint));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <C extends Comparable<?>> Range<C> upTo(C endpoint, BoundType boundType) {
/* 252 */     switch (boundType) {
/*     */       case OPEN:
/* 254 */         return lessThan(endpoint);
/*     */       case CLOSED:
/* 256 */         return atMost(endpoint);
/*     */     } 
/* 258 */     throw new AssertionError();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <C extends Comparable<?>> Range<C> greaterThan(C endpoint) {
/* 268 */     return create((Cut)Cut.aboveValue((Comparable)endpoint), (Cut)Cut.aboveAll());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <C extends Comparable<?>> Range<C> atLeast(C endpoint) {
/* 277 */     return create((Cut)Cut.belowValue((Comparable)endpoint), (Cut)Cut.aboveAll());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <C extends Comparable<?>> Range<C> downTo(C endpoint, BoundType boundType) {
/* 287 */     switch (boundType) {
/*     */       case OPEN:
/* 289 */         return greaterThan(endpoint);
/*     */       case CLOSED:
/* 291 */         return atLeast(endpoint);
/*     */     } 
/* 293 */     throw new AssertionError();
/*     */   }
/*     */ 
/*     */   
/* 297 */   private static final Range<Comparable> ALL = new Range((Cut)Cut.belowAll(), (Cut)Cut.aboveAll());
/*     */   
/*     */   final Cut<C> lowerBound;
/*     */   
/*     */   final Cut<C> upperBound;
/*     */   
/*     */   private static final long serialVersionUID = 0L;
/*     */   
/*     */   public static <C extends Comparable<?>> Range<C> all() {
/* 306 */     return (Range)ALL;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <C extends Comparable<?>> Range<C> singleton(C value) {
/* 316 */     return closed(value, value);
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
/*     */   public static <C extends Comparable<?>> Range<C> encloseAll(Iterable<C> values) {
/* 329 */     Preconditions.checkNotNull(values);
/* 330 */     if (values instanceof SortedSet) {
/* 331 */       SortedSet<? extends C> set = cast(values);
/* 332 */       Comparator<?> comparator = set.comparator();
/* 333 */       if (Ordering.<Comparable>natural().equals(comparator) || comparator == null) {
/* 334 */         return closed(set.first(), set.last());
/*     */       }
/*     */     } 
/* 337 */     Iterator<C> valueIterator = values.iterator();
/* 338 */     Comparable comparable1 = (Comparable)Preconditions.checkNotNull(valueIterator.next());
/* 339 */     Comparable comparable2 = comparable1;
/* 340 */     while (valueIterator.hasNext()) {
/* 341 */       Comparable comparable = (Comparable)Preconditions.checkNotNull(valueIterator.next());
/* 342 */       comparable1 = (Comparable)Ordering.<Comparable>natural().min(comparable1, comparable);
/* 343 */       comparable2 = (Comparable)Ordering.<Comparable>natural().max(comparable2, comparable);
/*     */     } 
/* 345 */     return closed((C)comparable1, (C)comparable2);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private Range(Cut<C> lowerBound, Cut<C> upperBound) {
/* 352 */     this.lowerBound = (Cut<C>)Preconditions.checkNotNull(lowerBound);
/* 353 */     this.upperBound = (Cut<C>)Preconditions.checkNotNull(upperBound);
/* 354 */     if (lowerBound.compareTo(upperBound) > 0 || lowerBound == 
/* 355 */       Cut.aboveAll() || upperBound == 
/* 356 */       Cut.belowAll()) {
/* 357 */       throw new IllegalArgumentException("Invalid range: " + toString(lowerBound, upperBound));
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean hasLowerBound() {
/* 363 */     return (this.lowerBound != Cut.belowAll());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public C lowerEndpoint() {
/* 373 */     return this.lowerBound.endpoint();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public BoundType lowerBoundType() {
/* 384 */     return this.lowerBound.typeAsLowerBound();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean hasUpperBound() {
/* 389 */     return (this.upperBound != Cut.aboveAll());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public C upperEndpoint() {
/* 399 */     return this.upperBound.endpoint();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public BoundType upperBoundType() {
/* 410 */     return this.upperBound.typeAsUpperBound();
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
/*     */   public boolean isEmpty() {
/* 423 */     return this.lowerBound.equals(this.upperBound);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean contains(C value) {
/* 432 */     Preconditions.checkNotNull(value);
/*     */     
/* 434 */     return (this.lowerBound.isLessThan(value) && !this.upperBound.isLessThan(value));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   public boolean apply(C input) {
/* 444 */     return contains(input);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean containsAll(Iterable<? extends C> values) {
/* 452 */     if (Iterables.isEmpty(values)) {
/* 453 */       return true;
/*     */     }
/*     */ 
/*     */     
/* 457 */     if (values instanceof SortedSet) {
/* 458 */       SortedSet<? extends C> set = cast(values);
/* 459 */       Comparator<?> comparator = set.comparator();
/* 460 */       if (Ordering.<Comparable>natural().equals(comparator) || comparator == null) {
/* 461 */         return (contains(set.first()) && contains(set.last()));
/*     */       }
/*     */     } 
/*     */     
/* 465 */     for (Comparable comparable : values) {
/* 466 */       if (!contains((C)comparable)) {
/* 467 */         return false;
/*     */       }
/*     */     } 
/* 470 */     return true;
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
/*     */   public boolean encloses(Range<C> other) {
/* 497 */     return (this.lowerBound.compareTo(other.lowerBound) <= 0 && this.upperBound
/* 498 */       .compareTo(other.upperBound) >= 0);
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
/*     */   public boolean isConnected(Range<C> other) {
/* 527 */     return (this.lowerBound.compareTo(other.upperBound) <= 0 && other.lowerBound
/* 528 */       .compareTo(this.upperBound) <= 0);
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
/*     */   public Range<C> intersection(Range<C> connectedRange) {
/* 548 */     int lowerCmp = this.lowerBound.compareTo(connectedRange.lowerBound);
/* 549 */     int upperCmp = this.upperBound.compareTo(connectedRange.upperBound);
/* 550 */     if (lowerCmp >= 0 && upperCmp <= 0)
/* 551 */       return this; 
/* 552 */     if (lowerCmp <= 0 && upperCmp >= 0) {
/* 553 */       return connectedRange;
/*     */     }
/* 555 */     Cut<C> newLower = (lowerCmp >= 0) ? this.lowerBound : connectedRange.lowerBound;
/* 556 */     Cut<C> newUpper = (upperCmp <= 0) ? this.upperBound : connectedRange.upperBound;
/* 557 */     return (Range)create(newLower, newUpper);
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
/*     */   public Range<C> gap(Range<C> otherRange) {
/* 579 */     boolean isThisFirst = (this.lowerBound.compareTo(otherRange.lowerBound) < 0);
/* 580 */     Range<C> firstRange = isThisFirst ? this : otherRange;
/* 581 */     Range<C> secondRange = isThisFirst ? otherRange : this;
/* 582 */     return (Range)create(firstRange.upperBound, secondRange.lowerBound);
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
/*     */   public Range<C> span(Range<C> other) {
/* 597 */     int lowerCmp = this.lowerBound.compareTo(other.lowerBound);
/* 598 */     int upperCmp = this.upperBound.compareTo(other.upperBound);
/* 599 */     if (lowerCmp <= 0 && upperCmp >= 0)
/* 600 */       return this; 
/* 601 */     if (lowerCmp >= 0 && upperCmp <= 0) {
/* 602 */       return other;
/*     */     }
/* 604 */     Cut<C> newLower = (lowerCmp <= 0) ? this.lowerBound : other.lowerBound;
/* 605 */     Cut<C> newUpper = (upperCmp >= 0) ? this.upperBound : other.upperBound;
/* 606 */     return (Range)create(newLower, newUpper);
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
/*     */   public Range<C> canonical(DiscreteDomain<C> domain) {
/* 635 */     Preconditions.checkNotNull(domain);
/* 636 */     Cut<C> lower = this.lowerBound.canonical(domain);
/* 637 */     Cut<C> upper = this.upperBound.canonical(domain);
/* 638 */     return (lower == this.lowerBound && upper == this.upperBound) ? this : (Range)create(lower, upper);
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
/*     */   public boolean equals(Object object) {
/* 650 */     if (object instanceof Range) {
/* 651 */       Range<?> other = (Range)object;
/* 652 */       return (this.lowerBound.equals(other.lowerBound) && this.upperBound.equals(other.upperBound));
/*     */     } 
/* 654 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 660 */     return this.lowerBound.hashCode() * 31 + this.upperBound.hashCode();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String toString() {
/* 669 */     return toString(this.lowerBound, this.upperBound);
/*     */   }
/*     */   
/*     */   private static String toString(Cut<?> lowerBound, Cut<?> upperBound) {
/* 673 */     StringBuilder sb = new StringBuilder(16);
/* 674 */     lowerBound.describeAsLowerBound(sb);
/* 675 */     sb.append("..");
/* 676 */     upperBound.describeAsUpperBound(sb);
/* 677 */     return sb.toString();
/*     */   }
/*     */ 
/*     */   
/*     */   private static <T> SortedSet<T> cast(Iterable<T> iterable) {
/* 682 */     return (SortedSet<T>)iterable;
/*     */   }
/*     */   
/*     */   Object readResolve() {
/* 686 */     if (equals(ALL)) {
/* 687 */       return all();
/*     */     }
/* 689 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   static int compareOrThrow(Comparable<Comparable> left, Comparable right) {
/* 695 */     return left.compareTo(right);
/*     */   }
/*     */   
/*     */   private static class RangeLexOrdering
/*     */     extends Ordering<Range<?>> implements Serializable {
/* 700 */     static final Ordering<Range<?>> INSTANCE = new RangeLexOrdering();
/*     */     private static final long serialVersionUID = 0L;
/*     */     
/*     */     public int compare(Range<?> left, Range<?> right) {
/* 704 */       return ComparisonChain.start()
/* 705 */         .compare(left.lowerBound, right.lowerBound)
/* 706 */         .compare(left.upperBound, right.upperBound)
/* 707 */         .result();
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\google\common\collect\Range.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */