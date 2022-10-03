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
/*    */ public class Yield
/*    */   extends AstNode
/*    */ {
/*    */   private AstNode value;
/*    */   
/*    */   public Yield() {}
/*    */   
/*    */   public Yield(int pos) {
/* 30 */     super(pos);
/*    */   }
/*    */   
/*    */   public Yield(int pos, int len) {
/* 34 */     super(pos, len);
/*    */   }
/*    */   
/*    */   public Yield(int pos, int len, AstNode value) {
/* 38 */     super(pos, len);
/* 39 */     setValue(value);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public AstNode getValue() {
/* 46 */     return this.value;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void setValue(AstNode expr) {
/* 54 */     this.value = expr;
/* 55 */     if (expr != null) {
/* 56 */       expr.setParent(this);
/*    */     }
/*    */   }
/*    */   
/*    */   public String toSource(int depth) {
/* 61 */     return (this.value == null) ? "yield" : ("yield " + this.value.toSource(0));
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void visit(NodeVisitor v) {
/* 71 */     if (v.visit(this) && this.value != null)
/* 72 */       this.value.visit(v); 
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\mozilla\javascript\ast\Yield.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */