/*     */ package com.google.common.collect;
/*     */ 
/*     */ import com.google.common.annotations.Beta;
/*     */ import com.google.common.annotations.GwtIncompatible;
/*     */ import java.util.Collection;
/*     */ import java.util.Iterator;
/*     */ import java.util.NavigableSet;
/*     */ import java.util.Set;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
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
/*     */ public abstract class ForwardingNavigableSet<E>
/*     */   extends ForwardingSortedSet<E>
/*     */   implements NavigableSet<E>
/*     */ {
/*     */   public E lower(E e) {
/*  63 */     return delegate().lower(e);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected E standardLower(E e) {
/*  72 */     return Iterators.getNext(headSet(e, false).descendingIterator(), null);
/*     */   }
/*     */ 
/*     */   
/*     */   public E floor(E e) {
/*  77 */     return delegate().floor(e);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected E standardFloor(E e) {
/*  86 */     return Iterators.getNext(headSet(e, true).descendingIterator(), null);
/*     */   }
/*     */ 
/*     */   
/*     */   public E ceiling(E e) {
/*  91 */     return delegate().ceiling(e);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected E standardCeiling(E e) {
/* 100 */     return Iterators.getNext(tailSet(e, true).iterator(), null);
/*     */   }
/*     */ 
/*     */   
/*     */   public E higher(E e) {
/* 105 */     return delegate().higher(e);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected E standardHigher(E e) {
/* 114 */     return Iterators.getNext(tailSet(e, false).iterator(), null);
/*     */   }
/*     */ 
/*     */   
/*     */   public E pollFirst() {
/* 119 */     return delegate().pollFirst();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected E standardPollFirst() {
/* 128 */     return Iterators.pollNext(iterator());
/*     */   }
/*     */ 
/*     */   
/*     */   public E pollLast() {
/* 133 */     return delegate().pollLast();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected E standardPollLast() {
/* 142 */     return Iterators.pollNext(descendingIterator());
/*     */   }
/*     */   
/*     */   protected E standardFirst() {
/* 146 */     return iterator().next();
/*     */   }
/*     */   
/*     */   protected E standardLast() {
/* 150 */     return descendingIterator().next();
/*     */   }
/*     */ 
/*     */   
/*     */   public NavigableSet<E> descendingSet() {
/* 155 */     return delegate().descendingSet();
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
/*     */   protected class StandardDescendingSet
/*     */     extends Sets.DescendingSet<E>
/*     */   {
/*     */     public StandardDescendingSet() {
/* 171 */       super(ForwardingNavigableSet.this);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public Iterator<E> descendingIterator() {
/* 177 */     return delegate().descendingIterator();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public NavigableSet<E> subSet(E fromElement, boolean fromInclusive, E toElement, boolean toInclusive) {
/* 183 */     return delegate().subSet(fromElement, fromInclusive, toElement, toInclusive);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Beta
/*     */   protected NavigableSet<E> standardSubSet(E fromElement, boolean fromInclusive, E toElement, boolean toInclusive) {
/* 194 */     return tailSet(fromElement, fromInclusive).headSet(toElement, toInclusive);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected SortedSet<E> standardSubSet(E fromElement, E toElement) {
/* 205 */     return subSet(fromElement, true, toElement, false);
/*     */   }
/*     */ 
/*     */   
/*     */   public NavigableSet<E> headSet(E toElement, boolean inclusive) {
/* 210 */     return delegate().headSet(toElement, inclusive);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected SortedSet<E> standardHeadSet(E toElement) {
/* 219 */     return headSet(toElement, false);
/*     */   }
/*     */ 
/*     */   
/*     */   public NavigableSet<E> tailSet(E fromElement, boolean inclusive) {
/* 224 */     return delegate().tailSet(fromElement, inclusive);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected SortedSet<E> standardTailSet(E fromElement) {
/* 233 */     return tailSet(fromElement, true);
/*     */   }
/*     */   
/*     */   protected abstract NavigableSet<E> delegate();
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\google\common\collect\ForwardingNavigableSet.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */