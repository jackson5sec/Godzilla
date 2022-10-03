/*    */ package org.hamcrest;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public abstract class DiagnosingMatcher<T>
/*    */   extends BaseMatcher<T>
/*    */ {
/*    */   public final boolean matches(Object item) {
/* 12 */     return matches(item, Description.NONE);
/*    */   }
/*    */ 
/*    */   
/*    */   public final void describeMismatch(Object item, Description mismatchDescription) {
/* 17 */     matches(item, mismatchDescription);
/*    */   }
/*    */   
/*    */   protected abstract boolean matches(Object paramObject, Description paramDescription);
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\hamcrest\DiagnosingMatcher.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */