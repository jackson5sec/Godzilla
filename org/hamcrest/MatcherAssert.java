/*    */ package org.hamcrest;
/*    */ 
/*    */ 
/*    */ 
/*    */ public class MatcherAssert
/*    */ {
/*    */   public static <T> void assertThat(T actual, Matcher<? super T> matcher) {
/*  8 */     assertThat("", actual, matcher);
/*    */   }
/*    */   
/*    */   public static <T> void assertThat(String reason, T actual, Matcher<? super T> matcher) {
/* 12 */     if (!matcher.matches(actual)) {
/* 13 */       Description description = new StringDescription();
/* 14 */       description.appendText(reason).appendText("\nExpected: ").appendDescriptionOf(matcher).appendText("\n     but: ");
/*    */ 
/*    */ 
/*    */       
/* 18 */       matcher.describeMismatch(actual, description);
/*    */       
/* 20 */       throw new AssertionError(description.toString());
/*    */     } 
/*    */   }
/*    */   
/*    */   public static void assertThat(String reason, boolean assertion) {
/* 25 */     if (!assertion)
/* 26 */       throw new AssertionError(reason); 
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\hamcrest\MatcherAssert.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */