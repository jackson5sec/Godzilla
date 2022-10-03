/*    */ package com.google.common.eventbus;
/*    */ 
/*    */ import com.google.common.annotations.Beta;
/*    */ import java.util.concurrent.Executor;
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
/*    */ @Beta
/*    */ public class AsyncEventBus
/*    */   extends EventBus
/*    */ {
/*    */   public AsyncEventBus(String identifier, Executor executor) {
/* 39 */     super(identifier, executor, Dispatcher.legacyAsync(), EventBus.LoggingHandler.INSTANCE);
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
/*    */   public AsyncEventBus(Executor executor, SubscriberExceptionHandler subscriberExceptionHandler) {
/* 52 */     super("default", executor, Dispatcher.legacyAsync(), subscriberExceptionHandler);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public AsyncEventBus(Executor executor) {
/* 62 */     super("default", executor, Dispatcher.legacyAsync(), EventBus.LoggingHandler.INSTANCE);
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\google\common\eventbus\AsyncEventBus.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */