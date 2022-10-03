/*    */ package org.fife.rsta.ac.c;
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
/*    */ public class CLanguageSupport
/*    */   extends AbstractLanguageSupport
/*    */ {
/*    */   private CCompletionProvider provider;
/*    */   
/*    */   public CLanguageSupport() {
/* 38 */     setShowDescWindow(true);
/* 39 */     setAutoCompleteEnabled(true);
/* 40 */     setAutoActivationEnabled(true);
/* 41 */     setAutoActivationDelay(800);
/* 42 */     setParameterAssistanceEnabled(true);
/* 43 */     setShowDescWindow(true);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   protected ListCellRenderer<Object> createDefaultCompletionCellRenderer() {
/* 52 */     return (ListCellRenderer<Object>)new CCellRenderer();
/*    */   }
/*    */ 
/*    */   
/*    */   private CCompletionProvider getProvider() {
/* 57 */     if (this.provider == null) {
/* 58 */       this.provider = new CCompletionProvider();
/*    */     }
/* 60 */     return this.provider;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void install(RSyntaxTextArea textArea) {
/* 70 */     CCompletionProvider provider = getProvider();
/* 71 */     AutoCompletion ac = createAutoCompletion((CompletionProvider)provider);
/* 72 */     ac.install((JTextComponent)textArea);
/* 73 */     installImpl(textArea, ac);
/*    */     
/* 75 */     textArea.setToolTipSupplier((ToolTipSupplier)provider);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void uninstall(RSyntaxTextArea textArea) {
/* 85 */     uninstallImpl(textArea);
/* 86 */     textArea.setToolTipSupplier(null);
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\fife\rsta\ac\c\CLanguageSupport.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */