/*     */ package com.google.common.collect;
/*     */ 
/*     */ import com.google.common.annotations.GwtCompatible;
/*     */ import java.util.Collection;
/*     */ import java.util.Comparator;
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
/*     */ @GwtCompatible(emulated = true)
/*     */ final class UnmodifiableSortedMultiset<E>
/*     */   extends Multisets.UnmodifiableMultiset<E>
/*     */   implements SortedMultiset<E>
/*     */ {
/*     */   private transient UnmodifiableSortedMultiset<E> descendingMultiset;
/*     */   private static final long serialVersionUID = 0L;
/*     */   
/*     */   UnmodifiableSortedMultiset(SortedMultiset<E> delegate) {
/*  36 */     super(delegate);
/*     */   }
/*     */ 
/*     */   
/*     */   protected SortedMultiset<E> delegate() {
/*  41 */     return (SortedMultiset<E>)super.delegate();
/*     */   }
/*     */ 
/*     */   
/*     */   public Comparator<? super E> comparator() {
/*  46 */     return delegate().comparator();
/*     */   }
/*     */ 
/*     */   
/*     */   NavigableSet<E> createElementSet() {
/*  51 */     return Sets.unmodifiableNavigableSet(delegate().elementSet());
/*     */   }
/*     */ 
/*     */   
/*     */   public NavigableSet<E> elementSet() {
/*  56 */     return (NavigableSet<E>)super.elementSet();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public SortedMultiset<E> descendingMultiset() {
/*  63 */     UnmodifiableSortedMultiset<E> result = this.descendingMultiset;
/*  64 */     if (result == null) {
/*  65 */       result = new UnmodifiableSortedMultiset(delegate().descendingMultiset());
/*  66 */       result.descendingMultiset = this;
/*  67 */       return this.descendingMultiset = result;
/*     */     } 
/*  69 */     return result;
/*     */   }
/*     */ 
/*     */   
/*     */   public Multiset.Entry<E> firstEntry() {
/*  74 */     return delegate().firstEntry();
/*     */   }
/*     */ 
/*     */   
/*     */   public Multiset.Entry<E> lastEntry() {
/*  79 */     return delegate().lastEntry();
/*     */   }
/*     */ 
/*     */   
/*     */   public Multiset.Entry<E> pollFirstEntry() {
/*  84 */     throw new UnsupportedOperationException();
/*     */   }
/*     */ 
/*     */   
/*     */   public Multiset.Entry<E> pollLastEntry() {
/*  89 */     throw new UnsupportedOperationException();
/*     */   }
/*     */ 
/*     */   
/*     */   public SortedMultiset<E> headMultiset(E upperBound, BoundType boundType) {
/*  94 */     return Multisets.unmodifiableSortedMultiset(delegate().headMultiset(upperBound, boundType));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public SortedMultiset<E> subMultiset(E lowerBound, BoundType lowerBoundType, E upperBound, BoundType upperBoundType) {
/* 100 */     return Multisets.unmodifiableSortedMultiset(
/* 101 */         delegate().subMultiset(lowerBound, lowerBoundType, upperBound, upperBoundType));
/*     */   }
/*     */ 
/*     */   
/*     */   public SortedMultiset<E> tailMultiset(E lowerBound, BoundType boundType) {
/* 106 */     return Multisets.unmodifiableSortedMultiset(delegate().tailMultiset(lowerBound, boundType));
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\google\common\collect\UnmodifiableSortedMultiset.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */