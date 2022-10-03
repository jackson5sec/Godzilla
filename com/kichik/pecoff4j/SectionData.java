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
/*    */ public class SectionData
/*    */ {
/*    */   private byte[] data;
/*    */   private byte[] preamble;
/*    */   
/*    */   public byte[] getPreamble() {
/* 17 */     return this.preamble;
/*    */   }
/*    */   
/*    */   public void setPreamble(byte[] preamble) {
/* 21 */     this.preamble = preamble;
/*    */   }
/*    */   
/*    */   public byte[] getData() {
/* 25 */     return this.data;
/*    */   }
/*    */   
/*    */   public void setData(byte[] data) {
/* 29 */     this.data = data;
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\kichik\pecoff4j\SectionData.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */