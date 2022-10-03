/*     */ package com.google.common.collect;
/*     */ 
/*     */ import com.google.common.annotations.GwtIncompatible;
/*     */ import com.google.common.base.Preconditions;
/*     */ import com.google.errorprone.annotations.CanIgnoreReturnValue;
/*     */ import com.google.errorprone.annotations.concurrent.LazyInit;
/*     */ import java.io.Serializable;
/*     */ import java.util.Arrays;
/*     */ import java.util.Collection;
/*     */ import java.util.Collections;
/*     */ import java.util.Comparator;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.NavigableSet;
/*     */ import java.util.Set;
/*     */ import java.util.SortedSet;
/*     */ import java.util.function.Function;
/*     */ import java.util.function.ToIntFunction;
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
/*     */ @GwtIncompatible
/*     */ public abstract class ImmutableSortedMultiset<E>
/*     */   extends ImmutableSortedMultisetFauxverideShim<E>
/*     */   implements SortedMultiset<E>
/*     */ {
/*     */   @LazyInit
/*     */   transient ImmutableSortedMultiset<E> descendingMultiset;
/*     */   
/*     */   public static <E> Collector<E, ?, ImmutableSortedMultiset<E>> toImmutableSortedMultiset(Comparator<? super E> comparator) {
/*  66 */     return toImmutableSortedMultiset(comparator, (Function)Function.identity(), e -> 1);
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
/*     */   public static <T, E> Collector<T, ?, ImmutableSortedMultiset<E>> toImmutableSortedMultiset(Comparator<? super E> comparator, Function<? super T, ? extends E> elementFunction, ToIntFunction<? super T> countFunction) {
/*  84 */     Preconditions.checkNotNull(comparator);
/*  85 */     Preconditions.checkNotNull(elementFunction);
/*  86 */     Preconditions.checkNotNull(countFunction);
/*  87 */     return Collector.of(() -> TreeMultiset.create(comparator), (multiset, t) -> multiset.add(Preconditions.checkNotNull(elementFunction.apply(t)), countFunction.applyAsInt(t)), (multiset1, multiset2) -> { multiset1.addAll(multiset2); return multiset1; }multiset -> copyOfSortedEntries(comparator, multiset.entrySet()), new Collector.Characteristics[0]);
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
/*     */   public static <E> ImmutableSortedMultiset<E> of() {
/* 101 */     return (ImmutableSortedMultiset)RegularImmutableSortedMultiset.NATURAL_EMPTY_MULTISET;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static <E extends Comparable<? super E>> ImmutableSortedMultiset<E> of(E element) {
/* 107 */     RegularImmutableSortedSet<E> elementSet = (RegularImmutableSortedSet<E>)ImmutableSortedSet.<E>of(element);
/* 108 */     long[] cumulativeCounts = { 0L, 1L };
/* 109 */     return new RegularImmutableSortedMultiset<>(elementSet, cumulativeCounts, 0, 1);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <E extends Comparable<? super E>> ImmutableSortedMultiset<E> of(E e1, E e2) {
/* 120 */     return copyOf(Ordering.natural(), Arrays.asList((E[])new Comparable[] { (Comparable)e1, (Comparable)e2 }));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <E extends Comparable<? super E>> ImmutableSortedMultiset<E> of(E e1, E e2, E e3) {
/* 131 */     return copyOf(Ordering.natural(), Arrays.asList((E[])new Comparable[] { (Comparable)e1, (Comparable)e2, (Comparable)e3 }));
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
/*     */   public static <E extends Comparable<? super E>> ImmutableSortedMultiset<E> of(E e1, E e2, E e3, E e4) {
/* 143 */     return copyOf(Ordering.natural(), Arrays.asList((E[])new Comparable[] { (Comparable)e1, (Comparable)e2, (Comparable)e3, (Comparable)e4 }));
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
/*     */   public static <E extends Comparable<? super E>> ImmutableSortedMultiset<E> of(E e1, E e2, E e3, E e4, E e5) {
/* 155 */     return copyOf(Ordering.natural(), Arrays.asList((E[])new Comparable[] { (Comparable)e1, (Comparable)e2, (Comparable)e3, (Comparable)e4, (Comparable)e5 }));
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
/*     */   public static <E extends Comparable<? super E>> ImmutableSortedMultiset<E> of(E e1, E e2, E e3, E e4, E e5, E e6, E... remaining) {
/* 167 */     int size = remaining.length + 6;
/* 168 */     List<E> all = Lists.newArrayListWithCapacity(size);
/* 169 */     Collections.addAll(all, (E[])new Comparable[] { (Comparable)e1, (Comparable)e2, (Comparable)e3, (Comparable)e4, (Comparable)e5, (Comparable)e6 });
/* 170 */     Collections.addAll(all, remaining);
/* 171 */     return copyOf(Ordering.natural(), all);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <E extends Comparable<? super E>> ImmutableSortedMultiset<E> copyOf(E[] elements) {
/* 181 */     return copyOf(Ordering.natural(), Arrays.asList(elements));
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
/*     */   public static <E> ImmutableSortedMultiset<E> copyOf(Iterable<? extends E> elements) {
/* 209 */     Ordering<E> naturalOrder = Ordering.natural();
/* 210 */     return copyOf(naturalOrder, elements);
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
/*     */   public static <E> ImmutableSortedMultiset<E> copyOf(Iterator<? extends E> elements) {
/* 227 */     Ordering<E> naturalOrder = Ordering.natural();
/* 228 */     return copyOf(naturalOrder, elements);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <E> ImmutableSortedMultiset<E> copyOf(Comparator<? super E> comparator, Iterator<? extends E> elements) {
/* 239 */     Preconditions.checkNotNull(comparator);
/* 240 */     return (new Builder<>(comparator)).addAll(elements).build();
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
/*     */   public static <E> ImmutableSortedMultiset<E> copyOf(Comparator<? super E> comparator, Iterable<? extends E> elements) {
/* 255 */     if (elements instanceof ImmutableSortedMultiset) {
/*     */       
/* 257 */       ImmutableSortedMultiset<E> multiset = (ImmutableSortedMultiset)elements;
/* 258 */       if (comparator.equals(multiset.comparator())) {
/* 259 */         if (multiset.isPartialView()) {
/* 260 */           return copyOfSortedEntries(comparator, multiset.entrySet().asList());
/*     */         }
/* 262 */         return multiset;
/*     */       } 
/*     */     } 
/*     */     
/* 266 */     elements = Lists.newArrayList(elements);
/* 267 */     TreeMultiset<E> sortedCopy = TreeMultiset.create((Comparator<? super E>)Preconditions.checkNotNull(comparator));
/* 268 */     Iterables.addAll(sortedCopy, elements);
/* 269 */     return copyOfSortedEntries(comparator, sortedCopy.entrySet());
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
/*     */   public static <E> ImmutableSortedMultiset<E> copyOfSorted(SortedMultiset<E> sortedMultiset) {
/* 287 */     return copyOfSortedEntries(sortedMultiset
/* 288 */         .comparator(), Lists.newArrayList(sortedMultiset.entrySet()));
/*     */   }
/*     */ 
/*     */   
/*     */   private static <E> ImmutableSortedMultiset<E> copyOfSortedEntries(Comparator<? super E> comparator, Collection<Multiset.Entry<E>> entries) {
/* 293 */     if (entries.isEmpty()) {
/* 294 */       return emptyMultiset(comparator);
/*     */     }
/* 296 */     ImmutableList.Builder<E> elementsBuilder = new ImmutableList.Builder<>(entries.size());
/* 297 */     long[] cumulativeCounts = new long[entries.size() + 1];
/* 298 */     int i = 0;
/* 299 */     for (Multiset.Entry<E> entry : entries) {
/* 300 */       elementsBuilder.add(entry.getElement());
/* 301 */       cumulativeCounts[i + 1] = cumulativeCounts[i] + entry.getCount();
/* 302 */       i++;
/*     */     } 
/* 304 */     return new RegularImmutableSortedMultiset<>(new RegularImmutableSortedSet<>(elementsBuilder
/* 305 */           .build(), comparator), cumulativeCounts, 0, entries
/*     */ 
/*     */         
/* 308 */         .size());
/*     */   }
/*     */ 
/*     */   
/*     */   static <E> ImmutableSortedMultiset<E> emptyMultiset(Comparator<? super E> comparator) {
/* 313 */     if (Ordering.<Comparable>natural().equals(comparator)) {
/* 314 */       return (ImmutableSortedMultiset)RegularImmutableSortedMultiset.NATURAL_EMPTY_MULTISET;
/*     */     }
/* 316 */     return new RegularImmutableSortedMultiset<>(comparator);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final Comparator<? super E> comparator() {
/* 324 */     return elementSet().comparator();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ImmutableSortedMultiset<E> descendingMultiset() {
/* 334 */     ImmutableSortedMultiset<E> result = this.descendingMultiset;
/* 335 */     if (result == null) {
/* 336 */       return this
/*     */         
/* 338 */         .descendingMultiset = isEmpty() ? emptyMultiset(Ordering.from(comparator()).reverse()) : new DescendingImmutableSortedMultiset<>(this);
/*     */     }
/*     */     
/* 341 */     return result;
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
/*     */   @Deprecated
/*     */   @CanIgnoreReturnValue
/*     */   public final Multiset.Entry<E> pollFirstEntry() {
/* 356 */     throw new UnsupportedOperationException();
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
/*     */   @Deprecated
/*     */   @CanIgnoreReturnValue
/*     */   public final Multiset.Entry<E> pollLastEntry() {
/* 371 */     throw new UnsupportedOperationException();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ImmutableSortedMultiset<E> subMultiset(E lowerBound, BoundType lowerBoundType, E upperBound, BoundType upperBoundType) {
/* 380 */     Preconditions.checkArgument(
/* 381 */         (comparator().compare(lowerBound, upperBound) <= 0), "Expected lowerBound <= upperBound but %s > %s", lowerBound, upperBound);
/*     */ 
/*     */ 
/*     */     
/* 385 */     return tailMultiset(lowerBound, lowerBoundType).headMultiset(upperBound, upperBoundType);
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
/*     */   public static <E> Builder<E> orderedBy(Comparator<E> comparator) {
/* 400 */     return new Builder<>(comparator);
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
/*     */   public static <E extends Comparable<?>> Builder<E> reverseOrder() {
/* 412 */     return new Builder<>(Ordering.<Comparable>natural().reverse());
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
/*     */   public static <E extends Comparable<?>> Builder<E> naturalOrder() {
/* 426 */     return new Builder<>(Ordering.natural());
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
/*     */   public static class Builder<E>
/*     */     extends ImmutableMultiset.Builder<E>
/*     */   {
/*     */     public Builder(Comparator<? super E> comparator) {
/* 454 */       super(TreeMultiset.create((Comparator<? super E>)Preconditions.checkNotNull(comparator)));
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
/*     */     public Builder<E> add(E element) {
/* 467 */       super.add(element);
/* 468 */       return this;
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
/*     */     public Builder<E> add(E... elements) {
/* 481 */       super.add(elements);
/* 482 */       return this;
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
/*     */     public Builder<E> addCopies(E element, int occurrences) {
/* 499 */       super.addCopies(element, occurrences);
/* 500 */       return this;
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
/*     */     public Builder<E> setCount(E element, int count) {
/* 516 */       super.setCount(element, count);
/* 517 */       return this;
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
/*     */     public Builder<E> addAll(Iterable<? extends E> elements) {
/* 530 */       super.addAll(elements);
/* 531 */       return this;
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
/*     */     public Builder<E> addAll(Iterator<? extends E> elements) {
/* 544 */       super.addAll(elements);
/* 545 */       return this;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public ImmutableSortedMultiset<E> build() {
/* 554 */       return ImmutableSortedMultiset.copyOfSorted((SortedMultiset<E>)this.contents);
/*     */     }
/*     */   }
/*     */   
/*     */   private static final class SerializedForm<E>
/*     */     implements Serializable {
/*     */     final Comparator<? super E> comparator;
/*     */     final E[] elements;
/*     */     final int[] counts;
/*     */     
/*     */     SerializedForm(SortedMultiset<E> multiset) {
/* 565 */       this.comparator = multiset.comparator();
/* 566 */       int n = multiset.entrySet().size();
/* 567 */       this.elements = (E[])new Object[n];
/* 568 */       this.counts = new int[n];
/* 569 */       int i = 0;
/* 570 */       for (Multiset.Entry<E> entry : multiset.entrySet()) {
/* 571 */         this.elements[i] = entry.getElement();
/* 572 */         this.counts[i] = entry.getCount();
/* 573 */         i++;
/*     */       } 
/*     */     }
/*     */     
/*     */     Object readResolve() {
/* 578 */       int n = this.elements.length;
/* 579 */       ImmutableSortedMultiset.Builder<E> builder = new ImmutableSortedMultiset.Builder<>(this.comparator);
/* 580 */       for (int i = 0; i < n; i++) {
/* 581 */         builder.addCopies(this.elements[i], this.counts[i]);
/*     */       }
/* 583 */       return builder.build();
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   Object writeReplace() {
/* 589 */     return new SerializedForm<>(this);
/*     */   }
/*     */   
/*     */   public abstract ImmutableSortedSet<E> elementSet();
/*     */   
/*     */   public abstract ImmutableSortedMultiset<E> headMultiset(E paramE, BoundType paramBoundType);
/*     */   
/*     */   public abstract ImmutableSortedMultiset<E> tailMultiset(E paramE, BoundType paramBoundType);
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\google\common\collect\ImmutableSortedMultiset.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */