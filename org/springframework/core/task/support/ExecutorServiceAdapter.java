/*    */ package org.springframework.core.task.support;
/*    */ 
/*    */ import java.util.List;
/*    */ import java.util.concurrent.AbstractExecutorService;
/*    */ import java.util.concurrent.TimeUnit;
/*    */ import org.springframework.core.task.TaskExecutor;
/*    */ import org.springframework.util.Assert;
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
/*    */ public class ExecutorServiceAdapter
/*    */   extends AbstractExecutorService
/*    */ {
/*    */   private final TaskExecutor taskExecutor;
/*    */   
/*    */   public ExecutorServiceAdapter(TaskExecutor taskExecutor) {
/* 55 */     Assert.notNull(taskExecutor, "TaskExecutor must not be null");
/* 56 */     this.taskExecutor = taskExecutor;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public void execute(Runnable task) {
/* 62 */     this.taskExecutor.execute(task);
/*    */   }
/*    */ 
/*    */   
/*    */   public void shutdown() {
/* 67 */     throw new IllegalStateException("Manual shutdown not supported - ExecutorServiceAdapter is dependent on an external lifecycle");
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public List<Runnable> shutdownNow() {
/* 73 */     throw new IllegalStateException("Manual shutdown not supported - ExecutorServiceAdapter is dependent on an external lifecycle");
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public boolean awaitTermination(long timeout, TimeUnit unit) throws InterruptedException {
/* 79 */     throw new IllegalStateException("Manual shutdown not supported - ExecutorServiceAdapter is dependent on an external lifecycle");
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public boolean isShutdown() {
/* 85 */     return false;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean isTerminated() {
/* 90 */     return false;
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\springframework\core\task\support\ExecutorServiceAdapter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */