/*     */ package core.shell;
/*     */ 
/*     */ import core.ApplicationContext;
/*     */ import core.Db;
/*     */ import core.Encoding;
/*     */ import core.imp.Cryption;
/*     */ import core.imp.Payload;
/*     */ import core.shell.cache.CachePayload;
/*     */ import core.ui.ShellManage;
/*     */ import java.io.ByteArrayOutputStream;
/*     */ import java.io.PrintStream;
/*     */ import java.sql.PreparedStatement;
/*     */ import java.sql.ResultSet;
/*     */ import java.util.HashMap;
/*     */ import java.util.Hashtable;
/*     */ import java.util.Iterator;
/*     */ import java.util.Map;
/*     */ import util.Log;
/*     */ import util.functions;
/*     */ import util.http.Http;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ShellEntity
/*     */ {
/*     */   private String url;
/*     */   private String password;
/*     */   private String secretKey;
/*     */   private String payload;
/*     */   private String cryption;
/*     */   private String remark;
/*     */   private String encoding;
/*     */   private Map<String, String> headers;
/*     */   private String reqLeft;
/*     */   private String reqRight;
/*     */   private int connTimeout;
/*     */   private int readTimeout;
/*     */   private String proxyType;
/*     */   private String proxyHost;
/*     */   private int proxyPort;
/*     */   private Cryption cryptionModel;
/*     */   private Payload payloadModel;
/*     */   private String id;
/*     */   private ShellManage frame;
/*     */   private Encoding encodingModule;
/*     */   private Encoding dbEncodingModule;
/*     */   private String dbEncoding;
/*     */   private Http http;
/*     */   private boolean isSendLRReqData;
/*     */   private boolean useCache;
/*     */   
/*     */   public ShellEntity(boolean useCache) {
/*  55 */     this.url = "";
/*  56 */     this.password = "";
/*  57 */     this.secretKey = "";
/*  58 */     this.payload = "";
/*  59 */     this.cryption = "";
/*  60 */     this.remark = "";
/*  61 */     this.encoding = "";
/*  62 */     this.headers = new HashMap<>();
/*  63 */     this.reqLeft = "";
/*  64 */     this.reqRight = "";
/*  65 */     this.connTimeout = 60000;
/*  66 */     this.readTimeout = 60000;
/*  67 */     this.proxyType = "";
/*  68 */     this.proxyHost = "";
/*  69 */     this.proxyPort = 8888;
/*  70 */     this.id = "";
/*  71 */     this.useCache = useCache;
/*     */   }
/*     */   public ShellEntity() {
/*  74 */     this(false);
/*     */   }
/*     */   
/*     */   public boolean initShellOpertion() {
/*  78 */     boolean state = false;
/*     */     try {
/*  80 */       this.http = ApplicationContext.getHttp(this);
/*  81 */       if (isUseCache()) {
/*  82 */         this.payloadModel = CachePayload.openUseCachePayload(this, ApplicationContext.getPayload(this.payload).getClass());
/*  83 */         if (this.payloadModel != null) {
/*  84 */           this.payloadModel.init(this);
/*  85 */           return true;
/*     */         } 
/*  87 */         return false;
/*     */       } 
/*  89 */       if (ApplicationContext.isOpenCache()) {
/*  90 */         this.payloadModel = CachePayload.openCachePayload(this, ApplicationContext.getPayload(this.payload).getClass());
/*     */       } else {
/*  92 */         this.payloadModel = ApplicationContext.getPayload(this.payload);
/*     */       } 
/*  94 */       this.cryptionModel = ApplicationContext.getCryption(this.payload, this.cryption);
/*  95 */       this.cryptionModel.init(this);
/*  96 */       if (this.cryptionModel.check()) {
/*  97 */         this.payloadModel.init(this);
/*  98 */         if (this.payloadModel.test()) {
/*  99 */           state = true;
/*     */         } else {
/* 101 */           Log.error("payload Initialize Fail !");
/*     */         } 
/*     */       } else {
/* 104 */         Log.error("cryption Initialize Fail !");
/*     */       }
/*     */     
/* 107 */     } catch (Throwable e) {
/* 108 */       Log.error(e);
/* 109 */       ByteArrayOutputStream stream = new ByteArrayOutputStream();
/* 110 */       PrintStream printStream = new PrintStream(stream);
/* 111 */       e.printStackTrace(printStream);
/* 112 */       printStream.flush();
/* 113 */       printStream.close();
/* 114 */       Log.log(new String(stream.toByteArray()), new Object[0]);
/* 115 */       return state;
/*     */     } 
/* 117 */     return state;
/*     */   }
/*     */   public Http getHttp() {
/* 120 */     return this.http;
/*     */   }
/*     */   public Cryption getCryptionModule() {
/* 123 */     return this.cryptionModel;
/*     */   }
/*     */   public Payload getPayloadModule() {
/* 126 */     return this.payloadModel;
/*     */   }
/*     */   public String getId() {
/* 129 */     return this.id;
/*     */   }
/*     */   public void setId(String id) {
/* 132 */     this.id = id;
/*     */   }
/*     */   public String getPassword() {
/* 135 */     return this.password;
/*     */   }
/*     */   public String getSecretKey() {
/* 138 */     return this.secretKey;
/*     */   }
/*     */   public String getSecretKeyX() {
/* 141 */     return functions.md5(getSecretKey()).substring(0, 16);
/*     */   }
/*     */   public String getPayload() {
/* 144 */     return this.payload;
/*     */   }
/*     */   public String getEncoding() {
/* 147 */     return this.encoding;
/*     */   }
/*     */   public synchronized Encoding getEncodingModule() {
/* 150 */     if (this.encodingModule == null) {
/* 151 */       this.encodingModule = Encoding.getEncoding(getEncoding());
/*     */     }
/* 153 */     return this.encodingModule;
/*     */   }
/*     */   public synchronized String getDbEncoding() {
/* 156 */     if (this.dbEncoding == null) {
/* 157 */       this.dbEncoding = "UTF-8";
/*     */     }
/* 159 */     return this.dbEncoding;
/*     */   }
/*     */   public synchronized Encoding getDbEncodingModule() {
/* 162 */     if (this.dbEncodingModule == null) {
/* 163 */       this.dbEncodingModule = Encoding.getEncoding(getDbEncoding());
/*     */     }
/* 165 */     return this.dbEncodingModule;
/*     */   }
/*     */   public String getProxyType() {
/* 168 */     return this.proxyType;
/*     */   }
/*     */   public String getProxyHost() {
/* 171 */     return this.proxyHost;
/*     */   }
/*     */   public int getProxyPort() {
/* 174 */     return this.proxyPort;
/*     */   }
/*     */   public String getCryption() {
/* 177 */     return this.cryption;
/*     */   }
/*     */   public void setCryption(String cryption) {
/* 180 */     this.cryption = cryption;
/*     */   }
/*     */   public void setHeaders(Map<String, String> headers) {
/* 183 */     this.headers = headers;
/*     */   }
/*     */   public void setPassword(String password) {
/* 186 */     this.password = password;
/*     */   }
/*     */   public void setSecretKey(String secretKey) {
/* 189 */     this.secretKey = secretKey;
/*     */   }
/*     */   public void setPayload(String str) {
/* 192 */     this.payload = str;
/*     */   }
/*     */   public void setEncoding(String encoding) {
/* 195 */     this.encoding = encoding;
/*     */   }
/*     */   
/*     */   public void setProxyType(String proxyType) {
/* 199 */     this.proxyType = proxyType;
/*     */   }
/*     */   public void setProxyHost(String proxyHost) {
/* 202 */     this.proxyHost = proxyHost;
/*     */   }
/*     */   public void setProxyPort(int proxyPort) {
/* 205 */     this.proxyPort = proxyPort;
/*     */   }
/*     */   public int getConnTimeout() {
/* 208 */     return this.connTimeout;
/*     */   }
/*     */   public int getReadTimeout() {
/* 211 */     return this.readTimeout;
/*     */   }
/*     */   public void setConnTimeout(int connTimeout) {
/* 214 */     this.connTimeout = connTimeout;
/*     */   }
/*     */   public void setReadTimeout(int readTimeout) {
/* 217 */     this.readTimeout = readTimeout;
/*     */   }
/*     */   public Map<String, String> getHeaders() {
/* 220 */     return this.headers;
/*     */   }
/*     */   public String getHeaderS() {
/* 223 */     StringBuilder builder = new StringBuilder();
/* 224 */     Iterator<String> iterator = this.headers.keySet().iterator();
/*     */ 
/*     */     
/* 227 */     while (iterator.hasNext()) {
/* 228 */       String key = iterator.next();
/* 229 */       String value = this.headers.get(key);
/* 230 */       builder.append(key);
/* 231 */       builder.append(": ");
/* 232 */       builder.append(value);
/* 233 */       builder.append("\r\n");
/*     */     } 
/* 235 */     return builder.toString();
/*     */   }
/*     */   public String getRemark() {
/* 238 */     return this.remark;
/*     */   }
/*     */   public void setRemark(String remark) {
/* 241 */     this.remark = remark;
/*     */   }
/*     */   public ShellManage getFrame() {
/* 244 */     return this.frame;
/*     */   }
/*     */   public void setFrame(ShellManage frame) {
/* 247 */     this.frame = frame;
/*     */   }
/*     */   public void setHeader(String reqString) {
/* 250 */     if (reqString != null) {
/* 251 */       String[] reqLines = reqString.split("\n");
/*     */ 
/*     */ 
/*     */       
/* 255 */       this.headers = new Hashtable<>();
/* 256 */       for (int i = 0; i < reqLines.length; i++) {
/* 257 */         if (!reqLines[i].trim().isEmpty()) {
/* 258 */           int index = reqLines[i].indexOf(":");
/* 259 */           if (index > 1) {
/* 260 */             String keyName = reqLines[i].substring(0, index).trim();
/* 261 */             String keyValue = reqLines[i].substring(index + 1).trim();
/* 262 */             this.headers.put(keyName, keyValue);
/*     */           } 
/*     */         } 
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   public String getReqLeft() {
/* 270 */     return this.reqLeft;
/*     */   }
/*     */   public void setReqLeft(String reqLeft) {
/* 273 */     this.reqLeft = reqLeft;
/*     */   }
/*     */   public String getReqRight() {
/* 276 */     return this.reqRight;
/*     */   }
/*     */   public void setReqRight(String reqRight) {
/* 279 */     this.reqRight = reqRight;
/*     */   }
/*     */   public String getUrl() {
/* 282 */     return this.url;
/*     */   }
/*     */   public void setUrl(String url) {
/* 285 */     this.url = url;
/*     */   }
/*     */   public boolean isSendLRReqData() {
/* 288 */     return this.cryptionModel.isSendRLData();
/*     */   }
/*     */   public boolean setEnv(String key, String value) {
/* 291 */     if (ApplicationContext.isOpenC("isSuperLog")) {
/* 292 */       Log.log(String.format("updateShellEnv id:%s key:%s value:%s", new Object[] { getId(), key, value }), new Object[0]);
/*     */     }
/* 294 */     if (existsSetingKey(key)) {
/* 295 */       String updateShellEnvSql = "UPDATE shellEnv set value=? WHERE shellId=? and key=?";
/* 296 */       PreparedStatement preparedStatement1 = Db.getPreparedStatement(updateShellEnvSql);
/*     */       try {
/* 298 */         preparedStatement1.setString(1, value);
/* 299 */         preparedStatement1.setString(2, getId());
/* 300 */         preparedStatement1.setString(3, key);
/* 301 */         int affectNum = preparedStatement1.executeUpdate();
/* 302 */         preparedStatement1.close();
/* 303 */         return (affectNum > 0);
/* 304 */       } catch (Exception e) {
/* 305 */         e.printStackTrace();
/* 306 */         return false;
/*     */       } 
/*     */     } 
/* 309 */     String updateSetingSql = "INSERT INTO shellEnv (\"shellId\",\"key\", \"value\") VALUES (?, ?, ?)";
/* 310 */     PreparedStatement preparedStatement = Db.getPreparedStatement(updateSetingSql);
/*     */     try {
/* 312 */       preparedStatement.setString(1, getId());
/* 313 */       preparedStatement.setString(2, key);
/* 314 */       preparedStatement.setString(3, value);
/* 315 */       int affectNum = preparedStatement.executeUpdate();
/* 316 */       preparedStatement.close();
/* 317 */       return (affectNum > 0);
/* 318 */     } catch (Exception e) {
/* 319 */       e.printStackTrace();
/* 320 */       return false;
/*     */     } 
/*     */   }
/*     */   
/*     */   public String getEnv(String key, String defaultValue) {
/* 325 */     String getShellEnvSql = "SELECT value FROM shellEnv WHERE shellId=? and key=?";
/* 326 */     if (existsSetingKey(key)) {
/*     */       try {
/* 328 */         PreparedStatement preparedStatement = Db.getPreparedStatement(getShellEnvSql);
/* 329 */         preparedStatement.setString(1, getId());
/* 330 */         preparedStatement.setString(2, key);
/* 331 */         ResultSet resultSet = preparedStatement.executeQuery();
/* 332 */         String value = resultSet.next() ? resultSet.getString("value") : null;
/* 333 */         resultSet.close();
/* 334 */         preparedStatement.close();
/* 335 */         return value;
/* 336 */       } catch (Exception e) {
/* 337 */         Log.error(e);
/* 338 */         return null;
/*     */       } 
/*     */     }
/* 341 */     setEnv(key, defaultValue);
/* 342 */     return defaultValue;
/*     */   }
/*     */   
/*     */   public void setGroup(String groupId) {
/* 346 */     setEnv("ENV_GROUP_ID", groupId);
/*     */   }
/*     */   public String getGroup() {
/* 349 */     return getEnv("ENV_GROUP_ID", "/");
/*     */   }
/*     */   public boolean removeEnv(String key) {
/* 352 */     String updateSetingSql = "DELETE FROM shellEnv WHERE shellId=? and key=?";
/* 353 */     PreparedStatement preparedStatement = Db.getPreparedStatement(updateSetingSql);
/*     */     try {
/* 355 */       preparedStatement.setString(1, getId());
/* 356 */       preparedStatement.setString(2, key);
/* 357 */       int affectNum = preparedStatement.executeUpdate();
/* 358 */       preparedStatement.close();
/* 359 */       return (affectNum > 0);
/* 360 */     } catch (Exception e) {
/* 361 */       e.printStackTrace();
/* 362 */       return false;
/*     */     } 
/*     */   }
/*     */   
/*     */   public boolean existsSetingKey(String key) {
/* 367 */     String selectKeyNumSql = "SELECT COUNT(1) as c FROM shellEnv WHERE shellId=? and key=?";
/*     */     try {
/* 369 */       PreparedStatement preparedStatement = Db.getPreparedStatement(selectKeyNumSql);
/* 370 */       preparedStatement.setString(1, getId());
/* 371 */       preparedStatement.setString(2, key);
/* 372 */       int c = preparedStatement.executeQuery().getInt("c");
/* 373 */       preparedStatement.close();
/* 374 */       return (c > 0);
/* 375 */     } catch (Exception e) {
/* 376 */       Log.error(e);
/* 377 */       return false;
/*     */     } 
/*     */   }
/*     */   
/*     */   public void setUseCache(boolean useCache) {
/* 382 */     this.useCache = useCache;
/*     */   }
/*     */   
/*     */   public boolean isUseCache() {
/* 386 */     return this.useCache;
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 391 */     return "ShellEntity [id=" + this.id + ", url=" + this.url + ", password=" + this.password + ", secretKey=" + this.secretKey + ", payload=" + this.payload + ", cryption=" + this.cryption + ", remark=" + this.remark + ", encoding=" + this.encoding + ", headers=" + this.headers + ", reqLeft=" + this.reqLeft + ", reqRight=" + this.reqRight + ", connTimeout=" + this.connTimeout + ", readTimeout=" + this.readTimeout + ", proxyType=" + this.proxyType + ", proxyHost=" + this.proxyHost + ", proxyPort=" + this.proxyPort + "]";
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\core\shell\ShellEntity.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */