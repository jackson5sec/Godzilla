/*    */ package com.formdev.flatlaf.extras.components;
/*    */ 
/*    */ import javax.swing.JComboBox;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class FlatComboBox<E>
/*    */   extends JComboBox<E>
/*    */   implements FlatComponentExtension
/*    */ {
/*    */   public String getPlaceholderText() {
/* 36 */     return (String)getClientProperty("JTextField.placeholderText");
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void setPlaceholderText(String placeholderText) {
/* 43 */     putClientProperty("JTextField.placeholderText", placeholderText);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public int getMinimumWidth() {
/* 51 */     return getClientPropertyInt("JComponent.minimumWidth", "ComboBox.minimumWidth");
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void setMinimumWidth(int minimumWidth) {
/* 58 */     putClientProperty("JComponent.minimumWidth", (minimumWidth >= 0) ? Integer.valueOf(minimumWidth) : null);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public boolean isRoundRect() {
/* 66 */     return getClientPropertyBoolean("JComponent.roundRect", false);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void setRoundRect(boolean roundRect) {
/* 73 */     putClientPropertyBoolean("JComponent.roundRect", roundRect, false);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public Object getOutline() {
/* 81 */     return getClientProperty("JComponent.outline");
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void setOutline(Object outline) {
/* 98 */     putClientProperty("JComponent.outline", outline);
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\formdev\flatlaf\extras\components\FlatComboBox.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */