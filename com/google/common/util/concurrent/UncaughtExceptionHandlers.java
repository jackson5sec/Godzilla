/*    */ package com.google.common.util.concurrent;
/*    */ 
/*    */ import com.google.common.annotations.GwtIncompatible;
/*    */ import com.google.common.annotations.VisibleForTesting;
/*    */ import java.util.Locale;
/*    */ import java.util.logging.Level;
/*    */ import java.util.logging.Logger;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ @GwtIncompatible
/*    */ public final class UncaughtExceptionHandlers
/*    */ {
/*    */   public static Thread.UncaughtExceptionHandler systemExit() {
/* 52 */     return new Exiter(Runtime.getRuntime());
/*    */   }
/*    */   
/*    */   @VisibleForTesting
/*    */   static final class Exiter implements Thread.UncaughtExceptionHandler {
/* 57 */     private static final Logger logger = Logger.getLogger(Exiter.class.getName());
/*    */     
/*    */     private final Runtime runtime;
/*    */     
/*    */     Exiter(Runtime runtime) {
/* 62 */       this.runtime = runtime;
/*    */     }
/*    */ 
/*    */ 
/*    */     
/*    */     public void uncaughtException(Thread t, Throwable e) {
/*    */       try {
/* 69 */         logger.log(Level.SEVERE, 
/* 70 */             String.format(Locale.ROOT, "Caught an exception in %s.  Shutting down.", new Object[] { t }), e);
/* 71 */       } catch (Throwable errorInLogging) {
/*    */ 
/*    */         
/* 74 */         System.err.println(e.getMessage());
/* 75 */         System.err.println(errorInLogging.getMessage());
/*    */       } finally {
/* 77 */         this.runtime.exit(1);
/*    */       } 
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\google\commo\\util\concurrent\UncaughtExceptionHandlers.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */