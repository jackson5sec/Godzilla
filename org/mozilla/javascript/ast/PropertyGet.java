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
/*    */ public class PropertyGet
/*    */   extends InfixExpression
/*    */ {
/*    */   public PropertyGet() {}
/*    */   
/*    */   public PropertyGet(int pos) {
/* 24 */     super(pos);
/*    */   }
/*    */   
/*    */   public PropertyGet(int pos, int len) {
/* 28 */     super(pos, len);
/*    */   }
/*    */   
/*    */   public PropertyGet(int pos, int len, AstNode target, Name property) {
/* 32 */     super(pos, len, target, property);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public PropertyGet(AstNode target, Name property) {
/* 40 */     super(target, property);
/*    */   }
/*    */   
/*    */   public PropertyGet(AstNode target, Name property, int dotPosition) {
/* 44 */     super(33, target, property, dotPosition);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public AstNode getTarget() {
/* 52 */     return getLeft();
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void setTarget(AstNode target) {
/* 62 */     setLeft(target);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public Name getProperty() {
/* 69 */     return (Name)getRight();
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void setProperty(Name property) {
/* 77 */     setRight(property);
/*    */   }
/*    */ 
/*    */   
/*    */   public String toSource(int depth) {
/* 82 */     StringBuilder sb = new StringBuilder();
/* 83 */     sb.append(makeIndent(depth));
/* 84 */     sb.append(getLeft().toSource(0));
/* 85 */     sb.append(".");
/* 86 */     sb.append(getRight().toSource(0));
/* 87 */     return sb.toString();
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void visit(NodeVisitor v) {
/* 95 */     if (v.visit(this)) {
/* 96 */       getTarget().visit(v);
/* 97 */       getProperty().visit(v);
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\mozilla\javascript\ast\PropertyGet.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */