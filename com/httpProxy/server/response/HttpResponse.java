/*     */ package com.httpProxy.server.response;
/*     */ 
/*     */ import com.httpProxy.server.ByteUtil;
/*     */ import java.io.ByteArrayInputStream;
/*     */ import java.io.ByteArrayOutputStream;
/*     */ import java.io.InputStream;
/*     */ import java.util.Arrays;
/*     */ import java.util.Iterator;
/*     */ 
/*     */ public class HttpResponse
/*     */ {
/*     */   private HttpResponseStatus httpResponseStatus;
/*     */   private HttpResponseHeader httpResponseHeader;
/*     */   private byte[] responseData;
/*     */   
/*     */   public HttpResponse(HttpResponseStatus httpResponseStatus, HttpResponseHeader httpResponseHeader, byte[] responseData) {
/*  17 */     this.httpResponseStatus = httpResponseStatus;
/*  18 */     this.httpResponseHeader = httpResponseHeader;
/*  19 */     this.responseData = responseData;
/*     */   }
/*     */   
/*     */   public HttpResponse(HttpResponseStatus httpResponseStatus, HttpResponseHeader httpResponseHeader, String responseData) {
/*  23 */     this(httpResponseStatus, httpResponseHeader, responseData.getBytes());
/*     */   }
/*     */   public HttpResponse(HttpResponseStatus httpResponseStatus) {
/*  26 */     this(httpResponseStatus, new HttpResponseHeader(), new byte[0]);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public HttpResponseStatus getHttpResponseStatus() {
/*  32 */     return this.httpResponseStatus;
/*     */   }
/*     */   
/*     */   public void setHttpResponseStatus(HttpResponseStatus httpResponseStatus) {
/*  36 */     this.httpResponseStatus = httpResponseStatus;
/*     */   }
/*     */   
/*     */   public HttpResponseHeader getHttpResponseHeader() {
/*  40 */     return this.httpResponseHeader;
/*     */   }
/*     */   
/*     */   public void setHttpResponseHeader(HttpResponseHeader httpResponseHeader) {
/*  44 */     this.httpResponseHeader = httpResponseHeader;
/*     */   }
/*     */   
/*     */   public byte[] getResponseData() {
/*  48 */     return this.responseData;
/*     */   }
/*     */   
/*     */   public void setResponseData(byte[] responseData) {
/*  52 */     this.responseData = responseData;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public byte[] encode() {
/*  58 */     ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
/*  59 */     byte[] ex = "\r\n".getBytes();
/*     */     try {
/*  61 */       outputStream.write("HTTP/1.1".getBytes());
/*  62 */       outputStream.write(32);
/*  63 */       outputStream.write(String.valueOf(this.httpResponseStatus.code()).getBytes());
/*  64 */       outputStream.write(32);
/*  65 */       outputStream.write(this.httpResponseStatus.getReasonPhrase().getBytes());
/*  66 */       outputStream.write(ex);
/*     */       
/*  68 */       if (this.httpResponseHeader == null) {
/*  69 */         this.httpResponseHeader = new HttpResponseHeader();
/*     */       }
/*     */       
/*  72 */       if (this.responseData != null) {
/*  73 */         this.httpResponseHeader.setHeader("Content-Length", String.valueOf(this.responseData.length));
/*     */       }
/*  75 */       if (this.httpResponseHeader != null) {
/*  76 */         Iterator<String[]> headers = (Iterator)this.httpResponseHeader.getHeaders().iterator();
/*  77 */         String[] kv = null;
/*  78 */         while (headers.hasNext()) {
/*  79 */           kv = headers.next();
/*  80 */           outputStream.write(kv[0].getBytes());
/*  81 */           outputStream.write(58);
/*  82 */           outputStream.write(32);
/*  83 */           outputStream.write(kv[1].getBytes());
/*  84 */           outputStream.write(ex);
/*     */         } 
/*     */       } 
/*  87 */       outputStream.write(ex);
/*  88 */       if (this.responseData != null) {
/*  89 */         outputStream.write(this.responseData);
/*     */       }
/*     */     }
/*  92 */     catch (Exception e) {
/*  93 */       throw new RuntimeException(e);
/*     */     } 
/*  95 */     return outputStream.toByteArray();
/*     */   }
/*     */   
/*     */   public static HttpResponse decode(byte[] responseData) {
/*  99 */     ByteArrayInputStream inputStream = new ByteArrayInputStream(responseData);
/* 100 */     return decode(inputStream);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static HttpResponse decode(InputStream inputStream) {
/* 106 */     HttpResponse httpResponse = new HttpResponse(null);
/* 107 */     String line = new String(ByteUtil.readNextLine(inputStream));
/* 108 */     int index = line.indexOf(" ");
/* 109 */     line = line.substring(index + 1);
/* 110 */     index = line.indexOf(" ");
/* 111 */     httpResponse.httpResponseStatus = new HttpResponseStatus(Integer.parseInt(line.substring(0, index)), line.substring(index + 1));
/* 112 */     httpResponse.httpResponseHeader = new HttpResponseHeader();
/* 113 */     String[] ext = null;
/* 114 */     while (!"".equals(line = new String(ByteUtil.readNextLine(inputStream)))) {
/*     */       try {
/* 116 */         index = line.indexOf(":");
/* 117 */         if (index != -1) {
/* 118 */           httpResponse.httpResponseHeader.addHeader(line.substring(0, index), line.substring(index + 1));
/*     */         }
/* 120 */       } catch (Exception e) {
/* 121 */         e.printStackTrace();
/*     */       } 
/*     */     } 
/*     */     try {
/* 125 */       httpResponse.responseData = ByteUtil.readInputStream(inputStream);
/* 126 */       httpResponse.httpResponseHeader.setHeader("Content-Length", String.valueOf(httpResponse.responseData.length));
/* 127 */     } catch (Exception exception) {}
/*     */ 
/*     */ 
/*     */     
/* 131 */     return httpResponse;
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 136 */     return "HttpResponse{httpResponseStatus=" + this.httpResponseStatus + ", httpResponseHeader=" + this.httpResponseHeader + ", responseData=" + 
/*     */ 
/*     */       
/* 139 */       Arrays.toString(this.responseData) + '}';
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\httpProxy\server\response\HttpResponse.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */