/*    */ package org.fife.ui.rsyntaxtextarea.folding;
/*    */ 
/*    */ import java.util.ArrayList;
/*    */ import java.util.List;
/*    */ import javax.swing.text.BadLocationException;
/*    */ import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;
/*    */ import org.fife.ui.rsyntaxtextarea.Token;
/*    */ import org.fife.ui.rsyntaxtextarea.TokenUtils;
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
/*    */ public class LinesWithContentFoldParser
/*    */   implements FoldParser
/*    */ {
/*    */   public List<Fold> getFolds(RSyntaxTextArea textArea) {
/* 30 */     List<Fold> folds = new ArrayList<>();
/*    */     
/* 32 */     Fold fold = null;
/* 33 */     int lineCount = textArea.getLineCount();
/*    */ 
/*    */     
/*    */     try {
/* 37 */       for (int line = 0; line < lineCount; line++) {
/*    */         
/* 39 */         Token t = textArea.getTokenListForLine(line);
/*    */         
/* 41 */         if (!TokenUtils.isBlankOrAllWhiteSpaceWithoutComments(t)) {
/*    */ 
/*    */           
/* 44 */           if (fold == null) {
/* 45 */             fold = new Fold(0, textArea, t.getOffset());
/* 46 */             folds.add(fold);
/*    */           }
/*    */         
/* 49 */         } else if (fold != null) {
/*    */           
/* 51 */           fold.setEndOffset(textArea.getLineStartOffset(line) - 1);
/* 52 */           if (fold.isOnSingleLine()) {
/* 53 */             folds.remove(folds.size() - 1);
/*    */           }
/* 55 */           fold = null;
/*    */         } 
/*    */       } 
/* 58 */     } catch (BadLocationException ble) {
/* 59 */       ble.printStackTrace();
/*    */     } 
/*    */     
/* 62 */     return folds;
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\fif\\ui\rsyntaxtextarea\folding\LinesWithContentFoldParser.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */