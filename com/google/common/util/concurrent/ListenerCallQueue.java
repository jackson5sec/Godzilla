/*     */ package com.google.common.util.concurrent;
/*     */ 
/*     */ import com.google.common.annotations.GwtIncompatible;
/*     */ import com.google.common.base.Preconditions;
/*     */ import com.google.common.collect.Queues;
/*     */ import com.google.errorprone.annotations.concurrent.GuardedBy;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collections;
/*     */ import java.util.List;
/*     */ import java.util.Queue;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @GwtIncompatible
/*     */ final class ListenerCallQueue<L>
/*     */ {
/*  58 */   private static final Logger logger = Logger.getLogger(ListenerCallQueue.class.getName());
/*     */ 
/*     */ 
/*     */   
/*  62 */   private final List<PerListenerQueue<L>> listeners = Collections.synchronizedList(new ArrayList<>());
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addListener(L listener, Executor executor) {
/*  75 */     Preconditions.checkNotNull(listener, "listener");
/*  76 */     Preconditions.checkNotNull(executor, "executor");
/*  77 */     this.listeners.add(new PerListenerQueue<>(listener, executor));
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
/*     */   public void enqueue(Event<L> event) {
/*  89 */     enqueueHelper(event, event);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void enqueue(Event<L> event, String label) {
/*  99 */     enqueueHelper(event, label);
/*     */   }
/*     */   
/*     */   private void enqueueHelper(Event<L> event, Object label) {
/* 103 */     Preconditions.checkNotNull(event, "event");
/* 104 */     Preconditions.checkNotNull(label, "label");
/* 105 */     synchronized (this.listeners) {
/* 106 */       for (PerListenerQueue<L> queue : this.listeners) {
/* 107 */         queue.add(event, label);
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void dispatch() {
/* 119 */     for (int i = 0; i < this.listeners.size(); i++) {
/* 120 */       ((PerListenerQueue)this.listeners.get(i)).dispatch();
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static final class PerListenerQueue<L>
/*     */     implements Runnable
/*     */   {
/*     */     final L listener;
/*     */ 
/*     */     
/*     */     final Executor executor;
/*     */ 
/*     */     
/*     */     @GuardedBy("this")
/* 136 */     final Queue<ListenerCallQueue.Event<L>> waitQueue = Queues.newArrayDeque();
/*     */     
/*     */     @GuardedBy("this")
/* 139 */     final Queue<Object> labelQueue = Queues.newArrayDeque();
/*     */     
/*     */     @GuardedBy("this")
/*     */     boolean isThreadScheduled;
/*     */     
/*     */     PerListenerQueue(L listener, Executor executor) {
/* 145 */       this.listener = (L)Preconditions.checkNotNull(listener);
/* 146 */       this.executor = (Executor)Preconditions.checkNotNull(executor);
/*     */     }
/*     */ 
/*     */     
/*     */     synchronized void add(ListenerCallQueue.Event<L> event, Object label) {
/* 151 */       this.waitQueue.add(event);
/* 152 */       this.labelQueue.add(label);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     void dispatch() {
/* 160 */       boolean scheduleEventRunner = false;
/* 161 */       synchronized (this) {
/* 162 */         if (!this.isThreadScheduled) {
/* 163 */           this.isThreadScheduled = true;
/* 164 */           scheduleEventRunner = true;
/*     */         } 
/*     */       } 
/* 167 */       if (scheduleEventRunner) {
/*     */         try {
/* 169 */           this.executor.execute(this);
/* 170 */         } catch (RuntimeException e) {
/*     */           
/* 172 */           synchronized (this) {
/* 173 */             this.isThreadScheduled = false;
/*     */           } 
/*     */           
/* 176 */           ListenerCallQueue.logger.log(Level.SEVERE, "Exception while running callbacks for " + this.listener + " on " + this.executor, e);
/*     */ 
/*     */ 
/*     */           
/* 180 */           throw e;
/*     */         } 
/*     */       }
/*     */     }
/*     */ 
/*     */     
/*     */     public void run() {
/* 187 */       boolean stillRunning = true;
/*     */       try {
/*     */         while (true) {
/*     */           ListenerCallQueue.Event<L> nextToRun;
/*     */           Object nextLabel;
/* 192 */           synchronized (this) {
/* 193 */             Preconditions.checkState(this.isThreadScheduled);
/* 194 */             nextToRun = this.waitQueue.poll();
/* 195 */             nextLabel = this.labelQueue.poll();
/* 196 */             if (nextToRun == null) {
/* 197 */               this.isThreadScheduled = false;
/* 198 */               stillRunning = false;
/*     */               
/*     */               break;
/*     */             } 
/*     */           } 
/*     */           
/*     */           try {
/* 205 */             nextToRun.call(this.listener);
/* 206 */           } catch (RuntimeException e) {
/*     */             
/* 208 */             ListenerCallQueue.logger.log(Level.SEVERE, "Exception while executing callback: " + this.listener + " " + nextLabel, e);
/*     */           }
/*     */         
/*     */         }
/*     */       
/*     */       } finally {
/*     */         
/* 215 */         if (stillRunning)
/*     */         {
/*     */           
/* 218 */           synchronized (this) {
/* 219 */             this.isThreadScheduled = false;
/*     */           } 
/*     */         }
/*     */       } 
/*     */     }
/*     */   }
/*     */   
/*     */   static interface Event<L> {
/*     */     void call(L param1L);
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\google\commo\\util\concurrent\ListenerCallQueue.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */