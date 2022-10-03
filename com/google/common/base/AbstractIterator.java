/*    */ package com.google.common.base;
/*    */ 
/*    */ import com.google.common.annotations.GwtCompatible;
/*    */ import com.google.errorprone.annotations.CanIgnoreReturnValue;
/*    */ import java.util.Iterator;
/*    */ import java.util.NoSuchElementException;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ @GwtCompatible
/*    */ abstract class AbstractIterator<T>
/*    */   implements Iterator<T>
/*    */ {
/*    */   private T next;
/* 31 */   private State state = State.NOT_READY;
/*    */   
/*    */   protected abstract T computeNext();
/*    */   
/*    */   private enum State {
/* 36 */     READY,
/* 37 */     NOT_READY,
/* 38 */     DONE,
/* 39 */     FAILED;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   @CanIgnoreReturnValue
/*    */   protected final T endOfData() {
/* 48 */     this.state = State.DONE;
/* 49 */     return null;
/*    */   }
/*    */ 
/*    */   
/*    */   public final boolean hasNext() {
/* 54 */     Preconditions.checkState((this.state != State.FAILED));
/* 55 */     switch (this.state) {
/*    */       case READY:
/* 57 */         return true;
/*    */       case DONE:
/* 59 */         return false;
/*    */     } 
/*    */     
/* 62 */     return tryToComputeNext();
/*    */   }
/*    */   
/*    */   private boolean tryToComputeNext() {
/* 66 */     this.state = State.FAILED;
/* 67 */     this.next = computeNext();
/* 68 */     if (this.state != State.DONE) {
/* 69 */       this.state = State.READY;
/* 70 */       return true;
/*    */     } 
/* 72 */     return false;
/*    */   }
/*    */ 
/*    */   
/*    */   public final T next() {
/* 77 */     if (!hasNext()) {
/* 78 */       throw new NoSuchElementException();
/*    */     }
/* 80 */     this.state = State.NOT_READY;
/* 81 */     T result = this.next;
/* 82 */     this.next = null;
/* 83 */     return result;
/*    */   }
/*    */ 
/*    */   
/*    */   public final void remove() {
/* 88 */     throw new UnsupportedOperationException();
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\google\common\base\AbstractIterator.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */