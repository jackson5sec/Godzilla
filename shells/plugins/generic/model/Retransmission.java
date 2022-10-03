/*    */ package shells.plugins.generic.model;
/*    */ 
/*    */ import core.socksServer.SocketStatus;
/*    */ import java.util.UUID;
/*    */ import shells.plugins.generic.model.enums.RetransmissionType;
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
/*    */ public class Retransmission
/*    */ {
/* 18 */   public String identifier = UUID.randomUUID().toString();
/*    */   
/*    */   public String listenAddress;
/*    */   
/*    */   public String toString() {
/* 23 */     return "Retransmission{listenAddress='" + this.listenAddress + '\'' + ", listenPort=" + this.listenPort + ", targetAddress='" + this.targetAddress + '\'' + ", targetPort=" + this.targetPort + ", retransmissionType=" + this.retransmissionType + '}';
/*    */   }
/*    */   
/*    */   public int listenPort;
/*    */   public String targetAddress;
/*    */   public int targetPort;
/*    */   public RetransmissionType retransmissionType;
/*    */   public SocketStatus socketStatus;
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\shells\plugins\generic\model\Retransmission.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */