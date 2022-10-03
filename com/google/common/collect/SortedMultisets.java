/*     */ package com.google.common.collect;
/*     */ 
/*     */ import com.google.common.annotations.GwtCompatible;
/*     */ import com.google.common.annotations.GwtIncompatible;
/*     */ import com.google.j2objc.annotations.Weak;
/*     */ import java.util.Comparator;
/*     */ import java.util.Iterator;
/*     */ import java.util.NavigableSet;
/*     */ import java.util.NoSuchElementException;
/*     */ import java.util.SortedSet;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
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
/*     */ final class SortedMultisets
/*     */ {
/*     */   static class ElementSet<E>
/*     */     extends Multisets.ElementSet<E>
/*     */     implements SortedSet<E>
/*     */   {
/*     */     @Weak
/*     */     private final SortedMultiset<E> multiset;
/*     */     
/*     */     ElementSet(SortedMultiset<E> multiset) {
/*  47 */       this.multiset = multiset;
/*     */     }
/*     */ 
/*     */     
/*     */     final SortedMultiset<E> multiset() {
/*  52 */       return this.multiset;
/*     */     }
/*     */ 
/*     */     
/*     */     public Iterator<E> iterator() {
/*  57 */       return Multisets.elementIterator(multiset().entrySet().iterator());
/*     */     }
/*     */ 
/*     */     
/*     */     public Comparator<? super E> comparator() {
/*  62 */       return multiset().comparator();
/*     */     }
/*     */ 
/*     */     
/*     */     public SortedSet<E> subSet(E fromElement, E toElement) {
/*  67 */       return multiset().subMultiset(fromElement, BoundType.CLOSED, toElement, BoundType.OPEN).elementSet();
/*     */     }
/*     */ 
/*     */     
/*     */     public SortedSet<E> headSet(E toElement) {
/*  72 */       return multiset().headMultiset(toElement, BoundType.OPEN).elementSet();
/*     */     }
/*     */ 
/*     */     
/*     */     public SortedSet<E> tailSet(E fromElement) {
/*  77 */       return multiset().tailMultiset(fromElement, BoundType.CLOSED).elementSet();
/*     */     }
/*     */ 
/*     */     
/*     */     public E first() {
/*  82 */       return SortedMultisets.getElementOrThrow(multiset().firstEntry());
/*     */     }
/*     */ 
/*     */     
/*     */     public E last() {
/*  87 */       return SortedMultisets.getElementOrThrow(multiset().lastEntry());
/*     */     }
/*     */   }
/*     */   
/*     */   @GwtIncompatible
/*     */   static class NavigableElementSet<E>
/*     */     extends ElementSet<E> implements NavigableSet<E> {
/*     */     NavigableElementSet(SortedMultiset<E> multiset) {
/*  95 */       super(multiset);
/*     */     }
/*     */ 
/*     */     
/*     */     public E lower(E e) {
/* 100 */       return SortedMultisets.getElementOrNull(multiset().headMultiset(e, BoundType.OPEN).lastEntry());
/*     */     }
/*     */ 
/*     */     
/*     */     public E floor(E e) {
/* 105 */       return SortedMultisets.getElementOrNull(multiset().headMultiset(e, BoundType.CLOSED).lastEntry());
/*     */     }
/*     */ 
/*     */     
/*     */     public E ceiling(E e) {
/* 110 */       return SortedMultisets.getElementOrNull(multiset().tailMultiset(e, BoundType.CLOSED).firstEntry());
/*     */     }
/*     */ 
/*     */     
/*     */     public E higher(E e) {
/* 115 */       return SortedMultisets.getElementOrNull(multiset().tailMultiset(e, BoundType.OPEN).firstEntry());
/*     */     }
/*     */ 
/*     */     
/*     */     public NavigableSet<E> descendingSet() {
/* 120 */       return new NavigableElementSet(multiset().descendingMultiset());
/*     */     }
/*     */ 
/*     */     
/*     */     public Iterator<E> descendingIterator() {
/* 125 */       return descendingSet().iterator();
/*     */     }
/*     */ 
/*     */     
/*     */     public E pollFirst() {
/* 130 */       return SortedMultisets.getElementOrNull(multiset().pollFirstEntry());
/*     */     }
/*     */ 
/*     */     
/*     */     public E pollLast() {
/* 135 */       return SortedMultisets.getElementOrNull(multiset().pollLastEntry());
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public NavigableSet<E> subSet(E fromElement, boolean fromInclusive, E toElement, boolean toInclusive) {
/* 141 */       return new NavigableElementSet(
/* 142 */           multiset()
/* 143 */           .subMultiset(fromElement, 
/* 144 */             BoundType.forBoolean(fromInclusive), toElement, 
/* 145 */             BoundType.forBoolean(toInclusive)));
/*     */     }
/*     */ 
/*     */     
/*     */     public NavigableSet<E> headSet(E toElement, boolean inclusive) {
/* 150 */       return new NavigableElementSet(
/* 151 */           multiset().headMultiset(toElement, BoundType.forBoolean(inclusive)));
/*     */     }
/*     */ 
/*     */     
/*     */     public NavigableSet<E> tailSet(E fromElement, boolean inclusive) {
/* 156 */       return new NavigableElementSet(
/* 157 */           multiset().tailMultiset(fromElement, BoundType.forBoolean(inclusive)));
/*     */     }
/*     */   }
/*     */   
/*     */   private static <E> E getElementOrThrow(Multiset.Entry<E> entry) {
/* 162 */     if (entry == null) {
/* 163 */       throw new NoSuchElementException();
/*     */     }
/* 165 */     return entry.getElement();
/*     */   }
/*     */   
/*     */   private static <E> E getElementOrNull(Multiset.Entry<E> entry) {
/* 169 */     return (entry == null) ? null : entry.getElement();
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\google\common\collect\SortedMultisets.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */