/*    */ package com.formdev.flatlaf.ui;
/*    */ 
/*    */ import com.formdev.flatlaf.FlatLaf;
/*    */ import com.formdev.flatlaf.util.SystemInfo;
/*    */ import java.awt.event.ActionEvent;
/*    */ import javax.swing.AbstractAction;
/*    */ import javax.swing.ActionMap;
/*    */ import javax.swing.JComponent;
/*    */ import javax.swing.JMenu;
/*    */ import javax.swing.JMenuBar;
/*    */ import javax.swing.MenuSelectionManager;
/*    */ import javax.swing.SwingUtilities;
/*    */ import javax.swing.plaf.ActionMapUIResource;
/*    */ import javax.swing.plaf.ComponentUI;
/*    */ import javax.swing.plaf.basic.BasicMenuBarUI;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class FlatMenuBarUI
/*    */   extends BasicMenuBarUI
/*    */ {
/*    */   public static ComponentUI createUI(JComponent c) {
/* 50 */     return new FlatMenuBarUI();
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   protected void installKeyboardActions() {
/* 60 */     super.installKeyboardActions();
/*    */     
/* 62 */     ActionMap map = SwingUtilities.getUIActionMap(this.menuBar);
/* 63 */     if (map == null) {
/* 64 */       map = new ActionMapUIResource();
/* 65 */       SwingUtilities.replaceUIActionMap(this.menuBar, map);
/*    */     } 
/* 67 */     map.put("takeFocus", new TakeFocus());
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   private static class TakeFocus
/*    */     extends AbstractAction
/*    */   {
/*    */     private TakeFocus() {}
/*    */ 
/*    */ 
/*    */ 
/*    */     
/*    */     public void actionPerformed(ActionEvent e) {
/* 82 */       JMenuBar menuBar = (JMenuBar)e.getSource();
/* 83 */       JMenu menu = menuBar.getMenu(0);
/* 84 */       if (menu != null) {
/* 85 */         (new javax.swing.MenuElement[2])[0] = menuBar; (new javax.swing.MenuElement[2])[1] = menu; (new javax.swing.MenuElement[3])[0] = menuBar; (new javax.swing.MenuElement[3])[1] = menu; (new javax.swing.MenuElement[3])[2] = menu
/*    */           
/* 87 */           .getPopupMenu();
/*    */         MenuSelectionManager.defaultManager().setSelectedPath(SystemInfo.isWindows ? new javax.swing.MenuElement[2] : new javax.swing.MenuElement[3]);
/* 89 */         FlatLaf.showMnemonics(menuBar);
/*    */       } 
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\formdev\flatla\\ui\FlatMenuBarUI.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */