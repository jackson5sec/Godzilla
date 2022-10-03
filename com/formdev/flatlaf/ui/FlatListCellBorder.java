/*    */ package com.formdev.flatlaf.ui;
/*    */ 
/*    */ import java.awt.Component;
/*    */ import java.awt.Graphics;
/*    */ import javax.swing.JList;
/*    */ import javax.swing.SwingUtilities;
/*    */ import javax.swing.UIManager;
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
/*    */ public class FlatListCellBorder
/*    */   extends FlatLineBorder
/*    */ {
/* 36 */   final boolean showCellFocusIndicator = UIManager.getBoolean("List.showCellFocusIndicator");
/*    */   
/*    */   protected FlatListCellBorder() {
/* 39 */     super(UIManager.getInsets("List.cellMargins"), UIManager.getColor("List.cellFocusColor"));
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static class Default
/*    */     extends FlatListCellBorder
/*    */   {
/*    */     public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {}
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static class Focused
/*    */     extends FlatListCellBorder {}
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static class Selected
/*    */     extends FlatListCellBorder
/*    */   {
/*    */     public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
/* 77 */       if (!this.showCellFocusIndicator) {
/*    */         return;
/*    */       }
/*    */       
/* 81 */       JList<?> list = (JList)SwingUtilities.getAncestorOfClass(JList.class, c);
/* 82 */       if (list != null && list.getMinSelectionIndex() == list.getMaxSelectionIndex()) {
/*    */         return;
/*    */       }
/* 85 */       super.paintBorder(c, g, x, y, width, height);
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\formdev\flatla\\ui\FlatListCellBorder.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */