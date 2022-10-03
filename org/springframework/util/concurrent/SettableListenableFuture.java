/*     */ package org.springframework.util.concurrent;
/*     */ 
/*     */ import java.util.concurrent.Callable;
/*     */ import java.util.concurrent.CompletableFuture;
/*     */ import java.util.concurrent.ExecutionException;
/*     */ import java.util.concurrent.TimeUnit;
/*     */ import java.util.concurrent.TimeoutException;
/*     */ import org.springframework.lang.Nullable;
/*     */ import org.springframework.util.Assert;
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
/*     */ public class SettableListenableFuture<T>
/*     */   implements ListenableFuture<T>
/*     */ {
/*     */   private static final Callable<Object> DUMMY_CALLABLE = () -> {
/*     */       throw new IllegalStateException("Should never be called");
/*     */     };
/*  47 */   private final SettableTask<T> settableTask = new SettableTask<>();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean set(@Nullable T value) {
/*  58 */     return this.settableTask.setResultValue(value);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean setException(Throwable exception) {
/*  69 */     Assert.notNull(exception, "Exception must not be null");
/*  70 */     return this.settableTask.setExceptionResult(exception);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void addCallback(ListenableFutureCallback<? super T> callback) {
/*  76 */     this.settableTask.addCallback(callback);
/*     */   }
/*     */ 
/*     */   
/*     */   public void addCallback(SuccessCallback<? super T> successCallback, FailureCallback failureCallback) {
/*  81 */     this.settableTask.addCallback(successCallback, failureCallback);
/*     */   }
/*     */ 
/*     */   
/*     */   public CompletableFuture<T> completable() {
/*  86 */     return this.settableTask.completable();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean cancel(boolean mayInterruptIfRunning) {
/*  92 */     boolean cancelled = this.settableTask.cancel(mayInterruptIfRunning);
/*  93 */     if (cancelled && mayInterruptIfRunning) {
/*  94 */       interruptTask();
/*     */     }
/*  96 */     return cancelled;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isCancelled() {
/* 101 */     return this.settableTask.isCancelled();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isDone() {
/* 106 */     return this.settableTask.isDone();
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
/*     */   public T get() throws InterruptedException, ExecutionException {
/* 119 */     return this.settableTask.get();
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
/*     */   public T get(long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
/* 134 */     return this.settableTask.get(timeout, unit);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void interruptTask() {}
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static class SettableTask<T>
/*     */     extends ListenableFutureTask<T>
/*     */   {
/*     */     @Nullable
/*     */     private volatile Thread completingThread;
/*     */ 
/*     */ 
/*     */     
/*     */     public SettableTask() {
/* 154 */       super((Callable)SettableListenableFuture.DUMMY_CALLABLE);
/*     */     }
/*     */     
/*     */     public boolean setResultValue(@Nullable T value) {
/* 158 */       set(value);
/* 159 */       return checkCompletingThread();
/*     */     }
/*     */     
/*     */     public boolean setExceptionResult(Throwable exception) {
/* 163 */       setException(exception);
/* 164 */       return checkCompletingThread();
/*     */     }
/*     */ 
/*     */     
/*     */     protected void done() {
/* 169 */       if (!isCancelled())
/*     */       {
/*     */ 
/*     */         
/* 173 */         this.completingThread = Thread.currentThread();
/*     */       }
/* 175 */       super.done();
/*     */     }
/*     */     
/*     */     private boolean checkCompletingThread() {
/* 179 */       boolean check = (this.completingThread == Thread.currentThread());
/* 180 */       if (check) {
/* 181 */         this.completingThread = null;
/*     */       }
/* 183 */       return check;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\springframewor\\util\concurrent\SettableListenableFuture.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */