/*     */ package org.fife.rsta.ac.java.rjc.notices;
/*     */ 
/*     */ import org.fife.rsta.ac.java.rjc.lexer.Token;
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
/*     */ public class ParserNotice
/*     */ {
/*     */   private int line;
/*     */   private int column;
/*     */   private int length;
/*     */   private String message;
/*     */   
/*     */   public ParserNotice(Token t, String msg) {
/*  31 */     this.line = t.getLine();
/*  32 */     this.column = t.getColumn();
/*  33 */     this.length = t.getLexeme().length();
/*  34 */     this.message = msg;
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
/*     */   public ParserNotice(int line, int column, int length, String message) {
/*  47 */     this.line = line;
/*  48 */     this.column = column;
/*  49 */     this.length = length;
/*  50 */     this.message = message;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getColumn() {
/*  61 */     return this.column;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getLength() {
/*  71 */     return this.length;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getLine() {
/*  81 */     return this.line;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getMessage() {
/*  91 */     return this.message;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String toString() {
/* 102 */     return "(" + getLine() + ", " + getColumn() + ": " + getMessage();
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\fife\rsta\ac\java\rjc\notices\ParserNotice.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */