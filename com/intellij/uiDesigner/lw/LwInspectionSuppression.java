/*    */ package com.intellij.uiDesigner.lw;
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
/*    */ public class LwInspectionSuppression
/*    */ {
/* 23 */   public static final LwInspectionSuppression[] EMPTY_ARRAY = new LwInspectionSuppression[0];
/*    */   
/*    */   private String myInspectionId;
/*    */   private String myComponentId;
/*    */   
/*    */   public LwInspectionSuppression(String inspectionId, String componentId) {
/* 29 */     this.myInspectionId = inspectionId;
/* 30 */     this.myComponentId = componentId;
/*    */   }
/*    */   
/*    */   public String getInspectionId() {
/* 34 */     return this.myInspectionId;
/*    */   }
/*    */   
/*    */   public String getComponentId() {
/* 38 */     return this.myComponentId;
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\intelli\\uiDesigner\lw\LwInspectionSuppression.class
 * Java compiler version: 3 (47.0)
 * JD-Core Version:       1.1.3
 */