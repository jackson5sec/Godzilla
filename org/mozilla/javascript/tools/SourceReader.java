/*     */ package org.mozilla.javascript.tools;
/*     */ 
/*     */ import java.io.File;
/*     */ import java.io.FileInputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.net.MalformedURLException;
/*     */ import java.net.URL;
/*     */ import java.net.URLConnection;
/*     */ import org.mozilla.javascript.Kit;
/*     */ import org.mozilla.javascript.commonjs.module.provider.ParsedContentType;
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
/*     */ public class SourceReader
/*     */ {
/*     */   public static URL toUrl(String path) {
/*  28 */     if (path.indexOf(':') >= 2) {
/*     */       try {
/*  30 */         return new URL(path);
/*  31 */       } catch (MalformedURLException ex) {}
/*     */     }
/*     */ 
/*     */     
/*  35 */     return null;
/*     */   }
/*     */   public static Object readFileOrUrl(String path, boolean convertToString, String defaultEncoding) throws IOException {
/*     */     String encoding, contentType;
/*     */     byte[] data;
/*     */     Object result;
/*  41 */     URL url = toUrl(path);
/*  42 */     InputStream is = null;
/*  43 */     int capacityHint = 0;
/*     */ 
/*     */ 
/*     */     
/*     */     try {
/*  48 */       if (url == null) {
/*  49 */         File file = new File(path);
/*  50 */         contentType = encoding = null;
/*  51 */         capacityHint = (int)file.length();
/*  52 */         is = new FileInputStream(file);
/*     */       } else {
/*  54 */         URLConnection uc = url.openConnection();
/*  55 */         is = uc.getInputStream();
/*  56 */         if (convertToString) {
/*  57 */           ParsedContentType pct = new ParsedContentType(uc.getContentType());
/*  58 */           contentType = pct.getContentType();
/*  59 */           encoding = pct.getEncoding();
/*     */         } else {
/*     */           
/*  62 */           contentType = encoding = null;
/*     */         } 
/*  64 */         capacityHint = uc.getContentLength();
/*     */         
/*  66 */         if (capacityHint > 1048576) {
/*  67 */           capacityHint = -1;
/*     */         }
/*     */       } 
/*  70 */       if (capacityHint <= 0) {
/*  71 */         capacityHint = 4096;
/*     */       }
/*     */       
/*  74 */       data = Kit.readStream(is, capacityHint);
/*     */     } finally {
/*  76 */       if (is != null) {
/*  77 */         is.close();
/*     */       }
/*     */     } 
/*     */ 
/*     */     
/*  82 */     if (!convertToString) {
/*  83 */       result = data;
/*     */     } else {
/*  85 */       if (encoding == null)
/*     */       {
/*     */         
/*  88 */         if (data.length > 3 && data[0] == -1 && data[1] == -2 && data[2] == 0 && data[3] == 0) {
/*  89 */           encoding = "UTF-32LE";
/*     */         }
/*  91 */         else if (data.length > 3 && data[0] == 0 && data[1] == 0 && data[2] == -2 && data[3] == -1) {
/*  92 */           encoding = "UTF-32BE";
/*     */         }
/*  94 */         else if (data.length > 2 && data[0] == -17 && data[1] == -69 && data[2] == -65) {
/*  95 */           encoding = "UTF-8";
/*     */         }
/*  97 */         else if (data.length > 1 && data[0] == -1 && data[1] == -2) {
/*  98 */           encoding = "UTF-16LE";
/*     */         }
/* 100 */         else if (data.length > 1 && data[0] == -2 && data[1] == -1) {
/* 101 */           encoding = "UTF-16BE";
/*     */         }
/*     */         else {
/*     */           
/* 105 */           encoding = defaultEncoding;
/* 106 */           if (encoding == null)
/*     */           {
/* 108 */             if (url == null) {
/*     */               
/* 110 */               encoding = System.getProperty("file.encoding");
/*     */             }
/* 112 */             else if (contentType != null && contentType.startsWith("application/")) {
/*     */               
/* 114 */               encoding = "UTF-8";
/*     */             }
/*     */             else {
/*     */               
/* 118 */               encoding = "US-ASCII";
/*     */             } 
/*     */           }
/*     */         } 
/*     */       }
/* 123 */       String strResult = new String(data, encoding);
/*     */       
/* 125 */       if (strResult.length() > 0 && strResult.charAt(0) == '﻿')
/*     */       {
/* 127 */         strResult = strResult.substring(1);
/*     */       }
/* 129 */       result = strResult;
/*     */     } 
/* 131 */     return result;
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\mozilla\javascript\tools\SourceReader.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */