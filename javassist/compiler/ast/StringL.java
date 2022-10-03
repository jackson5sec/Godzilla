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
/*    */ public class StringL
/*    */   extends ASTree
/*    */ {
/*    */   private static final long serialVersionUID = 1L;
/*    */   protected String text;
/*    */   
/*    */   public StringL(String t) {
/* 30 */     this.text = t;
/*    */   }
/*    */   public String get() {
/* 33 */     return this.text;
/*    */   }
/*    */   public String toString() {
/* 36 */     return "\"" + this.text + "\"";
/*    */   }
/*    */   public void accept(Visitor v) throws CompileError {
/* 39 */     v.atStringL(this);
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\javassist\compiler\ast\StringL.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */