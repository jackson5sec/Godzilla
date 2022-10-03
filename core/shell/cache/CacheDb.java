/*     */ package core.shell.cache;
/*     */ 
/*     */ import core.ApplicationContext;
/*     */ import java.sql.Connection;
/*     */ import java.sql.DriverManager;
/*     */ import java.sql.PreparedStatement;
/*     */ import java.sql.ResultSet;
/*     */ import java.sql.SQLException;
/*     */ import java.sql.Statement;
/*     */ import util.Log;
/*     */ import util.functions;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class CacheDb
/*     */ {
/*     */   private static final String CREATE_CACHEREQUEST_TABLE = "create table cacheRequest(requestMd5 text not null,response blob not null,PRIMARY KEY (\"requestMd5\"));";
/*     */   private Connection dbConn;
/*     */   private static final String Drivde = "org.sqlite.JDBC";
/*     */   private String DB_URL;
/*     */   private String shellId;
/*     */   
/*     */   public CacheDb(String shellId) {
/*     */     try {
/*  26 */       Class.forName("org.sqlite.JDBC");
/*     */       
/*  28 */       this.DB_URL = String.format("jdbc:sqlite:%s/%s/cache.db", new Object[] { "GodzillaCache", shellId });
/*  29 */       this.dbConn = DriverManager.getConnection(this.DB_URL);
/*  30 */       this.dbConn.setAutoCommit(true);
/*  31 */       functions.addShutdownHook(CacheDb.class, this);
/*     */       
/*  33 */       if (!tableExists("cacheRequest")) {
/*  34 */         getPreparedStatement("create table cacheRequest(requestMd5 text not null,response blob not null,PRIMARY KEY (\"requestMd5\"));").execute();
/*     */       }
/*     */     }
/*  37 */     catch (Exception e) {
/*  38 */       Log.error(e);
/*     */     } 
/*     */   }
/*     */   
/*     */   public boolean tableExists(String tableName) {
/*  43 */     String selectTable = "SELECT COUNT(1) as result FROM sqlite_master WHERE name=?";
/*  44 */     boolean ret = false;
/*     */     try {
/*  46 */       PreparedStatement preparedStatement = getPreparedStatement(selectTable);
/*  47 */       preparedStatement.setString(1, tableName);
/*  48 */       ResultSet resultSet = preparedStatement.executeQuery();
/*  49 */       resultSet.next();
/*  50 */       int result = resultSet.getInt("result");
/*  51 */       if (result == 1) {
/*  52 */         ret = true;
/*     */       }
/*  54 */       resultSet.close();
/*  55 */       preparedStatement.close();
/*  56 */     } catch (Exception e) {
/*  57 */       Log.error(e);
/*     */     } 
/*     */     
/*  60 */     return ret;
/*     */   }
/*     */   
/*     */   public synchronized boolean addSetingKV(String key, byte[] value) {
/*  64 */     if (existsSetingKey(key)) {
/*  65 */       return updateSetingKV(key, value);
/*     */     }
/*  67 */     String updateSetingSql = "INSERT INTO cacheRequest (\"requestMd5\", \"response\") VALUES (?, ?)";
/*  68 */     PreparedStatement preparedStatement = getPreparedStatement(updateSetingSql);
/*     */     try {
/*  70 */       preparedStatement.setString(1, key);
/*  71 */       preparedStatement.setBytes(2, value);
/*  72 */       int affectNum = preparedStatement.executeUpdate();
/*  73 */       preparedStatement.close();
/*  74 */       return (affectNum > 0);
/*  75 */     } catch (Exception e) {
/*  76 */       e.printStackTrace();
/*  77 */       return false;
/*     */     } 
/*     */   }
/*     */   
/*     */   public synchronized boolean updateSetingKV(String key, byte[] value) {
/*  82 */     if (ApplicationContext.isOpenC("isSuperLog")) {
/*  83 */       Log.log(String.format("updateSetingKV key:%s value:%s", new Object[] { key, value }), new Object[0]);
/*     */     }
/*  85 */     if (existsSetingKey(key)) {
/*  86 */       String updateSetingSql = "UPDATE cacheRequest set response=? WHERE requestMd5=?";
/*  87 */       PreparedStatement preparedStatement = getPreparedStatement(updateSetingSql);
/*     */       try {
/*  89 */         preparedStatement.setBytes(1, value);
/*  90 */         preparedStatement.setString(2, key);
/*  91 */         int affectNum = preparedStatement.executeUpdate();
/*  92 */         preparedStatement.close();
/*  93 */         return (affectNum > 0);
/*  94 */       } catch (Exception e) {
/*  95 */         e.printStackTrace();
/*  96 */         return false;
/*     */       } 
/*     */     } 
/*  99 */     return addSetingKV(key, value);
/*     */   }
/*     */   
/*     */   public byte[] getSetingValue(String key) {
/* 103 */     String getSetingValueSql = "SELECT response FROM cacheRequest WHERE requestMd5=?";
/*     */     try {
/* 105 */       PreparedStatement preparedStatement = getPreparedStatement(getSetingValueSql);
/* 106 */       preparedStatement.setString(1, key);
/* 107 */       ResultSet resultSet = preparedStatement.executeQuery();
/* 108 */       byte[] value = resultSet.next() ? resultSet.getBytes("response") : null;
/* 109 */       resultSet.close();
/* 110 */       preparedStatement.close();
/* 111 */       return value;
/* 112 */     } catch (Exception e) {
/* 113 */       Log.error(e);
/* 114 */       return null;
/*     */     } 
/*     */   }
/*     */   public boolean existsSetingKey(String key) {
/* 118 */     String selectKeyNumSql = "SELECT COUNT(1) as c FROM cacheRequest WHERE requestMd5=?";
/*     */     try {
/* 120 */       PreparedStatement preparedStatement = getPreparedStatement(selectKeyNumSql);
/* 121 */       preparedStatement.setString(1, key);
/* 122 */       int c = preparedStatement.executeQuery().getInt("c");
/* 123 */       preparedStatement.close();
/* 124 */       return (c > 0);
/* 125 */     } catch (Exception e) {
/* 126 */       Log.error(e);
/* 127 */       return false;
/*     */     } 
/*     */   }
/*     */   public PreparedStatement getPreparedStatement(String sql) {
/* 131 */     if (this.dbConn != null) {
/*     */       try {
/* 133 */         return this.dbConn.prepareStatement(sql);
/* 134 */       } catch (SQLException e) {
/*     */         
/* 136 */         Log.error(e);
/* 137 */         return null;
/*     */       } 
/*     */     }
/* 140 */     return null;
/*     */   }
/*     */   
/*     */   public Statement getStatement() {
/* 144 */     if (this.dbConn != null) {
/*     */       try {
/* 146 */         return this.dbConn.createStatement();
/* 147 */       } catch (SQLException e) {
/* 148 */         Log.error(e);
/* 149 */         return null;
/*     */       } 
/*     */     }
/* 152 */     return null;
/*     */   }
/*     */   
/*     */   public void Tclose() {
/*     */     try {
/* 157 */       if (this.dbConn != null && !this.dbConn.isClosed()) {
/* 158 */         this.dbConn.close();
/*     */       }
/* 160 */     } catch (SQLException e) {
/* 161 */       Log.error(e);
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\core\shell\cache\CacheDb.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */