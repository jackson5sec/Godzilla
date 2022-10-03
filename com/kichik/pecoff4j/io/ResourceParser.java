/*     */ package com.kichik.pecoff4j.io;
/*     */ 
/*     */ import com.kichik.pecoff4j.resources.Bitmap;
/*     */ import com.kichik.pecoff4j.resources.BitmapFileHeader;
/*     */ import com.kichik.pecoff4j.resources.BitmapInfoHeader;
/*     */ import com.kichik.pecoff4j.resources.FixedFileInfo;
/*     */ import com.kichik.pecoff4j.resources.IconDirectory;
/*     */ import com.kichik.pecoff4j.resources.IconDirectoryEntry;
/*     */ import com.kichik.pecoff4j.resources.IconImage;
/*     */ import com.kichik.pecoff4j.resources.Manifest;
/*     */ import com.kichik.pecoff4j.resources.RGBQuad;
/*     */ import com.kichik.pecoff4j.resources.StringFileInfo;
/*     */ import com.kichik.pecoff4j.resources.StringPair;
/*     */ import com.kichik.pecoff4j.resources.StringTable;
/*     */ import com.kichik.pecoff4j.resources.VarFileInfo;
/*     */ import com.kichik.pecoff4j.resources.VersionInfo;
/*     */ import java.io.EOFException;
/*     */ import java.io.IOException;
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
/*     */ public class ResourceParser
/*     */ {
/*     */   public static Bitmap readBitmap(IDataReader dr) throws IOException {
/*  33 */     Bitmap bm = new Bitmap();
/*  34 */     bm.setFileHeader(readBitmapFileHeader(dr));
/*  35 */     bm.setInfoHeader(readBitmapInfoHeader(dr));
/*     */     
/*  37 */     return bm;
/*     */   }
/*     */ 
/*     */   
/*     */   public static BitmapFileHeader readBitmapFileHeader(IDataReader dr) throws IOException {
/*  42 */     BitmapFileHeader bfh = new BitmapFileHeader();
/*  43 */     bfh.setType(dr.readWord());
/*  44 */     bfh.setSize(dr.readDoubleWord());
/*  45 */     bfh.setReserved1(dr.readWord());
/*  46 */     bfh.setReserved2(dr.readWord());
/*  47 */     bfh.setOffBits(dr.readDoubleWord());
/*     */     
/*  49 */     return bfh;
/*     */   }
/*     */ 
/*     */   
/*     */   public static BitmapInfoHeader readBitmapInfoHeader(IDataReader dr) throws IOException {
/*  54 */     BitmapInfoHeader bh = new BitmapInfoHeader();
/*  55 */     bh.setSize(dr.readDoubleWord());
/*  56 */     bh.setWidth(dr.readDoubleWord());
/*  57 */     bh.setHeight(dr.readDoubleWord());
/*  58 */     bh.setPlanes(dr.readWord());
/*  59 */     bh.setBitCount(dr.readWord());
/*  60 */     bh.setCompression(dr.readDoubleWord());
/*  61 */     bh.setSizeImage(dr.readDoubleWord());
/*  62 */     bh.setXpelsPerMeter(dr.readDoubleWord());
/*  63 */     bh.setYpelsPerMeter(dr.readDoubleWord());
/*  64 */     bh.setClrUsed(dr.readDoubleWord());
/*  65 */     bh.setClrImportant(dr.readDoubleWord());
/*     */     
/*  67 */     return bh;
/*     */   }
/*     */ 
/*     */   
/*     */   public static FixedFileInfo readFixedFileInfo(IDataReader dr) throws IOException {
/*  72 */     FixedFileInfo ffi = new FixedFileInfo();
/*  73 */     ffi.setSignature(dr.readDoubleWord());
/*  74 */     ffi.setStrucVersion(dr.readDoubleWord());
/*  75 */     ffi.setFileVersionMS(dr.readDoubleWord());
/*  76 */     ffi.setFileVersionLS(dr.readDoubleWord());
/*  77 */     ffi.setProductVersionMS(dr.readDoubleWord());
/*  78 */     ffi.setProductVersionLS(dr.readDoubleWord());
/*  79 */     ffi.setFileFlagMask(dr.readDoubleWord());
/*  80 */     ffi.setFileFlags(dr.readDoubleWord());
/*  81 */     ffi.setFileOS(dr.readDoubleWord());
/*  82 */     ffi.setFileType(dr.readDoubleWord());
/*  83 */     ffi.setFileSubtype(dr.readDoubleWord());
/*  84 */     ffi.setFileDateMS(dr.readDoubleWord());
/*  85 */     ffi.setFileDateLS(dr.readDoubleWord());
/*  86 */     return ffi;
/*     */   }
/*     */ 
/*     */   
/*     */   public static IconImage readIconImage(IDataReader dr, int bytesInRes) throws IOException {
/*  91 */     IconImage ii = new IconImage();
/*  92 */     int quadSize = 0;
/*  93 */     ii.setHeader(readBitmapInfoHeader(dr));
/*  94 */     if (ii.getHeader().getClrUsed() != 0) {
/*  95 */       quadSize = ii.getHeader().getClrUsed();
/*     */     }
/*  97 */     else if (ii.getHeader().getBitCount() <= 8) {
/*  98 */       quadSize = 1 << ii.getHeader().getBitCount();
/*     */     } else {
/* 100 */       quadSize = 0;
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/* 105 */     int numBytesPerLine = ii.getHeader().getWidth() * ii.getHeader().getPlanes() * ii.getHeader().getBitCount() + 31 >> 5 << 2;
/* 106 */     int xorSize = numBytesPerLine * ii.getHeader().getHeight() / 2;
/* 107 */     int andSize = bytesInRes - quadSize * 4 - ii.getHeader().getSize() - xorSize;
/*     */ 
/*     */     
/* 110 */     if (quadSize > 0) {
/* 111 */       RGBQuad[] colors = new RGBQuad[quadSize];
/* 112 */       for (int i = 0; i < quadSize; i++) {
/* 113 */         colors[i] = readRGB(dr);
/*     */       }
/* 115 */       ii.setColors(colors);
/*     */     } 
/*     */     
/* 118 */     byte[] xorMask = new byte[xorSize];
/* 119 */     dr.read(xorMask);
/* 120 */     ii.setXorMask(xorMask);
/*     */     
/* 122 */     byte[] andMask = new byte[andSize];
/* 123 */     dr.read(andMask);
/* 124 */     ii.setAndMask(andMask);
/*     */     
/* 126 */     return ii;
/*     */   }
/*     */   
/*     */   public static IconImage readPNG(byte[] data) {
/* 130 */     IconImage ii = new IconImage();
/* 131 */     ii.setPngData(data);
/* 132 */     return ii;
/*     */   }
/*     */   
/*     */   public static VersionInfo readVersionInfo(byte[] data) throws IOException {
/* 136 */     return readVersionInfo(new DataReader(data));
/*     */   }
/*     */ 
/*     */   
/*     */   public static VersionInfo readVersionInfo(IDataReader dr) throws IOException {
/* 141 */     VersionInfo vi = new VersionInfo();
/* 142 */     vi.setLength(dr.readWord());
/* 143 */     vi.setValueLength(dr.readWord());
/* 144 */     vi.setType(dr.readWord());
/* 145 */     vi.setKey(dr.readUnicode());
/* 146 */     alignDataReader(dr);
/* 147 */     vi.setFixedFileInfo(readFixedFileInfo(dr));
/*     */ 
/*     */     
/*     */     while (true) {
/* 151 */       int padding = alignDataReader(dr);
/* 152 */       int initialPos = dr.getPosition();
/*     */       
/* 154 */       int length = dr.readWord();
/* 155 */       if (length == 0) {
/*     */         break;
/*     */       }
/* 158 */       int valueLength = dr.readWord();
/* 159 */       int type = dr.readWord();
/* 160 */       String key = dr.readUnicode();
/* 161 */       if ("VarFileInfo".equals(key)) {
/*     */         
/* 163 */         vi.setVarFileInfo(readVarFileInfo(dr, initialPos, length, valueLength, type, key, padding)); continue;
/* 164 */       }  if ("StringFileInfo".equals(key)) {
/* 165 */         vi.setStringFileInfo(readStringFileInfo(dr, initialPos, length, valueLength, type, key, padding));
/*     */         break;
/*     */       } 
/* 168 */       dr.jumpTo(initialPos + length);
/*     */       
/*     */       break;
/*     */     } 
/*     */     
/* 173 */     return vi;
/*     */   }
/*     */   
/*     */   public static VarFileInfo readVarFileInfo(IDataReader dr, int initialPos, int length, int valueLength, int type, String key, int padding) throws IOException {
/* 177 */     VarFileInfo vfi = new VarFileInfo();
/* 178 */     vfi.setKey(key);
/* 179 */     String name = null;
/* 180 */     while ((name = dr.readUnicode()) != null) {
/* 181 */       if (name.length() == 2) {
/* 182 */         name = dr.readUnicode();
/*     */       }
/* 184 */       vfi.add(name, dr.readUnicode());
/*     */     } 
/* 186 */     dr.jumpTo(initialPos + length - 2);
/* 187 */     return vfi;
/*     */   }
/*     */ 
/*     */   
/*     */   public static VarFileInfo readVarFileInfo(IDataReader dr) throws IOException {
/* 192 */     VarFileInfo vfi = new VarFileInfo();
/* 193 */     vfi.setKey(dr.readUnicode());
/* 194 */     String name = null;
/* 195 */     while ((name = dr.readUnicode()) != null) {
/* 196 */       if (name.length() % 2 == 1)
/* 197 */         dr.readWord(); 
/* 198 */       vfi.add(name, dr.readUnicode());
/*     */     } 
/* 200 */     return vfi;
/*     */   }
/*     */ 
/*     */   
/*     */   public static StringTable readStringTable(IDataReader dr) throws IOException {
/* 205 */     int initialPos = dr.getPosition();
/*     */     
/* 207 */     StringTable vfi = new StringTable();
/* 208 */     vfi.setLength(dr.readWord());
/* 209 */     if (vfi.getLength() == 0) {
/* 210 */       return null;
/*     */     }
/* 212 */     vfi.setValueLength(dr.readWord());
/* 213 */     vfi.setType(dr.readWord());
/* 214 */     vfi.setKey(dr.readUnicode());
/* 215 */     vfi.setPadding(alignDataReader(dr));
/*     */     
/* 217 */     while (dr.getPosition() - initialPos < vfi.getLength()) {
/* 218 */       vfi.add(readStringPair(dr));
/*     */     }
/* 220 */     return vfi;
/*     */   }
/*     */   
/*     */   public static StringPair readStringPair(IDataReader dr) throws IOException {
/* 224 */     int initialPos = dr.getPosition();
/*     */     
/* 226 */     StringPair sp = new StringPair();
/* 227 */     sp.setLength(dr.readWord());
/* 228 */     sp.setValueLength(dr.readWord());
/* 229 */     sp.setType(dr.readWord());
/* 230 */     sp.setKey(dr.readUnicode());
/* 231 */     sp.setPadding(alignDataReader(dr));
/*     */     
/* 233 */     int remainingWords = (sp.getLength() - dr.getPosition() - initialPos) / 2;
/* 234 */     int valueLength = sp.getValueLength();
/* 235 */     if (sp.getType() == 0)
/* 236 */       valueLength /= 2; 
/* 237 */     if (valueLength > remainingWords)
/* 238 */       valueLength = remainingWords; 
/* 239 */     sp.setValue(dr.readUnicode(valueLength).trim());
/*     */     
/* 241 */     int remainingBytes = sp.getLength() - dr.getPosition() - initialPos;
/* 242 */     dr.skipBytes(remainingBytes);
/* 243 */     alignDataReader(dr);
/* 244 */     return sp;
/*     */   }
/*     */ 
/*     */   
/*     */   public static Manifest readManifest(IDataReader dr, int length) throws IOException {
/* 249 */     Manifest mf = new Manifest();
/* 250 */     mf.set(dr.readUtf(length));
/* 251 */     return mf;
/*     */   }
/*     */   
/*     */   public static RGBQuad readRGB(IDataReader dr) throws IOException {
/* 255 */     RGBQuad r = new RGBQuad();
/* 256 */     r.setBlue(dr.readByte());
/* 257 */     r.setGreen(dr.readByte());
/* 258 */     r.setRed(dr.readByte());
/* 259 */     r.setReserved(dr.readByte());
/* 260 */     return r;
/*     */   }
/*     */   
/*     */   public static StringFileInfo readStringFileInfo(IDataReader dr, int initialPos, int length, int valueLength, int type, String key, int padding) throws IOException {
/* 264 */     StringFileInfo sfi = new StringFileInfo();
/*     */     
/* 266 */     sfi.setLength(length);
/* 267 */     sfi.setValueLength(valueLength);
/* 268 */     sfi.setType(type);
/* 269 */     sfi.setKey(key);
/* 270 */     sfi.setPadding(padding);
/* 271 */     while (dr.getPosition() - initialPos < sfi.getLength()) {
/* 272 */       sfi.add(readStringTable(dr));
/*     */     }
/* 274 */     return sfi;
/*     */   }
/*     */ 
/*     */   
/*     */   public static StringFileInfo readStringFileInfo(IDataReader dr) throws IOException {
/* 279 */     int initialPos = dr.getPosition();
/*     */     
/* 281 */     StringFileInfo sfi = new StringFileInfo();
/*     */     
/* 283 */     sfi.setLength(dr.readWord());
/* 284 */     sfi.setValueLength(dr.readWord());
/* 285 */     sfi.setType(dr.readWord());
/* 286 */     sfi.setKey(dr.readUnicode());
/* 287 */     sfi.setPadding(alignDataReader(dr));
/*     */     
/* 289 */     while (dr.getPosition() - initialPos < sfi.getLength()) {
/* 290 */       sfi.add(readStringTable(dr));
/*     */     }
/* 292 */     return sfi;
/*     */   }
/*     */ 
/*     */   
/*     */   public static IconDirectoryEntry readIconDirectoryEntry(IDataReader dr) throws IOException {
/* 297 */     IconDirectoryEntry ge = new IconDirectoryEntry();
/* 298 */     ge.setWidth(dr.readByte());
/* 299 */     ge.setHeight(dr.readByte());
/* 300 */     ge.setColorCount(dr.readByte());
/* 301 */     ge.setReserved(dr.readByte());
/* 302 */     ge.setPlanes(dr.readWord());
/* 303 */     ge.setBitCount(dr.readWord());
/* 304 */     ge.setBytesInRes(dr.readDoubleWord());
/* 305 */     ge.setOffset(dr.readDoubleWord());
/*     */     
/* 307 */     return ge;
/*     */   }
/*     */ 
/*     */   
/*     */   public static IconDirectory readIconDirectory(IDataReader dr) throws IOException {
/* 312 */     IconDirectory gi = new IconDirectory();
/* 313 */     gi.setReserved(dr.readWord());
/* 314 */     gi.setType(dr.readWord());
/* 315 */     int count = dr.readWord();
/* 316 */     for (int i = 0; i < count; i++) {
/* 317 */       gi.add(readIconDirectoryEntry(dr));
/*     */     }
/*     */     
/* 320 */     return gi;
/*     */   }
/*     */   
/*     */   private static int alignDataReader(IDataReader dr) throws IOException {
/* 324 */     int off = (4 - dr.getPosition() % 4) % 4;
/*     */     try {
/* 326 */       dr.skipBytes(off);
/* 327 */     } catch (EOFException eOFException) {}
/*     */ 
/*     */     
/* 330 */     return off;
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\kichik\pecoff4j\io\ResourceParser.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */