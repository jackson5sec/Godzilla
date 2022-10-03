/*    */ package org.sqlite.jdbc4;
/*    */ 
/*    */ import java.sql.SQLException;
/*    */ import org.sqlite.SQLiteConnection;
/*    */ import org.sqlite.jdbc3.JDBC3Statement;
/*    */ 
/*    */ public class JDBC4Statement extends JDBC3Statement implements Statement {
/*    */   private boolean closed;
/*    */   boolean closeOnCompletion;
/*    */   
/* 11 */   public JDBC4Statement(SQLiteConnection conn) { super(conn);
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
/* 23 */     this.closed = false; }
/*    */    public <T> T unwrap(Class<T> iface) throws ClassCastException {
/*    */     return iface.cast(this);
/*    */   } public void close() throws SQLException {
/* 27 */     super.close();
/* 28 */     this.closed = true;
/*    */   } public boolean isWrapperFor(Class<?> iface) {
/*    */     return iface.isInstance(this);
/*    */   } public boolean isClosed() {
/* 32 */     return this.closed;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public void closeOnCompletion() throws SQLException {
/* 38 */     if (this.closed) throw new SQLException("statement is closed"); 
/* 39 */     this.closeOnCompletion = true;
/*    */   }
/*    */   
/*    */   public boolean isCloseOnCompletion() throws SQLException {
/* 43 */     if (this.closed) throw new SQLException("statement is closed"); 
/* 44 */     return this.closeOnCompletion;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public void setPoolable(boolean poolable) throws SQLException {}
/*    */ 
/*    */ 
/*    */   
/*    */   public boolean isPoolable() throws SQLException {
/* 54 */     return false;
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\sqlite\jdbc4\JDBC4Statement.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */