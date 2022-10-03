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
/*     */ 
/*     */ 
/*     */ 
/*     */ public class OptionalHeader
/*     */ {
/*     */   public static final int MAGIC_PE32 = 267;
/*     */   public static final int MAGIC_PE32plus = 523;
/*     */   private int magic;
/*     */   private int majorLinkerVersion;
/*     */   private int minorLinkerVersion;
/*     */   private int sizeOfCode;
/*     */   private int sizeOfInitializedData;
/*     */   private int sizeOfUninitializedData;
/*     */   private int addressOfEntryPoint;
/*     */   private int baseOfCode;
/*     */   private int baseOfData;
/*     */   private long imageBase;
/*     */   private int sectionAlignment;
/*     */   private int fileAlignment;
/*     */   private int majorOperatingSystemVersion;
/*     */   private int minorOperatingSystemVersion;
/*     */   private int majorImageVersion;
/*     */   private int minorImageVersion;
/*     */   private int majorSubsystemVersion;
/*     */   private int minorSubsystemVersion;
/*     */   private int win32VersionValue;
/*     */   private int sizeOfImage;
/*     */   private int sizeOfHeaders;
/*     */   private int checkSum;
/*     */   private int subsystem;
/*     */   private int dllCharacteristics;
/*     */   private long sizeOfStackReserve;
/*     */   private long sizeOfStackCommit;
/*     */   private long sizeOfHeapReserve;
/*     */   private long sizeOfHeapCommit;
/*     */   private int loaderFlags;
/*     */   private int numberOfRvaAndSizes;
/*     */   private ImageDataDirectory[] dataDirectories;
/*     */   
/*     */   public int getMagic() {
/*  52 */     return this.magic;
/*     */   }
/*     */   
/*     */   public boolean isValid() {
/*  56 */     return (this.magic == 267 || this.magic == 523);
/*     */   }
/*     */   
/*     */   public boolean isPE32plus() {
/*  60 */     return (this.magic == 523);
/*     */   }
/*     */   
/*     */   public int getMajorLinkerVersion() {
/*  64 */     return this.majorLinkerVersion;
/*     */   }
/*     */   
/*     */   public int getMinorLinkerVersion() {
/*  68 */     return this.minorLinkerVersion;
/*     */   }
/*     */   
/*     */   public int getSizeOfCode() {
/*  72 */     return this.sizeOfCode;
/*     */   }
/*     */   
/*     */   public int getSizeOfInitializedData() {
/*  76 */     return this.sizeOfInitializedData;
/*     */   }
/*     */   
/*     */   public int getSizeOfUninitializedData() {
/*  80 */     return this.sizeOfUninitializedData;
/*     */   }
/*     */   
/*     */   public int getAddressOfEntryPoint() {
/*  84 */     return this.addressOfEntryPoint;
/*     */   }
/*     */   
/*     */   public int getBaseOfCode() {
/*  88 */     return this.baseOfCode;
/*     */   }
/*     */   
/*     */   public int getBaseOfData() {
/*  92 */     return this.baseOfData;
/*     */   }
/*     */   
/*     */   public long getImageBase() {
/*  96 */     return this.imageBase;
/*     */   }
/*     */   
/*     */   public int getSectionAlignment() {
/* 100 */     return this.sectionAlignment;
/*     */   }
/*     */   
/*     */   public int getFileAlignment() {
/* 104 */     return this.fileAlignment;
/*     */   }
/*     */   
/*     */   public int getMajorOperatingSystemVersion() {
/* 108 */     return this.majorOperatingSystemVersion;
/*     */   }
/*     */   
/*     */   public int getMinorOperatingSystemVersion() {
/* 112 */     return this.minorOperatingSystemVersion;
/*     */   }
/*     */   
/*     */   public int getMajorImageVersion() {
/* 116 */     return this.majorImageVersion;
/*     */   }
/*     */   
/*     */   public int getMinorImageVersion() {
/* 120 */     return this.minorImageVersion;
/*     */   }
/*     */   
/*     */   public int getMajorSubsystemVersion() {
/* 124 */     return this.majorSubsystemVersion;
/*     */   }
/*     */   
/*     */   public int getMinorSubsystemVersion() {
/* 128 */     return this.minorSubsystemVersion;
/*     */   }
/*     */   
/*     */   public int getWin32VersionValue() {
/* 132 */     return this.win32VersionValue;
/*     */   }
/*     */   
/*     */   public int getSizeOfImage() {
/* 136 */     return this.sizeOfImage;
/*     */   }
/*     */   
/*     */   public int getSizeOfHeaders() {
/* 140 */     return this.sizeOfHeaders;
/*     */   }
/*     */   
/*     */   public int getCheckSum() {
/* 144 */     return this.checkSum;
/*     */   }
/*     */   
/*     */   public int getSubsystem() {
/* 148 */     return this.subsystem;
/*     */   }
/*     */   
/*     */   public int getDllCharacteristics() {
/* 152 */     return this.dllCharacteristics;
/*     */   }
/*     */   
/*     */   public long getSizeOfStackReserve() {
/* 156 */     return this.sizeOfStackReserve;
/*     */   }
/*     */   
/*     */   public long getSizeOfStackCommit() {
/* 160 */     return this.sizeOfStackCommit;
/*     */   }
/*     */   
/*     */   public long getSizeOfHeapReserve() {
/* 164 */     return this.sizeOfHeapReserve;
/*     */   }
/*     */   
/*     */   public long getSizeOfHeapCommit() {
/* 168 */     return this.sizeOfHeapCommit;
/*     */   }
/*     */   
/*     */   public int getLoaderFlags() {
/* 172 */     return this.loaderFlags;
/*     */   }
/*     */   
/*     */   public int getNumberOfRvaAndSizes() {
/* 176 */     return this.numberOfRvaAndSizes;
/*     */   }
/*     */   
/*     */   public void setMagic(int magic) {
/* 180 */     this.magic = magic;
/*     */   }
/*     */   
/*     */   public void setMajorLinkerVersion(int majorLinkerVersion) {
/* 184 */     this.majorLinkerVersion = majorLinkerVersion;
/*     */   }
/*     */   
/*     */   public void setMinorLinkerVersion(int minorLinkerVersion) {
/* 188 */     this.minorLinkerVersion = minorLinkerVersion;
/*     */   }
/*     */   
/*     */   public void setSizeOfCode(int sizeOfCode) {
/* 192 */     this.sizeOfCode = sizeOfCode;
/*     */   }
/*     */   
/*     */   public void setSizeOfInitializedData(int sizeOfInitializedData) {
/* 196 */     this.sizeOfInitializedData = sizeOfInitializedData;
/*     */   }
/*     */   
/*     */   public void setSizeOfUninitializedData(int sizeOfUninitializedData) {
/* 200 */     this.sizeOfUninitializedData = sizeOfUninitializedData;
/*     */   }
/*     */   
/*     */   public void setAddressOfEntryPoint(int addressOfEntryPoint) {
/* 204 */     this.addressOfEntryPoint = addressOfEntryPoint;
/*     */   }
/*     */   
/*     */   public void setBaseOfCode(int baseOfCode) {
/* 208 */     this.baseOfCode = baseOfCode;
/*     */   }
/*     */   
/*     */   public void setBaseOfData(int baseOfData) {
/* 212 */     this.baseOfData = baseOfData;
/*     */   }
/*     */   
/*     */   public void setImageBase(long imageBase) {
/* 216 */     this.imageBase = imageBase;
/*     */   }
/*     */   
/*     */   public void setSectionAlignment(int sectionAlignment) {
/* 220 */     this.sectionAlignment = sectionAlignment;
/*     */   }
/*     */   
/*     */   public void setFileAlignment(int fileAlignment) {
/* 224 */     this.fileAlignment = fileAlignment;
/*     */   }
/*     */   
/*     */   public void setMajorOperatingSystemVersion(int majorOperatingSystemVersion) {
/* 228 */     this.majorOperatingSystemVersion = majorOperatingSystemVersion;
/*     */   }
/*     */   
/*     */   public void setMinorOperatingSystemVersion(int minorOperatingSystemVersion) {
/* 232 */     this.minorOperatingSystemVersion = minorOperatingSystemVersion;
/*     */   }
/*     */   
/*     */   public void setMajorImageVersion(int majorImageVersion) {
/* 236 */     this.majorImageVersion = majorImageVersion;
/*     */   }
/*     */   
/*     */   public void setMinorImageVersion(int minorImageVersion) {
/* 240 */     this.minorImageVersion = minorImageVersion;
/*     */   }
/*     */   
/*     */   public void setMajorSubsystemVersion(int majorSubsystemVersion) {
/* 244 */     this.majorSubsystemVersion = majorSubsystemVersion;
/*     */   }
/*     */   
/*     */   public void setMinorSubsystemVersion(int minorSubsystemVersion) {
/* 248 */     this.minorSubsystemVersion = minorSubsystemVersion;
/*     */   }
/*     */   
/*     */   public void setWin32VersionValue(int win32VersionValue) {
/* 252 */     this.win32VersionValue = win32VersionValue;
/*     */   }
/*     */   
/*     */   public void setSizeOfImage(int sizeOfImage) {
/* 256 */     this.sizeOfImage = sizeOfImage;
/*     */   }
/*     */   
/*     */   public void setSizeOfHeaders(int sizeOfHeaders) {
/* 260 */     this.sizeOfHeaders = sizeOfHeaders;
/*     */   }
/*     */   
/*     */   public void setCheckSum(int checkSum) {
/* 264 */     this.checkSum = checkSum;
/*     */   }
/*     */   
/*     */   public void setSubsystem(int subsystem) {
/* 268 */     this.subsystem = subsystem;
/*     */   }
/*     */   
/*     */   public void setDllCharacteristics(int dllCharacteristics) {
/* 272 */     this.dllCharacteristics = dllCharacteristics;
/*     */   }
/*     */   
/*     */   public void setSizeOfStackReserve(long sizeOfStackReserve) {
/* 276 */     this.sizeOfStackReserve = sizeOfStackReserve;
/*     */   }
/*     */   
/*     */   public void setSizeOfStackCommit(long sizeOfStackCommit) {
/* 280 */     this.sizeOfStackCommit = sizeOfStackCommit;
/*     */   }
/*     */   
/*     */   public void setSizeOfHeapReserve(long sizeOfHeapReserve) {
/* 284 */     this.sizeOfHeapReserve = sizeOfHeapReserve;
/*     */   }
/*     */   
/*     */   public void setSizeOfHeapCommit(long sizeOfHeapCommit) {
/* 288 */     this.sizeOfHeapCommit = sizeOfHeapCommit;
/*     */   }
/*     */   
/*     */   public void setLoaderFlags(int loaderFlags) {
/* 292 */     this.loaderFlags = loaderFlags;
/*     */   }
/*     */   
/*     */   public void setNumberOfRvaAndSizes(int numberOfRvaAndSizes) {
/* 296 */     this.numberOfRvaAndSizes = numberOfRvaAndSizes;
/*     */   }
/*     */   
/*     */   public int getDataDirectoryCount() {
/* 300 */     return this.dataDirectories.length;
/*     */   }
/*     */   
/*     */   public ImageDataDirectory[] getDataDirectories() {
/* 304 */     return this.dataDirectories;
/*     */   }
/*     */   
/*     */   public ImageDataDirectory getDataDirectory(int index) {
/* 308 */     return this.dataDirectories[index];
/*     */   }
/*     */   
/*     */   public void setDataDirectories(ImageDataDirectory[] dataDirectories) {
/* 312 */     this.dataDirectories = dataDirectories;
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\kichik\pecoff4j\OptionalHeader.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */