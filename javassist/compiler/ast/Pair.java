/*    */ package javassist.compiler.ast;
/*    */ 
/*    */ import javassist.compiler.CompileError;
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
/*    */ public class Pair
/*    */   extends ASTree
/*    */ {
/*    */   private static final long serialVersionUID = 1L;
/*    */   protected ASTree left;
/*    */   protected ASTree right;
/*    */   
/*    */   public Pair(ASTree _left, ASTree _right) {
/* 31 */     this.left = _left;
/* 32 */     this.right = _right;
/*    */   }
/*    */   
/*    */   public void accept(Visitor v) throws CompileError {
/* 36 */     v.atPair(this);
/*    */   }
/*    */   
/*    */   public String toString() {
/* 40 */     StringBuffer sbuf = new StringBuffer();
/* 41 */     sbuf.append("(<Pair> ");
/* 42 */     sbuf.append((this.left == null) ? "<null>" : this.left.toString());
/* 43 */     sbuf.append(" . ");
/* 44 */     sbuf.append((this.right == null) ? "<null>" : this.right.toString());
/* 45 */     sbuf.append(')');
/* 46 */     return sbuf.toString();
/*    */   }
/*    */   
/*    */   public ASTree getLeft() {
/* 50 */     return this.left;
/*    */   }
/*    */   public ASTree getRight() {
/* 53 */     return this.right;
/*    */   }
/*    */   public void setLeft(ASTree _left) {
/* 56 */     this.left = _left;
/*    */   }
/*    */   public void setRight(ASTree _right) {
/* 59 */     this.right = _right;
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\javassist\compiler\ast\Pair.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */