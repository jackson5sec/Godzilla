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
/*     */ 
/*     */ 
/*     */ 
/*     */ public class VariableInitializer
/*     */   extends AstNode
/*     */ {
/*     */   private AstNode target;
/*     */   private AstNode initializer;
/*     */   
/*     */   public void setNodeType(int nodeType) {
/*  34 */     if (nodeType != 122 && nodeType != 154 && nodeType != 153)
/*     */     {
/*     */       
/*  37 */       throw new IllegalArgumentException("invalid node type"); } 
/*  38 */     setType(nodeType);
/*     */   }
/*     */ 
/*     */   
/*     */   public VariableInitializer() {}
/*     */   
/*     */   public VariableInitializer(int pos) {
/*  45 */     super(pos);
/*     */   }
/*     */   
/*     */   public VariableInitializer(int pos, int len) {
/*  49 */     super(pos, len);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isDestructuring() {
/*  60 */     return !(this.target instanceof Name);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public AstNode getTarget() {
/*  67 */     return this.target;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setTarget(AstNode target) {
/*  78 */     if (target == null)
/*  79 */       throw new IllegalArgumentException("invalid target arg"); 
/*  80 */     this.target = target;
/*  81 */     target.setParent(this);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public AstNode getInitializer() {
/*  88 */     return this.initializer;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setInitializer(AstNode initializer) {
/*  96 */     this.initializer = initializer;
/*  97 */     if (initializer != null) {
/*  98 */       initializer.setParent(this);
/*     */     }
/*     */   }
/*     */   
/*     */   public String toSource(int depth) {
/* 103 */     StringBuilder sb = new StringBuilder();
/* 104 */     sb.append(makeIndent(depth));
/* 105 */     sb.append(this.target.toSource(0));
/* 106 */     if (this.initializer != null) {
/* 107 */       sb.append(" = ");
/* 108 */       sb.append(this.initializer.toSource(0));
/*     */     } 
/* 110 */     return sb.toString();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void visit(NodeVisitor v) {
/* 119 */     if (v.visit(this)) {
/* 120 */       this.target.visit(v);
/* 121 */       if (this.initializer != null)
/* 122 */         this.initializer.visit(v); 
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\mozilla\javascript\ast\VariableInitializer.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */