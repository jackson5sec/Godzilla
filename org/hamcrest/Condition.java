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
/*    */ public abstract class Condition<T>
/*    */ {
/* 14 */   public static final NotMatched<Object> NOT_MATCHED = new NotMatched();
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   private Condition() {}
/*    */ 
/*    */ 
/*    */   
/*    */   public final boolean matching(Matcher<T> match)
/*    */   {
/* 25 */     return matching(match, ""); } public final <U> Condition<U> then(Step<? super T, U> mapping) {
/* 26 */     return and(mapping);
/*    */   }
/*    */   
/*    */   public static <T> Condition<T> notMatched() {
/* 30 */     return NOT_MATCHED;
/*    */   }
/*    */   
/*    */   public static <T> Condition<T> matched(T theValue, Description mismatch) {
/* 34 */     return new Matched<T>(theValue, mismatch);
/*    */   }
/*    */   public abstract boolean matching(Matcher<T> paramMatcher, String paramString);
/*    */   public abstract <U> Condition<U> and(Step<? super T, U> paramStep);
/*    */   public static interface Step<I, O> {
/*    */     Condition<O> apply(I param1I, Description param1Description); }
/*    */   private static final class Matched<T> extends Condition<T> { private final T theValue;
/*    */     private Matched(T theValue, Description mismatch) {
/* 42 */       this.theValue = theValue;
/* 43 */       this.mismatch = mismatch;
/*    */     }
/*    */     private final Description mismatch;
/*    */     
/*    */     public boolean matching(Matcher<T> matcher, String message) {
/* 48 */       if (matcher.matches(this.theValue)) {
/* 49 */         return true;
/*    */       }
/* 51 */       this.mismatch.appendText(message);
/* 52 */       matcher.describeMismatch(this.theValue, this.mismatch);
/* 53 */       return false;
/*    */     }
/*    */ 
/*    */     
/*    */     public <U> Condition<U> and(Condition.Step<? super T, U> next) {
/* 58 */       return next.apply(this.theValue, this.mismatch);
/*    */     } }
/*    */   private static final class NotMatched<T> extends Condition<T> { private NotMatched() {}
/*    */     
/*    */     public boolean matching(Matcher<T> match, String message) {
/* 63 */       return false;
/*    */     }
/*    */     public <U> Condition<U> and(Condition.Step<? super T, U> mapping) {
/* 66 */       return notMatched();
/*    */     } }
/*    */ 
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\hamcrest\Condition.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */