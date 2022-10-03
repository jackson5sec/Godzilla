/*    */ package org.fife.rsta.ac.css;
/*    */ 
/*    */ import javax.swing.ListCellRenderer;
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
/*    */ public class CssLanguageSupport
/*    */   extends AbstractLanguageSupport
/*    */ {
/*    */   private CssCompletionProvider provider;
/*    */   
/*    */   public CssLanguageSupport() {
/* 38 */     setAutoActivationEnabled(true);
/* 39 */     setAutoActivationDelay(500);
/* 40 */     setParameterAssistanceEnabled(true);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   protected ListCellRenderer<Object> createDefaultCompletionCellRenderer() {
/* 47 */     return (ListCellRenderer<Object>)new CssCellRenderer();
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
/* 58 */     return new CssCompletionProvider();
/*    */   }
/*    */ 
/*    */   
/*    */   private CssCompletionProvider getProvider() {
/* 63 */     if (this.provider == null) {
/* 64 */       this.provider = createProvider();
/*    */     }
/* 66 */     return this.provider;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void install(RSyntaxTextArea textArea) {
/* 76 */     CssCompletionProvider provider = getProvider();
/* 77 */     AutoCompletion ac = createAutoCompletion((CompletionProvider)provider);
/* 78 */     ac.install((JTextComponent)textArea);
/* 79 */     installImpl(textArea, ac);
/*    */     
/* 81 */     textArea.setToolTipSupplier((ToolTipSupplier)provider);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void uninstall(RSyntaxTextArea textArea) {
/* 91 */     uninstallImpl(textArea);
/* 92 */     textArea.setToolTipSupplier(null);
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\fife\rsta\ac\css\CssLanguageSupport.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */