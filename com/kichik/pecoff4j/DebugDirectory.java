/*    */ package com.kichik.pecoff4j;
/*    */ 
/*    */ import com.kichik.pecoff4j.util.DataObject;
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
/*    */ public class DebugDirectory
/*    */   extends DataObject
/*    */ {
/*    */   private int characteristics;
/*    */   private int timeDateStamp;
/*    */   private int majorVersion;
/*    */   private int type;
/*    */   private int sizeOfData;
/*    */   private int addressOfRawData;
/*    */   private int pointerToRawData;
/*    */   
/*    */   public int getCharacteristics() {
/* 28 */     return this.characteristics;
/*    */   }
/*    */   
/*    */   public int getTimeDateStamp() {
/* 32 */     return this.timeDateStamp;
/*    */   }
/*    */   
/*    */   public int getMajorVersion() {
/* 36 */     return this.majorVersion;
/*    */   }
/*    */   
/*    */   public int getType() {
/* 40 */     return this.type;
/*    */   }
/*    */   
/*    */   public int getSizeOfData() {
/* 44 */     return this.sizeOfData;
/*    */   }
/*    */   
/*    */   public int getAddressOfRawData() {
/* 48 */     return this.addressOfRawData;
/*    */   }
/*    */   
/*    */   public int getPointerToRawData() {
/* 52 */     return this.pointerToRawData;
/*    */   }
/*    */   
/*    */   public void setCharacteristics(int characteristics) {
/* 56 */     this.characteristics = characteristics;
/*    */   }
/*    */   
/*    */   public void setTimeDateStamp(int timeDateStamp) {
/* 60 */     this.timeDateStamp = timeDateStamp;
/*    */   }
/*    */   
/*    */   public void setMajorVersion(int majorVersion) {
/* 64 */     this.majorVersion = majorVersion;
/*    */   }
/*    */   
/*    */   public void setType(int type) {
/* 68 */     this.type = type;
/*    */   }
/*    */   
/*    */   public void setSizeOfData(int sizeOfData) {
/* 72 */     this.sizeOfData = sizeOfData;
/*    */   }
/*    */   
/*    */   public void setAddressOfRawData(int addressOfRawData) {
/* 76 */     this.addressOfRawData = addressOfRawData;
/*    */   }
/*    */   
/*    */   public void setPointerToRawData(int pointerToRawData) {
/* 80 */     this.pointerToRawData = pointerToRawData;
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\kichik\pecoff4j\DebugDirectory.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */