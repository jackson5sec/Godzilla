/*     */ package org.springframework.core.codec;
/*     */ 
/*     */ import java.util.Collections;
/*     */ import java.util.Map;
/*     */ import org.apache.commons.logging.Log;
/*     */ import org.springframework.core.io.buffer.DataBuffer;
/*     */ import org.springframework.core.io.buffer.DataBufferUtils;
/*     */ import org.springframework.lang.Nullable;
/*     */ import org.springframework.util.CollectionUtils;
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
/*     */ public abstract class Hints
/*     */ {
/*  41 */   public static final String LOG_PREFIX_HINT = Log.class.getName() + ".PREFIX";
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  48 */   public static final String SUPPRESS_LOGGING_HINT = Log.class.getName() + ".SUPPRESS_LOGGING";
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Map<String, Object> from(String hintName, Object value) {
/*  58 */     return Collections.singletonMap(hintName, value);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Map<String, Object> none() {
/*  66 */     return Collections.emptyMap();
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
/*     */   public static <T> T getRequiredHint(@Nullable Map<String, Object> hints, String hintName) {
/*  79 */     if (hints == null) {
/*  80 */       throw new IllegalArgumentException("No hints map for required hint '" + hintName + "'");
/*     */     }
/*  82 */     T hint = (T)hints.get(hintName);
/*  83 */     if (hint == null) {
/*  84 */       throw new IllegalArgumentException("Hints map must contain the hint '" + hintName + "'");
/*     */     }
/*  86 */     return hint;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static String getLogPrefix(@Nullable Map<String, Object> hints) {
/*  95 */     return (hints != null) ? (String)hints.getOrDefault(LOG_PREFIX_HINT, "") : "";
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static boolean isLoggingSuppressed(@Nullable Map<String, Object> hints) {
/* 104 */     return (hints != null && ((Boolean)hints.getOrDefault(SUPPRESS_LOGGING_HINT, Boolean.valueOf(false))).booleanValue());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Map<String, Object> merge(Map<String, Object> hints1, Map<String, Object> hints2) {
/* 115 */     if (hints1.isEmpty() && hints2.isEmpty()) {
/* 116 */       return Collections.emptyMap();
/*     */     }
/* 118 */     if (hints2.isEmpty()) {
/* 119 */       return hints1;
/*     */     }
/* 121 */     if (hints1.isEmpty()) {
/* 122 */       return hints2;
/*     */     }
/*     */     
/* 125 */     Map<String, Object> result = CollectionUtils.newHashMap(hints1.size() + hints2.size());
/* 126 */     result.putAll(hints1);
/* 127 */     result.putAll(hints2);
/* 128 */     return result;
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
/*     */   public static Map<String, Object> merge(Map<String, Object> hints, String hintName, Object hintValue) {
/* 142 */     if (hints.isEmpty()) {
/* 143 */       return Collections.singletonMap(hintName, hintValue);
/*     */     }
/*     */     
/* 146 */     Map<String, Object> result = CollectionUtils.newHashMap(hints.size() + 1);
/* 147 */     result.putAll(hints);
/* 148 */     result.put(hintName, hintValue);
/* 149 */     return result;
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
/*     */   public static void touchDataBuffer(DataBuffer buffer, @Nullable Map<String, Object> hints, Log logger) {
/* 163 */     if (logger.isDebugEnabled() && hints != null) {
/* 164 */       Object logPrefix = hints.get(LOG_PREFIX_HINT);
/* 165 */       if (logPrefix != null)
/* 166 */         DataBufferUtils.touch(buffer, logPrefix); 
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\springframework\core\codec\Hints.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */