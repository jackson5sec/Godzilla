/*    */ package com.google.common.collect;
/*    */ 
/*    */ import com.google.common.annotations.GwtCompatible;
/*    */ import java.util.ListIterator;
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
/*    */ abstract class TransformedListIterator<F, T>
/*    */   extends TransformedIterator<F, T>
/*    */   implements ListIterator<T>
/*    */ {
/*    */   TransformedListIterator(ListIterator<? extends F> backingIterator) {
/* 33 */     super(backingIterator);
/*    */   }
/*    */   
/*    */   private ListIterator<? extends F> backingIterator() {
/* 37 */     return Iterators.cast(this.backingIterator);
/*    */   }
/*    */ 
/*    */   
/*    */   public final boolean hasPrevious() {
/* 42 */     return backingIterator().hasPrevious();
/*    */   }
/*    */ 
/*    */   
/*    */   public final T previous() {
/* 47 */     return transform(backingIterator().previous());
/*    */   }
/*    */ 
/*    */   
/*    */   public final int nextIndex() {
/* 52 */     return backingIterator().nextIndex();
/*    */   }
/*    */ 
/*    */   
/*    */   public final int previousIndex() {
/* 57 */     return backingIterator().previousIndex();
/*    */   }
/*    */ 
/*    */   
/*    */   public void set(T element) {
/* 62 */     throw new UnsupportedOperationException();
/*    */   }
/*    */ 
/*    */   
/*    */   public void add(T element) {
/* 67 */     throw new UnsupportedOperationException();
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\google\common\collect\TransformedListIterator.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */