/*     */ package org.sqlite;
/*     */ 
/*     */ import java.io.File;
/*     */ import java.io.FileOutputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.net.MalformedURLException;
/*     */ import java.net.URISyntaxException;
/*     */ import java.net.URL;
/*     */ import java.sql.Connection;
/*     */ import java.sql.DatabaseMetaData;
/*     */ import java.sql.SQLException;
/*     */ import java.util.Properties;
/*     */ import java.util.concurrent.Executor;
/*     */ import org.sqlite.core.CoreDatabaseMetaData;
/*     */ import org.sqlite.core.DB;
/*     */ import org.sqlite.core.NativeDB;
/*     */ import org.sqlite.jdbc4.JDBC4DatabaseMetaData;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class SQLiteConnection
/*     */   implements Connection
/*     */ {
/*     */   private static final String RESOURCE_NAME_PREFIX = ":resource:";
/*     */   private final DB db;
/*  30 */   private CoreDatabaseMetaData meta = null;
/*     */ 
/*     */   
/*     */   private final SQLiteConnectionConfig connectionConfig;
/*     */ 
/*     */ 
/*     */   
/*     */   public SQLiteConnection(DB db) {
/*  38 */     this.db = db;
/*  39 */     this.connectionConfig = db.getConfig().newConnectionConfig();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public SQLiteConnection(String url, String fileName) throws SQLException {
/*  49 */     this(url, fileName, new Properties());
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
/*     */   public SQLiteConnection(String url, String fileName, Properties prop) throws SQLException {
/*  61 */     this.db = open(url, fileName, prop);
/*  62 */     SQLiteConfig config = this.db.getConfig();
/*  63 */     this.connectionConfig = this.db.getConfig().newConnectionConfig();
/*     */     
/*  65 */     config.apply(this);
/*     */   }
/*     */   
/*     */   public SQLiteConnectionConfig getConnectionConfig() {
/*  69 */     return this.connectionConfig;
/*     */   }
/*     */   
/*     */   public CoreDatabaseMetaData getSQLiteDatabaseMetaData() throws SQLException {
/*  73 */     checkOpen();
/*     */     
/*  75 */     if (this.meta == null) {
/*  76 */       this.meta = (CoreDatabaseMetaData)new JDBC4DatabaseMetaData(this);
/*     */     }
/*     */     
/*  79 */     return this.meta;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public DatabaseMetaData getMetaData() throws SQLException {
/*  86 */     return (DatabaseMetaData)getSQLiteDatabaseMetaData();
/*     */   }
/*     */   
/*     */   public String getUrl() {
/*  90 */     return this.db.getUrl();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void setSchema(String schema) throws SQLException {}
/*     */ 
/*     */   
/*     */   public String getSchema() throws SQLException {
/*  99 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   public void abort(Executor executor) throws SQLException {}
/*     */ 
/*     */   
/*     */   public void setNetworkTimeout(Executor executor, int milliseconds) throws SQLException {}
/*     */   
/*     */   public int getNetworkTimeout() throws SQLException {
/* 109 */     return 0;
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
/*     */ 
/*     */ 
/*     */   
/*     */   protected void checkCursor(int rst, int rsc, int rsh) throws SQLException {
/* 125 */     if (rst != 1003)
/* 126 */       throw new SQLException("SQLite only supports TYPE_FORWARD_ONLY cursors"); 
/* 127 */     if (rsc != 1007)
/* 128 */       throw new SQLException("SQLite only supports CONCUR_READ_ONLY cursors"); 
/* 129 */     if (rsh != 2) {
/* 130 */       throw new SQLException("SQLite only supports closing cursors at commit");
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void setTransactionMode(SQLiteConfig.TransactionMode mode) {
/* 139 */     this.connectionConfig.setTransactionMode(mode);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getTransactionIsolation() {
/* 147 */     return this.connectionConfig.getTransactionIsolation();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setTransactionIsolation(int level) throws SQLException {
/* 154 */     checkOpen();
/*     */     
/* 156 */     switch (level) {
/*     */       case 8:
/* 158 */         getDatabase().exec("PRAGMA read_uncommitted = false;", getAutoCommit());
/*     */         break;
/*     */       case 1:
/* 161 */         getDatabase().exec("PRAGMA read_uncommitted = true;", getAutoCommit());
/*     */         break;
/*     */       default:
/* 164 */         throw new SQLException("SQLite supports only TRANSACTION_SERIALIZABLE and TRANSACTION_READ_UNCOMMITTED.");
/*     */     } 
/* 166 */     this.connectionConfig.setTransactionIsolation(level);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static DB open(String url, String origFileName, Properties props) throws SQLException {
/*     */     NativeDB nativeDB;
/* 176 */     Properties newProps = new Properties();
/* 177 */     newProps.putAll(props);
/*     */ 
/*     */     
/* 180 */     String fileName = extractPragmasFromFilename(url, origFileName, newProps);
/* 181 */     SQLiteConfig config = new SQLiteConfig(newProps);
/*     */ 
/*     */     
/* 184 */     if (!fileName.isEmpty() && !":memory:".equals(fileName) && !fileName.startsWith("file:") && !fileName.contains("mode=memory")) {
/* 185 */       if (fileName.startsWith(":resource:")) {
/* 186 */         String resourceName = fileName.substring(":resource:".length());
/*     */ 
/*     */         
/* 189 */         ClassLoader contextCL = Thread.currentThread().getContextClassLoader();
/* 190 */         URL resourceAddr = contextCL.getResource(resourceName);
/* 191 */         if (resourceAddr == null) {
/*     */           try {
/* 193 */             resourceAddr = new URL(resourceName);
/*     */           }
/* 195 */           catch (MalformedURLException e) {
/* 196 */             throw new SQLException(String.format("resource %s not found: %s", new Object[] { resourceName, e }));
/*     */           } 
/*     */         }
/*     */         
/*     */         try {
/* 201 */           fileName = extractResource(resourceAddr).getAbsolutePath();
/*     */         }
/* 203 */         catch (IOException e) {
/* 204 */           throw new SQLException(String.format("failed to load %s: %s", new Object[] { resourceName, e }));
/*     */         } 
/*     */       } else {
/*     */         
/* 208 */         File file = (new File(fileName)).getAbsoluteFile();
/* 209 */         File parent = file.getParentFile();
/* 210 */         if (parent != null && !parent.exists()) {
/* 211 */           for (File up = parent; up != null && !up.exists(); ) {
/* 212 */             parent = up;
/* 213 */             up = up.getParentFile();
/*     */           } 
/* 215 */           throw new SQLException("path to '" + fileName + "': '" + parent + "' does not exist");
/*     */         } 
/*     */ 
/*     */ 
/*     */ 
/*     */         
/*     */         try {
/* 222 */           if (!file.exists() && file.createNewFile()) {
/* 223 */             file.delete();
/*     */           }
/* 225 */         } catch (Exception e) {
/* 226 */           throw new SQLException("opening db: '" + fileName + "': " + e.getMessage());
/*     */         } 
/* 228 */         fileName = file.getAbsolutePath();
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/* 233 */     DB db = null;
/*     */     try {
/* 235 */       NativeDB.load();
/* 236 */       nativeDB = new NativeDB(url, fileName, config);
/*     */     }
/* 238 */     catch (Exception e) {
/* 239 */       SQLException err = new SQLException("Error opening connection");
/* 240 */       err.initCause(e);
/* 241 */       throw err;
/*     */     } 
/* 243 */     nativeDB.open(fileName, config.getOpenModeFlags());
/* 244 */     return (DB)nativeDB;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static File extractResource(URL resourceAddr) throws IOException {
/* 254 */     if (resourceAddr.getProtocol().equals("file")) {
/*     */       try {
/* 256 */         return new File(resourceAddr.toURI());
/*     */       }
/* 258 */       catch (URISyntaxException e) {
/* 259 */         throw new IOException(e.getMessage());
/*     */       } 
/*     */     }
/*     */     
/* 263 */     String tempFolder = (new File(System.getProperty("java.io.tmpdir"))).getAbsolutePath();
/* 264 */     String dbFileName = String.format("sqlite-jdbc-tmp-%d.db", new Object[] { Integer.valueOf(resourceAddr.hashCode()) });
/* 265 */     File dbFile = new File(tempFolder, dbFileName);
/*     */     
/* 267 */     if (dbFile.exists()) {
/* 268 */       long resourceLastModified = resourceAddr.openConnection().getLastModified();
/* 269 */       long tmpFileLastModified = dbFile.lastModified();
/* 270 */       if (resourceLastModified < tmpFileLastModified) {
/* 271 */         return dbFile;
/*     */       }
/*     */ 
/*     */       
/* 275 */       boolean deletionSucceeded = dbFile.delete();
/* 276 */       if (!deletionSucceeded) {
/* 277 */         throw new IOException("failed to remove existing DB file: " + dbFile.getAbsolutePath());
/*     */       }
/*     */     } 
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
/* 291 */     byte[] buffer = new byte[8192];
/* 292 */     FileOutputStream writer = new FileOutputStream(dbFile);
/* 293 */     InputStream reader = resourceAddr.openStream();
/*     */     try {
/* 295 */       int bytesRead = 0;
/* 296 */       while ((bytesRead = reader.read(buffer)) != -1) {
/* 297 */         writer.write(buffer, 0, bytesRead);
/*     */       }
/* 299 */       return dbFile;
/*     */     } finally {
/*     */       
/* 302 */       writer.close();
/* 303 */       reader.close();
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public DB getDatabase() {
/* 309 */     return this.db;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean getAutoCommit() throws SQLException {
/* 317 */     checkOpen();
/*     */     
/* 319 */     return this.connectionConfig.isAutoCommit();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setAutoCommit(boolean ac) throws SQLException {
/* 327 */     checkOpen();
/* 328 */     if (this.connectionConfig.isAutoCommit() == ac) {
/*     */       return;
/*     */     }
/* 331 */     this.connectionConfig.setAutoCommit(ac);
/* 332 */     this.db.exec(this.connectionConfig.isAutoCommit() ? "commit;" : this.connectionConfig.transactionPrefix(), ac);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getBusyTimeout() {
/* 340 */     return this.db.getConfig().getBusyTimeout();
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
/*     */   public void setBusyTimeout(int timeoutMillis) throws SQLException {
/* 353 */     this.db.getConfig().setBusyTimeout(timeoutMillis);
/* 354 */     this.db.busy_timeout(timeoutMillis);
/*     */   }
/*     */   
/*     */   public void setLimit(SQLiteLimits limit, int value) throws SQLException {
/* 358 */     this.db.limit(limit.getId(), value);
/*     */   }
/*     */   
/*     */   public void getLimit(SQLiteLimits limit) throws SQLException {
/* 362 */     this.db.limit(limit.getId(), -1);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isClosed() throws SQLException {
/* 368 */     return this.db.isClosed();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void close() throws SQLException {
/* 376 */     if (isClosed())
/*     */       return; 
/* 378 */     if (this.meta != null) {
/* 379 */       this.meta.close();
/*     */     }
/* 381 */     this.db.close();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void checkOpen() throws SQLException {
/* 389 */     if (isClosed()) {
/* 390 */       throw new SQLException("database connection closed");
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String libversion() throws SQLException {
/* 399 */     checkOpen();
/*     */     
/* 401 */     return this.db.libversion();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void commit() throws SQLException {
/* 409 */     checkOpen();
/* 410 */     if (this.connectionConfig.isAutoCommit())
/* 411 */       throw new SQLException("database in auto-commit mode"); 
/* 412 */     this.db.exec("commit;", getAutoCommit());
/* 413 */     this.db.exec(this.connectionConfig.transactionPrefix(), getAutoCommit());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void rollback() throws SQLException {
/* 421 */     checkOpen();
/* 422 */     if (this.connectionConfig.isAutoCommit())
/* 423 */       throw new SQLException("database in auto-commit mode"); 
/* 424 */     this.db.exec("rollback;", getAutoCommit());
/* 425 */     this.db.exec(this.connectionConfig.transactionPrefix(), getAutoCommit());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addUpdateListener(SQLiteUpdateListener listener) {
/* 434 */     this.db.addUpdateListener(listener);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void removeUpdateListener(SQLiteUpdateListener listener) {
/* 443 */     this.db.removeUpdateListener(listener);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addCommitListener(SQLiteCommitListener listener) {
/* 453 */     this.db.addCommitListener(listener);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void removeCommitListener(SQLiteCommitListener listener) {
/* 462 */     this.db.removeCommitListener(listener);
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
/*     */   
/*     */   protected static String extractPragmasFromFilename(String url, String filename, Properties prop) throws SQLException {
/* 476 */     int parameterDelimiter = filename.indexOf('?');
/* 477 */     if (parameterDelimiter == -1)
/*     */     {
/* 479 */       return filename;
/*     */     }
/*     */     
/* 482 */     StringBuilder sb = new StringBuilder();
/* 483 */     sb.append(filename.substring(0, parameterDelimiter));
/*     */     
/* 485 */     int nonPragmaCount = 0;
/* 486 */     String[] parameters = filename.substring(parameterDelimiter + 1).split("&");
/* 487 */     for (int i = 0; i < parameters.length; i++) {
/*     */       
/* 489 */       String parameter = parameters[parameters.length - 1 - i].trim();
/*     */       
/* 491 */       if (!parameter.isEmpty()) {
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 496 */         String[] kvp = parameter.split("=");
/* 497 */         String key = kvp[0].trim().toLowerCase();
/* 498 */         if (SQLiteConfig.pragmaSet.contains(key)) {
/* 499 */           if (kvp.length == 1) {
/* 500 */             throw new SQLException(String.format("Please specify a value for PRAGMA %s in URL %s", new Object[] { key, url }));
/*     */           }
/* 502 */           String value = kvp[1].trim();
/* 503 */           if (!value.isEmpty() && 
/* 504 */             !prop.containsKey(key))
/*     */           {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */             
/* 514 */             prop.setProperty(key, value);
/*     */           }
/*     */         }
/*     */         else {
/*     */           
/* 519 */           sb.append((nonPragmaCount == 0) ? 63 : 38);
/* 520 */           sb.append(parameter);
/* 521 */           nonPragmaCount++;
/*     */         } 
/*     */       } 
/*     */     } 
/* 525 */     String newFilename = sb.toString();
/* 526 */     return newFilename;
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\sqlite\SQLiteConnection.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */