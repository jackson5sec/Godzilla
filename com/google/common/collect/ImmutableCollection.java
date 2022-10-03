/*     */ package com.google.common.collect;
/*     */ 
/*     */ import com.google.common.annotations.GwtCompatible;
/*     */ import com.google.common.base.Preconditions;
/*     */ import com.google.errorprone.annotations.CanIgnoreReturnValue;
/*     */ import java.io.Serializable;
/*     */ import java.util.AbstractCollection;
/*     */ import java.util.Collection;
/*     */ import java.util.Iterator;
/*     */ import java.util.Spliterator;
/*     */ import java.util.Spliterators;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
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
/*     */ public abstract class ImmutableCollection<E>
/*     */   extends AbstractCollection<E>
/*     */   implements Serializable
/*     */ {
/*     */   static final int SPLITERATOR_CHARACTERISTICS = 1296;
/*     */   
/*     */   public Spliterator<E> spliterator() {
/* 180 */     return Spliterators.spliterator(this, 1296);
/*     */   }
/*     */   
/* 183 */   private static final Object[] EMPTY_ARRAY = new Object[0];
/*     */ 
/*     */   
/*     */   public final Object[] toArray() {
/* 187 */     return toArray(EMPTY_ARRAY);
/*     */   }
/*     */ 
/*     */   
/*     */   @CanIgnoreReturnValue
/*     */   public final <T> T[] toArray(T[] other) {
/* 193 */     Preconditions.checkNotNull(other);
/* 194 */     int size = size();
/*     */     
/* 196 */     if (other.length < size) {
/* 197 */       Object[] internal = internalArray();
/* 198 */       if (internal != null) {
/* 199 */         return Platform.copy(internal, internalArrayStart(), internalArrayEnd(), other);
/*     */       }
/* 201 */       other = ObjectArrays.newArray(other, size);
/* 202 */     } else if (other.length > size) {
/* 203 */       other[size] = null;
/*     */     } 
/* 205 */     copyIntoArray((Object[])other, 0);
/* 206 */     return other;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   Object[] internalArray() {
/* 212 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   int internalArrayStart() {
/* 220 */     throw new UnsupportedOperationException();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   int internalArrayEnd() {
/* 228 */     throw new UnsupportedOperationException();
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
/*     */   @Deprecated
/*     */   @CanIgnoreReturnValue
/*     */   public final boolean add(E e) {
/* 244 */     throw new UnsupportedOperationException();
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
/*     */   public final boolean remove(Object object) {
/* 257 */     throw new UnsupportedOperationException();
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
/*     */   public final boolean addAll(Collection<? extends E> newElements) {
/* 270 */     throw new UnsupportedOperationException();
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
/*     */   public final boolean removeAll(Collection<?> oldElements) {
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
/*     */   public final boolean removeIf(Predicate<? super E> filter) {
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
/*     */   public final boolean retainAll(Collection<?> elementsToKeep) {
/* 308 */     throw new UnsupportedOperationException();
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
/*     */   public final void clear() {
/* 320 */     throw new UnsupportedOperationException();
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
/*     */   public ImmutableList<E> asList() {
/* 334 */     switch (size()) {
/*     */       case 0:
/* 336 */         return ImmutableList.of();
/*     */       case 1:
/* 338 */         return ImmutableList.of(iterator().next());
/*     */     } 
/* 340 */     return new RegularImmutableAsList<>(this, toArray());
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
/*     */   @CanIgnoreReturnValue
/*     */   int copyIntoArray(Object[] dst, int offset) {
/* 358 */     for (UnmodifiableIterator<E> unmodifiableIterator = iterator(); unmodifiableIterator.hasNext(); ) { E e = unmodifiableIterator.next();
/* 359 */       dst[offset++] = e; }
/*     */     
/* 361 */     return offset;
/*     */   }
/*     */ 
/*     */   
/*     */   Object writeReplace() {
/* 366 */     return new ImmutableList.SerializedForm(toArray());
/*     */   }
/*     */   
/*     */   public abstract UnmodifiableIterator<E> iterator();
/*     */   
/*     */   public abstract boolean contains(Object paramObject);
/*     */   
/*     */   abstract boolean isPartialView();
/*     */   
/*     */   public static abstract class Builder<E> { static final int DEFAULT_INITIAL_CAPACITY = 4;
/*     */     
/*     */     static int expandedCapacity(int oldCapacity, int minCapacity) {
/* 378 */       if (minCapacity < 0) {
/* 379 */         throw new AssertionError("cannot store more than MAX_VALUE elements");
/*     */       }
/*     */       
/* 382 */       int newCapacity = oldCapacity + (oldCapacity >> 1) + 1;
/* 383 */       if (newCapacity < minCapacity) {
/* 384 */         newCapacity = Integer.highestOneBit(minCapacity - 1) << 1;
/*     */       }
/* 386 */       if (newCapacity < 0) {
/* 387 */         newCapacity = Integer.MAX_VALUE;
/*     */       }
/*     */       
/* 390 */       return newCapacity;
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
/*     */     public abstract Builder<E> add(E param1E);
/*     */ 
/*     */ 
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
/* 419 */       for (E element : elements) {
/* 420 */         add(element);
/*     */       }
/* 422 */       return this;
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
/*     */     public Builder<E> addAll(Iterable<? extends E> elements) {
/* 437 */       for (E element : elements) {
/* 438 */         add(element);
/*     */       }
/* 440 */       return this;
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
/*     */     public Builder<E> addAll(Iterator<? extends E> elements) {
/* 455 */       while (elements.hasNext()) {
/* 456 */         add(elements.next());
/*     */       }
/* 458 */       return this;
/*     */     }
/*     */     
/*     */     public abstract ImmutableCollection<E> build(); }
/*     */ 
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\google\common\collect\ImmutableCollection.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */