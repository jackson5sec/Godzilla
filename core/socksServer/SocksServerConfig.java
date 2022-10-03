/*     */ package core.socksServer;
/*     */ 
/*     */ import java.net.InetSocketAddress;
/*     */ import java.util.concurrent.atomic.AtomicInteger;
/*     */ 
/*     */ public class SocksServerConfig {
/*     */   public String bindAddress;
/*     */   public int bindPort;
/*     */   public String remoteProxyUrl;
/*     */   public String remoteKey;
/*     */   public int serverSocketOnceReadSize;
/*     */   public int serverPacketSize;
/*     */   public InetSocketAddress listenAddress;
/*     */   public AtomicInteger clientSocketOnceReadSize;
/*     */   public AtomicInteger clientPacketSize;
/*     */   public AtomicInteger requestDelay;
/*     */   public AtomicInteger requestErrRetry;
/*     */   public AtomicInteger requestErrDelay;
/*     */   public AtomicInteger capacity;
/*     */   public HttpRequestHandle requestHandle;
/*     */   
/*     */   public SocksServerConfig(String bindAddress, int bindPort) {
/*  23 */     this.bindAddress = bindAddress;
/*  24 */     this.bindPort = bindPort;
/*  25 */     this.clientSocketOnceReadSize = new AtomicInteger();
/*  26 */     this.clientPacketSize = new AtomicInteger();
/*  27 */     this.requestDelay = new AtomicInteger();
/*  28 */     this.requestErrRetry = new AtomicInteger();
/*  29 */     this.requestErrDelay = new AtomicInteger();
/*  30 */     this.capacity = new AtomicInteger();
/*     */   }
/*     */   
/*     */   public int getCapacity() {
/*  34 */     return this.capacity.get();
/*     */   }
/*     */   
/*     */   public void setCapacity(int capacity) {
/*  38 */     this.capacity.set(capacity);
/*     */   }
/*     */   
/*     */   public HttpRequestHandle getRequestHandle() {
/*  42 */     return this.requestHandle;
/*     */   }
/*     */   
/*     */   public void setRequestHandle(HttpRequestHandle requestHandle) {
/*  46 */     this.requestHandle = requestHandle;
/*     */   }
/*     */   
/*     */   public String getBindAddress() {
/*  50 */     return this.bindAddress;
/*     */   }
/*     */   
/*     */   public void setBindAddress(String bindAddress) {
/*  54 */     this.bindAddress = bindAddress;
/*  55 */     this.listenAddress = new InetSocketAddress(bindAddress, this.bindPort);
/*     */   }
/*     */   
/*     */   public int getBindPort() {
/*  59 */     return this.bindPort;
/*     */   }
/*     */   
/*     */   public void setBindPort(int bindPort) {
/*  63 */     this.bindPort = bindPort;
/*  64 */     if (this.bindAddress != null) {
/*  65 */       this.listenAddress = new InetSocketAddress(this.bindAddress, bindPort);
/*     */     }
/*     */   }
/*     */   
/*     */   public String getRemoteProxyUrl() {
/*  70 */     return this.remoteProxyUrl;
/*     */   }
/*     */   
/*     */   public void setRemoteProxyUrl(String remoteProxyUrl) {
/*  74 */     this.remoteProxyUrl = remoteProxyUrl;
/*     */   }
/*     */   
/*     */   public String getRemoteKey() {
/*  78 */     return this.remoteKey;
/*     */   }
/*     */   
/*     */   public void setRemoteKey(String remoteKey) {
/*  82 */     this.remoteKey = remoteKey;
/*     */   }
/*     */   
/*     */   public int getServerSocketOnceReadSize() {
/*  86 */     return this.serverSocketOnceReadSize;
/*     */   }
/*     */   
/*     */   public void setServerSocketOnceReadSize(int serverSocketOnceReadSize) {
/*  90 */     this.serverSocketOnceReadSize = serverSocketOnceReadSize;
/*     */   }
/*     */   
/*     */   public int getServerPacketSize() {
/*  94 */     return this.serverPacketSize;
/*     */   }
/*     */   
/*     */   public void setServerPacketSize(int serverOnceReadSize) {
/*  98 */     this.serverPacketSize = serverOnceReadSize;
/*     */   }
/*     */   
/*     */   public int getClientSocketOnceReadSize() {
/* 102 */     return this.clientSocketOnceReadSize.get();
/*     */   }
/*     */   
/*     */   public void setClientSocketOnceReadSize(int clientSocketOnceReadSize) {
/* 106 */     this.clientSocketOnceReadSize.set(clientSocketOnceReadSize);
/*     */   }
/*     */   
/*     */   public int getClientPacketSize() {
/* 110 */     return this.clientPacketSize.get();
/*     */   }
/*     */   
/*     */   public void setClientPacketSize(int clientOnceReadSize) {
/* 114 */     this.clientPacketSize.set(clientOnceReadSize);
/*     */   }
/*     */   
/*     */   public int getRequestDelay() {
/* 118 */     return this.requestDelay.get();
/*     */   }
/*     */   
/*     */   public void setRequestDelay(int requestDelay) {
/* 122 */     this.requestDelay.set(requestDelay);
/*     */   }
/*     */   
/*     */   public int getRequestErrRetry() {
/* 126 */     return this.requestErrRetry.get();
/*     */   }
/*     */   
/*     */   public void setRequestErrRetry(int requestErrRetry) {
/* 130 */     this.requestErrRetry.set(requestErrRetry);
/*     */   }
/*     */   
/*     */   public int getRequestErrDelay() {
/* 134 */     return this.requestErrDelay.get();
/*     */   }
/*     */   
/*     */   public void setRequestErrDelay(int requestErrDelay) {
/* 138 */     this.requestErrDelay.set(requestErrDelay);
/*     */   }
/*     */   
/*     */   public InetSocketAddress getListenAddress() {
/* 142 */     return this.listenAddress;
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\core\socksServer\SocksServerConfig.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */