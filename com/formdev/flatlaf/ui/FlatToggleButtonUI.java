/*     */ package com.formdev.flatlaf.ui;
/*     */ 
/*     */ import com.formdev.flatlaf.FlatClientProperties;
/*     */ import com.formdev.flatlaf.util.UIScale;
/*     */ import java.awt.Color;
/*     */ import java.awt.Component;
/*     */ import java.awt.Graphics;
/*     */ import java.beans.PropertyChangeEvent;
/*     */ import java.util.function.Supplier;
/*     */ import javax.swing.AbstractButton;
/*     */ import javax.swing.JComponent;
/*     */ import javax.swing.JToggleButton;
/*     */ import javax.swing.UIManager;
/*     */ import javax.swing.plaf.ComponentUI;
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
/*     */ public class FlatToggleButtonUI
/*     */   extends FlatButtonUI
/*     */ {
/*     */   protected int tabUnderlineHeight;
/*     */   protected Color tabUnderlineColor;
/*     */   protected Color tabDisabledUnderlineColor;
/*     */   protected Color tabSelectedBackground;
/*     */   protected Color tabHoverBackground;
/*     */   protected Color tabFocusBackground;
/*     */   private boolean defaults_initialized = false;
/*     */   
/*     */   public static ComponentUI createUI(JComponent c) {
/*  86 */     return FlatUIUtils.createSharedUI(FlatToggleButtonUI.class, FlatToggleButtonUI::new);
/*     */   }
/*     */ 
/*     */   
/*     */   protected String getPropertyPrefix() {
/*  91 */     return "ToggleButton.";
/*     */   }
/*     */ 
/*     */   
/*     */   protected void installDefaults(AbstractButton b) {
/*  96 */     super.installDefaults(b);
/*     */     
/*  98 */     if (!this.defaults_initialized) {
/*  99 */       this.tabUnderlineHeight = UIManager.getInt("ToggleButton.tab.underlineHeight");
/* 100 */       this.tabUnderlineColor = UIManager.getColor("ToggleButton.tab.underlineColor");
/* 101 */       this.tabDisabledUnderlineColor = UIManager.getColor("ToggleButton.tab.disabledUnderlineColor");
/* 102 */       this.tabSelectedBackground = UIManager.getColor("ToggleButton.tab.selectedBackground");
/* 103 */       this.tabHoverBackground = UIManager.getColor("ToggleButton.tab.hoverBackground");
/* 104 */       this.tabFocusBackground = UIManager.getColor("ToggleButton.tab.focusBackground");
/*     */       
/* 106 */       this.defaults_initialized = true;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   protected void uninstallDefaults(AbstractButton b) {
/* 112 */     super.uninstallDefaults(b);
/* 113 */     this.defaults_initialized = false;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void propertyChange(AbstractButton b, PropertyChangeEvent e) {
/* 118 */     super.propertyChange(b, e);
/*     */     
/* 120 */     switch (e.getPropertyName()) {
/*     */       case "JButton.buttonType":
/* 122 */         if ("tab".equals(e.getOldValue()) || "tab".equals(e.getNewValue())) {
/* 123 */           MigLayoutVisualPadding.uninstall(b);
/* 124 */           MigLayoutVisualPadding.install(b);
/* 125 */           b.revalidate();
/*     */         } 
/*     */         
/* 128 */         b.repaint();
/*     */         break;
/*     */       
/*     */       case "JToggleButton.tab.underlineHeight":
/*     */       case "JToggleButton.tab.underlineColor":
/*     */       case "JToggleButton.tab.selectedBackground":
/* 134 */         b.repaint();
/*     */         break;
/*     */     } 
/*     */   }
/*     */   
/*     */   static boolean isTabButton(Component c) {
/* 140 */     return (c instanceof JToggleButton && FlatClientProperties.clientPropertyEquals((JToggleButton)c, "JButton.buttonType", "tab"));
/*     */   }
/*     */ 
/*     */   
/*     */   protected void paintBackground(Graphics g, JComponent c) {
/* 145 */     if (isTabButton(c)) {
/* 146 */       int height = c.getHeight();
/* 147 */       int width = c.getWidth();
/* 148 */       boolean selected = ((AbstractButton)c).isSelected();
/* 149 */       Color enabledColor = selected ? FlatClientProperties.clientPropertyColor(c, "JToggleButton.tab.selectedBackground", this.tabSelectedBackground) : null;
/*     */ 
/*     */       
/* 152 */       if (enabledColor == null) {
/* 153 */         Color bg = c.getBackground();
/* 154 */         if (isCustomBackground(bg)) {
/* 155 */           enabledColor = bg;
/*     */         }
/*     */       } 
/*     */       
/* 159 */       Color background = buttonStateColor(c, enabledColor, null, this.tabFocusBackground, this.tabHoverBackground, null);
/*     */       
/* 161 */       if (background != null) {
/* 162 */         g.setColor(background);
/* 163 */         g.fillRect(0, 0, width, height);
/*     */       } 
/*     */ 
/*     */       
/* 167 */       if (selected) {
/* 168 */         int underlineHeight = UIScale.scale(FlatClientProperties.clientPropertyInt(c, "JToggleButton.tab.underlineHeight", this.tabUnderlineHeight));
/* 169 */         g.setColor(c.isEnabled() ? 
/* 170 */             FlatClientProperties.clientPropertyColor(c, "JToggleButton.tab.underlineColor", this.tabUnderlineColor) : this.tabDisabledUnderlineColor);
/*     */         
/* 172 */         g.fillRect(0, height - underlineHeight, width, underlineHeight);
/*     */       } 
/*     */     } else {
/* 175 */       super.paintBackground(g, c);
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\formdev\flatla\\ui\FlatToggleButtonUI.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */