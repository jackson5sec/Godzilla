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
/*    */ public class RVAConverter
/*    */ {
/*    */   private int[] virtualAddress;
/*    */   private int[] pointerToRawData;
/*    */   
/*    */   public RVAConverter(int[] virtualAddress, int[] pointerToRawData) {
/* 17 */     this.virtualAddress = virtualAddress;
/* 18 */     this.pointerToRawData = pointerToRawData;
/*    */   }
/*    */   
/*    */   public int convertVirtualAddressToRawDataPointer(int virtualAddress) {
/* 22 */     for (int i = 0; i < this.virtualAddress.length; i++) {
/* 23 */       if (virtualAddress < this.virtualAddress[i]) {
/* 24 */         if (i > 0) {
/* 25 */           int j = this.pointerToRawData[i - 1];
/* 26 */           int k = this.virtualAddress[i - 1];
/* 27 */           return j + virtualAddress - k;
/*    */         } 
/* 29 */         return virtualAddress;
/*    */       } 
/*    */     } 
/*    */ 
/*    */ 
/*    */     
/* 35 */     int prd = this.pointerToRawData[this.virtualAddress.length - 1];
/* 36 */     int vad = this.virtualAddress[this.virtualAddress.length - 1];
/* 37 */     return prd + virtualAddress - vad;
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\kichik\pecoff4j\RVAConverter.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */