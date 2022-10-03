/*     */ package com.google.common.collect;
/*     */ 
/*     */ import com.google.common.annotations.GwtCompatible;
/*     */ import com.google.common.base.Objects;
/*     */ import com.google.errorprone.annotations.CanIgnoreReturnValue;
/*     */ import java.util.Collection;
/*     */ import java.util.Iterator;
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
/*     */ public abstract class ForwardingCollection<E>
/*     */   extends ForwardingObject
/*     */   implements Collection<E>
/*     */ {
/*     */   public Iterator<E> iterator() {
/*  60 */     return delegate().iterator();
/*     */   }
/*     */ 
/*     */   
/*     */   public int size() {
/*  65 */     return delegate().size();
/*     */   }
/*     */ 
/*     */   
/*     */   @CanIgnoreReturnValue
/*     */   public boolean removeAll(Collection<?> collection) {
/*  71 */     return delegate().removeAll(collection);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isEmpty() {
/*  76 */     return delegate().isEmpty();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean contains(Object object) {
/*  81 */     return delegate().contains(object);
/*     */   }
/*     */ 
/*     */   
/*     */   @CanIgnoreReturnValue
/*     */   public boolean add(E element) {
/*  87 */     return delegate().add(element);
/*     */   }
/*     */ 
/*     */   
/*     */   @CanIgnoreReturnValue
/*     */   public boolean remove(Object object) {
/*  93 */     return delegate().remove(object);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean containsAll(Collection<?> collection) {
/*  98 */     return delegate().containsAll(collection);
/*     */   }
/*     */ 
/*     */   
/*     */   @CanIgnoreReturnValue
/*     */   public boolean addAll(Collection<? extends E> collection) {
/* 104 */     return delegate().addAll(collection);
/*     */   }
/*     */ 
/*     */   
/*     */   @CanIgnoreReturnValue
/*     */   public boolean retainAll(Collection<?> collection) {
/* 110 */     return delegate().retainAll(collection);
/*     */   }
/*     */ 
/*     */   
/*     */   public void clear() {
/* 115 */     delegate().clear();
/*     */   }
/*     */ 
/*     */   
/*     */   public Object[] toArray() {
/* 120 */     return delegate().toArray();
/*     */   }
/*     */ 
/*     */   
/*     */   @CanIgnoreReturnValue
/*     */   public <T> T[] toArray(T[] array) {
/* 126 */     return delegate().toArray(array);
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
/* 137 */     return Iterators.contains(iterator(), object);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected boolean standardContainsAll(Collection<?> collection) {
/* 148 */     return Collections2.containsAllImpl(this, collection);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected boolean standardAddAll(Collection<? extends E> collection) {
/* 158 */     return Iterators.addAll(this, collection.iterator());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected boolean standardRemove(Object object) {
/* 169 */     Iterator<E> iterator = iterator();
/* 170 */     while (iterator.hasNext()) {
/* 171 */       if (Objects.equal(iterator.next(), object)) {
/* 172 */         iterator.remove();
/* 173 */         return true;
/*     */       } 
/*     */     } 
/* 176 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected boolean standardRemoveAll(Collection<?> collection) {
/* 187 */     return Iterators.removeAll(iterator(), collection);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected boolean standardRetainAll(Collection<?> collection) {
/* 198 */     return Iterators.retainAll(iterator(), collection);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void standardClear() {
/* 209 */     Iterators.clear(iterator());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected boolean standardIsEmpty() {
/* 220 */     return !iterator().hasNext();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected String standardToString() {
/* 231 */     return Collections2.toStringImpl(this);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected Object[] standardToArray() {
/* 242 */     Object[] newArray = new Object[size()];
/* 243 */     return toArray(newArray);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected <T> T[] standardToArray(T[] array) {
/* 254 */     return ObjectArrays.toArrayImpl(this, array);
/*     */   }
/*     */   
/*     */   protected abstract Collection<E> delegate();
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\google\common\collect\ForwardingCollection.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */