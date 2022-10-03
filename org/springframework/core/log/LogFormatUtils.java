/*     */ package org.springframework.core.log;
/*     */ 
/*     */ import java.util.function.Function;
/*     */ import org.apache.commons.logging.Log;
/*     */ import org.springframework.lang.Nullable;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class LogFormatUtils
/*     */ {
/*     */   public static String formatValue(@Nullable Object value, boolean limitLength) {
/*  46 */     return formatValue(value, limitLength ? 100 : -1, limitLength);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static String formatValue(@Nullable Object value, int maxLength, boolean replaceNewlines) {
/*     */     String result;
/*  59 */     if (value == null) {
/*  60 */       return "";
/*     */     }
/*     */     
/*     */     try {
/*  64 */       result = value.toString();
/*     */     }
/*  66 */     catch (Throwable ex) {
/*  67 */       result = ex.toString();
/*     */     } 
/*  69 */     if (maxLength != -1) {
/*  70 */       result = (result.length() > maxLength) ? (result.substring(0, maxLength) + " (truncated)...") : result;
/*     */     }
/*  72 */     if (replaceNewlines) {
/*  73 */       result = result.replace("\n", "<LF>").replace("\r", "<CR>");
/*     */     }
/*  75 */     if (value instanceof CharSequence) {
/*  76 */       result = "\"" + result + "\"";
/*     */     }
/*  78 */     return result;
/*     */   }
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
/*     */   public static void traceDebug(Log logger, Function<Boolean, String> messageFactory) {
/* 100 */     if (logger.isDebugEnabled()) {
/* 101 */       boolean traceEnabled = logger.isTraceEnabled();
/* 102 */       String logMessage = messageFactory.apply(Boolean.valueOf(traceEnabled));
/* 103 */       if (traceEnabled) {
/* 104 */         logger.trace(logMessage);
/*     */       } else {
/*     */         
/* 107 */         logger.debug(logMessage);
/*     */       } 
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\springframework\core\log\LogFormatUtils.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */