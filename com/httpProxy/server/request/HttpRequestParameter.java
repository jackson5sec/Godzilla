/*     */ package com.httpProxy.server.request;
/*     */ 
/*     */ import com.httpProxy.server.ByteUtil;
/*     */ import java.io.ByteArrayInputStream;
/*     */ import java.io.ByteArrayOutputStream;
/*     */ import java.io.InputStream;
/*     */ import java.util.Arrays;
/*     */ import java.util.HashMap;
/*     */ import java.util.Iterator;
/*     */ 
/*     */ 
/*     */ public class HttpRequestParameter
/*     */ {
/*  14 */   private static String defaultBoundary = "--";
/*  15 */   private static byte[] crlf = "\r\n".getBytes();
/*     */   
/*     */   private boolean isMultipart = false;
/*     */   private String boundary;
/*  19 */   private HashMap<String, byte[]> parameter = (HashMap)new HashMap<>();
/*     */ 
/*     */ 
/*     */   
/*     */   public HttpRequestParameter() {}
/*     */ 
/*     */   
/*     */   public HttpRequestParameter(boolean isMultipart, String boundary) {
/*  27 */     this.isMultipart = isMultipart;
/*  28 */     this.boundary = boundary;
/*     */   }
/*     */   
/*     */   public HttpRequestParameter decode(byte[] data) {
/*  32 */     ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(data);
/*  33 */     return decode(byteArrayInputStream);
/*     */   }
/*     */   public HttpRequestParameter decode(InputStream inputStream) {
/*  36 */     if (this.isMultipart) {
/*  37 */       decodeByMultipart(inputStream);
/*     */     } else {
/*  39 */       decodeByWwwForm(inputStream);
/*     */     } 
/*  41 */     return this;
/*     */   }
/*     */   
/*     */   public HttpRequestParameter decodeByUrl(String url) {
/*  45 */     int index = url.indexOf("?");
/*  46 */     int splitIndex = 0;
/*  47 */     if (index != -1) {
/*  48 */       StringBuilder stringBuilder = new StringBuilder(url.substring(index + 1, url.length()));
/*  49 */       while ((index = stringBuilder.indexOf("=")) != -1) {
/*  50 */         splitIndex = stringBuilder.indexOf("&");
/*  51 */         splitIndex = (splitIndex == -1) ? stringBuilder.length() : splitIndex;
/*  52 */         String name = stringBuilder.substring(0, index);
/*  53 */         String value = stringBuilder.substring(index + 1, splitIndex);
/*  54 */         stringBuilder.delete(0, splitIndex + 1);
/*  55 */         this.parameter.put(name, value.getBytes());
/*     */       } 
/*     */     } 
/*     */     
/*  59 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public byte[] encode() {
/*  65 */     if (this.isMultipart) {
/*  66 */       return encodeByMultipart();
/*     */     }
/*  68 */     return encodeByWwwForm();
/*     */   }
/*     */ 
/*     */   
/*     */   protected byte[] encodeByWwwForm() {
/*  73 */     ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
/*  74 */     Iterator<String> iterator = this.parameter.keySet().iterator();
/*  75 */     while (iterator.hasNext()) {
/*  76 */       String key = iterator.next();
/*  77 */       byte[] value = this.parameter.get(key);
/*  78 */       byte[] keyBytes = key.getBytes();
/*  79 */       byteArrayOutputStream.write(keyBytes, 0, keyBytes.length);
/*  80 */       byteArrayOutputStream.write(61);
/*  81 */       byteArrayOutputStream.write(value, 0, value.length);
/*  82 */       if (iterator.hasNext()) {
/*  83 */         byteArrayOutputStream.write(38);
/*     */       }
/*     */     } 
/*  86 */     return byteArrayOutputStream.toByteArray();
/*     */   }
/*     */   
/*     */   protected byte[] encodeByMultipart() {
/*  90 */     ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
/*  91 */     byte[] lineBoundary = (defaultBoundary + this.boundary + "\r\n").getBytes();
/*  92 */     byte[] lineBoundaryEnd = (defaultBoundary + this.boundary + defaultBoundary + "\r\n").getBytes();
/*  93 */     String nameFormat = "Content-Disposition: form-data; name=\"%s\"\r\n";
/*  94 */     Iterator<String> iterator = this.parameter.keySet().iterator();
/*  95 */     while (iterator.hasNext()) {
/*  96 */       String key = iterator.next();
/*  97 */       byte[] value = this.parameter.get(key);
/*  98 */       byte[] keyBytes = String.format(nameFormat, new Object[] { key }).getBytes();
/*  99 */       byteArrayOutputStream.write(lineBoundary, 0, lineBoundary.length);
/* 100 */       byteArrayOutputStream.write(keyBytes, 0, keyBytes.length);
/* 101 */       byteArrayOutputStream.write(crlf, 0, crlf.length);
/* 102 */       byteArrayOutputStream.write(value, 0, value.length);
/* 103 */       byteArrayOutputStream.write(crlf, 0, crlf.length);
/* 104 */       if (!iterator.hasNext()) {
/* 105 */         byteArrayOutputStream.write(lineBoundaryEnd, 0, lineBoundaryEnd.length);
/*     */       }
/*     */     } 
/* 108 */     return byteArrayOutputStream.toByteArray();
/*     */   }
/*     */   
/*     */   public HttpRequestParameter add(String name, byte[] value) {
/* 112 */     this.parameter.put(name, value);
/* 113 */     return this;
/*     */   }
/*     */   
/*     */   public HttpRequestParameter add(String name, String value) {
/* 117 */     return add(name, value.getBytes());
/*     */   }
/*     */   
/*     */   public HttpRequestParameter add(String name, Object value) {
/* 121 */     return add(name, value.toString());
/*     */   }
/*     */   
/*     */   protected void decodeByWwwForm(InputStream inputStream) {
/* 125 */     StringBuilder stringBuilder = new StringBuilder(new String(ByteUtil.readNextLine(inputStream)));
/*     */     
/* 127 */     int index = 0;
/* 128 */     int splitIndex = 0;
/*     */     
/* 130 */     while ((index = stringBuilder.indexOf("=")) != -1) {
/* 131 */       splitIndex = stringBuilder.indexOf("&");
/* 132 */       splitIndex = (splitIndex == -1) ? stringBuilder.length() : splitIndex;
/* 133 */       String name = stringBuilder.substring(0, index);
/* 134 */       String value = stringBuilder.substring(index + 1, splitIndex);
/* 135 */       stringBuilder.delete(0, splitIndex + 1);
/* 136 */       this.parameter.put(name, value.getBytes());
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected void decodeByMultipart(InputStream inputStream) {
/* 143 */     byte[] line = null;
/* 144 */     byte[] lineBoundary = (defaultBoundary + this.boundary + "\r\n").getBytes();
/* 145 */     byte[] lineBoundaryEnd = (defaultBoundary + this.boundary + defaultBoundary + "\r\n").getBytes();
/* 146 */     boolean hasNextLine = false;
/* 147 */     String lineString = null;
/* 148 */     int index = -1;
/* 149 */     label36: while (hasNextLine || Arrays.equals(line = ByteUtil.readNextLine(inputStream, true), lineBoundary)) {
/* 150 */       String name = null;
/* 151 */       ByteArrayOutputStream value = new ByteArrayOutputStream();
/* 152 */       hasNextLine = false;
/* 153 */       line = ByteUtil.readNextLine(inputStream);
/* 154 */       lineString = new String(line);
/* 155 */       index = lineString.indexOf("name=\"") + "name=\"".length();
/* 156 */       if (index == -1) {
/*     */         break;
/*     */       }
/* 159 */       name = lineString.substring(index, lineString.indexOf("\"", index));
/* 160 */       while (!Arrays.equals(line = ByteUtil.readNextLine(inputStream, true), crlf));
/*     */       
/* 162 */       byte[] lastLine = null;
/*     */       while (true) {
/* 164 */         line = ByteUtil.readNextLine(inputStream, true);
/* 165 */         int lineSize = line.length;
/*     */         
/* 167 */         if (lineSize == lineBoundary.length && Arrays.equals(line, lineBoundary)) {
/* 168 */           if (lastLine != null) {
/* 169 */             value.write(lastLine, 0, lastLine.length - 2);
/* 170 */             lastLine = null;
/*     */           } 
/* 172 */           this.parameter.put(name, value.toByteArray());
/* 173 */           hasNextLine = true; continue label36;
/*     */         } 
/* 175 */         if (lineSize == lineBoundaryEnd.length && Arrays.equals(line, lineBoundaryEnd)) {
/* 176 */           if (lastLine != null) {
/* 177 */             value.write(lastLine, 0, lastLine.length - 2);
/* 178 */             lastLine = null;
/*     */           } 
/* 180 */           this.parameter.put(name, value.toByteArray());
/* 181 */           hasNextLine = false;
/*     */           continue label36;
/*     */         } 
/* 184 */         if (lastLine != null) {
/* 185 */           value.write(lastLine, 0, lastLine.length);
/* 186 */           lastLine = null;
/*     */         } 
/* 188 */         lastLine = line;
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static String getDefaultBoundary() {
/* 197 */     return defaultBoundary;
/*     */   }
/*     */   
/*     */   public static void setDefaultBoundary(String defaultBoundary) {
/* 201 */     HttpRequestParameter.defaultBoundary = defaultBoundary;
/*     */   }
/*     */   
/*     */   public boolean isMultipart() {
/* 205 */     return this.isMultipart;
/*     */   }
/*     */   
/*     */   public void setMultipart(boolean multipart) {
/* 209 */     this.isMultipart = multipart;
/*     */   }
/*     */   
/*     */   public String getBoundary() {
/* 213 */     return this.boundary;
/*     */   }
/*     */   
/*     */   public void setBoundary(String boundary) {
/* 217 */     this.boundary = boundary;
/*     */   }
/*     */   
/*     */   public HashMap<String, byte[]> getParameter() {
/* 221 */     return this.parameter;
/*     */   }
/*     */   
/*     */   public void setParameter(HashMap<String, byte[]> parameter) {
/* 225 */     this.parameter = parameter;
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 230 */     return "HttpRequestParameter{parameter=" + this.parameter + '}';
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\httpProxy\server\request\HttpRequestParameter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */