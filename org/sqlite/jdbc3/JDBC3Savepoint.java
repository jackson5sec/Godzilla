/*    */ package org.sqlite.jdbc3;
/*    */ 
/*    */ import java.sql.SQLException;
/*    */ import java.sql.Savepoint;
/*    */ 
/*    */ public class JDBC3Savepoint
/*    */   implements Savepoint
/*    */ {
/*    */   final int id;
/*    */   final String name;
/*    */   
/*    */   JDBC3Savepoint(int id) {
/* 13 */     this.id = id;
/* 14 */     this.name = null;
/*    */   }
/*    */   
/*    */   JDBC3Savepoint(int id, String name) {
/* 18 */     this.id = id;
/* 19 */     this.name = name;
/*    */   }
/*    */   
/*    */   public int getSavepointId() throws SQLException {
/* 23 */     return this.id;
/*    */   }
/*    */   
/*    */   public String getSavepointName() throws SQLException {
/* 27 */     return (this.name == null) ? String.format("SQLITE_SAVEPOINT_%s", new Object[] { Integer.valueOf(this.id) }) : this.name;
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\sqlite\jdbc3\JDBC3Savepoint.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */