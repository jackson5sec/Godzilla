/*    */ package com.formdev.flatlaf.icons;
/*    */ 
/*    */ import com.formdev.flatlaf.ui.FlatUIUtils;
/*    */ import java.awt.Component;
/*    */ import java.awt.Graphics2D;
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
/*    */ public class FlatFileViewFloppyDriveIcon
/*    */   extends FlatAbstractIcon
/*    */ {
/*    */   public FlatFileViewFloppyDriveIcon() {
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
/* 51 */     path.append(FlatUIUtils.createPath(new double[] { 11.0D, 14.0D, 11.0D, 11.0D, 5.0D, 11.0D, 5.0D, 14.0D, 2.0D, 14.0D, 2.0D, 2.0D, 14.0D, 2.0D, 14.0D, 14.0D, 11.0D, 14.0D }, ), false);
/* 52 */     path.append(FlatUIUtils.createPath(new double[] { 4.0D, 4.0D, 4.0D, 8.0D, 12.0D, 8.0D, 12.0D, 4.0D, 4.0D, 4.0D }, ), false);
/* 53 */     g.fill(path);
/*    */     
/* 55 */     g.fillRect(6, 12, 4, 2);
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\formdev\flatlaf\icons\FlatFileViewFloppyDriveIcon.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */