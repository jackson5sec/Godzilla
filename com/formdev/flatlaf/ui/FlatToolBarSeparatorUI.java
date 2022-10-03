/*     */ package com.formdev.flatlaf.ui;
/*     */ 
/*     */ import com.formdev.flatlaf.util.UIScale;
/*     */ import java.awt.Color;
/*     */ import java.awt.Dimension;
/*     */ import java.awt.Graphics;
/*     */ import java.awt.Graphics2D;
/*     */ import java.awt.geom.Rectangle2D;
/*     */ import java.util.function.Supplier;
/*     */ import javax.swing.JComponent;
/*     */ import javax.swing.JSeparator;
/*     */ import javax.swing.JToolBar;
/*     */ import javax.swing.UIManager;
/*     */ import javax.swing.plaf.ComponentUI;
/*     */ import javax.swing.plaf.basic.BasicToolBarSeparatorUI;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class FlatToolBarSeparatorUI
/*     */   extends BasicToolBarSeparatorUI
/*     */ {
/*     */   private static final int LINE_WIDTH = 1;
/*     */   protected int separatorWidth;
/*     */   protected Color separatorColor;
/*     */   private boolean defaults_initialized = false;
/*     */   
/*     */   public static ComponentUI createUI(JComponent c) {
/*  54 */     return FlatUIUtils.createSharedUI(FlatToolBarSeparatorUI.class, FlatToolBarSeparatorUI::new);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void installDefaults(JSeparator c) {
/*  59 */     super.installDefaults(c);
/*     */     
/*  61 */     if (!this.defaults_initialized) {
/*  62 */       this.separatorWidth = UIManager.getInt("ToolBar.separatorWidth");
/*  63 */       this.separatorColor = UIManager.getColor("ToolBar.separatorColor");
/*     */       
/*  65 */       this.defaults_initialized = true;
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/*  70 */     c.setAlignmentX(0.0F);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void uninstallDefaults(JSeparator s) {
/*  75 */     super.uninstallDefaults(s);
/*  76 */     this.defaults_initialized = false;
/*     */   }
/*     */ 
/*     */   
/*     */   public Dimension getPreferredSize(JComponent c) {
/*  81 */     Dimension size = ((JToolBar.Separator)c).getSeparatorSize();
/*     */     
/*  83 */     if (size != null) {
/*  84 */       return UIScale.scale(size);
/*     */     }
/*     */     
/*  87 */     int sepWidth = UIScale.scale((this.separatorWidth - 1) / 2) * 2 + UIScale.scale(1);
/*     */     
/*  89 */     boolean vertical = isVertical(c);
/*  90 */     return new Dimension(vertical ? sepWidth : 0, vertical ? 0 : sepWidth);
/*     */   }
/*     */ 
/*     */   
/*     */   public Dimension getMaximumSize(JComponent c) {
/*  95 */     Dimension size = getPreferredSize(c);
/*  96 */     if (isVertical(c)) {
/*  97 */       return new Dimension(size.width, 32767);
/*     */     }
/*  99 */     return new Dimension(32767, size.height);
/*     */   }
/*     */ 
/*     */   
/*     */   public void paint(Graphics g, JComponent c) {
/* 104 */     int width = c.getWidth();
/* 105 */     int height = c.getHeight();
/* 106 */     float lineWidth = UIScale.scale(1.0F);
/* 107 */     float offset = UIScale.scale(2.0F);
/*     */     
/* 109 */     Object[] oldRenderingHints = FlatUIUtils.setRenderingHints(g);
/* 110 */     g.setColor(this.separatorColor);
/*     */     
/* 112 */     if (isVertical(c)) {
/* 113 */       ((Graphics2D)g).fill(new Rectangle2D.Float(Math.round((width - lineWidth) / 2.0F), offset, lineWidth, height - offset * 2.0F));
/*     */     } else {
/* 115 */       ((Graphics2D)g).fill(new Rectangle2D.Float(offset, Math.round((height - lineWidth) / 2.0F), width - offset * 2.0F, lineWidth));
/*     */     } 
/* 117 */     FlatUIUtils.resetRenderingHints(g, oldRenderingHints);
/*     */   }
/*     */   
/*     */   private boolean isVertical(JComponent c) {
/* 121 */     return (((JToolBar.Separator)c).getOrientation() == 1);
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\formdev\flatla\\ui\FlatToolBarSeparatorUI.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */