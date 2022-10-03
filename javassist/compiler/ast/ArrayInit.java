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
/*    */ public class ArrayInit
/*    */   extends ASTList
/*    */ {
/*    */   private static final long serialVersionUID = 1L;
/*    */   
/*    */   public ArrayInit(ASTree firstElement) {
/* 33 */     super(firstElement);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public int size() {
/* 42 */     int s = length();
/* 43 */     if (s == 1 && head() == null) {
/* 44 */       return 0;
/*    */     }
/* 46 */     return s;
/*    */   }
/*    */   
/*    */   public void accept(Visitor v) throws CompileError {
/* 50 */     v.atArrayInit(this);
/*    */   }
/*    */   public String getTag() {
/* 53 */     return "array";
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\javassist\compiler\ast\ArrayInit.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */