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
/*    */ public class IconImage
/*    */ {
/*    */   private BitmapInfoHeader header;
/*    */   private RGBQuad[] colors;
/*    */   private byte[] xorMask;
/*    */   private byte[] andMask;
/*    */   private byte[] pngData;
/*    */   
/*    */   public BitmapInfoHeader getHeader() {
/* 20 */     return this.header;
/*    */   }
/*    */   
/*    */   public RGBQuad[] getColors() {
/* 24 */     return this.colors;
/*    */   }
/*    */   
/*    */   public byte[] getXorMask() {
/* 28 */     return this.xorMask;
/*    */   }
/*    */   
/*    */   public byte[] getAndMask() {
/* 32 */     return this.andMask;
/*    */   }
/*    */   
/*    */   public byte[] getPNG() {
/* 36 */     return this.pngData;
/*    */   }
/*    */   
/*    */   public void setHeader(BitmapInfoHeader header) {
/* 40 */     this.header = header;
/*    */   }
/*    */   
/*    */   public void setColors(RGBQuad[] colors) {
/* 44 */     this.colors = colors;
/*    */   }
/*    */   
/*    */   public void setXorMask(byte[] xorMask) {
/* 48 */     this.xorMask = xorMask;
/*    */   }
/*    */   
/*    */   public void setAndMask(byte[] andMask) {
/* 52 */     this.andMask = andMask;
/*    */   }
/*    */   
/*    */   public void setPngData(byte[] pngData) {
/* 56 */     this.pngData = pngData;
/*    */   }
/*    */   
/*    */   public int sizeOf() {
/* 60 */     return (this.header == null) ? this.pngData.length : (
/*    */       
/* 62 */       this.header.getSize() + ((this.colors == null) ? 0 : (this.colors.length * 4)) + this.xorMask.length + this.andMask.length);
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\kichik\pecoff4j\resources\IconImage.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */