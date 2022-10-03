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
/*     */ 
/*     */ public class DOSHeader
/*     */ {
/*     */   public static final int DOS_MAGIC = 0;
/*     */   private int magic;
/*     */   private int usedBytesInLastPage;
/*     */   private int fileSizeInPages;
/*     */   private int numRelocationItems;
/*     */   private int headerSizeInParagraphs;
/*     */   private int minExtraParagraphs;
/*     */   private int maxExtraParagraphs;
/*     */   private int initialSS;
/*     */   private int initialSP;
/*     */   private int checksum;
/*     */   private int initialIP;
/*     */   private int initialRelativeCS;
/*     */   private int addressOfRelocationTable;
/*     */   private int overlayNumber;
/*     */   private int[] reserved;
/*     */   private int[] reserved2;
/*     */   private int oemId;
/*     */   private int oemInfo;
/*     */   private int addressOfNewExeHeader;
/*     */   private int stubSize;
/*     */   
/*     */   public int getMagic() {
/*  37 */     return this.magic;
/*     */   }
/*     */   
/*     */   public boolean isValidMagic() {
/*  41 */     return (this.magic == 0);
/*     */   }
/*     */   
/*     */   public int getUsedBytesInLastPage() {
/*  45 */     return this.usedBytesInLastPage;
/*     */   }
/*     */   
/*     */   public int getFileSizeInPages() {
/*  49 */     return this.fileSizeInPages;
/*     */   }
/*     */   
/*     */   public int getNumRelocationItems() {
/*  53 */     return this.numRelocationItems;
/*     */   }
/*     */   
/*     */   public int getHeaderSizeInParagraphs() {
/*  57 */     return this.headerSizeInParagraphs;
/*     */   }
/*     */   
/*     */   public int getMinExtraParagraphs() {
/*  61 */     return this.minExtraParagraphs;
/*     */   }
/*     */   
/*     */   public int getMaxExtraParagraphs() {
/*  65 */     return this.maxExtraParagraphs;
/*     */   }
/*     */   
/*     */   public int getInitialSS() {
/*  69 */     return this.initialSS;
/*     */   }
/*     */   
/*     */   public int getInitialSP() {
/*  73 */     return this.initialSP;
/*     */   }
/*     */   
/*     */   public int getChecksum() {
/*  77 */     return this.checksum;
/*     */   }
/*     */   
/*     */   public int getInitialIP() {
/*  81 */     return this.initialIP;
/*     */   }
/*     */   
/*     */   public int getInitialRelativeCS() {
/*  85 */     return this.initialRelativeCS;
/*     */   }
/*     */   
/*     */   public int getAddressOfRelocationTable() {
/*  89 */     return this.addressOfRelocationTable;
/*     */   }
/*     */   
/*     */   public int getOverlayNumber() {
/*  93 */     return this.overlayNumber;
/*     */   }
/*     */   
/*     */   public int getOemId() {
/*  97 */     return this.oemId;
/*     */   }
/*     */   
/*     */   public int getOemInfo() {
/* 101 */     return this.oemInfo;
/*     */   }
/*     */   
/*     */   public int getAddressOfNewExeHeader() {
/* 105 */     return this.addressOfNewExeHeader;
/*     */   }
/*     */   
/*     */   public int[] getReserved() {
/* 109 */     return this.reserved;
/*     */   }
/*     */   
/*     */   public int[] getReserved2() {
/* 113 */     return this.reserved2;
/*     */   }
/*     */   
/*     */   public int getStubSize() {
/* 117 */     return this.stubSize;
/*     */   }
/*     */   
/*     */   public void setMagic(int magic) {
/* 121 */     this.magic = magic;
/*     */   }
/*     */   
/*     */   public void setUsedBytesInLastPage(int usedBytesInLastPage) {
/* 125 */     this.usedBytesInLastPage = usedBytesInLastPage;
/*     */   }
/*     */   
/*     */   public void setFileSizeInPages(int fileSizeInPages) {
/* 129 */     this.fileSizeInPages = fileSizeInPages;
/*     */   }
/*     */   
/*     */   public void setNumRelocationItems(int numRelocationItems) {
/* 133 */     this.numRelocationItems = numRelocationItems;
/*     */   }
/*     */   
/*     */   public void setHeaderSizeInParagraphs(int headerSizeInParagraphs) {
/* 137 */     this.headerSizeInParagraphs = headerSizeInParagraphs;
/*     */   }
/*     */   
/*     */   public void setMinExtraParagraphs(int minExtraParagraphs) {
/* 141 */     this.minExtraParagraphs = minExtraParagraphs;
/*     */   }
/*     */   
/*     */   public void setMaxExtraParagraphs(int maxExtraParagraphs) {
/* 145 */     this.maxExtraParagraphs = maxExtraParagraphs;
/*     */   }
/*     */   
/*     */   public void setInitialSS(int initialSS) {
/* 149 */     this.initialSS = initialSS;
/*     */   }
/*     */   
/*     */   public void setInitialSP(int initialSP) {
/* 153 */     this.initialSP = initialSP;
/*     */   }
/*     */   
/*     */   public void setChecksum(int checksum) {
/* 157 */     this.checksum = checksum;
/*     */   }
/*     */   
/*     */   public void setInitialIP(int initialIP) {
/* 161 */     this.initialIP = initialIP;
/*     */   }
/*     */   
/*     */   public void setInitialRelativeCS(int initialRelativeCS) {
/* 165 */     this.initialRelativeCS = initialRelativeCS;
/*     */   }
/*     */   
/*     */   public void setAddressOfRelocationTable(int addressOfRelocationTable) {
/* 169 */     this.addressOfRelocationTable = addressOfRelocationTable;
/*     */   }
/*     */   
/*     */   public void setOverlayNumber(int overlayNumber) {
/* 173 */     this.overlayNumber = overlayNumber;
/*     */   }
/*     */   
/*     */   public void setReserved(int[] reserved) {
/* 177 */     this.reserved = reserved;
/*     */   }
/*     */   
/*     */   public void setReserved2(int[] reserved2) {
/* 181 */     this.reserved2 = reserved2;
/*     */   }
/*     */   
/*     */   public void setOemId(int oemId) {
/* 185 */     this.oemId = oemId;
/*     */   }
/*     */   
/*     */   public void setOemInfo(int oemInfo) {
/* 189 */     this.oemInfo = oemInfo;
/*     */   }
/*     */   
/*     */   public void setAddressOfNewExeHeader(int addressOfNewExeHeader) {
/* 193 */     this.addressOfNewExeHeader = addressOfNewExeHeader;
/*     */   }
/*     */   
/*     */   public void setStubSize(int stubSize) {
/* 197 */     this.stubSize = stubSize;
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\kichik\pecoff4j\DOSHeader.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */