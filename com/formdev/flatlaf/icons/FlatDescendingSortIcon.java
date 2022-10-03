/*    */ package com.formdev.flatlaf.icons;
/*    */ 
/*    */ import com.formdev.flatlaf.ui.FlatUIUtils;
/*    */ import java.awt.BasicStroke;
/*    */ import java.awt.Color;
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
/*    */ 
/*    */ public class FlatDescendingSortIcon
/*    */   extends FlatAbstractIcon
/*    */ {
/* 38 */   protected final boolean chevron = FlatUIUtils.isChevron(UIManager.getString("Component.arrowType"));
/* 39 */   protected final Color sortIconColor = UIManager.getColor("Table.sortIconColor");
/*    */   
/*    */   public FlatDescendingSortIcon() {
/* 42 */     super(10, 5, null);
/*    */   }
/*    */ 
/*    */   
/*    */   protected void paintIcon(Component c, Graphics2D g) {
/* 47 */     g.setColor(this.sortIconColor);
/* 48 */     if (this.chevron) {
/*    */       
/* 50 */       Path2D path = FlatUIUtils.createPath(false, new double[] { 1.0D, 0.0D, 5.0D, 4.0D, 9.0D, 0.0D });
/* 51 */       g.setStroke(new BasicStroke(1.0F));
/* 52 */       g.draw(path);
/*    */     } else {
/*    */       
/* 55 */       g.fill(FlatUIUtils.createPath(new double[] { 0.5D, 0.0D, 5.0D, 5.0D, 9.5D, 0.0D }));
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\formdev\flatlaf\icons\FlatDescendingSortIcon.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */