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
/*    */ public class MethodDecl
/*    */   extends ASTList
/*    */ {
/*    */   private static final long serialVersionUID = 1L;
/*    */   public static final String initName = "<init>";
/*    */   
/*    */   public MethodDecl(ASTree _head, ASTList _tail) {
/* 27 */     super(_head, _tail);
/*    */   }
/*    */   
/*    */   public boolean isConstructor() {
/* 31 */     Symbol sym = getReturn().getVariable();
/* 32 */     return (sym != null && "<init>".equals(sym.get()));
/*    */   }
/*    */   public ASTList getModifiers() {
/* 35 */     return (ASTList)getLeft();
/*    */   } public Declarator getReturn() {
/* 37 */     return (Declarator)tail().head();
/*    */   } public ASTList getParams() {
/* 39 */     return (ASTList)sublist(2).head();
/*    */   } public ASTList getThrows() {
/* 41 */     return (ASTList)sublist(3).head();
/*    */   } public Stmnt getBody() {
/* 43 */     return (Stmnt)sublist(4).head();
/*    */   }
/*    */   
/*    */   public void accept(Visitor v) throws CompileError {
/* 47 */     v.atMethodDecl(this);
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\javassist\compiler\ast\MethodDecl.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */