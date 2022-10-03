/*     */ package com.google.common.collect;
/*     */ 
/*     */ import com.google.common.annotations.Beta;
/*     */ import com.google.common.annotations.GwtCompatible;
/*     */ import com.google.errorprone.annotations.CanIgnoreReturnValue;
/*     */ import java.util.Collection;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.ListIterator;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
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
/*     */ public abstract class ForwardingList<E>
/*     */   extends ForwardingCollection<E>
/*     */   implements List<E>
/*     */ {
/*     */   public void add(int index, E element) {
/*  65 */     delegate().add(index, element);
/*     */   }
/*     */ 
/*     */   
/*     */   @CanIgnoreReturnValue
/*     */   public boolean addAll(int index, Collection<? extends E> elements) {
/*  71 */     return delegate().addAll(index, elements);
/*     */   }
/*     */ 
/*     */   
/*     */   public E get(int index) {
/*  76 */     return delegate().get(index);
/*     */   }
/*     */ 
/*     */   
/*     */   public int indexOf(Object element) {
/*  81 */     return delegate().indexOf(element);
/*     */   }
/*     */ 
/*     */   
/*     */   public int lastIndexOf(Object element) {
/*  86 */     return delegate().lastIndexOf(element);
/*     */   }
/*     */ 
/*     */   
/*     */   public ListIterator<E> listIterator() {
/*  91 */     return delegate().listIterator();
/*     */   }
/*     */ 
/*     */   
/*     */   public ListIterator<E> listIterator(int index) {
/*  96 */     return delegate().listIterator(index);
/*     */   }
/*     */ 
/*     */   
/*     */   @CanIgnoreReturnValue
/*     */   public E remove(int index) {
/* 102 */     return delegate().remove(index);
/*     */   }
/*     */ 
/*     */   
/*     */   @CanIgnoreReturnValue
/*     */   public E set(int index, E element) {
/* 108 */     return delegate().set(index, element);
/*     */   }
/*     */ 
/*     */   
/*     */   public List<E> subList(int fromIndex, int toIndex) {
/* 113 */     return delegate().subList(fromIndex, toIndex);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean equals(Object object) {
/* 118 */     return (object == this || delegate().equals(object));
/*     */   }
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 123 */     return delegate().hashCode();
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
/* 134 */     add(size(), element);
/* 135 */     return true;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected boolean standardAddAll(int index, Iterable<? extends E> elements) {
/* 146 */     return Lists.addAllImpl(this, index, elements);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected int standardIndexOf(Object element) {
/* 157 */     return Lists.indexOfImpl(this, element);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected int standardLastIndexOf(Object element) {
/* 168 */     return Lists.lastIndexOfImpl(this, element);
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
/* 179 */     return listIterator();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected ListIterator<E> standardListIterator() {
/* 190 */     return listIterator(0);
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
/*     */   protected ListIterator<E> standardListIterator(int start) {
/* 203 */     return Lists.listIteratorImpl(this, start);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Beta
/*     */   protected List<E> standardSubList(int fromIndex, int toIndex) {
/* 214 */     return Lists.subListImpl(this, fromIndex, toIndex);
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
/*     */   protected boolean standardEquals(Object object) {
/* 226 */     return Lists.equalsImpl(this, object);
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
/*     */   protected int standardHashCode() {
/* 238 */     return Lists.hashCodeImpl(this);
/*     */   }
/*     */   
/*     */   protected abstract List<E> delegate();
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\google\common\collect\ForwardingList.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */