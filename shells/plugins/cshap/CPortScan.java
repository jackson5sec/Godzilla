/*    */ package shells.plugins.cshap;
/*    */ 
/*    */ import core.annotation.PluginAnnotation;
/*    */ import java.io.IOException;
/*    */ import java.io.InputStream;
/*    */ import shells.plugins.generic.PortScan;
/*    */ import util.functions;
/*    */ 
/*    */ 
/*    */ @PluginAnnotation(payloadName = "CShapDynamicPayload", Name = "PortScan", DisplayName = "端口扫描")
/*    */ public class CPortScan
/*    */   extends PortScan
/*    */ {
/*    */   private static final String CLASS_NAME = "CProtScan.Run";
/*    */   
/*    */   public byte[] readPlugin() throws IOException {
/* 17 */     InputStream inputStream = getClass().getResourceAsStream(String.format("assets/%s.dll", new Object[] { "CProtScan.Run".substring(0, "CProtScan.Run".indexOf(".")) }));
/* 18 */     return functions.readInputStreamAutoClose(inputStream);
/*    */   }
/*    */ 
/*    */   
/*    */   public String getClassName() {
/* 23 */     return "CProtScan.Run";
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\shells\plugins\cshap\CPortScan.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */