/*     */ package org.yaml.snakeyaml.external.biz.binaryEscape;
/*     */ 
/*     */ import java.io.ByteArrayOutputStream;
/*     */ import java.io.IOException;
/*     */ 
/*     */ 
/*     */ public class BinaryEscape
/*     */ {
/*     */   public static String escape(byte[] buf) {
/*  10 */     String strHex = "";
/*  11 */     StringBuilder sb = new StringBuilder();
/*  12 */     for (int n = 0; n < buf.length; n++) {
/*  13 */       strHex = Integer.toHexString(buf[n] & 0xFF);
/*  14 */       sb.append("\\x");
/*  15 */       sb.append((strHex.length() == 1) ? ("0" + strHex) : strHex);
/*     */     } 
/*  17 */     return sb.toString();
/*     */   }
/*     */   public static String escapeStr(String str) {
/*  20 */     return escape(str.getBytes());
/*     */   }
/*     */   
/*     */   public static String unescape(byte[] buf) {
/*  24 */     String strHex = "";
/*  25 */     StringBuilder sb = new StringBuilder();
/*  26 */     for (int n = 0; n < buf.length; n++) {
/*  27 */       strHex = Integer.toHexString(buf[n] & 0xFF);
/*  28 */       sb.append("\\x");
/*  29 */       sb.append((strHex.length() == 1) ? ("0" + strHex) : strHex);
/*     */     } 
/*  31 */     return sb.toString();
/*     */   }
/*     */   
/*     */   public static String unescapeToStr(String str) {
/*  35 */     return new String(unescapeToBytes(str));
/*     */   }
/*     */   
/*     */   public static byte[] unescapeToBytes(String str) {
/*  39 */     ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
/*  40 */     StringIterator stringIterator = new StringIterator(str);
/*  41 */     while (stringIterator.hasNext()) {
/*  42 */       char next = stringIterator.next();
/*  43 */       if (next == '\\' && stringIterator.hasNext()) {
/*  44 */         char next2 = stringIterator.next();
/*  45 */         if (next2 == 'u') {
/*  46 */           if (!stringIterator.hasNext(4)) {
/*  47 */             throw new UnsupportedOperationException("not enough remaining characters for \\uXXXX" + stringIterator.getErrorToken());
/*     */           }
/*     */           
/*  50 */           String next3 = stringIterator.next(4);
/*     */           try {
/*  52 */             outputStream.write((char)Integer.parseInt(next3, 16));
/*     */           }
/*  54 */           catch (NumberFormatException ex) {
/*  55 */             throw new UnsupportedOperationException("invalid unicode escape \\u" + next3 + " - must be hex digits" + stringIterator.getErrorToken());
/*     */           } 
/*     */           continue;
/*     */         } 
/*  59 */         if (next2 == 'x') {
/*  60 */           if (!stringIterator.hasNext(2)) {
/*  61 */             throw new UnsupportedOperationException("not enough remaining characters for \\uXXXX" + stringIterator.getErrorToken());
/*     */           }
/*     */           
/*  64 */           String next4 = stringIterator.next(2);
/*     */           try {
/*  66 */             outputStream.write(Integer.parseInt(next4, 16));
/*     */           }
/*  68 */           catch (NumberFormatException ex2) {
/*  69 */             throw new UnsupportedOperationException("invalid unicode escape \\x" + next4 + " - must be hex digits" + stringIterator.getErrorToken());
/*     */           } 
/*     */           continue;
/*     */         } 
/*  73 */         if (next2 == 'n') {
/*  74 */           outputStream.write(10); continue;
/*     */         } 
/*  76 */         if (next2 == 'r') {
/*  77 */           outputStream.write(13); continue;
/*     */         } 
/*  79 */         if (next2 == 't') {
/*  80 */           outputStream.write(9); continue;
/*  81 */         }  if (next2 == 'b') {
/*  82 */           outputStream.write(8); continue;
/*     */         } 
/*  84 */         if (next2 == 'f') {
/*  85 */           outputStream.write(12); continue;
/*     */         } 
/*  87 */         if (next2 == '\\') {
/*  88 */           outputStream.write(92); continue;
/*     */         } 
/*  90 */         if (next2 == '"') {
/*  91 */           outputStream.write(34); continue;
/*     */         } 
/*  93 */         if (next2 == '\'') {
/*  94 */           outputStream.write(39);
/*     */           continue;
/*     */         } 
/*  97 */         throw new UnsupportedOperationException("unknown escape \\" + next2 + stringIterator.getErrorToken());
/*     */       } 
/*     */ 
/*     */       
/*     */       try {
/* 102 */         outputStream.write(Character.toString(next).getBytes());
/* 103 */       } catch (IOException e) {
/* 104 */         throw new RuntimeException(e);
/*     */       } 
/*     */     } 
/*     */     
/* 108 */     return outputStream.toByteArray();
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\yaml\snakeyaml\external\biz\binaryEscape\BinaryEscape.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */