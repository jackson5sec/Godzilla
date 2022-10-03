/*    */ package org.springframework.expression.spel.standard;
/*    */ 
/*    */ import org.springframework.lang.Nullable;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ class Token
/*    */ {
/*    */   TokenKind kind;
/*    */   @Nullable
/*    */   String data;
/*    */   int startPos;
/*    */   int endPos;
/*    */   
/*    */   Token(TokenKind tokenKind, int startPos, int endPos) {
/* 47 */     this.kind = tokenKind;
/* 48 */     this.startPos = startPos;
/* 49 */     this.endPos = endPos;
/*    */   }
/*    */   
/*    */   Token(TokenKind tokenKind, char[] tokenData, int startPos, int endPos) {
/* 53 */     this(tokenKind, startPos, endPos);
/* 54 */     this.data = new String(tokenData);
/*    */   }
/*    */ 
/*    */   
/*    */   public TokenKind getKind() {
/* 59 */     return this.kind;
/*    */   }
/*    */   
/*    */   public boolean isIdentifier() {
/* 63 */     return (this.kind == TokenKind.IDENTIFIER);
/*    */   }
/*    */   
/*    */   public boolean isNumericRelationalOperator() {
/* 67 */     return (this.kind == TokenKind.GT || this.kind == TokenKind.GE || this.kind == TokenKind.LT || this.kind == TokenKind.LE || this.kind == TokenKind.EQ || this.kind == TokenKind.NE);
/*    */   }
/*    */ 
/*    */   
/*    */   public String stringValue() {
/* 72 */     return (this.data != null) ? this.data : "";
/*    */   }
/*    */   
/*    */   public Token asInstanceOfToken() {
/* 76 */     return new Token(TokenKind.INSTANCEOF, this.startPos, this.endPos);
/*    */   }
/*    */   
/*    */   public Token asMatchesToken() {
/* 80 */     return new Token(TokenKind.MATCHES, this.startPos, this.endPos);
/*    */   }
/*    */   
/*    */   public Token asBetweenToken() {
/* 84 */     return new Token(TokenKind.BETWEEN, this.startPos, this.endPos);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public String toString() {
/* 90 */     StringBuilder s = new StringBuilder();
/* 91 */     s.append('[').append(this.kind.toString());
/* 92 */     if (this.kind.hasPayload()) {
/* 93 */       s.append(':').append(this.data);
/*    */     }
/* 95 */     s.append(']');
/* 96 */     s.append('(').append(this.startPos).append(',').append(this.endPos).append(')');
/* 97 */     return s.toString();
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\springframework\expression\spel\standard\Token.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */