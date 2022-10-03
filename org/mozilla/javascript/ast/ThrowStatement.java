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
/*    */ 
/*    */ 
/*    */ 
/*    */ public class ThrowStatement
/*    */   extends AstNode
/*    */ {
/*    */   private AstNode expression;
/*    */   
/*    */   public ThrowStatement() {}
/*    */   
/*    */   public ThrowStatement(int pos) {
/* 29 */     super(pos);
/*    */   }
/*    */   
/*    */   public ThrowStatement(int pos, int len) {
/* 33 */     super(pos, len);
/*    */   }
/*    */   
/*    */   public ThrowStatement(AstNode expr) {
/* 37 */     setExpression(expr);
/*    */   }
/*    */   
/*    */   public ThrowStatement(int pos, AstNode expr) {
/* 41 */     super(pos, expr.getLength());
/* 42 */     setExpression(expr);
/*    */   }
/*    */   
/*    */   public ThrowStatement(int pos, int len, AstNode expr) {
/* 46 */     super(pos, len);
/* 47 */     setExpression(expr);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public AstNode getExpression() {
/* 54 */     return this.expression;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void setExpression(AstNode expression) {
/* 63 */     assertNotNull(expression);
/* 64 */     this.expression = expression;
/* 65 */     expression.setParent(this);
/*    */   }
/*    */ 
/*    */   
/*    */   public String toSource(int depth) {
/* 70 */     StringBuilder sb = new StringBuilder();
/* 71 */     sb.append(makeIndent(depth));
/* 72 */     sb.append("throw");
/* 73 */     sb.append(" ");
/* 74 */     sb.append(this.expression.toSource(0));
/* 75 */     sb.append(";\n");
/* 76 */     return sb.toString();
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void visit(NodeVisitor v) {
/* 84 */     if (v.visit(this))
/* 85 */       this.expression.visit(v); 
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\mozilla\javascript\ast\ThrowStatement.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */