/*    */ package com.formdev.flatlaf.ui;
/*    */ 
/*    */ import javax.swing.DefaultDesktopManager;
/*    */ import javax.swing.JComponent;
/*    */ import javax.swing.JInternalFrame;
/*    */ import javax.swing.plaf.ComponentUI;
/*    */ import javax.swing.plaf.UIResource;
/*    */ import javax.swing.plaf.basic.BasicDesktopPaneUI;
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
/*    */ public class FlatDesktopPaneUI
/*    */   extends BasicDesktopPaneUI
/*    */ {
/*    */   public static ComponentUI createUI(JComponent c) {
/* 40 */     return new FlatDesktopPaneUI();
/*    */   }
/*    */ 
/*    */   
/*    */   protected void installDesktopManager() {
/* 45 */     this.desktopManager = this.desktop.getDesktopManager();
/* 46 */     if (this.desktopManager == null) {
/* 47 */       this.desktopManager = new FlatDesktopManager();
/* 48 */       this.desktop.setDesktopManager(this.desktopManager);
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   private class FlatDesktopManager
/*    */     extends DefaultDesktopManager
/*    */     implements UIResource
/*    */   {
/*    */     private FlatDesktopManager() {}
/*    */     
/*    */     public void iconifyFrame(JInternalFrame f) {
/* 60 */       super.iconifyFrame(f);
/*    */       
/* 62 */       ((FlatDesktopIconUI)f.getDesktopIcon().getUI()).updateDockIcon();
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\formdev\flatla\\ui\FlatDesktopPaneUI.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */