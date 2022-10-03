/*    */ package com.google.common.collect;
/*    */ 
/*    */ import com.google.common.annotations.GwtCompatible;
/*    */ import com.google.common.annotations.VisibleForTesting;
/*    */ import java.util.ListIterator;
/*    */ import java.util.Spliterator;
/*    */ import java.util.Spliterators;
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
/*    */ @GwtCompatible(serializable = true, emulated = true)
/*    */ class RegularImmutableList<E>
/*    */   extends ImmutableList<E>
/*    */ {
/* 32 */   static final ImmutableList<Object> EMPTY = new RegularImmutableList(new Object[0]);
/*    */   @VisibleForTesting
/*    */   final transient Object[] array;
/*    */   
/*    */   RegularImmutableList(Object[] array) {
/* 37 */     this.array = array;
/*    */   }
/*    */ 
/*    */   
/*    */   public int size() {
/* 42 */     return this.array.length;
/*    */   }
/*    */ 
/*    */   
/*    */   boolean isPartialView() {
/* 47 */     return false;
/*    */   }
/*    */ 
/*    */   
/*    */   Object[] internalArray() {
/* 52 */     return this.array;
/*    */   }
/*    */ 
/*    */   
/*    */   int internalArrayStart() {
/* 57 */     return 0;
/*    */   }
/*    */ 
/*    */   
/*    */   int internalArrayEnd() {
/* 62 */     return this.array.length;
/*    */   }
/*    */ 
/*    */   
/*    */   int copyIntoArray(Object[] dst, int dstOff) {
/* 67 */     System.arraycopy(this.array, 0, dst, dstOff, this.array.length);
/* 68 */     return dstOff + this.array.length;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public E get(int index) {
/* 75 */     return (E)this.array[index];
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public UnmodifiableListIterator<E> listIterator(int index) {
/* 83 */     return Iterators.forArray((E[])this.array, 0, this.array.length, index);
/*    */   }
/*    */ 
/*    */   
/*    */   public Spliterator<E> spliterator() {
/* 88 */     return Spliterators.spliterator(this.array, 1296);
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\google\common\collect\RegularImmutableList.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */