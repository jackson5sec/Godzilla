/*    */ package org.fife.rsta.ac.groovy;
/*    */ 
/*    */ import javax.swing.text.JTextComponent;
/*    */ import org.fife.rsta.ac.AbstractLanguageSupport;
/*    */ import org.fife.ui.autocomplete.AutoCompletion;
/*    */ import org.fife.ui.autocomplete.CompletionProvider;
/*    */ import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;
/*    */ import org.fife.ui.rtextarea.ToolTipSupplier;
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
/*    */ public class GroovyLanguageSupport
/*    */   extends AbstractLanguageSupport
/*    */ {
/*    */   private GroovyCompletionProvider provider;
/*    */   
/*    */   public GroovyLanguageSupport() {
/* 38 */     setParameterAssistanceEnabled(true);
/* 39 */     setShowDescWindow(true);
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
/*    */   private GroovyCompletionProvider getProvider() {
/* 52 */     if (this.provider == null) {
/* 53 */       this.provider = new GroovyCompletionProvider();
/*    */     }
/* 55 */     return this.provider;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void install(RSyntaxTextArea textArea) {
/* 65 */     GroovyCompletionProvider provider = getProvider();
/* 66 */     AutoCompletion ac = createAutoCompletion((CompletionProvider)provider);
/* 67 */     ac.install((JTextComponent)textArea);
/* 68 */     installImpl(textArea, ac);
/*    */     
/* 70 */     textArea.setToolTipSupplier((ToolTipSupplier)provider);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void uninstall(RSyntaxTextArea textArea) {
/* 80 */     uninstallImpl(textArea);
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\fife\rsta\ac\groovy\GroovyLanguageSupport.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */