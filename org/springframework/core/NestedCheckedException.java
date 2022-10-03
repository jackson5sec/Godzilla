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
/*     */ public abstract class NestedCheckedException
/*     */   extends Exception
/*     */ {
/*     */   private static final long serialVersionUID = 7100714597678207546L;
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
/*     */   public NestedCheckedException(String msg) {
/*  56 */     super(msg);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public NestedCheckedException(@Nullable String msg, @Nullable Throwable cause) {
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
/*     */   @Nullable
/*     */   public Throwable getRootCause() {
/*  87 */     return NestedExceptionUtils.getRootCause(this);
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
/*  99 */     Throwable rootCause = getRootCause();
/* 100 */     return (rootCause != null) ? rootCause : this;
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
/* 111 */     if (exType == null) {
/* 112 */       return false;
/*     */     }
/* 114 */     if (exType.isInstance(this)) {
/* 115 */       return true;
/*     */     }
/* 117 */     Throwable cause = getCause();
/* 118 */     if (cause == this) {
/* 119 */       return false;
/*     */     }
/* 121 */     if (cause instanceof NestedCheckedException) {
/* 122 */       return ((NestedCheckedException)cause).contains(exType);
/*     */     }
/*     */     
/* 125 */     while (cause != null) {
/* 126 */       if (exType.isInstance(cause)) {
/* 127 */         return true;
/*     */       }
/* 129 */       if (cause.getCause() == cause) {
/*     */         break;
/*     */       }
/* 132 */       cause = cause.getCause();
/*     */     } 
/* 134 */     return false;
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\springframework\core\NestedCheckedException.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */