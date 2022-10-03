/*    */ package com.google.common.collect;
/*    */ 
/*    */ import com.google.common.annotations.GwtCompatible;
/*    */ import com.google.common.base.Preconditions;
/*    */ import java.util.Iterator;
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
/*    */ abstract class TransformedIterator<F, T>
/*    */   implements Iterator<T>
/*    */ {
/*    */   final Iterator<? extends F> backingIterator;
/*    */   
/*    */   TransformedIterator(Iterator<? extends F> backingIterator) {
/* 35 */     this.backingIterator = (Iterator<? extends F>)Preconditions.checkNotNull(backingIterator);
/*    */   }
/*    */ 
/*    */   
/*    */   abstract T transform(F paramF);
/*    */   
/*    */   public final boolean hasNext() {
/* 42 */     return this.backingIterator.hasNext();
/*    */   }
/*    */ 
/*    */   
/*    */   public final T next() {
/* 47 */     return transform(this.backingIterator.next());
/*    */   }
/*    */ 
/*    */   
/*    */   public final void remove() {
/* 52 */     this.backingIterator.remove();
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\google\common\collect\TransformedIterator.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */