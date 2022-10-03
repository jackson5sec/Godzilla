/*     */ package com.google.common.util.concurrent;
/*     */ 
/*     */ import com.google.common.annotations.Beta;
/*     */ import com.google.common.annotations.GwtIncompatible;
/*     */ import com.google.common.base.Preconditions;
/*     */ import java.util.concurrent.Executor;
/*     */ import java.util.concurrent.Executors;
/*     */ import java.util.concurrent.Future;
/*     */ import java.util.concurrent.ThreadFactory;
/*     */ import java.util.concurrent.atomic.AtomicBoolean;
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
/*     */ @Beta
/*     */ @GwtIncompatible
/*     */ public final class JdkFutureAdapters
/*     */ {
/*     */   public static <V> ListenableFuture<V> listenInPoolThread(Future<V> future) {
/*  53 */     if (future instanceof ListenableFuture) {
/*  54 */       return (ListenableFuture<V>)future;
/*     */     }
/*  56 */     return new ListenableFutureAdapter<>(future);
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
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <V> ListenableFuture<V> listenInPoolThread(Future<V> future, Executor executor) {
/*  80 */     Preconditions.checkNotNull(executor);
/*  81 */     if (future instanceof ListenableFuture) {
/*  82 */       return (ListenableFuture<V>)future;
/*     */     }
/*  84 */     return new ListenableFutureAdapter<>(future, executor);
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
/*     */   private static class ListenableFutureAdapter<V>
/*     */     extends ForwardingFuture<V>
/*     */     implements ListenableFuture<V>
/*     */   {
/*  99 */     private static final ThreadFactory threadFactory = (new ThreadFactoryBuilder())
/*     */       
/* 101 */       .setDaemon(true)
/* 102 */       .setNameFormat("ListenableFutureAdapter-thread-%d")
/* 103 */       .build();
/*     */     
/* 105 */     private static final Executor defaultAdapterExecutor = Executors.newCachedThreadPool(threadFactory);
/*     */ 
/*     */     
/*     */     private final Executor adapterExecutor;
/*     */     
/* 110 */     private final ExecutionList executionList = new ExecutionList();
/*     */ 
/*     */ 
/*     */     
/* 114 */     private final AtomicBoolean hasListeners = new AtomicBoolean(false);
/*     */     
/*     */     private final Future<V> delegate;
/*     */ 
/*     */     
/*     */     ListenableFutureAdapter(Future<V> delegate) {
/* 120 */       this(delegate, defaultAdapterExecutor);
/*     */     }
/*     */     
/*     */     ListenableFutureAdapter(Future<V> delegate, Executor adapterExecutor) {
/* 124 */       this.delegate = (Future<V>)Preconditions.checkNotNull(delegate);
/* 125 */       this.adapterExecutor = (Executor)Preconditions.checkNotNull(adapterExecutor);
/*     */     }
/*     */ 
/*     */     
/*     */     protected Future<V> delegate() {
/* 130 */       return this.delegate;
/*     */     }
/*     */ 
/*     */     
/*     */     public void addListener(Runnable listener, Executor exec) {
/* 135 */       this.executionList.add(listener, exec);
/*     */ 
/*     */ 
/*     */       
/* 139 */       if (this.hasListeners.compareAndSet(false, true)) {
/* 140 */         if (this.delegate.isDone()) {
/*     */ 
/*     */           
/* 143 */           this.executionList.execute();
/*     */           
/*     */           return;
/*     */         } 
/*     */         
/* 148 */         this.adapterExecutor.execute(new Runnable()
/*     */             {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */               
/*     */               public void run()
/*     */               {
/*     */                 try {
/* 158 */                   Uninterruptibles.getUninterruptibly(JdkFutureAdapters.ListenableFutureAdapter.this.delegate);
/* 159 */                 } catch (Throwable throwable) {}
/*     */ 
/*     */ 
/*     */                 
/* 163 */                 JdkFutureAdapters.ListenableFutureAdapter.this.executionList.execute();
/*     */               }
/*     */             });
/*     */       } 
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\google\commo\\util\concurrent\JdkFutureAdapters.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */