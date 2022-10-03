/*     */ package core.socksServer;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.net.InetSocketAddress;
/*     */ import java.net.ServerSocket;
/*     */ import java.net.Socket;
/*     */ import util.Log;
/*     */ 
/*     */ public class PortForward
/*     */   implements SocketStatus {
/*     */   private InetSocketAddress socketAddress;
/*     */   private HttpToSocks httpToSocks;
/*     */   private ServerSocket serverSocket;
/*     */   private String destHost;
/*     */   private String destPort;
/*     */   private String errMsg;
/*     */   private boolean alive;
/*     */   
/*     */   public PortForward(InetSocketAddress socketAddress, HttpToSocks httpToSocks, String destHost, String destPort) {
/*  20 */     this.socketAddress = socketAddress;
/*  21 */     this.httpToSocks = httpToSocks;
/*  22 */     this.destHost = destHost;
/*  23 */     this.destPort = destPort;
/*  24 */     this.alive = true;
/*     */     try {
/*  26 */       this.serverSocket = new ServerSocket();
/*  27 */     } catch (IOException e) {
/*  28 */       e.printStackTrace();
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String getErrorMessage() {
/*  35 */     return this.errMsg;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isActive() {
/*  40 */     return this.alive;
/*     */   }
/*     */   
/*     */   public void handle() {
/*     */     try {
/*  45 */       while (this.httpToSocks.isAlive() && this.alive) {
/*  46 */         Socket client = this.serverSocket.accept();
/*  47 */         SocksRelayInfo socksRelayInfo = new SocksRelayInfo(this.httpToSocks.socksServerConfig.clientSocketOnceReadSize.get(), this.httpToSocks.socksServerConfig.capacity.get());
/*  48 */         socksRelayInfo.setClient(client);
/*  49 */         socksRelayInfo.setDestHost(this.destHost);
/*  50 */         socksRelayInfo.setDestPort(Short.decode(this.destPort).shortValue());
/*  51 */         if (this.httpToSocks.addRelaySocket(socksRelayInfo)) {
/*  52 */           (new Thread(socksRelayInfo::startConnect)).start();
/*     */         }
/*     */       } 
/*  55 */     } catch (Exception e) {
/*  56 */       stop();
/*     */     } 
/*  58 */     stop();
/*     */   }
/*     */   
/*     */   public boolean start() {
/*     */     try {
/*  63 */       this.serverSocket.bind(this.socketAddress);
/*  64 */       (new Thread(this::handle)).start();
/*  65 */     } catch (Exception e) {
/*  66 */       this.errMsg = e.getLocalizedMessage();
/*  67 */       stop();
/*     */     } 
/*  69 */     return this.alive;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean stop() {
/*  74 */     if (this.alive) {
/*  75 */       this.alive = false;
/*  76 */       if (this.serverSocket != null) {
/*     */         try {
/*  78 */           this.serverSocket.close();
/*  79 */         } catch (IOException e) {
/*  80 */           Log.error(e);
/*     */         } 
/*     */       }
/*     */     } 
/*  84 */     return !this.alive;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public InetSocketAddress getSocketAddress() {
/*  90 */     return this.socketAddress;
/*     */   }
/*     */   
/*     */   public void setSocketAddress(InetSocketAddress socketAddress) {
/*  94 */     this.socketAddress = socketAddress;
/*     */   }
/*     */   
/*     */   public HttpToSocks getHttpToSocks() {
/*  98 */     return this.httpToSocks;
/*     */   }
/*     */   
/*     */   public void setHttpToSocks(HttpToSocks httpToSocks) {
/* 102 */     this.httpToSocks = httpToSocks;
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\core\socksServer\PortForward.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */