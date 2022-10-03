/*     */ package com.google.common.collect;
/*     */ 
/*     */ import com.google.common.annotations.GwtCompatible;
/*     */ import com.google.common.annotations.GwtIncompatible;
/*     */ import com.google.common.base.Preconditions;
/*     */ import java.util.Collection;
/*     */ import java.util.Collections;
/*     */ import java.util.Comparator;
/*     */ import java.util.Iterator;
/*     */ import java.util.NoSuchElementException;
/*     */ import java.util.Set;
/*     */ import java.util.Spliterator;
/*     */ import java.util.function.Consumer;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
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
/*     */ final class RegularImmutableSortedSet<E>
/*     */   extends ImmutableSortedSet<E>
/*     */ {
/*  43 */   static final RegularImmutableSortedSet<Comparable> NATURAL_EMPTY_SET = new RegularImmutableSortedSet(
/*  44 */       ImmutableList.of(), Ordering.natural());
/*     */   
/*     */   private final transient ImmutableList<E> elements;
/*     */   
/*     */   RegularImmutableSortedSet(ImmutableList<E> elements, Comparator<? super E> comparator) {
/*  49 */     super(comparator);
/*  50 */     this.elements = elements;
/*     */   }
/*     */ 
/*     */   
/*     */   Object[] internalArray() {
/*  55 */     return this.elements.internalArray();
/*     */   }
/*     */ 
/*     */   
/*     */   int internalArrayStart() {
/*  60 */     return this.elements.internalArrayStart();
/*     */   }
/*     */ 
/*     */   
/*     */   int internalArrayEnd() {
/*  65 */     return this.elements.internalArrayEnd();
/*     */   }
/*     */ 
/*     */   
/*     */   public UnmodifiableIterator<E> iterator() {
/*  70 */     return this.elements.iterator();
/*     */   }
/*     */ 
/*     */   
/*     */   @GwtIncompatible
/*     */   public UnmodifiableIterator<E> descendingIterator() {
/*  76 */     return this.elements.reverse().iterator();
/*     */   }
/*     */ 
/*     */   
/*     */   public Spliterator<E> spliterator() {
/*  81 */     return asList().spliterator();
/*     */   }
/*     */ 
/*     */   
/*     */   public void forEach(Consumer<? super E> action) {
/*  86 */     this.elements.forEach(action);
/*     */   }
/*     */ 
/*     */   
/*     */   public int size() {
/*  91 */     return this.elements.size();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean contains(Object o) {
/*     */     try {
/*  97 */       return (o != null && unsafeBinarySearch(o) >= 0);
/*  98 */     } catch (ClassCastException e) {
/*  99 */       return false;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean containsAll(Collection<?> targets) {
/* 109 */     if (targets instanceof Multiset) {
/* 110 */       targets = ((Multiset)targets).elementSet();
/*     */     }
/* 112 */     if (!SortedIterables.hasSameComparator(comparator(), targets) || targets.size() <= 1) {
/* 113 */       return super.containsAll(targets);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 120 */     Iterator<E> thisIterator = iterator();
/*     */     
/* 122 */     Iterator<?> thatIterator = targets.iterator();
/*     */ 
/*     */     
/* 125 */     if (!thisIterator.hasNext()) {
/* 126 */       return false;
/*     */     }
/*     */     
/* 129 */     Object target = thatIterator.next();
/* 130 */     E current = thisIterator.next();
/*     */     try {
/*     */       while (true) {
/* 133 */         int cmp = unsafeCompare(current, target);
/*     */         
/* 135 */         if (cmp < 0) {
/* 136 */           if (!thisIterator.hasNext()) {
/* 137 */             return false;
/*     */           }
/* 139 */           current = thisIterator.next(); continue;
/* 140 */         }  if (cmp == 0) {
/* 141 */           if (!thatIterator.hasNext()) {
/* 142 */             return true;
/*     */           }
/* 144 */           target = thatIterator.next(); continue;
/*     */         } 
/* 146 */         if (cmp > 0) {
/* 147 */           return false;
/*     */         }
/*     */       } 
/* 150 */     } catch (NullPointerException|ClassCastException e) {
/* 151 */       return false;
/*     */     } 
/*     */   }
/*     */   
/*     */   private int unsafeBinarySearch(Object key) throws ClassCastException {
/* 156 */     return Collections.binarySearch(this.elements, (E)key, (Comparator)unsafeComparator());
/*     */   }
/*     */ 
/*     */   
/*     */   boolean isPartialView() {
/* 161 */     return this.elements.isPartialView();
/*     */   }
/*     */ 
/*     */   
/*     */   int copyIntoArray(Object[] dst, int offset) {
/* 166 */     return this.elements.copyIntoArray(dst, offset);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean equals(Object object) {
/* 171 */     if (object == this) {
/* 172 */       return true;
/*     */     }
/* 174 */     if (!(object instanceof Set)) {
/* 175 */       return false;
/*     */     }
/*     */     
/* 178 */     Set<?> that = (Set)object;
/* 179 */     if (size() != that.size())
/* 180 */       return false; 
/* 181 */     if (isEmpty()) {
/* 182 */       return true;
/*     */     }
/*     */     
/* 185 */     if (SortedIterables.hasSameComparator(this.comparator, that)) {
/* 186 */       Iterator<?> otherIterator = that.iterator();
/*     */       try {
/* 188 */         Iterator<E> iterator = iterator();
/* 189 */         while (iterator.hasNext()) {
/* 190 */           Object element = iterator.next();
/* 191 */           Object otherElement = otherIterator.next();
/* 192 */           if (otherElement == null || unsafeCompare(element, otherElement) != 0) {
/* 193 */             return false;
/*     */           }
/*     */         } 
/* 196 */         return true;
/* 197 */       } catch (ClassCastException e) {
/* 198 */         return false;
/* 199 */       } catch (NoSuchElementException e) {
/* 200 */         return false;
/*     */       } 
/*     */     } 
/* 203 */     return containsAll(that);
/*     */   }
/*     */ 
/*     */   
/*     */   public E first() {
/* 208 */     if (isEmpty()) {
/* 209 */       throw new NoSuchElementException();
/*     */     }
/* 211 */     return this.elements.get(0);
/*     */   }
/*     */ 
/*     */   
/*     */   public E last() {
/* 216 */     if (isEmpty()) {
/* 217 */       throw new NoSuchElementException();
/*     */     }
/* 219 */     return this.elements.get(size() - 1);
/*     */   }
/*     */ 
/*     */   
/*     */   public E lower(E element) {
/* 224 */     int index = headIndex(element, false) - 1;
/* 225 */     return (index == -1) ? null : this.elements.get(index);
/*     */   }
/*     */ 
/*     */   
/*     */   public E floor(E element) {
/* 230 */     int index = headIndex(element, true) - 1;
/* 231 */     return (index == -1) ? null : this.elements.get(index);
/*     */   }
/*     */ 
/*     */   
/*     */   public E ceiling(E element) {
/* 236 */     int index = tailIndex(element, true);
/* 237 */     return (index == size()) ? null : this.elements.get(index);
/*     */   }
/*     */ 
/*     */   
/*     */   public E higher(E element) {
/* 242 */     int index = tailIndex(element, false);
/* 243 */     return (index == size()) ? null : this.elements.get(index);
/*     */   }
/*     */ 
/*     */   
/*     */   ImmutableSortedSet<E> headSetImpl(E toElement, boolean inclusive) {
/* 248 */     return getSubSet(0, headIndex(toElement, inclusive));
/*     */   }
/*     */   
/*     */   int headIndex(E toElement, boolean inclusive) {
/* 252 */     int index = Collections.binarySearch(this.elements, (E)Preconditions.checkNotNull(toElement), comparator());
/* 253 */     if (index >= 0) {
/* 254 */       return inclusive ? (index + 1) : index;
/*     */     }
/* 256 */     return index ^ 0xFFFFFFFF;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   ImmutableSortedSet<E> subSetImpl(E fromElement, boolean fromInclusive, E toElement, boolean toInclusive) {
/* 263 */     return tailSetImpl(fromElement, fromInclusive).headSetImpl(toElement, toInclusive);
/*     */   }
/*     */ 
/*     */   
/*     */   ImmutableSortedSet<E> tailSetImpl(E fromElement, boolean inclusive) {
/* 268 */     return getSubSet(tailIndex(fromElement, inclusive), size());
/*     */   }
/*     */   
/*     */   int tailIndex(E fromElement, boolean inclusive) {
/* 272 */     int index = Collections.binarySearch(this.elements, (E)Preconditions.checkNotNull(fromElement), comparator());
/* 273 */     if (index >= 0) {
/* 274 */       return inclusive ? index : (index + 1);
/*     */     }
/* 276 */     return index ^ 0xFFFFFFFF;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   Comparator<Object> unsafeComparator() {
/* 285 */     return (Comparator)this.comparator;
/*     */   }
/*     */   
/*     */   RegularImmutableSortedSet<E> getSubSet(int newFromIndex, int newToIndex) {
/* 289 */     if (newFromIndex == 0 && newToIndex == size())
/* 290 */       return this; 
/* 291 */     if (newFromIndex < newToIndex) {
/* 292 */       return new RegularImmutableSortedSet(this.elements
/* 293 */           .subList(newFromIndex, newToIndex), this.comparator);
/*     */     }
/* 295 */     return emptySet(this.comparator);
/*     */   }
/*     */ 
/*     */   
/*     */   int indexOf(Object target) {
/*     */     int position;
/* 301 */     if (target == null) {
/* 302 */       return -1;
/*     */     }
/*     */     
/*     */     try {
/* 306 */       position = Collections.binarySearch(this.elements, (E)target, (Comparator)unsafeComparator());
/* 307 */     } catch (ClassCastException e) {
/* 308 */       return -1;
/*     */     } 
/* 310 */     return (position >= 0) ? position : -1;
/*     */   }
/*     */ 
/*     */   
/*     */   ImmutableList<E> createAsList() {
/* 315 */     return (size() <= 1) ? this.elements : new ImmutableSortedAsList<>(this, this.elements);
/*     */   }
/*     */ 
/*     */   
/*     */   ImmutableSortedSet<E> createDescendingSet() {
/* 320 */     Comparator<? super E> reversedOrder = Collections.reverseOrder(this.comparator);
/* 321 */     return isEmpty() ? 
/* 322 */       emptySet(reversedOrder) : new RegularImmutableSortedSet(this.elements
/* 323 */         .reverse(), reversedOrder);
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\google\common\collect\RegularImmutableSortedSet.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */