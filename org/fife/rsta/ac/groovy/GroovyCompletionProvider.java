/*    */ package org.fife.rsta.ac.groovy;
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
/*    */ 
/*    */ public class GroovyCompletionProvider
/*    */   extends LanguageAwareCompletionProvider
/*    */ {
/*    */   public GroovyCompletionProvider() {
/* 33 */     setDefaultCompletionProvider(createCodeCompletionProvider());
/* 34 */     setStringCompletionProvider(createStringCompletionProvider());
/* 35 */     setCommentCompletionProvider(createCommentCompletionProvider());
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   protected void addShorthandCompletions(DefaultCompletionProvider codeCP) {}
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   protected CompletionProvider createCodeCompletionProvider() {
/* 57 */     return (CompletionProvider)new GroovySourceCompletionProvider();
/*    */   }
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
/*    */   protected CompletionProvider createCommentCompletionProvider() {
/* 70 */     DefaultCompletionProvider cp = new DefaultCompletionProvider();
/* 71 */     cp.addCompletion((Completion)new BasicCompletion((CompletionProvider)cp, "TODO:", "A to-do reminder"));
/* 72 */     cp.addCompletion((Completion)new BasicCompletion((CompletionProvider)cp, "FIXME:", "A bug that needs to be fixed"));
/* 73 */     return (CompletionProvider)cp;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   protected CompletionProvider createStringCompletionProvider() {
/* 85 */     DefaultCompletionProvider cp = new DefaultCompletionProvider();
/* 86 */     cp.addCompletion((Completion)new BasicCompletion((CompletionProvider)cp, "%c", "char", "Prints a character"));
/* 87 */     cp.addCompletion((Completion)new BasicCompletion((CompletionProvider)cp, "%i", "signed int", "Prints a signed integer"));
/* 88 */     cp.addCompletion((Completion)new BasicCompletion((CompletionProvider)cp, "%f", "float", "Prints a float"));
/* 89 */     cp.addCompletion((Completion)new BasicCompletion((CompletionProvider)cp, "%s", "string", "Prints a string"));
/* 90 */     cp.addCompletion((Completion)new BasicCompletion((CompletionProvider)cp, "%u", "unsigned int", "Prints an unsigned integer"));
/* 91 */     cp.addCompletion((Completion)new BasicCompletion((CompletionProvider)cp, "\\n", "Newline", "Prints a newline"));
/* 92 */     return (CompletionProvider)cp;
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\fife\rsta\ac\groovy\GroovyCompletionProvider.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */