/*     */ package org.fife.ui.rsyntaxtextarea;
/*     */ 
/*     */ import javax.swing.text.Segment;
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
/*     */ class DefaultTokenFactory
/*     */   implements TokenFactory
/*     */ {
/*     */   private int size;
/*     */   private int increment;
/*     */   private TokenImpl[] tokenList;
/*     */   private int currentFreeToken;
/*     */   protected static final int DEFAULT_START_SIZE = 30;
/*     */   protected static final int DEFAULT_INCREMENT = 10;
/*     */   
/*     */   DefaultTokenFactory() {
/*  46 */     this(30, 10);
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
/*     */   DefaultTokenFactory(int size, int increment) {
/*  59 */     this.size = size;
/*  60 */     this.increment = increment;
/*  61 */     this.currentFreeToken = 0;
/*     */ 
/*     */     
/*  64 */     this.tokenList = new TokenImpl[size];
/*  65 */     for (int i = 0; i < size; i++) {
/*  66 */       this.tokenList[i] = new TokenImpl();
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void augmentTokenList() {
/*  77 */     TokenImpl[] temp = new TokenImpl[this.size + this.increment];
/*  78 */     System.arraycopy(this.tokenList, 0, temp, 0, this.size);
/*  79 */     this.size += this.increment;
/*  80 */     this.tokenList = temp;
/*  81 */     for (int i = 0; i < this.increment; i++) {
/*  82 */       this.tokenList[this.size - i - 1] = new TokenImpl();
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public TokenImpl createToken() {
/*  90 */     TokenImpl token = this.tokenList[this.currentFreeToken];
/*  91 */     token.text = null;
/*  92 */     token.setType(0);
/*  93 */     token.setOffset(-1);
/*  94 */     token.setNextToken(null);
/*  95 */     this.currentFreeToken++;
/*  96 */     if (this.currentFreeToken == this.size) {
/*  97 */       augmentTokenList();
/*     */     }
/*  99 */     return token;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public TokenImpl createToken(Segment line, int beg, int end, int startOffset, int type) {
/* 106 */     return createToken(line.array, beg, end, startOffset, type);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public TokenImpl createToken(char[] line, int beg, int end, int startOffset, int type) {
/* 113 */     TokenImpl token = this.tokenList[this.currentFreeToken];
/* 114 */     token.set(line, beg, end, startOffset, type);
/* 115 */     this.currentFreeToken++;
/* 116 */     if (this.currentFreeToken == this.size) {
/* 117 */       augmentTokenList();
/*     */     }
/* 119 */     return token;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void resetAllTokens() {
/* 130 */     this.currentFreeToken = 0;
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\fif\\ui\rsyntaxtextarea\DefaultTokenFactory.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */