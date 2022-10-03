/*     */ package org.mozilla.javascript.ast;
/*     */ 
/*     */ import org.mozilla.javascript.Token;
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
/*     */ public class InfixExpression
/*     */   extends AstNode
/*     */ {
/*     */   protected AstNode left;
/*     */   protected AstNode right;
/*  19 */   protected int operatorPosition = -1;
/*     */ 
/*     */   
/*     */   public InfixExpression() {}
/*     */   
/*     */   public InfixExpression(int pos) {
/*  25 */     super(pos);
/*     */   }
/*     */   
/*     */   public InfixExpression(int pos, int len) {
/*  29 */     super(pos, len);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public InfixExpression(int pos, int len, AstNode left, AstNode right) {
/*  35 */     super(pos, len);
/*  36 */     setLeft(left);
/*  37 */     setRight(right);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public InfixExpression(AstNode left, AstNode right) {
/*  45 */     setLeftAndRight(left, right);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public InfixExpression(int operator, AstNode left, AstNode right, int operatorPos) {
/*  54 */     setType(operator);
/*  55 */     setOperatorPosition(operatorPos - left.getPosition());
/*  56 */     setLeftAndRight(left, right);
/*     */   }
/*     */   
/*     */   public void setLeftAndRight(AstNode left, AstNode right) {
/*  60 */     assertNotNull(left);
/*  61 */     assertNotNull(right);
/*     */     
/*  63 */     int beg = left.getPosition();
/*  64 */     int end = right.getPosition() + right.getLength();
/*  65 */     setBounds(beg, end);
/*     */ 
/*     */     
/*  68 */     setLeft(left);
/*  69 */     setRight(right);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getOperator() {
/*  76 */     return getType();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setOperator(int operator) {
/*  86 */     if (!Token.isValidToken(operator))
/*  87 */       throw new IllegalArgumentException("Invalid token: " + operator); 
/*  88 */     setType(operator);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public AstNode getLeft() {
/*  95 */     return this.left;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setLeft(AstNode left) {
/* 105 */     assertNotNull(left);
/* 106 */     this.left = left;
/*     */     
/* 108 */     setLineno(left.getLineno());
/* 109 */     left.setParent(this);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public AstNode getRight() {
/* 119 */     return this.right;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setRight(AstNode right) {
/* 128 */     assertNotNull(right);
/* 129 */     this.right = right;
/* 130 */     right.setParent(this);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getOperatorPosition() {
/* 137 */     return this.operatorPosition;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setOperatorPosition(int operatorPosition) {
/* 145 */     this.operatorPosition = operatorPosition;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean hasSideEffects() {
/* 151 */     switch (getType()) {
/*     */       case 89:
/* 153 */         return (this.right != null && this.right.hasSideEffects());
/*     */       case 104:
/*     */       case 105:
/* 156 */         return ((this.left != null && this.left.hasSideEffects()) || (this.right != null && this.right.hasSideEffects()));
/*     */     } 
/*     */     
/* 159 */     return super.hasSideEffects();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String toSource(int depth) {
/* 165 */     StringBuilder sb = new StringBuilder();
/* 166 */     sb.append(makeIndent(depth));
/* 167 */     sb.append(this.left.toSource());
/* 168 */     sb.append(" ");
/* 169 */     sb.append(operatorToString(getType()));
/* 170 */     sb.append(" ");
/* 171 */     sb.append(this.right.toSource());
/* 172 */     return sb.toString();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void visit(NodeVisitor v) {
/* 180 */     if (v.visit(this)) {
/* 181 */       this.left.visit(v);
/* 182 */       this.right.visit(v);
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\mozilla\javascript\ast\InfixExpression.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */