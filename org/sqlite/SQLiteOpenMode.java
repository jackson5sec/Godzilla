/*    */ package org.sqlite;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public enum SQLiteOpenMode
/*    */ {
/* 36 */   READONLY(1),
/* 37 */   READWRITE(2),
/* 38 */   CREATE(4),
/* 39 */   DELETEONCLOSE(8),
/* 40 */   EXCLUSIVE(16),
/* 41 */   OPEN_URI(64),
/* 42 */   OPEN_MEMORY(128),
/* 43 */   MAIN_DB(256),
/* 44 */   TEMP_DB(512),
/* 45 */   TRANSIENT_DB(1024),
/* 46 */   MAIN_JOURNAL(2048),
/* 47 */   TEMP_JOURNAL(4096),
/* 48 */   SUBJOURNAL(8192),
/* 49 */   MASTER_JOURNAL(16384),
/* 50 */   NOMUTEX(32768),
/* 51 */   FULLMUTEX(65536),
/* 52 */   SHAREDCACHE(131072),
/* 53 */   PRIVATECACHE(262144);
/*    */   
/*    */   public final int flag;
/*    */ 
/*    */   
/*    */   SQLiteOpenMode(int flag) {
/* 59 */     this.flag = flag;
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\sqlite\SQLiteOpenMode.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */