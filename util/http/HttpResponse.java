/*     */ package util.http;
/*     */ 
/*     */ import com.httpProxy.server.response.HttpResponseHeader;
/*     */ import com.httpProxy.server.response.HttpResponseStatus;
/*     */ import core.ApplicationContext;
/*     */ import core.shell.ShellEntity;
/*     */ import core.ui.component.dialog.HttpProgressBar;
/*     */ import java.io.ByteArrayOutputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.net.HttpCookie;
/*     */ import java.net.HttpURLConnection;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
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
/*     */ public class HttpResponse
/*     */ {
/*     */   private byte[] result;
/*     */   private final ShellEntity shellEntity;
/*     */   private Map<String, List<String>> headerMap;
/*     */   private String message;
/*     */   private int responseCode;
/*     */   
/*     */   public byte[] getResult() {
/*  36 */     return this.result;
/*     */   }
/*     */   public Map<String, List<String>> getHeaderMap() {
/*  39 */     return this.headerMap;
/*     */   }
/*     */   public void setResult(byte[] result) {
/*  42 */     this.result = result;
/*     */   }
/*     */   public void setHeaderMap(Map<String, List<String>> headerMap) {
/*  45 */     this.headerMap = headerMap;
/*     */   }
/*     */   
/*     */   public HttpResponse(HttpURLConnection http, ShellEntity shellEntity) throws IOException {
/*  49 */     this.shellEntity = shellEntity;
/*  50 */     handleHeader(http.getHeaderFields());
/*  51 */     ReadAllData(getInputStream(http));
/*     */   }
/*     */   protected void handleHeader(Map<String, List<String>> map) {
/*  54 */     this.headerMap = map;
/*     */     try {
/*  56 */       this.message = ((List<String>)map.get(null)).get(0);
/*  57 */       Http http = this.shellEntity.getHttp();
/*  58 */       http.getCookieManager().put(http.getUri(), map);
/*  59 */       http.getCookieManager().getCookieStore().get(http.getUri());
/*     */       
/*  61 */       List<HttpCookie> cookies = http.getCookieManager().getCookieStore().get(http.getUri());
/*  62 */       StringBuilder sb = new StringBuilder();
/*     */       
/*  64 */       cookies.forEach(cookie -> sb.append(String.format(" %s=%s;", new Object[] { cookie.getName(), cookie.getValue() })));
/*     */       
/*  66 */       if (sb.length() > 0) {
/*  67 */         this.shellEntity.getHeaders().put("Cookie", sb.toString().trim());
/*     */       }
/*     */     }
/*  70 */     catch (IOException e) {
/*  71 */       e.printStackTrace();
/*     */     } 
/*     */   }
/*     */   
/*     */   protected InputStream getInputStream(HttpURLConnection httpURLConnection) throws IOException {
/*  76 */     InputStream inputStream = httpURLConnection.getErrorStream();
/*  77 */     if (inputStream != null) {
/*  78 */       return inputStream;
/*     */     }
/*  80 */     return httpURLConnection.getInputStream();
/*     */   }
/*     */   
/*     */   protected void ReadAllData(InputStream inputStream) throws IOException {
/*  84 */     int maxLen = 0;
/*     */     try {
/*  86 */       if (this.headerMap.get("Content-Length") != null && ((List)this.headerMap.get("Content-Length")).size() > 0) {
/*  87 */         maxLen = Integer.parseInt(((List<String>)this.headerMap.get("Content-Length")).get(0));
/*  88 */         this.result = ReadKnownNumData(inputStream, maxLen);
/*     */       } else {
/*  90 */         this.result = ReadUnknownNumData(inputStream);
/*     */       } 
/*  92 */     } catch (NumberFormatException e) {
/*  93 */       this.result = ReadUnknownNumData(inputStream);
/*     */     } 
/*  95 */     this.result = this.shellEntity.getCryptionModule().decode(this.result);
/*     */   }
/*     */ 
/*     */   
/*     */   protected byte[] ReadKnownNumData(InputStream inputStream, int num) throws IOException {
/* 100 */     if (num > 0) {
/* 101 */       byte[] temp = new byte[5120];
/* 102 */       int readOneNum = 0;
/* 103 */       int readNum = 0;
/* 104 */       ByteArrayOutputStream bos = new ByteArrayOutputStream();
/* 105 */       Boolean isShowBar = ApplicationContext.isShowHttpProgressBar.get();
/* 106 */       if (isShowBar != null && isShowBar.booleanValue()) {
/* 107 */         HttpProgressBar httpProgressBar = new HttpProgressBar("download threadId:" + Thread.currentThread().getId(), num);
/* 108 */         while ((readOneNum = inputStream.read(temp)) != -1) {
/* 109 */           bos.write(temp, 0, readOneNum);
/* 110 */           readNum += readOneNum;
/* 111 */           httpProgressBar.setValue(readNum);
/*     */         } 
/*     */       } else {
/* 114 */         while ((readOneNum = inputStream.read(temp)) != -1) {
/* 115 */           bos.write(temp, 0, readOneNum);
/*     */         }
/*     */       } 
/* 118 */       return bos.toByteArray();
/*     */     } 
/* 120 */     if (num == 0) {
/* 121 */       return ReadUnknownNumData(inputStream);
/*     */     }
/* 123 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected byte[] ReadUnknownNumData(InputStream inputStream) throws IOException {
/* 134 */     byte[] temp = new byte[5120];
/* 135 */     int readOneNum = 0;
/* 136 */     ByteArrayOutputStream bos = new ByteArrayOutputStream();
/* 137 */     while ((readOneNum = inputStream.read(temp)) != -1) {
/* 138 */       bos.write(temp, 0, readOneNum);
/*     */     }
/* 140 */     return bos.toByteArray();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public com.httpProxy.server.response.HttpResponse parseHttpResponse() {
/* 146 */     com.httpProxy.server.response.HttpResponse httpResponse = new com.httpProxy.server.response.HttpResponse(new HttpResponseStatus(this.responseCode));
/* 147 */     httpResponse.setResponseData(this.result);
/* 148 */     HttpResponseHeader responseHeader = httpResponse.getHttpResponseHeader();
/*     */     
/* 150 */     Iterator<String> headerKeys = this.headerMap.keySet().iterator();
/*     */     
/* 152 */     while (headerKeys.hasNext()) {
/* 153 */       String keyString = headerKeys.next();
/*     */       
/* 155 */       List<String> headList = this.headerMap.get(keyString);
/* 156 */       if (headList != null) {
/* 157 */         headList.parallelStream().forEach(v -> responseHeader.addHeader(keyString, v));
/*     */       }
/*     */     } 
/*     */ 
/*     */     
/* 162 */     return httpResponse;
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar\\util\http\HttpResponse.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */