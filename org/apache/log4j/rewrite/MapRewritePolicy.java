/*    */ package org.apache.log4j.rewrite;
/*    */ 
/*    */ import java.util.HashMap;
/*    */ import java.util.Iterator;
/*    */ import java.util.Map;
/*    */ import org.apache.log4j.Category;
/*    */ import org.apache.log4j.Logger;
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
/*    */ public class MapRewritePolicy
/*    */   implements RewritePolicy
/*    */ {
/*    */   public LoggingEvent rewrite(LoggingEvent source) {
/* 46 */     Object msg = source.getMessage();
/* 47 */     if (msg instanceof Map) {
/* 48 */       Map props = new HashMap(source.getProperties());
/* 49 */       Map eventProps = (Map)msg;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */       
/* 55 */       Object newMsg = eventProps.get("message");
/* 56 */       if (newMsg == null) {
/* 57 */         newMsg = msg;
/*    */       }
/*    */       
/* 60 */       Iterator iter = eventProps.entrySet().iterator();
/* 61 */       while (iter.hasNext()) {
/*    */         
/* 63 */         Map.Entry entry = iter.next();
/* 64 */         if (!"message".equals(entry.getKey())) {
/* 65 */           props.put(entry.getKey(), entry.getValue());
/*    */         }
/*    */       } 
/*    */       
/* 69 */       return new LoggingEvent(source.getFQNOfLoggerClass(), (source.getLogger() != null) ? source.getLogger() : (Category)Logger.getLogger(source.getLoggerName()), source.getTimeStamp(), source.getLevel(), newMsg, source.getThreadName(), source.getThrowableInformation(), source.getNDC(), source.getLocationInformation(), props);
/*    */     } 
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
/* 81 */     return source;
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\apache\log4j\rewrite\MapRewritePolicy.class
 * Java compiler version: 4 (48.0)
 * JD-Core Version:       1.1.3
 */