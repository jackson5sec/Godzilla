/*    */ package com.formdev.flatlaf.extras.components;
/*    */ 
/*    */ import javax.swing.JProgressBar;
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
/*    */ public class FlatProgressBar
/*    */   extends JProgressBar
/*    */   implements FlatComponentExtension
/*    */ {
/*    */   public boolean isLargeHeight() {
/* 35 */     return getClientPropertyBoolean("JProgressBar.largeHeight", false);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void setLargeHeight(boolean largeHeight) {
/* 42 */     putClientPropertyBoolean("JProgressBar.largeHeight", largeHeight, false);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public boolean isSquare() {
/* 50 */     return getClientPropertyBoolean("JProgressBar.square", false);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void setSquare(boolean square) {
/* 57 */     putClientPropertyBoolean("JProgressBar.square", square, false);
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\formdev\flatlaf\extras\components\FlatProgressBar.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */