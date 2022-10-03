/*    */ package org.sqlite;
/*    */ 
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
/*    */ public class SQLiteException
/*    */   extends SQLException
/*    */ {
/*    */   private SQLiteErrorCode resultCode;
/*    */   
/*    */   public SQLiteException(String message, SQLiteErrorCode resultCode) {
/* 34 */     super(message, (String)null, resultCode.code & 0xFF);
/* 35 */     this.resultCode = resultCode;
/*    */   }
/*    */   
/*    */   public SQLiteErrorCode getResultCode() {
/* 39 */     return this.resultCode;
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\sqlite\SQLiteException.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */