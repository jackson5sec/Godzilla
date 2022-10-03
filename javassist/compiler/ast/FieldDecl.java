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
/*    */ public class FieldDecl
/*    */   extends ASTList
/*    */ {
/*    */   private static final long serialVersionUID = 1L;
/*    */   
/*    */   public FieldDecl(ASTree _head, ASTList _tail) {
/* 26 */     super(_head, _tail);
/*    */   }
/*    */   public ASTList getModifiers() {
/* 29 */     return (ASTList)getLeft();
/*    */   } public Declarator getDeclarator() {
/* 31 */     return (Declarator)tail().head();
/*    */   } public ASTree getInit() {
/* 33 */     return sublist(2).head();
/*    */   }
/*    */   
/*    */   public void accept(Visitor v) throws CompileError {
/* 37 */     v.atFieldDecl(this);
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\javassist\compiler\ast\FieldDecl.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */