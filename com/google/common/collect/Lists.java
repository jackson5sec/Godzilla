/*      */ package com.google.common.collect;
/*      */ 
/*      */ import com.google.common.annotations.Beta;
/*      */ import com.google.common.annotations.GwtCompatible;
/*      */ import com.google.common.annotations.GwtIncompatible;
/*      */ import com.google.common.annotations.VisibleForTesting;
/*      */ import com.google.common.base.Function;
/*      */ import com.google.common.base.Objects;
/*      */ import com.google.common.base.Preconditions;
/*      */ import com.google.common.math.IntMath;
/*      */ import com.google.common.primitives.Ints;
/*      */ import java.io.Serializable;
/*      */ import java.math.RoundingMode;
/*      */ import java.util.AbstractList;
/*      */ import java.util.AbstractSequentialList;
/*      */ import java.util.ArrayList;
/*      */ import java.util.Arrays;
/*      */ import java.util.Collection;
/*      */ import java.util.Collections;
/*      */ import java.util.Iterator;
/*      */ import java.util.LinkedList;
/*      */ import java.util.List;
/*      */ import java.util.ListIterator;
/*      */ import java.util.NoSuchElementException;
/*      */ import java.util.RandomAccess;
/*      */ import java.util.concurrent.CopyOnWriteArrayList;
/*      */ import java.util.function.Predicate;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ @GwtCompatible(emulated = true)
/*      */ public final class Lists
/*      */ {
/*      */   @GwtCompatible(serializable = true)
/*      */   public static <E> ArrayList<E> newArrayList() {
/*   83 */     return new ArrayList<>();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @SafeVarargs
/*      */   @GwtCompatible(serializable = true)
/*      */   public static <E> ArrayList<E> newArrayList(E... elements) {
/*  103 */     Preconditions.checkNotNull(elements);
/*      */     
/*  105 */     int capacity = computeArrayListCapacity(elements.length);
/*  106 */     ArrayList<E> list = new ArrayList<>(capacity);
/*  107 */     Collections.addAll(list, elements);
/*  108 */     return list;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @GwtCompatible(serializable = true)
/*      */   public static <E> ArrayList<E> newArrayList(Iterable<? extends E> elements) {
/*  126 */     Preconditions.checkNotNull(elements);
/*      */     
/*  128 */     return (elements instanceof Collection) ? new ArrayList<>(
/*  129 */         Collections2.cast(elements)) : 
/*  130 */       newArrayList(elements.iterator());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @GwtCompatible(serializable = true)
/*      */   public static <E> ArrayList<E> newArrayList(Iterator<? extends E> elements) {
/*  142 */     ArrayList<E> list = newArrayList();
/*  143 */     Iterators.addAll(list, elements);
/*  144 */     return list;
/*      */   }
/*      */   
/*      */   @VisibleForTesting
/*      */   static int computeArrayListCapacity(int arraySize) {
/*  149 */     CollectPreconditions.checkNonnegative(arraySize, "arraySize");
/*      */ 
/*      */     
/*  152 */     return Ints.saturatedCast(5L + arraySize + (arraySize / 10));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @GwtCompatible(serializable = true)
/*      */   public static <E> ArrayList<E> newArrayListWithCapacity(int initialArraySize) {
/*  173 */     CollectPreconditions.checkNonnegative(initialArraySize, "initialArraySize");
/*  174 */     return new ArrayList<>(initialArraySize);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @GwtCompatible(serializable = true)
/*      */   public static <E> ArrayList<E> newArrayListWithExpectedSize(int estimatedSize) {
/*  192 */     return new ArrayList<>(computeArrayListCapacity(estimatedSize));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @GwtCompatible(serializable = true)
/*      */   public static <E> LinkedList<E> newLinkedList() {
/*  214 */     return new LinkedList<>();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @GwtCompatible(serializable = true)
/*      */   public static <E> LinkedList<E> newLinkedList(Iterable<? extends E> elements) {
/*  236 */     LinkedList<E> list = newLinkedList();
/*  237 */     Iterables.addAll(list, elements);
/*  238 */     return list;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @GwtIncompatible
/*      */   public static <E> CopyOnWriteArrayList<E> newCopyOnWriteArrayList() {
/*  252 */     return new CopyOnWriteArrayList<>();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @GwtIncompatible
/*      */   public static <E> CopyOnWriteArrayList<E> newCopyOnWriteArrayList(Iterable<? extends E> elements) {
/*  268 */     Collection<? extends E> elementsCollection = (elements instanceof Collection) ? Collections2.<E>cast(elements) : newArrayList(elements);
/*  269 */     return new CopyOnWriteArrayList<>(elementsCollection);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static <E> List<E> asList(E first, E[] rest) {
/*  287 */     return new OnePlusArrayList<>(first, rest);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static <E> List<E> asList(E first, E second, E[] rest) {
/*  307 */     return new TwoPlusArrayList<>(first, second, rest);
/*      */   }
/*      */   
/*      */   private static class OnePlusArrayList<E>
/*      */     extends AbstractList<E> implements Serializable, RandomAccess {
/*      */     final E first;
/*      */     final E[] rest;
/*      */     private static final long serialVersionUID = 0L;
/*      */     
/*      */     OnePlusArrayList(E first, E[] rest) {
/*  317 */       this.first = first;
/*  318 */       this.rest = (E[])Preconditions.checkNotNull(rest);
/*      */     }
/*      */ 
/*      */     
/*      */     public int size() {
/*  323 */       return IntMath.saturatedAdd(this.rest.length, 1);
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     public E get(int index) {
/*  329 */       Preconditions.checkElementIndex(index, size());
/*  330 */       return (index == 0) ? this.first : this.rest[index - 1];
/*      */     }
/*      */   }
/*      */   
/*      */   private static class TwoPlusArrayList<E>
/*      */     extends AbstractList<E>
/*      */     implements Serializable, RandomAccess
/*      */   {
/*      */     final E first;
/*      */     final E second;
/*      */     final E[] rest;
/*      */     private static final long serialVersionUID = 0L;
/*      */     
/*      */     TwoPlusArrayList(E first, E second, E[] rest) {
/*  344 */       this.first = first;
/*  345 */       this.second = second;
/*  346 */       this.rest = (E[])Preconditions.checkNotNull(rest);
/*      */     }
/*      */ 
/*      */     
/*      */     public int size() {
/*  351 */       return IntMath.saturatedAdd(this.rest.length, 2);
/*      */     }
/*      */ 
/*      */     
/*      */     public E get(int index) {
/*  356 */       switch (index) {
/*      */         case 0:
/*  358 */           return this.first;
/*      */         case 1:
/*  360 */           return this.second;
/*      */       } 
/*      */       
/*  363 */       Preconditions.checkElementIndex(index, size());
/*  364 */       return this.rest[index - 2];
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static <B> List<List<B>> cartesianProduct(List<? extends List<? extends B>> lists) {
/*  426 */     return CartesianList.create(lists);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @SafeVarargs
/*      */   public static <B> List<List<B>> cartesianProduct(List<? extends B>... lists) {
/*  485 */     return cartesianProduct(Arrays.asList(lists));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static <F, T> List<T> transform(List<F> fromList, Function<? super F, ? extends T> function) {
/*  522 */     return (fromList instanceof RandomAccess) ? new TransformingRandomAccessList<>(fromList, function) : new TransformingSequentialList<>(fromList, function);
/*      */   }
/*      */ 
/*      */   
/*      */   private static class TransformingSequentialList<F, T>
/*      */     extends AbstractSequentialList<T>
/*      */     implements Serializable
/*      */   {
/*      */     final List<F> fromList;
/*      */     
/*      */     final Function<? super F, ? extends T> function;
/*      */     
/*      */     private static final long serialVersionUID = 0L;
/*      */ 
/*      */     
/*      */     TransformingSequentialList(List<F> fromList, Function<? super F, ? extends T> function) {
/*  538 */       this.fromList = (List<F>)Preconditions.checkNotNull(fromList);
/*  539 */       this.function = (Function<? super F, ? extends T>)Preconditions.checkNotNull(function);
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public void clear() {
/*  548 */       this.fromList.clear();
/*      */     }
/*      */ 
/*      */     
/*      */     public int size() {
/*  553 */       return this.fromList.size();
/*      */     }
/*      */ 
/*      */     
/*      */     public ListIterator<T> listIterator(int index) {
/*  558 */       return new TransformedListIterator<F, T>(this.fromList.listIterator(index))
/*      */         {
/*      */           T transform(F from) {
/*  561 */             return (T)Lists.TransformingSequentialList.this.function.apply(from);
/*      */           }
/*      */         };
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean removeIf(Predicate<? super T> filter) {
/*  568 */       Preconditions.checkNotNull(filter);
/*  569 */       return this.fromList.removeIf(element -> filter.test(this.function.apply(element)));
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private static class TransformingRandomAccessList<F, T>
/*      */     extends AbstractList<T>
/*      */     implements RandomAccess, Serializable
/*      */   {
/*      */     final List<F> fromList;
/*      */ 
/*      */     
/*      */     final Function<? super F, ? extends T> function;
/*      */     
/*      */     private static final long serialVersionUID = 0L;
/*      */ 
/*      */     
/*      */     TransformingRandomAccessList(List<F> fromList, Function<? super F, ? extends T> function) {
/*  588 */       this.fromList = (List<F>)Preconditions.checkNotNull(fromList);
/*  589 */       this.function = (Function<? super F, ? extends T>)Preconditions.checkNotNull(function);
/*      */     }
/*      */ 
/*      */     
/*      */     public void clear() {
/*  594 */       this.fromList.clear();
/*      */     }
/*      */ 
/*      */     
/*      */     public T get(int index) {
/*  599 */       return (T)this.function.apply(this.fromList.get(index));
/*      */     }
/*      */ 
/*      */     
/*      */     public Iterator<T> iterator() {
/*  604 */       return listIterator();
/*      */     }
/*      */ 
/*      */     
/*      */     public ListIterator<T> listIterator(int index) {
/*  609 */       return new TransformedListIterator<F, T>(this.fromList.listIterator(index))
/*      */         {
/*      */           T transform(F from) {
/*  612 */             return (T)Lists.TransformingRandomAccessList.this.function.apply(from);
/*      */           }
/*      */         };
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean isEmpty() {
/*  619 */       return this.fromList.isEmpty();
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean removeIf(Predicate<? super T> filter) {
/*  624 */       Preconditions.checkNotNull(filter);
/*  625 */       return this.fromList.removeIf(element -> filter.test(this.function.apply(element)));
/*      */     }
/*      */ 
/*      */     
/*      */     public T remove(int index) {
/*  630 */       return (T)this.function.apply(this.fromList.remove(index));
/*      */     }
/*      */ 
/*      */     
/*      */     public int size() {
/*  635 */       return this.fromList.size();
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static <T> List<List<T>> partition(List<T> list, int size) {
/*  657 */     Preconditions.checkNotNull(list);
/*  658 */     Preconditions.checkArgument((size > 0));
/*  659 */     return (list instanceof RandomAccess) ? new RandomAccessPartition<>(list, size) : new Partition<>(list, size);
/*      */   }
/*      */   
/*      */   private static class Partition<T>
/*      */     extends AbstractList<List<T>>
/*      */   {
/*      */     final List<T> list;
/*      */     final int size;
/*      */     
/*      */     Partition(List<T> list, int size) {
/*  669 */       this.list = list;
/*  670 */       this.size = size;
/*      */     }
/*      */ 
/*      */     
/*      */     public List<T> get(int index) {
/*  675 */       Preconditions.checkElementIndex(index, size());
/*  676 */       int start = index * this.size;
/*  677 */       int end = Math.min(start + this.size, this.list.size());
/*  678 */       return this.list.subList(start, end);
/*      */     }
/*      */ 
/*      */     
/*      */     public int size() {
/*  683 */       return IntMath.divide(this.list.size(), this.size, RoundingMode.CEILING);
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean isEmpty() {
/*  688 */       return this.list.isEmpty();
/*      */     }
/*      */   }
/*      */   
/*      */   private static class RandomAccessPartition<T> extends Partition<T> implements RandomAccess {
/*      */     RandomAccessPartition(List<T> list, int size) {
/*  694 */       super(list, size);
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static ImmutableList<Character> charactersOf(String string) {
/*  704 */     return new StringAsImmutableList((String)Preconditions.checkNotNull(string));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Beta
/*      */   public static List<Character> charactersOf(CharSequence sequence) {
/*  718 */     return new CharSequenceAsList((CharSequence)Preconditions.checkNotNull(sequence));
/*      */   }
/*      */   
/*      */   private static final class StringAsImmutableList
/*      */     extends ImmutableList<Character>
/*      */   {
/*      */     private final String string;
/*      */     
/*      */     StringAsImmutableList(String string) {
/*  727 */       this.string = string;
/*      */     }
/*      */ 
/*      */     
/*      */     public int indexOf(Object object) {
/*  732 */       return (object instanceof Character) ? this.string.indexOf(((Character)object).charValue()) : -1;
/*      */     }
/*      */ 
/*      */     
/*      */     public int lastIndexOf(Object object) {
/*  737 */       return (object instanceof Character) ? this.string.lastIndexOf(((Character)object).charValue()) : -1;
/*      */     }
/*      */ 
/*      */     
/*      */     public ImmutableList<Character> subList(int fromIndex, int toIndex) {
/*  742 */       Preconditions.checkPositionIndexes(fromIndex, toIndex, size());
/*  743 */       return Lists.charactersOf(this.string.substring(fromIndex, toIndex));
/*      */     }
/*      */ 
/*      */     
/*      */     boolean isPartialView() {
/*  748 */       return false;
/*      */     }
/*      */ 
/*      */     
/*      */     public Character get(int index) {
/*  753 */       Preconditions.checkElementIndex(index, size());
/*  754 */       return Character.valueOf(this.string.charAt(index));
/*      */     }
/*      */ 
/*      */     
/*      */     public int size() {
/*  759 */       return this.string.length();
/*      */     }
/*      */   }
/*      */   
/*      */   private static final class CharSequenceAsList extends AbstractList<Character> {
/*      */     private final CharSequence sequence;
/*      */     
/*      */     CharSequenceAsList(CharSequence sequence) {
/*  767 */       this.sequence = sequence;
/*      */     }
/*      */ 
/*      */     
/*      */     public Character get(int index) {
/*  772 */       Preconditions.checkElementIndex(index, size());
/*  773 */       return Character.valueOf(this.sequence.charAt(index));
/*      */     }
/*      */ 
/*      */     
/*      */     public int size() {
/*  778 */       return this.sequence.length();
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static <T> List<T> reverse(List<T> list) {
/*  794 */     if (list instanceof ImmutableList)
/*  795 */       return ((ImmutableList<T>)list).reverse(); 
/*  796 */     if (list instanceof ReverseList)
/*  797 */       return ((ReverseList<T>)list).getForwardList(); 
/*  798 */     if (list instanceof RandomAccess) {
/*  799 */       return new RandomAccessReverseList<>(list);
/*      */     }
/*  801 */     return new ReverseList<>(list);
/*      */   }
/*      */   
/*      */   private static class ReverseList<T>
/*      */     extends AbstractList<T> {
/*      */     private final List<T> forwardList;
/*      */     
/*      */     ReverseList(List<T> forwardList) {
/*  809 */       this.forwardList = (List<T>)Preconditions.checkNotNull(forwardList);
/*      */     }
/*      */     
/*      */     List<T> getForwardList() {
/*  813 */       return this.forwardList;
/*      */     }
/*      */     
/*      */     private int reverseIndex(int index) {
/*  817 */       int size = size();
/*  818 */       Preconditions.checkElementIndex(index, size);
/*  819 */       return size - 1 - index;
/*      */     }
/*      */     
/*      */     private int reversePosition(int index) {
/*  823 */       int size = size();
/*  824 */       Preconditions.checkPositionIndex(index, size);
/*  825 */       return size - index;
/*      */     }
/*      */ 
/*      */     
/*      */     public void add(int index, T element) {
/*  830 */       this.forwardList.add(reversePosition(index), element);
/*      */     }
/*      */ 
/*      */     
/*      */     public void clear() {
/*  835 */       this.forwardList.clear();
/*      */     }
/*      */ 
/*      */     
/*      */     public T remove(int index) {
/*  840 */       return this.forwardList.remove(reverseIndex(index));
/*      */     }
/*      */ 
/*      */     
/*      */     protected void removeRange(int fromIndex, int toIndex) {
/*  845 */       subList(fromIndex, toIndex).clear();
/*      */     }
/*      */ 
/*      */     
/*      */     public T set(int index, T element) {
/*  850 */       return this.forwardList.set(reverseIndex(index), element);
/*      */     }
/*      */ 
/*      */     
/*      */     public T get(int index) {
/*  855 */       return this.forwardList.get(reverseIndex(index));
/*      */     }
/*      */ 
/*      */     
/*      */     public int size() {
/*  860 */       return this.forwardList.size();
/*      */     }
/*      */ 
/*      */     
/*      */     public List<T> subList(int fromIndex, int toIndex) {
/*  865 */       Preconditions.checkPositionIndexes(fromIndex, toIndex, size());
/*  866 */       return Lists.reverse(this.forwardList.subList(reversePosition(toIndex), reversePosition(fromIndex)));
/*      */     }
/*      */ 
/*      */     
/*      */     public Iterator<T> iterator() {
/*  871 */       return listIterator();
/*      */     }
/*      */ 
/*      */     
/*      */     public ListIterator<T> listIterator(int index) {
/*  876 */       int start = reversePosition(index);
/*  877 */       final ListIterator<T> forwardIterator = this.forwardList.listIterator(start);
/*  878 */       return new ListIterator<T>()
/*      */         {
/*      */           boolean canRemoveOrSet;
/*      */ 
/*      */           
/*      */           public void add(T e) {
/*  884 */             forwardIterator.add(e);
/*  885 */             forwardIterator.previous();
/*  886 */             this.canRemoveOrSet = false;
/*      */           }
/*      */ 
/*      */           
/*      */           public boolean hasNext() {
/*  891 */             return forwardIterator.hasPrevious();
/*      */           }
/*      */ 
/*      */           
/*      */           public boolean hasPrevious() {
/*  896 */             return forwardIterator.hasNext();
/*      */           }
/*      */ 
/*      */           
/*      */           public T next() {
/*  901 */             if (!hasNext()) {
/*  902 */               throw new NoSuchElementException();
/*      */             }
/*  904 */             this.canRemoveOrSet = true;
/*  905 */             return forwardIterator.previous();
/*      */           }
/*      */ 
/*      */           
/*      */           public int nextIndex() {
/*  910 */             return Lists.ReverseList.this.reversePosition(forwardIterator.nextIndex());
/*      */           }
/*      */ 
/*      */           
/*      */           public T previous() {
/*  915 */             if (!hasPrevious()) {
/*  916 */               throw new NoSuchElementException();
/*      */             }
/*  918 */             this.canRemoveOrSet = true;
/*  919 */             return forwardIterator.next();
/*      */           }
/*      */ 
/*      */           
/*      */           public int previousIndex() {
/*  924 */             return nextIndex() - 1;
/*      */           }
/*      */ 
/*      */           
/*      */           public void remove() {
/*  929 */             CollectPreconditions.checkRemove(this.canRemoveOrSet);
/*  930 */             forwardIterator.remove();
/*  931 */             this.canRemoveOrSet = false;
/*      */           }
/*      */ 
/*      */           
/*      */           public void set(T e) {
/*  936 */             Preconditions.checkState(this.canRemoveOrSet);
/*  937 */             forwardIterator.set(e);
/*      */           }
/*      */         };
/*      */     }
/*      */   }
/*      */   
/*      */   private static class RandomAccessReverseList<T> extends ReverseList<T> implements RandomAccess {
/*      */     RandomAccessReverseList(List<T> forwardList) {
/*  945 */       super(forwardList);
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   static int hashCodeImpl(List<?> list) {
/*  952 */     int hashCode = 1;
/*  953 */     for (Object o : list) {
/*  954 */       hashCode = 31 * hashCode + ((o == null) ? 0 : o.hashCode());
/*      */       
/*  956 */       hashCode = hashCode ^ 0xFFFFFFFF ^ 0xFFFFFFFF;
/*      */     } 
/*      */     
/*  959 */     return hashCode;
/*      */   }
/*      */ 
/*      */   
/*      */   static boolean equalsImpl(List<?> thisList, Object other) {
/*  964 */     if (other == Preconditions.checkNotNull(thisList)) {
/*  965 */       return true;
/*      */     }
/*  967 */     if (!(other instanceof List)) {
/*  968 */       return false;
/*      */     }
/*  970 */     List<?> otherList = (List)other;
/*  971 */     int size = thisList.size();
/*  972 */     if (size != otherList.size()) {
/*  973 */       return false;
/*      */     }
/*  975 */     if (thisList instanceof RandomAccess && otherList instanceof RandomAccess) {
/*      */       
/*  977 */       for (int i = 0; i < size; i++) {
/*  978 */         if (!Objects.equal(thisList.get(i), otherList.get(i))) {
/*  979 */           return false;
/*      */         }
/*      */       } 
/*  982 */       return true;
/*      */     } 
/*  984 */     return Iterators.elementsEqual(thisList.iterator(), otherList.iterator());
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   static <E> boolean addAllImpl(List<E> list, int index, Iterable<? extends E> elements) {
/*  990 */     boolean changed = false;
/*  991 */     ListIterator<E> listIterator = list.listIterator(index);
/*  992 */     for (E e : elements) {
/*  993 */       listIterator.add(e);
/*  994 */       changed = true;
/*      */     } 
/*  996 */     return changed;
/*      */   }
/*      */ 
/*      */   
/*      */   static int indexOfImpl(List<?> list, Object element) {
/* 1001 */     if (list instanceof RandomAccess) {
/* 1002 */       return indexOfRandomAccess(list, element);
/*      */     }
/* 1004 */     ListIterator<?> listIterator = list.listIterator();
/* 1005 */     while (listIterator.hasNext()) {
/* 1006 */       if (Objects.equal(element, listIterator.next())) {
/* 1007 */         return listIterator.previousIndex();
/*      */       }
/*      */     } 
/* 1010 */     return -1;
/*      */   }
/*      */ 
/*      */   
/*      */   private static int indexOfRandomAccess(List<?> list, Object element) {
/* 1015 */     int size = list.size();
/* 1016 */     if (element == null) {
/* 1017 */       for (int i = 0; i < size; i++) {
/* 1018 */         if (list.get(i) == null) {
/* 1019 */           return i;
/*      */         }
/*      */       } 
/*      */     } else {
/* 1023 */       for (int i = 0; i < size; i++) {
/* 1024 */         if (element.equals(list.get(i))) {
/* 1025 */           return i;
/*      */         }
/*      */       } 
/*      */     } 
/* 1029 */     return -1;
/*      */   }
/*      */ 
/*      */   
/*      */   static int lastIndexOfImpl(List<?> list, Object element) {
/* 1034 */     if (list instanceof RandomAccess) {
/* 1035 */       return lastIndexOfRandomAccess(list, element);
/*      */     }
/* 1037 */     ListIterator<?> listIterator = list.listIterator(list.size());
/* 1038 */     while (listIterator.hasPrevious()) {
/* 1039 */       if (Objects.equal(element, listIterator.previous())) {
/* 1040 */         return listIterator.nextIndex();
/*      */       }
/*      */     } 
/* 1043 */     return -1;
/*      */   }
/*      */ 
/*      */   
/*      */   private static int lastIndexOfRandomAccess(List<?> list, Object element) {
/* 1048 */     if (element == null) {
/* 1049 */       for (int i = list.size() - 1; i >= 0; i--) {
/* 1050 */         if (list.get(i) == null) {
/* 1051 */           return i;
/*      */         }
/*      */       } 
/*      */     } else {
/* 1055 */       for (int i = list.size() - 1; i >= 0; i--) {
/* 1056 */         if (element.equals(list.get(i))) {
/* 1057 */           return i;
/*      */         }
/*      */       } 
/*      */     } 
/* 1061 */     return -1;
/*      */   }
/*      */ 
/*      */   
/*      */   static <E> ListIterator<E> listIteratorImpl(List<E> list, int index) {
/* 1066 */     return (new AbstractListWrapper<>(list)).listIterator(index);
/*      */   }
/*      */ 
/*      */   
/*      */   static <E> List<E> subListImpl(List<E> list, int fromIndex, int toIndex) {
/*      */     List<E> wrapper;
/* 1072 */     if (list instanceof RandomAccess) {
/* 1073 */       wrapper = new RandomAccessListWrapper<E>(list) {
/*      */           private static final long serialVersionUID = 0L;
/*      */           
/*      */           public ListIterator<E> listIterator(int index) {
/* 1077 */             return this.backingList.listIterator(index);
/*      */           }
/*      */         };
/*      */     }
/*      */     else {
/*      */       
/* 1083 */       wrapper = new AbstractListWrapper<E>(list) {
/*      */           private static final long serialVersionUID = 0L;
/*      */           
/*      */           public ListIterator<E> listIterator(int index) {
/* 1087 */             return this.backingList.listIterator(index);
/*      */           }
/*      */         };
/*      */     } 
/*      */ 
/*      */     
/* 1093 */     return wrapper.subList(fromIndex, toIndex);
/*      */   }
/*      */   
/*      */   private static class AbstractListWrapper<E> extends AbstractList<E> {
/*      */     final List<E> backingList;
/*      */     
/*      */     AbstractListWrapper(List<E> backingList) {
/* 1100 */       this.backingList = (List<E>)Preconditions.checkNotNull(backingList);
/*      */     }
/*      */ 
/*      */     
/*      */     public void add(int index, E element) {
/* 1105 */       this.backingList.add(index, element);
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean addAll(int index, Collection<? extends E> c) {
/* 1110 */       return this.backingList.addAll(index, c);
/*      */     }
/*      */ 
/*      */     
/*      */     public E get(int index) {
/* 1115 */       return this.backingList.get(index);
/*      */     }
/*      */ 
/*      */     
/*      */     public E remove(int index) {
/* 1120 */       return this.backingList.remove(index);
/*      */     }
/*      */ 
/*      */     
/*      */     public E set(int index, E element) {
/* 1125 */       return this.backingList.set(index, element);
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean contains(Object o) {
/* 1130 */       return this.backingList.contains(o);
/*      */     }
/*      */ 
/*      */     
/*      */     public int size() {
/* 1135 */       return this.backingList.size();
/*      */     }
/*      */   }
/*      */   
/*      */   private static class RandomAccessListWrapper<E>
/*      */     extends AbstractListWrapper<E> implements RandomAccess {
/*      */     RandomAccessListWrapper(List<E> backingList) {
/* 1142 */       super(backingList);
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   static <T> List<T> cast(Iterable<T> iterable) {
/* 1148 */     return (List<T>)iterable;
/*      */   }
/*      */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\google\common\collect\Lists.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */