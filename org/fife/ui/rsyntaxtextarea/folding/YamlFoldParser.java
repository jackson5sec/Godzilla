/*     */ package org.fife.ui.rsyntaxtextarea.folding;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import java.util.Stack;
/*     */ import javax.swing.text.BadLocationException;
/*     */ import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;
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
/*     */ public class YamlFoldParser
/*     */   implements FoldParser
/*     */ {
/*     */   private static boolean isSpaces(Token t) {
/*  36 */     String lexeme = t.getLexeme();
/*  37 */     return lexeme.trim().isEmpty();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public List<Fold> getFolds(RSyntaxTextArea textArea) {
/*  43 */     List<Fold> folds = new ArrayList<>();
/*  44 */     Stack<Integer> indentStack = new Stack<>();
/*     */     
/*  46 */     Fold currentFold = null;
/*  47 */     int lineCount = textArea.getLineCount();
/*     */ 
/*     */     
/*  50 */     int lastOffset = 0;
/*     */ 
/*     */     
/*     */     try {
/*  54 */       for (int line = 0; line < lineCount; line++) {
/*     */         
/*  56 */         Token t = textArea.getTokenListForLine(line);
/*  57 */         if (t.isPaintable())
/*     */         {
/*     */           
/*  60 */           Token startLine = t;
/*  61 */           int offset = t.getOffset();
/*     */ 
/*     */           
/*  64 */           int indent = 0;
/*  65 */           while (t != null && t.isPaintable() && isSpaces(t)) {
/*  66 */             indent += t.length();
/*  67 */             t = t.getNextToken();
/*     */           } 
/*  69 */           if (t != null && t.isPaintable() && t.isSingleChar('-')) {
/*  70 */             indent++;
/*  71 */             t = t.getNextToken();
/*     */           } 
/*     */           
/*  74 */           while (!indentStack.empty()) {
/*  75 */             int outer = ((Integer)indentStack.peek()).intValue();
/*  76 */             if (outer >= indent && currentFold != null) {
/*  77 */               currentFold.setEndOffset(lastOffset);
/*  78 */               Fold parentFold = currentFold.getParent();
/*     */               
/*  80 */               if (currentFold.isOnSingleLine()) {
/*  81 */                 removeFold(currentFold, folds);
/*     */               }
/*  83 */               currentFold = parentFold;
/*  84 */               indentStack.pop();
/*     */             } 
/*     */           } 
/*     */ 
/*     */ 
/*     */ 
/*     */           
/*  91 */           while (t != null && t.isPaintable()) {
/*  92 */             offset = t.getOffset();
/*  93 */             t = t.getNextToken();
/*     */           } 
/*  95 */           lastOffset = offset;
/*     */           
/*  97 */           if (currentFold == null) {
/*  98 */             currentFold = new Fold(0, textArea, startLine.getOffset());
/*  99 */             folds.add(currentFold);
/*     */           } else {
/* 101 */             currentFold = currentFold.createChild(0, startLine.getOffset());
/*     */           } 
/* 103 */           indentStack.push(Integer.valueOf(indent));
/*     */         }
/*     */       
/*     */       } 
/* 107 */     } catch (BadLocationException ble) {
/* 108 */       ble.printStackTrace();
/*     */     } 
/*     */     
/* 111 */     return folds;
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
/*     */   private static void removeFold(Fold fold, List<Fold> folds) {
/* 126 */     if (!fold.removeFromParent())
/* 127 */       folds.remove(folds.size() - 1); 
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\fif\\ui\rsyntaxtextarea\folding\YamlFoldParser.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */