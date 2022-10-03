/*     */ package com.google.common.collect;
/*     */ 
/*     */ import com.google.common.annotations.GwtIncompatible;
/*     */ import java.util.Iterator;
/*     */ import java.util.NavigableSet;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @GwtIncompatible
/*     */ final class DescendingImmutableSortedSet<E>
/*     */   extends ImmutableSortedSet<E>
/*     */ {
/*     */   private final ImmutableSortedSet<E> forward;
/*     */   
/*     */   DescendingImmutableSortedSet(ImmutableSortedSet<E> forward) {
/*  32 */     super(Ordering.from(forward.comparator()).reverse());
/*  33 */     this.forward = forward;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean contains(Object object) {
/*  38 */     return this.forward.contains(object);
/*     */   }
/*     */ 
/*     */   
/*     */   public int size() {
/*  43 */     return this.forward.size();
/*     */   }
/*     */ 
/*     */   
/*     */   public UnmodifiableIterator<E> iterator() {
/*  48 */     return this.forward.descendingIterator();
/*     */   }
/*     */ 
/*     */   
/*     */   ImmutableSortedSet<E> headSetImpl(E toElement, boolean inclusive) {
/*  53 */     return this.forward.tailSet(toElement, inclusive).descendingSet();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   ImmutableSortedSet<E> subSetImpl(E fromElement, boolean fromInclusive, E toElement, boolean toInclusive) {
/*  59 */     return this.forward.subSet(toElement, toInclusive, fromElement, fromInclusive).descendingSet();
/*     */   }
/*     */ 
/*     */   
/*     */   ImmutableSortedSet<E> tailSetImpl(E fromElement, boolean inclusive) {
/*  64 */     return this.forward.headSet(fromElement, inclusive).descendingSet();
/*     */   }
/*     */ 
/*     */   
/*     */   @GwtIncompatible("NavigableSet")
/*     */   public ImmutableSortedSet<E> descendingSet() {
/*  70 */     return this.forward;
/*     */   }
/*     */ 
/*     */   
/*     */   @GwtIncompatible("NavigableSet")
/*     */   public UnmodifiableIterator<E> descendingIterator() {
/*  76 */     return this.forward.iterator();
/*     */   }
/*     */ 
/*     */   
/*     */   @GwtIncompatible("NavigableSet")
/*     */   ImmutableSortedSet<E> createDescendingSet() {
/*  82 */     throw new AssertionError("should never be called");
/*     */   }
/*     */ 
/*     */   
/*     */   public E lower(E element) {
/*  87 */     return this.forward.higher(element);
/*     */   }
/*     */ 
/*     */   
/*     */   public E floor(E element) {
/*  92 */     return this.forward.ceiling(element);
/*     */   }
/*     */ 
/*     */   
/*     */   public E ceiling(E element) {
/*  97 */     return this.forward.floor(element);
/*     */   }
/*     */ 
/*     */   
/*     */   public E higher(E element) {
/* 102 */     return this.forward.lower(element);
/*     */   }
/*     */ 
/*     */   
/*     */   int indexOf(Object target) {
/* 107 */     int index = this.forward.indexOf(target);
/* 108 */     if (index == -1) {
/* 109 */       return index;
/*     */     }
/* 111 */     return size() - 1 - index;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   boolean isPartialView() {
/* 117 */     return this.forward.isPartialView();
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\google\common\collect\DescendingImmutableSortedSet.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */