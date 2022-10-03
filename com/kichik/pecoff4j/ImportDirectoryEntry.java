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
/*    */ public class ImportDirectoryEntry
/*    */ {
/*    */   private int importLookupTableRVA;
/*    */   private int timeDateStamp;
/*    */   private int forwarderChain;
/*    */   private int nameRVA;
/*    */   private int importAddressTableRVA;
/*    */   
/*    */   public int getImportLookupTableRVA() {
/* 20 */     return this.importLookupTableRVA;
/*    */   }
/*    */   
/*    */   public int getTimeDateStamp() {
/* 24 */     return this.timeDateStamp;
/*    */   }
/*    */   
/*    */   public int getForwarderChain() {
/* 28 */     return this.forwarderChain;
/*    */   }
/*    */   
/*    */   public int getNameRVA() {
/* 32 */     return this.nameRVA;
/*    */   }
/*    */   
/*    */   public int getImportAddressTableRVA() {
/* 36 */     return this.importAddressTableRVA;
/*    */   }
/*    */   
/*    */   public void setImportLookupTableRVA(int importLookupTableRVA) {
/* 40 */     this.importLookupTableRVA = importLookupTableRVA;
/*    */   }
/*    */   
/*    */   public void setTimeDateStamp(int timeDateStamp) {
/* 44 */     this.timeDateStamp = timeDateStamp;
/*    */   }
/*    */   
/*    */   public void setForwarderChain(int forwarderChain) {
/* 48 */     this.forwarderChain = forwarderChain;
/*    */   }
/*    */   
/*    */   public void setNameRVA(int nameRVA) {
/* 52 */     this.nameRVA = nameRVA;
/*    */   }
/*    */   
/*    */   public void setImportAddressTableRVA(int importAddressTableRVA) {
/* 56 */     this.importAddressTableRVA = importAddressTableRVA;
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\kichik\pecoff4j\ImportDirectoryEntry.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */