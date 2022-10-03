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
/*     */ public class ForInLoop
/*     */   extends Loop
/*     */ {
/*     */   protected AstNode iterator;
/*     */   protected AstNode iteratedObject;
/*  21 */   protected int inPosition = -1;
/*  22 */   protected int eachPosition = -1;
/*     */ 
/*     */   
/*     */   protected boolean isForEach;
/*     */ 
/*     */ 
/*     */   
/*     */   public ForInLoop() {}
/*     */ 
/*     */   
/*     */   public ForInLoop(int pos) {
/*  33 */     super(pos);
/*     */   }
/*     */   
/*     */   public ForInLoop(int pos, int len) {
/*  37 */     super(pos, len);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public AstNode getIterator() {
/*  44 */     return this.iterator;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setIterator(AstNode iterator) {
/*  53 */     assertNotNull(iterator);
/*  54 */     this.iterator = iterator;
/*  55 */     iterator.setParent(this);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public AstNode getIteratedObject() {
/*  62 */     return this.iteratedObject;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setIteratedObject(AstNode object) {
/*  70 */     assertNotNull(object);
/*  71 */     this.iteratedObject = object;
/*  72 */     object.setParent(this);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isForEach() {
/*  79 */     return this.isForEach;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setIsForEach(boolean isForEach) {
/*  86 */     this.isForEach = isForEach;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getInPosition() {
/*  93 */     return this.inPosition;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setInPosition(int inPosition) {
/* 102 */     this.inPosition = inPosition;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getEachPosition() {
/* 109 */     return this.eachPosition;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setEachPosition(int eachPosition) {
/* 118 */     this.eachPosition = eachPosition;
/*     */   }
/*     */ 
/*     */   
/*     */   public String toSource(int depth) {
/* 123 */     StringBuilder sb = new StringBuilder();
/* 124 */     sb.append(makeIndent(depth));
/* 125 */     sb.append("for ");
/* 126 */     if (isForEach()) {
/* 127 */       sb.append("each ");
/*     */     }
/* 129 */     sb.append("(");
/* 130 */     sb.append(this.iterator.toSource(0));
/* 131 */     sb.append(" in ");
/* 132 */     sb.append(this.iteratedObject.toSource(0));
/* 133 */     sb.append(") ");
/* 134 */     if (this.body.getType() == 129) {
/* 135 */       sb.append(this.body.toSource(depth).trim()).append("\n");
/*     */     } else {
/* 137 */       sb.append("\n").append(this.body.toSource(depth + 1));
/*     */     } 
/* 139 */     return sb.toString();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void visit(NodeVisitor v) {
/* 147 */     if (v.visit(this)) {
/* 148 */       this.iterator.visit(v);
/* 149 */       this.iteratedObject.visit(v);
/* 150 */       this.body.visit(v);
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\mozilla\javascript\ast\ForInLoop.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */