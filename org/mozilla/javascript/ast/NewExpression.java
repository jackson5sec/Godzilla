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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class NewExpression
/*    */   extends FunctionCall
/*    */ {
/*    */   private ObjectLiteral initializer;
/*    */   
/*    */   public NewExpression() {}
/*    */   
/*    */   public NewExpression(int pos) {
/* 34 */     super(pos);
/*    */   }
/*    */   
/*    */   public NewExpression(int pos, int len) {
/* 38 */     super(pos, len);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public ObjectLiteral getInitializer() {
/* 47 */     return this.initializer;
/*    */   }
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
/*    */   public void setInitializer(ObjectLiteral initializer) {
/* 60 */     this.initializer = initializer;
/* 61 */     if (initializer != null) {
/* 62 */       initializer.setParent(this);
/*    */     }
/*    */   }
/*    */   
/*    */   public String toSource(int depth) {
/* 67 */     StringBuilder sb = new StringBuilder();
/* 68 */     sb.append(makeIndent(depth));
/* 69 */     sb.append("new ");
/* 70 */     sb.append(this.target.toSource(0));
/* 71 */     sb.append("(");
/* 72 */     if (this.arguments != null) {
/* 73 */       printList(this.arguments, sb);
/*    */     }
/* 75 */     sb.append(")");
/* 76 */     if (this.initializer != null) {
/* 77 */       sb.append(" ");
/* 78 */       sb.append(this.initializer.toSource(0));
/*    */     } 
/* 80 */     return sb.toString();
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void visit(NodeVisitor v) {
/* 89 */     if (v.visit(this)) {
/* 90 */       this.target.visit(v);
/* 91 */       for (AstNode arg : getArguments()) {
/* 92 */         arg.visit(v);
/*    */       }
/* 94 */       if (this.initializer != null)
/* 95 */         this.initializer.visit(v); 
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\mozilla\javascript\ast\NewExpression.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */