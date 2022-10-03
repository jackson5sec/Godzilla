/*    */ package com.intellij.uiDesigner.core;
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
/*    */ final class VerticalInfo
/*    */   extends DimensionInfo
/*    */ {
/*    */   VerticalInfo(LayoutState layoutState, int gap) {
/* 20 */     super(layoutState, gap);
/*    */   }
/*    */   
/*    */   protected int getOriginalCell(GridConstraints constraints) {
/* 24 */     return constraints.getRow();
/*    */   }
/*    */   
/*    */   protected int getOriginalSpan(GridConstraints constraints) {
/* 28 */     return constraints.getRowSpan();
/*    */   }
/*    */   
/*    */   int getSizePolicy(int componentIndex) {
/* 32 */     return this.myLayoutState.getConstraints(componentIndex).getVSizePolicy();
/*    */   }
/*    */   
/*    */   int getChildLayoutCellCount(GridLayoutManager childLayout) {
/* 36 */     return childLayout.getRowCount();
/*    */   }
/*    */   
/*    */   public int getMinimumWidth(int componentIndex) {
/* 40 */     return (getMinimumSize(componentIndex)).height;
/*    */   }
/*    */   
/*    */   public DimensionInfo getDimensionInfo(GridLayoutManager grid) {
/* 44 */     return grid.myVerticalInfo;
/*    */   }
/*    */   
/*    */   public int getCellCount() {
/* 48 */     return this.myLayoutState.getRowCount();
/*    */   }
/*    */   
/*    */   public int getPreferredWidth(int componentIndex) {
/* 52 */     return (getPreferredSize(componentIndex)).height;
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\intelli\\uiDesigner\core\VerticalInfo.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */