/*    */ package com.google.common.util.concurrent;
/*    */ 
/*    */ import com.google.common.annotations.GwtIncompatible;
/*    */ import java.util.concurrent.Callable;
/*    */ import java.util.concurrent.Executor;
/*    */ import java.util.concurrent.FutureTask;
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
/*    */ public class ListenableFutureTask<V>
/*    */   extends FutureTask<V>
/*    */   implements ListenableFuture<V>
/*    */ {
/* 43 */   private final ExecutionList executionList = new ExecutionList();
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static <V> ListenableFutureTask<V> create(Callable<V> callable) {
/* 53 */     return new ListenableFutureTask<>(callable);
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
/*    */   
/*    */   public static <V> ListenableFutureTask<V> create(Runnable runnable, V result) {
/* 67 */     return new ListenableFutureTask<>(runnable, result);
/*    */   }
/*    */   
/*    */   ListenableFutureTask(Callable<V> callable) {
/* 71 */     super(callable);
/*    */   }
/*    */   
/*    */   ListenableFutureTask(Runnable runnable, V result) {
/* 75 */     super(runnable, result);
/*    */   }
/*    */ 
/*    */   
/*    */   public void addListener(Runnable listener, Executor exec) {
/* 80 */     this.executionList.add(listener, exec);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   protected void done() {
/* 86 */     this.executionList.execute();
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\google\commo\\util\concurrent\ListenableFutureTask.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */