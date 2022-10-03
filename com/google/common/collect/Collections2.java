/*     */ package com.google.common.collect;
/*     */ 
/*     */ import com.google.common.annotations.Beta;
/*     */ import com.google.common.annotations.GwtCompatible;
/*     */ import com.google.common.base.Function;
/*     */ import com.google.common.base.Preconditions;
/*     */ import com.google.common.base.Predicate;
/*     */ import com.google.common.base.Predicates;
/*     */ import com.google.common.math.IntMath;
/*     */ import java.util.AbstractCollection;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.Collection;
/*     */ import java.util.Collections;
/*     */ import java.util.Comparator;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Spliterator;
/*     */ import java.util.function.Consumer;
/*     */ import java.util.function.Function;
/*     */ import java.util.function.Predicate;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
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
/*     */ public final class Collections2
/*     */ {
/*     */   public static <E> Collection<E> filter(Collection<E> unfiltered, Predicate<? super E> predicate) {
/*  87 */     if (unfiltered instanceof FilteredCollection)
/*     */     {
/*     */       
/*  90 */       return ((FilteredCollection<E>)unfiltered).createCombined(predicate);
/*     */     }
/*     */     
/*  93 */     return new FilteredCollection<>((Collection<E>)Preconditions.checkNotNull(unfiltered), (Predicate<? super E>)Preconditions.checkNotNull(predicate));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static boolean safeContains(Collection<?> collection, Object object) {
/* 101 */     Preconditions.checkNotNull(collection);
/*     */     try {
/* 103 */       return collection.contains(object);
/* 104 */     } catch (ClassCastException|NullPointerException e) {
/* 105 */       return false;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static boolean safeRemove(Collection<?> collection, Object object) {
/* 114 */     Preconditions.checkNotNull(collection);
/*     */     try {
/* 116 */       return collection.remove(object);
/* 117 */     } catch (ClassCastException|NullPointerException e) {
/* 118 */       return false;
/*     */     } 
/*     */   }
/*     */   
/*     */   static class FilteredCollection<E> extends AbstractCollection<E> {
/*     */     final Collection<E> unfiltered;
/*     */     final Predicate<? super E> predicate;
/*     */     
/*     */     FilteredCollection(Collection<E> unfiltered, Predicate<? super E> predicate) {
/* 127 */       this.unfiltered = unfiltered;
/* 128 */       this.predicate = predicate;
/*     */     }
/*     */     
/*     */     FilteredCollection<E> createCombined(Predicate<? super E> newPredicate) {
/* 132 */       return new FilteredCollection(this.unfiltered, Predicates.and(this.predicate, newPredicate));
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public boolean add(E element) {
/* 138 */       Preconditions.checkArgument(this.predicate.apply(element));
/* 139 */       return this.unfiltered.add(element);
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean addAll(Collection<? extends E> collection) {
/* 144 */       for (E element : collection) {
/* 145 */         Preconditions.checkArgument(this.predicate.apply(element));
/*     */       }
/* 147 */       return this.unfiltered.addAll(collection);
/*     */     }
/*     */ 
/*     */     
/*     */     public void clear() {
/* 152 */       Iterables.removeIf(this.unfiltered, this.predicate);
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean contains(Object element) {
/* 157 */       if (Collections2.safeContains(this.unfiltered, element)) {
/*     */         
/* 159 */         E e = (E)element;
/* 160 */         return this.predicate.apply(e);
/*     */       } 
/* 162 */       return false;
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean containsAll(Collection<?> collection) {
/* 167 */       return Collections2.containsAllImpl(this, collection);
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean isEmpty() {
/* 172 */       return !Iterables.any(this.unfiltered, this.predicate);
/*     */     }
/*     */ 
/*     */     
/*     */     public Iterator<E> iterator() {
/* 177 */       return Iterators.filter(this.unfiltered.iterator(), this.predicate);
/*     */     }
/*     */ 
/*     */     
/*     */     public Spliterator<E> spliterator() {
/* 182 */       return CollectSpliterators.filter(this.unfiltered.spliterator(), (Predicate<? super E>)this.predicate);
/*     */     }
/*     */ 
/*     */     
/*     */     public void forEach(Consumer<? super E> action) {
/* 187 */       Preconditions.checkNotNull(action);
/* 188 */       this.unfiltered.forEach(e -> {
/*     */             if (this.predicate.test(e)) {
/*     */               action.accept(e);
/*     */             }
/*     */           });
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public boolean remove(Object element) {
/* 198 */       return (contains(element) && this.unfiltered.remove(element));
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean removeAll(Collection<?> collection) {
/* 203 */       return removeIf(collection::contains);
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean retainAll(Collection<?> collection) {
/* 208 */       return removeIf(element -> !collection.contains(element));
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean removeIf(Predicate<? super E> filter) {
/* 213 */       Preconditions.checkNotNull(filter);
/* 214 */       return this.unfiltered.removeIf(element -> (this.predicate.apply(element) && filter.test(element)));
/*     */     }
/*     */ 
/*     */     
/*     */     public int size() {
/* 219 */       int size = 0;
/* 220 */       for (E e : this.unfiltered) {
/* 221 */         if (this.predicate.apply(e)) {
/* 222 */           size++;
/*     */         }
/*     */       } 
/* 225 */       return size;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public Object[] toArray() {
/* 231 */       return Lists.<E>newArrayList(iterator()).toArray();
/*     */     }
/*     */ 
/*     */     
/*     */     public <T> T[] toArray(T[] array) {
/* 236 */       return (T[])Lists.<E>newArrayList(iterator()).toArray((Object[])array);
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <F, T> Collection<T> transform(Collection<F> fromCollection, Function<? super F, T> function) {
/* 261 */     return new TransformedCollection<>(fromCollection, function);
/*     */   }
/*     */   
/*     */   static class TransformedCollection<F, T> extends AbstractCollection<T> {
/*     */     final Collection<F> fromCollection;
/*     */     final Function<? super F, ? extends T> function;
/*     */     
/*     */     TransformedCollection(Collection<F> fromCollection, Function<? super F, ? extends T> function) {
/* 269 */       this.fromCollection = (Collection<F>)Preconditions.checkNotNull(fromCollection);
/* 270 */       this.function = (Function<? super F, ? extends T>)Preconditions.checkNotNull(function);
/*     */     }
/*     */ 
/*     */     
/*     */     public void clear() {
/* 275 */       this.fromCollection.clear();
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean isEmpty() {
/* 280 */       return this.fromCollection.isEmpty();
/*     */     }
/*     */ 
/*     */     
/*     */     public Iterator<T> iterator() {
/* 285 */       return Iterators.transform(this.fromCollection.iterator(), this.function);
/*     */     }
/*     */ 
/*     */     
/*     */     public Spliterator<T> spliterator() {
/* 290 */       return CollectSpliterators.map(this.fromCollection.spliterator(), (Function<? super F, ? extends T>)this.function);
/*     */     }
/*     */ 
/*     */     
/*     */     public void forEach(Consumer<? super T> action) {
/* 295 */       Preconditions.checkNotNull(action);
/* 296 */       this.fromCollection.forEach(f -> action.accept(this.function.apply(f)));
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean removeIf(Predicate<? super T> filter) {
/* 301 */       Preconditions.checkNotNull(filter);
/* 302 */       return this.fromCollection.removeIf(element -> filter.test(this.function.apply(element)));
/*     */     }
/*     */ 
/*     */     
/*     */     public int size() {
/* 307 */       return this.fromCollection.size();
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
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static boolean containsAllImpl(Collection<?> self, Collection<?> c) {
/* 323 */     for (Object o : c) {
/* 324 */       if (!self.contains(o)) {
/* 325 */         return false;
/*     */       }
/*     */     } 
/* 328 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   static String toStringImpl(Collection<?> collection) {
/* 333 */     StringBuilder sb = newStringBuilderForCollection(collection.size()).append('[');
/* 334 */     boolean first = true;
/* 335 */     for (Object o : collection) {
/* 336 */       if (!first) {
/* 337 */         sb.append(", ");
/*     */       }
/* 339 */       first = false;
/* 340 */       if (o == collection) {
/* 341 */         sb.append("(this Collection)"); continue;
/*     */       } 
/* 343 */       sb.append(o);
/*     */     } 
/*     */     
/* 346 */     return sb.append(']').toString();
/*     */   }
/*     */ 
/*     */   
/*     */   static StringBuilder newStringBuilderForCollection(int size) {
/* 351 */     CollectPreconditions.checkNonnegative(size, "size");
/* 352 */     return new StringBuilder((int)Math.min(size * 8L, 1073741824L));
/*     */   }
/*     */ 
/*     */   
/*     */   static <T> Collection<T> cast(Iterable<T> iterable) {
/* 357 */     return (Collection<T>)iterable;
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
/*     */   @Beta
/*     */   public static <E extends Comparable<? super E>> Collection<List<E>> orderedPermutations(Iterable<E> elements) {
/* 385 */     return orderedPermutations(elements, Ordering.natural());
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
/*     */ 
/*     */ 
/*     */   
/*     */   @Beta
/*     */   public static <E> Collection<List<E>> orderedPermutations(Iterable<E> elements, Comparator<? super E> comparator) {
/* 437 */     return new OrderedPermutationCollection<>(elements, comparator);
/*     */   }
/*     */   
/*     */   private static final class OrderedPermutationCollection<E> extends AbstractCollection<List<E>> {
/*     */     final ImmutableList<E> inputList;
/*     */     final Comparator<? super E> comparator;
/*     */     final int size;
/*     */     
/*     */     OrderedPermutationCollection(Iterable<E> input, Comparator<? super E> comparator) {
/* 446 */       this.inputList = ImmutableList.sortedCopyOf(comparator, input);
/* 447 */       this.comparator = comparator;
/* 448 */       this.size = calculateSize(this.inputList, comparator);
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
/*     */     private static <E> int calculateSize(List<E> sortedInputList, Comparator<? super E> comparator) {
/* 462 */       int permutations = 1;
/* 463 */       int n = 1;
/* 464 */       int r = 1;
/* 465 */       while (n < sortedInputList.size()) {
/* 466 */         int comparison = comparator.compare(sortedInputList.get(n - 1), sortedInputList.get(n));
/* 467 */         if (comparison < 0) {
/*     */           
/* 469 */           permutations = IntMath.saturatedMultiply(permutations, IntMath.binomial(n, r));
/* 470 */           r = 0;
/* 471 */           if (permutations == Integer.MAX_VALUE) {
/* 472 */             return Integer.MAX_VALUE;
/*     */           }
/*     */         } 
/* 475 */         n++;
/* 476 */         r++;
/*     */       } 
/* 478 */       return IntMath.saturatedMultiply(permutations, IntMath.binomial(n, r));
/*     */     }
/*     */ 
/*     */     
/*     */     public int size() {
/* 483 */       return this.size;
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean isEmpty() {
/* 488 */       return false;
/*     */     }
/*     */ 
/*     */     
/*     */     public Iterator<List<E>> iterator() {
/* 493 */       return new Collections2.OrderedPermutationIterator<>(this.inputList, this.comparator);
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean contains(Object obj) {
/* 498 */       if (obj instanceof List) {
/* 499 */         List<?> list = (List)obj;
/* 500 */         return Collections2.isPermutation(this.inputList, list);
/*     */       } 
/* 502 */       return false;
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString() {
/* 507 */       return "orderedPermutationCollection(" + this.inputList + ")";
/*     */     }
/*     */   }
/*     */   
/*     */   private static final class OrderedPermutationIterator<E> extends AbstractIterator<List<E>> {
/*     */     List<E> nextPermutation;
/*     */     final Comparator<? super E> comparator;
/*     */     
/*     */     OrderedPermutationIterator(List<E> list, Comparator<? super E> comparator) {
/* 516 */       this.nextPermutation = Lists.newArrayList(list);
/* 517 */       this.comparator = comparator;
/*     */     }
/*     */ 
/*     */     
/*     */     protected List<E> computeNext() {
/* 522 */       if (this.nextPermutation == null) {
/* 523 */         return endOfData();
/*     */       }
/* 525 */       ImmutableList<E> next = ImmutableList.copyOf(this.nextPermutation);
/* 526 */       calculateNextPermutation();
/* 527 */       return next;
/*     */     }
/*     */     
/*     */     void calculateNextPermutation() {
/* 531 */       int j = findNextJ();
/* 532 */       if (j == -1) {
/* 533 */         this.nextPermutation = null;
/*     */         
/*     */         return;
/*     */       } 
/* 537 */       int l = findNextL(j);
/* 538 */       Collections.swap(this.nextPermutation, j, l);
/* 539 */       int n = this.nextPermutation.size();
/* 540 */       Collections.reverse(this.nextPermutation.subList(j + 1, n));
/*     */     }
/*     */     
/*     */     int findNextJ() {
/* 544 */       for (int k = this.nextPermutation.size() - 2; k >= 0; k--) {
/* 545 */         if (this.comparator.compare(this.nextPermutation.get(k), this.nextPermutation.get(k + 1)) < 0) {
/* 546 */           return k;
/*     */         }
/*     */       } 
/* 549 */       return -1;
/*     */     }
/*     */     
/*     */     int findNextL(int j) {
/* 553 */       E ak = this.nextPermutation.get(j);
/* 554 */       for (int l = this.nextPermutation.size() - 1; l > j; l--) {
/* 555 */         if (this.comparator.compare(ak, this.nextPermutation.get(l)) < 0) {
/* 556 */           return l;
/*     */         }
/*     */       } 
/* 559 */       throw new AssertionError("this statement should be unreachable");
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
/*     */ 
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
/*     */   public static <E> Collection<List<E>> permutations(Collection<E> elements) {
/* 582 */     return new PermutationCollection<>(ImmutableList.copyOf(elements));
/*     */   }
/*     */   
/*     */   private static final class PermutationCollection<E> extends AbstractCollection<List<E>> {
/*     */     final ImmutableList<E> inputList;
/*     */     
/*     */     PermutationCollection(ImmutableList<E> input) {
/* 589 */       this.inputList = input;
/*     */     }
/*     */ 
/*     */     
/*     */     public int size() {
/* 594 */       return IntMath.factorial(this.inputList.size());
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean isEmpty() {
/* 599 */       return false;
/*     */     }
/*     */ 
/*     */     
/*     */     public Iterator<List<E>> iterator() {
/* 604 */       return new Collections2.PermutationIterator<>(this.inputList);
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean contains(Object obj) {
/* 609 */       if (obj instanceof List) {
/* 610 */         List<?> list = (List)obj;
/* 611 */         return Collections2.isPermutation(this.inputList, list);
/*     */       } 
/* 613 */       return false;
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString() {
/* 618 */       return "permutations(" + this.inputList + ")";
/*     */     }
/*     */   }
/*     */   
/*     */   private static class PermutationIterator<E> extends AbstractIterator<List<E>> {
/*     */     final List<E> list;
/*     */     final int[] c;
/*     */     final int[] o;
/*     */     int j;
/*     */     
/*     */     PermutationIterator(List<E> list) {
/* 629 */       this.list = new ArrayList<>(list);
/* 630 */       int n = list.size();
/* 631 */       this.c = new int[n];
/* 632 */       this.o = new int[n];
/* 633 */       Arrays.fill(this.c, 0);
/* 634 */       Arrays.fill(this.o, 1);
/* 635 */       this.j = Integer.MAX_VALUE;
/*     */     }
/*     */ 
/*     */     
/*     */     protected List<E> computeNext() {
/* 640 */       if (this.j <= 0) {
/* 641 */         return endOfData();
/*     */       }
/* 643 */       ImmutableList<E> next = ImmutableList.copyOf(this.list);
/* 644 */       calculateNextPermutation();
/* 645 */       return next;
/*     */     }
/*     */     
/*     */     void calculateNextPermutation() {
/* 649 */       this.j = this.list.size() - 1;
/* 650 */       int s = 0;
/*     */ 
/*     */ 
/*     */       
/* 654 */       if (this.j == -1) {
/*     */         return;
/*     */       }
/*     */       
/*     */       while (true) {
/* 659 */         int q = this.c[this.j] + this.o[this.j];
/* 660 */         if (q < 0) {
/* 661 */           switchDirection();
/*     */           continue;
/*     */         } 
/* 664 */         if (q == this.j + 1) {
/* 665 */           if (this.j == 0) {
/*     */             break;
/*     */           }
/* 668 */           s++;
/* 669 */           switchDirection();
/*     */           
/*     */           continue;
/*     */         } 
/* 673 */         Collections.swap(this.list, this.j - this.c[this.j] + s, this.j - q + s);
/* 674 */         this.c[this.j] = q;
/*     */         break;
/*     */       } 
/*     */     }
/*     */     
/*     */     void switchDirection() {
/* 680 */       this.o[this.j] = -this.o[this.j];
/* 681 */       this.j--;
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   private static boolean isPermutation(List<?> first, List<?> second) {
/* 687 */     if (first.size() != second.size()) {
/* 688 */       return false;
/*     */     }
/* 690 */     Multiset<?> firstMultiset = HashMultiset.create(first);
/* 691 */     Multiset<?> secondMultiset = HashMultiset.create(second);
/* 692 */     return firstMultiset.equals(secondMultiset);
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\google\common\collect\Collections2.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */