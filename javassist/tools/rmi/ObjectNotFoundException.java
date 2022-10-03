/*    */ package javassist.tools.rmi;
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
/*    */ public class ObjectNotFoundException
/*    */   extends Exception
/*    */ {
/*    */   private static final long serialVersionUID = 1L;
/*    */   
/*    */   public ObjectNotFoundException(String name) {
/* 24 */     super(name + " is not exported");
/*    */   }
/*    */   
/*    */   public ObjectNotFoundException(String name, Exception e) {
/* 28 */     super(name + " because of " + e.toString());
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\javassist\tools\rmi\ObjectNotFoundException.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */