/*    */ package com.formdev.flatlaf.icons;
/*    */ 
/*    */ import java.awt.Shape;
/*    */ import java.awt.geom.Ellipse2D;
/*    */ import java.awt.geom.Path2D;
/*    */ import java.awt.geom.Rectangle2D;
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
/*    */ public class FlatOptionPaneErrorIcon
/*    */   extends FlatOptionPaneAbstractIcon
/*    */ {
/*    */   public FlatOptionPaneErrorIcon() {
/* 36 */     super("OptionPane.icon.errorColor", "Actions.Red");
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
/*    */ 
/*    */ 
/*    */   
/*    */   protected Shape createOutside() {
/* 51 */     return new Ellipse2D.Float(2.0F, 2.0F, 28.0F, 28.0F);
/*    */   }
/*    */ 
/*    */   
/*    */   protected Shape createInside() {
/* 56 */     Path2D inside = new Path2D.Float(0);
/* 57 */     inside.append(new Rectangle2D.Float(14.0F, 7.0F, 4.0F, 11.0F), false);
/* 58 */     inside.append(new Rectangle2D.Float(14.0F, 21.0F, 4.0F, 4.0F), false);
/* 59 */     return inside;
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\formdev\flatlaf\icons\FlatOptionPaneErrorIcon.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */