/*     */ package com.formdev.flatlaf.extras.components;
/*     */ 
/*     */ import javax.swing.JFormattedTextField;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class FlatFormattedTextField
/*     */   extends JFormattedTextField
/*     */   implements FlatComponentExtension
/*     */ {
/*     */   public String getPlaceholderText() {
/*  37 */     return (String)getClientProperty("JTextField.placeholderText");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setPlaceholderText(String placeholderText) {
/*  44 */     putClientProperty("JTextField.placeholderText", placeholderText);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public FlatTextField.SelectAllOnFocusPolicy getSelectAllOnFocusPolicy() {
/*  52 */     return getClientPropertyEnumString("JTextField.selectAllOnFocusPolicy", FlatTextField.SelectAllOnFocusPolicy.class, "TextComponent.selectAllOnFocusPolicy", FlatTextField.SelectAllOnFocusPolicy.once);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setSelectAllOnFocusPolicy(FlatTextField.SelectAllOnFocusPolicy selectAllOnFocusPolicy) {
/*  60 */     putClientPropertyEnumString("JTextField.selectAllOnFocusPolicy", selectAllOnFocusPolicy);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getMinimumWidth() {
/*  68 */     return getClientPropertyInt("JComponent.minimumWidth", "Component.minimumWidth");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setMinimumWidth(int minimumWidth) {
/*  75 */     putClientProperty("JComponent.minimumWidth", (minimumWidth >= 0) ? Integer.valueOf(minimumWidth) : null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isRoundRect() {
/*  83 */     return getClientPropertyBoolean("JComponent.roundRect", false);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setRoundRect(boolean roundRect) {
/*  90 */     putClientPropertyBoolean("JComponent.roundRect", roundRect, false);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Object getOutline() {
/*  98 */     return getClientProperty("JComponent.outline");
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
/* 115 */     putClientProperty("JComponent.outline", outline);
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\formdev\flatlaf\extras\components\FlatFormattedTextField.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */