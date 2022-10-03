/*    */ package core.ui.config;
/*    */ 
/*    */ import java.util.HashMap;
/*    */ 
/*    */ public class DatabaseSql
/*    */ {
/*  7 */   public static HashMap<String, String> sqlMap = new HashMap<>();
/*    */   
/*    */   static {
/* 10 */     sqlMap.put("mysql-getAllDatabase", "SHOW DATABASES");
/* 11 */     sqlMap.put("mysql-getTableByDatabase", "SHOW TABLES FROM `{databaseName}`");
/* 12 */     sqlMap.put("mysql-getTableDataByDT", "SELECT * FROM `{databaseName}`.`{tableName}` LIMIT 0,10");
/* 13 */     sqlMap.put("mysql-getCountByDT", "SELECT COUNT(1) FROM `{databaseName}`.`{tableName}`");
/* 14 */     sqlMap.put("sqlserver-getAllDatabase", "SELECT name FROM  master..sysdatabases");
/* 15 */     sqlMap.put("sqlserver-getTableByDatabase", "SELECT name FROM [{databaseName}]..sysobjects  WHERE xtype='U'");
/* 16 */     sqlMap.put("sqlserver-getTableDataByDT", "SELECT TOP 10 * FROM [{databaseName}].dbo.[{tableName}]");
/* 17 */     sqlMap.put("sqlserver-getCountByDT", "SELECT COUNT(1) FROM [{databaseName}].dbo.[{tableName}]");
/* 18 */     sqlMap.put("oracle-getAllDatabase", "SELECT USERNAME FROM ALL_USERS ORDER BY 1");
/* 19 */     sqlMap.put("oracle-getTableByDatabase", "SELECT TABLE_NAME FROM (SELECT TABLE_NAME FROM ALL_TABLES WHERE OWNER='%s' ORDER BY 1)");
/* 20 */     sqlMap.put("oracle-getTableDataByDT", "SELECT * FROM \"{databaseName}\".\"{tableName}\" where rownum<=10");
/* 21 */     sqlMap.put("oracle-getCountByDT", "SELECT COUNT(1) FROM \"{databaseName}\".\"{tableName}\"");
/* 22 */     sqlMap.put("postgresql-getAllDatabase", "SELECT datname FROM pg_database where datistemplate='f'");
/* 23 */     sqlMap.put("postgresql-getTableByDatabase", "SELECT table_name FROM information_schema.tables WHERE table_type='BASE TABLE' AND table_schema NOT IN ('pg_catalog', 'information_schema')");
/* 24 */     sqlMap.put("postgresql-getTableDataByDT", "SELECT * FROM \"{tableName}\" limit 10");
/* 25 */     sqlMap.put("postgresql-getCountByDT", "SELECT COUNT(1) FROM \"{tableName}\"");
/* 26 */     sqlMap.put("sqlite-getAllDatabase", "SELECT 'main'");
/* 27 */     sqlMap.put("sqlite-getTableByDatabase", "select tbl_name from sqlite_master where type='table' order by tbl_name");
/* 28 */     sqlMap.put("sqlite-getTableDataByDT", "SELECT * FROM `{tableName}`");
/* 29 */     sqlMap.put("sqlite-getCountByDT", "SELECT COUNT(1) FROM `{tableName}`");
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\cor\\ui\config\DatabaseSql.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */