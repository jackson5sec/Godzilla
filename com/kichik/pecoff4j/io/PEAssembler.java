/*     */ package com.kichik.pecoff4j.io;
/*     */ 
/*     */ import com.kichik.pecoff4j.BoundImport;
/*     */ import com.kichik.pecoff4j.BoundImportDirectoryTable;
/*     */ import com.kichik.pecoff4j.COFFHeader;
/*     */ import com.kichik.pecoff4j.DOSHeader;
/*     */ import com.kichik.pecoff4j.DOSStub;
/*     */ import com.kichik.pecoff4j.ImageData;
/*     */ import com.kichik.pecoff4j.ImageDataDirectory;
/*     */ import com.kichik.pecoff4j.OptionalHeader;
/*     */ import com.kichik.pecoff4j.PE;
/*     */ import com.kichik.pecoff4j.PESignature;
/*     */ import com.kichik.pecoff4j.RVAConverter;
/*     */ import com.kichik.pecoff4j.SectionData;
/*     */ import com.kichik.pecoff4j.SectionHeader;
/*     */ import com.kichik.pecoff4j.SectionTable;
/*     */ import java.io.ByteArrayOutputStream;
/*     */ import java.io.File;
/*     */ import java.io.FileOutputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.OutputStream;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collections;
/*     */ import java.util.Comparator;
/*     */ import java.util.HashSet;
/*     */ import java.util.List;
/*     */ import java.util.Set;
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
/*     */ public class PEAssembler
/*     */ {
/*     */   public static byte[] toBytes(PE pe) throws IOException {
/*  42 */     ByteArrayOutputStream bos = new ByteArrayOutputStream();
/*  43 */     write(pe, bos);
/*  44 */     return bos.toByteArray();
/*     */   }
/*     */   
/*     */   public static void write(PE pe, String filename) throws IOException {
/*  48 */     write(pe, new FileOutputStream(filename));
/*     */   }
/*     */   
/*     */   public static void write(PE pe, File file) throws IOException {
/*  52 */     write(pe, new FileOutputStream(file));
/*     */   }
/*     */   
/*     */   public static void write(PE pe, OutputStream os) throws IOException {
/*  56 */     DataWriter dw = new DataWriter(os);
/*  57 */     write(pe, dw);
/*  58 */     dw.flush();
/*     */   }
/*     */   
/*     */   public static void write(PE pe, IDataWriter dw) throws IOException {
/*  62 */     write(pe.getDosHeader(), dw);
/*  63 */     write(pe.getStub(), dw);
/*  64 */     write(pe.getSignature(), dw);
/*  65 */     write(pe.getCoffHeader(), dw);
/*  66 */     write(pe.getOptionalHeader(), dw);
/*  67 */     writeSectionHeaders(pe, dw);
/*     */ 
/*     */     
/*  70 */     DataEntry entry = null;
/*  71 */     while ((entry = PEParser.findNextEntry(pe, dw.getPosition())) != null) {
/*  72 */       if (entry.isSection) {
/*  73 */         writeSection(pe, entry, dw); continue;
/*  74 */       }  if (entry.isDebugRawData) {
/*  75 */         writeDebugRawData(pe, entry, dw); continue;
/*     */       } 
/*  77 */       writeImageData(pe, entry, dw);
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/*  82 */     byte[] tb = pe.getImageData().getTrailingData();
/*  83 */     if (tb != null)
/*  84 */       dw.writeBytes(tb); 
/*     */   }
/*     */   
/*     */   private static void write(DOSHeader dh, IDataWriter dw) throws IOException {
/*  88 */     dw.writeWord(dh.getMagic());
/*  89 */     dw.writeWord(dh.getUsedBytesInLastPage());
/*  90 */     dw.writeWord(dh.getFileSizeInPages());
/*  91 */     dw.writeWord(dh.getNumRelocationItems());
/*  92 */     dw.writeWord(dh.getHeaderSizeInParagraphs());
/*  93 */     dw.writeWord(dh.getMinExtraParagraphs());
/*  94 */     dw.writeWord(dh.getMaxExtraParagraphs());
/*  95 */     dw.writeWord(dh.getInitialSS());
/*  96 */     dw.writeWord(dh.getInitialSP());
/*  97 */     dw.writeWord(dh.getChecksum());
/*  98 */     dw.writeWord(dh.getInitialIP());
/*  99 */     dw.writeWord(dh.getInitialRelativeCS());
/* 100 */     dw.writeWord(dh.getAddressOfRelocationTable());
/* 101 */     dw.writeWord(dh.getOverlayNumber());
/* 102 */     int[] res = dh.getReserved();
/* 103 */     for (int i = 0; i < res.length; i++) {
/* 104 */       dw.writeWord(res[i]);
/*     */     }
/* 106 */     dw.writeWord(dh.getOemId());
/* 107 */     dw.writeWord(dh.getOemInfo());
/* 108 */     int[] res2 = dh.getReserved2();
/* 109 */     for (int j = 0; j < res2.length; j++) {
/* 110 */       dw.writeWord(res2[j]);
/*     */     }
/* 112 */     dw.writeDoubleWord(dh.getAddressOfNewExeHeader());
/*     */   }
/*     */   
/*     */   private static void write(DOSStub stub, IDataWriter dw) throws IOException {
/* 116 */     dw.writeBytes(stub.getStub());
/*     */   }
/*     */   
/*     */   private static void write(PESignature s, IDataWriter dw) throws IOException {
/* 120 */     dw.writeBytes(s.getSignature());
/*     */   }
/*     */   
/*     */   private static void write(COFFHeader ch, IDataWriter dw) throws IOException {
/* 124 */     dw.writeWord(ch.getMachine());
/* 125 */     dw.writeWord(ch.getNumberOfSections());
/* 126 */     dw.writeDoubleWord(ch.getTimeDateStamp());
/* 127 */     dw.writeDoubleWord(ch.getPointerToSymbolTable());
/* 128 */     dw.writeDoubleWord(ch.getNumberOfSymbols());
/* 129 */     dw.writeWord(ch.getSizeOfOptionalHeader());
/* 130 */     dw.writeWord(ch.getCharacteristics());
/*     */   }
/*     */ 
/*     */   
/*     */   private static void write(OptionalHeader oh, IDataWriter dw) throws IOException {
/* 135 */     boolean is64 = oh.isPE32plus();
/*     */     
/* 137 */     dw.writeWord(oh.getMagic());
/* 138 */     dw.writeByte(oh.getMajorLinkerVersion());
/* 139 */     dw.writeByte(oh.getMinorLinkerVersion());
/* 140 */     dw.writeDoubleWord(oh.getSizeOfCode());
/* 141 */     dw.writeDoubleWord(oh.getSizeOfInitializedData());
/* 142 */     dw.writeDoubleWord(oh.getSizeOfUninitializedData());
/* 143 */     dw.writeDoubleWord(oh.getAddressOfEntryPoint());
/* 144 */     dw.writeDoubleWord(oh.getBaseOfCode());
/* 145 */     if (!is64) {
/* 146 */       dw.writeDoubleWord(oh.getBaseOfData());
/*     */     }
/*     */     
/* 149 */     if (is64) {
/* 150 */       dw.writeLong(oh.getImageBase());
/*     */     } else {
/* 152 */       dw.writeDoubleWord((int)oh.getImageBase());
/*     */     } 
/* 154 */     dw.writeDoubleWord(oh.getSectionAlignment());
/* 155 */     dw.writeDoubleWord(oh.getFileAlignment());
/* 156 */     dw.writeWord(oh.getMajorOperatingSystemVersion());
/* 157 */     dw.writeWord(oh.getMinorOperatingSystemVersion());
/* 158 */     dw.writeWord(oh.getMajorImageVersion());
/* 159 */     dw.writeWord(oh.getMinorImageVersion());
/* 160 */     dw.writeWord(oh.getMajorSubsystemVersion());
/* 161 */     dw.writeWord(oh.getMinorSubsystemVersion());
/* 162 */     dw.writeDoubleWord(oh.getWin32VersionValue());
/* 163 */     dw.writeDoubleWord(oh.getSizeOfImage());
/* 164 */     dw.writeDoubleWord(oh.getSizeOfHeaders());
/* 165 */     dw.writeDoubleWord(oh.getCheckSum());
/* 166 */     dw.writeWord(oh.getSubsystem());
/* 167 */     dw.writeWord(oh.getDllCharacteristics());
/* 168 */     if (is64) {
/* 169 */       dw.writeLong(oh.getSizeOfStackReserve());
/* 170 */       dw.writeLong(oh.getSizeOfStackCommit());
/* 171 */       dw.writeLong(oh.getSizeOfHeapReserve());
/* 172 */       dw.writeLong(oh.getSizeOfHeapCommit());
/*     */     } else {
/* 174 */       dw.writeDoubleWord((int)oh.getSizeOfStackReserve());
/* 175 */       dw.writeDoubleWord((int)oh.getSizeOfStackCommit());
/* 176 */       dw.writeDoubleWord((int)oh.getSizeOfHeapReserve());
/* 177 */       dw.writeDoubleWord((int)oh.getSizeOfHeapCommit());
/*     */     } 
/*     */     
/* 180 */     dw.writeDoubleWord(oh.getLoaderFlags());
/* 181 */     dw.writeDoubleWord(oh.getNumberOfRvaAndSizes());
/*     */ 
/*     */     
/* 184 */     int ddc = oh.getDataDirectoryCount();
/* 185 */     for (int i = 0; i < ddc; i++) {
/* 186 */       write(oh.getDataDirectory(i), dw);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   private static void write(ImageDataDirectory idd, IDataWriter dw) throws IOException {
/* 192 */     dw.writeDoubleWord(idd.getVirtualAddress());
/* 193 */     dw.writeDoubleWord(idd.getSize());
/*     */   }
/*     */ 
/*     */   
/*     */   private static void writeSectionHeaders(PE pe, IDataWriter dw) throws IOException {
/* 198 */     SectionTable st = pe.getSectionTable();
/* 199 */     int ns = st.getNumberOfSections();
/* 200 */     for (int i = 0; i < ns; i++) {
/* 201 */       SectionHeader sh = st.getHeader(i);
/* 202 */       writeSectionHeader(sh, dw);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private static void writeSectionHeader(SectionHeader sh, IDataWriter dw) throws IOException {
/* 208 */     dw.writeUtf(sh.getName(), 8);
/* 209 */     dw.writeDoubleWord(sh.getVirtualSize());
/* 210 */     dw.writeDoubleWord(sh.getVirtualAddress());
/* 211 */     dw.writeDoubleWord(sh.getSizeOfRawData());
/* 212 */     dw.writeDoubleWord(sh.getPointerToRawData());
/* 213 */     dw.writeDoubleWord(sh.getPointerToRelocations());
/* 214 */     dw.writeDoubleWord(sh.getPointerToLineNumbers());
/* 215 */     dw.writeWord(sh.getNumberOfRelocations());
/* 216 */     dw.writeWord(sh.getNumberOfLineNumbers());
/* 217 */     dw.writeDoubleWord(sh.getCharacteristics());
/*     */   }
/*     */ 
/*     */   
/*     */   private static void writeImageData(PE pe, DataEntry entry, IDataWriter dw) throws IOException {
/* 222 */     ImageDataDirectory idd = pe.getOptionalHeader().getDataDirectory(entry.index);
/*     */     
/* 224 */     RVAConverter rvc = pe.getSectionTable().getRVAConverter();
/* 225 */     int prd = idd.getVirtualAddress();
/* 226 */     if (entry.index != 4)
/* 227 */       prd = rvc.convertVirtualAddressToRawDataPointer(idd
/* 228 */           .getVirtualAddress()); 
/* 229 */     if (prd > dw.getPosition()) {
/* 230 */       byte[] pa = pe.getImageData().getPreamble(entry.index);
/* 231 */       if (pa != null) {
/* 232 */         dw.writeBytes(pa);
/*     */       } else {
/* 234 */         dw.writeByte(0, prd - dw.getPosition());
/*     */       } 
/*     */     } 
/* 237 */     ImageData id = pe.getImageData();
/*     */     
/* 239 */     switch (entry.index) {
/*     */       case 0:
/* 241 */         dw.writeBytes(id.getExportTable().get());
/*     */         break;
/*     */       case 1:
/* 244 */         dw.writeBytes(id.getImportTable().get());
/*     */         break;
/*     */       case 2:
/* 247 */         dw.writeBytes(id.getResourceTable().get());
/*     */         break;
/*     */       case 3:
/* 250 */         dw.writeBytes(id.getExceptionTable());
/*     */         break;
/*     */       case 4:
/* 253 */         dw.writeBytes(id.getCertificateTable().get());
/*     */         break;
/*     */       case 5:
/* 256 */         dw.writeBytes(id.getBaseRelocationTable());
/*     */         break;
/*     */       case 6:
/* 259 */         dw.writeBytes(id.getDebug().get());
/*     */         break;
/*     */       case 7:
/* 262 */         dw.writeBytes(id.getArchitecture());
/*     */         break;
/*     */       case 8:
/* 265 */         dw.writeBytes(id.getGlobalPtr());
/*     */         break;
/*     */       case 9:
/* 268 */         dw.writeBytes(id.getTlsTable());
/*     */         break;
/*     */ 
/*     */       
/*     */       case 11:
/* 273 */         write(pe, id.getBoundImports(), dw);
/*     */         break;
/*     */       case 12:
/* 276 */         dw.writeBytes(id.getIat());
/*     */         break;
/*     */       case 13:
/* 279 */         dw.writeBytes(id.getDelayImportDescriptor());
/*     */         break;
/*     */       case 14:
/* 282 */         dw.writeBytes(id.getClrRuntimeHeader());
/*     */         break;
/*     */       case 15:
/* 285 */         dw.writeBytes(id.getReserved());
/*     */         break;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private static void writeDebugRawData(PE pe, DataEntry entry, IDataWriter dw) throws IOException {
/* 292 */     if (entry.pointer > dw.getPosition()) {
/* 293 */       byte[] pa = pe.getImageData().getDebugRawDataPreamble();
/* 294 */       if (pa != null) {
/* 295 */         dw.writeBytes(pa);
/*     */       } else {
/* 297 */         dw.writeByte(0, entry.pointer - dw.getPosition());
/*     */       } 
/* 299 */     }  dw.writeBytes(pe.getImageData().getDebugRawData());
/*     */   }
/*     */ 
/*     */   
/*     */   private static void writeSection(PE pe, DataEntry entry, IDataWriter dw) throws IOException {
/* 304 */     SectionTable st = pe.getSectionTable();
/* 305 */     SectionHeader sh = st.getHeader(entry.index);
/* 306 */     SectionData sd = st.getSection(entry.index);
/* 307 */     int prd = sh.getPointerToRawData();
/* 308 */     if (prd > dw.getPosition()) {
/* 309 */       byte[] pa = sd.getPreamble();
/* 310 */       if (pa != null) {
/* 311 */         dw.writeBytes(pa);
/*     */       } else {
/* 313 */         dw.writeByte(0, prd - dw.getPosition());
/*     */       } 
/*     */     } 
/*     */     
/* 317 */     byte[] b = sd.getData();
/* 318 */     dw.writeBytes(b);
/*     */   }
/*     */ 
/*     */   
/*     */   private static void write(PE pe, BoundImportDirectoryTable bidt, IDataWriter dw) throws IOException {
/* 323 */     int pos = dw.getPosition();
/* 324 */     List<BoundImport> bil = new ArrayList<>();
/*     */     
/* 326 */     for (int i = 0; i < bidt.size(); i++) {
/* 327 */       BoundImport bi = bidt.get(i);
/* 328 */       bil.add(bi);
/* 329 */       dw.writeDoubleWord((int)bi.getTimestamp());
/* 330 */       dw.writeWord(bi.getOffsetToModuleName());
/* 331 */       dw.writeWord(bi.getNumberOfModuleForwarderRefs());
/*     */     } 
/*     */     
/* 334 */     Collections.sort(bil, new Comparator<BoundImport>()
/*     */         {
/*     */           public int compare(BoundImport o1, BoundImport o2) {
/* 337 */             return o1.getOffsetToModuleName() - o2.getOffsetToModuleName();
/*     */           }
/*     */         });
/*     */ 
/*     */     
/* 342 */     dw.writeDoubleWord(0);
/* 343 */     dw.writeDoubleWord(0);
/*     */ 
/*     */     
/* 346 */     Set<String> names = new HashSet();
/* 347 */     for (int j = 0; j < bil.size(); j++) {
/* 348 */       String s = ((BoundImport)bil.get(j)).getModuleName();
/* 349 */       if (!names.contains(s))
/* 350 */         dw.writeUtf(s); 
/* 351 */       names.add(s);
/*     */     } 
/*     */ 
/*     */     
/* 355 */     int dpos = dw.getPosition() - pos;
/*     */ 
/*     */     
/* 358 */     int bis = pe.getOptionalHeader().getDataDirectory(11).getSize();
/* 359 */     if (bis > dpos)
/* 360 */       dw.writeByte(0, bis - dpos); 
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\kichik\pecoff4j\io\PEAssembler.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */