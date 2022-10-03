/*     */ package org.fife.ui.rsyntaxtextarea.folding;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
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
/*     */ public class XmlFoldParser
/*     */   implements FoldParser
/*     */ {
/*  30 */   private static final char[] MARKUP_CLOSING_TAG_START = new char[] { '<', '/' };
/*  31 */   private static final char[] MARKUP_SHORT_TAG_END = new char[] { '/', '>' };
/*  32 */   private static final char[] MLC_END = new char[] { '-', '-', '>' };
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public List<Fold> getFolds(RSyntaxTextArea textArea) {
/*  38 */     List<Fold> folds = new ArrayList<>();
/*     */     
/*  40 */     Fold currentFold = null;
/*  41 */     int lineCount = textArea.getLineCount();
/*  42 */     boolean inMLC = false;
/*  43 */     int mlcStart = 0;
/*     */ 
/*     */     
/*     */     try {
/*  47 */       for (int line = 0; line < lineCount; line++)
/*     */       {
/*  49 */         Token t = textArea.getTokenListForLine(line);
/*  50 */         while (t != null && t.isPaintable())
/*     */         {
/*  52 */           if (t.isComment()) {
/*     */ 
/*     */             
/*  55 */             if (inMLC) {
/*     */               
/*  57 */               if (t.endsWith(MLC_END)) {
/*  58 */                 int mlcEnd = t.getEndOffset() - 1;
/*  59 */                 if (currentFold == null) {
/*  60 */                   currentFold = new Fold(1, textArea, mlcStart);
/*  61 */                   currentFold.setEndOffset(mlcEnd);
/*  62 */                   folds.add(currentFold);
/*  63 */                   currentFold = null;
/*     */                 } else {
/*     */                   
/*  66 */                   currentFold = currentFold.createChild(1, mlcStart);
/*  67 */                   currentFold.setEndOffset(mlcEnd);
/*  68 */                   currentFold = currentFold.getParent();
/*     */                 } 
/*  70 */                 inMLC = false;
/*  71 */                 mlcStart = 0;
/*     */ 
/*     */               
/*     */               }
/*     */ 
/*     */ 
/*     */             
/*     */             }
/*  79 */             else if (t.getType() == 2 && !t.endsWith(MLC_END)) {
/*  80 */               inMLC = true;
/*  81 */               mlcStart = t.getOffset();
/*     */             
/*     */             }
/*     */ 
/*     */           
/*     */           }
/*  87 */           else if (t.isSingleChar(25, '<')) {
/*  88 */             if (currentFold == null) {
/*  89 */               currentFold = new Fold(0, textArea, t.getOffset());
/*  90 */               folds.add(currentFold);
/*     */             } else {
/*     */               
/*  93 */               currentFold = currentFold.createChild(0, t.getOffset());
/*     */             }
/*     */           
/*     */           }
/*  97 */           else if (t.is(25, MARKUP_SHORT_TAG_END)) {
/*  98 */             if (currentFold != null) {
/*  99 */               Fold parentFold = currentFold.getParent();
/* 100 */               removeFold(currentFold, folds);
/* 101 */               currentFold = parentFold;
/*     */             }
/*     */           
/*     */           }
/* 105 */           else if (t.is(25, MARKUP_CLOSING_TAG_START) && 
/* 106 */             currentFold != null) {
/* 107 */             currentFold.setEndOffset(t.getOffset());
/* 108 */             Fold parentFold = currentFold.getParent();
/*     */             
/* 110 */             if (currentFold.isOnSingleLine()) {
/* 111 */               removeFold(currentFold, folds);
/*     */             }
/* 113 */             currentFold = parentFold;
/*     */           } 
/*     */ 
/*     */           
/* 117 */           t = t.getNextToken();
/*     */         }
/*     */       
/*     */       }
/*     */     
/*     */     }
/* 123 */     catch (BadLocationException ble) {
/* 124 */       ble.printStackTrace();
/*     */     } 
/*     */     
/* 127 */     return folds;
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
/*     */   private static void removeFold(Fold fold, List<Fold> folds) {
/* 141 */     if (!fold.removeFromParent())
/* 142 */       folds.remove(folds.size() - 1); 
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\fif\\ui\rsyntaxtextarea\folding\XmlFoldParser.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */