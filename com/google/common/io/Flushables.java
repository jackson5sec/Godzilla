/*    */ package com.google.common.io;
/*    */ 
/*    */ import com.google.common.annotations.Beta;
/*    */ import com.google.common.annotations.GwtIncompatible;
/*    */ import java.io.Flushable;
/*    */ import java.io.IOException;
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
/*    */ @Beta
/*    */ @GwtIncompatible
/*    */ public final class Flushables
/*    */ {
/* 33 */   private static final Logger logger = Logger.getLogger(Flushables.class.getName());
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
/*    */   public static void flush(Flushable flushable, boolean swallowIOException) throws IOException {
/*    */     try {
/* 52 */       flushable.flush();
/* 53 */     } catch (IOException e) {
/* 54 */       if (swallowIOException) {
/* 55 */         logger.log(Level.WARNING, "IOException thrown while flushing Flushable.", e);
/*    */       } else {
/* 57 */         throw e;
/*    */       } 
/*    */     } 
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static void flushQuietly(Flushable flushable) {
/*    */     try {
/* 70 */       flush(flushable, true);
/* 71 */     } catch (IOException e) {
/* 72 */       logger.log(Level.SEVERE, "IOException should not have been thrown.", e);
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\google\common\io\Flushables.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */