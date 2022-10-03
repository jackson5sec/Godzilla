/*    */ package org.mozilla.javascript.ast;
/*    */ 
/*    */ import org.mozilla.javascript.ScriptRuntime;
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
/*    */ public class StringLiteral
/*    */   extends AstNode
/*    */ {
/*    */   private String value;
/*    */   private char quoteChar;
/*    */   
/*    */   public StringLiteral() {}
/*    */   
/*    */   public StringLiteral(int pos) {
/* 29 */     super(pos);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public StringLiteral(int pos, int len) {
/* 37 */     super(pos, len);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public String getValue() {
/* 46 */     return this.value;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public String getValue(boolean includeQuotes) {
/* 53 */     if (!includeQuotes)
/* 54 */       return this.value; 
/* 55 */     return this.quoteChar + this.value + this.quoteChar;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void setValue(String value) {
/* 64 */     assertNotNull(value);
/* 65 */     this.value = value;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public char getQuoteCharacter() {
/* 72 */     return this.quoteChar;
/*    */   }
/*    */   
/*    */   public void setQuoteCharacter(char c) {
/* 76 */     this.quoteChar = c;
/*    */   }
/*    */ 
/*    */   
/*    */   public String toSource(int depth) {
/* 81 */     return makeIndent(depth) + this.quoteChar + ScriptRuntime.escapeString(this.value, this.quoteChar) + this.quoteChar;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void visit(NodeVisitor v) {
/* 93 */     v.visit(this);
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\mozilla\javascript\ast\StringLiteral.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */