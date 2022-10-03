/*    */ package com.google.common.collect;
/*    */ 
/*    */ import com.google.common.annotations.GwtCompatible;
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
/*    */ 
/*    */ @GwtCompatible
/*    */ public abstract class AbstractSequentialIterator<T>
/*    */   extends UnmodifiableIterator<T>
/*    */ {
/*    */   private T nextOrNull;
/*    */   
/*    */   protected AbstractSequentialIterator(T firstOrNull) {
/* 51 */     this.nextOrNull = firstOrNull;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   protected abstract T computeNext(T paramT);
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public final boolean hasNext() {
/* 64 */     return (this.nextOrNull != null);
/*    */   }
/*    */ 
/*    */   
/*    */   public final T next() {
/* 69 */     if (!hasNext()) {
/* 70 */       throw new NoSuchElementException();
/*    */     }
/*    */     try {
/* 73 */       return this.nextOrNull;
/*    */     } finally {
/* 75 */       this.nextOrNull = computeNext(this.nextOrNull);
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\google\common\collect\AbstractSequentialIterator.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */