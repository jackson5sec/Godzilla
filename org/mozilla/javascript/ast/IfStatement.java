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
/*     */ public class IfStatement
/*     */   extends AstNode
/*     */ {
/*     */   private AstNode condition;
/*     */   private AstNode thenPart;
/*  22 */   private int elsePosition = -1;
/*     */   private AstNode elsePart;
/*  24 */   private int lp = -1;
/*  25 */   private int rp = -1;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public IfStatement() {}
/*     */ 
/*     */ 
/*     */   
/*     */   public IfStatement(int pos) {
/*  35 */     super(pos);
/*     */   }
/*     */   
/*     */   public IfStatement(int pos, int len) {
/*  39 */     super(pos, len);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public AstNode getCondition() {
/*  46 */     return this.condition;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setCondition(AstNode condition) {
/*  54 */     assertNotNull(condition);
/*  55 */     this.condition = condition;
/*  56 */     condition.setParent(this);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public AstNode getThenPart() {
/*  63 */     return this.thenPart;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setThenPart(AstNode thenPart) {
/*  71 */     assertNotNull(thenPart);
/*  72 */     this.thenPart = thenPart;
/*  73 */     thenPart.setParent(this);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public AstNode getElsePart() {
/*  80 */     return this.elsePart;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setElsePart(AstNode elsePart) {
/*  89 */     this.elsePart = elsePart;
/*  90 */     if (elsePart != null) {
/*  91 */       elsePart.setParent(this);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public int getElsePosition() {
/*  98 */     return this.elsePosition;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setElsePosition(int elsePosition) {
/* 105 */     this.elsePosition = elsePosition;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getLp() {
/* 112 */     return this.lp;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setLp(int lp) {
/* 119 */     this.lp = lp;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getRp() {
/* 126 */     return this.rp;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setRp(int rp) {
/* 133 */     this.rp = rp;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setParens(int lp, int rp) {
/* 140 */     this.lp = lp;
/* 141 */     this.rp = rp;
/*     */   }
/*     */ 
/*     */   
/*     */   public String toSource(int depth) {
/* 146 */     String pad = makeIndent(depth);
/* 147 */     StringBuilder sb = new StringBuilder(32);
/* 148 */     sb.append(pad);
/* 149 */     sb.append("if (");
/* 150 */     sb.append(this.condition.toSource(0));
/* 151 */     sb.append(") ");
/* 152 */     if (this.thenPart.getType() != 129) {
/* 153 */       sb.append("\n").append(makeIndent(depth + 1));
/*     */     }
/* 155 */     sb.append(this.thenPart.toSource(depth).trim());
/* 156 */     if (this.elsePart != null) {
/* 157 */       if (this.thenPart.getType() != 129) {
/* 158 */         sb.append("\n").append(pad).append("else ");
/*     */       } else {
/* 160 */         sb.append(" else ");
/*     */       } 
/* 162 */       if (this.elsePart.getType() != 129 && this.elsePart.getType() != 112)
/*     */       {
/* 164 */         sb.append("\n").append(makeIndent(depth + 1));
/*     */       }
/* 166 */       sb.append(this.elsePart.toSource(depth).trim());
/*     */     } 
/* 168 */     sb.append("\n");
/* 169 */     return sb.toString();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void visit(NodeVisitor v) {
/* 178 */     if (v.visit(this)) {
/* 179 */       this.condition.visit(v);
/* 180 */       this.thenPart.visit(v);
/* 181 */       if (this.elsePart != null)
/* 182 */         this.elsePart.visit(v); 
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\mozilla\javascript\ast\IfStatement.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */