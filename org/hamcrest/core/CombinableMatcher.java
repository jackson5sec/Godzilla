/*    */ package org.hamcrest.core;
/*    */ import java.util.ArrayList;
/*    */ import org.hamcrest.Description;
/*    */ import org.hamcrest.Factory;
/*    */ import org.hamcrest.Matcher;
/*    */ import org.hamcrest.SelfDescribing;
/*    */ import org.hamcrest.TypeSafeDiagnosingMatcher;
/*    */ 
/*    */ public class CombinableMatcher<T> extends TypeSafeDiagnosingMatcher<T> {
/*    */   public CombinableMatcher(Matcher<? super T> matcher) {
/* 11 */     this.matcher = matcher;
/*    */   }
/*    */   private final Matcher<? super T> matcher;
/*    */   
/*    */   protected boolean matchesSafely(T item, Description mismatch) {
/* 16 */     if (!this.matcher.matches(item)) {
/* 17 */       this.matcher.describeMismatch(item, mismatch);
/* 18 */       return false;
/*    */     } 
/* 20 */     return true;
/*    */   }
/*    */ 
/*    */   
/*    */   public void describeTo(Description description) {
/* 25 */     description.appendDescriptionOf((SelfDescribing)this.matcher);
/*    */   }
/*    */   
/*    */   public CombinableMatcher<T> and(Matcher<? super T> other) {
/* 29 */     return new CombinableMatcher((Matcher<? super T>)new AllOf<T>(templatedListWith(other)));
/*    */   }
/*    */   
/*    */   public CombinableMatcher<T> or(Matcher<? super T> other) {
/* 33 */     return new CombinableMatcher((Matcher<? super T>)new AnyOf<T>(templatedListWith(other)));
/*    */   }
/*    */   
/*    */   private ArrayList<Matcher<? super T>> templatedListWith(Matcher<? super T> other) {
/* 37 */     ArrayList<Matcher<? super T>> matchers = new ArrayList<Matcher<? super T>>();
/* 38 */     matchers.add(this.matcher);
/* 39 */     matchers.add(other);
/* 40 */     return matchers;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   @Factory
/*    */   public static <LHS> CombinableBothMatcher<LHS> both(Matcher<? super LHS> matcher) {
/* 51 */     return new CombinableBothMatcher<LHS>(matcher);
/*    */   }
/*    */   
/*    */   public static final class CombinableBothMatcher<X> { private final Matcher<? super X> first;
/*    */     
/*    */     public CombinableBothMatcher(Matcher<? super X> matcher) {
/* 57 */       this.first = matcher;
/*    */     }
/*    */     public CombinableMatcher<X> and(Matcher<? super X> other) {
/* 60 */       return (new CombinableMatcher<X>(this.first)).and(other);
/*    */     } }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   @Factory
/*    */   public static <LHS> CombinableEitherMatcher<LHS> either(Matcher<? super LHS> matcher) {
/* 72 */     return new CombinableEitherMatcher<LHS>(matcher);
/*    */   }
/*    */   
/*    */   public static final class CombinableEitherMatcher<X> { private final Matcher<? super X> first;
/*    */     
/*    */     public CombinableEitherMatcher(Matcher<? super X> matcher) {
/* 78 */       this.first = matcher;
/*    */     }
/*    */     public CombinableMatcher<X> or(Matcher<? super X> other) {
/* 81 */       return (new CombinableMatcher<X>(this.first)).or(other);
/*    */     } }
/*    */ 
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\hamcrest\core\CombinableMatcher.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */