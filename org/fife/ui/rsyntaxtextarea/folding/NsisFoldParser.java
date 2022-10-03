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
/*     */ public class NsisFoldParser
/*     */   implements FoldParser
/*     */ {
/*  33 */   private static final char[] KEYWORD_FUNCTION = "Function".toCharArray();
/*  34 */   private static final char[] KEYWORD_FUNCTION_END = "FunctionEnd".toCharArray();
/*  35 */   private static final char[] KEYWORD_SECTION = "Section".toCharArray();
/*  36 */   private static final char[] KEYWORD_SECTION_END = "SectionEnd".toCharArray();
/*     */   
/*  38 */   protected static final char[] C_MLC_END = "*/".toCharArray();
/*     */ 
/*     */ 
/*     */   
/*     */   private static boolean foundEndKeyword(char[] keyword, Token t, Stack<char[]> endWordStack) {
/*  43 */     return (t.is(6, keyword) && !endWordStack.isEmpty() && keyword == endWordStack
/*  44 */       .peek());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public List<Fold> getFolds(RSyntaxTextArea textArea) {
/*  51 */     List<Fold> folds = new ArrayList<>();
/*     */     
/*  53 */     Fold currentFold = null;
/*  54 */     int lineCount = textArea.getLineCount();
/*  55 */     boolean inMLC = false;
/*  56 */     int mlcStart = 0;
/*  57 */     Stack<char[]> endWordStack = (Stack)new Stack<>();
/*     */ 
/*     */     
/*     */     try {
/*  61 */       for (int line = 0; line < lineCount; line++)
/*     */       {
/*  63 */         Token t = textArea.getTokenListForLine(line);
/*  64 */         while (t != null && t.isPaintable())
/*     */         {
/*  66 */           if (t.isComment()) {
/*     */             
/*  68 */             if (inMLC)
/*     */             {
/*     */               
/*  71 */               if (t.endsWith(C_MLC_END)) {
/*  72 */                 int mlcEnd = t.getEndOffset() - 1;
/*  73 */                 if (currentFold == null) {
/*  74 */                   currentFold = new Fold(1, textArea, mlcStart);
/*  75 */                   currentFold.setEndOffset(mlcEnd);
/*  76 */                   folds.add(currentFold);
/*  77 */                   currentFold = null;
/*     */                 } else {
/*     */                   
/*  80 */                   currentFold = currentFold.createChild(1, mlcStart);
/*  81 */                   currentFold.setEndOffset(mlcEnd);
/*  82 */                   currentFold = currentFold.getParent();
/*     */                 } 
/*     */                 
/*  85 */                 inMLC = false;
/*  86 */                 mlcStart = 0;
/*     */ 
/*     */               
/*     */               }
/*     */ 
/*     */             
/*     */             }
/*  93 */             else if (t.getType() != 1 && !t.endsWith(C_MLC_END))
/*     */             {
/*  95 */               inMLC = true;
/*  96 */               mlcStart = t.getOffset();
/*     */             
/*     */             }
/*     */ 
/*     */           
/*     */           }
/* 102 */           else if (t.is(6, KEYWORD_SECTION)) {
/* 103 */             if (currentFold == null) {
/* 104 */               currentFold = new Fold(0, textArea, t.getOffset());
/* 105 */               folds.add(currentFold);
/*     */             } else {
/*     */               
/* 108 */               currentFold = currentFold.createChild(0, t.getOffset());
/*     */             } 
/* 110 */             endWordStack.push(KEYWORD_SECTION_END);
/*     */           
/*     */           }
/* 113 */           else if (t.is(6, KEYWORD_FUNCTION)) {
/* 114 */             if (currentFold == null) {
/* 115 */               currentFold = new Fold(0, textArea, t.getOffset());
/* 116 */               folds.add(currentFold);
/*     */             } else {
/*     */               
/* 119 */               currentFold = currentFold.createChild(0, t.getOffset());
/*     */             } 
/* 121 */             endWordStack.push(KEYWORD_FUNCTION_END);
/*     */           
/*     */           }
/* 124 */           else if ((foundEndKeyword(KEYWORD_SECTION_END, t, endWordStack) || 
/* 125 */             foundEndKeyword(KEYWORD_FUNCTION_END, t, endWordStack)) && 
/* 126 */             currentFold != null) {
/* 127 */             currentFold.setEndOffset(t.getOffset());
/* 128 */             Fold parentFold = currentFold.getParent();
/* 129 */             endWordStack.pop();
/*     */             
/* 131 */             if (currentFold.isOnSingleLine() && 
/* 132 */               !currentFold.removeFromParent()) {
/* 133 */               folds.remove(folds.size() - 1);
/*     */             }
/*     */             
/* 136 */             currentFold = parentFold;
/*     */           } 
/*     */ 
/*     */           
/* 140 */           t = t.getNextToken();
/*     */         }
/*     */       
/*     */       }
/*     */     
/*     */     }
/* 146 */     catch (BadLocationException ble) {
/* 147 */       ble.printStackTrace();
/*     */     } 
/*     */     
/* 150 */     return folds;
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\fif\\ui\rsyntaxtextarea\folding\NsisFoldParser.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */