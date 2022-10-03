/*     */ package com.google.common.util.concurrent;
/*     */ 
/*     */ import com.google.common.annotations.GwtCompatible;
/*     */ import com.google.common.annotations.GwtIncompatible;
/*     */ import com.google.common.base.Preconditions;
/*     */ import java.util.concurrent.ExecutionException;
/*     */ import java.util.concurrent.Executor;
/*     */ import java.util.concurrent.TimeUnit;
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
/*     */ @GwtCompatible(emulated = true)
/*     */ abstract class ImmediateFuture<V>
/*     */   implements ListenableFuture<V>
/*     */ {
/*  32 */   private static final Logger log = Logger.getLogger(ImmediateFuture.class.getName());
/*     */ 
/*     */   
/*     */   public void addListener(Runnable listener, Executor executor) {
/*  36 */     Preconditions.checkNotNull(listener, "Runnable was null.");
/*  37 */     Preconditions.checkNotNull(executor, "Executor was null.");
/*     */     try {
/*  39 */       executor.execute(listener);
/*  40 */     } catch (RuntimeException e) {
/*     */ 
/*     */       
/*  43 */       log.log(Level.SEVERE, "RuntimeException while executing runnable " + listener + " with executor " + executor, e);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean cancel(boolean mayInterruptIfRunning) {
/*  52 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public abstract V get() throws ExecutionException;
/*     */ 
/*     */   
/*     */   public V get(long timeout, TimeUnit unit) throws ExecutionException {
/*  60 */     Preconditions.checkNotNull(unit);
/*  61 */     return get();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isCancelled() {
/*  66 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isDone() {
/*  71 */     return true;
/*     */   }
/*     */   
/*     */   static class ImmediateSuccessfulFuture<V> extends ImmediateFuture<V> {
/*  75 */     static final ImmediateSuccessfulFuture<Object> NULL = new ImmediateSuccessfulFuture(null);
/*     */     private final V value;
/*     */     
/*     */     ImmediateSuccessfulFuture(V value) {
/*  79 */       this.value = value;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public V get() {
/*  85 */       return this.value;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public String toString() {
/*  91 */       return super.toString() + "[status=SUCCESS, result=[" + this.value + "]]";
/*     */     }
/*     */   }
/*     */   
/*     */   @GwtIncompatible
/*     */   static class ImmediateSuccessfulCheckedFuture<V, X extends Exception>
/*     */     extends ImmediateFuture<V> implements CheckedFuture<V, X> {
/*     */     private final V value;
/*     */     
/*     */     ImmediateSuccessfulCheckedFuture(V value) {
/* 101 */       this.value = value;
/*     */     }
/*     */ 
/*     */     
/*     */     public V get() {
/* 106 */       return this.value;
/*     */     }
/*     */ 
/*     */     
/*     */     public V checkedGet() {
/* 111 */       return this.value;
/*     */     }
/*     */ 
/*     */     
/*     */     public V checkedGet(long timeout, TimeUnit unit) {
/* 116 */       Preconditions.checkNotNull(unit);
/* 117 */       return this.value;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public String toString() {
/* 123 */       return super.toString() + "[status=SUCCESS, result=[" + this.value + "]]";
/*     */     }
/*     */   }
/*     */   
/*     */   static final class ImmediateFailedFuture<V> extends AbstractFuture.TrustedFuture<V> {
/*     */     ImmediateFailedFuture(Throwable thrown) {
/* 129 */       setException(thrown);
/*     */     }
/*     */   }
/*     */   
/*     */   static final class ImmediateCancelledFuture<V> extends AbstractFuture.TrustedFuture<V> {
/*     */     ImmediateCancelledFuture() {
/* 135 */       cancel(false);
/*     */     }
/*     */   }
/*     */   
/*     */   @GwtIncompatible
/*     */   static class ImmediateFailedCheckedFuture<V, X extends Exception>
/*     */     extends ImmediateFuture<V> implements CheckedFuture<V, X> {
/*     */     private final X thrown;
/*     */     
/*     */     ImmediateFailedCheckedFuture(X thrown) {
/* 145 */       this.thrown = thrown;
/*     */     }
/*     */ 
/*     */     
/*     */     public V get() throws ExecutionException {
/* 150 */       throw new ExecutionException(this.thrown);
/*     */     }
/*     */ 
/*     */     
/*     */     public V checkedGet() throws X {
/* 155 */       throw this.thrown;
/*     */     }
/*     */ 
/*     */     
/*     */     public V checkedGet(long timeout, TimeUnit unit) throws X {
/* 160 */       Preconditions.checkNotNull(unit);
/* 161 */       throw this.thrown;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public String toString() {
/* 167 */       return super.toString() + "[status=FAILURE, cause=[" + this.thrown + "]]";
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\google\commo\\util\concurrent\ImmediateFuture.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */