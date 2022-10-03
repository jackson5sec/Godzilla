/*    */ package com.google.common.util.concurrent;
/*    */ 
/*    */ import com.google.common.annotations.GwtCompatible;
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
/*    */ @GwtCompatible
/*    */ public class UncheckedExecutionException
/*    */   extends RuntimeException
/*    */ {
/*    */   private static final long serialVersionUID = 0L;
/*    */   
/*    */   protected UncheckedExecutionException() {}
/*    */   
/*    */   protected UncheckedExecutionException(String message) {
/* 42 */     super(message);
/*    */   }
/*    */ 
/*    */   
/*    */   public UncheckedExecutionException(String message, Throwable cause) {
/* 47 */     super(message, cause);
/*    */   }
/*    */ 
/*    */   
/*    */   public UncheckedExecutionException(Throwable cause) {
/* 52 */     super(cause);
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\google\commo\\util\concurrent\UncheckedExecutionException.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */