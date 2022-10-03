/*     */ package org.fife.rsta.ac.jsp;
/*     */ 
/*     */ import java.util.HashSet;
/*     */ import java.util.Set;
/*     */ import javax.swing.ListCellRenderer;
/*     */ import javax.swing.text.JTextComponent;
/*     */ import org.fife.rsta.ac.AbstractMarkupLanguageSupport;
/*     */ import org.fife.rsta.ac.html.HtmlCellRenderer;
/*     */ import org.fife.rsta.ac.html.HtmlCompletionProvider;
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
/*     */ public class JspLanguageSupport
/*     */   extends AbstractMarkupLanguageSupport
/*     */ {
/*     */   private JspCompletionProvider provider;
/*  47 */   private static Set<String> tagsToClose = new HashSet<>();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public JspLanguageSupport() {
/*  54 */     setAutoActivationEnabled(true);
/*  55 */     setParameterAssistanceEnabled(false);
/*  56 */     setShowDescWindow(true);
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
/*     */   private JspCompletionProvider getProvider() {
/*  71 */     if (this.provider == null) {
/*  72 */       this.provider = new JspCompletionProvider();
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
/*     */   public void install(RSyntaxTextArea textArea) {
/*  84 */     HtmlCompletionProvider provider = getProvider();
/*  85 */     AutoCompletion ac = createAutoCompletion((CompletionProvider)provider);
/*  86 */     ac.install((JTextComponent)textArea);
/*  87 */     installImpl(textArea, ac);
/*  88 */     installKeyboardShortcuts(textArea);
/*     */     
/*  90 */     textArea.setToolTipSupplier(null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected boolean shouldAutoCloseTag(String tag) {
/* 100 */     return tagsToClose.contains(tag.toLowerCase());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void uninstall(RSyntaxTextArea textArea) {
/* 109 */     uninstallImpl(textArea);
/* 110 */     uninstallKeyboardShortcuts(textArea);
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\fife\rsta\ac\jsp\JspLanguageSupport.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */