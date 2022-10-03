/*    */ package org.hamcrest.internal;
/*    */ 
/*    */ import java.util.Iterator;
/*    */ import org.hamcrest.SelfDescribing;
/*    */ 
/*    */ public class SelfDescribingValueIterator<T>
/*    */   implements Iterator<SelfDescribing> {
/*    */   private Iterator<T> values;
/*    */   
/*    */   public SelfDescribingValueIterator(Iterator<T> values) {
/* 11 */     this.values = values;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean hasNext() {
/* 16 */     return this.values.hasNext();
/*    */   }
/*    */ 
/*    */   
/*    */   public SelfDescribing next() {
/* 21 */     return new SelfDescribingValue(this.values.next());
/*    */   }
/*    */ 
/*    */   
/*    */   public void remove() {
/* 26 */     this.values.remove();
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\hamcrest\internal\SelfDescribingValueIterator.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */