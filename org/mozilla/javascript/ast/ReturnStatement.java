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
/*    */ public class ReturnStatement
/*    */   extends AstNode
/*    */ {
/*    */   private AstNode returnValue;
/*    */   
/*    */   public ReturnStatement() {}
/*    */   
/*    */   public ReturnStatement(int pos) {
/* 29 */     super(pos);
/*    */   }
/*    */   
/*    */   public ReturnStatement(int pos, int len) {
/* 33 */     super(pos, len);
/*    */   }
/*    */   
/*    */   public ReturnStatement(int pos, int len, AstNode returnValue) {
/* 37 */     super(pos, len);
/* 38 */     setReturnValue(returnValue);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public AstNode getReturnValue() {
/* 45 */     return this.returnValue;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void setReturnValue(AstNode returnValue) {
/* 53 */     this.returnValue = returnValue;
/* 54 */     if (returnValue != null) {
/* 55 */       returnValue.setParent(this);
/*    */     }
/*    */   }
/*    */   
/*    */   public String toSource(int depth) {
/* 60 */     StringBuilder sb = new StringBuilder();
/* 61 */     sb.append(makeIndent(depth));
/* 62 */     sb.append("return");
/* 63 */     if (this.returnValue != null) {
/* 64 */       sb.append(" ");
/* 65 */       sb.append(this.returnValue.toSource(0));
/*    */     } 
/* 67 */     sb.append(";\n");
/* 68 */     return sb.toString();
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void visit(NodeVisitor v) {
/* 76 */     if (v.visit(this) && this.returnValue != null)
/* 77 */       this.returnValue.visit(v); 
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\mozilla\javascript\ast\ReturnStatement.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */