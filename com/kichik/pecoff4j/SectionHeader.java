/*     */ package com.kichik.pecoff4j;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class SectionHeader
/*     */ {
/*     */   private String name;
/*     */   private int virtualSize;
/*     */   private int virtualAddress;
/*     */   private int sizeOfRawData;
/*     */   private int pointerToRawData;
/*     */   private int pointerToRelocations;
/*     */   private int pointerToLineNumbers;
/*     */   private int numberOfRelocations;
/*     */   private int numberOfLineNumbers;
/*     */   private int characteristics;
/*     */   
/*     */   public String getName() {
/*  25 */     return this.name;
/*     */   }
/*     */   
/*     */   public int getVirtualSize() {
/*  29 */     return this.virtualSize;
/*     */   }
/*     */   
/*     */   public int getVirtualAddress() {
/*  33 */     return this.virtualAddress;
/*     */   }
/*     */   
/*     */   public int getSizeOfRawData() {
/*  37 */     return this.sizeOfRawData;
/*     */   }
/*     */   
/*     */   public int getPointerToRawData() {
/*  41 */     return this.pointerToRawData;
/*     */   }
/*     */   
/*     */   public int getPointerToRelocations() {
/*  45 */     return this.pointerToRelocations;
/*     */   }
/*     */   
/*     */   public int getPointerToLineNumbers() {
/*  49 */     return this.pointerToLineNumbers;
/*     */   }
/*     */   
/*     */   public int getNumberOfRelocations() {
/*  53 */     return this.numberOfRelocations;
/*     */   }
/*     */   
/*     */   public int getNumberOfLineNumbers() {
/*  57 */     return this.numberOfLineNumbers;
/*     */   }
/*     */   
/*     */   public int getCharacteristics() {
/*  61 */     return this.characteristics;
/*     */   }
/*     */   
/*     */   public void setName(String name) {
/*  65 */     this.name = name;
/*     */   }
/*     */   
/*     */   public void setVirtualSize(int virtualSize) {
/*  69 */     this.virtualSize = virtualSize;
/*     */   }
/*     */   
/*     */   public void setVirtualAddress(int virtualAddress) {
/*  73 */     this.virtualAddress = virtualAddress;
/*     */   }
/*     */   
/*     */   public void setSizeOfRawData(int sizeOfRawData) {
/*  77 */     this.sizeOfRawData = sizeOfRawData;
/*     */   }
/*     */   
/*     */   public void setPointerToRawData(int pointerToRawData) {
/*  81 */     this.pointerToRawData = pointerToRawData;
/*     */   }
/*     */   
/*     */   public void setPointerToRelocations(int pointerToRelocations) {
/*  85 */     this.pointerToRelocations = pointerToRelocations;
/*     */   }
/*     */   
/*     */   public void setPointerToLineNumbers(int pointerToLineNumbers) {
/*  89 */     this.pointerToLineNumbers = pointerToLineNumbers;
/*     */   }
/*     */   
/*     */   public void setNumberOfRelocations(int numberOfRelocations) {
/*  93 */     this.numberOfRelocations = numberOfRelocations;
/*     */   }
/*     */   
/*     */   public void setNumberOfLineNumbers(int numberOfLineNumbers) {
/*  97 */     this.numberOfLineNumbers = numberOfLineNumbers;
/*     */   }
/*     */   
/*     */   public void setCharacteristics(int characteristics) {
/* 101 */     this.characteristics = characteristics;
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\kichik\pecoff4j\SectionHeader.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */