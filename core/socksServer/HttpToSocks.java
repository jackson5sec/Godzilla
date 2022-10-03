/*     */ package core.socksServer;
/*     */ 
/*     */ import com.httpProxy.server.request.HttpRequest;
/*     */ import com.httpProxy.server.response.HttpResponse;
/*     */ import core.EasyI18N;
/*     */ import java.net.Socket;
/*     */ import java.nio.ByteBuffer;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Map;
/*     */ import java.util.concurrent.ConcurrentHashMap;
/*     */ import javax.crypto.Cipher;
/*     */ import javax.crypto.spec.IvParameterSpec;
/*     */ import javax.crypto.spec.SecretKeySpec;
/*     */ import util.Log;
/*     */ import util.ParameterInputStream;
/*     */ import util.ParameterOutputStream;
/*     */ import util.functions;
/*     */ import util.http.Parameter;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class HttpToSocks
/*     */ {
/*  26 */   Map<String, SocksRelayInfo> session = new ConcurrentHashMap<>(50);
/*     */   
/*     */   String[] keys;
/*     */   int keyIndex;
/*     */   byte[] sessionKey;
/*  31 */   long accessNum = 1L;
/*     */   
/*     */   SocksServer socksServer;
/*     */   
/*     */   boolean alive;
/*     */   String sessionId;
/*     */   long summaryUploadBytes;
/*     */   long summaryDownloadBytes;
/*     */   long tmpUploadBytes;
/*     */   long requestSuccessNum;
/*     */   long requestFailureNum;
/*     */   long startSocksTime;
/*     */   SocksServerConfig socksServerConfig;
/*     */   
/*     */   public HttpToSocks(SocksServerConfig socksServerConfig) {
/*  46 */     this.socksServerConfig = socksServerConfig;
/*  47 */     this.alive = false;
/*     */   }
/*     */ 
/*     */   
/*     */   public static void main(String[] args) {
/*  52 */     SocksServerConfig socksServerConfig = new SocksServerConfig("127.0.0.1", 1088);
/*     */     
/*  54 */     socksServerConfig.clientSocketOnceReadSize.set(10);
/*  55 */     socksServerConfig.clientPacketSize.set(10);
/*  56 */     socksServerConfig.remoteKey = "admin";
/*  57 */     socksServerConfig.remoteProxyUrl = "http://127.0.0.1:8088/";
/*  58 */     socksServerConfig.requestErrRetry.set(50);
/*  59 */     socksServerConfig.requestHandle = new SimpleHttpRequestHandle();
/*  60 */     socksServerConfig.serverPacketSize = 10;
/*  61 */     socksServerConfig.serverSocketOnceReadSize = 10;
/*  62 */     socksServerConfig.capacity.set(5);
/*     */ 
/*     */     
/*  65 */     HttpToSocks handleSocks = new HttpToSocks(socksServerConfig);
/*  66 */     System.out.println(handleSocks.generateSessionId());
/*  67 */     System.out.println(handleSocks.testConnect());
/*  68 */     System.out.println(handleSocks.testConnect());
/*  69 */     System.out.println(handleSocks.testConnect());
/*     */   }
/*     */   
/*     */   public boolean start() throws Exception {
/*  73 */     if (this.alive) {
/*  74 */       Log.error("服务已开启");
/*  75 */       return false;
/*     */     } 
/*  77 */     this.socksServer = new SocksServer();
/*  78 */     this.startSocksTime = System.currentTimeMillis();
/*  79 */     this.socksServer.start(this.socksServerConfig.listenAddress);
/*  80 */     (new Thread(this::startSocksRelay)).start();
/*  81 */     (new Thread(() -> {
/*     */           this.startSocksTime = System.currentTimeMillis();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */           
/*     */           try {
/*     */             while (this.alive) {
/*     */               Socket client = this.socksServer.accept();
/*     */ 
/*     */ 
/*     */ 
/*     */               
/*     */               (new Thread(())).start();
/*     */             } 
/*  97 */           } catch (Exception e) {
/*     */             e.printStackTrace();
/*     */             reset();
/*     */           } 
/* 101 */         })).start();
/* 102 */     this.alive = true;
/* 103 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public void reset() {
/* 108 */     this.startSocksTime = 0L;
/* 109 */     this.summaryUploadBytes = 0L;
/* 110 */     this.summaryDownloadBytes = 0L;
/* 111 */     this.requestSuccessNum = 0L;
/* 112 */     this.requestFailureNum = 0L;
/* 113 */     this.keys = null;
/* 114 */     this.keyIndex = 0;
/* 115 */     this.accessNum = 1L;
/* 116 */     this.session = new ConcurrentHashMap<>(50);
/* 117 */     if (!this.alive) {
/*     */       return;
/*     */     }
/* 120 */     if (this.alive) {
/*     */       try {
/* 122 */         ArrayList<Parameter> packets = new ArrayList();
/* 123 */         packets.add(createParameter((byte)7));
/* 124 */         SendRequest(packets);
/* 125 */       } catch (Throwable e) {
/* 126 */         e.printStackTrace();
/*     */       } 
/*     */     }
/* 129 */     close();
/* 130 */     this.alive = false;
/* 131 */     System.gc();
/* 132 */     System.gc();
/* 133 */     System.gc();
/*     */   }
/*     */ 
/*     */   
/*     */   public long getAccessNum() {
/* 138 */     return this.accessNum;
/*     */   }
/*     */   public long addAccessNum() {
/* 141 */     return this.accessNum++;
/*     */   }
/*     */ 
/*     */   
/*     */   public Map<String, SocksRelayInfo> getSession() {
/* 146 */     return this.session;
/*     */   }
/*     */   
/*     */   public long getSummaryUploadBytes() {
/* 150 */     return this.summaryUploadBytes;
/*     */   }
/*     */   
/*     */   public long getSummaryDownloadBytes() {
/* 154 */     return this.summaryDownloadBytes;
/*     */   }
/*     */   
/*     */   public long getRequestSuccessNum() {
/* 158 */     return this.requestSuccessNum;
/*     */   }
/*     */   
/*     */   public long getRequestFailureNum() {
/* 162 */     return this.requestFailureNum;
/*     */   }
/*     */   
/*     */   public long getStartSocksTime() {
/* 166 */     return this.startSocksTime;
/*     */   }
/*     */   
/*     */   public String getSessionId() {
/* 170 */     return this.sessionId;
/*     */   }
/*     */   
/*     */   public SocksServerConfig getSocksServerConfig() {
/* 174 */     return this.socksServerConfig;
/*     */   }
/*     */   
/*     */   public boolean isAlive() {
/* 178 */     return this.alive;
/*     */   }
/*     */   
/*     */   public boolean addRelaySocket(SocksRelayInfo socksRelayInfo) {
/* 182 */     if (this.alive) {
/* 183 */       this.session.put(socksRelayInfo.getSocketId(), socksRelayInfo);
/* 184 */       return true;
/*     */     } 
/* 186 */     return false;
/*     */   }
/*     */   public SocksRelayInfo addBindMirror(String bindHost, String bindPort, String destHost, String destPort) throws UnsupportedOperationException {
/* 189 */     SocksRelayInfo socksRelayInfo = new SocksRelayInfo(this.socksServerConfig.clientSocketOnceReadSize.get(), 1024);
/* 190 */     if (this.alive) {
/* 191 */       socksRelayInfo.setDestHost(destHost);
/* 192 */       socksRelayInfo.setDestPort(Short.parseShort(destPort));
/* 193 */       socksRelayInfo.setBindHost(bindHost);
/* 194 */       socksRelayInfo.setBindPort(Short.parseShort(destPort));
/* 195 */       addRelaySocket(socksRelayInfo);
/* 196 */       boolean ok = socksRelayInfo.startBind(bindHost, bindPort);
/* 197 */       if (!ok) {
/* 198 */         socksRelayInfo.close();
/* 199 */         Log.log(EasyI18N.getI18nString("mirrorSocket 启动失败可能是地址/端口已被占用 errMsg:%s"), new Object[] { socksRelayInfo.getErrorMessage() });
/*     */       } 
/*     */     } else {
/* 202 */       Log.error(EasyI18N.getI18nString("HttpToSocks未启动"));
/* 203 */       new UnsupportedOperationException(EasyI18N.getI18nString("HttpToSocks未启动"));
/*     */     } 
/* 205 */     return socksRelayInfo;
/*     */   }
/*     */   public int deleteDeadSocket() {
/* 208 */     int deleteNum = 0;
/* 209 */     this.session.keySet().iterator().forEachRemaining(key -> {
/*     */           SocksRelayInfo socksRelayInfo = this.session.get(key);
/*     */           
/*     */           if (!socksRelayInfo.isAlive() && socksRelayInfo.getReadTaskSize() == 0) {
/*     */             this.session.remove(key);
/*     */             Log.log(String.format("free socket socketId:%s", new Object[] { socksRelayInfo.getSocketId() }), new Object[0]);
/*     */           } 
/*     */         });
/* 217 */     return deleteNum;
/*     */   }
/*     */   public void close() {
/*     */     try {
/* 221 */       this.alive = false;
/* 222 */       if (this.socksServer != null) {
/* 223 */         deleteDeadSocket();
/* 224 */         if (this.socksServer != null) {
/* 225 */           this.socksServer.close();
/*     */         }
/* 227 */         this.session.values().forEach(socksRelayInfo -> {
/*     */               try {
/*     */                 if (socksRelayInfo.isAlive()) {
/*     */                   socksRelayInfo.close();
/*     */                 }
/* 232 */               } catch (Exception e) {
/*     */                 e.printStackTrace();
/*     */               } 
/*     */             });
/*     */       } 
/* 237 */       Log.log("HttpToSocks->close", new Object[0]);
/* 238 */     } catch (Exception e) {
/* 239 */       Log.error(e);
/*     */     } 
/* 241 */     this.session.clear();
/* 242 */     System.gc();
/*     */   }
/*     */   
/*     */   public void closeSocket(String socketId) {
/*     */     try {
/* 247 */       SocksRelayInfo socksRelayInfo = this.session.get(socketId);
/* 248 */       if (socksRelayInfo != null) {
/* 249 */         socksRelayInfo.close();
/*     */       }
/* 251 */     } catch (Exception e) {
/* 252 */       e.printStackTrace();
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean testConnect() {
/*     */     try {
/* 259 */       ArrayList<Parameter> messageList = new ArrayList();
/*     */       
/* 261 */       messageList.add(createParameter((byte)8));
/*     */       
/* 263 */       Parameter res = SendRequest(messageList, this.socksServerConfig.requestErrRetry.get()).readParameter();
/* 264 */       if (res != null) {
/* 265 */         byte[] typeArr = res.getParameterByteArray("type");
/* 266 */         if (typeArr != null && typeArr.length > 0 && 
/* 267 */           typeArr[0] == 5) {
/* 268 */           return true;
/*     */         }
/*     */       }
/*     */     
/* 272 */     } catch (Exception e) {
/* 273 */       Log.error(e);
/*     */     } 
/* 275 */     return false;
/*     */   }
/*     */   
/*     */   public synchronized void handleMessage(ParameterInputStream parameterInputStream) {
/* 279 */     String socketId = null;
/* 280 */     SocksRelayInfo _socksRelayInfo = null;
/* 281 */     Parameter resParameter = null;
/* 282 */     while ((resParameter = parameterInputStream.readParameter()) != null) {
/* 283 */       socketId = resParameter.getParameterString("socketId");
/* 284 */       byte[] typeArray = resParameter.getParameterByteArray("type");
/*     */       
/* 286 */       if (typeArray != null && typeArray.length > 0) {
/*     */         
/* 288 */         if (!this.session.containsKey(socketId) && typeArray[0] != 2) {
/* 289 */           SocksRelayInfo socksRelayInfo = new SocksRelayInfo(0, 5);
/* 290 */           socksRelayInfo.setSocketId(socketId);
/* 291 */           addRelaySocket(socksRelayInfo);
/* 292 */           socksRelayInfo.close();
/*     */           continue;
/*     */         } 
/* 295 */         _socksRelayInfo = this.session.get(socketId);
/*     */         try {
/*     */           SocksRelayInfo mirrorRelayInfo;
/* 298 */           switch (typeArray[0]) {
/*     */             case 2:
/* 300 */               _socksRelayInfo = this.session.remove(socketId);
/* 301 */               if (_socksRelayInfo != null) {
/* 302 */                 _socksRelayInfo.pushWriteTask(resParameter);
/* 303 */                 _socksRelayInfo.close();
/*     */               } 
/*     */             
/*     */             case 3:
/* 307 */               if (_socksRelayInfo.isAlive()) {
/* 308 */                 _socksRelayInfo.pushWriteTask(resParameter);
/* 309 */                 this.summaryDownloadBytes += (resParameter.getParameterByteArray("data")).length;
/*     */               } 
/*     */             
/*     */             case 5:
/* 313 */               _socksRelayInfo.pushWriteTask(resParameter);
/*     */             
/*     */             case 1:
/* 316 */               mirrorRelayInfo = this.session.get(socketId);
/* 317 */               if (mirrorRelayInfo != null) {
/* 318 */                 SocksRelayInfo socksRelayInfo = new SocksRelayInfo(this.socksServerConfig.clientSocketOnceReadSize.get(), this.socksServerConfig.capacity.get());
/* 319 */                 socksRelayInfo.setSocketId(resParameter.getParameterString("destSocketId"));
/* 320 */                 socksRelayInfo.setDestHost(mirrorRelayInfo.getDestHost());
/* 321 */                 socksRelayInfo.setDestPort(mirrorRelayInfo.getDestPort());
/* 322 */                 addRelaySocket(socksRelayInfo);
/* 323 */                 (new Thread(socksRelayInfo::bindSocketServerOpenSocket)).start();
/*     */               } 
/*     */           } 
/*     */ 
/*     */ 
/*     */         
/* 329 */         } catch (Exception e) {
/* 330 */           e.printStackTrace();
/*     */         } 
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void startSocksRelay() {
/* 338 */     Thread.currentThread().setName(Thread.currentThread().getStackTrace()[1].getMethodName());
/* 339 */     while (this.alive) {
/* 340 */       if (this.session.size() == 0) {
/*     */         try {
/* 342 */           Thread.sleep(50L);
/* 343 */         } catch (InterruptedException e) {
/* 344 */           e.printStackTrace();
/*     */         } 
/*     */         continue;
/*     */       } 
/* 348 */       deleteDeadSocket();
/*     */       try {
/* 350 */         Thread.sleep(this.socksServerConfig.requestDelay.get());
/* 351 */       } catch (InterruptedException e) {
/* 352 */         e.printStackTrace();
/*     */       } 
/* 354 */       ArrayList<Parameter> packets = getAllChanData();
/*     */       
/* 356 */       ParameterInputStream parameterInputStream = SendRequest(packets);
/*     */       
/* 358 */       if (parameterInputStream == null) {
/*     */         break;
/*     */       }
/* 361 */       this.summaryUploadBytes += this.tmpUploadBytes;
/* 362 */       handleMessage(parameterInputStream);
/*     */     } 
/*     */ 
/*     */     
/* 366 */     reset();
/*     */   }
/*     */   
/*     */   public ArrayList<Parameter> getAllChanData() {
/* 370 */     this.tmpUploadBytes = 0L;
/* 371 */     int dataLength = 0;
/* 372 */     ArrayList<Parameter> packets = new ArrayList<>();
/* 373 */     for (int forNum = 0; forNum < 3; forNum++) {
/* 374 */       while (dataLength < this.socksServerConfig.clientPacketSize.get() && this.session.size() > 0 && this.keyIndex < this.session.size()) {
/* 375 */         if (this.keyIndex == 0) {
/* 376 */           this.keys = (String[])this.session.keySet().toArray((Object[])new String[0]);
/*     */         }
/* 378 */         if (this.session.size() > 0) {
/* 379 */           String key = this.keys[this.keyIndex];
/* 380 */           SocksRelayInfo socket = this.session.get(key);
/* 381 */           if (socket != null) {
/* 382 */             Parameter resParameter = socket.ReadTaskData(true);
/* 383 */             if (resParameter != null) {
/* 384 */               dataLength += resParameter.len();
/* 385 */               packets.add(resParameter);
/*     */               
/* 387 */               if (resParameter.getParameterByteArray("type")[0] == 3) {
/* 388 */                 this.tmpUploadBytes += (resParameter.getParameterByteArray("data")).length;
/*     */               }
/*     */             } 
/* 391 */             if (this.keyIndex + 1 >= this.keys.length) {
/* 392 */               this.keyIndex = 0;
/*     */               break;
/*     */             } 
/* 395 */             this.keyIndex++;
/*     */           } 
/*     */         } 
/*     */       } 
/*     */     } 
/*     */     
/* 401 */     if (packets.isEmpty()) {
/* 402 */       packets.add(createParameter((byte)4));
/*     */     }
/* 404 */     return packets;
/*     */   }
/*     */   public String generateSessionId() throws UnsupportedOperationException {
/* 407 */     this.sessionKey = getSessionKey(this.socksServerConfig.remoteKey);
/*     */     try {
/* 409 */       Parameter request = createParameter((byte)0).addParameterByteArray("accessNum", ByteBuffer.allocate(8).putLong(0L).array());
/*     */       
/* 411 */       request.add("serverMaxResponseSize", ByteBuffer.allocate(8).putLong(this.socksServerConfig.serverPacketSize).array());
/* 412 */       request.add("serverPacketMaxSize", ByteBuffer.allocate(8).putLong(this.socksServerConfig.serverSocketOnceReadSize).array());
/* 413 */       request.add("serverCapacity", ByteBuffer.allocate(8).putLong(this.socksServerConfig.capacity.get()).array());
/*     */       
/* 415 */       HttpResponse response = SendRequestPostRaw(this.socksServerConfig.remoteProxyUrl, encryptRequestData(request.serialize(), this.sessionKey), this.socksServerConfig.requestHandle);
/* 416 */       Parameter parameter = Parameter.unSerialize(Dcrypt(response.getResponseData(), this.sessionKey));
/* 417 */       if (parameter.getParameterByteArray("type")[0] == 5) {
/* 418 */         this.sessionId = parameter.getParameterString("sessionId");
/* 419 */         return this.sessionId;
/*     */       } 
/* 421 */     } catch (Exception e) {
/* 422 */       throw new UnsupportedOperationException(e);
/*     */     } 
/* 424 */     return null;
/*     */   }
/*     */   public static Parameter createParameter(byte type) {
/* 427 */     Parameter ret = new Parameter();
/* 428 */     ret.add("type", new byte[] { type });
/* 429 */     return ret;
/*     */   }
/*     */   
/*     */   public ParameterInputStream SendRequest(ArrayList<Parameter> packets) {
/* 433 */     return SendRequest(packets, this.socksServerConfig.requestErrRetry.get());
/*     */   }
/*     */   
/*     */   public ParameterInputStream SendRequest(ArrayList<Parameter> packets, long maxErrNum) {
/* 437 */     Parameter headerParameter = new Parameter();
/* 438 */     ParameterInputStream ret = null;
/* 439 */     HttpResponse response = null;
/* 440 */     packets.add(0, headerParameter);
/* 441 */     long currentErrNum = 0L;
/* 442 */     while (currentErrNum < maxErrNum) {
/*     */       try {
/* 444 */         byte[] currentTimeByteArr = ByteBuffer.allocate(8).putLong(System.currentTimeMillis()).array();
/* 445 */         headerParameter.add("accessTime", currentTimeByteArr);
/* 446 */         headerParameter.add("accessNum", ByteBuffer.allocate(8).putLong(getAccessNum()).array());
/* 447 */         headerParameter.add("sessionId", this.sessionId.getBytes());
/* 448 */         ParameterOutputStream parameterOutputStream = new ParameterOutputStream();
/* 449 */         for (int i = 0; i < packets.size(); i++) {
/* 450 */           parameterOutputStream.writeParameter(packets.get(i));
/*     */         }
/* 452 */         addAccessNum();
/* 453 */         byte[] reqData = encryptRequestData(parameterOutputStream.getBuffer(), this.sessionKey);
/* 454 */         response = SendRequestPostRaw(this.socksServerConfig.remoteProxyUrl, reqData, this.socksServerConfig.requestHandle);
/* 455 */         if (response != null && (response.getResponseData()).length > 0) {
/* 456 */           ret = ParameterInputStream.asParameterInputStream(Dcrypt(response.getResponseData(), this.sessionKey));
/* 457 */           this.requestSuccessNum++;
/*     */           break;
/*     */         } 
/* 460 */         this.requestFailureNum++;
/*     */       }
/* 462 */       catch (Exception e) {
/* 463 */         this.requestFailureNum++;
/* 464 */         Log.error(e);
/*     */         try {
/* 466 */           Thread.sleep(this.socksServerConfig.requestErrDelay.get());
/* 467 */         } catch (InterruptedException interruptedException) {
/* 468 */           interruptedException.printStackTrace();
/*     */         } 
/*     */       } 
/* 471 */       currentErrNum++;
/*     */     } 
/* 473 */     if (ret == null && this.alive) {
/* 474 */       Log.error(String.format(EasyI18N.getI18nString("最大错误尝试次数:%d 错误尝试后服务器依然无法通信 已强制关闭httpToSocks"), new Object[] { Integer.valueOf(this.socksServerConfig.requestErrRetry.get()) }));
/* 475 */       close();
/* 476 */       return null;
/*     */     } 
/* 478 */     return ret;
/*     */   }
/*     */   public static byte[] Dcrypt(byte[] bs, byte[] key) throws Exception {
/* 481 */     SecretKeySpec skeySpec = new SecretKeySpec(key, "AES");
/* 482 */     Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
/* 483 */     cipher.init(2, skeySpec, new IvParameterSpec(new byte[16]));
/* 484 */     return functions.gzipD(cipher.doFinal(bs));
/*     */   }
/*     */   public static byte[] Encrypt(byte[] bs, byte[] key) throws Exception {
/* 487 */     SecretKeySpec skeySpec = new SecretKeySpec(key, "AES");
/* 488 */     Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
/* 489 */     cipher.init(1, skeySpec, new IvParameterSpec(new byte[16]));
/* 490 */     return cipher.doFinal(functions.gzipE(bs));
/*     */   }
/*     */   public static byte[] encryptRequestData(byte[] pars, byte[] sessionKey) throws Exception {
/* 493 */     return Encrypt(pars, sessionKey);
/*     */   }
/*     */   public byte[] getSessionKey(String key) {
/* 496 */     byte[] md = key.getBytes();
/* 497 */     for (int i = 0; i <= 20; i++) {
/* 498 */       md = functions.md5(md);
/*     */     }
/* 500 */     return md;
/*     */   }
/*     */   public static HttpResponse SendRequestPostFrom(String url, String data, HttpRequestHandle requestHandle) {
/* 503 */     HttpRequest httpRequest = new HttpRequest();
/* 504 */     httpRequest.getHttpRequestHeader().setContentType("application/x-www-form-urlencoded");
/* 505 */     httpRequest.setUrl(url);
/* 506 */     httpRequest.setRequestData(data.getBytes());
/* 507 */     httpRequest.setMethod("POST");
/* 508 */     return sendHttpRequest(httpRequest, requestHandle);
/*     */   }
/*     */   public static HttpResponse SendRequestPostRaw(String url, byte[] data, HttpRequestHandle requestHandle) {
/* 511 */     HttpRequest httpRequest = new HttpRequest();
/* 512 */     httpRequest.getHttpRequestHeader().setContentType("application/octet-stream");
/* 513 */     httpRequest.setUrl(url);
/* 514 */     httpRequest.setRequestData(data);
/* 515 */     httpRequest.setMethod("POST");
/* 516 */     return sendHttpRequest(httpRequest, requestHandle);
/*     */   }
/*     */   
/*     */   public static HttpResponse sendHttpRequest(HttpRequest httpRequest, HttpRequestHandle requestHandle) {
/* 520 */     return requestHandle.sendHttpRequest(httpRequest);
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\core\socksServer\HttpToSocks.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */