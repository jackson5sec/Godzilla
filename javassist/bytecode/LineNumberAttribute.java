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
/*     */ public class LineNumberAttribute
/*     */   extends AttributeInfo
/*     */ {
/*     */   public static final String tag = "LineNumberTable";
/*     */   
/*     */   LineNumberAttribute(ConstPool cp, int n, DataInputStream in) throws IOException {
/*  35 */     super(cp, n, in);
/*     */   }
/*     */   
/*     */   private LineNumberAttribute(ConstPool cp, byte[] i) {
/*  39 */     super(cp, "LineNumberTable", i);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int tableLength() {
/*  47 */     return ByteArray.readU16bit(this.info, 0);
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
/*  58 */     return ByteArray.readU16bit(this.info, i * 4 + 2);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int lineNumber(int i) {
/*  69 */     return ByteArray.readU16bit(this.info, i * 4 + 4);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int toLineNumber(int pc) {
/*  78 */     int n = tableLength();
/*  79 */     int i = 0;
/*  80 */     for (; i < n; i++) {
/*  81 */       if (pc < startPc(i)) {
/*  82 */         if (i == 0)
/*  83 */           return lineNumber(0); 
/*     */         break;
/*     */       } 
/*     */     } 
/*  87 */     return lineNumber(i - 1);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int toStartPc(int line) {
/*  98 */     int n = tableLength();
/*  99 */     for (int i = 0; i < n; i++) {
/* 100 */       if (line == lineNumber(i))
/* 101 */         return startPc(i); 
/*     */     } 
/* 103 */     return -1;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static class Pc
/*     */   {
/*     */     public int index;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public int line;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Pc toNearPc(int line) {
/* 130 */     int n = tableLength();
/* 131 */     int nearPc = 0;
/* 132 */     int distance = 0;
/* 133 */     if (n > 0) {
/* 134 */       distance = lineNumber(0) - line;
/* 135 */       nearPc = startPc(0);
/*     */     } 
/*     */     
/* 138 */     for (int i = 1; i < n; i++) {
/* 139 */       int d = lineNumber(i) - line;
/* 140 */       if ((d < 0 && d > distance) || (d >= 0 && (d < distance || distance < 0))) {
/*     */         
/* 142 */         distance = d;
/* 143 */         nearPc = startPc(i);
/*     */       } 
/*     */     } 
/*     */     
/* 147 */     Pc res = new Pc();
/* 148 */     res.index = nearPc;
/* 149 */     res.line = line + distance;
/* 150 */     return res;
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
/* 161 */     byte[] src = this.info;
/* 162 */     int num = src.length;
/* 163 */     byte[] dest = new byte[num];
/* 164 */     for (int i = 0; i < num; i++) {
/* 165 */       dest[i] = src[i];
/*     */     }
/* 167 */     LineNumberAttribute attr = new LineNumberAttribute(newCp, dest);
/* 168 */     return attr;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   void shiftPc(int where, int gapLength, boolean exclusive) {
/* 175 */     int n = tableLength();
/* 176 */     for (int i = 0; i < n; i++) {
/* 177 */       int pos = i * 4 + 2;
/* 178 */       int pc = ByteArray.readU16bit(this.info, pos);
/* 179 */       if (pc > where || (exclusive && pc == where))
/* 180 */         ByteArray.write16bit(pc + gapLength, this.info, pos); 
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\javassist\bytecode\LineNumberAttribute.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */