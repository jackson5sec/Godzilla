/*     */ package org.fife.ui.rsyntaxtextarea.templates;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.ObjectInputStream;
/*     */ import javax.swing.text.BadLocationException;
/*     */ import javax.swing.text.Caret;
/*     */ import javax.swing.text.Element;
/*     */ import org.fife.ui.rsyntaxtextarea.RSyntaxDocument;
/*     */ import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;
/*     */ import org.fife.ui.rsyntaxtextarea.RSyntaxUtilities;
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
/*     */ public class StaticCodeTemplate
/*     */   extends AbstractCodeTemplate
/*     */ {
/*     */   private static final long serialVersionUID = 1L;
/*     */   private String beforeCaret;
/*     */   private String afterCaret;
/*     */   private transient int firstBeforeNewline;
/*     */   private transient int firstAfterNewline;
/*     */   private static final String EMPTY_STRING = "";
/*     */   
/*     */   public StaticCodeTemplate() {}
/*     */   
/*     */   public StaticCodeTemplate(String id, String beforeCaret, String afterCaret) {
/*  88 */     super(id);
/*  89 */     setBeforeCaretText(beforeCaret);
/*  90 */     setAfterCaretText(afterCaret);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getAfterCaretText() {
/* 101 */     return this.afterCaret;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getBeforeCaretText() {
/* 112 */     return this.beforeCaret;
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
/*     */   private String getAfterTextIndented(String indent) {
/* 124 */     return getTextIndented(getAfterCaretText(), this.firstAfterNewline, indent);
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
/*     */   private String getBeforeTextIndented(String indent) {
/* 136 */     return getTextIndented(getBeforeCaretText(), this.firstBeforeNewline, indent);
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
/*     */   private String getTextIndented(String text, int firstNewline, String indent) {
/* 149 */     if (firstNewline == -1) {
/* 150 */       return text;
/*     */     }
/* 152 */     int pos = 0;
/* 153 */     int old = firstNewline + 1;
/* 154 */     StringBuilder sb = new StringBuilder(text.substring(0, old));
/* 155 */     sb.append(indent);
/* 156 */     while ((pos = text.indexOf('\n', old)) > -1) {
/* 157 */       sb.append(text.substring(old, pos + 1));
/* 158 */       sb.append(indent);
/* 159 */       old = pos + 1;
/*     */     } 
/* 161 */     if (old < text.length()) {
/* 162 */       sb.append(text.substring(old));
/*     */     }
/* 164 */     return sb.toString();
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
/*     */   public void invoke(RSyntaxTextArea textArea) throws BadLocationException {
/* 178 */     Caret c = textArea.getCaret();
/* 179 */     int dot = c.getDot();
/* 180 */     int mark = c.getMark();
/* 181 */     int p0 = Math.min(dot, mark);
/* 182 */     int p1 = Math.max(dot, mark);
/* 183 */     RSyntaxDocument doc = (RSyntaxDocument)textArea.getDocument();
/* 184 */     Element map = doc.getDefaultRootElement();
/*     */     
/* 186 */     int lineNum = map.getElementIndex(dot);
/* 187 */     Element line = map.getElement(lineNum);
/* 188 */     int start = line.getStartOffset();
/* 189 */     int end = line.getEndOffset() - 1;
/* 190 */     String s = textArea.getText(start, end - start);
/* 191 */     int len = s.length();
/*     */ 
/*     */ 
/*     */     
/* 195 */     int endWS = 0;
/* 196 */     while (endWS < len && RSyntaxUtilities.isWhitespace(s.charAt(endWS))) {
/* 197 */       endWS++;
/*     */     }
/* 199 */     s = s.substring(0, endWS);
/* 200 */     p0 -= getID().length();
/* 201 */     String beforeText = getBeforeTextIndented(s);
/* 202 */     String afterText = getAfterTextIndented(s);
/* 203 */     doc.replace(p0, p1 - p0, beforeText + afterText, null);
/* 204 */     textArea.setCaretPosition(p0 + beforeText.length());
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
/*     */   private void readObject(ObjectInputStream in) throws ClassNotFoundException, IOException {
/* 219 */     in.defaultReadObject();
/*     */ 
/*     */     
/* 222 */     setBeforeCaretText(this.beforeCaret);
/* 223 */     setAfterCaretText(this.afterCaret);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setAfterCaretText(String afterCaret) {
/* 234 */     this.afterCaret = (afterCaret == null) ? "" : afterCaret;
/* 235 */     this.firstAfterNewline = this.afterCaret.indexOf('\n');
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setBeforeCaretText(String beforeCaret) {
/* 246 */     this.beforeCaret = (beforeCaret == null) ? "" : beforeCaret;
/* 247 */     this.firstBeforeNewline = this.beforeCaret.indexOf('\n');
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
/*     */   public String toString() {
/* 259 */     return "[StaticCodeTemplate: id=" + getID() + ", text=" + 
/* 260 */       getBeforeCaretText() + "|" + getAfterCaretText() + "]";
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\fif\\ui\rsyntaxtextarea\templates\StaticCodeTemplate.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */