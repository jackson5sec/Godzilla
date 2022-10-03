/*     */ package org.springframework.util;
/*     */ 
/*     */ import java.nio.charset.Charset;
/*     */ import java.nio.charset.StandardCharsets;
/*     */ import java.util.Base64;
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
/*     */ public abstract class Base64Utils
/*     */ {
/*  35 */   private static final Charset DEFAULT_CHARSET = StandardCharsets.UTF_8;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static byte[] encode(byte[] src) {
/*  44 */     if (src.length == 0) {
/*  45 */       return src;
/*     */     }
/*  47 */     return Base64.getEncoder().encode(src);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static byte[] decode(byte[] src) {
/*  56 */     if (src.length == 0) {
/*  57 */       return src;
/*     */     }
/*  59 */     return Base64.getDecoder().decode(src);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static byte[] encodeUrlSafe(byte[] src) {
/*  70 */     if (src.length == 0) {
/*  71 */       return src;
/*     */     }
/*  73 */     return Base64.getUrlEncoder().encode(src);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static byte[] decodeUrlSafe(byte[] src) {
/*  84 */     if (src.length == 0) {
/*  85 */       return src;
/*     */     }
/*  87 */     return Base64.getUrlDecoder().decode(src);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static String encodeToString(byte[] src) {
/*  96 */     if (src.length == 0) {
/*  97 */       return "";
/*     */     }
/*  99 */     return new String(encode(src), DEFAULT_CHARSET);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static byte[] decodeFromString(String src) {
/* 108 */     if (src.isEmpty()) {
/* 109 */       return new byte[0];
/*     */     }
/* 111 */     return decode(src.getBytes(DEFAULT_CHARSET));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static String encodeToUrlSafeString(byte[] src) {
/* 121 */     return new String(encodeUrlSafe(src), DEFAULT_CHARSET);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static byte[] decodeFromUrlSafeString(String src) {
/* 131 */     return decodeUrlSafe(src.getBytes(DEFAULT_CHARSET));
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\springframewor\\util\Base64Utils.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */