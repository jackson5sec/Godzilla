/*     */ package org.sqlite;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public enum SQLiteErrorCode
/*     */ {
/*  36 */   UNKNOWN_ERROR(-1, "unknown error"),
/*  37 */   SQLITE_OK(0, "Successful result"),
/*     */   
/*  39 */   SQLITE_ERROR(1, "SQL error or missing database"),
/*  40 */   SQLITE_INTERNAL(2, "Internal logic error in SQLite"),
/*  41 */   SQLITE_PERM(3, " Access permission denied"),
/*  42 */   SQLITE_ABORT(4, " Callback routine requested an abort"),
/*  43 */   SQLITE_BUSY(5, " The database file is locked"),
/*  44 */   SQLITE_LOCKED(6, " A table in the database is locked"),
/*  45 */   SQLITE_NOMEM(7, " A malloc() failed"),
/*  46 */   SQLITE_READONLY(8, " Attempt to write a readonly database"),
/*  47 */   SQLITE_INTERRUPT(9, " Operation terminated by sqlite3_interrupt()"),
/*  48 */   SQLITE_IOERR(10, " Some kind of disk I/O error occurred"),
/*  49 */   SQLITE_CORRUPT(11, " The database disk image is malformed"),
/*  50 */   SQLITE_NOTFOUND(12, " NOT USED. Table or record not found"),
/*  51 */   SQLITE_FULL(13, " Insertion failed because database is full"),
/*  52 */   SQLITE_CANTOPEN(14, " Unable to open the database file"),
/*  53 */   SQLITE_PROTOCOL(15, " NOT USED. Database lock protocol error"),
/*  54 */   SQLITE_EMPTY(16, " Database is empty"),
/*  55 */   SQLITE_SCHEMA(17, " The database schema changed"),
/*  56 */   SQLITE_TOOBIG(18, " String or BLOB exceeds size limit"),
/*  57 */   SQLITE_CONSTRAINT(19, " Abort due to constraint violation"),
/*  58 */   SQLITE_MISMATCH(20, " Data type mismatch"),
/*  59 */   SQLITE_MISUSE(21, " Library used incorrectly"),
/*  60 */   SQLITE_NOLFS(22, " Uses OS features not supported on host"),
/*  61 */   SQLITE_AUTH(23, " Authorization denied"),
/*  62 */   SQLITE_FORMAT(24, " Auxiliary database format error"),
/*  63 */   SQLITE_RANGE(25, " 2nd parameter to sqlite3_bind out of range"),
/*  64 */   SQLITE_NOTADB(26, " File opened that is not a database file"),
/*  65 */   SQLITE_ROW(100, " sqlite3_step() has another row ready"),
/*  66 */   SQLITE_DONE(101, " sqlite3_step() has finished executing"),
/*     */   
/*  68 */   SQLITE_BUSY_RECOVERY(261, " Another process is busy recovering a WAL mode database file following a crash"),
/*  69 */   SQLITE_LOCKED_SHAREDCACHE(262, " Contention with a different database connection that shares the cache"),
/*  70 */   SQLITE_READONLY_RECOVERY(264, " The database file needs to be recovered"),
/*  71 */   SQLITE_IOERR_READ(266, " I/O error in the VFS layer while trying to read from a file on disk"),
/*  72 */   SQLITE_CORRUPT_VTAB(267, " Content in the virtual table is corrupt"),
/*  73 */   SQLITE_CONSTRAINT_CHECK(275, " A CHECK constraint failed"),
/*  74 */   SQLITE_ABORT_ROLLBACK(516, " The transaction that was active when the SQL statement first started was rolled back"),
/*  75 */   SQLITE_BUSY_SNAPSHOT(517, " Another database connection has already written to the database"),
/*  76 */   SQLITE_READONLY_CANTLOCK(520, " The shared-memory file associated with that database is read-only"),
/*  77 */   SQLITE_IOERR_SHORT_READ(522, " The VFS layer was unable to obtain as many bytes as was requested"),
/*  78 */   SQLITE_CANTOPEN_ISDIR(526, " The file is really a directory"),
/*  79 */   SQLITE_CONSTRAINT_COMMITHOOK(531, " A commit hook callback returned non-zero"),
/*  80 */   SQLITE_READONLY_ROLLBACK(776, "  Hot journal needs to be rolled back"),
/*  81 */   SQLITE_IOERR_WRITE(778, " I/O error in the VFS layer while trying to write to a file on disk"),
/*  82 */   SQLITE_CANTOPEN_FULLPATH(782, " The operating system was unable to convert the filename into a full pathname"),
/*  83 */   SQLITE_CONSTRAINT_FOREIGNKEY(787, " A foreign key constraint failed"),
/*  84 */   SQLITE_READONLY_DBMOVED(1032, " The database file has been moved since it was opened"),
/*  85 */   SQLITE_IOERR_FSYNC(1034, " I/O error in the VFS layer while trying to flush previously written content"),
/*  86 */   SQLITE_CANTOPEN_CONVPATH(1038, " cygwin_conv_path() system call failed while trying to open a file"),
/*  87 */   SQLITE_CONSTRAINT_FUNCTION(1043, " Error reported by extension function"),
/*  88 */   SQLITE_IOERR_DIR_FSYNC(1290, " I/O error in the VFS layer while trying to invoke fsync() on a directory"),
/*  89 */   SQLITE_CONSTRAINT_NOTNULL(1299, " A NOT NULL constraint failed"),
/*  90 */   SQLITE_IOERR_TRUNCATE(1546, " I/O error in the VFS layer while trying to truncate a file to a smaller size"),
/*  91 */   SQLITE_CONSTRAINT_PRIMARYKEY(1555, " A PRIMARY KEY constraint failed"),
/*  92 */   SQLITE_IOERR_FSTAT(1802, " I/O error in the VFS layer while trying to invoke fstat()"),
/*  93 */   SQLITE_CONSTRAINT_TRIGGER(1811, " A RAISE function within a trigger fired, causing the SQL statement to abort"),
/*  94 */   SQLITE_IOERR_UNLOCK(2058, " I/O error within xUnlock"),
/*  95 */   SQLITE_CONSTRAINT_UNIQUE(2067, " A UNIQUE constraint failed"),
/*  96 */   SQLITE_IOERR_RDLOCK(2314, " I/O error within xLock"),
/*  97 */   SQLITE_CONSTRAINT_VTAB(2323, " Error reported by application-defined virtual table"),
/*  98 */   SQLITE_IOERR_DELETE(2570, " I/O error within xDelete"),
/*  99 */   SQLITE_CONSTRAINT_ROWID(2579, " rowid is not unique"),
/* 100 */   SQLITE_IOERR_NOMEM(3082, " Unable to allocate sufficient memory"),
/* 101 */   SQLITE_IOERR_ACCESS(3338, " I/O error within the xAccess"),
/* 102 */   SQLITE_IOERR_CHECKRESERVEDLOCK(3594, " I/O error within xCheckReservedLock"),
/* 103 */   SQLITE_IOERR_LOCK(3850, " I/O error in the advisory file locking logic"),
/* 104 */   SQLITE_IOERR_CLOSE(4106, " I/O error within xClose"),
/* 105 */   SQLITE_IOERR_SHMOPEN(4618, " I/O error within xShmMap while trying to open a new shared memory segment"),
/* 106 */   SQLITE_IOERR_SHMSIZE(4874, " I/O error within xShmMap while trying to resize an existing shared memory segment"),
/* 107 */   SQLITE_IOERR_SHMMAP(5386, " I/O error within xShmMap while trying to map a shared memory segment"),
/* 108 */   SQLITE_IOERR_SEEK(5642, " I/O error while trying to seek a file descriptor"),
/* 109 */   SQLITE_IOERR_DELETE_NOENT(5898, " The file being deleted does not exist"),
/* 110 */   SQLITE_IOERR_MMAP(6154, " I/O error while trying to map or unmap part of the database file"),
/* 111 */   SQLITE_IOERR_GETTEMPPATH(6410, " Unable to determine a suitable directory in which to place temporary files"),
/* 112 */   SQLITE_IOERR_CONVPATH(6666, " cygwin_conv_path() system call failed");
/*     */ 
/*     */ 
/*     */   
/*     */   public final int code;
/*     */ 
/*     */   
/*     */   public final String message;
/*     */ 
/*     */ 
/*     */   
/*     */   SQLiteErrorCode(int code, String message) {
/* 124 */     this.code = code;
/* 125 */     this.message = message;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static SQLiteErrorCode getErrorCode(int errorCode) {
/* 134 */     for (SQLiteErrorCode each : values()) {
/*     */       
/* 136 */       if (errorCode == each.code)
/* 137 */         return each; 
/*     */     } 
/* 139 */     return UNKNOWN_ERROR;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String toString() {
/* 148 */     return String.format("[%s] %s", new Object[] { name(), this.message });
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\sqlite\SQLiteErrorCode.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */