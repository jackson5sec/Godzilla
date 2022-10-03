/*    */ package com.formdev.flatlaf.extras.components;
/*    */ 
/*    */ import javax.swing.JSpinner;
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
/*    */ public class FlatSpinner
/*    */   extends JSpinner
/*    */   implements FlatComponentExtension
/*    */ {
/*    */   public int getMinimumWidth() {
/* 36 */     return getClientPropertyInt("JComponent.minimumWidth", "Component.minimumWidth");
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void setMinimumWidth(int minimumWidth) {
/* 43 */     putClientProperty("JComponent.minimumWidth", (minimumWidth >= 0) ? Integer.valueOf(minimumWidth) : null);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public boolean isRoundRect() {
/* 51 */     return getClientPropertyBoolean("JComponent.roundRect", false);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void setRoundRect(boolean roundRect) {
/* 58 */     putClientPropertyBoolean("JComponent.roundRect", roundRect, false);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public Object getOutline() {
/* 66 */     return getClientProperty("JComponent.outline");
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
/* 83 */     putClientProperty("JComponent.outline", outline);
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\formdev\flatlaf\extras\components\FlatSpinner.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */