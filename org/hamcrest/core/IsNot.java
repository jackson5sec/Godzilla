/*    */ package org.hamcrest.core;
/*    */ 
/*    */ import org.hamcrest.BaseMatcher;
/*    */ import org.hamcrest.Description;
/*    */ import org.hamcrest.Factory;
/*    */ import org.hamcrest.Matcher;
/*    */ import org.hamcrest.SelfDescribing;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class IsNot<T>
/*    */   extends BaseMatcher<T>
/*    */ {
/*    */   private final Matcher<T> matcher;
/*    */   
/*    */   public IsNot(Matcher<T> matcher) {
/* 20 */     this.matcher = matcher;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean matches(Object arg) {
/* 25 */     return !this.matcher.matches(arg);
/*    */   }
/*    */ 
/*    */   
/*    */   public void describeTo(Description description) {
/* 30 */     description.appendText("not ").appendDescriptionOf((SelfDescribing)this.matcher);
/*    */   }
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
/*    */   @Factory
/*    */   public static <T> Matcher<T> not(Matcher<T> matcher) {
/* 46 */     return (Matcher<T>)new IsNot<T>(matcher);
/*    */   }
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
/*    */   @Factory
/*    */   public static <T> Matcher<T> not(T value) {
/* 62 */     return not(IsEqual.equalTo(value));
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\hamcrest\core\IsNot.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */