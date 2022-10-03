/*    */ package javassist.bytecode.annotation;
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
/*    */ public class NoSuchClassError
/*    */   extends Error
/*    */ {
/*    */   private static final long serialVersionUID = 1L;
/*    */   private String className;
/*    */   
/*    */   public NoSuchClassError(String className, Error cause) {
/* 32 */     super(cause.toString(), cause);
/* 33 */     this.className = className;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public String getClassName() {
/* 40 */     return this.className;
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\javassist\bytecode\annotation\NoSuchClassError.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */