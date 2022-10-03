/*     */ package org.sqlite.core;
/*     */ 
/*     */ import java.sql.SQLException;
/*     */ import java.sql.Statement;
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ import org.sqlite.SQLiteConnectionConfig;
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
/*     */ public abstract class CoreResultSet
/*     */   implements Codes
/*     */ {
/*     */   protected final CoreStatement stmt;
/*     */   public boolean open = false;
/*     */   public int maxRows;
/*  34 */   public String[] cols = null;
/*  35 */   public String[] colsMeta = null;
/*  36 */   protected boolean[][] meta = (boolean[][])null;
/*     */   
/*     */   protected int limitRows;
/*  39 */   protected int row = 0;
/*     */   
/*     */   protected int lastCol;
/*     */   public boolean closeStmt;
/*  43 */   protected Map<String, Integer> columnNameToIndex = null;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected CoreResultSet(CoreStatement stmt) {
/*  50 */     this.stmt = stmt;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected DB getDatabase() {
/*  56 */     return this.stmt.getDatbase();
/*     */   }
/*     */   
/*     */   protected SQLiteConnectionConfig getConnectionConfig() {
/*  60 */     return this.stmt.getConnectionConfig();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isOpen() {
/*  68 */     return this.open;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void checkOpen() throws SQLException {
/*  75 */     if (!this.open) {
/*  76 */       throw new SQLException("ResultSet closed");
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int checkCol(int col) throws SQLException {
/*  87 */     if (this.colsMeta == null) {
/*  88 */       throw new IllegalStateException("SQLite JDBC: inconsistent internal state");
/*     */     }
/*  90 */     if (col < 1 || col > this.colsMeta.length) {
/*  91 */       throw new SQLException("column " + col + " out of bounds [1," + this.colsMeta.length + "]");
/*     */     }
/*  93 */     return --col;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected int markCol(int col) throws SQLException {
/* 103 */     checkOpen();
/* 104 */     checkCol(col);
/* 105 */     this.lastCol = col;
/* 106 */     return --col;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void checkMeta() throws SQLException {
/* 113 */     checkCol(1);
/* 114 */     if (this.meta == null) {
/* 115 */       this.meta = this.stmt.getDatbase().column_metadata(this.stmt.pointer);
/*     */     }
/*     */   }
/*     */   
/*     */   public void close() throws SQLException {
/* 120 */     this.cols = null;
/* 121 */     this.colsMeta = null;
/* 122 */     this.meta = (boolean[][])null;
/* 123 */     this.limitRows = 0;
/* 124 */     this.row = 0;
/* 125 */     this.lastCol = -1;
/* 126 */     this.columnNameToIndex = null;
/*     */     
/* 128 */     if (!this.open) {
/*     */       return;
/*     */     }
/*     */     
/* 132 */     DB db = this.stmt.getDatbase();
/* 133 */     synchronized (db) {
/* 134 */       if (this.stmt.pointer != 0L) {
/* 135 */         db.reset(this.stmt.pointer);
/*     */         
/* 137 */         if (this.closeStmt) {
/* 138 */           this.closeStmt = false;
/* 139 */           ((Statement)this.stmt).close();
/*     */         } 
/*     */       } 
/*     */     } 
/*     */     
/* 144 */     this.open = false;
/*     */   }
/*     */   
/*     */   protected Integer findColumnIndexInCache(String col) {
/* 148 */     if (this.columnNameToIndex == null) {
/* 149 */       return null;
/*     */     }
/* 151 */     return this.columnNameToIndex.get(col);
/*     */   }
/*     */   
/*     */   protected int addColumnIndexInCache(String col, int index) {
/* 155 */     if (this.columnNameToIndex == null) {
/* 156 */       this.columnNameToIndex = new HashMap<>(this.cols.length);
/*     */     }
/* 158 */     this.columnNameToIndex.put(col, Integer.valueOf(index));
/* 159 */     return index;
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\sqlite\core\CoreResultSet.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */