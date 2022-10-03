/*      */ package org.sqlite.core;
/*      */ 
/*      */ import java.sql.BatchUpdateException;
/*      */ import java.sql.SQLException;
/*      */ import java.util.HashMap;
/*      */ import java.util.HashSet;
/*      */ import java.util.Iterator;
/*      */ import java.util.Map;
/*      */ import java.util.Set;
/*      */ import java.util.concurrent.atomic.AtomicBoolean;
/*      */ import org.sqlite.BusyHandler;
/*      */ import org.sqlite.Function;
/*      */ import org.sqlite.ProgressHandler;
/*      */ import org.sqlite.SQLiteCommitListener;
/*      */ import org.sqlite.SQLiteConfig;
/*      */ import org.sqlite.SQLiteErrorCode;
/*      */ import org.sqlite.SQLiteException;
/*      */ import org.sqlite.SQLiteUpdateListener;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ public abstract class DB
/*      */   implements Codes
/*      */ {
/*      */   private final String url;
/*      */   private final String fileName;
/*      */   private final SQLiteConfig config;
/*   46 */   private final AtomicBoolean closed = new AtomicBoolean(true);
/*      */ 
/*      */   
/*   49 */   long begin = 0L;
/*   50 */   long commit = 0L;
/*      */ 
/*      */   
/*   53 */   private final Map<Long, CoreStatement> stmts = new HashMap<>();
/*      */   
/*   55 */   private final Set<SQLiteUpdateListener> updateListeners = new HashSet<>();
/*   56 */   private final Set<SQLiteCommitListener> commitListeners = new HashSet<>();
/*      */ 
/*      */ 
/*      */   
/*      */   public DB(String url, String fileName, SQLiteConfig config) throws SQLException {
/*   61 */     this.url = url;
/*   62 */     this.fileName = fileName;
/*   63 */     this.config = config;
/*      */   }
/*      */   
/*      */   public String getUrl() {
/*   67 */     return this.url;
/*      */   }
/*      */   
/*      */   public boolean isClosed() {
/*   71 */     return this.closed.get();
/*      */   }
/*      */   
/*      */   public SQLiteConfig getConfig() {
/*   75 */     return this.config;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public abstract void interrupt() throws SQLException;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public abstract void busy_timeout(int paramInt) throws SQLException;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public abstract void busy_handler(BusyHandler paramBusyHandler) throws SQLException;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   abstract String errmsg() throws SQLException;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public abstract String libversion() throws SQLException;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public abstract int changes() throws SQLException;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public abstract int total_changes() throws SQLException;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public abstract int shared_cache(boolean paramBoolean) throws SQLException;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public abstract int enable_load_extension(boolean paramBoolean) throws SQLException;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final synchronized void exec(String sql, boolean autoCommit) throws SQLException {
/*  167 */     long pointer = 0L;
/*      */     try {
/*  169 */       pointer = prepare(sql);
/*  170 */       int rc = step(pointer);
/*  171 */       switch (rc) {
/*      */         case 101:
/*  173 */           ensureAutoCommit(autoCommit);
/*      */           return;
/*      */         case 100:
/*      */           return;
/*      */       } 
/*  178 */       throwex(rc);
/*      */     }
/*      */     finally {
/*      */       
/*  182 */       finalize(pointer);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final synchronized void open(String file, int openFlags) throws SQLException {
/*  195 */     _open(file, openFlags);
/*  196 */     this.closed.set(false);
/*      */     
/*  198 */     if (this.fileName.startsWith("file:") && !this.fileName.contains("cache="))
/*      */     {
/*  200 */       shared_cache(this.config.isEnabledSharedCache());
/*      */     }
/*  202 */     enable_load_extension(this.config.isEnabledLoadExtension());
/*  203 */     busy_timeout(this.config.getBusyTimeout());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final synchronized void close() throws SQLException {
/*  214 */     synchronized (this.stmts) {
/*  215 */       Iterator<Map.Entry<Long, CoreStatement>> i = this.stmts.entrySet().iterator();
/*  216 */       while (i.hasNext()) {
/*  217 */         Map.Entry<Long, CoreStatement> entry = i.next();
/*  218 */         CoreStatement stmt = entry.getValue();
/*  219 */         finalize(((Long)entry.getKey()).longValue());
/*  220 */         if (stmt != null) {
/*  221 */           stmt.pointer = 0L;
/*      */         }
/*  223 */         i.remove();
/*      */       } 
/*      */     } 
/*      */ 
/*      */     
/*  228 */     free_functions();
/*      */ 
/*      */     
/*  231 */     if (this.begin != 0L) {
/*  232 */       finalize(this.begin);
/*  233 */       this.begin = 0L;
/*      */     } 
/*  235 */     if (this.commit != 0L) {
/*  236 */       finalize(this.commit);
/*  237 */       this.commit = 0L;
/*      */     } 
/*      */     
/*  240 */     this.closed.set(true);
/*  241 */     _close();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final synchronized void prepare(CoreStatement stmt) throws SQLException {
/*  251 */     if (stmt.sql == null) {
/*  252 */       throw new NullPointerException();
/*      */     }
/*  254 */     if (stmt.pointer != 0L) {
/*  255 */       finalize(stmt);
/*      */     }
/*  257 */     stmt.pointer = prepare(stmt.sql);
/*  258 */     this.stmts.put(new Long(stmt.pointer), stmt);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final synchronized int finalize(CoreStatement stmt) throws SQLException {
/*  269 */     if (stmt.pointer == 0L) {
/*  270 */       return 0;
/*      */     }
/*  272 */     int rc = 1;
/*      */     try {
/*  274 */       rc = finalize(stmt.pointer);
/*      */     } finally {
/*      */       
/*  277 */       this.stmts.remove(new Long(stmt.pointer));
/*  278 */       stmt.pointer = 0L;
/*      */     } 
/*  280 */     return rc;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected abstract void _open(String paramString, int paramInt) throws SQLException;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected abstract void _close() throws SQLException;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public abstract int _exec(String paramString) throws SQLException;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected abstract long prepare(String paramString) throws SQLException;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected abstract int finalize(long paramLong) throws SQLException;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public abstract int step(long paramLong) throws SQLException;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public abstract int reset(long paramLong) throws SQLException;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public abstract int clear_bindings(long paramLong) throws SQLException;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   abstract int bind_parameter_count(long paramLong) throws SQLException;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public abstract int column_count(long paramLong) throws SQLException;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public abstract int column_type(long paramLong, int paramInt) throws SQLException;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public abstract String column_decltype(long paramLong, int paramInt) throws SQLException;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public abstract String column_table_name(long paramLong, int paramInt) throws SQLException;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public abstract String column_name(long paramLong, int paramInt) throws SQLException;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public abstract String column_text(long paramLong, int paramInt) throws SQLException;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public abstract byte[] column_blob(long paramLong, int paramInt) throws SQLException;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public abstract double column_double(long paramLong, int paramInt) throws SQLException;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public abstract long column_long(long paramLong, int paramInt) throws SQLException;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public abstract int column_int(long paramLong, int paramInt) throws SQLException;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   abstract int bind_null(long paramLong, int paramInt) throws SQLException;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   abstract int bind_int(long paramLong, int paramInt1, int paramInt2) throws SQLException;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   abstract int bind_long(long paramLong1, int paramInt, long paramLong2) throws SQLException;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   abstract int bind_double(long paramLong, int paramInt, double paramDouble) throws SQLException;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   abstract int bind_text(long paramLong, int paramInt, String paramString) throws SQLException;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   abstract int bind_blob(long paramLong, int paramInt, byte[] paramArrayOfbyte) throws SQLException;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public abstract void result_null(long paramLong) throws SQLException;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public abstract void result_text(long paramLong, String paramString) throws SQLException;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public abstract void result_blob(long paramLong, byte[] paramArrayOfbyte) throws SQLException;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public abstract void result_double(long paramLong, double paramDouble) throws SQLException;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public abstract void result_long(long paramLong1, long paramLong2) throws SQLException;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public abstract void result_int(long paramLong, int paramInt) throws SQLException;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public abstract void result_error(long paramLong, String paramString) throws SQLException;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public abstract String value_text(Function paramFunction, int paramInt) throws SQLException;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public abstract byte[] value_blob(Function paramFunction, int paramInt) throws SQLException;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public abstract double value_double(Function paramFunction, int paramInt) throws SQLException;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public abstract long value_long(Function paramFunction, int paramInt) throws SQLException;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public abstract int value_int(Function paramFunction, int paramInt) throws SQLException;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public abstract int value_type(Function paramFunction, int paramInt) throws SQLException;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public abstract int create_function(String paramString, Function paramFunction, int paramInt1, int paramInt2) throws SQLException;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public abstract int destroy_function(String paramString, int paramInt) throws SQLException;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   abstract void free_functions() throws SQLException;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public abstract int backup(String paramString1, String paramString2, ProgressObserver paramProgressObserver) throws SQLException;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public abstract int restore(String paramString1, String paramString2, ProgressObserver paramProgressObserver) throws SQLException;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public abstract int limit(int paramInt1, int paramInt2) throws SQLException;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public abstract void register_progress_handler(int paramInt, ProgressHandler paramProgressHandler) throws SQLException;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public abstract void clear_progress_handler() throws SQLException;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   abstract boolean[][] column_metadata(long paramLong) throws SQLException;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final synchronized String[] column_names(long stmt) throws SQLException {
/*  731 */     String[] names = new String[column_count(stmt)];
/*  732 */     for (int i = 0; i < names.length; i++) {
/*  733 */       names[i] = column_name(stmt, i);
/*      */     }
/*  735 */     return names;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   final synchronized int sqlbind(long stmt, int pos, Object v) throws SQLException {
/*  748 */     pos++;
/*  749 */     if (v == null) {
/*  750 */       return bind_null(stmt, pos);
/*      */     }
/*  752 */     if (v instanceof Integer) {
/*  753 */       return bind_int(stmt, pos, ((Integer)v).intValue());
/*      */     }
/*  755 */     if (v instanceof Short) {
/*  756 */       return bind_int(stmt, pos, ((Short)v).intValue());
/*      */     }
/*  758 */     if (v instanceof Long) {
/*  759 */       return bind_long(stmt, pos, ((Long)v).longValue());
/*      */     }
/*  761 */     if (v instanceof Float) {
/*  762 */       return bind_double(stmt, pos, ((Float)v).doubleValue());
/*      */     }
/*  764 */     if (v instanceof Double) {
/*  765 */       return bind_double(stmt, pos, ((Double)v).doubleValue());
/*      */     }
/*  767 */     if (v instanceof String) {
/*  768 */       return bind_text(stmt, pos, (String)v);
/*      */     }
/*  770 */     if (v instanceof byte[]) {
/*  771 */       return bind_blob(stmt, pos, (byte[])v);
/*      */     }
/*      */     
/*  774 */     throw new SQLException("unexpected param type: " + v.getClass());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   final synchronized int[] executeBatch(long stmt, int count, Object[] vals, boolean autoCommit) throws SQLException {
/*  789 */     if (count < 1) {
/*  790 */       throw new SQLException("count (" + count + ") < 1");
/*      */     }
/*      */     
/*  793 */     int params = bind_parameter_count(stmt);
/*      */ 
/*      */     
/*  796 */     int[] changes = new int[count];
/*      */     
/*      */     try {
/*  799 */       for (int i = 0; i < count; i++) {
/*  800 */         reset(stmt);
/*  801 */         for (int j = 0; j < params; j++) {
/*  802 */           int k = sqlbind(stmt, j, vals[i * params + j]);
/*  803 */           if (k != 0) {
/*  804 */             throwex(k);
/*      */           }
/*      */         } 
/*      */         
/*  808 */         int rc = step(stmt);
/*  809 */         if (rc != 101) {
/*  810 */           reset(stmt);
/*  811 */           if (rc == 100) {
/*  812 */             throw new BatchUpdateException("batch entry " + i + ": query returns results", changes);
/*      */           }
/*  814 */           throwex(rc);
/*      */         } 
/*      */         
/*  817 */         changes[i] = changes();
/*      */       } 
/*      */     } finally {
/*      */       
/*  821 */       ensureAutoCommit(autoCommit);
/*      */     } 
/*      */     
/*  824 */     reset(stmt);
/*  825 */     return changes;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final synchronized boolean execute(CoreStatement stmt, Object[] vals) throws SQLException {
/*  836 */     if (vals != null) {
/*  837 */       int params = bind_parameter_count(stmt.pointer);
/*  838 */       if (params > vals.length) {
/*  839 */         throw new SQLException("assertion failure: param count (" + params + ") > value count (" + vals.length + ")");
/*      */       }
/*      */ 
/*      */       
/*  843 */       for (int i = 0; i < params; i++) {
/*  844 */         int rc = sqlbind(stmt.pointer, i, vals[i]);
/*  845 */         if (rc != 0) {
/*  846 */           throwex(rc);
/*      */         }
/*      */       } 
/*      */     } 
/*      */     
/*  851 */     int statusCode = step(stmt.pointer);
/*  852 */     switch (statusCode & 0xFF) {
/*      */       case 101:
/*  854 */         reset(stmt.pointer);
/*  855 */         ensureAutoCommit(stmt.conn.getAutoCommit());
/*  856 */         return false;
/*      */       case 100:
/*  858 */         return true;
/*      */       case 5:
/*      */       case 6:
/*      */       case 19:
/*      */       case 21:
/*  863 */         throw newSQLException(statusCode);
/*      */     } 
/*  865 */     finalize(stmt);
/*  866 */     throw newSQLException(statusCode);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   final synchronized boolean execute(String sql, boolean autoCommit) throws SQLException {
/*  879 */     int statusCode = _exec(sql);
/*  880 */     switch (statusCode) {
/*      */       case 0:
/*  882 */         return false;
/*      */       case 101:
/*  884 */         ensureAutoCommit(autoCommit);
/*  885 */         return false;
/*      */       case 100:
/*  887 */         return true;
/*      */     } 
/*  889 */     throw newSQLException(statusCode);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final synchronized int executeUpdate(CoreStatement stmt, Object[] vals) throws SQLException {
/*      */     try {
/*  904 */       if (execute(stmt, vals)) {
/*  905 */         throw new SQLException("query returns results");
/*      */       }
/*      */     } finally {
/*  908 */       if (stmt.pointer != 0L) reset(stmt.pointer); 
/*      */     } 
/*  910 */     return changes();
/*      */   }
/*      */   abstract void set_commit_listener(boolean paramBoolean);
/*      */   
/*      */   abstract void set_update_listener(boolean paramBoolean);
/*      */   
/*      */   public synchronized void addUpdateListener(SQLiteUpdateListener listener) {
/*  917 */     if (this.updateListeners.add(listener) && this.updateListeners.size() == 1) {
/*  918 */       set_update_listener(true);
/*      */     }
/*      */   }
/*      */   
/*      */   public synchronized void addCommitListener(SQLiteCommitListener listener) {
/*  923 */     if (this.commitListeners.add(listener) && this.commitListeners.size() == 1) {
/*  924 */       set_commit_listener(true);
/*      */     }
/*      */   }
/*      */   
/*      */   public synchronized void removeUpdateListener(SQLiteUpdateListener listener) {
/*  929 */     if (this.updateListeners.remove(listener) && this.updateListeners.isEmpty()) {
/*  930 */       set_update_listener(false);
/*      */     }
/*      */   }
/*      */   
/*      */   public synchronized void removeCommitListener(SQLiteCommitListener listener) {
/*  935 */     if (this.commitListeners.remove(listener) && this.commitListeners.isEmpty()) {
/*  936 */       set_commit_listener(false);
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   void onUpdate(int type, String database, String table, long rowId) {
/*      */     Set<SQLiteUpdateListener> listeners;
/*  943 */     synchronized (this) {
/*  944 */       listeners = new HashSet<>(this.updateListeners);
/*      */     } 
/*      */     
/*  947 */     for (SQLiteUpdateListener listener : listeners) {
/*      */       SQLiteUpdateListener.Type operationType;
/*      */       
/*  950 */       switch (type) { case 18:
/*  951 */           operationType = SQLiteUpdateListener.Type.INSERT; break;
/*  952 */         case 9: operationType = SQLiteUpdateListener.Type.DELETE; break;
/*  953 */         case 23: operationType = SQLiteUpdateListener.Type.UPDATE; break;
/*  954 */         default: throw new AssertionError("Unknown type: " + type); }
/*      */ 
/*      */ 
/*      */       
/*  958 */       listener.onUpdate(operationType, database, table, rowId);
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   void onCommit(boolean commit) {
/*      */     Set<SQLiteCommitListener> listeners;
/*  965 */     synchronized (this) {
/*  966 */       listeners = new HashSet<>(this.commitListeners);
/*      */     } 
/*      */     
/*  969 */     for (SQLiteCommitListener listener : listeners) {
/*  970 */       if (commit) { listener.onCommit(); continue; }
/*  971 */        listener.onRollback();
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   final void throwex() throws SQLException {
/*  980 */     throw new SQLException(errmsg());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final void throwex(int errorCode) throws SQLException {
/*  989 */     throw newSQLException(errorCode);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   static final void throwex(int errorCode, String errorMessage) throws SQLiteException {
/*  999 */     throw newSQLException(errorCode, errorMessage);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static SQLiteException newSQLException(int errorCode, String errorMessage) {
/* 1010 */     SQLiteErrorCode code = SQLiteErrorCode.getErrorCode(errorCode);
/*      */     
/* 1012 */     SQLiteException e = new SQLiteException(String.format("%s (%s)", new Object[] { code, errorMessage }), code);
/*      */     
/* 1014 */     return e;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private SQLiteException newSQLException(int errorCode) throws SQLException {
/* 1024 */     return newSQLException(errorCode, errmsg());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   final void ensureAutoCommit(boolean autoCommit) throws SQLException {
/* 1060 */     if (!autoCommit) {
/*      */       return;
/*      */     }
/*      */     
/* 1064 */     if (this.begin == 0L) {
/* 1065 */       this.begin = prepare("begin;");
/*      */     }
/* 1067 */     if (this.commit == 0L) {
/* 1068 */       this.commit = prepare("commit;");
/*      */     }
/*      */     
/*      */     try {
/* 1072 */       if (step(this.begin) != 101) {
/*      */         return;
/*      */       }
/*      */       
/* 1076 */       int rc = step(this.commit);
/* 1077 */       if (rc != 101) {
/* 1078 */         reset(this.commit);
/* 1079 */         throwex(rc);
/*      */       }
/*      */     
/*      */     } finally {
/*      */       
/* 1084 */       reset(this.begin);
/* 1085 */       reset(this.commit);
/*      */     } 
/*      */   }
/*      */   
/*      */   public static interface ProgressObserver {
/*      */     void progress(int param1Int1, int param1Int2);
/*      */   }
/*      */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\sqlite\core\DB.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */