/*     */ package org.mozilla.javascript.typedarrays;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ByteIo
/*     */ {
/*     */   public static Object readInt8(byte[] buf, int offset) {
/*  13 */     return Byte.valueOf(buf[offset]);
/*     */   }
/*     */ 
/*     */   
/*     */   public static void writeInt8(byte[] buf, int offset, int val) {
/*  18 */     buf[offset] = (byte)val;
/*     */   }
/*     */ 
/*     */   
/*     */   public static Object readUint8(byte[] buf, int offset) {
/*  23 */     return Integer.valueOf(buf[offset] & 0xFF);
/*     */   }
/*     */ 
/*     */   
/*     */   public static void writeUint8(byte[] buf, int offset, int val) {
/*  28 */     buf[offset] = (byte)(val & 0xFF);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static short doReadInt16(byte[] buf, int offset, boolean littleEndian) {
/*  34 */     if (littleEndian) {
/*  35 */       return (short)(buf[offset] & 0xFF | (buf[offset + 1] & 0xFF) << 8);
/*     */     }
/*     */ 
/*     */     
/*  39 */     return (short)((buf[offset] & 0xFF) << 8 | buf[offset + 1] & 0xFF);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static void doWriteInt16(byte[] buf, int offset, int val, boolean littleEndian) {
/*  46 */     if (littleEndian) {
/*  47 */       buf[offset] = (byte)(val & 0xFF);
/*  48 */       buf[offset + 1] = (byte)(val >>> 8 & 0xFF);
/*     */     } else {
/*  50 */       buf[offset] = (byte)(val >>> 8 & 0xFF);
/*  51 */       buf[offset + 1] = (byte)(val & 0xFF);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public static Object readInt16(byte[] buf, int offset, boolean littleEndian) {
/*  57 */     return Short.valueOf(doReadInt16(buf, offset, littleEndian));
/*     */   }
/*     */ 
/*     */   
/*     */   public static void writeInt16(byte[] buf, int offset, int val, boolean littleEndian) {
/*  62 */     doWriteInt16(buf, offset, val, littleEndian);
/*     */   }
/*     */ 
/*     */   
/*     */   public static Object readUint16(byte[] buf, int offset, boolean littleEndian) {
/*  67 */     return Integer.valueOf(doReadInt16(buf, offset, littleEndian) & 0xFFFF);
/*     */   }
/*     */ 
/*     */   
/*     */   public static void writeUint16(byte[] buf, int offset, int val, boolean littleEndian) {
/*  72 */     doWriteInt16(buf, offset, val & 0xFFFF, littleEndian);
/*     */   }
/*     */ 
/*     */   
/*     */   public static Object readInt32(byte[] buf, int offset, boolean littleEndian) {
/*  77 */     if (littleEndian) {
/*  78 */       return Integer.valueOf(buf[offset] & 0xFF | (buf[offset + 1] & 0xFF) << 8 | (buf[offset + 2] & 0xFF) << 16 | (buf[offset + 3] & 0xFF) << 24);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  84 */     return Integer.valueOf((buf[offset] & 0xFF) << 24 | (buf[offset + 1] & 0xFF) << 16 | (buf[offset + 2] & 0xFF) << 8 | buf[offset + 3] & 0xFF);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void writeInt32(byte[] buf, int offset, int val, boolean littleEndian) {
/*  93 */     if (littleEndian) {
/*  94 */       buf[offset] = (byte)(val & 0xFF);
/*  95 */       buf[offset + 1] = (byte)(val >>> 8 & 0xFF);
/*  96 */       buf[offset + 2] = (byte)(val >>> 16 & 0xFF);
/*  97 */       buf[offset + 3] = (byte)(val >>> 24 & 0xFF);
/*     */     } else {
/*  99 */       buf[offset] = (byte)(val >>> 24 & 0xFF);
/* 100 */       buf[offset + 1] = (byte)(val >>> 16 & 0xFF);
/* 101 */       buf[offset + 2] = (byte)(val >>> 8 & 0xFF);
/* 102 */       buf[offset + 3] = (byte)(val & 0xFF);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public static long readUint32Primitive(byte[] buf, int offset, boolean littleEndian) {
/* 108 */     if (littleEndian) {
/* 109 */       return (buf[offset] & 0xFFL | (buf[offset + 1] & 0xFFL) << 8L | (buf[offset + 2] & 0xFFL) << 16L | (buf[offset + 3] & 0xFFL) << 24L) & 0xFFFFFFFFL;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 116 */     return ((buf[offset] & 0xFFL) << 24L | (buf[offset + 1] & 0xFFL) << 16L | (buf[offset + 2] & 0xFFL) << 8L | buf[offset + 3] & 0xFFL) & 0xFFFFFFFFL;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void writeUint32(byte[] buf, int offset, long val, boolean littleEndian) {
/* 126 */     if (littleEndian) {
/* 127 */       buf[offset] = (byte)(int)(val & 0xFFL);
/* 128 */       buf[offset + 1] = (byte)(int)(val >>> 8L & 0xFFL);
/* 129 */       buf[offset + 2] = (byte)(int)(val >>> 16L & 0xFFL);
/* 130 */       buf[offset + 3] = (byte)(int)(val >>> 24L & 0xFFL);
/*     */     } else {
/* 132 */       buf[offset] = (byte)(int)(val >>> 24L & 0xFFL);
/* 133 */       buf[offset + 1] = (byte)(int)(val >>> 16L & 0xFFL);
/* 134 */       buf[offset + 2] = (byte)(int)(val >>> 8L & 0xFFL);
/* 135 */       buf[offset + 3] = (byte)(int)(val & 0xFFL);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public static Object readUint32(byte[] buf, int offset, boolean littleEndian) {
/* 141 */     return Long.valueOf(readUint32Primitive(buf, offset, littleEndian));
/*     */   }
/*     */ 
/*     */   
/*     */   public static long readUint64Primitive(byte[] buf, int offset, boolean littleEndian) {
/* 146 */     if (littleEndian) {
/* 147 */       return buf[offset] & 0xFFL | (buf[offset + 1] & 0xFFL) << 8L | (buf[offset + 2] & 0xFFL) << 16L | (buf[offset + 3] & 0xFFL) << 24L | (buf[offset + 4] & 0xFFL) << 32L | (buf[offset + 5] & 0xFFL) << 40L | (buf[offset + 6] & 0xFFL) << 48L | (buf[offset + 7] & 0xFFL) << 56L;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 157 */     return (buf[offset] & 0xFFL) << 56L | (buf[offset + 1] & 0xFFL) << 48L | (buf[offset + 2] & 0xFFL) << 40L | (buf[offset + 3] & 0xFFL) << 32L | (buf[offset + 4] & 0xFFL) << 24L | (buf[offset + 5] & 0xFFL) << 16L | (buf[offset + 6] & 0xFFL) << 8L | (buf[offset + 7] & 0xFFL) << 0L;
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
/*     */   public static void writeUint64(byte[] buf, int offset, long val, boolean littleEndian) {
/* 170 */     if (littleEndian) {
/* 171 */       buf[offset] = (byte)(int)(val & 0xFFL);
/* 172 */       buf[offset + 1] = (byte)(int)(val >>> 8L & 0xFFL);
/* 173 */       buf[offset + 2] = (byte)(int)(val >>> 16L & 0xFFL);
/* 174 */       buf[offset + 3] = (byte)(int)(val >>> 24L & 0xFFL);
/* 175 */       buf[offset + 4] = (byte)(int)(val >>> 32L & 0xFFL);
/* 176 */       buf[offset + 5] = (byte)(int)(val >>> 40L & 0xFFL);
/* 177 */       buf[offset + 6] = (byte)(int)(val >>> 48L & 0xFFL);
/* 178 */       buf[offset + 7] = (byte)(int)(val >>> 56L & 0xFFL);
/*     */     } else {
/* 180 */       buf[offset] = (byte)(int)(val >>> 56L & 0xFFL);
/* 181 */       buf[offset + 1] = (byte)(int)(val >>> 48L & 0xFFL);
/* 182 */       buf[offset + 2] = (byte)(int)(val >>> 40L & 0xFFL);
/* 183 */       buf[offset + 3] = (byte)(int)(val >>> 32L & 0xFFL);
/* 184 */       buf[offset + 4] = (byte)(int)(val >>> 24L & 0xFFL);
/* 185 */       buf[offset + 5] = (byte)(int)(val >>> 16L & 0xFFL);
/* 186 */       buf[offset + 6] = (byte)(int)(val >>> 8L & 0xFFL);
/* 187 */       buf[offset + 7] = (byte)(int)(val & 0xFFL);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public static Object readFloat32(byte[] buf, int offset, boolean littleEndian) {
/* 193 */     long base = readUint32Primitive(buf, offset, littleEndian);
/* 194 */     return Float.valueOf(Float.intBitsToFloat((int)base));
/*     */   }
/*     */ 
/*     */   
/*     */   public static void writeFloat32(byte[] buf, int offset, double val, boolean littleEndian) {
/* 199 */     long base = Float.floatToIntBits((float)val);
/* 200 */     writeUint32(buf, offset, base, littleEndian);
/*     */   }
/*     */ 
/*     */   
/*     */   public static Object readFloat64(byte[] buf, int offset, boolean littleEndian) {
/* 205 */     long base = readUint64Primitive(buf, offset, littleEndian);
/* 206 */     return Double.valueOf(Double.longBitsToDouble(base));
/*     */   }
/*     */ 
/*     */   
/*     */   public static void writeFloat64(byte[] buf, int offset, double val, boolean littleEndian) {
/* 211 */     long base = Double.doubleToLongBits(val);
/* 212 */     writeUint64(buf, offset, base, littleEndian);
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\mozilla\javascript\typedarrays\ByteIo.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */