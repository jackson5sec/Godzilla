/*    */ package org.sqlite;
/*    */ 
/*    */ public enum SQLiteLimits
/*    */ {
/*  5 */   SQLITE_LIMIT_LENGTH(0),
/*  6 */   SQLITE_LIMIT_SQL_LENGTH(1),
/*  7 */   SQLITE_LIMIT_COLUMN(2),
/*  8 */   SQLITE_LIMIT_EXPR_DEPTH(3),
/*  9 */   SQLITE_LIMIT_COMPOUND_SELECT(4),
/* 10 */   SQLITE_LIMIT_VDBE_OP(5),
/* 11 */   SQLITE_LIMIT_FUNCTION_ARG(6),
/* 12 */   SQLITE_LIMIT_ATTACHED(7),
/* 13 */   SQLITE_LIMIT_LIKE_PATTERN_LENGTH(8),
/* 14 */   SQLITE_LIMIT_VARIABLE_NUMBER(9),
/* 15 */   SQLITE_LIMIT_TRIGGER_DEPTH(10),
/* 16 */   SQLITE_LIMIT_WORKER_THREADS(11);
/*    */   
/*    */   private final int id;
/*    */   
/*    */   SQLiteLimits(int id) {
/* 21 */     this.id = id;
/*    */   }
/*    */   
/*    */   public int getId() {
/* 25 */     return this.id;
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\sqlite\SQLiteLimits.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */