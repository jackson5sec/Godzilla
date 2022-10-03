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
/*    */ public class WhileLoop
/*    */   extends Loop
/*    */ {
/*    */   private AstNode condition;
/*    */   
/*    */   public WhileLoop() {}
/*    */   
/*    */   public WhileLoop(int pos) {
/* 29 */     super(pos);
/*    */   }
/*    */   
/*    */   public WhileLoop(int pos, int len) {
/* 33 */     super(pos, len);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public AstNode getCondition() {
/* 40 */     return this.condition;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void setCondition(AstNode condition) {
/* 48 */     assertNotNull(condition);
/* 49 */     this.condition = condition;
/* 50 */     condition.setParent(this);
/*    */   }
/*    */ 
/*    */   
/*    */   public String toSource(int depth) {
/* 55 */     StringBuilder sb = new StringBuilder();
/* 56 */     sb.append(makeIndent(depth));
/* 57 */     sb.append("while (");
/* 58 */     sb.append(this.condition.toSource(0));
/* 59 */     sb.append(") ");
/* 60 */     if (this.body.getType() == 129) {
/* 61 */       sb.append(this.body.toSource(depth).trim());
/* 62 */       sb.append("\n");
/*    */     } else {
/* 64 */       sb.append("\n").append(this.body.toSource(depth + 1));
/*    */     } 
/* 66 */     return sb.toString();
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void visit(NodeVisitor v) {
/* 74 */     if (v.visit(this)) {
/* 75 */       this.condition.visit(v);
/* 76 */       this.body.visit(v);
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\mozilla\javascript\ast\WhileLoop.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */