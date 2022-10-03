/*     */ package com.formdev.flatlaf.extras.components;
/*     */ 
/*     */ import javax.swing.JTextField;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class FlatTextField
/*     */   extends JTextField
/*     */   implements FlatComponentExtension
/*     */ {
/*     */   public String getPlaceholderText() {
/*  36 */     return (String)getClientProperty("JTextField.placeholderText");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setPlaceholderText(String placeholderText) {
/*  43 */     putClientProperty("JTextField.placeholderText", placeholderText);
/*     */   }
/*     */   
/*     */   public enum SelectAllOnFocusPolicy
/*     */   {
/*  48 */     never, once, always;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public SelectAllOnFocusPolicy getSelectAllOnFocusPolicy() {
/*  54 */     return getClientPropertyEnumString("JTextField.selectAllOnFocusPolicy", SelectAllOnFocusPolicy.class, "TextComponent.selectAllOnFocusPolicy", SelectAllOnFocusPolicy.once);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setSelectAllOnFocusPolicy(SelectAllOnFocusPolicy selectAllOnFocusPolicy) {
/*  62 */     putClientPropertyEnumString("JTextField.selectAllOnFocusPolicy", selectAllOnFocusPolicy);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getMinimumWidth() {
/*  70 */     return getClientPropertyInt("JComponent.minimumWidth", "Component.minimumWidth");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setMinimumWidth(int minimumWidth) {
/*  77 */     putClientProperty("JComponent.minimumWidth", (minimumWidth >= 0) ? Integer.valueOf(minimumWidth) : null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isRoundRect() {
/*  85 */     return getClientPropertyBoolean("JComponent.roundRect", false);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setRoundRect(boolean roundRect) {
/*  92 */     putClientPropertyBoolean("JComponent.roundRect", roundRect, false);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Object getOutline() {
/* 100 */     return getClientProperty("JComponent.outline");
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
/* 117 */     putClientProperty("JComponent.outline", outline);
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\formdev\flatlaf\extras\components\FlatTextField.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */