/*    */ package org.hamcrest;
/*    */ 
/*    */ import org.hamcrest.internal.ReflectiveTypeFinder;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public abstract class FeatureMatcher<T, U>
/*    */   extends TypeSafeDiagnosingMatcher<T>
/*    */ {
/* 13 */   private static final ReflectiveTypeFinder TYPE_FINDER = new ReflectiveTypeFinder("featureValueOf", 1, 0);
/*    */ 
/*    */   
/*    */   private final Matcher<? super U> subMatcher;
/*    */ 
/*    */   
/*    */   private final String featureDescription;
/*    */   
/*    */   private final String featureName;
/*    */ 
/*    */   
/*    */   public FeatureMatcher(Matcher<? super U> subMatcher, String featureDescription, String featureName) {
/* 25 */     super(TYPE_FINDER);
/* 26 */     this.subMatcher = subMatcher;
/* 27 */     this.featureDescription = featureDescription;
/* 28 */     this.featureName = featureName;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   protected abstract U featureValueOf(T paramT);
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   protected boolean matchesSafely(T actual, Description mismatch) {
/* 40 */     U featureValue = featureValueOf(actual);
/* 41 */     if (!this.subMatcher.matches(featureValue)) {
/* 42 */       mismatch.appendText(this.featureName).appendText(" ");
/* 43 */       this.subMatcher.describeMismatch(featureValue, mismatch);
/* 44 */       return false;
/*    */     } 
/* 46 */     return true;
/*    */   }
/*    */ 
/*    */   
/*    */   public final void describeTo(Description description) {
/* 51 */     description.appendText(this.featureDescription).appendText(" ").appendDescriptionOf(this.subMatcher);
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\hamcrest\FeatureMatcher.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */