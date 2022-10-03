/*     */ package com.formdev.flatlaf.ui;
/*     */ 
/*     */ import com.formdev.flatlaf.util.HiDPIUtils;
/*     */ import java.awt.Dimension;
/*     */ import java.awt.Graphics;
/*     */ import java.awt.Graphics2D;
/*     */ import java.beans.PropertyChangeEvent;
/*     */ import javax.swing.JComponent;
/*     */ import javax.swing.UIManager;
/*     */ import javax.swing.plaf.ComponentUI;
/*     */ import javax.swing.plaf.basic.BasicTextPaneUI;
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
/*     */ public class FlatTextPaneUI
/*     */   extends BasicTextPaneUI
/*     */ {
/*     */   protected int minimumWidth;
/*     */   protected boolean isIntelliJTheme;
/*     */   private Object oldHonorDisplayProperties;
/*     */   
/*     */   public static ComponentUI createUI(JComponent c) {
/*  66 */     return new FlatTextPaneUI();
/*     */   }
/*     */ 
/*     */   
/*     */   protected void installDefaults() {
/*  71 */     super.installDefaults();
/*     */     
/*  73 */     this.minimumWidth = UIManager.getInt("Component.minimumWidth");
/*  74 */     this.isIntelliJTheme = UIManager.getBoolean("Component.isIntelliJTheme");
/*     */ 
/*     */     
/*  77 */     this.oldHonorDisplayProperties = getComponent().getClientProperty("JEditorPane.honorDisplayProperties");
/*  78 */     getComponent().putClientProperty("JEditorPane.honorDisplayProperties", Boolean.valueOf(true));
/*     */   }
/*     */ 
/*     */   
/*     */   protected void uninstallDefaults() {
/*  83 */     super.uninstallDefaults();
/*     */     
/*  85 */     getComponent().putClientProperty("JEditorPane.honorDisplayProperties", this.oldHonorDisplayProperties);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void propertyChange(PropertyChangeEvent e) {
/*  90 */     super.propertyChange(e);
/*  91 */     FlatEditorPaneUI.propertyChange(getComponent(), e);
/*     */   }
/*     */ 
/*     */   
/*     */   public Dimension getPreferredSize(JComponent c) {
/*  96 */     return FlatEditorPaneUI.applyMinimumWidth(c, super.getPreferredSize(c), this.minimumWidth);
/*     */   }
/*     */ 
/*     */   
/*     */   public Dimension getMinimumSize(JComponent c) {
/* 101 */     return FlatEditorPaneUI.applyMinimumWidth(c, super.getMinimumSize(c), this.minimumWidth);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void paintSafely(Graphics g) {
/* 106 */     super.paintSafely(HiDPIUtils.createGraphicsTextYCorrection((Graphics2D)g));
/*     */   }
/*     */ 
/*     */   
/*     */   protected void paintBackground(Graphics g) {
/* 111 */     JTextComponent c = getComponent();
/*     */ 
/*     */     
/* 114 */     if (this.isIntelliJTheme && (!c.isEnabled() || !c.isEditable()) && c.getBackground() instanceof javax.swing.plaf.UIResource) {
/* 115 */       FlatUIUtils.paintParentBackground(g, c);
/*     */       
/*     */       return;
/*     */     } 
/* 119 */     super.paintBackground(g);
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\formdev\flatla\\ui\FlatTextPaneUI.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */