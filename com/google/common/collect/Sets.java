/*      */ package com.google.common.collect;
/*      */ 
/*      */ import com.google.common.annotations.Beta;
/*      */ import com.google.common.annotations.GwtCompatible;
/*      */ import com.google.common.annotations.GwtIncompatible;
/*      */ import com.google.common.base.Preconditions;
/*      */ import com.google.common.base.Predicate;
/*      */ import com.google.common.base.Predicates;
/*      */ import com.google.common.math.IntMath;
/*      */ import com.google.errorprone.annotations.CanIgnoreReturnValue;
/*      */ import java.io.Serializable;
/*      */ import java.util.AbstractSet;
/*      */ import java.util.Arrays;
/*      */ import java.util.BitSet;
/*      */ import java.util.Collection;
/*      */ import java.util.Collections;
/*      */ import java.util.Comparator;
/*      */ import java.util.EnumSet;
/*      */ import java.util.HashSet;
/*      */ import java.util.Iterator;
/*      */ import java.util.LinkedHashSet;
/*      */ import java.util.List;
/*      */ import java.util.Map;
/*      */ import java.util.NavigableSet;
/*      */ import java.util.NoSuchElementException;
/*      */ import java.util.Set;
/*      */ import java.util.SortedSet;
/*      */ import java.util.TreeSet;
/*      */ import java.util.concurrent.ConcurrentHashMap;
/*      */ import java.util.concurrent.CopyOnWriteArraySet;
/*      */ import java.util.function.Consumer;
/*      */ import java.util.function.Predicate;
/*      */ import java.util.function.Supplier;
/*      */ import java.util.stream.Collector;
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
/*      */ @GwtCompatible(emulated = true)
/*      */ public final class Sets
/*      */ {
/*      */   static abstract class ImprovedAbstractSet<E>
/*      */     extends AbstractSet<E>
/*      */   {
/*      */     public boolean removeAll(Collection<?> c) {
/*   80 */       return Sets.removeAllImpl(this, c);
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean retainAll(Collection<?> c) {
/*   85 */       return super.retainAll((Collection)Preconditions.checkNotNull(c));
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
/*      */   @GwtCompatible(serializable = true)
/*      */   public static <E extends Enum<E>> ImmutableSet<E> immutableEnumSet(E anElement, E... otherElements) {
/*  104 */     return ImmutableEnumSet.asImmutable(EnumSet.of(anElement, otherElements));
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
/*      */   @GwtCompatible(serializable = true)
/*      */   public static <E extends Enum<E>> ImmutableSet<E> immutableEnumSet(Iterable<E> elements) {
/*  120 */     if (elements instanceof ImmutableEnumSet)
/*  121 */       return (ImmutableEnumSet)elements; 
/*  122 */     if (elements instanceof Collection) {
/*  123 */       Collection<E> collection = (Collection<E>)elements;
/*  124 */       if (collection.isEmpty()) {
/*  125 */         return ImmutableSet.of();
/*      */       }
/*  127 */       return ImmutableEnumSet.asImmutable(EnumSet.copyOf(collection));
/*      */     } 
/*      */     
/*  130 */     Iterator<E> itr = elements.iterator();
/*  131 */     if (itr.hasNext()) {
/*  132 */       EnumSet<E> enumSet = EnumSet.of(itr.next());
/*  133 */       Iterators.addAll(enumSet, itr);
/*  134 */       return ImmutableEnumSet.asImmutable(enumSet);
/*      */     } 
/*  136 */     return ImmutableSet.of();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static final class Accumulator<E extends Enum<E>>
/*      */   {
/*  144 */     static final Collector<Enum<?>, ?, ImmutableSet<? extends Enum<?>>> TO_IMMUTABLE_ENUM_SET = Collector.of(Accumulator::new, Accumulator::add, Accumulator::combine, Accumulator::toImmutableSet, new Collector.Characteristics[] { Collector.Characteristics.UNORDERED });
/*      */ 
/*      */ 
/*      */     
/*      */     private EnumSet<E> set;
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     void add(E e) {
/*  154 */       if (this.set == null) {
/*  155 */         this.set = EnumSet.of(e);
/*      */       } else {
/*  157 */         this.set.add(e);
/*      */       } 
/*      */     }
/*      */     
/*      */     Accumulator<E> combine(Accumulator<E> other) {
/*  162 */       if (this.set == null)
/*  163 */         return other; 
/*  164 */       if (other.set == null) {
/*  165 */         return this;
/*      */       }
/*  167 */       this.set.addAll(other.set);
/*  168 */       return this;
/*      */     }
/*      */ 
/*      */     
/*      */     ImmutableSet<E> toImmutableSet() {
/*  173 */       return (this.set == null) ? ImmutableSet.<E>of() : ImmutableEnumSet.asImmutable(this.set);
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
/*      */   public static <E extends Enum<E>> Collector<E, ?, ImmutableSet<E>> toImmutableEnumSet() {
/*  185 */     return (Collector)Accumulator.TO_IMMUTABLE_ENUM_SET;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static <E extends Enum<E>> EnumSet<E> newEnumSet(Iterable<E> iterable, Class<E> elementType) {
/*  195 */     EnumSet<E> set = EnumSet.noneOf(elementType);
/*  196 */     Iterables.addAll(set, iterable);
/*  197 */     return set;
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
/*      */   public static <E> HashSet<E> newHashSet() {
/*  215 */     return new HashSet<>();
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
/*      */   public static <E> HashSet<E> newHashSet(E... elements) {
/*  232 */     HashSet<E> set = newHashSetWithExpectedSize(elements.length);
/*  233 */     Collections.addAll(set, elements);
/*  234 */     return set;
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
/*      */   public static <E> HashSet<E> newHashSet(Iterable<? extends E> elements) {
/*  256 */     return (elements instanceof Collection) ? new HashSet<>(
/*  257 */         Collections2.cast(elements)) : 
/*  258 */       newHashSet(elements.iterator());
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
/*      */   public static <E> HashSet<E> newHashSet(Iterator<? extends E> elements) {
/*  274 */     HashSet<E> set = newHashSet();
/*  275 */     Iterators.addAll(set, elements);
/*  276 */     return set;
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
/*      */   public static <E> HashSet<E> newHashSetWithExpectedSize(int expectedSize) {
/*  292 */     return new HashSet<>(Maps.capacity(expectedSize));
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
/*      */   public static <E> Set<E> newConcurrentHashSet() {
/*  306 */     return Collections.newSetFromMap(new ConcurrentHashMap<>());
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
/*      */   public static <E> Set<E> newConcurrentHashSet(Iterable<? extends E> elements) {
/*  323 */     Set<E> set = newConcurrentHashSet();
/*  324 */     Iterables.addAll(set, elements);
/*  325 */     return set;
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
/*      */   public static <E> LinkedHashSet<E> newLinkedHashSet() {
/*  342 */     return new LinkedHashSet<>();
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
/*      */   public static <E> LinkedHashSet<E> newLinkedHashSet(Iterable<? extends E> elements) {
/*  361 */     if (elements instanceof Collection) {
/*  362 */       return new LinkedHashSet<>(Collections2.cast(elements));
/*      */     }
/*  364 */     LinkedHashSet<E> set = newLinkedHashSet();
/*  365 */     Iterables.addAll(set, elements);
/*  366 */     return set;
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
/*      */   public static <E> LinkedHashSet<E> newLinkedHashSetWithExpectedSize(int expectedSize) {
/*  382 */     return new LinkedHashSet<>(Maps.capacity(expectedSize));
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
/*      */   public static <E extends Comparable> TreeSet<E> newTreeSet() {
/*  400 */     return new TreeSet<>();
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
/*      */   public static <E extends Comparable> TreeSet<E> newTreeSet(Iterable<? extends E> elements) {
/*  425 */     TreeSet<E> set = newTreeSet();
/*  426 */     Iterables.addAll(set, elements);
/*  427 */     return set;
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
/*      */   public static <E> TreeSet<E> newTreeSet(Comparator<? super E> comparator) {
/*  447 */     return new TreeSet<>((Comparator<? super E>)Preconditions.checkNotNull(comparator));
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
/*      */   public static <E> Set<E> newIdentityHashSet() {
/*  460 */     return Collections.newSetFromMap(Maps.newIdentityHashMap());
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
/*      */   public static <E> CopyOnWriteArraySet<E> newCopyOnWriteArraySet() {
/*  474 */     return new CopyOnWriteArraySet<>();
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
/*      */   @GwtIncompatible
/*      */   public static <E> CopyOnWriteArraySet<E> newCopyOnWriteArraySet(Iterable<? extends E> elements) {
/*  491 */     Collection<? extends E> elementsCollection = (elements instanceof Collection) ? Collections2.<E>cast(elements) : Lists.<E>newArrayList(elements);
/*  492 */     return new CopyOnWriteArraySet<>(elementsCollection);
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
/*      */   public static <E extends Enum<E>> EnumSet<E> complementOf(Collection<E> collection) {
/*  509 */     if (collection instanceof EnumSet) {
/*  510 */       return EnumSet.complementOf((EnumSet<E>)collection);
/*      */     }
/*  512 */     Preconditions.checkArgument(
/*  513 */         !collection.isEmpty(), "collection is empty; use the other version of this method");
/*  514 */     Class<E> type = ((Enum<E>)collection.iterator().next()).getDeclaringClass();
/*  515 */     return makeComplementByHand(collection, type);
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
/*      */   public static <E extends Enum<E>> EnumSet<E> complementOf(Collection<E> collection, Class<E> type) {
/*  530 */     Preconditions.checkNotNull(collection);
/*  531 */     return (collection instanceof EnumSet) ? 
/*  532 */       EnumSet.<E>complementOf((EnumSet<E>)collection) : 
/*  533 */       makeComplementByHand(collection, type);
/*      */   }
/*      */ 
/*      */   
/*      */   private static <E extends Enum<E>> EnumSet<E> makeComplementByHand(Collection<E> collection, Class<E> type) {
/*  538 */     EnumSet<E> result = EnumSet.allOf(type);
/*  539 */     result.removeAll(collection);
/*  540 */     return result;
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
/*      */   @Deprecated
/*      */   public static <E> Set<E> newSetFromMap(Map<E, Boolean> map) {
/*  574 */     return Collections.newSetFromMap(map);
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
/*      */   public static abstract class SetView<E>
/*      */     extends AbstractSet<E>
/*      */   {
/*      */     private SetView() {}
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public ImmutableSet<E> immutableCopy() {
/*  598 */       return ImmutableSet.copyOf(this);
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     @CanIgnoreReturnValue
/*      */     public <S extends Set<E>> S copyInto(S set) {
/*  612 */       set.addAll(this);
/*  613 */       return set;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     @Deprecated
/*      */     @CanIgnoreReturnValue
/*      */     public final boolean add(E e) {
/*  626 */       throw new UnsupportedOperationException();
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     @Deprecated
/*      */     @CanIgnoreReturnValue
/*      */     public final boolean remove(Object object) {
/*  639 */       throw new UnsupportedOperationException();
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     @Deprecated
/*      */     @CanIgnoreReturnValue
/*      */     public final boolean addAll(Collection<? extends E> newElements) {
/*  652 */       throw new UnsupportedOperationException();
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     @Deprecated
/*      */     @CanIgnoreReturnValue
/*      */     public final boolean removeAll(Collection<?> oldElements) {
/*  665 */       throw new UnsupportedOperationException();
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     @Deprecated
/*      */     @CanIgnoreReturnValue
/*      */     public final boolean removeIf(Predicate<? super E> filter) {
/*  678 */       throw new UnsupportedOperationException();
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     @Deprecated
/*      */     @CanIgnoreReturnValue
/*      */     public final boolean retainAll(Collection<?> elementsToKeep) {
/*  691 */       throw new UnsupportedOperationException();
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     @Deprecated
/*      */     public final void clear() {
/*  703 */       throw new UnsupportedOperationException();
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public abstract UnmodifiableIterator<E> iterator();
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
/*      */   public static <E> SetView<E> union(final Set<? extends E> set1, final Set<? extends E> set2) {
/*  726 */     Preconditions.checkNotNull(set1, "set1");
/*  727 */     Preconditions.checkNotNull(set2, "set2");
/*      */     
/*  729 */     return new SetView<E>()
/*      */       {
/*      */         public int size() {
/*  732 */           int size = set1.size();
/*  733 */           for (E e : set2) {
/*  734 */             if (!set1.contains(e)) {
/*  735 */               size++;
/*      */             }
/*      */           } 
/*  738 */           return size;
/*      */         }
/*      */ 
/*      */         
/*      */         public boolean isEmpty() {
/*  743 */           return (set1.isEmpty() && set2.isEmpty());
/*      */         }
/*      */ 
/*      */         
/*      */         public UnmodifiableIterator<E> iterator() {
/*  748 */           return new AbstractIterator() {
/*  749 */               final Iterator<? extends E> itr1 = set1.iterator();
/*  750 */               final Iterator<? extends E> itr2 = set2.iterator();
/*      */ 
/*      */               
/*      */               protected E computeNext() {
/*  754 */                 if (this.itr1.hasNext()) {
/*  755 */                   return this.itr1.next();
/*      */                 }
/*  757 */                 while (this.itr2.hasNext()) {
/*  758 */                   E e = this.itr2.next();
/*  759 */                   if (!set1.contains(e)) {
/*  760 */                     return e;
/*      */                   }
/*      */                 } 
/*  763 */                 return endOfData();
/*      */               }
/*      */             };
/*      */         }
/*      */ 
/*      */         
/*      */         public Stream<E> stream() {
/*  770 */           return Stream.concat(set1.stream(), set2.stream().filter(e -> !set1.contains(e)));
/*      */         }
/*      */ 
/*      */         
/*      */         public Stream<E> parallelStream() {
/*  775 */           return stream().parallel();
/*      */         }
/*      */ 
/*      */         
/*      */         public boolean contains(Object object) {
/*  780 */           return (set1.contains(object) || set2.contains(object));
/*      */         }
/*      */ 
/*      */         
/*      */         public <S extends Set<E>> S copyInto(S set) {
/*  785 */           set.addAll(set1);
/*  786 */           set.addAll(set2);
/*  787 */           return set;
/*      */         }
/*      */ 
/*      */         
/*      */         public ImmutableSet<E> immutableCopy() {
/*  792 */           return (new ImmutableSet.Builder<>()).addAll(set1).addAll(set2).build();
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static <E> SetView<E> intersection(final Set<E> set1, final Set<?> set2) {
/*  825 */     Preconditions.checkNotNull(set1, "set1");
/*  826 */     Preconditions.checkNotNull(set2, "set2");
/*      */     
/*  828 */     return new SetView<E>()
/*      */       {
/*      */         public UnmodifiableIterator<E> iterator() {
/*  831 */           return new AbstractIterator() {
/*  832 */               final Iterator<E> itr = set1.iterator();
/*      */ 
/*      */               
/*      */               protected E computeNext() {
/*  836 */                 while (this.itr.hasNext()) {
/*  837 */                   E e = this.itr.next();
/*  838 */                   if (set2.contains(e)) {
/*  839 */                     return e;
/*      */                   }
/*      */                 } 
/*  842 */                 return endOfData();
/*      */               }
/*      */             };
/*      */         }
/*      */ 
/*      */         
/*      */         public Stream<E> stream() {
/*  849 */           return set1.stream().filter(set2::contains);
/*      */         }
/*      */ 
/*      */         
/*      */         public Stream<E> parallelStream() {
/*  854 */           return set1.parallelStream().filter(set2::contains);
/*      */         }
/*      */ 
/*      */         
/*      */         public int size() {
/*  859 */           int size = 0;
/*  860 */           for (E e : set1) {
/*  861 */             if (set2.contains(e)) {
/*  862 */               size++;
/*      */             }
/*      */           } 
/*  865 */           return size;
/*      */         }
/*      */ 
/*      */         
/*      */         public boolean isEmpty() {
/*  870 */           return Collections.disjoint(set2, set1);
/*      */         }
/*      */ 
/*      */         
/*      */         public boolean contains(Object object) {
/*  875 */           return (set1.contains(object) && set2.contains(object));
/*      */         }
/*      */ 
/*      */         
/*      */         public boolean containsAll(Collection<?> collection) {
/*  880 */           return (set1.containsAll(collection) && set2.containsAll(collection));
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
/*      */   public static <E> SetView<E> difference(final Set<E> set1, final Set<?> set2) {
/*  896 */     Preconditions.checkNotNull(set1, "set1");
/*  897 */     Preconditions.checkNotNull(set2, "set2");
/*      */     
/*  899 */     return new SetView<E>()
/*      */       {
/*      */         public UnmodifiableIterator<E> iterator() {
/*  902 */           return new AbstractIterator() {
/*  903 */               final Iterator<E> itr = set1.iterator();
/*      */ 
/*      */               
/*      */               protected E computeNext() {
/*  907 */                 while (this.itr.hasNext()) {
/*  908 */                   E e = this.itr.next();
/*  909 */                   if (!set2.contains(e)) {
/*  910 */                     return e;
/*      */                   }
/*      */                 } 
/*  913 */                 return endOfData();
/*      */               }
/*      */             };
/*      */         }
/*      */ 
/*      */         
/*      */         public Stream<E> stream() {
/*  920 */           return set1.stream().filter(e -> !set2.contains(e));
/*      */         }
/*      */ 
/*      */         
/*      */         public Stream<E> parallelStream() {
/*  925 */           return set1.parallelStream().filter(e -> !set2.contains(e));
/*      */         }
/*      */ 
/*      */         
/*      */         public int size() {
/*  930 */           int size = 0;
/*  931 */           for (E e : set1) {
/*  932 */             if (!set2.contains(e)) {
/*  933 */               size++;
/*      */             }
/*      */           } 
/*  936 */           return size;
/*      */         }
/*      */ 
/*      */         
/*      */         public boolean isEmpty() {
/*  941 */           return set2.containsAll(set1);
/*      */         }
/*      */ 
/*      */         
/*      */         public boolean contains(Object element) {
/*  946 */           return (set1.contains(element) && !set2.contains(element));
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
/*      */   public static <E> SetView<E> symmetricDifference(final Set<? extends E> set1, final Set<? extends E> set2) {
/*  964 */     Preconditions.checkNotNull(set1, "set1");
/*  965 */     Preconditions.checkNotNull(set2, "set2");
/*      */     
/*  967 */     return new SetView<E>()
/*      */       {
/*      */         public UnmodifiableIterator<E> iterator() {
/*  970 */           final Iterator<? extends E> itr1 = set1.iterator();
/*  971 */           final Iterator<? extends E> itr2 = set2.iterator();
/*  972 */           return new AbstractIterator()
/*      */             {
/*      */               public E computeNext() {
/*  975 */                 while (itr1.hasNext()) {
/*  976 */                   E elem1 = itr1.next();
/*  977 */                   if (!set2.contains(elem1)) {
/*  978 */                     return elem1;
/*      */                   }
/*      */                 } 
/*  981 */                 while (itr2.hasNext()) {
/*  982 */                   E elem2 = itr2.next();
/*  983 */                   if (!set1.contains(elem2)) {
/*  984 */                     return elem2;
/*      */                   }
/*      */                 } 
/*  987 */                 return endOfData();
/*      */               }
/*      */             };
/*      */         }
/*      */ 
/*      */         
/*      */         public int size() {
/*  994 */           int size = 0;
/*  995 */           for (E e : set1) {
/*  996 */             if (!set2.contains(e)) {
/*  997 */               size++;
/*      */             }
/*      */           } 
/* 1000 */           for (E e : set2) {
/* 1001 */             if (!set1.contains(e)) {
/* 1002 */               size++;
/*      */             }
/*      */           } 
/* 1005 */           return size;
/*      */         }
/*      */ 
/*      */         
/*      */         public boolean isEmpty() {
/* 1010 */           return set1.equals(set2);
/*      */         }
/*      */ 
/*      */         
/*      */         public boolean contains(Object element) {
/* 1015 */           return set1.contains(element) ^ set2.contains(element);
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static <E> Set<E> filter(Set<E> unfiltered, Predicate<? super E> predicate) {
/* 1048 */     if (unfiltered instanceof SortedSet) {
/* 1049 */       return filter((SortedSet<E>)unfiltered, predicate);
/*      */     }
/* 1051 */     if (unfiltered instanceof FilteredSet) {
/*      */ 
/*      */       
/* 1054 */       FilteredSet<E> filtered = (FilteredSet<E>)unfiltered;
/* 1055 */       Predicate<E> combinedPredicate = Predicates.and(filtered.predicate, predicate);
/* 1056 */       return new FilteredSet<>((Set<E>)filtered.unfiltered, combinedPredicate);
/*      */     } 
/*      */     
/* 1059 */     return new FilteredSet<>((Set<E>)Preconditions.checkNotNull(unfiltered), (Predicate<? super E>)Preconditions.checkNotNull(predicate));
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
/*      */   public static <E> SortedSet<E> filter(SortedSet<E> unfiltered, Predicate<? super E> predicate) {
/* 1087 */     if (unfiltered instanceof FilteredSet) {
/*      */ 
/*      */       
/* 1090 */       FilteredSet<E> filtered = (FilteredSet<E>)unfiltered;
/* 1091 */       Predicate<E> combinedPredicate = Predicates.and(filtered.predicate, predicate);
/* 1092 */       return new FilteredSortedSet<>((SortedSet<E>)filtered.unfiltered, combinedPredicate);
/*      */     } 
/*      */     
/* 1095 */     return new FilteredSortedSet<>((SortedSet<E>)Preconditions.checkNotNull(unfiltered), (Predicate<? super E>)Preconditions.checkNotNull(predicate));
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
/*      */   @GwtIncompatible
/*      */   public static <E> NavigableSet<E> filter(NavigableSet<E> unfiltered, Predicate<? super E> predicate) {
/* 1126 */     if (unfiltered instanceof FilteredSet) {
/*      */ 
/*      */       
/* 1129 */       FilteredSet<E> filtered = (FilteredSet<E>)unfiltered;
/* 1130 */       Predicate<E> combinedPredicate = Predicates.and(filtered.predicate, predicate);
/* 1131 */       return new FilteredNavigableSet<>((NavigableSet<E>)filtered.unfiltered, combinedPredicate);
/*      */     } 
/*      */     
/* 1134 */     return new FilteredNavigableSet<>((NavigableSet<E>)Preconditions.checkNotNull(unfiltered), (Predicate<? super E>)Preconditions.checkNotNull(predicate));
/*      */   }
/*      */   
/*      */   private static class FilteredSet<E> extends Collections2.FilteredCollection<E> implements Set<E> {
/*      */     FilteredSet(Set<E> unfiltered, Predicate<? super E> predicate) {
/* 1139 */       super(unfiltered, predicate);
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean equals(Object object) {
/* 1144 */       return Sets.equalsImpl(this, object);
/*      */     }
/*      */ 
/*      */     
/*      */     public int hashCode() {
/* 1149 */       return Sets.hashCodeImpl(this);
/*      */     }
/*      */   }
/*      */   
/*      */   private static class FilteredSortedSet<E>
/*      */     extends FilteredSet<E> implements SortedSet<E> {
/*      */     FilteredSortedSet(SortedSet<E> unfiltered, Predicate<? super E> predicate) {
/* 1156 */       super(unfiltered, predicate);
/*      */     }
/*      */ 
/*      */     
/*      */     public Comparator<? super E> comparator() {
/* 1161 */       return ((SortedSet<E>)this.unfiltered).comparator();
/*      */     }
/*      */ 
/*      */     
/*      */     public SortedSet<E> subSet(E fromElement, E toElement) {
/* 1166 */       return new FilteredSortedSet(((SortedSet<E>)this.unfiltered)
/* 1167 */           .subSet(fromElement, toElement), this.predicate);
/*      */     }
/*      */ 
/*      */     
/*      */     public SortedSet<E> headSet(E toElement) {
/* 1172 */       return new FilteredSortedSet(((SortedSet<E>)this.unfiltered).headSet(toElement), this.predicate);
/*      */     }
/*      */ 
/*      */     
/*      */     public SortedSet<E> tailSet(E fromElement) {
/* 1177 */       return new FilteredSortedSet(((SortedSet<E>)this.unfiltered).tailSet(fromElement), this.predicate);
/*      */     }
/*      */ 
/*      */     
/*      */     public E first() {
/* 1182 */       return Iterators.find(this.unfiltered.iterator(), this.predicate);
/*      */     }
/*      */ 
/*      */     
/*      */     public E last() {
/* 1187 */       SortedSet<E> sortedUnfiltered = (SortedSet<E>)this.unfiltered;
/*      */       while (true) {
/* 1189 */         E element = sortedUnfiltered.last();
/* 1190 */         if (this.predicate.apply(element)) {
/* 1191 */           return element;
/*      */         }
/* 1193 */         sortedUnfiltered = sortedUnfiltered.headSet(element);
/*      */       } 
/*      */     }
/*      */   }
/*      */   
/*      */   @GwtIncompatible
/*      */   private static class FilteredNavigableSet<E>
/*      */     extends FilteredSortedSet<E> implements NavigableSet<E> {
/*      */     FilteredNavigableSet(NavigableSet<E> unfiltered, Predicate<? super E> predicate) {
/* 1202 */       super(unfiltered, predicate);
/*      */     }
/*      */     
/*      */     NavigableSet<E> unfiltered() {
/* 1206 */       return (NavigableSet<E>)this.unfiltered;
/*      */     }
/*      */ 
/*      */     
/*      */     public E lower(E e) {
/* 1211 */       return Iterators.find(unfiltered().headSet(e, false).descendingIterator(), this.predicate, null);
/*      */     }
/*      */ 
/*      */     
/*      */     public E floor(E e) {
/* 1216 */       return Iterators.find(unfiltered().headSet(e, true).descendingIterator(), this.predicate, null);
/*      */     }
/*      */ 
/*      */     
/*      */     public E ceiling(E e) {
/* 1221 */       return Iterables.find(unfiltered().tailSet(e, true), this.predicate, null);
/*      */     }
/*      */ 
/*      */     
/*      */     public E higher(E e) {
/* 1226 */       return Iterables.find(unfiltered().tailSet(e, false), this.predicate, null);
/*      */     }
/*      */ 
/*      */     
/*      */     public E pollFirst() {
/* 1231 */       return Iterables.removeFirstMatching(unfiltered(), this.predicate);
/*      */     }
/*      */ 
/*      */     
/*      */     public E pollLast() {
/* 1236 */       return Iterables.removeFirstMatching(unfiltered().descendingSet(), this.predicate);
/*      */     }
/*      */ 
/*      */     
/*      */     public NavigableSet<E> descendingSet() {
/* 1241 */       return Sets.filter(unfiltered().descendingSet(), this.predicate);
/*      */     }
/*      */ 
/*      */     
/*      */     public Iterator<E> descendingIterator() {
/* 1246 */       return Iterators.filter(unfiltered().descendingIterator(), this.predicate);
/*      */     }
/*      */ 
/*      */     
/*      */     public E last() {
/* 1251 */       return Iterators.find(unfiltered().descendingIterator(), this.predicate);
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     public NavigableSet<E> subSet(E fromElement, boolean fromInclusive, E toElement, boolean toInclusive) {
/* 1257 */       return Sets.filter(
/* 1258 */           unfiltered().subSet(fromElement, fromInclusive, toElement, toInclusive), this.predicate);
/*      */     }
/*      */ 
/*      */     
/*      */     public NavigableSet<E> headSet(E toElement, boolean inclusive) {
/* 1263 */       return Sets.filter(unfiltered().headSet(toElement, inclusive), this.predicate);
/*      */     }
/*      */ 
/*      */     
/*      */     public NavigableSet<E> tailSet(E fromElement, boolean inclusive) {
/* 1268 */       return Sets.filter(unfiltered().tailSet(fromElement, inclusive), this.predicate);
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
/*      */   public static <B> Set<List<B>> cartesianProduct(List<? extends Set<? extends B>> sets) {
/* 1325 */     return CartesianSet.create(sets);
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
/*      */   @SafeVarargs
/*      */   public static <B> Set<List<B>> cartesianProduct(Set<? extends B>... sets) {
/* 1382 */     return cartesianProduct(Arrays.asList(sets));
/*      */   }
/*      */   
/*      */   private static final class CartesianSet<E>
/*      */     extends ForwardingCollection<List<E>> implements Set<List<E>> {
/*      */     private final transient ImmutableList<ImmutableSet<E>> axes;
/*      */     private final transient CartesianList<E> delegate;
/*      */     
/*      */     static <E> Set<List<E>> create(List<? extends Set<? extends E>> sets) {
/* 1391 */       ImmutableList.Builder<ImmutableSet<E>> axesBuilder = new ImmutableList.Builder<>(sets.size());
/* 1392 */       for (Set<? extends E> set : sets) {
/* 1393 */         ImmutableSet<E> copy = ImmutableSet.copyOf(set);
/* 1394 */         if (copy.isEmpty()) {
/* 1395 */           return ImmutableSet.of();
/*      */         }
/* 1397 */         axesBuilder.add(copy);
/*      */       } 
/* 1399 */       final ImmutableList<ImmutableSet<E>> axes = axesBuilder.build();
/* 1400 */       ImmutableList<List<E>> listAxes = (ImmutableList)new ImmutableList<List<List<E>>>()
/*      */         {
/*      */           public int size()
/*      */           {
/* 1404 */             return axes.size();
/*      */           }
/*      */ 
/*      */           
/*      */           public List<E> get(int index) {
/* 1409 */             return ((ImmutableSet<E>)axes.get(index)).asList();
/*      */           }
/*      */ 
/*      */           
/*      */           boolean isPartialView() {
/* 1414 */             return true;
/*      */           }
/*      */         };
/* 1417 */       return new CartesianSet<>(axes, new CartesianList<>(listAxes));
/*      */     }
/*      */     
/*      */     private CartesianSet(ImmutableList<ImmutableSet<E>> axes, CartesianList<E> delegate) {
/* 1421 */       this.axes = axes;
/* 1422 */       this.delegate = delegate;
/*      */     }
/*      */ 
/*      */     
/*      */     protected Collection<List<E>> delegate() {
/* 1427 */       return this.delegate;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public boolean equals(Object object) {
/* 1434 */       if (object instanceof CartesianSet) {
/* 1435 */         CartesianSet<?> that = (CartesianSet)object;
/* 1436 */         return this.axes.equals(that.axes);
/*      */       } 
/* 1438 */       return super.equals(object);
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public int hashCode() {
/* 1447 */       int adjust = size() - 1;
/* 1448 */       for (int i = 0; i < this.axes.size(); i++) {
/* 1449 */         adjust *= 31;
/* 1450 */         adjust = adjust ^ 0xFFFFFFFF ^ 0xFFFFFFFF;
/*      */       } 
/*      */       
/* 1453 */       int hash = 1;
/* 1454 */       for (UnmodifiableIterator<ImmutableSet<E>> unmodifiableIterator = this.axes.iterator(); unmodifiableIterator.hasNext(); ) { Set<E> axis = unmodifiableIterator.next();
/* 1455 */         hash = 31 * hash + size() / axis.size() * axis.hashCode();
/*      */         
/* 1457 */         hash = hash ^ 0xFFFFFFFF ^ 0xFFFFFFFF; }
/*      */       
/* 1459 */       hash += adjust;
/* 1460 */       return hash ^ 0xFFFFFFFF ^ 0xFFFFFFFF;
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
/*      */   @GwtCompatible(serializable = false)
/*      */   public static <E> Set<Set<E>> powerSet(Set<E> set) {
/* 1490 */     return new PowerSet<>(set);
/*      */   }
/*      */   
/*      */   private static final class SubSet<E> extends AbstractSet<E> {
/*      */     private final ImmutableMap<E, Integer> inputSet;
/*      */     private final int mask;
/*      */     
/*      */     SubSet(ImmutableMap<E, Integer> inputSet, int mask) {
/* 1498 */       this.inputSet = inputSet;
/* 1499 */       this.mask = mask;
/*      */     }
/*      */ 
/*      */     
/*      */     public Iterator<E> iterator() {
/* 1504 */       return new UnmodifiableIterator<E>() {
/* 1505 */           final ImmutableList<E> elements = Sets.SubSet.this.inputSet.keySet().asList();
/* 1506 */           int remainingSetBits = Sets.SubSet.this.mask;
/*      */ 
/*      */           
/*      */           public boolean hasNext() {
/* 1510 */             return (this.remainingSetBits != 0);
/*      */           }
/*      */ 
/*      */           
/*      */           public E next() {
/* 1515 */             int index = Integer.numberOfTrailingZeros(this.remainingSetBits);
/* 1516 */             if (index == 32) {
/* 1517 */               throw new NoSuchElementException();
/*      */             }
/* 1519 */             this.remainingSetBits &= 1 << index ^ 0xFFFFFFFF;
/* 1520 */             return this.elements.get(index);
/*      */           }
/*      */         };
/*      */     }
/*      */ 
/*      */     
/*      */     public int size() {
/* 1527 */       return Integer.bitCount(this.mask);
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean contains(Object o) {
/* 1532 */       Integer index = this.inputSet.get(o);
/* 1533 */       return (index != null && (this.mask & 1 << index.intValue()) != 0);
/*      */     }
/*      */   }
/*      */   
/*      */   private static final class PowerSet<E> extends AbstractSet<Set<E>> {
/*      */     final ImmutableMap<E, Integer> inputSet;
/*      */     
/*      */     PowerSet(Set<E> input) {
/* 1541 */       Preconditions.checkArgument(
/* 1542 */           (input.size() <= 30), "Too many elements to create power set: %s > 30", input.size());
/* 1543 */       this.inputSet = Maps.indexMap(input);
/*      */     }
/*      */ 
/*      */     
/*      */     public int size() {
/* 1548 */       return 1 << this.inputSet.size();
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean isEmpty() {
/* 1553 */       return false;
/*      */     }
/*      */ 
/*      */     
/*      */     public Iterator<Set<E>> iterator() {
/* 1558 */       return (Iterator)new AbstractIndexedListIterator<Set<Set<E>>>(size())
/*      */         {
/*      */           protected Set<E> get(int setBits) {
/* 1561 */             return new Sets.SubSet<>(Sets.PowerSet.this.inputSet, setBits);
/*      */           }
/*      */         };
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean contains(Object obj) {
/* 1568 */       if (obj instanceof Set) {
/* 1569 */         Set<?> set = (Set)obj;
/* 1570 */         return this.inputSet.keySet().containsAll(set);
/*      */       } 
/* 1572 */       return false;
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean equals(Object obj) {
/* 1577 */       if (obj instanceof PowerSet) {
/* 1578 */         PowerSet<?> that = (PowerSet)obj;
/* 1579 */         return this.inputSet.equals(that.inputSet);
/*      */       } 
/* 1581 */       return super.equals(obj);
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public int hashCode() {
/* 1591 */       return this.inputSet.keySet().hashCode() << this.inputSet.size() - 1;
/*      */     }
/*      */ 
/*      */     
/*      */     public String toString() {
/* 1596 */       return "powerSet(" + this.inputSet + ")";
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
/*      */   @Beta
/*      */   public static <E> Set<Set<E>> combinations(Set<E> set, final int size) {
/* 1626 */     final ImmutableMap<E, Integer> index = Maps.indexMap(set);
/* 1627 */     CollectPreconditions.checkNonnegative(size, "size");
/* 1628 */     Preconditions.checkArgument((size <= index.size()), "size (%s) must be <= set.size() (%s)", size, index.size());
/* 1629 */     if (size == 0)
/* 1630 */       return ImmutableSet.of(ImmutableSet.of()); 
/* 1631 */     if (size == index.size()) {
/* 1632 */       return ImmutableSet.of(index.keySet());
/*      */     }
/* 1634 */     return (Set)new AbstractSet<Set<Set<E>>>()
/*      */       {
/*      */         public boolean contains(Object o) {
/* 1637 */           if (o instanceof Set) {
/* 1638 */             Set<?> s = (Set)o;
/* 1639 */             return (s.size() == size && index.keySet().containsAll(s));
/*      */           } 
/* 1641 */           return false;
/*      */         }
/*      */ 
/*      */         
/*      */         public Iterator<Set<E>> iterator() {
/* 1646 */           return new AbstractIterator() {
/* 1647 */               final BitSet bits = new BitSet(index.size());
/*      */ 
/*      */               
/*      */               protected Set<E> computeNext() {
/* 1651 */                 if (this.bits.isEmpty()) {
/* 1652 */                   this.bits.set(0, size);
/*      */                 } else {
/* 1654 */                   int firstSetBit = this.bits.nextSetBit(0);
/* 1655 */                   int bitToFlip = this.bits.nextClearBit(firstSetBit);
/*      */                   
/* 1657 */                   if (bitToFlip == index.size()) {
/* 1658 */                     return endOfData();
/*      */                   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */                   
/* 1674 */                   this.bits.set(0, bitToFlip - firstSetBit - 1);
/* 1675 */                   this.bits.clear(bitToFlip - firstSetBit - 1, bitToFlip);
/* 1676 */                   this.bits.set(bitToFlip);
/*      */                 } 
/* 1678 */                 final BitSet copy = (BitSet)this.bits.clone();
/* 1679 */                 return new AbstractSet<E>()
/*      */                   {
/*      */                     public boolean contains(Object o) {
/* 1682 */                       Integer i = (Integer)index.get(o);
/* 1683 */                       return (i != null && copy.get(i.intValue()));
/*      */                     }
/*      */ 
/*      */                     
/*      */                     public Iterator<E> iterator() {
/* 1688 */                       return new AbstractIterator() {
/* 1689 */                           int i = -1;
/*      */ 
/*      */                           
/*      */                           protected E computeNext() {
/* 1693 */                             this.i = copy.nextSetBit(this.i + 1);
/* 1694 */                             if (this.i == -1) {
/* 1695 */                               return endOfData();
/*      */                             }
/* 1697 */                             return index.keySet().asList().get(this.i);
/*      */                           }
/*      */                         };
/*      */                     }
/*      */ 
/*      */                     
/*      */                     public int size() {
/* 1704 */                       return size;
/*      */                     }
/*      */                   };
/*      */               }
/*      */             };
/*      */         }
/*      */ 
/*      */         
/*      */         public int size() {
/* 1713 */           return IntMath.binomial(index.size(), size);
/*      */         }
/*      */ 
/*      */         
/*      */         public String toString() {
/* 1718 */           return "Sets.combinations(" + index.keySet() + ", " + size + ")";
/*      */         }
/*      */       };
/*      */   }
/*      */ 
/*      */   
/*      */   static int hashCodeImpl(Set<?> s) {
/* 1725 */     int hashCode = 0;
/* 1726 */     for (Object o : s) {
/* 1727 */       hashCode += (o != null) ? o.hashCode() : 0;
/*      */       
/* 1729 */       hashCode = hashCode ^ 0xFFFFFFFF ^ 0xFFFFFFFF;
/*      */     } 
/*      */     
/* 1732 */     return hashCode;
/*      */   }
/*      */ 
/*      */   
/*      */   static boolean equalsImpl(Set<?> s, Object object) {
/* 1737 */     if (s == object) {
/* 1738 */       return true;
/*      */     }
/* 1740 */     if (object instanceof Set) {
/* 1741 */       Set<?> o = (Set)object;
/*      */       
/*      */       try {
/* 1744 */         return (s.size() == o.size() && s.containsAll(o));
/* 1745 */       } catch (NullPointerException|ClassCastException ignored) {
/* 1746 */         return false;
/*      */       } 
/*      */     } 
/* 1749 */     return false;
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
/*      */   public static <E> NavigableSet<E> unmodifiableNavigableSet(NavigableSet<E> set) {
/* 1766 */     if (set instanceof ImmutableCollection || set instanceof UnmodifiableNavigableSet) {
/* 1767 */       return set;
/*      */     }
/* 1769 */     return new UnmodifiableNavigableSet<>(set);
/*      */   }
/*      */   
/*      */   static final class UnmodifiableNavigableSet<E> extends ForwardingSortedSet<E> implements NavigableSet<E>, Serializable { private final NavigableSet<E> delegate;
/*      */     private final SortedSet<E> unmodifiableDelegate;
/*      */     private transient UnmodifiableNavigableSet<E> descendingSet;
/*      */     private static final long serialVersionUID = 0L;
/*      */     
/*      */     UnmodifiableNavigableSet(NavigableSet<E> delegate) {
/* 1778 */       this.delegate = (NavigableSet<E>)Preconditions.checkNotNull(delegate);
/* 1779 */       this.unmodifiableDelegate = Collections.unmodifiableSortedSet(delegate);
/*      */     }
/*      */ 
/*      */     
/*      */     protected SortedSet<E> delegate() {
/* 1784 */       return this.unmodifiableDelegate;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public boolean removeIf(Predicate<? super E> filter) {
/* 1791 */       throw new UnsupportedOperationException();
/*      */     }
/*      */ 
/*      */     
/*      */     public Stream<E> stream() {
/* 1796 */       return this.delegate.stream();
/*      */     }
/*      */ 
/*      */     
/*      */     public Stream<E> parallelStream() {
/* 1801 */       return this.delegate.parallelStream();
/*      */     }
/*      */ 
/*      */     
/*      */     public void forEach(Consumer<? super E> action) {
/* 1806 */       this.delegate.forEach(action);
/*      */     }
/*      */ 
/*      */     
/*      */     public E lower(E e) {
/* 1811 */       return this.delegate.lower(e);
/*      */     }
/*      */ 
/*      */     
/*      */     public E floor(E e) {
/* 1816 */       return this.delegate.floor(e);
/*      */     }
/*      */ 
/*      */     
/*      */     public E ceiling(E e) {
/* 1821 */       return this.delegate.ceiling(e);
/*      */     }
/*      */ 
/*      */     
/*      */     public E higher(E e) {
/* 1826 */       return this.delegate.higher(e);
/*      */     }
/*      */ 
/*      */     
/*      */     public E pollFirst() {
/* 1831 */       throw new UnsupportedOperationException();
/*      */     }
/*      */ 
/*      */     
/*      */     public E pollLast() {
/* 1836 */       throw new UnsupportedOperationException();
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public NavigableSet<E> descendingSet() {
/* 1843 */       UnmodifiableNavigableSet<E> result = this.descendingSet;
/* 1844 */       if (result == null) {
/* 1845 */         result = this.descendingSet = new UnmodifiableNavigableSet(this.delegate.descendingSet());
/* 1846 */         result.descendingSet = this;
/*      */       } 
/* 1848 */       return result;
/*      */     }
/*      */ 
/*      */     
/*      */     public Iterator<E> descendingIterator() {
/* 1853 */       return Iterators.unmodifiableIterator(this.delegate.descendingIterator());
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     public NavigableSet<E> subSet(E fromElement, boolean fromInclusive, E toElement, boolean toInclusive) {
/* 1859 */       return Sets.unmodifiableNavigableSet(this.delegate
/* 1860 */           .subSet(fromElement, fromInclusive, toElement, toInclusive));
/*      */     }
/*      */ 
/*      */     
/*      */     public NavigableSet<E> headSet(E toElement, boolean inclusive) {
/* 1865 */       return Sets.unmodifiableNavigableSet(this.delegate.headSet(toElement, inclusive));
/*      */     }
/*      */ 
/*      */     
/*      */     public NavigableSet<E> tailSet(E fromElement, boolean inclusive) {
/* 1870 */       return Sets.unmodifiableNavigableSet(this.delegate.tailSet(fromElement, inclusive));
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
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
/*      */   public static <E> NavigableSet<E> synchronizedNavigableSet(NavigableSet<E> navigableSet) {
/* 1923 */     return Synchronized.navigableSet(navigableSet);
/*      */   }
/*      */ 
/*      */   
/*      */   static boolean removeAllImpl(Set<?> set, Iterator<?> iterator) {
/* 1928 */     boolean changed = false;
/* 1929 */     while (iterator.hasNext()) {
/* 1930 */       changed |= set.remove(iterator.next());
/*      */     }
/* 1932 */     return changed;
/*      */   }
/*      */   
/*      */   static boolean removeAllImpl(Set<?> set, Collection<?> collection) {
/* 1936 */     Preconditions.checkNotNull(collection);
/* 1937 */     if (collection instanceof Multiset) {
/* 1938 */       collection = ((Multiset)collection).elementSet();
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1947 */     if (collection instanceof Set && collection.size() > set.size()) {
/* 1948 */       return Iterators.removeAll(set.iterator(), collection);
/*      */     }
/* 1950 */     return removeAllImpl(set, collection.iterator());
/*      */   }
/*      */   
/*      */   @GwtIncompatible
/*      */   static class DescendingSet<E>
/*      */     extends ForwardingNavigableSet<E> {
/*      */     private final NavigableSet<E> forward;
/*      */     
/*      */     DescendingSet(NavigableSet<E> forward) {
/* 1959 */       this.forward = forward;
/*      */     }
/*      */ 
/*      */     
/*      */     protected NavigableSet<E> delegate() {
/* 1964 */       return this.forward;
/*      */     }
/*      */ 
/*      */     
/*      */     public E lower(E e) {
/* 1969 */       return this.forward.higher(e);
/*      */     }
/*      */ 
/*      */     
/*      */     public E floor(E e) {
/* 1974 */       return this.forward.ceiling(e);
/*      */     }
/*      */ 
/*      */     
/*      */     public E ceiling(E e) {
/* 1979 */       return this.forward.floor(e);
/*      */     }
/*      */ 
/*      */     
/*      */     public E higher(E e) {
/* 1984 */       return this.forward.lower(e);
/*      */     }
/*      */ 
/*      */     
/*      */     public E pollFirst() {
/* 1989 */       return this.forward.pollLast();
/*      */     }
/*      */ 
/*      */     
/*      */     public E pollLast() {
/* 1994 */       return this.forward.pollFirst();
/*      */     }
/*      */ 
/*      */     
/*      */     public NavigableSet<E> descendingSet() {
/* 1999 */       return this.forward;
/*      */     }
/*      */ 
/*      */     
/*      */     public Iterator<E> descendingIterator() {
/* 2004 */       return this.forward.iterator();
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     public NavigableSet<E> subSet(E fromElement, boolean fromInclusive, E toElement, boolean toInclusive) {
/* 2010 */       return this.forward.subSet(toElement, toInclusive, fromElement, fromInclusive).descendingSet();
/*      */     }
/*      */ 
/*      */     
/*      */     public SortedSet<E> subSet(E fromElement, E toElement) {
/* 2015 */       return standardSubSet(fromElement, toElement);
/*      */     }
/*      */ 
/*      */     
/*      */     public NavigableSet<E> headSet(E toElement, boolean inclusive) {
/* 2020 */       return this.forward.tailSet(toElement, inclusive).descendingSet();
/*      */     }
/*      */ 
/*      */     
/*      */     public SortedSet<E> headSet(E toElement) {
/* 2025 */       return standardHeadSet(toElement);
/*      */     }
/*      */ 
/*      */     
/*      */     public NavigableSet<E> tailSet(E fromElement, boolean inclusive) {
/* 2030 */       return this.forward.headSet(fromElement, inclusive).descendingSet();
/*      */     }
/*      */ 
/*      */     
/*      */     public SortedSet<E> tailSet(E fromElement) {
/* 2035 */       return standardTailSet(fromElement);
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     public Comparator<? super E> comparator() {
/* 2041 */       Comparator<? super E> forwardComparator = this.forward.comparator();
/* 2042 */       if (forwardComparator == null) {
/* 2043 */         return Ordering.<Comparable>natural().reverse();
/*      */       }
/* 2045 */       return reverse(forwardComparator);
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     private static <T> Ordering<T> reverse(Comparator<T> forward) {
/* 2051 */       return Ordering.<T>from(forward).reverse();
/*      */     }
/*      */ 
/*      */     
/*      */     public E first() {
/* 2056 */       return this.forward.last();
/*      */     }
/*      */ 
/*      */     
/*      */     public E last() {
/* 2061 */       return this.forward.first();
/*      */     }
/*      */ 
/*      */     
/*      */     public Iterator<E> iterator() {
/* 2066 */       return this.forward.descendingIterator();
/*      */     }
/*      */ 
/*      */     
/*      */     public Object[] toArray() {
/* 2071 */       return standardToArray();
/*      */     }
/*      */ 
/*      */     
/*      */     public <T> T[] toArray(T[] array) {
/* 2076 */       return (T[])standardToArray((Object[])array);
/*      */     }
/*      */ 
/*      */     
/*      */     public String toString() {
/* 2081 */       return standardToString();
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
/*      */   @Beta
/*      */   @GwtIncompatible
/*      */   public static <K extends Comparable<? super K>> NavigableSet<K> subSet(NavigableSet<K> set, Range<K> range) {
/* 2105 */     if (set.comparator() != null && set
/* 2106 */       .comparator() != Ordering.natural() && range
/* 2107 */       .hasLowerBound() && range
/* 2108 */       .hasUpperBound()) {
/* 2109 */       Preconditions.checkArgument(
/* 2110 */           (set.comparator().compare(range.lowerEndpoint(), range.upperEndpoint()) <= 0), "set is using a custom comparator which is inconsistent with the natural ordering.");
/*      */     }
/*      */     
/* 2113 */     if (range.hasLowerBound() && range.hasUpperBound())
/* 2114 */       return set.subSet(range
/* 2115 */           .lowerEndpoint(), 
/* 2116 */           (range.lowerBoundType() == BoundType.CLOSED), range
/* 2117 */           .upperEndpoint(), 
/* 2118 */           (range.upperBoundType() == BoundType.CLOSED)); 
/* 2119 */     if (range.hasLowerBound())
/* 2120 */       return set.tailSet(range.lowerEndpoint(), (range.lowerBoundType() == BoundType.CLOSED)); 
/* 2121 */     if (range.hasUpperBound()) {
/* 2122 */       return set.headSet(range.upperEndpoint(), (range.upperBoundType() == BoundType.CLOSED));
/*      */     }
/* 2124 */     return (NavigableSet<K>)Preconditions.checkNotNull(set);
/*      */   }
/*      */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\google\common\collect\Sets.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */