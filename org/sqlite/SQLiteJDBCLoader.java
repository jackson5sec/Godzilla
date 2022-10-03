/*     */ package org.sqlite;
/*     */ 
/*     */ import java.io.BufferedInputStream;
/*     */ import java.io.ByteArrayOutputStream;
/*     */ import java.io.File;
/*     */ import java.io.FileInputStream;
/*     */ import java.io.FileOutputStream;
/*     */ import java.io.FilenameFilter;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.net.URL;
/*     */ import java.security.DigestInputStream;
/*     */ import java.security.MessageDigest;
/*     */ import java.security.NoSuchAlgorithmException;
/*     */ import java.util.LinkedList;
/*     */ import java.util.List;
/*     */ import java.util.Properties;
/*     */ import java.util.UUID;
/*     */ import org.sqlite.util.OSInfo;
/*     */ import org.sqlite.util.StringUtils;
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
/*     */ public class SQLiteJDBCLoader
/*     */ {
/*     */   private static boolean extracted = false;
/*     */   
/*     */   public static synchronized boolean initialize() throws Exception {
/*  64 */     if (!extracted) {
/*  65 */       cleanup();
/*     */     }
/*  67 */     loadSQLiteNativeLibrary();
/*  68 */     return extracted;
/*     */   }
/*     */   
/*     */   private static File getTempDir() {
/*  72 */     return new File(System.getProperty("org.sqlite.tmpdir", System.getProperty("java.io.tmpdir")));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static void cleanup() {
/*  80 */     String tempFolder = getTempDir().getAbsolutePath();
/*  81 */     File dir = new File(tempFolder);
/*     */     
/*  83 */     File[] nativeLibFiles = dir.listFiles(new FilenameFilter() {
/*  84 */           private final String searchPattern = "sqlite-" + SQLiteJDBCLoader.getVersion();
/*     */           public boolean accept(File dir, String name) {
/*  86 */             return (name.startsWith(this.searchPattern) && !name.endsWith(".lck"));
/*     */           }
/*     */         });
/*  89 */     if (nativeLibFiles != null) {
/*  90 */       for (File nativeLibFile : nativeLibFiles) {
/*  91 */         File lckFile = new File(nativeLibFile.getAbsolutePath() + ".lck");
/*  92 */         if (!lckFile.exists()) {
/*     */           try {
/*  94 */             nativeLibFile.delete();
/*     */           }
/*  96 */           catch (SecurityException e) {
/*  97 */             System.err.println("Failed to delete old native lib" + e.getMessage());
/*     */           } 
/*     */         }
/*     */       } 
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   static boolean getPureJavaFlag() {
/* 111 */     return Boolean.parseBoolean(System.getProperty("sqlite.purejava", "false"));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   public static boolean isPureJavaMode() {
/* 122 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static boolean isNativeMode() throws Exception {
/* 132 */     initialize();
/* 133 */     return extracted;
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
/*     */   static String md5sum(InputStream input) throws IOException {
/* 145 */     BufferedInputStream in = new BufferedInputStream(input);
/*     */     
/*     */     try {
/* 148 */       MessageDigest digest = MessageDigest.getInstance("MD5");
/* 149 */       DigestInputStream digestInputStream = new DigestInputStream(in, digest);
/* 150 */       while (digestInputStream.read() >= 0);
/*     */ 
/*     */       
/* 153 */       ByteArrayOutputStream md5out = new ByteArrayOutputStream();
/* 154 */       md5out.write(digest.digest());
/* 155 */       return md5out.toString();
/*     */     }
/* 157 */     catch (NoSuchAlgorithmException e) {
/* 158 */       throw new IllegalStateException("MD5 algorithm is not available: " + e);
/*     */     } finally {
/*     */       
/* 161 */       in.close();
/*     */     } 
/*     */   }
/*     */   
/*     */   private static boolean contentsEquals(InputStream in1, InputStream in2) throws IOException {
/* 166 */     if (!(in1 instanceof BufferedInputStream)) {
/* 167 */       in1 = new BufferedInputStream(in1);
/*     */     }
/* 169 */     if (!(in2 instanceof BufferedInputStream)) {
/* 170 */       in2 = new BufferedInputStream(in2);
/*     */     }
/*     */     
/* 173 */     int ch = in1.read();
/* 174 */     while (ch != -1) {
/* 175 */       int i = in2.read();
/* 176 */       if (ch != i) {
/* 177 */         return false;
/*     */       }
/* 179 */       ch = in1.read();
/*     */     } 
/* 181 */     int ch2 = in2.read();
/* 182 */     return (ch2 == -1);
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
/*     */   
/*     */   private static boolean extractAndLoadLibraryFile(String libFolderForCurrentOS, String libraryFileName, String targetFolder) {
/* 195 */     String nativeLibraryFilePath = libFolderForCurrentOS + "/" + libraryFileName;
/*     */ 
/*     */     
/* 198 */     String uuid = UUID.randomUUID().toString();
/* 199 */     String extractedLibFileName = String.format("sqlite-%s-%s-%s", new Object[] { getVersion(), uuid, libraryFileName });
/* 200 */     String extractedLckFileName = extractedLibFileName + ".lck";
/*     */     
/* 202 */     File extractedLibFile = new File(targetFolder, extractedLibFileName);
/* 203 */     File extractedLckFile = new File(targetFolder, extractedLckFileName);
/*     */ 
/*     */     
/*     */     try {
/* 207 */       InputStream reader = SQLiteJDBCLoader.class.getResourceAsStream(nativeLibraryFilePath);
/* 208 */       if (!extractedLckFile.exists()) {
/* 209 */         (new FileOutputStream(extractedLckFile)).close();
/*     */       }
/* 211 */       FileOutputStream writer = new FileOutputStream(extractedLibFile);
/*     */       try {
/* 213 */         byte[] buffer = new byte[8192];
/* 214 */         int bytesRead = 0;
/* 215 */         while ((bytesRead = reader.read(buffer)) != -1) {
/* 216 */           writer.write(buffer, 0, bytesRead);
/*     */         }
/*     */       }
/*     */       finally {
/*     */         
/* 221 */         extractedLibFile.deleteOnExit();
/* 222 */         extractedLckFile.deleteOnExit();
/*     */ 
/*     */         
/* 225 */         if (writer != null) {
/* 226 */           writer.close();
/*     */         }
/* 228 */         if (reader != null) {
/* 229 */           reader.close();
/*     */         }
/*     */       } 
/*     */ 
/*     */       
/* 234 */       extractedLibFile.setReadable(true);
/* 235 */       extractedLibFile.setWritable(true, true);
/* 236 */       extractedLibFile.setExecutable(true);
/*     */ 
/*     */ 
/*     */       
/* 240 */       InputStream nativeIn = SQLiteJDBCLoader.class.getResourceAsStream(nativeLibraryFilePath);
/* 241 */       InputStream extractedLibIn = new FileInputStream(extractedLibFile);
/*     */       try {
/* 243 */         if (!contentsEquals(nativeIn, extractedLibIn)) {
/* 244 */           throw new RuntimeException(String.format("Failed to write a native library file at %s", new Object[] { extractedLibFile }));
/*     */         }
/*     */       } finally {
/*     */         
/* 248 */         if (nativeIn != null) {
/* 249 */           nativeIn.close();
/*     */         }
/* 251 */         if (extractedLibIn != null) {
/* 252 */           extractedLibIn.close();
/*     */         }
/*     */       } 
/*     */       
/* 256 */       return loadNativeLibrary(targetFolder, extractedLibFileName);
/*     */     }
/* 258 */     catch (IOException e) {
/* 259 */       System.err.println(e.getMessage());
/* 260 */       return false;
/*     */     } 
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
/*     */   private static boolean loadNativeLibrary(String path, String name) {
/* 273 */     File libPath = new File(path, name);
/* 274 */     if (libPath.exists()) {
/*     */       
/*     */       try {
/* 277 */         System.load((new File(path, name)).getAbsolutePath());
/* 278 */         return true;
/*     */       }
/* 280 */       catch (UnsatisfiedLinkError e) {
/* 281 */         System.err.println("Failed to load native library:" + name + ". osinfo: " + OSInfo.getNativeLibFolderPathForCurrentOS());
/* 282 */         System.err.println(e);
/* 283 */         return false;
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/* 288 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static void loadSQLiteNativeLibrary() throws Exception {
/* 298 */     if (extracted) {
/*     */       return;
/*     */     }
/*     */     
/* 302 */     List<String> triedPaths = new LinkedList<>();
/*     */ 
/*     */     
/* 305 */     String sqliteNativeLibraryPath = System.getProperty("org.sqlite.lib.path");
/* 306 */     String sqliteNativeLibraryName = System.getProperty("org.sqlite.lib.name");
/* 307 */     if (sqliteNativeLibraryName == null) {
/* 308 */       sqliteNativeLibraryName = System.mapLibraryName("sqlitejdbc");
/* 309 */       if (sqliteNativeLibraryName != null && sqliteNativeLibraryName.endsWith(".dylib")) {
/* 310 */         sqliteNativeLibraryName = sqliteNativeLibraryName.replace(".dylib", ".jnilib");
/*     */       }
/*     */     } 
/*     */     
/* 314 */     if (sqliteNativeLibraryPath != null) {
/* 315 */       if (loadNativeLibrary(sqliteNativeLibraryPath, sqliteNativeLibraryName)) {
/* 316 */         extracted = true;
/*     */         return;
/*     */       } 
/* 319 */       triedPaths.add(sqliteNativeLibraryPath);
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/* 324 */     String packagePath = SQLiteJDBCLoader.class.getPackage().getName().replaceAll("\\.", "/");
/* 325 */     sqliteNativeLibraryPath = String.format("/%s/native/%s", new Object[] { packagePath, OSInfo.getNativeLibFolderPathForCurrentOS() });
/* 326 */     boolean hasNativeLib = hasResource(sqliteNativeLibraryPath + "/" + sqliteNativeLibraryName);
/*     */ 
/*     */     
/* 329 */     if (!hasNativeLib && 
/* 330 */       OSInfo.getOSName().equals("Mac")) {
/*     */       
/* 332 */       String altName = "libsqlitejdbc.jnilib";
/* 333 */       if (hasResource(sqliteNativeLibraryPath + "/" + altName)) {
/* 334 */         sqliteNativeLibraryName = altName;
/* 335 */         hasNativeLib = true;
/*     */       } 
/*     */     } 
/*     */ 
/*     */     
/* 340 */     if (hasNativeLib) {
/*     */       
/* 342 */       String tempFolder = getTempDir().getAbsolutePath();
/*     */       
/* 344 */       if (extractAndLoadLibraryFile(sqliteNativeLibraryPath, sqliteNativeLibraryName, tempFolder)) {
/* 345 */         extracted = true;
/*     */         return;
/*     */       } 
/* 348 */       triedPaths.add(sqliteNativeLibraryPath);
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/* 353 */     String javaLibraryPath = System.getProperty("java.library.path", "");
/* 354 */     for (String ldPath : javaLibraryPath.split(File.pathSeparator)) {
/* 355 */       if (!ldPath.isEmpty()) {
/*     */ 
/*     */         
/* 358 */         if (loadNativeLibrary(ldPath, sqliteNativeLibraryName)) {
/* 359 */           extracted = true;
/*     */           return;
/*     */         } 
/* 362 */         triedPaths.add(ldPath);
/*     */       } 
/*     */     } 
/*     */     
/* 366 */     extracted = false;
/* 367 */     throw new Exception(String.format("No native library found for os.name=%s, os.arch=%s, paths=[%s]", new Object[] {
/* 368 */             OSInfo.getOSName(), OSInfo.getArchName(), StringUtils.join(triedPaths, File.pathSeparator) }));
/*     */   }
/*     */   
/*     */   private static boolean hasResource(String path) {
/* 372 */     return (SQLiteJDBCLoader.class.getResource(path) != null);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static void getNativeLibraryFolderForTheCurrentOS() {
/* 378 */     String osName = OSInfo.getOSName();
/* 379 */     String archName = OSInfo.getArchName();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static int getMajorVersion() {
/* 386 */     String[] c = getVersion().split("\\.");
/* 387 */     return (c.length > 0) ? Integer.parseInt(c[0]) : 1;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static int getMinorVersion() {
/* 394 */     String[] c = getVersion().split("\\.");
/* 395 */     return (c.length > 1) ? Integer.parseInt(c[1]) : 0;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static String getVersion() {
/* 403 */     URL versionFile = SQLiteJDBCLoader.class.getResource("/META-INF/maven/org.xerial/sqlite-jdbc/pom.properties");
/* 404 */     if (versionFile == null) {
/* 405 */       versionFile = SQLiteJDBCLoader.class.getResource("/META-INF/maven/org.xerial/sqlite-jdbc/VERSION");
/*     */     }
/*     */     
/* 408 */     String version = "unknown";
/*     */     try {
/* 410 */       if (versionFile != null) {
/* 411 */         Properties versionData = new Properties();
/* 412 */         versionData.load(versionFile.openStream());
/* 413 */         version = versionData.getProperty("version", version);
/* 414 */         version = version.trim().replaceAll("[^0-9\\.]", "");
/*     */       }
/*     */     
/* 417 */     } catch (IOException e) {
/* 418 */       System.err.println(e);
/*     */     } 
/* 420 */     return version;
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\sqlite\SQLiteJDBCLoader.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */