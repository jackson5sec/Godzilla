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
/*     */ public class ElementGet
/*     */   extends AstNode
/*     */ {
/*     */   private AstNode target;
/*     */   private AstNode element;
/*  26 */   private int lb = -1;
/*  27 */   private int rb = -1;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ElementGet() {}
/*     */ 
/*     */ 
/*     */   
/*     */   public ElementGet(int pos) {
/*  37 */     super(pos);
/*     */   }
/*     */   
/*     */   public ElementGet(int pos, int len) {
/*  41 */     super(pos, len);
/*     */   }
/*     */   
/*     */   public ElementGet(AstNode target, AstNode element) {
/*  45 */     setTarget(target);
/*  46 */     setElement(element);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public AstNode getTarget() {
/*  53 */     return this.target;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setTarget(AstNode target) {
/*  63 */     assertNotNull(target);
/*  64 */     this.target = target;
/*  65 */     target.setParent(this);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public AstNode getElement() {
/*  72 */     return this.element;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setElement(AstNode element) {
/*  80 */     assertNotNull(element);
/*  81 */     this.element = element;
/*  82 */     element.setParent(this);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getLb() {
/*  89 */     return this.lb;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setLb(int lb) {
/*  96 */     this.lb = lb;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getRb() {
/* 103 */     return this.rb;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setRb(int rb) {
/* 110 */     this.rb = rb;
/*     */   }
/*     */   
/*     */   public void setParens(int lb, int rb) {
/* 114 */     this.lb = lb;
/* 115 */     this.rb = rb;
/*     */   }
/*     */ 
/*     */   
/*     */   public String toSource(int depth) {
/* 120 */     StringBuilder sb = new StringBuilder();
/* 121 */     sb.append(makeIndent(depth));
/* 122 */     sb.append(this.target.toSource(0));
/* 123 */     sb.append("[");
/* 124 */     sb.append(this.element.toSource(0));
/* 125 */     sb.append("]");
/* 126 */     return sb.toString();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void visit(NodeVisitor v) {
/* 134 */     if (v.visit(this)) {
/* 135 */       this.target.visit(v);
/* 136 */       this.element.visit(v);
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\mozilla\javascript\ast\ElementGet.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */