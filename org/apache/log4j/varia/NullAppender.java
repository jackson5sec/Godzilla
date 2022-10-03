/*    */ package org.apache.log4j.varia;
/*    */ 
/*    */ import org.apache.log4j.AppenderSkeleton;
/*    */ import org.apache.log4j.spi.LoggingEvent;
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
/*    */ public class NullAppender
/*    */   extends AppenderSkeleton
/*    */ {
/* 30 */   private static NullAppender instance = new NullAppender();
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void activateOptions() {}
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public NullAppender getInstance() {
/* 47 */     return instance;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static NullAppender getNullAppender() {
/* 55 */     return instance;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void close() {}
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void doAppend(LoggingEvent event) {}
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   protected void append(LoggingEvent event) {}
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public boolean requiresLayout() {
/* 77 */     return false;
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\apache\log4j\varia\NullAppender.class
 * Java compiler version: 4 (48.0)
 * JD-Core Version:       1.1.3
 */