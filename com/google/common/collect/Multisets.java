/*      */ package com.google.common.collect;
/*      */ 
/*      */ import com.google.common.annotations.Beta;
/*      */ import com.google.common.annotations.GwtCompatible;
/*      */ import com.google.common.base.Objects;
/*      */ import com.google.common.base.Preconditions;
/*      */ import com.google.common.base.Predicate;
/*      */ import com.google.common.base.Predicates;
/*      */ import com.google.common.math.IntMath;
/*      */ import com.google.common.primitives.Ints;
/*      */ import com.google.errorprone.annotations.CanIgnoreReturnValue;
/*      */ import java.io.Serializable;
/*      */ import java.util.Arrays;
/*      */ import java.util.Collection;
/*      */ import java.util.Collections;
/*      */ import java.util.Comparator;
/*      */ import java.util.Iterator;
/*      */ import java.util.NoSuchElementException;
/*      */ import java.util.Set;
/*      */ import java.util.Spliterator;
/*      */ import java.util.function.Function;
/*      */ import java.util.function.Supplier;
/*      */ import java.util.function.ToIntFunction;
/*      */ import java.util.stream.Collector;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ @GwtCompatible
/*      */ public final class Multisets
/*      */ {
/*      */   public static <T, E, M extends Multiset<E>> Collector<T, ?, M> toMultiset(Function<? super T, E> elementFunction, ToIntFunction<? super T> countFunction, Supplier<M> multisetSupplier) {
/*   80 */     Preconditions.checkNotNull(elementFunction);
/*   81 */     Preconditions.checkNotNull(countFunction);
/*   82 */     Preconditions.checkNotNull(multisetSupplier);
/*   83 */     return (Collector)Collector.of(multisetSupplier, (ms, t) -> ms.add(elementFunction.apply(t), countFunction.applyAsInt(t)), (ms1, ms2) -> { ms1.addAll(ms2); return ms1; }new Collector.Characteristics[0]);
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
/*      */   public static <E> Multiset<E> unmodifiableMultiset(Multiset<? extends E> multiset) {
/*  103 */     if (multiset instanceof UnmodifiableMultiset || multiset instanceof ImmutableMultiset)
/*      */     {
/*  105 */       return (Multiset)multiset;
/*      */     }
/*      */     
/*  108 */     return new UnmodifiableMultiset<>((Multiset<? extends E>)Preconditions.checkNotNull(multiset));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Deprecated
/*      */   public static <E> Multiset<E> unmodifiableMultiset(ImmutableMultiset<E> multiset) {
/*  119 */     return (Multiset<E>)Preconditions.checkNotNull(multiset);
/*      */   }
/*      */   static class UnmodifiableMultiset<E> extends ForwardingMultiset<E> implements Serializable { final Multiset<? extends E> delegate; transient Set<E> elementSet;
/*      */     transient Set<Multiset.Entry<E>> entrySet;
/*      */     private static final long serialVersionUID = 0L;
/*      */     
/*      */     UnmodifiableMultiset(Multiset<? extends E> delegate) {
/*  126 */       this.delegate = delegate;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     protected Multiset<E> delegate() {
/*  133 */       return (Multiset)this.delegate;
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     Set<E> createElementSet() {
/*  139 */       return Collections.unmodifiableSet(this.delegate.elementSet());
/*      */     }
/*      */ 
/*      */     
/*      */     public Set<E> elementSet() {
/*  144 */       Set<E> es = this.elementSet;
/*  145 */       return (es == null) ? (this.elementSet = createElementSet()) : es;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public Set<Multiset.Entry<E>> entrySet() {
/*  153 */       Set<Multiset.Entry<E>> es = this.entrySet;
/*  154 */       return (es == null) ? (this
/*      */ 
/*      */         
/*  157 */         .entrySet = Collections.unmodifiableSet(this.delegate.entrySet())) : es;
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     public Iterator<E> iterator() {
/*  163 */       return Iterators.unmodifiableIterator(this.delegate.iterator());
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean add(E element) {
/*  168 */       throw new UnsupportedOperationException();
/*      */     }
/*      */ 
/*      */     
/*      */     public int add(E element, int occurences) {
/*  173 */       throw new UnsupportedOperationException();
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean addAll(Collection<? extends E> elementsToAdd) {
/*  178 */       throw new UnsupportedOperationException();
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean remove(Object element) {
/*  183 */       throw new UnsupportedOperationException();
/*      */     }
/*      */ 
/*      */     
/*      */     public int remove(Object element, int occurrences) {
/*  188 */       throw new UnsupportedOperationException();
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean removeAll(Collection<?> elementsToRemove) {
/*  193 */       throw new UnsupportedOperationException();
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean retainAll(Collection<?> elementsToRetain) {
/*  198 */       throw new UnsupportedOperationException();
/*      */     }
/*      */ 
/*      */     
/*      */     public void clear() {
/*  203 */       throw new UnsupportedOperationException();
/*      */     }
/*      */ 
/*      */     
/*      */     public int setCount(E element, int count) {
/*  208 */       throw new UnsupportedOperationException();
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean setCount(E element, int oldCount, int newCount) {
/*  213 */       throw new UnsupportedOperationException();
/*      */     } }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
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
/*      */   public static <E> SortedMultiset<E> unmodifiableSortedMultiset(SortedMultiset<E> sortedMultiset) {
/*  233 */     return new UnmodifiableSortedMultiset<>((SortedMultiset<E>)Preconditions.checkNotNull(sortedMultiset));
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
/*      */   public static <E> Multiset.Entry<E> immutableEntry(E e, int n) {
/*  245 */     return new ImmutableEntry<>(e, n);
/*      */   }
/*      */   
/*      */   static class ImmutableEntry<E> extends AbstractEntry<E> implements Serializable { private final E element;
/*      */     private final int count;
/*      */     private static final long serialVersionUID = 0L;
/*      */     
/*      */     ImmutableEntry(E element, int count) {
/*  253 */       this.element = element;
/*  254 */       this.count = count;
/*  255 */       CollectPreconditions.checkNonnegative(count, "count");
/*      */     }
/*      */ 
/*      */     
/*      */     public final E getElement() {
/*  260 */       return this.element;
/*      */     }
/*      */ 
/*      */     
/*      */     public final int getCount() {
/*  265 */       return this.count;
/*      */     }
/*      */     
/*      */     public ImmutableEntry<E> nextInBucket() {
/*  269 */       return null;
/*      */     } }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
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
/*      */   public static <E> Multiset<E> filter(Multiset<E> unfiltered, Predicate<? super E> predicate) {
/*  302 */     if (unfiltered instanceof FilteredMultiset) {
/*      */ 
/*      */       
/*  305 */       FilteredMultiset<E> filtered = (FilteredMultiset<E>)unfiltered;
/*  306 */       Predicate<E> combinedPredicate = Predicates.and(filtered.predicate, predicate);
/*  307 */       return new FilteredMultiset<>(filtered.unfiltered, combinedPredicate);
/*      */     } 
/*  309 */     return new FilteredMultiset<>(unfiltered, predicate);
/*      */   }
/*      */   
/*      */   private static final class FilteredMultiset<E> extends ViewMultiset<E> {
/*      */     final Multiset<E> unfiltered;
/*      */     final Predicate<? super E> predicate;
/*      */     
/*      */     FilteredMultiset(Multiset<E> unfiltered, Predicate<? super E> predicate) {
/*  317 */       this.unfiltered = (Multiset<E>)Preconditions.checkNotNull(unfiltered);
/*  318 */       this.predicate = (Predicate<? super E>)Preconditions.checkNotNull(predicate);
/*      */     }
/*      */ 
/*      */     
/*      */     public UnmodifiableIterator<E> iterator() {
/*  323 */       return Iterators.filter(this.unfiltered.iterator(), this.predicate);
/*      */     }
/*      */ 
/*      */     
/*      */     Set<E> createElementSet() {
/*  328 */       return Sets.filter(this.unfiltered.elementSet(), this.predicate);
/*      */     }
/*      */ 
/*      */     
/*      */     Iterator<E> elementIterator() {
/*  333 */       throw new AssertionError("should never be called");
/*      */     }
/*      */ 
/*      */     
/*      */     Set<Multiset.Entry<E>> createEntrySet() {
/*  338 */       return Sets.filter(this.unfiltered
/*  339 */           .entrySet(), new Predicate<Multiset.Entry<E>>()
/*      */           {
/*      */             public boolean apply(Multiset.Entry<E> entry)
/*      */             {
/*  343 */               return Multisets.FilteredMultiset.this.predicate.apply(entry.getElement());
/*      */             }
/*      */           });
/*      */     }
/*      */ 
/*      */     
/*      */     Iterator<Multiset.Entry<E>> entryIterator() {
/*  350 */       throw new AssertionError("should never be called");
/*      */     }
/*      */ 
/*      */     
/*      */     public int count(Object element) {
/*  355 */       int count = this.unfiltered.count(element);
/*  356 */       if (count > 0) {
/*      */         
/*  358 */         E e = (E)element;
/*  359 */         return this.predicate.apply(e) ? count : 0;
/*      */       } 
/*  361 */       return 0;
/*      */     }
/*      */ 
/*      */     
/*      */     public int add(E element, int occurrences) {
/*  366 */       Preconditions.checkArgument(this.predicate
/*  367 */           .apply(element), "Element %s does not match predicate %s", element, this.predicate);
/*  368 */       return this.unfiltered.add(element, occurrences);
/*      */     }
/*      */ 
/*      */     
/*      */     public int remove(Object element, int occurrences) {
/*  373 */       CollectPreconditions.checkNonnegative(occurrences, "occurrences");
/*  374 */       if (occurrences == 0) {
/*  375 */         return count(element);
/*      */       }
/*  377 */       return contains(element) ? this.unfiltered.remove(element, occurrences) : 0;
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   static int inferDistinctElements(Iterable<?> elements) {
/*  388 */     if (elements instanceof Multiset) {
/*  389 */       return ((Multiset)elements).elementSet().size();
/*      */     }
/*  391 */     return 11;
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
/*      */   @Beta
/*      */   public static <E> Multiset<E> union(final Multiset<? extends E> multiset1, final Multiset<? extends E> multiset2) {
/*  409 */     Preconditions.checkNotNull(multiset1);
/*  410 */     Preconditions.checkNotNull(multiset2);
/*      */     
/*  412 */     return new ViewMultiset<E>()
/*      */       {
/*      */         public boolean contains(Object element) {
/*  415 */           return (multiset1.contains(element) || multiset2.contains(element));
/*      */         }
/*      */ 
/*      */         
/*      */         public boolean isEmpty() {
/*  420 */           return (multiset1.isEmpty() && multiset2.isEmpty());
/*      */         }
/*      */ 
/*      */         
/*      */         public int count(Object element) {
/*  425 */           return Math.max(multiset1.count(element), multiset2.count(element));
/*      */         }
/*      */ 
/*      */         
/*      */         Set<E> createElementSet() {
/*  430 */           return Sets.union(multiset1.elementSet(), multiset2.elementSet());
/*      */         }
/*      */ 
/*      */         
/*      */         Iterator<E> elementIterator() {
/*  435 */           throw new AssertionError("should never be called");
/*      */         }
/*      */ 
/*      */         
/*      */         Iterator<Multiset.Entry<E>> entryIterator() {
/*  440 */           final Iterator<? extends Multiset.Entry<? extends E>> iterator1 = multiset1.entrySet().iterator();
/*  441 */           final Iterator<? extends Multiset.Entry<? extends E>> iterator2 = multiset2.entrySet().iterator();
/*      */           
/*  443 */           return new AbstractIterator()
/*      */             {
/*      */               protected Multiset.Entry<E> computeNext() {
/*  446 */                 if (iterator1.hasNext()) {
/*  447 */                   Multiset.Entry<? extends E> entry1 = iterator1.next();
/*  448 */                   E element = entry1.getElement();
/*  449 */                   int count = Math.max(entry1.getCount(), multiset2.count(element));
/*  450 */                   return Multisets.immutableEntry(element, count);
/*      */                 } 
/*  452 */                 while (iterator2.hasNext()) {
/*  453 */                   Multiset.Entry<? extends E> entry2 = iterator2.next();
/*  454 */                   E element = entry2.getElement();
/*  455 */                   if (!multiset1.contains(element)) {
/*  456 */                     return Multisets.immutableEntry(element, entry2.getCount());
/*      */                   }
/*      */                 } 
/*  459 */                 return endOfData();
/*      */               }
/*      */             };
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
/*      */   public static <E> Multiset<E> intersection(final Multiset<E> multiset1, final Multiset<?> multiset2) {
/*  480 */     Preconditions.checkNotNull(multiset1);
/*  481 */     Preconditions.checkNotNull(multiset2);
/*      */     
/*  483 */     return new ViewMultiset<E>()
/*      */       {
/*      */         public int count(Object element) {
/*  486 */           int count1 = multiset1.count(element);
/*  487 */           return (count1 == 0) ? 0 : Math.min(count1, multiset2.count(element));
/*      */         }
/*      */ 
/*      */         
/*      */         Set<E> createElementSet() {
/*  492 */           return Sets.intersection(multiset1.elementSet(), multiset2.elementSet());
/*      */         }
/*      */ 
/*      */         
/*      */         Iterator<E> elementIterator() {
/*  497 */           throw new AssertionError("should never be called");
/*      */         }
/*      */ 
/*      */         
/*      */         Iterator<Multiset.Entry<E>> entryIterator() {
/*  502 */           final Iterator<Multiset.Entry<E>> iterator1 = multiset1.entrySet().iterator();
/*      */           
/*  504 */           return new AbstractIterator()
/*      */             {
/*      */               protected Multiset.Entry<E> computeNext() {
/*  507 */                 while (iterator1.hasNext()) {
/*  508 */                   Multiset.Entry<E> entry1 = iterator1.next();
/*  509 */                   E element = entry1.getElement();
/*  510 */                   int count = Math.min(entry1.getCount(), multiset2.count(element));
/*  511 */                   if (count > 0) {
/*  512 */                     return Multisets.immutableEntry(element, count);
/*      */                   }
/*      */                 } 
/*  515 */                 return endOfData();
/*      */               }
/*      */             };
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
/*      */   @Beta
/*      */   public static <E> Multiset<E> sum(final Multiset<? extends E> multiset1, final Multiset<? extends E> multiset2) {
/*  537 */     Preconditions.checkNotNull(multiset1);
/*  538 */     Preconditions.checkNotNull(multiset2);
/*      */ 
/*      */     
/*  541 */     return new ViewMultiset<E>()
/*      */       {
/*      */         public boolean contains(Object element) {
/*  544 */           return (multiset1.contains(element) || multiset2.contains(element));
/*      */         }
/*      */ 
/*      */         
/*      */         public boolean isEmpty() {
/*  549 */           return (multiset1.isEmpty() && multiset2.isEmpty());
/*      */         }
/*      */ 
/*      */         
/*      */         public int size() {
/*  554 */           return IntMath.saturatedAdd(multiset1.size(), multiset2.size());
/*      */         }
/*      */ 
/*      */         
/*      */         public int count(Object element) {
/*  559 */           return multiset1.count(element) + multiset2.count(element);
/*      */         }
/*      */ 
/*      */         
/*      */         Set<E> createElementSet() {
/*  564 */           return Sets.union(multiset1.elementSet(), multiset2.elementSet());
/*      */         }
/*      */ 
/*      */         
/*      */         Iterator<E> elementIterator() {
/*  569 */           throw new AssertionError("should never be called");
/*      */         }
/*      */ 
/*      */         
/*      */         Iterator<Multiset.Entry<E>> entryIterator() {
/*  574 */           final Iterator<? extends Multiset.Entry<? extends E>> iterator1 = multiset1.entrySet().iterator();
/*  575 */           final Iterator<? extends Multiset.Entry<? extends E>> iterator2 = multiset2.entrySet().iterator();
/*  576 */           return new AbstractIterator()
/*      */             {
/*      */               protected Multiset.Entry<E> computeNext() {
/*  579 */                 if (iterator1.hasNext()) {
/*  580 */                   Multiset.Entry<? extends E> entry1 = iterator1.next();
/*  581 */                   E element = entry1.getElement();
/*  582 */                   int count = entry1.getCount() + multiset2.count(element);
/*  583 */                   return Multisets.immutableEntry(element, count);
/*      */                 } 
/*  585 */                 while (iterator2.hasNext()) {
/*  586 */                   Multiset.Entry<? extends E> entry2 = iterator2.next();
/*  587 */                   E element = entry2.getElement();
/*  588 */                   if (!multiset1.contains(element)) {
/*  589 */                     return Multisets.immutableEntry(element, entry2.getCount());
/*      */                   }
/*      */                 } 
/*  592 */                 return endOfData();
/*      */               }
/*      */             };
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
/*      */   @Beta
/*      */   public static <E> Multiset<E> difference(final Multiset<E> multiset1, final Multiset<?> multiset2) {
/*  614 */     Preconditions.checkNotNull(multiset1);
/*  615 */     Preconditions.checkNotNull(multiset2);
/*      */ 
/*      */     
/*  618 */     return new ViewMultiset<E>()
/*      */       {
/*      */         public int count(Object element) {
/*  621 */           int count1 = multiset1.count(element);
/*  622 */           return (count1 == 0) ? 0 : Math.max(0, count1 - multiset2.count(element));
/*      */         }
/*      */ 
/*      */         
/*      */         public void clear() {
/*  627 */           throw new UnsupportedOperationException();
/*      */         }
/*      */ 
/*      */         
/*      */         Iterator<E> elementIterator() {
/*  632 */           final Iterator<Multiset.Entry<E>> iterator1 = multiset1.entrySet().iterator();
/*  633 */           return new AbstractIterator()
/*      */             {
/*      */               protected E computeNext() {
/*  636 */                 while (iterator1.hasNext()) {
/*  637 */                   Multiset.Entry<E> entry1 = iterator1.next();
/*  638 */                   E element = entry1.getElement();
/*  639 */                   if (entry1.getCount() > multiset2.count(element)) {
/*  640 */                     return element;
/*      */                   }
/*      */                 } 
/*  643 */                 return endOfData();
/*      */               }
/*      */             };
/*      */         }
/*      */ 
/*      */         
/*      */         Iterator<Multiset.Entry<E>> entryIterator() {
/*  650 */           final Iterator<Multiset.Entry<E>> iterator1 = multiset1.entrySet().iterator();
/*  651 */           return new AbstractIterator()
/*      */             {
/*      */               protected Multiset.Entry<E> computeNext() {
/*  654 */                 while (iterator1.hasNext()) {
/*  655 */                   Multiset.Entry<E> entry1 = iterator1.next();
/*  656 */                   E element = entry1.getElement();
/*  657 */                   int count = entry1.getCount() - multiset2.count(element);
/*  658 */                   if (count > 0) {
/*  659 */                     return Multisets.immutableEntry(element, count);
/*      */                   }
/*      */                 } 
/*  662 */                 return endOfData();
/*      */               }
/*      */             };
/*      */         }
/*      */ 
/*      */         
/*      */         int distinctElements() {
/*  669 */           return Iterators.size(entryIterator());
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
/*      */   @CanIgnoreReturnValue
/*      */   public static boolean containsOccurrences(Multiset<?> superMultiset, Multiset<?> subMultiset) {
/*  682 */     Preconditions.checkNotNull(superMultiset);
/*  683 */     Preconditions.checkNotNull(subMultiset);
/*  684 */     for (Multiset.Entry<?> entry : subMultiset.entrySet()) {
/*  685 */       int superCount = superMultiset.count(entry.getElement());
/*  686 */       if (superCount < entry.getCount()) {
/*  687 */         return false;
/*      */       }
/*      */     } 
/*  690 */     return true;
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
/*      */   @CanIgnoreReturnValue
/*      */   public static boolean retainOccurrences(Multiset<?> multisetToModify, Multiset<?> multisetToRetain) {
/*  712 */     return retainOccurrencesImpl(multisetToModify, multisetToRetain);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private static <E> boolean retainOccurrencesImpl(Multiset<E> multisetToModify, Multiset<?> occurrencesToRetain) {
/*  718 */     Preconditions.checkNotNull(multisetToModify);
/*  719 */     Preconditions.checkNotNull(occurrencesToRetain);
/*      */     
/*  721 */     Iterator<Multiset.Entry<E>> entryIterator = multisetToModify.entrySet().iterator();
/*  722 */     boolean changed = false;
/*  723 */     while (entryIterator.hasNext()) {
/*  724 */       Multiset.Entry<E> entry = entryIterator.next();
/*  725 */       int retainCount = occurrencesToRetain.count(entry.getElement());
/*  726 */       if (retainCount == 0) {
/*  727 */         entryIterator.remove();
/*  728 */         changed = true; continue;
/*  729 */       }  if (retainCount < entry.getCount()) {
/*  730 */         multisetToModify.setCount(entry.getElement(), retainCount);
/*  731 */         changed = true;
/*      */       } 
/*      */     } 
/*  734 */     return changed;
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
/*      */   @CanIgnoreReturnValue
/*      */   public static boolean removeOccurrences(Multiset<?> multisetToModify, Iterable<?> occurrencesToRemove) {
/*  763 */     if (occurrencesToRemove instanceof Multiset) {
/*  764 */       return removeOccurrences(multisetToModify, (Multiset)occurrencesToRemove);
/*      */     }
/*  766 */     Preconditions.checkNotNull(multisetToModify);
/*  767 */     Preconditions.checkNotNull(occurrencesToRemove);
/*  768 */     boolean changed = false;
/*  769 */     for (Object o : occurrencesToRemove) {
/*  770 */       changed |= multisetToModify.remove(o);
/*      */     }
/*  772 */     return changed;
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
/*      */   @CanIgnoreReturnValue
/*      */   public static boolean removeOccurrences(Multiset<?> multisetToModify, Multiset<?> occurrencesToRemove) {
/*  801 */     Preconditions.checkNotNull(multisetToModify);
/*  802 */     Preconditions.checkNotNull(occurrencesToRemove);
/*      */     
/*  804 */     boolean changed = false;
/*  805 */     Iterator<? extends Multiset.Entry<?>> entryIterator = multisetToModify.entrySet().iterator();
/*  806 */     while (entryIterator.hasNext()) {
/*  807 */       Multiset.Entry<?> entry = entryIterator.next();
/*  808 */       int removeCount = occurrencesToRemove.count(entry.getElement());
/*  809 */       if (removeCount >= entry.getCount()) {
/*  810 */         entryIterator.remove();
/*  811 */         changed = true; continue;
/*  812 */       }  if (removeCount > 0) {
/*  813 */         multisetToModify.remove(entry.getElement(), removeCount);
/*  814 */         changed = true;
/*      */       } 
/*      */     } 
/*  817 */     return changed;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   static abstract class AbstractEntry<E>
/*      */     implements Multiset.Entry<E>
/*      */   {
/*      */     public boolean equals(Object object) {
/*  831 */       if (object instanceof Multiset.Entry) {
/*  832 */         Multiset.Entry<?> that = (Multiset.Entry)object;
/*  833 */         return (getCount() == that.getCount() && 
/*  834 */           Objects.equal(getElement(), that.getElement()));
/*      */       } 
/*  836 */       return false;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public int hashCode() {
/*  845 */       E e = getElement();
/*  846 */       return ((e == null) ? 0 : e.hashCode()) ^ getCount();
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public String toString() {
/*  857 */       String text = String.valueOf(getElement());
/*  858 */       int n = getCount();
/*  859 */       return (n == 1) ? text : (text + " x " + n);
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   static boolean equalsImpl(Multiset<?> multiset, Object object) {
/*  865 */     if (object == multiset) {
/*  866 */       return true;
/*      */     }
/*  868 */     if (object instanceof Multiset) {
/*  869 */       Multiset<?> that = (Multiset)object;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*  876 */       if (multiset.size() != that.size() || multiset.entrySet().size() != that.entrySet().size()) {
/*  877 */         return false;
/*      */       }
/*  879 */       for (Multiset.Entry<?> entry : that.entrySet()) {
/*  880 */         if (multiset.count(entry.getElement()) != entry.getCount()) {
/*  881 */           return false;
/*      */         }
/*      */       } 
/*  884 */       return true;
/*      */     } 
/*  886 */     return false;
/*      */   }
/*      */ 
/*      */   
/*      */   static <E> boolean addAllImpl(Multiset<E> self, Collection<? extends E> elements) {
/*  891 */     Preconditions.checkNotNull(self);
/*  892 */     Preconditions.checkNotNull(elements);
/*  893 */     if (elements instanceof Multiset)
/*  894 */       return addAllImpl(self, cast(elements)); 
/*  895 */     if (elements.isEmpty()) {
/*  896 */       return false;
/*      */     }
/*  898 */     return Iterators.addAll(self, elements.iterator());
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private static <E> boolean addAllImpl(Multiset<E> self, Multiset<? extends E> elements) {
/*  904 */     if (elements.isEmpty()) {
/*  905 */       return false;
/*      */     }
/*  907 */     elements.forEachEntry(self::add);
/*  908 */     return true;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   static boolean removeAllImpl(Multiset<?> self, Collection<?> elementsToRemove) {
/*  915 */     Collection<?> collection = (elementsToRemove instanceof Multiset) ? ((Multiset)elementsToRemove).elementSet() : elementsToRemove;
/*      */ 
/*      */     
/*  918 */     return self.elementSet().removeAll(collection);
/*      */   }
/*      */ 
/*      */   
/*      */   static boolean retainAllImpl(Multiset<?> self, Collection<?> elementsToRetain) {
/*  923 */     Preconditions.checkNotNull(elementsToRetain);
/*      */ 
/*      */     
/*  926 */     Collection<?> collection = (elementsToRetain instanceof Multiset) ? ((Multiset)elementsToRetain).elementSet() : elementsToRetain;
/*      */ 
/*      */     
/*  929 */     return self.elementSet().retainAll(collection);
/*      */   }
/*      */ 
/*      */   
/*      */   static <E> int setCountImpl(Multiset<E> self, E element, int count) {
/*  934 */     CollectPreconditions.checkNonnegative(count, "count");
/*      */     
/*  936 */     int oldCount = self.count(element);
/*      */     
/*  938 */     int delta = count - oldCount;
/*  939 */     if (delta > 0) {
/*  940 */       self.add(element, delta);
/*  941 */     } else if (delta < 0) {
/*  942 */       self.remove(element, -delta);
/*      */     } 
/*      */     
/*  945 */     return oldCount;
/*      */   }
/*      */ 
/*      */   
/*      */   static <E> boolean setCountImpl(Multiset<E> self, E element, int oldCount, int newCount) {
/*  950 */     CollectPreconditions.checkNonnegative(oldCount, "oldCount");
/*  951 */     CollectPreconditions.checkNonnegative(newCount, "newCount");
/*      */     
/*  953 */     if (self.count(element) == oldCount) {
/*  954 */       self.setCount(element, newCount);
/*  955 */       return true;
/*      */     } 
/*  957 */     return false;
/*      */   }
/*      */ 
/*      */   
/*      */   static <E> Iterator<E> elementIterator(Iterator<Multiset.Entry<E>> entryIterator) {
/*  962 */     return new TransformedIterator<Multiset.Entry<E>, E>(entryIterator)
/*      */       {
/*      */         E transform(Multiset.Entry<E> entry) {
/*  965 */           return entry.getElement();
/*      */         }
/*      */       };
/*      */   }
/*      */   
/*      */   static abstract class ElementSet<E>
/*      */     extends Sets.ImprovedAbstractSet<E> {
/*      */     abstract Multiset<E> multiset();
/*      */     
/*      */     public void clear() {
/*  975 */       multiset().clear();
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean contains(Object o) {
/*  980 */       return multiset().contains(o);
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean containsAll(Collection<?> c) {
/*  985 */       return multiset().containsAll(c);
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean isEmpty() {
/*  990 */       return multiset().isEmpty();
/*      */     }
/*      */ 
/*      */     
/*      */     public abstract Iterator<E> iterator();
/*      */ 
/*      */     
/*      */     public boolean remove(Object o) {
/*  998 */       return (multiset().remove(o, 2147483647) > 0);
/*      */     }
/*      */ 
/*      */     
/*      */     public int size() {
/* 1003 */       return multiset().entrySet().size();
/*      */     }
/*      */   }
/*      */   
/*      */   static abstract class EntrySet<E>
/*      */     extends Sets.ImprovedAbstractSet<Multiset.Entry<E>> {
/*      */     abstract Multiset<E> multiset();
/*      */     
/*      */     public boolean contains(Object o) {
/* 1012 */       if (o instanceof Multiset.Entry) {
/*      */ 
/*      */ 
/*      */ 
/*      */         
/* 1017 */         Multiset.Entry<?> entry = (Multiset.Entry)o;
/* 1018 */         if (entry.getCount() <= 0) {
/* 1019 */           return false;
/*      */         }
/* 1021 */         int count = multiset().count(entry.getElement());
/* 1022 */         return (count == entry.getCount());
/*      */       } 
/* 1024 */       return false;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public boolean remove(Object object) {
/* 1031 */       if (object instanceof Multiset.Entry) {
/* 1032 */         Multiset.Entry<?> entry = (Multiset.Entry)object;
/* 1033 */         Object element = entry.getElement();
/* 1034 */         int entryCount = entry.getCount();
/* 1035 */         if (entryCount != 0) {
/*      */ 
/*      */           
/* 1038 */           Multiset<Object> multiset = (Multiset)multiset();
/* 1039 */           return multiset.setCount(element, entryCount, 0);
/*      */         } 
/*      */       } 
/* 1042 */       return false;
/*      */     }
/*      */ 
/*      */     
/*      */     public void clear() {
/* 1047 */       multiset().clear();
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   static <E> Iterator<E> iteratorImpl(Multiset<E> multiset) {
/* 1053 */     return new MultisetIteratorImpl<>(multiset, multiset.entrySet().iterator());
/*      */   }
/*      */ 
/*      */   
/*      */   static final class MultisetIteratorImpl<E>
/*      */     implements Iterator<E>
/*      */   {
/*      */     private final Multiset<E> multiset;
/*      */     
/*      */     private final Iterator<Multiset.Entry<E>> entryIterator;
/*      */     
/*      */     private Multiset.Entry<E> currentEntry;
/*      */     private int laterCount;
/*      */     private int totalCount;
/*      */     private boolean canRemove;
/*      */     
/*      */     MultisetIteratorImpl(Multiset<E> multiset, Iterator<Multiset.Entry<E>> entryIterator) {
/* 1070 */       this.multiset = multiset;
/* 1071 */       this.entryIterator = entryIterator;
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean hasNext() {
/* 1076 */       return (this.laterCount > 0 || this.entryIterator.hasNext());
/*      */     }
/*      */ 
/*      */     
/*      */     public E next() {
/* 1081 */       if (!hasNext()) {
/* 1082 */         throw new NoSuchElementException();
/*      */       }
/* 1084 */       if (this.laterCount == 0) {
/* 1085 */         this.currentEntry = this.entryIterator.next();
/* 1086 */         this.totalCount = this.laterCount = this.currentEntry.getCount();
/*      */       } 
/* 1088 */       this.laterCount--;
/* 1089 */       this.canRemove = true;
/* 1090 */       return this.currentEntry.getElement();
/*      */     }
/*      */ 
/*      */     
/*      */     public void remove() {
/* 1095 */       CollectPreconditions.checkRemove(this.canRemove);
/* 1096 */       if (this.totalCount == 1) {
/* 1097 */         this.entryIterator.remove();
/*      */       } else {
/* 1099 */         this.multiset.remove(this.currentEntry.getElement());
/*      */       } 
/* 1101 */       this.totalCount--;
/* 1102 */       this.canRemove = false;
/*      */     }
/*      */   }
/*      */   
/*      */   static <E> Spliterator<E> spliteratorImpl(Multiset<E> multiset) {
/* 1107 */     Spliterator<Multiset.Entry<E>> entrySpliterator = multiset.entrySet().spliterator();
/* 1108 */     return CollectSpliterators.flatMap(entrySpliterator, entry -> Collections.nCopies(entry.getCount(), entry.getElement()).spliterator(), 0x40 | entrySpliterator
/*      */ 
/*      */ 
/*      */         
/* 1112 */         .characteristics() & 0x510, multiset
/*      */         
/* 1114 */         .size());
/*      */   }
/*      */ 
/*      */   
/*      */   static int linearTimeSizeImpl(Multiset<?> multiset) {
/* 1119 */     long size = 0L;
/* 1120 */     for (Multiset.Entry<?> entry : multiset.entrySet()) {
/* 1121 */       size += entry.getCount();
/*      */     }
/* 1123 */     return Ints.saturatedCast(size);
/*      */   }
/*      */ 
/*      */   
/*      */   static <T> Multiset<T> cast(Iterable<T> iterable) {
/* 1128 */     return (Multiset<T>)iterable;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Beta
/*      */   public static <E> ImmutableMultiset<E> copyHighestCountFirst(Multiset<E> multiset) {
/* 1139 */     Multiset.Entry[] arrayOfEntry = (Multiset.Entry[])multiset.entrySet().toArray((Object[])new Multiset.Entry[0]);
/* 1140 */     Arrays.sort(arrayOfEntry, DecreasingCount.INSTANCE);
/* 1141 */     return ImmutableMultiset.copyFromEntries(Arrays.asList((Multiset.Entry<? extends E>[])arrayOfEntry));
/*      */   }
/*      */   
/*      */   private static final class DecreasingCount implements Comparator<Multiset.Entry<?>> {
/* 1145 */     static final DecreasingCount INSTANCE = new DecreasingCount();
/*      */ 
/*      */     
/*      */     public int compare(Multiset.Entry<?> entry1, Multiset.Entry<?> entry2) {
/* 1149 */       return entry2.getCount() - entry1.getCount();
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   private static abstract class ViewMultiset<E>
/*      */     extends AbstractMultiset<E>
/*      */   {
/*      */     private ViewMultiset() {}
/*      */     
/*      */     public int size() {
/* 1160 */       return Multisets.linearTimeSizeImpl(this);
/*      */     }
/*      */ 
/*      */     
/*      */     public void clear() {
/* 1165 */       elementSet().clear();
/*      */     }
/*      */ 
/*      */     
/*      */     public Iterator<E> iterator() {
/* 1170 */       return Multisets.iteratorImpl(this);
/*      */     }
/*      */ 
/*      */     
/*      */     int distinctElements() {
/* 1175 */       return elementSet().size();
/*      */     }
/*      */   }
/*      */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\google\common\collect\Multisets.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */