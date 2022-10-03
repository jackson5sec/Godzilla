/*    */ package org.fife.rsta.ac.css;
/*    */ 
/*    */ import org.fife.ui.autocomplete.BasicCompletion;
/*    */ import org.fife.ui.autocomplete.Completion;
/*    */ import org.fife.ui.autocomplete.CompletionProvider;
/*    */ import org.fife.ui.autocomplete.DefaultCompletionProvider;
/*    */ import org.fife.ui.autocomplete.LanguageAwareCompletionProvider;
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
/*    */ public class CssCompletionProvider
/*    */   extends LanguageAwareCompletionProvider
/*    */ {
/*    */   public CssCompletionProvider() {
/* 32 */     setDefaultCompletionProvider(createCodeCompletionProvider());
/* 33 */     setCommentCompletionProvider(createCommentCompletionProvider());
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   protected CompletionProvider createCodeCompletionProvider() {
/* 44 */     return (CompletionProvider)new PropertyValueCompletionProvider(false);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   protected CompletionProvider createCommentCompletionProvider() {
/* 55 */     DefaultCompletionProvider cp = new DefaultCompletionProvider();
/* 56 */     cp.addCompletion((Completion)new BasicCompletion((CompletionProvider)cp, "TODO:", "A to-do reminder"));
/* 57 */     cp.addCompletion((Completion)new BasicCompletion((CompletionProvider)cp, "FIXME:", "A bug that needs to be fixed"));
/* 58 */     return (CompletionProvider)cp;
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\fife\rsta\ac\css\CssCompletionProvider.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */