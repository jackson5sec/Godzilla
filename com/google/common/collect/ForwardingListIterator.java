/*    */ package com.google.common.collect;
/*    */ 
/*    */ import com.google.common.annotations.GwtCompatible;
/*    */ import com.google.errorprone.annotations.CanIgnoreReturnValue;
/*    */ import java.util.Iterator;
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
/*    */ public abstract class ForwardingListIterator<E>
/*    */   extends ForwardingIterator<E>
/*    */   implements ListIterator<E>
/*    */ {
/*    */   public void add(E element) {
/* 50 */     delegate().add(element);
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean hasPrevious() {
/* 55 */     return delegate().hasPrevious();
/*    */   }
/*    */ 
/*    */   
/*    */   public int nextIndex() {
/* 60 */     return delegate().nextIndex();
/*    */   }
/*    */ 
/*    */   
/*    */   @CanIgnoreReturnValue
/*    */   public E previous() {
/* 66 */     return delegate().previous();
/*    */   }
/*    */ 
/*    */   
/*    */   public int previousIndex() {
/* 71 */     return delegate().previousIndex();
/*    */   }
/*    */ 
/*    */   
/*    */   public void set(E element) {
/* 76 */     delegate().set(element);
/*    */   }
/*    */   
/*    */   protected abstract ListIterator<E> delegate();
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\google\common\collect\ForwardingListIterator.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */