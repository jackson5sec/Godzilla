/*     */ package org.sqlite.jdbc3;
/*     */ 
/*     */ import java.sql.CallableStatement;
/*     */ import java.sql.PreparedStatement;
/*     */ import java.sql.SQLException;
/*     */ import java.sql.SQLWarning;
/*     */ import java.sql.Savepoint;
/*     */ import java.sql.Statement;
/*     */ import java.sql.Struct;
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ import java.util.Properties;
/*     */ import java.util.concurrent.atomic.AtomicInteger;
/*     */ import org.sqlite.SQLiteConnection;
/*     */ import org.sqlite.SQLiteOpenMode;
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class JDBC3Connection
/*     */   extends SQLiteConnection
/*     */ {
/*  22 */   private final AtomicInteger savePoint = new AtomicInteger(0);
/*     */   
/*     */   private Map<String, Class<?>> typeMap;
/*     */ 
/*     */   
/*     */   protected JDBC3Connection(String url, String fileName, Properties prop) throws SQLException {
/*  28 */     super(url, fileName, prop);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getCatalog() throws SQLException {
/*  37 */     checkOpen();
/*  38 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setCatalog(String catalog) throws SQLException {
/*  47 */     checkOpen();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getHoldability() throws SQLException {
/*  56 */     checkOpen();
/*  57 */     return 2;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setHoldability(int h) throws SQLException {
/*  66 */     checkOpen();
/*  67 */     if (h != 2) {
/*  68 */       throw new SQLException("SQLite only supports CLOSE_CURSORS_AT_COMMIT");
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Map<String, Class<?>> getTypeMap() throws SQLException {
/*  78 */     synchronized (this) {
/*  79 */       if (this.typeMap == null) {
/*  80 */         this.typeMap = new HashMap<>();
/*     */       }
/*     */       
/*  83 */       return this.typeMap;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setTypeMap(Map<String, Class<?>> map) throws SQLException {
/*  93 */     synchronized (this) {
/*  94 */       this.typeMap = map;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isReadOnly() throws SQLException {
/* 104 */     return ((getDatabase().getConfig().getOpenModeFlags() & SQLiteOpenMode.READONLY.flag) != 0);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setReadOnly(boolean ro) throws SQLException {
/* 114 */     if (ro != isReadOnly()) {
/* 115 */       throw new SQLException("Cannot change read-only flag after establishing a connection. Use SQLiteConfig#setReadOnly and SQLiteConfig.createConnection().");
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String nativeSQL(String sql) {
/* 126 */     return sql;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void clearWarnings() throws SQLException {}
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public SQLWarning getWarnings() throws SQLException {
/* 143 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Statement createStatement() throws SQLException {
/* 152 */     return createStatement(1003, 1007, 2);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Statement createStatement(int rsType, int rsConcurr) throws SQLException {
/* 162 */     return createStatement(rsType, rsConcurr, 2);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public abstract Statement createStatement(int paramInt1, int paramInt2, int paramInt3) throws SQLException;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public CallableStatement prepareCall(String sql) throws SQLException {
/* 177 */     return prepareCall(sql, 1003, 1007, 2);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public CallableStatement prepareCall(String sql, int rst, int rsc) throws SQLException {
/* 187 */     return prepareCall(sql, rst, rsc, 2);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public CallableStatement prepareCall(String sql, int rst, int rsc, int rsh) throws SQLException {
/* 196 */     throw new SQLException("SQLite does not support Stored Procedures");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public PreparedStatement prepareStatement(String sql) throws SQLException {
/* 205 */     return prepareStatement(sql, 1003, 1007);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public PreparedStatement prepareStatement(String sql, int autoC) throws SQLException {
/* 214 */     return prepareStatement(sql);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public PreparedStatement prepareStatement(String sql, int[] colInds) throws SQLException {
/* 223 */     return prepareStatement(sql);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public PreparedStatement prepareStatement(String sql, String[] colNames) throws SQLException {
/* 232 */     return prepareStatement(sql);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public PreparedStatement prepareStatement(String sql, int rst, int rsc) throws SQLException {
/* 241 */     return prepareStatement(sql, rst, rsc, 2);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public abstract PreparedStatement prepareStatement(String paramString, int paramInt1, int paramInt2, int paramInt3) throws SQLException;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Savepoint setSavepoint() throws SQLException {
/* 256 */     checkOpen();
/* 257 */     if (getAutoCommit())
/*     */     {
/*     */ 
/*     */ 
/*     */       
/* 262 */       getConnectionConfig().setAutoCommit(false);
/*     */     }
/* 264 */     Savepoint sp = new JDBC3Savepoint(this.savePoint.incrementAndGet());
/* 265 */     getDatabase().exec(String.format("SAVEPOINT %s", new Object[] { sp.getSavepointName() }), false);
/* 266 */     return sp;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Savepoint setSavepoint(String name) throws SQLException {
/* 275 */     checkOpen();
/* 276 */     if (getAutoCommit())
/*     */     {
/*     */ 
/*     */ 
/*     */       
/* 281 */       getConnectionConfig().setAutoCommit(false);
/*     */     }
/* 283 */     Savepoint sp = new JDBC3Savepoint(this.savePoint.incrementAndGet(), name);
/* 284 */     getDatabase().exec(String.format("SAVEPOINT %s", new Object[] { sp.getSavepointName() }), false);
/* 285 */     return sp;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void releaseSavepoint(Savepoint savepoint) throws SQLException {
/* 294 */     checkOpen();
/* 295 */     if (getAutoCommit()) {
/* 296 */       throw new SQLException("database in auto-commit mode");
/*     */     }
/* 298 */     getDatabase().exec(String.format("RELEASE SAVEPOINT %s", new Object[] { savepoint.getSavepointName() }), false);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void rollback(Savepoint savepoint) throws SQLException {
/* 307 */     checkOpen();
/* 308 */     if (getAutoCommit()) {
/* 309 */       throw new SQLException("database in auto-commit mode");
/*     */     }
/* 311 */     getDatabase().exec(String.format("ROLLBACK TO SAVEPOINT %s", new Object[] { savepoint.getSavepointName() }), getAutoCommit());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Struct createStruct(String t, Object[] attr) throws SQLException {
/* 319 */     throw new SQLException("unsupported by SQLite");
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\sqlite\jdbc3\JDBC3Connection.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */