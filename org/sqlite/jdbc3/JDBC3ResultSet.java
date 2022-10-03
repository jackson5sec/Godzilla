/*      */ package org.sqlite.jdbc3;
/*      */ 
/*      */ import java.io.ByteArrayInputStream;
/*      */ import java.io.InputStream;
/*      */ import java.io.Reader;
/*      */ import java.io.StringReader;
/*      */ import java.math.BigDecimal;
/*      */ import java.sql.Date;
/*      */ import java.sql.ResultSetMetaData;
/*      */ import java.sql.SQLException;
/*      */ import java.sql.SQLWarning;
/*      */ import java.sql.Statement;
/*      */ import java.sql.Time;
/*      */ import java.sql.Timestamp;
/*      */ import java.util.Calendar;
/*      */ import java.util.Locale;
/*      */ import java.util.regex.Matcher;
/*      */ import java.util.regex.Pattern;
/*      */ import org.sqlite.core.CoreResultSet;
/*      */ import org.sqlite.core.CoreStatement;
/*      */ import org.sqlite.core.DB;
/*      */ import org.sqlite.date.FastDateFormat;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ public abstract class JDBC3ResultSet
/*      */   extends CoreResultSet
/*      */ {
/*      */   protected JDBC3ResultSet(CoreStatement stmt) {
/*   31 */     super(stmt);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int findColumn(String col) throws SQLException {
/*   39 */     checkOpen();
/*   40 */     Integer index = findColumnIndexInCache(col);
/*   41 */     if (index != null) {
/*   42 */       return index.intValue();
/*      */     }
/*   44 */     for (int i = 0; i < this.cols.length; i++) {
/*   45 */       if (col.equalsIgnoreCase(this.cols[i])) {
/*   46 */         return addColumnIndexInCache(col, i + 1);
/*      */       }
/*      */     } 
/*   49 */     throw new SQLException("no such column: '" + col + "'");
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean next() throws SQLException {
/*   56 */     if (!this.open)
/*      */     {
/*   58 */       return false;
/*      */     }
/*   60 */     this.lastCol = -1;
/*      */ 
/*      */     
/*   63 */     if (this.row == 0) {
/*   64 */       this.row++;
/*   65 */       return true;
/*      */     } 
/*      */ 
/*      */     
/*   69 */     if (this.maxRows != 0 && this.row == this.maxRows) {
/*   70 */       return false;
/*      */     }
/*      */ 
/*      */     
/*   74 */     int statusCode = getDatabase().step(this.stmt.pointer);
/*   75 */     switch (statusCode) {
/*      */       case 101:
/*   77 */         close();
/*   78 */         return false;
/*      */       case 100:
/*   80 */         this.row++;
/*   81 */         return true;
/*      */     } 
/*      */     
/*   84 */     getDatabase().throwex(statusCode);
/*   85 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int getType() throws SQLException {
/*   93 */     return 1003;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int getFetchSize() throws SQLException {
/*  100 */     return this.limitRows;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setFetchSize(int rows) throws SQLException {
/*  107 */     if (0 > rows || (this.maxRows != 0 && rows > this.maxRows)) {
/*  108 */       throw new SQLException("fetch size " + rows + " out of bounds " + this.maxRows);
/*      */     }
/*  110 */     this.limitRows = rows;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int getFetchDirection() throws SQLException {
/*  117 */     checkOpen();
/*  118 */     return 1000;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setFetchDirection(int d) throws SQLException {
/*  125 */     checkOpen();
/*  126 */     if (d != 1000) {
/*  127 */       throw new SQLException("only FETCH_FORWARD direction supported");
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean isAfterLast() throws SQLException {
/*  135 */     return !this.open;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean isBeforeFirst() throws SQLException {
/*  142 */     return (this.open && this.row == 0);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean isFirst() throws SQLException {
/*  149 */     return (this.row == 1);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean isLast() throws SQLException {
/*  156 */     throw new SQLException("function not yet implemented for SQLite");
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int getRow() throws SQLException {
/*  163 */     return this.row;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean wasNull() throws SQLException {
/*  170 */     return (getDatabase().column_type(this.stmt.pointer, markCol(this.lastCol)) == 5);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public BigDecimal getBigDecimal(int col) throws SQLException {
/*  179 */     String stringValue = getString(col);
/*  180 */     if (stringValue == null) {
/*  181 */       return null;
/*      */     }
/*      */     
/*      */     try {
/*  185 */       return new BigDecimal(stringValue);
/*      */     }
/*  187 */     catch (NumberFormatException e) {
/*  188 */       throw new SQLException("Bad value for type BigDecimal : " + stringValue);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public BigDecimal getBigDecimal(String col) throws SQLException {
/*  197 */     return getBigDecimal(findColumn(col));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean getBoolean(int col) throws SQLException {
/*  204 */     return !(getInt(col) == 0);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean getBoolean(String col) throws SQLException {
/*  211 */     return getBoolean(findColumn(col));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public InputStream getBinaryStream(int col) throws SQLException {
/*  218 */     byte[] bytes = getBytes(col);
/*  219 */     if (bytes != null) {
/*  220 */       return new ByteArrayInputStream(bytes);
/*      */     }
/*      */     
/*  223 */     return null;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public InputStream getBinaryStream(String col) throws SQLException {
/*  231 */     return getBinaryStream(findColumn(col));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public byte getByte(int col) throws SQLException {
/*  238 */     return (byte)getInt(col);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public byte getByte(String col) throws SQLException {
/*  245 */     return getByte(findColumn(col));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public byte[] getBytes(int col) throws SQLException {
/*  252 */     return getDatabase().column_blob(this.stmt.pointer, markCol(col));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public byte[] getBytes(String col) throws SQLException {
/*  259 */     return getBytes(findColumn(col));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Reader getCharacterStream(int col) throws SQLException {
/*  266 */     String string = getString(col);
/*  267 */     return (string == null) ? null : new StringReader(string);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Reader getCharacterStream(String col) throws SQLException {
/*  274 */     return getCharacterStream(findColumn(col));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Date getDate(int col) throws SQLException {
/*  281 */     DB db = getDatabase();
/*  282 */     switch (db.column_type(this.stmt.pointer, markCol(col))) {
/*      */       case 5:
/*  284 */         return null;
/*      */       
/*      */       case 3:
/*      */         try {
/*  288 */           return new Date(getConnectionConfig().getDateFormat().parse(db.column_text(this.stmt.pointer, markCol(col))).getTime());
/*      */         }
/*  290 */         catch (Exception e) {
/*  291 */           SQLException error = new SQLException("Error parsing date");
/*  292 */           error.initCause(e);
/*      */           
/*  294 */           throw error;
/*      */         } 
/*      */       
/*      */       case 2:
/*  298 */         return new Date(julianDateToCalendar(Double.valueOf(db.column_double(this.stmt.pointer, markCol(col)))).getTimeInMillis());
/*      */     } 
/*      */     
/*  301 */     return new Date(db.column_long(this.stmt.pointer, markCol(col)) * getConnectionConfig().getDateMultiplier());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Date getDate(int col, Calendar cal) throws SQLException {
/*  309 */     checkCalendar(cal);
/*      */     
/*  311 */     DB db = getDatabase();
/*  312 */     switch (db.column_type(this.stmt.pointer, markCol(col))) {
/*      */       case 5:
/*  314 */         return null;
/*      */       
/*      */       case 3:
/*      */         try {
/*  318 */           FastDateFormat dateFormat = FastDateFormat.getInstance(getConnectionConfig().getDateStringFormat(), cal.getTimeZone());
/*      */           
/*  320 */           return new Date(dateFormat.parse(db.column_text(this.stmt.pointer, markCol(col))).getTime());
/*      */         }
/*  322 */         catch (Exception e) {
/*  323 */           SQLException error = new SQLException("Error parsing time stamp");
/*  324 */           error.initCause(e);
/*      */           
/*  326 */           throw error;
/*      */         } 
/*      */       
/*      */       case 2:
/*  330 */         return new Date(julianDateToCalendar(Double.valueOf(db.column_double(this.stmt.pointer, markCol(col))), cal).getTimeInMillis());
/*      */     } 
/*      */     
/*  333 */     cal.setTimeInMillis(db.column_long(this.stmt.pointer, markCol(col)) * getConnectionConfig().getDateMultiplier());
/*  334 */     return new Date(cal.getTime().getTime());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Date getDate(String col) throws SQLException {
/*  342 */     return getDate(findColumn(col), Calendar.getInstance());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Date getDate(String col, Calendar cal) throws SQLException {
/*  349 */     return getDate(findColumn(col), cal);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public double getDouble(int col) throws SQLException {
/*  356 */     DB db = getDatabase();
/*  357 */     if (db.column_type(this.stmt.pointer, markCol(col)) == 5) {
/*  358 */       return 0.0D;
/*      */     }
/*  360 */     return db.column_double(this.stmt.pointer, markCol(col));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public double getDouble(String col) throws SQLException {
/*  367 */     return getDouble(findColumn(col));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public float getFloat(int col) throws SQLException {
/*  374 */     DB db = getDatabase();
/*  375 */     if (db.column_type(this.stmt.pointer, markCol(col)) == 5) {
/*  376 */       return 0.0F;
/*      */     }
/*  378 */     return (float)db.column_double(this.stmt.pointer, markCol(col));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public float getFloat(String col) throws SQLException {
/*  385 */     return getFloat(findColumn(col));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int getInt(int col) throws SQLException {
/*  392 */     DB db = getDatabase();
/*  393 */     return db.column_int(this.stmt.pointer, markCol(col));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int getInt(String col) throws SQLException {
/*  400 */     return getInt(findColumn(col));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public long getLong(int col) throws SQLException {
/*  407 */     DB db = getDatabase();
/*  408 */     return db.column_long(this.stmt.pointer, markCol(col));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public long getLong(String col) throws SQLException {
/*  415 */     return getLong(findColumn(col));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public short getShort(int col) throws SQLException {
/*  422 */     return (short)getInt(col);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public short getShort(String col) throws SQLException {
/*  429 */     return getShort(findColumn(col));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String getString(int col) throws SQLException {
/*  436 */     DB db = getDatabase();
/*  437 */     return db.column_text(this.stmt.pointer, markCol(col));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String getString(String col) throws SQLException {
/*  444 */     return getString(findColumn(col));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Time getTime(int col) throws SQLException {
/*  451 */     DB db = getDatabase();
/*  452 */     switch (db.column_type(this.stmt.pointer, markCol(col))) {
/*      */       case 5:
/*  454 */         return null;
/*      */       
/*      */       case 3:
/*      */         try {
/*  458 */           return new Time(getConnectionConfig().getDateFormat().parse(db.column_text(this.stmt.pointer, markCol(col))).getTime());
/*      */         }
/*  460 */         catch (Exception e) {
/*  461 */           SQLException error = new SQLException("Error parsing time");
/*  462 */           error.initCause(e);
/*      */           
/*  464 */           throw error;
/*      */         } 
/*      */       
/*      */       case 2:
/*  468 */         return new Time(julianDateToCalendar(Double.valueOf(db.column_double(this.stmt.pointer, markCol(col)))).getTimeInMillis());
/*      */     } 
/*      */     
/*  471 */     return new Time(db.column_long(this.stmt.pointer, markCol(col)) * getConnectionConfig().getDateMultiplier());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Time getTime(int col, Calendar cal) throws SQLException {
/*  479 */     checkCalendar(cal);
/*  480 */     DB db = getDatabase();
/*  481 */     switch (db.column_type(this.stmt.pointer, markCol(col))) {
/*      */       case 5:
/*  483 */         return null;
/*      */       
/*      */       case 3:
/*      */         try {
/*  487 */           FastDateFormat dateFormat = FastDateFormat.getInstance(getConnectionConfig().getDateStringFormat(), cal.getTimeZone());
/*      */           
/*  489 */           return new Time(dateFormat.parse(db.column_text(this.stmt.pointer, markCol(col))).getTime());
/*      */         }
/*  491 */         catch (Exception e) {
/*  492 */           SQLException error = new SQLException("Error parsing time");
/*  493 */           error.initCause(e);
/*      */           
/*  495 */           throw error;
/*      */         } 
/*      */       
/*      */       case 2:
/*  499 */         return new Time(julianDateToCalendar(Double.valueOf(db.column_double(this.stmt.pointer, markCol(col))), cal).getTimeInMillis());
/*      */     } 
/*      */     
/*  502 */     cal.setTimeInMillis(db.column_long(this.stmt.pointer, markCol(col)) * getConnectionConfig().getDateMultiplier());
/*  503 */     return new Time(cal.getTime().getTime());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Time getTime(String col) throws SQLException {
/*  511 */     return getTime(findColumn(col));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Time getTime(String col, Calendar cal) throws SQLException {
/*  518 */     return getTime(findColumn(col), cal);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Timestamp getTimestamp(int col) throws SQLException {
/*  525 */     DB db = getDatabase();
/*  526 */     switch (db.column_type(this.stmt.pointer, markCol(col))) {
/*      */       case 5:
/*  528 */         return null;
/*      */       
/*      */       case 3:
/*      */         try {
/*  532 */           return new Timestamp(getConnectionConfig().getDateFormat().parse(db.column_text(this.stmt.pointer, markCol(col))).getTime());
/*      */         }
/*  534 */         catch (Exception e) {
/*  535 */           SQLException error = new SQLException("Error parsing time stamp");
/*  536 */           error.initCause(e);
/*      */           
/*  538 */           throw error;
/*      */         } 
/*      */       
/*      */       case 2:
/*  542 */         return new Timestamp(julianDateToCalendar(Double.valueOf(db.column_double(this.stmt.pointer, markCol(col)))).getTimeInMillis());
/*      */     } 
/*      */     
/*  545 */     return new Timestamp(db.column_long(this.stmt.pointer, markCol(col)) * getConnectionConfig().getDateMultiplier());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Timestamp getTimestamp(int col, Calendar cal) throws SQLException {
/*  553 */     if (cal == null) {
/*  554 */       return getTimestamp(col);
/*      */     }
/*      */     
/*  557 */     DB db = getDatabase();
/*  558 */     switch (db.column_type(this.stmt.pointer, markCol(col))) {
/*      */       case 5:
/*  560 */         return null;
/*      */       
/*      */       case 3:
/*      */         try {
/*  564 */           FastDateFormat dateFormat = FastDateFormat.getInstance(getConnectionConfig().getDateStringFormat(), cal.getTimeZone());
/*      */           
/*  566 */           return new Timestamp(dateFormat.parse(db.column_text(this.stmt.pointer, markCol(col))).getTime());
/*      */         }
/*  568 */         catch (Exception e) {
/*  569 */           SQLException error = new SQLException("Error parsing time stamp");
/*  570 */           error.initCause(e);
/*      */           
/*  572 */           throw error;
/*      */         } 
/*      */       
/*      */       case 2:
/*  576 */         return new Timestamp(julianDateToCalendar(Double.valueOf(db.column_double(this.stmt.pointer, markCol(col))), cal).getTimeInMillis());
/*      */     } 
/*      */     
/*  579 */     cal.setTimeInMillis(db.column_long(this.stmt.pointer, markCol(col)) * getConnectionConfig().getDateMultiplier());
/*      */     
/*  581 */     return new Timestamp(cal.getTime().getTime());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Timestamp getTimestamp(String col) throws SQLException {
/*  589 */     return getTimestamp(findColumn(col));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Timestamp getTimestamp(String c, Calendar ca) throws SQLException {
/*  596 */     return getTimestamp(findColumn(c), ca);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public Object getObject(int col) throws SQLException {
/*      */     long val;
/*  603 */     switch (getDatabase().column_type(this.stmt.pointer, markCol(col))) {
/*      */       case 1:
/*  605 */         val = getLong(col);
/*  606 */         if (val > 2147483647L || val < -2147483648L) {
/*  607 */           return new Long(val);
/*      */         }
/*      */         
/*  610 */         return new Integer((int)val);
/*      */       
/*      */       case 2:
/*  613 */         return new Double(getDouble(col));
/*      */       case 4:
/*  615 */         return getBytes(col);
/*      */       case 5:
/*  617 */         return null;
/*      */     } 
/*      */     
/*  620 */     return getString(col);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Object getObject(String col) throws SQLException {
/*  628 */     return getObject(findColumn(col));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Statement getStatement() {
/*  635 */     return (Statement)this.stmt;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String getCursorName() throws SQLException {
/*  642 */     return null;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public SQLWarning getWarnings() throws SQLException {
/*  649 */     return null;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void clearWarnings() throws SQLException {}
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  662 */   protected static final Pattern COLUMN_TYPENAME = Pattern.compile("([^\\(]*)");
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  667 */   protected static final Pattern COLUMN_TYPECAST = Pattern.compile("cast\\(.*?\\s+as\\s+(.*?)\\s*\\)");
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  672 */   protected static final Pattern COLUMN_PRECISION = Pattern.compile(".*?\\((.*?)\\)");
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public ResultSetMetaData getMetaData() throws SQLException {
/*  681 */     return (ResultSetMetaData)this;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String getCatalogName(int col) throws SQLException {
/*  688 */     return getDatabase().column_table_name(this.stmt.pointer, checkCol(col));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String getColumnClassName(int col) throws SQLException {
/*  695 */     checkCol(col);
/*  696 */     return "java.lang.Object";
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int getColumnCount() throws SQLException {
/*  703 */     checkCol(1);
/*  704 */     return this.colsMeta.length;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int getColumnDisplaySize(int col) throws SQLException {
/*  711 */     return Integer.MAX_VALUE;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String getColumnLabel(int col) throws SQLException {
/*  718 */     return getColumnName(col);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String getColumnName(int col) throws SQLException {
/*  725 */     return getDatabase().column_name(this.stmt.pointer, checkCol(col));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int getColumnType(int col) throws SQLException {
/*  732 */     String typeName = getColumnTypeName(col);
/*  733 */     int valueType = getDatabase().column_type(this.stmt.pointer, checkCol(col));
/*      */     
/*  735 */     if (valueType == 1 || valueType == 5) {
/*  736 */       if ("BOOLEAN".equals(typeName)) {
/*  737 */         return 16;
/*      */       }
/*      */       
/*  740 */       if ("TINYINT".equals(typeName)) {
/*  741 */         return -6;
/*      */       }
/*      */       
/*  744 */       if ("SMALLINT".equals(typeName) || "INT2".equals(typeName)) {
/*  745 */         return 5;
/*      */       }
/*      */       
/*  748 */       if ("BIGINT".equals(typeName) || "INT8".equals(typeName) || "UNSIGNED BIG INT"
/*  749 */         .equals(typeName)) {
/*  750 */         return -5;
/*      */       }
/*      */       
/*  753 */       if ("DATE".equals(typeName) || "DATETIME".equals(typeName)) {
/*  754 */         return 91;
/*      */       }
/*      */       
/*  757 */       if ("TIMESTAMP".equals(typeName)) {
/*  758 */         return 93;
/*      */       }
/*      */       
/*  761 */       if (valueType == 1 || "INT"
/*  762 */         .equals(typeName) || "INTEGER"
/*  763 */         .equals(typeName) || "MEDIUMINT"
/*  764 */         .equals(typeName)) {
/*  765 */         return 4;
/*      */       }
/*      */     } 
/*      */     
/*  769 */     if (valueType == 2 || valueType == 5) {
/*  770 */       if ("DECIMAL".equals(typeName)) {
/*  771 */         return 3;
/*      */       }
/*      */       
/*  774 */       if ("DOUBLE".equals(typeName) || "DOUBLE PRECISION".equals(typeName)) {
/*  775 */         return 8;
/*      */       }
/*      */       
/*  778 */       if ("NUMERIC".equals(typeName)) {
/*  779 */         return 2;
/*      */       }
/*      */       
/*  782 */       if ("REAL".equals(typeName)) {
/*  783 */         return 7;
/*      */       }
/*      */       
/*  786 */       if (valueType == 2 || "FLOAT"
/*  787 */         .equals(typeName)) {
/*  788 */         return 6;
/*      */       }
/*      */     } 
/*      */     
/*  792 */     if (valueType == 3 || valueType == 5) {
/*  793 */       if ("CHARACTER".equals(typeName) || "NCHAR".equals(typeName) || "NATIVE CHARACTER"
/*  794 */         .equals(typeName) || "CHAR".equals(typeName)) {
/*  795 */         return 1;
/*      */       }
/*      */       
/*  798 */       if ("CLOB".equals(typeName)) {
/*  799 */         return 2005;
/*      */       }
/*      */       
/*  802 */       if ("DATE".equals(typeName) || "DATETIME".equals(typeName)) {
/*  803 */         return 91;
/*      */       }
/*      */       
/*  806 */       if (valueType == 3 || "VARCHAR"
/*  807 */         .equals(typeName) || "VARYING CHARACTER"
/*  808 */         .equals(typeName) || "NVARCHAR"
/*  809 */         .equals(typeName) || "TEXT"
/*  810 */         .equals(typeName)) {
/*  811 */         return 12;
/*      */       }
/*      */     } 
/*      */     
/*  815 */     if (valueType == 4 || valueType == 5) {
/*  816 */       if ("BINARY".equals(typeName)) {
/*  817 */         return -2;
/*      */       }
/*      */       
/*  820 */       if (valueType == 4 || "BLOB"
/*  821 */         .equals(typeName)) {
/*  822 */         return 2004;
/*      */       }
/*      */     } 
/*      */     
/*  826 */     return 2;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String getColumnTypeName(int col) throws SQLException {
/*  835 */     String declType = getColumnDeclType(col);
/*      */     
/*  837 */     if (declType != null) {
/*  838 */       Matcher matcher = COLUMN_TYPENAME.matcher(declType);
/*      */       
/*  840 */       matcher.find();
/*  841 */       return matcher.group(1).toUpperCase(Locale.ENGLISH);
/*      */     } 
/*      */     
/*  844 */     switch (getDatabase().column_type(this.stmt.pointer, checkCol(col))) {
/*      */       case 1:
/*  846 */         return "INTEGER";
/*      */       case 2:
/*  848 */         return "FLOAT";
/*      */       case 4:
/*  850 */         return "BLOB";
/*      */       case 5:
/*  852 */         return "NUMERIC";
/*      */       case 3:
/*  854 */         return "TEXT";
/*      */     } 
/*  856 */     return "NUMERIC";
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int getPrecision(int col) throws SQLException {
/*  864 */     String declType = getColumnDeclType(col);
/*      */     
/*  866 */     if (declType != null) {
/*  867 */       Matcher matcher = COLUMN_PRECISION.matcher(declType);
/*      */       
/*  869 */       return matcher.find() ? Integer.parseInt(matcher.group(1).split(",")[0].trim()) : 0;
/*      */     } 
/*      */     
/*  872 */     return 0;
/*      */   }
/*      */   
/*      */   private String getColumnDeclType(int col) throws SQLException {
/*  876 */     DB db = getDatabase();
/*  877 */     String declType = db.column_decltype(this.stmt.pointer, checkCol(col));
/*      */     
/*  879 */     if (declType == null) {
/*  880 */       Matcher matcher = COLUMN_TYPECAST.matcher(db.column_name(this.stmt.pointer, checkCol(col)));
/*  881 */       declType = matcher.find() ? matcher.group(1) : null;
/*      */     } 
/*      */     
/*  884 */     return declType;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public int getScale(int col) throws SQLException {
/*  890 */     String declType = getColumnDeclType(col);
/*      */     
/*  892 */     if (declType != null) {
/*  893 */       Matcher matcher = COLUMN_PRECISION.matcher(declType);
/*      */       
/*  895 */       if (matcher.find()) {
/*  896 */         String[] array = matcher.group(1).split(",");
/*      */         
/*  898 */         if (array.length == 2) {
/*  899 */           return Integer.parseInt(array[1].trim());
/*      */         }
/*      */       } 
/*      */     } 
/*      */     
/*  904 */     return 0;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String getSchemaName(int col) throws SQLException {
/*  911 */     return "";
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String getTableName(int col) throws SQLException {
/*  918 */     String tableName = getDatabase().column_table_name(this.stmt.pointer, checkCol(col));
/*  919 */     if (tableName == null)
/*      */     {
/*      */       
/*  922 */       return "";
/*      */     }
/*  924 */     return tableName;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int isNullable(int col) throws SQLException {
/*  931 */     checkMeta();
/*  932 */     return this.meta[checkCol(col)][1] ? 0 : 1;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean isAutoIncrement(int col) throws SQLException {
/*  939 */     checkMeta();
/*  940 */     return this.meta[checkCol(col)][2];
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean isCaseSensitive(int col) throws SQLException {
/*  947 */     return true;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean isCurrency(int col) throws SQLException {
/*  954 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean isDefinitelyWritable(int col) throws SQLException {
/*  961 */     return true;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean isReadOnly(int col) throws SQLException {
/*  968 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean isSearchable(int col) throws SQLException {
/*  975 */     return true;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean isSigned(int col) throws SQLException {
/*  982 */     String typeName = getColumnTypeName(col);
/*      */     
/*  984 */     return ("NUMERIC".equals(typeName) || "INTEGER"
/*  985 */       .equals(typeName) || "REAL"
/*  986 */       .equals(typeName));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean isWritable(int col) throws SQLException {
/*  993 */     return true;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int getConcurrency() throws SQLException {
/* 1000 */     return 1007;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean rowDeleted() throws SQLException {
/* 1007 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean rowInserted() throws SQLException {
/* 1014 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean rowUpdated() throws SQLException {
/* 1021 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private Calendar julianDateToCalendar(Double jd) {
/* 1028 */     return julianDateToCalendar(jd, Calendar.getInstance());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private Calendar julianDateToCalendar(Double jd, Calendar cal) {
/*      */     int A;
/* 1037 */     if (jd == null) {
/* 1038 */       return null;
/*      */     }
/*      */ 
/*      */ 
/*      */     
/* 1043 */     double w = jd.doubleValue() + 0.5D;
/* 1044 */     int Z = (int)w;
/* 1045 */     double F = w - Z;
/*      */     
/* 1047 */     if (Z < 2299161) {
/* 1048 */       A = Z;
/*      */     } else {
/*      */       
/* 1051 */       int alpha = (int)((Z - 1867216.25D) / 36524.25D);
/* 1052 */       A = Z + 1 + alpha - (int)(alpha / 4.0D);
/*      */     } 
/*      */     
/* 1055 */     int B = A + 1524;
/* 1056 */     int C = (int)((B - 122.1D) / 365.25D);
/* 1057 */     int D = (int)(365.25D * C);
/* 1058 */     int E = (int)((B - D) / 30.6001D);
/*      */ 
/*      */     
/* 1061 */     int mm = E - ((E < 13.5D) ? 1 : 13);
/*      */ 
/*      */     
/* 1064 */     int yyyy = C - ((mm > 2.5D) ? 4716 : 4715);
/*      */ 
/*      */     
/* 1067 */     double jjd = (B - D - (int)(30.6001D * E)) + F;
/* 1068 */     int dd = (int)jjd;
/*      */ 
/*      */     
/* 1071 */     double hhd = jjd - dd;
/* 1072 */     int hh = (int)(24.0D * hhd);
/*      */ 
/*      */     
/* 1075 */     double mnd = 24.0D * hhd - hh;
/* 1076 */     int mn = (int)(60.0D * mnd);
/*      */ 
/*      */     
/* 1079 */     double ssd = 60.0D * mnd - mn;
/* 1080 */     int ss = (int)(60.0D * ssd);
/*      */ 
/*      */     
/* 1083 */     double msd = 60.0D * ssd - ss;
/* 1084 */     int ms = (int)(1000.0D * msd);
/*      */     
/* 1086 */     cal.set(yyyy, mm - 1, dd, hh, mn, ss);
/* 1087 */     cal.set(14, ms);
/*      */     
/* 1089 */     if (yyyy < 1) {
/* 1090 */       cal.set(0, 0);
/* 1091 */       cal.set(1, -(yyyy - 1));
/*      */     } 
/*      */     
/* 1094 */     return cal;
/*      */   }
/*      */   
/*      */   public void checkCalendar(Calendar cal) throws SQLException {
/* 1098 */     if (cal != null) {
/*      */       return;
/*      */     }
/* 1101 */     SQLException e = new SQLException("Expected a calendar instance.");
/* 1102 */     e.initCause(new NullPointerException());
/*      */     
/* 1104 */     throw e;
/*      */   }
/*      */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\sqlite\jdbc3\JDBC3ResultSet.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */