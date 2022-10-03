/*     */ package util.http;
/*     */ import core.ApplicationContext;
/*     */ import core.shell.ShellEntity;
/*     */ import java.io.ByteArrayInputStream;
/*     */ import java.net.CookieManager;
/*     */ import java.net.HttpCookie;
/*     */ import java.net.HttpURLConnection;
/*     */ import java.net.Proxy;
/*     */ import java.net.Socket;
/*     */ import java.net.URI;
/*     */ import java.net.URL;
/*     */ import java.security.cert.CertificateException;
/*     */ import java.security.cert.X509Certificate;
/*     */ import java.util.Iterator;
/*     */ import java.util.Map;
/*     */ import javax.net.ssl.HttpsURLConnection;
/*     */ import javax.net.ssl.SSLContext;
/*     */ import javax.net.ssl.TrustManager;
/*     */ 
/*     */ public class Http {
/*  21 */   private static final HostnameVerifier hostnameVerifier = new TrustAnyHostnameVerifier();
/*     */   
/*     */   private final Proxy proxy;
/*     */   private final ShellEntity shellContext;
/*     */   private CookieManager cookieManager;
/*     */   private URI uri;
/*  27 */   public String requestMethod = "POST";
/*     */   
/*     */   public Http(ShellEntity shellContext) {
/*  30 */     this.shellContext = shellContext;
/*  31 */     this.proxy = ApplicationContext.getProxy(this.shellContext);
/*     */   }
/*     */   
/*     */   static {
/*  35 */     trustAllHttpsCertificates();
/*     */   }
/*     */   public HttpResponse SendHttpConn(String urlString, String method, Map<String, String> header, byte[] requestData, int connTimeOut, int readTimeOut, Proxy proxy) {
/*     */     try {
/*  39 */       URL url = new URL(urlString);
/*     */       
/*  41 */       HttpURLConnection httpConn = (HttpURLConnection)url.openConnection(proxy);
/*  42 */       httpConn.setFixedLengthStreamingMode(requestData.length);
/*  43 */       if (httpConn instanceof HttpsURLConnection) {
/*  44 */         ((HttpsURLConnection)httpConn).setHostnameVerifier(hostnameVerifier);
/*     */       }
/*  46 */       httpConn.setDoInput(true);
/*  47 */       httpConn.setDoOutput(!"GET".equalsIgnoreCase(method));
/*  48 */       if (connTimeOut > 0) httpConn.setConnectTimeout(connTimeOut); 
/*  49 */       if (readTimeOut > 0) httpConn.setReadTimeout(readTimeOut); 
/*  50 */       httpConn.setRequestMethod(method.toUpperCase());
/*  51 */       String ua = ShellSuperRequest.randomUa();
/*  52 */       if (ua != null && ua.trim().length() > 0) {
/*  53 */         httpConn.setRequestProperty("User-Agent", ua.trim());
/*     */       }
/*  55 */       addHttpHeader(httpConn, ApplicationContext.getGloballHttpHeaderX());
/*  56 */       addHttpHeader(httpConn, header);
/*  57 */       Boolean isShowBar = ApplicationContext.isShowHttpProgressBar.get();
/*  58 */       if (isShowBar != null && isShowBar.booleanValue()) {
/*  59 */         if (httpConn.getDoOutput()) {
/*  60 */           OutputStream outputStream = httpConn.getOutputStream();
/*  61 */           ByteArrayInputStream arrayInputStream = new ByteArrayInputStream(requestData);
/*  62 */           byte[] req = new byte[1024];
/*  63 */           int readOneNum = -1;
/*  64 */           int readNum = 0;
/*  65 */           while ((readOneNum = arrayInputStream.read(req)) != -1) {
/*  66 */             outputStream.write(req, 0, readOneNum);
/*  67 */             readNum += readOneNum;
/*     */           }
/*     */         
/*     */         } 
/*  71 */       } else if (httpConn.getDoOutput()) {
/*  72 */         httpConn.getOutputStream().write(requestData);
/*  73 */         httpConn.getOutputStream().flush();
/*     */       } 
/*     */       
/*  76 */       return new HttpResponse(httpConn, this.shellContext);
/*     */     }
/*  78 */     catch (Exception e) {
/*  79 */       Log.error(e);
/*  80 */       return null;
/*     */     } 
/*     */   }
/*     */   public HttpResponse sendHttpResponse(Map<String, String> header, byte[] requestData, int connTimeOut, int readTimeOut) {
/*  84 */     requestData = this.shellContext.getCryptionModule().encode(requestData);
/*     */     
/*  86 */     String left = this.shellContext.getReqLeft();
/*  87 */     String right = this.shellContext.getReqRight();
/*  88 */     if (this.shellContext.isSendLRReqData()) {
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
/* 104 */       byte[] leftData = left.getBytes();
/* 105 */       byte[] rightData = right.getBytes();
/* 106 */       requestData = (byte[])functions.concatArrays(functions.concatArrays(leftData, 0, ((leftData.length > 0) ? leftData.length : 1) - 1, requestData, 0, requestData.length - 1), 0, leftData.length + requestData.length - 1, rightData, 0, ((rightData.length > 0) ? rightData.length : 1) - 1);
/*     */     } 
/*     */     
/* 109 */     return SendHttpConn(this.shellContext.getUrl(), this.requestMethod, header, requestData, connTimeOut, readTimeOut, this.proxy);
/*     */   }
/*     */   public HttpResponse sendHttpResponse(byte[] requestData, int connTimeOut, int readTimeOut) {
/* 112 */     return sendHttpResponse(this.shellContext.getHeaders(), requestData, connTimeOut, readTimeOut);
/*     */   }
/*     */   public HttpResponse sendHttpResponse(byte[] requestData) {
/* 115 */     return sendHttpResponse(requestData, this.shellContext.getConnTimeout(), this.shellContext.getReadTimeout());
/*     */   }
/*     */   public static void addHttpHeader(HttpURLConnection connection, Map<String, String> headerMap) {
/* 118 */     if (headerMap != null) {
/* 119 */       Iterator<String> names = headerMap.keySet().iterator();
/* 120 */       String name = "";
/* 121 */       while (names.hasNext()) {
/* 122 */         name = names.next();
/* 123 */         connection.setRequestProperty(name, headerMap.get(name));
/*     */       } 
/*     */     } 
/*     */   }
/*     */   private static void trustAllHttpsCertificates() {
/*     */     try {
/* 129 */       TrustManager[] trustAllCerts = new TrustManager[1];
/* 130 */       TrustManager tm = new miTM();
/* 131 */       trustAllCerts[0] = tm;
/* 132 */       SSLContext sc = SSLContext.getInstance("SSL");
/* 133 */       sc.init(null, trustAllCerts, new SecureRandom());
/* 134 */       HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
/* 135 */       SSLContext sc2 = SSLContext.getInstance("TLS");
/* 136 */       sc2.init(null, trustAllCerts, new SecureRandom());
/* 137 */       HttpsURLConnection.setDefaultSSLSocketFactory(sc2.getSocketFactory());
/* 138 */     } catch (Exception e) {
/* 139 */       e.printStackTrace();
/*     */     } 
/*     */   }
/*     */   public synchronized URI getUri() {
/* 143 */     if (this.uri == null) {
/*     */       try {
/* 145 */         this.uri = URI.create(this.shellContext.getUrl());
/* 146 */       } catch (Exception e) {
/* 147 */         e.printStackTrace();
/*     */       } 
/*     */     }
/* 150 */     return this.uri;
/*     */   }
/*     */   
/*     */   public synchronized CookieManager getCookieManager() {
/* 154 */     if (this.cookieManager == null) {
/* 155 */       this.cookieManager = new CookieManager();
/*     */       try {
/* 157 */         String cookieStr = (String)this.shellContext.getHeaders().get("Cookie");
/* 158 */         if (cookieStr == null) {
/* 159 */           cookieStr = (String)this.shellContext.getHeaders().get("cookie");
/*     */         }
/* 161 */         if (cookieStr != null) {
/* 162 */           String[] cookies = cookieStr.split(";");
/* 163 */           for (String cookieStr2 : cookies) {
/* 164 */             String[] cookieAtt = cookieStr2.split("=");
/* 165 */             if (cookieAtt.length == 2) {
/* 166 */               HttpCookie httpCookie = new HttpCookie(cookieAtt[0], cookieAtt[1]);
/* 167 */               this.cookieManager.getCookieStore().add(getUri(), httpCookie);
/*     */             } 
/*     */           } 
/*     */         } 
/* 171 */       } catch (Exception e) {
/* 172 */         e.printStackTrace();
/*     */       } 
/*     */     } 
/* 175 */     return this.cookieManager;
/*     */   }
/*     */   private static class miTM extends X509ExtendedTrustManager implements TrustManager, X509TrustManager { private miTM() {}
/*     */     
/*     */     public X509Certificate[] getAcceptedIssuers() {
/* 180 */       return null;
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean isServerTrusted(X509Certificate[] certs) {
/* 185 */       return true;
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean isClientTrusted(X509Certificate[] certs) {
/* 190 */       return true;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public void checkServerTrusted(X509Certificate[] certs, String authType) throws CertificateException {}
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public void checkClientTrusted(X509Certificate[] certs, String authType) throws CertificateException {}
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public void checkClientTrusted(X509Certificate[] x509Certificates, String s, Socket socket) throws CertificateException {}
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public void checkServerTrusted(X509Certificate[] x509Certificates, String s, Socket socket) throws CertificateException {}
/*     */ 
/*     */ 
/*     */     
/*     */     public void checkClientTrusted(X509Certificate[] x509Certificates, String s, SSLEngine sslEngine) throws CertificateException {}
/*     */ 
/*     */ 
/*     */     
/*     */     public void checkServerTrusted(X509Certificate[] x509Certificates, String s, SSLEngine sslEngine) throws CertificateException {} }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static class TrustAnyHostnameVerifier
/*     */     implements HostnameVerifier
/*     */   {
/*     */     public boolean verify(String hostname, SSLSession session) {
/* 228 */       return true;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar\\util\http\Http.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */