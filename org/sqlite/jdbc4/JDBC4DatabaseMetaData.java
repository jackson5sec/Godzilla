/*    */ package org.sqlite.jdbc4;
/*    */ 
/*    */ import java.sql.ResultSet;
/*    */ import java.sql.RowIdLifetime;
/*    */ import java.sql.SQLException;
/*    */ import java.sql.SQLFeatureNotSupportedException;
/*    */ import org.sqlite.SQLiteConnection;
/*    */ import org.sqlite.jdbc3.JDBC3DatabaseMetaData;
/*    */ 
/*    */ public class JDBC4DatabaseMetaData
/*    */   extends JDBC3DatabaseMetaData
/*    */ {
/*    */   public JDBC4DatabaseMetaData(SQLiteConnection conn) {
/* 14 */     super(conn);
/*    */   }
/*    */ 
/*    */   
/*    */   public <T> T unwrap(Class<T> iface) throws ClassCastException {
/* 19 */     return iface.cast(this);
/*    */   }
/*    */   
/*    */   public boolean isWrapperFor(Class<?> iface) {
/* 23 */     return iface.isInstance(this);
/*    */   }
/*    */   
/*    */   public RowIdLifetime getRowIdLifetime() throws SQLException {
/* 27 */     throw new SQLFeatureNotSupportedException();
/*    */   }
/*    */ 
/*    */   
/*    */   public ResultSet getSchemas(String catalog, String schemaPattern) throws SQLException {
/* 32 */     throw new SQLFeatureNotSupportedException();
/*    */   }
/*    */   
/*    */   public boolean supportsStoredFunctionsUsingCallSyntax() throws SQLException {
/* 36 */     throw new SQLFeatureNotSupportedException();
/*    */   }
/*    */   
/*    */   public boolean autoCommitFailureClosesAllResultSets() throws SQLException {
/* 40 */     throw new SQLFeatureNotSupportedException();
/*    */   }
/*    */   
/*    */   public ResultSet getClientInfoProperties() throws SQLException {
/* 44 */     throw new SQLFeatureNotSupportedException();
/*    */   }
/*    */ 
/*    */   
/*    */   public ResultSet getFunctions(String catalog, String schemaPattern, String functionNamePattern) throws SQLException {
/* 49 */     throw new SQLFeatureNotSupportedException();
/*    */   }
/*    */   public ResultSet getPseudoColumns(String catalog, String schemaPattern, String tableNamePattern, String columnNamePattern) throws SQLException {
/* 52 */     throw new SQLFeatureNotSupportedException();
/*    */   }
/*    */   public boolean generatedKeyAlwaysReturned() throws SQLException {
/* 55 */     throw new SQLFeatureNotSupportedException();
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\sqlite\jdbc4\JDBC4DatabaseMetaData.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */