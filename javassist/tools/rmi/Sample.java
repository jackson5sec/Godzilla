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
/*    */ 
/*    */ public class Sample
/*    */ {
/*    */   private ObjectImporter importer;
/*    */   private int objectId;
/*    */   
/*    */   public Object forward(Object[] args, int identifier) {
/* 29 */     return this.importer.call(this.objectId, identifier, args);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public static Object forwardStatic(Object[] args, int identifier) throws RemoteException {
/* 35 */     throw new RemoteException("cannot call a static method.");
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\javassist\tools\rmi\Sample.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */