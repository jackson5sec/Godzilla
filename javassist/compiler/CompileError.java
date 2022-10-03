/*    */ package javassist.compiler;
/*    */ 
/*    */ import javassist.CannotCompileException;
/*    */ import javassist.NotFoundException;
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
/*    */ public class CompileError
/*    */   extends Exception
/*    */ {
/*    */   private static final long serialVersionUID = 1L;
/*    */   private Lex lex;
/*    */   private String reason;
/*    */   
/*    */   public CompileError(String s, Lex l) {
/* 29 */     this.reason = s;
/* 30 */     this.lex = l;
/*    */   }
/*    */   
/*    */   public CompileError(String s) {
/* 34 */     this.reason = s;
/* 35 */     this.lex = null;
/*    */   }
/*    */   
/*    */   public CompileError(CannotCompileException e) {
/* 39 */     this(e.getReason());
/*    */   }
/*    */   
/*    */   public CompileError(NotFoundException e) {
/* 43 */     this("cannot find " + e.getMessage());
/*    */   }
/*    */   public Lex getLex() {
/* 46 */     return this.lex;
/*    */   }
/*    */   
/*    */   public String getMessage() {
/* 50 */     return this.reason;
/*    */   }
/*    */ 
/*    */   
/*    */   public String toString() {
/* 55 */     return "compile error: " + this.reason;
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\javassist\compiler\CompileError.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */