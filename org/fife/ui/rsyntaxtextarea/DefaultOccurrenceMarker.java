/*    */ package org.fife.ui.rsyntaxtextarea;
/*    */ 
/*    */ import javax.swing.text.BadLocationException;
/*    */ import javax.swing.text.Caret;
/*    */ import org.fife.ui.rtextarea.SmartHighlightPainter;
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
/*    */ class DefaultOccurrenceMarker
/*    */   implements OccurrenceMarker
/*    */ {
/*    */   public Token getTokenToMark(RSyntaxTextArea textArea) {
/* 32 */     int line = textArea.getCaretLineNumber();
/* 33 */     Token tokenList = textArea.getTokenListForLine(line);
/* 34 */     Caret c = textArea.getCaret();
/* 35 */     int dot = c.getDot();
/*    */     
/* 37 */     Token t = RSyntaxUtilities.getTokenAtOffset(tokenList, dot);
/* 38 */     if (t == null || !isValidType(textArea, t) || 
/* 39 */       RSyntaxUtilities.isNonWordChar(t)) {
/*    */       
/* 41 */       dot--;
/*    */       try {
/* 43 */         if (dot >= textArea.getLineStartOffset(line)) {
/* 44 */           t = RSyntaxUtilities.getTokenAtOffset(tokenList, dot);
/*    */         }
/* 46 */       } catch (BadLocationException ble) {
/* 47 */         ble.printStackTrace();
/*    */       } 
/*    */     } 
/*    */     
/* 51 */     return t;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public boolean isValidType(RSyntaxTextArea textArea, Token t) {
/* 58 */     return textArea.getMarkOccurrencesOfTokenType(t.getType());
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void markOccurrences(RSyntaxDocument doc, Token t, RSyntaxTextAreaHighlighter h, SmartHighlightPainter p) {
/* 65 */     markOccurrencesOfToken(doc, t, h, p);
/*    */   }
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
/*    */   public static void markOccurrencesOfToken(RSyntaxDocument doc, Token t, RSyntaxTextAreaHighlighter h, SmartHighlightPainter p) {
/* 81 */     char[] lexeme = t.getLexeme().toCharArray();
/* 82 */     int type = t.getType();
/* 83 */     int lineCount = doc.getDefaultRootElement().getElementCount();
/*    */     
/* 85 */     for (int i = 0; i < lineCount; i++) {
/* 86 */       Token temp = doc.getTokenListForLine(i);
/* 87 */       while (temp != null && temp.isPaintable()) {
/* 88 */         if (temp.is(type, lexeme)) {
/*    */           try {
/* 90 */             int end = temp.getEndOffset();
/* 91 */             h.addMarkedOccurrenceHighlight(temp.getOffset(), end, p);
/* 92 */           } catch (BadLocationException ble) {
/* 93 */             ble.printStackTrace();
/*    */           } 
/*    */         }
/* 96 */         temp = temp.getNextToken();
/*    */       } 
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\fif\\ui\rsyntaxtextarea\DefaultOccurrenceMarker.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */