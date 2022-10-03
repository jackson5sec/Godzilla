/*     */ package org.fife.ui.rsyntaxtextarea;
/*     */ 
/*     */ import java.util.Iterator;
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
/*     */ class TokenIterator
/*     */   implements Iterator<Token>
/*     */ {
/*     */   private RSyntaxDocument doc;
/*     */   private int curLine;
/*     */   private Token token;
/*     */   
/*     */   TokenIterator(RSyntaxDocument doc) {
/*  34 */     this.doc = doc;
/*  35 */     loadTokenListForCurLine();
/*  36 */     int lineCount = getLineCount();
/*  37 */     while ((this.token == null || !this.token.isPaintable()) && this.curLine < lineCount - 1) {
/*  38 */       this.curLine++;
/*  39 */       loadTokenListForCurLine();
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private int getLineCount() {
/*  45 */     return this.doc.getDefaultRootElement().getElementCount();
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
/*     */   public boolean hasNext() {
/*  57 */     return (this.token != null);
/*     */   }
/*     */ 
/*     */   
/*     */   private void loadTokenListForCurLine() {
/*  62 */     this.token = this.doc.getTokenListForLine(this.curLine);
/*  63 */     if (this.token != null && !this.token.isPaintable())
/*     */     {
/*  65 */       this.token = null;
/*     */     }
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
/*     */   public Token next() {
/*  79 */     Token t = this.token;
/*  80 */     boolean tIsCloned = false;
/*  81 */     int lineCount = getLineCount();
/*     */ 
/*     */     
/*  84 */     if (this.token != null && this.token.isPaintable()) {
/*  85 */       this.token = this.token.getNextToken();
/*     */     }
/*  87 */     else if (this.curLine < lineCount - 1) {
/*  88 */       t = new TokenImpl(t);
/*  89 */       tIsCloned = true;
/*  90 */       this.curLine++;
/*  91 */       loadTokenListForCurLine();
/*     */     }
/*  93 */     else if (this.token != null && !this.token.isPaintable()) {
/*     */       
/*  95 */       this.token = null;
/*     */     } 
/*     */     
/*  98 */     while ((this.token == null || !this.token.isPaintable()) && this.curLine < lineCount - 1) {
/*  99 */       if (!tIsCloned) {
/* 100 */         t = new TokenImpl(t);
/* 101 */         tIsCloned = true;
/*     */       } 
/* 103 */       this.curLine++;
/* 104 */       loadTokenListForCurLine();
/*     */     } 
/* 106 */     if (this.token != null && !this.token.isPaintable() && this.curLine == lineCount - 1) {
/* 107 */       this.token = null;
/*     */     }
/*     */     
/* 110 */     return t;
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
/*     */   public void remove() {
/* 123 */     throw new UnsupportedOperationException();
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\fif\\ui\rsyntaxtextarea\TokenIterator.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */