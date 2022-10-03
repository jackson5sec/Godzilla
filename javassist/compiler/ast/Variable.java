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
/*    */ public class Variable
/*    */   extends Symbol
/*    */ {
/*    */   private static final long serialVersionUID = 1L;
/*    */   protected Declarator declarator;
/*    */   
/*    */   public Variable(String sym, Declarator d) {
/* 30 */     super(sym);
/* 31 */     this.declarator = d;
/*    */   }
/*    */   public Declarator getDeclarator() {
/* 34 */     return this.declarator;
/*    */   }
/*    */   
/*    */   public String toString() {
/* 38 */     return this.identifier + ":" + this.declarator.getType();
/*    */   }
/*    */   
/*    */   public void accept(Visitor v) throws CompileError {
/* 42 */     v.atVariable(this);
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\javassist\compiler\ast\Variable.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */