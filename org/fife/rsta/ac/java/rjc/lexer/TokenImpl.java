/*     */ package org.fife.rsta.ac.java.rjc.lexer;
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
/*     */ class TokenImpl
/*     */   implements Token
/*     */ {
/*     */   private int type;
/*     */   private String lexeme;
/*     */   private int line;
/*     */   private int column;
/*     */   private int offset;
/*     */   private boolean invalid;
/*     */   
/*     */   public TokenImpl(int type, String lexeme, int line, int column, int offs) {
/*  51 */     this(type, lexeme, line, column, offs, false);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public TokenImpl(int type, String lexeme, int line, int column, int offs, boolean invalid) {
/*  57 */     this.type = type;
/*  58 */     this.lexeme = lexeme;
/*  59 */     this.line = line;
/*  60 */     this.column = column;
/*  61 */     this.offset = offs;
/*  62 */     this.invalid = invalid;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean equals(Object obj) {
/*  68 */     if (obj == this) {
/*  69 */       return true;
/*     */     }
/*  71 */     if (obj instanceof Token) {
/*  72 */       Token t2 = (Token)obj;
/*  73 */       return (this.type == t2.getType() && this.lexeme.equals(t2.getLexeme()) && this.line == t2
/*  74 */         .getLine() && this.column == t2.getColumn() && this.invalid == t2
/*  75 */         .isInvalid());
/*     */     } 
/*  77 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public int getColumn() {
/*  83 */     return this.column;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public int getLength() {
/*  89 */     return this.lexeme.length();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String getLexeme() {
/*  95 */     return this.lexeme;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public int getLine() {
/* 101 */     return this.line;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public int getOffset() {
/* 107 */     return this.offset;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public int getType() {
/* 113 */     return this.type;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 119 */     return this.lexeme.hashCode();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isBasicType() {
/* 125 */     switch (getType()) {
/*     */       case 131075:
/*     */       case 131077:
/*     */       case 131080:
/*     */       case 131086:
/*     */       case 131092:
/*     */       case 131099:
/*     */       case 131101:
/*     */       case 131109:
/* 134 */         return true;
/*     */     } 
/* 136 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isIdentifier() {
/* 143 */     return ((getType() & 0x40000) > 0);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isInvalid() {
/* 149 */     return this.invalid;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isOperator() {
/* 155 */     return ((getType() & 0x1000000) > 0);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isType(int type) {
/* 161 */     return (this.type == type);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String toString() {
/* 167 */     return "[TokenImpl: type=" + this.type + "; lexeme=\"" + this.lexeme + "\"; line=" + 
/*     */ 
/*     */       
/* 170 */       getLine() + "; col=" + 
/* 171 */       getColumn() + "; offs=" + 
/* 172 */       getOffset() + "; invalid=" + 
/* 173 */       isInvalid() + "]";
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\fife\rsta\ac\java\rjc\lexer\TokenImpl.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */