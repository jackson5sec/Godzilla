/*     */ package org.fife.ui.rsyntaxtextarea;
/*     */ 
/*     */ import javax.swing.Action;
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
/*     */ public abstract class TokenMakerBase
/*     */   implements TokenMaker
/*     */ {
/*  64 */   protected TokenImpl firstToken = this.currentToken = this.previousToken = null;
/*  65 */   private TokenFactory tokenFactory = new DefaultTokenFactory(); protected TokenImpl currentToken;
/*     */   protected TokenImpl previousToken;
/*     */   private OccurrenceMarker occurrenceMarker;
/*     */   private int languageIndex;
/*     */   
/*     */   public void addNullToken() {
/*  71 */     if (this.firstToken == null) {
/*  72 */       this.firstToken = this.tokenFactory.createToken();
/*  73 */       this.currentToken = this.firstToken;
/*     */     } else {
/*     */       
/*  76 */       TokenImpl next = this.tokenFactory.createToken();
/*  77 */       this.currentToken.setNextToken(next);
/*  78 */       this.previousToken = this.currentToken;
/*  79 */       this.currentToken = next;
/*     */     } 
/*  81 */     this.currentToken.setLanguageIndex(this.languageIndex);
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
/*     */   
/*     */   public void addToken(Segment segment, int start, int end, int tokenType, int startOffset) {
/*  97 */     addToken(segment.array, start, end, tokenType, startOffset);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addToken(char[] array, int start, int end, int tokenType, int startOffset) {
/* 104 */     addToken(array, start, end, tokenType, startOffset, false);
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
/*     */ 
/*     */ 
/*     */   
/*     */   public void addToken(char[] array, int start, int end, int tokenType, int startOffset, boolean hyperlink) {
/* 122 */     if (this.firstToken == null) {
/* 123 */       this.firstToken = this.tokenFactory.createToken(array, start, end, startOffset, tokenType);
/*     */       
/* 125 */       this.currentToken = this.firstToken;
/*     */     } else {
/*     */       
/* 128 */       TokenImpl next = this.tokenFactory.createToken(array, start, end, startOffset, tokenType);
/*     */       
/* 130 */       this.currentToken.setNextToken(next);
/* 131 */       this.previousToken = this.currentToken;
/* 132 */       this.currentToken = next;
/*     */     } 
/*     */     
/* 135 */     this.currentToken.setLanguageIndex(this.languageIndex);
/* 136 */     this.currentToken.setHyperlink(hyperlink);
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
/*     */   protected OccurrenceMarker createOccurrenceMarker() {
/* 148 */     return new DefaultOccurrenceMarker();
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
/*     */ 
/*     */   
/*     */   public int getClosestStandardTokenTypeForInternalType(int type) {
/* 165 */     return type;
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
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean getCurlyBracesDenoteCodeBlocks(int languageIndex) {
/* 184 */     return false;
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
/*     */   public Action getInsertBreakAction() {
/* 197 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected int getLanguageIndex() {
/* 208 */     return this.languageIndex;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getLastTokenTypeOnLine(Segment text, int initialTokenType) {
/* 216 */     Token t = getTokenList(text, initialTokenType, 0);
/*     */     
/* 218 */     while (t.getNextToken() != null) {
/* 219 */       t = t.getNextToken();
/*     */     }
/*     */     
/* 222 */     return t.getType();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String[] getLineCommentStartAndEnd(int languageIndex) {
/* 229 */     return null;
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
/*     */ 
/*     */   
/*     */   public boolean getMarkOccurrencesOfTokenType(int type) {
/* 246 */     return (type == 20);
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
/*     */   protected boolean getNoTokensIdentifiedYet() {
/* 258 */     return (this.firstToken == null);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public OccurrenceMarker getOccurrenceMarker() {
/* 264 */     if (this.occurrenceMarker == null) {
/* 265 */       this.occurrenceMarker = createOccurrenceMarker();
/*     */     }
/* 267 */     return this.occurrenceMarker;
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
/*     */   public boolean getShouldIndentNextLineAfter(Token token) {
/* 280 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isIdentifierChar(int languageIndex, char ch) {
/* 291 */     return (Character.isLetterOrDigit(ch) || ch == '_' || ch == '$');
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
/*     */   public boolean isMarkupLanguage() {
/* 304 */     return false;
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
/*     */   protected void resetTokenList() {
/* 316 */     this.firstToken = this.currentToken = this.previousToken = null;
/* 317 */     this.tokenFactory.resetAllTokens();
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
/*     */   
/*     */   protected void setLanguageIndex(int languageIndex) {
/* 333 */     this.languageIndex = Math.max(0, languageIndex);
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\fif\\ui\rsyntaxtextarea\TokenMakerBase.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */