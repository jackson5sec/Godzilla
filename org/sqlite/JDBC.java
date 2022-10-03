/*     */ package org.sqlite;
/*     */ 
/*     */ import java.sql.Connection;
/*     */ import java.sql.Driver;
/*     */ import java.sql.DriverManager;
/*     */ import java.sql.DriverPropertyInfo;
/*     */ import java.sql.SQLException;
/*     */ import java.sql.SQLFeatureNotSupportedException;
/*     */ import java.util.Properties;
/*     */ import java.util.logging.Logger;
/*     */ import org.sqlite.jdbc4.JDBC4Connection;
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
/*     */ public class JDBC
/*     */   implements Driver
/*     */ {
/*     */   public static final String PREFIX = "jdbc:sqlite:";
/*     */   
/*     */   static {
/*     */     try {
/*  31 */       DriverManager.registerDriver(new JDBC());
/*     */     }
/*  33 */     catch (SQLException e) {
/*  34 */       e.printStackTrace();
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getMajorVersion() {
/*  42 */     return SQLiteJDBCLoader.getMajorVersion();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getMinorVersion() {
/*  49 */     return SQLiteJDBCLoader.getMinorVersion();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean jdbcCompliant() {
/*  56 */     return false;
/*     */   }
/*     */   
/*     */   public Logger getParentLogger() throws SQLFeatureNotSupportedException {
/*  60 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean acceptsURL(String url) {
/*  67 */     return isValidURL(url);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static boolean isValidURL(String url) {
/*  76 */     return (url != null && url.toLowerCase().startsWith("jdbc:sqlite:"));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public DriverPropertyInfo[] getPropertyInfo(String url, Properties info) throws SQLException {
/*  83 */     return SQLiteConfig.getDriverPropertyInfo();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Connection connect(String url, Properties info) throws SQLException {
/*  90 */     return createConnection(url, info);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static String extractAddress(String url) {
/*  99 */     return url.substring("jdbc:sqlite:".length());
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
/*     */   public static SQLiteConnection createConnection(String url, Properties prop) throws SQLException {
/* 111 */     if (!isValidURL(url)) {
/* 112 */       return null;
/*     */     }
/* 114 */     url = url.trim();
/* 115 */     return (SQLiteConnection)new JDBC4Connection(url, extractAddress(url), prop);
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\sqlite\JDBC.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */