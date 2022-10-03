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
/*    */ public class ErrorNode
/*    */   extends AstNode
/*    */ {
/*    */   private String message;
/*    */   
/*    */   public ErrorNode() {}
/*    */   
/*    */   public ErrorNode(int pos) {
/* 27 */     super(pos);
/*    */   }
/*    */   
/*    */   public ErrorNode(int pos, int len) {
/* 31 */     super(pos, len);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public String getMessage() {
/* 38 */     return this.message;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void setMessage(String message) {
/* 45 */     this.message = message;
/*    */   }
/*    */ 
/*    */   
/*    */   public String toSource(int depth) {
/* 50 */     return "";
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void visit(NodeVisitor v) {
/* 59 */     v.visit(this);
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\mozilla\javascript\ast\ErrorNode.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */