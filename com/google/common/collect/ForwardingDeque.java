/*     */ package com.google.common.collect;
/*     */ 
/*     */ import com.google.common.annotations.GwtIncompatible;
/*     */ import com.google.errorprone.annotations.CanIgnoreReturnValue;
/*     */ import java.util.Collection;
/*     */ import java.util.Deque;
/*     */ import java.util.Iterator;
/*     */ import java.util.Queue;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @GwtIncompatible
/*     */ public abstract class ForwardingDeque<E>
/*     */   extends ForwardingQueue<E>
/*     */   implements Deque<E>
/*     */ {
/*     */   public void addFirst(E e) {
/*  52 */     delegate().addFirst(e);
/*     */   }
/*     */ 
/*     */   
/*     */   public void addLast(E e) {
/*  57 */     delegate().addLast(e);
/*     */   }
/*     */ 
/*     */   
/*     */   public Iterator<E> descendingIterator() {
/*  62 */     return delegate().descendingIterator();
/*     */   }
/*     */ 
/*     */   
/*     */   public E getFirst() {
/*  67 */     return delegate().getFirst();
/*     */   }
/*     */ 
/*     */   
/*     */   public E getLast() {
/*  72 */     return delegate().getLast();
/*     */   }
/*     */ 
/*     */   
/*     */   @CanIgnoreReturnValue
/*     */   public boolean offerFirst(E e) {
/*  78 */     return delegate().offerFirst(e);
/*     */   }
/*     */ 
/*     */   
/*     */   @CanIgnoreReturnValue
/*     */   public boolean offerLast(E e) {
/*  84 */     return delegate().offerLast(e);
/*     */   }
/*     */ 
/*     */   
/*     */   public E peekFirst() {
/*  89 */     return delegate().peekFirst();
/*     */   }
/*     */ 
/*     */   
/*     */   public E peekLast() {
/*  94 */     return delegate().peekLast();
/*     */   }
/*     */ 
/*     */   
/*     */   @CanIgnoreReturnValue
/*     */   public E pollFirst() {
/* 100 */     return delegate().pollFirst();
/*     */   }
/*     */ 
/*     */   
/*     */   @CanIgnoreReturnValue
/*     */   public E pollLast() {
/* 106 */     return delegate().pollLast();
/*     */   }
/*     */ 
/*     */   
/*     */   @CanIgnoreReturnValue
/*     */   public E pop() {
/* 112 */     return delegate().pop();
/*     */   }
/*     */ 
/*     */   
/*     */   public void push(E e) {
/* 117 */     delegate().push(e);
/*     */   }
/*     */ 
/*     */   
/*     */   @CanIgnoreReturnValue
/*     */   public E removeFirst() {
/* 123 */     return delegate().removeFirst();
/*     */   }
/*     */ 
/*     */   
/*     */   @CanIgnoreReturnValue
/*     */   public E removeLast() {
/* 129 */     return delegate().removeLast();
/*     */   }
/*     */ 
/*     */   
/*     */   @CanIgnoreReturnValue
/*     */   public boolean removeFirstOccurrence(Object o) {
/* 135 */     return delegate().removeFirstOccurrence(o);
/*     */   }
/*     */ 
/*     */   
/*     */   @CanIgnoreReturnValue
/*     */   public boolean removeLastOccurrence(Object o) {
/* 141 */     return delegate().removeLastOccurrence(o);
/*     */   }
/*     */   
/*     */   protected abstract Deque<E> delegate();
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\google\common\collect\ForwardingDeque.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */