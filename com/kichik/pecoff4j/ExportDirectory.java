/*     */ package com.kichik.pecoff4j;
/*     */ 
/*     */ import com.kichik.pecoff4j.util.DataObject;
/*     */ import com.kichik.pecoff4j.util.Reflection;
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
/*     */ public class ExportDirectory
/*     */   extends DataObject
/*     */ {
/*     */   private long exportFlags;
/*     */   private long timeDateStamp;
/*     */   private int majorVersion;
/*     */   private int minorVersion;
/*     */   private long nameRVA;
/*     */   private long ordinalBase;
/*     */   private long addressTableEntries;
/*     */   private long numberOfNamePointers;
/*     */   private long exportAddressTableRVA;
/*     */   private long namePointerRVA;
/*     */   private long ordinalTableRVA;
/*     */   
/*     */   public long getExportFlags() {
/*  33 */     return this.exportFlags;
/*     */   }
/*     */   
/*     */   public long getTimeDateStamp() {
/*  37 */     return this.timeDateStamp;
/*     */   }
/*     */   
/*     */   public int getMajorVersion() {
/*  41 */     return this.majorVersion;
/*     */   }
/*     */   
/*     */   public int getMinorVersion() {
/*  45 */     return this.minorVersion;
/*     */   }
/*     */   
/*     */   public long getNameRVA() {
/*  49 */     return this.nameRVA;
/*     */   }
/*     */   
/*     */   public long getOrdinalBase() {
/*  53 */     return this.ordinalBase;
/*     */   }
/*     */   
/*     */   public long getAddressTableEntries() {
/*  57 */     return this.addressTableEntries;
/*     */   }
/*     */   
/*     */   public long getNumberOfNamePointers() {
/*  61 */     return this.numberOfNamePointers;
/*     */   }
/*     */   
/*     */   public long getExportAddressTableRVA() {
/*  65 */     return this.exportAddressTableRVA;
/*     */   }
/*     */   
/*     */   public long getNamePointerRVA() {
/*  69 */     return this.namePointerRVA;
/*     */   }
/*     */   
/*     */   public long getOrdinalTableRVA() {
/*  73 */     return this.ordinalTableRVA;
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/*  78 */     return Reflection.toString(this);
/*     */   }
/*     */   
/*     */   public void setExportFlags(long exportFlags) {
/*  82 */     this.exportFlags = exportFlags;
/*     */   }
/*     */   
/*     */   public void setTimeDateStamp(long timeDateStamp) {
/*  86 */     this.timeDateStamp = timeDateStamp;
/*     */   }
/*     */   
/*     */   public void setMajorVersion(int majorVersion) {
/*  90 */     this.majorVersion = majorVersion;
/*     */   }
/*     */   
/*     */   public void setMinorVersion(int minorVersion) {
/*  94 */     this.minorVersion = minorVersion;
/*     */   }
/*     */   
/*     */   public void setNameRVA(long nameRVA) {
/*  98 */     this.nameRVA = nameRVA;
/*     */   }
/*     */   
/*     */   public void setOrdinalBase(long ordinalBase) {
/* 102 */     this.ordinalBase = ordinalBase;
/*     */   }
/*     */   
/*     */   public void setAddressTableEntries(long addressTableEntries) {
/* 106 */     this.addressTableEntries = addressTableEntries;
/*     */   }
/*     */   
/*     */   public void setNumberOfNamePointers(long numberOfNamePointers) {
/* 110 */     this.numberOfNamePointers = numberOfNamePointers;
/*     */   }
/*     */   
/*     */   public void setExportAddressTableRVA(long exportAddressTableRVA) {
/* 114 */     this.exportAddressTableRVA = exportAddressTableRVA;
/*     */   }
/*     */   
/*     */   public void setNamePointerRVA(long namePointerRVA) {
/* 118 */     this.namePointerRVA = namePointerRVA;
/*     */   }
/*     */   
/*     */   public void setOrdinalTableRVA(long ordinalTableRVA) {
/* 122 */     this.ordinalTableRVA = ordinalTableRVA;
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\kichik\pecoff4j\ExportDirectory.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */