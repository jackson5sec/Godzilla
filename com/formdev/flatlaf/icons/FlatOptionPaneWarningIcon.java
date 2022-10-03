/*    */ package com.formdev.flatlaf.icons;
/*    */ 
/*    */ import com.formdev.flatlaf.ui.FlatUIUtils;
/*    */ import java.awt.Shape;
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
/*    */ public class FlatOptionPaneWarningIcon
/*    */   extends FlatOptionPaneAbstractIcon
/*    */ {
/*    */   public FlatOptionPaneWarningIcon() {
/* 36 */     super("OptionPane.icon.warningColor", "Actions.Yellow");
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
/* 51 */     return FlatUIUtils.createPath(new double[] { 16.0D, 2.0D, 31.0D, 28.0D, 1.0D, 28.0D });
/*    */   }
/*    */ 
/*    */   
/*    */   protected Shape createInside() {
/* 56 */     Path2D inside = new Path2D.Float(0);
/* 57 */     inside.append(new Rectangle2D.Float(14.0F, 10.0F, 4.0F, 8.0F), false);
/* 58 */     inside.append(new Rectangle2D.Float(14.0F, 21.0F, 4.0F, 4.0F), false);
/* 59 */     return inside;
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\formdev\flatlaf\icons\FlatOptionPaneWarningIcon.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */