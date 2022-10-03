/*    */ package com.formdev.flatlaf.icons;
/*    */ 
/*    */ import java.awt.BasicStroke;
/*    */ import java.awt.Color;
/*    */ import java.awt.Component;
/*    */ import java.awt.Graphics2D;
/*    */ import java.awt.geom.Path2D;
/*    */ import javax.swing.AbstractButton;
/*    */ import javax.swing.JMenuItem;
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
/*    */ 
/*    */ 
/*    */ public class FlatCheckBoxMenuItemIcon
/*    */   extends FlatAbstractIcon
/*    */ {
/* 41 */   protected final Color checkmarkColor = UIManager.getColor("MenuItemCheckBox.icon.checkmarkColor");
/* 42 */   protected final Color disabledCheckmarkColor = UIManager.getColor("MenuItemCheckBox.icon.disabledCheckmarkColor");
/* 43 */   protected final Color selectionForeground = UIManager.getColor("MenuItem.selectionForeground");
/*    */   
/*    */   public FlatCheckBoxMenuItemIcon() {
/* 46 */     super(15, 15, null);
/*    */   }
/*    */ 
/*    */   
/*    */   protected void paintIcon(Component c, Graphics2D g2) {
/* 51 */     boolean selected = (c instanceof AbstractButton && ((AbstractButton)c).isSelected());
/*    */ 
/*    */     
/* 54 */     if (selected) {
/* 55 */       g2.setColor(getCheckmarkColor(c));
/* 56 */       paintCheckmark(g2);
/*    */     } 
/*    */   }
/*    */   
/*    */   protected void paintCheckmark(Graphics2D g2) {
/* 61 */     Path2D.Float path = new Path2D.Float();
/* 62 */     path.moveTo(4.5F, 7.5F);
/* 63 */     path.lineTo(6.6F, 10.0F);
/* 64 */     path.lineTo(11.25F, 3.5F);
/*    */     
/* 66 */     g2.setStroke(new BasicStroke(1.9F, 1, 1));
/* 67 */     g2.draw(path);
/*    */   }
/*    */   
/*    */   protected Color getCheckmarkColor(Component c) {
/* 71 */     if (c instanceof JMenuItem && ((JMenuItem)c).isArmed() && !isUnderlineSelection()) {
/* 72 */       return this.selectionForeground;
/*    */     }
/* 74 */     return c.isEnabled() ? this.checkmarkColor : this.disabledCheckmarkColor;
/*    */   }
/*    */ 
/*    */   
/*    */   protected boolean isUnderlineSelection() {
/* 79 */     return "underline".equals(UIManager.getString("MenuItem.selectionType"));
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\formdev\flatlaf\icons\FlatCheckBoxMenuItemIcon.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */