/*    */ package com.formdev.flatlaf.icons;
/*    */ 
/*    */ import com.formdev.flatlaf.ui.FlatUIUtils;
/*    */ import java.awt.BasicStroke;
/*    */ import java.awt.Color;
/*    */ import java.awt.Component;
/*    */ import java.awt.Graphics2D;
/*    */ import java.awt.geom.Path2D;
/*    */ import javax.swing.JMenu;
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
/*    */ 
/*    */ public class FlatMenuArrowIcon
/*    */   extends FlatAbstractIcon
/*    */ {
/* 42 */   protected final boolean chevron = FlatUIUtils.isChevron(UIManager.getString("Component.arrowType"));
/* 43 */   protected final Color arrowColor = UIManager.getColor("Menu.icon.arrowColor");
/* 44 */   protected final Color disabledArrowColor = UIManager.getColor("Menu.icon.disabledArrowColor");
/* 45 */   protected final Color selectionForeground = UIManager.getColor("Menu.selectionForeground");
/*    */   
/*    */   public FlatMenuArrowIcon() {
/* 48 */     super(6, 10, null);
/*    */   }
/*    */ 
/*    */   
/*    */   protected void paintIcon(Component c, Graphics2D g) {
/* 53 */     if (!c.getComponentOrientation().isLeftToRight()) {
/* 54 */       g.rotate(Math.toRadians(180.0D), this.width / 2.0D, this.height / 2.0D);
/*    */     }
/* 56 */     g.setColor(getArrowColor(c));
/* 57 */     if (this.chevron) {
/*    */       
/* 59 */       Path2D path = FlatUIUtils.createPath(false, new double[] { 1.0D, 1.0D, 5.0D, 5.0D, 1.0D, 9.0D });
/* 60 */       g.setStroke(new BasicStroke(1.0F));
/* 61 */       g.draw(path);
/*    */     } else {
/*    */       
/* 64 */       g.fill(FlatUIUtils.createPath(new double[] { 0.0D, 0.5D, 5.0D, 5.0D, 0.0D, 9.5D }));
/*    */     } 
/*    */   }
/*    */   
/*    */   protected Color getArrowColor(Component c) {
/* 69 */     if (c instanceof JMenu && ((JMenu)c).isSelected() && !isUnderlineSelection()) {
/* 70 */       return this.selectionForeground;
/*    */     }
/* 72 */     return c.isEnabled() ? this.arrowColor : this.disabledArrowColor;
/*    */   }
/*    */ 
/*    */   
/*    */   protected boolean isUnderlineSelection() {
/* 77 */     return "underline".equals(UIManager.getString("MenuItem.selectionType"));
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\formdev\flatlaf\icons\FlatMenuArrowIcon.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */