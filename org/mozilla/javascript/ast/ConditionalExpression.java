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
/*     */ 
/*     */ public class ConditionalExpression
/*     */   extends AstNode
/*     */ {
/*     */   private AstNode testExpression;
/*     */   private AstNode trueExpression;
/*     */   private AstNode falseExpression;
/*  30 */   private int questionMarkPosition = -1;
/*  31 */   private int colonPosition = -1;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ConditionalExpression() {}
/*     */ 
/*     */ 
/*     */   
/*     */   public ConditionalExpression(int pos) {
/*  41 */     super(pos);
/*     */   }
/*     */   
/*     */   public ConditionalExpression(int pos, int len) {
/*  45 */     super(pos, len);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public AstNode getTestExpression() {
/*  52 */     return this.testExpression;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setTestExpression(AstNode testExpression) {
/*  61 */     assertNotNull(testExpression);
/*  62 */     this.testExpression = testExpression;
/*  63 */     testExpression.setParent(this);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public AstNode getTrueExpression() {
/*  70 */     return this.trueExpression;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setTrueExpression(AstNode trueExpression) {
/*  80 */     assertNotNull(trueExpression);
/*  81 */     this.trueExpression = trueExpression;
/*  82 */     trueExpression.setParent(this);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public AstNode getFalseExpression() {
/*  89 */     return this.falseExpression;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setFalseExpression(AstNode falseExpression) {
/* 100 */     assertNotNull(falseExpression);
/* 101 */     this.falseExpression = falseExpression;
/* 102 */     falseExpression.setParent(this);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getQuestionMarkPosition() {
/* 109 */     return this.questionMarkPosition;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setQuestionMarkPosition(int questionMarkPosition) {
/* 117 */     this.questionMarkPosition = questionMarkPosition;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getColonPosition() {
/* 124 */     return this.colonPosition;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setColonPosition(int colonPosition) {
/* 132 */     this.colonPosition = colonPosition;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean hasSideEffects() {
/* 137 */     if (this.testExpression == null || this.trueExpression == null || this.falseExpression == null)
/*     */     {
/* 139 */       codeBug(); } 
/* 140 */     return (this.trueExpression.hasSideEffects() && this.falseExpression.hasSideEffects());
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String toSource(int depth) {
/* 146 */     StringBuilder sb = new StringBuilder();
/* 147 */     sb.append(makeIndent(depth));
/* 148 */     sb.append(this.testExpression.toSource(depth));
/* 149 */     sb.append(" ? ");
/* 150 */     sb.append(this.trueExpression.toSource(0));
/* 151 */     sb.append(" : ");
/* 152 */     sb.append(this.falseExpression.toSource(0));
/* 153 */     return sb.toString();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void visit(NodeVisitor v) {
/* 162 */     if (v.visit(this)) {
/* 163 */       this.testExpression.visit(v);
/* 164 */       this.trueExpression.visit(v);
/* 165 */       this.falseExpression.visit(v);
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\mozilla\javascript\ast\ConditionalExpression.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */