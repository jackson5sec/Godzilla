/*     */ package org.sqlite.core;
/*     */ 
/*     */ import java.sql.Date;
/*     */ import java.sql.SQLException;
/*     */ import java.util.Calendar;
/*     */ import org.sqlite.SQLiteConfig;
/*     */ import org.sqlite.SQLiteConnection;
/*     */ import org.sqlite.SQLiteConnectionConfig;
/*     */ import org.sqlite.date.FastDateFormat;
/*     */ import org.sqlite.jdbc4.JDBC4Statement;
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
/*     */ public abstract class CorePreparedStatement
/*     */   extends JDBC4Statement
/*     */ {
/*     */   protected int columnCount;
/*     */   protected int paramCount;
/*     */   protected int batchQueryCount;
/*     */   
/*     */   protected CorePreparedStatement(SQLiteConnection conn, String sql) throws SQLException {
/*  41 */     super(conn);
/*     */     
/*  43 */     this.sql = sql;
/*  44 */     DB db = conn.getDatabase();
/*  45 */     db.prepare((CoreStatement)this);
/*  46 */     this.rs.colsMeta = db.column_names(this.pointer);
/*  47 */     this.columnCount = db.column_count(this.pointer);
/*  48 */     this.paramCount = db.bind_parameter_count(this.pointer);
/*  49 */     this.batchQueryCount = 0;
/*  50 */     this.batch = null;
/*  51 */     this.batchPos = 0;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int[] executeBatch() throws SQLException {
/*  59 */     if (this.batchQueryCount == 0) {
/*  60 */       return new int[0];
/*     */     }
/*     */     
/*     */     try {
/*  64 */       return this.conn.getDatabase().executeBatch(this.pointer, this.batchQueryCount, this.batch, this.conn.getAutoCommit());
/*     */     } finally {
/*     */       
/*  67 */       clearBatch();
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void clearBatch() throws SQLException {
/*  76 */     super.clearBatch();
/*  77 */     this.batchQueryCount = 0;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getUpdateCount() throws SQLException {
/*  85 */     if (this.pointer == 0L || this.resultsWaiting || this.rs.isOpen()) {
/*  86 */       return -1;
/*     */     }
/*     */     
/*  89 */     return this.conn.getDatabase().changes();
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
/*     */   protected void batch(int pos, Object value) throws SQLException {
/* 102 */     checkOpen();
/* 103 */     if (this.batch == null) {
/* 104 */       this.batch = new Object[this.paramCount];
/*     */     }
/* 106 */     this.batch[this.batchPos + pos - 1] = value;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void setDateByMilliseconds(int pos, Long value, Calendar calendar) throws SQLException {
/* 114 */     SQLiteConnectionConfig config = this.conn.getConnectionConfig();
/* 115 */     switch (config.getDateClass()) {
/*     */       case TEXT:
/* 117 */         batch(pos, FastDateFormat.getInstance(config.getDateStringFormat(), calendar.getTimeZone()).format(new Date(value.longValue())));
/*     */         return;
/*     */ 
/*     */       
/*     */       case REAL:
/* 122 */         batch(pos, new Double(value.longValue() / 8.64E7D + 2440587.5D));
/*     */         return;
/*     */     } 
/*     */     
/* 126 */     batch(pos, new Long(value.longValue() / config.getDateMultiplier()));
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\sqlite\core\CorePreparedStatement.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */