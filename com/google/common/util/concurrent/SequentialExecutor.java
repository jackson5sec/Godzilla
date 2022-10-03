/*     */ package com.google.common.util.concurrent;
/*     */ 
/*     */ import com.google.common.annotations.GwtIncompatible;
/*     */ import com.google.common.base.Preconditions;
/*     */ import com.google.errorprone.annotations.concurrent.GuardedBy;
/*     */ import java.util.ArrayDeque;
/*     */ import java.util.Deque;
/*     */ import java.util.concurrent.Executor;
/*     */ import java.util.logging.Level;
/*     */ import java.util.logging.Logger;
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
/*     */ @GwtIncompatible
/*     */ final class SequentialExecutor
/*     */   implements Executor
/*     */ {
/*  50 */   private static final Logger log = Logger.getLogger(SequentialExecutor.class.getName());
/*     */   private final Executor executor;
/*     */   
/*     */   enum WorkerRunningState {
/*  54 */     IDLE,
/*     */     
/*  56 */     QUEUING,
/*     */     
/*  58 */     QUEUED,
/*  59 */     RUNNING;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @GuardedBy("queue")
/*  65 */   private final Deque<Runnable> queue = new ArrayDeque<>();
/*     */ 
/*     */   
/*     */   @GuardedBy("queue")
/*  69 */   private WorkerRunningState workerRunningState = WorkerRunningState.IDLE;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @GuardedBy("queue")
/*  79 */   private long workerRunCount = 0L;
/*     */ 
/*     */   
/*  82 */   private final QueueWorker worker = new QueueWorker();
/*     */ 
/*     */   
/*     */   SequentialExecutor(Executor executor) {
/*  86 */     this.executor = (Executor)Preconditions.checkNotNull(executor);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void execute(final Runnable task) {
/*     */     Runnable submittedTask;
/*     */     long oldRunCount;
/*  97 */     Preconditions.checkNotNull(task);
/*     */ 
/*     */     
/* 100 */     synchronized (this.queue) {
/*     */ 
/*     */       
/* 103 */       if (this.workerRunningState == WorkerRunningState.RUNNING || this.workerRunningState == WorkerRunningState.QUEUED) {
/* 104 */         this.queue.add(task);
/*     */         
/*     */         return;
/*     */       } 
/* 108 */       oldRunCount = this.workerRunCount;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 116 */       submittedTask = new Runnable()
/*     */         {
/*     */           public void run()
/*     */           {
/* 120 */             task.run();
/*     */           }
/*     */         };
/* 123 */       this.queue.add(submittedTask);
/* 124 */       this.workerRunningState = WorkerRunningState.QUEUING;
/*     */     } 
/*     */     
/*     */     try {
/* 128 */       this.executor.execute(this.worker);
/* 129 */     } catch (RuntimeException|Error t) {
/* 130 */       synchronized (this.queue) {
/*     */ 
/*     */         
/* 133 */         boolean removed = ((this.workerRunningState == WorkerRunningState.IDLE || this.workerRunningState == WorkerRunningState.QUEUING) && this.queue.removeLastOccurrence(submittedTask));
/*     */ 
/*     */         
/* 136 */         if (!(t instanceof java.util.concurrent.RejectedExecutionException) || removed) {
/* 137 */           throw t;
/*     */         }
/*     */       } 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/*     */       return;
/*     */     } 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 154 */     boolean alreadyMarkedQueued = (this.workerRunningState != WorkerRunningState.QUEUING);
/* 155 */     if (alreadyMarkedQueued) {
/*     */       return;
/*     */     }
/* 158 */     synchronized (this.queue) {
/* 159 */       if (this.workerRunCount == oldRunCount && this.workerRunningState == WorkerRunningState.QUEUING)
/* 160 */         this.workerRunningState = WorkerRunningState.QUEUED; 
/*     */     } 
/*     */   }
/*     */   
/*     */   private final class QueueWorker
/*     */     implements Runnable
/*     */   {
/*     */     private QueueWorker() {}
/*     */     
/*     */     public void run() {
/*     */       try {
/* 171 */         workOnQueue();
/* 172 */       } catch (Error e) {
/* 173 */         synchronized (SequentialExecutor.this.queue) {
/* 174 */           SequentialExecutor.this.workerRunningState = SequentialExecutor.WorkerRunningState.IDLE;
/*     */         } 
/* 176 */         throw e;
/*     */       } 
/*     */     }
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
/*     */     private void workOnQueue() {
/* 195 */       boolean interruptedDuringTask = false;
/* 196 */       boolean hasSetRunning = false;
/*     */       try {
/*     */         while (true) {
/*     */           Runnable task;
/* 200 */           synchronized (SequentialExecutor.this.queue) {
/*     */ 
/*     */             
/* 203 */             if (!hasSetRunning) {
/* 204 */               if (SequentialExecutor.this.workerRunningState == SequentialExecutor.WorkerRunningState.RUNNING) {
/*     */                 return;
/*     */               }
/*     */ 
/*     */ 
/*     */ 
/*     */               
/* 211 */               SequentialExecutor.this.workerRunCount++;
/* 212 */               SequentialExecutor.this.workerRunningState = SequentialExecutor.WorkerRunningState.RUNNING;
/* 213 */               hasSetRunning = true;
/*     */             } 
/*     */             
/* 216 */             task = SequentialExecutor.this.queue.poll();
/* 217 */             if (task == null) {
/* 218 */               SequentialExecutor.this.workerRunningState = SequentialExecutor.WorkerRunningState.IDLE;
/*     */ 
/*     */               
/*     */               return;
/*     */             } 
/*     */           } 
/*     */           
/* 225 */           interruptedDuringTask |= Thread.interrupted();
/*     */           try {
/* 227 */             task.run();
/* 228 */           } catch (RuntimeException e) {
/* 229 */             SequentialExecutor.log.log(Level.SEVERE, "Exception while executing runnable " + task, e);
/*     */           }
/*     */         
/*     */         }
/*     */       
/*     */       } finally {
/*     */         
/* 236 */         if (interruptedDuringTask)
/* 237 */           Thread.currentThread().interrupt(); 
/*     */       } 
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\google\commo\\util\concurrent\SequentialExecutor.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */