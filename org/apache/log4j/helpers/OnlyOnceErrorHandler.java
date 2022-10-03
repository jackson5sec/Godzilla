/*    */ package org.apache.log4j.helpers;
/*    */ 
/*    */ import org.apache.log4j.Appender;
/*    */ import org.apache.log4j.Logger;
/*    */ import org.apache.log4j.spi.ErrorHandler;
/*    */ import org.apache.log4j.spi.LoggingEvent;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class OnlyOnceErrorHandler
/*    */   implements ErrorHandler
/*    */ {
/* 43 */   final String WARN_PREFIX = "log4j warning: ";
/* 44 */   final String ERROR_PREFIX = "log4j error: ";
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   boolean firstTime = true;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void setLogger(Logger logger) {}
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void activateOptions() {}
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void error(String message, Exception e, int errorCode) {
/* 70 */     error(message, e, errorCode, null);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void error(String message, Exception e, int errorCode, LoggingEvent event) {
/* 79 */     if (e instanceof java.io.InterruptedIOException || e instanceof InterruptedException) {
/* 80 */       Thread.currentThread().interrupt();
/*    */     }
/* 82 */     if (this.firstTime) {
/* 83 */       LogLog.error(message, e);
/* 84 */       this.firstTime = false;
/*    */     } 
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void error(String message) {
/* 95 */     if (this.firstTime) {
/* 96 */       LogLog.error(message);
/* 97 */       this.firstTime = false;
/*    */     } 
/*    */   }
/*    */   
/*    */   public void setAppender(Appender appender) {}
/*    */   
/*    */   public void setBackupAppender(Appender appender) {}
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\apache\log4j\helpers\OnlyOnceErrorHandler.class
 * Java compiler version: 4 (48.0)
 * JD-Core Version:       1.1.3
 */