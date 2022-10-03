/*    */ package com.kichik.pecoff4j;
/*    */ 
/*    */ import java.util.Arrays;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class PESignature
/*    */ {
/* 15 */   private static byte[] expected1 = new byte[] { 80, 69, 0, 0 };
/* 16 */   private static byte[] expected2 = new byte[] { 80, 105, 0, 0 };
/*    */   private byte[] signature;
/*    */   
/*    */   public byte[] getSignature() {
/* 20 */     return this.signature;
/*    */   }
/*    */   
/*    */   public void setSignature(byte[] signature) {
/* 24 */     this.signature = signature;
/*    */   }
/*    */   
/*    */   public boolean isValid() {
/* 28 */     return (Arrays.equals(expected1, this.signature) || 
/* 29 */       Arrays.equals(expected2, this.signature));
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\kichik\pecoff4j\PESignature.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */