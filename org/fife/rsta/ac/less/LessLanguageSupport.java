/*    */ package org.fife.rsta.ac.less;
/*    */ 
/*    */ import org.fife.rsta.ac.css.CssCompletionProvider;
/*    */ import org.fife.rsta.ac.css.CssLanguageSupport;
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
/*    */ public class LessLanguageSupport
/*    */   extends CssLanguageSupport
/*    */ {
/*    */   public LessLanguageSupport() {
/* 30 */     setShowDescWindow(true);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   protected CssCompletionProvider createProvider() {
/* 41 */     return new LessCompletionProvider();
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\fife\rsta\ac\less\LessLanguageSupport.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */