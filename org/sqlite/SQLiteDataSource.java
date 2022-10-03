/*     */ package org.sqlite;
/*     */ 
/*     */ import java.io.PrintWriter;
/*     */ import java.sql.Connection;
/*     */ import java.sql.SQLException;
/*     */ import java.sql.SQLFeatureNotSupportedException;
/*     */ import java.util.Properties;
/*     */ import java.util.logging.Logger;
/*     */ import javax.sql.DataSource;
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
/*     */ public class SQLiteDataSource
/*     */   implements DataSource
/*     */ {
/*     */   private SQLiteConfig config;
/*     */   private transient PrintWriter logger;
/*  47 */   private int loginTimeout = 1;
/*     */   
/*  49 */   private String url = "jdbc:sqlite:";
/*  50 */   private String databaseName = "";
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public SQLiteDataSource() {
/*  56 */     this.config = new SQLiteConfig();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public SQLiteDataSource(SQLiteConfig config) {
/*  64 */     this.config = config;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setConfig(SQLiteConfig config) {
/*  72 */     this.config = config;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public SQLiteConfig getConfig() {
/*  79 */     return this.config;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setUrl(String url) {
/*  87 */     this.url = url;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getUrl() {
/*  94 */     return this.url;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setDatabaseName(String databaseName) {
/* 102 */     this.databaseName = databaseName;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getDatabaseName() {
/* 110 */     return this.databaseName;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setSharedCache(boolean enable) {
/* 120 */     this.config.setSharedCache(enable);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setLoadExtension(boolean enable) {
/* 129 */     this.config.enableLoadExtension(enable);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setReadOnly(boolean readOnly) {
/* 138 */     this.config.setReadOnly(readOnly);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setCacheSize(int numberOfPages) {
/* 148 */     this.config.setCacheSize(numberOfPages);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setCaseSensitiveLike(boolean enable) {
/* 157 */     this.config.enableCaseSensitiveLike(enable);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setCountChanges(boolean enable) {
/* 167 */     this.config.enableCountChanges(enable);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setDefaultCacheSize(int numberOfPages) {
/* 177 */     this.config.setDefaultCacheSize(numberOfPages);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setEncoding(String encoding) {
/* 188 */     this.config.setEncoding(SQLiteConfig.Encoding.getEncoding(encoding));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setEnforceForeignKeys(boolean enforce) {
/* 198 */     this.config.enforceForeignKeys(enforce);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setFullColumnNames(boolean enable) {
/* 209 */     this.config.enableFullColumnNames(enable);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setFullSync(boolean enable) {
/* 219 */     this.config.enableFullSync(enable);
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
/*     */   public void setIncrementalVacuum(int numberOfPagesToBeRemoved) {
/* 231 */     this.config.incrementalVacuum(numberOfPagesToBeRemoved);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setJournalMode(String mode) {
/* 242 */     this.config.setJournalMode(SQLiteConfig.JournalMode.valueOf(mode));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setJournalSizeLimit(int limit) {
/* 253 */     this.config.setJounalSizeLimit(limit);
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
/*     */   public void setLegacyFileFormat(boolean use) {
/* 266 */     this.config.useLegacyFileFormat(use);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setLockingMode(String mode) {
/* 276 */     this.config.setLockingMode(SQLiteConfig.LockingMode.valueOf(mode));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setPageSize(int numBytes) {
/* 286 */     this.config.setPageSize(numBytes);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setMaxPageCount(int numPages) {
/* 296 */     this.config.setMaxPageCount(numPages);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setReadUncommited(boolean useReadUncommitedIsolationMode) {
/* 305 */     this.config.setReadUncommited(useReadUncommitedIsolationMode);
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
/*     */   public void setRecursiveTriggers(boolean enable) {
/* 317 */     this.config.enableRecursiveTriggers(enable);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setReverseUnorderedSelects(boolean enable) {
/* 328 */     this.config.enableReverseUnorderedSelects(enable);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setShortColumnNames(boolean enable) {
/* 339 */     this.config.enableShortColumnNames(enable);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setSynchronous(String mode) {
/* 349 */     this.config.setSynchronous(SQLiteConfig.SynchronousMode.valueOf(mode));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setTempStore(String storeType) {
/* 359 */     this.config.setTempStore(SQLiteConfig.TempStore.valueOf(storeType));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setTempStoreDirectory(String directoryName) {
/* 370 */     this.config.setTempStoreDirectory(directoryName);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setTransactionMode(String transactionMode) {
/* 379 */     this.config.setTransactionMode(transactionMode);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setUserVersion(int version) {
/* 389 */     this.config.setUserVersion(version);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Connection getConnection() throws SQLException {
/* 398 */     return getConnection((String)null, (String)null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public SQLiteConnection getConnection(String username, String password) throws SQLException {
/* 405 */     Properties p = this.config.toProperties();
/* 406 */     if (username != null)
/* 407 */       p.put("user", username); 
/* 408 */     if (password != null)
/* 409 */       p.put("pass", password); 
/* 410 */     return JDBC.createConnection(this.url, p);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public PrintWriter getLogWriter() throws SQLException {
/* 417 */     return this.logger;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getLoginTimeout() throws SQLException {
/* 424 */     return this.loginTimeout;
/*     */   }
/*     */   
/*     */   public Logger getParentLogger() throws SQLFeatureNotSupportedException {
/* 428 */     throw new SQLFeatureNotSupportedException("getParentLogger");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setLogWriter(PrintWriter out) throws SQLException {
/* 435 */     this.logger = out;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setLoginTimeout(int seconds) throws SQLException {
/* 442 */     this.loginTimeout = seconds;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isWrapperFor(Class<?> iface) throws SQLException {
/* 452 */     return iface.isInstance(this);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public <T> T unwrap(Class<T> iface) throws SQLException {
/* 463 */     return (T)this;
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\sqlite\SQLiteDataSource.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */