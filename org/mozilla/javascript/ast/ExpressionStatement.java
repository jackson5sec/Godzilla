/*     */ package org.mozilla.javascript.ast;
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
/*     */ public class ExpressionStatement
/*     */   extends AstNode
/*     */ {
/*     */   private AstNode expr;
/*     */   
/*     */   public void setHasResult() {
/*  29 */     this.type = 134;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ExpressionStatement() {}
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ExpressionStatement(AstNode expr, boolean hasResult) {
/*  46 */     this(expr);
/*  47 */     if (hasResult) setHasResult();
/*     */   
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ExpressionStatement(AstNode expr) {
/*  59 */     this(expr.getPosition(), expr.getLength(), expr);
/*     */   }
/*     */   
/*     */   public ExpressionStatement(int pos, int len) {
/*  63 */     super(pos, len);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ExpressionStatement(int pos, int len, AstNode expr) {
/*  74 */     super(pos, len);
/*  75 */     setExpression(expr);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public AstNode getExpression() {
/*  82 */     return this.expr;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setExpression(AstNode expression) {
/*  90 */     assertNotNull(expression);
/*  91 */     this.expr = expression;
/*  92 */     expression.setParent(this);
/*  93 */     setLineno(expression.getLineno());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean hasSideEffects() {
/* 103 */     return (this.type == 134 || this.expr.hasSideEffects());
/*     */   }
/*     */ 
/*     */   
/*     */   public String toSource(int depth) {
/* 108 */     StringBuilder sb = new StringBuilder();
/* 109 */     sb.append(this.expr.toSource(depth));
/* 110 */     sb.append(";\n");
/* 111 */     return sb.toString();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void visit(NodeVisitor v) {
/* 119 */     if (v.visit(this))
/* 120 */       this.expr.visit(v); 
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\mozilla\javascript\ast\ExpressionStatement.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */