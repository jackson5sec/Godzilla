/*     */ package com.google.common.collect;
/*     */ 
/*     */ import com.google.common.annotations.Beta;
/*     */ import com.google.common.annotations.GwtCompatible;
/*     */ import com.google.common.annotations.GwtIncompatible;
/*     */ import com.google.common.base.Preconditions;
/*     */ import java.util.NavigableSet;
/*     */ import java.util.NoSuchElementException;
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
/*     */ @GwtCompatible(emulated = true)
/*     */ public abstract class ContiguousSet<C extends Comparable>
/*     */   extends ImmutableSortedSet<C>
/*     */ {
/*     */   final DiscreteDomain<C> domain;
/*     */   
/*     */   public static <C extends Comparable> ContiguousSet<C> create(Range<C> range, DiscreteDomain<C> domain) {
/*  62 */     Preconditions.checkNotNull(range);
/*  63 */     Preconditions.checkNotNull(domain);
/*  64 */     Range<C> effectiveRange = range;
/*     */     try {
/*  66 */       if (!range.hasLowerBound()) {
/*  67 */         effectiveRange = effectiveRange.intersection((Range)Range.atLeast((Comparable<?>)domain.minValue()));
/*     */       }
/*  69 */       if (!range.hasUpperBound()) {
/*  70 */         effectiveRange = effectiveRange.intersection((Range)Range.atMost((Comparable<?>)domain.maxValue()));
/*     */       }
/*  72 */     } catch (NoSuchElementException e) {
/*  73 */       throw new IllegalArgumentException(e);
/*     */     } 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  79 */     boolean empty = (effectiveRange.isEmpty() || Range.compareOrThrow((Comparable)range.lowerBound
/*  80 */         .leastValueAbove(domain), (Comparable)range.upperBound
/*  81 */         .greatestValueBelow(domain)) > 0);
/*     */ 
/*     */     
/*  84 */     return empty ? new EmptyContiguousSet<>(domain) : new RegularContiguousSet<>(effectiveRange, domain);
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
/*     */   public static ContiguousSet<Integer> closed(int lower, int upper) {
/*  99 */     return create(Range.closed(Integer.valueOf(lower), Integer.valueOf(upper)), DiscreteDomain.integers());
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
/*     */   @Beta
/*     */   public static ContiguousSet<Long> closed(long lower, long upper) {
/* 112 */     return create(Range.closed(Long.valueOf(lower), Long.valueOf(upper)), DiscreteDomain.longs());
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
/*     */   @Beta
/*     */   public static ContiguousSet<Integer> closedOpen(int lower, int upper) {
/* 125 */     return create(Range.closedOpen(Integer.valueOf(lower), Integer.valueOf(upper)), DiscreteDomain.integers());
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
/*     */   @Beta
/*     */   public static ContiguousSet<Long> closedOpen(long lower, long upper) {
/* 138 */     return create(Range.closedOpen(Long.valueOf(lower), Long.valueOf(upper)), DiscreteDomain.longs());
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   ContiguousSet(DiscreteDomain<C> domain) {
/* 144 */     super(Ordering.natural());
/* 145 */     this.domain = domain;
/*     */   }
/*     */ 
/*     */   
/*     */   public ContiguousSet<C> headSet(C toElement) {
/* 150 */     return headSetImpl((C)Preconditions.checkNotNull(toElement), false);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @GwtIncompatible
/*     */   public ContiguousSet<C> headSet(C toElement, boolean inclusive) {
/* 157 */     return headSetImpl((C)Preconditions.checkNotNull(toElement), inclusive);
/*     */   }
/*     */ 
/*     */   
/*     */   public ContiguousSet<C> subSet(C fromElement, C toElement) {
/* 162 */     Preconditions.checkNotNull(fromElement);
/* 163 */     Preconditions.checkNotNull(toElement);
/* 164 */     Preconditions.checkArgument((comparator().compare(fromElement, toElement) <= 0));
/* 165 */     return subSetImpl(fromElement, true, toElement, false);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @GwtIncompatible
/*     */   public ContiguousSet<C> subSet(C fromElement, boolean fromInclusive, C toElement, boolean toInclusive) {
/* 173 */     Preconditions.checkNotNull(fromElement);
/* 174 */     Preconditions.checkNotNull(toElement);
/* 175 */     Preconditions.checkArgument((comparator().compare(fromElement, toElement) <= 0));
/* 176 */     return subSetImpl(fromElement, fromInclusive, toElement, toInclusive);
/*     */   }
/*     */ 
/*     */   
/*     */   public ContiguousSet<C> tailSet(C fromElement) {
/* 181 */     return tailSetImpl((C)Preconditions.checkNotNull(fromElement), true);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @GwtIncompatible
/*     */   public ContiguousSet<C> tailSet(C fromElement, boolean inclusive) {
/* 188 */     return tailSetImpl((C)Preconditions.checkNotNull(fromElement), inclusive);
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
/*     */   @GwtIncompatible
/*     */   ImmutableSortedSet<C> createDescendingSet() {
/* 237 */     return new DescendingImmutableSortedSet<>(this);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String toString() {
/* 243 */     return range().toString();
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
/*     */   @Deprecated
/*     */   public static <E> ImmutableSortedSet.Builder<E> builder() {
/* 256 */     throw new UnsupportedOperationException();
/*     */   }
/*     */   
/*     */   abstract ContiguousSet<C> headSetImpl(C paramC, boolean paramBoolean);
/*     */   
/*     */   abstract ContiguousSet<C> subSetImpl(C paramC1, boolean paramBoolean1, C paramC2, boolean paramBoolean2);
/*     */   
/*     */   abstract ContiguousSet<C> tailSetImpl(C paramC, boolean paramBoolean);
/*     */   
/*     */   public abstract ContiguousSet<C> intersection(ContiguousSet<C> paramContiguousSet);
/*     */   
/*     */   public abstract Range<C> range();
/*     */   
/*     */   public abstract Range<C> range(BoundType paramBoundType1, BoundType paramBoundType2);
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\google\common\collect\ContiguousSet.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */