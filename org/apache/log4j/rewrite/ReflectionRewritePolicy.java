/*    */ package org.apache.log4j.rewrite;
/*    */ 
/*    */ import java.beans.Introspector;
/*    */ import java.beans.PropertyDescriptor;
/*    */ import java.util.HashMap;
/*    */ import java.util.Map;
/*    */ import org.apache.log4j.Category;
/*    */ import org.apache.log4j.Logger;
/*    */ import org.apache.log4j.helpers.LogLog;
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
/*    */ public class ReflectionRewritePolicy
/*    */   implements RewritePolicy
/*    */ {
/*    */   public LoggingEvent rewrite(LoggingEvent source) {
/* 46 */     Object msg = source.getMessage();
/* 47 */     if (!(msg instanceof String)) {
/* 48 */       Object newMsg = msg;
/* 49 */       Map rewriteProps = new HashMap(source.getProperties());
/*    */       
/*    */       try {
/* 52 */         PropertyDescriptor[] props = Introspector.getBeanInfo(msg.getClass(), Object.class).getPropertyDescriptors();
/*    */         
/* 54 */         if (props.length > 0) {
/* 55 */           for (int i = 0; i < props.length; i++) {
/*    */             try {
/* 57 */               Object propertyValue = props[i].getReadMethod().invoke(msg, (Object[])null);
/*    */ 
/*    */               
/* 60 */               if ("message".equalsIgnoreCase(props[i].getName())) {
/* 61 */                 newMsg = propertyValue;
/*    */               } else {
/* 63 */                 rewriteProps.put(props[i].getName(), propertyValue);
/*    */               } 
/* 65 */             } catch (Exception e) {
/* 66 */               LogLog.warn("Unable to evaluate property " + props[i].getName(), e);
/*    */             } 
/*    */           } 
/*    */           
/* 70 */           return new LoggingEvent(source.getFQNOfLoggerClass(), (source.getLogger() != null) ? source.getLogger() : (Category)Logger.getLogger(source.getLoggerName()), source.getTimeStamp(), source.getLevel(), newMsg, source.getThreadName(), source.getThrowableInformation(), source.getNDC(), source.getLocationInformation(), rewriteProps);
/*    */ 
/*    */ 
/*    */ 
/*    */         
/*    */         }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */       
/*    */       }
/* 82 */       catch (Exception e) {
/* 83 */         LogLog.warn("Unable to get property descriptors", e);
/*    */       } 
/*    */     } 
/*    */     
/* 87 */     return source;
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\apache\log4j\rewrite\ReflectionRewritePolicy.class
 * Java compiler version: 4 (48.0)
 * JD-Core Version:       1.1.3
 */