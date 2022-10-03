/*    */ package org.hamcrest.core;
/*    */ 
/*    */ import java.lang.reflect.Array;
/*    */ import org.hamcrest.BaseMatcher;
/*    */ import org.hamcrest.Description;
/*    */ import org.hamcrest.Factory;
/*    */ import org.hamcrest.Matcher;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class IsEqual<T>
/*    */   extends BaseMatcher<T>
/*    */ {
/*    */   private final Object expectedValue;
/*    */   
/*    */   public IsEqual(T equalArg) {
/* 21 */     this.expectedValue = equalArg;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean matches(Object actualValue) {
/* 26 */     return areEqual(actualValue, this.expectedValue);
/*    */   }
/*    */ 
/*    */   
/*    */   public void describeTo(Description description) {
/* 31 */     description.appendValue(this.expectedValue);
/*    */   }
/*    */   
/*    */   private static boolean areEqual(Object actual, Object expected) {
/* 35 */     if (actual == null) {
/* 36 */       return (expected == null);
/*    */     }
/*    */     
/* 39 */     if (expected != null && isArray(actual)) {
/* 40 */       return (isArray(expected) && areArraysEqual(actual, expected));
/*    */     }
/*    */     
/* 43 */     return actual.equals(expected);
/*    */   }
/*    */   
/*    */   private static boolean areArraysEqual(Object actualArray, Object expectedArray) {
/* 47 */     return (areArrayLengthsEqual(actualArray, expectedArray) && areArrayElementsEqual(actualArray, expectedArray));
/*    */   }
/*    */   
/*    */   private static boolean areArrayLengthsEqual(Object actualArray, Object expectedArray) {
/* 51 */     return (Array.getLength(actualArray) == Array.getLength(expectedArray));
/*    */   }
/*    */   
/*    */   private static boolean areArrayElementsEqual(Object actualArray, Object expectedArray) {
/* 55 */     for (int i = 0; i < Array.getLength(actualArray); i++) {
/* 56 */       if (!areEqual(Array.get(actualArray, i), Array.get(expectedArray, i))) {
/* 57 */         return false;
/*    */       }
/*    */     } 
/* 60 */     return true;
/*    */   }
/*    */   
/*    */   private static boolean isArray(Object o) {
/* 64 */     return o.getClass().isArray();
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
/*    */   public static <T> Matcher<T> equalTo(T operand) {
/* 92 */     return (Matcher<T>)new IsEqual<T>(operand);
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\hamcrest\core\IsEqual.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */