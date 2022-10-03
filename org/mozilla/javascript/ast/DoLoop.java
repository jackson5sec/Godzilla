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
/*    */ public class DoLoop
/*    */   extends Loop
/*    */ {
/*    */   private AstNode condition;
/* 20 */   private int whilePosition = -1;
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public DoLoop() {}
/*    */ 
/*    */ 
/*    */   
/*    */   public DoLoop(int pos) {
/* 30 */     super(pos);
/*    */   }
/*    */   
/*    */   public DoLoop(int pos, int len) {
/* 34 */     super(pos, len);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public AstNode getCondition() {
/* 41 */     return this.condition;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void setCondition(AstNode condition) {
/* 49 */     assertNotNull(condition);
/* 50 */     this.condition = condition;
/* 51 */     condition.setParent(this);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public int getWhilePosition() {
/* 58 */     return this.whilePosition;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void setWhilePosition(int whilePosition) {
/* 65 */     this.whilePosition = whilePosition;
/*    */   }
/*    */ 
/*    */   
/*    */   public String toSource(int depth) {
/* 70 */     StringBuilder sb = new StringBuilder();
/* 71 */     sb.append(makeIndent(depth));
/* 72 */     sb.append("do ");
/* 73 */     sb.append(this.body.toSource(depth).trim());
/* 74 */     sb.append(" while (");
/* 75 */     sb.append(this.condition.toSource(0));
/* 76 */     sb.append(");\n");
/* 77 */     return sb.toString();
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void visit(NodeVisitor v) {
/* 85 */     if (v.visit(this)) {
/* 86 */       this.body.visit(v);
/* 87 */       this.condition.visit(v);
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\mozilla\javascript\ast\DoLoop.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */