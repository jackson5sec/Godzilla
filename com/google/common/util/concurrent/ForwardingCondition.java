/*    */ package com.google.common.util.concurrent;
/*    */ 
/*    */ import java.util.Date;
/*    */ import java.util.concurrent.TimeUnit;
/*    */ import java.util.concurrent.locks.Condition;
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
/*    */ abstract class ForwardingCondition
/*    */   implements Condition
/*    */ {
/*    */   abstract Condition delegate();
/*    */   
/*    */   public void await() throws InterruptedException {
/* 27 */     delegate().await();
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean await(long time, TimeUnit unit) throws InterruptedException {
/* 32 */     return delegate().await(time, unit);
/*    */   }
/*    */ 
/*    */   
/*    */   public void awaitUninterruptibly() {
/* 37 */     delegate().awaitUninterruptibly();
/*    */   }
/*    */ 
/*    */   
/*    */   public long awaitNanos(long nanosTimeout) throws InterruptedException {
/* 42 */     return delegate().awaitNanos(nanosTimeout);
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean awaitUntil(Date deadline) throws InterruptedException {
/* 47 */     return delegate().awaitUntil(deadline);
/*    */   }
/*    */ 
/*    */   
/*    */   public void signal() {
/* 52 */     delegate().signal();
/*    */   }
/*    */ 
/*    */   
/*    */   public void signalAll() {
/* 57 */     delegate().signalAll();
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\google\commo\\util\concurrent\ForwardingCondition.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */