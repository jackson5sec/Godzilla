/*     */ package com.kichik.pecoff4j;
/*     */ 
/*     */ import com.kichik.pecoff4j.util.DataObject;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class LoadConfigDirectory
/*     */   extends DataObject
/*     */ {
/*     */   private int size;
/*     */   private int timeDateStamp;
/*     */   private int majorVersion;
/*     */   private int minorVersion;
/*     */   private int globalFlagsClear;
/*     */   private int globalFlagsSet;
/*     */   private int criticalSectionDefaultTimeout;
/*     */   private long deCommitFreeBlockThreshold;
/*     */   private long deCommitTotalFreeThreshold;
/*     */   private long lockPrefixTable;
/*     */   private long maximumAllocationSize;
/*     */   private long virtualMemoryThreshold;
/*     */   private long processAffinityMask;
/*     */   private int processHeapFlags;
/*     */   private int csdVersion;
/*     */   private int reserved;
/*     */   private long editList;
/*     */   private long securityCookie;
/*     */   private long seHandlerTable;
/*     */   private long seHandlerCount;
/*     */   
/*     */   public int getSize() {
/*  37 */     return this.size;
/*     */   }
/*     */   
/*     */   public int getTimeDateStamp() {
/*  41 */     return this.timeDateStamp;
/*     */   }
/*     */   
/*     */   public int getMajorVersion() {
/*  45 */     return this.majorVersion;
/*     */   }
/*     */   
/*     */   public int getMinorVersion() {
/*  49 */     return this.minorVersion;
/*     */   }
/*     */   
/*     */   public int getGlobalFlagsClear() {
/*  53 */     return this.globalFlagsClear;
/*     */   }
/*     */   
/*     */   public int getGlobalFlagsSet() {
/*  57 */     return this.globalFlagsSet;
/*     */   }
/*     */   
/*     */   public int getCriticalSectionDefaultTimeout() {
/*  61 */     return this.criticalSectionDefaultTimeout;
/*     */   }
/*     */   
/*     */   public long getDeCommitFreeBlockThreshold() {
/*  65 */     return this.deCommitFreeBlockThreshold;
/*     */   }
/*     */   
/*     */   public long getDeCommitTotalFreeThreshold() {
/*  69 */     return this.deCommitTotalFreeThreshold;
/*     */   }
/*     */   
/*     */   public long getLockPrefixTable() {
/*  73 */     return this.lockPrefixTable;
/*     */   }
/*     */   
/*     */   public long getMaximumAllocationSize() {
/*  77 */     return this.maximumAllocationSize;
/*     */   }
/*     */   
/*     */   public long getVirtualMemoryThreshold() {
/*  81 */     return this.virtualMemoryThreshold;
/*     */   }
/*     */   
/*     */   public long getProcessAffinityMask() {
/*  85 */     return this.processAffinityMask;
/*     */   }
/*     */   
/*     */   public int getProcessHeapFlags() {
/*  89 */     return this.processHeapFlags;
/*     */   }
/*     */   
/*     */   public int getCsdVersion() {
/*  93 */     return this.csdVersion;
/*     */   }
/*     */   
/*     */   public int getReserved() {
/*  97 */     return this.reserved;
/*     */   }
/*     */   
/*     */   public long getEditList() {
/* 101 */     return this.editList;
/*     */   }
/*     */   
/*     */   public long getSecurityCookie() {
/* 105 */     return this.securityCookie;
/*     */   }
/*     */   
/*     */   public long getSeHandlerTable() {
/* 109 */     return this.seHandlerTable;
/*     */   }
/*     */   
/*     */   public long getSeHandlerCount() {
/* 113 */     return this.seHandlerCount;
/*     */   }
/*     */   
/*     */   public void setSize(int characteristics) {
/* 117 */     this.size = characteristics;
/*     */   }
/*     */   
/*     */   public void setTimeDateStamp(int timeDateStamp) {
/* 121 */     this.timeDateStamp = timeDateStamp;
/*     */   }
/*     */   
/*     */   public void setMajorVersion(int majorVersion) {
/* 125 */     this.majorVersion = majorVersion;
/*     */   }
/*     */   
/*     */   public void setMinorVersion(int minorVersion) {
/* 129 */     this.minorVersion = minorVersion;
/*     */   }
/*     */   
/*     */   public void setGlobalFlagsClear(int globalFlagsClear) {
/* 133 */     this.globalFlagsClear = globalFlagsClear;
/*     */   }
/*     */   
/*     */   public void setGlobalFlagsSet(int globalFlagsSet) {
/* 137 */     this.globalFlagsSet = globalFlagsSet;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setCriticalSectionDefaultTimeout(int criticalSectionDefaultTimeout) {
/* 142 */     this.criticalSectionDefaultTimeout = criticalSectionDefaultTimeout;
/*     */   }
/*     */   
/*     */   public void setDeCommitFreeBlockThreshold(long deCommitFreeBlockThreshold) {
/* 146 */     this.deCommitFreeBlockThreshold = deCommitFreeBlockThreshold;
/*     */   }
/*     */   
/*     */   public void setDeCommitTotalFreeThreshold(long deCommitTotalFreeThreshold) {
/* 150 */     this.deCommitTotalFreeThreshold = deCommitTotalFreeThreshold;
/*     */   }
/*     */   
/*     */   public void setLockPrefixTable(long lockPrefixTable) {
/* 154 */     this.lockPrefixTable = lockPrefixTable;
/*     */   }
/*     */   
/*     */   public void setMaximumAllocationSize(long maximumAllocationSize) {
/* 158 */     this.maximumAllocationSize = maximumAllocationSize;
/*     */   }
/*     */   
/*     */   public void setVirtualMemoryThreshold(long virtualMemoryThreshold) {
/* 162 */     this.virtualMemoryThreshold = virtualMemoryThreshold;
/*     */   }
/*     */   
/*     */   public void setProcessAffinityMask(long processAffinityMask) {
/* 166 */     this.processAffinityMask = processAffinityMask;
/*     */   }
/*     */   
/*     */   public void setProcessHeapFlags(int processHeapFlags) {
/* 170 */     this.processHeapFlags = processHeapFlags;
/*     */   }
/*     */   
/*     */   public void setCsdVersion(int csdVersion) {
/* 174 */     this.csdVersion = csdVersion;
/*     */   }
/*     */   
/*     */   public void setReserved(int reserved) {
/* 178 */     this.reserved = reserved;
/*     */   }
/*     */   
/*     */   public void setEditList(long editList) {
/* 182 */     this.editList = editList;
/*     */   }
/*     */   
/*     */   public void setSecurityCookie(long securityCookie) {
/* 186 */     this.securityCookie = securityCookie;
/*     */   }
/*     */   
/*     */   public void setSeHandlerTable(long seHandlerTable) {
/* 190 */     this.seHandlerTable = seHandlerTable;
/*     */   }
/*     */   
/*     */   public void setSeHandlerCount(long seHandlerCount) {
/* 194 */     this.seHandlerCount = seHandlerCount;
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\kichik\pecoff4j\LoadConfigDirectory.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */