/*     */ package com.google.common.collect;
/*     */ 
/*     */ import com.google.common.annotations.Beta;
/*     */ import com.google.common.annotations.GwtCompatible;
/*     */ import java.util.Collection;
/*     */ import java.util.Comparator;
/*     */ import java.util.Iterator;
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
/*     */ 
/*     */ 
/*     */ @GwtCompatible
/*     */ public abstract class ForwardingSortedSet<E>
/*     */   extends ForwardingSet<E>
/*     */   implements SortedSet<E>
/*     */ {
/*     */   public Comparator<? super E> comparator() {
/*  65 */     return delegate().comparator();
/*     */   }
/*     */ 
/*     */   
/*     */   public E first() {
/*  70 */     return delegate().first();
/*     */   }
/*     */ 
/*     */   
/*     */   public SortedSet<E> headSet(E toElement) {
/*  75 */     return delegate().headSet(toElement);
/*     */   }
/*     */ 
/*     */   
/*     */   public E last() {
/*  80 */     return delegate().last();
/*     */   }
/*     */ 
/*     */   
/*     */   public SortedSet<E> subSet(E fromElement, E toElement) {
/*  85 */     return delegate().subSet(fromElement, toElement);
/*     */   }
/*     */ 
/*     */   
/*     */   public SortedSet<E> tailSet(E fromElement) {
/*  90 */     return delegate().tailSet(fromElement);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private int unsafeCompare(Object o1, Object o2) {
/*  96 */     Comparator<? super E> comparator = comparator();
/*  97 */     return (comparator == null) ? ((Comparable<Object>)o1)
/*  98 */       .compareTo(o2) : comparator
/*  99 */       .compare((E)o1, (E)o2);
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
/*     */   @Beta
/*     */   protected boolean standardContains(Object object) {
/*     */     try {
/* 115 */       SortedSet<Object> self = (SortedSet)this;
/* 116 */       Object ceiling = self.tailSet(object).first();
/* 117 */       return (unsafeCompare(ceiling, object) == 0);
/* 118 */     } catch (ClassCastException|java.util.NoSuchElementException|NullPointerException e) {
/* 119 */       return false;
/*     */     } 
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
/*     */   @Beta
/*     */   protected boolean standardRemove(Object object) {
/*     */     try {
/* 136 */       SortedSet<Object> self = (SortedSet)this;
/* 137 */       Iterator<Object> iterator = self.tailSet(object).iterator();
/* 138 */       if (iterator.hasNext()) {
/* 139 */         Object ceiling = iterator.next();
/* 140 */         if (unsafeCompare(ceiling, object) == 0) {
/* 141 */           iterator.remove();
/* 142 */           return true;
/*     */         } 
/*     */       } 
/* 145 */     } catch (ClassCastException|NullPointerException e) {
/* 146 */       return false;
/*     */     } 
/* 148 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Beta
/*     */   protected SortedSet<E> standardSubSet(E fromElement, E toElement) {
/* 160 */     return tailSet(fromElement).headSet(toElement);
/*     */   }
/*     */   
/*     */   protected abstract SortedSet<E> delegate();
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\google\common\collect\ForwardingSortedSet.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */