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
/*    */ public abstract class TypeSafeMatcher<T>
/*    */   extends BaseMatcher<T>
/*    */ {
/* 14 */   private static final ReflectiveTypeFinder TYPE_FINDER = new ReflectiveTypeFinder("matchesSafely", 1, 0);
/*    */ 
/*    */   
/*    */   private final Class<?> expectedType;
/*    */ 
/*    */ 
/*    */   
/*    */   protected TypeSafeMatcher() {
/* 22 */     this(TYPE_FINDER);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   protected TypeSafeMatcher(Class<?> expectedType) {
/* 31 */     this.expectedType = expectedType;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   protected TypeSafeMatcher(ReflectiveTypeFinder typeFinder) {
/* 40 */     this.expectedType = typeFinder.findExpectedType(getClass());
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   protected abstract boolean matchesSafely(T paramT);
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   protected void describeMismatchSafely(T item, Description mismatchDescription) {
/* 54 */     super.describeMismatch(item, mismatchDescription);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public final boolean matches(Object item) {
/* 65 */     return (item != null && this.expectedType.isInstance(item) && matchesSafely((T)item));
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public final void describeMismatch(Object item, Description description) {
/* 73 */     if (item == null) {
/* 74 */       super.describeMismatch(item, description);
/* 75 */     } else if (!this.expectedType.isInstance(item)) {
/* 76 */       description.appendText("was a ").appendText(item.getClass().getName()).appendText(" (").appendValue(item).appendText(")");
/*    */     
/*    */     }
/*    */     else {
/*    */ 
/*    */       
/* 82 */       describeMismatchSafely((T)item, description);
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\hamcrest\TypeSafeMatcher.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */