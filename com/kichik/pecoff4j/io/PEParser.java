/*     */ package com.kichik.pecoff4j.io;
/*     */ 
/*     */ import com.kichik.pecoff4j.AttributeCertificateTable;
/*     */ import com.kichik.pecoff4j.BoundImport;
/*     */ import com.kichik.pecoff4j.BoundImportDirectoryTable;
/*     */ import com.kichik.pecoff4j.COFFHeader;
/*     */ import com.kichik.pecoff4j.DOSHeader;
/*     */ import com.kichik.pecoff4j.DOSStub;
/*     */ import com.kichik.pecoff4j.DebugDirectory;
/*     */ import com.kichik.pecoff4j.ExportDirectory;
/*     */ import com.kichik.pecoff4j.ImageData;
/*     */ import com.kichik.pecoff4j.ImageDataDirectory;
/*     */ import com.kichik.pecoff4j.ImportDirectory;
/*     */ import com.kichik.pecoff4j.ImportDirectoryEntry;
/*     */ import com.kichik.pecoff4j.ImportDirectoryTable;
/*     */ import com.kichik.pecoff4j.ImportEntry;
/*     */ import com.kichik.pecoff4j.LoadConfigDirectory;
/*     */ import com.kichik.pecoff4j.OptionalHeader;
/*     */ import com.kichik.pecoff4j.PE;
/*     */ import com.kichik.pecoff4j.PESignature;
/*     */ import com.kichik.pecoff4j.RVAConverter;
/*     */ import com.kichik.pecoff4j.ResourceDirectory;
/*     */ import com.kichik.pecoff4j.ResourceDirectoryTable;
/*     */ import com.kichik.pecoff4j.ResourceEntry;
/*     */ import com.kichik.pecoff4j.SectionData;
/*     */ import com.kichik.pecoff4j.SectionHeader;
/*     */ import com.kichik.pecoff4j.SectionTable;
/*     */ import com.kichik.pecoff4j.util.IntMap;
/*     */ import java.io.File;
/*     */ import java.io.FileInputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collections;
/*     */ import java.util.Comparator;
/*     */ import java.util.List;
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
/*     */ public class PEParser
/*     */ {
/*     */   public static PE parse(InputStream is) throws IOException {
/*  51 */     DataReader dr = new DataReader(is); Throwable throwable = null; 
/*  52 */     try { return read(dr); } catch (Throwable throwable1) { throwable = throwable1 = null; throw throwable1; }
/*  53 */     finally { $closeResource(throwable, dr); }
/*     */   
/*     */   }
/*     */   public static PE parse(String filename) throws IOException {
/*  57 */     return parse(new File(filename));
/*     */   }
/*     */   
/*     */   public static PE parse(File file) throws IOException {
/*  61 */     FileInputStream is = new FileInputStream(file); Throwable throwable = null; 
/*  62 */     try { DataReader dr = new DataReader(is); Throwable throwable1 = null; }
/*     */     catch (Throwable throwable1) { throwable = throwable1 = null; throw throwable1; }
/*  64 */     finally { $closeResource(throwable, is); }
/*     */   
/*     */   }
/*     */   public static PE read(IDataReader dr) throws IOException {
/*  68 */     PE pe = new PE();
/*  69 */     pe.setDosHeader(readDos(dr));
/*     */ 
/*     */     
/*  72 */     if (pe.getDosHeader().getAddressOfNewExeHeader() == 0 || pe
/*  73 */       .getDosHeader().getAddressOfNewExeHeader() > 8192) {
/*  74 */       return pe;
/*     */     }
/*     */     
/*  77 */     pe.setStub(readStub(pe.getDosHeader(), dr));
/*  78 */     pe.setSignature(readSignature(dr));
/*     */ 
/*     */     
/*  81 */     if (!pe.getSignature().isValid()) {
/*  82 */       return pe;
/*     */     }
/*     */     
/*  85 */     pe.setCoffHeader(readCOFF(dr));
/*  86 */     pe.setOptionalHeader(readOptional(dr));
/*  87 */     pe.setSectionTable(readSectionHeaders(pe, dr));
/*     */     
/*  89 */     pe.set64(pe.getOptionalHeader().isPE32plus());
/*     */ 
/*     */     
/*  92 */     DataEntry entry = null;
/*  93 */     while ((entry = findNextEntry(pe, dr.getPosition())) != null) {
/*  94 */       if (entry.isSection) {
/*  95 */         readSection(pe, entry, dr); continue;
/*  96 */       }  if (entry.isDebugRawData) {
/*  97 */         readDebugRawData(pe, entry, dr); continue;
/*     */       } 
/*  99 */       readImageData(pe, entry, dr);
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/* 104 */     byte[] tb = dr.readAll();
/* 105 */     if (tb.length > 0) {
/* 106 */       pe.getImageData().setTrailingData(tb);
/*     */     }
/*     */     
/* 109 */     return pe;
/*     */   }
/*     */   
/*     */   public static DOSHeader readDos(IDataReader dr) throws IOException {
/* 113 */     DOSHeader dh = new DOSHeader();
/* 114 */     dh.setMagic(dr.readWord());
/* 115 */     dh.setUsedBytesInLastPage(dr.readWord());
/* 116 */     dh.setFileSizeInPages(dr.readWord());
/* 117 */     dh.setNumRelocationItems(dr.readWord());
/* 118 */     dh.setHeaderSizeInParagraphs(dr.readWord());
/* 119 */     dh.setMinExtraParagraphs(dr.readWord());
/* 120 */     dh.setMaxExtraParagraphs(dr.readWord());
/* 121 */     dh.setInitialSS(dr.readWord());
/* 122 */     dh.setInitialSP(dr.readWord());
/* 123 */     dh.setChecksum(dr.readWord());
/* 124 */     dh.setInitialIP(dr.readWord());
/* 125 */     dh.setInitialRelativeCS(dr.readWord());
/* 126 */     dh.setAddressOfRelocationTable(dr.readWord());
/* 127 */     dh.setOverlayNumber(dr.readWord());
/* 128 */     int[] reserved = new int[4];
/* 129 */     for (int i = 0; i < reserved.length; i++) {
/* 130 */       reserved[i] = dr.readWord();
/*     */     }
/* 132 */     dh.setReserved(reserved);
/* 133 */     dh.setOemId(dr.readWord());
/* 134 */     dh.setOemInfo(dr.readWord());
/* 135 */     int[] reserved2 = new int[10];
/* 136 */     for (int j = 0; j < reserved2.length; j++) {
/* 137 */       reserved2[j] = dr.readWord();
/*     */     }
/* 139 */     dh.setReserved2(reserved2);
/* 140 */     dh.setAddressOfNewExeHeader(dr.readDoubleWord());
/*     */ 
/*     */ 
/*     */     
/* 144 */     int stubSize = dh.getFileSizeInPages() * 512 - 512 - dh.getUsedBytesInLastPage();
/* 145 */     if (stubSize > dh.getAddressOfNewExeHeader())
/* 146 */       stubSize = dh.getAddressOfNewExeHeader(); 
/* 147 */     stubSize -= dh.getHeaderSizeInParagraphs() * 16;
/* 148 */     dh.setStubSize(stubSize);
/*     */     
/* 150 */     return dh;
/*     */   }
/*     */ 
/*     */   
/*     */   public static DOSStub readStub(DOSHeader header, IDataReader dr) throws IOException {
/* 155 */     DOSStub ds = new DOSStub();
/* 156 */     int pos = dr.getPosition();
/* 157 */     int add = header.getAddressOfNewExeHeader();
/* 158 */     byte[] stub = new byte[add - pos];
/* 159 */     dr.read(stub);
/* 160 */     ds.setStub(stub);
/* 161 */     return ds;
/*     */   }
/*     */   
/*     */   public static PESignature readSignature(IDataReader dr) throws IOException {
/* 165 */     PESignature ps = new PESignature();
/* 166 */     byte[] signature = new byte[4];
/* 167 */     dr.read(signature);
/* 168 */     ps.setSignature(signature);
/* 169 */     return ps;
/*     */   }
/*     */   
/*     */   public static COFFHeader readCOFF(IDataReader dr) throws IOException {
/* 173 */     COFFHeader h = new COFFHeader();
/* 174 */     h.setMachine(dr.readWord());
/* 175 */     h.setNumberOfSections(dr.readWord());
/* 176 */     h.setTimeDateStamp(dr.readDoubleWord());
/* 177 */     h.setPointerToSymbolTable(dr.readDoubleWord());
/* 178 */     h.setNumberOfSymbols(dr.readDoubleWord());
/* 179 */     h.setSizeOfOptionalHeader(dr.readWord());
/* 180 */     h.setCharacteristics(dr.readWord());
/* 181 */     return h;
/*     */   }
/*     */ 
/*     */   
/*     */   public static OptionalHeader readOptional(IDataReader dr) throws IOException {
/* 186 */     OptionalHeader oh = new OptionalHeader();
/* 187 */     oh.setMagic(dr.readWord());
/* 188 */     boolean is64 = oh.isPE32plus();
/* 189 */     oh.setMajorLinkerVersion(dr.readByte());
/* 190 */     oh.setMinorLinkerVersion(dr.readByte());
/* 191 */     oh.setSizeOfCode(dr.readDoubleWord());
/* 192 */     oh.setSizeOfInitializedData(dr.readDoubleWord());
/* 193 */     oh.setSizeOfUninitializedData(dr.readDoubleWord());
/* 194 */     oh.setAddressOfEntryPoint(dr.readDoubleWord());
/* 195 */     oh.setBaseOfCode(dr.readDoubleWord());
/*     */     
/* 197 */     if (!is64) {
/* 198 */       oh.setBaseOfData(dr.readDoubleWord());
/*     */     }
/*     */     
/* 201 */     oh.setImageBase(is64 ? dr.readLong() : dr.readDoubleWord());
/* 202 */     oh.setSectionAlignment(dr.readDoubleWord());
/* 203 */     oh.setFileAlignment(dr.readDoubleWord());
/* 204 */     oh.setMajorOperatingSystemVersion(dr.readWord());
/* 205 */     oh.setMinorOperatingSystemVersion(dr.readWord());
/* 206 */     oh.setMajorImageVersion(dr.readWord());
/* 207 */     oh.setMinorImageVersion(dr.readWord());
/* 208 */     oh.setMajorSubsystemVersion(dr.readWord());
/* 209 */     oh.setMinorSubsystemVersion(dr.readWord());
/* 210 */     oh.setWin32VersionValue(dr.readDoubleWord());
/* 211 */     oh.setSizeOfImage(dr.readDoubleWord());
/* 212 */     oh.setSizeOfHeaders(dr.readDoubleWord());
/* 213 */     oh.setCheckSum(dr.readDoubleWord());
/* 214 */     oh.setSubsystem(dr.readWord());
/* 215 */     oh.setDllCharacteristics(dr.readWord());
/* 216 */     oh.setSizeOfStackReserve(is64 ? dr.readLong() : dr.readDoubleWord());
/* 217 */     oh.setSizeOfStackCommit(is64 ? dr.readLong() : dr.readDoubleWord());
/* 218 */     oh.setSizeOfHeapReserve(is64 ? dr.readLong() : dr.readDoubleWord());
/* 219 */     oh.setSizeOfHeapCommit(is64 ? dr.readLong() : dr.readDoubleWord());
/* 220 */     oh.setLoaderFlags(dr.readDoubleWord());
/* 221 */     oh.setNumberOfRvaAndSizes(dr.readDoubleWord());
/*     */ 
/*     */     
/* 224 */     ImageDataDirectory[] dds = new ImageDataDirectory[16];
/* 225 */     for (int i = 0; i < dds.length; i++) {
/* 226 */       dds[i] = readImageDD(dr);
/*     */     }
/* 228 */     oh.setDataDirectories(dds);
/*     */     
/* 230 */     return oh;
/*     */   }
/*     */ 
/*     */   
/*     */   public static ImageDataDirectory readImageDD(IDataReader dr) throws IOException {
/* 235 */     ImageDataDirectory idd = new ImageDataDirectory();
/* 236 */     idd.setVirtualAddress(dr.readDoubleWord());
/* 237 */     idd.setSize(dr.readDoubleWord());
/* 238 */     return idd;
/*     */   }
/*     */ 
/*     */   
/*     */   public static SectionTable readSectionHeaders(PE pe, IDataReader dr) throws IOException {
/* 243 */     SectionTable st = new SectionTable();
/* 244 */     int ns = pe.getCoffHeader().getNumberOfSections();
/* 245 */     for (int i = 0; i < ns; i++) {
/* 246 */       st.add(readSectionHeader(dr));
/*     */     }
/*     */     
/* 249 */     SectionHeader[] sorted = st.getHeadersPointerSorted();
/* 250 */     int[] virtualAddress = new int[sorted.length];
/* 251 */     int[] pointerToRawData = new int[sorted.length];
/* 252 */     for (int j = 0; j < sorted.length; j++) {
/* 253 */       virtualAddress[j] = sorted[j].getVirtualAddress();
/* 254 */       pointerToRawData[j] = sorted[j].getPointerToRawData();
/*     */     } 
/*     */     
/* 257 */     st.setRvaConverter(new RVAConverter(virtualAddress, pointerToRawData));
/* 258 */     return st;
/*     */   }
/*     */ 
/*     */   
/*     */   public static SectionHeader readSectionHeader(IDataReader dr) throws IOException {
/* 263 */     SectionHeader sh = new SectionHeader();
/* 264 */     sh.setName(dr.readUtf(8));
/* 265 */     sh.setVirtualSize(dr.readDoubleWord());
/* 266 */     sh.setVirtualAddress(dr.readDoubleWord());
/* 267 */     sh.setSizeOfRawData(dr.readDoubleWord());
/* 268 */     sh.setPointerToRawData(dr.readDoubleWord());
/* 269 */     sh.setPointerToRelocations(dr.readDoubleWord());
/* 270 */     sh.setPointerToLineNumbers(dr.readDoubleWord());
/* 271 */     sh.setNumberOfRelocations(dr.readWord());
/* 272 */     sh.setNumberOfLineNumbers(dr.readWord());
/* 273 */     sh.setCharacteristics(dr.readDoubleWord());
/* 274 */     return sh;
/*     */   }
/*     */   
/*     */   public static DataEntry findNextEntry(PE pe, int pos) {
/* 278 */     DataEntry de = new DataEntry();
/*     */ 
/*     */     
/* 281 */     int ns = pe.getCoffHeader().getNumberOfSections();
/* 282 */     for (int i = 0; i < ns; i++) {
/* 283 */       SectionHeader sh = pe.getSectionTable().getHeader(i);
/* 284 */       if (sh.getSizeOfRawData() > 0 && sh
/* 285 */         .getPointerToRawData() >= pos && (de.pointer == 0 || sh
/* 286 */         .getPointerToRawData() < de.pointer)) {
/* 287 */         de.pointer = sh.getPointerToRawData();
/* 288 */         de.index = i;
/* 289 */         de.isSection = true;
/*     */       } 
/*     */     } 
/*     */ 
/*     */     
/* 294 */     RVAConverter rvc = pe.getSectionTable().getRVAConverter();
/* 295 */     int dc = pe.getOptionalHeader().getDataDirectoryCount();
/* 296 */     for (int j = 0; j < dc; j++) {
/* 297 */       ImageDataDirectory idd = pe.getOptionalHeader().getDataDirectory(j);
/* 298 */       if (idd.getSize() > 0) {
/* 299 */         int prd = idd.getVirtualAddress();
/*     */         
/* 301 */         if (j != 4 && 
/* 302 */           isInsideSection(pe, idd)) {
/* 303 */           prd = rvc.convertVirtualAddressToRawDataPointer(idd
/* 304 */               .getVirtualAddress());
/*     */         }
/* 306 */         if (prd >= pos && (de.pointer == 0 || prd < de.pointer)) {
/* 307 */           de.pointer = prd;
/* 308 */           de.index = j;
/* 309 */           de.isSection = false;
/*     */         } 
/*     */       } 
/*     */     } 
/*     */ 
/*     */     
/* 315 */     ImageData id = pe.getImageData();
/* 316 */     DebugDirectory dd = null;
/* 317 */     if (id != null)
/* 318 */       dd = id.getDebug(); 
/* 319 */     if (dd != null) {
/* 320 */       int prd = dd.getPointerToRawData();
/* 321 */       if (prd >= pos && (de.pointer == 0 || prd < de.pointer)) {
/* 322 */         de.pointer = prd;
/* 323 */         de.index = -1;
/* 324 */         de.isDebugRawData = true;
/* 325 */         de.isSection = false;
/* 326 */         de.baseAddress = prd;
/*     */       } 
/*     */     } 
/*     */     
/* 330 */     if (de.pointer == 0) {
/* 331 */       return null;
/*     */     }
/* 333 */     return de;
/*     */   }
/*     */   
/*     */   private static boolean isInsideSection(PE pe, ImageDataDirectory idd) {
/* 337 */     int prd = idd.getVirtualAddress();
/* 338 */     int pex = prd + idd.getSize();
/* 339 */     SectionTable st = pe.getSectionTable();
/* 340 */     int ns = st.getNumberOfSections();
/* 341 */     for (int i = 0; i < ns; i++) {
/* 342 */       SectionHeader sh = st.getHeader(i);
/* 343 */       int vad = sh.getVirtualAddress();
/* 344 */       int vex = vad + sh.getVirtualSize();
/* 345 */       if (prd >= vad && prd < vex && pex <= vex)
/* 346 */         return true; 
/*     */     } 
/* 348 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static void readImageData(PE pe, DataEntry entry, IDataReader dr) throws IOException {
/* 355 */     ImageData id = pe.getImageData();
/* 356 */     byte[] pa = readPreambleData(entry.pointer, dr);
/* 357 */     if (pa != null) {
/* 358 */       id.put(entry.index, pa);
/*     */     }
/*     */     
/* 361 */     ImageDataDirectory idd = pe.getOptionalHeader().getDataDirectory(entry.index);
/*     */     
/* 363 */     byte[] b = new byte[idd.getSize()];
/* 364 */     dr.read(b);
/*     */     
/* 366 */     switch (entry.index) {
/*     */       case 0:
/* 368 */         id.setExportTable(readExportDirectory(b));
/*     */         break;
/*     */       case 1:
/* 371 */         id.setImportTable(readImportDirectory(b, entry.baseAddress));
/*     */         break;
/*     */       case 2:
/* 374 */         id.setResourceTable(readResourceDirectory(b, entry.baseAddress));
/*     */         break;
/*     */       case 3:
/* 377 */         id.setExceptionTable(b);
/*     */         break;
/*     */       case 4:
/* 380 */         id.setCertificateTable(readAttributeCertificateTable(b));
/*     */         break;
/*     */       case 5:
/* 383 */         id.setBaseRelocationTable(b);
/*     */         break;
/*     */       case 6:
/* 386 */         id.setDebug(readDebugDirectory(b));
/*     */         break;
/*     */       case 7:
/* 389 */         id.setArchitecture(b);
/*     */         break;
/*     */       case 8:
/* 392 */         id.setGlobalPtr(b);
/*     */         break;
/*     */       case 9:
/* 395 */         id.setTlsTable(b);
/*     */         break;
/*     */       case 10:
/* 398 */         id.setLoadConfigTable(readLoadConfigDirectory(pe, b));
/*     */         break;
/*     */       case 11:
/* 401 */         id.setBoundImports(readBoundImportDirectoryTable(b));
/*     */         break;
/*     */       case 12:
/* 404 */         id.setIat(b);
/*     */         break;
/*     */       case 13:
/* 407 */         id.setDelayImportDescriptor(b);
/*     */         break;
/*     */       case 14:
/* 410 */         id.setClrRuntimeHeader(b);
/*     */         break;
/*     */       case 15:
/* 413 */         id.setReserved(b);
/*     */         break;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private static byte[] readPreambleData(int pointer, IDataReader dr) throws IOException {
/* 420 */     if (pointer > dr.getPosition()) {
/* 421 */       byte[] pa = new byte[pointer - dr.getPosition()];
/* 422 */       dr.read(pa);
/* 423 */       boolean zeroes = true;
/* 424 */       for (int i = 0; i < pa.length; i++) {
/* 425 */         if (pa[i] != 0) {
/* 426 */           zeroes = false;
/*     */           break;
/*     */         } 
/*     */       } 
/* 430 */       if (!zeroes) {
/* 431 */         return pa;
/*     */       }
/*     */     } 
/* 434 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static void readDebugRawData(PE pe, DataEntry entry, IDataReader dr) throws IOException {
/* 440 */     ImageData id = pe.getImageData();
/* 441 */     byte[] pa = readPreambleData(entry.pointer, dr);
/* 442 */     if (pa != null)
/* 443 */       id.setDebugRawDataPreamble(pa); 
/* 444 */     DebugDirectory dd = id.getDebug();
/* 445 */     byte[] b = new byte[dd.getSizeOfData()];
/* 446 */     dr.read(b);
/* 447 */     id.setDebugRawData(b);
/*     */   }
/*     */ 
/*     */   
/*     */   private static void readSection(PE pe, DataEntry entry, IDataReader dr) throws IOException {
/* 452 */     SectionTable st = pe.getSectionTable();
/* 453 */     SectionHeader sh = st.getHeader(entry.index);
/* 454 */     SectionData sd = new SectionData();
/*     */ 
/*     */     
/* 457 */     byte[] pa = readPreambleData(sh.getPointerToRawData(), dr);
/* 458 */     if (pa != null) {
/* 459 */       sd.setPreamble(pa);
/*     */     }
/*     */     
/* 462 */     dr.jumpTo(sh.getPointerToRawData());
/* 463 */     byte[] b = new byte[sh.getSizeOfRawData()];
/* 464 */     dr.read(b);
/* 465 */     sd.setData(b);
/* 466 */     st.put(entry.index, sd);
/*     */ 
/*     */     
/* 469 */     int ddc = pe.getOptionalHeader().getDataDirectoryCount();
/* 470 */     for (int i = 0; i < ddc; i++) {
/* 471 */       if (i != 4) {
/*     */         
/* 473 */         ImageDataDirectory idd = pe.getOptionalHeader().getDataDirectory(i);
/* 474 */         if (idd.getSize() > 0) {
/* 475 */           int vad = sh.getVirtualAddress();
/* 476 */           int vex = vad + sh.getVirtualSize();
/* 477 */           int dad = idd.getVirtualAddress();
/* 478 */           if (dad >= vad && dad < vex) {
/* 479 */             int off = dad - vad;
/*     */             
/* 481 */             IDataReader idr = new ByteArrayDataReader(b, off, idd.getSize());
/* 482 */             DataEntry de = new DataEntry(i, 0);
/* 483 */             de.baseAddress = sh.getVirtualAddress();
/* 484 */             readImageData(pe, de, idr);
/*     */           } 
/*     */         } 
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   private static BoundImportDirectoryTable readBoundImportDirectoryTable(byte[] b) throws IOException {
/* 492 */     DataReader dr = new DataReader(b);
/* 493 */     BoundImportDirectoryTable bidt = new BoundImportDirectoryTable();
/* 494 */     List<BoundImport> imports = new ArrayList<>();
/* 495 */     BoundImport bi = null;
/* 496 */     while ((bi = readBoundImport(dr)) != null) {
/* 497 */       bidt.add(bi);
/* 498 */       imports.add(bi);
/*     */     } 
/* 500 */     Collections.sort(imports, new Comparator<BoundImport>()
/*     */         {
/*     */           public int compare(BoundImport o1, BoundImport o2) {
/* 503 */             return o1.getOffsetToModuleName() - o2.getOffsetToModuleName();
/*     */           }
/*     */         });
/* 506 */     IntMap names = new IntMap();
/* 507 */     for (int i = 0; i < imports.size(); i++) {
/* 508 */       bi = imports.get(i);
/* 509 */       int offset = bi.getOffsetToModuleName();
/* 510 */       String n = (String)names.get(offset);
/* 511 */       if (n == null) {
/* 512 */         dr.jumpTo(offset);
/* 513 */         n = dr.readUtf();
/* 514 */         names.put(offset, n);
/*     */       } 
/* 516 */       bi.setModuleName(n);
/*     */     } 
/* 518 */     return bidt;
/*     */   }
/*     */ 
/*     */   
/*     */   private static BoundImport readBoundImport(IDataReader dr) throws IOException {
/* 523 */     BoundImport bi = new BoundImport();
/* 524 */     bi.setTimestamp(dr.readDoubleWord());
/* 525 */     bi.setOffsetToModuleName(dr.readWord());
/* 526 */     bi.setNumberOfModuleForwarderRefs(dr.readWord());
/*     */     
/* 528 */     if (bi.getTimestamp() == 0L && bi.getOffsetToModuleName() == 0 && bi
/* 529 */       .getNumberOfModuleForwarderRefs() == 0) {
/* 530 */       return null;
/*     */     }
/* 532 */     return bi;
/*     */   }
/*     */ 
/*     */   
/*     */   public static ImportDirectory readImportDirectory(byte[] b, int baseAddress) throws IOException {
/* 537 */     DataReader dr = new DataReader(b);
/* 538 */     ImportDirectory id = new ImportDirectory();
/* 539 */     ImportDirectoryEntry ide = null;
/* 540 */     while ((ide = readImportDirectoryEntry(dr)) != null) {
/* 541 */       id.add(ide);
/*     */     }
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
/* 555 */     return id;
/*     */   }
/*     */ 
/*     */   
/*     */   public static ImportDirectoryEntry readImportDirectoryEntry(IDataReader dr) throws IOException {
/* 560 */     ImportDirectoryEntry id = new ImportDirectoryEntry();
/* 561 */     id.setImportLookupTableRVA(dr.readDoubleWord());
/* 562 */     id.setTimeDateStamp(dr.readDoubleWord());
/* 563 */     id.setForwarderChain(dr.readDoubleWord());
/* 564 */     id.setNameRVA(dr.readDoubleWord());
/* 565 */     id.setImportAddressTableRVA(dr.readDoubleWord());
/*     */ 
/*     */     
/* 568 */     if (id.getImportLookupTableRVA() == 0) {
/* 569 */       return null;
/*     */     }
/*     */     
/* 572 */     return id;
/*     */   }
/*     */ 
/*     */   
/*     */   public static ImportDirectoryTable readImportDirectoryTable(IDataReader dr, int baseAddress) throws IOException {
/* 577 */     ImportDirectoryTable idt = new ImportDirectoryTable();
/* 578 */     ImportEntry ie = null;
/* 579 */     while ((ie = readImportEntry(dr)) != null) {
/* 580 */       idt.add(ie);
/*     */     }
/*     */     
/* 583 */     for (int i = 0; i < idt.size(); i++) {
/* 584 */       ImportEntry iee = idt.getEntry(i);
/* 585 */       if ((iee.getVal() & Integer.MIN_VALUE) != 0) {
/* 586 */         iee.setOrdinal(iee.getVal() & Integer.MAX_VALUE);
/*     */       } else {
/* 588 */         dr.jumpTo(iee.getVal() - baseAddress);
/* 589 */         dr.readWord();
/* 590 */         iee.setName(dr.readUtf());
/*     */       } 
/*     */     } 
/* 593 */     return idt;
/*     */   }
/*     */ 
/*     */   
/*     */   public static ImportEntry readImportEntry(IDataReader dr) throws IOException {
/* 598 */     ImportEntry ie = new ImportEntry();
/* 599 */     ie.setVal(dr.readDoubleWord());
/* 600 */     if (ie.getVal() == 0) {
/* 601 */       return null;
/*     */     }
/*     */     
/* 604 */     return ie;
/*     */   }
/*     */ 
/*     */   
/*     */   public static ExportDirectory readExportDirectory(byte[] b) throws IOException {
/* 609 */     DataReader dr = new DataReader(b);
/* 610 */     ExportDirectory edt = new ExportDirectory();
/* 611 */     edt.set(b);
/* 612 */     edt.setExportFlags(dr.readDoubleWord());
/* 613 */     edt.setTimeDateStamp(dr.readDoubleWord());
/* 614 */     edt.setMajorVersion(dr.readWord());
/* 615 */     edt.setMinorVersion(dr.readWord());
/* 616 */     edt.setNameRVA(dr.readDoubleWord());
/* 617 */     edt.setOrdinalBase(dr.readDoubleWord());
/* 618 */     edt.setAddressTableEntries(dr.readDoubleWord());
/* 619 */     edt.setNumberOfNamePointers(dr.readDoubleWord());
/* 620 */     edt.setExportAddressTableRVA(dr.readDoubleWord());
/* 621 */     edt.setNamePointerRVA(dr.readDoubleWord());
/* 622 */     edt.setOrdinalTableRVA(dr.readDoubleWord());
/* 623 */     return edt;
/*     */   }
/*     */ 
/*     */   
/*     */   public static LoadConfigDirectory readLoadConfigDirectory(PE pe, byte[] b) throws IOException {
/* 628 */     DataReader dr = new DataReader(b);
/* 629 */     LoadConfigDirectory lcd = new LoadConfigDirectory();
/* 630 */     lcd.set(b);
/* 631 */     lcd.setSize(dr.readDoubleWord());
/* 632 */     lcd.setTimeDateStamp(dr.readDoubleWord());
/* 633 */     lcd.setMajorVersion(dr.readWord());
/* 634 */     lcd.setMinorVersion(dr.readWord());
/* 635 */     lcd.setGlobalFlagsClear(dr.readDoubleWord());
/* 636 */     lcd.setGlobalFlagsSet(dr.readDoubleWord());
/* 637 */     lcd.setCriticalSectionDefaultTimeout(dr.readDoubleWord());
/* 638 */     lcd.setDeCommitFreeBlockThreshold(pe.is64() ? dr.readLong() : dr.readDoubleWord());
/* 639 */     lcd.setDeCommitTotalFreeThreshold(pe.is64() ? dr.readLong() : dr.readDoubleWord());
/* 640 */     lcd.setLockPrefixTable(pe.is64() ? dr.readLong() : dr.readDoubleWord());
/* 641 */     lcd.setMaximumAllocationSize(pe.is64() ? dr.readLong() : dr.readDoubleWord());
/* 642 */     lcd.setVirtualMemoryThreshold(pe.is64() ? dr.readLong() : dr.readDoubleWord());
/* 643 */     lcd.setProcessAffinityMask(pe.is64() ? dr.readLong() : dr.readDoubleWord());
/* 644 */     lcd.setProcessHeapFlags(dr.readDoubleWord());
/* 645 */     lcd.setCsdVersion(dr.readWord());
/* 646 */     lcd.setReserved(dr.readWord());
/* 647 */     lcd.setEditList(pe.is64() ? dr.readLong() : dr.readDoubleWord());
/* 648 */     if (dr.hasMore())
/* 649 */       lcd.setSecurityCookie(pe.is64() ? dr.readLong() : dr.readDoubleWord()); 
/* 650 */     if (dr.hasMore())
/* 651 */       lcd.setSeHandlerTable(pe.is64() ? dr.readLong() : dr.readDoubleWord()); 
/* 652 */     if (dr.hasMore()) {
/* 653 */       lcd.setSeHandlerCount(pe.is64() ? dr.readLong() : dr.readDoubleWord());
/*     */     }
/* 655 */     return lcd;
/*     */   }
/*     */ 
/*     */   
/*     */   public static DebugDirectory readDebugDirectory(byte[] b) throws IOException {
/* 660 */     return readDebugDirectory(b, new DataReader(b));
/*     */   }
/*     */ 
/*     */   
/*     */   public static DebugDirectory readDebugDirectory(byte[] b, IDataReader dr) throws IOException {
/* 665 */     DebugDirectory dd = new DebugDirectory();
/* 666 */     dd.set(b);
/* 667 */     dd.setCharacteristics(dr.readDoubleWord());
/* 668 */     dd.setTimeDateStamp(dr.readDoubleWord());
/* 669 */     dd.setMajorVersion(dr.readWord());
/* 670 */     dd.setMajorVersion(dr.readWord());
/* 671 */     dd.setType(dr.readDoubleWord());
/* 672 */     dd.setSizeOfData(dr.readDoubleWord());
/* 673 */     dd.setAddressOfRawData(dr.readDoubleWord());
/* 674 */     dd.setPointerToRawData(dr.readDoubleWord());
/* 675 */     return dd;
/*     */   }
/*     */ 
/*     */   
/*     */   private static ResourceDirectory readResourceDirectory(byte[] b, int baseAddress) throws IOException {
/* 680 */     IDataReader dr = new ByteArrayDataReader(b);
/* 681 */     return readResourceDirectory(dr, baseAddress);
/*     */   }
/*     */ 
/*     */   
/*     */   private static ResourceDirectory readResourceDirectory(IDataReader dr, int baseAddress) throws IOException {
/* 686 */     ResourceDirectory d = new ResourceDirectory();
/* 687 */     d.setTable(readResourceDirectoryTable(dr));
/*     */     
/* 689 */     int ne = d.getTable().getNumNameEntries() + d.getTable().getNumIdEntries();
/* 690 */     for (int i = 0; i < ne; i++) {
/* 691 */       d.add(readResourceEntry(dr, baseAddress));
/*     */     }
/*     */     
/* 694 */     return d;
/*     */   }
/*     */ 
/*     */   
/*     */   private static ResourceEntry readResourceEntry(IDataReader dr, int baseAddress) throws IOException {
/* 699 */     ResourceEntry re = new ResourceEntry();
/* 700 */     int id = dr.readDoubleWord();
/* 701 */     int offset = dr.readDoubleWord();
/* 702 */     re.setOffset(offset);
/* 703 */     int pos = dr.getPosition();
/* 704 */     if ((id & Integer.MIN_VALUE) != 0) {
/* 705 */       dr.jumpTo(id & Integer.MAX_VALUE);
/* 706 */       re.setName(dr.readUnicode(dr.readWord()));
/*     */     } else {
/* 708 */       re.setId(id);
/*     */     } 
/* 710 */     if ((offset & Integer.MIN_VALUE) != 0) {
/* 711 */       dr.jumpTo(offset & Integer.MAX_VALUE);
/* 712 */       re.setDirectory(readResourceDirectory(dr, baseAddress));
/*     */     } else {
/* 714 */       dr.jumpTo(offset);
/* 715 */       int rva = dr.readDoubleWord();
/* 716 */       int size = dr.readDoubleWord();
/* 717 */       int cp = dr.readDoubleWord();
/* 718 */       int res = dr.readDoubleWord();
/* 719 */       re.setDataRVA(rva);
/* 720 */       re.setCodePage(cp);
/* 721 */       re.setReserved(res);
/* 722 */       dr.jumpTo(rva - baseAddress);
/* 723 */       byte[] b = new byte[size];
/* 724 */       dr.read(b);
/* 725 */       re.setData(b);
/*     */     } 
/* 727 */     dr.jumpTo(pos);
/* 728 */     return re;
/*     */   }
/*     */ 
/*     */   
/*     */   private static ResourceDirectoryTable readResourceDirectoryTable(IDataReader dr) throws IOException {
/* 733 */     ResourceDirectoryTable t = new ResourceDirectoryTable();
/* 734 */     t.setCharacteristics(dr.readDoubleWord());
/* 735 */     t.setTimeDateStamp(dr.readDoubleWord());
/* 736 */     t.setMajorVersion(dr.readWord());
/* 737 */     t.setMinVersion(dr.readWord());
/* 738 */     t.setNumNameEntries(dr.readWord());
/* 739 */     t.setNumIdEntries(dr.readWord());
/*     */     
/* 741 */     return t;
/*     */   }
/*     */ 
/*     */   
/*     */   public static AttributeCertificateTable readAttributeCertificateTable(byte[] b) throws IOException {
/* 746 */     return readAttributeCertificateTable(b, new DataReader(b));
/*     */   }
/*     */ 
/*     */   
/*     */   public static AttributeCertificateTable readAttributeCertificateTable(byte[] b, IDataReader dr) throws IOException {
/* 751 */     AttributeCertificateTable dd = new AttributeCertificateTable();
/* 752 */     dd.set(b);
/* 753 */     dd.setLength(dr.readDoubleWord());
/* 754 */     dd.setRevision(dr.readWord());
/* 755 */     dd.setCertificateType(dr.readWord());
/* 756 */     byte[] certificate = new byte[dd.getLength() - 8];
/* 757 */     dr.read(certificate);
/* 758 */     dd.setCertificate(certificate);
/* 759 */     return dd;
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\kichik\pecoff4j\io\PEParser.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */