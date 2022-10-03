/*    */ package org.hamcrest.core;
/*    */ 
/*    */ import org.hamcrest.Description;
/*    */ import org.hamcrest.TypeSafeMatcher;
/*    */ 
/*    */ 
/*    */ 
/*    */ public abstract class SubstringMatcher
/*    */   extends TypeSafeMatcher<String>
/*    */ {
/*    */   protected final String substring;
/*    */   
/*    */   protected SubstringMatcher(String substring) {
/* 14 */     this.substring = substring;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean matchesSafely(String item) {
/* 19 */     return evalSubstringOf(item);
/*    */   }
/*    */   
/*    */   public void describeMismatchSafely(String item, Description mismatchDescription) {
/* 23 */     mismatchDescription.appendText("was \"").appendText(item).appendText("\"");
/*    */   }
/*    */ 
/*    */   
/*    */   public void describeTo(Description description) {
/* 28 */     description.appendText("a string ").appendText(relationship()).appendText(" ").appendValue(this.substring);
/*    */   }
/*    */   
/*    */   protected abstract boolean evalSubstringOf(String paramString);
/*    */   
/*    */   protected abstract String relationship();
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\hamcrest\core\SubstringMatcher.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */