/*    */ package org.hamcrest.core;
/*    */ 
/*    */ import org.hamcrest.Description;
/*    */ import org.hamcrest.DiagnosingMatcher;
/*    */ import org.hamcrest.Factory;
/*    */ import org.hamcrest.Matcher;
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
/*    */ public class IsInstanceOf
/*    */   extends DiagnosingMatcher<Object>
/*    */ {
/*    */   private final Class<?> expectedClass;
/*    */   private final Class<?> matchableClass;
/*    */   
/*    */   public IsInstanceOf(Class<?> expectedClass) {
/* 26 */     this.expectedClass = expectedClass;
/* 27 */     this.matchableClass = matchableClass(expectedClass);
/*    */   }
/*    */   
/*    */   private static Class<?> matchableClass(Class<?> expectedClass) {
/* 31 */     if (boolean.class.equals(expectedClass)) return Boolean.class; 
/* 32 */     if (byte.class.equals(expectedClass)) return Byte.class; 
/* 33 */     if (char.class.equals(expectedClass)) return Character.class; 
/* 34 */     if (double.class.equals(expectedClass)) return Double.class; 
/* 35 */     if (float.class.equals(expectedClass)) return Float.class; 
/* 36 */     if (int.class.equals(expectedClass)) return Integer.class; 
/* 37 */     if (long.class.equals(expectedClass)) return Long.class; 
/* 38 */     if (short.class.equals(expectedClass)) return Short.class; 
/* 39 */     return expectedClass;
/*    */   }
/*    */ 
/*    */   
/*    */   protected boolean matches(Object item, Description mismatch) {
/* 44 */     if (null == item) {
/* 45 */       mismatch.appendText("null");
/* 46 */       return false;
/*    */     } 
/*    */     
/* 49 */     if (!this.matchableClass.isInstance(item)) {
/* 50 */       mismatch.appendValue(item).appendText(" is a " + item.getClass().getName());
/* 51 */       return false;
/*    */     } 
/*    */     
/* 54 */     return true;
/*    */   }
/*    */ 
/*    */   
/*    */   public void describeTo(Description description) {
/* 59 */     description.appendText("an instance of ").appendText(this.expectedClass.getName());
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
/*    */   
/*    */   @Factory
/*    */   public static <T> Matcher<T> instanceOf(Class<?> type) {
/* 76 */     return (Matcher<T>)new IsInstanceOf(type);
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
/*    */ 
/*    */ 
/*    */   
/*    */   @Factory
/*    */   public static <T> Matcher<T> any(Class<T> type) {
/* 95 */     return (Matcher<T>)new IsInstanceOf(type);
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\hamcrest\core\IsInstanceOf.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */