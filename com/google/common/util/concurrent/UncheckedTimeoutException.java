/*    */ package com.google.common.util.concurrent;
/*    */ 
/*    */ import com.google.common.annotations.GwtIncompatible;
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
/*    */ @GwtIncompatible
/*    */ public class UncheckedTimeoutException
/*    */   extends RuntimeException
/*    */ {
/*    */   private static final long serialVersionUID = 0L;
/*    */   
/*    */   public UncheckedTimeoutException() {}
/*    */   
/*    */   public UncheckedTimeoutException(String message) {
/* 31 */     super(message);
/*    */   }
/*    */   
/*    */   public UncheckedTimeoutException(Throwable cause) {
/* 35 */     super(cause);
/*    */   }
/*    */   
/*    */   public UncheckedTimeoutException(String message, Throwable cause) {
/* 39 */     super(message, cause);
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\google\commo\\util\concurrent\UncheckedTimeoutException.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */