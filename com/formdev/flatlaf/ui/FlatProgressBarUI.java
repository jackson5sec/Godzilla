/*     */ package com.formdev.flatlaf.ui;
/*     */ 
/*     */ import com.formdev.flatlaf.FlatClientProperties;
/*     */ import com.formdev.flatlaf.util.HiDPIUtils;
/*     */ import com.formdev.flatlaf.util.UIScale;
/*     */ import java.awt.Dimension;
/*     */ import java.awt.FontMetrics;
/*     */ import java.awt.Graphics;
/*     */ import java.awt.Graphics2D;
/*     */ import java.awt.Insets;
/*     */ import java.awt.geom.Area;
/*     */ import java.awt.geom.RoundRectangle2D;
/*     */ import java.beans.PropertyChangeEvent;
/*     */ import java.beans.PropertyChangeListener;
/*     */ import javax.swing.JComponent;
/*     */ import javax.swing.LookAndFeel;
/*     */ import javax.swing.UIManager;
/*     */ import javax.swing.plaf.ComponentUI;
/*     */ import javax.swing.plaf.basic.BasicProgressBarUI;
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
/*     */ public class FlatProgressBarUI
/*     */   extends BasicProgressBarUI
/*     */ {
/*     */   protected int arc;
/*     */   protected Dimension horizontalSize;
/*     */   protected Dimension verticalSize;
/*     */   private PropertyChangeListener propertyChangeListener;
/*     */   
/*     */   public static ComponentUI createUI(JComponent c) {
/*  69 */     return new FlatProgressBarUI();
/*     */   }
/*     */ 
/*     */   
/*     */   protected void installDefaults() {
/*  74 */     super.installDefaults();
/*     */     
/*  76 */     LookAndFeel.installProperty(this.progressBar, "opaque", Boolean.valueOf(false));
/*     */     
/*  78 */     this.arc = UIManager.getInt("ProgressBar.arc");
/*  79 */     this.horizontalSize = UIManager.getDimension("ProgressBar.horizontalSize");
/*  80 */     this.verticalSize = UIManager.getDimension("ProgressBar.verticalSize");
/*     */   }
/*     */ 
/*     */   
/*     */   protected void installListeners() {
/*  85 */     super.installListeners();
/*     */     
/*  87 */     this.propertyChangeListener = (e -> {
/*     */         switch (e.getPropertyName()) {
/*     */           case "JProgressBar.largeHeight":
/*     */           case "JProgressBar.square":
/*     */             this.progressBar.revalidate();
/*     */             this.progressBar.repaint();
/*     */             break;
/*     */         } 
/*     */       });
/*  96 */     this.progressBar.addPropertyChangeListener(this.propertyChangeListener);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void uninstallListeners() {
/* 101 */     super.uninstallListeners();
/*     */     
/* 103 */     this.progressBar.removePropertyChangeListener(this.propertyChangeListener);
/* 104 */     this.propertyChangeListener = null;
/*     */   }
/*     */ 
/*     */   
/*     */   public Dimension getPreferredSize(JComponent c) {
/* 109 */     Dimension size = super.getPreferredSize(c);
/*     */     
/* 111 */     if (this.progressBar.isStringPainted() || FlatClientProperties.clientPropertyBoolean(c, "JProgressBar.largeHeight", false)) {
/*     */       
/* 113 */       Insets insets = this.progressBar.getInsets();
/* 114 */       FontMetrics fm = this.progressBar.getFontMetrics(this.progressBar.getFont());
/* 115 */       if (this.progressBar.getOrientation() == 0) {
/* 116 */         size.height = Math.max(fm.getHeight() + insets.top + insets.bottom, (getPreferredInnerHorizontal()).height);
/*     */       } else {
/* 118 */         size.width = Math.max(fm.getHeight() + insets.left + insets.right, (getPreferredInnerVertical()).width);
/*     */       } 
/*     */     } 
/* 121 */     return size;
/*     */   }
/*     */ 
/*     */   
/*     */   protected Dimension getPreferredInnerHorizontal() {
/* 126 */     return UIScale.scale(this.horizontalSize);
/*     */   }
/*     */ 
/*     */   
/*     */   protected Dimension getPreferredInnerVertical() {
/* 131 */     return UIScale.scale(this.verticalSize);
/*     */   }
/*     */ 
/*     */   
/*     */   public void update(Graphics g, JComponent c) {
/* 136 */     if (c.isOpaque()) {
/* 137 */       FlatUIUtils.paintParentBackground(g, c);
/*     */     }
/* 139 */     paint(g, c);
/*     */   }
/*     */ 
/*     */   
/*     */   public void paint(Graphics g, JComponent c) {
/* 144 */     Insets insets = this.progressBar.getInsets();
/* 145 */     int x = insets.left;
/* 146 */     int y = insets.top;
/* 147 */     int width = this.progressBar.getWidth() - insets.right + insets.left;
/* 148 */     int height = this.progressBar.getHeight() - insets.top + insets.bottom;
/*     */     
/* 150 */     if (width <= 0 || height <= 0) {
/*     */       return;
/*     */     }
/* 153 */     boolean horizontal = (this.progressBar.getOrientation() == 0);
/*     */ 
/*     */     
/* 156 */     int arc = FlatClientProperties.clientPropertyBoolean(c, "JProgressBar.square", false) ? 0 : Math.min(UIScale.scale(this.arc), horizontal ? height : width);
/*     */     
/* 158 */     Object[] oldRenderingHints = FlatUIUtils.setRenderingHints(g);
/*     */ 
/*     */     
/* 161 */     RoundRectangle2D.Float trackShape = new RoundRectangle2D.Float(x, y, width, height, arc, arc);
/* 162 */     g.setColor(this.progressBar.getBackground());
/* 163 */     ((Graphics2D)g).fill(trackShape);
/*     */ 
/*     */     
/* 166 */     int amountFull = 0;
/* 167 */     if (this.progressBar.isIndeterminate()) {
/* 168 */       this.boxRect = getBox(this.boxRect);
/* 169 */       if (this.boxRect != null) {
/* 170 */         g.setColor(this.progressBar.getForeground());
/* 171 */         ((Graphics2D)g).fill(new RoundRectangle2D.Float(this.boxRect.x, this.boxRect.y, this.boxRect.width, this.boxRect.height, arc, arc));
/*     */       } 
/*     */     } else {
/*     */       
/* 175 */       amountFull = getAmountFull(insets, width, height);
/*     */       
/* 177 */       if (horizontal) {  } else {  }
/* 178 */        RoundRectangle2D.Float progressShape = new RoundRectangle2D.Float(x, (y + height - amountFull), width, amountFull, arc, arc);
/*     */ 
/*     */ 
/*     */       
/* 182 */       g.setColor(this.progressBar.getForeground());
/* 183 */       if (amountFull < (horizontal ? height : width)) {
/*     */         
/* 185 */         Area area = new Area(trackShape);
/* 186 */         area.intersect(new Area(progressShape));
/* 187 */         ((Graphics2D)g).fill(area);
/*     */       } else {
/* 189 */         ((Graphics2D)g).fill(progressShape);
/*     */       } 
/*     */     } 
/* 192 */     FlatUIUtils.resetRenderingHints(g, oldRenderingHints);
/*     */     
/* 194 */     if (this.progressBar.isStringPainted()) {
/* 195 */       paintString(g, x, y, width, height, amountFull, insets);
/*     */     }
/*     */   }
/*     */   
/*     */   protected void paintString(Graphics g, int x, int y, int width, int height, int amountFull, Insets b) {
/* 200 */     super.paintString(HiDPIUtils.createGraphicsTextYCorrection((Graphics2D)g), x, y, width, height, amountFull, b);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void setAnimationIndex(int newValue) {
/* 205 */     super.setAnimationIndex(newValue);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 213 */     double systemScaleFactor = UIScale.getSystemScaleFactor(this.progressBar.getGraphicsConfiguration());
/* 214 */     if ((int)systemScaleFactor != systemScaleFactor)
/* 215 */       this.progressBar.repaint(); 
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\formdev\flatla\\ui\FlatProgressBarUI.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */