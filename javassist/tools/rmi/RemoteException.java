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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class RemoteException
/*    */   extends RuntimeException
/*    */ {
/*    */   private static final long serialVersionUID = 1L;
/*    */   
/*    */   public RemoteException(String msg) {
/* 28 */     super(msg);
/*    */   }
/*    */   
/*    */   public RemoteException(Exception e) {
/* 32 */     super("by " + e.toString());
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\javassist\tools\rmi\RemoteException.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */