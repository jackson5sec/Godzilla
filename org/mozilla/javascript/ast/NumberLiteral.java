/*    */ package org.mozilla.javascript.ast;
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
/*    */ public class NumberLiteral
/*    */   extends AstNode
/*    */ {
/*    */   private String value;
/*    */   private double number;
/*    */   
/*    */   public NumberLiteral() {}
/*    */   
/*    */   public NumberLiteral(int pos) {
/* 27 */     super(pos);
/*    */   }
/*    */   
/*    */   public NumberLiteral(int pos, int len) {
/* 31 */     super(pos, len);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public NumberLiteral(int pos, String value) {
/* 38 */     super(pos);
/* 39 */     setValue(value);
/* 40 */     setLength(value.length());
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public NumberLiteral(int pos, String value, double number) {
/* 47 */     this(pos, value);
/* 48 */     setDouble(number);
/*    */   }
/*    */   
/*    */   public NumberLiteral(double number) {
/* 52 */     setDouble(number);
/* 53 */     setValue(Double.toString(number));
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public String getValue() {
/* 60 */     return this.value;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void setValue(String value) {
/* 68 */     assertNotNull(value);
/* 69 */     this.value = value;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public double getNumber() {
/* 76 */     return this.number;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void setNumber(double value) {
/* 83 */     this.number = value;
/*    */   }
/*    */ 
/*    */   
/*    */   public String toSource(int depth) {
/* 88 */     return makeIndent(depth) + ((this.value == null) ? "<null>" : this.value);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void visit(NodeVisitor v) {
/* 96 */     v.visit(this);
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\mozilla\javascript\ast\NumberLiteral.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */