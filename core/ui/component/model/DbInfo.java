/*    */ package core.ui.component.model;
/*    */ 
/*    */ import core.Encoding;
/*    */ 
/*    */ public class DbInfo {
/*    */   private String dbType;
/*    */   private String dbHost;
/*    */   private int dbPort;
/*    */   private String dbUserName;
/*    */   private String dbPassword;
/*    */   private Encoding charset;
/*    */   
/*    */   public DbInfo(Encoding dbEncoding) {
/* 14 */     this.dbType = "";
/* 15 */     this.dbHost = "";
/* 16 */     this.dbPort = 0;
/* 17 */     this.dbUserName = "";
/* 18 */     this.dbPassword = "";
/* 19 */     this.charset = dbEncoding;
/*    */   }
/*    */   
/*    */   public String getDbType() {
/* 23 */     return this.dbType;
/*    */   }
/*    */   public String getDbHost() {
/* 26 */     return this.dbHost;
/*    */   }
/*    */   public int getDbPort() {
/* 29 */     return this.dbPort;
/*    */   }
/*    */   public String getDbUserName() {
/* 32 */     return this.dbUserName;
/*    */   }
/*    */   public String getDbPassword() {
/* 35 */     return this.dbPassword;
/*    */   }
/*    */   public void setDbType(String dbType) {
/* 38 */     this.dbType = dbType;
/*    */   }
/*    */   public void setDbHost(String dbHost) {
/* 41 */     this.dbHost = dbHost;
/*    */   }
/*    */   public void setDbPort(int dbPort) {
/* 44 */     this.dbPort = dbPort;
/*    */   }
/*    */   public void setDbUserName(String dbUserName) {
/* 47 */     this.dbUserName = dbUserName;
/*    */   }
/*    */   public void setDbPassword(String dbPassword) {
/* 50 */     this.dbPassword = dbPassword;
/*    */   }
/*    */   
/*    */   public Encoding getCharset() {
/* 54 */     return this.charset;
/*    */   }
/*    */   
/*    */   public void setCharset(String charsetString) {
/* 58 */     this.charset.setCharsetString(charsetString);
/*    */   }
/*    */ 
/*    */   
/*    */   public String toString() {
/* 63 */     return "DbInfo{dbType='" + this.dbType + '\'' + ", dbHost='" + this.dbHost + '\'' + ", dbPort=" + this.dbPort + ", dbUserName='" + this.dbUserName + '\'' + ", dbPassword='" + this.dbPassword + '\'' + '}';
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\cor\\ui\component\model\DbInfo.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */