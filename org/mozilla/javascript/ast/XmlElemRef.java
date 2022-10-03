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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class XmlElemRef
/*     */   extends XmlRef
/*     */ {
/*     */   private AstNode indexExpr;
/*  37 */   private int lb = -1;
/*  38 */   private int rb = -1;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public XmlElemRef() {}
/*     */ 
/*     */ 
/*     */   
/*     */   public XmlElemRef(int pos) {
/*  48 */     super(pos);
/*     */   }
/*     */   
/*     */   public XmlElemRef(int pos, int len) {
/*  52 */     super(pos, len);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public AstNode getExpression() {
/*  60 */     return this.indexExpr;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setExpression(AstNode expr) {
/*  68 */     assertNotNull(expr);
/*  69 */     this.indexExpr = expr;
/*  70 */     expr.setParent(this);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getLb() {
/*  77 */     return this.lb;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setLb(int lb) {
/*  84 */     this.lb = lb;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getRb() {
/*  91 */     return this.rb;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setRb(int rb) {
/*  98 */     this.rb = rb;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setBrackets(int lb, int rb) {
/* 105 */     this.lb = lb;
/* 106 */     this.rb = rb;
/*     */   }
/*     */ 
/*     */   
/*     */   public String toSource(int depth) {
/* 111 */     StringBuilder sb = new StringBuilder();
/* 112 */     sb.append(makeIndent(depth));
/* 113 */     if (isAttributeAccess()) {
/* 114 */       sb.append("@");
/*     */     }
/* 116 */     if (this.namespace != null) {
/* 117 */       sb.append(this.namespace.toSource(0));
/* 118 */       sb.append("::");
/*     */     } 
/* 120 */     sb.append("[");
/* 121 */     sb.append(this.indexExpr.toSource(0));
/* 122 */     sb.append("]");
/* 123 */     return sb.toString();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void visit(NodeVisitor v) {
/* 132 */     if (v.visit(this)) {
/* 133 */       if (this.namespace != null) {
/* 134 */         this.namespace.visit(v);
/*     */       }
/* 136 */       this.indexExpr.visit(v);
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\mozilla\javascript\ast\XmlElemRef.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */