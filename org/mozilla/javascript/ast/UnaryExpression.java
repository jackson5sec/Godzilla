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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class UnaryExpression
/*     */   extends AstNode
/*     */ {
/*     */   private AstNode operand;
/*     */   private boolean isPostfix;
/*     */   
/*     */   public UnaryExpression() {}
/*     */   
/*     */   public UnaryExpression(int pos) {
/*  32 */     super(pos);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public UnaryExpression(int pos, int len) {
/*  39 */     super(pos, len);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public UnaryExpression(int operator, int operatorPosition, AstNode operand) {
/*  47 */     this(operator, operatorPosition, operand, false);
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
/*     */ 
/*     */   
/*     */   public UnaryExpression(int operator, int operatorPosition, AstNode operand, boolean postFix) {
/*  62 */     assertNotNull(operand);
/*  63 */     int beg = postFix ? operand.getPosition() : operatorPosition;
/*     */     
/*  65 */     int end = postFix ? (operatorPosition + 2) : (operand.getPosition() + operand.getLength());
/*     */ 
/*     */     
/*  68 */     setBounds(beg, end);
/*  69 */     setOperator(operator);
/*  70 */     setOperand(operand);
/*  71 */     this.isPostfix = postFix;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getOperator() {
/*  78 */     return this.type;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setOperator(int operator) {
/*  88 */     if (!Token.isValidToken(operator))
/*  89 */       throw new IllegalArgumentException("Invalid token: " + operator); 
/*  90 */     setType(operator);
/*     */   }
/*     */   
/*     */   public AstNode getOperand() {
/*  94 */     return this.operand;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setOperand(AstNode operand) {
/* 102 */     assertNotNull(operand);
/* 103 */     this.operand = operand;
/* 104 */     operand.setParent(this);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isPostfix() {
/* 111 */     return this.isPostfix;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isPrefix() {
/* 118 */     return !this.isPostfix;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setIsPostfix(boolean isPostfix) {
/* 125 */     this.isPostfix = isPostfix;
/*     */   }
/*     */ 
/*     */   
/*     */   public String toSource(int depth) {
/* 130 */     StringBuilder sb = new StringBuilder();
/* 131 */     sb.append(makeIndent(depth));
/* 132 */     int type = getType();
/* 133 */     if (!this.isPostfix) {
/* 134 */       sb.append(operatorToString(type));
/* 135 */       if (type == 32 || type == 31 || type == 126) {
/* 136 */         sb.append(" ");
/*     */       }
/*     */     } 
/* 139 */     sb.append(this.operand.toSource());
/* 140 */     if (this.isPostfix) {
/* 141 */       sb.append(operatorToString(type));
/*     */     }
/* 143 */     return sb.toString();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void visit(NodeVisitor v) {
/* 151 */     if (v.visit(this))
/* 152 */       this.operand.visit(v); 
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\mozilla\javascript\ast\UnaryExpression.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */