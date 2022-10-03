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
/*    */ public class ResourceEntry
/*    */ {
/*    */   private int id;
/*    */   private String name;
/*    */   private int offset;
/*    */   private byte[] data;
/*    */   private ResourceDirectory directory;
/*    */   private int dataRVA;
/*    */   private int codePage;
/*    */   private int reserved;
/*    */   
/*    */   public int getId() {
/* 23 */     return this.id;
/*    */   }
/*    */   
/*    */   public void setId(int id) {
/* 27 */     this.id = id;
/*    */   }
/*    */   
/*    */   public String getName() {
/* 31 */     return this.name;
/*    */   }
/*    */   
/*    */   public void setName(String name) {
/* 35 */     this.name = name;
/*    */   }
/*    */   
/*    */   public int getOffset() {
/* 39 */     return this.offset;
/*    */   }
/*    */   
/*    */   public void setOffset(int offset) {
/* 43 */     this.offset = offset;
/*    */   }
/*    */   
/*    */   public byte[] getData() {
/* 47 */     return this.data;
/*    */   }
/*    */   
/*    */   public void setData(byte[] data) {
/* 51 */     this.data = data;
/*    */   }
/*    */   
/*    */   public ResourceDirectory getDirectory() {
/* 55 */     return this.directory;
/*    */   }
/*    */   
/*    */   public void setDirectory(ResourceDirectory directory) {
/* 59 */     this.directory = directory;
/*    */   }
/*    */   
/*    */   public int getDataRVA() {
/* 63 */     return this.dataRVA;
/*    */   }
/*    */   
/*    */   public void setDataRVA(int dataRVA) {
/* 67 */     this.dataRVA = dataRVA;
/*    */   }
/*    */   
/*    */   public int getCodePage() {
/* 71 */     return this.codePage;
/*    */   }
/*    */   
/*    */   public void setCodePage(int codePage) {
/* 75 */     this.codePage = codePage;
/*    */   }
/*    */   
/*    */   public int getReserved() {
/* 79 */     return this.reserved;
/*    */   }
/*    */   
/*    */   public void setReserved(int reserved) {
/* 83 */     this.reserved = reserved;
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\kichik\pecoff4j\ResourceEntry.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */