/*     */ package com.google.common.collect;
/*     */ 
/*     */ import com.google.common.annotations.GwtCompatible;
/*     */ import com.google.common.annotations.GwtIncompatible;
/*     */ import java.io.Serializable;
/*     */ import java.util.Iterator;
/*     */ import java.util.NoSuchElementException;
/*     */ import java.util.Set;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
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
/*     */ final class EmptyContiguousSet<C extends Comparable>
/*     */   extends ContiguousSet<C>
/*     */ {
/*     */   EmptyContiguousSet(DiscreteDomain<C> domain) {
/*  32 */     super(domain);
/*     */   }
/*     */ 
/*     */   
/*     */   public C first() {
/*  37 */     throw new NoSuchElementException();
/*     */   }
/*     */ 
/*     */   
/*     */   public C last() {
/*  42 */     throw new NoSuchElementException();
/*     */   }
/*     */ 
/*     */   
/*     */   public int size() {
/*  47 */     return 0;
/*     */   }
/*     */ 
/*     */   
/*     */   public ContiguousSet<C> intersection(ContiguousSet<C> other) {
/*  52 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public Range<C> range() {
/*  57 */     throw new NoSuchElementException();
/*     */   }
/*     */ 
/*     */   
/*     */   public Range<C> range(BoundType lowerBoundType, BoundType upperBoundType) {
/*  62 */     throw new NoSuchElementException();
/*     */   }
/*     */ 
/*     */   
/*     */   ContiguousSet<C> headSetImpl(C toElement, boolean inclusive) {
/*  67 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   ContiguousSet<C> subSetImpl(C fromElement, boolean fromInclusive, C toElement, boolean toInclusive) {
/*  73 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   ContiguousSet<C> tailSetImpl(C fromElement, boolean fromInclusive) {
/*  78 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean contains(Object object) {
/*  83 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   @GwtIncompatible
/*     */   int indexOf(Object target) {
/*  89 */     return -1;
/*     */   }
/*     */ 
/*     */   
/*     */   public UnmodifiableIterator<C> iterator() {
/*  94 */     return Iterators.emptyIterator();
/*     */   }
/*     */ 
/*     */   
/*     */   @GwtIncompatible
/*     */   public UnmodifiableIterator<C> descendingIterator() {
/* 100 */     return Iterators.emptyIterator();
/*     */   }
/*     */ 
/*     */   
/*     */   boolean isPartialView() {
/* 105 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isEmpty() {
/* 110 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public ImmutableList<C> asList() {
/* 115 */     return ImmutableList.of();
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 120 */     return "[]";
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean equals(Object object) {
/* 125 */     if (object instanceof Set) {
/* 126 */       Set<?> that = (Set)object;
/* 127 */       return that.isEmpty();
/*     */     } 
/* 129 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   @GwtIncompatible
/*     */   boolean isHashCodeFast() {
/* 135 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 140 */     return 0;
/*     */   }
/*     */   
/*     */   @GwtIncompatible
/*     */   private static final class SerializedForm<C extends Comparable> implements Serializable {
/*     */     private final DiscreteDomain<C> domain;
/*     */     
/*     */     private SerializedForm(DiscreteDomain<C> domain) {
/* 148 */       this.domain = domain;
/*     */     }
/*     */     private static final long serialVersionUID = 0L;
/*     */     private Object readResolve() {
/* 152 */       return new EmptyContiguousSet<>(this.domain);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @GwtIncompatible
/*     */   Object writeReplace() {
/* 161 */     return new SerializedForm<>(this.domain);
/*     */   }
/*     */ 
/*     */   
/*     */   @GwtIncompatible
/*     */   ImmutableSortedSet<C> createDescendingSet() {
/* 167 */     return ImmutableSortedSet.emptySet(Ordering.<Comparable>natural().reverse());
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\google\common\collect\EmptyContiguousSet.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */