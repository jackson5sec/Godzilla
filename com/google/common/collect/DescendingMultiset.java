/*     */ package com.google.common.collect;
/*     */ 
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
/*     */ @GwtCompatible(emulated = true)
/*     */ abstract class DescendingMultiset<E>
/*     */   extends ForwardingMultiset<E>
/*     */   implements SortedMultiset<E>
/*     */ {
/*     */   private transient Comparator<? super E> comparator;
/*     */   private transient NavigableSet<E> elementSet;
/*     */   private transient Set<Multiset.Entry<E>> entrySet;
/*     */   
/*     */   public Comparator<? super E> comparator() {
/*  41 */     Comparator<? super E> result = this.comparator;
/*  42 */     if (result == null) {
/*  43 */       return this.comparator = Ordering.from(forwardMultiset().comparator()).<Object>reverse();
/*     */     }
/*  45 */     return result;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public NavigableSet<E> elementSet() {
/*  52 */     NavigableSet<E> result = this.elementSet;
/*  53 */     if (result == null) {
/*  54 */       return this.elementSet = new SortedMultisets.NavigableElementSet<>(this);
/*     */     }
/*  56 */     return result;
/*     */   }
/*     */ 
/*     */   
/*     */   public Multiset.Entry<E> pollFirstEntry() {
/*  61 */     return forwardMultiset().pollLastEntry();
/*     */   }
/*     */ 
/*     */   
/*     */   public Multiset.Entry<E> pollLastEntry() {
/*  66 */     return forwardMultiset().pollFirstEntry();
/*     */   }
/*     */ 
/*     */   
/*     */   public SortedMultiset<E> headMultiset(E toElement, BoundType boundType) {
/*  71 */     return forwardMultiset().tailMultiset(toElement, boundType).descendingMultiset();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public SortedMultiset<E> subMultiset(E fromElement, BoundType fromBoundType, E toElement, BoundType toBoundType) {
/*  77 */     return forwardMultiset()
/*  78 */       .subMultiset(toElement, toBoundType, fromElement, fromBoundType)
/*  79 */       .descendingMultiset();
/*     */   }
/*     */ 
/*     */   
/*     */   public SortedMultiset<E> tailMultiset(E fromElement, BoundType boundType) {
/*  84 */     return forwardMultiset().headMultiset(fromElement, boundType).descendingMultiset();
/*     */   }
/*     */ 
/*     */   
/*     */   protected Multiset<E> delegate() {
/*  89 */     return forwardMultiset();
/*     */   }
/*     */ 
/*     */   
/*     */   public SortedMultiset<E> descendingMultiset() {
/*  94 */     return forwardMultiset();
/*     */   }
/*     */ 
/*     */   
/*     */   public Multiset.Entry<E> firstEntry() {
/*  99 */     return forwardMultiset().lastEntry();
/*     */   }
/*     */ 
/*     */   
/*     */   public Multiset.Entry<E> lastEntry() {
/* 104 */     return forwardMultiset().firstEntry();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Set<Multiset.Entry<E>> entrySet() {
/* 113 */     Set<Multiset.Entry<E>> result = this.entrySet;
/* 114 */     return (result == null) ? (this.entrySet = createEntrySet()) : result;
/*     */   }
/*     */   
/*     */   Set<Multiset.Entry<E>> createEntrySet() {
/*     */     class EntrySetImpl
/*     */       extends Multisets.EntrySet<E>
/*     */     {
/*     */       Multiset<E> multiset() {
/* 122 */         return DescendingMultiset.this;
/*     */       }
/*     */ 
/*     */       
/*     */       public Iterator<Multiset.Entry<E>> iterator() {
/* 127 */         return DescendingMultiset.this.entryIterator();
/*     */       }
/*     */ 
/*     */       
/*     */       public int size() {
/* 132 */         return DescendingMultiset.this.forwardMultiset().entrySet().size();
/*     */       }
/*     */     };
/* 135 */     return new EntrySetImpl();
/*     */   }
/*     */ 
/*     */   
/*     */   public Iterator<E> iterator() {
/* 140 */     return Multisets.iteratorImpl(this);
/*     */   }
/*     */ 
/*     */   
/*     */   public Object[] toArray() {
/* 145 */     return standardToArray();
/*     */   }
/*     */ 
/*     */   
/*     */   public <T> T[] toArray(T[] array) {
/* 150 */     return (T[])standardToArray((Object[])array);
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 155 */     return entrySet().toString();
/*     */   }
/*     */   
/*     */   abstract SortedMultiset<E> forwardMultiset();
/*     */   
/*     */   abstract Iterator<Multiset.Entry<E>> entryIterator();
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\google\common\collect\DescendingMultiset.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */