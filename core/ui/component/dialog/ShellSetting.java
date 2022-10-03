/*    */ package core.ui.component.dialog;
/*    */ 
/*    */ import core.ui.MainActivity;
/*    */ import java.awt.Frame;
/*    */ import javax.swing.JDialog;
/*    */ 
/*    */ public class ShellSetting
/*    */   extends JDialog
/*    */ {
/*    */   public ShellSetting(String id) {
/* 11 */     super((Frame)MainActivity.getFrame(), "Shell Setting", true);
/* 12 */     core.ui.component.frame.ShellSetting shellSetting = new core.ui.component.frame.ShellSetting(id, "/");
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\cor\\ui\component\dialog\ShellSetting.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */