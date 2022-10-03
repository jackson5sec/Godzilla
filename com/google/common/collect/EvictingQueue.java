/*     */ package com.google.common.collect;
/*     */ 
/*     */ import com.google.common.annotations.Beta;
/*     */ import com.google.common.annotations.GwtCompatible;
/*     */ import com.google.common.annotations.VisibleForTesting;
/*     */ import com.google.common.base.Preconditions;
/*     */ import com.google.errorprone.annotations.CanIgnoreReturnValue;
/*     */ import java.io.Serializable;
/*     */ import java.util.ArrayDeque;
/*     */ import java.util.Collection;
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
/*     */ @Beta
/*     */ @GwtCompatible
/*     */ public final class EvictingQueue<E>
/*     */   extends ForwardingQueue<E>
/*     */   implements Serializable
/*     */ {
/*     */   private final Queue<E> delegate;
/*     */   @VisibleForTesting
/*     */   final int maxSize;
/*     */   private static final long serialVersionUID = 0L;
/*     */   
/*     */   private EvictingQueue(int maxSize) {
/*  55 */     Preconditions.checkArgument((maxSize >= 0), "maxSize (%s) must >= 0", maxSize);
/*  56 */     this.delegate = new ArrayDeque<>(maxSize);
/*  57 */     this.maxSize = maxSize;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <E> EvictingQueue<E> create(int maxSize) {
/*  67 */     return new EvictingQueue<>(maxSize);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int remainingCapacity() {
/*  77 */     return this.maxSize - size();
/*     */   }
/*     */ 
/*     */   
/*     */   protected Queue<E> delegate() {
/*  82 */     return this.delegate;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @CanIgnoreReturnValue
/*     */   public boolean offer(E e) {
/*  94 */     return add(e);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @CanIgnoreReturnValue
/*     */   public boolean add(E e) {
/* 106 */     Preconditions.checkNotNull(e);
/* 107 */     if (this.maxSize == 0) {
/* 108 */       return true;
/*     */     }
/* 110 */     if (size() == this.maxSize) {
/* 111 */       this.delegate.remove();
/*     */     }
/* 113 */     this.delegate.add(e);
/* 114 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   @CanIgnoreReturnValue
/*     */   public boolean addAll(Collection<? extends E> collection) {
/* 120 */     int size = collection.size();
/* 121 */     if (size >= this.maxSize) {
/* 122 */       clear();
/* 123 */       return Iterables.addAll(this, Iterables.skip(collection, size - this.maxSize));
/*     */     } 
/* 125 */     return standardAddAll(collection);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean contains(Object object) {
/* 130 */     return delegate().contains(Preconditions.checkNotNull(object));
/*     */   }
/*     */ 
/*     */   
/*     */   @CanIgnoreReturnValue
/*     */   public boolean remove(Object object) {
/* 136 */     return delegate().remove(Preconditions.checkNotNull(object));
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\google\common\collect\EvictingQueue.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */