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
/*    */ public class InstanceOfExpr
/*    */   extends CastExpr
/*    */ {
/*    */   private static final long serialVersionUID = 1L;
/*    */   
/*    */   public InstanceOfExpr(ASTList className, int dim, ASTree expr) {
/* 29 */     super(className, dim, expr);
/*    */   }
/*    */   
/*    */   public InstanceOfExpr(int type, int dim, ASTree expr) {
/* 33 */     super(type, dim, expr);
/*    */   }
/*    */ 
/*    */   
/*    */   public String getTag() {
/* 38 */     return "instanceof:" + this.castType + ":" + this.arrayDim;
/*    */   }
/*    */ 
/*    */   
/*    */   public void accept(Visitor v) throws CompileError {
/* 43 */     v.atInstanceOfExpr(this);
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\javassist\compiler\ast\InstanceOfExpr.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */