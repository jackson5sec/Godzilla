/*     */ package com.google.common.collect;
/*     */ 
/*     */ import com.google.common.annotations.GwtCompatible;
/*     */ import com.google.common.annotations.VisibleForTesting;
/*     */ import com.google.common.base.Function;
/*     */ import com.google.common.base.Preconditions;
/*     */ import com.google.errorprone.annotations.CanIgnoreReturnValue;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.Collection;
/*     */ import java.util.Collections;
/*     */ import java.util.Comparator;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.concurrent.ConcurrentMap;
/*     */ import java.util.concurrent.atomic.AtomicInteger;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
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
/*     */ public abstract class Ordering<T>
/*     */   implements Comparator<T>
/*     */ {
/*     */   static final int LEFT_IS_GREATER = 1;
/*     */   static final int RIGHT_IS_GREATER = -1;
/*     */   
/*     */   @GwtCompatible(serializable = true)
/*     */   public static <C extends Comparable> Ordering<C> natural() {
/* 159 */     return NaturalOrdering.INSTANCE;
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
/*     */   @GwtCompatible(serializable = true)
/*     */   public static <T> Ordering<T> from(Comparator<T> comparator) {
/* 179 */     return (comparator instanceof Ordering) ? (Ordering<T>)comparator : new ComparatorOrdering<>(comparator);
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
/*     */   @GwtCompatible(serializable = true)
/*     */   public static <T> Ordering<T> from(Ordering<T> ordering) {
/* 192 */     return (Ordering<T>)Preconditions.checkNotNull(ordering);
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
/*     */   @GwtCompatible(serializable = true)
/*     */   public static <T> Ordering<T> explicit(List<T> valuesInOrder) {
/* 217 */     return new ExplicitOrdering<>(valuesInOrder);
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
/*     */   @GwtCompatible(serializable = true)
/*     */   public static <T> Ordering<T> explicit(T leastValue, T... remainingValuesInOrder) {
/* 243 */     return explicit(Lists.asList(leastValue, remainingValuesInOrder));
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
/*     */   @GwtCompatible(serializable = true)
/*     */   public static Ordering<Object> allEqual() {
/* 279 */     return AllEqualOrdering.INSTANCE;
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
/*     */   @GwtCompatible(serializable = true)
/*     */   public static Ordering<Object> usingToString() {
/* 292 */     return UsingToStringOrdering.INSTANCE;
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
/*     */   public static Ordering<Object> arbitrary() {
/* 312 */     return ArbitraryOrderingHolder.ARBITRARY_ORDERING;
/*     */   }
/*     */   
/*     */   private static class ArbitraryOrderingHolder {
/* 316 */     static final Ordering<Object> ARBITRARY_ORDERING = new Ordering.ArbitraryOrdering();
/*     */   }
/*     */   
/*     */   @VisibleForTesting
/*     */   static class ArbitraryOrdering
/*     */     extends Ordering<Object> {
/* 322 */     private final AtomicInteger counter = new AtomicInteger(0);
/*     */     
/* 324 */     private final ConcurrentMap<Object, Integer> uids = Platform.tryWeakKeys(new MapMaker()).makeMap();
/*     */     
/*     */     private Integer getUid(Object obj) {
/* 327 */       Integer uid = this.uids.get(obj);
/* 328 */       if (uid == null) {
/*     */ 
/*     */ 
/*     */         
/* 332 */         uid = Integer.valueOf(this.counter.getAndIncrement());
/* 333 */         Integer alreadySet = this.uids.putIfAbsent(obj, uid);
/* 334 */         if (alreadySet != null) {
/* 335 */           uid = alreadySet;
/*     */         }
/*     */       } 
/* 338 */       return uid;
/*     */     }
/*     */ 
/*     */     
/*     */     public int compare(Object left, Object right) {
/* 343 */       if (left == right)
/* 344 */         return 0; 
/* 345 */       if (left == null)
/* 346 */         return -1; 
/* 347 */       if (right == null) {
/* 348 */         return 1;
/*     */       }
/* 350 */       int leftCode = identityHashCode(left);
/* 351 */       int rightCode = identityHashCode(right);
/* 352 */       if (leftCode != rightCode) {
/* 353 */         return (leftCode < rightCode) ? -1 : 1;
/*     */       }
/*     */ 
/*     */       
/* 357 */       int result = getUid(left).compareTo(getUid(right));
/* 358 */       if (result == 0) {
/* 359 */         throw new AssertionError();
/*     */       }
/* 361 */       return result;
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString() {
/* 366 */       return "Ordering.arbitrary()";
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
/*     */     int identityHashCode(Object object) {
/* 378 */       return System.identityHashCode(object);
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
/*     */   @GwtCompatible(serializable = true)
/*     */   public <S extends T> Ordering<S> reverse() {
/* 402 */     return new ReverseOrdering<>(this);
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
/*     */   @GwtCompatible(serializable = true)
/*     */   public <S extends T> Ordering<S> nullsFirst() {
/* 415 */     return new NullsFirstOrdering<>(this);
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
/*     */   @GwtCompatible(serializable = true)
/*     */   public <S extends T> Ordering<S> nullsLast() {
/* 428 */     return new NullsLastOrdering<>(this);
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
/*     */   @GwtCompatible(serializable = true)
/*     */   public <F> Ordering<F> onResultOf(Function<F, ? extends T> function) {
/* 446 */     return new ByFunctionOrdering<>(function, this);
/*     */   }
/*     */   
/*     */   <T2 extends T> Ordering<Map.Entry<T2, ?>> onKeys() {
/* 450 */     return onResultOf(Maps.keyFunction());
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
/*     */   @GwtCompatible(serializable = true)
/*     */   public <U extends T> Ordering<U> compound(Comparator<? super U> secondaryComparator) {
/* 468 */     return new CompoundOrdering<>(this, (Comparator<? super U>)Preconditions.checkNotNull(secondaryComparator));
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
/*     */   @GwtCompatible(serializable = true)
/*     */   public static <T> Ordering<T> compound(Iterable<? extends Comparator<? super T>> comparators) {
/* 492 */     return new CompoundOrdering<>(comparators);
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
/*     */   @GwtCompatible(serializable = true)
/*     */   public <S extends T> Ordering<Iterable<S>> lexicographical() {
/* 522 */     return new LexicographicalOrdering<>(this);
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
/*     */   @CanIgnoreReturnValue
/*     */   public abstract int compare(T paramT1, T paramT2);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public <E extends T> E min(Iterator<E> iterator) {
/* 549 */     E minSoFar = iterator.next();
/*     */     
/* 551 */     while (iterator.hasNext()) {
/* 552 */       minSoFar = min(minSoFar, iterator.next());
/*     */     }
/*     */     
/* 555 */     return minSoFar;
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
/*     */   public <E extends T> E min(Iterable<E> iterable) {
/* 574 */     return min(iterable.iterator());
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
/*     */   public <E extends T> E min(E a, E b) {
/* 593 */     return (compare((T)a, (T)b) <= 0) ? a : b;
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
/*     */   public <E extends T> E min(E a, E b, E c, E... rest) {
/* 611 */     E minSoFar = min(min(a, b), c);
/*     */     
/* 613 */     for (E r : rest) {
/* 614 */       minSoFar = min(minSoFar, r);
/*     */     }
/*     */     
/* 617 */     return minSoFar;
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
/*     */   public <E extends T> E max(Iterator<E> iterator) {
/* 637 */     E maxSoFar = iterator.next();
/*     */     
/* 639 */     while (iterator.hasNext()) {
/* 640 */       maxSoFar = max(maxSoFar, iterator.next());
/*     */     }
/*     */     
/* 643 */     return maxSoFar;
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
/*     */   public <E extends T> E max(Iterable<E> iterable) {
/* 662 */     return max(iterable.iterator());
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
/*     */   public <E extends T> E max(E a, E b) {
/* 681 */     return (compare((T)a, (T)b) >= 0) ? a : b;
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
/*     */   public <E extends T> E max(E a, E b, E c, E... rest) {
/* 699 */     E maxSoFar = max(max(a, b), c);
/*     */     
/* 701 */     for (E r : rest) {
/* 702 */       maxSoFar = max(maxSoFar, r);
/*     */     }
/*     */     
/* 705 */     return maxSoFar;
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
/*     */   public <E extends T> List<E> leastOf(Iterable<E> iterable, int k) {
/* 725 */     if (iterable instanceof Collection) {
/* 726 */       Collection<E> collection = (Collection<E>)iterable;
/* 727 */       if (collection.size() <= 2L * k) {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 733 */         E[] array = (E[])collection.toArray();
/* 734 */         Arrays.sort(array, this);
/* 735 */         if (array.length > k) {
/* 736 */           array = Arrays.copyOf(array, k);
/*     */         }
/* 738 */         return Collections.unmodifiableList(Arrays.asList(array));
/*     */       } 
/*     */     } 
/* 741 */     return leastOf(iterable.iterator(), k);
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
/*     */   public <E extends T> List<E> leastOf(Iterator<E> iterator, int k) {
/* 761 */     Preconditions.checkNotNull(iterator);
/* 762 */     CollectPreconditions.checkNonnegative(k, "k");
/*     */     
/* 764 */     if (k == 0 || !iterator.hasNext())
/* 765 */       return Collections.emptyList(); 
/* 766 */     if (k >= 1073741823) {
/*     */       
/* 768 */       ArrayList<E> list = Lists.newArrayList(iterator);
/* 769 */       Collections.sort(list, this);
/* 770 */       if (list.size() > k) {
/* 771 */         list.subList(k, list.size()).clear();
/*     */       }
/* 773 */       list.trimToSize();
/* 774 */       return Collections.unmodifiableList(list);
/*     */     } 
/* 776 */     TopKSelector<E> selector = TopKSelector.least(k, this);
/* 777 */     selector.offerAll(iterator);
/* 778 */     return selector.topK();
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
/*     */   public <E extends T> List<E> greatestOf(Iterable<E> iterable, int k) {
/* 801 */     return reverse().leastOf(iterable, k);
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
/*     */   public <E extends T> List<E> greatestOf(Iterator<E> iterator, int k) {
/* 821 */     return reverse().leastOf(iterator, k);
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
/*     */   public <E extends T> List<E> sortedCopy(Iterable<E> elements) {
/* 842 */     E[] array = (E[])Iterables.toArray(elements);
/* 843 */     Arrays.sort(array, this);
/* 844 */     return Lists.newArrayList(Arrays.asList(array));
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
/*     */   public <E extends T> ImmutableList<E> immutableSortedCopy(Iterable<E> elements) {
/* 864 */     return ImmutableList.sortedCopyOf(this, elements);
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
/*     */   public boolean isOrdered(Iterable<? extends T> iterable) {
/* 877 */     Iterator<? extends T> it = iterable.iterator();
/* 878 */     if (it.hasNext()) {
/* 879 */       T prev = it.next();
/* 880 */       while (it.hasNext()) {
/* 881 */         T next = it.next();
/* 882 */         if (compare(prev, next) > 0) {
/* 883 */           return false;
/*     */         }
/* 885 */         prev = next;
/*     */       } 
/*     */     } 
/* 888 */     return true;
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
/*     */   public boolean isStrictlyOrdered(Iterable<? extends T> iterable) {
/* 901 */     Iterator<? extends T> it = iterable.iterator();
/* 902 */     if (it.hasNext()) {
/* 903 */       T prev = it.next();
/* 904 */       while (it.hasNext()) {
/* 905 */         T next = it.next();
/* 906 */         if (compare(prev, next) >= 0) {
/* 907 */           return false;
/*     */         }
/* 909 */         prev = next;
/*     */       } 
/*     */     } 
/* 912 */     return true;
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
/*     */   public int binarySearch(List<? extends T> sortedList, T key) {
/* 925 */     return Collections.binarySearch(sortedList, key, this);
/*     */   }
/*     */ 
/*     */   
/*     */   @VisibleForTesting
/*     */   static class IncomparableValueException
/*     */     extends ClassCastException
/*     */   {
/*     */     final Object value;
/*     */     
/*     */     private static final long serialVersionUID = 0L;
/*     */     
/*     */     IncomparableValueException(Object value) {
/* 938 */       super("Cannot compare value: " + value);
/* 939 */       this.value = value;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\google\common\collect\Ordering.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */