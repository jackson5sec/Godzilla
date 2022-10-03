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
/*    */ public class CondExpr
/*    */   extends ASTList
/*    */ {
/*    */   private static final long serialVersionUID = 1L;
/*    */   
/*    */   public CondExpr(ASTree cond, ASTree thenp, ASTree elsep) {
/* 29 */     super(cond, new ASTList(thenp, new ASTList(elsep)));
/*    */   }
/*    */   public ASTree condExpr() {
/* 32 */     return head();
/*    */   } public void setCond(ASTree t) {
/* 34 */     setHead(t);
/*    */   } public ASTree thenExpr() {
/* 36 */     return tail().head();
/*    */   } public void setThen(ASTree t) {
/* 38 */     tail().setHead(t);
/*    */   } public ASTree elseExpr() {
/* 40 */     return tail().tail().head();
/*    */   } public void setElse(ASTree t) {
/* 42 */     tail().tail().setHead(t);
/*    */   }
/*    */   public String getTag() {
/* 45 */     return "?:";
/*    */   }
/*    */   public void accept(Visitor v) throws CompileError {
/* 48 */     v.atCondExpr(this);
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\javassist\compiler\ast\CondExpr.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */