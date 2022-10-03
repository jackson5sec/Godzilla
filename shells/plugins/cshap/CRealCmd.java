/*    */ package shells.plugins.cshap;
/*    */ 
/*    */ import core.annotation.PluginAnnotation;
/*    */ import shells.plugins.generic.RealCmd;
/*    */ import util.functions;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ @PluginAnnotation(payloadName = "CShapDynamicPayload", Name = "RealCmd", DisplayName = "虚拟终端")
/*    */ public class CRealCmd
/*    */   extends RealCmd
/*    */ {
/*    */   private static final String CLASS_NAME = "RealCmd.Run";
/*    */   
/*    */   public byte[] readPlugin() {
/* 42 */     return functions.readInputStreamAutoClose(getClass().getResourceAsStream("assets/RealCmd.dll"));
/*    */   }
/*    */ 
/*    */   
/*    */   public String getClassName() {
/* 47 */     return "RealCmd.Run";
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\shells\plugins\cshap\CRealCmd.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */