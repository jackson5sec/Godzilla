/*    */ package com.formdev.flatlaf.demo;
/*    */ 
/*    */ import com.formdev.flatlaf.util.UIScale;
/*    */ import java.awt.Dimension;
/*    */ import java.awt.Rectangle;
/*    */ import javax.swing.JPanel;
/*    */ import javax.swing.Scrollable;
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
/*    */ public class ScrollablePanel
/*    */   extends JPanel
/*    */   implements Scrollable
/*    */ {
/*    */   public Dimension getPreferredScrollableViewportSize() {
/* 37 */     return UIScale.scale(new Dimension(400, 400));
/*    */   }
/*    */ 
/*    */   
/*    */   public int getScrollableUnitIncrement(Rectangle visibleRect, int orientation, int direction) {
/* 42 */     return UIScale.scale(50);
/*    */   }
/*    */ 
/*    */   
/*    */   public int getScrollableBlockIncrement(Rectangle visibleRect, int orientation, int direction) {
/* 47 */     return (orientation == 1) ? visibleRect.height : visibleRect.width;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean getScrollableTracksViewportWidth() {
/* 52 */     return false;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean getScrollableTracksViewportHeight() {
/* 57 */     return false;
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\formdev\flatlaf\demo\ScrollablePanel.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */