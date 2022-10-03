/*     */ package com.kichik.pecoff4j;
/*     */ 
/*     */ import com.kichik.pecoff4j.util.IntMap;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ImageData
/*     */ {
/*     */   private byte[] headerPadding;
/*     */   private ExportDirectory exportTable;
/*     */   private ImportDirectory importTable;
/*     */   private ResourceDirectory resourceTable;
/*     */   private byte[] exceptionTable;
/*     */   private AttributeCertificateTable certificateTable;
/*     */   private byte[] baseRelocationTable;
/*     */   private DebugDirectory debug;
/*     */   private byte[] architecture;
/*     */   private byte[] globalPtr;
/*     */   private byte[] tlsTable;
/*     */   private LoadConfigDirectory loadConfigTable;
/*     */   private BoundImportDirectoryTable boundImports;
/*     */   private byte[] iat;
/*     */   private byte[] delayImportDescriptor;
/*     */   private byte[] clrRuntimeHeader;
/*     */   private byte[] reserved;
/*     */   private byte[] debugRawDataPreamble;
/*     */   private byte[] debugRawData;
/*  40 */   private IntMap preambles = new IntMap();
/*     */   
/*     */   private byte[] trailingData;
/*     */ 
/*     */   
/*     */   public byte[] getHeaderPadding() {
/*  46 */     return this.headerPadding;
/*     */   }
/*     */   
/*     */   public void setHeaderPadding(byte[] headerPadding) {
/*  50 */     this.headerPadding = headerPadding;
/*     */   }
/*     */   
/*     */   public byte[] getPreamble(int directory) {
/*  54 */     return (byte[])this.preambles.get(directory);
/*     */   }
/*     */   
/*     */   public void put(int directory, byte[] preamble) {
/*  58 */     this.preambles.put(directory, preamble);
/*     */   }
/*     */   
/*     */   public ExportDirectory getExportTable() {
/*  62 */     return this.exportTable;
/*     */   }
/*     */   
/*     */   public void setExportTable(ExportDirectory exportTable) {
/*  66 */     this.exportTable = exportTable;
/*     */   }
/*     */   
/*     */   public ImportDirectory getImportTable() {
/*  70 */     return this.importTable;
/*     */   }
/*     */   
/*     */   public void setImportTable(ImportDirectory importTable) {
/*  74 */     this.importTable = importTable;
/*     */   }
/*     */   
/*     */   public ResourceDirectory getResourceTable() {
/*  78 */     return this.resourceTable;
/*     */   }
/*     */   
/*     */   public void setResourceTable(ResourceDirectory resourceTable) {
/*  82 */     this.resourceTable = resourceTable;
/*     */   }
/*     */   
/*     */   public byte[] getExceptionTable() {
/*  86 */     return this.exceptionTable;
/*     */   }
/*     */   
/*     */   public void setExceptionTable(byte[] exceptionTable) {
/*  90 */     this.exceptionTable = exceptionTable;
/*     */   }
/*     */   
/*     */   public AttributeCertificateTable getCertificateTable() {
/*  94 */     return this.certificateTable;
/*     */   }
/*     */   
/*     */   public void setCertificateTable(AttributeCertificateTable certificateTable) {
/*  98 */     this.certificateTable = certificateTable;
/*     */   }
/*     */   
/*     */   public byte[] getBaseRelocationTable() {
/* 102 */     return this.baseRelocationTable;
/*     */   }
/*     */   
/*     */   public void setBaseRelocationTable(byte[] baseRelocationTable) {
/* 106 */     this.baseRelocationTable = baseRelocationTable;
/*     */   }
/*     */   
/*     */   public DebugDirectory getDebug() {
/* 110 */     return this.debug;
/*     */   }
/*     */   
/*     */   public void setDebug(DebugDirectory debug) {
/* 114 */     this.debug = debug;
/*     */   }
/*     */   
/*     */   public byte[] getArchitecture() {
/* 118 */     return this.architecture;
/*     */   }
/*     */   
/*     */   public void setArchitecture(byte[] architecture) {
/* 122 */     this.architecture = architecture;
/*     */   }
/*     */   
/*     */   public byte[] getGlobalPtr() {
/* 126 */     return this.globalPtr;
/*     */   }
/*     */   
/*     */   public void setGlobalPtr(byte[] globalPtr) {
/* 130 */     this.globalPtr = globalPtr;
/*     */   }
/*     */   
/*     */   public byte[] getTlsTable() {
/* 134 */     return this.tlsTable;
/*     */   }
/*     */   
/*     */   public void setTlsTable(byte[] tlsTable) {
/* 138 */     this.tlsTable = tlsTable;
/*     */   }
/*     */   
/*     */   public LoadConfigDirectory getLoadConfigTable() {
/* 142 */     return this.loadConfigTable;
/*     */   }
/*     */   
/*     */   public void setLoadConfigTable(LoadConfigDirectory loadConfigTable) {
/* 146 */     this.loadConfigTable = loadConfigTable;
/*     */   }
/*     */   
/*     */   public BoundImportDirectoryTable getBoundImports() {
/* 150 */     return this.boundImports;
/*     */   }
/*     */   
/*     */   public void setBoundImports(BoundImportDirectoryTable boundImports) {
/* 154 */     this.boundImports = boundImports;
/*     */   }
/*     */   
/*     */   public byte[] getIat() {
/* 158 */     return this.iat;
/*     */   }
/*     */   
/*     */   public void setIat(byte[] iat) {
/* 162 */     this.iat = iat;
/*     */   }
/*     */   
/*     */   public byte[] getDelayImportDescriptor() {
/* 166 */     return this.delayImportDescriptor;
/*     */   }
/*     */   
/*     */   public void setDelayImportDescriptor(byte[] delayImportDescriptor) {
/* 170 */     this.delayImportDescriptor = delayImportDescriptor;
/*     */   }
/*     */   
/*     */   public byte[] getClrRuntimeHeader() {
/* 174 */     return this.clrRuntimeHeader;
/*     */   }
/*     */   
/*     */   public void setClrRuntimeHeader(byte[] clrRuntimeHeader) {
/* 178 */     this.clrRuntimeHeader = clrRuntimeHeader;
/*     */   }
/*     */   
/*     */   public byte[] getReserved() {
/* 182 */     return this.reserved;
/*     */   }
/*     */   
/*     */   public void setReserved(byte[] reserved) {
/* 186 */     this.reserved = reserved;
/*     */   }
/*     */   
/*     */   public byte[] getDebugRawData() {
/* 190 */     return this.debugRawData;
/*     */   }
/*     */   
/*     */   public void setDebugRawData(byte[] debugRawData) {
/* 194 */     this.debugRawData = debugRawData;
/*     */   }
/*     */   
/*     */   public byte[] getTrailingData() {
/* 198 */     return this.trailingData;
/*     */   }
/*     */   
/*     */   public void setTrailingData(byte[] trailingData) {
/* 202 */     this.trailingData = trailingData;
/*     */   }
/*     */   
/*     */   public byte[] getDebugRawDataPreamble() {
/* 206 */     return this.debugRawDataPreamble;
/*     */   }
/*     */   
/*     */   public void setDebugRawDataPreamble(byte[] debugRawDataPreamble) {
/* 210 */     this.debugRawDataPreamble = debugRawDataPreamble;
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\kichik\pecoff4j\ImageData.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */