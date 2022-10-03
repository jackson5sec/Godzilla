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
/*    */ public class Assignment
/*    */   extends InfixExpression
/*    */ {
/*    */   public Assignment() {}
/*    */   
/*    */   public Assignment(int pos) {
/* 19 */     super(pos);
/*    */   }
/*    */   
/*    */   public Assignment(int pos, int len) {
/* 23 */     super(pos, len);
/*    */   }
/*    */   
/*    */   public Assignment(int pos, int len, AstNode left, AstNode right) {
/* 27 */     super(pos, len, left, right);
/*    */   }
/*    */   
/*    */   public Assignment(AstNode left, AstNode right) {
/* 31 */     super(left, right);
/*    */   }
/*    */ 
/*    */   
/*    */   public Assignment(int operator, AstNode left, AstNode right, int operatorPos) {
/* 36 */     super(operator, left, right, operatorPos);
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\mozilla\javascript\ast\Assignment.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */