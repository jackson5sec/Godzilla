/*     */ package com.google.common.util.concurrent;
/*     */ 
/*     */ import com.google.common.annotations.GwtIncompatible;
/*     */ import com.google.common.base.Preconditions;
/*     */ import com.google.errorprone.annotations.concurrent.GuardedBy;
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
/*     */ @GwtIncompatible
/*     */ public final class ExecutionList
/*     */ {
/*  45 */   private static final Logger log = Logger.getLogger(ExecutionList.class.getName());
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @GuardedBy("this")
/*     */   private RunnableExecutorPair runnables;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @GuardedBy("this")
/*     */   private boolean executed;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void add(Runnable runnable, Executor executor) {
/*  71 */     Preconditions.checkNotNull(runnable, "Runnable was null.");
/*  72 */     Preconditions.checkNotNull(executor, "Executor was null.");
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  77 */     synchronized (this) {
/*  78 */       if (!this.executed) {
/*  79 */         this.runnables = new RunnableExecutorPair(runnable, executor, this.runnables);
/*     */ 
/*     */         
/*     */         return;
/*     */       } 
/*     */     } 
/*     */ 
/*     */     
/*  87 */     executeListener(runnable, executor);
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
/*     */   public void execute() {
/*     */     RunnableExecutorPair list;
/* 105 */     synchronized (this) {
/* 106 */       if (this.executed) {
/*     */         return;
/*     */       }
/* 109 */       this.executed = true;
/* 110 */       list = this.runnables;
/* 111 */       this.runnables = null;
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
/* 122 */     RunnableExecutorPair reversedList = null;
/* 123 */     while (list != null) {
/* 124 */       RunnableExecutorPair tmp = list;
/* 125 */       list = list.next;
/* 126 */       tmp.next = reversedList;
/* 127 */       reversedList = tmp;
/*     */     } 
/* 129 */     while (reversedList != null) {
/* 130 */       executeListener(reversedList.runnable, reversedList.executor);
/* 131 */       reversedList = reversedList.next;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static void executeListener(Runnable runnable, Executor executor) {
/*     */     try {
/* 141 */       executor.execute(runnable);
/* 142 */     } catch (RuntimeException e) {
/*     */ 
/*     */ 
/*     */       
/* 146 */       log.log(Level.SEVERE, "RuntimeException while executing runnable " + runnable + " with executor " + executor, e);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private static final class RunnableExecutorPair
/*     */   {
/*     */     final Runnable runnable;
/*     */     
/*     */     final Executor executor;
/*     */     RunnableExecutorPair next;
/*     */     
/*     */     RunnableExecutorPair(Runnable runnable, Executor executor, RunnableExecutorPair next) {
/* 159 */       this.runnable = runnable;
/* 160 */       this.executor = executor;
/* 161 */       this.next = next;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\google\commo\\util\concurrent\ExecutionList.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */