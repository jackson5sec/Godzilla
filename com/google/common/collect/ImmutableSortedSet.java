/*     */ package com.google.common.collect;
/*     */ 
/*     */ import com.google.common.annotations.GwtCompatible;
/*     */ import com.google.common.annotations.GwtIncompatible;
/*     */ import com.google.common.base.Preconditions;
/*     */ import com.google.errorprone.annotations.CanIgnoreReturnValue;
/*     */ import com.google.errorprone.annotations.concurrent.LazyInit;
/*     */ import java.io.InvalidObjectException;
/*     */ import java.io.ObjectInputStream;
/*     */ import java.io.Serializable;
/*     */ import java.util.Arrays;
/*     */ import java.util.Collection;
/*     */ import java.util.Collections;
/*     */ import java.util.Comparator;
/*     */ import java.util.Iterator;
/*     */ import java.util.NavigableSet;
/*     */ import java.util.SortedSet;
/*     */ import java.util.Spliterator;
/*     */ import java.util.Spliterators;
/*     */ import java.util.function.Consumer;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @GwtCompatible(serializable = true, emulated = true)
/*     */ public abstract class ImmutableSortedSet<E>
/*     */   extends ImmutableSortedSetFauxverideShim<E>
/*     */   implements NavigableSet<E>, SortedIterable<E>
/*     */ {
/*     */   static final int SPLITERATOR_CHARACTERISTICS = 1301;
/*     */   final transient Comparator<? super E> comparator;
/*     */   @LazyInit
/*     */   @GwtIncompatible
/*     */   transient ImmutableSortedSet<E> descendingSet;
/*     */   
/*     */   public static <E> Collector<E, ?, ImmutableSortedSet<E>> toImmutableSortedSet(Comparator<? super E> comparator) {
/*  79 */     return CollectCollectors.toImmutableSortedSet(comparator);
/*     */   }
/*     */   
/*     */   static <E> RegularImmutableSortedSet<E> emptySet(Comparator<? super E> comparator) {
/*  83 */     if (Ordering.<Comparable>natural().equals(comparator)) {
/*  84 */       return (RegularImmutableSortedSet)RegularImmutableSortedSet.NATURAL_EMPTY_SET;
/*     */     }
/*  86 */     return new RegularImmutableSortedSet<>(ImmutableList.of(), comparator);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static <E> ImmutableSortedSet<E> of() {
/*  92 */     return (ImmutableSortedSet)RegularImmutableSortedSet.NATURAL_EMPTY_SET;
/*     */   }
/*     */ 
/*     */   
/*     */   public static <E extends Comparable<? super E>> ImmutableSortedSet<E> of(E element) {
/*  97 */     return new RegularImmutableSortedSet<>(ImmutableList.of(element), Ordering.natural());
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
/*     */   public static <E extends Comparable<? super E>> ImmutableSortedSet<E> of(E e1, E e2) {
/* 109 */     return construct(Ordering.natural(), 2, (E[])new Comparable[] { (Comparable)e1, (Comparable)e2 });
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
/*     */   public static <E extends Comparable<? super E>> ImmutableSortedSet<E> of(E e1, E e2, E e3) {
/* 121 */     return construct(Ordering.natural(), 3, (E[])new Comparable[] { (Comparable)e1, (Comparable)e2, (Comparable)e3 });
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
/*     */   public static <E extends Comparable<? super E>> ImmutableSortedSet<E> of(E e1, E e2, E e3, E e4) {
/* 133 */     return construct(Ordering.natural(), 4, (E[])new Comparable[] { (Comparable)e1, (Comparable)e2, (Comparable)e3, (Comparable)e4 });
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
/*     */   public static <E extends Comparable<? super E>> ImmutableSortedSet<E> of(E e1, E e2, E e3, E e4, E e5) {
/* 146 */     return construct(Ordering.natural(), 5, (E[])new Comparable[] { (Comparable)e1, (Comparable)e2, (Comparable)e3, (Comparable)e4, (Comparable)e5 });
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
/*     */   public static <E extends Comparable<? super E>> ImmutableSortedSet<E> of(E e1, E e2, E e3, E e4, E e5, E e6, E... remaining) {
/* 160 */     Comparable[] contents = new Comparable[6 + remaining.length];
/* 161 */     contents[0] = (Comparable)e1;
/* 162 */     contents[1] = (Comparable)e2;
/* 163 */     contents[2] = (Comparable)e3;
/* 164 */     contents[3] = (Comparable)e4;
/* 165 */     contents[4] = (Comparable)e5;
/* 166 */     contents[5] = (Comparable)e6;
/* 167 */     System.arraycopy(remaining, 0, contents, 6, remaining.length);
/* 168 */     return construct(Ordering.natural(), contents.length, (E[])contents);
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
/*     */   public static <E extends Comparable<? super E>> ImmutableSortedSet<E> copyOf(E[] elements) {
/* 182 */     return construct(Ordering.natural(), elements.length, (E[])elements.clone());
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
/*     */   public static <E> ImmutableSortedSet<E> copyOf(Iterable<? extends E> elements) {
/* 210 */     Ordering<E> naturalOrder = Ordering.natural();
/* 211 */     return copyOf(naturalOrder, elements);
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
/*     */   public static <E> ImmutableSortedSet<E> copyOf(Collection<? extends E> elements) {
/* 242 */     Ordering<E> naturalOrder = Ordering.natural();
/* 243 */     return copyOf(naturalOrder, elements);
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
/*     */   public static <E> ImmutableSortedSet<E> copyOf(Iterator<? extends E> elements) {
/* 261 */     Ordering<E> naturalOrder = Ordering.natural();
/* 262 */     return copyOf(naturalOrder, elements);
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
/*     */   public static <E> ImmutableSortedSet<E> copyOf(Comparator<? super E> comparator, Iterator<? extends E> elements) {
/* 274 */     return (new Builder<>(comparator)).addAll(elements).build();
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
/*     */   public static <E> ImmutableSortedSet<E> copyOf(Comparator<? super E> comparator, Iterable<? extends E> elements) {
/* 290 */     Preconditions.checkNotNull(comparator);
/* 291 */     boolean hasSameComparator = SortedIterables.hasSameComparator(comparator, elements);
/*     */     
/* 293 */     if (hasSameComparator && elements instanceof ImmutableSortedSet) {
/*     */       
/* 295 */       ImmutableSortedSet<E> original = (ImmutableSortedSet)elements;
/* 296 */       if (!original.isPartialView()) {
/* 297 */         return original;
/*     */       }
/*     */     } 
/*     */     
/* 301 */     E[] array = (E[])Iterables.toArray(elements);
/* 302 */     return construct(comparator, array.length, array);
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
/*     */   public static <E> ImmutableSortedSet<E> copyOf(Comparator<? super E> comparator, Collection<? extends E> elements) {
/* 322 */     return copyOf(comparator, elements);
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
/*     */   public static <E> ImmutableSortedSet<E> copyOfSorted(SortedSet<E> sortedSet) {
/* 340 */     Comparator<? super E> comparator = SortedIterables.comparator(sortedSet);
/* 341 */     ImmutableList<E> list = ImmutableList.copyOf(sortedSet);
/* 342 */     if (list.isEmpty()) {
/* 343 */       return emptySet(comparator);
/*     */     }
/* 345 */     return new RegularImmutableSortedSet<>(list, comparator);
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
/*     */   static <E> ImmutableSortedSet<E> construct(Comparator<? super E> comparator, int n, E... contents) {
/* 362 */     if (n == 0) {
/* 363 */       return emptySet(comparator);
/*     */     }
/* 365 */     ObjectArrays.checkElementsNotNull((Object[])contents, n);
/* 366 */     Arrays.sort(contents, 0, n, comparator);
/* 367 */     int uniques = 1;
/* 368 */     for (int i = 1; i < n; i++) {
/* 369 */       E cur = contents[i];
/* 370 */       E prev = contents[uniques - 1];
/* 371 */       if (comparator.compare(cur, prev) != 0) {
/* 372 */         contents[uniques++] = cur;
/*     */       }
/*     */     } 
/* 375 */     Arrays.fill((Object[])contents, uniques, n, (Object)null);
/* 376 */     return new RegularImmutableSortedSet<>(
/* 377 */         ImmutableList.asImmutableList((Object[])contents, uniques), comparator);
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
/*     */   public static <E> Builder<E> orderedBy(Comparator<E> comparator) {
/* 389 */     return new Builder<>(comparator);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <E extends Comparable<?>> Builder<E> reverseOrder() {
/* 397 */     return new Builder<>(Collections.reverseOrder());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <E extends Comparable<?>> Builder<E> naturalOrder() {
/* 407 */     return new Builder<>(Ordering.natural());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static final class Builder<E>
/*     */     extends ImmutableSet.Builder<E>
/*     */   {
/*     */     private final Comparator<? super E> comparator;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     private E[] elements;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     private int n;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public Builder(Comparator<? super E> comparator) {
/* 437 */       super(true);
/* 438 */       this.comparator = (Comparator<? super E>)Preconditions.checkNotNull(comparator);
/* 439 */       this.elements = (E[])new Object[4];
/* 440 */       this.n = 0;
/*     */     }
/*     */ 
/*     */     
/*     */     void copy() {
/* 445 */       this.elements = Arrays.copyOf(this.elements, this.elements.length);
/*     */     }
/*     */     
/*     */     private void sortAndDedup() {
/* 449 */       if (this.n == 0) {
/*     */         return;
/*     */       }
/* 452 */       Arrays.sort(this.elements, 0, this.n, this.comparator);
/* 453 */       int unique = 1;
/* 454 */       for (int i = 1; i < this.n; i++) {
/* 455 */         int cmp = this.comparator.compare(this.elements[unique - 1], this.elements[i]);
/* 456 */         if (cmp < 0) {
/* 457 */           this.elements[unique++] = this.elements[i];
/* 458 */         } else if (cmp > 0) {
/* 459 */           throw new AssertionError("Comparator " + this.comparator + " compare method violates its contract");
/*     */         } 
/*     */       } 
/*     */       
/* 463 */       Arrays.fill((Object[])this.elements, unique, this.n, (Object)null);
/* 464 */       this.n = unique;
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
/*     */     @CanIgnoreReturnValue
/*     */     public Builder<E> add(E element) {
/* 479 */       Preconditions.checkNotNull(element);
/* 480 */       copyIfNecessary();
/* 481 */       if (this.n == this.elements.length) {
/* 482 */         sortAndDedup();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 488 */         int newLength = ImmutableCollection.Builder.expandedCapacity(this.n, this.n + 1);
/* 489 */         if (newLength > this.elements.length) {
/* 490 */           this.elements = Arrays.copyOf(this.elements, newLength);
/*     */         }
/*     */       } 
/* 493 */       this.elements[this.n++] = element;
/* 494 */       return this;
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
/*     */     @CanIgnoreReturnValue
/*     */     public Builder<E> add(E... elements) {
/* 508 */       ObjectArrays.checkElementsNotNull((Object[])elements);
/* 509 */       for (E e : elements) {
/* 510 */         add(e);
/*     */       }
/* 512 */       return this;
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
/*     */     @CanIgnoreReturnValue
/*     */     public Builder<E> addAll(Iterable<? extends E> elements) {
/* 526 */       super.addAll(elements);
/* 527 */       return this;
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
/*     */     @CanIgnoreReturnValue
/*     */     public Builder<E> addAll(Iterator<? extends E> elements) {
/* 541 */       super.addAll(elements);
/* 542 */       return this;
/*     */     }
/*     */ 
/*     */     
/*     */     @CanIgnoreReturnValue
/*     */     Builder<E> combine(ImmutableSet.Builder<E> builder) {
/* 548 */       copyIfNecessary();
/* 549 */       Builder<E> other = (Builder<E>)builder;
/* 550 */       for (int i = 0; i < other.n; i++) {
/* 551 */         add(other.elements[i]);
/*     */       }
/* 553 */       return this;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public ImmutableSortedSet<E> build() {
/* 562 */       sortAndDedup();
/* 563 */       if (this.n == 0) {
/* 564 */         return ImmutableSortedSet.emptySet(this.comparator);
/*     */       }
/* 566 */       this.forceCopy = true;
/* 567 */       return new RegularImmutableSortedSet<>(
/* 568 */           ImmutableList.asImmutableList((Object[])this.elements, this.n), this.comparator);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   int unsafeCompare(Object a, Object b) {
/* 574 */     return unsafeCompare(this.comparator, a, b);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static int unsafeCompare(Comparator<?> comparator, Object a, Object b) {
/* 582 */     Comparator<Object> unsafeComparator = (Comparator)comparator;
/* 583 */     return unsafeComparator.compare(a, b);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   ImmutableSortedSet(Comparator<? super E> comparator) {
/* 589 */     this.comparator = comparator;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Comparator<? super E> comparator() {
/* 599 */     return this.comparator;
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
/*     */   public ImmutableSortedSet<E> headSet(E toElement) {
/* 617 */     return headSet(toElement, false);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @GwtIncompatible
/*     */   public ImmutableSortedSet<E> headSet(E toElement, boolean inclusive) {
/* 624 */     return headSetImpl((E)Preconditions.checkNotNull(toElement), inclusive);
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
/*     */   public ImmutableSortedSet<E> subSet(E fromElement, E toElement) {
/* 641 */     return subSet(fromElement, true, toElement, false);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @GwtIncompatible
/*     */   public ImmutableSortedSet<E> subSet(E fromElement, boolean fromInclusive, E toElement, boolean toInclusive) {
/* 649 */     Preconditions.checkNotNull(fromElement);
/* 650 */     Preconditions.checkNotNull(toElement);
/* 651 */     Preconditions.checkArgument((this.comparator.compare(fromElement, toElement) <= 0));
/* 652 */     return subSetImpl(fromElement, fromInclusive, toElement, toInclusive);
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
/*     */   public ImmutableSortedSet<E> tailSet(E fromElement) {
/* 667 */     return tailSet(fromElement, true);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @GwtIncompatible
/*     */   public ImmutableSortedSet<E> tailSet(E fromElement, boolean inclusive) {
/* 674 */     return tailSetImpl((E)Preconditions.checkNotNull(fromElement), inclusive);
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
/*     */   @GwtIncompatible
/*     */   public E lower(E e) {
/* 692 */     return Iterators.getNext(headSet(e, false).descendingIterator(), null);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @GwtIncompatible
/*     */   public E floor(E e) {
/* 699 */     return Iterators.getNext(headSet(e, true).descendingIterator(), null);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @GwtIncompatible
/*     */   public E ceiling(E e) {
/* 706 */     return Iterables.getFirst(tailSet(e, true), null);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @GwtIncompatible
/*     */   public E higher(E e) {
/* 713 */     return Iterables.getFirst(tailSet(e, false), null);
/*     */   }
/*     */ 
/*     */   
/*     */   public E first() {
/* 718 */     return iterator().next();
/*     */   }
/*     */ 
/*     */   
/*     */   public E last() {
/* 723 */     return descendingIterator().next();
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
/*     */   @CanIgnoreReturnValue
/*     */   @GwtIncompatible
/*     */   public final E pollFirst() {
/* 738 */     throw new UnsupportedOperationException();
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
/*     */   @CanIgnoreReturnValue
/*     */   @GwtIncompatible
/*     */   public final E pollLast() {
/* 753 */     throw new UnsupportedOperationException();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @GwtIncompatible
/*     */   public ImmutableSortedSet<E> descendingSet() {
/* 765 */     ImmutableSortedSet<E> result = this.descendingSet;
/* 766 */     if (result == null) {
/* 767 */       result = this.descendingSet = createDescendingSet();
/* 768 */       result.descendingSet = this;
/*     */     } 
/* 770 */     return result;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Spliterator<E> spliterator() {
/* 781 */     return new Spliterators.AbstractSpliterator<E>(
/* 782 */         size(), 1365) {
/* 783 */         final UnmodifiableIterator<E> iterator = ImmutableSortedSet.this.iterator();
/*     */ 
/*     */         
/*     */         public boolean tryAdvance(Consumer<? super E> action) {
/* 787 */           if (this.iterator.hasNext()) {
/* 788 */             action.accept(this.iterator.next());
/* 789 */             return true;
/*     */           } 
/* 791 */           return false;
/*     */         }
/*     */ 
/*     */ 
/*     */         
/*     */         public Comparator<? super E> getComparator() {
/* 797 */           return ImmutableSortedSet.this.comparator;
/*     */         }
/*     */       };
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static class SerializedForm<E>
/*     */     implements Serializable
/*     */   {
/*     */     final Comparator<? super E> comparator;
/*     */ 
/*     */ 
/*     */     
/*     */     final Object[] elements;
/*     */ 
/*     */ 
/*     */     
/*     */     private static final long serialVersionUID = 0L;
/*     */ 
/*     */ 
/*     */     
/*     */     public SerializedForm(Comparator<? super E> comparator, Object[] elements) {
/* 821 */       this.comparator = comparator;
/* 822 */       this.elements = elements;
/*     */     }
/*     */ 
/*     */     
/*     */     Object readResolve() {
/* 827 */       return (new ImmutableSortedSet.Builder((Comparator)this.comparator)).add(this.elements).build();
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private void readObject(ObjectInputStream unused) throws InvalidObjectException {
/* 834 */     throw new InvalidObjectException("Use SerializedForm");
/*     */   }
/*     */ 
/*     */   
/*     */   Object writeReplace() {
/* 839 */     return new SerializedForm<>(this.comparator, toArray());
/*     */   }
/*     */   
/*     */   public abstract UnmodifiableIterator<E> iterator();
/*     */   
/*     */   abstract ImmutableSortedSet<E> headSetImpl(E paramE, boolean paramBoolean);
/*     */   
/*     */   abstract ImmutableSortedSet<E> subSetImpl(E paramE1, boolean paramBoolean1, E paramE2, boolean paramBoolean2);
/*     */   
/*     */   abstract ImmutableSortedSet<E> tailSetImpl(E paramE, boolean paramBoolean);
/*     */   
/*     */   @GwtIncompatible
/*     */   abstract ImmutableSortedSet<E> createDescendingSet();
/*     */   
/*     */   @GwtIncompatible
/*     */   public abstract UnmodifiableIterator<E> descendingIterator();
/*     */   
/*     */   abstract int indexOf(Object paramObject);
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\google\common\collect\ImmutableSortedSet.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */