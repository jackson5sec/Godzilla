/*    */ package com.formdev.flatlaf.ui;
/*    */ 
/*    */ import com.formdev.flatlaf.util.UIScale;
/*    */ import java.awt.Color;
/*    */ import java.awt.Component;
/*    */ import java.awt.Graphics;
/*    */ import java.awt.Insets;
/*    */ import javax.swing.JMenuBar;
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
/*    */ public class FlatMenuBarBorder
/*    */   extends FlatMarginBorder
/*    */ {
/* 37 */   private final Color borderColor = UIManager.getColor("MenuBar.borderColor");
/*    */ 
/*    */   
/*    */   public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
/* 41 */     float lineHeight = UIScale.scale(1.0F);
/* 42 */     FlatUIUtils.paintFilledRectangle(g, this.borderColor, x, (y + height) - lineHeight, width, lineHeight);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public Insets getBorderInsets(Component c, Insets insets) {
/* 48 */     Insets margin = (c instanceof JMenuBar) ? ((JMenuBar)c).getMargin() : new Insets(0, 0, 0, 0);
/*    */     
/* 50 */     insets.top = UIScale.scale(margin.top);
/* 51 */     insets.left = UIScale.scale(margin.left);
/* 52 */     insets.bottom = UIScale.scale(margin.bottom + 1);
/* 53 */     insets.right = UIScale.scale(margin.right);
/* 54 */     return insets;
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\formdev\flatla\\ui\FlatMenuBarBorder.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */