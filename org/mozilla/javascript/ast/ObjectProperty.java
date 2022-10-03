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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ObjectProperty
/*     */   extends InfixExpression
/*     */ {
/*     */   public void setNodeType(int nodeType) {
/*  45 */     if (nodeType != 103 && nodeType != 151 && nodeType != 152)
/*     */     {
/*     */       
/*  48 */       throw new IllegalArgumentException("invalid node type: " + nodeType);
/*     */     }
/*  50 */     setType(nodeType);
/*     */   }
/*     */ 
/*     */   
/*     */   public ObjectProperty() {}
/*     */   
/*     */   public ObjectProperty(int pos) {
/*  57 */     super(pos);
/*     */   }
/*     */   
/*     */   public ObjectProperty(int pos, int len) {
/*  61 */     super(pos, len);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setIsGetter() {
/*  68 */     this.type = 151;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isGetter() {
/*  75 */     return (this.type == 151);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setIsSetter() {
/*  82 */     this.type = 152;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isSetter() {
/*  89 */     return (this.type == 152);
/*     */   }
/*     */ 
/*     */   
/*     */   public String toSource(int depth) {
/*  94 */     StringBuilder sb = new StringBuilder();
/*  95 */     sb.append("\n");
/*  96 */     sb.append(makeIndent(depth + 1));
/*  97 */     if (isGetter()) {
/*  98 */       sb.append("get ");
/*  99 */     } else if (isSetter()) {
/* 100 */       sb.append("set ");
/*     */     } 
/* 102 */     sb.append(this.left.toSource((getType() == 103) ? 0 : depth));
/* 103 */     if (this.type == 103) {
/* 104 */       sb.append(": ");
/*     */     }
/* 106 */     sb.append(this.right.toSource((getType() == 103) ? 0 : (depth + 1)));
/* 107 */     return sb.toString();
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\mozilla\javascript\ast\ObjectProperty.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */