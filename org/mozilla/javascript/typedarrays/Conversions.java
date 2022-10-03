/*     */ package org.mozilla.javascript.typedarrays;
/*     */ 
/*     */ import org.mozilla.javascript.ScriptRuntime;
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
/*     */ public class Conversions
/*     */ {
/*     */   public static final int EIGHT_BIT = 256;
/*     */   public static final int SIXTEEN_BIT = 65536;
/*     */   public static final long THIRTYTWO_BIT = 4294967296L;
/*     */   
/*     */   public static int toInt8(Object arg) {
/*     */     int iv;
/*  24 */     if (arg instanceof Integer) {
/*  25 */       iv = ((Integer)arg).intValue();
/*     */     } else {
/*  27 */       iv = ScriptRuntime.toInt32(arg);
/*     */     } 
/*     */     
/*  30 */     int int8Bit = iv % 256;
/*  31 */     return (int8Bit >= 128) ? (int8Bit - 256) : int8Bit;
/*     */   }
/*     */ 
/*     */   
/*     */   public static int toUint8(Object arg) {
/*     */     int iv;
/*  37 */     if (arg instanceof Integer) {
/*  38 */       iv = ((Integer)arg).intValue();
/*     */     } else {
/*  40 */       iv = ScriptRuntime.toInt32(arg);
/*     */     } 
/*     */     
/*  43 */     return iv % 256;
/*     */   }
/*     */ 
/*     */   
/*     */   public static int toUint8Clamp(Object arg) {
/*  48 */     double d = ScriptRuntime.toNumber(arg);
/*  49 */     if (d <= 0.0D) {
/*  50 */       return 0;
/*     */     }
/*  52 */     if (d >= 255.0D) {
/*  53 */       return 255;
/*     */     }
/*     */ 
/*     */     
/*  57 */     double f = Math.floor(d);
/*  58 */     if (f + 0.5D < d) {
/*  59 */       return (int)(f + 1.0D);
/*     */     }
/*  61 */     if (d < f + 0.5D) {
/*  62 */       return (int)f;
/*     */     }
/*  64 */     if ((int)f % 2 != 0) {
/*  65 */       return (int)f + 1;
/*     */     }
/*  67 */     return (int)f;
/*     */   }
/*     */ 
/*     */   
/*     */   public static int toInt16(Object arg) {
/*     */     int iv;
/*  73 */     if (arg instanceof Integer) {
/*  74 */       iv = ((Integer)arg).intValue();
/*     */     } else {
/*  76 */       iv = ScriptRuntime.toInt32(arg);
/*     */     } 
/*     */     
/*  79 */     int int16Bit = iv % 65536;
/*  80 */     return (int16Bit >= 32768) ? (int16Bit - 65536) : int16Bit;
/*     */   }
/*     */ 
/*     */   
/*     */   public static int toUint16(Object arg) {
/*     */     int iv;
/*  86 */     if (arg instanceof Integer) {
/*  87 */       iv = ((Integer)arg).intValue();
/*     */     } else {
/*  89 */       iv = ScriptRuntime.toInt32(arg);
/*     */     } 
/*     */     
/*  92 */     return iv % 65536;
/*     */   }
/*     */ 
/*     */   
/*     */   public static int toInt32(Object arg) {
/*  97 */     long lv = (long)ScriptRuntime.toNumber(arg);
/*  98 */     long int32Bit = lv % 4294967296L;
/*  99 */     return (int)((int32Bit >= 2147483648L) ? (int32Bit - 4294967296L) : int32Bit);
/*     */   }
/*     */ 
/*     */   
/*     */   public static long toUint32(Object arg) {
/* 104 */     long lv = (long)ScriptRuntime.toNumber(arg);
/* 105 */     return lv % 4294967296L;
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\mozilla\javascript\typedarrays\Conversions.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */