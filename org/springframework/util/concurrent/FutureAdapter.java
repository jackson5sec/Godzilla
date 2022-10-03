/*     */ package org.springframework.util.concurrent;
/*     */ 
/*     */ import java.util.concurrent.ExecutionException;
/*     */ import java.util.concurrent.Future;
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
/*     */ public abstract class FutureAdapter<T, S>
/*     */   implements Future<T>
/*     */ {
/*     */   private final Future<S> adaptee;
/*     */   @Nullable
/*     */   private Object result;
/*  44 */   private State state = State.NEW;
/*     */   
/*  46 */   private final Object mutex = new Object();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected FutureAdapter(Future<S> adaptee) {
/*  54 */     Assert.notNull(adaptee, "Delegate must not be null");
/*  55 */     this.adaptee = adaptee;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected Future<S> getAdaptee() {
/*  63 */     return this.adaptee;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean cancel(boolean mayInterruptIfRunning) {
/*  68 */     return this.adaptee.cancel(mayInterruptIfRunning);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isCancelled() {
/*  73 */     return this.adaptee.isCancelled();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isDone() {
/*  78 */     return this.adaptee.isDone();
/*     */   }
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public T get() throws InterruptedException, ExecutionException {
/*  84 */     return adaptInternal(this.adaptee.get());
/*     */   }
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public T get(long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
/*  90 */     return adaptInternal(this.adaptee.get(timeout, unit));
/*     */   }
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   final T adaptInternal(S adapteeResult) throws ExecutionException {
/*  96 */     synchronized (this.mutex) {
/*  97 */       switch (this.state) {
/*     */         case SUCCESS:
/*  99 */           return (T)this.result;
/*     */         case FAILURE:
/* 101 */           Assert.state(this.result instanceof ExecutionException, "Failure without exception");
/* 102 */           throw (ExecutionException)this.result;
/*     */         case NEW:
/*     */           try {
/* 105 */             T adapted = adapt(adapteeResult);
/* 106 */             this.result = adapted;
/* 107 */             this.state = State.SUCCESS;
/* 108 */             return adapted;
/*     */           }
/* 110 */           catch (ExecutionException ex) {
/* 111 */             this.result = ex;
/* 112 */             this.state = State.FAILURE;
/* 113 */             throw ex;
/*     */           }
/* 115 */           catch (Throwable ex) {
/* 116 */             ExecutionException execEx = new ExecutionException(ex);
/* 117 */             this.result = execEx;
/* 118 */             this.state = State.FAILURE;
/* 119 */             throw execEx;
/*     */           } 
/*     */       } 
/* 122 */       throw new IllegalStateException();
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   protected abstract T adapt(S paramS) throws ExecutionException;
/*     */ 
/*     */ 
/*     */   
/*     */   private enum State
/*     */   {
/* 135 */     NEW, SUCCESS, FAILURE;
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\springframewor\\util\concurrent\FutureAdapter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */