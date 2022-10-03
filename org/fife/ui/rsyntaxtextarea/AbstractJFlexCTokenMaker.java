/*     */ package org.fife.ui.rsyntaxtextarea;
/*     */ 
/*     */ import java.awt.Component;
/*     */ import java.awt.event.ActionEvent;
/*     */ import java.util.regex.Matcher;
/*     */ import java.util.regex.Pattern;
/*     */ import javax.swing.Action;
/*     */ import javax.swing.UIManager;
/*     */ import javax.swing.text.BadLocationException;
/*     */ import org.fife.ui.rtextarea.RTextArea;
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
/*     */ public abstract class AbstractJFlexCTokenMaker
/*     */   extends AbstractJFlexTokenMaker
/*     */ {
/*  39 */   private static final Pattern MLC_PATTERN = Pattern.compile("([ \\t]*)(/?[\\*]+)([ \\t]*)");
/*     */ 
/*     */ 
/*     */   
/*  43 */   private final Action INSERT_BREAK_ACTION = createInsertBreakAction();
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
/*     */   protected Action createInsertBreakAction() {
/*  56 */     return (Action)new CStyleInsertBreakAction();
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
/*     */   public boolean getCurlyBracesDenoteCodeBlocks(int languageIndex) {
/*  68 */     return true;
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
/*     */   public Action getInsertBreakAction() {
/*  81 */     return this.INSERT_BREAK_ACTION;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean getMarkOccurrencesOfTokenType(int type) {
/*  87 */     return (type == 20 || type == 8);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean getShouldIndentNextLineAfter(Token t) {
/*  93 */     if (t != null && t.length() == 1) {
/*  94 */       char ch = t.charAt(0);
/*  95 */       return (ch == '{' || ch == '(');
/*     */     } 
/*  97 */     return false;
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
/*     */   private boolean isInternalEolTokenForMLCs(Token t) {
/* 114 */     int type = t.getType();
/* 115 */     if (type < 0) {
/* 116 */       type = getClosestStandardTokenTypeForInternalType(type);
/* 117 */       return (type == 2 || type == 3);
/*     */     } 
/*     */     
/* 120 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected class CStyleInsertBreakAction
/*     */     extends RSyntaxTextAreaEditorKit.InsertBreakAction
/*     */   {
/*     */     public void actionPerformedImpl(ActionEvent e, RTextArea textArea) {
/* 134 */       if (!textArea.isEditable() || !textArea.isEnabled()) {
/* 135 */         UIManager.getLookAndFeel().provideErrorFeedback((Component)textArea);
/*     */         
/*     */         return;
/*     */       } 
/* 139 */       RSyntaxTextArea rsta = (RSyntaxTextArea)getTextComponent(e);
/* 140 */       RSyntaxDocument doc = (RSyntaxDocument)rsta.getDocument();
/*     */       
/* 142 */       int line = textArea.getCaretLineNumber();
/* 143 */       int type = doc.getLastTokenTypeOnLine(line);
/* 144 */       if (type < 0) {
/* 145 */         type = doc.getClosestStandardTokenTypeForInternalType(type);
/*     */       }
/*     */ 
/*     */       
/* 149 */       if (type == 3 || type == 2) {
/*     */         
/* 151 */         insertBreakInMLC(e, rsta, line);
/*     */       } else {
/*     */         
/* 154 */         handleInsertBreak(rsta, true);
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
/*     */ 
/*     */ 
/*     */     
/*     */     private boolean appearsNested(RSyntaxTextArea textArea, int line, int offs) {
/* 175 */       int firstLine = line;
/*     */       
/* 177 */       while (line < textArea.getLineCount()) {
/* 178 */         Token t = textArea.getTokenListForLine(line);
/* 179 */         int i = 0;
/*     */         
/* 181 */         if (line++ == firstLine) {
/* 182 */           t = RSyntaxUtilities.getTokenAtOffset(t, offs);
/* 183 */           if (t == null) {
/*     */             continue;
/*     */           }
/* 186 */           i = t.documentToToken(offs);
/*     */         } else {
/*     */           
/* 189 */           i = t.getTextOffset();
/*     */         } 
/* 191 */         int textOffset = t.getTextOffset();
/* 192 */         while (i < textOffset + t.length() - 1) {
/* 193 */           if (t.charAt(i - textOffset) == '/' && t.charAt(i - textOffset + 1) == '*') {
/* 194 */             return true;
/*     */           }
/* 196 */           i++;
/*     */         } 
/*     */         
/* 199 */         if ((t = t.getNextToken()) != null && !AbstractJFlexCTokenMaker.this.isInternalEolTokenForMLCs(t)) {
/* 200 */           return false;
/*     */         }
/*     */       } 
/*     */       
/* 204 */       return true;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     private void insertBreakInMLC(ActionEvent e, RSyntaxTextArea textArea, int line) {
/* 211 */       Matcher m = null;
/* 212 */       int start = -1;
/* 213 */       int end = -1;
/* 214 */       String text = null;
/*     */       try {
/* 216 */         start = textArea.getLineStartOffset(line);
/* 217 */         end = textArea.getLineEndOffset(line);
/* 218 */         text = textArea.getText(start, end - start);
/* 219 */         m = AbstractJFlexCTokenMaker.MLC_PATTERN.matcher(text);
/* 220 */       } catch (BadLocationException ble) {
/* 221 */         UIManager.getLookAndFeel().provideErrorFeedback((Component)textArea);
/* 222 */         ble.printStackTrace();
/*     */         
/*     */         return;
/*     */       } 
/* 226 */       if (m.lookingAt()) {
/*     */         
/* 228 */         String leadingWS = m.group(1);
/* 229 */         String mlcMarker = m.group(2);
/*     */ 
/*     */ 
/*     */         
/* 233 */         int dot = textArea.getCaretPosition();
/* 234 */         if (dot >= start && dot < start + leadingWS
/* 235 */           .length() + mlcMarker.length()) {
/*     */ 
/*     */           
/* 238 */           if (mlcMarker.charAt(0) == '/') {
/* 239 */             handleInsertBreak(textArea, true);
/*     */             return;
/*     */           } 
/* 242 */           textArea.setCaretPosition(end - 1);
/*     */         
/*     */         }
/*     */         else {
/*     */ 
/*     */           
/* 248 */           boolean moved = false;
/* 249 */           while (dot < end - 1 && 
/* 250 */             Character.isWhitespace(text.charAt(dot - start))) {
/* 251 */             moved = true;
/* 252 */             dot++;
/*     */           } 
/* 254 */           if (moved) {
/* 255 */             textArea.setCaretPosition(dot);
/*     */           }
/*     */         } 
/*     */         
/* 259 */         boolean firstMlcLine = (mlcMarker.charAt(0) == '/');
/* 260 */         boolean nested = appearsNested(textArea, line, start + leadingWS
/* 261 */             .length() + 2);
/*     */ 
/*     */         
/* 264 */         String header = leadingWS + (firstMlcLine ? " * " : "*") + m.group(3);
/* 265 */         textArea.replaceSelection("\n" + header);
/* 266 */         if (nested) {
/* 267 */           dot = textArea.getCaretPosition();
/* 268 */           textArea.insert("\n" + leadingWS + " */", dot);
/* 269 */           textArea.setCaretPosition(dot);
/*     */         }
/*     */       
/*     */       } else {
/*     */         
/* 274 */         handleInsertBreak(textArea, true);
/*     */       } 
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\fif\\ui\rsyntaxtextarea\AbstractJFlexCTokenMaker.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */