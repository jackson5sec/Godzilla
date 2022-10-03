/*     */ package com.google.common.collect;
/*     */ 
/*     */ import com.google.common.annotations.GwtCompatible;
/*     */ import com.google.common.base.Preconditions;
/*     */ import com.google.errorprone.annotations.CanIgnoreReturnValue;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
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
/*     */ public abstract class AbstractIterator<T>
/*     */   extends UnmodifiableIterator<T>
/*     */ {
/*  65 */   private State state = State.NOT_READY;
/*     */   private T next;
/*     */   
/*     */   protected abstract T computeNext();
/*     */   
/*     */   private enum State
/*     */   {
/*  72 */     READY,
/*     */ 
/*     */     
/*  75 */     NOT_READY,
/*     */ 
/*     */     
/*  78 */     DONE,
/*     */ 
/*     */     
/*  81 */     FAILED;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
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
/*     */   protected final T endOfData() {
/* 121 */     this.state = State.DONE;
/* 122 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   @CanIgnoreReturnValue
/*     */   public final boolean hasNext() {
/* 128 */     Preconditions.checkState((this.state != State.FAILED));
/* 129 */     switch (this.state) {
/*     */       case DONE:
/* 131 */         return false;
/*     */       case READY:
/* 133 */         return true;
/*     */     } 
/*     */     
/* 136 */     return tryToComputeNext();
/*     */   }
/*     */   
/*     */   private boolean tryToComputeNext() {
/* 140 */     this.state = State.FAILED;
/* 141 */     this.next = computeNext();
/* 142 */     if (this.state != State.DONE) {
/* 143 */       this.state = State.READY;
/* 144 */       return true;
/*     */     } 
/* 146 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   @CanIgnoreReturnValue
/*     */   public final T next() {
/* 152 */     if (!hasNext()) {
/* 153 */       throw new NoSuchElementException();
/*     */     }
/* 155 */     this.state = State.NOT_READY;
/* 156 */     T result = this.next;
/* 157 */     this.next = null;
/* 158 */     return result;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final T peek() {
/* 169 */     if (!hasNext()) {
/* 170 */       throw new NoSuchElementException();
/*     */     }
/* 172 */     return this.next;
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\google\common\collect\AbstractIterator.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */