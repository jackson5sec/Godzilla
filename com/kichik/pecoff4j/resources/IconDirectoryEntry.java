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
/*    */ public class IconDirectoryEntry
/*    */ {
/*    */   private int width;
/*    */   private int height;
/*    */   private int colorCount;
/*    */   private int reserved;
/*    */   private int planes;
/*    */   private int bitCount;
/*    */   private int bytesInRes;
/*    */   private int offset;
/*    */   
/*    */   public int getWidth() {
/* 23 */     return this.width;
/*    */   }
/*    */   
/*    */   public int getHeight() {
/* 27 */     return this.height;
/*    */   }
/*    */   
/*    */   public int getColorCount() {
/* 31 */     return this.colorCount;
/*    */   }
/*    */   
/*    */   public int getReserved() {
/* 35 */     return this.reserved;
/*    */   }
/*    */   
/*    */   public int getPlanes() {
/* 39 */     return this.planes;
/*    */   }
/*    */   
/*    */   public int getBitCount() {
/* 43 */     return this.bitCount;
/*    */   }
/*    */   
/*    */   public int getBytesInRes() {
/* 47 */     return this.bytesInRes;
/*    */   }
/*    */   
/*    */   public int getOffset() {
/* 51 */     return this.offset;
/*    */   }
/*    */   
/*    */   public void setWidth(int width) {
/* 55 */     this.width = width;
/*    */   }
/*    */   
/*    */   public void setHeight(int height) {
/* 59 */     this.height = height;
/*    */   }
/*    */   
/*    */   public void setColorCount(int colorCount) {
/* 63 */     this.colorCount = colorCount;
/*    */   }
/*    */   
/*    */   public void setReserved(int reserved) {
/* 67 */     this.reserved = reserved;
/*    */   }
/*    */   
/*    */   public void setPlanes(int planes) {
/* 71 */     this.planes = planes;
/*    */   }
/*    */   
/*    */   public void setBitCount(int bitCount) {
/* 75 */     this.bitCount = bitCount;
/*    */   }
/*    */   
/*    */   public void setBytesInRes(int bytesInRes) {
/* 79 */     this.bytesInRes = bytesInRes;
/*    */   }
/*    */   
/*    */   public void setOffset(int offset) {
/* 83 */     this.offset = offset;
/*    */   }
/*    */   
/*    */   public void copyFrom(GroupIconDirectoryEntry gide) {
/* 87 */     this.width = gide.getWidth();
/* 88 */     this.height = gide.getHeight();
/* 89 */     this.colorCount = gide.getColorCount();
/* 90 */     this.reserved = 0;
/* 91 */     this.planes = gide.getPlanes();
/* 92 */     this.bitCount = gide.getBitCount();
/* 93 */     this.bytesInRes = gide.getBitCount();
/* 94 */     this.offset = 0;
/*    */   }
/*    */   
/*    */   public static int sizeOf() {
/* 98 */     return 16;
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\kichik\pecoff4j\resources\IconDirectoryEntry.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */