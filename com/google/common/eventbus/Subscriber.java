/*     */ package com.google.common.eventbus;
/*     */ 
/*     */ import com.google.common.annotations.VisibleForTesting;
/*     */ import com.google.common.base.Preconditions;
/*     */ import com.google.j2objc.annotations.Weak;
/*     */ import java.lang.reflect.InvocationTargetException;
/*     */ import java.lang.reflect.Method;
/*     */ import java.util.concurrent.Executor;
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
/*     */ class Subscriber
/*     */ {
/*     */   @Weak
/*     */   private EventBus bus;
/*     */   @VisibleForTesting
/*     */   final Object target;
/*     */   private final Method method;
/*     */   private final Executor executor;
/*     */   
/*     */   static Subscriber create(EventBus bus, Object listener, Method method) {
/*  39 */     return isDeclaredThreadSafe(method) ? new Subscriber(bus, listener, method) : new SynchronizedSubscriber(bus, listener, method);
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private Subscriber(EventBus bus, Object target, Method method) {
/*  57 */     this.bus = bus;
/*  58 */     this.target = Preconditions.checkNotNull(target);
/*  59 */     this.method = method;
/*  60 */     method.setAccessible(true);
/*     */     
/*  62 */     this.executor = bus.executor();
/*     */   }
/*     */ 
/*     */   
/*     */   final void dispatchEvent(final Object event) {
/*  67 */     this.executor.execute(new Runnable()
/*     */         {
/*     */           public void run()
/*     */           {
/*     */             try {
/*  72 */               Subscriber.this.invokeSubscriberMethod(event);
/*  73 */             } catch (InvocationTargetException e) {
/*  74 */               Subscriber.this.bus.handleSubscriberException(e.getCause(), Subscriber.this.context(event));
/*     */             } 
/*     */           }
/*     */         });
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @VisibleForTesting
/*     */   void invokeSubscriberMethod(Object event) throws InvocationTargetException {
/*     */     try {
/*  87 */       this.method.invoke(this.target, new Object[] { Preconditions.checkNotNull(event) });
/*  88 */     } catch (IllegalArgumentException e) {
/*  89 */       throw new Error("Method rejected target/argument: " + event, e);
/*  90 */     } catch (IllegalAccessException e) {
/*  91 */       throw new Error("Method became inaccessible: " + event, e);
/*  92 */     } catch (InvocationTargetException e) {
/*  93 */       if (e.getCause() instanceof Error) {
/*  94 */         throw (Error)e.getCause();
/*     */       }
/*  96 */       throw e;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private SubscriberExceptionContext context(Object event) {
/* 102 */     return new SubscriberExceptionContext(this.bus, event, this.target, this.method);
/*     */   }
/*     */ 
/*     */   
/*     */   public final int hashCode() {
/* 107 */     return (31 + this.method.hashCode()) * 31 + System.identityHashCode(this.target);
/*     */   }
/*     */ 
/*     */   
/*     */   public final boolean equals(Object obj) {
/* 112 */     if (obj instanceof Subscriber) {
/* 113 */       Subscriber that = (Subscriber)obj;
/*     */ 
/*     */ 
/*     */       
/* 117 */       return (this.target == that.target && this.method.equals(that.method));
/*     */     } 
/* 119 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static boolean isDeclaredThreadSafe(Method method) {
/* 127 */     return (method.getAnnotation(AllowConcurrentEvents.class) != null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @VisibleForTesting
/*     */   static final class SynchronizedSubscriber
/*     */     extends Subscriber
/*     */   {
/*     */     private SynchronizedSubscriber(EventBus bus, Object target, Method method) {
/* 138 */       super(bus, target, method);
/*     */     }
/*     */ 
/*     */     
/*     */     void invokeSubscriberMethod(Object event) throws InvocationTargetException {
/* 143 */       synchronized (this) {
/* 144 */         super.invokeSubscriberMethod(event);
/*     */       } 
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\google\common\eventbus\Subscriber.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */