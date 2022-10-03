/*    */ package org.hamcrest;
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
/*    */ public abstract class CustomTypeSafeMatcher<T>
/*    */   extends TypeSafeMatcher<T>
/*    */ {
/*    */   private final String fixedDescription;
/*    */   
/*    */   public CustomTypeSafeMatcher(String description) {
/* 29 */     if (description == null) {
/* 30 */       throw new IllegalArgumentException("Description must be non null!");
/*    */     }
/* 32 */     this.fixedDescription = description;
/*    */   }
/*    */ 
/*    */   
/*    */   public final void describeTo(Description description) {
/* 37 */     description.appendText(this.fixedDescription);
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\hamcrest\CustomTypeSafeMatcher.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */