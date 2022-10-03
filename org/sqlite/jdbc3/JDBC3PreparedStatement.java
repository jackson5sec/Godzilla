/*     */ package org.sqlite.jdbc3;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.Reader;
/*     */ import java.io.UnsupportedEncodingException;
/*     */ import java.math.BigDecimal;
/*     */ import java.net.URL;
/*     */ import java.sql.Array;
/*     */ import java.sql.Blob;
/*     */ import java.sql.Clob;
/*     */ import java.sql.Date;
/*     */ import java.sql.ParameterMetaData;
/*     */ import java.sql.Ref;
/*     */ import java.sql.ResultSet;
/*     */ import java.sql.ResultSetMetaData;
/*     */ import java.sql.SQLException;
/*     */ import java.sql.Statement;
/*     */ import java.sql.Time;
/*     */ import java.sql.Timestamp;
/*     */ import java.util.Calendar;
/*     */ import java.util.Date;
/*     */ import org.sqlite.SQLiteConnection;
/*     */ import org.sqlite.core.CorePreparedStatement;
/*     */ import org.sqlite.core.CoreStatement;
/*     */ 
/*     */ public abstract class JDBC3PreparedStatement
/*     */   extends CorePreparedStatement {
/*     */   protected JDBC3PreparedStatement(SQLiteConnection conn, String sql) throws SQLException {
/*  30 */     super(conn, sql);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void clearParameters() throws SQLException {
/*  37 */     checkOpen();
/*  38 */     this.conn.getDatabase().clear_bindings(this.pointer);
/*  39 */     if (this.batch != null) {
/*  40 */       for (int i = this.batchPos; i < this.batchPos + this.paramCount; i++) {
/*  41 */         this.batch[i] = null;
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean execute() throws SQLException {
/*  48 */     checkOpen();
/*  49 */     this.rs.close();
/*  50 */     this.conn.getDatabase().reset(this.pointer);
/*     */     
/*  52 */     boolean success = false;
/*     */     try {
/*  54 */       this.resultsWaiting = this.conn.getDatabase().execute((CoreStatement)this, this.batch);
/*  55 */       success = true;
/*  56 */       return (this.columnCount != 0);
/*     */     } finally {
/*  58 */       if (!success && this.pointer != 0L) this.conn.getDatabase().reset(this.pointer);
/*     */     
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public ResultSet executeQuery() throws SQLException {
/*  66 */     checkOpen();
/*     */     
/*  68 */     if (this.columnCount == 0) {
/*  69 */       throw new SQLException("Query does not return results");
/*     */     }
/*     */     
/*  72 */     this.rs.close();
/*  73 */     this.conn.getDatabase().reset(this.pointer);
/*     */     
/*  75 */     boolean success = false;
/*     */     try {
/*  77 */       this.resultsWaiting = this.conn.getDatabase().execute((CoreStatement)this, this.batch);
/*  78 */       success = true;
/*     */     } finally {
/*  80 */       if (!success && this.pointer != 0L) this.conn.getDatabase().reset(this.pointer); 
/*     */     } 
/*  82 */     return getResultSet();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int executeUpdate() throws SQLException {
/*  89 */     checkOpen();
/*     */     
/*  91 */     if (this.columnCount != 0) {
/*  92 */       throw new SQLException("Query returns results");
/*     */     }
/*     */     
/*  95 */     this.rs.close();
/*  96 */     this.conn.getDatabase().reset(this.pointer);
/*     */     
/*  98 */     return this.conn.getDatabase().executeUpdate((CoreStatement)this, this.batch);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addBatch() throws SQLException {
/* 105 */     checkOpen();
/* 106 */     this.batchPos += this.paramCount;
/* 107 */     this.batchQueryCount++;
/* 108 */     if (this.batch == null) {
/* 109 */       this.batch = new Object[this.paramCount];
/*     */     }
/* 111 */     if (this.batchPos + this.paramCount > this.batch.length) {
/* 112 */       Object[] nb = new Object[this.batch.length * 2];
/* 113 */       System.arraycopy(this.batch, 0, nb, 0, this.batch.length);
/* 114 */       this.batch = nb;
/*     */     } 
/* 116 */     System.arraycopy(this.batch, this.batchPos - this.paramCount, this.batch, this.batchPos, this.paramCount);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ParameterMetaData getParameterMetaData() {
/* 125 */     return (ParameterMetaData)this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getParameterCount() throws SQLException {
/* 132 */     checkOpen();
/* 133 */     return this.paramCount;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getParameterClassName(int param) throws SQLException {
/* 140 */     checkOpen();
/* 141 */     return "java.lang.String";
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getParameterTypeName(int pos) {
/* 148 */     return "VARCHAR";
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getParameterType(int pos) {
/* 155 */     return 12;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getParameterMode(int pos) {
/* 162 */     return 1;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getPrecision(int pos) {
/* 169 */     return 0;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getScale(int pos) {
/* 176 */     return 0;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int isNullable(int pos) {
/* 183 */     return 1;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isSigned(int pos) {
/* 190 */     return true;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Statement getStatement() {
/* 197 */     return (Statement)this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setBigDecimal(int pos, BigDecimal value) throws SQLException {
/* 204 */     batch(pos, (value == null) ? null : value.toString());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private byte[] readBytes(InputStream istream, int length) throws SQLException {
/* 215 */     if (length < 0) {
/* 216 */       SQLException exception = new SQLException("Error reading stream. Length should be non-negative");
/*     */ 
/*     */       
/* 219 */       throw exception;
/*     */     } 
/*     */     
/* 222 */     byte[] bytes = new byte[length];
/*     */ 
/*     */ 
/*     */     
/*     */     try {
/* 227 */       int totalBytesRead = 0;
/*     */       
/* 229 */       while (totalBytesRead < length) {
/* 230 */         int bytesRead = istream.read(bytes, totalBytesRead, length - totalBytesRead);
/* 231 */         if (bytesRead == -1) {
/* 232 */           throw new IOException("End of stream has been reached");
/*     */         }
/* 234 */         totalBytesRead += bytesRead;
/*     */       } 
/*     */       
/* 237 */       return bytes;
/*     */     }
/* 239 */     catch (IOException cause) {
/*     */       
/* 241 */       SQLException exception = new SQLException("Error reading stream");
/*     */       
/* 243 */       exception.initCause(cause);
/* 244 */       throw exception;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setBinaryStream(int pos, InputStream istream, int length) throws SQLException {
/* 252 */     if (istream == null && length == 0) {
/* 253 */       setBytes(pos, (byte[])null);
/*     */     }
/*     */     
/* 256 */     setBytes(pos, readBytes(istream, length));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setAsciiStream(int pos, InputStream istream, int length) throws SQLException {
/* 263 */     setUnicodeStream(pos, istream, length);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setUnicodeStream(int pos, InputStream istream, int length) throws SQLException {
/* 270 */     if (istream == null && length == 0) {
/* 271 */       setString(pos, (String)null);
/*     */     }
/*     */     
/*     */     try {
/* 275 */       setString(pos, new String(readBytes(istream, length), "UTF-8"));
/* 276 */     } catch (UnsupportedEncodingException e) {
/* 277 */       SQLException exception = new SQLException("UTF-8 is not supported");
/*     */       
/* 279 */       exception.initCause(e);
/* 280 */       throw exception;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setBoolean(int pos, boolean value) throws SQLException {
/* 288 */     setInt(pos, value ? 1 : 0);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setByte(int pos, byte value) throws SQLException {
/* 295 */     setInt(pos, value);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setBytes(int pos, byte[] value) throws SQLException {
/* 302 */     batch(pos, value);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setDouble(int pos, double value) throws SQLException {
/* 309 */     batch(pos, new Double(value));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setFloat(int pos, float value) throws SQLException {
/* 316 */     batch(pos, new Float(value));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setInt(int pos, int value) throws SQLException {
/* 323 */     batch(pos, new Integer(value));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setLong(int pos, long value) throws SQLException {
/* 330 */     batch(pos, new Long(value));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setNull(int pos, int u1) throws SQLException {
/* 337 */     setNull(pos, u1, (String)null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setNull(int pos, int u1, String u2) throws SQLException {
/* 344 */     batch(pos, null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setObject(int pos, Object value) throws SQLException {
/* 351 */     if (value == null) {
/* 352 */       batch(pos, null);
/*     */     }
/* 354 */     else if (value instanceof Date) {
/* 355 */       setDateByMilliseconds(pos, Long.valueOf(((Date)value).getTime()), Calendar.getInstance());
/*     */     }
/* 357 */     else if (value instanceof Long) {
/* 358 */       batch(pos, value);
/*     */     }
/* 360 */     else if (value instanceof Integer) {
/* 361 */       batch(pos, value);
/*     */     }
/* 363 */     else if (value instanceof Short) {
/* 364 */       batch(pos, new Integer(((Short)value).intValue()));
/*     */     }
/* 366 */     else if (value instanceof Float) {
/* 367 */       batch(pos, value);
/*     */     }
/* 369 */     else if (value instanceof Double) {
/* 370 */       batch(pos, value);
/*     */     }
/* 372 */     else if (value instanceof Boolean) {
/* 373 */       setBoolean(pos, ((Boolean)value).booleanValue());
/*     */     }
/* 375 */     else if (value instanceof byte[]) {
/* 376 */       batch(pos, value);
/*     */     }
/* 378 */     else if (value instanceof BigDecimal) {
/* 379 */       setBigDecimal(pos, (BigDecimal)value);
/*     */     } else {
/*     */       
/* 382 */       batch(pos, value.toString());
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setObject(int p, Object v, int t) throws SQLException {
/* 390 */     setObject(p, v);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setObject(int p, Object v, int t, int s) throws SQLException {
/* 397 */     setObject(p, v);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setShort(int pos, short value) throws SQLException {
/* 404 */     setInt(pos, value);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setString(int pos, String value) throws SQLException {
/* 411 */     batch(pos, value);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setCharacterStream(int pos, Reader reader, int length) throws SQLException {
/*     */     try {
/* 420 */       StringBuffer sb = new StringBuffer();
/* 421 */       char[] cbuf = new char[8192];
/*     */       
/*     */       int cnt;
/* 424 */       while ((cnt = reader.read(cbuf)) > 0) {
/* 425 */         sb.append(cbuf, 0, cnt);
/*     */       }
/*     */ 
/*     */       
/* 429 */       setString(pos, sb.toString());
/*     */     }
/* 431 */     catch (IOException e) {
/* 432 */       throw new SQLException("Cannot read from character stream, exception message: " + e.getMessage());
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setDate(int pos, Date x) throws SQLException {
/* 440 */     setDate(pos, x, Calendar.getInstance());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setDate(int pos, Date x, Calendar cal) throws SQLException {
/* 447 */     if (x == null) {
/* 448 */       setObject(pos, (Object)null);
/*     */     } else {
/* 450 */       setDateByMilliseconds(pos, Long.valueOf(x.getTime()), cal);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setTime(int pos, Time x) throws SQLException {
/* 459 */     setTime(pos, x, Calendar.getInstance());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setTime(int pos, Time x, Calendar cal) throws SQLException {
/* 466 */     if (x == null) {
/* 467 */       setObject(pos, (Object)null);
/*     */     } else {
/* 469 */       setDateByMilliseconds(pos, Long.valueOf(x.getTime()), cal);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setTimestamp(int pos, Timestamp x) throws SQLException {
/* 477 */     setTimestamp(pos, x, Calendar.getInstance());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setTimestamp(int pos, Timestamp x, Calendar cal) throws SQLException {
/* 484 */     if (x == null) {
/* 485 */       setObject(pos, (Object)null);
/*     */     } else {
/* 487 */       setDateByMilliseconds(pos, Long.valueOf(x.getTime()), cal);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ResultSetMetaData getMetaData() throws SQLException {
/* 495 */     checkOpen();
/* 496 */     return (ResultSetMetaData)this.rs;
/*     */   }
/*     */ 
/*     */   
/*     */   protected SQLException unused() {
/* 501 */     return new SQLException("not implemented by SQLite JDBC driver");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setArray(int i, Array x) throws SQLException {
/* 508 */     throw unused();
/*     */   }
/*     */   
/*     */   public void setBlob(int i, Blob x) throws SQLException {
/* 512 */     throw unused();
/*     */   } public void setClob(int i, Clob x) throws SQLException {
/* 514 */     throw unused();
/*     */   } public void setRef(int i, Ref x) throws SQLException {
/* 516 */     throw unused();
/*     */   } public void setURL(int pos, URL x) throws SQLException {
/* 518 */     throw unused();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean execute(String sql) throws SQLException {
/* 525 */     throw unused();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int executeUpdate(String sql) throws SQLException {
/* 533 */     throw unused();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ResultSet executeQuery(String sql) throws SQLException {
/* 541 */     throw unused();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addBatch(String sql) throws SQLException {
/* 548 */     throw unused();
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\sqlite\jdbc3\JDBC3PreparedStatement.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */