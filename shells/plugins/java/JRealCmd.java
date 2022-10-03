/*    */ package shells.plugins.java;
/*    */ 
/*    */ import core.annotation.PluginAnnotation;
/*    */ import java.io.InputStream;
/*    */ import shells.plugins.generic.RealCmd;
/*    */ import util.Log;
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
/*    */ @PluginAnnotation(payloadName = "JavaDynamicPayload", Name = "RealCmd", DisplayName = "虚拟终端")
/*    */ public class JRealCmd
/*    */   extends RealCmd
/*    */ {
/*    */   private static final String CLASS_NAME = "plugin.RealCmd";
/*    */   
/*    */   public byte[] readPlugin() {
/* 40 */     byte[] data = null;
/*    */     try {
/* 42 */       InputStream inputStream = getClass().getResourceAsStream("assets/RealCmd.classs");
/* 43 */       data = functions.readInputStream(inputStream);
/* 44 */       inputStream.close();
/* 45 */     } catch (Exception e) {
/* 46 */       Log.error(e);
/*    */     } 
/* 48 */     return data;
/*    */   }
/*    */ 
/*    */   
/*    */   public String getClassName() {
/* 53 */     return "plugin.RealCmd";
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\shells\plugins\java\JRealCmd.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */