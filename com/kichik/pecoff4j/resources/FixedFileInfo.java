/*     */ package com.kichik.pecoff4j.resources;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class FixedFileInfo
/*     */ {
/*     */   private int signature;
/*     */   private int strucVersion;
/*     */   private int fileVersionMS;
/*     */   private int fileVersionLS;
/*     */   private int productVersionMS;
/*     */   private int productVersionLS;
/*     */   private int fileFlagMask;
/*     */   private int fileFlags;
/*     */   private int fileOS;
/*     */   private int fileType;
/*     */   private int fileSubtype;
/*     */   private int fileDateMS;
/*     */   private int fileDateLS;
/*     */   
/*     */   public int getSignature() {
/*  28 */     return this.signature;
/*     */   }
/*     */   
/*     */   public void setSignature(int signature) {
/*  32 */     this.signature = signature;
/*     */   }
/*     */   
/*     */   public int getStrucVersion() {
/*  36 */     return this.strucVersion;
/*     */   }
/*     */   
/*     */   public void setStrucVersion(int strucVersion) {
/*  40 */     this.strucVersion = strucVersion;
/*     */   }
/*     */   
/*     */   public int getFileVersionMS() {
/*  44 */     return this.fileVersionMS;
/*     */   }
/*     */   
/*     */   public void setFileVersionMS(int fileVersionMS) {
/*  48 */     this.fileVersionMS = fileVersionMS;
/*     */   }
/*     */   
/*     */   public int getFileVersionLS() {
/*  52 */     return this.fileVersionLS;
/*     */   }
/*     */   
/*     */   public void setFileVersionLS(int fileVersionLS) {
/*  56 */     this.fileVersionLS = fileVersionLS;
/*     */   }
/*     */   
/*     */   public int getProductVersionMS() {
/*  60 */     return this.productVersionMS;
/*     */   }
/*     */   
/*     */   public void setProductVersionMS(int productVersionMS) {
/*  64 */     this.productVersionMS = productVersionMS;
/*     */   }
/*     */   
/*     */   public int getProductVersionLS() {
/*  68 */     return this.productVersionLS;
/*     */   }
/*     */   
/*     */   public void setProductVersionLS(int productVersionLS) {
/*  72 */     this.productVersionLS = productVersionLS;
/*     */   }
/*     */   
/*     */   public int getFileFlagMask() {
/*  76 */     return this.fileFlagMask;
/*     */   }
/*     */   
/*     */   public void setFileFlagMask(int fileFlagMask) {
/*  80 */     this.fileFlagMask = fileFlagMask;
/*     */   }
/*     */   
/*     */   public int getFileFlags() {
/*  84 */     return this.fileFlags;
/*     */   }
/*     */   
/*     */   public void setFileFlags(int fileFlags) {
/*  88 */     this.fileFlags = fileFlags;
/*     */   }
/*     */   
/*     */   public int getFileType() {
/*  92 */     return this.fileType;
/*     */   }
/*     */   
/*     */   public void setFileType(int fileType) {
/*  96 */     this.fileType = fileType;
/*     */   }
/*     */   
/*     */   public int getFileSubtype() {
/* 100 */     return this.fileSubtype;
/*     */   }
/*     */   
/*     */   public void setFileSubtype(int fileSubtype) {
/* 104 */     this.fileSubtype = fileSubtype;
/*     */   }
/*     */   
/*     */   public int getFileDateMS() {
/* 108 */     return this.fileDateMS;
/*     */   }
/*     */   
/*     */   public void setFileDateMS(int fileDateMS) {
/* 112 */     this.fileDateMS = fileDateMS;
/*     */   }
/*     */   
/*     */   public int getFileDateLS() {
/* 116 */     return this.fileDateLS;
/*     */   }
/*     */   
/*     */   public void setFileDateLS(int fileDateLS) {
/* 120 */     this.fileDateLS = fileDateLS;
/*     */   }
/*     */   
/*     */   public static int sizeOf() {
/* 124 */     return 52;
/*     */   }
/*     */   
/*     */   public int getFileOS() {
/* 128 */     return this.fileOS;
/*     */   }
/*     */   
/*     */   public void setFileOS(int fileOS) {
/* 132 */     this.fileOS = fileOS;
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\kichik\pecoff4j\resources\FixedFileInfo.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */