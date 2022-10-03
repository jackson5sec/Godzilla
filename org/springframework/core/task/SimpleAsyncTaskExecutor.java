/*     */ package org.springframework.core.task;
/*     */ 
/*     */ import java.io.Serializable;
/*     */ import java.util.concurrent.Callable;
/*     */ import java.util.concurrent.Future;
/*     */ import java.util.concurrent.FutureTask;
/*     */ import java.util.concurrent.ThreadFactory;
/*     */ import org.springframework.lang.Nullable;
/*     */ import org.springframework.util.Assert;
/*     */ import org.springframework.util.ConcurrencyThrottleSupport;
/*     */ import org.springframework.util.CustomizableThreadCreator;
/*     */ import org.springframework.util.concurrent.ListenableFuture;
/*     */ import org.springframework.util.concurrent.ListenableFutureTask;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class SimpleAsyncTaskExecutor
/*     */   extends CustomizableThreadCreator
/*     */   implements AsyncListenableTaskExecutor, Serializable
/*     */ {
/*     */   public static final int UNBOUNDED_CONCURRENCY = -1;
/*     */   public static final int NO_CONCURRENCY = 0;
/*  68 */   private final ConcurrencyThrottleAdapter concurrencyThrottle = new ConcurrencyThrottleAdapter();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   private ThreadFactory threadFactory;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   private TaskDecorator taskDecorator;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public SimpleAsyncTaskExecutor(String threadNamePrefix) {
/*  89 */     super(threadNamePrefix);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public SimpleAsyncTaskExecutor(ThreadFactory threadFactory) {
/*  97 */     this.threadFactory = threadFactory;
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
/*     */   public void setThreadFactory(@Nullable ThreadFactory threadFactory) {
/* 110 */     this.threadFactory = threadFactory;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public final ThreadFactory getThreadFactory() {
/* 118 */     return this.threadFactory;
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
/*     */   
/*     */   public final void setTaskDecorator(TaskDecorator taskDecorator) {
/* 137 */     this.taskDecorator = taskDecorator;
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
/*     */   public void setConcurrencyLimit(int concurrencyLimit) {
/* 151 */     this.concurrencyThrottle.setConcurrencyLimit(concurrencyLimit);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final int getConcurrencyLimit() {
/* 158 */     return this.concurrencyThrottle.getConcurrencyLimit();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final boolean isThrottleActive() {
/* 168 */     return this.concurrencyThrottle.isThrottleActive();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void execute(Runnable task) {
/* 179 */     execute(task, Long.MAX_VALUE);
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
/*     */   public void execute(Runnable task, long startTimeout) {
/* 193 */     Assert.notNull(task, "Runnable must not be null");
/* 194 */     Runnable taskToUse = (this.taskDecorator != null) ? this.taskDecorator.decorate(task) : task;
/* 195 */     if (isThrottleActive() && startTimeout > 0L) {
/* 196 */       this.concurrencyThrottle.beforeAccess();
/* 197 */       doExecute(new ConcurrencyThrottlingRunnable(taskToUse));
/*     */     } else {
/*     */       
/* 200 */       doExecute(taskToUse);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public Future<?> submit(Runnable task) {
/* 206 */     FutureTask<Object> future = new FutureTask(task, null);
/* 207 */     execute(future, Long.MAX_VALUE);
/* 208 */     return future;
/*     */   }
/*     */ 
/*     */   
/*     */   public <T> Future<T> submit(Callable<T> task) {
/* 213 */     FutureTask<T> future = new FutureTask<>(task);
/* 214 */     execute(future, Long.MAX_VALUE);
/* 215 */     return future;
/*     */   }
/*     */ 
/*     */   
/*     */   public ListenableFuture<?> submitListenable(Runnable task) {
/* 220 */     ListenableFutureTask<Object> future = new ListenableFutureTask(task, null);
/* 221 */     execute((Runnable)future, Long.MAX_VALUE);
/* 222 */     return (ListenableFuture<?>)future;
/*     */   }
/*     */ 
/*     */   
/*     */   public <T> ListenableFuture<T> submitListenable(Callable<T> task) {
/* 227 */     ListenableFutureTask<T> future = new ListenableFutureTask(task);
/* 228 */     execute((Runnable)future, Long.MAX_VALUE);
/* 229 */     return (ListenableFuture<T>)future;
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
/*     */   protected void doExecute(Runnable task) {
/* 241 */     Thread thread = (this.threadFactory != null) ? this.threadFactory.newThread(task) : createThread(task);
/* 242 */     thread.start();
/*     */   }
/*     */ 
/*     */   
/*     */   public SimpleAsyncTaskExecutor() {}
/*     */ 
/*     */   
/*     */   private static class ConcurrencyThrottleAdapter
/*     */     extends ConcurrencyThrottleSupport
/*     */   {
/*     */     private ConcurrencyThrottleAdapter() {}
/*     */     
/*     */     protected void beforeAccess() {
/* 255 */       super.beforeAccess();
/*     */     }
/*     */ 
/*     */     
/*     */     protected void afterAccess() {
/* 260 */       super.afterAccess();
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private class ConcurrencyThrottlingRunnable
/*     */     implements Runnable
/*     */   {
/*     */     private final Runnable target;
/*     */ 
/*     */ 
/*     */     
/*     */     public ConcurrencyThrottlingRunnable(Runnable target) {
/* 274 */       this.target = target;
/*     */     }
/*     */ 
/*     */     
/*     */     public void run() {
/*     */       try {
/* 280 */         this.target.run();
/*     */       } finally {
/*     */         
/* 283 */         SimpleAsyncTaskExecutor.this.concurrencyThrottle.afterAccess();
/*     */       } 
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\springframework\core\task\SimpleAsyncTaskExecutor.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */