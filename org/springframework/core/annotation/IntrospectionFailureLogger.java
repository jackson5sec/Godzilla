/*    */ package org.springframework.core.annotation;
/*    */ 
/*    */ import org.apache.commons.logging.Log;
/*    */ import org.apache.commons.logging.LogFactory;
/*    */ import org.springframework.lang.Nullable;
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
/*    */ enum IntrospectionFailureLogger
/*    */ {
/* 35 */   DEBUG
/*    */   {
/*    */     public boolean isEnabled() {
/* 38 */       return getLogger().isDebugEnabled();
/*    */     }
/*    */     
/*    */     public void log(String message) {
/* 42 */       getLogger().debug(message);
/*    */     }
/*    */   },
/*    */   
/* 46 */   INFO
/*    */   {
/*    */     public boolean isEnabled() {
/* 49 */       return getLogger().isInfoEnabled();
/*    */     }
/*    */     
/*    */     public void log(String message) {
/* 53 */       getLogger().info(message);
/*    */     }
/*    */   };
/*    */ 
/*    */   
/*    */   @Nullable
/*    */   private static Log logger;
/*    */ 
/*    */   
/*    */   void log(String message, @Nullable Object source, Exception ex) {
/* 63 */     String on = (source != null) ? (" on " + source) : "";
/* 64 */     log(message + on + ": " + ex);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   private static Log getLogger() {
/* 73 */     Log logger = IntrospectionFailureLogger.logger;
/* 74 */     if (logger == null) {
/* 75 */       logger = LogFactory.getLog(MergedAnnotation.class);
/* 76 */       IntrospectionFailureLogger.logger = logger;
/*    */     } 
/* 78 */     return logger;
/*    */   }
/*    */   
/*    */   abstract boolean isEnabled();
/*    */   
/*    */   abstract void log(String paramString);
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\springframework\core\annotation\IntrospectionFailureLogger.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */