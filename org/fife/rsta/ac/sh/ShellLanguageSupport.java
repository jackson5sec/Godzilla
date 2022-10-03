/*     */ package org.fife.rsta.ac.sh;
/*     */ 
/*     */ import java.io.File;
/*     */ import javax.swing.ListCellRenderer;
/*     */ import javax.swing.text.JTextComponent;
/*     */ import org.fife.rsta.ac.AbstractLanguageSupport;
/*     */ import org.fife.ui.autocomplete.AutoCompletion;
/*     */ import org.fife.ui.autocomplete.CompletionCellRenderer;
/*     */ import org.fife.ui.autocomplete.CompletionProvider;
/*     */ import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;
/*     */ import org.fife.ui.rtextarea.ToolTipSupplier;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ShellLanguageSupport
/*     */   extends AbstractLanguageSupport
/*     */ {
/*     */   private ShellCompletionProvider provider;
/*     */   private boolean useLocalManPages;
/*     */   
/*     */   public ShellLanguageSupport() {
/*  45 */     setParameterAssistanceEnabled(false);
/*  46 */     setShowDescWindow(true);
/*  47 */     setShowDescWindow(true);
/*  48 */     setAutoCompleteEnabled(true);
/*  49 */     setAutoActivationEnabled(true);
/*  50 */     setAutoActivationDelay(800);
/*  51 */     this.useLocalManPages = (File.separatorChar == '/');
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected ListCellRenderer<Object> createDefaultCompletionCellRenderer() {
/*  60 */     return (ListCellRenderer<Object>)new CompletionCellRenderer();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private ShellCompletionProvider getProvider() {
/*  70 */     if (this.provider == null) {
/*  71 */       this.provider = new ShellCompletionProvider();
/*  72 */       ShellCompletionProvider.setUseLocalManPages(getUseLocalManPages());
/*     */     } 
/*  74 */     return this.provider;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean getUseLocalManPages() {
/*  88 */     return this.useLocalManPages;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void install(RSyntaxTextArea textArea) {
/*  98 */     ShellCompletionProvider provider = getProvider();
/*  99 */     AutoCompletion ac = createAutoCompletion((CompletionProvider)provider);
/* 100 */     ac.install((JTextComponent)textArea);
/* 101 */     installImpl(textArea, ac);
/*     */     
/* 103 */     textArea.setToolTipSupplier((ToolTipSupplier)provider);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setUseLocalManPages(boolean use) {
/* 118 */     if (use != this.useLocalManPages) {
/* 119 */       this.useLocalManPages = use;
/* 120 */       if (this.provider != null) {
/* 121 */         ShellCompletionProvider.setUseLocalManPages(this.useLocalManPages);
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void uninstall(RSyntaxTextArea textArea) {
/* 133 */     uninstallImpl(textArea);
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\fife\rsta\ac\sh\ShellLanguageSupport.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */