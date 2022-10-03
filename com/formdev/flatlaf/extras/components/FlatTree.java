/*    */ package com.formdev.flatlaf.extras.components;
/*    */ 
/*    */ import javax.swing.JTree;
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
/*    */ public class FlatTree
/*    */   extends JTree
/*    */   implements FlatComponentExtension
/*    */ {
/*    */   public boolean isWideSelection() {
/* 34 */     return getClientPropertyBoolean("JTree.wideSelection", "Tree.wideSelection");
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void setWideSelection(boolean wideSelection) {
/* 41 */     putClientProperty("JTree.wideSelection", Boolean.valueOf(wideSelection));
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public boolean isPaintSelection() {
/* 49 */     return getClientPropertyBoolean("JTree.paintSelection", true);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void setPaintSelection(boolean paintSelection) {
/* 57 */     putClientProperty("JTree.paintSelection", Boolean.valueOf(paintSelection));
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\formdev\flatlaf\extras\components\FlatTree.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */