/*     */ package org.fife.rsta.ac.html;
/*     */ 
/*     */ import java.io.BufferedReader;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.InputStreamReader;
/*     */ import java.util.HashSet;
/*     */ import java.util.Set;
/*     */ import javax.swing.ListCellRenderer;
/*     */ import javax.swing.text.JTextComponent;
/*     */ import org.fife.rsta.ac.AbstractMarkupLanguageSupport;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class HtmlLanguageSupport
/*     */   extends AbstractMarkupLanguageSupport
/*     */ {
/*     */   private HtmlCompletionProvider provider;
/*     */   
/*     */   public HtmlLanguageSupport() {
/*  54 */     setAutoActivationEnabled(true);
/*  55 */     setParameterAssistanceEnabled(false);
/*  56 */     setShowDescWindow(true);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected ListCellRenderer<Object> createDefaultCompletionCellRenderer() {
/*  65 */     return (ListCellRenderer<Object>)new HtmlCellRenderer();
/*     */   }
/*     */ 
/*     */   
/*     */   private HtmlCompletionProvider getProvider() {
/*  70 */     if (this.provider == null) {
/*  71 */       this.provider = new HtmlCompletionProvider();
/*     */     }
/*  73 */     return this.provider;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Set<String> getTagsToClose() {
/*  84 */     return tagsToClose;
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
/*     */   private static Set<String> getTagsToClose(String res) {
/*  96 */     Set<String> tags = new HashSet<>();
/*  97 */     InputStream in = HtmlLanguageSupport.class.getResourceAsStream(res);
/*  98 */     if (in != null) {
/*     */       
/*     */       try {
/* 101 */         BufferedReader r = new BufferedReader(new InputStreamReader(in)); String line;
/* 102 */         while ((line = r.readLine()) != null) {
/* 103 */           if (line.length() > 0 && line.charAt(0) != '#') {
/* 104 */             tags.add(line.trim());
/*     */           }
/*     */         } 
/* 107 */         r.close();
/* 108 */       } catch (IOException ioe) {
/* 109 */         ioe.printStackTrace();
/*     */       } 
/*     */     }
/* 112 */     return tags;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void install(RSyntaxTextArea textArea) {
/* 122 */     HtmlCompletionProvider provider = getProvider();
/* 123 */     AutoCompletion ac = createAutoCompletion((CompletionProvider)provider);
/* 124 */     ac.install((JTextComponent)textArea);
/* 125 */     installImpl(textArea, ac);
/* 126 */     installKeyboardShortcuts(textArea);
/*     */     
/* 128 */     textArea.setToolTipSupplier(null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected boolean shouldAutoCloseTag(String tag) {
/* 138 */     return tagsToClose.contains(tag.toLowerCase());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void uninstall(RSyntaxTextArea textArea) {
/* 147 */     uninstallImpl(textArea);
/* 148 */     uninstallKeyboardShortcuts(textArea);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 153 */   private static Set<String> tagsToClose = getTagsToClose("html5_close_tags.txt");
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\fife\rsta\ac\html\HtmlLanguageSupport.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */