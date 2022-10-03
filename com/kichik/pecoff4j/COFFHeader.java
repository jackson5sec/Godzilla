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
/*    */ public class COFFHeader
/*    */ {
/*    */   private int machine;
/*    */   private int numberOfSections;
/*    */   private int timeDateStamp;
/*    */   private int pointerToSymbolTable;
/*    */   private int numberOfSymbols;
/*    */   private int sizeOfOptionalHeader;
/*    */   private int characteristics;
/*    */   
/*    */   public int getMachine() {
/* 22 */     return this.machine;
/*    */   }
/*    */   
/*    */   public int getNumberOfSections() {
/* 26 */     return this.numberOfSections;
/*    */   }
/*    */   
/*    */   public int getTimeDateStamp() {
/* 30 */     return this.timeDateStamp;
/*    */   }
/*    */   
/*    */   public int getPointerToSymbolTable() {
/* 34 */     return this.pointerToSymbolTable;
/*    */   }
/*    */   
/*    */   public int getNumberOfSymbols() {
/* 38 */     return this.numberOfSymbols;
/*    */   }
/*    */   
/*    */   public int getSizeOfOptionalHeader() {
/* 42 */     return this.sizeOfOptionalHeader;
/*    */   }
/*    */   
/*    */   public int getCharacteristics() {
/* 46 */     return this.characteristics;
/*    */   }
/*    */   
/*    */   public void setMachine(int machine) {
/* 50 */     this.machine = machine;
/*    */   }
/*    */   
/*    */   public void setNumberOfSections(int numberOfSections) {
/* 54 */     this.numberOfSections = numberOfSections;
/*    */   }
/*    */   
/*    */   public void setTimeDateStamp(int timeDateStamp) {
/* 58 */     this.timeDateStamp = timeDateStamp;
/*    */   }
/*    */   
/*    */   public void setPointerToSymbolTable(int pointerToSymbolTable) {
/* 62 */     this.pointerToSymbolTable = pointerToSymbolTable;
/*    */   }
/*    */   
/*    */   public void setNumberOfSymbols(int numberOfSymbols) {
/* 66 */     this.numberOfSymbols = numberOfSymbols;
/*    */   }
/*    */   
/*    */   public void setSizeOfOptionalHeader(int sizeOfOptionalHeader) {
/* 70 */     this.sizeOfOptionalHeader = sizeOfOptionalHeader;
/*    */   }
/*    */   
/*    */   public void setCharacteristics(int characteristics) {
/* 74 */     this.characteristics = characteristics;
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\kichik\pecoff4j\COFFHeader.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */