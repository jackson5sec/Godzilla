/*     */ package org.sqlite.jdbc4;
/*     */ 
/*     */ import java.io.InputStream;
/*     */ import java.io.Reader;
/*     */ import java.sql.NClob;
/*     */ import java.sql.ParameterMetaData;
/*     */ import java.sql.PreparedStatement;
/*     */ import java.sql.RowId;
/*     */ import java.sql.SQLException;
/*     */ import java.sql.SQLFeatureNotSupportedException;
/*     */ import java.sql.SQLXML;
/*     */ import java.util.Arrays;
/*     */ import org.sqlite.SQLiteConnection;
/*     */ import org.sqlite.jdbc3.JDBC3PreparedStatement;
/*     */ 
/*     */ public class JDBC4PreparedStatement
/*     */   extends JDBC3PreparedStatement
/*     */   implements PreparedStatement, ParameterMetaData
/*     */ {
/*     */   public String toString() {
/*  21 */     return this.sql + " \n parameters=" + Arrays.toString(this.batch);
/*     */   }
/*     */   
/*     */   public JDBC4PreparedStatement(SQLiteConnection conn, String sql) throws SQLException {
/*  25 */     super(conn, sql);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void setRowId(int parameterIndex, RowId x) throws SQLException {
/*  31 */     throw new SQLFeatureNotSupportedException();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void setNString(int parameterIndex, String value) throws SQLException {
/*  37 */     throw new SQLFeatureNotSupportedException();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void setNCharacterStream(int parameterIndex, Reader value, long length) throws SQLException {
/*  43 */     throw new SQLFeatureNotSupportedException();
/*     */   }
/*     */ 
/*     */   
/*     */   public void setNClob(int parameterIndex, NClob value) throws SQLException {
/*  48 */     throw new SQLFeatureNotSupportedException();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void setClob(int parameterIndex, Reader reader, long length) throws SQLException {
/*  54 */     throw new SQLFeatureNotSupportedException();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void setBlob(int parameterIndex, InputStream inputStream, long length) throws SQLException {
/*  60 */     throw new SQLFeatureNotSupportedException();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void setNClob(int parameterIndex, Reader reader, long length) throws SQLException {
/*  66 */     throw new SQLFeatureNotSupportedException();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void setSQLXML(int parameterIndex, SQLXML xmlObject) throws SQLException {
/*  72 */     throw new SQLFeatureNotSupportedException();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void setAsciiStream(int parameterIndex, InputStream x, long length) throws SQLException {
/*  78 */     throw new SQLFeatureNotSupportedException();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void setBinaryStream(int parameterIndex, InputStream x, long length) throws SQLException {
/*  84 */     throw new SQLFeatureNotSupportedException();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void setCharacterStream(int parameterIndex, Reader reader, long length) throws SQLException {
/*  90 */     throw new SQLFeatureNotSupportedException();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void setAsciiStream(int parameterIndex, InputStream x) throws SQLException {
/*  96 */     throw new SQLFeatureNotSupportedException();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void setBinaryStream(int parameterIndex, InputStream x) throws SQLException {
/* 102 */     throw new SQLFeatureNotSupportedException();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void setCharacterStream(int parameterIndex, Reader reader) throws SQLException {
/* 108 */     throw new SQLFeatureNotSupportedException();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void setNCharacterStream(int parameterIndex, Reader value) throws SQLException {
/* 114 */     throw new SQLFeatureNotSupportedException();
/*     */   }
/*     */ 
/*     */   
/*     */   public void setClob(int parameterIndex, Reader reader) throws SQLException {
/* 119 */     throw new SQLFeatureNotSupportedException();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void setBlob(int parameterIndex, InputStream inputStream) throws SQLException {
/* 125 */     throw new SQLFeatureNotSupportedException();
/*     */   }
/*     */ 
/*     */   
/*     */   public void setNClob(int parameterIndex, Reader reader) throws SQLException {
/* 130 */     throw new SQLFeatureNotSupportedException();
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\sqlite\jdbc4\JDBC4PreparedStatement.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */