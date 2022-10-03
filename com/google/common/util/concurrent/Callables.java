/*     */ package com.google.common.util.concurrent;
/*     */ 
/*     */ import com.google.common.annotations.Beta;
/*     */ import com.google.common.annotations.GwtCompatible;
/*     */ import com.google.common.annotations.GwtIncompatible;
/*     */ import com.google.common.base.Preconditions;
/*     */ import com.google.common.base.Supplier;
/*     */ import java.util.concurrent.Callable;
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
/*     */ @GwtCompatible(emulated = true)
/*     */ public final class Callables
/*     */ {
/*     */   public static <T> Callable<T> returning(final T value) {
/*  38 */     return new Callable<T>()
/*     */       {
/*     */         public T call() {
/*  41 */           return (T)value;
/*     */         }
/*     */       };
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
/*     */   @Beta
/*     */   @GwtIncompatible
/*     */   public static <T> AsyncCallable<T> asAsyncCallable(final Callable<T> callable, final ListeningExecutorService listeningExecutorService) {
/*  58 */     Preconditions.checkNotNull(callable);
/*  59 */     Preconditions.checkNotNull(listeningExecutorService);
/*  60 */     return new AsyncCallable<T>()
/*     */       {
/*     */         public ListenableFuture<T> call() throws Exception {
/*  63 */           return listeningExecutorService.submit(callable);
/*     */         }
/*     */       };
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
/*     */   @GwtIncompatible
/*     */   static <T> Callable<T> threadRenaming(final Callable<T> callable, final Supplier<String> nameSupplier) {
/*  80 */     Preconditions.checkNotNull(nameSupplier);
/*  81 */     Preconditions.checkNotNull(callable);
/*  82 */     return new Callable<T>()
/*     */       {
/*     */         public T call() throws Exception {
/*  85 */           Thread currentThread = Thread.currentThread();
/*  86 */           String oldName = currentThread.getName();
/*  87 */           boolean restoreName = Callables.trySetName((String)nameSupplier.get(), currentThread);
/*     */           try {
/*  89 */             return (T)callable.call();
/*     */           } finally {
/*  91 */             if (restoreName) {
/*  92 */               boolean bool = Callables.trySetName(oldName, currentThread);
/*     */             }
/*     */           } 
/*     */         }
/*     */       };
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
/*     */   @GwtIncompatible
/*     */   static Runnable threadRenaming(final Runnable task, final Supplier<String> nameSupplier) {
/* 110 */     Preconditions.checkNotNull(nameSupplier);
/* 111 */     Preconditions.checkNotNull(task);
/* 112 */     return new Runnable()
/*     */       {
/*     */         public void run() {
/* 115 */           Thread currentThread = Thread.currentThread();
/* 116 */           String oldName = currentThread.getName();
/* 117 */           boolean restoreName = Callables.trySetName((String)nameSupplier.get(), currentThread);
/*     */           try {
/* 119 */             task.run();
/*     */           } finally {
/* 121 */             if (restoreName) {
/* 122 */               boolean bool = Callables.trySetName(oldName, currentThread);
/*     */             }
/*     */           } 
/*     */         }
/*     */       };
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @GwtIncompatible
/*     */   private static boolean trySetName(String threadName, Thread currentThread) {
/*     */     try {
/* 136 */       currentThread.setName(threadName);
/* 137 */       return true;
/* 138 */     } catch (SecurityException e) {
/* 139 */       return false;
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\google\commo\\util\concurrent\Callables.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */