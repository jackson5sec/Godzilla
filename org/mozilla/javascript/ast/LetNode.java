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
/*     */ public class LetNode
/*     */   extends Scope
/*     */ {
/*     */   private VariableDeclaration variables;
/*     */   private AstNode body;
/*  29 */   private int lp = -1;
/*  30 */   private int rp = -1;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public LetNode() {}
/*     */ 
/*     */ 
/*     */   
/*     */   public LetNode(int pos) {
/*  40 */     super(pos);
/*     */   }
/*     */   
/*     */   public LetNode(int pos, int len) {
/*  44 */     super(pos, len);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public VariableDeclaration getVariables() {
/*  51 */     return this.variables;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setVariables(VariableDeclaration variables) {
/*  59 */     assertNotNull(variables);
/*  60 */     this.variables = variables;
/*  61 */     variables.setParent(this);
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
/*     */   public AstNode getBody() {
/*  73 */     return this.body;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setBody(AstNode body) {
/*  83 */     this.body = body;
/*  84 */     if (body != null) {
/*  85 */       body.setParent(this);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public int getLp() {
/*  92 */     return this.lp;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setLp(int lp) {
/*  99 */     this.lp = lp;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getRp() {
/* 106 */     return this.rp;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setRp(int rp) {
/* 113 */     this.rp = rp;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setParens(int lp, int rp) {
/* 120 */     this.lp = lp;
/* 121 */     this.rp = rp;
/*     */   }
/*     */ 
/*     */   
/*     */   public String toSource(int depth) {
/* 126 */     String pad = makeIndent(depth);
/* 127 */     StringBuilder sb = new StringBuilder();
/* 128 */     sb.append(pad);
/* 129 */     sb.append("let (");
/* 130 */     printList(this.variables.getVariables(), sb);
/* 131 */     sb.append(") ");
/* 132 */     if (this.body != null) {
/* 133 */       sb.append(this.body.toSource(depth));
/*     */     }
/* 135 */     return sb.toString();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void visit(NodeVisitor v) {
/* 144 */     if (v.visit(this)) {
/* 145 */       this.variables.visit(v);
/* 146 */       if (this.body != null)
/* 147 */         this.body.visit(v); 
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\mozilla\javascript\ast\LetNode.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */