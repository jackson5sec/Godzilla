/*    */ package org.sqlite;
/*    */ 
/*    */ import java.sql.Connection;
/*    */ import java.sql.SQLException;
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
/*    */ public abstract class ProgressHandler
/*    */ {
/*    */   public static final void setHandler(Connection conn, int vmCalls, ProgressHandler progressHandler) throws SQLException {
/* 20 */     if (conn == null || !(conn instanceof SQLiteConnection)) {
/* 21 */       throw new SQLException("connection must be to an SQLite db");
/*    */     }
/* 23 */     if (conn.isClosed()) {
/* 24 */       throw new SQLException("connection closed");
/*    */     }
/* 26 */     SQLiteConnection sqliteConnection = (SQLiteConnection)conn;
/* 27 */     sqliteConnection.getDatabase().register_progress_handler(vmCalls, progressHandler);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static final void clearHandler(Connection conn) throws SQLException {
/* 37 */     SQLiteConnection sqliteConnection = (SQLiteConnection)conn;
/* 38 */     sqliteConnection.getDatabase().clear_progress_handler();
/*    */   }
/*    */   
/*    */   protected abstract int progress() throws SQLException;
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\sqlite\ProgressHandler.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */