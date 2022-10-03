/*     */ package com.formdev.flatlaf.ui;
/*     */ 
/*     */ import com.formdev.flatlaf.util.HiDPIUtils;
/*     */ import com.formdev.flatlaf.util.UIScale;
/*     */ import java.awt.Dimension;
/*     */ import java.awt.Graphics;
/*     */ import java.awt.Graphics2D;
/*     */ import java.beans.PropertyChangeEvent;
/*     */ import javax.swing.JComponent;
/*     */ import javax.swing.UIManager;
/*     */ import javax.swing.plaf.ComponentUI;
/*     */ import javax.swing.plaf.basic.BasicEditorPaneUI;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ public class FlatEditorPaneUI
/*     */   extends BasicEditorPaneUI
/*     */ {
/*     */   protected int minimumWidth;
/*     */   protected boolean isIntelliJTheme;
/*     */   private Object oldHonorDisplayProperties;
/*     */   
/*     */   public static ComponentUI createUI(JComponent c) {
/*  68 */     return new FlatEditorPaneUI();
/*     */   }
/*     */ 
/*     */   
/*     */   protected void installDefaults() {
/*  73 */     super.installDefaults();
/*     */     
/*  75 */     this.minimumWidth = UIManager.getInt("Component.minimumWidth");
/*  76 */     this.isIntelliJTheme = UIManager.getBoolean("Component.isIntelliJTheme");
/*     */ 
/*     */     
/*  79 */     this.oldHonorDisplayProperties = getComponent().getClientProperty("JEditorPane.honorDisplayProperties");
/*  80 */     getComponent().putClientProperty("JEditorPane.honorDisplayProperties", Boolean.valueOf(true));
/*     */   }
/*     */ 
/*     */   
/*     */   protected void uninstallDefaults() {
/*  85 */     super.uninstallDefaults();
/*     */     
/*  87 */     getComponent().putClientProperty("JEditorPane.honorDisplayProperties", this.oldHonorDisplayProperties);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void propertyChange(PropertyChangeEvent e) {
/*  92 */     super.propertyChange(e);
/*  93 */     propertyChange(getComponent(), e);
/*     */   }
/*     */   
/*     */   static void propertyChange(JTextComponent c, PropertyChangeEvent e) {
/*  97 */     switch (e.getPropertyName()) {
/*     */       case "JComponent.minimumWidth":
/*  99 */         c.revalidate();
/*     */         break;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public Dimension getPreferredSize(JComponent c) {
/* 106 */     return applyMinimumWidth(c, super.getPreferredSize(c), this.minimumWidth);
/*     */   }
/*     */ 
/*     */   
/*     */   public Dimension getMinimumSize(JComponent c) {
/* 111 */     return applyMinimumWidth(c, super.getMinimumSize(c), this.minimumWidth);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static Dimension applyMinimumWidth(JComponent c, Dimension size, int minimumWidth) {
/* 119 */     minimumWidth = FlatUIUtils.minimumWidth(c, minimumWidth);
/* 120 */     size.width = Math.max(size.width, UIScale.scale(minimumWidth) - UIScale.scale(1) * 2);
/* 121 */     return size;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void paintSafely(Graphics g) {
/* 126 */     super.paintSafely(HiDPIUtils.createGraphicsTextYCorrection((Graphics2D)g));
/*     */   }
/*     */ 
/*     */   
/*     */   protected void paintBackground(Graphics g) {
/* 131 */     JTextComponent c = getComponent();
/*     */ 
/*     */     
/* 134 */     if (this.isIntelliJTheme && (!c.isEnabled() || !c.isEditable()) && c.getBackground() instanceof javax.swing.plaf.UIResource) {
/* 135 */       FlatUIUtils.paintParentBackground(g, c);
/*     */       
/*     */       return;
/*     */     } 
/* 139 */     super.paintBackground(g);
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\formdev\flatla\\ui\FlatEditorPaneUI.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */