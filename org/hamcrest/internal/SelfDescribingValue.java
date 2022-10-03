/*    */ package org.hamcrest.internal;
/*    */ 
/*    */ import org.hamcrest.Description;
/*    */ import org.hamcrest.SelfDescribing;
/*    */ 
/*    */ public class SelfDescribingValue<T> implements SelfDescribing {
/*    */   private T value;
/*    */   
/*    */   public SelfDescribingValue(T value) {
/* 10 */     this.value = value;
/*    */   }
/*    */ 
/*    */   
/*    */   public void describeTo(Description description) {
/* 15 */     description.appendValue(this.value);
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\hamcrest\internal\SelfDescribingValue.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */