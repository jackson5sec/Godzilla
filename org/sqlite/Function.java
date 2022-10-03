/*     */ package org.sqlite;
/*     */ 
/*     */ import java.sql.Connection;
/*     */ import java.sql.SQLException;
/*     */ import org.sqlite.core.DB;
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
/*     */ public abstract class Function
/*     */ {
/*     */   public static final int FLAG_DETERMINISTIC = 2048;
/*     */   private SQLiteConnection conn;
/*     */   private DB db;
/*  68 */   long context = 0L;
/*  69 */   long value = 0L;
/*  70 */   int args = 0;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static final void create(Connection conn, String name, Function f) throws SQLException {
/*  80 */     create(conn, name, f, 0);
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
/*     */   public static final void create(Connection conn, String name, Function f, int flags) throws SQLException {
/*  92 */     create(conn, name, f, -1, flags);
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
/*     */   public static final void create(Connection conn, String name, Function f, int nArgs, int flags) throws SQLException {
/* 105 */     if (conn == null || !(conn instanceof SQLiteConnection)) {
/* 106 */       throw new SQLException("connection must be to an SQLite db");
/*     */     }
/* 108 */     if (conn.isClosed()) {
/* 109 */       throw new SQLException("connection closed");
/*     */     }
/*     */     
/* 112 */     f.conn = (SQLiteConnection)conn;
/* 113 */     f.db = f.conn.getDatabase();
/*     */     
/* 115 */     if (nArgs < -1 || nArgs > 127) {
/* 116 */       throw new SQLException("invalid args provided: " + nArgs);
/*     */     }
/*     */     
/* 119 */     if (name == null || name.length() > 255) {
/* 120 */       throw new SQLException("invalid function name: '" + name + "'");
/*     */     }
/*     */     
/* 123 */     if (f.db.create_function(name, f, nArgs, flags) != 0) {
/* 124 */       throw new SQLException("error creating function");
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
/*     */   public static final void destroy(Connection conn, String name, int nArgs) throws SQLException {
/* 137 */     if (conn == null || !(conn instanceof SQLiteConnection)) {
/* 138 */       throw new SQLException("connection must be to an SQLite db");
/*     */     }
/* 140 */     ((SQLiteConnection)conn).getDatabase().destroy_function(name, nArgs);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static final void destroy(Connection conn, String name) throws SQLException {
/* 151 */     destroy(conn, name, -1);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected abstract void xFunc() throws SQLException;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected final synchronized int args() throws SQLException {
/* 168 */     checkContext(); return this.args;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected final synchronized void result(byte[] value) throws SQLException {
/* 175 */     checkContext(); this.db.result_blob(this.context, value);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected final synchronized void result(double value) throws SQLException {
/* 182 */     checkContext(); this.db.result_double(this.context, value);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected final synchronized void result(int value) throws SQLException {
/* 189 */     checkContext(); this.db.result_int(this.context, value);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected final synchronized void result(long value) throws SQLException {
/* 196 */     checkContext(); this.db.result_long(this.context, value);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected final synchronized void result() throws SQLException {
/* 202 */     checkContext(); this.db.result_null(this.context);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected final synchronized void result(String value) throws SQLException {
/* 209 */     checkContext(); this.db.result_text(this.context, value);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected final synchronized void error(String err) throws SQLException {
/* 216 */     checkContext(); this.db.result_error(this.context, err);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected final synchronized String value_text(int arg) throws SQLException {
/* 223 */     checkValue(arg); return this.db.value_text(this, arg);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected final synchronized byte[] value_blob(int arg) throws SQLException {
/* 230 */     checkValue(arg); return this.db.value_blob(this, arg);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected final synchronized double value_double(int arg) throws SQLException {
/* 237 */     checkValue(arg); return this.db.value_double(this, arg);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected final synchronized int value_int(int arg) throws SQLException {
/* 244 */     checkValue(arg); return this.db.value_int(this, arg);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected final synchronized long value_long(int arg) throws SQLException {
/* 251 */     checkValue(arg); return this.db.value_long(this, arg);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected final synchronized int value_type(int arg) throws SQLException {
/* 258 */     checkValue(arg); return this.db.value_type(this, arg);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void checkContext() throws SQLException {
/* 265 */     if (this.conn == null || this.conn.getDatabase() == null || this.context == 0L) {
/* 266 */       throw new SQLException("no context, not allowed to read value");
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void checkValue(int arg) throws SQLException {
/* 275 */     if (this.conn == null || this.conn.getDatabase() == null || this.value == 0L) {
/* 276 */       throw new SQLException("not in value access state");
/*     */     }
/* 278 */     if (arg >= this.args) {
/* 279 */       throw new SQLException("arg " + arg + " out bounds [0," + this.args + ")");
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static abstract class Aggregate
/*     */     extends Function
/*     */     implements Cloneable
/*     */   {
/*     */     protected final void xFunc() {}
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     protected abstract void xStep() throws SQLException;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     protected abstract void xFinal() throws SQLException;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public Object clone() throws CloneNotSupportedException {
/* 315 */       return super.clone();
/*     */     }
/*     */   }
/*     */   
/*     */   public static abstract class Window extends Aggregate {
/*     */     protected abstract void xInverse() throws SQLException;
/*     */     
/*     */     protected abstract void xValue() throws SQLException;
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\sqlite\Function.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */