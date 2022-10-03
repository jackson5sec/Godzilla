/*     */ package com.google.common.collect;
/*     */ 
/*     */ import com.google.common.annotations.GwtCompatible;
/*     */ import com.google.errorprone.annotations.CanIgnoreReturnValue;
/*     */ import java.util.AbstractCollection;
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
/*     */ @GwtCompatible
/*     */ abstract class AbstractMultiset<E>
/*     */   extends AbstractCollection<E>
/*     */   implements Multiset<E>
/*     */ {
/*     */   private transient Set<E> elementSet;
/*     */   private transient Set<Multiset.Entry<E>> entrySet;
/*     */   
/*     */   public boolean isEmpty() {
/*  50 */     return entrySet().isEmpty();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean contains(Object element) {
/*  55 */     return (count(element) > 0);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @CanIgnoreReturnValue
/*     */   public final boolean add(E element) {
/*  62 */     add(element, 1);
/*  63 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   @CanIgnoreReturnValue
/*     */   public int add(E element, int occurrences) {
/*  69 */     throw new UnsupportedOperationException();
/*     */   }
/*     */ 
/*     */   
/*     */   @CanIgnoreReturnValue
/*     */   public final boolean remove(Object element) {
/*  75 */     return (remove(element, 1) > 0);
/*     */   }
/*     */ 
/*     */   
/*     */   @CanIgnoreReturnValue
/*     */   public int remove(Object element, int occurrences) {
/*  81 */     throw new UnsupportedOperationException();
/*     */   }
/*     */ 
/*     */   
/*     */   @CanIgnoreReturnValue
/*     */   public int setCount(E element, int count) {
/*  87 */     return Multisets.setCountImpl(this, element, count);
/*     */   }
/*     */ 
/*     */   
/*     */   @CanIgnoreReturnValue
/*     */   public boolean setCount(E element, int oldCount, int newCount) {
/*  93 */     return Multisets.setCountImpl(this, element, oldCount, newCount);
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
/*     */   @CanIgnoreReturnValue
/*     */   public final boolean addAll(Collection<? extends E> elementsToAdd) {
/* 107 */     return Multisets.addAllImpl(this, elementsToAdd);
/*     */   }
/*     */ 
/*     */   
/*     */   @CanIgnoreReturnValue
/*     */   public final boolean removeAll(Collection<?> elementsToRemove) {
/* 113 */     return Multisets.removeAllImpl(this, elementsToRemove);
/*     */   }
/*     */ 
/*     */   
/*     */   @CanIgnoreReturnValue
/*     */   public final boolean retainAll(Collection<?> elementsToRetain) {
/* 119 */     return Multisets.retainAllImpl(this, elementsToRetain);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public abstract void clear();
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Set<E> elementSet() {
/* 131 */     Set<E> result = this.elementSet;
/* 132 */     if (result == null) {
/* 133 */       this.elementSet = result = createElementSet();
/*     */     }
/* 135 */     return result;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   Set<E> createElementSet() {
/* 143 */     return new ElementSet();
/*     */   }
/*     */   
/*     */   abstract Iterator<E> elementIterator();
/*     */   
/*     */   class ElementSet extends Multisets.ElementSet<E> {
/*     */     Multiset<E> multiset() {
/* 150 */       return AbstractMultiset.this;
/*     */     }
/*     */ 
/*     */     
/*     */     public Iterator<E> iterator() {
/* 155 */       return AbstractMultiset.this.elementIterator();
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Set<Multiset.Entry<E>> entrySet() {
/* 165 */     Set<Multiset.Entry<E>> result = this.entrySet;
/* 166 */     if (result == null) {
/* 167 */       this.entrySet = result = createEntrySet();
/*     */     }
/* 169 */     return result;
/*     */   }
/*     */   
/*     */   class EntrySet
/*     */     extends Multisets.EntrySet<E>
/*     */   {
/*     */     Multiset<E> multiset() {
/* 176 */       return AbstractMultiset.this;
/*     */     }
/*     */ 
/*     */     
/*     */     public Iterator<Multiset.Entry<E>> iterator() {
/* 181 */       return AbstractMultiset.this.entryIterator();
/*     */     }
/*     */ 
/*     */     
/*     */     public int size() {
/* 186 */       return AbstractMultiset.this.distinctElements();
/*     */     }
/*     */   }
/*     */   
/*     */   Set<Multiset.Entry<E>> createEntrySet() {
/* 191 */     return new EntrySet();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   abstract Iterator<Multiset.Entry<E>> entryIterator();
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   abstract int distinctElements();
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final boolean equals(Object object) {
/* 208 */     return Multisets.equalsImpl(this, object);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final int hashCode() {
/* 218 */     return entrySet().hashCode();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final String toString() {
/* 229 */     return entrySet().toString();
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\google\common\collect\AbstractMultiset.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */