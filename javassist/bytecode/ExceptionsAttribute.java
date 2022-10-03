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
/*     */ public class ExceptionsAttribute
/*     */   extends AttributeInfo
/*     */ {
/*     */   public static final String tag = "Exceptions";
/*     */   
/*     */   ExceptionsAttribute(ConstPool cp, int n, DataInputStream in) throws IOException {
/*  35 */     super(cp, n, in);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private ExceptionsAttribute(ConstPool cp, ExceptionsAttribute src, Map<String, String> classnames) {
/*  46 */     super(cp, "Exceptions");
/*  47 */     copyFrom(src, classnames);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ExceptionsAttribute(ConstPool cp) {
/*  56 */     super(cp, "Exceptions");
/*  57 */     byte[] data = new byte[2];
/*  58 */     data[1] = 0; data[0] = 0;
/*  59 */     this.info = data;
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
/*  72 */     return new ExceptionsAttribute(newCp, this, classnames);
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
/*     */   private void copyFrom(ExceptionsAttribute srcAttr, Map<String, String> classnames) {
/*  84 */     ConstPool srcCp = srcAttr.constPool;
/*  85 */     ConstPool destCp = this.constPool;
/*  86 */     byte[] src = srcAttr.info;
/*  87 */     int num = src.length;
/*  88 */     byte[] dest = new byte[num];
/*  89 */     dest[0] = src[0];
/*  90 */     dest[1] = src[1];
/*  91 */     for (int i = 2; i < num; i += 2) {
/*  92 */       int index = ByteArray.readU16bit(src, i);
/*  93 */       ByteArray.write16bit(srcCp.copy(index, destCp, classnames), dest, i);
/*     */     } 
/*     */ 
/*     */     
/*  97 */     this.info = dest;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int[] getExceptionIndexes() {
/* 104 */     byte[] blist = this.info;
/* 105 */     int n = blist.length;
/* 106 */     if (n <= 2) {
/* 107 */       return null;
/*     */     }
/* 109 */     int[] elist = new int[n / 2 - 1];
/* 110 */     int k = 0;
/* 111 */     for (int j = 2; j < n; j += 2) {
/* 112 */       elist[k++] = (blist[j] & 0xFF) << 8 | blist[j + 1] & 0xFF;
/*     */     }
/* 114 */     return elist;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String[] getExceptions() {
/* 121 */     byte[] blist = this.info;
/* 122 */     int n = blist.length;
/* 123 */     if (n <= 2) {
/* 124 */       return null;
/*     */     }
/* 126 */     String[] elist = new String[n / 2 - 1];
/* 127 */     int k = 0;
/* 128 */     for (int j = 2; j < n; j += 2) {
/* 129 */       int index = (blist[j] & 0xFF) << 8 | blist[j + 1] & 0xFF;
/* 130 */       elist[k++] = this.constPool.getClassInfo(index);
/*     */     } 
/*     */     
/* 133 */     return elist;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setExceptionIndexes(int[] elist) {
/* 140 */     int n = elist.length;
/* 141 */     byte[] blist = new byte[n * 2 + 2];
/* 142 */     ByteArray.write16bit(n, blist, 0);
/* 143 */     for (int i = 0; i < n; i++) {
/* 144 */       ByteArray.write16bit(elist[i], blist, i * 2 + 2);
/*     */     }
/* 146 */     this.info = blist;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setExceptions(String[] elist) {
/* 153 */     int n = elist.length;
/* 154 */     byte[] blist = new byte[n * 2 + 2];
/* 155 */     ByteArray.write16bit(n, blist, 0);
/* 156 */     for (int i = 0; i < n; i++) {
/* 157 */       ByteArray.write16bit(this.constPool.addClassInfo(elist[i]), blist, i * 2 + 2);
/*     */     }
/*     */     
/* 160 */     this.info = blist;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public int tableLength() {
/* 166 */     return this.info.length / 2 - 1;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public int getException(int nth) {
/* 172 */     int index = nth * 2 + 2;
/* 173 */     return (this.info[index] & 0xFF) << 8 | this.info[index + 1] & 0xFF;
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\javassist\bytecode\ExceptionsAttribute.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */