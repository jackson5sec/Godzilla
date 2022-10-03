/*    */ package com.formdev.flatlaf.ui;
/*    */ 
/*    */ import java.awt.Component;
/*    */ import java.awt.Graphics;
/*    */ import java.util.function.Supplier;
/*    */ import javax.swing.JComponent;
/*    */ import javax.swing.JViewport;
/*    */ import javax.swing.plaf.ComponentUI;
/*    */ import javax.swing.plaf.basic.BasicViewportUI;
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
/*    */ public class FlatViewportUI
/*    */   extends BasicViewportUI
/*    */ {
/*    */   public static ComponentUI createUI(JComponent c) {
/* 42 */     return FlatUIUtils.createSharedUI(FlatViewportUI.class, FlatViewportUI::new);
/*    */   }
/*    */ 
/*    */   
/*    */   public void update(Graphics g, JComponent c) {
/* 47 */     Component view = ((JViewport)c).getView();
/* 48 */     if (c.isOpaque() && view instanceof javax.swing.JTable) {
/*    */       
/* 50 */       g.setColor(view.getBackground());
/* 51 */       g.fillRect(0, 0, c.getWidth(), c.getHeight());
/*    */       
/* 53 */       paint(g, c);
/*    */     } else {
/* 55 */       super.update(g, c);
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\formdev\flatla\\ui\FlatViewportUI.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */