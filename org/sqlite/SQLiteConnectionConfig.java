/*     */ package org.sqlite;
/*     */ 
/*     */ import java.util.EnumMap;
/*     */ import java.util.Map;
/*     */ import java.util.Properties;
/*     */ import org.sqlite.date.FastDateFormat;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class SQLiteConnectionConfig
/*     */   implements Cloneable
/*     */ {
/*  17 */   private SQLiteConfig.DateClass dateClass = SQLiteConfig.DateClass.INTEGER;
/*  18 */   private SQLiteConfig.DatePrecision datePrecision = SQLiteConfig.DatePrecision.MILLISECONDS;
/*  19 */   private String dateStringFormat = "yyyy-MM-dd HH:mm:ss.SSS";
/*  20 */   private FastDateFormat dateFormat = FastDateFormat.getInstance(this.dateStringFormat);
/*     */   
/*  22 */   private int transactionIsolation = 8;
/*  23 */   private SQLiteConfig.TransactionMode transactionMode = SQLiteConfig.TransactionMode.DEFERRED;
/*     */   private boolean autoCommit = true;
/*     */   
/*     */   public static SQLiteConnectionConfig fromPragmaTable(Properties pragmaTable) {
/*  27 */     return new SQLiteConnectionConfig(
/*  28 */         SQLiteConfig.DateClass.getDateClass(pragmaTable.getProperty(SQLiteConfig.Pragma.DATE_CLASS.pragmaName, SQLiteConfig.DateClass.INTEGER.name())), 
/*  29 */         SQLiteConfig.DatePrecision.getPrecision(pragmaTable.getProperty(SQLiteConfig.Pragma.DATE_PRECISION.pragmaName, SQLiteConfig.DatePrecision.MILLISECONDS.name())), pragmaTable
/*  30 */         .getProperty(SQLiteConfig.Pragma.DATE_STRING_FORMAT.pragmaName, "yyyy-MM-dd HH:mm:ss.SSS"), 8, 
/*     */         
/*  32 */         SQLiteConfig.TransactionMode.getMode(pragmaTable
/*  33 */           .getProperty(SQLiteConfig.Pragma.TRANSACTION_MODE.pragmaName, SQLiteConfig.TransactionMode.DEFERRED.name())), true);
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
/*     */   public SQLiteConnectionConfig(SQLiteConfig.DateClass dateClass, SQLiteConfig.DatePrecision datePrecision, String dateStringFormat, int transactionIsolation, SQLiteConfig.TransactionMode transactionMode, boolean autoCommit) {
/*  46 */     setDateClass(dateClass);
/*  47 */     setDatePrecision(datePrecision);
/*  48 */     setDateStringFormat(dateStringFormat);
/*  49 */     setTransactionIsolation(transactionIsolation);
/*  50 */     setTransactionMode(transactionMode);
/*  51 */     setAutoCommit(autoCommit);
/*     */   }
/*     */   
/*     */   public SQLiteConnectionConfig copyConfig() {
/*  55 */     return new SQLiteConnectionConfig(this.dateClass, this.datePrecision, this.dateStringFormat, this.transactionIsolation, this.transactionMode, this.autoCommit);
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
/*     */   public long getDateMultiplier() {
/*  67 */     return (this.datePrecision == SQLiteConfig.DatePrecision.MILLISECONDS) ? 1L : 1000L;
/*     */   }
/*     */ 
/*     */   
/*     */   public SQLiteConfig.DateClass getDateClass() {
/*  72 */     return this.dateClass;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setDateClass(SQLiteConfig.DateClass dateClass) {
/*  77 */     this.dateClass = dateClass;
/*     */   }
/*     */ 
/*     */   
/*     */   public SQLiteConfig.DatePrecision getDatePrecision() {
/*  82 */     return this.datePrecision;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setDatePrecision(SQLiteConfig.DatePrecision datePrecision) {
/*  87 */     this.datePrecision = datePrecision;
/*     */   }
/*     */ 
/*     */   
/*     */   public String getDateStringFormat() {
/*  92 */     return this.dateStringFormat;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setDateStringFormat(String dateStringFormat) {
/*  97 */     this.dateStringFormat = dateStringFormat;
/*  98 */     this.dateFormat = FastDateFormat.getInstance(dateStringFormat);
/*     */   }
/*     */ 
/*     */   
/*     */   public FastDateFormat getDateFormat() {
/* 103 */     return this.dateFormat;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isAutoCommit() {
/* 108 */     return this.autoCommit;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setAutoCommit(boolean autoCommit) {
/* 113 */     this.autoCommit = autoCommit;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getTransactionIsolation() {
/* 118 */     return this.transactionIsolation;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setTransactionIsolation(int transactionIsolation) {
/* 123 */     this.transactionIsolation = transactionIsolation;
/*     */   }
/*     */ 
/*     */   
/*     */   public SQLiteConfig.TransactionMode getTransactionMode() {
/* 128 */     return this.transactionMode;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void setTransactionMode(SQLiteConfig.TransactionMode transactionMode) {
/* 134 */     if (transactionMode == SQLiteConfig.TransactionMode.DEFFERED) {
/* 135 */       transactionMode = SQLiteConfig.TransactionMode.DEFERRED;
/*     */     }
/* 137 */     this.transactionMode = transactionMode;
/*     */   }
/*     */ 
/*     */   
/* 141 */   private static final Map<SQLiteConfig.TransactionMode, String> beginCommandMap = new EnumMap<>(SQLiteConfig.TransactionMode.class);
/*     */ 
/*     */ 
/*     */   
/*     */   static {
/* 146 */     beginCommandMap.put(SQLiteConfig.TransactionMode.DEFERRED, "begin;");
/* 147 */     beginCommandMap.put(SQLiteConfig.TransactionMode.IMMEDIATE, "begin immediate;");
/* 148 */     beginCommandMap.put(SQLiteConfig.TransactionMode.EXCLUSIVE, "begin exclusive;");
/*     */   }
/*     */   
/*     */   String transactionPrefix() {
/* 152 */     return beginCommandMap.get(this.transactionMode);
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\sqlite\SQLiteConnectionConfig.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */