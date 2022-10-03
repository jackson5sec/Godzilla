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
/*     */ public class WithStatement
/*     */   extends AstNode
/*     */ {
/*     */   private AstNode expression;
/*     */   private AstNode statement;
/*  21 */   private int lp = -1;
/*  22 */   private int rp = -1;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public WithStatement() {}
/*     */ 
/*     */ 
/*     */   
/*     */   public WithStatement(int pos) {
/*  32 */     super(pos);
/*     */   }
/*     */   
/*     */   public WithStatement(int pos, int len) {
/*  36 */     super(pos, len);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public AstNode getExpression() {
/*  43 */     return this.expression;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setExpression(AstNode expression) {
/*  51 */     assertNotNull(expression);
/*  52 */     this.expression = expression;
/*  53 */     expression.setParent(this);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public AstNode getStatement() {
/*  60 */     return this.statement;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setStatement(AstNode statement) {
/*  68 */     assertNotNull(statement);
/*  69 */     this.statement = statement;
/*  70 */     statement.setParent(this);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getLp() {
/*  77 */     return this.lp;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setLp(int lp) {
/*  84 */     this.lp = lp;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getRp() {
/*  91 */     return this.rp;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setRp(int rp) {
/*  98 */     this.rp = rp;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setParens(int lp, int rp) {
/* 105 */     this.lp = lp;
/* 106 */     this.rp = rp;
/*     */   }
/*     */ 
/*     */   
/*     */   public String toSource(int depth) {
/* 111 */     StringBuilder sb = new StringBuilder();
/* 112 */     sb.append(makeIndent(depth));
/* 113 */     sb.append("with (");
/* 114 */     sb.append(this.expression.toSource(0));
/* 115 */     sb.append(") ");
/* 116 */     if (this.statement.getType() == 129) {
/* 117 */       sb.append(this.statement.toSource(depth).trim());
/* 118 */       sb.append("\n");
/*     */     } else {
/* 120 */       sb.append("\n").append(this.statement.toSource(depth + 1));
/*     */     } 
/* 122 */     return sb.toString();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void visit(NodeVisitor v) {
/* 130 */     if (v.visit(this)) {
/* 131 */       this.expression.visit(v);
/* 132 */       this.statement.visit(v);
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\mozilla\javascript\ast\WithStatement.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */