/*      */ package com.google.common.collect;
/*      */ 
/*      */ import com.google.common.annotations.Beta;
/*      */ import com.google.common.annotations.GwtCompatible;
/*      */ import com.google.common.annotations.GwtIncompatible;
/*      */ import com.google.common.base.Function;
/*      */ import com.google.common.base.Objects;
/*      */ import com.google.common.base.Optional;
/*      */ import com.google.common.base.Preconditions;
/*      */ import com.google.common.base.Predicate;
/*      */ import com.google.common.base.Predicates;
/*      */ import com.google.common.primitives.Ints;
/*      */ import com.google.errorprone.annotations.CanIgnoreReturnValue;
/*      */ import java.util.ArrayDeque;
/*      */ import java.util.Arrays;
/*      */ import java.util.Collection;
/*      */ import java.util.Collections;
/*      */ import java.util.Comparator;
/*      */ import java.util.Deque;
/*      */ import java.util.Enumeration;
/*      */ import java.util.Iterator;
/*      */ import java.util.List;
/*      */ import java.util.ListIterator;
/*      */ import java.util.NoSuchElementException;
/*      */ import java.util.PriorityQueue;
/*      */ import java.util.Queue;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
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
/*      */ public final class Iterators
/*      */ {
/*      */   static <T> UnmodifiableIterator<T> emptyIterator() {
/*   77 */     return emptyListIterator();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   static <T> UnmodifiableListIterator<T> emptyListIterator() {
/*   88 */     return (UnmodifiableListIterator)ArrayItr.EMPTY;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private enum EmptyModifiableIterator
/*      */     implements Iterator<Object>
/*      */   {
/*   96 */     INSTANCE;
/*      */ 
/*      */     
/*      */     public boolean hasNext() {
/*  100 */       return false;
/*      */     }
/*      */ 
/*      */     
/*      */     public Object next() {
/*  105 */       throw new NoSuchElementException();
/*      */     }
/*      */ 
/*      */     
/*      */     public void remove() {
/*  110 */       CollectPreconditions.checkRemove(false);
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   static <T> Iterator<T> emptyModifiableIterator() {
/*  121 */     return EmptyModifiableIterator.INSTANCE;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public static <T> UnmodifiableIterator<T> unmodifiableIterator(final Iterator<? extends T> iterator) {
/*  127 */     Preconditions.checkNotNull(iterator);
/*  128 */     if (iterator instanceof UnmodifiableIterator) {
/*      */       
/*  130 */       UnmodifiableIterator<T> result = (UnmodifiableIterator)iterator;
/*  131 */       return result;
/*      */     } 
/*  133 */     return new UnmodifiableIterator<T>()
/*      */       {
/*      */         public boolean hasNext() {
/*  136 */           return iterator.hasNext();
/*      */         }
/*      */ 
/*      */         
/*      */         public T next() {
/*  141 */           return iterator.next();
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
/*      */   @Deprecated
/*      */   public static <T> UnmodifiableIterator<T> unmodifiableIterator(UnmodifiableIterator<T> iterator) {
/*  154 */     return (UnmodifiableIterator<T>)Preconditions.checkNotNull(iterator);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static int size(Iterator<?> iterator) {
/*  162 */     long count = 0L;
/*  163 */     while (iterator.hasNext()) {
/*  164 */       iterator.next();
/*  165 */       count++;
/*      */     } 
/*  167 */     return Ints.saturatedCast(count);
/*      */   }
/*      */ 
/*      */   
/*      */   public static boolean contains(Iterator<?> iterator, Object element) {
/*  172 */     if (element == null) {
/*  173 */       while (iterator.hasNext()) {
/*  174 */         if (iterator.next() == null) {
/*  175 */           return true;
/*      */         }
/*      */       } 
/*      */     } else {
/*  179 */       while (iterator.hasNext()) {
/*  180 */         if (element.equals(iterator.next())) {
/*  181 */           return true;
/*      */         }
/*      */       } 
/*      */     } 
/*  185 */     return false;
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
/*      */   @CanIgnoreReturnValue
/*      */   public static boolean removeAll(Iterator<?> removeFrom, Collection<?> elementsToRemove) {
/*  198 */     Preconditions.checkNotNull(elementsToRemove);
/*  199 */     boolean result = false;
/*  200 */     while (removeFrom.hasNext()) {
/*  201 */       if (elementsToRemove.contains(removeFrom.next())) {
/*  202 */         removeFrom.remove();
/*  203 */         result = true;
/*      */       } 
/*      */     } 
/*  206 */     return result;
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
/*      */   @CanIgnoreReturnValue
/*      */   public static <T> boolean removeIf(Iterator<T> removeFrom, Predicate<? super T> predicate) {
/*  220 */     Preconditions.checkNotNull(predicate);
/*  221 */     boolean modified = false;
/*  222 */     while (removeFrom.hasNext()) {
/*  223 */       if (predicate.apply(removeFrom.next())) {
/*  224 */         removeFrom.remove();
/*  225 */         modified = true;
/*      */       } 
/*      */     } 
/*  228 */     return modified;
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
/*      */   @CanIgnoreReturnValue
/*      */   public static boolean retainAll(Iterator<?> removeFrom, Collection<?> elementsToRetain) {
/*  242 */     Preconditions.checkNotNull(elementsToRetain);
/*  243 */     boolean result = false;
/*  244 */     while (removeFrom.hasNext()) {
/*  245 */       if (!elementsToRetain.contains(removeFrom.next())) {
/*  246 */         removeFrom.remove();
/*  247 */         result = true;
/*      */       } 
/*      */     } 
/*  250 */     return result;
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
/*      */   public static boolean elementsEqual(Iterator<?> iterator1, Iterator<?> iterator2) {
/*  263 */     while (iterator1.hasNext()) {
/*  264 */       if (!iterator2.hasNext()) {
/*  265 */         return false;
/*      */       }
/*  267 */       Object o1 = iterator1.next();
/*  268 */       Object o2 = iterator2.next();
/*  269 */       if (!Objects.equal(o1, o2)) {
/*  270 */         return false;
/*      */       }
/*      */     } 
/*  273 */     return !iterator2.hasNext();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static String toString(Iterator<?> iterator) {
/*  281 */     StringBuilder sb = (new StringBuilder()).append('[');
/*  282 */     boolean first = true;
/*  283 */     while (iterator.hasNext()) {
/*  284 */       if (!first) {
/*  285 */         sb.append(", ");
/*      */       }
/*  287 */       first = false;
/*  288 */       sb.append(iterator.next());
/*      */     } 
/*  290 */     return sb.append(']').toString();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static <T> T getOnlyElement(Iterator<T> iterator) {
/*  301 */     T first = iterator.next();
/*  302 */     if (!iterator.hasNext()) {
/*  303 */       return first;
/*      */     }
/*      */     
/*  306 */     StringBuilder sb = (new StringBuilder()).append("expected one element but was: <").append(first);
/*  307 */     for (int i = 0; i < 4 && iterator.hasNext(); i++) {
/*  308 */       sb.append(", ").append(iterator.next());
/*      */     }
/*  310 */     if (iterator.hasNext()) {
/*  311 */       sb.append(", ...");
/*      */     }
/*  313 */     sb.append('>');
/*      */     
/*  315 */     throw new IllegalArgumentException(sb.toString());
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
/*      */   public static <T> T getOnlyElement(Iterator<? extends T> iterator, T defaultValue) {
/*  327 */     return iterator.hasNext() ? getOnlyElement((Iterator)iterator) : defaultValue;
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
/*      */   @GwtIncompatible
/*      */   public static <T> T[] toArray(Iterator<? extends T> iterator, Class<T> type) {
/*  340 */     List<T> list = Lists.newArrayList(iterator);
/*  341 */     return Iterables.toArray(list, type);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @CanIgnoreReturnValue
/*      */   public static <T> boolean addAll(Collection<T> addTo, Iterator<? extends T> iterator) {
/*  352 */     Preconditions.checkNotNull(addTo);
/*  353 */     Preconditions.checkNotNull(iterator);
/*  354 */     boolean wasModified = false;
/*  355 */     while (iterator.hasNext()) {
/*  356 */       wasModified |= addTo.add(iterator.next());
/*      */     }
/*  358 */     return wasModified;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static int frequency(Iterator<?> iterator, Object element) {
/*  368 */     int count = 0;
/*  369 */     while (contains(iterator, element))
/*      */     {
/*      */       
/*  372 */       count++;
/*      */     }
/*  374 */     return count;
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
/*      */   public static <T> Iterator<T> cycle(final Iterable<T> iterable) {
/*  390 */     Preconditions.checkNotNull(iterable);
/*  391 */     return new Iterator<T>() {
/*  392 */         Iterator<T> iterator = Iterators.emptyModifiableIterator();
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */         
/*      */         public boolean hasNext() {
/*  405 */           return (this.iterator.hasNext() || iterable.iterator().hasNext());
/*      */         }
/*      */ 
/*      */         
/*      */         public T next() {
/*  410 */           if (!this.iterator.hasNext()) {
/*  411 */             this.iterator = iterable.iterator();
/*  412 */             if (!this.iterator.hasNext()) {
/*  413 */               throw new NoSuchElementException();
/*      */             }
/*      */           } 
/*  416 */           return this.iterator.next();
/*      */         }
/*      */ 
/*      */         
/*      */         public void remove() {
/*  421 */           this.iterator.remove();
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
/*      */   @SafeVarargs
/*      */   public static <T> Iterator<T> cycle(T... elements) {
/*  440 */     return cycle(Lists.newArrayList(elements));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static <T> Iterator<T> consumingForArray(T... elements) {
/*  450 */     return new UnmodifiableIterator<T>() {
/*  451 */         int index = 0;
/*      */ 
/*      */         
/*      */         public boolean hasNext() {
/*  455 */           return (this.index < elements.length);
/*      */         }
/*      */ 
/*      */         
/*      */         public T next() {
/*  460 */           if (!hasNext()) {
/*  461 */             throw new NoSuchElementException();
/*      */           }
/*  463 */           T result = (T)elements[this.index];
/*  464 */           elements[this.index] = null;
/*  465 */           this.index++;
/*  466 */           return result;
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
/*      */   public static <T> Iterator<T> concat(Iterator<? extends T> a, Iterator<? extends T> b) {
/*  480 */     Preconditions.checkNotNull(a);
/*  481 */     Preconditions.checkNotNull(b);
/*  482 */     return concat(consumingForArray((Iterator<? extends T>[])new Iterator[] { a, b }));
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
/*      */   public static <T> Iterator<T> concat(Iterator<? extends T> a, Iterator<? extends T> b, Iterator<? extends T> c) {
/*  495 */     Preconditions.checkNotNull(a);
/*  496 */     Preconditions.checkNotNull(b);
/*  497 */     Preconditions.checkNotNull(c);
/*  498 */     return concat(consumingForArray((Iterator<? extends T>[])new Iterator[] { a, b, c }));
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
/*      */   public static <T> Iterator<T> concat(Iterator<? extends T> a, Iterator<? extends T> b, Iterator<? extends T> c, Iterator<? extends T> d) {
/*  515 */     Preconditions.checkNotNull(a);
/*  516 */     Preconditions.checkNotNull(b);
/*  517 */     Preconditions.checkNotNull(c);
/*  518 */     Preconditions.checkNotNull(d);
/*  519 */     return concat(consumingForArray((Iterator<? extends T>[])new Iterator[] { a, b, c, d }));
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
/*      */   public static <T> Iterator<T> concat(Iterator<? extends T>... inputs) {
/*  533 */     return concatNoDefensiveCopy(Arrays.<Iterator<? extends T>>copyOf(inputs, inputs.length));
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
/*      */   public static <T> Iterator<T> concat(Iterator<? extends Iterator<? extends T>> inputs) {
/*  546 */     return new ConcatenatedIterator<>(inputs);
/*      */   }
/*      */ 
/*      */   
/*      */   static <T> Iterator<T> concatNoDefensiveCopy(Iterator<? extends T>... inputs) {
/*  551 */     for (Iterator<? extends T> input : (Iterator[])Preconditions.checkNotNull(inputs)) {
/*  552 */       Preconditions.checkNotNull(input);
/*      */     }
/*  554 */     return concat(consumingForArray(inputs));
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
/*      */   public static <T> UnmodifiableIterator<List<T>> partition(Iterator<T> iterator, int size) {
/*  572 */     return partitionImpl(iterator, size, false);
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
/*      */   public static <T> UnmodifiableIterator<List<T>> paddedPartition(Iterator<T> iterator, int size) {
/*  590 */     return partitionImpl(iterator, size, true);
/*      */   }
/*      */ 
/*      */   
/*      */   private static <T> UnmodifiableIterator<List<T>> partitionImpl(final Iterator<T> iterator, final int size, final boolean pad) {
/*  595 */     Preconditions.checkNotNull(iterator);
/*  596 */     Preconditions.checkArgument((size > 0));
/*  597 */     return new UnmodifiableIterator<List<T>>()
/*      */       {
/*      */         public boolean hasNext() {
/*  600 */           return iterator.hasNext();
/*      */         }
/*      */ 
/*      */         
/*      */         public List<T> next() {
/*  605 */           if (!hasNext()) {
/*  606 */             throw new NoSuchElementException();
/*      */           }
/*  608 */           Object[] array = new Object[size];
/*  609 */           int count = 0;
/*  610 */           for (; count < size && iterator.hasNext(); count++) {
/*  611 */             array[count] = iterator.next();
/*      */           }
/*  613 */           for (int i = count; i < size; i++) {
/*  614 */             array[i] = null;
/*      */           }
/*      */ 
/*      */           
/*  618 */           List<T> list = Collections.unmodifiableList(Arrays.asList((T[])array));
/*  619 */           return (pad || count == size) ? list : list.subList(0, count);
/*      */         }
/*      */       };
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static <T> UnmodifiableIterator<T> filter(final Iterator<T> unfiltered, final Predicate<? super T> retainIfTrue) {
/*  630 */     Preconditions.checkNotNull(unfiltered);
/*  631 */     Preconditions.checkNotNull(retainIfTrue);
/*  632 */     return new AbstractIterator<T>()
/*      */       {
/*      */         protected T computeNext() {
/*  635 */           while (unfiltered.hasNext()) {
/*  636 */             T element = unfiltered.next();
/*  637 */             if (retainIfTrue.apply(element)) {
/*  638 */               return element;
/*      */             }
/*      */           } 
/*  641 */           return endOfData();
/*      */         }
/*      */       };
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @GwtIncompatible
/*      */   public static <T> UnmodifiableIterator<T> filter(Iterator<?> unfiltered, Class<T> desiredType) {
/*  653 */     return filter((Iterator)unfiltered, Predicates.instanceOf(desiredType));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static <T> boolean any(Iterator<T> iterator, Predicate<? super T> predicate) {
/*  661 */     return (indexOf(iterator, predicate) != -1);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static <T> boolean all(Iterator<T> iterator, Predicate<? super T> predicate) {
/*  669 */     Preconditions.checkNotNull(predicate);
/*  670 */     while (iterator.hasNext()) {
/*  671 */       T element = iterator.next();
/*  672 */       if (!predicate.apply(element)) {
/*  673 */         return false;
/*      */       }
/*      */     } 
/*  676 */     return true;
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
/*      */   public static <T> T find(Iterator<T> iterator, Predicate<? super T> predicate) {
/*  689 */     Preconditions.checkNotNull(iterator);
/*  690 */     Preconditions.checkNotNull(predicate);
/*  691 */     while (iterator.hasNext()) {
/*  692 */       T t = iterator.next();
/*  693 */       if (predicate.apply(t)) {
/*  694 */         return t;
/*      */       }
/*      */     } 
/*  697 */     throw new NoSuchElementException();
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
/*      */   public static <T> T find(Iterator<? extends T> iterator, Predicate<? super T> predicate, T defaultValue) {
/*  710 */     Preconditions.checkNotNull(iterator);
/*  711 */     Preconditions.checkNotNull(predicate);
/*  712 */     while (iterator.hasNext()) {
/*  713 */       T t = iterator.next();
/*  714 */       if (predicate.apply(t)) {
/*  715 */         return t;
/*      */       }
/*      */     } 
/*  718 */     return defaultValue;
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
/*      */   public static <T> Optional<T> tryFind(Iterator<T> iterator, Predicate<? super T> predicate) {
/*  733 */     Preconditions.checkNotNull(iterator);
/*  734 */     Preconditions.checkNotNull(predicate);
/*  735 */     while (iterator.hasNext()) {
/*  736 */       T t = iterator.next();
/*  737 */       if (predicate.apply(t)) {
/*  738 */         return Optional.of(t);
/*      */       }
/*      */     } 
/*  741 */     return Optional.absent();
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
/*      */   public static <T> int indexOf(Iterator<T> iterator, Predicate<? super T> predicate) {
/*  759 */     Preconditions.checkNotNull(predicate, "predicate");
/*  760 */     for (int i = 0; iterator.hasNext(); i++) {
/*  761 */       T current = iterator.next();
/*  762 */       if (predicate.apply(current)) {
/*  763 */         return i;
/*      */       }
/*      */     } 
/*  766 */     return -1;
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
/*      */   public static <F, T> Iterator<T> transform(Iterator<F> fromIterator, final Function<? super F, ? extends T> function) {
/*  779 */     Preconditions.checkNotNull(function);
/*  780 */     return new TransformedIterator<F, T>(fromIterator)
/*      */       {
/*      */         T transform(F from) {
/*  783 */           return (T)function.apply(from);
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
/*      */   public static <T> T get(Iterator<T> iterator, int position) {
/*  798 */     checkNonnegative(position);
/*  799 */     int skipped = advance(iterator, position);
/*  800 */     if (!iterator.hasNext()) {
/*  801 */       throw new IndexOutOfBoundsException("position (" + position + ") must be less than the number of elements that remained (" + skipped + ")");
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  808 */     return iterator.next();
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
/*      */   public static <T> T get(Iterator<? extends T> iterator, int position, T defaultValue) {
/*  825 */     checkNonnegative(position);
/*  826 */     advance(iterator, position);
/*  827 */     return getNext(iterator, defaultValue);
/*      */   }
/*      */   
/*      */   static void checkNonnegative(int position) {
/*  831 */     if (position < 0) {
/*  832 */       throw new IndexOutOfBoundsException("position (" + position + ") must not be negative");
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
/*      */   public static <T> T getNext(Iterator<? extends T> iterator, T defaultValue) {
/*  845 */     return iterator.hasNext() ? iterator.next() : defaultValue;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static <T> T getLast(Iterator<T> iterator) {
/*      */     while (true) {
/*  856 */       T current = iterator.next();
/*  857 */       if (!iterator.hasNext()) {
/*  858 */         return current;
/*      */       }
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
/*      */   public static <T> T getLast(Iterator<? extends T> iterator, T defaultValue) {
/*  872 */     return iterator.hasNext() ? getLast((Iterator)iterator) : defaultValue;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @CanIgnoreReturnValue
/*      */   public static int advance(Iterator<?> iterator, int numberToAdvance) {
/*  884 */     Preconditions.checkNotNull(iterator);
/*  885 */     Preconditions.checkArgument((numberToAdvance >= 0), "numberToAdvance must be nonnegative");
/*      */     
/*      */     int i;
/*  888 */     for (i = 0; i < numberToAdvance && iterator.hasNext(); i++) {
/*  889 */       iterator.next();
/*      */     }
/*  891 */     return i;
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
/*      */   public static <T> Iterator<T> limit(final Iterator<T> iterator, final int limitSize) {
/*  905 */     Preconditions.checkNotNull(iterator);
/*  906 */     Preconditions.checkArgument((limitSize >= 0), "limit is negative");
/*  907 */     return new Iterator<T>()
/*      */       {
/*      */         private int count;
/*      */         
/*      */         public boolean hasNext() {
/*  912 */           return (this.count < limitSize && iterator.hasNext());
/*      */         }
/*      */ 
/*      */         
/*      */         public T next() {
/*  917 */           if (!hasNext()) {
/*  918 */             throw new NoSuchElementException();
/*      */           }
/*  920 */           this.count++;
/*  921 */           return iterator.next();
/*      */         }
/*      */ 
/*      */         
/*      */         public void remove() {
/*  926 */           iterator.remove();
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
/*      */   public static <T> Iterator<T> consumingIterator(final Iterator<T> iterator) {
/*  943 */     Preconditions.checkNotNull(iterator);
/*  944 */     return new UnmodifiableIterator<T>()
/*      */       {
/*      */         public boolean hasNext() {
/*  947 */           return iterator.hasNext();
/*      */         }
/*      */ 
/*      */         
/*      */         public T next() {
/*  952 */           T next = iterator.next();
/*  953 */           iterator.remove();
/*  954 */           return next;
/*      */         }
/*      */ 
/*      */         
/*      */         public String toString() {
/*  959 */           return "Iterators.consumingIterator(...)";
/*      */         }
/*      */       };
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   static <T> T pollNext(Iterator<T> iterator) {
/*  969 */     if (iterator.hasNext()) {
/*  970 */       T result = iterator.next();
/*  971 */       iterator.remove();
/*  972 */       return result;
/*      */     } 
/*  974 */     return null;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   static void clear(Iterator<?> iterator) {
/*  982 */     Preconditions.checkNotNull(iterator);
/*  983 */     while (iterator.hasNext()) {
/*  984 */       iterator.next();
/*  985 */       iterator.remove();
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
/*      */   @SafeVarargs
/*      */   public static <T> UnmodifiableIterator<T> forArray(T... array) {
/* 1001 */     return forArray(array, 0, array.length, 0);
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
/*      */   static <T> UnmodifiableListIterator<T> forArray(T[] array, int offset, int length, int index) {
/* 1013 */     Preconditions.checkArgument((length >= 0));
/* 1014 */     int end = offset + length;
/*      */ 
/*      */     
/* 1017 */     Preconditions.checkPositionIndexes(offset, end, array.length);
/* 1018 */     Preconditions.checkPositionIndex(index, length);
/* 1019 */     if (length == 0) {
/* 1020 */       return emptyListIterator();
/*      */     }
/* 1022 */     return new ArrayItr<>(array, offset, length, index);
/*      */   }
/*      */   
/*      */   private static final class ArrayItr<T> extends AbstractIndexedListIterator<T> {
/* 1026 */     static final UnmodifiableListIterator<Object> EMPTY = new ArrayItr((T[])new Object[0], 0, 0, 0);
/*      */     
/*      */     private final T[] array;
/*      */     private final int offset;
/*      */     
/*      */     ArrayItr(T[] array, int offset, int length, int index) {
/* 1032 */       super(length, index);
/* 1033 */       this.array = array;
/* 1034 */       this.offset = offset;
/*      */     }
/*      */ 
/*      */     
/*      */     protected T get(int index) {
/* 1039 */       return this.array[this.offset + index];
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static <T> UnmodifiableIterator<T> singletonIterator(final T value) {
/* 1049 */     return new UnmodifiableIterator<T>()
/*      */       {
/*      */         boolean done;
/*      */         
/*      */         public boolean hasNext() {
/* 1054 */           return !this.done;
/*      */         }
/*      */ 
/*      */         
/*      */         public T next() {
/* 1059 */           if (this.done) {
/* 1060 */             throw new NoSuchElementException();
/*      */           }
/* 1062 */           this.done = true;
/* 1063 */           return (T)value;
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
/*      */   public static <T> UnmodifiableIterator<T> forEnumeration(final Enumeration<T> enumeration) {
/* 1076 */     Preconditions.checkNotNull(enumeration);
/* 1077 */     return new UnmodifiableIterator<T>()
/*      */       {
/*      */         public boolean hasNext() {
/* 1080 */           return enumeration.hasMoreElements();
/*      */         }
/*      */ 
/*      */         
/*      */         public T next() {
/* 1085 */           return enumeration.nextElement();
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
/*      */   public static <T> Enumeration<T> asEnumeration(final Iterator<T> iterator) {
/* 1097 */     Preconditions.checkNotNull(iterator);
/* 1098 */     return new Enumeration<T>()
/*      */       {
/*      */         public boolean hasMoreElements() {
/* 1101 */           return iterator.hasNext();
/*      */         }
/*      */ 
/*      */         
/*      */         public T nextElement() {
/* 1106 */           return iterator.next();
/*      */         }
/*      */       };
/*      */   }
/*      */   
/*      */   private static class PeekingImpl<E>
/*      */     implements PeekingIterator<E>
/*      */   {
/*      */     private final Iterator<? extends E> iterator;
/*      */     private boolean hasPeeked;
/*      */     private E peekedElement;
/*      */     
/*      */     public PeekingImpl(Iterator<? extends E> iterator) {
/* 1119 */       this.iterator = (Iterator<? extends E>)Preconditions.checkNotNull(iterator);
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean hasNext() {
/* 1124 */       return (this.hasPeeked || this.iterator.hasNext());
/*      */     }
/*      */ 
/*      */     
/*      */     public E next() {
/* 1129 */       if (!this.hasPeeked) {
/* 1130 */         return this.iterator.next();
/*      */       }
/* 1132 */       E result = this.peekedElement;
/* 1133 */       this.hasPeeked = false;
/* 1134 */       this.peekedElement = null;
/* 1135 */       return result;
/*      */     }
/*      */ 
/*      */     
/*      */     public void remove() {
/* 1140 */       Preconditions.checkState(!this.hasPeeked, "Can't remove after you've peeked at next");
/* 1141 */       this.iterator.remove();
/*      */     }
/*      */ 
/*      */     
/*      */     public E peek() {
/* 1146 */       if (!this.hasPeeked) {
/* 1147 */         this.peekedElement = this.iterator.next();
/* 1148 */         this.hasPeeked = true;
/*      */       } 
/* 1150 */       return this.peekedElement;
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
/*      */   public static <T> PeekingIterator<T> peekingIterator(Iterator<? extends T> iterator) {
/* 1191 */     if (iterator instanceof PeekingImpl) {
/*      */ 
/*      */ 
/*      */       
/* 1195 */       PeekingImpl<T> peeking = (PeekingImpl)iterator;
/* 1196 */       return peeking;
/*      */     } 
/* 1198 */     return new PeekingImpl<>(iterator);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Deprecated
/*      */   public static <T> PeekingIterator<T> peekingIterator(PeekingIterator<T> iterator) {
/* 1209 */     return (PeekingIterator<T>)Preconditions.checkNotNull(iterator);
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
/*      */   public static <T> UnmodifiableIterator<T> mergeSorted(Iterable<? extends Iterator<? extends T>> iterators, Comparator<? super T> comparator) {
/* 1227 */     Preconditions.checkNotNull(iterators, "iterators");
/* 1228 */     Preconditions.checkNotNull(comparator, "comparator");
/*      */     
/* 1230 */     return new MergingIterator<>(iterators, comparator);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static class MergingIterator<T>
/*      */     extends UnmodifiableIterator<T>
/*      */   {
/*      */     final Queue<PeekingIterator<T>> queue;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public MergingIterator(Iterable<? extends Iterator<? extends T>> iterators, final Comparator<? super T> itemComparator) {
/* 1250 */       Comparator<PeekingIterator<T>> heapComparator = (Comparator)new Comparator<PeekingIterator<PeekingIterator<T>>>()
/*      */         {
/*      */           public int compare(PeekingIterator<T> o1, PeekingIterator<T> o2)
/*      */           {
/* 1254 */             return itemComparator.compare(o1.peek(), o2.peek());
/*      */           }
/*      */         };
/*      */       
/* 1258 */       this.queue = new PriorityQueue<>(2, heapComparator);
/*      */       
/* 1260 */       for (Iterator<? extends T> iterator : iterators) {
/* 1261 */         if (iterator.hasNext()) {
/* 1262 */           this.queue.add(Iterators.peekingIterator(iterator));
/*      */         }
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean hasNext() {
/* 1269 */       return !this.queue.isEmpty();
/*      */     }
/*      */ 
/*      */     
/*      */     public T next() {
/* 1274 */       PeekingIterator<T> nextIter = this.queue.remove();
/* 1275 */       T next = nextIter.next();
/* 1276 */       if (nextIter.hasNext()) {
/* 1277 */         this.queue.add(nextIter);
/*      */       }
/* 1279 */       return next;
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static class ConcatenatedIterator<T>
/*      */     implements Iterator<T>
/*      */   {
/*      */     private Iterator<? extends T> toRemove;
/*      */ 
/*      */     
/*      */     private Iterator<? extends T> iterator;
/*      */ 
/*      */     
/*      */     private Iterator<? extends Iterator<? extends T>> topMetaIterator;
/*      */ 
/*      */     
/*      */     private Deque<Iterator<? extends Iterator<? extends T>>> metaIterators;
/*      */ 
/*      */ 
/*      */     
/*      */     ConcatenatedIterator(Iterator<? extends Iterator<? extends T>> metaIterator) {
/* 1303 */       this.iterator = Iterators.emptyIterator();
/* 1304 */       this.topMetaIterator = (Iterator<? extends Iterator<? extends T>>)Preconditions.checkNotNull(metaIterator);
/*      */     }
/*      */ 
/*      */     
/*      */     private Iterator<? extends Iterator<? extends T>> getTopMetaIterator() {
/* 1309 */       while (this.topMetaIterator == null || !this.topMetaIterator.hasNext()) {
/* 1310 */         if (this.metaIterators != null && !this.metaIterators.isEmpty()) {
/* 1311 */           this.topMetaIterator = this.metaIterators.removeFirst(); continue;
/*      */         } 
/* 1313 */         return null;
/*      */       } 
/*      */       
/* 1316 */       return this.topMetaIterator;
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean hasNext() {
/* 1321 */       while (!((Iterator)Preconditions.checkNotNull(this.iterator)).hasNext()) {
/*      */ 
/*      */ 
/*      */         
/* 1325 */         this.topMetaIterator = getTopMetaIterator();
/* 1326 */         if (this.topMetaIterator == null) {
/* 1327 */           return false;
/*      */         }
/*      */         
/* 1330 */         this.iterator = this.topMetaIterator.next();
/*      */         
/* 1332 */         if (this.iterator instanceof ConcatenatedIterator) {
/*      */ 
/*      */ 
/*      */           
/* 1336 */           ConcatenatedIterator<T> topConcat = (ConcatenatedIterator)this.iterator;
/* 1337 */           this.iterator = topConcat.iterator;
/*      */ 
/*      */ 
/*      */ 
/*      */           
/* 1342 */           if (this.metaIterators == null) {
/* 1343 */             this.metaIterators = new ArrayDeque<>();
/*      */           }
/* 1345 */           this.metaIterators.addFirst(this.topMetaIterator);
/* 1346 */           if (topConcat.metaIterators != null) {
/* 1347 */             while (!topConcat.metaIterators.isEmpty()) {
/* 1348 */               this.metaIterators.addFirst(topConcat.metaIterators.removeLast());
/*      */             }
/*      */           }
/* 1351 */           this.topMetaIterator = topConcat.topMetaIterator;
/*      */         } 
/*      */       } 
/* 1354 */       return true;
/*      */     }
/*      */ 
/*      */     
/*      */     public T next() {
/* 1359 */       if (hasNext()) {
/* 1360 */         this.toRemove = this.iterator;
/* 1361 */         return this.iterator.next();
/*      */       } 
/* 1363 */       throw new NoSuchElementException();
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     public void remove() {
/* 1369 */       CollectPreconditions.checkRemove((this.toRemove != null));
/* 1370 */       this.toRemove.remove();
/* 1371 */       this.toRemove = null;
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   static <T> ListIterator<T> cast(Iterator<T> iterator) {
/* 1377 */     return (ListIterator<T>)iterator;
/*      */   }
/*      */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\google\common\collect\Iterators.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */