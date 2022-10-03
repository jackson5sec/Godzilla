/*     */ package com.google.common.collect;
/*     */ 
/*     */ import com.google.common.annotations.Beta;
/*     */ import com.google.common.annotations.GwtCompatible;
/*     */ import java.util.Collection;
/*     */ import java.util.Comparator;
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
/*     */ @Beta
/*     */ @GwtCompatible(emulated = true)
/*     */ public abstract class ForwardingSortedMultiset<E>
/*     */   extends ForwardingMultiset<E>
/*     */   implements SortedMultiset<E>
/*     */ {
/*     */   public NavigableSet<E> elementSet() {
/*  57 */     return delegate().elementSet();
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
/*     */   protected class StandardElementSet
/*     */     extends SortedMultisets.NavigableElementSet<E>
/*     */   {
/*     */     public StandardElementSet() {
/*  76 */       super(ForwardingSortedMultiset.this);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public Comparator<? super E> comparator() {
/*  82 */     return delegate().comparator();
/*     */   }
/*     */ 
/*     */   
/*     */   public SortedMultiset<E> descendingMultiset() {
/*  87 */     return delegate().descendingMultiset();
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
/*     */   protected abstract class StandardDescendingMultiset
/*     */     extends DescendingMultiset<E>
/*     */   {
/*     */     SortedMultiset<E> forwardMultiset() {
/* 108 */       return ForwardingSortedMultiset.this;
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public Multiset.Entry<E> firstEntry() {
/* 114 */     return delegate().firstEntry();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected Multiset.Entry<E> standardFirstEntry() {
/* 124 */     Iterator<Multiset.Entry<E>> entryIterator = entrySet().iterator();
/* 125 */     if (!entryIterator.hasNext()) {
/* 126 */       return null;
/*     */     }
/* 128 */     Multiset.Entry<E> entry = entryIterator.next();
/* 129 */     return Multisets.immutableEntry(entry.getElement(), entry.getCount());
/*     */   }
/*     */ 
/*     */   
/*     */   public Multiset.Entry<E> lastEntry() {
/* 134 */     return delegate().lastEntry();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected Multiset.Entry<E> standardLastEntry() {
/* 145 */     Iterator<Multiset.Entry<E>> entryIterator = descendingMultiset().entrySet().iterator();
/* 146 */     if (!entryIterator.hasNext()) {
/* 147 */       return null;
/*     */     }
/* 149 */     Multiset.Entry<E> entry = entryIterator.next();
/* 150 */     return Multisets.immutableEntry(entry.getElement(), entry.getCount());
/*     */   }
/*     */ 
/*     */   
/*     */   public Multiset.Entry<E> pollFirstEntry() {
/* 155 */     return delegate().pollFirstEntry();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected Multiset.Entry<E> standardPollFirstEntry() {
/* 165 */     Iterator<Multiset.Entry<E>> entryIterator = entrySet().iterator();
/* 166 */     if (!entryIterator.hasNext()) {
/* 167 */       return null;
/*     */     }
/* 169 */     Multiset.Entry<E> entry = entryIterator.next();
/* 170 */     entry = Multisets.immutableEntry(entry.getElement(), entry.getCount());
/* 171 */     entryIterator.remove();
/* 172 */     return entry;
/*     */   }
/*     */ 
/*     */   
/*     */   public Multiset.Entry<E> pollLastEntry() {
/* 177 */     return delegate().pollLastEntry();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected Multiset.Entry<E> standardPollLastEntry() {
/* 188 */     Iterator<Multiset.Entry<E>> entryIterator = descendingMultiset().entrySet().iterator();
/* 189 */     if (!entryIterator.hasNext()) {
/* 190 */       return null;
/*     */     }
/* 192 */     Multiset.Entry<E> entry = entryIterator.next();
/* 193 */     entry = Multisets.immutableEntry(entry.getElement(), entry.getCount());
/* 194 */     entryIterator.remove();
/* 195 */     return entry;
/*     */   }
/*     */ 
/*     */   
/*     */   public SortedMultiset<E> headMultiset(E upperBound, BoundType boundType) {
/* 200 */     return delegate().headMultiset(upperBound, boundType);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public SortedMultiset<E> subMultiset(E lowerBound, BoundType lowerBoundType, E upperBound, BoundType upperBoundType) {
/* 206 */     return delegate().subMultiset(lowerBound, lowerBoundType, upperBound, upperBoundType);
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
/*     */   protected SortedMultiset<E> standardSubMultiset(E lowerBound, BoundType lowerBoundType, E upperBound, BoundType upperBoundType) {
/* 219 */     return tailMultiset(lowerBound, lowerBoundType).headMultiset(upperBound, upperBoundType);
/*     */   }
/*     */ 
/*     */   
/*     */   public SortedMultiset<E> tailMultiset(E lowerBound, BoundType boundType) {
/* 224 */     return delegate().tailMultiset(lowerBound, boundType);
/*     */   }
/*     */   
/*     */   protected abstract SortedMultiset<E> delegate();
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\google\common\collect\ForwardingSortedMultiset.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */