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
/*    */ public class FlatOptionPaneQuestionIcon
/*    */   extends FlatOptionPaneAbstractIcon
/*    */ {
/*    */   public FlatOptionPaneQuestionIcon() {
/* 36 */     super("OptionPane.icon.questionColor", "Actions.Blue");
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
/* 56 */     Path2D q = new Path2D.Float();
/* 57 */     q.moveTo(14.0D, 20.0D);
/* 58 */     q.lineTo(18.0D, 20.0D);
/* 59 */     q.curveTo(18.0D, 16.0D, 23.0D, 16.0D, 23.0D, 12.0D);
/* 60 */     q.curveTo(23.0D, 8.0D, 20.0D, 6.0D, 16.0D, 6.0D);
/* 61 */     q.curveTo(12.0D, 6.0D, 9.0D, 8.0D, 9.0D, 12.0D);
/* 62 */     q.curveTo(9.0D, 12.0D, 13.0D, 12.0D, 13.0D, 12.0D);
/* 63 */     q.curveTo(13.0D, 10.0D, 14.0D, 9.0D, 16.0D, 9.0D);
/* 64 */     q.curveTo(18.0D, 9.0D, 19.0D, 10.0D, 19.0D, 12.0D);
/* 65 */     q.curveTo(19.0D, 15.0D, 14.0D, 15.0D, 14.0D, 20.0D);
/* 66 */     q.closePath();
/*    */     
/* 68 */     Path2D inside = new Path2D.Float(0);
/* 69 */     inside.append(new Rectangle2D.Float(14.0F, 22.0F, 4.0F, 4.0F), false);
/* 70 */     inside.append(q, false);
/* 71 */     return inside;
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\formdev\flatlaf\icons\FlatOptionPaneQuestionIcon.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */