/*    */ package com.google.common.eventbus;
/*    */ 
/*    */ import com.google.common.base.Preconditions;
/*    */ import java.lang.reflect.Method;
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
/*    */ public class SubscriberExceptionContext
/*    */ {
/*    */   private final EventBus eventBus;
/*    */   private final Object event;
/*    */   private final Object subscriber;
/*    */   private final Method subscriberMethod;
/*    */   
/*    */   SubscriberExceptionContext(EventBus eventBus, Object event, Object subscriber, Method subscriberMethod) {
/* 41 */     this.eventBus = (EventBus)Preconditions.checkNotNull(eventBus);
/* 42 */     this.event = Preconditions.checkNotNull(event);
/* 43 */     this.subscriber = Preconditions.checkNotNull(subscriber);
/* 44 */     this.subscriberMethod = (Method)Preconditions.checkNotNull(subscriberMethod);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public EventBus getEventBus() {
/* 52 */     return this.eventBus;
/*    */   }
/*    */ 
/*    */   
/*    */   public Object getEvent() {
/* 57 */     return this.event;
/*    */   }
/*    */ 
/*    */   
/*    */   public Object getSubscriber() {
/* 62 */     return this.subscriber;
/*    */   }
/*    */ 
/*    */   
/*    */   public Method getSubscriberMethod() {
/* 67 */     return this.subscriberMethod;
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\google\common\eventbus\SubscriberExceptionContext.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */