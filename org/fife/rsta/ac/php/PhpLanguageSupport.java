/*     */ package org.fife.rsta.ac.php;
/*     */ 
/*     */ import java.util.HashSet;
/*     */ import java.util.Set;
/*     */ import javax.swing.ListCellRenderer;
/*     */ import javax.swing.text.JTextComponent;
/*     */ import org.fife.rsta.ac.AbstractMarkupLanguageSupport;
/*     */ import org.fife.rsta.ac.html.HtmlCellRenderer;
/*     */ import org.fife.rsta.ac.html.HtmlLanguageSupport;
/*     */ import org.fife.ui.autocomplete.AutoCompletion;
/*     */ import org.fife.ui.autocomplete.CompletionProvider;
/*     */ import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ public class PhpLanguageSupport
/*     */   extends AbstractMarkupLanguageSupport
/*     */ {
/*     */   private PhpCompletionProvider provider;
/*  46 */   private static Set<String> tagsToClose = new HashSet<>();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public PhpLanguageSupport() {
/*  53 */     setAutoActivationEnabled(true);
/*  54 */     setParameterAssistanceEnabled(true);
/*  55 */     setShowDescWindow(true);
/*  56 */     setAutoActivationDelay(800);
/*  57 */     tagsToClose = HtmlLanguageSupport.getTagsToClose();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected ListCellRenderer<Object> createDefaultCompletionCellRenderer() {
/*  66 */     return (ListCellRenderer<Object>)new HtmlCellRenderer();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private PhpCompletionProvider getProvider() {
/*  76 */     if (this.provider == null) {
/*  77 */       this.provider = new PhpCompletionProvider();
/*     */     }
/*  79 */     return this.provider;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void install(RSyntaxTextArea textArea) {
/*  89 */     PhpCompletionProvider provider = getProvider();
/*  90 */     AutoCompletion ac = createAutoCompletion((CompletionProvider)provider);
/*  91 */     ac.install((JTextComponent)textArea);
/*  92 */     installImpl(textArea, ac);
/*  93 */     installKeyboardShortcuts(textArea);
/*     */     
/*  95 */     textArea.setToolTipSupplier(null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected boolean shouldAutoCloseTag(String tag) {
/* 105 */     return tagsToClose.contains(tag.toLowerCase());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void uninstall(RSyntaxTextArea textArea) {
/* 114 */     uninstallImpl(textArea);
/* 115 */     uninstallKeyboardShortcuts(textArea);
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\fife\rsta\ac\php\PhpLanguageSupport.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */