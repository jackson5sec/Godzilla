/*    */ package org.hamcrest;
/*    */ 
/*    */ import org.hamcrest.internal.ReflectiveTypeFinder;
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
/*    */ public abstract class TypeSafeDiagnosingMatcher<T>
/*    */   extends BaseMatcher<T>
/*    */ {
/* 18 */   private static final ReflectiveTypeFinder TYPE_FINDER = new ReflectiveTypeFinder("matchesSafely", 2, 0);
/*    */ 
/*    */ 
/*    */   
/*    */   private final Class<?> expectedType;
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   protected abstract boolean matchesSafely(T paramT, Description paramDescription);
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   protected TypeSafeDiagnosingMatcher(Class<?> expectedType) {
/* 33 */     this.expectedType = expectedType;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   protected TypeSafeDiagnosingMatcher(ReflectiveTypeFinder typeFinder) {
/* 42 */     this.expectedType = typeFinder.findExpectedType(getClass());
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   protected TypeSafeDiagnosingMatcher() {
/* 49 */     this(TYPE_FINDER);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public final boolean matches(Object item) {
/* 55 */     return (item != null && this.expectedType.isInstance(item) && matchesSafely((T)item, new Description.NullDescription()));
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public final void describeMismatch(Object item, Description mismatchDescription) {
/* 63 */     if (item == null || !this.expectedType.isInstance(item)) {
/* 64 */       super.describeMismatch(item, mismatchDescription);
/*    */     } else {
/* 66 */       matchesSafely((T)item, mismatchDescription);
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\hamcrest\TypeSafeDiagnosingMatcher.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */