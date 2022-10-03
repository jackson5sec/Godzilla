/*     */ package org.springframework.expression.spel.standard;
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
/*     */ enum TokenKind
/*     */ {
/*  29 */   LITERAL_INT,
/*     */   
/*  31 */   LITERAL_LONG,
/*     */   
/*  33 */   LITERAL_HEXINT,
/*     */   
/*  35 */   LITERAL_HEXLONG,
/*     */   
/*  37 */   LITERAL_STRING,
/*     */   
/*  39 */   LITERAL_REAL,
/*     */   
/*  41 */   LITERAL_REAL_FLOAT,
/*     */   
/*  43 */   LPAREN("("),
/*     */   
/*  45 */   RPAREN(")"),
/*     */   
/*  47 */   COMMA(","),
/*     */   
/*  49 */   IDENTIFIER,
/*     */   
/*  51 */   COLON(":"),
/*     */   
/*  53 */   HASH("#"),
/*     */   
/*  55 */   RSQUARE("]"),
/*     */   
/*  57 */   LSQUARE("["),
/*     */   
/*  59 */   LCURLY("{"),
/*     */   
/*  61 */   RCURLY("}"),
/*     */   
/*  63 */   DOT("."),
/*     */   
/*  65 */   PLUS("+"),
/*     */   
/*  67 */   STAR("*"),
/*     */   
/*  69 */   MINUS("-"),
/*     */   
/*  71 */   SELECT_FIRST("^["),
/*     */   
/*  73 */   SELECT_LAST("$["),
/*     */   
/*  75 */   QMARK("?"),
/*     */   
/*  77 */   PROJECT("!["),
/*     */   
/*  79 */   DIV("/"),
/*     */   
/*  81 */   GE(">="),
/*     */   
/*  83 */   GT(">"),
/*     */   
/*  85 */   LE("<="),
/*     */   
/*  87 */   LT("<"),
/*     */   
/*  89 */   EQ("=="),
/*     */   
/*  91 */   NE("!="),
/*     */   
/*  93 */   MOD("%"),
/*     */   
/*  95 */   NOT("!"),
/*     */   
/*  97 */   ASSIGN("="),
/*     */   
/*  99 */   INSTANCEOF("instanceof"),
/*     */   
/* 101 */   MATCHES("matches"),
/*     */   
/* 103 */   BETWEEN("between"),
/*     */   
/* 105 */   SELECT("?["),
/*     */   
/* 107 */   POWER("^"),
/*     */   
/* 109 */   ELVIS("?:"),
/*     */   
/* 111 */   SAFE_NAVI("?."),
/*     */   
/* 113 */   BEAN_REF("@"),
/*     */   
/* 115 */   FACTORY_BEAN_REF("&"),
/*     */   
/* 117 */   SYMBOLIC_OR("||"),
/*     */   
/* 119 */   SYMBOLIC_AND("&&"),
/*     */   
/* 121 */   INC("++"),
/*     */   
/* 123 */   DEC("--");
/*     */ 
/*     */   
/*     */   final char[] tokenChars;
/*     */   
/*     */   private final boolean hasPayload;
/*     */ 
/*     */   
/*     */   TokenKind(String tokenString) {
/* 132 */     this.tokenChars = tokenString.toCharArray();
/* 133 */     this.hasPayload = (this.tokenChars.length == 0);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String toString() {
/* 143 */     return name() + ((this.tokenChars.length != 0) ? ("(" + new String(this.tokenChars) + ")") : "");
/*     */   }
/*     */   
/*     */   public boolean hasPayload() {
/* 147 */     return this.hasPayload;
/*     */   }
/*     */   
/*     */   public int getLength() {
/* 151 */     return this.tokenChars.length;
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\springframework\expression\spel\standard\TokenKind.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */