/*     */ package com.google.common.collect;
/*     */ 
/*     */ import com.google.common.annotations.Beta;
/*     */ import com.google.common.annotations.GwtCompatible;
/*     */ import com.google.common.base.Objects;
/*     */ import com.google.errorprone.annotations.CanIgnoreReturnValue;
/*     */ import java.util.Collection;
/*     */ import java.util.Iterator;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
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
/*     */ public abstract class ForwardingMultiset<E>
/*     */   extends ForwardingCollection<E>
/*     */   implements Multiset<E>
/*     */ {
/*     */   public int count(Object element) {
/*  61 */     return delegate().count(element);
/*     */   }
/*     */ 
/*     */   
/*     */   @CanIgnoreReturnValue
/*     */   public int add(E element, int occurrences) {
/*  67 */     return delegate().add(element, occurrences);
/*     */   }
/*     */ 
/*     */   
/*     */   @CanIgnoreReturnValue
/*     */   public int remove(Object element, int occurrences) {
/*  73 */     return delegate().remove(element, occurrences);
/*     */   }
/*     */ 
/*     */   
/*     */   public Set<E> elementSet() {
/*  78 */     return delegate().elementSet();
/*     */   }
/*     */ 
/*     */   
/*     */   public Set<Multiset.Entry<E>> entrySet() {
/*  83 */     return delegate().entrySet();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean equals(Object object) {
/*  88 */     return (object == this || delegate().equals(object));
/*     */   }
/*     */ 
/*     */   
/*     */   public int hashCode() {
/*  93 */     return delegate().hashCode();
/*     */   }
/*     */ 
/*     */   
/*     */   @CanIgnoreReturnValue
/*     */   public int setCount(E element, int count) {
/*  99 */     return delegate().setCount(element, count);
/*     */   }
/*     */ 
/*     */   
/*     */   @CanIgnoreReturnValue
/*     */   public boolean setCount(E element, int oldCount, int newCount) {
/* 105 */     return delegate().setCount(element, oldCount, newCount);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected boolean standardContains(Object object) {
/* 116 */     return (count(object) > 0);
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
/*     */   protected void standardClear() {
/* 128 */     Iterators.clear(entrySet().iterator());
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
/*     */   protected int standardCount(Object object) {
/* 140 */     for (Multiset.Entry<?> entry : entrySet()) {
/* 141 */       if (Objects.equal(entry.getElement(), object)) {
/* 142 */         return entry.getCount();
/*     */       }
/*     */     } 
/* 145 */     return 0;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected boolean standardAdd(E element) {
/* 156 */     add(element, 1);
/* 157 */     return true;
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
/*     */   protected boolean standardAddAll(Collection<? extends E> elementsToAdd) {
/* 170 */     return Multisets.addAllImpl(this, elementsToAdd);
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
/*     */   protected boolean standardRemove(Object element) {
/* 182 */     return (remove(element, 1) > 0);
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
/*     */   protected boolean standardRemoveAll(Collection<?> elementsToRemove) {
/* 194 */     return Multisets.removeAllImpl(this, elementsToRemove);
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
/*     */   protected boolean standardRetainAll(Collection<?> elementsToRetain) {
/* 206 */     return Multisets.retainAllImpl(this, elementsToRetain);
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
/*     */   protected int standardSetCount(E element, int count) {
/* 218 */     return Multisets.setCountImpl(this, element, count);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected boolean standardSetCount(E element, int oldCount, int newCount) {
/* 229 */     return Multisets.setCountImpl(this, element, oldCount, newCount);
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
/*     */   @Beta
/*     */   protected class StandardElementSet
/*     */     extends Multisets.ElementSet<E>
/*     */   {
/*     */     Multiset<E> multiset() {
/* 250 */       return ForwardingMultiset.this;
/*     */     }
/*     */ 
/*     */     
/*     */     public Iterator<E> iterator() {
/* 255 */       return Multisets.elementIterator(multiset().entrySet().iterator());
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
/*     */   protected Iterator<E> standardIterator() {
/* 267 */     return Multisets.iteratorImpl(this);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected int standardSize() {
/* 278 */     return Multisets.linearTimeSizeImpl(this);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected boolean standardEquals(Object object) {
/* 289 */     return Multisets.equalsImpl(this, object);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected int standardHashCode() {
/* 300 */     return entrySet().hashCode();
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
/*     */   protected String standardToString() {
/* 312 */     return entrySet().toString();
/*     */   }
/*     */   
/*     */   protected abstract Multiset<E> delegate();
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\google\common\collect\ForwardingMultiset.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */