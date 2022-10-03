/*    */ package org.apache.log4j.spi;
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
/*    */ public class DefaultRepositorySelector
/*    */   implements RepositorySelector
/*    */ {
/*    */   final LoggerRepository repository;
/*    */   
/*    */   public DefaultRepositorySelector(LoggerRepository repository) {
/* 29 */     this.repository = repository;
/*    */   }
/*    */ 
/*    */   
/*    */   public LoggerRepository getLoggerRepository() {
/* 34 */     return this.repository;
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\apache\log4j\spi\DefaultRepositorySelector.class
 * Java compiler version: 4 (48.0)
 * JD-Core Version:       1.1.3
 */