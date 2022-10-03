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
/*    */ public abstract class BaseMatcher<T>
/*    */   implements Matcher<T>
/*    */ {
/*    */   @Deprecated
/*    */   public final void _dont_implement_Matcher___instead_extend_BaseMatcher_() {}
/*    */   
/*    */   public void describeMismatch(Object item, Description description) {
/* 23 */     description.appendText("was ").appendValue(item);
/*    */   }
/*    */ 
/*    */   
/*    */   public String toString() {
/* 28 */     return StringDescription.toString(this);
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\hamcrest\BaseMatcher.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */