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
/*     */ public class LatexFoldParser
/*     */   implements FoldParser
/*     */ {
/*  29 */   private static final char[] BEGIN = "\\begin".toCharArray();
/*  30 */   private static final char[] END = "\\end".toCharArray();
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public List<Fold> getFolds(RSyntaxTextArea textArea) {
/*  36 */     List<Fold> folds = new ArrayList<>();
/*  37 */     Stack<String> expectedStack = new Stack<>();
/*     */     
/*  39 */     Fold currentFold = null;
/*  40 */     int lineCount = textArea.getLineCount();
/*     */ 
/*     */     
/*     */     try {
/*  44 */       for (int line = 0; line < lineCount; line++)
/*     */       {
/*  46 */         Token t = textArea.getTokenListForLine(line);
/*  47 */         while (t != null && t.isPaintable())
/*     */         {
/*  49 */           if (t.is(6, BEGIN)) {
/*  50 */             Token temp = t.getNextToken();
/*  51 */             if (temp != null && temp.isLeftCurly()) {
/*  52 */               temp = temp.getNextToken();
/*  53 */               if (temp != null && temp.getType() == 6) {
/*  54 */                 if (currentFold == null) {
/*  55 */                   currentFold = new Fold(0, textArea, t.getOffset());
/*  56 */                   folds.add(currentFold);
/*     */                 } else {
/*     */                   
/*  59 */                   currentFold = currentFold.createChild(0, t.getOffset());
/*     */                 } 
/*  61 */                 expectedStack.push(temp.getLexeme());
/*  62 */                 t = temp;
/*     */               }
/*     */             
/*     */             }
/*     */           
/*  67 */           } else if (t.is(6, END) && currentFold != null && 
/*  68 */             !expectedStack.isEmpty()) {
/*  69 */             Token temp = t.getNextToken();
/*  70 */             if (temp != null && temp.isLeftCurly()) {
/*  71 */               temp = temp.getNextToken();
/*  72 */               if (temp != null && temp.getType() == 6) {
/*  73 */                 String value = temp.getLexeme();
/*  74 */                 if (((String)expectedStack.peek()).equals(value)) {
/*  75 */                   expectedStack.pop();
/*  76 */                   currentFold.setEndOffset(t.getOffset());
/*  77 */                   Fold parentFold = currentFold.getParent();
/*     */                   
/*  79 */                   if (currentFold.isOnSingleLine() && 
/*  80 */                     !currentFold.removeFromParent()) {
/*  81 */                     folds.remove(folds.size() - 1);
/*     */                   }
/*     */                   
/*  84 */                   t = temp;
/*  85 */                   currentFold = parentFold;
/*     */                 } 
/*     */               } 
/*     */             } 
/*     */           } 
/*     */           
/*  91 */           t = t.getNextToken();
/*     */         }
/*     */       
/*     */       }
/*     */     
/*     */     }
/*  97 */     catch (BadLocationException ble) {
/*  98 */       ble.printStackTrace();
/*     */     } 
/*     */     
/* 101 */     return folds;
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\fif\\ui\rsyntaxtextarea\folding\LatexFoldParser.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */