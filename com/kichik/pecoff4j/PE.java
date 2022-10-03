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
/*    */ public class PE
/*    */ {
/*    */   private DOSHeader dosHeader;
/*    */   private DOSStub stub;
/*    */   private PESignature signature;
/*    */   private COFFHeader coffHeader;
/*    */   private OptionalHeader optionalHeader;
/*    */   private ImageData imageData;
/*    */   private SectionTable sectionTable;
/*    */   private boolean is64bit;
/*    */   
/*    */   public DOSHeader getDosHeader() {
/* 23 */     return this.dosHeader;
/*    */   }
/*    */   
/*    */   public DOSStub getStub() {
/* 27 */     return this.stub;
/*    */   }
/*    */   
/*    */   public PESignature getSignature() {
/* 31 */     return this.signature;
/*    */   }
/*    */   
/*    */   public COFFHeader getCoffHeader() {
/* 35 */     return this.coffHeader;
/*    */   }
/*    */   
/*    */   public OptionalHeader getOptionalHeader() {
/* 39 */     return this.optionalHeader;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean is64() {
/* 44 */     return this.is64bit;
/*    */   }
/*    */   
/*    */   public SectionTable getSectionTable() {
/* 48 */     return this.sectionTable;
/*    */   }
/*    */   
/*    */   public void setDosHeader(DOSHeader dosHeader) {
/* 52 */     this.dosHeader = dosHeader;
/*    */   }
/*    */   
/*    */   public void setStub(DOSStub stub) {
/* 56 */     this.stub = stub;
/*    */   }
/*    */   
/*    */   public void setSignature(PESignature signature) {
/* 60 */     this.signature = signature;
/*    */   }
/*    */   
/*    */   public void setCoffHeader(COFFHeader coffHeader) {
/* 64 */     this.coffHeader = coffHeader;
/*    */   }
/*    */   
/*    */   public void setOptionalHeader(OptionalHeader optionalHeader) {
/* 68 */     this.optionalHeader = optionalHeader;
/*    */   }
/*    */ 
/*    */   
/*    */   public void set64(boolean is64bit) {
/* 73 */     this.is64bit = is64bit;
/*    */   }
/*    */   
/*    */   public void setSectionTable(SectionTable sectionTable) {
/* 77 */     this.sectionTable = sectionTable;
/*    */   }
/*    */   
/*    */   public ImageData getImageData() {
/* 81 */     if (this.imageData == null)
/* 82 */       this.imageData = new ImageData(); 
/* 83 */     return this.imageData;
/*    */   }
/*    */   
/*    */   public void setImageData(ImageData imageData) {
/* 87 */     this.imageData = imageData;
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\kichik\pecoff4j\PE.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */