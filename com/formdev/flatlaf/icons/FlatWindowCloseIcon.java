/*    */ package com.formdev.flatlaf.icons;
/*    */ 
/*    */ import com.formdev.flatlaf.ui.FlatButtonUI;
/*    */ import java.awt.BasicStroke;
/*    */ import java.awt.Color;
/*    */ import java.awt.Component;
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
/*    */ public class FlatWindowCloseIcon
/*    */   extends FlatWindowAbstractIcon
/*    */ {
/* 41 */   private final Color hoverForeground = UIManager.getColor("TitlePane.closeHoverForeground");
/* 42 */   private final Color pressedForeground = UIManager.getColor("TitlePane.closePressedForeground");
/*    */   
/*    */   public FlatWindowCloseIcon() {
/* 45 */     super(UIManager.getDimension("TitlePane.buttonSize"), 
/* 46 */         UIManager.getColor("TitlePane.closeHoverBackground"), 
/* 47 */         UIManager.getColor("TitlePane.closePressedBackground"));
/*    */   }
/*    */ 
/*    */   
/*    */   protected void paintIconAt1x(Graphics2D g, int x, int y, int width, int height, double scaleFactor) {
/* 52 */     int iwh = (int)(10.0D * scaleFactor);
/* 53 */     int ix = x + (width - iwh) / 2;
/* 54 */     int iy = y + (height - iwh) / 2;
/* 55 */     int ix2 = ix + iwh - 1;
/* 56 */     int iy2 = iy + iwh - 1;
/* 57 */     int thickness = (int)scaleFactor;
/*    */     
/* 59 */     Path2D path = new Path2D.Float(0);
/* 60 */     path.append(new Line2D.Float(ix, iy, ix2, iy2), false);
/* 61 */     path.append(new Line2D.Float(ix, iy2, ix2, iy), false);
/* 62 */     g.setStroke(new BasicStroke(thickness));
/* 63 */     g.draw(path);
/*    */   }
/*    */ 
/*    */   
/*    */   protected Color getForeground(Component c) {
/* 68 */     return FlatButtonUI.buttonStateColor(c, c.getForeground(), null, null, this.hoverForeground, this.pressedForeground);
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\formdev\flatlaf\icons\FlatWindowCloseIcon.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */