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
/*    */ public class ParenthesizedExpression
/*    */   extends AstNode
/*    */ {
/*    */   private AstNode expression;
/*    */   
/*    */   public ParenthesizedExpression() {}
/*    */   
/*    */   public ParenthesizedExpression(int pos) {
/* 27 */     super(pos);
/*    */   }
/*    */   
/*    */   public ParenthesizedExpression(int pos, int len) {
/* 31 */     super(pos, len);
/*    */   }
/*    */   
/*    */   public ParenthesizedExpression(AstNode expr) {
/* 35 */     this((expr != null) ? expr.getPosition() : 0, (expr != null) ? expr.getLength() : 1, expr);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public ParenthesizedExpression(int pos, int len, AstNode expr) {
/* 41 */     super(pos, len);
/* 42 */     setExpression(expr);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public AstNode getExpression() {
/* 49 */     return this.expression;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void setExpression(AstNode expression) {
/* 59 */     assertNotNull(expression);
/* 60 */     this.expression = expression;
/* 61 */     expression.setParent(this);
/*    */   }
/*    */ 
/*    */   
/*    */   public String toSource(int depth) {
/* 66 */     return makeIndent(depth) + "(" + this.expression.toSource(0) + ")";
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void visit(NodeVisitor v) {
/* 74 */     if (v.visit(this))
/* 75 */       this.expression.visit(v); 
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\mozilla\javascript\ast\ParenthesizedExpression.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */