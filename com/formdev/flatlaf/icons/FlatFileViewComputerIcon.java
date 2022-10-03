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
/*    */ public class FlatFileViewComputerIcon
/*    */   extends FlatAbstractIcon
/*    */ {
/*    */   public FlatFileViewComputerIcon() {
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
/*    */ 
/*    */ 
/*    */   
/*    */   protected void paintIcon(Component c, Graphics2D g) {
/* 50 */     Path2D path = new Path2D.Float(0);
/* 51 */     path.append(new Rectangle2D.Float(2.0F, 3.0F, 12.0F, 8.0F), false);
/* 52 */     path.append(new Rectangle2D.Float(4.0F, 5.0F, 8.0F, 4.0F), false);
/* 53 */     g.fill(path);
/*    */     
/* 55 */     g.fillRect(2, 12, 12, 2);
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\formdev\flatlaf\icons\FlatFileViewComputerIcon.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */