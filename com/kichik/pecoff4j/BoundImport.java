/*    */ package com.kichik.pecoff4j;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class BoundImport
/*    */ {
/*    */   private long timestamp;
/*    */   private int offsetToModuleName;
/*    */   private String moduleName;
/*    */   private int numModuleForwarderRefs;
/*    */   
/*    */   public long getTimestamp() {
/* 19 */     return this.timestamp;
/*    */   }
/*    */   
/*    */   public void setTimestamp(long timestamp) {
/* 23 */     this.timestamp = timestamp;
/*    */   }
/*    */   
/*    */   public String getModuleName() {
/* 27 */     return this.moduleName;
/*    */   }
/*    */   
/*    */   public void setModuleName(String moduleName) {
/* 31 */     this.moduleName = moduleName;
/*    */   }
/*    */   
/*    */   public int getNumberOfModuleForwarderRefs() {
/* 35 */     return this.numModuleForwarderRefs;
/*    */   }
/*    */   
/*    */   public void setNumberOfModuleForwarderRefs(int numModuleForwarderRefs) {
/* 39 */     this.numModuleForwarderRefs = numModuleForwarderRefs;
/*    */   }
/*    */   
/*    */   public int getOffsetToModuleName() {
/* 43 */     return this.offsetToModuleName;
/*    */   }
/*    */   
/*    */   public void setOffsetToModuleName(int offsetToModuleName) {
/* 47 */     this.offsetToModuleName = offsetToModuleName;
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\kichik\pecoff4j\BoundImport.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */