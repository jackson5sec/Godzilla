/*     */ package javassist.bytecode;
/*     */ 
/*     */ import java.io.DataInputStream;
/*     */ import java.io.IOException;
/*     */ import java.util.Map;
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
/*     */ public class LocalVariableAttribute
/*     */   extends AttributeInfo
/*     */ {
/*     */   public static final String tag = "LocalVariableTable";
/*     */   public static final String typeTag = "LocalVariableTypeTable";
/*     */   
/*     */   public LocalVariableAttribute(ConstPool cp) {
/*  41 */     super(cp, "LocalVariableTable", new byte[2]);
/*  42 */     ByteArray.write16bit(0, this.info, 0);
/*     */   }
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
/*     */   @Deprecated
/*     */   public LocalVariableAttribute(ConstPool cp, String name) {
/*  58 */     super(cp, name, new byte[2]);
/*  59 */     ByteArray.write16bit(0, this.info, 0);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   LocalVariableAttribute(ConstPool cp, int n, DataInputStream in) throws IOException {
/*  65 */     super(cp, n, in);
/*     */   }
/*     */   
/*     */   LocalVariableAttribute(ConstPool cp, String name, byte[] i) {
/*  69 */     super(cp, name, i);
/*     */   }
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
/*     */   public void addEntry(int startPc, int length, int nameIndex, int descriptorIndex, int index) {
/*  83 */     int size = this.info.length;
/*  84 */     byte[] newInfo = new byte[size + 10];
/*  85 */     ByteArray.write16bit(tableLength() + 1, newInfo, 0);
/*  86 */     for (int i = 2; i < size; i++) {
/*  87 */       newInfo[i] = this.info[i];
/*     */     }
/*  89 */     ByteArray.write16bit(startPc, newInfo, size);
/*  90 */     ByteArray.write16bit(length, newInfo, size + 2);
/*  91 */     ByteArray.write16bit(nameIndex, newInfo, size + 4);
/*  92 */     ByteArray.write16bit(descriptorIndex, newInfo, size + 6);
/*  93 */     ByteArray.write16bit(index, newInfo, size + 8);
/*  94 */     this.info = newInfo;
/*     */   }
/*     */ 
/*     */   
/*     */   void renameClass(String oldname, String newname) {
/*  99 */     ConstPool cp = getConstPool();
/* 100 */     int n = tableLength();
/* 101 */     for (int i = 0; i < n; i++) {
/* 102 */       int pos = i * 10 + 2;
/* 103 */       int index = ByteArray.readU16bit(this.info, pos + 6);
/* 104 */       if (index != 0) {
/* 105 */         String desc = cp.getUtf8Info(index);
/* 106 */         desc = renameEntry(desc, oldname, newname);
/* 107 */         ByteArray.write16bit(cp.addUtf8Info(desc), this.info, pos + 6);
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   String renameEntry(String desc, String oldname, String newname) {
/* 113 */     return Descriptor.rename(desc, oldname, newname);
/*     */   }
/*     */ 
/*     */   
/*     */   void renameClass(Map<String, String> classnames) {
/* 118 */     ConstPool cp = getConstPool();
/* 119 */     int n = tableLength();
/* 120 */     for (int i = 0; i < n; i++) {
/* 121 */       int pos = i * 10 + 2;
/* 122 */       int index = ByteArray.readU16bit(this.info, pos + 6);
/* 123 */       if (index != 0) {
/* 124 */         String desc = cp.getUtf8Info(index);
/* 125 */         desc = renameEntry(desc, classnames);
/* 126 */         ByteArray.write16bit(cp.addUtf8Info(desc), this.info, pos + 6);
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   String renameEntry(String desc, Map<String, String> classnames) {
/* 132 */     return Descriptor.rename(desc, classnames);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void shiftIndex(int lessThan, int delta) {
/* 143 */     int size = this.info.length;
/* 144 */     for (int i = 2; i < size; i += 10) {
/* 145 */       int org = ByteArray.readU16bit(this.info, i + 8);
/* 146 */       if (org >= lessThan) {
/* 147 */         ByteArray.write16bit(org + delta, this.info, i + 8);
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int tableLength() {
/* 156 */     return ByteArray.readU16bit(this.info, 0);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int startPc(int i) {
/* 167 */     return ByteArray.readU16bit(this.info, i * 10 + 2);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int codeLength(int i) {
/* 178 */     return ByteArray.readU16bit(this.info, i * 10 + 4);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   void shiftPc(int where, int gapLength, boolean exclusive) {
/* 185 */     int n = tableLength();
/* 186 */     for (int i = 0; i < n; i++) {
/* 187 */       int pos = i * 10 + 2;
/* 188 */       int pc = ByteArray.readU16bit(this.info, pos);
/* 189 */       int len = ByteArray.readU16bit(this.info, pos + 2);
/*     */ 
/*     */ 
/*     */       
/* 193 */       if (pc > where || (exclusive && pc == where && pc != 0)) {
/* 194 */         ByteArray.write16bit(pc + gapLength, this.info, pos);
/* 195 */       } else if (pc + len > where || (exclusive && pc + len == where)) {
/* 196 */         ByteArray.write16bit(len + gapLength, this.info, pos + 2);
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int nameIndex(int i) {
/* 207 */     return ByteArray.readU16bit(this.info, i * 10 + 6);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String variableName(int i) {
/* 217 */     return getConstPool().getUtf8Info(nameIndex(i));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String variableNameByIndex(int index) {
/* 228 */     for (int i = 0; i < tableLength(); i++) {
/* 229 */       if (index(i) == index) {
/* 230 */         return variableName(i);
/*     */       }
/*     */     } 
/* 233 */     throw new ArrayIndexOutOfBoundsException();
/*     */   }
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
/*     */   public int descriptorIndex(int i) {
/* 249 */     return ByteArray.readU16bit(this.info, i * 10 + 8);
/*     */   }
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
/*     */   public int signatureIndex(int i) {
/* 263 */     return descriptorIndex(i);
/*     */   }
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
/*     */   public String descriptor(int i) {
/* 277 */     return getConstPool().getUtf8Info(descriptorIndex(i));
/*     */   }
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
/*     */   public String signature(int i) {
/* 294 */     return descriptor(i);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int index(int i) {
/* 304 */     return ByteArray.readU16bit(this.info, i * 10 + 10);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public AttributeInfo copy(ConstPool newCp, Map<String, String> classnames) {
/* 315 */     byte[] src = get();
/* 316 */     byte[] dest = new byte[src.length];
/* 317 */     ConstPool cp = getConstPool();
/* 318 */     LocalVariableAttribute attr = makeThisAttr(newCp, dest);
/* 319 */     int n = ByteArray.readU16bit(src, 0);
/* 320 */     ByteArray.write16bit(n, dest, 0);
/* 321 */     int j = 2;
/* 322 */     for (int i = 0; i < n; i++) {
/* 323 */       int start = ByteArray.readU16bit(src, j);
/* 324 */       int len = ByteArray.readU16bit(src, j + 2);
/* 325 */       int name = ByteArray.readU16bit(src, j + 4);
/* 326 */       int type = ByteArray.readU16bit(src, j + 6);
/* 327 */       int index = ByteArray.readU16bit(src, j + 8);
/*     */       
/* 329 */       ByteArray.write16bit(start, dest, j);
/* 330 */       ByteArray.write16bit(len, dest, j + 2);
/* 331 */       if (name != 0) {
/* 332 */         name = cp.copy(name, newCp, null);
/*     */       }
/* 334 */       ByteArray.write16bit(name, dest, j + 4);
/*     */       
/* 336 */       if (type != 0) {
/* 337 */         String sig = cp.getUtf8Info(type);
/* 338 */         sig = Descriptor.rename(sig, classnames);
/* 339 */         type = newCp.addUtf8Info(sig);
/*     */       } 
/*     */       
/* 342 */       ByteArray.write16bit(type, dest, j + 6);
/* 343 */       ByteArray.write16bit(index, dest, j + 8);
/* 344 */       j += 10;
/*     */     } 
/*     */     
/* 347 */     return attr;
/*     */   }
/*     */ 
/*     */   
/*     */   LocalVariableAttribute makeThisAttr(ConstPool cp, byte[] dest) {
/* 352 */     return new LocalVariableAttribute(cp, "LocalVariableTable", dest);
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\javassist\bytecode\LocalVariableAttribute.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */