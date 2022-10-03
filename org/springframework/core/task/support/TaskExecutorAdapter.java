/*     */ package org.springframework.core.task.support;
/*     */ 
/*     */ import java.util.concurrent.Callable;
/*     */ import java.util.concurrent.Executor;
/*     */ import java.util.concurrent.ExecutorService;
/*     */ import java.util.concurrent.Future;
/*     */ import java.util.concurrent.FutureTask;
/*     */ import java.util.concurrent.RejectedExecutionException;
/*     */ import org.springframework.core.task.AsyncListenableTaskExecutor;
/*     */ import org.springframework.core.task.TaskDecorator;
/*     */ import org.springframework.core.task.TaskRejectedException;
/*     */ import org.springframework.lang.Nullable;
/*     */ import org.springframework.util.Assert;
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
/*     */ public class TaskExecutorAdapter
/*     */   implements AsyncListenableTaskExecutor
/*     */ {
/*     */   private final Executor concurrentExecutor;
/*     */   @Nullable
/*     */   private TaskDecorator taskDecorator;
/*     */   
/*     */   public TaskExecutorAdapter(Executor concurrentExecutor) {
/*  60 */     Assert.notNull(concurrentExecutor, "Executor must not be null");
/*  61 */     this.concurrentExecutor = concurrentExecutor;
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
/*     */   
/*     */   public final void setTaskDecorator(TaskDecorator taskDecorator) {
/*  81 */     this.taskDecorator = taskDecorator;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void execute(Runnable task) {
/*     */     try {
/*  92 */       doExecute(this.concurrentExecutor, this.taskDecorator, task);
/*     */     }
/*  94 */     catch (RejectedExecutionException ex) {
/*  95 */       throw new TaskRejectedException("Executor [" + this.concurrentExecutor + "] did not accept task: " + task, ex);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void execute(Runnable task, long startTimeout) {
/* 102 */     execute(task);
/*     */   }
/*     */ 
/*     */   
/*     */   public Future<?> submit(Runnable task) {
/*     */     try {
/* 108 */       if (this.taskDecorator == null && this.concurrentExecutor instanceof ExecutorService) {
/* 109 */         return ((ExecutorService)this.concurrentExecutor).submit(task);
/*     */       }
/*     */       
/* 112 */       FutureTask<Object> future = new FutureTask(task, null);
/* 113 */       doExecute(this.concurrentExecutor, this.taskDecorator, future);
/* 114 */       return future;
/*     */     
/*     */     }
/* 117 */     catch (RejectedExecutionException ex) {
/* 118 */       throw new TaskRejectedException("Executor [" + this.concurrentExecutor + "] did not accept task: " + task, ex);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public <T> Future<T> submit(Callable<T> task) {
/*     */     try {
/* 126 */       if (this.taskDecorator == null && this.concurrentExecutor instanceof ExecutorService) {
/* 127 */         return ((ExecutorService)this.concurrentExecutor).submit(task);
/*     */       }
/*     */       
/* 130 */       FutureTask<T> future = new FutureTask<>(task);
/* 131 */       doExecute(this.concurrentExecutor, this.taskDecorator, future);
/* 132 */       return future;
/*     */     
/*     */     }
/* 135 */     catch (RejectedExecutionException ex) {
/* 136 */       throw new TaskRejectedException("Executor [" + this.concurrentExecutor + "] did not accept task: " + task, ex);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public ListenableFuture<?> submitListenable(Runnable task) {
/*     */     try {
/* 144 */       ListenableFutureTask<Object> future = new ListenableFutureTask(task, null);
/* 145 */       doExecute(this.concurrentExecutor, this.taskDecorator, (Runnable)future);
/* 146 */       return (ListenableFuture<?>)future;
/*     */     }
/* 148 */     catch (RejectedExecutionException ex) {
/* 149 */       throw new TaskRejectedException("Executor [" + this.concurrentExecutor + "] did not accept task: " + task, ex);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public <T> ListenableFuture<T> submitListenable(Callable<T> task) {
/*     */     try {
/* 157 */       ListenableFutureTask<T> future = new ListenableFutureTask(task);
/* 158 */       doExecute(this.concurrentExecutor, this.taskDecorator, (Runnable)future);
/* 159 */       return (ListenableFuture<T>)future;
/*     */     }
/* 161 */     catch (RejectedExecutionException ex) {
/* 162 */       throw new TaskRejectedException("Executor [" + this.concurrentExecutor + "] did not accept task: " + task, ex);
/*     */     } 
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
/*     */   protected void doExecute(Executor concurrentExecutor, @Nullable TaskDecorator taskDecorator, Runnable runnable) throws RejectedExecutionException {
/* 180 */     concurrentExecutor.execute((taskDecorator != null) ? taskDecorator.decorate(runnable) : runnable);
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\springframework\core\task\support\TaskExecutorAdapter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */