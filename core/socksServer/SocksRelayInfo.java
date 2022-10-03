/*     */ package core.socksServer;
/*     */ 
/*     */ import core.EasyI18N;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.OutputStream;
/*     */ import java.net.InetSocketAddress;
/*     */ import java.net.Socket;
/*     */ import java.net.SocketTimeoutException;
/*     */ import java.util.Arrays;
/*     */ import java.util.UUID;
/*     */ import java.util.concurrent.LinkedBlockingDeque;
/*     */ import util.Log;
/*     */ import util.http.Parameter;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class SocksRelayInfo
/*     */   implements SocketStatus
/*     */ {
/*  23 */   private int packetMaxSize = 51200;
/*     */   
/*     */   private byte version;
/*     */   
/*     */   private byte command;
/*     */   
/*     */   private short destPort;
/*     */   private String destHost;
/*     */   private String bindHost;
/*     */   private short bindPort;
/*     */   private String userId;
/*     */   private Socket client;
/*     */   private boolean alive;
/*     */   private final LinkedBlockingDeque<Parameter> readtaskList;
/*     */   private final LinkedBlockingDeque<Parameter> writetaskList;
/*     */   private String socketId;
/*     */   private boolean isBind;
/*     */   public byte[] connectSuccessMessage;
/*     */   private String errMsg;
/*     */   
/*     */   public SocksRelayInfo(int packetMaxSize, int capacity) {
/*  44 */     this.packetMaxSize = packetMaxSize;
/*  45 */     this.alive = true;
/*  46 */     this.userId = "null";
/*  47 */     this.readtaskList = new LinkedBlockingDeque<>(capacity);
/*  48 */     this.writetaskList = new LinkedBlockingDeque<>();
/*  49 */     this.socketId = UUID.randomUUID().toString().replace("-", "");
/*     */   }
/*     */   
/*     */   public byte getVersion() {
/*  53 */     return this.version;
/*     */   }
/*     */   
/*     */   public void setVersion(byte version) {
/*  57 */     this.version = version;
/*     */   }
/*     */   
/*     */   public byte getCommand() {
/*  61 */     return this.command;
/*     */   }
/*     */   
/*     */   public void setCommand(byte command) {
/*  65 */     this.command = command;
/*     */   }
/*     */   
/*     */   public short getDestPort() {
/*  69 */     return this.destPort;
/*     */   }
/*     */   
/*     */   public void setDestPort(short destPort) {
/*  73 */     this.destPort = destPort;
/*     */   }
/*     */   
/*     */   public String getDestHost() {
/*  77 */     return this.destHost;
/*     */   }
/*     */   
/*     */   public void setDestHost(String destHost) {
/*  81 */     this.destHost = destHost;
/*     */   }
/*     */   
/*     */   public String getBindHost() {
/*  85 */     return this.bindHost;
/*     */   }
/*     */   
/*     */   public void setBindHost(String bindHost) {
/*  89 */     this.bindHost = bindHost;
/*     */   }
/*     */   
/*     */   public short getBindPort() {
/*  93 */     return this.bindPort;
/*     */   }
/*     */   
/*     */   public void setBindPort(short bindPort) {
/*  97 */     this.bindPort = bindPort;
/*     */   }
/*     */   
/*     */   public String getUSERID() {
/* 101 */     return this.userId;
/*     */   }
/*     */   
/*     */   public void setUSERID(String USERID) {
/* 105 */     this.userId = USERID;
/*     */   }
/*     */ 
/*     */   
/*     */   public Socket getRelaySocket() {
/* 110 */     return this.client;
/*     */   }
/*     */   public Socket getSocket() {
/* 113 */     return this.client;
/*     */   }
/*     */   
/*     */   public Socket getClient() {
/* 117 */     return this.client;
/*     */   }
/*     */   
/*     */   public void setClient(Socket client) {
/* 121 */     this.client = client;
/*     */   }
/*     */   
/*     */   public boolean isAlive() {
/* 125 */     return this.alive;
/*     */   }
/*     */   
/*     */   public int getReadTaskSize() {
/* 129 */     return this.readtaskList.size();
/*     */   }
/*     */   
/*     */   public void setAlive(boolean alive) {
/* 133 */     this.alive = alive;
/*     */   }
/*     */   
/*     */   public Parameter ReadTaskData(boolean remove) {
/*     */     try {
/* 138 */       if (remove) {
/* 139 */         return this.readtaskList.poll();
/*     */       }
/* 141 */       return this.readtaskList.getFirst();
/*     */     }
/* 143 */     catch (Exception e) {
/* 144 */       return null;
/*     */     } 
/*     */   }
/*     */   
/*     */   public void pushReadTask(Parameter reqParameter) {
/*     */     try {
/* 150 */       this.readtaskList.put(reqParameter);
/* 151 */     } catch (Exception e) {
/* 152 */       e.printStackTrace();
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void pushWriteTask(Parameter resParameter) {
/* 158 */     if (!this.alive && resParameter.getParameterByteArray("type")[0] == 3) {
/*     */       return;
/*     */     }
/*     */     
/* 162 */     this.writetaskList.add(resParameter);
/*     */   }
/*     */   
/*     */   public boolean startBind(String bindHost, String bindPort) {
/* 166 */     Thread.currentThread().setName(Thread.currentThread().getStackTrace()[1].getMethodName());
/* 167 */     this.isBind = true;
/* 168 */     Parameter reqParameter = HttpToSocks.createParameter((byte)6);
/* 169 */     reqParameter.add("socketId", getSocketId());
/* 170 */     reqParameter.add("host", bindHost);
/* 171 */     reqParameter.add("port", bindPort);
/* 172 */     pushReadTask(reqParameter);
/*     */     try {
/* 174 */       Parameter resParameter = this.writetaskList.take();
/* 175 */       byte type = resParameter.getParameterByteArray("type")[0];
/* 176 */       switch (type) {
/*     */         case 5:
/* 178 */           Log.log("创建套接字绑定成功", new Object[0]);
/* 179 */           return true;
/*     */       } 
/* 181 */       this.errMsg = resParameter.getParameterString("errMsg");
/* 182 */       Log.error(String.format(EasyI18N.getI18nString("创建套接字绑定失败 destHost:%s destPort:%s errmsg:%s"), new Object[] { this.destHost, Short.valueOf(this.destPort), this.errMsg }));
/* 183 */       close();
/* 184 */       return false;
/*     */     }
/* 186 */     catch (InterruptedException e) {
/* 187 */       e.printStackTrace();
/*     */       
/* 189 */       close();
/* 190 */       return false;
/*     */     } 
/*     */   }
/*     */   public void bindSocketServerOpenSocket() {
/* 194 */     Socket socket = new Socket();
/*     */     try {
/* 196 */       socket.connect(new InetSocketAddress(getDestHost(), getDestPort()));
/* 197 */       Parameter reqParameter = HttpToSocks.createParameter((byte)5);
/* 198 */       reqParameter.add("socketId", getSocketId());
/* 199 */       setClient(socket);
/* 200 */       pushReadTask(reqParameter);
/* 201 */       start();
/* 202 */     } catch (Exception e) {
/* 203 */       close();
/* 204 */       Log.error(e);
/* 205 */       Log.error(String.format(EasyI18N.getI18nString("连接socket失败 socketId:%s domain:%s port:%s"), new Object[] { getSocketId(), getDestHost(), Short.valueOf(getDestPort()) }));
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void startConnect() {
/* 211 */     Thread.currentThread().setName(Thread.currentThread().getStackTrace()[1].getMethodName());
/* 212 */     Parameter openSocketReqParameter = HttpToSocks.createParameter((byte)1);
/* 213 */     openSocketReqParameter.add("host", getDestHost());
/* 214 */     openSocketReqParameter.add("port", String.valueOf(getDestPort()));
/* 215 */     openSocketReqParameter.add("socketId", getSocketId());
/* 216 */     pushReadTask(openSocketReqParameter);
/*     */     try {
/* 218 */       Parameter resParameter = this.writetaskList.take();
/* 219 */       byte type = resParameter.getParameterByteArray("type")[0];
/* 220 */       switch (type) {
/*     */         case 5:
/* 222 */           if (this.connectSuccessMessage != null) {
/* 223 */             pushWriteTask((new Parameter()).addParameterByteArray("type", new byte[] { 3 }).addParameterByteArray("data", this.connectSuccessMessage));
/* 224 */             this.connectSuccessMessage = null;
/*     */           } 
/* 226 */           start();
/*     */           return;
/*     */       } 
/* 229 */       this.errMsg = resParameter.getParameterString("errMsg");
/* 230 */       Log.error(String.format(EasyI18N.getI18nString("创建套接字失败 destHost:%s destPort:%s errmsg:%s"), new Object[] { this.destHost, Short.valueOf(this.destPort), this.errMsg }));
/* 231 */       close();
/*     */       
/*     */       return;
/* 234 */     } catch (InterruptedException e) {
/* 235 */       e.printStackTrace();
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 241 */     return "SocksRelayInfo{Version=" + this.version + ", Command=" + this.command + ", DestPort=" + this.destPort + ", DestHost=" + this.destHost + ", USERID='" + this.userId + '\'' + '}';
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getErrorMessage() {
/* 253 */     return this.errMsg;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isActive() {
/* 258 */     return this.alive;
/*     */   }
/*     */   
/*     */   public boolean start() {
/* 262 */     (new Thread(this::_startRead)).start();
/* 263 */     (new Thread(this::_startWrite)).start();
/* 264 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean stop() {
/* 269 */     close();
/* 270 */     return !this.alive;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getPacketMaxSize() {
/* 275 */     return this.packetMaxSize;
/*     */   }
/*     */   
/*     */   public void setPacketMaxSize(int packetMaxSize) {
/* 279 */     this.packetMaxSize = packetMaxSize;
/*     */   }
/*     */   
/*     */   private void _startRead() {
/* 283 */     Thread.currentThread().setName(hashCode() + Thread.currentThread().getStackTrace()[1].getMethodName());
/* 284 */     byte[] buff = new byte[this.packetMaxSize];
/*     */     try {
/* 286 */       InputStream inputStream = getRelaySocket().getInputStream();
/* 287 */       while (this.alive && !this.client.isClosed()) {
/*     */         try {
/* 289 */           int readNum = inputStream.read(buff);
/* 290 */           if (readNum > 0) {
/* 291 */             Parameter reqParameter = new Parameter();
/* 292 */             reqParameter.add("type", new byte[] { 3 });
/* 293 */             reqParameter.add("socketId", getSocketId().getBytes());
/* 294 */             reqParameter.add("data", Arrays.copyOfRange(buff, 0, readNum));
/* 295 */             pushReadTask(reqParameter);
/*     */             continue;
/*     */           } 
/* 298 */           close();
/*     */           
/*     */           return;
/* 301 */         } catch (IOException e) {
/* 302 */           if (SocketTimeoutException.class.isAssignableFrom(e.getClass())) {
/*     */             continue;
/*     */           }
/* 305 */           close();
/*     */           return;
/*     */         } 
/*     */       } 
/* 309 */     } catch (IOException e) {
/* 310 */       e.printStackTrace();
/*     */     } 
/* 312 */     close();
/*     */   }
/*     */   
/*     */   private void _startWrite() {
/* 316 */     Thread.currentThread().setName(hashCode() + Thread.currentThread().getStackTrace()[1].getMethodName());
/*     */     try {
/* 318 */       OutputStream outputStream = getRelaySocket().getOutputStream();
/* 319 */       while (this.alive && !this.client.isClosed()) {
/*     */         try {
/* 321 */           Parameter resParameter = this.writetaskList.take();
/* 322 */           byte[] typeArr = resParameter.getParameterByteArray("type");
/* 323 */           byte[] data = resParameter.getParameterByteArray("data");
/* 324 */           if (typeArr != null && typeArr.length > 0) {
/* 325 */             switch (typeArr[0]) {
/*     */               case 3:
/* 327 */                 if (!getRelaySocket().isClosed()) {
/* 328 */                   outputStream.write(data);
/*     */                   continue;
/*     */                 } 
/*     */                 return;
/*     */               
/*     */               case 2:
/* 334 */                 close();
/*     */                 return;
/*     */             } 
/*     */ 
/*     */ 
/*     */           
/*     */           }
/* 341 */         } catch (InterruptedException e) {
/* 342 */           e.printStackTrace();
/*     */         } 
/*     */       } 
/* 345 */     } catch (IOException e) {
/* 346 */       e.printStackTrace();
/*     */     } 
/* 348 */     close();
/*     */   }
/*     */ 
/*     */   
/*     */   public void tryWrite(byte[] data) {
/* 353 */     if (this.alive) {
/*     */       try {
/* 355 */         getRelaySocket().getOutputStream().write(data);
/* 356 */       } catch (Exception e) {
/* 357 */         close();
/*     */       } 
/*     */     }
/*     */   }
/*     */   
/*     */   public synchronized void close() {
/* 363 */     if (!this.alive) {
/*     */       return;
/*     */     }
/*     */     try {
/* 367 */       if (this.client != null && !this.client.isClosed()) {
/* 368 */         this.client.close();
/*     */       }
/* 370 */     } catch (IOException e) {
/* 371 */       e.printStackTrace();
/*     */     } 
/* 373 */     this.alive = false;
/* 374 */     Parameter reqParameter = this.readtaskList.peekLast();
/* 375 */     if (reqParameter != null) {
/* 376 */       if (reqParameter.getParameterByteArray("type")[0] != 2) {
/* 377 */         reqParameter = new Parameter();
/* 378 */         reqParameter.add("type", new byte[] { 2 });
/* 379 */         reqParameter.add("socketId", getSocketId().getBytes());
/* 380 */         pushReadTask(reqParameter);
/* 381 */         this.writetaskList.add(reqParameter);
/*     */       } 
/*     */     } else {
/* 384 */       reqParameter = new Parameter();
/* 385 */       reqParameter.add("type", new byte[] { 2 });
/* 386 */       reqParameter.add("socketId", getSocketId().getBytes());
/* 387 */       pushReadTask(reqParameter);
/* 388 */       this.writetaskList.add(reqParameter);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public String getSocketId() {
/* 394 */     return this.socketId;
/*     */   }
/*     */   
/*     */   public void setSocketId(String socketId) {
/* 398 */     this.socketId = socketId;
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\core\socksServer\SocksRelayInfo.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */