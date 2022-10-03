/*     */ package com.google.common.collect;
/*     */ 
/*     */ import com.google.common.annotations.Beta;
/*     */ import com.google.common.annotations.GwtCompatible;
/*     */ import com.google.common.annotations.VisibleForTesting;
/*     */ import com.google.common.base.Preconditions;
/*     */ import com.google.errorprone.annotations.CanIgnoreReturnValue;
/*     */ import java.io.InvalidObjectException;
/*     */ import java.io.ObjectInputStream;
/*     */ import java.io.Serializable;
/*     */ import java.util.Arrays;
/*     */ import java.util.Collection;
/*     */ import java.util.Comparator;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.ListIterator;
/*     */ import java.util.RandomAccess;
/*     */ import java.util.Spliterator;
/*     */ import java.util.function.Consumer;
/*     */ import java.util.function.UnaryOperator;
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
/*     */ @GwtCompatible(serializable = true, emulated = true)
/*     */ public abstract class ImmutableList<E>
/*     */   extends ImmutableCollection<E>
/*     */   implements List<E>, RandomAccess
/*     */ {
/*     */   public static <E> Collector<E, ?, ImmutableList<E>> toImmutableList() {
/*  71 */     return CollectCollectors.toImmutableList();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <E> ImmutableList<E> of() {
/*  82 */     return (ImmutableList)RegularImmutableList.EMPTY;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <E> ImmutableList<E> of(E element) {
/*  93 */     return new SingletonImmutableList<>(element);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <E> ImmutableList<E> of(E e1, E e2) {
/* 102 */     return construct(new Object[] { e1, e2 });
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <E> ImmutableList<E> of(E e1, E e2, E e3) {
/* 111 */     return construct(new Object[] { e1, e2, e3 });
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <E> ImmutableList<E> of(E e1, E e2, E e3, E e4) {
/* 120 */     return construct(new Object[] { e1, e2, e3, e4 });
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <E> ImmutableList<E> of(E e1, E e2, E e3, E e4, E e5) {
/* 129 */     return construct(new Object[] { e1, e2, e3, e4, e5 });
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <E> ImmutableList<E> of(E e1, E e2, E e3, E e4, E e5, E e6) {
/* 138 */     return construct(new Object[] { e1, e2, e3, e4, e5, e6 });
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <E> ImmutableList<E> of(E e1, E e2, E e3, E e4, E e5, E e6, E e7) {
/* 147 */     return construct(new Object[] { e1, e2, e3, e4, e5, e6, e7 });
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <E> ImmutableList<E> of(E e1, E e2, E e3, E e4, E e5, E e6, E e7, E e8) {
/* 156 */     return construct(new Object[] { e1, e2, e3, e4, e5, e6, e7, e8 });
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <E> ImmutableList<E> of(E e1, E e2, E e3, E e4, E e5, E e6, E e7, E e8, E e9) {
/* 165 */     return construct(new Object[] { e1, e2, e3, e4, e5, e6, e7, e8, e9 });
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <E> ImmutableList<E> of(E e1, E e2, E e3, E e4, E e5, E e6, E e7, E e8, E e9, E e10) {
/* 175 */     return construct(new Object[] { e1, e2, e3, e4, e5, e6, e7, e8, e9, e10 });
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <E> ImmutableList<E> of(E e1, E e2, E e3, E e4, E e5, E e6, E e7, E e8, E e9, E e10, E e11) {
/* 185 */     return construct(new Object[] { e1, e2, e3, e4, e5, e6, e7, e8, e9, e10, e11 });
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
/*     */   @SafeVarargs
/*     */   public static <E> ImmutableList<E> of(E e1, E e2, E e3, E e4, E e5, E e6, E e7, E e8, E e9, E e10, E e11, E e12, E... others) {
/* 202 */     Preconditions.checkArgument((others.length <= 2147483635), "the total number of elements must fit in an int");
/*     */     
/* 204 */     Object[] array = new Object[12 + others.length];
/* 205 */     array[0] = e1;
/* 206 */     array[1] = e2;
/* 207 */     array[2] = e3;
/* 208 */     array[3] = e4;
/* 209 */     array[4] = e5;
/* 210 */     array[5] = e6;
/* 211 */     array[6] = e7;
/* 212 */     array[7] = e8;
/* 213 */     array[8] = e9;
/* 214 */     array[9] = e10;
/* 215 */     array[10] = e11;
/* 216 */     array[11] = e12;
/* 217 */     System.arraycopy(others, 0, array, 12, others.length);
/* 218 */     return construct(array);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <E> ImmutableList<E> copyOf(Iterable<? extends E> elements) {
/* 229 */     Preconditions.checkNotNull(elements);
/* 230 */     return (elements instanceof Collection) ? 
/* 231 */       copyOf((Collection<? extends E>)elements) : 
/* 232 */       copyOf(elements.iterator());
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
/*     */   public static <E> ImmutableList<E> copyOf(Collection<? extends E> elements) {
/* 253 */     if (elements instanceof ImmutableCollection) {
/*     */       
/* 255 */       ImmutableList<E> list = ((ImmutableCollection)elements).asList();
/* 256 */       return list.isPartialView() ? asImmutableList(list.toArray()) : list;
/*     */     } 
/* 258 */     return construct(elements.toArray());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <E> ImmutableList<E> copyOf(Iterator<? extends E> elements) {
/* 268 */     if (!elements.hasNext()) {
/* 269 */       return of();
/*     */     }
/* 271 */     E first = elements.next();
/* 272 */     if (!elements.hasNext()) {
/* 273 */       return of(first);
/*     */     }
/* 275 */     return (new Builder<>()).add(first).addAll(elements).build();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <E> ImmutableList<E> copyOf(E[] elements) {
/* 286 */     switch (elements.length) {
/*     */       case 0:
/* 288 */         return of();
/*     */       case 1:
/* 290 */         return of(elements[0]);
/*     */     } 
/* 292 */     return construct((Object[])elements.clone());
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
/*     */   public static <E extends Comparable<? super E>> ImmutableList<E> sortedCopyOf(Iterable<? extends E> elements) {
/* 313 */     Comparable[] arrayOfComparable = Iterables.<Comparable>toArray(elements, new Comparable[0]);
/* 314 */     ObjectArrays.checkElementsNotNull((Object[])arrayOfComparable);
/* 315 */     Arrays.sort((Object[])arrayOfComparable);
/* 316 */     return asImmutableList((Object[])arrayOfComparable);
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
/*     */   public static <E> ImmutableList<E> sortedCopyOf(Comparator<? super E> comparator, Iterable<? extends E> elements) {
/* 336 */     Preconditions.checkNotNull(comparator);
/*     */     
/* 338 */     E[] array = (E[])Iterables.toArray(elements);
/* 339 */     ObjectArrays.checkElementsNotNull((Object[])array);
/* 340 */     Arrays.sort(array, comparator);
/* 341 */     return asImmutableList((Object[])array);
/*     */   }
/*     */ 
/*     */   
/*     */   private static <E> ImmutableList<E> construct(Object... elements) {
/* 346 */     return asImmutableList(ObjectArrays.checkElementsNotNull(elements));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static <E> ImmutableList<E> asImmutableList(Object[] elements) {
/* 355 */     return asImmutableList(elements, elements.length);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static <E> ImmutableList<E> asImmutableList(Object[] elements, int length) {
/* 363 */     switch (length) {
/*     */       case 0:
/* 365 */         return of();
/*     */       case 1:
/* 367 */         return of((E)elements[0]);
/*     */     } 
/* 369 */     if (length < elements.length) {
/* 370 */       elements = Arrays.copyOf(elements, length);
/*     */     }
/* 372 */     return new RegularImmutableList<>(elements);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public UnmodifiableIterator<E> iterator() {
/* 382 */     return listIterator();
/*     */   }
/*     */ 
/*     */   
/*     */   public UnmodifiableListIterator<E> listIterator() {
/* 387 */     return listIterator(0);
/*     */   }
/*     */ 
/*     */   
/*     */   public UnmodifiableListIterator<E> listIterator(int index) {
/* 392 */     return new AbstractIndexedListIterator<E>(size(), index)
/*     */       {
/*     */         protected E get(int index) {
/* 395 */           return ImmutableList.this.get(index);
/*     */         }
/*     */       };
/*     */   }
/*     */ 
/*     */   
/*     */   public void forEach(Consumer<? super E> consumer) {
/* 402 */     Preconditions.checkNotNull(consumer);
/* 403 */     int n = size();
/* 404 */     for (int i = 0; i < n; i++) {
/* 405 */       consumer.accept(get(i));
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public int indexOf(Object object) {
/* 411 */     return (object == null) ? -1 : Lists.indexOfImpl(this, object);
/*     */   }
/*     */ 
/*     */   
/*     */   public int lastIndexOf(Object object) {
/* 416 */     return (object == null) ? -1 : Lists.lastIndexOfImpl(this, object);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean contains(Object object) {
/* 421 */     return (indexOf(object) >= 0);
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
/*     */   public ImmutableList<E> subList(int fromIndex, int toIndex) {
/* 433 */     Preconditions.checkPositionIndexes(fromIndex, toIndex, size());
/* 434 */     int length = toIndex - fromIndex;
/* 435 */     if (length == size())
/* 436 */       return this; 
/* 437 */     if (length == 0)
/* 438 */       return of(); 
/* 439 */     if (length == 1) {
/* 440 */       return of(get(fromIndex));
/*     */     }
/* 442 */     return subListUnchecked(fromIndex, toIndex);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   ImmutableList<E> subListUnchecked(int fromIndex, int toIndex) {
/* 451 */     return new SubList(fromIndex, toIndex - fromIndex);
/*     */   }
/*     */   
/*     */   class SubList extends ImmutableList<E> {
/*     */     final transient int offset;
/*     */     final transient int length;
/*     */     
/*     */     SubList(int offset, int length) {
/* 459 */       this.offset = offset;
/* 460 */       this.length = length;
/*     */     }
/*     */ 
/*     */     
/*     */     public int size() {
/* 465 */       return this.length;
/*     */     }
/*     */ 
/*     */     
/*     */     public E get(int index) {
/* 470 */       Preconditions.checkElementIndex(index, this.length);
/* 471 */       return ImmutableList.this.get(index + this.offset);
/*     */     }
/*     */ 
/*     */     
/*     */     public ImmutableList<E> subList(int fromIndex, int toIndex) {
/* 476 */       Preconditions.checkPositionIndexes(fromIndex, toIndex, this.length);
/* 477 */       return ImmutableList.this.subList(fromIndex + this.offset, toIndex + this.offset);
/*     */     }
/*     */ 
/*     */     
/*     */     boolean isPartialView() {
/* 482 */       return true;
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
/*     */   @Deprecated
/*     */   @CanIgnoreReturnValue
/*     */   public final boolean addAll(int index, Collection<? extends E> newElements) {
/* 496 */     throw new UnsupportedOperationException();
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
/*     */   public final E set(int index, E element) {
/* 509 */     throw new UnsupportedOperationException();
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
/*     */   public final void add(int index, E element) {
/* 521 */     throw new UnsupportedOperationException();
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
/*     */   public final E remove(int index) {
/* 534 */     throw new UnsupportedOperationException();
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
/*     */   public final void replaceAll(UnaryOperator<E> operator) {
/* 546 */     throw new UnsupportedOperationException();
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
/*     */   public final void sort(Comparator<? super E> c) {
/* 558 */     throw new UnsupportedOperationException();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final ImmutableList<E> asList() {
/* 568 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public Spliterator<E> spliterator() {
/* 573 */     return CollectSpliterators.indexed(size(), 1296, this::get);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   int copyIntoArray(Object[] dst, int offset) {
/* 579 */     int size = size();
/* 580 */     for (int i = 0; i < size; i++) {
/* 581 */       dst[offset + i] = get(i);
/*     */     }
/* 583 */     return offset + size;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ImmutableList<E> reverse() {
/* 594 */     return (size() <= 1) ? this : new ReverseImmutableList<>(this);
/*     */   }
/*     */   
/*     */   private static class ReverseImmutableList<E> extends ImmutableList<E> {
/*     */     private final transient ImmutableList<E> forwardList;
/*     */     
/*     */     ReverseImmutableList(ImmutableList<E> backingList) {
/* 601 */       this.forwardList = backingList;
/*     */     }
/*     */     
/*     */     private int reverseIndex(int index) {
/* 605 */       return size() - 1 - index;
/*     */     }
/*     */     
/*     */     private int reversePosition(int index) {
/* 609 */       return size() - index;
/*     */     }
/*     */ 
/*     */     
/*     */     public ImmutableList<E> reverse() {
/* 614 */       return this.forwardList;
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean contains(Object object) {
/* 619 */       return this.forwardList.contains(object);
/*     */     }
/*     */ 
/*     */     
/*     */     public int indexOf(Object object) {
/* 624 */       int index = this.forwardList.lastIndexOf(object);
/* 625 */       return (index >= 0) ? reverseIndex(index) : -1;
/*     */     }
/*     */ 
/*     */     
/*     */     public int lastIndexOf(Object object) {
/* 630 */       int index = this.forwardList.indexOf(object);
/* 631 */       return (index >= 0) ? reverseIndex(index) : -1;
/*     */     }
/*     */ 
/*     */     
/*     */     public ImmutableList<E> subList(int fromIndex, int toIndex) {
/* 636 */       Preconditions.checkPositionIndexes(fromIndex, toIndex, size());
/* 637 */       return this.forwardList.subList(reversePosition(toIndex), reversePosition(fromIndex)).reverse();
/*     */     }
/*     */ 
/*     */     
/*     */     public E get(int index) {
/* 642 */       Preconditions.checkElementIndex(index, size());
/* 643 */       return this.forwardList.get(reverseIndex(index));
/*     */     }
/*     */ 
/*     */     
/*     */     public int size() {
/* 648 */       return this.forwardList.size();
/*     */     }
/*     */ 
/*     */     
/*     */     boolean isPartialView() {
/* 653 */       return this.forwardList.isPartialView();
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean equals(Object obj) {
/* 659 */     return Lists.equalsImpl(this, obj);
/*     */   }
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 664 */     int hashCode = 1;
/* 665 */     int n = size();
/* 666 */     for (int i = 0; i < n; i++) {
/* 667 */       hashCode = 31 * hashCode + get(i).hashCode();
/*     */       
/* 669 */       hashCode = hashCode ^ 0xFFFFFFFF ^ 0xFFFFFFFF;
/*     */     } 
/*     */     
/* 672 */     return hashCode;
/*     */   }
/*     */ 
/*     */   
/*     */   static class SerializedForm
/*     */     implements Serializable
/*     */   {
/*     */     final Object[] elements;
/*     */     private static final long serialVersionUID = 0L;
/*     */     
/*     */     SerializedForm(Object[] elements) {
/* 683 */       this.elements = elements;
/*     */     }
/*     */     
/*     */     Object readResolve() {
/* 687 */       return ImmutableList.copyOf(this.elements);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private void readObject(ObjectInputStream stream) throws InvalidObjectException {
/* 694 */     throw new InvalidObjectException("Use SerializedForm");
/*     */   }
/*     */ 
/*     */   
/*     */   Object writeReplace() {
/* 699 */     return new SerializedForm(toArray());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <E> Builder<E> builder() {
/* 707 */     return new Builder<>();
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
/*     */   public static <E> Builder<E> builderWithExpectedSize(int expectedSize) {
/* 724 */     CollectPreconditions.checkNonnegative(expectedSize, "expectedSize");
/* 725 */     return new Builder<>(expectedSize);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static final class Builder<E>
/*     */     extends ImmutableCollection.Builder<E>
/*     */   {
/*     */     @VisibleForTesting
/*     */     Object[] contents;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     private int size;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     private boolean forceCopy;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public Builder() {
/* 758 */       this(4);
/*     */     }
/*     */     
/*     */     Builder(int capacity) {
/* 762 */       this.contents = new Object[capacity];
/* 763 */       this.size = 0;
/*     */     }
/*     */     
/*     */     private void getReadyToExpandTo(int minCapacity) {
/* 767 */       if (this.contents.length < minCapacity) {
/* 768 */         this.contents = Arrays.copyOf(this.contents, expandedCapacity(this.contents.length, minCapacity));
/* 769 */         this.forceCopy = false;
/* 770 */       } else if (this.forceCopy) {
/* 771 */         this.contents = Arrays.copyOf(this.contents, this.contents.length);
/* 772 */         this.forceCopy = false;
/*     */       } 
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
/* 786 */       Preconditions.checkNotNull(element);
/* 787 */       getReadyToExpandTo(this.size + 1);
/* 788 */       this.contents[this.size++] = element;
/* 789 */       return this;
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
/* 802 */       ObjectArrays.checkElementsNotNull((Object[])elements);
/* 803 */       add((Object[])elements, elements.length);
/* 804 */       return this;
/*     */     }
/*     */     
/*     */     private void add(Object[] elements, int n) {
/* 808 */       getReadyToExpandTo(this.size + n);
/* 809 */       System.arraycopy(elements, 0, this.contents, this.size, n);
/* 810 */       this.size += n;
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
/* 823 */       Preconditions.checkNotNull(elements);
/* 824 */       if (elements instanceof Collection) {
/* 825 */         Collection<?> collection = (Collection)elements;
/* 826 */         getReadyToExpandTo(this.size + collection.size());
/* 827 */         if (collection instanceof ImmutableCollection) {
/* 828 */           ImmutableCollection<?> immutableCollection = (ImmutableCollection)collection;
/* 829 */           this.size = immutableCollection.copyIntoArray(this.contents, this.size);
/* 830 */           return this;
/*     */         } 
/*     */       } 
/* 833 */       super.addAll(elements);
/* 834 */       return this;
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
/* 847 */       super.addAll(elements);
/* 848 */       return this;
/*     */     }
/*     */     
/*     */     @CanIgnoreReturnValue
/*     */     Builder<E> combine(Builder<E> builder) {
/* 853 */       Preconditions.checkNotNull(builder);
/* 854 */       add(builder.contents, builder.size);
/* 855 */       return this;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public ImmutableList<E> build() {
/* 863 */       this.forceCopy = true;
/* 864 */       return ImmutableList.asImmutableList(this.contents, this.size);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\google\common\collect\ImmutableList.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */