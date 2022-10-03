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
/*    */ public class ArrayComprehensionLoop
/*    */   extends ForInLoop
/*    */ {
/*    */   public ArrayComprehensionLoop() {}
/*    */   
/*    */   public ArrayComprehensionLoop(int pos) {
/* 23 */     super(pos);
/*    */   }
/*    */   
/*    */   public ArrayComprehensionLoop(int pos, int len) {
/* 27 */     super(pos, len);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public AstNode getBody() {
/* 36 */     return null;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void setBody(AstNode body) {
/* 46 */     throw new UnsupportedOperationException("this node type has no body");
/*    */   }
/*    */ 
/*    */   
/*    */   public String toSource(int depth) {
/* 51 */     return makeIndent(depth) + " for " + (isForEach() ? "each " : "") + "(" + this.iterator.toSource(0) + " in " + this.iteratedObject.toSource(0) + ")";
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
/*    */ 
/*    */ 
/*    */   
/*    */   public void visit(NodeVisitor v) {
/* 67 */     if (v.visit(this)) {
/* 68 */       this.iterator.visit(v);
/* 69 */       this.iteratedObject.visit(v);
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\mozilla\javascript\ast\ArrayComprehensionLoop.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */