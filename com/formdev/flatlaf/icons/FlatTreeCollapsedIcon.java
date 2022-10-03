/*    */ package com.formdev.flatlaf.icons;
/*    */ 
/*    */ import com.formdev.flatlaf.ui.FlatUIUtils;
/*    */ import java.awt.Color;
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
/*    */ 
/*    */ public class FlatTreeCollapsedIcon
/*    */   extends FlatAbstractIcon
/*    */ {
/*    */   private final boolean chevron;
/*    */   
/*    */   public FlatTreeCollapsedIcon() {
/* 39 */     this(UIManager.getColor("Tree.icon.collapsedColor"));
/*    */   }
/*    */   
/*    */   FlatTreeCollapsedIcon(Color color) {
/* 43 */     super(11, 11, color);
/* 44 */     this.chevron = FlatUIUtils.isChevron(UIManager.getString("Component.arrowType"));
/*    */   }
/*    */ 
/*    */   
/*    */   protected void paintIcon(Component c, Graphics2D g) {
/* 49 */     rotate(c, g);
/*    */     
/* 51 */     if (this.chevron) {
/*    */       
/* 53 */       g.fill(FlatUIUtils.createPath(new double[] { 3.0D, 1.0D, 3.0D, 2.5D, 6.0D, 5.5D, 3.0D, 8.5D, 3.0D, 10.0D, 4.5D, 10.0D, 9.0D, 5.5D, 4.5D, 1.0D }));
/*    */     } else {
/*    */       
/* 56 */       g.fill(FlatUIUtils.createPath(new double[] { 2.0D, 1.0D, 2.0D, 10.0D, 10.0D, 5.5D }));
/*    */     } 
/*    */   }
/*    */   
/*    */   void rotate(Component c, Graphics2D g) {
/* 61 */     if (!c.getComponentOrientation().isLeftToRight())
/* 62 */       g.rotate(Math.toRadians(180.0D), this.width / 2.0D, this.height / 2.0D); 
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\formdev\flatlaf\icons\FlatTreeCollapsedIcon.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */