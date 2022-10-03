/*    */ package javassist.bytecode;
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
/*    */ public class BadBytecode
/*    */   extends Exception
/*    */ {
/*    */   private static final long serialVersionUID = 1L;
/*    */   
/*    */   public BadBytecode(int opcode) {
/* 27 */     super("bytecode " + opcode);
/*    */   }
/*    */   
/*    */   public BadBytecode(String msg) {
/* 31 */     super(msg);
/*    */   }
/*    */   
/*    */   public BadBytecode(String msg, Throwable cause) {
/* 35 */     super(msg, cause);
/*    */   }
/*    */   
/*    */   public BadBytecode(MethodInfo minfo, Throwable cause) {
/* 39 */     super(minfo.toString() + " in " + minfo
/* 40 */         .getConstPool().getClassName() + ": " + cause
/* 41 */         .getMessage(), cause);
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\javassist\bytecode\BadBytecode.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */