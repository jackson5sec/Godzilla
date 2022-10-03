/*    */ package javassist.compiler;
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
/*    */ public class SyntaxError
/*    */   extends CompileError
/*    */ {
/*    */   private static final long serialVersionUID = 1L;
/*    */   
/*    */   public SyntaxError(Lex lexer) {
/* 24 */     super("syntax error near \"" + lexer.getTextAround() + "\"", lexer);
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\javassist\compiler\SyntaxError.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */