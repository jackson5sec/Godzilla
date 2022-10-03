/*    */ package javassist.tools.reflect;
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
/*    */ public class CannotCreateException
/*    */   extends Exception
/*    */ {
/*    */   private static final long serialVersionUID = 1L;
/*    */   
/*    */   public CannotCreateException(String s) {
/* 27 */     super(s);
/*    */   }
/*    */   
/*    */   public CannotCreateException(Exception e) {
/* 31 */     super("by " + e.toString());
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\javassist\tools\reflect\CannotCreateException.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */