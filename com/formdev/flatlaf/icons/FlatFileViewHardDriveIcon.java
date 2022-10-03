/*    */ package com.formdev.flatlaf.icons;
/*    */ 
/*    */ import java.awt.Component;
/*    */ import java.awt.Graphics2D;
/*    */ import java.awt.geom.Path2D;
/*    */ import java.awt.geom.Rectangle2D;
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
/*    */ public class FlatFileViewHardDriveIcon
/*    */   extends FlatAbstractIcon
/*    */ {
/*    */   public FlatFileViewHardDriveIcon() {
/* 36 */     super(16, 16, UIManager.getColor("Objects.Grey"));
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   protected void paintIcon(Component c, Graphics2D g) {
/* 47 */     Path2D path = new Path2D.Float(0);
/* 48 */     path.append(new Rectangle2D.Float(2.0F, 6.0F, 12.0F, 4.0F), false);
/* 49 */     path.append(new Rectangle2D.Float(12.0F, 8.0F, 1.0F, 1.0F), false);
/* 50 */     path.append(new Rectangle2D.Float(10.0F, 8.0F, 1.0F, 1.0F), false);
/* 51 */     g.fill(path);
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\formdev\flatlaf\icons\FlatFileViewHardDriveIcon.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */