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
/*    */ public class Keyword
/*    */   extends ASTree
/*    */ {
/*    */   private static final long serialVersionUID = 1L;
/*    */   protected int tokenId;
/*    */   
/*    */   public Keyword(int token) {
/* 30 */     this.tokenId = token;
/*    */   }
/*    */   public int get() {
/* 33 */     return this.tokenId;
/*    */   }
/*    */   public String toString() {
/* 36 */     return "id:" + this.tokenId;
/*    */   }
/*    */   public void accept(Visitor v) throws CompileError {
/* 39 */     v.atKeyword(this);
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\javassist\compiler\ast\Keyword.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */