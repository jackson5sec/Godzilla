/*     */ package com.httpProxy.server.core;
/*     */ 
/*     */ import com.httpProxy.server.CertPool;
/*     */ import com.httpProxy.server.request.HttpRequest;
/*     */ import com.httpProxy.server.response.HttpResponse;
/*     */ import com.httpProxy.server.response.HttpResponseStatus;
/*     */ import java.io.ByteArrayOutputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.PrintStream;
/*     */ import java.net.InetAddress;
/*     */ import java.net.ServerSocket;
/*     */ import java.net.Socket;
/*     */ import javax.net.ssl.SSLSocket;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class HttpProxyServer
/*     */ {
/*     */   private int listenPort;
/*     */   private int backlog;
/*     */   private InetAddress bindAddr;
/*     */   private boolean nextSocket = true;
/*     */   private CertPool certPool;
/*     */   private ServerSocket serverSocket;
/*     */   private HttpProxyHandle proxyHandle;
/*     */   
/*     */   public HttpProxyServer(int listenPort, int backlog, InetAddress bindAddr, CertPool certPool, HttpProxyHandle proxyHandle) {
/*  34 */     this.listenPort = listenPort;
/*  35 */     this.backlog = backlog;
/*  36 */     this.bindAddr = bindAddr;
/*  37 */     this.certPool = certPool;
/*  38 */     this.proxyHandle = proxyHandle;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean startup() {
/*     */     try {
/*  45 */       this.serverSocket = new ServerSocket(this.listenPort, this.backlog, this.bindAddr);
/*     */     
/*     */     }
/*  48 */     catch (Exception e) {
/*  49 */       e.printStackTrace();
/*  50 */       return false;
/*     */     } 
/*     */     
/*  53 */     return acceptService(this.serverSocket);
/*     */   }
/*     */   public void shutdown() {
/*     */     try {
/*  57 */       this.serverSocket.close();
/*  58 */     } catch (IOException e) {
/*  59 */       throw new RuntimeException(e);
/*     */     } 
/*     */   }
/*     */   
/*     */   public void handler(Socket socket, HttpRequest httpRequest) throws Exception {
/*     */     try {
/*  65 */       if (this.proxyHandle != null) {
/*  66 */         this.proxyHandle.handler(socket, httpRequest);
/*     */       } else {
/*  68 */         HttpResponse httpResponse = new HttpResponse(HttpResponseStatus.INTERNAL_SERVER_ERROR, null, "No Input HttpProxyHandle");
/*  69 */         socket.getOutputStream().write(httpResponse.encode());
/*     */       } 
/*  71 */     } catch (Exception e) {
/*  72 */       ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
/*  73 */       PrintStream printStream = new PrintStream(byteArrayOutputStream);
/*  74 */       e.printStackTrace(printStream);
/*  75 */       printStream.flush();
/*  76 */       printStream.close();
/*  77 */       HttpResponse httpResponse = new HttpResponse(HttpResponseStatus.INTERNAL_SERVER_ERROR, null, byteArrayOutputStream.toByteArray());
/*  78 */       byteArrayOutputStream.close();
/*  79 */       socket.getOutputStream().write(httpResponse.encode());
/*     */     } 
/*     */ 
/*     */     
/*  83 */     closeSocket(socket);
/*     */   }
/*     */   
/*     */   private boolean acceptService(ServerSocket sslServerSocket) {
/*  87 */     (new Thread(() -> {
/*     */           while (this.nextSocket) {
/*     */             Socket sslSocket;
/*     */             
/*     */             try {
/*     */               sslSocket = sslServerSocket.accept();
/*  93 */             } catch (IOException e) {
/*     */               return;
/*     */             } 
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
/*     */             (new Thread(())).start();
/*     */           } 
/* 122 */         })).start();
/* 123 */     return true;
/*     */   }
/*     */   protected void closeSocket(Socket socket) {
/* 126 */     if (socket == null && !socket.isClosed()) {
/*     */       return;
/*     */     }
/*     */     try {
/* 130 */       if (!socket.isClosed()) {
/* 131 */         socket.close();
/*     */       }
/* 133 */     } catch (IOException iOException) {}
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public HttpProxyHandle getProxyHandle() {
/* 139 */     return this.proxyHandle;
/*     */   }
/*     */   
/*     */   public void setProxyHandle(HttpProxyHandle proxyHandle) {
/* 143 */     this.proxyHandle = proxyHandle;
/*     */   }
/*     */   
/*     */   public boolean isNextSocket() {
/* 147 */     return this.nextSocket;
/*     */   }
/*     */   
/*     */   public void setNextSocket(boolean nextSocket) {
/* 151 */     this.nextSocket = nextSocket;
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\httpProxy\server\core\HttpProxyServer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */