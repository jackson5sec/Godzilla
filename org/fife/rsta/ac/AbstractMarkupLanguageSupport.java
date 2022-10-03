/*     */ package org.fife.rsta.ac;
/*     */ 
/*     */ import java.awt.event.ActionEvent;
/*     */ import javax.swing.ActionMap;
/*     */ import javax.swing.InputMap;
/*     */ import javax.swing.KeyStroke;
/*     */ import javax.swing.text.Caret;
/*     */ import javax.swing.text.Element;
/*     */ import javax.swing.text.TextAction;
/*     */ import org.fife.ui.rsyntaxtextarea.RSyntaxDocument;
/*     */ import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;
/*     */ import org.fife.ui.rsyntaxtextarea.RSyntaxUtilities;
/*     */ import org.fife.ui.rsyntaxtextarea.Token;
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
/*     */ public abstract class AbstractMarkupLanguageSupport
/*     */   extends AbstractLanguageSupport
/*     */ {
/*     */   protected static final String INSERT_CLOSING_TAG_ACTION = "HtmlLanguageSupport.InsertClosingTag";
/*     */   private boolean autoAddClosingTags;
/*     */   
/*     */   protected AbstractMarkupLanguageSupport() {
/*  52 */     setAutoAddClosingTags(true);
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
/*     */   public boolean getAutoAddClosingTags() {
/*  65 */     return this.autoAddClosingTags;
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
/*     */ 
/*     */ 
/*     */   
/*     */   protected void installKeyboardShortcuts(RSyntaxTextArea textArea) {
/*  83 */     InputMap im = textArea.getInputMap();
/*  84 */     ActionMap am = textArea.getActionMap();
/*     */     
/*  86 */     im.put(KeyStroke.getKeyStroke('>'), "HtmlLanguageSupport.InsertClosingTag");
/*  87 */     am.put("HtmlLanguageSupport.InsertClosingTag", new InsertClosingTagAction());
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
/*     */   protected abstract boolean shouldAutoCloseTag(String paramString);
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
/*     */   public void setAutoAddClosingTags(boolean autoAdd) {
/* 111 */     this.autoAddClosingTags = autoAdd;
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
/*     */   protected void uninstallKeyboardShortcuts(RSyntaxTextArea textArea) {
/* 126 */     InputMap im = textArea.getInputMap();
/* 127 */     ActionMap am = textArea.getActionMap();
/*     */     
/* 129 */     im.remove(KeyStroke.getKeyStroke('>'));
/* 130 */     am.remove("HtmlLanguageSupport.InsertClosingTag");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private class InsertClosingTagAction
/*     */     extends TextAction
/*     */   {
/*     */     InsertClosingTagAction() {
/* 143 */       super("HtmlLanguageSupport.InsertClosingTag");
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public void actionPerformed(ActionEvent e) {
/* 149 */       RSyntaxTextArea textArea = (RSyntaxTextArea)getTextComponent(e);
/* 150 */       RSyntaxDocument doc = (RSyntaxDocument)textArea.getDocument();
/* 151 */       Caret c = textArea.getCaret();
/*     */       
/* 153 */       int dot = c.getDot();
/* 154 */       boolean selection = (dot != c.getMark());
/* 155 */       textArea.replaceSelection(">");
/*     */ 
/*     */       
/* 158 */       if (!selection && AbstractMarkupLanguageSupport.this.getAutoAddClosingTags()) {
/*     */         
/* 160 */         Token t = doc.getTokenListForLine(textArea.getCaretLineNumber());
/* 161 */         t = RSyntaxUtilities.getTokenAtOffset(t, dot);
/* 162 */         if (t != null && t.isSingleChar(25, '>')) {
/* 163 */           String tagName = discoverTagName(doc, dot);
/* 164 */           if (tagName != null) {
/* 165 */             textArea.replaceSelection("</" + tagName + ">");
/* 166 */             textArea.setCaretPosition(dot + 1);
/*     */           } 
/*     */         } 
/*     */       } 
/*     */     }
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
/*     */     private String discoverTagName(RSyntaxDocument doc, int dot) {
/* 186 */       String candidate = null;
/*     */       
/* 188 */       Element root = doc.getDefaultRootElement();
/* 189 */       int curLine = root.getElementIndex(dot);
/*     */ 
/*     */ 
/*     */       
/* 193 */       Token t = doc.getTokenListForLine(curLine);
/* 194 */       while (t != null && t.isPaintable()) {
/* 195 */         if (t.getType() == 25) {
/* 196 */           if (t.isSingleChar('<')) {
/* 197 */             t = t.getNextToken();
/* 198 */             if (t != null && t.isPaintable()) {
/* 199 */               candidate = t.getLexeme();
/*     */             }
/*     */           }
/* 202 */           else if (t.isSingleChar('>')) {
/* 203 */             if (t.getOffset() == dot) {
/* 204 */               if (candidate == null || AbstractMarkupLanguageSupport.this
/* 205 */                 .shouldAutoCloseTag(candidate)) {
/* 206 */                 return candidate;
/*     */               }
/* 208 */               return null;
/*     */             }
/*     */           
/* 211 */           } else if (t.is(25, "</")) {
/* 212 */             candidate = null;
/*     */           } 
/*     */         }
/*     */         
/* 216 */         t = t.getNextToken();
/*     */       } 
/*     */ 
/*     */       
/* 220 */       return null;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\fife\rsta\ac\AbstractMarkupLanguageSupport.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */