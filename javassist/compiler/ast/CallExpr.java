/*    */ package javassist.compiler.ast;
/*    */ 
/*    */ import javassist.compiler.CompileError;
/*    */ import javassist.compiler.MemberResolver;
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
/*    */ public class CallExpr
/*    */   extends Expr
/*    */ {
/*    */   private static final long serialVersionUID = 1L;
/*    */   private MemberResolver.Method method;
/*    */   
/*    */   private CallExpr(ASTree _head, ASTList _tail) {
/* 32 */     super(67, _head, _tail);
/* 33 */     this.method = null;
/*    */   }
/*    */   
/*    */   public void setMethod(MemberResolver.Method m) {
/* 37 */     this.method = m;
/*    */   }
/*    */   
/*    */   public MemberResolver.Method getMethod() {
/* 41 */     return this.method;
/*    */   }
/*    */   
/*    */   public static CallExpr makeCall(ASTree target, ASTree args) {
/* 45 */     return new CallExpr(target, new ASTList(args));
/*    */   }
/*    */   
/*    */   public void accept(Visitor v) throws CompileError {
/* 49 */     v.atCallExpr(this);
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\javassist\compiler\ast\CallExpr.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */