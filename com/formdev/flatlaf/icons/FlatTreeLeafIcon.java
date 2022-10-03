/*    */ package com.formdev.flatlaf.icons;
/*    */ 
/*    */ import com.formdev.flatlaf.ui.FlatUIUtils;
/*    */ import java.awt.Component;
/*    */ import java.awt.Graphics2D;
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
/*    */ public class FlatTreeLeafIcon
/*    */   extends FlatAbstractIcon
/*    */ {
/*    */   public FlatTreeLeafIcon() {
/* 35 */     super(16, 16, UIManager.getColor("Tree.icon.leafColor"));
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
/* 49 */     g.fill(FlatUIUtils.createPath(new double[] { 8.0D, 6.0D, 8.0D, 1.0D, 13.0D, 1.0D, 13.0D, 15.0D, 3.0D, 15.0D, 3.0D, 6.0D }));
/* 50 */     g.fill(FlatUIUtils.createPath(new double[] { 3.0D, 5.0D, 7.0D, 5.0D, 7.0D, 1.0D }));
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\formdev\flatlaf\icons\FlatTreeLeafIcon.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */