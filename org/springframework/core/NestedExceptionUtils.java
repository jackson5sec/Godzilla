/*    */ package org.springframework.core;
/*    */ 
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
/*    */ public abstract class NestedExceptionUtils
/*    */ {
/*    */   @Nullable
/*    */   public static String buildMessage(@Nullable String message, @Nullable Throwable cause) {
/* 45 */     if (cause == null) {
/* 46 */       return message;
/*    */     }
/* 48 */     StringBuilder sb = new StringBuilder(64);
/* 49 */     if (message != null) {
/* 50 */       sb.append(message).append("; ");
/*    */     }
/* 52 */     sb.append("nested exception is ").append(cause);
/* 53 */     return sb.toString();
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   @Nullable
/*    */   public static Throwable getRootCause(@Nullable Throwable original) {
/* 64 */     if (original == null) {
/* 65 */       return null;
/*    */     }
/* 67 */     Throwable rootCause = null;
/* 68 */     Throwable cause = original.getCause();
/* 69 */     while (cause != null && cause != rootCause) {
/* 70 */       rootCause = cause;
/* 71 */       cause = cause.getCause();
/*    */     } 
/* 73 */     return rootCause;
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
/*    */   public static Throwable getMostSpecificCause(Throwable original) {
/* 86 */     Throwable rootCause = getRootCause(original);
/* 87 */     return (rootCause != null) ? rootCause : original;
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\springframework\core\NestedExceptionUtils.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */