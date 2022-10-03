/*    */ package shells.plugins.cshap;
/*    */ 
/*    */ import core.annotation.PluginAnnotation;
/*    */ import core.imp.Plugin;
/*    */ import java.io.IOException;
/*    */ import java.io.InputStream;
/*    */ import shells.plugins.generic.HttpProxy;
/*    */ import util.functions;
/*    */ 
/*    */ 
/*    */ @PluginAnnotation(payloadName = "CShapDynamicPayload", Name = "HttpProxy", DisplayName = "Http代理")
/*    */ public class CHttpProxy
/*    */   extends HttpProxy
/*    */   implements Plugin
/*    */ {
/*    */   private static final String CLASS_NAME = "HttpRequest.Run";
/*    */   
/*    */   public byte[] readPlugin() throws IOException {
/* 19 */     InputStream inputStream = getClass().getResourceAsStream(String.format("assets/%s.dll", new Object[] { "HttpRequest" }));
/* 20 */     byte[] data = functions.readInputStream(inputStream);
/* 21 */     inputStream.close();
/* 22 */     return data;
/*    */   }
/*    */ 
/*    */   
/*    */   public String getClassName() {
/* 27 */     return "HttpRequest.Run";
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\shells\plugins\cshap\CHttpProxy.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */