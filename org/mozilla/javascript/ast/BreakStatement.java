/*    */ package org.mozilla.javascript.ast;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class BreakStatement
/*    */   extends Jump
/*    */ {
/*    */   private Name breakLabel;
/*    */   private AstNode target;
/*    */   
/*    */   public BreakStatement() {}
/*    */   
/*    */   public BreakStatement(int pos) {
/* 31 */     this.position = pos;
/*    */   }
/*    */   
/*    */   public BreakStatement(int pos, int len) {
/* 35 */     this.position = pos;
/* 36 */     this.length = len;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public Name getBreakLabel() {
/* 45 */     return this.breakLabel;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void setBreakLabel(Name label) {
/* 55 */     this.breakLabel = label;
/* 56 */     if (label != null) {
/* 57 */       label.setParent(this);
/*    */     }
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public AstNode getBreakTarget() {
/* 66 */     return this.target;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void setBreakTarget(Jump target) {
/* 75 */     assertNotNull(target);
/* 76 */     this.target = target;
/* 77 */     setJumpStatement(target);
/*    */   }
/*    */ 
/*    */   
/*    */   public String toSource(int depth) {
/* 82 */     StringBuilder sb = new StringBuilder();
/* 83 */     sb.append(makeIndent(depth));
/* 84 */     sb.append("break");
/* 85 */     if (this.breakLabel != null) {
/* 86 */       sb.append(" ");
/* 87 */       sb.append(this.breakLabel.toSource(0));
/*    */     } 
/* 89 */     sb.append(";\n");
/* 90 */     return sb.toString();
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void visit(NodeVisitor v) {
/* 98 */     if (v.visit(this) && this.breakLabel != null)
/* 99 */       this.breakLabel.visit(v); 
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\mozilla\javascript\ast\BreakStatement.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */