/*     */ package org.sqlite.jdbc4;
/*     */ 
/*     */ import java.io.ByteArrayInputStream;
/*     */ import java.io.InputStream;
/*     */ import java.io.OutputStream;
/*     */ import java.io.Reader;
/*     */ import java.io.StringReader;
/*     */ import java.io.UnsupportedEncodingException;
/*     */ import java.io.Writer;
/*     */ import java.math.BigDecimal;
/*     */ import java.net.URL;
/*     */ import java.sql.Array;
/*     */ import java.sql.Blob;
/*     */ import java.sql.Clob;
/*     */ import java.sql.Date;
/*     */ import java.sql.NClob;
/*     */ import java.sql.Ref;
/*     */ import java.sql.ResultSet;
/*     */ import java.sql.ResultSetMetaData;
/*     */ import java.sql.RowId;
/*     */ import java.sql.SQLException;
/*     */ import java.sql.SQLFeatureNotSupportedException;
/*     */ import java.sql.SQLXML;
/*     */ import java.sql.Time;
/*     */ import java.sql.Timestamp;
/*     */ import java.util.Map;
/*     */ import org.sqlite.core.CoreStatement;
/*     */ import org.sqlite.jdbc3.JDBC3ResultSet;
/*     */ 
/*     */ public class JDBC4ResultSet
/*     */   extends JDBC3ResultSet
/*     */   implements ResultSet, ResultSetMetaData {
/*     */   public JDBC4ResultSet(CoreStatement stmt) {
/*  34 */     super(stmt);
/*     */   }
/*     */ 
/*     */   
/*     */   public void close() throws SQLException {
/*  39 */     boolean wasOpen = isOpen();
/*  40 */     super.close();
/*     */     
/*  42 */     if (wasOpen && this.stmt instanceof JDBC4Statement) {
/*  43 */       JDBC4Statement stat = (JDBC4Statement)this.stmt;
/*     */       
/*  45 */       if (stat.closeOnCompletion && !stat.isClosed()) {
/*  46 */         stat.close();
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public <T> T unwrap(Class<T> iface) throws ClassCastException {
/*  53 */     return iface.cast(this);
/*     */   }
/*     */   
/*     */   public boolean isWrapperFor(Class<?> iface) {
/*  57 */     return iface.isInstance(this);
/*     */   }
/*     */ 
/*     */   
/*     */   public RowId getRowId(int columnIndex) throws SQLException {
/*  62 */     throw new SQLFeatureNotSupportedException();
/*     */   }
/*     */ 
/*     */   
/*     */   public RowId getRowId(String columnLabel) throws SQLException {
/*  67 */     throw new SQLFeatureNotSupportedException();
/*     */   }
/*     */ 
/*     */   
/*     */   public void updateRowId(int columnIndex, RowId x) throws SQLException {
/*  72 */     throw new SQLFeatureNotSupportedException();
/*     */   }
/*     */ 
/*     */   
/*     */   public void updateRowId(String columnLabel, RowId x) throws SQLException {
/*  77 */     throw new SQLFeatureNotSupportedException();
/*     */   }
/*     */ 
/*     */   
/*     */   public int getHoldability() throws SQLException {
/*  82 */     return 0;
/*     */   }
/*     */   
/*     */   public boolean isClosed() throws SQLException {
/*  86 */     return !isOpen();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void updateNString(int columnIndex, String nString) throws SQLException {
/*  92 */     throw new SQLFeatureNotSupportedException();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void updateNString(String columnLabel, String nString) throws SQLException {
/*  98 */     throw new SQLFeatureNotSupportedException();
/*     */   }
/*     */ 
/*     */   
/*     */   public void updateNClob(int columnIndex, NClob nClob) throws SQLException {
/* 103 */     throw new SQLFeatureNotSupportedException();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void updateNClob(String columnLabel, NClob nClob) throws SQLException {
/* 109 */     throw new SQLFeatureNotSupportedException();
/*     */   }
/*     */ 
/*     */   
/*     */   public NClob getNClob(int columnIndex) throws SQLException {
/* 114 */     throw new SQLFeatureNotSupportedException();
/*     */   }
/*     */ 
/*     */   
/*     */   public NClob getNClob(String columnLabel) throws SQLException {
/* 119 */     throw new SQLFeatureNotSupportedException();
/*     */   }
/*     */ 
/*     */   
/*     */   public SQLXML getSQLXML(int columnIndex) throws SQLException {
/* 124 */     throw new SQLFeatureNotSupportedException();
/*     */   }
/*     */ 
/*     */   
/*     */   public SQLXML getSQLXML(String columnLabel) throws SQLException {
/* 129 */     throw new SQLFeatureNotSupportedException();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void updateSQLXML(int columnIndex, SQLXML xmlObject) throws SQLException {
/* 135 */     throw new SQLFeatureNotSupportedException();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void updateSQLXML(String columnLabel, SQLXML xmlObject) throws SQLException {
/* 141 */     throw new SQLFeatureNotSupportedException();
/*     */   }
/*     */ 
/*     */   
/*     */   public String getNString(int columnIndex) throws SQLException {
/* 146 */     throw new SQLFeatureNotSupportedException();
/*     */   }
/*     */ 
/*     */   
/*     */   public String getNString(String columnLabel) throws SQLException {
/* 151 */     throw new SQLFeatureNotSupportedException();
/*     */   }
/*     */   
/*     */   public Reader getNCharacterStream(int col) throws SQLException {
/* 155 */     String data = getString(col);
/* 156 */     return getNCharacterStreamInternal(data);
/*     */   }
/*     */   
/*     */   private Reader getNCharacterStreamInternal(String data) {
/* 160 */     if (data == null) {
/* 161 */       return null;
/*     */     }
/* 163 */     Reader reader = new StringReader(data);
/* 164 */     return reader;
/*     */   }
/*     */   
/*     */   public Reader getNCharacterStream(String col) throws SQLException {
/* 168 */     String data = getString(col);
/* 169 */     return getNCharacterStreamInternal(data);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void updateNCharacterStream(int columnIndex, Reader x, long length) throws SQLException {
/* 175 */     throw new SQLFeatureNotSupportedException();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void updateNCharacterStream(String columnLabel, Reader reader, long length) throws SQLException {
/* 181 */     throw new SQLFeatureNotSupportedException();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void updateAsciiStream(int columnIndex, InputStream x, long length) throws SQLException {
/* 187 */     throw new SQLFeatureNotSupportedException();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void updateBinaryStream(int columnIndex, InputStream x, long length) throws SQLException {
/* 193 */     throw new SQLFeatureNotSupportedException();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void updateCharacterStream(int columnIndex, Reader x, long length) throws SQLException {
/* 199 */     throw new SQLFeatureNotSupportedException();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void updateAsciiStream(String columnLabel, InputStream x, long length) throws SQLException {
/* 205 */     throw new SQLFeatureNotSupportedException();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void updateBinaryStream(String columnLabel, InputStream x, long length) throws SQLException {
/* 211 */     throw new SQLFeatureNotSupportedException();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void updateCharacterStream(String columnLabel, Reader reader, long length) throws SQLException {
/* 217 */     throw new SQLFeatureNotSupportedException();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void updateBlob(int columnIndex, InputStream inputStream, long length) throws SQLException {
/* 223 */     throw new SQLFeatureNotSupportedException();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void updateBlob(String columnLabel, InputStream inputStream, long length) throws SQLException {
/* 229 */     throw new SQLFeatureNotSupportedException();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void updateClob(int columnIndex, Reader reader, long length) throws SQLException {
/* 235 */     throw new SQLFeatureNotSupportedException();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void updateClob(String columnLabel, Reader reader, long length) throws SQLException {
/* 241 */     throw new SQLFeatureNotSupportedException();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void updateNClob(int columnIndex, Reader reader, long length) throws SQLException {
/* 247 */     throw new SQLFeatureNotSupportedException();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void updateNClob(String columnLabel, Reader reader, long length) throws SQLException {
/* 253 */     throw new SQLFeatureNotSupportedException();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void updateNCharacterStream(int columnIndex, Reader x) throws SQLException {
/* 259 */     throw new SQLFeatureNotSupportedException();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void updateNCharacterStream(String columnLabel, Reader reader) throws SQLException {
/* 265 */     throw new SQLFeatureNotSupportedException();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void updateAsciiStream(int columnIndex, InputStream x) throws SQLException {
/* 271 */     throw new SQLFeatureNotSupportedException();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void updateBinaryStream(int columnIndex, InputStream x) throws SQLException {
/* 277 */     throw new SQLFeatureNotSupportedException();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void updateCharacterStream(int columnIndex, Reader x) throws SQLException {
/* 283 */     throw new SQLFeatureNotSupportedException();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void updateAsciiStream(String columnLabel, InputStream x) throws SQLException {
/* 289 */     throw new SQLFeatureNotSupportedException();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void updateBinaryStream(String columnLabel, InputStream x) throws SQLException {
/* 295 */     throw new SQLFeatureNotSupportedException();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void updateCharacterStream(String columnLabel, Reader reader) throws SQLException {
/* 301 */     throw new SQLFeatureNotSupportedException();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void updateBlob(int columnIndex, InputStream inputStream) throws SQLException {
/* 307 */     throw new SQLFeatureNotSupportedException();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void updateBlob(String columnLabel, InputStream inputStream) throws SQLException {
/* 313 */     throw new SQLFeatureNotSupportedException();
/*     */   }
/*     */ 
/*     */   
/*     */   public void updateClob(int columnIndex, Reader reader) throws SQLException {
/* 318 */     throw new SQLFeatureNotSupportedException();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void updateClob(String columnLabel, Reader reader) throws SQLException {
/* 324 */     throw new SQLFeatureNotSupportedException();
/*     */   }
/*     */ 
/*     */   
/*     */   public void updateNClob(int columnIndex, Reader reader) throws SQLException {
/* 329 */     throw new SQLFeatureNotSupportedException();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void updateNClob(String columnLabel, Reader reader) throws SQLException {
/* 335 */     throw new SQLFeatureNotSupportedException();
/*     */   }
/*     */   
/*     */   public <T> T getObject(int columnIndex, Class<T> type) throws SQLException {
/* 339 */     throw new SQLFeatureNotSupportedException();
/*     */   }
/*     */   
/*     */   public <T> T getObject(String columnLabel, Class<T> type) throws SQLException {
/* 343 */     throw new SQLFeatureNotSupportedException();
/*     */   }
/*     */   
/*     */   protected SQLException unused() {
/* 347 */     return new SQLFeatureNotSupportedException();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Array getArray(int i) throws SQLException {
/* 354 */     throw unused();
/*     */   } public Array getArray(String col) throws SQLException {
/* 356 */     throw unused();
/*     */   }
/*     */   public InputStream getAsciiStream(int col) throws SQLException {
/* 359 */     String data = getString(col);
/* 360 */     return getAsciiStreamInternal(data);
/*     */   }
/*     */   
/*     */   public InputStream getAsciiStream(String col) throws SQLException {
/* 364 */     String data = getString(col);
/* 365 */     return getAsciiStreamInternal(data);
/*     */   }
/*     */   private InputStream getAsciiStreamInternal(String data) {
/*     */     InputStream inputStream;
/* 369 */     if (data == null) {
/* 370 */       return null;
/*     */     }
/*     */     
/*     */     try {
/* 374 */       inputStream = new ByteArrayInputStream(data.getBytes("ASCII"));
/* 375 */     } catch (UnsupportedEncodingException e) {
/* 376 */       return null;
/*     */     } 
/* 378 */     return inputStream;
/*     */   }
/*     */   
/*     */   @Deprecated
/*     */   public BigDecimal getBigDecimal(int col, int s) throws SQLException {
/* 383 */     throw unused();
/*     */   } @Deprecated
/*     */   public BigDecimal getBigDecimal(String col, int s) throws SQLException {
/* 386 */     throw unused();
/*     */   } public Blob getBlob(int col) throws SQLException {
/* 388 */     throw unused();
/*     */   } public Blob getBlob(String col) throws SQLException {
/* 390 */     throw unused();
/*     */   }
/*     */   public Clob getClob(int col) throws SQLException {
/* 393 */     return new SqliteClob(getString(col));
/*     */   }
/*     */   
/*     */   public Clob getClob(String col) throws SQLException {
/* 397 */     return new SqliteClob(getString(col));
/*     */   }
/*     */ 
/*     */   
/*     */   public Object getObject(int col, Map map) throws SQLException {
/* 402 */     throw unused();
/*     */   }
/*     */   public Object getObject(String col, Map map) throws SQLException {
/* 405 */     throw unused();
/*     */   } public Ref getRef(int i) throws SQLException {
/* 407 */     throw unused();
/*     */   } public Ref getRef(String col) throws SQLException {
/* 409 */     throw unused();
/*     */   }
/*     */   public InputStream getUnicodeStream(int col) throws SQLException {
/* 412 */     return getAsciiStream(col);
/*     */   }
/*     */   
/*     */   public InputStream getUnicodeStream(String col) throws SQLException {
/* 416 */     return getAsciiStream(col);
/*     */   }
/*     */   
/*     */   public URL getURL(int col) throws SQLException {
/* 420 */     throw unused();
/*     */   } public URL getURL(String col) throws SQLException {
/* 422 */     throw unused();
/*     */   }
/*     */   public void insertRow() throws SQLException {
/* 425 */     throw new SQLException("ResultSet is TYPE_FORWARD_ONLY");
/*     */   } public void moveToCurrentRow() throws SQLException {
/* 427 */     throw new SQLException("ResultSet is TYPE_FORWARD_ONLY");
/*     */   } public void moveToInsertRow() throws SQLException {
/* 429 */     throw new SQLException("ResultSet is TYPE_FORWARD_ONLY");
/*     */   } public boolean last() throws SQLException {
/* 431 */     throw new SQLException("ResultSet is TYPE_FORWARD_ONLY");
/*     */   } public boolean previous() throws SQLException {
/* 433 */     throw new SQLException("ResultSet is TYPE_FORWARD_ONLY");
/*     */   } public boolean relative(int rows) throws SQLException {
/* 435 */     throw new SQLException("ResultSet is TYPE_FORWARD_ONLY");
/*     */   } public boolean absolute(int row) throws SQLException {
/* 437 */     throw new SQLException("ResultSet is TYPE_FORWARD_ONLY");
/*     */   } public void afterLast() throws SQLException {
/* 439 */     throw new SQLException("ResultSet is TYPE_FORWARD_ONLY");
/*     */   } public void beforeFirst() throws SQLException {
/* 441 */     throw new SQLException("ResultSet is TYPE_FORWARD_ONLY");
/*     */   } public boolean first() throws SQLException {
/* 443 */     throw new SQLException("ResultSet is TYPE_FORWARD_ONLY");
/*     */   }
/*     */   public void cancelRowUpdates() throws SQLException {
/* 446 */     throw unused();
/*     */   } public void deleteRow() throws SQLException {
/* 448 */     throw unused();
/*     */   }
/*     */   public void updateArray(int col, Array x) throws SQLException {
/* 451 */     throw unused();
/*     */   } public void updateArray(String col, Array x) throws SQLException {
/* 453 */     throw unused();
/*     */   } public void updateAsciiStream(int col, InputStream x, int l) throws SQLException {
/* 455 */     throw unused();
/*     */   } public void updateAsciiStream(String col, InputStream x, int l) throws SQLException {
/* 457 */     throw unused();
/*     */   } public void updateBigDecimal(int col, BigDecimal x) throws SQLException {
/* 459 */     throw unused();
/*     */   } public void updateBigDecimal(String col, BigDecimal x) throws SQLException {
/* 461 */     throw unused();
/*     */   } public void updateBinaryStream(int c, InputStream x, int l) throws SQLException {
/* 463 */     throw unused();
/*     */   } public void updateBinaryStream(String c, InputStream x, int l) throws SQLException {
/* 465 */     throw unused();
/*     */   } public void updateBlob(int col, Blob x) throws SQLException {
/* 467 */     throw unused();
/*     */   } public void updateBlob(String col, Blob x) throws SQLException {
/* 469 */     throw unused();
/*     */   } public void updateBoolean(int col, boolean x) throws SQLException {
/* 471 */     throw unused();
/*     */   } public void updateBoolean(String col, boolean x) throws SQLException {
/* 473 */     throw unused();
/*     */   } public void updateByte(int col, byte x) throws SQLException {
/* 475 */     throw unused();
/*     */   } public void updateByte(String col, byte x) throws SQLException {
/* 477 */     throw unused();
/*     */   } public void updateBytes(int col, byte[] x) throws SQLException {
/* 479 */     throw unused();
/*     */   } public void updateBytes(String col, byte[] x) throws SQLException {
/* 481 */     throw unused();
/*     */   } public void updateCharacterStream(int c, Reader x, int l) throws SQLException {
/* 483 */     throw unused();
/*     */   } public void updateCharacterStream(String c, Reader r, int l) throws SQLException {
/* 485 */     throw unused();
/*     */   } public void updateClob(int col, Clob x) throws SQLException {
/* 487 */     throw unused();
/*     */   } public void updateClob(String col, Clob x) throws SQLException {
/* 489 */     throw unused();
/*     */   } public void updateDate(int col, Date x) throws SQLException {
/* 491 */     throw unused();
/*     */   } public void updateDate(String col, Date x) throws SQLException {
/* 493 */     throw unused();
/*     */   } public void updateDouble(int col, double x) throws SQLException {
/* 495 */     throw unused();
/*     */   } public void updateDouble(String col, double x) throws SQLException {
/* 497 */     throw unused();
/*     */   } public void updateFloat(int col, float x) throws SQLException {
/* 499 */     throw unused();
/*     */   } public void updateFloat(String col, float x) throws SQLException {
/* 501 */     throw unused();
/*     */   } public void updateInt(int col, int x) throws SQLException {
/* 503 */     throw unused();
/*     */   } public void updateInt(String col, int x) throws SQLException {
/* 505 */     throw unused();
/*     */   } public void updateLong(int col, long x) throws SQLException {
/* 507 */     throw unused();
/*     */   } public void updateLong(String col, long x) throws SQLException {
/* 509 */     throw unused();
/*     */   } public void updateNull(int col) throws SQLException {
/* 511 */     throw unused();
/*     */   } public void updateNull(String col) throws SQLException {
/* 513 */     throw unused();
/*     */   } public void updateObject(int c, Object x) throws SQLException {
/* 515 */     throw unused();
/*     */   } public void updateObject(int c, Object x, int s) throws SQLException {
/* 517 */     throw unused();
/*     */   } public void updateObject(String col, Object x) throws SQLException {
/* 519 */     throw unused();
/*     */   } public void updateObject(String c, Object x, int s) throws SQLException {
/* 521 */     throw unused();
/*     */   } public void updateRef(int col, Ref x) throws SQLException {
/* 523 */     throw unused();
/*     */   } public void updateRef(String c, Ref x) throws SQLException {
/* 525 */     throw unused();
/*     */   } public void updateRow() throws SQLException {
/* 527 */     throw unused();
/*     */   } public void updateShort(int c, short x) throws SQLException {
/* 529 */     throw unused();
/*     */   } public void updateShort(String c, short x) throws SQLException {
/* 531 */     throw unused();
/*     */   } public void updateString(int c, String x) throws SQLException {
/* 533 */     throw unused();
/*     */   } public void updateString(String c, String x) throws SQLException {
/* 535 */     throw unused();
/*     */   } public void updateTime(int c, Time x) throws SQLException {
/* 537 */     throw unused();
/*     */   } public void updateTime(String c, Time x) throws SQLException {
/* 539 */     throw unused();
/*     */   } public void updateTimestamp(int c, Timestamp x) throws SQLException {
/* 541 */     throw unused();
/*     */   } public void updateTimestamp(String c, Timestamp x) throws SQLException {
/* 543 */     throw unused();
/*     */   }
/*     */   public void refreshRow() throws SQLException {
/* 546 */     throw unused();
/*     */   }
/*     */   
/*     */   class SqliteClob
/*     */     implements NClob {
/*     */     private String data;
/*     */     
/*     */     protected SqliteClob(String data) {
/* 554 */       this.data = data;
/*     */     }
/*     */     
/*     */     public void free() throws SQLException {
/* 558 */       this.data = null;
/*     */     }
/*     */     
/*     */     public InputStream getAsciiStream() throws SQLException {
/* 562 */       return JDBC4ResultSet.this.getAsciiStreamInternal(this.data);
/*     */     }
/*     */     
/*     */     public Reader getCharacterStream() throws SQLException {
/* 566 */       return JDBC4ResultSet.this.getNCharacterStreamInternal(this.data);
/*     */     }
/*     */     
/*     */     public Reader getCharacterStream(long arg0, long arg1) throws SQLException {
/* 570 */       return JDBC4ResultSet.this.getNCharacterStreamInternal(this.data);
/*     */     }
/*     */     
/*     */     public String getSubString(long position, int length) throws SQLException {
/* 574 */       if (this.data == null) {
/* 575 */         throw new SQLException("no data");
/*     */       }
/* 577 */       if (position < 1L) {
/* 578 */         throw new SQLException("Position must be greater than or equal to 1");
/*     */       }
/* 580 */       if (length < 0) {
/* 581 */         throw new SQLException("Length must be greater than or equal to 0");
/*     */       }
/* 583 */       int start = (int)position - 1;
/* 584 */       return this.data.substring(start, Math.min(start + length, this.data.length()));
/*     */     }
/*     */     
/*     */     public long length() throws SQLException {
/* 588 */       if (this.data == null) {
/* 589 */         throw new SQLException("no data");
/*     */       }
/* 591 */       return this.data.length();
/*     */     }
/*     */     
/*     */     public long position(String arg0, long arg1) throws SQLException {
/* 595 */       JDBC4ResultSet.this.unused();
/* 596 */       return -1L;
/*     */     }
/*     */     
/*     */     public long position(Clob arg0, long arg1) throws SQLException {
/* 600 */       JDBC4ResultSet.this.unused();
/* 601 */       return -1L;
/*     */     }
/*     */     
/*     */     public OutputStream setAsciiStream(long arg0) throws SQLException {
/* 605 */       JDBC4ResultSet.this.unused();
/* 606 */       return null;
/*     */     }
/*     */     
/*     */     public Writer setCharacterStream(long arg0) throws SQLException {
/* 610 */       JDBC4ResultSet.this.unused();
/* 611 */       return null;
/*     */     }
/*     */     
/*     */     public int setString(long arg0, String arg1) throws SQLException {
/* 615 */       JDBC4ResultSet.this.unused();
/* 616 */       return -1;
/*     */     }
/*     */     
/*     */     public int setString(long arg0, String arg1, int arg2, int arg3) throws SQLException {
/* 620 */       JDBC4ResultSet.this.unused();
/* 621 */       return -1;
/*     */     }
/*     */     
/*     */     public void truncate(long arg0) throws SQLException {
/* 625 */       JDBC4ResultSet.this.unused();
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\sqlite\jdbc4\JDBC4ResultSet.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */