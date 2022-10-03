/*    */ package com.formdev.flatlaf.ui;
/*    */ 
/*    */ import java.awt.Component;
/*    */ import java.awt.Insets;
/*    */ import java.awt.event.ContainerEvent;
/*    */ import java.awt.event.ContainerListener;
/*    */ import javax.swing.JComponent;
/*    */ import javax.swing.border.Border;
/*    */ import javax.swing.plaf.ComponentUI;
/*    */ import javax.swing.plaf.basic.BasicToolBarUI;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class FlatToolBarUI
/*    */   extends BasicToolBarUI
/*    */ {
/*    */   public static ComponentUI createUI(JComponent c) {
/* 50 */     return new FlatToolBarUI();
/*    */   }
/*    */ 
/*    */   
/*    */   protected ContainerListener createToolBarContListener() {
/* 55 */     return new BasicToolBarUI.ToolBarContListener()
/*    */       {
/*    */         public void componentAdded(ContainerEvent e) {
/* 58 */           super.componentAdded(e);
/*    */           
/* 60 */           Component c = e.getChild();
/* 61 */           if (c instanceof javax.swing.AbstractButton) {
/* 62 */             c.setFocusable(false);
/*    */           }
/*    */         }
/*    */         
/*    */         public void componentRemoved(ContainerEvent e) {
/* 67 */           super.componentRemoved(e);
/*    */           
/* 69 */           Component c = e.getChild();
/* 70 */           if (c instanceof javax.swing.AbstractButton)
/* 71 */             c.setFocusable(true); 
/*    */         }
/*    */       };
/*    */   }
/*    */   
/*    */   protected void setBorderToRollover(Component c) {}
/*    */   
/*    */   protected void setBorderToNonRollover(Component c) {}
/*    */   
/*    */   protected void setBorderToNormal(Component c) {}
/*    */   
/*    */   protected Border createRolloverBorder() {
/* 83 */     return null; } protected void installRolloverBorders(JComponent c) {} protected void installNonRolloverBorders(JComponent c) {} protected void installNormalBorders(JComponent c) {} protected Border createNonRolloverBorder() {
/* 84 */     return null;
/*    */   }
/*    */   
/*    */   public void setOrientation(int orientation) {
/* 88 */     if (orientation != this.toolBar.getOrientation()) {
/*    */       
/* 90 */       Insets margin = this.toolBar.getMargin();
/* 91 */       Insets newMargin = new Insets(margin.left, margin.top, margin.right, margin.bottom);
/* 92 */       if (!newMargin.equals(margin)) {
/* 93 */         this.toolBar.setMargin(newMargin);
/*    */       }
/*    */     } 
/* 96 */     super.setOrientation(orientation);
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\formdev\flatla\\ui\FlatToolBarUI.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */