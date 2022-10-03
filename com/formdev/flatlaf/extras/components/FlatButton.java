/*     */ package com.formdev.flatlaf.extras.components;
/*     */ 
/*     */ import javax.swing.JButton;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class FlatButton
/*     */   extends JButton
/*     */   implements FlatComponentExtension
/*     */ {
/*     */   public enum ButtonType
/*     */   {
/*  33 */     none, square, roundRect, tab, help, toolBarButton;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public ButtonType getButtonType() {
/*  39 */     return getClientPropertyEnumString("JButton.buttonType", ButtonType.class, null, ButtonType.none);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setButtonType(ButtonType buttonType) {
/*  46 */     if (buttonType == ButtonType.none)
/*  47 */       buttonType = null; 
/*  48 */     putClientPropertyEnumString("JButton.buttonType", buttonType);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isSquareSize() {
/*  56 */     return getClientPropertyBoolean("JButton.squareSize", false);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setSquareSize(boolean squareSize) {
/*  63 */     putClientPropertyBoolean("JButton.squareSize", squareSize, false);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getMinimumWidth() {
/*  71 */     return getClientPropertyInt("JComponent.minimumWidth", "Button.minimumWidth");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setMinimumWidth(int minimumWidth) {
/*  78 */     putClientProperty("JComponent.minimumWidth", (minimumWidth >= 0) ? Integer.valueOf(minimumWidth) : null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getMinimumHeight() {
/*  86 */     return getClientPropertyInt("JComponent.minimumHeight", 0);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setMinimumHeight(int minimumHeight) {
/*  93 */     putClientProperty("JComponent.minimumHeight", (minimumHeight >= 0) ? Integer.valueOf(minimumHeight) : null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Object getOutline() {
/* 101 */     return getClientProperty("JComponent.outline");
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
/* 118 */     putClientProperty("JComponent.outline", outline);
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\formdev\flatlaf\extras\components\FlatButton.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */