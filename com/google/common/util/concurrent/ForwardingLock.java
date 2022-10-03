/*    */ package com.google.common.util.concurrent;
/*    */ 
/*    */ import java.util.concurrent.TimeUnit;
/*    */ import java.util.concurrent.locks.Condition;
/*    */ import java.util.concurrent.locks.Lock;
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
/*    */ abstract class ForwardingLock
/*    */   implements Lock
/*    */ {
/*    */   abstract Lock delegate();
/*    */   
/*    */   public void lock() {
/* 27 */     delegate().lock();
/*    */   }
/*    */ 
/*    */   
/*    */   public void lockInterruptibly() throws InterruptedException {
/* 32 */     delegate().lockInterruptibly();
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean tryLock() {
/* 37 */     return delegate().tryLock();
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean tryLock(long time, TimeUnit unit) throws InterruptedException {
/* 42 */     return delegate().tryLock(time, unit);
/*    */   }
/*    */ 
/*    */   
/*    */   public void unlock() {
/* 47 */     delegate().unlock();
/*    */   }
/*    */ 
/*    */   
/*    */   public Condition newCondition() {
/* 52 */     return delegate().newCondition();
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\google\commo\\util\concurrent\ForwardingLock.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */