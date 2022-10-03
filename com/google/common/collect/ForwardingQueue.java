/*     */ package com.google.common.collect;
/*     */ 
/*     */ import com.google.common.annotations.GwtCompatible;
/*     */ import com.google.errorprone.annotations.CanIgnoreReturnValue;
/*     */ import java.util.Collection;
/*     */ import java.util.NoSuchElementException;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @GwtCompatible
/*     */ public abstract class ForwardingQueue<E>
/*     */   extends ForwardingCollection<E>
/*     */   implements Queue<E>
/*     */ {
/*     */   @CanIgnoreReturnValue
/*     */   public boolean offer(E o) {
/*  58 */     return delegate().offer(o);
/*     */   }
/*     */ 
/*     */   
/*     */   @CanIgnoreReturnValue
/*     */   public E poll() {
/*  64 */     return delegate().poll();
/*     */   }
/*     */ 
/*     */   
/*     */   @CanIgnoreReturnValue
/*     */   public E remove() {
/*  70 */     return delegate().remove();
/*     */   }
/*     */ 
/*     */   
/*     */   public E peek() {
/*  75 */     return delegate().peek();
/*     */   }
/*     */ 
/*     */   
/*     */   public E element() {
/*  80 */     return delegate().element();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected boolean standardOffer(E e) {
/*     */     try {
/*  91 */       return add(e);
/*  92 */     } catch (IllegalStateException caught) {
/*  93 */       return false;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected E standardPeek() {
/*     */     try {
/* 105 */       return element();
/* 106 */     } catch (NoSuchElementException caught) {
/* 107 */       return null;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected E standardPoll() {
/*     */     try {
/* 119 */       return remove();
/* 120 */     } catch (NoSuchElementException caught) {
/* 121 */       return null;
/*     */     } 
/*     */   }
/*     */   
/*     */   protected abstract Queue<E> delegate();
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\google\common\collect\ForwardingQueue.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */