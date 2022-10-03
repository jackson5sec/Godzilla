/*      */ package com.google.common.collect;
/*      */ 
/*      */ import com.google.common.annotations.Beta;
/*      */ import com.google.common.annotations.GwtCompatible;
/*      */ import com.google.common.annotations.GwtIncompatible;
/*      */ import com.google.common.base.Function;
/*      */ import com.google.common.base.Optional;
/*      */ import com.google.common.base.Preconditions;
/*      */ import com.google.common.base.Predicate;
/*      */ import com.google.common.base.Predicates;
/*      */ import com.google.errorprone.annotations.CanIgnoreReturnValue;
/*      */ import java.util.Collection;
/*      */ import java.util.Comparator;
/*      */ import java.util.Iterator;
/*      */ import java.util.List;
/*      */ import java.util.NoSuchElementException;
/*      */ import java.util.Queue;
/*      */ import java.util.Set;
/*      */ import java.util.Spliterator;
/*      */ import java.util.function.Consumer;
/*      */ import java.util.function.Function;
/*      */ import java.util.function.Predicate;
/*      */ import java.util.stream.Stream;
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
/*      */ public final class Iterables
/*      */ {
/*      */   public static <T> Iterable<T> unmodifiableIterable(Iterable<? extends T> iterable) {
/*   72 */     Preconditions.checkNotNull(iterable);
/*   73 */     if (iterable instanceof UnmodifiableIterable || iterable instanceof ImmutableCollection)
/*      */     {
/*   75 */       return (Iterable)iterable;
/*      */     }
/*      */     
/*   78 */     return new UnmodifiableIterable<>(iterable);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Deprecated
/*      */   public static <E> Iterable<E> unmodifiableIterable(ImmutableCollection<E> iterable) {
/*   89 */     return (Iterable<E>)Preconditions.checkNotNull(iterable);
/*      */   }
/*      */   
/*      */   private static final class UnmodifiableIterable<T> extends FluentIterable<T> {
/*      */     private final Iterable<? extends T> iterable;
/*      */     
/*      */     private UnmodifiableIterable(Iterable<? extends T> iterable) {
/*   96 */       this.iterable = iterable;
/*      */     }
/*      */ 
/*      */     
/*      */     public Iterator<T> iterator() {
/*  101 */       return Iterators.unmodifiableIterator(this.iterable.iterator());
/*      */     }
/*      */ 
/*      */     
/*      */     public void forEach(Consumer<? super T> action) {
/*  106 */       this.iterable.forEach(action);
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     public Spliterator<T> spliterator() {
/*  112 */       return (Spliterator)this.iterable.spliterator();
/*      */     }
/*      */ 
/*      */     
/*      */     public String toString() {
/*  117 */       return this.iterable.toString();
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public static int size(Iterable<?> iterable) {
/*  124 */     return (iterable instanceof Collection) ? ((Collection)iterable)
/*  125 */       .size() : 
/*  126 */       Iterators.size(iterable.iterator());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static boolean contains(Iterable<?> iterable, Object element) {
/*  136 */     if (iterable instanceof Collection) {
/*  137 */       Collection<?> collection = (Collection)iterable;
/*  138 */       return Collections2.safeContains(collection, element);
/*      */     } 
/*  140 */     return Iterators.contains(iterable.iterator(), element);
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
/*      */   @CanIgnoreReturnValue
/*      */   public static boolean removeAll(Iterable<?> removeFrom, Collection<?> elementsToRemove) {
/*  155 */     return (removeFrom instanceof Collection) ? ((Collection)removeFrom)
/*  156 */       .removeAll((Collection)Preconditions.checkNotNull(elementsToRemove)) : 
/*  157 */       Iterators.removeAll(removeFrom.iterator(), elementsToRemove);
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
/*      */   @CanIgnoreReturnValue
/*      */   public static boolean retainAll(Iterable<?> removeFrom, Collection<?> elementsToRetain) {
/*  172 */     return (removeFrom instanceof Collection) ? ((Collection)removeFrom)
/*  173 */       .retainAll((Collection)Preconditions.checkNotNull(elementsToRetain)) : 
/*  174 */       Iterators.retainAll(removeFrom.iterator(), elementsToRetain);
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
/*      */   @CanIgnoreReturnValue
/*      */   public static <T> boolean removeIf(Iterable<T> removeFrom, Predicate<? super T> predicate) {
/*  195 */     if (removeFrom instanceof Collection) {
/*  196 */       return ((Collection<T>)removeFrom).removeIf((Predicate<? super T>)predicate);
/*      */     }
/*  198 */     return Iterators.removeIf(removeFrom.iterator(), predicate);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   static <T> T removeFirstMatching(Iterable<T> removeFrom, Predicate<? super T> predicate) {
/*  204 */     Preconditions.checkNotNull(predicate);
/*  205 */     Iterator<T> iterator = removeFrom.iterator();
/*  206 */     while (iterator.hasNext()) {
/*  207 */       T next = iterator.next();
/*  208 */       if (predicate.apply(next)) {
/*  209 */         iterator.remove();
/*  210 */         return next;
/*      */       } 
/*      */     } 
/*  213 */     return null;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static boolean elementsEqual(Iterable<?> iterable1, Iterable<?> iterable2) {
/*  223 */     if (iterable1 instanceof Collection && iterable2 instanceof Collection) {
/*  224 */       Collection<?> collection1 = (Collection)iterable1;
/*  225 */       Collection<?> collection2 = (Collection)iterable2;
/*  226 */       if (collection1.size() != collection2.size()) {
/*  227 */         return false;
/*      */       }
/*      */     } 
/*  230 */     return Iterators.elementsEqual(iterable1.iterator(), iterable2.iterator());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static String toString(Iterable<?> iterable) {
/*  241 */     return Iterators.toString(iterable.iterator());
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
/*      */   public static <T> T getOnlyElement(Iterable<T> iterable) {
/*  254 */     return Iterators.getOnlyElement(iterable.iterator());
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
/*      */   public static <T> T getOnlyElement(Iterable<? extends T> iterable, T defaultValue) {
/*  268 */     return Iterators.getOnlyElement(iterable.iterator(), defaultValue);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @GwtIncompatible
/*      */   public static <T> T[] toArray(Iterable<? extends T> iterable, Class<T> type) {
/*  280 */     return toArray(iterable, ObjectArrays.newArray(type, 0));
/*      */   }
/*      */   
/*      */   static <T> T[] toArray(Iterable<? extends T> iterable, T[] array) {
/*  284 */     Collection<? extends T> collection = castOrCopyToCollection(iterable);
/*  285 */     return collection.toArray(array);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   static Object[] toArray(Iterable<?> iterable) {
/*  295 */     return castOrCopyToCollection(iterable).toArray();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static <E> Collection<E> castOrCopyToCollection(Iterable<E> iterable) {
/*  304 */     return (iterable instanceof Collection) ? (Collection<E>)iterable : 
/*      */       
/*  306 */       Lists.<E>newArrayList(iterable.iterator());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @CanIgnoreReturnValue
/*      */   public static <T> boolean addAll(Collection<T> addTo, Iterable<? extends T> elementsToAdd) {
/*  316 */     if (elementsToAdd instanceof Collection) {
/*  317 */       Collection<? extends T> c = Collections2.cast(elementsToAdd);
/*  318 */       return addTo.addAll(c);
/*      */     } 
/*  320 */     return Iterators.addAll(addTo, ((Iterable<? extends T>)Preconditions.checkNotNull(elementsToAdd)).iterator());
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
/*      */   public static int frequency(Iterable<?> iterable, Object element) {
/*  335 */     if (iterable instanceof Multiset)
/*  336 */       return ((Multiset)iterable).count(element); 
/*  337 */     if (iterable instanceof Set) {
/*  338 */       return ((Set)iterable).contains(element) ? 1 : 0;
/*      */     }
/*  340 */     return Iterators.frequency(iterable.iterator(), element);
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
/*      */   public static <T> Iterable<T> cycle(final Iterable<T> iterable) {
/*  362 */     Preconditions.checkNotNull(iterable);
/*  363 */     return new FluentIterable<T>()
/*      */       {
/*      */         public Iterator<T> iterator() {
/*  366 */           return Iterators.cycle(iterable);
/*      */         }
/*      */ 
/*      */         
/*      */         public Spliterator<T> spliterator() {
/*  371 */           return Stream.generate(() -> iterable).flatMap(Streams::stream).spliterator();
/*      */         }
/*      */ 
/*      */         
/*      */         public String toString() {
/*  376 */           return iterable.toString() + " (cycled)";
/*      */         }
/*      */       };
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
/*      */   @SafeVarargs
/*      */   public static <T> Iterable<T> cycle(T... elements) {
/*  403 */     return cycle(Lists.newArrayList(elements));
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
/*      */   public static <T> Iterable<T> concat(Iterable<? extends T> a, Iterable<? extends T> b) {
/*  418 */     return FluentIterable.concat(a, b);
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
/*      */   public static <T> Iterable<T> concat(Iterable<? extends T> a, Iterable<? extends T> b, Iterable<? extends T> c) {
/*  434 */     return FluentIterable.concat(a, b, c);
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
/*      */   public static <T> Iterable<T> concat(Iterable<? extends T> a, Iterable<? extends T> b, Iterable<? extends T> c, Iterable<? extends T> d) {
/*  454 */     return FluentIterable.concat(a, b, c, d);
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
/*      */   @SafeVarargs
/*      */   public static <T> Iterable<T> concat(Iterable<? extends T>... inputs) {
/*  472 */     return FluentIterable.concat(inputs);
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
/*      */   public static <T> Iterable<T> concat(Iterable<? extends Iterable<? extends T>> inputs) {
/*  488 */     return FluentIterable.concat(inputs);
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
/*      */   public static <T> Iterable<List<T>> partition(final Iterable<T> iterable, final int size) {
/*  510 */     Preconditions.checkNotNull(iterable);
/*  511 */     Preconditions.checkArgument((size > 0));
/*  512 */     return new FluentIterable<List<T>>()
/*      */       {
/*      */         public Iterator<List<T>> iterator() {
/*  515 */           return Iterators.partition(iterable.iterator(), size);
/*      */         }
/*      */       };
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
/*      */   public static <T> Iterable<List<T>> paddedPartition(final Iterable<T> iterable, final int size) {
/*  536 */     Preconditions.checkNotNull(iterable);
/*  537 */     Preconditions.checkArgument((size > 0));
/*  538 */     return new FluentIterable<List<T>>()
/*      */       {
/*      */         public Iterator<List<T>> iterator() {
/*  541 */           return Iterators.paddedPartition(iterable.iterator(), size);
/*      */         }
/*      */       };
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static <T> Iterable<T> filter(final Iterable<T> unfiltered, final Predicate<? super T> retainIfTrue) {
/*  554 */     Preconditions.checkNotNull(unfiltered);
/*  555 */     Preconditions.checkNotNull(retainIfTrue);
/*  556 */     return new FluentIterable<T>()
/*      */       {
/*      */         public Iterator<T> iterator() {
/*  559 */           return Iterators.filter(unfiltered.iterator(), retainIfTrue);
/*      */         }
/*      */ 
/*      */         
/*      */         public void forEach(Consumer<? super T> action) {
/*  564 */           Preconditions.checkNotNull(action);
/*  565 */           unfiltered.forEach(a -> {
/*      */                 if (retainIfTrue.test(a)) {
/*      */                   action.accept(a);
/*      */                 }
/*      */               });
/*      */         }
/*      */ 
/*      */ 
/*      */         
/*      */         public Spliterator<T> spliterator() {
/*  575 */           return CollectSpliterators.filter(unfiltered.spliterator(), (Predicate<? super T>)retainIfTrue);
/*      */         }
/*      */       };
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
/*      */   @GwtIncompatible
/*      */   public static <T> Iterable<T> filter(Iterable<?> unfiltered, Class<T> desiredType) {
/*  597 */     Preconditions.checkNotNull(unfiltered);
/*  598 */     Preconditions.checkNotNull(desiredType);
/*  599 */     return filter((Iterable)unfiltered, Predicates.instanceOf(desiredType));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static <T> boolean any(Iterable<T> iterable, Predicate<? super T> predicate) {
/*  608 */     return Iterators.any(iterable.iterator(), predicate);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static <T> boolean all(Iterable<T> iterable, Predicate<? super T> predicate) {
/*  618 */     return Iterators.all(iterable.iterator(), predicate);
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
/*      */   public static <T> T find(Iterable<T> iterable, Predicate<? super T> predicate) {
/*  631 */     return Iterators.find(iterable.iterator(), predicate);
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
/*      */   public static <T> T find(Iterable<? extends T> iterable, Predicate<? super T> predicate, T defaultValue) {
/*  646 */     return Iterators.find(iterable.iterator(), predicate, defaultValue);
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
/*      */   public static <T> Optional<T> tryFind(Iterable<T> iterable, Predicate<? super T> predicate) {
/*  661 */     return Iterators.tryFind(iterable.iterator(), predicate);
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
/*      */   public static <T> int indexOf(Iterable<T> iterable, Predicate<? super T> predicate) {
/*  675 */     return Iterators.indexOf(iterable.iterator(), predicate);
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
/*      */   public static <F, T> Iterable<T> transform(final Iterable<F> fromIterable, final Function<? super F, ? extends T> function) {
/*  693 */     Preconditions.checkNotNull(fromIterable);
/*  694 */     Preconditions.checkNotNull(function);
/*  695 */     return new FluentIterable<T>()
/*      */       {
/*      */         public Iterator<T> iterator() {
/*  698 */           return Iterators.transform(fromIterable.iterator(), function);
/*      */         }
/*      */ 
/*      */         
/*      */         public void forEach(Consumer<? super T> action) {
/*  703 */           Preconditions.checkNotNull(action);
/*  704 */           fromIterable.forEach(f -> action.accept(function.apply(f)));
/*      */         }
/*      */ 
/*      */         
/*      */         public Spliterator<T> spliterator() {
/*  709 */           return CollectSpliterators.map(fromIterable.spliterator(), (Function<?, ? extends T>)function);
/*      */         }
/*      */       };
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
/*      */   public static <T> T get(Iterable<T> iterable, int position) {
/*  726 */     Preconditions.checkNotNull(iterable);
/*  727 */     return (iterable instanceof List) ? ((List<T>)iterable)
/*  728 */       .get(position) : 
/*  729 */       Iterators.<T>get(iterable.iterator(), position);
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
/*      */   public static <T> T get(Iterable<? extends T> iterable, int position, T defaultValue) {
/*  749 */     Preconditions.checkNotNull(iterable);
/*  750 */     Iterators.checkNonnegative(position);
/*  751 */     if (iterable instanceof List) {
/*  752 */       List<? extends T> list = Lists.cast(iterable);
/*  753 */       return (position < list.size()) ? list.get(position) : defaultValue;
/*      */     } 
/*  755 */     Iterator<? extends T> iterator = iterable.iterator();
/*  756 */     Iterators.advance(iterator, position);
/*  757 */     return Iterators.getNext(iterator, defaultValue);
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
/*      */   public static <T> T getFirst(Iterable<? extends T> iterable, T defaultValue) {
/*  779 */     return Iterators.getNext(iterable.iterator(), defaultValue);
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
/*      */   public static <T> T getLast(Iterable<T> iterable) {
/*  793 */     if (iterable instanceof List) {
/*  794 */       List<T> list = (List<T>)iterable;
/*  795 */       if (list.isEmpty()) {
/*  796 */         throw new NoSuchElementException();
/*      */       }
/*  798 */       return getLastInNonemptyList(list);
/*      */     } 
/*      */     
/*  801 */     return Iterators.getLast(iterable.iterator());
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
/*      */   public static <T> T getLast(Iterable<? extends T> iterable, T defaultValue) {
/*  816 */     if (iterable instanceof Collection) {
/*  817 */       Collection<? extends T> c = Collections2.cast(iterable);
/*  818 */       if (c.isEmpty())
/*  819 */         return defaultValue; 
/*  820 */       if (iterable instanceof List) {
/*  821 */         return getLastInNonemptyList(Lists.cast((Iterable)iterable));
/*      */       }
/*      */     } 
/*      */     
/*  825 */     return Iterators.getLast(iterable.iterator(), defaultValue);
/*      */   }
/*      */   
/*      */   private static <T> T getLastInNonemptyList(List<T> list) {
/*  829 */     return list.get(list.size() - 1);
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
/*      */   public static <T> Iterable<T> skip(final Iterable<T> iterable, final int numberToSkip) {
/*  852 */     Preconditions.checkNotNull(iterable);
/*  853 */     Preconditions.checkArgument((numberToSkip >= 0), "number to skip cannot be negative");
/*      */     
/*  855 */     return new FluentIterable<T>()
/*      */       {
/*      */         public Iterator<T> iterator() {
/*  858 */           if (iterable instanceof List) {
/*  859 */             List<T> list = (List<T>)iterable;
/*  860 */             int toSkip = Math.min(list.size(), numberToSkip);
/*  861 */             return list.subList(toSkip, list.size()).iterator();
/*      */           } 
/*  863 */           final Iterator<T> iterator = iterable.iterator();
/*      */           
/*  865 */           Iterators.advance(iterator, numberToSkip);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */           
/*  872 */           return new Iterator()
/*      */             {
/*      */               boolean atStart = true;
/*      */               
/*      */               public boolean hasNext() {
/*  877 */                 return iterator.hasNext();
/*      */               }
/*      */ 
/*      */               
/*      */               public T next() {
/*  882 */                 T result = iterator.next();
/*  883 */                 this.atStart = false;
/*  884 */                 return result;
/*      */               }
/*      */ 
/*      */               
/*      */               public void remove() {
/*  889 */                 CollectPreconditions.checkRemove(!this.atStart);
/*  890 */                 iterator.remove();
/*      */               }
/*      */             };
/*      */         }
/*      */ 
/*      */         
/*      */         public Spliterator<T> spliterator() {
/*  897 */           if (iterable instanceof List) {
/*  898 */             List<T> list = (List<T>)iterable;
/*  899 */             int toSkip = Math.min(list.size(), numberToSkip);
/*  900 */             return list.subList(toSkip, list.size()).spliterator();
/*      */           } 
/*  902 */           return Streams.<T>stream(iterable).skip(numberToSkip).spliterator();
/*      */         }
/*      */       };
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
/*      */   public static <T> Iterable<T> limit(final Iterable<T> iterable, final int limitSize) {
/*  922 */     Preconditions.checkNotNull(iterable);
/*  923 */     Preconditions.checkArgument((limitSize >= 0), "limit is negative");
/*  924 */     return new FluentIterable<T>()
/*      */       {
/*      */         public Iterator<T> iterator() {
/*  927 */           return Iterators.limit(iterable.iterator(), limitSize);
/*      */         }
/*      */ 
/*      */         
/*      */         public Spliterator<T> spliterator() {
/*  932 */           return Streams.<T>stream(iterable).limit(limitSize).spliterator();
/*      */         }
/*      */       };
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
/*      */   public static <T> Iterable<T> consumingIterable(final Iterable<T> iterable) {
/*  954 */     Preconditions.checkNotNull(iterable);
/*      */     
/*  956 */     return new FluentIterable<T>()
/*      */       {
/*      */         public Iterator<T> iterator() {
/*  959 */           return (iterable instanceof Queue) ? new ConsumingQueueIterator<>((Queue<T>)iterable) : 
/*      */             
/*  961 */             Iterators.<T>consumingIterator(iterable.iterator());
/*      */         }
/*      */ 
/*      */         
/*      */         public String toString() {
/*  966 */           return "Iterables.consumingIterable(...)";
/*      */         }
/*      */       };
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
/*      */   public static boolean isEmpty(Iterable<?> iterable) {
/*  985 */     if (iterable instanceof Collection) {
/*  986 */       return ((Collection)iterable).isEmpty();
/*      */     }
/*  988 */     return !iterable.iterator().hasNext();
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
/*      */   @Beta
/*      */   public static <T> Iterable<T> mergeSorted(final Iterable<? extends Iterable<? extends T>> iterables, final Comparator<? super T> comparator) {
/* 1007 */     Preconditions.checkNotNull(iterables, "iterables");
/* 1008 */     Preconditions.checkNotNull(comparator, "comparator");
/* 1009 */     Iterable<T> iterable = new FluentIterable<T>()
/*      */       {
/*      */         public Iterator<T> iterator()
/*      */         {
/* 1013 */           return Iterators.mergeSorted(
/* 1014 */               Iterables.transform(iterables, (Function)Iterables.toIterator()), comparator);
/*      */         }
/*      */       };
/* 1017 */     return new UnmodifiableIterable<>(iterable);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   static <T> Function<Iterable<? extends T>, Iterator<? extends T>> toIterator() {
/* 1023 */     return new Function<Iterable<? extends T>, Iterator<? extends T>>()
/*      */       {
/*      */         public Iterator<? extends T> apply(Iterable<? extends T> iterable) {
/* 1026 */           return iterable.iterator();
/*      */         }
/*      */       };
/*      */   }
/*      */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\google\common\collect\Iterables.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */