/*    */ package org.fife.rsta.ac.ts;
/*    */ 
/*    */ import javax.swing.text.JTextComponent;
/*    */ import org.fife.rsta.ac.AbstractLanguageSupport;
/*    */ import org.fife.ui.autocomplete.AutoCompletion;
/*    */ import org.fife.ui.autocomplete.CompletionProvider;
/*    */ import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;
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
/*    */ public class TypeScriptLanguageSupport
/*    */   extends AbstractLanguageSupport
/*    */ {
/* 25 */   private TypeScriptCompletionProvider provider = new TypeScriptCompletionProvider(this);
/*    */ 
/*    */ 
/*    */   
/*    */   private AutoCompletion createAutoCompletion() {
/* 30 */     AutoCompletion ac = new AutoCompletion((CompletionProvider)this.provider);
/* 31 */     return ac;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void install(RSyntaxTextArea textArea) {
/* 38 */     AutoCompletion ac = createAutoCompletion();
/* 39 */     ac.install((JTextComponent)textArea);
/* 40 */     installImpl(textArea, ac);
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void uninstall(RSyntaxTextArea textArea) {
/* 61 */     uninstallImpl(textArea);
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\fife\rsta\ac\ts\TypeScriptLanguageSupport.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */