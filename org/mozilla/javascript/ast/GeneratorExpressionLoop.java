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
/*    */ public class GeneratorExpressionLoop
/*    */   extends ForInLoop
/*    */ {
/*    */   public GeneratorExpressionLoop() {}
/*    */   
/*    */   public GeneratorExpressionLoop(int pos) {
/* 17 */     super(pos);
/*    */   }
/*    */   
/*    */   public GeneratorExpressionLoop(int pos, int len) {
/* 21 */     super(pos, len);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public boolean isForEach() {
/* 29 */     return false;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void setIsForEach(boolean isForEach) {
/* 37 */     throw new UnsupportedOperationException("this node type does not support for each");
/*    */   }
/*    */ 
/*    */   
/*    */   public String toSource(int depth) {
/* 42 */     return makeIndent(depth) + " for " + (isForEach() ? "each " : "") + "(" + this.iterator.toSource(0) + " in " + this.iteratedObject.toSource(0) + ")";
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
/* 58 */     if (v.visit(this)) {
/* 59 */       this.iterator.visit(v);
/* 60 */       this.iteratedObject.visit(v);
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\mozilla\javascript\ast\GeneratorExpressionLoop.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */