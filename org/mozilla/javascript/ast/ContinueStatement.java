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
/*     */ public class ContinueStatement
/*     */   extends Jump
/*     */ {
/*     */   private Name label;
/*     */   private Loop target;
/*     */   
/*     */   public ContinueStatement() {}
/*     */   
/*     */   public ContinueStatement(int pos) {
/*  31 */     this(pos, -1);
/*     */   }
/*     */ 
/*     */   
/*     */   public ContinueStatement(int pos, int len) {
/*  36 */     this.position = pos;
/*  37 */     this.length = len;
/*     */   }
/*     */   
/*     */   public ContinueStatement(Name label) {
/*  41 */     setLabel(label);
/*     */   }
/*     */   
/*     */   public ContinueStatement(int pos, Name label) {
/*  45 */     this(pos);
/*  46 */     setLabel(label);
/*     */   }
/*     */   
/*     */   public ContinueStatement(int pos, int len, Name label) {
/*  50 */     this(pos, len);
/*  51 */     setLabel(label);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Loop getTarget() {
/*  58 */     return this.target;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setTarget(Loop target) {
/*  68 */     assertNotNull(target);
/*  69 */     this.target = target;
/*  70 */     setJumpStatement(target);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Name getLabel() {
/*  79 */     return this.label;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setLabel(Name label) {
/*  88 */     this.label = label;
/*  89 */     if (label != null) {
/*  90 */       label.setParent(this);
/*     */     }
/*     */   }
/*     */   
/*     */   public String toSource(int depth) {
/*  95 */     StringBuilder sb = new StringBuilder();
/*  96 */     sb.append(makeIndent(depth));
/*  97 */     sb.append("continue");
/*  98 */     if (this.label != null) {
/*  99 */       sb.append(" ");
/* 100 */       sb.append(this.label.toSource(0));
/*     */     } 
/* 102 */     sb.append(";\n");
/* 103 */     return sb.toString();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void visit(NodeVisitor v) {
/* 111 */     if (v.visit(this) && this.label != null)
/* 112 */       this.label.visit(v); 
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\mozilla\javascript\ast\ContinueStatement.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */