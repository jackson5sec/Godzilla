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
/*    */ 
/*    */ public abstract class BusyHandler
/*    */ {
/*    */   private static void commitHandler(Connection conn, BusyHandler busyHandler) throws SQLException {
/* 21 */     if (conn == null || !(conn instanceof SQLiteConnection)) {
/* 22 */       throw new SQLException("connection must be to an SQLite db");
/*    */     }
/*    */     
/* 25 */     if (conn.isClosed()) {
/* 26 */       throw new SQLException("connection closed");
/*    */     }
/*    */     
/* 29 */     SQLiteConnection sqliteConnection = (SQLiteConnection)conn;
/* 30 */     sqliteConnection.getDatabase().busy_handler(busyHandler);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static final void setHandler(Connection conn, BusyHandler busyHandler) throws SQLException {
/* 41 */     commitHandler(conn, busyHandler);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static final void clearHandler(Connection conn) throws SQLException {
/* 51 */     commitHandler(conn, null);
/*    */   }
/*    */   
/*    */   protected abstract int callback(int paramInt) throws SQLException;
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\sqlite\BusyHandler.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */