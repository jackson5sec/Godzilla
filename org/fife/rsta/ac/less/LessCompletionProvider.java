/*    */ package org.fife.rsta.ac.less;
/*    */ 
/*    */ import org.fife.rsta.ac.css.CssCompletionProvider;
/*    */ import org.fife.ui.autocomplete.CompletionProvider;
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
/*    */ class LessCompletionProvider
/*    */   extends CssCompletionProvider
/*    */ {
/*    */   protected CompletionProvider createCodeCompletionProvider() {
/* 33 */     return (CompletionProvider)new LessCodeCompletionProvider();
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\fife\rsta\ac\less\LessCompletionProvider.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */