/*     */ package com.google.common.collect;
/*     */ 
/*     */ import com.google.common.annotations.GwtCompatible;
/*     */ import com.google.common.base.Preconditions;
/*     */ import java.util.NoSuchElementException;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
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
/*     */ abstract class AbstractIndexedListIterator<E>
/*     */   extends UnmodifiableListIterator<E>
/*     */ {
/*     */   private final int size;
/*     */   private int position;
/*     */   
/*     */   protected abstract E get(int paramInt);
/*     */   
/*     */   protected AbstractIndexedListIterator(int size) {
/*  48 */     this(size, 0);
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
/*     */   protected AbstractIndexedListIterator(int size, int position) {
/*  62 */     Preconditions.checkPositionIndex(position, size);
/*  63 */     this.size = size;
/*  64 */     this.position = position;
/*     */   }
/*     */ 
/*     */   
/*     */   public final boolean hasNext() {
/*  69 */     return (this.position < this.size);
/*     */   }
/*     */ 
/*     */   
/*     */   public final E next() {
/*  74 */     if (!hasNext()) {
/*  75 */       throw new NoSuchElementException();
/*     */     }
/*  77 */     return get(this.position++);
/*     */   }
/*     */ 
/*     */   
/*     */   public final int nextIndex() {
/*  82 */     return this.position;
/*     */   }
/*     */ 
/*     */   
/*     */   public final boolean hasPrevious() {
/*  87 */     return (this.position > 0);
/*     */   }
/*     */ 
/*     */   
/*     */   public final E previous() {
/*  92 */     if (!hasPrevious()) {
/*  93 */       throw new NoSuchElementException();
/*     */     }
/*  95 */     return get(--this.position);
/*     */   }
/*     */ 
/*     */   
/*     */   public final int previousIndex() {
/* 100 */     return this.position - 1;
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\google\common\collect\AbstractIndexedListIterator.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */