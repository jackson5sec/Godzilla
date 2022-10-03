/*     */ package org.sqlite.core;
/*     */ 
/*     */ import java.sql.DatabaseMetaData;
/*     */ import java.sql.PreparedStatement;
/*     */ import java.sql.ResultSet;
/*     */ import java.sql.SQLException;
/*     */ import java.util.regex.Pattern;
/*     */ import org.sqlite.SQLiteConnection;
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
/*     */ public abstract class CoreDatabaseMetaData
/*     */   implements DatabaseMetaData
/*     */ {
/*     */   protected SQLiteConnection conn;
/*  30 */   protected PreparedStatement getTables = null, getTableTypes = null, getTypeInfo = null, getCatalogs = null, getSchemas = null, getUDTs = null, getColumnsTblName = null, getSuperTypes = null, getSuperTables = null, getTablePrivileges = null, getIndexInfo = null, getProcedures = null, getProcedureColumns = null, getAttributes = null, getBestRowIdentifier = null, getVersionColumns = null, getColumnPrivileges = null;
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
/*  44 */   protected PreparedStatement getGeneratedKeys = null;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected CoreDatabaseMetaData(SQLiteConnection conn) {
/*  51 */     this.conn = conn;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public abstract ResultSet getGeneratedKeys() throws SQLException;
/*     */ 
/*     */   
/*     */   protected void checkOpen() throws SQLException {
/*  60 */     if (this.conn == null) {
/*  61 */       throw new SQLException("connection closed");
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public synchronized void close() throws SQLException {
/*  69 */     if (this.conn == null) {
/*     */       return;
/*     */     }
/*     */     
/*     */     try {
/*  74 */       if (this.getTables != null) {
/*  75 */         this.getTables.close();
/*     */       }
/*  77 */       if (this.getTableTypes != null) {
/*  78 */         this.getTableTypes.close();
/*     */       }
/*  80 */       if (this.getTypeInfo != null) {
/*  81 */         this.getTypeInfo.close();
/*     */       }
/*  83 */       if (this.getCatalogs != null) {
/*  84 */         this.getCatalogs.close();
/*     */       }
/*  86 */       if (this.getSchemas != null) {
/*  87 */         this.getSchemas.close();
/*     */       }
/*  89 */       if (this.getUDTs != null) {
/*  90 */         this.getUDTs.close();
/*     */       }
/*  92 */       if (this.getColumnsTblName != null) {
/*  93 */         this.getColumnsTblName.close();
/*     */       }
/*  95 */       if (this.getSuperTypes != null) {
/*  96 */         this.getSuperTypes.close();
/*     */       }
/*  98 */       if (this.getSuperTables != null) {
/*  99 */         this.getSuperTables.close();
/*     */       }
/* 101 */       if (this.getTablePrivileges != null) {
/* 102 */         this.getTablePrivileges.close();
/*     */       }
/* 104 */       if (this.getIndexInfo != null) {
/* 105 */         this.getIndexInfo.close();
/*     */       }
/* 107 */       if (this.getProcedures != null) {
/* 108 */         this.getProcedures.close();
/*     */       }
/* 110 */       if (this.getProcedureColumns != null) {
/* 111 */         this.getProcedureColumns.close();
/*     */       }
/* 113 */       if (this.getAttributes != null) {
/* 114 */         this.getAttributes.close();
/*     */       }
/* 116 */       if (this.getBestRowIdentifier != null) {
/* 117 */         this.getBestRowIdentifier.close();
/*     */       }
/* 119 */       if (this.getVersionColumns != null) {
/* 120 */         this.getVersionColumns.close();
/*     */       }
/* 122 */       if (this.getColumnPrivileges != null) {
/* 123 */         this.getColumnPrivileges.close();
/*     */       }
/* 125 */       if (this.getGeneratedKeys != null) {
/* 126 */         this.getGeneratedKeys.close();
/*     */       }
/*     */       
/* 129 */       this.getTables = null;
/* 130 */       this.getTableTypes = null;
/* 131 */       this.getTypeInfo = null;
/* 132 */       this.getCatalogs = null;
/* 133 */       this.getSchemas = null;
/* 134 */       this.getUDTs = null;
/* 135 */       this.getColumnsTblName = null;
/* 136 */       this.getSuperTypes = null;
/* 137 */       this.getSuperTables = null;
/* 138 */       this.getTablePrivileges = null;
/* 139 */       this.getIndexInfo = null;
/* 140 */       this.getProcedures = null;
/* 141 */       this.getProcedureColumns = null;
/* 142 */       this.getAttributes = null;
/* 143 */       this.getBestRowIdentifier = null;
/* 144 */       this.getVersionColumns = null;
/* 145 */       this.getColumnPrivileges = null;
/* 146 */       this.getGeneratedKeys = null;
/*     */     } finally {
/*     */       
/* 149 */       this.conn = null;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected static String quote(String tableName) {
/* 159 */     if (tableName == null) {
/* 160 */       return "null";
/*     */     }
/*     */     
/* 163 */     return String.format("'%s'", new Object[] { tableName });
/*     */   }
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
/*     */   protected String escape(String val) {
/* 176 */     int len = val.length();
/* 177 */     StringBuilder buf = new StringBuilder(len);
/* 178 */     for (int i = 0; i < len; i++) {
/* 179 */       if (val.charAt(i) == '\'') {
/* 180 */         buf.append('\'');
/*     */       }
/* 182 */       buf.append(val.charAt(i));
/*     */     } 
/* 184 */     return buf.toString();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 193 */   protected static final Pattern PK_UNNAMED_PATTERN = Pattern.compile(".*\\sPRIMARY\\s+KEY\\s+\\((.*?,+.*?)\\).*", 34);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 200 */   protected static final Pattern PK_NAMED_PATTERN = Pattern.compile(".*\\sCONSTRAINT\\s+(.*?)\\s+PRIMARY\\s+KEY\\s+\\((.*?)\\).*", 34);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void finalize() throws Throwable {
/* 207 */     close();
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\sqlite\core\CoreDatabaseMetaData.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */