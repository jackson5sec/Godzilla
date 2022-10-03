/*     */ package com.formdev.flatlaf.ui;
/*     */ 
/*     */ import com.formdev.flatlaf.util.HiDPIUtils;
/*     */ import java.awt.Color;
/*     */ import java.awt.Dimension;
/*     */ import java.awt.Graphics;
/*     */ import java.awt.Graphics2D;
/*     */ import java.beans.PropertyChangeEvent;
/*     */ import javax.swing.JComponent;
/*     */ import javax.swing.JTextArea;
/*     */ import javax.swing.UIManager;
/*     */ import javax.swing.plaf.ComponentUI;
/*     */ import javax.swing.plaf.basic.BasicTextAreaUI;
/*     */ import javax.swing.text.JTextComponent;
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
/*     */ public class FlatTextAreaUI
/*     */   extends BasicTextAreaUI
/*     */ {
/*     */   protected int minimumWidth;
/*     */   protected boolean isIntelliJTheme;
/*     */   protected Color background;
/*     */   protected Color disabledBackground;
/*     */   protected Color inactiveBackground;
/*     */   
/*     */   public static ComponentUI createUI(JComponent c) {
/*  68 */     return new FlatTextAreaUI();
/*     */   }
/*     */ 
/*     */   
/*     */   public void installUI(JComponent c) {
/*  73 */     super.installUI(c);
/*     */     
/*  75 */     updateBackground();
/*     */   }
/*     */ 
/*     */   
/*     */   protected void installDefaults() {
/*  80 */     super.installDefaults();
/*     */     
/*  82 */     this.minimumWidth = UIManager.getInt("Component.minimumWidth");
/*  83 */     this.isIntelliJTheme = UIManager.getBoolean("Component.isIntelliJTheme");
/*  84 */     this.background = UIManager.getColor("TextArea.background");
/*  85 */     this.disabledBackground = UIManager.getColor("TextArea.disabledBackground");
/*  86 */     this.inactiveBackground = UIManager.getColor("TextArea.inactiveBackground");
/*     */   }
/*     */ 
/*     */   
/*     */   protected void uninstallDefaults() {
/*  91 */     super.uninstallDefaults();
/*     */     
/*  93 */     this.background = null;
/*  94 */     this.disabledBackground = null;
/*  95 */     this.inactiveBackground = null;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void propertyChange(PropertyChangeEvent e) {
/* 100 */     super.propertyChange(e);
/* 101 */     FlatEditorPaneUI.propertyChange(getComponent(), e);
/*     */     
/* 103 */     switch (e.getPropertyName()) {
/*     */       case "editable":
/*     */       case "enabled":
/* 106 */         updateBackground();
/*     */         break;
/*     */     } 
/*     */   }
/*     */   
/*     */   private void updateBackground() {
/* 112 */     JTextComponent c = getComponent();
/*     */     
/* 114 */     Color background = c.getBackground();
/* 115 */     if (!(background instanceof javax.swing.plaf.UIResource)) {
/*     */       return;
/*     */     }
/*     */     
/* 119 */     if (background != this.background && background != this.disabledBackground && background != this.inactiveBackground) {
/*     */       return;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 126 */     Color newBackground = !c.isEnabled() ? this.disabledBackground : (!c.isEditable() ? this.inactiveBackground : this.background);
/*     */ 
/*     */ 
/*     */     
/* 130 */     if (newBackground != background) {
/* 131 */       c.setBackground(newBackground);
/*     */     }
/*     */   }
/*     */   
/*     */   public Dimension getPreferredSize(JComponent c) {
/* 136 */     return applyMinimumWidth(c, super.getPreferredSize(c));
/*     */   }
/*     */ 
/*     */   
/*     */   public Dimension getMinimumSize(JComponent c) {
/* 141 */     return applyMinimumWidth(c, super.getMinimumSize(c));
/*     */   }
/*     */ 
/*     */   
/*     */   private Dimension applyMinimumWidth(JComponent c, Dimension size) {
/* 146 */     if (c instanceof JTextArea && ((JTextArea)c).getColumns() > 0) {
/* 147 */       return size;
/*     */     }
/* 149 */     return FlatEditorPaneUI.applyMinimumWidth(c, size, this.minimumWidth);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void paintSafely(Graphics g) {
/* 154 */     super.paintSafely(HiDPIUtils.createGraphicsTextYCorrection((Graphics2D)g));
/*     */   }
/*     */ 
/*     */   
/*     */   protected void paintBackground(Graphics g) {
/* 159 */     JTextComponent c = getComponent();
/*     */ 
/*     */     
/* 162 */     if (this.isIntelliJTheme && (!c.isEnabled() || !c.isEditable()) && c.getBackground() instanceof javax.swing.plaf.UIResource) {
/* 163 */       FlatUIUtils.paintParentBackground(g, c);
/*     */       
/*     */       return;
/*     */     } 
/* 167 */     super.paintBackground(g);
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\formdev\flatla\\ui\FlatTextAreaUI.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */