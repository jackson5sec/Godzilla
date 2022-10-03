/*    */ package com.formdev.flatlaf.demo.intellijthemes;
/*    */ 
/*    */ import com.formdev.flatlaf.ui.FlatUIUtils;
/*    */ import com.formdev.flatlaf.util.UIScale;
/*    */ import java.awt.Component;
/*    */ import java.awt.FontMetrics;
/*    */ import java.awt.Graphics;
/*    */ import java.awt.Graphics2D;
/*    */ import java.awt.Insets;
/*    */ import java.awt.geom.Rectangle2D;
/*    */ import javax.swing.JList;
/*    */ import javax.swing.UIManager;
/*    */ import javax.swing.border.Border;
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
/*    */ class ListCellTitledBorder
/*    */   implements Border
/*    */ {
/*    */   private final JList<?> list;
/*    */   private final String title;
/*    */   
/*    */   ListCellTitledBorder(JList<?> list, String title) {
/* 37 */     this.list = list;
/* 38 */     this.title = title;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean isBorderOpaque() {
/* 43 */     return true;
/*    */   }
/*    */ 
/*    */   
/*    */   public Insets getBorderInsets(Component c) {
/* 48 */     int height = c.getFontMetrics(this.list.getFont()).getHeight();
/* 49 */     return new Insets(height, 0, 0, 0);
/*    */   }
/*    */ 
/*    */   
/*    */   public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
/* 54 */     FontMetrics fm = c.getFontMetrics(this.list.getFont());
/* 55 */     int titleWidth = fm.stringWidth(this.title);
/* 56 */     int titleHeight = fm.getHeight();
/*    */ 
/*    */     
/* 59 */     g.setColor(this.list.getBackground());
/* 60 */     g.fillRect(x, y, width, titleHeight);
/*    */     
/* 62 */     int gap = UIScale.scale(4);
/*    */     
/* 64 */     Graphics2D g2 = (Graphics2D)g.create();
/*    */     try {
/* 66 */       FlatUIUtils.setRenderingHints(g2);
/*    */       
/* 68 */       g2.setColor(UIManager.getColor("Label.disabledForeground"));
/*    */ 
/*    */       
/* 71 */       int sepWidth = (width - titleWidth) / 2 - gap - gap;
/* 72 */       if (sepWidth > 0) {
/* 73 */         int sy = y + Math.round(titleHeight / 2.0F);
/* 74 */         float sepHeight = UIScale.scale(1.0F);
/*    */         
/* 76 */         g2.fill(new Rectangle2D.Float((x + gap), sy, sepWidth, sepHeight));
/* 77 */         g2.fill(new Rectangle2D.Float((x + width - gap - sepWidth), sy, sepWidth, sepHeight));
/*    */       } 
/*    */ 
/*    */       
/* 81 */       int xt = x + (width - titleWidth) / 2;
/* 82 */       int yt = y + fm.getAscent();
/*    */       
/* 84 */       FlatUIUtils.drawString(this.list, g2, this.title, xt, yt);
/*    */     } finally {
/* 86 */       g2.dispose();
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\formdev\flatlaf\demo\intellijthemes\ListCellTitledBorder.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */