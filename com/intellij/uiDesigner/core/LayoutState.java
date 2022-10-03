/*    */ package com.intellij.uiDesigner.core;
/*    */ 
/*    */ import java.awt.Component;
/*    */ import java.awt.Dimension;
/*    */ import java.util.ArrayList;
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
/*    */ public final class LayoutState
/*    */ {
/*    */   private final Component[] myComponents;
/*    */   private final GridConstraints[] myConstraints;
/*    */   private final int myColumnCount;
/*    */   private final int myRowCount;
/*    */   final Dimension[] myPreferredSizes;
/*    */   final Dimension[] myMinimumSizes;
/*    */   
/*    */   public LayoutState(GridLayoutManager layout, boolean ignoreInvisibleComponents) {
/* 33 */     ArrayList<Component> componentsList = new ArrayList(layout.getComponentCount());
/* 34 */     ArrayList<GridConstraints> constraintsList = new ArrayList(layout.getComponentCount());
/* 35 */     for (int i = 0; i < layout.getComponentCount(); i++) {
/* 36 */       Component component = layout.getComponent(i);
/* 37 */       if (!ignoreInvisibleComponents || component.isVisible()) {
/* 38 */         componentsList.add(component);
/* 39 */         GridConstraints constraints = layout.getConstraints(i);
/* 40 */         constraintsList.add(constraints);
/*    */       } 
/*    */     } 
/*    */     
/* 44 */     this.myComponents = componentsList.<Component>toArray(new Component[0]);
/* 45 */     this.myConstraints = constraintsList.<GridConstraints>toArray(GridConstraints.EMPTY_ARRAY);
/*    */     
/* 47 */     this.myMinimumSizes = new Dimension[this.myComponents.length];
/* 48 */     this.myPreferredSizes = new Dimension[this.myComponents.length];
/*    */     
/* 50 */     this.myColumnCount = layout.getColumnCount();
/* 51 */     this.myRowCount = layout.getRowCount();
/*    */   }
/*    */   
/*    */   public int getComponentCount() {
/* 55 */     return this.myComponents.length;
/*    */   }
/*    */   
/*    */   public Component getComponent(int index) {
/* 59 */     return this.myComponents[index];
/*    */   }
/*    */   
/*    */   public GridConstraints getConstraints(int index) {
/* 63 */     return this.myConstraints[index];
/*    */   }
/*    */   
/*    */   public int getColumnCount() {
/* 67 */     return this.myColumnCount;
/*    */   }
/*    */   
/*    */   public int getRowCount() {
/* 71 */     return this.myRowCount;
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\intelli\\uiDesigner\core\LayoutState.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */