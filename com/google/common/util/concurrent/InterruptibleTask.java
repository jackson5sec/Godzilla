/*     */ package com.google.common.util.concurrent;
/*     */ 
/*     */ import com.google.common.annotations.GwtCompatible;
/*     */ import com.google.j2objc.annotations.ReflectionSupport;
/*     */ import java.util.concurrent.atomic.AtomicReference;
/*     */ import java.util.concurrent.locks.LockSupport;
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
/*     */ @ReflectionSupport(ReflectionSupport.Level.FULL)
/*     */ abstract class InterruptibleTask<T>
/*     */   extends AtomicReference<Runnable>
/*     */   implements Runnable
/*     */ {
/*     */   static {
/*  35 */     Class<LockSupport> clazz = LockSupport.class;
/*     */   }
/*     */   
/*     */   private static final class DoNothingRunnable implements Runnable {
/*     */     private DoNothingRunnable() {}
/*     */     
/*     */     public void run() {}
/*     */   }
/*     */   
/*  44 */   private static final Runnable DONE = new DoNothingRunnable();
/*  45 */   private static final Runnable INTERRUPTING = new DoNothingRunnable();
/*  46 */   private static final Runnable PARKED = new DoNothingRunnable();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static final int MAX_BUSY_WAIT_SPINS = 1000;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final void run() {
/*  59 */     Thread currentThread = Thread.currentThread();
/*  60 */     if (!compareAndSet(null, currentThread)) {
/*     */       return;
/*     */     }
/*     */     
/*  64 */     boolean run = !isDone();
/*  65 */     T result = null;
/*  66 */     Throwable error = null;
/*     */     try {
/*  68 */       if (run) {
/*  69 */         result = runInterruptibly();
/*     */       }
/*  71 */     } catch (Throwable t) {
/*  72 */       error = t;
/*     */     } finally {
/*     */       
/*  75 */       if (!compareAndSet(currentThread, DONE)) {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */         
/*  82 */         boolean restoreInterruptedBit = false;
/*  83 */         int spinCount = 0;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */         
/*  93 */         Runnable state = get();
/*  94 */         while (state == INTERRUPTING || state == PARKED) {
/*  95 */           spinCount++;
/*  96 */           if (spinCount > 1000) {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */             
/* 103 */             if (state == PARKED || compareAndSet(INTERRUPTING, PARKED)) {
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
/* 115 */               restoreInterruptedBit = (Thread.interrupted() || restoreInterruptedBit);
/* 116 */               LockSupport.park(this);
/*     */             } 
/*     */           } else {
/* 119 */             Thread.yield();
/*     */           } 
/* 121 */           state = get();
/*     */         } 
/* 123 */         if (restoreInterruptedBit) {
/* 124 */           currentThread.interrupt();
/*     */         }
/*     */       } 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 132 */       if (run) {
/* 133 */         afterRanInterruptibly(result, error);
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
/*     */   final void interruptTask() {
/* 164 */     Runnable currentRunner = get();
/* 165 */     if (currentRunner instanceof Thread && compareAndSet(currentRunner, INTERRUPTING)) {
/*     */       
/*     */       try {
/*     */ 
/*     */ 
/*     */         
/* 171 */         ((Thread)currentRunner).interrupt();
/*     */       } finally {
/* 173 */         Runnable prev = getAndSet(DONE);
/* 174 */         if (prev == PARKED) {
/* 175 */           LockSupport.unpark((Thread)currentRunner);
/*     */         }
/*     */       } 
/*     */     }
/*     */   }
/*     */   
/*     */   public final String toString() {
/*     */     String result;
/* 183 */     Runnable state = get();
/*     */     
/* 185 */     if (state == DONE) {
/* 186 */       result = "running=[DONE]";
/* 187 */     } else if (state == INTERRUPTING) {
/* 188 */       result = "running=[INTERRUPTED]";
/* 189 */     } else if (state instanceof Thread) {
/*     */       
/* 191 */       result = "running=[RUNNING ON " + ((Thread)state).getName() + "]";
/*     */     } else {
/* 193 */       result = "running=[NOT STARTED YET]";
/*     */     } 
/* 195 */     return result + ", " + toPendingString();
/*     */   }
/*     */   
/*     */   abstract boolean isDone();
/*     */   
/*     */   abstract T runInterruptibly() throws Exception;
/*     */   
/*     */   abstract void afterRanInterruptibly(T paramT, Throwable paramThrowable);
/*     */   
/*     */   abstract String toPendingString();
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\google\commo\\util\concurrent\InterruptibleTask.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */