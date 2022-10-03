/*    */ package org.mozilla.javascript.typedarrays;
/*    */ 
/*    */ import java.util.ListIterator;
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
/*    */ public class NativeTypedArrayIterator<T>
/*    */   implements ListIterator<T>
/*    */ {
/*    */   private final NativeTypedArrayView<T> view;
/*    */   private int position;
/* 19 */   private int lastPosition = -1;
/*    */ 
/*    */   
/*    */   NativeTypedArrayIterator(NativeTypedArrayView<T> view, int start) {
/* 23 */     this.view = view;
/* 24 */     this.position = start;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public boolean hasNext() {
/* 30 */     return (this.position < this.view.length);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public boolean hasPrevious() {
/* 36 */     return (this.position > 0);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public int nextIndex() {
/* 42 */     return this.position;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public int previousIndex() {
/* 48 */     return this.position - 1;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public T next() {
/* 54 */     if (hasNext()) {
/* 55 */       T ret = this.view.get(this.position);
/* 56 */       this.lastPosition = this.position;
/* 57 */       this.position++;
/* 58 */       return ret;
/*    */     } 
/* 60 */     throw new NoSuchElementException();
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public T previous() {
/* 66 */     if (hasPrevious()) {
/*    */       
/* 68 */       this.lastPosition = --this.position;
/* 69 */       return this.view.get(this.position);
/*    */     } 
/* 71 */     throw new NoSuchElementException();
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public void set(T t) {
/* 77 */     if (this.lastPosition < 0) {
/* 78 */       throw new IllegalStateException();
/*    */     }
/* 80 */     this.view.js_set(this.lastPosition, t);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public void remove() {
/* 86 */     throw new UnsupportedOperationException();
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public void add(T t) {
/* 92 */     throw new UnsupportedOperationException();
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\mozilla\javascript\typedarrays\NativeTypedArrayIterator.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */