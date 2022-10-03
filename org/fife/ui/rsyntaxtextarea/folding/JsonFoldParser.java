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
/*     */ public class JsonFoldParser
/*     */   implements FoldParser
/*     */ {
/*  32 */   private static final Object OBJECT_BLOCK = new Object();
/*  33 */   private static final Object ARRAY_BLOCK = new Object();
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public List<Fold> getFolds(RSyntaxTextArea textArea) {
/*  39 */     Stack<Object> blocks = new Stack();
/*  40 */     List<Fold> folds = new ArrayList<>();
/*     */     
/*  42 */     Fold currentFold = null;
/*  43 */     int lineCount = textArea.getLineCount();
/*     */ 
/*     */     
/*     */     try {
/*  47 */       for (int line = 0; line < lineCount; line++)
/*     */       {
/*  49 */         Token t = textArea.getTokenListForLine(line);
/*  50 */         while (t != null && t.isPaintable())
/*     */         {
/*  52 */           if (t.isLeftCurly()) {
/*  53 */             if (currentFold == null) {
/*  54 */               currentFold = new Fold(0, textArea, t.getOffset());
/*  55 */               folds.add(currentFold);
/*     */             } else {
/*     */               
/*  58 */               currentFold = currentFold.createChild(0, t.getOffset());
/*     */             } 
/*  60 */             blocks.push(OBJECT_BLOCK);
/*     */           
/*     */           }
/*  63 */           else if (t.isRightCurly() && popOffTop(blocks, OBJECT_BLOCK)) {
/*  64 */             if (currentFold != null) {
/*  65 */               currentFold.setEndOffset(t.getOffset());
/*  66 */               Fold parentFold = currentFold.getParent();
/*     */ 
/*     */               
/*  69 */               if (currentFold.isOnSingleLine() && 
/*  70 */                 !currentFold.removeFromParent()) {
/*  71 */                 folds.remove(folds.size() - 1);
/*     */               }
/*     */               
/*  74 */               currentFold = parentFold;
/*     */             }
/*     */           
/*     */           }
/*  78 */           else if (isLeftBracket(t)) {
/*  79 */             if (currentFold == null) {
/*  80 */               currentFold = new Fold(0, textArea, t.getOffset());
/*  81 */               folds.add(currentFold);
/*     */             } else {
/*     */               
/*  84 */               currentFold = currentFold.createChild(0, t.getOffset());
/*     */             } 
/*  86 */             blocks.push(ARRAY_BLOCK);
/*     */           
/*     */           }
/*  89 */           else if (isRightBracket(t) && popOffTop(blocks, ARRAY_BLOCK) && 
/*  90 */             currentFold != null) {
/*  91 */             currentFold.setEndOffset(t.getOffset());
/*  92 */             Fold parentFold = currentFold.getParent();
/*     */ 
/*     */             
/*  95 */             if (currentFold.isOnSingleLine() && 
/*  96 */               !currentFold.removeFromParent()) {
/*  97 */               folds.remove(folds.size() - 1);
/*     */             }
/*     */             
/* 100 */             currentFold = parentFold;
/*     */           } 
/*     */ 
/*     */           
/* 104 */           t = t.getNextToken();
/*     */         }
/*     */       
/*     */       }
/*     */     
/*     */     }
/* 110 */     catch (BadLocationException ble) {
/* 111 */       ble.printStackTrace();
/*     */     } 
/*     */     
/* 114 */     return folds;
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
/*     */   private static boolean isLeftBracket(Token t) {
/* 127 */     return (t.getType() == 22 && t.isSingleChar('['));
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
/*     */   private static boolean isRightBracket(Token t) {
/* 139 */     return (t.getType() == 22 && t.isSingleChar(']'));
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
/*     */   private static boolean popOffTop(Stack<Object> stack, Object value) {
/* 152 */     if (stack.size() > 0 && stack.peek() == value) {
/* 153 */       stack.pop();
/* 154 */       return true;
/*     */     } 
/* 156 */     return false;
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\fif\\ui\rsyntaxtextarea\folding\JsonFoldParser.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */