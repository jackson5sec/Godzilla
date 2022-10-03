/*     */ package com.httpProxy.server.request;
/*     */ 
/*     */ import com.httpProxy.server.ByteUtil;
/*     */ import java.io.ByteArrayInputStream;
/*     */ import java.io.InputStream;
/*     */ import java.util.Arrays;
/*     */ 
/*     */ public class HttpRequest
/*     */ {
/*     */   private String uri;
/*     */   private String url;
/*     */   private String method;
/*     */   private String host;
/*  14 */   private int port = -1;
/*     */   private HttpRequestHeader httpRequestHeader;
/*     */   private byte[] requestData;
/*     */   private String httpVersion;
/*     */   private boolean isHttps;
/*     */   
/*     */   public HttpRequest() {
/*  21 */     this.httpRequestHeader = new HttpRequestHeader();
/*  22 */     getHttpRequestHeader().setHeader("Accept", "*/*");
/*  23 */     this.method = "GET";
/*  24 */     this.httpVersion = "HTTP/1.1";
/*     */   }
/*     */ 
/*     */   
/*     */   public String getUri() {
/*  29 */     return this.uri;
/*     */   }
/*     */   
/*     */   public HttpRequest setUri(String uri) {
/*  33 */     this.uri = uri;
/*  34 */     return this;
/*     */   }
/*     */   
/*     */   public String getUrl() {
/*  38 */     return this.url;
/*     */   }
/*     */   
/*     */   public HttpRequest setUrl(String url) {
/*  42 */     if (url.startsWith("https://")) {
/*  43 */       this.isHttps = true;
/*  44 */       this.port = 443;
/*  45 */     } else if (url.startsWith("http://")) {
/*  46 */       this.port = 80;
/*     */     } else {
/*  48 */       this.port = -1;
/*     */     } 
/*  50 */     int index = url.lastIndexOf(":");
/*  51 */     int findex = url.indexOf(":");
/*  52 */     if (index > 5) {
/*  53 */       String thost = url.substring(findex + 3, index);
/*  54 */       String tempStr = url.substring(index + 1);
/*  55 */       String tport = tempStr.substring(0, tempStr.indexOf("/"));
/*  56 */       String turi = tempStr.substring(tempStr.indexOf("/"));
/*  57 */       setHost(thost);
/*  58 */       this.port = Integer.parseInt(tport);
/*  59 */       this.uri = turi;
/*     */     } else {
/*  61 */       String temUrl = url.substring(findex + 3);
/*  62 */       String thost = temUrl.substring(0, temUrl.indexOf("/"));
/*  63 */       String tempStr = temUrl.substring(temUrl.indexOf("/"));
/*  64 */       String turi = tempStr;
/*  65 */       setHost(thost);
/*  66 */       this.uri = turi;
/*     */     } 
/*  68 */     this.url = url;
/*     */     
/*  70 */     return this;
/*     */   }
/*     */   
/*     */   public String getMethod() {
/*  74 */     return this.method;
/*     */   }
/*     */   
/*     */   public HttpRequest setMethod(String method) {
/*  78 */     this.method = method;
/*  79 */     return this;
/*     */   }
/*     */   
/*     */   public String getHost() {
/*  83 */     return this.host;
/*     */   }
/*     */   
/*     */   public HttpRequest setHost(String host) {
/*  87 */     this.host = host;
/*  88 */     getHttpRequestHeader().setHeader("Host", host);
/*  89 */     return this;
/*     */   }
/*     */   
/*     */   public int getPort() {
/*  93 */     return this.port;
/*     */   }
/*     */   
/*     */   public HttpRequest setPort(int port) {
/*  97 */     this.port = port;
/*  98 */     return this;
/*     */   }
/*     */   
/*     */   public HttpRequestHeader getHttpRequestHeader() {
/* 102 */     return this.httpRequestHeader;
/*     */   }
/*     */   
/*     */   public HttpRequest setHttpRequestHeader(HttpRequestHeader httpRequestHeader) {
/* 106 */     this.httpRequestHeader = httpRequestHeader;
/* 107 */     return this;
/*     */   }
/*     */   
/*     */   public byte[] getRequestData() {
/* 111 */     return this.requestData;
/*     */   }
/*     */   
/*     */   public HttpRequest setRequestData(byte[] requestData) {
/* 115 */     this.requestData = requestData;
/* 116 */     return this;
/*     */   }
/*     */   
/*     */   public String getHttpVersion() {
/* 120 */     return this.httpVersion;
/*     */   }
/*     */   
/*     */   public HttpRequest setHttpVersion(String httpVersion) {
/* 124 */     this.httpVersion = httpVersion;
/* 125 */     return this;
/*     */   }
/*     */   
/*     */   public boolean isHttps() {
/* 129 */     return this.isHttps;
/*     */   }
/*     */   
/*     */   public HttpRequest setHttps(boolean https) {
/* 133 */     this.isHttps = https;
/* 134 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public static HttpRequest Decode(byte[] metadata) {
/* 139 */     ByteArrayInputStream inputStream = new ByteArrayInputStream(metadata);
/* 140 */     return Decode(inputStream);
/*     */   }
/*     */   public static HttpRequest Decode(InputStream inputStream) {
/* 143 */     return Decode(inputStream, new HttpRequest());
/*     */   }
/*     */   public static HttpRequest Decode(InputStream inputStream, HttpRequest httpRequest) {
/* 146 */     httpRequest.httpRequestHeader = new HttpRequestHeader();
/* 147 */     String line = new String(ByteUtil.readNextLine(inputStream));
/* 148 */     String[] ext = line.split(" ");
/* 149 */     int index = -1;
/* 150 */     httpRequest.method = ext[0];
/* 151 */     httpRequest.uri = ext[1];
/* 152 */     httpRequest.httpVersion = ext[2];
/*     */     
/* 154 */     if ("CONNECT".equals(httpRequest.method.toUpperCase())) {
/* 155 */       httpRequest.isHttps = true;
/*     */     }
/*     */     
/* 158 */     while (!"".equals(line = new String(ByteUtil.readNextLine(inputStream)))) {
/*     */       try {
/* 160 */         index = line.indexOf(":");
/* 161 */         if (index != -1) {
/* 162 */           httpRequest.httpRequestHeader.addHeader(line.substring(0, index), line.substring(index + 1));
/*     */         }
/* 164 */       } catch (Exception e) {
/* 165 */         e.printStackTrace();
/*     */       } 
/*     */     } 
/* 168 */     httpRequest.host = httpRequest.httpRequestHeader.getHeader("Host");
/*     */     
/* 170 */     if ((index = httpRequest.host.indexOf(":")) != -1) {
/* 171 */       httpRequest.host = httpRequest.host.substring(0, index);
/*     */     }
/*     */     
/* 174 */     if (httpRequest.isHttps) {
/* 175 */       if ((index = httpRequest.uri.lastIndexOf(":")) != -1 && httpRequest.port == -1) {
/* 176 */         String tport = httpRequest.uri.substring(index + 1);
/* 177 */         index = tport.indexOf("/");
/* 178 */         if (index != -1) {
/* 179 */           httpRequest.port = Integer.parseInt(tport.substring(0, index));
/*     */         } else {
/*     */           try {
/* 182 */             httpRequest.port = Integer.parseInt(tport);
/* 183 */           } catch (Exception e) {
/* 184 */             e.printStackTrace();
/*     */           } 
/*     */         } 
/*     */       } 
/* 188 */       if (!"CONNECT".equals(httpRequest.method.toUpperCase())) {
/* 189 */         httpRequest.url = "https://" + httpRequest.host + ((httpRequest.port == 443) ? "" : (":" + String.valueOf(httpRequest.port))) + httpRequest.uri;
/*     */       } else {
/* 191 */         httpRequest.url = String.format("https://%s/", new Object[] { httpRequest.host + ((httpRequest.port == 443) ? "" : (":" + String.valueOf(httpRequest.port))) });
/*     */       } 
/*     */     } else {
/* 194 */       httpRequest.url = httpRequest.uri;
/*     */       try {
/* 196 */         index = httpRequest.url.lastIndexOf(":");
/* 197 */         if (index != -1 && httpRequest.url.indexOf(":") != index) {
/* 198 */           String tport = httpRequest.url.substring(index + 1);
/* 199 */           index = tport.indexOf("/");
/* 200 */           httpRequest.port = Integer.parseInt(tport.substring(0, index));
/*     */         } else {
/* 202 */           httpRequest.port = 80;
/*     */         } 
/* 204 */       } catch (Exception e) {
/* 205 */         e.printStackTrace();
/*     */       } 
/*     */     } 
/*     */     
/*     */     try {
/* 210 */       httpRequest.requestData = ByteUtil.readInputStream(inputStream);
/* 211 */       httpRequest.httpRequestHeader.setHeader("Content-Length", String.valueOf(httpRequest.requestData.length));
/* 212 */     } catch (Exception exception) {}
/*     */ 
/*     */ 
/*     */     
/* 216 */     httpRequest.httpRequestHeader.removeHeader("Proxy-Connection");
/*     */ 
/*     */     
/* 219 */     return httpRequest;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String toString() {
/* 225 */     return "HttpRequest{uri='" + this.uri + '\'' + ", url='" + this.url + '\'' + ", method='" + this.method + '\'' + ", host='" + this.host + '\'' + ", port=" + this.port + ", httpRequestHeader=" + this.httpRequestHeader + ", requestData=" + 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 232 */       Arrays.toString(this.requestData) + ", httpVersion='" + this.httpVersion + '\'' + ", isHttps=" + this.isHttps + '}';
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\httpProxy\server\request\HttpRequest.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */