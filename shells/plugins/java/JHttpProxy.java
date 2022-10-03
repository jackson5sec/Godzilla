/*    */ package shells.plugins.java;
/*    */ 
/*    */ import core.annotation.PluginAnnotation;
/*    */ import core.imp.Plugin;
/*    */ import core.shell.ShellEntity;
/*    */ import java.io.IOException;
/*    */ import java.io.InputStream;
/*    */ import javax.swing.JPanel;
/*    */ import shells.plugins.generic.HttpProxy;
/*    */ import util.functions;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ @PluginAnnotation(payloadName = "JavaDynamicPayload", Name = "HttpProxy", DisplayName = "Http代理")
/*    */ public class JHttpProxy
/*    */   extends HttpProxy
/*    */   implements Plugin
/*    */ {
/*    */   private static final String CLASS_NAME = "plugin.HttpRequest";
/*    */   
/*    */   public byte[] readPlugin() throws IOException {
/* 25 */     InputStream inputStream = getClass().getResourceAsStream(String.format("assets/%s.classs", new Object[] { "HttpRequest" }));
/* 26 */     byte[] data = functions.readInputStream(inputStream);
/* 27 */     inputStream.close();
/* 28 */     return data;
/*    */   }
/*    */ 
/*    */   
/*    */   public String getClassName() {
/* 33 */     return "plugin.HttpRequest";
/*    */   }
/*    */ 
/*    */   
/*    */   public void init(ShellEntity shellEntity) {
/* 38 */     super.init(shellEntity);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public JPanel getView() {
/* 45 */     return super.getView();
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\shells\plugins\java\JHttpProxy.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */