/*    */ package javassist.tools.rmi;
/*    */ 
/*    */ import java.io.Serializable;
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
/*    */ public class RemoteRef
/*    */   implements Serializable
/*    */ {
/*    */   private static final long serialVersionUID = 1L;
/*    */   public int oid;
/*    */   public String classname;
/*    */   
/*    */   public RemoteRef(int i) {
/* 30 */     this.oid = i;
/* 31 */     this.classname = null;
/*    */   }
/*    */   
/*    */   public RemoteRef(int i, String name) {
/* 35 */     this.oid = i;
/* 36 */     this.classname = name;
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\javassist\tools\rmi\RemoteRef.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */