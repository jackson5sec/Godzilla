/*      */ package org.sqlite.jdbc3;
/*      */ 
/*      */ import java.io.IOException;
/*      */ import java.io.InputStream;
/*      */ import java.sql.Connection;
/*      */ import java.sql.ResultSet;
/*      */ import java.sql.SQLException;
/*      */ import java.sql.Statement;
/*      */ import java.sql.Struct;
/*      */ import java.util.ArrayList;
/*      */ import java.util.Collections;
/*      */ import java.util.HashMap;
/*      */ import java.util.Iterator;
/*      */ import java.util.List;
/*      */ import java.util.Map;
/*      */ import java.util.Properties;
/*      */ import java.util.regex.Matcher;
/*      */ import java.util.regex.Pattern;
/*      */ import org.sqlite.SQLiteConnection;
/*      */ import org.sqlite.core.CoreDatabaseMetaData;
/*      */ import org.sqlite.core.CoreStatement;
/*      */ import org.sqlite.util.StringUtils;
/*      */ 
/*      */ 
/*      */ 
/*      */ public abstract class JDBC3DatabaseMetaData
/*      */   extends CoreDatabaseMetaData
/*      */ {
/*      */   private static String driverName;
/*      */   private static String driverVersion;
/*      */   
/*      */   static {
/*   33 */     InputStream sqliteJdbcPropStream = null;
/*      */     try {
/*   35 */       sqliteJdbcPropStream = JDBC3DatabaseMetaData.class.getClassLoader().getResourceAsStream("sqlite-jdbc.properties");
/*   36 */       if (sqliteJdbcPropStream == null) {
/*   37 */         throw new IOException("Cannot load sqlite-jdbc.properties from jar");
/*      */       }
/*   39 */       Properties sqliteJdbcProp = new Properties();
/*   40 */       sqliteJdbcProp.load(sqliteJdbcPropStream);
/*   41 */       driverName = sqliteJdbcProp.getProperty("name");
/*   42 */       driverVersion = sqliteJdbcProp.getProperty("version");
/*   43 */     } catch (Exception e) {
/*      */       
/*   45 */       driverName = "SQLite JDBC";
/*   46 */       driverVersion = "3.0.0-UNKNOWN";
/*      */     } finally {
/*   48 */       if (null != sqliteJdbcPropStream) {
/*      */         
/*      */         try {
/*      */           
/*   52 */           sqliteJdbcPropStream.close();
/*      */         }
/*   54 */         catch (Exception exception) {}
/*      */       }
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected JDBC3DatabaseMetaData(SQLiteConnection conn) {
/*   63 */     super(conn);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Connection getConnection() {
/*   70 */     return (Connection)this.conn;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int getDatabaseMajorVersion() throws SQLException {
/*   77 */     return Integer.valueOf(this.conn.libversion().split("\\.")[0]).intValue();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int getDatabaseMinorVersion() throws SQLException {
/*   84 */     return Integer.valueOf(this.conn.libversion().split("\\.")[1]).intValue();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int getDriverMajorVersion() {
/*   91 */     return Integer.valueOf(driverVersion.split("\\.")[0]).intValue();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int getDriverMinorVersion() {
/*   98 */     return Integer.valueOf(driverVersion.split("\\.")[1]).intValue();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int getJDBCMajorVersion() {
/*  105 */     return 2;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int getJDBCMinorVersion() {
/*  112 */     return 1;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int getDefaultTransactionIsolation() {
/*  119 */     return 8;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int getMaxBinaryLiteralLength() {
/*  126 */     return 0;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int getMaxCatalogNameLength() {
/*  133 */     return 0;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int getMaxCharLiteralLength() {
/*  140 */     return 0;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int getMaxColumnNameLength() {
/*  147 */     return 0;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int getMaxColumnsInGroupBy() {
/*  154 */     return 0;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int getMaxColumnsInIndex() {
/*  161 */     return 0;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int getMaxColumnsInOrderBy() {
/*  168 */     return 0;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int getMaxColumnsInSelect() {
/*  175 */     return 0;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int getMaxColumnsInTable() {
/*  182 */     return 0;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int getMaxConnections() {
/*  189 */     return 0;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int getMaxCursorNameLength() {
/*  196 */     return 0;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int getMaxIndexLength() {
/*  203 */     return 0;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int getMaxProcedureNameLength() {
/*  210 */     return 0;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int getMaxRowSize() {
/*  217 */     return 0;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int getMaxSchemaNameLength() {
/*  224 */     return 0;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int getMaxStatementLength() {
/*  231 */     return 0;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int getMaxStatements() {
/*  238 */     return 0;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int getMaxTableNameLength() {
/*  245 */     return 0;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int getMaxTablesInSelect() {
/*  252 */     return 0;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int getMaxUserNameLength() {
/*  259 */     return 0;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int getResultSetHoldability() {
/*  266 */     return 2;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int getSQLStateType() {
/*  273 */     return 2;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String getDatabaseProductName() {
/*  280 */     return "SQLite";
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String getDatabaseProductVersion() throws SQLException {
/*  287 */     return this.conn.libversion();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String getDriverName() {
/*  294 */     return driverName;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String getDriverVersion() {
/*  301 */     return driverVersion;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String getExtraNameCharacters() {
/*  308 */     return "";
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String getCatalogSeparator() {
/*  315 */     return ".";
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String getCatalogTerm() {
/*  322 */     return "catalog";
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String getSchemaTerm() {
/*  329 */     return "schema";
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String getProcedureTerm() {
/*  336 */     return "not_implemented";
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String getSearchStringEscape() {
/*  343 */     return null;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String getIdentifierQuoteString() {
/*  350 */     return "\"";
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String getSQLKeywords() {
/*  359 */     return "ABORT,ACTION,AFTER,ANALYZE,ATTACH,AUTOINCREMENT,BEFORE,CASCADE,CONFLICT,DATABASE,DEFERRABLE,DEFERRED,DESC,DETACH,EXCLUSIVE,EXPLAIN,FAIL,GLOB,IGNORE,INDEX,INDEXED,INITIALLY,INSTEAD,ISNULL,KEY,LIMIT,NOTNULL,OFFSET,PLAN,PRAGMA,QUERY,RAISE,REGEXP,REINDEX,RENAME,REPLACE,RESTRICT,TEMP,TEMPORARY,TRANSACTION,VACUUM,VIEW,VIRTUAL";
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String getNumericFunctions() {
/*  371 */     return "";
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String getStringFunctions() {
/*  378 */     return "";
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String getSystemFunctions() {
/*  385 */     return "";
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String getTimeDateFunctions() {
/*  392 */     return "DATE,TIME,DATETIME,JULIANDAY,STRFTIME";
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String getURL() {
/*  399 */     return this.conn.getUrl();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String getUserName() {
/*  406 */     return null;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean allProceduresAreCallable() {
/*  413 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean allTablesAreSelectable() {
/*  420 */     return true;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean dataDefinitionCausesTransactionCommit() {
/*  427 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean dataDefinitionIgnoredInTransactions() {
/*  434 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean doesMaxRowSizeIncludeBlobs() {
/*  441 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean deletesAreDetected(int type) {
/*  448 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean insertsAreDetected(int type) {
/*  455 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean isCatalogAtStart() {
/*  462 */     return true;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean locatorsUpdateCopy() {
/*  469 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean nullPlusNonNullIsNull() {
/*  476 */     return true;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean nullsAreSortedAtEnd() {
/*  483 */     return !nullsAreSortedAtStart();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean nullsAreSortedAtStart() {
/*  490 */     return true;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean nullsAreSortedHigh() {
/*  497 */     return true;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean nullsAreSortedLow() {
/*  504 */     return !nullsAreSortedHigh();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean othersDeletesAreVisible(int type) {
/*  511 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean othersInsertsAreVisible(int type) {
/*  518 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean othersUpdatesAreVisible(int type) {
/*  525 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean ownDeletesAreVisible(int type) {
/*  532 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean ownInsertsAreVisible(int type) {
/*  539 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean ownUpdatesAreVisible(int type) {
/*  546 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean storesLowerCaseIdentifiers() {
/*  553 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean storesLowerCaseQuotedIdentifiers() {
/*  560 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean storesMixedCaseIdentifiers() {
/*  567 */     return true;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean storesMixedCaseQuotedIdentifiers() {
/*  574 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean storesUpperCaseIdentifiers() {
/*  581 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean storesUpperCaseQuotedIdentifiers() {
/*  588 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean supportsAlterTableWithAddColumn() {
/*  595 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean supportsAlterTableWithDropColumn() {
/*  602 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean supportsANSI92EntryLevelSQL() {
/*  609 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean supportsANSI92FullSQL() {
/*  616 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean supportsANSI92IntermediateSQL() {
/*  623 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean supportsBatchUpdates() {
/*  630 */     return true;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean supportsCatalogsInDataManipulation() {
/*  637 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean supportsCatalogsInIndexDefinitions() {
/*  644 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean supportsCatalogsInPrivilegeDefinitions() {
/*  651 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean supportsCatalogsInProcedureCalls() {
/*  658 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean supportsCatalogsInTableDefinitions() {
/*  665 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean supportsColumnAliasing() {
/*  672 */     return true;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean supportsConvert() {
/*  679 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean supportsConvert(int fromType, int toType) {
/*  686 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean supportsCorrelatedSubqueries() {
/*  693 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean supportsDataDefinitionAndDataManipulationTransactions() {
/*  700 */     return true;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean supportsDataManipulationTransactionsOnly() {
/*  707 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean supportsDifferentTableCorrelationNames() {
/*  714 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean supportsExpressionsInOrderBy() {
/*  721 */     return true;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean supportsMinimumSQLGrammar() {
/*  728 */     return true;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean supportsCoreSQLGrammar() {
/*  735 */     return true;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean supportsExtendedSQLGrammar() {
/*  742 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean supportsLimitedOuterJoins() {
/*  749 */     return true;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean supportsFullOuterJoins() {
/*  756 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean supportsGetGeneratedKeys() {
/*  763 */     return true;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean supportsGroupBy() {
/*  770 */     return true;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean supportsGroupByBeyondSelect() {
/*  777 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean supportsGroupByUnrelated() {
/*  784 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean supportsIntegrityEnhancementFacility() {
/*  791 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean supportsLikeEscapeClause() {
/*  798 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean supportsMixedCaseIdentifiers() {
/*  805 */     return true;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean supportsMixedCaseQuotedIdentifiers() {
/*  812 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean supportsMultipleOpenResults() {
/*  819 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean supportsMultipleResultSets() {
/*  826 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean supportsMultipleTransactions() {
/*  833 */     return true;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean supportsNamedParameters() {
/*  840 */     return true;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean supportsNonNullableColumns() {
/*  847 */     return true;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean supportsOpenCursorsAcrossCommit() {
/*  854 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean supportsOpenCursorsAcrossRollback() {
/*  861 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean supportsOpenStatementsAcrossCommit() {
/*  868 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean supportsOpenStatementsAcrossRollback() {
/*  875 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean supportsOrderByUnrelated() {
/*  882 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean supportsOuterJoins() {
/*  889 */     return true;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean supportsPositionedDelete() {
/*  896 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean supportsPositionedUpdate() {
/*  903 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean supportsResultSetConcurrency(int t, int c) {
/*  910 */     return (t == 1003 && c == 1007);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean supportsResultSetHoldability(int h) {
/*  917 */     return (h == 2);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean supportsResultSetType(int t) {
/*  924 */     return (t == 1003);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean supportsSavepoints() {
/*  931 */     return true;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean supportsSchemasInDataManipulation() {
/*  938 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean supportsSchemasInIndexDefinitions() {
/*  945 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean supportsSchemasInPrivilegeDefinitions() {
/*  952 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean supportsSchemasInProcedureCalls() {
/*  959 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean supportsSchemasInTableDefinitions() {
/*  966 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean supportsSelectForUpdate() {
/*  973 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean supportsStatementPooling() {
/*  980 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean supportsStoredProcedures() {
/*  987 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean supportsSubqueriesInComparisons() {
/*  994 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean supportsSubqueriesInExists() {
/* 1001 */     return true;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean supportsSubqueriesInIns() {
/* 1008 */     return true;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean supportsSubqueriesInQuantifieds() {
/* 1015 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean supportsTableCorrelationNames() {
/* 1022 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean supportsTransactionIsolationLevel(int level) {
/* 1029 */     return (level == 8);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean supportsTransactions() {
/* 1036 */     return true;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean supportsUnion() {
/* 1043 */     return true;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean supportsUnionAll() {
/* 1050 */     return true;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean updatesAreDetected(int type) {
/* 1057 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean usesLocalFilePerTable() {
/* 1064 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean usesLocalFiles() {
/* 1071 */     return true;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean isReadOnly() throws SQLException {
/* 1078 */     return this.conn.isReadOnly();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public ResultSet getAttributes(String c, String s, String t, String a) throws SQLException {
/* 1086 */     if (this.getAttributes == null) {
/* 1087 */       this.getAttributes = this.conn.prepareStatement("select null as TYPE_CAT, null as TYPE_SCHEM, null as TYPE_NAME, null as ATTR_NAME, null as DATA_TYPE, null as ATTR_TYPE_NAME, null as ATTR_SIZE, null as DECIMAL_DIGITS, null as NUM_PREC_RADIX, null as NULLABLE, null as REMARKS, null as ATTR_DEF, null as SQL_DATA_TYPE, null as SQL_DATETIME_SUB, null as CHAR_OCTET_LENGTH, null as ORDINAL_POSITION, null as IS_NULLABLE, null as SCOPE_CATALOG, null as SCOPE_SCHEMA, null as SCOPE_TABLE, null as SOURCE_DATA_TYPE limit 0;");
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1096 */     return this.getAttributes.executeQuery();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public ResultSet getBestRowIdentifier(String c, String s, String t, int scope, boolean n) throws SQLException {
/* 1104 */     if (this.getBestRowIdentifier == null) {
/* 1105 */       this.getBestRowIdentifier = this.conn.prepareStatement("select null as SCOPE, null as COLUMN_NAME, null as DATA_TYPE, null as TYPE_NAME, null as COLUMN_SIZE, null as BUFFER_LENGTH, null as DECIMAL_DIGITS, null as PSEUDO_COLUMN limit 0;");
/*      */     }
/*      */ 
/*      */ 
/*      */     
/* 1110 */     return this.getBestRowIdentifier.executeQuery();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public ResultSet getColumnPrivileges(String c, String s, String t, String colPat) throws SQLException {
/* 1118 */     if (this.getColumnPrivileges == null) {
/* 1119 */       this.getColumnPrivileges = this.conn.prepareStatement("select null as TABLE_CAT, null as TABLE_SCHEM, null as TABLE_NAME, null as COLUMN_NAME, null as GRANTOR, null as GRANTEE, null as PRIVILEGE, null as IS_GRANTABLE limit 0;");
/*      */     }
/*      */ 
/*      */ 
/*      */     
/* 1124 */     return this.getColumnPrivileges.executeQuery();
/*      */   }
/*      */ 
/*      */   
/* 1128 */   protected static final Pattern TYPE_INTEGER = Pattern.compile(".*(INT|BOOL).*");
/* 1129 */   protected static final Pattern TYPE_VARCHAR = Pattern.compile(".*(CHAR|CLOB|TEXT|BLOB).*");
/* 1130 */   protected static final Pattern TYPE_FLOAT = Pattern.compile(".*(REAL|FLOA|DOUB|DEC|NUM).*");
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public ResultSet getColumns(String c, String s, String tblNamePattern, String colNamePattern) throws SQLException {
/* 1185 */     checkOpen();
/*      */     
/* 1187 */     StringBuilder sql = new StringBuilder(700);
/* 1188 */     sql.append("select null as TABLE_CAT, null as TABLE_SCHEM, tblname as TABLE_NAME, ")
/* 1189 */       .append("cn as COLUMN_NAME, ct as DATA_TYPE, tn as TYPE_NAME, 2000000000 as COLUMN_SIZE, ")
/* 1190 */       .append("2000000000 as BUFFER_LENGTH, 10   as DECIMAL_DIGITS, 10   as NUM_PREC_RADIX, ")
/* 1191 */       .append("colnullable as NULLABLE, null as REMARKS, colDefault as COLUMN_DEF, ")
/* 1192 */       .append("0    as SQL_DATA_TYPE, 0    as SQL_DATETIME_SUB, 2000000000 as CHAR_OCTET_LENGTH, ")
/* 1193 */       .append("ordpos as ORDINAL_POSITION, (case colnullable when 0 then 'NO' when 1 then 'YES' else '' end)")
/* 1194 */       .append("    as IS_NULLABLE, null as SCOPE_CATLOG, null as SCOPE_SCHEMA, ")
/* 1195 */       .append("null as SCOPE_TABLE, null as SOURCE_DATA_TYPE, ")
/* 1196 */       .append("(case colautoincrement when 0 then 'NO' when 1 then 'YES' else '' end) as IS_AUTOINCREMENT, ")
/* 1197 */       .append("'' as IS_GENERATEDCOLUMN from (");
/*      */     
/* 1199 */     boolean colFound = false;
/*      */     
/* 1201 */     ResultSet rs = null;
/*      */     
/*      */     try {
/* 1204 */       String[] types = { "TABLE", "VIEW" };
/* 1205 */       rs = getTables(c, s, tblNamePattern, types);
/* 1206 */       while (rs.next()) {
/* 1207 */         String tableName = rs.getString(3);
/*      */         
/* 1209 */         boolean isAutoIncrement = false;
/*      */         
/* 1211 */         Statement statColAutoinc = this.conn.createStatement();
/* 1212 */         ResultSet rsColAutoinc = null;
/*      */         try {
/* 1214 */           statColAutoinc = this.conn.createStatement();
/* 1215 */           rsColAutoinc = statColAutoinc.executeQuery("SELECT LIKE('%autoincrement%', LOWER(sql)) FROM sqlite_master WHERE LOWER(name) = LOWER('" + 
/* 1216 */               escape(tableName) + "') AND TYPE IN ('table', 'view')");
/* 1217 */           rsColAutoinc.next();
/* 1218 */           isAutoIncrement = (rsColAutoinc.getInt(1) == 1);
/*      */         } finally {
/* 1220 */           if (rsColAutoinc != null) {
/*      */             try {
/* 1222 */               rsColAutoinc.close();
/* 1223 */             } catch (Exception e) {
/* 1224 */               e.printStackTrace();
/*      */             } 
/*      */           }
/* 1227 */           if (statColAutoinc != null) {
/*      */             try {
/* 1229 */               statColAutoinc.close();
/* 1230 */             } catch (Exception e) {
/* 1231 */               e.printStackTrace();
/*      */             } 
/*      */           }
/*      */         } 
/*      */         
/* 1236 */         Statement colstat = this.conn.createStatement();
/* 1237 */         ResultSet rscol = null;
/*      */         
/*      */         try {
/* 1240 */           String pragmaStatement = "PRAGMA table_info('" + escape(tableName) + "')";
/* 1241 */           rscol = colstat.executeQuery(pragmaStatement);
/*      */           
/* 1243 */           for (int i = 0; rscol.next(); i++) {
/* 1244 */             String colName = rscol.getString(2);
/* 1245 */             String colType = rscol.getString(3);
/* 1246 */             String colNotNull = rscol.getString(4);
/* 1247 */             String colDefault = rscol.getString(5);
/* 1248 */             boolean isPk = "1".equals(rscol.getString(6));
/*      */             
/* 1250 */             int colNullable = 2;
/* 1251 */             if (colNotNull != null) {
/* 1252 */               colNullable = colNotNull.equals("0") ? 1 : 0;
/*      */             }
/*      */             
/* 1255 */             if (colFound) {
/* 1256 */               sql.append(" union all ");
/*      */             }
/* 1258 */             colFound = true;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */             
/* 1265 */             colType = (colType == null) ? "TEXT" : colType.toUpperCase();
/*      */             
/* 1267 */             int colAutoIncrement = 0;
/* 1268 */             if (isPk && isAutoIncrement)
/*      */             {
/* 1270 */               colAutoIncrement = 1;
/*      */             }
/* 1272 */             int colJavaType = -1;
/*      */             
/* 1274 */             if (TYPE_INTEGER.matcher(colType).find()) {
/* 1275 */               colJavaType = 4;
/*      */             }
/* 1277 */             else if (TYPE_VARCHAR.matcher(colType).find()) {
/* 1278 */               colJavaType = 12;
/*      */             }
/* 1280 */             else if (TYPE_FLOAT.matcher(colType).find()) {
/* 1281 */               colJavaType = 6;
/*      */             }
/*      */             else {
/*      */               
/* 1285 */               colJavaType = 12;
/*      */             } 
/*      */             
/* 1288 */             sql.append("select ").append(i + 1).append(" as ordpos, ")
/* 1289 */               .append(colNullable).append(" as colnullable,")
/* 1290 */               .append("'").append(colJavaType).append("' as ct, ")
/* 1291 */               .append("'").append(tableName).append("' as tblname, ")
/* 1292 */               .append("'").append(escape(colName)).append("' as cn, ")
/* 1293 */               .append("'").append(escape(colType)).append("' as tn, ")
/* 1294 */               .append(quote((colDefault == null) ? null : escape(colDefault))).append(" as colDefault,")
/* 1295 */               .append(colAutoIncrement).append(" as colautoincrement");
/*      */             
/* 1297 */             if (colNamePattern != null) {
/* 1298 */               sql.append(" where upper(cn) like upper('").append(escape(colNamePattern)).append("')");
/*      */             }
/*      */           } 
/*      */         } finally {
/* 1302 */           if (rscol != null) {
/*      */             try {
/* 1304 */               rscol.close();
/* 1305 */             } catch (SQLException sQLException) {}
/*      */           }
/* 1307 */           if (colstat != null) {
/*      */             try {
/* 1309 */               colstat.close();
/* 1310 */             } catch (SQLException sQLException) {}
/*      */           }
/*      */         } 
/*      */       } 
/*      */     } finally {
/* 1315 */       if (rs != null) {
/*      */         try {
/* 1317 */           rs.close();
/* 1318 */         } catch (Exception e) {
/* 1319 */           e.printStackTrace();
/*      */         } 
/*      */       }
/*      */     } 
/*      */     
/* 1324 */     if (colFound) {
/* 1325 */       sql.append(") order by TABLE_SCHEM, TABLE_NAME, ORDINAL_POSITION;");
/*      */     } else {
/*      */       
/* 1328 */       sql.append("select null as ordpos, null as colnullable, null as ct, null as tblname, null as cn, null as tn, null as colDefault, null as colautoincrement) limit 0;");
/*      */     } 
/*      */     
/* 1331 */     Statement stat = this.conn.createStatement();
/* 1332 */     return ((CoreStatement)stat).executeQuery(sql.toString(), true);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public ResultSet getCrossReference(String pc, String ps, String pt, String fc, String fs, String ft) throws SQLException {
/* 1339 */     if (pt == null) {
/* 1340 */       return getExportedKeys(fc, fs, ft);
/*      */     }
/*      */     
/* 1343 */     if (ft == null) {
/* 1344 */       return getImportedKeys(pc, ps, pt);
/*      */     }
/*      */     
/* 1347 */     StringBuilder query = new StringBuilder();
/* 1348 */     query.append("select ").append(quote(pc)).append(" as PKTABLE_CAT, ")
/* 1349 */       .append(quote(ps)).append(" as PKTABLE_SCHEM, ").append(quote(pt)).append(" as PKTABLE_NAME, ")
/* 1350 */       .append("'' as PKCOLUMN_NAME, ").append(quote(fc)).append(" as FKTABLE_CAT, ")
/* 1351 */       .append(quote(fs)).append(" as FKTABLE_SCHEM, ").append(quote(ft)).append(" as FKTABLE_NAME, ")
/* 1352 */       .append("'' as FKCOLUMN_NAME, -1 as KEY_SEQ, 3 as UPDATE_RULE, 3 as DELETE_RULE, '' as FK_NAME, '' as PK_NAME, ")
/* 1353 */       .append(Integer.toString(5)).append(" as DEFERRABILITY limit 0 ");
/*      */     
/* 1355 */     return ((CoreStatement)this.conn.createStatement()).executeQuery(query.toString(), true);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public ResultSet getSchemas() throws SQLException {
/* 1362 */     if (this.getSchemas == null) {
/* 1363 */       this.getSchemas = this.conn.prepareStatement("select null as TABLE_SCHEM, null as TABLE_CATALOG limit 0;");
/*      */     }
/*      */     
/* 1366 */     return this.getSchemas.executeQuery();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public ResultSet getCatalogs() throws SQLException {
/* 1373 */     if (this.getCatalogs == null) {
/* 1374 */       this.getCatalogs = this.conn.prepareStatement("select null as TABLE_CAT limit 0;");
/*      */     }
/*      */     
/* 1377 */     return this.getCatalogs.executeQuery();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public ResultSet getPrimaryKeys(String c, String s, String table) throws SQLException {
/* 1385 */     PrimaryKeyFinder pkFinder = new PrimaryKeyFinder(table);
/* 1386 */     String[] columns = pkFinder.getColumns();
/*      */     
/* 1388 */     Statement stat = this.conn.createStatement();
/* 1389 */     StringBuilder sql = new StringBuilder(512);
/* 1390 */     sql.append("select null as TABLE_CAT, null as TABLE_SCHEM, '")
/* 1391 */       .append(escape(table))
/* 1392 */       .append("' as TABLE_NAME, cn as COLUMN_NAME, ks as KEY_SEQ, pk as PK_NAME from (");
/*      */     
/* 1394 */     if (columns == null) {
/* 1395 */       sql.append("select null as cn, null as pk, 0 as ks) limit 0;");
/*      */       
/* 1397 */       return ((CoreStatement)stat).executeQuery(sql.toString(), true);
/*      */     } 
/*      */     
/* 1400 */     String pkName = pkFinder.getName();
/* 1401 */     if (pkName != null) {
/* 1402 */       pkName = "'" + pkName + "'";
/*      */     }
/*      */     
/* 1405 */     for (int i = 0; i < columns.length; i++) {
/* 1406 */       if (i > 0) sql.append(" union "); 
/* 1407 */       sql.append("select ").append(pkName).append(" as pk, '")
/* 1408 */         .append(escape(unquoteIdentifier(columns[i]))).append("' as cn, ")
/* 1409 */         .append(i + 1).append(" as ks");
/*      */     } 
/*      */     
/* 1412 */     return ((CoreStatement)stat).executeQuery(sql.append(") order by cn;").toString(), true);
/*      */   }
/*      */   
/* 1415 */   private static final Map<String, Integer> RULE_MAP = new HashMap<>();
/*      */   
/*      */   static {
/* 1418 */     RULE_MAP.put("NO ACTION", Integer.valueOf(3));
/* 1419 */     RULE_MAP.put("CASCADE", Integer.valueOf(0));
/* 1420 */     RULE_MAP.put("RESTRICT", Integer.valueOf(1));
/* 1421 */     RULE_MAP.put("SET NULL", Integer.valueOf(2));
/* 1422 */     RULE_MAP.put("SET DEFAULT", Integer.valueOf(4));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public ResultSet getExportedKeys(String catalog, String schema, String table) throws SQLException {
/* 1430 */     PrimaryKeyFinder pkFinder = new PrimaryKeyFinder(table);
/* 1431 */     String[] pkColumns = pkFinder.getColumns();
/* 1432 */     Statement stat = this.conn.createStatement();
/*      */     
/* 1434 */     catalog = (catalog != null) ? quote(catalog) : null;
/* 1435 */     schema = (schema != null) ? quote(schema) : null;
/*      */     
/* 1437 */     StringBuilder exportedKeysQuery = new StringBuilder(512);
/*      */     
/* 1439 */     String target = null;
/* 1440 */     int count = 0;
/* 1441 */     if (pkColumns != null) {
/*      */       
/* 1443 */       ResultSet rs = stat.executeQuery("select name from sqlite_master where type = 'table'");
/* 1444 */       ArrayList<String> tableList = new ArrayList<>();
/*      */       
/* 1446 */       while (rs.next()) {
/* 1447 */         String tblname = rs.getString(1);
/* 1448 */         tableList.add(tblname);
/* 1449 */         if (tblname.equalsIgnoreCase(table))
/*      */         {
/*      */           
/* 1452 */           target = tblname;
/*      */         }
/*      */       } 
/*      */       
/* 1456 */       rs.close();
/*      */ 
/*      */       
/* 1459 */       for (String tbl : tableList) {
/*      */         try {
/* 1461 */           ImportedKeyFinder impFkFinder = new ImportedKeyFinder(tbl);
/* 1462 */           List<ImportedKeyFinder.ForeignKey> fkNames = impFkFinder.getFkList();
/*      */           
/* 1464 */           for (Iterator<ImportedKeyFinder.ForeignKey> iterator = fkNames.iterator(); iterator.hasNext(); ) {
/* 1465 */             ImportedKeyFinder.ForeignKey foreignKey = iterator.next();
/*      */             
/* 1467 */             String PKTabName = foreignKey.getPkTableName();
/*      */             
/* 1469 */             if (PKTabName == null || !PKTabName.equalsIgnoreCase(target)) {
/*      */               continue;
/*      */             }
/*      */             
/* 1473 */             for (int j = 0; j < foreignKey.getColumnMappingCount(); j++) {
/* 1474 */               int keySeq = j + 1;
/* 1475 */               String[] columnMapping = foreignKey.getColumnMapping(j);
/* 1476 */               String PKColName = columnMapping[1];
/* 1477 */               PKColName = (PKColName == null) ? "" : PKColName;
/* 1478 */               String FKColName = columnMapping[0];
/* 1479 */               FKColName = (FKColName == null) ? "" : FKColName;
/*      */               
/* 1481 */               boolean usePkName = false;
/* 1482 */               for (int k = 0; k < pkColumns.length; k++) {
/* 1483 */                 if (pkColumns[k] != null && pkColumns[k].equalsIgnoreCase(PKColName)) {
/* 1484 */                   usePkName = true;
/*      */                   break;
/*      */                 } 
/*      */               } 
/* 1488 */               String pkName = (usePkName && pkFinder.getName() != null) ? pkFinder.getName() : "";
/*      */               
/* 1490 */               exportedKeysQuery
/* 1491 */                 .append((count > 0) ? " union all select " : "select ")
/* 1492 */                 .append(Integer.toString(keySeq)).append(" as ks, '")
/* 1493 */                 .append(escape(tbl)).append("' as fkt, '")
/* 1494 */                 .append(escape(FKColName)).append("' as fcn, '")
/* 1495 */                 .append(escape(PKColName)).append("' as pcn, '")
/* 1496 */                 .append(escape(pkName)).append("' as pkn, ")
/* 1497 */                 .append(RULE_MAP.get(foreignKey.getOnUpdate())).append(" as ur, ")
/* 1498 */                 .append(RULE_MAP.get(foreignKey.getOnDelete())).append(" as dr, ");
/*      */               
/* 1500 */               String fkName = foreignKey.getFkName();
/*      */               
/* 1502 */               if (fkName != null) {
/* 1503 */                 exportedKeysQuery.append("'").append(escape(fkName)).append("' as fkn");
/*      */               } else {
/*      */                 
/* 1506 */                 exportedKeysQuery.append("'' as fkn");
/*      */               } 
/*      */               
/* 1509 */               count++;
/*      */             } 
/*      */           } 
/*      */         } finally {
/*      */           
/*      */           try {
/* 1515 */             if (rs != null) rs.close(); 
/* 1516 */           } catch (SQLException sQLException) {}
/*      */         } 
/*      */       } 
/*      */     } 
/*      */     
/* 1521 */     boolean hasImportedKey = (count > 0);
/* 1522 */     StringBuilder sql = new StringBuilder(512);
/* 1523 */     sql.append("select ")
/* 1524 */       .append(catalog).append(" as PKTABLE_CAT, ")
/* 1525 */       .append(schema).append(" as PKTABLE_SCHEM, ")
/* 1526 */       .append(quote(target)).append(" as PKTABLE_NAME, ")
/* 1527 */       .append(hasImportedKey ? "pcn" : "''").append(" as PKCOLUMN_NAME, ")
/* 1528 */       .append(catalog).append(" as FKTABLE_CAT, ")
/* 1529 */       .append(schema).append(" as FKTABLE_SCHEM, ")
/* 1530 */       .append(hasImportedKey ? "fkt" : "''").append(" as FKTABLE_NAME, ")
/* 1531 */       .append(hasImportedKey ? "fcn" : "''").append(" as FKCOLUMN_NAME, ")
/* 1532 */       .append(hasImportedKey ? "ks" : "-1").append(" as KEY_SEQ, ")
/* 1533 */       .append(hasImportedKey ? "ur" : "3").append(" as UPDATE_RULE, ")
/* 1534 */       .append(hasImportedKey ? "dr" : "3").append(" as DELETE_RULE, ")
/* 1535 */       .append(hasImportedKey ? "fkn" : "''").append(" as FK_NAME, ")
/* 1536 */       .append(hasImportedKey ? "pkn" : "''").append(" as PK_NAME, ")
/* 1537 */       .append(Integer.toString(5))
/* 1538 */       .append(" as DEFERRABILITY ");
/*      */     
/* 1540 */     if (hasImportedKey) {
/* 1541 */       sql.append("from (").append(exportedKeysQuery).append(") ORDER BY FKTABLE_CAT, FKTABLE_SCHEM, FKTABLE_NAME, KEY_SEQ");
/*      */     } else {
/*      */       
/* 1544 */       sql.append("limit 0");
/*      */     } 
/*      */     
/* 1547 */     return ((CoreStatement)stat).executeQuery(sql.toString(), true);
/*      */   }
/*      */   
/*      */   private StringBuilder appendDummyForeignKeyList(StringBuilder sql) {
/* 1551 */     sql.append("select -1 as ks, '' as ptn, '' as fcn, '' as pcn, ")
/* 1552 */       .append(3).append(" as ur, ")
/* 1553 */       .append(3).append(" as dr, ")
/* 1554 */       .append(" '' as fkn, ")
/* 1555 */       .append(" '' as pkn ")
/* 1556 */       .append(") limit 0;");
/* 1557 */     return sql;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public ResultSet getImportedKeys(String catalog, String schema, String table) throws SQLException {
/* 1565 */     ResultSet rs = null;
/* 1566 */     Statement stat = this.conn.createStatement();
/* 1567 */     StringBuilder sql = new StringBuilder(700);
/*      */     
/* 1569 */     sql.append("select ").append(quote(catalog)).append(" as PKTABLE_CAT, ")
/* 1570 */       .append(quote(schema)).append(" as PKTABLE_SCHEM, ")
/* 1571 */       .append("ptn as PKTABLE_NAME, pcn as PKCOLUMN_NAME, ")
/* 1572 */       .append(quote(catalog)).append(" as FKTABLE_CAT, ")
/* 1573 */       .append(quote(schema)).append(" as FKTABLE_SCHEM, ")
/* 1574 */       .append(quote(table)).append(" as FKTABLE_NAME, ")
/* 1575 */       .append("fcn as FKCOLUMN_NAME, ks as KEY_SEQ, ur as UPDATE_RULE, dr as DELETE_RULE, fkn as FK_NAME, pkn as PK_NAME, ")
/* 1576 */       .append(Integer.toString(5)).append(" as DEFERRABILITY from (");
/*      */ 
/*      */     
/*      */     try {
/* 1580 */       rs = stat.executeQuery("pragma foreign_key_list('" + escape(table) + "');");
/*      */     }
/* 1582 */     catch (SQLException e) {
/* 1583 */       sql = appendDummyForeignKeyList(sql);
/* 1584 */       return ((CoreStatement)stat).executeQuery(sql.toString(), true);
/*      */     } 
/*      */     
/* 1587 */     ImportedKeyFinder impFkFinder = new ImportedKeyFinder(table);
/* 1588 */     List<ImportedKeyFinder.ForeignKey> fkNames = impFkFinder.getFkList();
/*      */     
/* 1590 */     int i = 0;
/* 1591 */     for (; rs.next(); i++) {
/* 1592 */       int keySeq = rs.getInt(2) + 1;
/* 1593 */       int keyId = rs.getInt(1);
/* 1594 */       String PKTabName = rs.getString(3);
/* 1595 */       String FKColName = rs.getString(4);
/* 1596 */       String PKColName = rs.getString(5);
/*      */       
/* 1598 */       PrimaryKeyFinder pkFinder = new PrimaryKeyFinder(PKTabName);
/* 1599 */       String pkName = pkFinder.getName();
/* 1600 */       if (PKColName == null) {
/* 1601 */         PKColName = pkFinder.getColumns()[0];
/*      */       }
/*      */       
/* 1604 */       String updateRule = rs.getString(6);
/* 1605 */       String deleteRule = rs.getString(7);
/*      */       
/* 1607 */       if (i > 0) {
/* 1608 */         sql.append(" union all ");
/*      */       }
/*      */       
/* 1611 */       String fkName = null;
/* 1612 */       if (fkNames.size() > keyId) fkName = ((ImportedKeyFinder.ForeignKey)fkNames.get(keyId)).getFkName();
/*      */       
/* 1614 */       sql.append("select ").append(keySeq).append(" as ks,")
/* 1615 */         .append("'").append(escape(PKTabName)).append("' as ptn, '")
/* 1616 */         .append(escape(FKColName)).append("' as fcn, '")
/* 1617 */         .append(escape(PKColName)).append("' as pcn,")
/* 1618 */         .append("case '").append(escape(updateRule)).append("'")
/* 1619 */         .append(" when 'NO ACTION' then ").append(3)
/* 1620 */         .append(" when 'CASCADE' then ").append(0)
/* 1621 */         .append(" when 'RESTRICT' then ").append(1)
/* 1622 */         .append(" when 'SET NULL' then ").append(2)
/* 1623 */         .append(" when 'SET DEFAULT' then ").append(4).append(" end as ur, ")
/* 1624 */         .append("case '").append(escape(deleteRule)).append("'")
/* 1625 */         .append(" when 'NO ACTION' then ").append(3)
/* 1626 */         .append(" when 'CASCADE' then ").append(0)
/* 1627 */         .append(" when 'RESTRICT' then ").append(1)
/* 1628 */         .append(" when 'SET NULL' then ").append(2)
/* 1629 */         .append(" when 'SET DEFAULT' then ").append(4).append(" end as dr, ")
/* 1630 */         .append((fkName == null) ? "''" : quote(fkName)).append(" as fkn, ")
/* 1631 */         .append((pkName == null) ? "''" : quote(pkName)).append(" as pkn");
/*      */     } 
/* 1633 */     rs.close();
/*      */     
/* 1635 */     if (i == 0) {
/* 1636 */       sql = appendDummyForeignKeyList(sql);
/*      */     }
/* 1638 */     sql.append(") ORDER BY PKTABLE_CAT, PKTABLE_SCHEM, PKTABLE_NAME, KEY_SEQ;");
/*      */     
/* 1640 */     return ((CoreStatement)stat).executeQuery(sql.toString(), true);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public ResultSet getIndexInfo(String c, String s, String table, boolean u, boolean approximate) throws SQLException {
/* 1648 */     ResultSet rs = null;
/* 1649 */     Statement stat = this.conn.createStatement();
/* 1650 */     StringBuilder sql = new StringBuilder(500);
/*      */ 
/*      */ 
/*      */     
/* 1654 */     sql.append("select null as TABLE_CAT, null as TABLE_SCHEM, '")
/* 1655 */       .append(escape(table)).append("' as TABLE_NAME, un as NON_UNIQUE, null as INDEX_QUALIFIER, n as INDEX_NAME, ")
/* 1656 */       .append(Integer.toString(3)).append(" as TYPE, op as ORDINAL_POSITION, ")
/* 1657 */       .append("cn as COLUMN_NAME, null as ASC_OR_DESC, 0 as CARDINALITY, 0 as PAGES, null as FILTER_CONDITION from (");
/*      */ 
/*      */     
/* 1660 */     rs = stat.executeQuery("pragma index_list('" + escape(table) + "');");
/*      */     
/* 1662 */     ArrayList<ArrayList<Object>> indexList = new ArrayList<>();
/* 1663 */     while (rs.next()) {
/* 1664 */       indexList.add(new ArrayList());
/* 1665 */       ((ArrayList<String>)indexList.get(indexList.size() - 1)).add(rs.getString(2));
/* 1666 */       ((ArrayList<Integer>)indexList.get(indexList.size() - 1)).add(Integer.valueOf(rs.getInt(3)));
/*      */     } 
/* 1668 */     rs.close();
/* 1669 */     if (indexList.size() == 0) {
/*      */       
/* 1671 */       sql.append("select null as un, null as n, null as op, null as cn) limit 0;");
/* 1672 */       return ((CoreStatement)stat).executeQuery(sql.toString(), true);
/*      */     } 
/*      */ 
/*      */     
/* 1676 */     int i = 0;
/* 1677 */     Iterator<ArrayList<Object>> indexIterator = indexList.iterator();
/*      */ 
/*      */     
/* 1680 */     ArrayList<String> unionAll = new ArrayList<>();
/*      */     
/* 1682 */     while (indexIterator.hasNext()) {
/* 1683 */       ArrayList<Object> currentIndex = indexIterator.next();
/* 1684 */       String indexName = currentIndex.get(0).toString();
/* 1685 */       rs = stat.executeQuery("pragma index_info('" + escape(indexName) + "');");
/*      */       
/* 1687 */       while (rs.next()) {
/*      */         
/* 1689 */         StringBuilder sqlRow = new StringBuilder();
/*      */         
/* 1691 */         String colName = rs.getString(3);
/* 1692 */         sqlRow.append("select ").append(Integer.toString(1 - ((Integer)currentIndex.get(1)).intValue())).append(" as un,'")
/* 1693 */           .append(escape(indexName)).append("' as n,")
/* 1694 */           .append(Integer.toString(rs.getInt(1) + 1)).append(" as op,");
/* 1695 */         if (colName == null) {
/* 1696 */           sqlRow.append("null");
/*      */         } else {
/*      */           
/* 1699 */           sqlRow.append("'").append(escape(colName)).append("'");
/*      */         } 
/* 1701 */         sqlRow.append(" as cn");
/*      */         
/* 1703 */         unionAll.add(sqlRow.toString());
/*      */       } 
/*      */       
/* 1706 */       rs.close();
/*      */     } 
/*      */     
/* 1709 */     String sqlBlock = StringUtils.join(unionAll, " union all ");
/*      */     
/* 1711 */     return ((CoreStatement)stat).executeQuery(sql.append(sqlBlock).append(");").toString(), true);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public ResultSet getProcedureColumns(String c, String s, String p, String colPat) throws SQLException {
/* 1720 */     if (this.getProcedureColumns == null) {
/* 1721 */       this.getProcedureColumns = this.conn.prepareStatement("select null as PROCEDURE_CAT, null as PROCEDURE_SCHEM, null as PROCEDURE_NAME, null as COLUMN_NAME, null as COLUMN_TYPE, null as DATA_TYPE, null as TYPE_NAME, null as PRECISION, null as LENGTH, null as SCALE, null as RADIX, null as NULLABLE, null as REMARKS limit 0;");
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1727 */     return this.getProcedureColumns.executeQuery();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public ResultSet getProcedures(String c, String s, String p) throws SQLException {
/* 1736 */     if (this.getProcedures == null) {
/* 1737 */       this.getProcedures = this.conn.prepareStatement("select null as PROCEDURE_CAT, null as PROCEDURE_SCHEM, null as PROCEDURE_NAME, null as UNDEF1, null as UNDEF2, null as UNDEF3, null as REMARKS, null as PROCEDURE_TYPE limit 0;");
/*      */     }
/*      */ 
/*      */     
/* 1741 */     return this.getProcedures.executeQuery();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public ResultSet getSuperTables(String c, String s, String t) throws SQLException {
/* 1749 */     if (this.getSuperTables == null) {
/* 1750 */       this.getSuperTables = this.conn.prepareStatement("select null as TABLE_CAT, null as TABLE_SCHEM, null as TABLE_NAME, null as SUPERTABLE_NAME limit 0;");
/*      */     }
/*      */     
/* 1753 */     return this.getSuperTables.executeQuery();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public ResultSet getSuperTypes(String c, String s, String t) throws SQLException {
/* 1761 */     if (this.getSuperTypes == null) {
/* 1762 */       this.getSuperTypes = this.conn.prepareStatement("select null as TYPE_CAT, null as TYPE_SCHEM, null as TYPE_NAME, null as SUPERTYPE_CAT, null as SUPERTYPE_SCHEM, null as SUPERTYPE_NAME limit 0;");
/*      */     }
/*      */ 
/*      */     
/* 1766 */     return this.getSuperTypes.executeQuery();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public ResultSet getTablePrivileges(String c, String s, String t) throws SQLException {
/* 1774 */     if (this.getTablePrivileges == null) {
/* 1775 */       this.getTablePrivileges = this.conn.prepareStatement("select  null as TABLE_CAT, null as TABLE_SCHEM, null as TABLE_NAME, null as GRANTOR, null GRANTEE,  null as PRIVILEGE, null as IS_GRANTABLE limit 0;");
/*      */     }
/*      */ 
/*      */     
/* 1779 */     return this.getTablePrivileges.executeQuery();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public synchronized ResultSet getTables(String c, String s, String tblNamePattern, String[] types) throws SQLException {
/* 1788 */     checkOpen();
/*      */     
/* 1790 */     tblNamePattern = (tblNamePattern == null || "".equals(tblNamePattern)) ? "%" : escape(tblNamePattern);
/*      */     
/* 1792 */     StringBuilder sql = new StringBuilder();
/* 1793 */     sql.append("SELECT").append("\n");
/* 1794 */     sql.append("  NULL AS TABLE_CAT,").append("\n");
/* 1795 */     sql.append("  NULL AS TABLE_SCHEM,").append("\n");
/* 1796 */     sql.append("  NAME AS TABLE_NAME,").append("\n");
/* 1797 */     sql.append("  TYPE AS TABLE_TYPE,").append("\n");
/* 1798 */     sql.append("  NULL AS REMARKS,").append("\n");
/* 1799 */     sql.append("  NULL AS TYPE_CAT,").append("\n");
/* 1800 */     sql.append("  NULL AS TYPE_SCHEM,").append("\n");
/* 1801 */     sql.append("  NULL AS TYPE_NAME,").append("\n");
/* 1802 */     sql.append("  NULL AS SELF_REFERENCING_COL_NAME,").append("\n");
/* 1803 */     sql.append("  NULL AS REF_GENERATION").append("\n");
/* 1804 */     sql.append("FROM").append("\n");
/* 1805 */     sql.append("  (").append("\n");
/* 1806 */     sql.append("    SELECT").append("\n");
/* 1807 */     sql.append("      NAME,").append("\n");
/* 1808 */     sql.append("      UPPER(TYPE) AS TYPE").append("\n");
/* 1809 */     sql.append("    FROM").append("\n");
/* 1810 */     sql.append("      sqlite_master").append("\n");
/* 1811 */     sql.append("    WHERE").append("\n");
/* 1812 */     sql.append("      NAME NOT LIKE 'sqlite\\_%' ESCAPE '\\'").append("\n");
/* 1813 */     sql.append("      AND UPPER(TYPE) IN ('TABLE', 'VIEW')").append("\n");
/* 1814 */     sql.append("    UNION ALL").append("\n");
/* 1815 */     sql.append("    SELECT").append("\n");
/* 1816 */     sql.append("      NAME,").append("\n");
/* 1817 */     sql.append("      'GLOBAL TEMPORARY' AS TYPE").append("\n");
/* 1818 */     sql.append("    FROM").append("\n");
/* 1819 */     sql.append("      sqlite_temp_master").append("\n");
/* 1820 */     sql.append("    UNION ALL").append("\n");
/* 1821 */     sql.append("    SELECT").append("\n");
/* 1822 */     sql.append("      NAME,").append("\n");
/* 1823 */     sql.append("      'SYSTEM TABLE' AS TYPE").append("\n");
/* 1824 */     sql.append("    FROM").append("\n");
/* 1825 */     sql.append("      sqlite_master").append("\n");
/* 1826 */     sql.append("    WHERE").append("\n");
/* 1827 */     sql.append("      NAME LIKE 'sqlite\\_%' ESCAPE '\\'").append("\n");
/* 1828 */     sql.append("  )").append("\n");
/* 1829 */     sql.append(" WHERE TABLE_NAME LIKE '").append(tblNamePattern).append("' AND TABLE_TYPE IN (");
/*      */     
/* 1831 */     if (types == null || types.length == 0) {
/* 1832 */       sql.append("'TABLE','VIEW'");
/*      */     } else {
/*      */       
/* 1835 */       sql.append("'").append(types[0].toUpperCase()).append("'");
/*      */       
/* 1837 */       for (int i = 1; i < types.length; i++) {
/* 1838 */         sql.append(",'").append(types[i].toUpperCase()).append("'");
/*      */       }
/*      */     } 
/*      */     
/* 1842 */     sql.append(") ORDER BY TABLE_TYPE, TABLE_NAME;");
/*      */     
/* 1844 */     return ((CoreStatement)this.conn.createStatement()).executeQuery(sql.toString(), true);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public ResultSet getTableTypes() throws SQLException {
/* 1851 */     checkOpen();
/*      */     
/* 1853 */     String sql = "SELECT 'TABLE' AS TABLE_TYPE UNION SELECT 'VIEW' AS TABLE_TYPE UNION SELECT 'SYSTEM TABLE' AS TABLE_TYPE UNION SELECT 'GLOBAL TEMPORARY' AS TABLE_TYPE;";
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1861 */     if (this.getTableTypes == null) {
/* 1862 */       this.getTableTypes = this.conn.prepareStatement(sql);
/*      */     }
/* 1864 */     this.getTableTypes.clearParameters();
/* 1865 */     return this.getTableTypes.executeQuery();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public ResultSet getTypeInfo() throws SQLException {
/* 1872 */     if (this.getTypeInfo == null) {
/* 1873 */       this.getTypeInfo = this.conn.prepareStatement("select tn as TYPE_NAME, dt as DATA_TYPE, 0 as PRECISION, null as LITERAL_PREFIX, null as LITERAL_SUFFIX, null as CREATE_PARAMS, 1 as NULLABLE, 1 as CASE_SENSITIVE, 3 as SEARCHABLE, 0 as UNSIGNED_ATTRIBUTE, 0 as FIXED_PREC_SCALE, 0 as AUTO_INCREMENT, null as LOCAL_TYPE_NAME, 0 as MINIMUM_SCALE, 0 as MAXIMUM_SCALE, 0 as SQL_DATA_TYPE, 0 as SQL_DATETIME_SUB, 10 as NUM_PREC_RADIX from (    select 'BLOB' as tn, 2004 as dt union    select 'NULL' as tn, 0 as dt union    select 'REAL' as tn, 7 as dt union    select 'TEXT' as tn, 12 as dt union    select 'INTEGER' as tn, 4 as dt) order by TYPE_NAME;");
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1906 */     this.getTypeInfo.clearParameters();
/* 1907 */     return this.getTypeInfo.executeQuery();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public ResultSet getUDTs(String c, String s, String t, int[] types) throws SQLException {
/* 1915 */     if (this.getUDTs == null) {
/* 1916 */       this.getUDTs = this.conn.prepareStatement("select  null as TYPE_CAT, null as TYPE_SCHEM, null as TYPE_NAME,  null as CLASS_NAME,  null as DATA_TYPE, null as REMARKS, null as BASE_TYPE limit 0;");
/*      */     }
/*      */ 
/*      */ 
/*      */     
/* 1921 */     this.getUDTs.clearParameters();
/* 1922 */     return this.getUDTs.executeQuery();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public ResultSet getVersionColumns(String c, String s, String t) throws SQLException {
/* 1930 */     if (this.getVersionColumns == null) {
/* 1931 */       this.getVersionColumns = this.conn.prepareStatement("select null as SCOPE, null as COLUMN_NAME, null as DATA_TYPE, null as TYPE_NAME, null as COLUMN_SIZE, null as BUFFER_LENGTH, null as DECIMAL_DIGITS, null as PSEUDO_COLUMN limit 0;");
/*      */     }
/*      */ 
/*      */     
/* 1935 */     return this.getVersionColumns.executeQuery();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public ResultSet getGeneratedKeys() throws SQLException {
/* 1943 */     if (this.getGeneratedKeys == null) {
/* 1944 */       this.getGeneratedKeys = this.conn.prepareStatement("select last_insert_rowid();");
/*      */     }
/*      */     
/* 1947 */     return this.getGeneratedKeys.executeQuery();
/*      */   }
/*      */ 
/*      */   
/*      */   public Struct createStruct(String t, Object[] attr) throws SQLException {
/* 1952 */     throw new SQLException("Not yet implemented by SQLite JDBC driver");
/*      */   }
/*      */ 
/*      */   
/*      */   public ResultSet getFunctionColumns(String a, String b, String c, String d) throws SQLException {
/* 1957 */     throw new SQLException("Not yet implemented by SQLite JDBC driver");
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/* 1966 */   protected static final Pattern PK_UNNAMED_PATTERN = Pattern.compile(".*PRIMARY\\s+KEY\\s*\\((.*?)\\).*", 34);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/* 1973 */   protected static final Pattern PK_NAMED_PATTERN = Pattern.compile(".*CONSTRAINT\\s*(.*?)\\s*PRIMARY\\s+KEY\\s*\\((.*?)\\).*", 34);
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   class PrimaryKeyFinder
/*      */   {
/*      */     String table;
/*      */ 
/*      */ 
/*      */     
/* 1984 */     String pkName = null;
/*      */ 
/*      */     
/* 1987 */     String[] pkColumns = null;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public PrimaryKeyFinder(String table) throws SQLException {
/* 1995 */       this.table = table;
/*      */       
/* 1997 */       if (table == null || table.trim().length() == 0) {
/* 1998 */         throw new SQLException("Invalid table name: '" + this.table + "'");
/*      */       }
/*      */       
/* 2001 */       Statement stat = null;
/* 2002 */       ResultSet rs = null;
/*      */       
/*      */       try {
/* 2005 */         stat = JDBC3DatabaseMetaData.this.conn.createStatement();
/*      */         
/* 2007 */         rs = stat.executeQuery("select sql from sqlite_master where lower(name) = lower('" + JDBC3DatabaseMetaData.this
/* 2008 */             .escape(table) + "') and type in ('table', 'view')");
/*      */         
/* 2010 */         if (!rs.next()) {
/* 2011 */           throw new SQLException("Table not found: '" + table + "'");
/*      */         }
/* 2013 */         Matcher matcher = JDBC3DatabaseMetaData.PK_NAMED_PATTERN.matcher(rs.getString(1));
/* 2014 */         if (matcher.find()) {
/* 2015 */           this.pkName = JDBC3DatabaseMetaData.this.unquoteIdentifier(JDBC3DatabaseMetaData.this.escape(matcher.group(1)));
/* 2016 */           this.pkColumns = matcher.group(2).split(",");
/*      */         } else {
/*      */           
/* 2019 */           matcher = JDBC3DatabaseMetaData.PK_UNNAMED_PATTERN.matcher(rs.getString(1));
/* 2020 */           if (matcher.find()) {
/* 2021 */             this.pkColumns = matcher.group(1).split(",");
/*      */           }
/*      */         } 
/*      */         
/* 2025 */         if (this.pkColumns == null) {
/* 2026 */           rs = stat.executeQuery("pragma table_info('" + JDBC3DatabaseMetaData.this.escape(table) + "');");
/* 2027 */           while (rs.next()) {
/* 2028 */             if (rs.getBoolean(6)) {
/* 2029 */               this.pkColumns = new String[] { rs.getString(2) };
/*      */             }
/*      */           } 
/*      */         } 
/* 2033 */         if (this.pkColumns != null) {
/* 2034 */           for (int i = 0; i < this.pkColumns.length; i++) {
/* 2035 */             this.pkColumns[i] = JDBC3DatabaseMetaData.this.unquoteIdentifier(this.pkColumns[i]);
/*      */           }
/*      */         }
/*      */       } finally {
/*      */         
/*      */         try {
/* 2041 */           if (rs != null) rs.close(); 
/* 2042 */         } catch (Exception exception) {}
/*      */         try {
/* 2044 */           if (stat != null) stat.close(); 
/* 2045 */         } catch (Exception exception) {}
/*      */       } 
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public String getName() {
/* 2053 */       return this.pkName;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public String[] getColumns() {
/* 2060 */       return this.pkColumns;
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   class ImportedKeyFinder
/*      */   {
/* 2070 */     private final Pattern FK_NAMED_PATTERN = Pattern.compile("CONSTRAINT\\s*([A-Za-z_][A-Za-z\\d_]*)?\\s*FOREIGN\\s+KEY\\s*\\((.*?)\\)", 34);
/*      */     
/*      */     private String fkTableName;
/*      */     
/* 2074 */     private List<ForeignKey> fkList = new ArrayList<>();
/*      */ 
/*      */     
/*      */     public ImportedKeyFinder(String table) throws SQLException {
/* 2078 */       if (table == null || table.trim().length() == 0) {
/* 2079 */         throw new SQLException("Invalid table name: '" + table + "'");
/*      */       }
/*      */       
/* 2082 */       this.fkTableName = table;
/*      */       
/* 2084 */       List<String> fkNames = getForeignKeyNames(this.fkTableName);
/*      */       
/* 2086 */       Statement stat = null;
/* 2087 */       ResultSet rs = null;
/*      */       
/*      */       try {
/* 2090 */         stat = JDBC3DatabaseMetaData.this.conn.createStatement();
/* 2091 */         rs = stat.executeQuery("pragma foreign_key_list('" + JDBC3DatabaseMetaData.this
/* 2092 */             .escape(this.fkTableName.toLowerCase()) + "')");
/*      */         
/* 2094 */         int prevFkId = -1;
/* 2095 */         int count = 0;
/* 2096 */         ForeignKey fk = null;
/* 2097 */         while (rs.next()) {
/* 2098 */           int fkId = rs.getInt(1);
/* 2099 */           int colSeq = rs.getInt(2);
/* 2100 */           String pkTableName = rs.getString(3);
/* 2101 */           String fkColName = rs.getString(4);
/* 2102 */           String pkColName = rs.getString(5);
/* 2103 */           String onUpdate = rs.getString(6);
/* 2104 */           String onDelete = rs.getString(7);
/* 2105 */           String match = rs.getString(8);
/*      */           
/* 2107 */           String fkName = null;
/* 2108 */           if (fkNames.size() > count) fkName = fkNames.get(count);
/*      */           
/* 2110 */           if (fkId != prevFkId) {
/* 2111 */             fk = new ForeignKey(fkName, pkTableName, this.fkTableName, onUpdate, onDelete, match);
/* 2112 */             this.fkList.add(fk);
/* 2113 */             prevFkId = fkId;
/* 2114 */             count++;
/*      */           } 
/* 2116 */           fk.addColumnMapping(fkColName, pkColName);
/*      */         } 
/*      */       } finally {
/*      */         
/*      */         try {
/* 2121 */           if (rs != null) rs.close(); 
/* 2122 */         } catch (Exception exception) {}
/*      */         try {
/* 2124 */           if (stat != null) stat.close(); 
/* 2125 */         } catch (Exception exception) {}
/*      */       } 
/*      */     }
/*      */     
/*      */     private List<String> getForeignKeyNames(String tbl) throws SQLException {
/* 2130 */       List<String> fkNames = new ArrayList<>();
/* 2131 */       if (tbl == null) {
/* 2132 */         return fkNames;
/*      */       }
/* 2134 */       Statement stat2 = null;
/* 2135 */       ResultSet rs = null;
/*      */       try {
/* 2137 */         stat2 = JDBC3DatabaseMetaData.this.conn.createStatement();
/*      */         
/* 2139 */         rs = stat2.executeQuery("select sql from sqlite_master where lower(name) = lower('" + JDBC3DatabaseMetaData.this
/* 2140 */             .escape(tbl) + "')");
/* 2141 */         if (rs.next()) {
/* 2142 */           Matcher matcher = this.FK_NAMED_PATTERN.matcher(rs.getString(1));
/*      */           
/* 2144 */           while (matcher.find()) {
/* 2145 */             fkNames.add(matcher.group(1));
/*      */           }
/*      */         } 
/*      */       } finally {
/*      */         try {
/* 2150 */           if (rs != null)
/* 2151 */             rs.close(); 
/* 2152 */         } catch (SQLException sQLException) {}
/*      */         
/*      */         try {
/* 2155 */           if (stat2 != null)
/* 2156 */             stat2.close(); 
/* 2157 */         } catch (SQLException sQLException) {}
/*      */       } 
/*      */       
/* 2160 */       Collections.reverse(fkNames);
/* 2161 */       return fkNames;
/*      */     }
/*      */     
/*      */     public String getFkTableName() {
/* 2165 */       return this.fkTableName;
/*      */     }
/*      */     
/*      */     public List<ForeignKey> getFkList() {
/* 2169 */       return this.fkList;
/*      */     }
/*      */     
/*      */     class ForeignKey
/*      */     {
/*      */       private String fkName;
/*      */       private String pkTableName;
/*      */       private String fkTableName;
/* 2177 */       private List<String> fkColNames = new ArrayList<>();
/* 2178 */       private List<String> pkColNames = new ArrayList<>();
/*      */       private String onUpdate;
/*      */       private String onDelete;
/*      */       private String match;
/*      */       
/*      */       ForeignKey(String fkName, String pkTableName, String fkTableName, String onUpdate, String onDelete, String match) {
/* 2184 */         this.fkName = fkName;
/* 2185 */         this.pkTableName = pkTableName;
/* 2186 */         this.fkTableName = fkTableName;
/* 2187 */         this.onUpdate = onUpdate;
/* 2188 */         this.onDelete = onDelete;
/* 2189 */         this.match = match;
/*      */       }
/*      */ 
/*      */       
/*      */       public String getFkName() {
/* 2194 */         return this.fkName;
/*      */       }
/*      */       
/*      */       void addColumnMapping(String fkColName, String pkColName) {
/* 2198 */         this.fkColNames.add(fkColName);
/* 2199 */         this.pkColNames.add(pkColName);
/*      */       }
/*      */       
/*      */       public String[] getColumnMapping(int colSeq) {
/* 2203 */         return new String[] { this.fkColNames.get(colSeq), this.pkColNames.get(colSeq) };
/*      */       }
/*      */       
/*      */       public int getColumnMappingCount() {
/* 2207 */         return this.fkColNames.size();
/*      */       }
/*      */       
/*      */       public String getPkTableName() {
/* 2211 */         return this.pkTableName;
/*      */       }
/*      */       
/*      */       public String getFkTableName() {
/* 2215 */         return this.fkTableName;
/*      */       }
/*      */       
/*      */       public String getOnUpdate() {
/* 2219 */         return this.onUpdate;
/*      */       }
/*      */       
/*      */       public String getOnDelete() {
/* 2223 */         return this.onDelete;
/*      */       }
/*      */       
/*      */       public String getMatch() {
/* 2227 */         return this.match;
/*      */       }
/*      */ 
/*      */       
/*      */       public String toString()
/*      */       {
/* 2233 */         return "ForeignKey [fkName=" + this.fkName + ", pkTableName=" + this.pkTableName + ", fkTableName=" + this.fkTableName + ", pkColNames=" + this.pkColNames + ", fkColNames=" + this.fkColNames + "]"; } } } class ForeignKey { private String fkName; private String pkTableName; private String fkTableName; private List<String> fkColNames = new ArrayList<>(); private List<String> pkColNames = new ArrayList<>(); private String onUpdate; private String onDelete; private String match; ForeignKey(String fkName, String pkTableName, String fkTableName, String onUpdate, String onDelete, String match) { this.fkName = fkName; this.pkTableName = pkTableName; this.fkTableName = fkTableName; this.onUpdate = onUpdate; this.onDelete = onDelete; this.match = match; } public String getFkName() { return this.fkName; } void addColumnMapping(String fkColName, String pkColName) { this.fkColNames.add(fkColName); this.pkColNames.add(pkColName); } public String[] getColumnMapping(int colSeq) { return new String[] { this.fkColNames.get(colSeq), this.pkColNames.get(colSeq) }; } public int getColumnMappingCount() { return this.fkColNames.size(); } public String getPkTableName() { return this.pkTableName; } public String toString() { return "ForeignKey [fkName=" + this.fkName + ", pkTableName=" + this.pkTableName + ", fkTableName=" + this.fkTableName + ", pkColNames=" + this.pkColNames + ", fkColNames=" + this.fkColNames + "]"; }
/*      */      public String getFkTableName() {
/*      */       return this.fkTableName;
/*      */     } public String getOnUpdate() {
/*      */       return this.onUpdate;
/*      */     } public String getOnDelete() {
/*      */       return this.onDelete;
/*      */     } public String getMatch() {
/*      */       return this.match;
/*      */     } }
/*      */   protected void finalize() throws Throwable {
/* 2244 */     close();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private String unquoteIdentifier(String name) {
/* 2253 */     if (name == null) return name; 
/* 2254 */     name = name.trim();
/* 2255 */     if (name.length() > 2 && ((name
/* 2256 */       .startsWith("`") && name.endsWith("`")) || (name
/* 2257 */       .startsWith("\"") && name.endsWith("\"")) || (name
/* 2258 */       .startsWith("[") && name.endsWith("]"))))
/*      */     {
/*      */       
/* 2261 */       name = name.substring(1, name.length() - 1);
/*      */     }
/* 2263 */     return name;
/*      */   }
/*      */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\sqlite\jdbc3\JDBC3DatabaseMetaData.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */