/*     */ package com.google.common.util.concurrent;
/*     */ 
/*     */ import com.google.common.annotations.GwtIncompatible;
/*     */ import com.google.common.collect.ForwardingDeque;
/*     */ import java.util.Collection;
/*     */ import java.util.Deque;
/*     */ import java.util.Queue;
/*     */ import java.util.concurrent.BlockingDeque;
/*     */ import java.util.concurrent.TimeUnit;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
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
/*     */ public abstract class ForwardingBlockingDeque<E>
/*     */   extends ForwardingDeque<E>
/*     */   implements BlockingDeque<E>
/*     */ {
/*     */   public int remainingCapacity() {
/*  58 */     return delegate().remainingCapacity();
/*     */   }
/*     */ 
/*     */   
/*     */   public void putFirst(E e) throws InterruptedException {
/*  63 */     delegate().putFirst(e);
/*     */   }
/*     */ 
/*     */   
/*     */   public void putLast(E e) throws InterruptedException {
/*  68 */     delegate().putLast(e);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean offerFirst(E e, long timeout, TimeUnit unit) throws InterruptedException {
/*  73 */     return delegate().offerFirst(e, timeout, unit);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean offerLast(E e, long timeout, TimeUnit unit) throws InterruptedException {
/*  78 */     return delegate().offerLast(e, timeout, unit);
/*     */   }
/*     */ 
/*     */   
/*     */   public E takeFirst() throws InterruptedException {
/*  83 */     return delegate().takeFirst();
/*     */   }
/*     */ 
/*     */   
/*     */   public E takeLast() throws InterruptedException {
/*  88 */     return delegate().takeLast();
/*     */   }
/*     */ 
/*     */   
/*     */   public E pollFirst(long timeout, TimeUnit unit) throws InterruptedException {
/*  93 */     return delegate().pollFirst(timeout, unit);
/*     */   }
/*     */ 
/*     */   
/*     */   public E pollLast(long timeout, TimeUnit unit) throws InterruptedException {
/*  98 */     return delegate().pollLast(timeout, unit);
/*     */   }
/*     */ 
/*     */   
/*     */   public void put(E e) throws InterruptedException {
/* 103 */     delegate().put(e);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean offer(E e, long timeout, TimeUnit unit) throws InterruptedException {
/* 108 */     return delegate().offer(e, timeout, unit);
/*     */   }
/*     */ 
/*     */   
/*     */   public E take() throws InterruptedException {
/* 113 */     return delegate().take();
/*     */   }
/*     */ 
/*     */   
/*     */   public E poll(long timeout, TimeUnit unit) throws InterruptedException {
/* 118 */     return delegate().poll(timeout, unit);
/*     */   }
/*     */ 
/*     */   
/*     */   public int drainTo(Collection<? super E> c) {
/* 123 */     return delegate().drainTo(c);
/*     */   }
/*     */ 
/*     */   
/*     */   public int drainTo(Collection<? super E> c, int maxElements) {
/* 128 */     return delegate().drainTo(c, maxElements);
/*     */   }
/*     */   
/*     */   protected abstract BlockingDeque<E> delegate();
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\google\commo\\util\concurrent\ForwardingBlockingDeque.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */