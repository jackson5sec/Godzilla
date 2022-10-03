/*     */ package com.formdev.flatlaf.ui;
/*     */ 
/*     */ import com.formdev.flatlaf.util.UIScale;
/*     */ import java.awt.Color;
/*     */ import java.awt.Component;
/*     */ import java.awt.Graphics;
/*     */ import java.awt.Graphics2D;
/*     */ import java.awt.Insets;
/*     */ import javax.swing.JComponent;
/*     */ import javax.swing.JInternalFrame;
/*     */ import javax.swing.LookAndFeel;
/*     */ import javax.swing.UIManager;
/*     */ import javax.swing.plaf.ComponentUI;
/*     */ import javax.swing.plaf.basic.BasicInternalFrameUI;
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
/*     */ public class FlatInternalFrameUI
/*     */   extends BasicInternalFrameUI
/*     */ {
/*     */   protected FlatWindowResizer windowResizer;
/*     */   
/*     */   public static ComponentUI createUI(JComponent c) {
/*  90 */     return new FlatInternalFrameUI((JInternalFrame)c);
/*     */   }
/*     */   
/*     */   public FlatInternalFrameUI(JInternalFrame b) {
/*  94 */     super(b);
/*     */   }
/*     */ 
/*     */   
/*     */   public void installUI(JComponent c) {
/*  99 */     super.installUI(c);
/*     */     
/* 101 */     LookAndFeel.installProperty(this.frame, "opaque", Boolean.valueOf(false));
/*     */     
/* 103 */     this.windowResizer = createWindowResizer();
/*     */   }
/*     */ 
/*     */   
/*     */   public void uninstallUI(JComponent c) {
/* 108 */     super.uninstallUI(c);
/*     */     
/* 110 */     if (this.windowResizer != null) {
/* 111 */       this.windowResizer.uninstall();
/* 112 */       this.windowResizer = null;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   protected JComponent createNorthPane(JInternalFrame w) {
/* 118 */     return new FlatInternalFrameTitlePane(w);
/*     */   }
/*     */   
/*     */   protected FlatWindowResizer createWindowResizer() {
/* 122 */     return new FlatWindowResizer.InternalFrameResizer(this.frame, this::getDesktopManager);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static class FlatInternalFrameBorder
/*     */     extends FlatEmptyBorder
/*     */   {
/* 130 */     private final Color activeBorderColor = UIManager.getColor("InternalFrame.activeBorderColor");
/* 131 */     private final Color inactiveBorderColor = UIManager.getColor("InternalFrame.inactiveBorderColor");
/* 132 */     private final int borderLineWidth = FlatUIUtils.getUIInt("InternalFrame.borderLineWidth", 1);
/* 133 */     private final boolean dropShadowPainted = UIManager.getBoolean("InternalFrame.dropShadowPainted");
/*     */     
/* 135 */     private final FlatDropShadowBorder activeDropShadowBorder = new FlatDropShadowBorder(
/* 136 */         UIManager.getColor("InternalFrame.activeDropShadowColor"), 
/* 137 */         UIManager.getInsets("InternalFrame.activeDropShadowInsets"), 
/* 138 */         FlatUIUtils.getUIFloat("InternalFrame.activeDropShadowOpacity", 0.5F));
/* 139 */     private final FlatDropShadowBorder inactiveDropShadowBorder = new FlatDropShadowBorder(
/* 140 */         UIManager.getColor("InternalFrame.inactiveDropShadowColor"), 
/* 141 */         UIManager.getInsets("InternalFrame.inactiveDropShadowInsets"), 
/* 142 */         FlatUIUtils.getUIFloat("InternalFrame.inactiveDropShadowOpacity", 0.5F));
/*     */     
/*     */     public FlatInternalFrameBorder() {
/* 145 */       super(UIManager.getInsets("InternalFrame.borderMargins"));
/*     */     }
/*     */ 
/*     */     
/*     */     public Insets getBorderInsets(Component c, Insets insets) {
/* 150 */       if (c instanceof JInternalFrame && ((JInternalFrame)c).isMaximum()) {
/* 151 */         insets.left = UIScale.scale(Math.min(this.borderLineWidth, this.left));
/* 152 */         insets.top = UIScale.scale(Math.min(this.borderLineWidth, this.top));
/* 153 */         insets.right = UIScale.scale(Math.min(this.borderLineWidth, this.right));
/* 154 */         insets.bottom = UIScale.scale(Math.min(this.borderLineWidth, this.bottom));
/* 155 */         return insets;
/*     */       } 
/*     */       
/* 158 */       return super.getBorderInsets(c, insets);
/*     */     }
/*     */ 
/*     */     
/*     */     public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
/* 163 */       JInternalFrame f = (JInternalFrame)c;
/*     */       
/* 165 */       Insets insets = getBorderInsets(c);
/* 166 */       float lineWidth = UIScale.scale(this.borderLineWidth);
/*     */       
/* 168 */       float rx = (x + insets.left) - lineWidth;
/* 169 */       float ry = (y + insets.top) - lineWidth;
/* 170 */       float rwidth = (width - insets.left - insets.right) + lineWidth * 2.0F;
/* 171 */       float rheight = (height - insets.top - insets.bottom) + lineWidth * 2.0F;
/*     */       
/* 173 */       Graphics2D g2 = (Graphics2D)g.create();
/*     */       try {
/* 175 */         FlatUIUtils.setRenderingHints(g2);
/* 176 */         g2.setColor(f.isSelected() ? this.activeBorderColor : this.inactiveBorderColor);
/*     */ 
/*     */         
/* 179 */         if (this.dropShadowPainted) {
/* 180 */           FlatDropShadowBorder dropShadowBorder = f.isSelected() ? this.activeDropShadowBorder : this.inactiveDropShadowBorder;
/*     */ 
/*     */           
/* 183 */           Insets dropShadowInsets = dropShadowBorder.getBorderInsets();
/* 184 */           dropShadowBorder.paintBorder(c, g2, (int)rx - dropShadowInsets.left, (int)ry - dropShadowInsets.top, (int)rwidth + dropShadowInsets.left + dropShadowInsets.right, (int)rheight + dropShadowInsets.top + dropShadowInsets.bottom);
/*     */         } 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 192 */         g2.fill(FlatUIUtils.createRectangle(rx, ry, rwidth, rheight, lineWidth));
/*     */       } finally {
/* 194 */         g2.dispose();
/*     */       } 
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\formdev\flatla\\ui\FlatInternalFrameUI.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */