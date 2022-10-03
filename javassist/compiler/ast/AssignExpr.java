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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class AssignExpr
/*    */   extends Expr
/*    */ {
/*    */   private static final long serialVersionUID = 1L;
/*    */   
/*    */   private AssignExpr(int op, ASTree _head, ASTList _tail) {
/* 33 */     super(op, _head, _tail);
/*    */   }
/*    */ 
/*    */   
/*    */   public static AssignExpr makeAssign(int op, ASTree oprand1, ASTree oprand2) {
/* 38 */     return new AssignExpr(op, oprand1, new ASTList(oprand2));
/*    */   }
/*    */ 
/*    */   
/*    */   public void accept(Visitor v) throws CompileError {
/* 43 */     v.atAssignExpr(this);
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\javassist\compiler\ast\AssignExpr.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */