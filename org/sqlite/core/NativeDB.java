/*     */ package org.sqlite.core;
/*     */ 
/*     */ import java.io.UnsupportedEncodingException;
/*     */ import java.nio.ByteBuffer;
/*     */ import java.nio.charset.Charset;
/*     */ import java.nio.charset.UnsupportedCharsetException;
/*     */ import java.sql.SQLException;
/*     */ import org.sqlite.BusyHandler;
/*     */ import org.sqlite.Function;
/*     */ import org.sqlite.ProgressHandler;
/*     */ import org.sqlite.SQLiteConfig;
/*     */ import org.sqlite.SQLiteJDBCLoader;
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
/*     */ public final class NativeDB
/*     */   extends DB
/*     */ {
/*  32 */   long pointer = 0L;
/*     */   private static boolean isLoaded;
/*     */   private static boolean loadSucceeded;
/*     */   private final long udfdatalist = 0L;
/*     */   
/*     */   static {
/*  38 */     if ("The Android Project".equals(System.getProperty("java.vm.vendor"))) {
/*  39 */       System.loadLibrary("sqlitejdbc");
/*  40 */       isLoaded = true;
/*  41 */       loadSucceeded = true;
/*     */     } else {
/*     */       
/*  44 */       isLoaded = false;
/*  45 */       loadSucceeded = false;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public NativeDB(String url, String fileName, SQLiteConfig config) throws SQLException {
/*  52 */     super(url, fileName, config);
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
/*  69 */     this.udfdatalist = 0L;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected synchronized void _open(String file, int openFlags) throws SQLException {
/*  78 */     _open_utf8(stringToUtf8ByteArray(file), openFlags);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static boolean load() throws Exception {
/*     */     if (isLoaded) {
/*     */       return (loadSucceeded == true);
/*     */     }
/*     */     loadSucceeded = SQLiteJDBCLoader.initialize();
/*     */     isLoaded = true;
/*     */     return loadSucceeded;
/*     */   }
/*     */ 
/*     */   
/*     */   public synchronized int _exec(String sql) throws SQLException {
/*  94 */     return _exec_utf8(stringToUtf8ByteArray(sql));
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
/*     */   protected synchronized long prepare(String sql) throws SQLException {
/* 134 */     return prepare_utf8(stringToUtf8ByteArray(sql));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   synchronized String errmsg() {
/* 144 */     return utf8ByteBufferToString(errmsg_utf8());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public synchronized String libversion() {
/* 154 */     return utf8ByteBufferToString(libversion_utf8());
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
/*     */   public synchronized String column_decltype(long stmt, int col) {
/* 218 */     return utf8ByteBufferToString(column_decltype_utf8(stmt, col));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public synchronized String column_table_name(long stmt, int col) {
/* 228 */     return utf8ByteBufferToString(column_table_name_utf8(stmt, col));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public synchronized String column_name(long stmt, int col) {
/* 239 */     return utf8ByteBufferToString(column_name_utf8(stmt, col));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public synchronized String column_text(long stmt, int col) {
/* 249 */     return utf8ByteBufferToString(column_text_utf8(stmt, col));
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
/*     */   synchronized int bind_text(long stmt, int pos, String v) {
/* 307 */     return bind_text_utf8(stmt, pos, stringToUtf8ByteArray(v));
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public synchronized void result_text(long context, String val) {
/* 329 */     result_text_utf8(context, stringToUtf8ByteArray(val));
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
/*     */   public synchronized void result_error(long context, String err) {
/* 363 */     result_error_utf8(context, stringToUtf8ByteArray(err));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public synchronized String value_text(Function f, int arg) {
/* 373 */     return utf8ByteBufferToString(value_text_utf8(f, arg));
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
/*     */   public synchronized int create_function(String name, Function func, int nArgs, int flags) {
/* 413 */     return create_function_utf8(stringToUtf8ByteArray(name), func, nArgs, flags);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public synchronized int destroy_function(String name, int nArgs) {
/* 423 */     return destroy_function_utf8(stringToUtf8ByteArray(name), nArgs);
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
/*     */ 
/*     */   
/*     */   public int backup(String dbName, String destFileName, DB.ProgressObserver observer) throws SQLException {
/* 442 */     return backup(stringToUtf8ByteArray(dbName), stringToUtf8ByteArray(destFileName), observer);
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
/*     */   public synchronized int restore(String dbName, String sourceFileName, DB.ProgressObserver observer) throws SQLException {
/* 456 */     return restore(stringToUtf8ByteArray(dbName), stringToUtf8ByteArray(sourceFileName), observer);
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
/*     */   static void throwex(String msg) throws SQLException {
/* 487 */     throw new SQLException(msg);
/*     */   }
/*     */   
/*     */   static byte[] stringToUtf8ByteArray(String str) {
/* 491 */     if (str == null) {
/* 492 */       return null;
/*     */     }
/*     */     try {
/* 495 */       return str.getBytes("UTF-8");
/*     */     }
/* 497 */     catch (UnsupportedEncodingException e) {
/* 498 */       throw new RuntimeException("UTF-8 is not supported", e);
/*     */     } 
/*     */   }
/*     */   
/*     */   static String utf8ByteBufferToString(ByteBuffer buffer) {
/* 503 */     if (buffer == null) {
/* 504 */       return null;
/*     */     }
/*     */     try {
/* 507 */       return Charset.forName("UTF-8").decode(buffer).toString();
/*     */     }
/* 509 */     catch (UnsupportedCharsetException e) {
/* 510 */       throw new RuntimeException("UTF-8 is not supported", e);
/*     */     } 
/*     */   }
/*     */   
/*     */   synchronized native void _open_utf8(byte[] paramArrayOfbyte, int paramInt) throws SQLException;
/*     */   
/*     */   protected synchronized native void _close() throws SQLException;
/*     */   
/*     */   synchronized native int _exec_utf8(byte[] paramArrayOfbyte) throws SQLException;
/*     */   
/*     */   public synchronized native int shared_cache(boolean paramBoolean);
/*     */   
/*     */   public synchronized native int enable_load_extension(boolean paramBoolean);
/*     */   
/*     */   public native void interrupt();
/*     */   
/*     */   public synchronized native void busy_timeout(int paramInt);
/*     */   
/*     */   public synchronized native void busy_handler(BusyHandler paramBusyHandler);
/*     */   
/*     */   synchronized native long prepare_utf8(byte[] paramArrayOfbyte) throws SQLException;
/*     */   
/*     */   synchronized native ByteBuffer errmsg_utf8();
/*     */   
/*     */   native ByteBuffer libversion_utf8();
/*     */   
/*     */   public synchronized native int changes();
/*     */   
/*     */   public synchronized native int total_changes();
/*     */   
/*     */   protected synchronized native int finalize(long paramLong);
/*     */   
/*     */   public synchronized native int step(long paramLong);
/*     */   
/*     */   public synchronized native int reset(long paramLong);
/*     */   
/*     */   public synchronized native int clear_bindings(long paramLong);
/*     */   
/*     */   synchronized native int bind_parameter_count(long paramLong);
/*     */   
/*     */   public synchronized native int column_count(long paramLong);
/*     */   
/*     */   public synchronized native int column_type(long paramLong, int paramInt);
/*     */   
/*     */   synchronized native ByteBuffer column_decltype_utf8(long paramLong, int paramInt);
/*     */   
/*     */   synchronized native ByteBuffer column_table_name_utf8(long paramLong, int paramInt);
/*     */   
/*     */   synchronized native ByteBuffer column_name_utf8(long paramLong, int paramInt);
/*     */   
/*     */   synchronized native ByteBuffer column_text_utf8(long paramLong, int paramInt);
/*     */   
/*     */   public synchronized native byte[] column_blob(long paramLong, int paramInt);
/*     */   
/*     */   public synchronized native double column_double(long paramLong, int paramInt);
/*     */   
/*     */   public synchronized native long column_long(long paramLong, int paramInt);
/*     */   
/*     */   public synchronized native int column_int(long paramLong, int paramInt);
/*     */   
/*     */   synchronized native int bind_null(long paramLong, int paramInt);
/*     */   
/*     */   synchronized native int bind_int(long paramLong, int paramInt1, int paramInt2);
/*     */   
/*     */   synchronized native int bind_long(long paramLong1, int paramInt, long paramLong2);
/*     */   
/*     */   synchronized native int bind_double(long paramLong, int paramInt, double paramDouble);
/*     */   
/*     */   synchronized native int bind_text_utf8(long paramLong, int paramInt, byte[] paramArrayOfbyte);
/*     */   
/*     */   synchronized native int bind_blob(long paramLong, int paramInt, byte[] paramArrayOfbyte);
/*     */   
/*     */   public synchronized native void result_null(long paramLong);
/*     */   
/*     */   synchronized native void result_text_utf8(long paramLong, byte[] paramArrayOfbyte);
/*     */   
/*     */   public synchronized native void result_blob(long paramLong, byte[] paramArrayOfbyte);
/*     */   
/*     */   public synchronized native void result_double(long paramLong, double paramDouble);
/*     */   
/*     */   public synchronized native void result_long(long paramLong1, long paramLong2);
/*     */   
/*     */   public synchronized native void result_int(long paramLong, int paramInt);
/*     */   
/*     */   synchronized native void result_error_utf8(long paramLong, byte[] paramArrayOfbyte);
/*     */   
/*     */   synchronized native ByteBuffer value_text_utf8(Function paramFunction, int paramInt);
/*     */   
/*     */   public synchronized native byte[] value_blob(Function paramFunction, int paramInt);
/*     */   
/*     */   public synchronized native double value_double(Function paramFunction, int paramInt);
/*     */   
/*     */   public synchronized native long value_long(Function paramFunction, int paramInt);
/*     */   
/*     */   public synchronized native int value_int(Function paramFunction, int paramInt);
/*     */   
/*     */   public synchronized native int value_type(Function paramFunction, int paramInt);
/*     */   
/*     */   synchronized native int create_function_utf8(byte[] paramArrayOfbyte, Function paramFunction, int paramInt1, int paramInt2);
/*     */   
/*     */   synchronized native int destroy_function_utf8(byte[] paramArrayOfbyte, int paramInt);
/*     */   
/*     */   synchronized native void free_functions();
/*     */   
/*     */   public synchronized native int limit(int paramInt1, int paramInt2) throws SQLException;
/*     */   
/*     */   synchronized native int backup(byte[] paramArrayOfbyte1, byte[] paramArrayOfbyte2, DB.ProgressObserver paramProgressObserver) throws SQLException;
/*     */   
/*     */   synchronized native int restore(byte[] paramArrayOfbyte1, byte[] paramArrayOfbyte2, DB.ProgressObserver paramProgressObserver) throws SQLException;
/*     */   
/*     */   synchronized native boolean[][] column_metadata(long paramLong);
/*     */   
/*     */   synchronized native void set_commit_listener(boolean paramBoolean);
/*     */   
/*     */   synchronized native void set_update_listener(boolean paramBoolean);
/*     */   
/*     */   public synchronized native void register_progress_handler(int paramInt, ProgressHandler paramProgressHandler) throws SQLException;
/*     */   
/*     */   public synchronized native void clear_progress_handler() throws SQLException;
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\sqlite\core\NativeDB.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */