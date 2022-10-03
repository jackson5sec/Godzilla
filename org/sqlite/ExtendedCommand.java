/*     */ package org.sqlite;
/*     */ 
/*     */ import java.sql.SQLException;
/*     */ import java.util.regex.Matcher;
/*     */ import java.util.regex.Pattern;
/*     */ import org.sqlite.core.DB;
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
/*     */ public class ExtendedCommand
/*     */ {
/*     */   public static SQLExtension parse(String sql) throws SQLException {
/*  40 */     if (sql == null)
/*  41 */       return null; 
/*  42 */     if (sql.length() > 5 && sql.substring(0, 6).toLowerCase().equals("backup"))
/*  43 */       return BackupCommand.parse(sql); 
/*  44 */     if (sql.length() > 6 && sql.substring(0, 7).toLowerCase().equals("restore")) {
/*  45 */       return RestoreCommand.parse(sql);
/*     */     }
/*  47 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static String removeQuotation(String s) {
/*  56 */     if (s == null) {
/*  57 */       return s;
/*     */     }
/*  59 */     if ((s.startsWith("\"") && s.endsWith("\"")) || (s.startsWith("'") && s.endsWith("'"))) {
/*  60 */       return s.substring(1, s.length() - 1);
/*     */     }
/*  62 */     return s;
/*     */   }
/*     */   
/*     */   public static interface SQLExtension
/*     */   {
/*     */     void execute(DB param1DB) throws SQLException;
/*     */   }
/*     */   
/*     */   public static class BackupCommand
/*     */     implements SQLExtension {
/*     */     public final String srcDB;
/*     */     public final String destFile;
/*     */     
/*     */     public BackupCommand(String srcDB, String destFile) {
/*  76 */       this.srcDB = srcDB;
/*  77 */       this.destFile = destFile;
/*     */     }
/*     */     
/*  80 */     private static Pattern backupCmd = Pattern.compile("backup(\\s+(\"[^\"]*\"|'[^']*'|\\S+))?\\s+to\\s+(\"[^\"]*\"|'[^']*'|\\S+)", 2);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public static BackupCommand parse(String sql) throws SQLException {
/*  90 */       if (sql != null) {
/*  91 */         Matcher m = backupCmd.matcher(sql);
/*  92 */         if (m.matches()) {
/*  93 */           String dbName = ExtendedCommand.removeQuotation(m.group(2));
/*  94 */           String dest = ExtendedCommand.removeQuotation(m.group(3));
/*  95 */           if (dbName == null || dbName.length() == 0) {
/*  96 */             dbName = "main";
/*     */           }
/*  98 */           return new BackupCommand(dbName, dest);
/*     */         } 
/*     */       } 
/* 101 */       throw new SQLException("syntax error: " + sql);
/*     */     }
/*     */     
/*     */     public void execute(DB db) throws SQLException {
/* 105 */       db.backup(this.srcDB, this.destFile, null);
/*     */     }
/*     */   }
/*     */   
/*     */   public static class RestoreCommand
/*     */     implements SQLExtension
/*     */   {
/*     */     public final String targetDB;
/*     */     public final String srcFile;
/* 114 */     private static Pattern restoreCmd = Pattern.compile("restore(\\s+(\"[^\"]*\"|'[^']*'|\\S+))?\\s+from\\s+(\"[^\"]*\"|'[^']*'|\\S+)", 2);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public RestoreCommand(String targetDB, String srcFile) {
/* 123 */       this.targetDB = targetDB;
/* 124 */       this.srcFile = srcFile;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public static RestoreCommand parse(String sql) throws SQLException {
/* 134 */       if (sql != null) {
/* 135 */         Matcher m = restoreCmd.matcher(sql);
/* 136 */         if (m.matches()) {
/* 137 */           String dbName = ExtendedCommand.removeQuotation(m.group(2));
/* 138 */           String dest = ExtendedCommand.removeQuotation(m.group(3));
/* 139 */           if (dbName == null || dbName.length() == 0)
/* 140 */             dbName = "main"; 
/* 141 */           return new RestoreCommand(dbName, dest);
/*     */         } 
/*     */       } 
/* 144 */       throw new SQLException("syntax error: " + sql);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public void execute(DB db) throws SQLException {
/* 151 */       db.restore(this.targetDB, this.srcFile, null);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\sqlite\ExtendedCommand.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */