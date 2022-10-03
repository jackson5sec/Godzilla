/*    */ package com.google.common.base;
/*    */ 
/*    */ import com.google.common.annotations.GwtCompatible;
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
/*    */ @GwtCompatible
/*    */ abstract class CommonPattern
/*    */ {
/*    */   public abstract CommonMatcher matcher(CharSequence paramCharSequence);
/*    */   
/*    */   public abstract String pattern();
/*    */   
/*    */   public abstract int flags();
/*    */   
/*    */   public abstract String toString();
/*    */   
/*    */   public static CommonPattern compile(String pattern) {
/* 37 */     return Platform.compilePattern(pattern);
/*    */   }
/*    */   
/*    */   public static boolean isPcreLike() {
/* 41 */     return Platform.patternCompilerIsPcreLike();
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\google\common\base\CommonPattern.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */