/*     */ package org.fife.rsta.ac.xml;
/*     */ 
/*     */ import javax.swing.Action;
/*     */ import javax.swing.ActionMap;
/*     */ import javax.swing.InputMap;
/*     */ import javax.swing.KeyStroke;
/*     */ import javax.swing.ListCellRenderer;
/*     */ import javax.swing.text.JTextComponent;
/*     */ import org.fife.rsta.ac.AbstractMarkupLanguageSupport;
/*     */ import org.fife.rsta.ac.GoToMemberAction;
/*     */ import org.fife.rsta.ac.html.HtmlCellRenderer;
/*     */ import org.fife.rsta.ac.xml.tree.XmlOutlineTree;
/*     */ import org.fife.ui.autocomplete.AutoCompletion;
/*     */ import org.fife.ui.autocomplete.CompletionProvider;
/*     */ import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;
/*     */ import org.fife.ui.rsyntaxtextarea.parser.Parser;
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
/*     */ public class XmlLanguageSupport
/*     */   extends AbstractMarkupLanguageSupport
/*     */ {
/*     */   private XmlCompletionProvider provider;
/*     */   private boolean showSyntaxErrors;
/*     */   
/*     */   public XmlLanguageSupport() {
/*  69 */     setAutoActivationEnabled(true);
/*  70 */     setParameterAssistanceEnabled(false);
/*  71 */     setShowDescWindow(false);
/*  72 */     setShowSyntaxErrors(true);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected ListCellRenderer<Object> createDefaultCompletionCellRenderer() {
/*  81 */     return (ListCellRenderer<Object>)new HtmlCellRenderer();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private XmlCompletionProvider getProvider() {
/*  91 */     if (this.provider == null) {
/*  92 */       this.provider = new XmlCompletionProvider();
/*     */     }
/*  94 */     return this.provider;
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
/*     */   public XmlParser getParser(RSyntaxTextArea textArea) {
/* 108 */     Object parser = textArea.getClientProperty("org.fife.rsta.ac.LanguageSupport.LanguageParser");
/* 109 */     if (parser instanceof XmlParser) {
/* 110 */       return (XmlParser)parser;
/*     */     }
/* 112 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean getShowSyntaxErrors() {
/* 123 */     return this.showSyntaxErrors;
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
/*     */   public void install(RSyntaxTextArea textArea) {
/* 135 */     XmlCompletionProvider provider = getProvider();
/* 136 */     AutoCompletion ac = createAutoCompletion((CompletionProvider)provider);
/* 137 */     ac.install((JTextComponent)textArea);
/* 138 */     installImpl(textArea, ac);
/*     */     
/* 140 */     XmlParser parser = new XmlParser(this);
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
/* 155 */     textArea.addParser((Parser)parser);
/* 156 */     textArea.putClientProperty("org.fife.rsta.ac.LanguageSupport.LanguageParser", parser);
/*     */     
/* 158 */     installKeyboardShortcuts(textArea);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void installKeyboardShortcuts(RSyntaxTextArea textArea) {
/* 169 */     super.installKeyboardShortcuts(textArea);
/*     */     
/* 171 */     InputMap im = textArea.getInputMap();
/* 172 */     ActionMap am = textArea.getActionMap();
/* 173 */     int c = textArea.getToolkit().getMenuShortcutKeyMask();
/* 174 */     int shift = 64;
/*     */     
/* 176 */     im.put(KeyStroke.getKeyStroke(79, c | shift), "GoToType");
/* 177 */     am.put("GoToType", (Action)new GoToMemberAction(XmlOutlineTree.class));
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
/*     */   public void setShowSyntaxErrors(boolean show) {
/* 189 */     this.showSyntaxErrors = show;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected boolean shouldAutoCloseTag(String tag) {
/* 198 */     return true;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void uninstall(RSyntaxTextArea textArea) {
/* 208 */     uninstallImpl(textArea);
/*     */     
/* 210 */     XmlParser parser = getParser(textArea);
/* 211 */     if (parser != null) {
/* 212 */       textArea.removeParser((Parser)parser);
/*     */     }
/*     */     
/* 215 */     uninstallKeyboardShortcuts(textArea);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void uninstallKeyboardShortcuts(RSyntaxTextArea textArea) {
/* 226 */     super.uninstallKeyboardShortcuts(textArea);
/*     */     
/* 228 */     InputMap im = textArea.getInputMap();
/* 229 */     ActionMap am = textArea.getActionMap();
/* 230 */     int c = textArea.getToolkit().getMenuShortcutKeyMask();
/* 231 */     int shift = 64;
/*     */     
/* 233 */     im.remove(KeyStroke.getKeyStroke(79, c | shift));
/* 234 */     am.remove("GoToType");
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\fife\rsta\ac\xml\XmlLanguageSupport.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */