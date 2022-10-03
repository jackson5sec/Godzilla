/*    */ package com.intellij.uiDesigner.compiler;
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
/*    */ public class CodeGenerationException
/*    */   extends Exception
/*    */ {
/*    */   private String myComponentId;
/*    */   
/*    */   public CodeGenerationException(String componentId, String message) {
/* 22 */     super(message);
/* 23 */     this.myComponentId = componentId;
/*    */   }
/*    */   
/*    */   public String getComponentId() {
/* 27 */     return this.myComponentId;
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\intelli\\uiDesigner\compiler\CodeGenerationException.class
 * Java compiler version: 3 (47.0)
 * JD-Core Version:       1.1.3
 */