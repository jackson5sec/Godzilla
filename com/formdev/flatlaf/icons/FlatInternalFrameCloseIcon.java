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
/*    */ 
/*    */ public class FlatInternalFrameCloseIcon
/*    */   extends FlatInternalFrameAbstractIcon
/*    */ {
/* 42 */   private final Color hoverForeground = UIManager.getColor("InternalFrame.closeHoverForeground");
/* 43 */   private final Color pressedForeground = UIManager.getColor("InternalFrame.closePressedForeground");
/*    */   
/*    */   public FlatInternalFrameCloseIcon() {
/* 46 */     super(UIManager.getDimension("InternalFrame.buttonSize"), 
/* 47 */         UIManager.getColor("InternalFrame.closeHoverBackground"), 
/* 48 */         UIManager.getColor("InternalFrame.closePressedBackground"));
/*    */   }
/*    */ 
/*    */   
/*    */   protected void paintIcon(Component c, Graphics2D g) {
/* 53 */     paintBackground(c, g);
/*    */     
/* 55 */     g.setColor(FlatButtonUI.buttonStateColor(c, c.getForeground(), null, null, this.hoverForeground, this.pressedForeground));
/*    */     
/* 57 */     float mx = (this.width / 2);
/* 58 */     float my = (this.height / 2);
/* 59 */     float r = 3.25F;
/*    */     
/* 61 */     Path2D path = new Path2D.Float(0);
/* 62 */     path.append(new Line2D.Float(mx - r, my - r, mx + r, my + r), false);
/* 63 */     path.append(new Line2D.Float(mx - r, my + r, mx + r, my - r), false);
/* 64 */     g.setStroke(new BasicStroke(1.0F));
/* 65 */     g.draw(path);
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\formdev\flatlaf\icons\FlatInternalFrameCloseIcon.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */