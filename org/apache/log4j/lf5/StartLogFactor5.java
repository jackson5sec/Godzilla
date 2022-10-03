/*    */ package org.apache.log4j.lf5;
/*    */ 
/*    */ import org.apache.log4j.lf5.viewer.LogBrokerMonitor;
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
/*    */ public class StartLogFactor5
/*    */ {
/*    */   public static final void main(String[] args) {
/* 57 */     LogBrokerMonitor monitor = new LogBrokerMonitor(LogLevel.getLog4JLevels());
/*    */ 
/*    */     
/* 60 */     monitor.setFrameSize(LF5Appender.getDefaultMonitorWidth(), LF5Appender.getDefaultMonitorHeight());
/*    */     
/* 62 */     monitor.setFontSize(12);
/* 63 */     monitor.show();
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\apache\log4j\lf5\StartLogFactor5.class
 * Java compiler version: 4 (48.0)
 * JD-Core Version:       1.1.3
 */