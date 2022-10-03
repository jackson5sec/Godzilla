/*    */ package shells.plugins.java;
/*    */ 
/*    */ import core.annotation.PluginAnnotation;
/*    */ import shells.plugins.generic.Mimikatz;
/*    */ import shells.plugins.generic.ShellcodeLoader;
/*    */ 
/*    */ @PluginAnnotation(payloadName = "JavaDynamicPayload", Name = "Mimikatz", DisplayName = "Mimikatz")
/*    */ public class Mimikatz extends Mimikatz {
/*    */   protected ShellcodeLoader getShellcodeLoader() {
/* 10 */     return (ShellcodeLoader)this.shellEntity.getFrame().getPlugin("ShellcodeLoader");
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\shells\plugins\java\Mimikatz.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */