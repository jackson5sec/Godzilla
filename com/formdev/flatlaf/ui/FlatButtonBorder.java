/*     */ package com.formdev.flatlaf.ui;
/*     */ 
/*     */ import com.formdev.flatlaf.util.UIScale;
/*     */ import java.awt.Color;
/*     */ import java.awt.Component;
/*     */ import java.awt.GradientPaint;
/*     */ import java.awt.Graphics;
/*     */ import java.awt.Insets;
/*     */ import java.awt.Paint;
/*     */ import javax.swing.AbstractButton;
/*     */ import javax.swing.UIManager;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class FlatButtonBorder
/*     */   extends FlatBorder
/*     */ {
/*  56 */   protected final Color borderColor = FlatUIUtils.getUIColor("Button.startBorderColor", "Button.borderColor");
/*  57 */   protected final Color endBorderColor = UIManager.getColor("Button.endBorderColor");
/*  58 */   protected final Color disabledBorderColor = UIManager.getColor("Button.disabledBorderColor");
/*  59 */   protected final Color focusedBorderColor = UIManager.getColor("Button.focusedBorderColor");
/*  60 */   protected final Color hoverBorderColor = UIManager.getColor("Button.hoverBorderColor");
/*  61 */   protected final Color defaultBorderColor = FlatUIUtils.getUIColor("Button.default.startBorderColor", "Button.default.borderColor");
/*  62 */   protected final Color defaultEndBorderColor = UIManager.getColor("Button.default.endBorderColor");
/*  63 */   protected final Color defaultHoverBorderColor = UIManager.getColor("Button.default.hoverBorderColor");
/*  64 */   protected final Color defaultFocusedBorderColor = UIManager.getColor("Button.default.focusedBorderColor");
/*  65 */   protected final Color defaultFocusColor = UIManager.getColor("Button.default.focusColor");
/*  66 */   protected final int borderWidth = UIManager.getInt("Button.borderWidth");
/*  67 */   protected final int defaultBorderWidth = UIManager.getInt("Button.default.borderWidth");
/*  68 */   protected final Insets toolbarMargin = UIManager.getInsets("Button.toolbar.margin");
/*  69 */   protected final Insets toolbarSpacingInsets = UIManager.getInsets("Button.toolbar.spacingInsets");
/*  70 */   protected final int arc = UIManager.getInt("Button.arc");
/*     */ 
/*     */   
/*     */   public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
/*  74 */     if (FlatButtonUI.isContentAreaFilled(c) && 
/*  75 */       !FlatButtonUI.isToolBarButton(c) && 
/*  76 */       !FlatButtonUI.isHelpButton(c) && 
/*  77 */       !FlatToggleButtonUI.isTabButton(c)) {
/*  78 */       super.paintBorder(c, g, x, y, width, height);
/*     */     }
/*     */   }
/*     */   
/*     */   protected Color getFocusColor(Component c) {
/*  83 */     return FlatButtonUI.isDefaultButton(c) ? this.defaultFocusColor : super.getFocusColor(c);
/*     */   }
/*     */ 
/*     */   
/*     */   protected boolean isFocused(Component c) {
/*  88 */     return (FlatButtonUI.isFocusPainted(c) && super.isFocused(c));
/*     */   }
/*     */ 
/*     */   
/*     */   protected Paint getBorderColor(Component c) {
/*  93 */     boolean def = FlatButtonUI.isDefaultButton(c);
/*  94 */     Paint color = FlatButtonUI.buttonStateColor(c, def ? this.defaultBorderColor : this.borderColor, this.disabledBorderColor, def ? this.defaultFocusedBorderColor : this.focusedBorderColor, def ? this.defaultHoverBorderColor : this.hoverBorderColor, null);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 102 */     Color startBg = def ? this.defaultBorderColor : this.borderColor;
/* 103 */     Color endBg = def ? this.defaultEndBorderColor : this.endBorderColor;
/* 104 */     if (color == startBg && endBg != null && !startBg.equals(endBg)) {
/* 105 */       color = new GradientPaint(0.0F, 0.0F, startBg, 0.0F, c.getHeight(), endBg);
/*     */     }
/* 107 */     return color;
/*     */   }
/*     */ 
/*     */   
/*     */   public Insets getBorderInsets(Component c, Insets insets) {
/* 112 */     if (FlatButtonUI.isToolBarButton(c)) {
/*     */ 
/*     */ 
/*     */       
/* 116 */       Insets margin = (c instanceof AbstractButton) ? ((AbstractButton)c).getMargin() : null;
/*     */ 
/*     */       
/* 119 */       FlatUIUtils.setInsets(insets, UIScale.scale(FlatUIUtils.addInsets(this.toolbarSpacingInsets, (margin != null && !(margin instanceof javax.swing.plaf.UIResource)) ? margin : this.toolbarMargin)));
/*     */     } else {
/*     */       
/* 122 */       insets = super.getBorderInsets(c, insets);
/*     */ 
/*     */       
/* 125 */       if (FlatButtonUI.isIconOnlyOrSingleCharacterButton(c) && ((AbstractButton)c).getMargin() instanceof javax.swing.plaf.UIResource) {
/* 126 */         insets.left = insets.right = Math.min(insets.top, insets.bottom);
/*     */       }
/*     */     } 
/* 129 */     return insets;
/*     */   }
/*     */ 
/*     */   
/*     */   protected int getFocusWidth(Component c) {
/* 134 */     return FlatToggleButtonUI.isTabButton(c) ? 0 : super.getFocusWidth(c);
/*     */   }
/*     */ 
/*     */   
/*     */   protected int getBorderWidth(Component c) {
/* 139 */     return FlatButtonUI.isDefaultButton(c) ? this.defaultBorderWidth : this.borderWidth;
/*     */   }
/*     */ 
/*     */   
/*     */   protected int getArc(Component c) {
/* 144 */     if (isCellEditor(c)) {
/* 145 */       return 0;
/*     */     }
/* 147 */     switch (FlatButtonUI.getButtonType(c)) { case 0:
/* 148 */         return 0;
/* 149 */       case 1: return 32767; }
/* 150 */      return this.arc;
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\formdev\flatla\\ui\FlatButtonBorder.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */