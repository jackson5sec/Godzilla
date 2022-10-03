/*     */ package com.google.common.collect;
/*     */ 
/*     */ import com.google.common.annotations.GwtCompatible;
/*     */ import com.google.common.base.Preconditions;
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
/*     */ @GwtCompatible(emulated = true)
/*     */ abstract class AbstractSortedMultiset<E>
/*     */   extends AbstractMultiset<E>
/*     */   implements SortedMultiset<E>
/*     */ {
/*     */   @GwtTransient
/*     */   final Comparator<? super E> comparator;
/*     */   private transient SortedMultiset<E> descendingMultiset;
/*     */   
/*     */   AbstractSortedMultiset() {
/*  43 */     this(Ordering.natural());
/*     */   }
/*     */   
/*     */   AbstractSortedMultiset(Comparator<? super E> comparator) {
/*  47 */     this.comparator = (Comparator<? super E>)Preconditions.checkNotNull(comparator);
/*     */   }
/*     */ 
/*     */   
/*     */   public NavigableSet<E> elementSet() {
/*  52 */     return (NavigableSet<E>)super.elementSet();
/*     */   }
/*     */ 
/*     */   
/*     */   NavigableSet<E> createElementSet() {
/*  57 */     return new SortedMultisets.NavigableElementSet<>(this);
/*     */   }
/*     */ 
/*     */   
/*     */   public Comparator<? super E> comparator() {
/*  62 */     return this.comparator;
/*     */   }
/*     */ 
/*     */   
/*     */   public Multiset.Entry<E> firstEntry() {
/*  67 */     Iterator<Multiset.Entry<E>> entryIterator = entryIterator();
/*  68 */     return entryIterator.hasNext() ? entryIterator.next() : null;
/*     */   }
/*     */ 
/*     */   
/*     */   public Multiset.Entry<E> lastEntry() {
/*  73 */     Iterator<Multiset.Entry<E>> entryIterator = descendingEntryIterator();
/*  74 */     return entryIterator.hasNext() ? entryIterator.next() : null;
/*     */   }
/*     */ 
/*     */   
/*     */   public Multiset.Entry<E> pollFirstEntry() {
/*  79 */     Iterator<Multiset.Entry<E>> entryIterator = entryIterator();
/*  80 */     if (entryIterator.hasNext()) {
/*  81 */       Multiset.Entry<E> result = entryIterator.next();
/*  82 */       result = Multisets.immutableEntry(result.getElement(), result.getCount());
/*  83 */       entryIterator.remove();
/*  84 */       return result;
/*     */     } 
/*  86 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   public Multiset.Entry<E> pollLastEntry() {
/*  91 */     Iterator<Multiset.Entry<E>> entryIterator = descendingEntryIterator();
/*  92 */     if (entryIterator.hasNext()) {
/*  93 */       Multiset.Entry<E> result = entryIterator.next();
/*  94 */       result = Multisets.immutableEntry(result.getElement(), result.getCount());
/*  95 */       entryIterator.remove();
/*  96 */       return result;
/*     */     } 
/*  98 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public SortedMultiset<E> subMultiset(E fromElement, BoundType fromBoundType, E toElement, BoundType toBoundType) {
/* 108 */     Preconditions.checkNotNull(fromBoundType);
/* 109 */     Preconditions.checkNotNull(toBoundType);
/* 110 */     return tailMultiset(fromElement, fromBoundType).headMultiset(toElement, toBoundType);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   Iterator<E> descendingIterator() {
/* 116 */     return Multisets.iteratorImpl(descendingMultiset());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public SortedMultiset<E> descendingMultiset() {
/* 123 */     SortedMultiset<E> result = this.descendingMultiset;
/* 124 */     return (result == null) ? (this.descendingMultiset = createDescendingMultiset()) : result;
/*     */   }
/*     */   
/*     */   SortedMultiset<E> createDescendingMultiset() {
/*     */     class DescendingMultisetImpl
/*     */       extends DescendingMultiset<E>
/*     */     {
/*     */       SortedMultiset<E> forwardMultiset() {
/* 132 */         return AbstractSortedMultiset.this;
/*     */       }
/*     */ 
/*     */       
/*     */       Iterator<Multiset.Entry<E>> entryIterator() {
/* 137 */         return AbstractSortedMultiset.this.descendingEntryIterator();
/*     */       }
/*     */ 
/*     */       
/*     */       public Iterator<E> iterator() {
/* 142 */         return AbstractSortedMultiset.this.descendingIterator();
/*     */       }
/*     */     };
/* 145 */     return new DescendingMultisetImpl();
/*     */   }
/*     */   
/*     */   abstract Iterator<Multiset.Entry<E>> descendingEntryIterator();
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\google\common\collect\AbstractSortedMultiset.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */