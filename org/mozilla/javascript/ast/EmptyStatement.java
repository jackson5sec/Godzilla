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
/*    */ public class EmptyStatement
/*    */   extends AstNode
/*    */ {
/*    */   public EmptyStatement() {}
/*    */   
/*    */   public EmptyStatement(int pos) {
/* 25 */     super(pos);
/*    */   }
/*    */   
/*    */   public EmptyStatement(int pos, int len) {
/* 29 */     super(pos, len);
/*    */   }
/*    */ 
/*    */   
/*    */   public String toSource(int depth) {
/* 34 */     StringBuilder sb = new StringBuilder();
/* 35 */     sb.append(makeIndent(depth)).append(";\n");
/* 36 */     return sb.toString();
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void visit(NodeVisitor v) {
/* 44 */     v.visit(this);
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\mozilla\javascript\ast\EmptyStatement.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */