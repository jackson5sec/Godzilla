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
/*     */ public class CurlyFoldParser
/*     */   implements FoldParser
/*     */ {
/*     */   private boolean foldableMultiLineComments;
/*     */   private final boolean java;
/*  60 */   private static final char[] KEYWORD_IMPORT = "import".toCharArray();
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  65 */   protected static final char[] C_MLC_END = "*/".toCharArray();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public CurlyFoldParser() {
/*  73 */     this(true, false);
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
/*     */   public CurlyFoldParser(boolean cStyleMultiLineComments, boolean java) {
/*  87 */     this.foldableMultiLineComments = cStyleMultiLineComments;
/*  88 */     this.java = java;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean getFoldableMultiLineComments() {
/*  99 */     return this.foldableMultiLineComments;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public List<Fold> getFolds(RSyntaxTextArea textArea) {
/* 106 */     List<Fold> folds = new ArrayList<>();
/*     */     
/* 108 */     Fold currentFold = null;
/* 109 */     int lineCount = textArea.getLineCount();
/* 110 */     boolean inMLC = false;
/* 111 */     int mlcStart = 0;
/* 112 */     int importStartLine = -1;
/* 113 */     int lastSeenImportLine = -1;
/* 114 */     int importGroupStartOffs = -1;
/* 115 */     int importGroupEndOffs = -1;
/* 116 */     int lastRightCurlyLine = -1;
/* 117 */     Fold prevFold = null;
/*     */ 
/*     */     
/*     */     try {
/* 121 */       for (int line = 0; line < lineCount; line++)
/*     */       {
/* 123 */         Token t = textArea.getTokenListForLine(line);
/* 124 */         while (t != null && t.isPaintable())
/*     */         {
/* 126 */           if (getFoldableMultiLineComments() && t.isComment()) {
/*     */ 
/*     */             
/* 129 */             if (this.java)
/*     */             {
/* 131 */               if (importStartLine > -1) {
/* 132 */                 if (lastSeenImportLine > importStartLine) {
/* 133 */                   Fold fold = null;
/*     */ 
/*     */ 
/*     */ 
/*     */                   
/* 138 */                   if (currentFold == null) {
/* 139 */                     fold = new Fold(2, textArea, importGroupStartOffs);
/*     */                     
/* 141 */                     folds.add(fold);
/*     */                   } else {
/*     */                     
/* 144 */                     fold = currentFold.createChild(2, importGroupStartOffs);
/*     */                   } 
/*     */                   
/* 147 */                   fold.setEndOffset(importGroupEndOffs);
/*     */                 } 
/* 149 */                 importStartLine = lastSeenImportLine = importGroupStartOffs = importGroupEndOffs = -1;
/*     */               } 
/*     */             }
/*     */ 
/*     */ 
/*     */             
/* 155 */             if (inMLC)
/*     */             {
/*     */               
/* 158 */               if (t.endsWith(C_MLC_END)) {
/* 159 */                 int mlcEnd = t.getEndOffset() - 1;
/* 160 */                 if (currentFold == null) {
/* 161 */                   currentFold = new Fold(1, textArea, mlcStart);
/* 162 */                   currentFold.setEndOffset(mlcEnd);
/* 163 */                   folds.add(currentFold);
/* 164 */                   currentFold = null;
/*     */                 } else {
/*     */                   
/* 167 */                   currentFold = currentFold.createChild(1, mlcStart);
/* 168 */                   currentFold.setEndOffset(mlcEnd);
/* 169 */                   currentFold = currentFold.getParent();
/*     */                 } 
/*     */                 
/* 172 */                 inMLC = false;
/* 173 */                 mlcStart = 0;
/*     */ 
/*     */               
/*     */               }
/*     */ 
/*     */             
/*     */             }
/* 180 */             else if (t.getType() != 1 && !t.endsWith(C_MLC_END))
/*     */             {
/* 182 */               inMLC = true;
/* 183 */               mlcStart = t.getOffset();
/*     */             
/*     */             }
/*     */ 
/*     */           
/*     */           }
/* 189 */           else if (isLeftCurly(t)) {
/*     */ 
/*     */             
/* 192 */             if (this.java)
/*     */             {
/* 194 */               if (importStartLine > -1) {
/* 195 */                 if (lastSeenImportLine > importStartLine) {
/* 196 */                   Fold fold = null;
/*     */ 
/*     */ 
/*     */ 
/*     */                   
/* 201 */                   if (currentFold == null) {
/* 202 */                     fold = new Fold(2, textArea, importGroupStartOffs);
/*     */                     
/* 204 */                     folds.add(fold);
/*     */                   } else {
/*     */                     
/* 207 */                     fold = currentFold.createChild(2, importGroupStartOffs);
/*     */                   } 
/*     */                   
/* 210 */                   fold.setEndOffset(importGroupEndOffs);
/*     */                 } 
/* 212 */                 importStartLine = lastSeenImportLine = importGroupStartOffs = importGroupEndOffs = -1;
/*     */               } 
/*     */             }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */             
/* 221 */             if (prevFold != null && line == lastRightCurlyLine) {
/* 222 */               currentFold = prevFold;
/*     */ 
/*     */ 
/*     */               
/* 226 */               prevFold = null;
/* 227 */               lastRightCurlyLine = -1;
/*     */             }
/* 229 */             else if (currentFold == null) {
/* 230 */               currentFold = new Fold(0, textArea, t.getOffset());
/* 231 */               folds.add(currentFold);
/*     */             } else {
/*     */               
/* 234 */               currentFold = currentFold.createChild(0, t.getOffset());
/*     */             
/*     */             }
/*     */           
/*     */           }
/* 239 */           else if (isRightCurly(t)) {
/*     */             
/* 241 */             if (currentFold != null) {
/* 242 */               currentFold.setEndOffset(t.getOffset());
/* 243 */               Fold parentFold = currentFold.getParent();
/*     */ 
/*     */               
/* 246 */               if (currentFold.isOnSingleLine()) {
/* 247 */                 if (!currentFold.removeFromParent()) {
/* 248 */                   folds.remove(folds.size() - 1);
/*     */                 
/*     */                 }
/*     */               
/*     */               }
/*     */               else {
/*     */                 
/* 255 */                 lastRightCurlyLine = line;
/* 256 */                 prevFold = currentFold;
/*     */               } 
/* 258 */               currentFold = parentFold;
/*     */             
/*     */             }
/*     */ 
/*     */           
/*     */           }
/* 264 */           else if (this.java) {
/*     */             
/* 266 */             if (t.is(6, KEYWORD_IMPORT)) {
/* 267 */               if (importStartLine == -1) {
/* 268 */                 importStartLine = line;
/* 269 */                 importGroupStartOffs = t.getOffset();
/* 270 */                 importGroupEndOffs = t.getOffset();
/*     */               } 
/* 272 */               lastSeenImportLine = line;
/*     */             
/*     */             }
/* 275 */             else if (importStartLine > -1 && t
/* 276 */               .isIdentifier() && t
/* 277 */               .isSingleChar(';')) {
/* 278 */               importGroupEndOffs = t.getOffset();
/*     */             } 
/*     */           } 
/*     */ 
/*     */           
/* 283 */           t = t.getNextToken();
/*     */         }
/*     */       
/*     */       }
/*     */     
/*     */     }
/* 289 */     catch (BadLocationException ble) {
/* 290 */       ble.printStackTrace();
/*     */     } 
/*     */     
/* 293 */     return folds;
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
/*     */   public boolean isLeftCurly(Token t) {
/* 307 */     return t.isLeftCurly();
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
/*     */   public boolean isRightCurly(Token t) {
/* 320 */     return t.isRightCurly();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setFoldableMultiLineComments(boolean foldable) {
/* 331 */     this.foldableMultiLineComments = foldable;
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\fif\\ui\rsyntaxtextarea\folding\CurlyFoldParser.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */