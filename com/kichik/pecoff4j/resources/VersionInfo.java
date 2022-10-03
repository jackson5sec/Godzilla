/*    */ package com.kichik.pecoff4j.resources;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class VersionInfo
/*    */ {
/*    */   private int length;
/*    */   private int valueLength;
/*    */   private int type;
/*    */   private String key;
/*    */   private FixedFileInfo fixedFileInfo;
/*    */   private StringFileInfo stringFileInfo;
/*    */   private VarFileInfo varFileInfo;
/*    */   
/*    */   public int getLength() {
/* 22 */     return this.length;
/*    */   }
/*    */   
/*    */   public void setLength(int length) {
/* 26 */     this.length = length;
/*    */   }
/*    */   
/*    */   public int getValueLength() {
/* 30 */     return this.valueLength;
/*    */   }
/*    */   
/*    */   public void setValueLength(int valueLength) {
/* 34 */     this.valueLength = valueLength;
/*    */   }
/*    */   
/*    */   public int getType() {
/* 38 */     return this.type;
/*    */   }
/*    */   
/*    */   public void setType(int type) {
/* 42 */     this.type = type;
/*    */   }
/*    */   
/*    */   public String getKey() {
/* 46 */     return this.key;
/*    */   }
/*    */   
/*    */   public void setKey(String key) {
/* 50 */     this.key = key;
/*    */   }
/*    */   
/*    */   public FixedFileInfo getFixedFileInfo() {
/* 54 */     return this.fixedFileInfo;
/*    */   }
/*    */   
/*    */   public void setFixedFileInfo(FixedFileInfo fixedFileInfo) {
/* 58 */     this.fixedFileInfo = fixedFileInfo;
/*    */   }
/*    */   
/*    */   public StringFileInfo getStringFileInfo() {
/* 62 */     return this.stringFileInfo;
/*    */   }
/*    */   
/*    */   public void setStringFileInfo(StringFileInfo stringFileInfo) {
/* 66 */     this.stringFileInfo = stringFileInfo;
/*    */   }
/*    */   
/*    */   public VarFileInfo getVarFileInfo() {
/* 70 */     return this.varFileInfo;
/*    */   }
/*    */   
/*    */   public void setVarFileInfo(VarFileInfo varFileInfo) {
/* 74 */     this.varFileInfo = varFileInfo;
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\kichik\pecoff4j\resources\VersionInfo.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */