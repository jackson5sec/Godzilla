/*    */ package com.formdev.flatlaf.extras.components;
/*    */ 
/*    */ import javax.swing.JScrollPane;
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
/*    */ public class FlatScrollPane
/*    */   extends JScrollPane
/*    */   implements FlatComponentExtension
/*    */ {
/*    */   public boolean isShowButtons() {
/* 36 */     return getClientPropertyBoolean("JScrollBar.showButtons", "ScrollBar.showButtons");
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void setShowButtons(boolean showButtons) {
/* 43 */     putClientProperty("JScrollBar.showButtons", Boolean.valueOf(showButtons));
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public boolean isSmoothScrolling() {
/* 51 */     return getClientPropertyBoolean("JScrollPane.smoothScrolling", "ScrollPane.smoothScrolling");
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void setSmoothScrolling(boolean smoothScrolling) {
/* 58 */     putClientProperty("JScrollPane.smoothScrolling", Boolean.valueOf(smoothScrolling));
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


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\formdev\flatlaf\extras\components\FlatScrollPane.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */