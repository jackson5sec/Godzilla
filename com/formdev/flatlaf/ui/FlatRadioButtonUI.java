/*     */ package com.formdev.flatlaf.ui;
/*     */ 
/*     */ import com.formdev.flatlaf.icons.FlatCheckBoxIcon;
/*     */ import com.formdev.flatlaf.util.UIScale;
/*     */ import java.awt.Color;
/*     */ import java.awt.Dimension;
/*     */ import java.awt.Graphics;
/*     */ import java.awt.Insets;
/*     */ import java.awt.Rectangle;
/*     */ import java.util.function.Supplier;
/*     */ import javax.swing.AbstractButton;
/*     */ import javax.swing.JComponent;
/*     */ import javax.swing.LookAndFeel;
/*     */ import javax.swing.UIManager;
/*     */ import javax.swing.plaf.ComponentUI;
/*     */ import javax.swing.plaf.basic.BasicRadioButtonUI;
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
/*     */ public class FlatRadioButtonUI
/*     */   extends BasicRadioButtonUI
/*     */ {
/*     */   protected int iconTextGap;
/*     */   protected Color disabledText;
/*     */   private Color defaultBackground;
/*     */   private boolean defaults_initialized = false;
/*     */   
/*     */   public static ComponentUI createUI(JComponent c) {
/*  65 */     return FlatUIUtils.createSharedUI(FlatRadioButtonUI.class, FlatRadioButtonUI::new);
/*     */   }
/*     */ 
/*     */   
/*     */   public void installDefaults(AbstractButton b) {
/*  70 */     super.installDefaults(b);
/*     */     
/*  72 */     if (!this.defaults_initialized) {
/*  73 */       String prefix = getPropertyPrefix();
/*     */       
/*  75 */       this.iconTextGap = FlatUIUtils.getUIInt(prefix + "iconTextGap", 4);
/*  76 */       this.disabledText = UIManager.getColor(prefix + "disabledText");
/*     */       
/*  78 */       this.defaultBackground = UIManager.getColor(prefix + "background");
/*     */       
/*  80 */       this.defaults_initialized = true;
/*     */     } 
/*     */     
/*  83 */     LookAndFeel.installProperty(b, "opaque", Boolean.valueOf(false));
/*  84 */     LookAndFeel.installProperty(b, "iconTextGap", Integer.valueOf(UIScale.scale(this.iconTextGap)));
/*     */     
/*  86 */     MigLayoutVisualPadding.install(b, null);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void uninstallDefaults(AbstractButton b) {
/*  91 */     super.uninstallDefaults(b);
/*     */     
/*  93 */     MigLayoutVisualPadding.uninstall(b);
/*  94 */     this.defaults_initialized = false;
/*     */   }
/*     */   
/*  97 */   private static Insets tempInsets = new Insets(0, 0, 0, 0);
/*     */ 
/*     */   
/*     */   public Dimension getPreferredSize(JComponent c) {
/* 101 */     Dimension size = super.getPreferredSize(c);
/* 102 */     if (size == null) {
/* 103 */       return null;
/*     */     }
/*     */     
/* 106 */     int focusWidth = getIconFocusWidth(c);
/* 107 */     if (focusWidth > 0) {
/*     */ 
/*     */ 
/*     */       
/* 111 */       Insets insets = c.getInsets(tempInsets);
/* 112 */       size.width += Math.max(focusWidth - insets.left, 0) + Math.max(focusWidth - insets.right, 0);
/* 113 */       size.height += Math.max(focusWidth - insets.top, 0) + Math.max(focusWidth - insets.bottom, 0);
/*     */     } 
/*     */     
/* 116 */     return size;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void paint(Graphics g, JComponent c) {
/* 124 */     if (!c.isOpaque() && ((AbstractButton)c)
/* 125 */       .isContentAreaFilled() && c
/* 126 */       .getBackground() != this.defaultBackground) {
/*     */       
/* 128 */       g.setColor(c.getBackground());
/* 129 */       g.fillRect(0, 0, c.getWidth(), c.getHeight());
/*     */     } 
/*     */ 
/*     */     
/* 133 */     int focusWidth = getIconFocusWidth(c);
/* 134 */     if (focusWidth > 0) {
/* 135 */       boolean ltr = c.getComponentOrientation().isLeftToRight();
/* 136 */       Insets insets = c.getInsets(tempInsets);
/* 137 */       int leftOrRightInset = ltr ? insets.left : insets.right;
/* 138 */       if (focusWidth > leftOrRightInset) {
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 143 */         int offset = focusWidth - leftOrRightInset;
/* 144 */         if (!ltr) {
/* 145 */           offset = -offset;
/*     */         }
/*     */         
/* 148 */         g.translate(offset, 0);
/* 149 */         super.paint(g, c);
/* 150 */         g.translate(-offset, 0);
/*     */         
/*     */         return;
/*     */       } 
/*     */     } 
/* 155 */     super.paint(FlatLabelUI.createGraphicsHTMLTextYCorrection(g, c), c);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void paintText(Graphics g, AbstractButton b, Rectangle textRect, String text) {
/* 160 */     FlatButtonUI.paintText(g, b, textRect, text, b.isEnabled() ? b.getForeground() : this.disabledText);
/*     */   }
/*     */   
/*     */   private int getIconFocusWidth(JComponent c) {
/* 164 */     AbstractButton b = (AbstractButton)c;
/* 165 */     return (b.getIcon() == null && getDefaultIcon() instanceof FlatCheckBoxIcon) ? 
/* 166 */       UIScale.scale(((FlatCheckBoxIcon)getDefaultIcon()).focusWidth) : 0;
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\formdev\flatla\\ui\FlatRadioButtonUI.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */