/*    */ package org.springframework.core.style;
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
/*    */ public abstract class StylerUtils
/*    */ {
/* 38 */   static final ValueStyler DEFAULT_VALUE_STYLER = new DefaultValueStyler();
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static String style(Object value) {
/* 47 */     return DEFAULT_VALUE_STYLER.style(value);
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\springframework\core\style\StylerUtils.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */