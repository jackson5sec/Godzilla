/*     */ package org.sqlite.jdbc3;
/*     */ 
/*     */ import java.sql.BatchUpdateException;
/*     */ import java.sql.Connection;
/*     */ import java.sql.ResultSet;
/*     */ import java.sql.SQLException;
/*     */ import java.sql.SQLWarning;
/*     */ import org.sqlite.ExtendedCommand;
/*     */ import org.sqlite.SQLiteConnection;
/*     */ import org.sqlite.core.CoreStatement;
/*     */ import org.sqlite.core.DB;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class JDBC3Statement
/*     */   extends CoreStatement
/*     */ {
/*     */   protected JDBC3Statement(SQLiteConnection conn) {
/*  20 */     super(conn);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void close() throws SQLException {
/*  27 */     internalClose();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean execute(String sql) throws SQLException {
/*  34 */     internalClose();
/*     */     
/*  36 */     ExtendedCommand.SQLExtension ext = ExtendedCommand.parse(sql);
/*  37 */     if (ext != null) {
/*  38 */       ext.execute(this.conn.getDatabase());
/*     */       
/*  40 */       return false;
/*     */     } 
/*     */     
/*  43 */     this.sql = sql;
/*     */     
/*  45 */     this.conn.getDatabase().prepare(this);
/*  46 */     return exec();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ResultSet executeQuery(String sql, boolean closeStmt) throws SQLException {
/*  54 */     this.rs.closeStmt = closeStmt;
/*     */     
/*  56 */     return executeQuery(sql);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ResultSet executeQuery(String sql) throws SQLException {
/*  63 */     internalClose();
/*  64 */     this.sql = sql;
/*     */     
/*  66 */     this.conn.getDatabase().prepare(this);
/*     */     
/*  68 */     if (!exec()) {
/*  69 */       internalClose();
/*  70 */       throw new SQLException("query does not return ResultSet", "SQLITE_DONE", 101);
/*     */     } 
/*     */     
/*  73 */     return getResultSet();
/*     */   }
/*     */   
/*     */   static class BackupObserver
/*     */     implements DB.ProgressObserver {
/*     */     public void progress(int remaining, int pageCount) {
/*  79 */       System.out.println(String.format("remaining:%d, page count:%d", new Object[] { Integer.valueOf(remaining), Integer.valueOf(pageCount) }));
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int executeUpdate(String sql) throws SQLException {
/*  87 */     internalClose();
/*  88 */     this.sql = sql;
/*  89 */     DB db = this.conn.getDatabase();
/*     */     
/*  91 */     int changes = 0;
/*  92 */     ExtendedCommand.SQLExtension ext = ExtendedCommand.parse(sql);
/*  93 */     if (ext != null) {
/*     */       
/*  95 */       ext.execute(db);
/*     */     } else {
/*     */       
/*     */       try {
/*  99 */         changes = db.total_changes();
/*     */ 
/*     */         
/* 102 */         int statusCode = db._exec(sql);
/* 103 */         if (statusCode != 0) {
/* 104 */           throw DB.newSQLException(statusCode, "");
/*     */         }
/* 106 */         changes = db.total_changes() - changes;
/*     */       } finally {
/*     */         
/* 109 */         internalClose();
/*     */       } 
/*     */     } 
/* 112 */     return changes;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ResultSet getResultSet() throws SQLException {
/* 119 */     checkOpen();
/*     */     
/* 121 */     if (this.rs.isOpen()) {
/* 122 */       throw new SQLException("ResultSet already requested");
/*     */     }
/* 124 */     DB db = this.conn.getDatabase();
/*     */     
/* 126 */     if (db.column_count(this.pointer) == 0) {
/* 127 */       return null;
/*     */     }
/*     */     
/* 130 */     if (this.rs.colsMeta == null) {
/* 131 */       this.rs.colsMeta = db.column_names(this.pointer);
/*     */     }
/*     */     
/* 134 */     this.rs.cols = this.rs.colsMeta;
/* 135 */     this.rs.open = this.resultsWaiting;
/* 136 */     this.resultsWaiting = false;
/*     */     
/* 138 */     return (ResultSet)this.rs;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getUpdateCount() throws SQLException {
/* 148 */     DB db = this.conn.getDatabase();
/* 149 */     if (this.pointer != 0L && !this.rs.isOpen() && !this.resultsWaiting && db.column_count(this.pointer) == 0)
/* 150 */       return db.changes(); 
/* 151 */     return -1;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addBatch(String sql) throws SQLException {
/* 158 */     internalClose();
/* 159 */     if (this.batch == null || this.batchPos + 1 >= this.batch.length) {
/* 160 */       Object[] nb = new Object[Math.max(10, this.batchPos * 2)];
/* 161 */       if (this.batch != null)
/* 162 */         System.arraycopy(this.batch, 0, nb, 0, this.batch.length); 
/* 163 */       this.batch = nb;
/*     */     } 
/* 165 */     this.batch[this.batchPos++] = sql;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void clearBatch() throws SQLException {
/* 172 */     this.batchPos = 0;
/* 173 */     if (this.batch != null) {
/* 174 */       for (int i = 0; i < this.batch.length; i++) {
/* 175 */         this.batch[i] = null;
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public int[] executeBatch() throws SQLException {
/* 183 */     internalClose();
/* 184 */     if (this.batch == null || this.batchPos == 0) {
/* 185 */       return new int[0];
/*     */     }
/* 187 */     int[] changes = new int[this.batchPos];
/* 188 */     DB db = this.conn.getDatabase();
/* 189 */     synchronized (db) {
/*     */       try {
/* 191 */         for (int i = 0; i < changes.length; i++) {
/*     */           
/* 193 */           try { this.sql = (String)this.batch[i];
/* 194 */             db.prepare(this);
/* 195 */             changes[i] = db.executeUpdate(this, null);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */             
/* 201 */             db.finalize(this); } catch (SQLException e) { throw new BatchUpdateException("batch entry " + i + ": " + e.getMessage(), changes); } finally { db.finalize(this); }
/*     */         
/*     */         } 
/*     */       } finally {
/*     */         
/* 206 */         clearBatch();
/*     */       } 
/*     */     } 
/*     */     
/* 210 */     return changes;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setCursorName(String name) {}
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public SQLWarning getWarnings() throws SQLException {
/* 222 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void clearWarnings() throws SQLException {}
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Connection getConnection() throws SQLException {
/* 234 */     return (Connection)this.conn;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void cancel() throws SQLException {
/* 241 */     this.conn.getDatabase().interrupt();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getQueryTimeout() throws SQLException {
/* 248 */     return this.conn.getBusyTimeout();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setQueryTimeout(int seconds) throws SQLException {
/* 255 */     if (seconds < 0)
/* 256 */       throw new SQLException("query timeout must be >= 0"); 
/* 257 */     this.conn.setBusyTimeout(1000 * seconds);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getMaxRows() throws SQLException {
/* 266 */     return this.rs.maxRows;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setMaxRows(int max) throws SQLException {
/* 274 */     if (max < 0)
/* 275 */       throw new SQLException("max row count must be >= 0"); 
/* 276 */     this.rs.maxRows = max;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getMaxFieldSize() throws SQLException {
/* 283 */     return 0;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setMaxFieldSize(int max) throws SQLException {
/* 290 */     if (max < 0) {
/* 291 */       throw new SQLException("max field size " + max + " cannot be negative");
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public int getFetchSize() throws SQLException {
/* 298 */     return ((ResultSet)this.rs).getFetchSize();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setFetchSize(int r) throws SQLException {
/* 305 */     ((ResultSet)this.rs).setFetchSize(r);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getFetchDirection() throws SQLException {
/* 312 */     return ((ResultSet)this.rs).getFetchDirection();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setFetchDirection(int d) throws SQLException {
/* 319 */     ((ResultSet)this.rs).setFetchDirection(d);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ResultSet getGeneratedKeys() throws SQLException {
/* 329 */     return this.conn.getSQLiteDatabaseMetaData().getGeneratedKeys();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean getMoreResults() throws SQLException {
/* 337 */     return getMoreResults(0);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean getMoreResults(int c) throws SQLException {
/* 344 */     checkOpen();
/* 345 */     internalClose();
/* 346 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getResultSetConcurrency() throws SQLException {
/* 353 */     return 1007;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getResultSetHoldability() throws SQLException {
/* 360 */     return 2;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getResultSetType() throws SQLException {
/* 367 */     return 1003;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setEscapeProcessing(boolean enable) throws SQLException {
/* 374 */     if (enable) {
/* 375 */       throw unused();
/*     */     }
/*     */   }
/*     */   
/*     */   protected SQLException unused() {
/* 380 */     return new SQLException("not implemented by SQLite JDBC driver");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean execute(String sql, int[] colinds) throws SQLException {
/* 387 */     throw unused();
/*     */   } public boolean execute(String sql, String[] colnames) throws SQLException {
/* 389 */     throw unused();
/*     */   } public int executeUpdate(String sql, int autoKeys) throws SQLException {
/* 391 */     throw unused();
/*     */   } public int executeUpdate(String sql, int[] colinds) throws SQLException {
/* 393 */     throw unused();
/*     */   } public int executeUpdate(String sql, String[] cols) throws SQLException {
/* 395 */     throw unused();
/*     */   } public boolean execute(String sql, int autokeys) throws SQLException {
/* 397 */     throw unused();
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\sqlite\jdbc3\JDBC3Statement.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */