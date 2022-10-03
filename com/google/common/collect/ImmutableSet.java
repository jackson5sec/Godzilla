/*     */ package com.google.common.collect;
/*     */ 
/*     */ import com.google.common.annotations.Beta;
/*     */ import com.google.common.annotations.GwtCompatible;
/*     */ import com.google.common.annotations.VisibleForTesting;
/*     */ import com.google.common.base.Preconditions;
/*     */ import com.google.common.math.IntMath;
/*     */ import com.google.errorprone.annotations.CanIgnoreReturnValue;
/*     */ import com.google.errorprone.annotations.concurrent.LazyInit;
/*     */ import com.google.j2objc.annotations.RetainedWith;
/*     */ import java.io.Serializable;
/*     */ import java.math.RoundingMode;
/*     */ import java.util.Arrays;
/*     */ import java.util.Collection;
/*     */ import java.util.EnumSet;
/*     */ import java.util.Iterator;
/*     */ import java.util.Set;
/*     */ import java.util.Spliterator;
/*     */ import java.util.function.Consumer;
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
/*     */ @GwtCompatible(serializable = true, emulated = true)
/*     */ public abstract class ImmutableSet<E>
/*     */   extends ImmutableCollection<E>
/*     */   implements Set<E>
/*     */ {
/*     */   static final int SPLITERATOR_CHARACTERISTICS = 1297;
/*     */   @LazyInit
/*     */   @RetainedWith
/*     */   private transient ImmutableList<E> asList;
/*     */   static final int MAX_TABLE_SIZE = 1073741824;
/*     */   private static final double DESIRED_LOAD_FACTOR = 0.7D;
/*     */   private static final int CUTOFF = 751619276;
/*     */   static final double HASH_FLOODING_FPP = 0.001D;
/*     */   static final int MAX_RUN_MULTIPLIER = 13;
/*     */   
/*     */   public static <E> Collector<E, ?, ImmutableSet<E>> toImmutableSet() {
/*  66 */     return CollectCollectors.toImmutableSet();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <E> ImmutableSet<E> of() {
/*  75 */     return RegularImmutableSet.EMPTY;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <E> ImmutableSet<E> of(E element) {
/*  84 */     return new SingletonImmutableSet<>(element);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <E> ImmutableSet<E> of(E e1, E e2) {
/*  93 */     return construct(2, new Object[] { e1, e2 });
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <E> ImmutableSet<E> of(E e1, E e2, E e3) {
/* 102 */     return construct(3, new Object[] { e1, e2, e3 });
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <E> ImmutableSet<E> of(E e1, E e2, E e3, E e4) {
/* 111 */     return construct(4, new Object[] { e1, e2, e3, e4 });
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <E> ImmutableSet<E> of(E e1, E e2, E e3, E e4, E e5) {
/* 120 */     return construct(5, new Object[] { e1, e2, e3, e4, e5 });
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
/*     */   @SafeVarargs
/*     */   public static <E> ImmutableSet<E> of(E e1, E e2, E e3, E e4, E e5, E e6, E... others) {
/* 134 */     Preconditions.checkArgument((others.length <= 2147483641), "the total number of elements must fit in an int");
/*     */     
/* 136 */     int paramCount = 6;
/* 137 */     Object[] elements = new Object[6 + others.length];
/* 138 */     elements[0] = e1;
/* 139 */     elements[1] = e2;
/* 140 */     elements[2] = e3;
/* 141 */     elements[3] = e4;
/* 142 */     elements[4] = e5;
/* 143 */     elements[5] = e6;
/* 144 */     System.arraycopy(others, 0, elements, 6, others.length);
/* 145 */     return construct(elements.length, elements);
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
/*     */   private static <E> ImmutableSet<E> construct(int n, Object... elements) {
/*     */     E elem;
/* 163 */     switch (n) {
/*     */       case 0:
/* 165 */         return of();
/*     */       
/*     */       case 1:
/* 168 */         elem = (E)elements[0];
/* 169 */         return of(elem);
/*     */     } 
/* 171 */     SetBuilderImpl<E> builder = new RegularSetBuilderImpl<>(4);
/*     */     
/* 173 */     for (int i = 0; i < n; i++) {
/*     */       
/* 175 */       E e = (E)Preconditions.checkNotNull(elements[i]);
/* 176 */       builder = builder.add(e);
/*     */     } 
/* 178 */     return builder.review().build();
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
/*     */   public static <E> ImmutableSet<E> copyOf(Collection<? extends E> elements) {
/* 200 */     if (elements instanceof ImmutableSet && !(elements instanceof java.util.SortedSet)) {
/*     */       
/* 202 */       ImmutableSet<E> set = (ImmutableSet)elements;
/* 203 */       if (!set.isPartialView()) {
/* 204 */         return set;
/*     */       }
/* 206 */     } else if (elements instanceof EnumSet) {
/* 207 */       return copyOfEnumSet((EnumSet)elements);
/*     */     } 
/* 209 */     Object[] array = elements.toArray();
/* 210 */     return construct(array.length, array);
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
/*     */   public static <E> ImmutableSet<E> copyOf(Iterable<? extends E> elements) {
/* 226 */     return (elements instanceof Collection) ? 
/* 227 */       copyOf((Collection<? extends E>)elements) : 
/* 228 */       copyOf(elements.iterator());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <E> ImmutableSet<E> copyOf(Iterator<? extends E> elements) {
/* 239 */     if (!elements.hasNext()) {
/* 240 */       return of();
/*     */     }
/* 242 */     E first = elements.next();
/* 243 */     if (!elements.hasNext()) {
/* 244 */       return of(first);
/*     */     }
/* 246 */     return (new Builder<>()).add(first).addAll(elements).build();
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
/*     */   public static <E> ImmutableSet<E> copyOf(E[] elements) {
/* 258 */     switch (elements.length) {
/*     */       case 0:
/* 260 */         return of();
/*     */       case 1:
/* 262 */         return of(elements[0]);
/*     */     } 
/* 264 */     return construct(elements.length, (Object[])elements.clone());
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static ImmutableSet copyOfEnumSet(EnumSet<Enum> enumSet) {
/* 270 */     return ImmutableEnumSet.asImmutable(EnumSet.copyOf(enumSet));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   boolean isHashCodeFast() {
/* 277 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean equals(Object object) {
/* 282 */     if (object == this)
/* 283 */       return true; 
/* 284 */     if (object instanceof ImmutableSet && 
/* 285 */       isHashCodeFast() && ((ImmutableSet)object)
/* 286 */       .isHashCodeFast() && 
/* 287 */       hashCode() != object.hashCode()) {
/* 288 */       return false;
/*     */     }
/* 290 */     return Sets.equalsImpl(this, object);
/*     */   }
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 295 */     return Sets.hashCodeImpl(this);
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
/*     */   public ImmutableList<E> asList() {
/* 307 */     ImmutableList<E> result = this.asList;
/* 308 */     return (result == null) ? (this.asList = createAsList()) : result;
/*     */   }
/*     */   
/*     */   ImmutableList<E> createAsList() {
/* 312 */     return new RegularImmutableAsList<>(this, toArray());
/*     */   }
/*     */ 
/*     */   
/*     */   static abstract class Indexed<E>
/*     */     extends ImmutableSet<E>
/*     */   {
/*     */     public UnmodifiableIterator<E> iterator() {
/* 320 */       return asList().iterator();
/*     */     }
/*     */ 
/*     */     
/*     */     public Spliterator<E> spliterator() {
/* 325 */       return CollectSpliterators.indexed(size(), 1297, this::get);
/*     */     }
/*     */ 
/*     */     
/*     */     public void forEach(Consumer<? super E> consumer) {
/* 330 */       Preconditions.checkNotNull(consumer);
/* 331 */       int n = size();
/* 332 */       for (int i = 0; i < n; i++) {
/* 333 */         consumer.accept(get(i));
/*     */       }
/*     */     }
/*     */ 
/*     */     
/*     */     int copyIntoArray(Object[] dst, int offset) {
/* 339 */       return asList().copyIntoArray(dst, offset);
/*     */     }
/*     */ 
/*     */     
/*     */     ImmutableList<E> createAsList() {
/* 344 */       return new ImmutableAsList<E>()
/*     */         {
/*     */           public E get(int index) {
/* 347 */             return ImmutableSet.Indexed.this.get(index);
/*     */           }
/*     */ 
/*     */           
/*     */           ImmutableSet.Indexed<E> delegateCollection() {
/* 352 */             return ImmutableSet.Indexed.this;
/*     */           }
/*     */         };
/*     */     }
/*     */ 
/*     */     
/*     */     abstract E get(int param1Int);
/*     */   }
/*     */ 
/*     */   
/*     */   private static class SerializedForm
/*     */     implements Serializable
/*     */   {
/*     */     final Object[] elements;
/*     */     private static final long serialVersionUID = 0L;
/*     */     
/*     */     SerializedForm(Object[] elements) {
/* 369 */       this.elements = elements;
/*     */     }
/*     */     
/*     */     Object readResolve() {
/* 373 */       return ImmutableSet.copyOf(this.elements);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   Object writeReplace() {
/* 381 */     return new SerializedForm(toArray());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <E> Builder<E> builder() {
/* 389 */     return new Builder<>();
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
/* 406 */     CollectPreconditions.checkNonnegative(expectedSize, "expectedSize");
/* 407 */     return new Builder<>(expectedSize);
/*     */   }
/*     */ 
/*     */   
/*     */   static Object[] rebuildHashTable(int newTableSize, Object[] elements, int n) {
/* 412 */     Object[] hashTable = new Object[newTableSize];
/* 413 */     int mask = hashTable.length - 1;
/* 414 */     for (int i = 0; i < n; ) {
/* 415 */       Object e = elements[i];
/* 416 */       int j0 = Hashing.smear(e.hashCode());
/* 417 */       int j = j0; for (;; i++) {
/* 418 */         int index = j & mask;
/* 419 */         if (hashTable[index] == null) {
/* 420 */           hashTable[index] = e;
/*     */         } else {
/*     */           j++; continue;
/*     */         } 
/*     */       } 
/* 425 */     }  return hashTable;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static class Builder<E>
/*     */     extends ImmutableCollection.Builder<E>
/*     */   {
/*     */     private ImmutableSet.SetBuilderImpl<E> impl;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     boolean forceCopy;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public Builder() {
/* 451 */       this(4);
/*     */     }
/*     */     
/*     */     Builder(int capacity) {
/* 455 */       this.impl = new ImmutableSet.RegularSetBuilderImpl<>(capacity);
/*     */     }
/*     */     
/*     */     Builder(boolean subclass) {
/* 459 */       this.impl = null;
/*     */     }
/*     */     
/*     */     @VisibleForTesting
/*     */     void forceJdk() {
/* 464 */       this.impl = new ImmutableSet.JdkBackedSetBuilderImpl<>(this.impl);
/*     */     }
/*     */     
/*     */     final void copyIfNecessary() {
/* 468 */       if (this.forceCopy) {
/* 469 */         copy();
/* 470 */         this.forceCopy = false;
/*     */       } 
/*     */     }
/*     */     
/*     */     void copy() {
/* 475 */       this.impl = this.impl.copy();
/*     */     }
/*     */ 
/*     */     
/*     */     @CanIgnoreReturnValue
/*     */     public Builder<E> add(E element) {
/* 481 */       Preconditions.checkNotNull(element);
/* 482 */       copyIfNecessary();
/* 483 */       this.impl = this.impl.add(element);
/* 484 */       return this;
/*     */     }
/*     */ 
/*     */     
/*     */     @CanIgnoreReturnValue
/*     */     public Builder<E> add(E... elements) {
/* 490 */       super.add(elements);
/* 491 */       return this;
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
/*     */     @CanIgnoreReturnValue
/*     */     public Builder<E> addAll(Iterable<? extends E> elements) {
/* 505 */       super.addAll(elements);
/* 506 */       return this;
/*     */     }
/*     */ 
/*     */     
/*     */     @CanIgnoreReturnValue
/*     */     public Builder<E> addAll(Iterator<? extends E> elements) {
/* 512 */       super.addAll(elements);
/* 513 */       return this;
/*     */     }
/*     */     
/*     */     Builder<E> combine(Builder<E> other) {
/* 517 */       copyIfNecessary();
/* 518 */       this.impl = this.impl.combine(other.impl);
/* 519 */       return this;
/*     */     }
/*     */ 
/*     */     
/*     */     public ImmutableSet<E> build() {
/* 524 */       this.forceCopy = true;
/* 525 */       this.impl = this.impl.review();
/* 526 */       return this.impl.build();
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   private static abstract class SetBuilderImpl<E>
/*     */   {
/*     */     E[] dedupedElements;
/*     */     int distinct;
/*     */     
/*     */     SetBuilderImpl(int expectedCapacity) {
/* 537 */       this.dedupedElements = (E[])new Object[expectedCapacity];
/* 538 */       this.distinct = 0;
/*     */     }
/*     */ 
/*     */     
/*     */     SetBuilderImpl(SetBuilderImpl<E> toCopy) {
/* 543 */       this.dedupedElements = Arrays.copyOf(toCopy.dedupedElements, toCopy.dedupedElements.length);
/* 544 */       this.distinct = toCopy.distinct;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     private void ensureCapacity(int minCapacity) {
/* 552 */       if (minCapacity > this.dedupedElements.length) {
/*     */         
/* 554 */         int newCapacity = ImmutableCollection.Builder.expandedCapacity(this.dedupedElements.length, minCapacity);
/* 555 */         this.dedupedElements = Arrays.copyOf(this.dedupedElements, newCapacity);
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     final void addDedupedElement(E e) {
/* 561 */       ensureCapacity(this.distinct + 1);
/* 562 */       this.dedupedElements[this.distinct++] = e;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     abstract SetBuilderImpl<E> add(E param1E);
/*     */ 
/*     */ 
/*     */     
/*     */     final SetBuilderImpl<E> combine(SetBuilderImpl<E> other) {
/* 573 */       SetBuilderImpl<E> result = this;
/* 574 */       for (int i = 0; i < other.distinct; i++) {
/* 575 */         result = result.add(other.dedupedElements[i]);
/*     */       }
/* 577 */       return result;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     abstract SetBuilderImpl<E> copy();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     SetBuilderImpl<E> review() {
/* 591 */       return this;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     abstract ImmutableSet<E> build();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @VisibleForTesting
/*     */   static int chooseTableSize(int setSize) {
/* 613 */     setSize = Math.max(setSize, 2);
/*     */     
/* 615 */     if (setSize < 751619276) {
/*     */       
/* 617 */       int tableSize = Integer.highestOneBit(setSize - 1) << 1;
/* 618 */       while (tableSize * 0.7D < setSize) {
/* 619 */         tableSize <<= 1;
/*     */       }
/* 621 */       return tableSize;
/*     */     } 
/*     */ 
/*     */     
/* 625 */     Preconditions.checkArgument((setSize < 1073741824), "collection too large");
/* 626 */     return 1073741824;
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
/*     */   static boolean hashFloodingDetected(Object[] hashTable) {
/* 668 */     int maxRunBeforeFallback = maxRunBeforeFallback(hashTable.length);
/*     */     
/*     */     int endOfStartRun;
/*     */     
/* 672 */     for (endOfStartRun = 0; endOfStartRun < hashTable.length && 
/* 673 */       hashTable[endOfStartRun] != null; ) {
/*     */ 
/*     */       
/* 676 */       endOfStartRun++;
/* 677 */       if (endOfStartRun > maxRunBeforeFallback) {
/* 678 */         return true;
/*     */       }
/*     */     } 
/*     */     int startOfEndRun;
/* 682 */     for (startOfEndRun = hashTable.length - 1; startOfEndRun > endOfStartRun && 
/* 683 */       hashTable[startOfEndRun] != null; startOfEndRun--) {
/*     */ 
/*     */       
/* 686 */       if (endOfStartRun + hashTable.length - 1 - startOfEndRun > maxRunBeforeFallback) {
/* 687 */         return true;
/*     */       }
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/* 693 */     int testBlockSize = maxRunBeforeFallback / 2;
/*     */     int i;
/* 695 */     for (i = endOfStartRun + 1; i + testBlockSize <= startOfEndRun; i += testBlockSize) {
/* 696 */       int j = 0; while (true) { if (j < testBlockSize) {
/* 697 */           if (hashTable[i + j] == null)
/*     */             break;  j++;
/*     */           continue;
/*     */         } 
/* 701 */         return true; }
/*     */     
/* 703 */     }  return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static int maxRunBeforeFallback(int tableSize) {
/* 712 */     return 13 * IntMath.log2(tableSize, RoundingMode.UNNECESSARY);
/*     */   }
/*     */ 
/*     */   
/*     */   public abstract UnmodifiableIterator<E> iterator();
/*     */ 
/*     */   
/*     */   private static final class RegularSetBuilderImpl<E>
/*     */     extends SetBuilderImpl<E>
/*     */   {
/*     */     private Object[] hashTable;
/*     */     
/*     */     private int maxRunBeforeFallback;
/*     */     
/*     */     private int expandTableThreshold;
/*     */     private int hashCode;
/*     */     
/*     */     RegularSetBuilderImpl(int expectedCapacity) {
/* 730 */       super(expectedCapacity);
/* 731 */       int tableSize = ImmutableSet.chooseTableSize(expectedCapacity);
/* 732 */       this.hashTable = new Object[tableSize];
/* 733 */       this.maxRunBeforeFallback = ImmutableSet.maxRunBeforeFallback(tableSize);
/* 734 */       this.expandTableThreshold = (int)(0.7D * tableSize);
/*     */     }
/*     */     
/*     */     RegularSetBuilderImpl(RegularSetBuilderImpl<E> toCopy) {
/* 738 */       super(toCopy);
/* 739 */       this.hashTable = Arrays.copyOf(toCopy.hashTable, toCopy.hashTable.length);
/* 740 */       this.maxRunBeforeFallback = toCopy.maxRunBeforeFallback;
/* 741 */       this.expandTableThreshold = toCopy.expandTableThreshold;
/* 742 */       this.hashCode = toCopy.hashCode;
/*     */     }
/*     */     
/*     */     void ensureTableCapacity(int minCapacity) {
/* 746 */       if (minCapacity > this.expandTableThreshold && this.hashTable.length < 1073741824) {
/* 747 */         int newTableSize = this.hashTable.length * 2;
/* 748 */         this.hashTable = ImmutableSet.rebuildHashTable(newTableSize, (Object[])this.dedupedElements, this.distinct);
/* 749 */         this.maxRunBeforeFallback = ImmutableSet.maxRunBeforeFallback(newTableSize);
/* 750 */         this.expandTableThreshold = (int)(0.7D * newTableSize);
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     ImmutableSet.SetBuilderImpl<E> add(E e) {
/* 756 */       Preconditions.checkNotNull(e);
/* 757 */       int eHash = e.hashCode();
/* 758 */       int i0 = Hashing.smear(eHash);
/* 759 */       int mask = this.hashTable.length - 1;
/* 760 */       for (int i = i0; i - i0 < this.maxRunBeforeFallback; i++) {
/* 761 */         int index = i & mask;
/* 762 */         Object tableEntry = this.hashTable[index];
/* 763 */         if (tableEntry == null) {
/* 764 */           addDedupedElement(e);
/* 765 */           this.hashTable[index] = e;
/* 766 */           this.hashCode += eHash;
/* 767 */           ensureTableCapacity(this.distinct);
/* 768 */           return this;
/* 769 */         }  if (tableEntry.equals(e)) {
/* 770 */           return this;
/*     */         }
/*     */       } 
/*     */       
/* 774 */       return (new ImmutableSet.JdkBackedSetBuilderImpl<>(this)).add(e);
/*     */     }
/*     */ 
/*     */     
/*     */     ImmutableSet.SetBuilderImpl<E> copy() {
/* 779 */       return new RegularSetBuilderImpl(this);
/*     */     }
/*     */ 
/*     */     
/*     */     ImmutableSet.SetBuilderImpl<E> review() {
/* 784 */       int targetTableSize = ImmutableSet.chooseTableSize(this.distinct);
/* 785 */       if (targetTableSize * 2 < this.hashTable.length) {
/* 786 */         this.hashTable = ImmutableSet.rebuildHashTable(targetTableSize, (Object[])this.dedupedElements, this.distinct);
/*     */       }
/* 788 */       return ImmutableSet.hashFloodingDetected(this.hashTable) ? new ImmutableSet.JdkBackedSetBuilderImpl<>(this) : this;
/*     */     }
/*     */ 
/*     */     
/*     */     ImmutableSet<E> build() {
/* 793 */       switch (this.distinct) {
/*     */         case 0:
/* 795 */           return ImmutableSet.of();
/*     */         case 1:
/* 797 */           return ImmutableSet.of(this.dedupedElements[0]);
/*     */       } 
/*     */ 
/*     */ 
/*     */       
/* 802 */       Object[] elements = (this.distinct == this.dedupedElements.length) ? (Object[])this.dedupedElements : Arrays.<Object>copyOf((Object[])this.dedupedElements, this.distinct);
/* 803 */       return new RegularImmutableSet<>(elements, this.hashCode, this.hashTable, this.hashTable.length - 1);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   private static final class JdkBackedSetBuilderImpl<E>
/*     */     extends SetBuilderImpl<E>
/*     */   {
/*     */     private final Set<Object> delegate;
/*     */ 
/*     */     
/*     */     JdkBackedSetBuilderImpl(ImmutableSet.SetBuilderImpl<E> toCopy) {
/* 815 */       super(toCopy);
/* 816 */       this.delegate = Sets.newHashSetWithExpectedSize(this.distinct);
/* 817 */       for (int i = 0; i < this.distinct; i++) {
/* 818 */         this.delegate.add(this.dedupedElements[i]);
/*     */       }
/*     */     }
/*     */ 
/*     */     
/*     */     ImmutableSet.SetBuilderImpl<E> add(E e) {
/* 824 */       Preconditions.checkNotNull(e);
/* 825 */       if (this.delegate.add(e)) {
/* 826 */         addDedupedElement(e);
/*     */       }
/* 828 */       return this;
/*     */     }
/*     */ 
/*     */     
/*     */     ImmutableSet.SetBuilderImpl<E> copy() {
/* 833 */       return new JdkBackedSetBuilderImpl(this);
/*     */     }
/*     */ 
/*     */     
/*     */     ImmutableSet<E> build() {
/* 838 */       switch (this.distinct) {
/*     */         case 0:
/* 840 */           return ImmutableSet.of();
/*     */         case 1:
/* 842 */           return ImmutableSet.of(this.dedupedElements[0]);
/*     */       } 
/* 844 */       return new JdkBackedImmutableSet<>(this.delegate, 
/* 845 */           ImmutableList.asImmutableList((Object[])this.dedupedElements, this.distinct));
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\google\common\collect\ImmutableSet.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */