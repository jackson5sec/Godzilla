/*     */ package com.google.common.collect;
/*     */ 
/*     */ import com.google.common.annotations.GwtIncompatible;
/*     */ import com.google.common.annotations.VisibleForTesting;
/*     */ import com.google.common.base.Preconditions;
/*     */ import com.google.common.primitives.Ints;
/*     */ import java.util.Comparator;
/*     */ import java.util.NavigableSet;
/*     */ import java.util.Set;
/*     */ import java.util.SortedSet;
/*     */ import java.util.function.ObjIntConsumer;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @GwtIncompatible
/*     */ final class RegularImmutableSortedMultiset<E>
/*     */   extends ImmutableSortedMultiset<E>
/*     */ {
/*  36 */   private static final long[] ZERO_CUMULATIVE_COUNTS = new long[] { 0L };
/*     */   
/*  38 */   static final ImmutableSortedMultiset<Comparable> NATURAL_EMPTY_MULTISET = new RegularImmutableSortedMultiset(
/*  39 */       Ordering.natural());
/*     */   @VisibleForTesting
/*     */   final transient RegularImmutableSortedSet<E> elementSet;
/*     */   private final transient long[] cumulativeCounts;
/*     */   private final transient int offset;
/*     */   private final transient int length;
/*     */   
/*     */   RegularImmutableSortedMultiset(Comparator<? super E> comparator) {
/*  47 */     this.elementSet = ImmutableSortedSet.emptySet(comparator);
/*  48 */     this.cumulativeCounts = ZERO_CUMULATIVE_COUNTS;
/*  49 */     this.offset = 0;
/*  50 */     this.length = 0;
/*     */   }
/*     */ 
/*     */   
/*     */   RegularImmutableSortedMultiset(RegularImmutableSortedSet<E> elementSet, long[] cumulativeCounts, int offset, int length) {
/*  55 */     this.elementSet = elementSet;
/*  56 */     this.cumulativeCounts = cumulativeCounts;
/*  57 */     this.offset = offset;
/*  58 */     this.length = length;
/*     */   }
/*     */   
/*     */   private int getCount(int index) {
/*  62 */     return (int)(this.cumulativeCounts[this.offset + index + 1] - this.cumulativeCounts[this.offset + index]);
/*     */   }
/*     */ 
/*     */   
/*     */   Multiset.Entry<E> getEntry(int index) {
/*  67 */     return Multisets.immutableEntry(this.elementSet.asList().get(index), getCount(index));
/*     */   }
/*     */ 
/*     */   
/*     */   public void forEachEntry(ObjIntConsumer<? super E> action) {
/*  72 */     Preconditions.checkNotNull(action);
/*  73 */     for (int i = 0; i < this.length; i++) {
/*  74 */       action.accept(this.elementSet.asList().get(i), getCount(i));
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public Multiset.Entry<E> firstEntry() {
/*  80 */     return isEmpty() ? null : getEntry(0);
/*     */   }
/*     */ 
/*     */   
/*     */   public Multiset.Entry<E> lastEntry() {
/*  85 */     return isEmpty() ? null : getEntry(this.length - 1);
/*     */   }
/*     */ 
/*     */   
/*     */   public int count(Object element) {
/*  90 */     int index = this.elementSet.indexOf(element);
/*  91 */     return (index >= 0) ? getCount(index) : 0;
/*     */   }
/*     */ 
/*     */   
/*     */   public int size() {
/*  96 */     long size = this.cumulativeCounts[this.offset + this.length] - this.cumulativeCounts[this.offset];
/*  97 */     return Ints.saturatedCast(size);
/*     */   }
/*     */ 
/*     */   
/*     */   public ImmutableSortedSet<E> elementSet() {
/* 102 */     return this.elementSet;
/*     */   }
/*     */ 
/*     */   
/*     */   public ImmutableSortedMultiset<E> headMultiset(E upperBound, BoundType boundType) {
/* 107 */     return getSubMultiset(0, this.elementSet.headIndex(upperBound, (Preconditions.checkNotNull(boundType) == BoundType.CLOSED)));
/*     */   }
/*     */ 
/*     */   
/*     */   public ImmutableSortedMultiset<E> tailMultiset(E lowerBound, BoundType boundType) {
/* 112 */     return getSubMultiset(this.elementSet
/* 113 */         .tailIndex(lowerBound, (Preconditions.checkNotNull(boundType) == BoundType.CLOSED)), this.length);
/*     */   }
/*     */   
/*     */   ImmutableSortedMultiset<E> getSubMultiset(int from, int to) {
/* 117 */     Preconditions.checkPositionIndexes(from, to, this.length);
/* 118 */     if (from == to)
/* 119 */       return emptyMultiset(comparator()); 
/* 120 */     if (from == 0 && to == this.length) {
/* 121 */       return this;
/*     */     }
/* 123 */     RegularImmutableSortedSet<E> subElementSet = this.elementSet.getSubSet(from, to);
/* 124 */     return new RegularImmutableSortedMultiset(subElementSet, this.cumulativeCounts, this.offset + from, to - from);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   boolean isPartialView() {
/* 131 */     return (this.offset > 0 || this.length < this.cumulativeCounts.length - 1);
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\google\common\collect\RegularImmutableSortedMultiset.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */