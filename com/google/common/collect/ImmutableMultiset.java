/*     */ package com.google.common.collect;
/*     */ 
/*     */ import com.google.common.annotations.GwtCompatible;
/*     */ import com.google.common.annotations.GwtIncompatible;
/*     */ import com.google.common.annotations.VisibleForTesting;
/*     */ import com.google.common.base.Preconditions;
/*     */ import com.google.errorprone.annotations.CanIgnoreReturnValue;
/*     */ import com.google.errorprone.annotations.concurrent.LazyInit;
/*     */ import java.io.Serializable;
/*     */ import java.util.Arrays;
/*     */ import java.util.Collection;
/*     */ import java.util.Collections;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Set;
/*     */ import java.util.function.Function;
/*     */ import java.util.function.ToIntFunction;
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
/*     */ @GwtCompatible(serializable = true, emulated = true)
/*     */ public abstract class ImmutableMultiset<E>
/*     */   extends ImmutableMultisetGwtSerializationDependencies<E>
/*     */   implements Multiset<E>
/*     */ {
/*     */   @LazyInit
/*     */   private transient ImmutableList<E> asList;
/*     */   @LazyInit
/*     */   private transient ImmutableSet<Multiset.Entry<E>> entrySet;
/*     */   
/*     */   public static <E> Collector<E, ?, ImmutableMultiset<E>> toImmutableMultiset() {
/*  67 */     return toImmutableMultiset((Function)Function.identity(), e -> 1);
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
/*     */   public static <T, E> Collector<T, ?, ImmutableMultiset<E>> toImmutableMultiset(Function<? super T, ? extends E> elementFunction, ToIntFunction<? super T> countFunction) {
/*  83 */     Preconditions.checkNotNull(elementFunction);
/*  84 */     Preconditions.checkNotNull(countFunction);
/*  85 */     return Collector.of(LinkedHashMultiset::create, (multiset, t) -> multiset.add(Preconditions.checkNotNull(elementFunction.apply(t)), countFunction.applyAsInt(t)), (multiset1, multiset2) -> { multiset1.addAll(multiset2); return multiset1; }multiset -> copyFromEntries(multiset.entrySet()), new Collector.Characteristics[0]);
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
/*     */   public static <E> ImmutableMultiset<E> of() {
/*  99 */     return (ImmutableMultiset)RegularImmutableMultiset.EMPTY;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <E> ImmutableMultiset<E> of(E element) {
/* 110 */     return copyFromElements((E[])new Object[] { element });
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <E> ImmutableMultiset<E> of(E e1, E e2) {
/* 121 */     return copyFromElements((E[])new Object[] { e1, e2 });
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
/*     */   public static <E> ImmutableMultiset<E> of(E e1, E e2, E e3) {
/* 133 */     return copyFromElements((E[])new Object[] { e1, e2, e3 });
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
/*     */   public static <E> ImmutableMultiset<E> of(E e1, E e2, E e3, E e4) {
/* 145 */     return copyFromElements((E[])new Object[] { e1, e2, e3, e4 });
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
/*     */   public static <E> ImmutableMultiset<E> of(E e1, E e2, E e3, E e4, E e5) {
/* 157 */     return copyFromElements((E[])new Object[] { e1, e2, e3, e4, e5 });
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
/*     */   public static <E> ImmutableMultiset<E> of(E e1, E e2, E e3, E e4, E e5, E e6, E... others) {
/* 169 */     return (new Builder<>()).add(e1).add(e2).add(e3).add(e4).add(e5).add(e6).add(others).build();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <E> ImmutableMultiset<E> copyOf(E[] elements) {
/* 180 */     return copyFromElements(elements);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <E> ImmutableMultiset<E> copyOf(Iterable<? extends E> elements) {
/* 190 */     if (elements instanceof ImmutableMultiset) {
/*     */       
/* 192 */       ImmutableMultiset<E> result = (ImmutableMultiset)elements;
/* 193 */       if (!result.isPartialView()) {
/* 194 */         return result;
/*     */       }
/*     */     } 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 201 */     Multiset<? extends E> multiset = (elements instanceof Multiset) ? Multisets.<E>cast(elements) : LinkedHashMultiset.<E>create(elements);
/*     */     
/* 203 */     return copyFromEntries(multiset.entrySet());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <E> ImmutableMultiset<E> copyOf(Iterator<? extends E> elements) {
/* 213 */     Multiset<E> multiset = LinkedHashMultiset.create();
/* 214 */     Iterators.addAll(multiset, elements);
/* 215 */     return copyFromEntries(multiset.entrySet());
/*     */   }
/*     */   
/*     */   private static <E> ImmutableMultiset<E> copyFromElements(E... elements) {
/* 219 */     Multiset<E> multiset = LinkedHashMultiset.create();
/* 220 */     Collections.addAll(multiset, elements);
/* 221 */     return copyFromEntries(multiset.entrySet());
/*     */   }
/*     */ 
/*     */   
/*     */   static <E> ImmutableMultiset<E> copyFromEntries(Collection<? extends Multiset.Entry<? extends E>> entries) {
/* 226 */     if (entries.isEmpty()) {
/* 227 */       return of();
/*     */     }
/* 229 */     return RegularImmutableMultiset.create(entries);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public UnmodifiableIterator<E> iterator() {
/* 237 */     final Iterator<Multiset.Entry<E>> entryIterator = entrySet().iterator();
/* 238 */     return new UnmodifiableIterator<E>()
/*     */       {
/*     */         int remaining;
/*     */         E element;
/*     */         
/*     */         public boolean hasNext() {
/* 244 */           return (this.remaining > 0 || entryIterator.hasNext());
/*     */         }
/*     */ 
/*     */         
/*     */         public E next() {
/* 249 */           if (this.remaining <= 0) {
/* 250 */             Multiset.Entry<E> entry = entryIterator.next();
/* 251 */             this.element = entry.getElement();
/* 252 */             this.remaining = entry.getCount();
/*     */           } 
/* 254 */           this.remaining--;
/* 255 */           return this.element;
/*     */         }
/*     */       };
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ImmutableList<E> asList() {
/* 264 */     ImmutableList<E> result = this.asList;
/* 265 */     return (result == null) ? (this.asList = super.asList()) : result;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean contains(Object object) {
/* 270 */     return (count(object) > 0);
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
/*     */   public final int add(E element, int occurrences) {
/* 283 */     throw new UnsupportedOperationException();
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
/*     */   public final int remove(Object element, int occurrences) {
/* 296 */     throw new UnsupportedOperationException();
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
/*     */   public final int setCount(E element, int count) {
/* 309 */     throw new UnsupportedOperationException();
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
/*     */   public final boolean setCount(E element, int oldCount, int newCount) {
/* 322 */     throw new UnsupportedOperationException();
/*     */   }
/*     */ 
/*     */   
/*     */   @GwtIncompatible
/*     */   int copyIntoArray(Object[] dst, int offset) {
/* 328 */     for (UnmodifiableIterator<Multiset.Entry<E>> unmodifiableIterator = entrySet().iterator(); unmodifiableIterator.hasNext(); ) { Multiset.Entry<E> entry = unmodifiableIterator.next();
/* 329 */       Arrays.fill(dst, offset, offset + entry.getCount(), entry.getElement());
/* 330 */       offset += entry.getCount(); }
/*     */     
/* 332 */     return offset;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean equals(Object object) {
/* 337 */     return Multisets.equalsImpl(this, object);
/*     */   }
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 342 */     return Sets.hashCodeImpl(entrySet());
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 347 */     return entrySet().toString();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ImmutableSet<Multiset.Entry<E>> entrySet() {
/* 358 */     ImmutableSet<Multiset.Entry<E>> es = this.entrySet;
/* 359 */     return (es == null) ? (this.entrySet = createEntrySet()) : es;
/*     */   }
/*     */   
/*     */   private ImmutableSet<Multiset.Entry<E>> createEntrySet() {
/* 363 */     return isEmpty() ? ImmutableSet.<Multiset.Entry<E>>of() : new EntrySet();
/*     */   }
/*     */   
/*     */   private final class EntrySet extends IndexedImmutableSet<Multiset.Entry<E>> {
/*     */     private static final long serialVersionUID = 0L;
/*     */     
/*     */     private EntrySet() {}
/*     */     
/*     */     boolean isPartialView() {
/* 372 */       return ImmutableMultiset.this.isPartialView();
/*     */     }
/*     */ 
/*     */     
/*     */     Multiset.Entry<E> get(int index) {
/* 377 */       return ImmutableMultiset.this.getEntry(index);
/*     */     }
/*     */ 
/*     */     
/*     */     public int size() {
/* 382 */       return ImmutableMultiset.this.elementSet().size();
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean contains(Object o) {
/* 387 */       if (o instanceof Multiset.Entry) {
/* 388 */         Multiset.Entry<?> entry = (Multiset.Entry)o;
/* 389 */         if (entry.getCount() <= 0) {
/* 390 */           return false;
/*     */         }
/* 392 */         int count = ImmutableMultiset.this.count(entry.getElement());
/* 393 */         return (count == entry.getCount());
/*     */       } 
/* 395 */       return false;
/*     */     }
/*     */ 
/*     */     
/*     */     public int hashCode() {
/* 400 */       return ImmutableMultiset.this.hashCode();
/*     */     }
/*     */ 
/*     */     
/*     */     @GwtIncompatible
/*     */     Object writeReplace() {
/* 406 */       return new ImmutableMultiset.EntrySetSerializedForm(ImmutableMultiset.this);
/*     */     }
/*     */   }
/*     */   
/*     */   @GwtIncompatible
/*     */   static class EntrySetSerializedForm<E>
/*     */     implements Serializable
/*     */   {
/*     */     final ImmutableMultiset<E> multiset;
/*     */     
/*     */     EntrySetSerializedForm(ImmutableMultiset<E> multiset) {
/* 417 */       this.multiset = multiset;
/*     */     }
/*     */     
/*     */     Object readResolve() {
/* 421 */       return this.multiset.entrySet();
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   @GwtIncompatible
/*     */   Object writeReplace() {
/* 428 */     return new SerializedForm(this);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <E> Builder<E> builder() {
/* 436 */     return new Builder<>();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public abstract ImmutableSet<E> elementSet();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   abstract Multiset.Entry<E> getEntry(int paramInt);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static class Builder<E>
/*     */     extends ImmutableCollection.Builder<E>
/*     */   {
/*     */     final Multiset<E> contents;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public Builder() {
/* 466 */       this(LinkedHashMultiset.create());
/*     */     }
/*     */     
/*     */     Builder(Multiset<E> contents) {
/* 470 */       this.contents = contents;
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
/* 483 */       this.contents.add((E)Preconditions.checkNotNull(element));
/* 484 */       return this;
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
/* 497 */       super.add(elements);
/* 498 */       return this;
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
/*     */     
/*     */     @CanIgnoreReturnValue
/*     */     public Builder<E> addCopies(E element, int occurrences) {
/* 514 */       this.contents.add((E)Preconditions.checkNotNull(element), occurrences);
/* 515 */       return this;
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
/*     */     public Builder<E> setCount(E element, int count) {
/* 530 */       this.contents.setCount((E)Preconditions.checkNotNull(element), count);
/* 531 */       return this;
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
/* 544 */       if (elements instanceof Multiset) {
/* 545 */         Multiset<? extends E> multiset = Multisets.cast(elements);
/* 546 */         multiset.forEachEntry((e, n) -> this.contents.add((E)Preconditions.checkNotNull(e), n));
/*     */       } else {
/* 548 */         super.addAll(elements);
/*     */       } 
/* 550 */       return this;
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
/* 563 */       super.addAll(elements);
/* 564 */       return this;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public ImmutableMultiset<E> build() {
/* 573 */       return ImmutableMultiset.copyOf(this.contents);
/*     */     }
/*     */     
/*     */     @VisibleForTesting
/*     */     ImmutableMultiset<E> buildJdkBacked() {
/* 578 */       if (this.contents.isEmpty()) {
/* 579 */         return ImmutableMultiset.of();
/*     */       }
/* 581 */       return JdkBackedImmutableMultiset.create(this.contents.entrySet());
/*     */     }
/*     */   }
/*     */   
/*     */   static final class ElementSet<E>
/*     */     extends ImmutableSet.Indexed<E> {
/*     */     private final List<Multiset.Entry<E>> entries;
/*     */     private final Multiset<E> delegate;
/*     */     
/*     */     ElementSet(List<Multiset.Entry<E>> entries, Multiset<E> delegate) {
/* 591 */       this.entries = entries;
/* 592 */       this.delegate = delegate;
/*     */     }
/*     */ 
/*     */     
/*     */     E get(int index) {
/* 597 */       return ((Multiset.Entry<E>)this.entries.get(index)).getElement();
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean contains(Object object) {
/* 602 */       return this.delegate.contains(object);
/*     */     }
/*     */ 
/*     */     
/*     */     boolean isPartialView() {
/* 607 */       return true;
/*     */     }
/*     */ 
/*     */     
/*     */     public int size() {
/* 612 */       return this.entries.size();
/*     */     } }
/*     */   
/*     */   static final class SerializedForm implements Serializable {
/*     */     final Object[] elements;
/*     */     final int[] counts;
/*     */     private static final long serialVersionUID = 0L;
/*     */     
/*     */     SerializedForm(Multiset<?> multiset) {
/* 621 */       int distinct = multiset.entrySet().size();
/* 622 */       this.elements = new Object[distinct];
/* 623 */       this.counts = new int[distinct];
/* 624 */       int i = 0;
/* 625 */       for (Multiset.Entry<?> entry : multiset.entrySet()) {
/* 626 */         this.elements[i] = entry.getElement();
/* 627 */         this.counts[i] = entry.getCount();
/* 628 */         i++;
/*     */       } 
/*     */     }
/*     */     
/*     */     Object readResolve() {
/* 633 */       LinkedHashMultiset<Object> multiset = LinkedHashMultiset.create(this.elements.length);
/* 634 */       for (int i = 0; i < this.elements.length; i++) {
/* 635 */         multiset.add(this.elements[i], this.counts[i]);
/*     */       }
/* 637 */       return ImmutableMultiset.copyOf(multiset);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\google\common\collect\ImmutableMultiset.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */