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
/*    */ final class HorizontalInfo
/*    */   extends DimensionInfo
/*    */ {
/*    */   HorizontalInfo(LayoutState layoutState, int gap) {
/* 20 */     super(layoutState, gap);
/*    */   }
/*    */   
/*    */   protected int getOriginalCell(GridConstraints constraints) {
/* 24 */     return constraints.getColumn();
/*    */   }
/*    */   
/*    */   protected int getOriginalSpan(GridConstraints constraints) {
/* 28 */     return constraints.getColSpan();
/*    */   }
/*    */   
/*    */   int getSizePolicy(int componentIndex) {
/* 32 */     return this.myLayoutState.getConstraints(componentIndex).getHSizePolicy();
/*    */   }
/*    */   
/*    */   int getChildLayoutCellCount(GridLayoutManager childLayout) {
/* 36 */     return childLayout.getColumnCount();
/*    */   }
/*    */   
/*    */   public int getMinimumWidth(int componentIndex) {
/* 40 */     return (getMinimumSize(componentIndex)).width;
/*    */   }
/*    */   
/*    */   public DimensionInfo getDimensionInfo(GridLayoutManager grid) {
/* 44 */     return grid.myHorizontalInfo;
/*    */   }
/*    */   
/*    */   public int getCellCount() {
/* 48 */     return this.myLayoutState.getColumnCount();
/*    */   }
/*    */   
/*    */   public int getPreferredWidth(int componentIndex) {
/* 52 */     return (getPreferredSize(componentIndex)).width;
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\intelli\\uiDesigner\core\HorizontalInfo.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */