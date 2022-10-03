/*     */ package org.springframework.util;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.security.MessageDigest;
/*     */ import java.security.NoSuchAlgorithmException;
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
/*     */ 
/*     */ 
/*     */ public abstract class DigestUtils
/*     */ {
/*     */   private static final String MD5_ALGORITHM_NAME = "MD5";
/*  40 */   private static final char[] HEX_CHARS = new char[] { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static byte[] md5Digest(byte[] bytes) {
/*  50 */     return digest("MD5", bytes);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static byte[] md5Digest(InputStream inputStream) throws IOException {
/*  61 */     return digest("MD5", inputStream);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static String md5DigestAsHex(byte[] bytes) {
/*  70 */     return digestAsHexString("MD5", bytes);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static String md5DigestAsHex(InputStream inputStream) throws IOException {
/*  81 */     return digestAsHexString("MD5", inputStream);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static StringBuilder appendMd5DigestAsHex(byte[] bytes, StringBuilder builder) {
/*  92 */     return appendDigestAsHex("MD5", bytes, builder);
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
/*     */   public static StringBuilder appendMd5DigestAsHex(InputStream inputStream, StringBuilder builder) throws IOException {
/* 105 */     return appendDigestAsHex("MD5", inputStream, builder);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static MessageDigest getDigest(String algorithm) {
/*     */     try {
/* 115 */       return MessageDigest.getInstance(algorithm);
/*     */     }
/* 117 */     catch (NoSuchAlgorithmException ex) {
/* 118 */       throw new IllegalStateException("Could not find MessageDigest with algorithm \"" + algorithm + "\"", ex);
/*     */     } 
/*     */   }
/*     */   
/*     */   private static byte[] digest(String algorithm, byte[] bytes) {
/* 123 */     return getDigest(algorithm).digest(bytes);
/*     */   }
/*     */   
/*     */   private static byte[] digest(String algorithm, InputStream inputStream) throws IOException {
/* 127 */     MessageDigest messageDigest = getDigest(algorithm);
/* 128 */     if (inputStream instanceof UpdateMessageDigestInputStream) {
/* 129 */       ((UpdateMessageDigestInputStream)inputStream).updateMessageDigest(messageDigest);
/* 130 */       return messageDigest.digest();
/*     */     } 
/*     */     
/* 133 */     byte[] buffer = new byte[4096];
/* 134 */     int bytesRead = -1;
/* 135 */     while ((bytesRead = inputStream.read(buffer)) != -1) {
/* 136 */       messageDigest.update(buffer, 0, bytesRead);
/*     */     }
/* 138 */     return messageDigest.digest();
/*     */   }
/*     */ 
/*     */   
/*     */   private static String digestAsHexString(String algorithm, byte[] bytes) {
/* 143 */     char[] hexDigest = digestAsHexChars(algorithm, bytes);
/* 144 */     return new String(hexDigest);
/*     */   }
/*     */   
/*     */   private static String digestAsHexString(String algorithm, InputStream inputStream) throws IOException {
/* 148 */     char[] hexDigest = digestAsHexChars(algorithm, inputStream);
/* 149 */     return new String(hexDigest);
/*     */   }
/*     */   
/*     */   private static StringBuilder appendDigestAsHex(String algorithm, byte[] bytes, StringBuilder builder) {
/* 153 */     char[] hexDigest = digestAsHexChars(algorithm, bytes);
/* 154 */     return builder.append(hexDigest);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static StringBuilder appendDigestAsHex(String algorithm, InputStream inputStream, StringBuilder builder) throws IOException {
/* 160 */     char[] hexDigest = digestAsHexChars(algorithm, inputStream);
/* 161 */     return builder.append(hexDigest);
/*     */   }
/*     */   
/*     */   private static char[] digestAsHexChars(String algorithm, byte[] bytes) {
/* 165 */     byte[] digest = digest(algorithm, bytes);
/* 166 */     return encodeHex(digest);
/*     */   }
/*     */   
/*     */   private static char[] digestAsHexChars(String algorithm, InputStream inputStream) throws IOException {
/* 170 */     byte[] digest = digest(algorithm, inputStream);
/* 171 */     return encodeHex(digest);
/*     */   }
/*     */   
/*     */   private static char[] encodeHex(byte[] bytes) {
/* 175 */     char[] chars = new char[32];
/* 176 */     for (int i = 0; i < chars.length; i += 2) {
/* 177 */       byte b = bytes[i / 2];
/* 178 */       chars[i] = HEX_CHARS[b >>> 4 & 0xF];
/* 179 */       chars[i + 1] = HEX_CHARS[b & 0xF];
/*     */     } 
/* 181 */     return chars;
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\springframewor\\util\DigestUtils.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */