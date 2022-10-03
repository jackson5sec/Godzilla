/*    */ package org.apache.log4j.lf5;
/*    */ 
/*    */ import org.apache.log4j.spi.ThrowableInformation;
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
/*    */ public class Log4JLogRecord
/*    */   extends LogRecord
/*    */ {
/*    */   public boolean isSevereLevel() {
/* 67 */     boolean isSevere = false;
/*    */     
/* 69 */     if (LogLevel.ERROR.equals(getLevel()) || LogLevel.FATAL.equals(getLevel()))
/*    */     {
/* 71 */       isSevere = true;
/*    */     }
/*    */     
/* 74 */     return isSevere;
/*    */   }
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
/*    */   public void setThrownStackTrace(ThrowableInformation throwableInfo) {
/* 88 */     String[] stackTraceArray = throwableInfo.getThrowableStrRep();
/*    */     
/* 90 */     StringBuffer stackTrace = new StringBuffer();
/*    */ 
/*    */     
/* 93 */     for (int i = 0; i < stackTraceArray.length; i++) {
/* 94 */       String nextLine = stackTraceArray[i] + "\n";
/* 95 */       stackTrace.append(nextLine);
/*    */     } 
/*    */     
/* 98 */     this._thrownStackTrace = stackTrace.toString();
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\apache\log4j\lf5\Log4JLogRecord.class
 * Java compiler version: 4 (48.0)
 * JD-Core Version:       1.1.3
 */