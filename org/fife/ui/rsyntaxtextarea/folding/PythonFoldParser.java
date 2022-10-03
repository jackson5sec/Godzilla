/*     */ package org.fife.ui.rsyntaxtextarea.folding;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import java.util.Stack;
/*     */ import javax.swing.text.BadLocationException;
/*     */ import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;
/*     */ import org.fife.ui.rsyntaxtextarea.Token;
/*     */ import org.fife.ui.rsyntaxtextarea.TokenUtils;
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
/*     */ public class PythonFoldParser
/*     */   implements FoldParser
/*     */ {
/*     */   public List<Fold> getFolds(RSyntaxTextArea textArea) {
/*  29 */     List<Fold> folds = new ArrayList<>();
/*     */     
/*  31 */     Fold currentFold = null;
/*  32 */     int lineCount = textArea.getLineCount();
/*  33 */     int tabSize = textArea.getTabSize();
/*  34 */     Stack<Integer> foldStartLeadingWhiteSpaceCounts = new Stack<>();
/*  35 */     int currentNextFoldStart = 0;
/*  36 */     int currentLeadingWhiteSpaceCount = 0;
/*     */ 
/*     */     
/*     */     try {
/*  40 */       for (int line = 0; line < lineCount; line++) {
/*     */         
/*  42 */         Token t = textArea.getTokenListForLine(line);
/*     */         
/*  44 */         int leadingWhiteSpaceCount = getLeadingWhiteSpaceCount(t, tabSize);
/*  45 */         if (leadingWhiteSpaceCount != -1) {
/*     */ 
/*     */ 
/*     */           
/*  49 */           if (leadingWhiteSpaceCount == currentLeadingWhiteSpaceCount) {
/*  50 */             currentNextFoldStart = t.getOffset() + leadingWhiteSpaceCount;
/*     */             continue;
/*     */           } 
/*  53 */           if (leadingWhiteSpaceCount > currentLeadingWhiteSpaceCount) {
/*     */             
/*  55 */             if (currentFold != null) {
/*  56 */               currentFold = currentFold.createChild(0, currentNextFoldStart);
/*     */             }
/*     */             else {
/*     */               
/*  60 */               currentFold = new Fold(0, textArea, currentNextFoldStart);
/*  61 */               folds.add(currentFold);
/*     */             } 
/*  63 */             foldStartLeadingWhiteSpaceCounts.push(Integer.valueOf(currentLeadingWhiteSpaceCount));
/*  64 */             currentNextFoldStart = t.getOffset() + leadingWhiteSpaceCount;
/*     */ 
/*     */             
/*     */             continue;
/*     */           } 
/*     */ 
/*     */           
/*  71 */           currentNextFoldStart = t.getOffset() + leadingWhiteSpaceCount;
/*     */ 
/*     */ 
/*     */ 
/*     */           
/*  76 */           int prevLine = line - 1;
/*     */           while (true)
/*  78 */           { t = textArea.getTokenListForLine(prevLine--);
/*  79 */             if (!TokenUtils.isBlankOrAllWhiteSpace(t))
/*  80 */             { int endOffs = t.getEndOffset() - 1;
/*     */               
/*  82 */               boolean foundBlock = false;
/*  83 */               while (!foldStartLeadingWhiteSpaceCounts.isEmpty() && ((Integer)foldStartLeadingWhiteSpaceCounts
/*  84 */                 .peek()).intValue() >= leadingWhiteSpaceCount) {
/*     */ 
/*     */                 
/*  87 */                 currentFold.setEndOffset(endOffs);
/*  88 */                 currentFold = currentFold.getParent();
/*  89 */                 foldStartLeadingWhiteSpaceCounts.pop();
/*  90 */                 foundBlock = true;
/*     */               } 
/*     */ 
/*     */               
/*  94 */               if (!foundBlock && currentFold != null && !currentFold.removeFromParent()) {
/*  95 */                 folds.remove(folds.size() - 1);
/*     */               }
/*     */ 
/*     */               
/*  99 */               currentLeadingWhiteSpaceCount = leadingWhiteSpaceCount; break; }  } 
/*     */         } 
/*     */       } 
/* 102 */     } catch (BadLocationException ble) {
/* 103 */       ble.printStackTrace();
/*     */     } 
/*     */     
/* 106 */     return folds;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static int getLeadingWhiteSpaceCount(Token t, int tabSize) {
/* 113 */     if (t == null || t.getType() == 13 || t
/* 114 */       .getType() == 14) {
/* 115 */       return -1;
/*     */     }
/*     */     
/* 118 */     int count = 0;
/* 119 */     while (t != null && t.isPaintable()) {
/* 120 */       if (!t.isWhitespace())
/*     */       {
/*     */         
/* 123 */         return (t.getType() == 1) ? -1 : count;
/*     */       }
/* 125 */       count += TokenUtils.getWhiteSpaceTokenLength(t, tabSize, count);
/* 126 */       t = t.getNextToken();
/*     */     } 
/*     */ 
/*     */     
/* 130 */     return -1;
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\fif\\ui\rsyntaxtextarea\folding\PythonFoldParser.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */