/*    */ package com.formdev.flatlaf.icons;
/*    */ 
/*    */ import java.awt.Component;
/*    */ import java.awt.Graphics2D;
/*    */ import java.awt.geom.Ellipse2D;
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
/*    */ public class FlatRadioButtonIcon
/*    */   extends FlatCheckBoxIcon
/*    */ {
/* 37 */   protected final int centerDiameter = getUIInt("RadioButton.icon.centerDiameter", 8, this.style);
/*    */ 
/*    */ 
/*    */   
/*    */   protected void paintFocusBorder(Component c, Graphics2D g) {
/* 42 */     int wh = 15 + this.focusWidth * 2;
/* 43 */     g.fillOval(-this.focusWidth, -this.focusWidth, wh, wh);
/*    */   }
/*    */ 
/*    */   
/*    */   protected void paintBorder(Component c, Graphics2D g) {
/* 48 */     g.fillOval(0, 0, 15, 15);
/*    */   }
/*    */ 
/*    */   
/*    */   protected void paintBackground(Component c, Graphics2D g) {
/* 53 */     g.fillOval(1, 1, 13, 13);
/*    */   }
/*    */ 
/*    */   
/*    */   protected void paintCheckmark(Component c, Graphics2D g) {
/* 58 */     float xy = (15 - this.centerDiameter) / 2.0F;
/* 59 */     g.fill(new Ellipse2D.Float(xy, xy, this.centerDiameter, this.centerDiameter));
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\formdev\flatlaf\icons\FlatRadioButtonIcon.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */