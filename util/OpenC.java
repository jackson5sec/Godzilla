/*    */ package util;
/*    */ 
/*    */ import core.ApplicationContext;
/*    */ import core.ui.component.dialog.GOptionPane;
/*    */ import java.awt.event.ActionEvent;
/*    */ import java.awt.event.ActionListener;
/*    */ import javax.swing.JCheckBox;
/*    */ 
/*    */ public class OpenC
/*    */   implements ActionListener {
/*    */   private final String keyString;
/*    */   private final JCheckBox checkBox;
/*    */   
/*    */   public OpenC(String key, JCheckBox checkBox) {
/* 15 */     this.keyString = key;
/* 16 */     this.checkBox = checkBox;
/*    */   }
/*    */   
/*    */   public void actionPerformed(ActionEvent paramActionEvent) {
/* 20 */     if (ApplicationContext.setOpenC(this.keyString, this.checkBox.isSelected())) {
/* 21 */       GOptionPane.showMessageDialog(null, "修改成功!", "提示", 1);
/*    */     } else {
/*    */       
/* 24 */       GOptionPane.showMessageDialog(null, "修改失败!", "提示", 2);
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar\\util\OpenC.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */