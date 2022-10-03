/*     */ package com.formdev.flatlaf.extras.components;
/*     */ 
/*     */ import java.awt.Color;
/*     */ import javax.swing.JToggleButton;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class FlatToggleButton
/*     */   extends JToggleButton
/*     */   implements FlatComponentExtension
/*     */ {
/*     */   public FlatButton.ButtonType getButtonType() {
/*  37 */     return getClientPropertyEnumString("JButton.buttonType", FlatButton.ButtonType.class, null, FlatButton.ButtonType.none);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setButtonType(FlatButton.ButtonType buttonType) {
/*  44 */     if (buttonType == FlatButton.ButtonType.none)
/*  45 */       buttonType = null; 
/*  46 */     putClientPropertyEnumString("JButton.buttonType", buttonType);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isSquareSize() {
/*  54 */     return getClientPropertyBoolean("JButton.squareSize", false);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setSquareSize(boolean squareSize) {
/*  61 */     putClientPropertyBoolean("JButton.squareSize", squareSize, false);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getMinimumWidth() {
/*  69 */     return getClientPropertyInt("JComponent.minimumWidth", "ToggleButton.minimumWidth");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setMinimumWidth(int minimumWidth) {
/*  76 */     putClientProperty("JComponent.minimumWidth", (minimumWidth >= 0) ? Integer.valueOf(minimumWidth) : null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getMinimumHeight() {
/*  84 */     return getClientPropertyInt("JComponent.minimumHeight", 0);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setMinimumHeight(int minimumHeight) {
/*  91 */     putClientProperty("JComponent.minimumHeight", (minimumHeight >= 0) ? Integer.valueOf(minimumHeight) : null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Object getOutline() {
/*  99 */     return getClientProperty("JComponent.outline");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setOutline(Object outline) {
/* 116 */     putClientProperty("JComponent.outline", outline);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getTabUnderlineHeight() {
/* 124 */     return getClientPropertyInt("JToggleButton.tab.underlineHeight", "ToggleButton.tab.underlineHeight");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setTabUnderlineHeight(int tabUnderlineHeight) {
/* 131 */     putClientProperty("JToggleButton.tab.underlineHeight", (tabUnderlineHeight >= 0) ? Integer.valueOf(tabUnderlineHeight) : null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Color getTabUnderlineColor() {
/* 139 */     return getClientPropertyColor("JToggleButton.tab.underlineColor", "ToggleButton.tab.underlineColor");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setTabUnderlineColor(Color tabUnderlineColor) {
/* 146 */     putClientProperty("JToggleButton.tab.underlineColor", tabUnderlineColor);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Color getTabSelectedBackground() {
/* 154 */     return getClientPropertyColor("JToggleButton.tab.selectedBackground", "ToggleButton.tab.selectedBackground");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setTabSelectedBackground(Color tabSelectedBackground) {
/* 161 */     putClientProperty("JToggleButton.tab.selectedBackground", tabSelectedBackground);
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\formdev\flatlaf\extras\components\FlatToggleButton.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */