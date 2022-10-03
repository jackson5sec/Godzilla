/*    */ package org.springframework.util;
/*    */ 
/*    */ import java.util.Iterator;
/*    */ import java.util.LinkedHashSet;
/*    */ import java.util.NoSuchElementException;
/*    */ import java.util.Set;
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
/*    */ public class CompositeIterator<E>
/*    */   implements Iterator<E>
/*    */ {
/* 38 */   private final Set<Iterator<E>> iterators = new LinkedHashSet<>();
/*    */ 
/*    */ 
/*    */   
/*    */   private boolean inUse = false;
/*    */ 
/*    */ 
/*    */   
/*    */   public void add(Iterator<E> iterator) {
/* 47 */     Assert.state(!this.inUse, "You can no longer add iterators to a composite iterator that's already in use");
/* 48 */     if (this.iterators.contains(iterator)) {
/* 49 */       throw new IllegalArgumentException("You cannot add the same iterator twice");
/*    */     }
/* 51 */     this.iterators.add(iterator);
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean hasNext() {
/* 56 */     this.inUse = true;
/* 57 */     for (Iterator<E> iterator : this.iterators) {
/* 58 */       if (iterator.hasNext()) {
/* 59 */         return true;
/*    */       }
/*    */     } 
/* 62 */     return false;
/*    */   }
/*    */ 
/*    */   
/*    */   public E next() {
/* 67 */     this.inUse = true;
/* 68 */     for (Iterator<E> iterator : this.iterators) {
/* 69 */       if (iterator.hasNext()) {
/* 70 */         return iterator.next();
/*    */       }
/*    */     } 
/* 73 */     throw new NoSuchElementException("All iterators exhausted");
/*    */   }
/*    */ 
/*    */   
/*    */   public void remove() {
/* 78 */     throw new UnsupportedOperationException("CompositeIterator does not support remove()");
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\springframewor\\util\CompositeIterator.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */