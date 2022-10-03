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
/*     */ public class CatchClause
/*     */   extends AstNode
/*     */ {
/*     */   private Name varName;
/*     */   private AstNode catchCondition;
/*     */   private Block body;
/*  23 */   private int ifPosition = -1;
/*  24 */   private int lp = -1;
/*  25 */   private int rp = -1;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public CatchClause() {}
/*     */ 
/*     */ 
/*     */   
/*     */   public CatchClause(int pos) {
/*  35 */     super(pos);
/*     */   }
/*     */   
/*     */   public CatchClause(int pos, int len) {
/*  39 */     super(pos, len);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Name getVarName() {
/*  47 */     return this.varName;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setVarName(Name varName) {
/*  56 */     assertNotNull(varName);
/*  57 */     this.varName = varName;
/*  58 */     varName.setParent(this);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public AstNode getCatchCondition() {
/*  66 */     return this.catchCondition;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setCatchCondition(AstNode catchCondition) {
/*  74 */     this.catchCondition = catchCondition;
/*  75 */     if (catchCondition != null) {
/*  76 */       catchCondition.setParent(this);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Block getBody() {
/*  83 */     return this.body;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setBody(Block body) {
/*  91 */     assertNotNull(body);
/*  92 */     this.body = body;
/*  93 */     body.setParent(this);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getLp() {
/* 100 */     return this.lp;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setLp(int lp) {
/* 107 */     this.lp = lp;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getRp() {
/* 114 */     return this.rp;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setRp(int rp) {
/* 121 */     this.rp = rp;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setParens(int lp, int rp) {
/* 128 */     this.lp = lp;
/* 129 */     this.rp = rp;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getIfPosition() {
/* 137 */     return this.ifPosition;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setIfPosition(int ifPosition) {
/* 145 */     this.ifPosition = ifPosition;
/*     */   }
/*     */ 
/*     */   
/*     */   public String toSource(int depth) {
/* 150 */     StringBuilder sb = new StringBuilder();
/* 151 */     sb.append(makeIndent(depth));
/* 152 */     sb.append("catch (");
/* 153 */     sb.append(this.varName.toSource(0));
/* 154 */     if (this.catchCondition != null) {
/* 155 */       sb.append(" if ");
/* 156 */       sb.append(this.catchCondition.toSource(0));
/*     */     } 
/* 158 */     sb.append(") ");
/* 159 */     sb.append(this.body.toSource(0));
/* 160 */     return sb.toString();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void visit(NodeVisitor v) {
/* 169 */     if (v.visit(this)) {
/* 170 */       this.varName.visit(v);
/* 171 */       if (this.catchCondition != null) {
/* 172 */         this.catchCondition.visit(v);
/*     */       }
/* 174 */       this.body.visit(v);
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\mozilla\javascript\ast\CatchClause.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */