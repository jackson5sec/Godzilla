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
/*    */ public class Symbol
/*    */   extends ASTree
/*    */ {
/*    */   private static final long serialVersionUID = 1L;
/*    */   protected String identifier;
/*    */   
/*    */   public Symbol(String sym) {
/* 30 */     this.identifier = sym;
/*    */   }
/*    */   public String get() {
/* 33 */     return this.identifier;
/*    */   }
/*    */   public String toString() {
/* 36 */     return this.identifier;
/*    */   }
/*    */   public void accept(Visitor v) throws CompileError {
/* 39 */     v.atSymbol(this);
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\javassist\compiler\ast\Symbol.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */