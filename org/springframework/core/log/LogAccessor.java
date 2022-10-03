/*     */ package org.springframework.core.log;
/*     */ 
/*     */ import java.util.function.Supplier;
/*     */ import org.apache.commons.logging.Log;
/*     */ import org.apache.commons.logging.LogFactory;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class LogAccessor
/*     */ {
/*     */   private final Log log;
/*     */   
/*     */   public LogAccessor(Log log) {
/*  43 */     this.log = log;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public LogAccessor(Class<?> logCategory) {
/*  51 */     this.log = LogFactory.getLog(logCategory);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public LogAccessor(String logCategory) {
/*  59 */     this.log = LogFactory.getLog(logCategory);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final Log getLog() {
/*  67 */     return this.log;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isFatalEnabled() {
/*  77 */     return this.log.isFatalEnabled();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isErrorEnabled() {
/*  84 */     return this.log.isErrorEnabled();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isWarnEnabled() {
/*  91 */     return this.log.isWarnEnabled();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isInfoEnabled() {
/*  98 */     return this.log.isInfoEnabled();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isDebugEnabled() {
/* 105 */     return this.log.isDebugEnabled();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isTraceEnabled() {
/* 112 */     return this.log.isTraceEnabled();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void fatal(CharSequence message) {
/* 123 */     this.log.fatal(message);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void fatal(Throwable cause, CharSequence message) {
/* 132 */     this.log.fatal(message, cause);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void error(CharSequence message) {
/* 140 */     this.log.error(message);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void error(Throwable cause, CharSequence message) {
/* 149 */     this.log.error(message, cause);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void warn(CharSequence message) {
/* 157 */     this.log.warn(message);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void warn(Throwable cause, CharSequence message) {
/* 166 */     this.log.warn(message, cause);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void info(CharSequence message) {
/* 174 */     this.log.info(message);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void info(Throwable cause, CharSequence message) {
/* 183 */     this.log.info(message, cause);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void debug(CharSequence message) {
/* 191 */     this.log.debug(message);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void debug(Throwable cause, CharSequence message) {
/* 200 */     this.log.debug(message, cause);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void trace(CharSequence message) {
/* 208 */     this.log.trace(message);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void trace(Throwable cause, CharSequence message) {
/* 217 */     this.log.trace(message, cause);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void fatal(Supplier<? extends CharSequence> messageSupplier) {
/* 228 */     if (this.log.isFatalEnabled()) {
/* 229 */       this.log.fatal(LogMessage.of(messageSupplier));
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void fatal(Throwable cause, Supplier<? extends CharSequence> messageSupplier) {
/* 239 */     if (this.log.isFatalEnabled()) {
/* 240 */       this.log.fatal(LogMessage.of(messageSupplier), cause);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void error(Supplier<? extends CharSequence> messageSupplier) {
/* 249 */     if (this.log.isErrorEnabled()) {
/* 250 */       this.log.error(LogMessage.of(messageSupplier));
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void error(Throwable cause, Supplier<? extends CharSequence> messageSupplier) {
/* 260 */     if (this.log.isErrorEnabled()) {
/* 261 */       this.log.error(LogMessage.of(messageSupplier), cause);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void warn(Supplier<? extends CharSequence> messageSupplier) {
/* 270 */     if (this.log.isWarnEnabled()) {
/* 271 */       this.log.warn(LogMessage.of(messageSupplier));
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void warn(Throwable cause, Supplier<? extends CharSequence> messageSupplier) {
/* 281 */     if (this.log.isWarnEnabled()) {
/* 282 */       this.log.warn(LogMessage.of(messageSupplier), cause);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void info(Supplier<? extends CharSequence> messageSupplier) {
/* 291 */     if (this.log.isInfoEnabled()) {
/* 292 */       this.log.info(LogMessage.of(messageSupplier));
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void info(Throwable cause, Supplier<? extends CharSequence> messageSupplier) {
/* 302 */     if (this.log.isInfoEnabled()) {
/* 303 */       this.log.info(LogMessage.of(messageSupplier), cause);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void debug(Supplier<? extends CharSequence> messageSupplier) {
/* 312 */     if (this.log.isDebugEnabled()) {
/* 313 */       this.log.debug(LogMessage.of(messageSupplier));
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void debug(Throwable cause, Supplier<? extends CharSequence> messageSupplier) {
/* 323 */     if (this.log.isDebugEnabled()) {
/* 324 */       this.log.debug(LogMessage.of(messageSupplier), cause);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void trace(Supplier<? extends CharSequence> messageSupplier) {
/* 333 */     if (this.log.isTraceEnabled()) {
/* 334 */       this.log.trace(LogMessage.of(messageSupplier));
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void trace(Throwable cause, Supplier<? extends CharSequence> messageSupplier) {
/* 344 */     if (this.log.isTraceEnabled())
/* 345 */       this.log.trace(LogMessage.of(messageSupplier), cause); 
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\springframework\core\log\LogAccessor.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */