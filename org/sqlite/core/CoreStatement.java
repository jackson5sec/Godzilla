/*     */ package org.sqlite.core;
/*     */ 
/*     */ import java.sql.ResultSet;
/*     */ import java.sql.SQLException;
/*     */ import org.sqlite.SQLiteConnection;
/*     */ import org.sqlite.SQLiteConnectionConfig;
/*     */ import org.sqlite.jdbc4.JDBC4ResultSet;
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
/*     */ public abstract class CoreStatement
/*     */   implements Codes
/*     */ {
/*     */   public final SQLiteConnection conn;
/*     */   protected final CoreResultSet rs;
/*     */   public long pointer;
/*  31 */   protected String sql = null;
/*     */   
/*     */   protected int batchPos;
/*  34 */   protected Object[] batch = null;
/*     */   protected boolean resultsWaiting = false;
/*     */   
/*     */   protected CoreStatement(SQLiteConnection c) {
/*  38 */     this.conn = c;
/*  39 */     this.rs = (CoreResultSet)new JDBC4ResultSet(this);
/*     */   }
/*     */   
/*     */   public DB getDatbase() {
/*  43 */     return this.conn.getDatabase();
/*     */   }
/*     */   
/*     */   public SQLiteConnectionConfig getConnectionConfig() {
/*  47 */     return this.conn.getConnectionConfig();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected final void checkOpen() throws SQLException {
/*  54 */     if (this.pointer == 0L) {
/*  55 */       throw new SQLException("statement is not executing");
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   boolean isOpen() throws SQLException {
/*  63 */     return (this.pointer != 0L);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected boolean exec() throws SQLException {
/*  72 */     if (this.sql == null)
/*  73 */       throw new SQLException("SQLiteJDBC internal error: sql==null"); 
/*  74 */     if (this.rs.isOpen()) {
/*  75 */       throw new SQLException("SQLite JDBC internal error: rs.isOpen() on exec.");
/*     */     }
/*  77 */     boolean success = false;
/*  78 */     boolean rc = false;
/*     */     try {
/*  80 */       rc = this.conn.getDatabase().execute(this, (Object[])null);
/*  81 */       success = true;
/*     */     } finally {
/*     */       
/*  84 */       this.resultsWaiting = rc;
/*  85 */       if (!success) this.conn.getDatabase().finalize(this);
/*     */     
/*     */     } 
/*  88 */     return (this.conn.getDatabase().column_count(this.pointer) != 0);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected boolean exec(String sql) throws SQLException {
/*  99 */     if (sql == null)
/* 100 */       throw new SQLException("SQLiteJDBC internal error: sql==null"); 
/* 101 */     if (this.rs.isOpen()) {
/* 102 */       throw new SQLException("SQLite JDBC internal error: rs.isOpen() on exec.");
/*     */     }
/* 104 */     boolean rc = false;
/* 105 */     boolean success = false;
/*     */     try {
/* 107 */       rc = this.conn.getDatabase().execute(sql, this.conn.getAutoCommit());
/* 108 */       success = true;
/*     */     } finally {
/*     */       
/* 111 */       this.resultsWaiting = rc;
/* 112 */       if (!success) this.conn.getDatabase().finalize(this);
/*     */     
/*     */     } 
/* 115 */     return (this.conn.getDatabase().column_count(this.pointer) != 0);
/*     */   }
/*     */   
/*     */   protected void internalClose() throws SQLException {
/* 119 */     if (this.pointer == 0L)
/*     */       return; 
/* 121 */     if (this.conn.isClosed()) {
/* 122 */       throw DB.newSQLException(1, "Connection is closed");
/*     */     }
/* 124 */     this.rs.close();
/*     */     
/* 126 */     this.batch = null;
/* 127 */     this.batchPos = 0;
/* 128 */     int resp = this.conn.getDatabase().finalize(this);
/*     */     
/* 130 */     if (resp != 0 && resp != 21)
/* 131 */       this.conn.getDatabase().throwex(resp); 
/*     */   }
/*     */   
/*     */   public abstract ResultSet executeQuery(String paramString, boolean paramBoolean) throws SQLException;
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\sqlite\core\CoreStatement.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */