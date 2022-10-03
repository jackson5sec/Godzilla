/*     */ package org.springframework.core;
/*     */ 
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
/*     */ public abstract class NestedRuntimeException
/*     */   extends RuntimeException
/*     */ {
/*     */   private static final long serialVersionUID = 5439915454935047936L;
/*     */   
/*     */   static {
/*  47 */     NestedExceptionUtils.class.getName();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public NestedRuntimeException(String msg) {
/*  56 */     super(msg);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public NestedRuntimeException(@Nullable String msg, @Nullable Throwable cause) {
/*  66 */     super(msg, cause);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public String getMessage() {
/*  77 */     return NestedExceptionUtils.buildMessage(super.getMessage(), getCause());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public Throwable getRootCause() {
/*  88 */     return NestedExceptionUtils.getRootCause(this);
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
/*     */   public Throwable getMostSpecificCause() {
/* 100 */     Throwable rootCause = getRootCause();
/* 101 */     return (rootCause != null) ? rootCause : this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean contains(@Nullable Class<?> exType) {
/* 112 */     if (exType == null) {
/* 113 */       return false;
/*     */     }
/* 115 */     if (exType.isInstance(this)) {
/* 116 */       return true;
/*     */     }
/* 118 */     Throwable cause = getCause();
/* 119 */     if (cause == this) {
/* 120 */       return false;
/*     */     }
/* 122 */     if (cause instanceof NestedRuntimeException) {
/* 123 */       return ((NestedRuntimeException)cause).contains(exType);
/*     */     }
/*     */     
/* 126 */     while (cause != null) {
/* 127 */       if (exType.isInstance(cause)) {
/* 128 */         return true;
/*     */       }
/* 130 */       if (cause.getCause() == cause) {
/*     */         break;
/*     */       }
/* 133 */       cause = cause.getCause();
/*     */     } 
/* 135 */     return false;
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\springframework\core\NestedRuntimeException.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */