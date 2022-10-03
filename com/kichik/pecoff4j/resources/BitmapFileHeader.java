/*    */ package com.kichik.pecoff4j.resources;
/*    */ 
/*    */ import com.kichik.pecoff4j.io.DataReader;
/*    */ import java.io.IOException;
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
/*    */ public class BitmapFileHeader
/*    */ {
/*    */   private int type;
/*    */   private int size;
/*    */   private int reserved1;
/*    */   private int reserved2;
/*    */   private int offBits;
/*    */   
/*    */   public static BitmapFileHeader read(DataReader dr) throws IOException {
/* 24 */     BitmapFileHeader bfh = new BitmapFileHeader();
/* 25 */     bfh.type = dr.readWord();
/* 26 */     bfh.size = dr.readDoubleWord();
/* 27 */     bfh.reserved1 = dr.readWord();
/* 28 */     bfh.reserved2 = dr.readWord();
/* 29 */     bfh.offBits = dr.readDoubleWord();
/*    */     
/* 31 */     return bfh;
/*    */   }
/*    */   
/*    */   public int getType() {
/* 35 */     return this.type;
/*    */   }
/*    */   
/*    */   public int getSize() {
/* 39 */     return this.size;
/*    */   }
/*    */   
/*    */   public int getReserved1() {
/* 43 */     return this.reserved1;
/*    */   }
/*    */   
/*    */   public int getReserved2() {
/* 47 */     return this.reserved2;
/*    */   }
/*    */   
/*    */   public int getOffBits() {
/* 51 */     return this.offBits;
/*    */   }
/*    */   
/*    */   public void setType(int type) {
/* 55 */     this.type = type;
/*    */   }
/*    */   
/*    */   public void setSize(int size) {
/* 59 */     this.size = size;
/*    */   }
/*    */   
/*    */   public void setReserved1(int reserved1) {
/* 63 */     this.reserved1 = reserved1;
/*    */   }
/*    */   
/*    */   public void setReserved2(int reserved2) {
/* 67 */     this.reserved2 = reserved2;
/*    */   }
/*    */   
/*    */   public void setOffBits(int offBits) {
/* 71 */     this.offBits = offBits;
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\kichik\pecoff4j\resources\BitmapFileHeader.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */