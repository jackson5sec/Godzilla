/*    */ package javassist.compiler.ast;
/*    */ 
/*    */ import java.io.Serializable;
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
/*    */ 
/*    */ 
/*    */ public abstract class ASTree
/*    */   implements Serializable
/*    */ {
/*    */   private static final long serialVersionUID = 1L;
/*    */   
/*    */   public ASTree getLeft() {
/* 32 */     return null;
/*    */   } public ASTree getRight() {
/* 34 */     return null;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public void setLeft(ASTree _left) {}
/*    */ 
/*    */   
/*    */   public void setRight(ASTree _right) {}
/*    */ 
/*    */   
/*    */   public abstract void accept(Visitor paramVisitor) throws CompileError;
/*    */ 
/*    */   
/*    */   public String toString() {
/* 49 */     StringBuffer sbuf = new StringBuffer();
/* 50 */     sbuf.append('<');
/* 51 */     sbuf.append(getTag());
/* 52 */     sbuf.append('>');
/* 53 */     return sbuf.toString();
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   protected String getTag() {
/* 61 */     String name = getClass().getName();
/* 62 */     return name.substring(name.lastIndexOf('.') + 1);
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\javassist\compiler\ast\ASTree.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */