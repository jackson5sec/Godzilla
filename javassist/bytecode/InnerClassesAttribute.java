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
/*     */ public class InnerClassesAttribute
/*     */   extends AttributeInfo
/*     */ {
/*     */   public static final String tag = "InnerClasses";
/*     */   
/*     */   InnerClassesAttribute(ConstPool cp, int n, DataInputStream in) throws IOException {
/*  35 */     super(cp, n, in);
/*     */   }
/*     */   
/*     */   private InnerClassesAttribute(ConstPool cp, byte[] info) {
/*  39 */     super(cp, "InnerClasses", info);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public InnerClassesAttribute(ConstPool cp) {
/*  48 */     super(cp, "InnerClasses", new byte[2]);
/*  49 */     ByteArray.write16bit(0, get(), 0);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public int tableLength() {
/*  55 */     return ByteArray.readU16bit(get(), 0);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public int innerClassIndex(int nth) {
/*  61 */     return ByteArray.readU16bit(get(), nth * 8 + 2);
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
/*     */   public String innerClass(int nth) {
/*  73 */     int i = innerClassIndex(nth);
/*  74 */     if (i == 0)
/*  75 */       return null; 
/*  76 */     return this.constPool.getClassInfo(i);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setInnerClassIndex(int nth, int index) {
/*  84 */     ByteArray.write16bit(index, get(), nth * 8 + 2);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int outerClassIndex(int nth) {
/*  91 */     return ByteArray.readU16bit(get(), nth * 8 + 4);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String outerClass(int nth) {
/* 101 */     int i = outerClassIndex(nth);
/* 102 */     if (i == 0)
/* 103 */       return null; 
/* 104 */     return this.constPool.getClassInfo(i);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setOuterClassIndex(int nth, int index) {
/* 112 */     ByteArray.write16bit(index, get(), nth * 8 + 4);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int innerNameIndex(int nth) {
/* 119 */     return ByteArray.readU16bit(get(), nth * 8 + 6);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String innerName(int nth) {
/* 129 */     int i = innerNameIndex(nth);
/* 130 */     if (i == 0)
/* 131 */       return null; 
/* 132 */     return this.constPool.getUtf8Info(i);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setInnerNameIndex(int nth, int index) {
/* 140 */     ByteArray.write16bit(index, get(), nth * 8 + 6);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int accessFlags(int nth) {
/* 147 */     return ByteArray.readU16bit(get(), nth * 8 + 8);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setAccessFlags(int nth, int flags) {
/* 155 */     ByteArray.write16bit(flags, get(), nth * 8 + 8);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int find(String name) {
/* 166 */     int n = tableLength();
/* 167 */     for (int i = 0; i < n; i++) {
/* 168 */       if (name.equals(innerClass(i)))
/* 169 */         return i; 
/*     */     } 
/* 171 */     return -1;
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
/*     */   public void append(String inner, String outer, String name, int flags) {
/* 183 */     int i = this.constPool.addClassInfo(inner);
/* 184 */     int o = this.constPool.addClassInfo(outer);
/* 185 */     int n = this.constPool.addUtf8Info(name);
/* 186 */     append(i, o, n, flags);
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
/*     */   public void append(int inner, int outer, int name, int flags) {
/* 198 */     byte[] data = get();
/* 199 */     int len = data.length;
/* 200 */     byte[] newData = new byte[len + 8];
/* 201 */     for (int i = 2; i < len; i++) {
/* 202 */       newData[i] = data[i];
/*     */     }
/* 204 */     int n = ByteArray.readU16bit(data, 0);
/* 205 */     ByteArray.write16bit(n + 1, newData, 0);
/*     */     
/* 207 */     ByteArray.write16bit(inner, newData, len);
/* 208 */     ByteArray.write16bit(outer, newData, len + 2);
/* 209 */     ByteArray.write16bit(name, newData, len + 4);
/* 210 */     ByteArray.write16bit(flags, newData, len + 6);
/*     */     
/* 212 */     set(newData);
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
/*     */   public int remove(int nth) {
/* 226 */     byte[] data = get();
/* 227 */     int len = data.length;
/* 228 */     if (len < 10) {
/* 229 */       return 0;
/*     */     }
/* 231 */     int n = ByteArray.readU16bit(data, 0);
/* 232 */     int nthPos = 2 + nth * 8;
/* 233 */     if (n <= nth) {
/* 234 */       return n;
/*     */     }
/* 236 */     byte[] newData = new byte[len - 8];
/* 237 */     ByteArray.write16bit(n - 1, newData, 0);
/* 238 */     int i = 2, j = 2;
/* 239 */     while (i < len) {
/* 240 */       if (i == nthPos) {
/* 241 */         i += 8; continue;
/*     */       } 
/* 243 */       newData[j++] = data[i++];
/*     */     } 
/* 245 */     set(newData);
/* 246 */     return n - 1;
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
/*     */   public AttributeInfo copy(ConstPool newCp, Map<String, String> classnames) {
/* 259 */     byte[] src = get();
/* 260 */     byte[] dest = new byte[src.length];
/* 261 */     ConstPool cp = getConstPool();
/* 262 */     InnerClassesAttribute attr = new InnerClassesAttribute(newCp, dest);
/* 263 */     int n = ByteArray.readU16bit(src, 0);
/* 264 */     ByteArray.write16bit(n, dest, 0);
/* 265 */     int j = 2;
/* 266 */     for (int i = 0; i < n; i++) {
/* 267 */       int innerClass = ByteArray.readU16bit(src, j);
/* 268 */       int outerClass = ByteArray.readU16bit(src, j + 2);
/* 269 */       int innerName = ByteArray.readU16bit(src, j + 4);
/* 270 */       int innerAccess = ByteArray.readU16bit(src, j + 6);
/*     */       
/* 272 */       if (innerClass != 0) {
/* 273 */         innerClass = cp.copy(innerClass, newCp, classnames);
/*     */       }
/* 275 */       ByteArray.write16bit(innerClass, dest, j);
/*     */       
/* 277 */       if (outerClass != 0) {
/* 278 */         outerClass = cp.copy(outerClass, newCp, classnames);
/*     */       }
/* 280 */       ByteArray.write16bit(outerClass, dest, j + 2);
/*     */       
/* 282 */       if (innerName != 0) {
/* 283 */         innerName = cp.copy(innerName, newCp, classnames);
/*     */       }
/* 285 */       ByteArray.write16bit(innerName, dest, j + 4);
/* 286 */       ByteArray.write16bit(innerAccess, dest, j + 6);
/* 287 */       j += 8;
/*     */     } 
/*     */     
/* 290 */     return attr;
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\javassist\bytecode\InnerClassesAttribute.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */