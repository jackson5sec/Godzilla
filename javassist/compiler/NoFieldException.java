/*    */ package javassist.compiler;
/*    */ 
/*    */ import javassist.compiler.ast.ASTree;
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
/*    */ public class NoFieldException
/*    */   extends CompileError
/*    */ {
/*    */   private static final long serialVersionUID = 1L;
/*    */   private String fieldName;
/*    */   private ASTree expr;
/*    */   
/*    */   public NoFieldException(String name, ASTree e) {
/* 30 */     super("no such field: " + name);
/* 31 */     this.fieldName = name;
/* 32 */     this.expr = e;
/*    */   }
/*    */ 
/*    */   
/*    */   public String getField() {
/* 37 */     return this.fieldName;
/*    */   }
/*    */   
/*    */   public ASTree getExpr() {
/* 41 */     return this.expr;
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\javassist\compiler\NoFieldException.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */