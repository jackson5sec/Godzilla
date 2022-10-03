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
/*    */ public class Bitmap
/*    */ {
/*    */   private BitmapFileHeader fileHeader;
/*    */   private BitmapInfoHeader infoHeader;
/*    */   private byte[] colors;
/*    */   private byte[] bitmapBits;
/*    */   
/*    */   public BitmapFileHeader getFileHeader() {
/* 19 */     return this.fileHeader;
/*    */   }
/*    */   
/*    */   public BitmapInfoHeader getInfoHeader() {
/* 23 */     return this.infoHeader;
/*    */   }
/*    */   
/*    */   public byte[] getColors() {
/* 27 */     return this.colors;
/*    */   }
/*    */   
/*    */   public byte[] getBitmapBits() {
/* 31 */     return this.bitmapBits;
/*    */   }
/*    */   
/*    */   public void setFileHeader(BitmapFileHeader fileHeader) {
/* 35 */     this.fileHeader = fileHeader;
/*    */   }
/*    */   
/*    */   public void setInfoHeader(BitmapInfoHeader infoHeader) {
/* 39 */     this.infoHeader = infoHeader;
/*    */   }
/*    */   
/*    */   public void setColors(byte[] colors) {
/* 43 */     this.colors = colors;
/*    */   }
/*    */   
/*    */   public void setBitmapBits(byte[] bitmapBits) {
/* 47 */     this.bitmapBits = bitmapBits;
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\kichik\pecoff4j\resources\Bitmap.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */