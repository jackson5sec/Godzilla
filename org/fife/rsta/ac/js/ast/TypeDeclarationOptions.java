/*    */ package org.fife.rsta.ac.js.ast;
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
/*    */ public class TypeDeclarationOptions
/*    */ {
/*    */   private String ownerScriptName;
/*    */   private boolean supportsLinks;
/*    */   private boolean preProcessing;
/*    */   
/*    */   public TypeDeclarationOptions(String ownerScriptName, boolean supportsLinks, boolean preProcessing) {
/* 21 */     this.ownerScriptName = ownerScriptName;
/* 22 */     this.supportsLinks = supportsLinks;
/* 23 */     this.preProcessing = preProcessing;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public String getOwnerScriptName() {
/* 30 */     return this.ownerScriptName;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void setOwnerScriptName(String ownerScriptName) {
/* 37 */     this.ownerScriptName = ownerScriptName;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public boolean isSupportsLinks() {
/* 44 */     return this.supportsLinks;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void setSupportsLinks(boolean supportsLinks) {
/* 51 */     this.supportsLinks = supportsLinks;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public boolean isPreProcessing() {
/* 58 */     return this.preProcessing;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void setPreProcessing(boolean preProcessing) {
/* 65 */     this.preProcessing = preProcessing;
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\fife\rsta\ac\js\ast\TypeDeclarationOptions.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */