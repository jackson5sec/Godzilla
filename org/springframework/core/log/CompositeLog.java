/*     */ package org.springframework.core.log;
/*     */ 
/*     */ import java.util.List;
/*     */ import java.util.function.Predicate;
/*     */ import org.apache.commons.logging.Log;
/*     */ import org.apache.commons.logging.impl.NoOpLog;
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
/*     */ final class CompositeLog
/*     */   implements Log
/*     */ {
/*  35 */   private static final Log NO_OP_LOG = (Log)new NoOpLog();
/*     */ 
/*     */   
/*     */   private final Log fatalLogger;
/*     */ 
/*     */   
/*     */   private final Log errorLogger;
/*     */ 
/*     */   
/*     */   private final Log warnLogger;
/*     */ 
/*     */   
/*     */   private final Log infoLogger;
/*     */ 
/*     */   
/*     */   private final Log debugLogger;
/*     */ 
/*     */   
/*     */   private final Log traceLogger;
/*     */ 
/*     */   
/*     */   public CompositeLog(List<Log> loggers) {
/*  57 */     this.fatalLogger = initLogger(loggers, Log::isFatalEnabled);
/*  58 */     this.errorLogger = initLogger(loggers, Log::isErrorEnabled);
/*  59 */     this.warnLogger = initLogger(loggers, Log::isWarnEnabled);
/*  60 */     this.infoLogger = initLogger(loggers, Log::isInfoEnabled);
/*  61 */     this.debugLogger = initLogger(loggers, Log::isDebugEnabled);
/*  62 */     this.traceLogger = initLogger(loggers, Log::isTraceEnabled);
/*     */   }
/*     */   
/*     */   private static Log initLogger(List<Log> loggers, Predicate<Log> predicate) {
/*  66 */     for (Log logger : loggers) {
/*  67 */       if (predicate.test(logger)) {
/*  68 */         return logger;
/*     */       }
/*     */     } 
/*  71 */     return NO_OP_LOG;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isFatalEnabled() {
/*  77 */     return (this.fatalLogger != NO_OP_LOG);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isErrorEnabled() {
/*  82 */     return (this.errorLogger != NO_OP_LOG);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isWarnEnabled() {
/*  87 */     return (this.warnLogger != NO_OP_LOG);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isInfoEnabled() {
/*  92 */     return (this.infoLogger != NO_OP_LOG);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isDebugEnabled() {
/*  97 */     return (this.debugLogger != NO_OP_LOG);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isTraceEnabled() {
/* 102 */     return (this.traceLogger != NO_OP_LOG);
/*     */   }
/*     */ 
/*     */   
/*     */   public void fatal(Object message) {
/* 107 */     this.fatalLogger.fatal(message);
/*     */   }
/*     */ 
/*     */   
/*     */   public void fatal(Object message, Throwable ex) {
/* 112 */     this.fatalLogger.fatal(message, ex);
/*     */   }
/*     */ 
/*     */   
/*     */   public void error(Object message) {
/* 117 */     this.errorLogger.error(message);
/*     */   }
/*     */ 
/*     */   
/*     */   public void error(Object message, Throwable ex) {
/* 122 */     this.errorLogger.error(message, ex);
/*     */   }
/*     */ 
/*     */   
/*     */   public void warn(Object message) {
/* 127 */     this.warnLogger.warn(message);
/*     */   }
/*     */ 
/*     */   
/*     */   public void warn(Object message, Throwable ex) {
/* 132 */     this.warnLogger.warn(message, ex);
/*     */   }
/*     */ 
/*     */   
/*     */   public void info(Object message) {
/* 137 */     this.infoLogger.info(message);
/*     */   }
/*     */ 
/*     */   
/*     */   public void info(Object message, Throwable ex) {
/* 142 */     this.infoLogger.info(message, ex);
/*     */   }
/*     */ 
/*     */   
/*     */   public void debug(Object message) {
/* 147 */     this.debugLogger.debug(message);
/*     */   }
/*     */ 
/*     */   
/*     */   public void debug(Object message, Throwable ex) {
/* 152 */     this.debugLogger.debug(message, ex);
/*     */   }
/*     */ 
/*     */   
/*     */   public void trace(Object message) {
/* 157 */     this.traceLogger.trace(message);
/*     */   }
/*     */ 
/*     */   
/*     */   public void trace(Object message, Throwable ex) {
/* 162 */     this.traceLogger.trace(message, ex);
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\springframework\core\log\CompositeLog.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */