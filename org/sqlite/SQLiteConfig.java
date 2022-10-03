/*     */ package org.sqlite;
/*     */ 
/*     */ import java.sql.Connection;
/*     */ import java.sql.DriverPropertyInfo;
/*     */ import java.sql.SQLException;
/*     */ import java.sql.Statement;
/*     */ import java.util.HashSet;
/*     */ import java.util.Properties;
/*     */ import java.util.Set;
/*     */ import java.util.TreeSet;
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
/*     */ public class SQLiteConfig
/*     */ {
/*     */   public static final String DEFAULT_DATE_STRING_FORMAT = "yyyy-MM-dd HH:mm:ss.SSS";
/*     */   private final Properties pragmaTable;
/*  51 */   private int openModeFlag = 0;
/*     */ 
/*     */   
/*     */   private final int busyTimeout;
/*     */ 
/*     */   
/*     */   private final SQLiteConnectionConfig defaultConnectionConfig;
/*     */ 
/*     */   
/*     */   public SQLiteConfig() {
/*  61 */     this(new Properties());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public SQLiteConfig(Properties prop) {
/*  70 */     this.pragmaTable = prop;
/*     */     
/*  72 */     String openMode = this.pragmaTable.getProperty(Pragma.OPEN_MODE.pragmaName);
/*  73 */     if (openMode != null) {
/*  74 */       this.openModeFlag = Integer.parseInt(openMode);
/*     */     }
/*     */     else {
/*     */       
/*  78 */       setOpenMode(SQLiteOpenMode.READWRITE);
/*  79 */       setOpenMode(SQLiteOpenMode.CREATE);
/*     */     } 
/*     */     
/*  82 */     setSharedCache(Boolean.parseBoolean(this.pragmaTable.getProperty(Pragma.SHARED_CACHE.pragmaName, "false")));
/*     */     
/*  84 */     setOpenMode(SQLiteOpenMode.OPEN_URI);
/*     */     
/*  86 */     this.busyTimeout = Integer.parseInt(this.pragmaTable.getProperty(Pragma.BUSY_TIMEOUT.pragmaName, "3000"));
/*  87 */     this.defaultConnectionConfig = SQLiteConnectionConfig.fromPragmaTable(this.pragmaTable);
/*     */   }
/*     */ 
/*     */   
/*     */   public SQLiteConnectionConfig newConnectionConfig() {
/*  92 */     return this.defaultConnectionConfig.copyConfig();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Connection createConnection(String url) throws SQLException {
/* 101 */     return JDBC.createConnection(url, toProperties());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void apply(Connection conn) throws SQLException {
/* 111 */     HashSet<String> pragmaParams = new HashSet<>();
/* 112 */     for (Pragma each : Pragma.values()) {
/* 113 */       pragmaParams.add(each.pragmaName);
/*     */     }
/*     */     
/* 116 */     if (conn instanceof SQLiteConnection) {
/* 117 */       if (this.pragmaTable.containsKey(Pragma.LIMIT_ATTACHED.pragmaName)) {
/* 118 */         ((SQLiteConnection)conn).setLimit(SQLiteLimits.SQLITE_LIMIT_ATTACHED, getInteger(Pragma.LIMIT_ATTACHED, "-1"));
/*     */       }
/*     */       
/* 121 */       if (this.pragmaTable.containsKey(Pragma.LIMIT_COLUMN.pragmaName)) {
/* 122 */         ((SQLiteConnection)conn).setLimit(SQLiteLimits.SQLITE_LIMIT_COLUMN, getInteger(Pragma.LIMIT_COLUMN, "-1"));
/*     */       }
/*     */       
/* 125 */       if (this.pragmaTable.containsKey(Pragma.LIMIT_COMPOUND_SELECT.pragmaName)) {
/* 126 */         ((SQLiteConnection)conn).setLimit(SQLiteLimits.SQLITE_LIMIT_COMPOUND_SELECT, getInteger(Pragma.LIMIT_COMPOUND_SELECT, "-1"));
/*     */       }
/*     */       
/* 129 */       if (this.pragmaTable.containsKey(Pragma.LIMIT_EXPR_DEPTH.pragmaName)) {
/* 130 */         ((SQLiteConnection)conn).setLimit(SQLiteLimits.SQLITE_LIMIT_EXPR_DEPTH, getInteger(Pragma.LIMIT_EXPR_DEPTH, "-1"));
/*     */       }
/*     */       
/* 133 */       if (this.pragmaTable.containsKey(Pragma.LIMIT_FUNCTION_ARG.pragmaName)) {
/* 134 */         ((SQLiteConnection)conn).setLimit(SQLiteLimits.SQLITE_LIMIT_FUNCTION_ARG, getInteger(Pragma.LIMIT_FUNCTION_ARG, "-1"));
/*     */       }
/*     */       
/* 137 */       if (this.pragmaTable.containsKey(Pragma.LIMIT_LENGTH.pragmaName)) {
/* 138 */         ((SQLiteConnection)conn).setLimit(SQLiteLimits.SQLITE_LIMIT_LENGTH, getInteger(Pragma.LIMIT_LENGTH, "-1"));
/*     */       }
/*     */       
/* 141 */       if (this.pragmaTable.containsKey(Pragma.LIMIT_LIKE_PATTERN_LENGTH.pragmaName)) {
/* 142 */         ((SQLiteConnection)conn).setLimit(SQLiteLimits.SQLITE_LIMIT_LIKE_PATTERN_LENGTH, getInteger(Pragma.LIMIT_LIKE_PATTERN_LENGTH, "-1"));
/*     */       }
/*     */       
/* 145 */       if (this.pragmaTable.containsKey(Pragma.LIMIT_SQL_LENGTH.pragmaName)) {
/* 146 */         ((SQLiteConnection)conn).setLimit(SQLiteLimits.SQLITE_LIMIT_SQL_LENGTH, getInteger(Pragma.LIMIT_SQL_LENGTH, "-1"));
/*     */       }
/*     */       
/* 149 */       if (this.pragmaTable.containsKey(Pragma.LIMIT_TRIGGER_DEPTH.pragmaName)) {
/* 150 */         ((SQLiteConnection)conn).setLimit(SQLiteLimits.SQLITE_LIMIT_TRIGGER_DEPTH, getInteger(Pragma.LIMIT_TRIGGER_DEPTH, "-1"));
/*     */       }
/*     */       
/* 153 */       if (this.pragmaTable.containsKey(Pragma.LIMIT_VARIABLE_NUMBER.pragmaName)) {
/* 154 */         ((SQLiteConnection)conn).setLimit(SQLiteLimits.SQLITE_LIMIT_VARIABLE_NUMBER, getInteger(Pragma.LIMIT_VARIABLE_NUMBER, "-1"));
/*     */       }
/*     */       
/* 157 */       if (this.pragmaTable.containsKey(Pragma.LIMIT_VDBE_OP.pragmaName)) {
/* 158 */         ((SQLiteConnection)conn).setLimit(SQLiteLimits.SQLITE_LIMIT_VDBE_OP, getInteger(Pragma.LIMIT_VDBE_OP, "-1"));
/*     */       }
/*     */       
/* 161 */       if (this.pragmaTable.containsKey(Pragma.LIMIT_WORKER_THREADS.pragmaName)) {
/* 162 */         ((SQLiteConnection)conn).setLimit(SQLiteLimits.SQLITE_LIMIT_WORKER_THREADS, getInteger(Pragma.LIMIT_WORKER_THREADS, "-1"));
/*     */       }
/*     */     } 
/*     */ 
/*     */     
/* 167 */     pragmaParams.remove(Pragma.OPEN_MODE.pragmaName);
/* 168 */     pragmaParams.remove(Pragma.SHARED_CACHE.pragmaName);
/* 169 */     pragmaParams.remove(Pragma.LOAD_EXTENSION.pragmaName);
/* 170 */     pragmaParams.remove(Pragma.DATE_PRECISION.pragmaName);
/* 171 */     pragmaParams.remove(Pragma.DATE_CLASS.pragmaName);
/* 172 */     pragmaParams.remove(Pragma.DATE_STRING_FORMAT.pragmaName);
/* 173 */     pragmaParams.remove(Pragma.PASSWORD.pragmaName);
/* 174 */     pragmaParams.remove(Pragma.HEXKEY_MODE.pragmaName);
/* 175 */     pragmaParams.remove(Pragma.LIMIT_ATTACHED.pragmaName);
/* 176 */     pragmaParams.remove(Pragma.LIMIT_COLUMN.pragmaName);
/* 177 */     pragmaParams.remove(Pragma.LIMIT_COMPOUND_SELECT.pragmaName);
/* 178 */     pragmaParams.remove(Pragma.LIMIT_EXPR_DEPTH.pragmaName);
/* 179 */     pragmaParams.remove(Pragma.LIMIT_FUNCTION_ARG.pragmaName);
/* 180 */     pragmaParams.remove(Pragma.LIMIT_LENGTH.pragmaName);
/* 181 */     pragmaParams.remove(Pragma.LIMIT_LIKE_PATTERN_LENGTH.pragmaName);
/* 182 */     pragmaParams.remove(Pragma.LIMIT_SQL_LENGTH.pragmaName);
/* 183 */     pragmaParams.remove(Pragma.LIMIT_TRIGGER_DEPTH.pragmaName);
/* 184 */     pragmaParams.remove(Pragma.LIMIT_VARIABLE_NUMBER.pragmaName);
/* 185 */     pragmaParams.remove(Pragma.LIMIT_VDBE_OP.pragmaName);
/* 186 */     pragmaParams.remove(Pragma.LIMIT_WORKER_THREADS.pragmaName);
/*     */ 
/*     */     
/* 189 */     Statement stat = conn.createStatement();
/*     */     try {
/* 191 */       if (this.pragmaTable.containsKey(Pragma.PASSWORD.pragmaName)) {
/* 192 */         String password = this.pragmaTable.getProperty(Pragma.PASSWORD.pragmaName);
/* 193 */         if (password != null && !password.isEmpty()) {
/* 194 */           String passwordPragma, hexkeyMode = this.pragmaTable.getProperty(Pragma.HEXKEY_MODE.pragmaName);
/*     */           
/* 196 */           if (HexKeyMode.SSE.name().equalsIgnoreCase(hexkeyMode)) {
/* 197 */             passwordPragma = "pragma hexkey = '%s'";
/* 198 */           } else if (HexKeyMode.SQLCIPHER.name().equalsIgnoreCase(hexkeyMode)) {
/* 199 */             passwordPragma = "pragma key = \"x'%s'\"";
/*     */           } else {
/* 201 */             passwordPragma = "pragma key = '%s'";
/*     */           } 
/* 203 */           stat.execute(String.format(passwordPragma, new Object[] { password.replace("'", "''") }));
/* 204 */           stat.execute("select 1 from sqlite_master");
/*     */         } 
/*     */       } 
/*     */       
/* 208 */       for (Object each : this.pragmaTable.keySet()) {
/* 209 */         String key = each.toString();
/* 210 */         if (!pragmaParams.contains(key)) {
/*     */           continue;
/*     */         }
/*     */         
/* 214 */         String value = this.pragmaTable.getProperty(key);
/* 215 */         if (value != null) {
/* 216 */           stat.execute(String.format("pragma %s=%s", new Object[] { key, value }));
/*     */         }
/*     */       } 
/*     */     } finally {
/*     */       
/* 221 */       if (stat != null) {
/* 222 */         stat.close();
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void set(Pragma pragma, boolean flag) {
/* 234 */     setPragma(pragma, Boolean.toString(flag));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void set(Pragma pragma, int num) {
/* 243 */     setPragma(pragma, Integer.toString(num));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean getBoolean(Pragma pragma, String defaultValue) {
/* 253 */     return Boolean.parseBoolean(this.pragmaTable.getProperty(pragma.pragmaName, defaultValue));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private int getInteger(Pragma pragma, String defaultValue) {
/* 263 */     return Integer.parseInt(this.pragmaTable.getProperty(pragma.pragmaName, defaultValue));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isEnabledSharedCache() {
/* 271 */     return getBoolean(Pragma.SHARED_CACHE, "false");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isEnabledLoadExtension() {
/* 279 */     return getBoolean(Pragma.LOAD_EXTENSION, "false");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getOpenModeFlags() {
/* 286 */     return this.openModeFlag;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setPragma(Pragma pragma, String value) {
/* 295 */     this.pragmaTable.put(pragma.pragmaName, value);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Properties toProperties() {
/* 304 */     this.pragmaTable.setProperty(Pragma.OPEN_MODE.pragmaName, Integer.toString(this.openModeFlag));
/* 305 */     this.pragmaTable.setProperty(Pragma.TRANSACTION_MODE.pragmaName, this.defaultConnectionConfig.getTransactionMode().getValue());
/* 306 */     this.pragmaTable.setProperty(Pragma.DATE_CLASS.pragmaName, this.defaultConnectionConfig.getDateClass().getValue());
/* 307 */     this.pragmaTable.setProperty(Pragma.DATE_PRECISION.pragmaName, this.defaultConnectionConfig.getDatePrecision().getValue());
/* 308 */     this.pragmaTable.setProperty(Pragma.DATE_STRING_FORMAT.pragmaName, this.defaultConnectionConfig.getDateStringFormat());
/*     */     
/* 310 */     return this.pragmaTable;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static DriverPropertyInfo[] getDriverPropertyInfo() {
/* 317 */     Pragma[] pragma = Pragma.values();
/* 318 */     DriverPropertyInfo[] result = new DriverPropertyInfo[pragma.length];
/* 319 */     int index = 0;
/* 320 */     for (Pragma p : Pragma.values()) {
/* 321 */       DriverPropertyInfo di = new DriverPropertyInfo(p.pragmaName, null);
/* 322 */       di.choices = p.choices;
/* 323 */       di.description = p.description;
/* 324 */       di.required = false;
/* 325 */       result[index++] = di;
/*     */     } 
/*     */     
/* 328 */     return result;
/*     */   }
/*     */   
/* 331 */   private static final String[] OnOff = new String[] { "true", "false" };
/*     */   
/* 333 */   static final Set<String> pragmaSet = new TreeSet<>();
/*     */   
/*     */   static {
/* 336 */     for (Pragma pragma : Pragma.values()) {
/* 337 */       pragmaSet.add(pragma.pragmaName);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public enum Pragma
/*     */   {
/* 344 */     OPEN_MODE("open_mode", "Database open-mode flag", null),
/* 345 */     SHARED_CACHE("shared_cache", "Enable SQLite Shared-Cache mode, native driver only", (String)SQLiteConfig.OnOff),
/* 346 */     LOAD_EXTENSION("enable_load_extension", "Enable SQLite load_extention() function, native driver only", (String)SQLiteConfig.OnOff),
/*     */ 
/*     */     
/* 349 */     CACHE_SIZE("cache_size"),
/* 350 */     MMAP_SIZE("mmap_size"),
/* 351 */     CASE_SENSITIVE_LIKE("case_sensitive_like", SQLiteConfig.OnOff),
/* 352 */     COUNT_CHANGES("count_changes", SQLiteConfig.OnOff),
/* 353 */     DEFAULT_CACHE_SIZE("default_cache_size"),
/* 354 */     DEFER_FOREIGN_KEYS("defer_foreign_keys", SQLiteConfig.OnOff),
/* 355 */     EMPTY_RESULT_CALLBACKS("empty_result_callback", SQLiteConfig.OnOff),
/* 356 */     ENCODING("encoding", SQLiteConfig.toStringArray((SQLiteConfig.PragmaValue[])SQLiteConfig.Encoding.values())),
/* 357 */     FOREIGN_KEYS("foreign_keys", SQLiteConfig.OnOff),
/* 358 */     FULL_COLUMN_NAMES("full_column_names", SQLiteConfig.OnOff),
/* 359 */     FULL_SYNC("fullsync", SQLiteConfig.OnOff),
/* 360 */     INCREMENTAL_VACUUM("incremental_vacuum"),
/* 361 */     JOURNAL_MODE("journal_mode", SQLiteConfig.toStringArray((SQLiteConfig.PragmaValue[])SQLiteConfig.JournalMode.values())),
/* 362 */     JOURNAL_SIZE_LIMIT("journal_size_limit"),
/* 363 */     LEGACY_FILE_FORMAT("legacy_file_format", SQLiteConfig.OnOff),
/* 364 */     LOCKING_MODE("locking_mode", SQLiteConfig.toStringArray((SQLiteConfig.PragmaValue[])SQLiteConfig.LockingMode.values())),
/* 365 */     PAGE_SIZE("page_size"),
/* 366 */     MAX_PAGE_COUNT("max_page_count"),
/* 367 */     READ_UNCOMMITTED("read_uncommitted", SQLiteConfig.OnOff),
/* 368 */     RECURSIVE_TRIGGERS("recursive_triggers", SQLiteConfig.OnOff),
/* 369 */     REVERSE_UNORDERED_SELECTS("reverse_unordered_selects", SQLiteConfig.OnOff),
/* 370 */     SECURE_DELETE("secure_delete", new String[] { "true", "false", "fast" }),
/* 371 */     SHORT_COLUMN_NAMES("short_column_names", SQLiteConfig.OnOff),
/* 372 */     SYNCHRONOUS("synchronous", SQLiteConfig.toStringArray((SQLiteConfig.PragmaValue[])SQLiteConfig.SynchronousMode.values())),
/* 373 */     TEMP_STORE("temp_store", SQLiteConfig.toStringArray((SQLiteConfig.PragmaValue[])SQLiteConfig.TempStore.values())),
/* 374 */     TEMP_STORE_DIRECTORY("temp_store_directory"),
/* 375 */     USER_VERSION("user_version"),
/* 376 */     APPLICATION_ID("application_id"),
/*     */ 
/*     */     
/* 379 */     LIMIT_LENGTH("limit_length", "The maximum size of any string or BLOB or table row, in bytes.", null),
/* 380 */     LIMIT_SQL_LENGTH("limit_sql_length", "The maximum length of an SQL statement, in bytes.", null),
/* 381 */     LIMIT_COLUMN("limit_column", "The maximum number of columns in a table definition or in the result set of a SELECT or the maximum number of columns in an index or in an ORDER BY or GROUP BY clause.", null),
/* 382 */     LIMIT_EXPR_DEPTH("limit_expr_depth", "The maximum depth of the parse tree on any expression.", null),
/* 383 */     LIMIT_COMPOUND_SELECT("limit_compound_select", "The maximum number of terms in a compound SELECT statement.", null),
/* 384 */     LIMIT_VDBE_OP("limit_vdbe_op", "The maximum number of instructions in a virtual machine program used to implement an SQL statement. If sqlite3_prepare_v2() or the equivalent tries to allocate space for more than this many opcodes in a single prepared statement, an SQLITE_NOMEM error is returned.", null),
/* 385 */     LIMIT_FUNCTION_ARG("limit_function_arg", "The maximum number of arguments on a function.", null),
/* 386 */     LIMIT_ATTACHED("limit_attached", "The maximum number of attached databases.", null),
/* 387 */     LIMIT_LIKE_PATTERN_LENGTH("limit_like_pattern_length", "The maximum length of the pattern argument to the LIKE or GLOB operators.", null),
/* 388 */     LIMIT_VARIABLE_NUMBER("limit_variable_number", "The maximum index number of any parameter in an SQL statement.", null),
/* 389 */     LIMIT_TRIGGER_DEPTH("limit_trigger_depth", "The maximum depth of recursion for triggers.", null),
/* 390 */     LIMIT_WORKER_THREADS("limit_worker_threads", "The maximum number of auxiliary worker threads that a single prepared statement may start.", null),
/*     */ 
/*     */     
/* 393 */     TRANSACTION_MODE("transaction_mode", SQLiteConfig.toStringArray((SQLiteConfig.PragmaValue[])SQLiteConfig.TransactionMode.values())),
/* 394 */     DATE_PRECISION("date_precision", "\"seconds\": Read and store integer dates as seconds from the Unix Epoch (SQLite standard).\n\"milliseconds\": (DEFAULT) Read and store integer dates as milliseconds from the Unix Epoch (Java standard).", (String)SQLiteConfig.toStringArray((SQLiteConfig.PragmaValue[])SQLiteConfig.DatePrecision.values())),
/* 395 */     DATE_CLASS("date_class", "\"integer\": (Default) store dates as number of seconds or milliseconds from the Unix Epoch\n\"text\": store dates as a string of text\n\"real\": store dates as Julian Dates", (String)SQLiteConfig.toStringArray((SQLiteConfig.PragmaValue[])SQLiteConfig.DateClass.values())),
/* 396 */     DATE_STRING_FORMAT("date_string_format", "Format to store and retrieve dates stored as text. Defaults to \"yyyy-MM-dd HH:mm:ss.SSS\"", null),
/* 397 */     BUSY_TIMEOUT("busy_timeout", null),
/* 398 */     HEXKEY_MODE("hexkey_mode", SQLiteConfig.toStringArray((SQLiteConfig.PragmaValue[])SQLiteConfig.HexKeyMode.values())),
/* 399 */     PASSWORD("password", null);
/*     */ 
/*     */ 
/*     */     
/*     */     public final String pragmaName;
/*     */ 
/*     */     
/*     */     public final String[] choices;
/*     */ 
/*     */     
/*     */     public final String description;
/*     */ 
/*     */ 
/*     */     
/*     */     Pragma(String pragmaName, String description, String[] choices) {
/* 414 */       this.pragmaName = pragmaName;
/* 415 */       this.description = description;
/* 416 */       this.choices = choices;
/*     */     }
/*     */ 
/*     */     
/*     */     public final String getPragmaName() {
/* 421 */       return this.pragmaName;
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setOpenMode(SQLiteOpenMode mode) {
/* 431 */     this.openModeFlag |= mode.flag;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void resetOpenMode(SQLiteOpenMode mode) {
/* 440 */     this.openModeFlag &= mode.flag ^ 0xFFFFFFFF;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setSharedCache(boolean enable) {
/* 450 */     set(Pragma.SHARED_CACHE, enable);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void enableLoadExtension(boolean enable) {
/* 459 */     set(Pragma.LOAD_EXTENSION, enable);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setReadOnly(boolean readOnly) {
/* 467 */     if (readOnly) {
/* 468 */       setOpenMode(SQLiteOpenMode.READONLY);
/* 469 */       resetOpenMode(SQLiteOpenMode.CREATE);
/* 470 */       resetOpenMode(SQLiteOpenMode.READWRITE);
/*     */     } else {
/*     */       
/* 473 */       setOpenMode(SQLiteOpenMode.READWRITE);
/* 474 */       setOpenMode(SQLiteOpenMode.CREATE);
/* 475 */       resetOpenMode(SQLiteOpenMode.READONLY);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setCacheSize(int numberOfPages) {
/* 486 */     set(Pragma.CACHE_SIZE, numberOfPages);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void enableCaseSensitiveLike(boolean enable) {
/* 495 */     set(Pragma.CASE_SENSITIVE_LIKE, enable);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   public void enableCountChanges(boolean enable) {
/* 507 */     set(Pragma.COUNT_CHANGES, enable);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setDefaultCacheSize(int numberOfPages) {
/* 518 */     set(Pragma.DEFAULT_CACHE_SIZE, numberOfPages);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void deferForeignKeys(boolean enable) {
/* 528 */     set(Pragma.DEFER_FOREIGN_KEYS, enable);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   public void enableEmptyResultCallBacks(boolean enable) {
/* 540 */     set(Pragma.EMPTY_RESULT_CALLBACKS, enable);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static interface PragmaValue
/*     */   {
/*     */     String getValue();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static String[] toStringArray(PragmaValue[] list) {
/* 558 */     String[] result = new String[list.length];
/* 559 */     for (int i = 0; i < list.length; i++) {
/* 560 */       result[i] = list[i].getValue();
/*     */     }
/* 562 */     return result;
/*     */   }
/*     */   
/*     */   public enum Encoding implements PragmaValue {
/* 566 */     UTF8("'UTF-8'"),
/* 567 */     UTF16("'UTF-16'"),
/* 568 */     UTF16_LITTLE_ENDIAN("'UTF-16le'"),
/* 569 */     UTF16_BIG_ENDIAN("'UTF-16be'"),
/* 570 */     UTF_8((String)UTF8),
/* 571 */     UTF_16((String)UTF16),
/* 572 */     UTF_16LE((String)UTF16_LITTLE_ENDIAN),
/* 573 */     UTF_16BE((String)UTF16_BIG_ENDIAN);
/*     */     
/*     */     public final String typeName;
/*     */     
/*     */     Encoding(String typeName) {
/* 578 */       this.typeName = typeName;
/*     */     }
/*     */     
/*     */     Encoding(Encoding encoding) {
/* 582 */       this.typeName = encoding.getValue();
/*     */     }
/*     */     
/*     */     public String getValue() {
/* 586 */       return this.typeName;
/*     */     }
/*     */     
/*     */     public static Encoding getEncoding(String value) {
/* 590 */       return valueOf(value.replaceAll("-", "_").toUpperCase());
/*     */     }
/*     */   }
/*     */   
/*     */   public enum JournalMode implements PragmaValue {
/* 595 */     DELETE, TRUNCATE, PERSIST, MEMORY, WAL, OFF;
/*     */     
/*     */     public String getValue() {
/* 598 */       return name();
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setEncoding(Encoding encoding) {
/* 608 */     setPragma(Pragma.ENCODING, encoding.typeName);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void enforceForeignKeys(boolean enforce) {
/* 619 */     set(Pragma.FOREIGN_KEYS, enforce);
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
/*     */   @Deprecated
/*     */   public void enableFullColumnNames(boolean enable) {
/* 632 */     set(Pragma.FULL_COLUMN_NAMES, enable);
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
/*     */   public void enableFullSync(boolean enable) {
/* 644 */     set(Pragma.FULL_SYNC, enable);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void incrementalVacuum(int numberOfPagesToBeRemoved) {
/* 655 */     set(Pragma.INCREMENTAL_VACUUM, numberOfPagesToBeRemoved);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setJournalMode(JournalMode mode) {
/* 665 */     setPragma(Pragma.JOURNAL_MODE, mode.name());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setJounalSizeLimit(int limit) {
/* 676 */     set(Pragma.JOURNAL_SIZE_LIMIT, limit);
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
/*     */   public void useLegacyFileFormat(boolean use) {
/* 689 */     set(Pragma.LEGACY_FILE_FORMAT, use);
/*     */   }
/*     */   
/*     */   public enum LockingMode implements PragmaValue {
/* 693 */     NORMAL, EXCLUSIVE;
/*     */     public String getValue() {
/* 695 */       return name();
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setLockingMode(LockingMode mode) {
/* 705 */     setPragma(Pragma.LOCKING_MODE, mode.name());
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
/*     */   
/*     */   public void setPageSize(int numBytes) {
/* 719 */     set(Pragma.PAGE_SIZE, numBytes);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setMaxPageCount(int numPages) {
/* 728 */     set(Pragma.MAX_PAGE_COUNT, numPages);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setReadUncommited(boolean useReadUncommitedIsolationMode) {
/* 738 */     set(Pragma.READ_UNCOMMITTED, useReadUncommitedIsolationMode);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void enableRecursiveTriggers(boolean enable) {
/* 747 */     set(Pragma.RECURSIVE_TRIGGERS, enable);
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
/*     */   public void enableReverseUnorderedSelects(boolean enable) {
/* 759 */     set(Pragma.REVERSE_UNORDERED_SELECTS, enable);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void enableShortColumnNames(boolean enable) {
/* 769 */     set(Pragma.SHORT_COLUMN_NAMES, enable);
/*     */   }
/*     */   
/*     */   public enum SynchronousMode implements PragmaValue {
/* 773 */     OFF, NORMAL, FULL;
/*     */     
/*     */     public String getValue() {
/* 776 */       return name();
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setSynchronous(SynchronousMode mode) {
/* 794 */     setPragma(Pragma.SYNCHRONOUS, mode.name());
/*     */   }
/*     */   
/*     */   public enum TempStore implements PragmaValue {
/* 798 */     DEFAULT, FILE, MEMORY;
/*     */     
/*     */     public String getValue() {
/* 801 */       return name();
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
/*     */   public void setHexKeyMode(HexKeyMode mode) {
/* 814 */     setPragma(Pragma.HEXKEY_MODE, mode.name());
/*     */   }
/*     */   
/*     */   public enum HexKeyMode implements PragmaValue {
/* 818 */     NONE, SSE, SQLCIPHER;
/*     */     
/*     */     public String getValue() {
/* 821 */       return name();
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
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setTempStore(TempStore storeType) {
/* 838 */     setPragma(Pragma.TEMP_STORE, storeType.name());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setTempStoreDirectory(String directoryName) {
/* 848 */     setPragma(Pragma.TEMP_STORE_DIRECTORY, String.format("'%s'", new Object[] { directoryName }));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setUserVersion(int version) {
/* 859 */     set(Pragma.USER_VERSION, version);
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
/*     */   public void setApplicationId(int id) {
/* 872 */     set(Pragma.APPLICATION_ID, id);
/*     */   }
/*     */ 
/*     */   
/*     */   public enum TransactionMode
/*     */     implements PragmaValue
/*     */   {
/* 879 */     DEFFERED,
/* 880 */     DEFERRED, IMMEDIATE, EXCLUSIVE;
/*     */     
/*     */     public String getValue() {
/* 883 */       return name();
/*     */     }
/*     */     
/*     */     public static TransactionMode getMode(String mode) {
/* 887 */       if ("DEFFERED".equalsIgnoreCase(mode)) {
/* 888 */         return DEFERRED;
/*     */       }
/* 890 */       return valueOf(mode.toUpperCase());
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setTransactionMode(TransactionMode transactionMode) {
/* 900 */     this.defaultConnectionConfig.setTransactionMode(transactionMode);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setTransactionMode(String transactionMode) {
/* 909 */     setTransactionMode(TransactionMode.getMode(transactionMode));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public TransactionMode getTransactionMode() {
/* 916 */     return this.defaultConnectionConfig.getTransactionMode();
/*     */   }
/*     */   
/*     */   public enum DatePrecision implements PragmaValue {
/* 920 */     SECONDS, MILLISECONDS;
/*     */     
/*     */     public String getValue() {
/* 923 */       return name();
/*     */     }
/*     */     
/*     */     public static DatePrecision getPrecision(String precision) {
/* 927 */       return valueOf(precision.toUpperCase());
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setDatePrecision(String datePrecision) throws SQLException {
/* 936 */     this.defaultConnectionConfig.setDatePrecision(DatePrecision.getPrecision(datePrecision));
/*     */   }
/*     */   
/*     */   public enum DateClass implements PragmaValue {
/* 940 */     INTEGER, TEXT, REAL;
/*     */     
/*     */     public String getValue() {
/* 943 */       return name();
/*     */     }
/*     */     
/*     */     public static DateClass getDateClass(String dateClass) {
/* 947 */       return valueOf(dateClass.toUpperCase());
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setDateClass(String dateClass) {
/* 955 */     this.defaultConnectionConfig.setDateClass(DateClass.getDateClass(dateClass));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setDateStringFormat(String dateStringFormat) {
/* 963 */     this.defaultConnectionConfig.setDateStringFormat(dateStringFormat);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setBusyTimeout(int milliseconds) {
/* 970 */     setPragma(Pragma.BUSY_TIMEOUT, Integer.toString(milliseconds));
/*     */   }
/*     */   
/*     */   public int getBusyTimeout() {
/* 974 */     return this.busyTimeout;
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\sqlite\SQLiteConfig.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */