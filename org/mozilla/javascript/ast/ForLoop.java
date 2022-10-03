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
/*     */ public class ForLoop
/*     */   extends Loop
/*     */ {
/*     */   private AstNode initializer;
/*     */   private AstNode condition;
/*     */   private AstNode increment;
/*     */   
/*     */   public ForLoop() {}
/*     */   
/*     */   public ForLoop(int pos) {
/*  32 */     super(pos);
/*     */   }
/*     */   
/*     */   public ForLoop(int pos, int len) {
/*  36 */     super(pos, len);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public AstNode getInitializer() {
/*  46 */     return this.initializer;
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
/*     */   public void setInitializer(AstNode initializer) {
/*  58 */     assertNotNull(initializer);
/*  59 */     this.initializer = initializer;
/*  60 */     initializer.setParent(this);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public AstNode getCondition() {
/*  67 */     return this.condition;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setCondition(AstNode condition) {
/*  77 */     assertNotNull(condition);
/*  78 */     this.condition = condition;
/*  79 */     condition.setParent(this);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public AstNode getIncrement() {
/*  86 */     return this.increment;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setIncrement(AstNode increment) {
/*  97 */     assertNotNull(increment);
/*  98 */     this.increment = increment;
/*  99 */     increment.setParent(this);
/*     */   }
/*     */ 
/*     */   
/*     */   public String toSource(int depth) {
/* 104 */     StringBuilder sb = new StringBuilder();
/* 105 */     sb.append(makeIndent(depth));
/* 106 */     sb.append("for (");
/* 107 */     sb.append(this.initializer.toSource(0));
/* 108 */     sb.append("; ");
/* 109 */     sb.append(this.condition.toSource(0));
/* 110 */     sb.append("; ");
/* 111 */     sb.append(this.increment.toSource(0));
/* 112 */     sb.append(") ");
/* 113 */     if (this.body.getType() == 129) {
/* 114 */       sb.append(this.body.toSource(depth).trim()).append("\n");
/*     */     } else {
/* 116 */       sb.append("\n").append(this.body.toSource(depth + 1));
/*     */     } 
/* 118 */     return sb.toString();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void visit(NodeVisitor v) {
/* 127 */     if (v.visit(this)) {
/* 128 */       this.initializer.visit(v);
/* 129 */       this.condition.visit(v);
/* 130 */       this.increment.visit(v);
/* 131 */       this.body.visit(v);
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\mozilla\javascript\ast\ForLoop.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */