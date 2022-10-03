/*    */ package com.formdev.flatlaf.ui;
/*    */ 
/*    */ import com.formdev.flatlaf.util.UIScale;
/*    */ import java.awt.Color;
/*    */ import java.awt.Component;
/*    */ import java.awt.Graphics;
/*    */ import java.awt.Graphics2D;
/*    */ import java.awt.Insets;
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
/*    */ public class FlatLineBorder
/*    */   extends FlatEmptyBorder
/*    */ {
/*    */   private final Color lineColor;
/*    */   private final float lineThickness;
/*    */   
/*    */   public FlatLineBorder(Insets insets, Color lineColor) {
/* 42 */     this(insets, lineColor, 1.0F);
/*    */   }
/*    */   
/*    */   public FlatLineBorder(Insets insets, Color lineColor, float lineThickness) {
/* 46 */     super(insets);
/* 47 */     this.lineColor = lineColor;
/* 48 */     this.lineThickness = lineThickness;
/*    */   }
/*    */   
/*    */   public Color getLineColor() {
/* 52 */     return this.lineColor;
/*    */   }
/*    */   
/*    */   public float getLineThickness() {
/* 56 */     return this.lineThickness;
/*    */   }
/*    */ 
/*    */   
/*    */   public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
/* 61 */     Graphics2D g2 = (Graphics2D)g.create();
/*    */     try {
/* 63 */       FlatUIUtils.setRenderingHints(g2);
/* 64 */       g2.setColor(this.lineColor);
/* 65 */       FlatUIUtils.paintComponentBorder(g2, x, y, width, height, 0.0F, UIScale.scale(this.lineThickness), 0.0F);
/*    */     } finally {
/* 67 */       g2.dispose();
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\formdev\flatla\\ui\FlatLineBorder.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */