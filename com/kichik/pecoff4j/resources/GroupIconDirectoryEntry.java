/*    */ package com.kichik.pecoff4j.resources;
/*    */ 
/*    */ import com.kichik.pecoff4j.io.IDataReader;
/*    */ import com.kichik.pecoff4j.util.Reflection;
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
/*    */ 
/*    */ public class GroupIconDirectoryEntry
/*    */ {
/*    */   private int width;
/*    */   private int height;
/*    */   private int colorCount;
/*    */   private int reserved;
/*    */   private int planes;
/*    */   private int bitCount;
/*    */   private int bytesInRes;
/*    */   private int id;
/*    */   
/*    */   public static GroupIconDirectoryEntry read(IDataReader dr) throws IOException {
/* 29 */     GroupIconDirectoryEntry ge = new GroupIconDirectoryEntry();
/* 30 */     ge.width = dr.readByte();
/* 31 */     ge.height = dr.readByte();
/* 32 */     ge.colorCount = dr.readByte();
/* 33 */     ge.reserved = dr.readByte();
/* 34 */     ge.planes = dr.readWord();
/* 35 */     ge.bitCount = dr.readWord();
/* 36 */     ge.bytesInRes = dr.readDoubleWord();
/* 37 */     ge.id = dr.readWord();
/*    */     
/* 39 */     return ge;
/*    */   }
/*    */ 
/*    */   
/*    */   public String toString() {
/* 44 */     return Reflection.toString(this);
/*    */   }
/*    */   
/*    */   public int getWidth() {
/* 48 */     return this.width;
/*    */   }
/*    */   
/*    */   public int getHeight() {
/* 52 */     return this.height;
/*    */   }
/*    */   
/*    */   public int getColorCount() {
/* 56 */     return this.colorCount;
/*    */   }
/*    */   
/*    */   public int getReserved() {
/* 60 */     return this.reserved;
/*    */   }
/*    */   
/*    */   public int getPlanes() {
/* 64 */     return this.planes;
/*    */   }
/*    */   
/*    */   public int getBitCount() {
/* 68 */     return this.bitCount;
/*    */   }
/*    */   
/*    */   public int getBytesInRes() {
/* 72 */     return this.bytesInRes;
/*    */   }
/*    */   
/*    */   public int getId() {
/* 76 */     return this.id;
/*    */   }
/*    */   
/*    */   public void setId(int id) {
/* 80 */     this.id = id;
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\kichik\pecoff4j\resources\GroupIconDirectoryEntry.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */