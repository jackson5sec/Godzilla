/*    */ package com.formdev.flatlaf.icons;
/*    */ 
/*    */ import com.formdev.flatlaf.ui.FlatButtonUI;
/*    */ import com.formdev.flatlaf.ui.FlatUIUtils;
/*    */ import java.awt.BasicStroke;
/*    */ import java.awt.Color;
/*    */ import java.awt.Component;
/*    */ import java.awt.Dimension;
/*    */ import java.awt.Graphics2D;
/*    */ import java.awt.geom.Line2D;
/*    */ import java.awt.geom.Path2D;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class FlatTabbedPaneCloseIcon
/*    */   extends FlatAbstractIcon
/*    */ {
/* 50 */   protected final Dimension size = UIManager.getDimension("TabbedPane.closeSize");
/* 51 */   protected final int arc = UIManager.getInt("TabbedPane.closeArc");
/* 52 */   protected final float crossPlainSize = FlatUIUtils.getUIFloat("TabbedPane.closeCrossPlainSize", 7.5F);
/* 53 */   protected final float crossFilledSize = FlatUIUtils.getUIFloat("TabbedPane.closeCrossFilledSize", this.crossPlainSize);
/* 54 */   protected final float closeCrossLineWidth = FlatUIUtils.getUIFloat("TabbedPane.closeCrossLineWidth", 1.0F);
/* 55 */   protected final Color background = UIManager.getColor("TabbedPane.closeBackground");
/* 56 */   protected final Color foreground = UIManager.getColor("TabbedPane.closeForeground");
/* 57 */   protected final Color hoverBackground = UIManager.getColor("TabbedPane.closeHoverBackground");
/* 58 */   protected final Color hoverForeground = UIManager.getColor("TabbedPane.closeHoverForeground");
/* 59 */   protected final Color pressedBackground = UIManager.getColor("TabbedPane.closePressedBackground");
/* 60 */   protected final Color pressedForeground = UIManager.getColor("TabbedPane.closePressedForeground");
/*    */   
/*    */   public FlatTabbedPaneCloseIcon() {
/* 63 */     super(16, 16, null);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   protected void paintIcon(Component c, Graphics2D g) {
/* 69 */     Color bg = FlatButtonUI.buttonStateColor(c, this.background, null, null, this.hoverBackground, this.pressedBackground);
/* 70 */     if (bg != null) {
/* 71 */       g.setColor(FlatUIUtils.deriveColor(bg, c.getBackground()));
/* 72 */       g.fillRoundRect((this.width - this.size.width) / 2, (this.height - this.size.height) / 2, this.size.width, this.size.height, this.arc, this.arc);
/*    */     } 
/*    */ 
/*    */ 
/*    */     
/* 77 */     Color fg = FlatButtonUI.buttonStateColor(c, this.foreground, null, null, this.hoverForeground, this.pressedForeground);
/* 78 */     g.setColor(FlatUIUtils.deriveColor(fg, c.getForeground()));
/*    */     
/* 80 */     float mx = (this.width / 2);
/* 81 */     float my = (this.height / 2);
/* 82 */     float r = ((bg != null) ? this.crossFilledSize : this.crossPlainSize) / 2.0F;
/*    */ 
/*    */     
/* 85 */     Path2D path = new Path2D.Float(0);
/* 86 */     path.append(new Line2D.Float(mx - r, my - r, mx + r, my + r), false);
/* 87 */     path.append(new Line2D.Float(mx - r, my + r, mx + r, my - r), false);
/* 88 */     g.setStroke(new BasicStroke(this.closeCrossLineWidth));
/* 89 */     g.draw(path);
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\formdev\flatlaf\icons\FlatTabbedPaneCloseIcon.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */