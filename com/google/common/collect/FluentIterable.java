/*     */ package com.google.common.collect;
/*     */ 
/*     */ import com.google.common.annotations.Beta;
/*     */ import com.google.common.annotations.GwtCompatible;
/*     */ import com.google.common.annotations.GwtIncompatible;
/*     */ import com.google.common.base.Function;
/*     */ import com.google.common.base.Joiner;
/*     */ import com.google.common.base.Optional;
/*     */ import com.google.common.base.Preconditions;
/*     */ import com.google.common.base.Predicate;
/*     */ import com.google.errorprone.annotations.CanIgnoreReturnValue;
/*     */ import java.util.Arrays;
/*     */ import java.util.Comparator;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.SortedSet;
/*     */ import java.util.stream.Stream;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
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
/*     */ public abstract class FluentIterable<E>
/*     */   implements Iterable<E>
/*     */ {
/*     */   private final Optional<Iterable<E>> iterableDelegate;
/*     */   
/*     */   protected FluentIterable() {
/* 119 */     this.iterableDelegate = Optional.absent();
/*     */   }
/*     */   
/*     */   FluentIterable(Iterable<E> iterable) {
/* 123 */     Preconditions.checkNotNull(iterable);
/* 124 */     this.iterableDelegate = Optional.fromNullable((this != iterable) ? iterable : null);
/*     */   }
/*     */   
/*     */   private Iterable<E> getDelegate() {
/* 128 */     return (Iterable<E>)this.iterableDelegate.or(this);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <E> FluentIterable<E> from(final Iterable<E> iterable) {
/* 139 */     return (iterable instanceof FluentIterable) ? (FluentIterable<E>)iterable : new FluentIterable<E>(iterable)
/*     */       {
/*     */         
/*     */         public Iterator<E> iterator()
/*     */         {
/* 144 */           return iterable.iterator();
/*     */         }
/*     */       };
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
/*     */   public static <E> FluentIterable<E> from(E[] elements) {
/* 161 */     return from(Arrays.asList(elements));
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
/*     */   public static <E> FluentIterable<E> from(FluentIterable<E> iterable) {
/* 174 */     return (FluentIterable<E>)Preconditions.checkNotNull(iterable);
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
/*     */   public static <T> FluentIterable<T> concat(Iterable<? extends T> a, Iterable<? extends T> b) {
/* 191 */     return concatNoDefensiveCopy((Iterable<? extends T>[])new Iterable[] { a, b });
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
/*     */   @Beta
/*     */   public static <T> FluentIterable<T> concat(Iterable<? extends T> a, Iterable<? extends T> b, Iterable<? extends T> c) {
/* 210 */     return concatNoDefensiveCopy((Iterable<? extends T>[])new Iterable[] { a, b, c });
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
/*     */   @Beta
/*     */   public static <T> FluentIterable<T> concat(Iterable<? extends T> a, Iterable<? extends T> b, Iterable<? extends T> c, Iterable<? extends T> d) {
/* 233 */     return concatNoDefensiveCopy((Iterable<? extends T>[])new Iterable[] { a, b, c, d });
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
/*     */   @Beta
/*     */   public static <T> FluentIterable<T> concat(Iterable<? extends T>... inputs) {
/* 253 */     return concatNoDefensiveCopy(Arrays.<Iterable<? extends T>>copyOf(inputs, inputs.length));
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
/*     */   @Beta
/*     */   public static <T> FluentIterable<T> concat(final Iterable<? extends Iterable<? extends T>> inputs) {
/* 273 */     Preconditions.checkNotNull(inputs);
/* 274 */     return new FluentIterable<T>()
/*     */       {
/*     */         public Iterator<T> iterator() {
/* 277 */           return Iterators.concat(Iterators.transform(inputs.iterator(), (Function)Iterables.toIterator()));
/*     */         }
/*     */       };
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static <T> FluentIterable<T> concatNoDefensiveCopy(Iterable<? extends T>... inputs) {
/* 285 */     for (Iterable<? extends T> input : inputs) {
/* 286 */       Preconditions.checkNotNull(input);
/*     */     }
/* 288 */     return new FluentIterable<T>()
/*     */       {
/*     */         public Iterator<T> iterator() {
/* 291 */           return Iterators.concat(new AbstractIndexedListIterator(inputs.length)
/*     */               {
/*     */                 
/*     */                 public Iterator<? extends T> get(int i)
/*     */                 {
/* 296 */                   return inputs[i].iterator();
/*     */                 }
/*     */               });
/*     */         }
/*     */       };
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Beta
/*     */   public static <E> FluentIterable<E> of() {
/* 312 */     return from(ImmutableList.of());
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
/*     */   public static <E> FluentIterable<E> of(E element, E... elements) {
/* 325 */     return from(Lists.asList(element, elements));
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
/*     */   public String toString() {
/* 337 */     return Iterables.toString(getDelegate());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final int size() {
/* 346 */     return Iterables.size(getDelegate());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final boolean contains(Object target) {
/* 356 */     return Iterables.contains(getDelegate(), target);
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
/*     */   public final FluentIterable<E> cycle() {
/* 377 */     return from(Iterables.cycle(getDelegate()));
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
/*     */   @Beta
/*     */   public final FluentIterable<E> append(Iterable<? extends E> other) {
/* 393 */     return concat(getDelegate(), other);
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
/*     */   public final FluentIterable<E> append(E... elements) {
/* 406 */     return concat(getDelegate(), Arrays.asList(elements));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final FluentIterable<E> filter(Predicate<? super E> predicate) {
/* 416 */     return from(Iterables.filter(getDelegate(), predicate));
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
/*     */   public final <T> FluentIterable<T> filter(Class<T> type) {
/* 434 */     return from(Iterables.filter(getDelegate(), type));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final boolean anyMatch(Predicate<? super E> predicate) {
/* 443 */     return Iterables.any(getDelegate(), predicate);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final boolean allMatch(Predicate<? super E> predicate) {
/* 453 */     return Iterables.all(getDelegate(), predicate);
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
/*     */   public final Optional<E> firstMatch(Predicate<? super E> predicate) {
/* 466 */     return Iterables.tryFind(getDelegate(), predicate);
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
/*     */   public final <T> FluentIterable<T> transform(Function<? super E, T> function) {
/* 480 */     return from(Iterables.transform(getDelegate(), function));
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
/*     */   public <T> FluentIterable<T> transformAndConcat(Function<? super E, ? extends Iterable<? extends T>> function) {
/* 498 */     return concat(transform(function));
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
/*     */   public final Optional<E> first() {
/* 512 */     Iterator<E> iterator = getDelegate().iterator();
/* 513 */     return iterator.hasNext() ? Optional.of(iterator.next()) : Optional.absent();
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
/*     */   public final Optional<E> last() {
/* 531 */     Iterable<E> iterable = getDelegate();
/* 532 */     if (iterable instanceof List) {
/* 533 */       List<E> list = (List<E>)iterable;
/* 534 */       if (list.isEmpty()) {
/* 535 */         return Optional.absent();
/*     */       }
/* 537 */       return Optional.of(list.get(list.size() - 1));
/*     */     } 
/* 539 */     Iterator<E> iterator = iterable.iterator();
/* 540 */     if (!iterator.hasNext()) {
/* 541 */       return Optional.absent();
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 548 */     if (iterable instanceof SortedSet) {
/* 549 */       SortedSet<E> sortedSet = (SortedSet<E>)iterable;
/* 550 */       return Optional.of(sortedSet.last());
/*     */     } 
/*     */     
/*     */     while (true) {
/* 554 */       E current = iterator.next();
/* 555 */       if (!iterator.hasNext()) {
/* 556 */         return Optional.of(current);
/*     */       }
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
/*     */   public final FluentIterable<E> skip(int numberToSkip) {
/* 579 */     return from(Iterables.skip(getDelegate(), numberToSkip));
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
/*     */   public final FluentIterable<E> limit(int maxSize) {
/* 594 */     return from(Iterables.limit(getDelegate(), maxSize));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final boolean isEmpty() {
/* 603 */     return !getDelegate().iterator().hasNext();
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
/*     */   public final ImmutableList<E> toList() {
/* 617 */     return ImmutableList.copyOf(getDelegate());
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
/*     */   public final ImmutableList<E> toSortedList(Comparator<? super E> comparator) {
/* 633 */     return Ordering.<E>from(comparator).immutableSortedCopy(getDelegate());
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
/*     */   public final ImmutableSet<E> toSet() {
/* 647 */     return ImmutableSet.copyOf(getDelegate());
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
/*     */   public final ImmutableSortedSet<E> toSortedSet(Comparator<? super E> comparator) {
/* 664 */     return ImmutableSortedSet.copyOf(comparator, getDelegate());
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
/*     */   public final ImmutableMultiset<E> toMultiset() {
/* 677 */     return ImmutableMultiset.copyOf(getDelegate());
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
/*     */   public final <V> ImmutableMap<E, V> toMap(Function<? super E, V> valueFunction) {
/* 697 */     return Maps.toMap(getDelegate(), valueFunction);
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
/*     */   public final <K> ImmutableListMultimap<K, E> index(Function<? super E, K> keyFunction) {
/* 719 */     return Multimaps.index(getDelegate(), keyFunction);
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
/*     */   public final <K> ImmutableMap<K, E> uniqueIndex(Function<? super E, K> keyFunction) {
/* 753 */     return Maps.uniqueIndex(getDelegate(), keyFunction);
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
/*     */   @GwtIncompatible
/*     */   public final E[] toArray(Class<E> type) {
/* 770 */     return Iterables.toArray(getDelegate(), type);
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
/*     */   @CanIgnoreReturnValue
/*     */   public final <C extends java.util.Collection<? super E>> C copyInto(C collection) {
/* 786 */     Preconditions.checkNotNull(collection);
/* 787 */     Iterable<E> iterable = getDelegate();
/* 788 */     if (iterable instanceof java.util.Collection) {
/* 789 */       collection.addAll(Collections2.cast(iterable));
/*     */     } else {
/* 791 */       for (E item : iterable) {
/* 792 */         collection.add(item);
/*     */       }
/*     */     } 
/* 795 */     return collection;
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
/*     */   public final String join(Joiner joiner) {
/* 810 */     return joiner.join(this);
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
/*     */   public final E get(int position) {
/* 827 */     return Iterables.get(getDelegate(), position);
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
/*     */   public final Stream<E> stream() {
/* 841 */     return Streams.stream(getDelegate());
/*     */   }
/*     */   
/*     */   private static class FromIterableFunction<E>
/*     */     implements Function<Iterable<E>, FluentIterable<E>>
/*     */   {
/*     */     public FluentIterable<E> apply(Iterable<E> fromObject) {
/* 848 */       return FluentIterable.from(fromObject);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\google\common\collect\FluentIterable.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */