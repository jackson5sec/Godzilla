/*    */ package org.apache.log4j.pattern;
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
/*    */ public final class FileDatePatternConverter
/*    */ {
/*    */   public static PatternConverter newInstance(String[] options) {
/* 41 */     if (options == null || options.length == 0) {
/* 42 */       return DatePatternConverter.newInstance(new String[] { "yyyy-MM-dd" });
/*    */     }
/*    */ 
/*    */ 
/*    */ 
/*    */     
/* 48 */     return DatePatternConverter.newInstance(options);
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\apache\log4j\pattern\FileDatePatternConverter.class
 * Java compiler version: 4 (48.0)
 * JD-Core Version:       1.1.3
 */