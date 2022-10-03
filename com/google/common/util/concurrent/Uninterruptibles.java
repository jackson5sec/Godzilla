/*     */ package com.google.common.util.concurrent;
/*     */ 
/*     */ import com.google.common.annotations.GwtCompatible;
/*     */ import com.google.common.annotations.GwtIncompatible;
/*     */ import com.google.common.base.Preconditions;
/*     */ import com.google.errorprone.annotations.CanIgnoreReturnValue;
/*     */ import java.util.concurrent.BlockingQueue;
/*     */ import java.util.concurrent.CountDownLatch;
/*     */ import java.util.concurrent.ExecutionException;
/*     */ import java.util.concurrent.Future;
/*     */ import java.util.concurrent.Semaphore;
/*     */ import java.util.concurrent.TimeUnit;
/*     */ import java.util.concurrent.TimeoutException;
/*     */ import java.util.concurrent.locks.Condition;
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
/*     */ @GwtCompatible(emulated = true)
/*     */ public final class Uninterruptibles
/*     */ {
/*     */   @GwtIncompatible
/*     */   public static void awaitUninterruptibly(CountDownLatch latch) {
/*  50 */     boolean interrupted = false;
/*     */     
/*     */     while (true) {
/*     */       try {
/*  54 */         latch.await();
/*     */         return;
/*  56 */       } catch (InterruptedException e) {
/*     */ 
/*     */       
/*     */       } finally {
/*     */         
/*  61 */         if (interrupted) {
/*  62 */           Thread.currentThread().interrupt();
/*     */         }
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @CanIgnoreReturnValue
/*     */   @GwtIncompatible
/*     */   public static boolean awaitUninterruptibly(CountDownLatch latch, long timeout, TimeUnit unit) {
/*  75 */     boolean interrupted = false;
/*     */     try {
/*  77 */       long remainingNanos = unit.toNanos(timeout);
/*  78 */       long end = System.nanoTime() + remainingNanos;
/*     */ 
/*     */       
/*     */       while (true) {
/*     */         try {
/*  83 */           return latch.await(remainingNanos, TimeUnit.NANOSECONDS);
/*  84 */         } catch (InterruptedException e) {
/*  85 */           interrupted = true;
/*  86 */           remainingNanos = end - System.nanoTime();
/*     */         } 
/*     */       } 
/*     */     } finally {
/*  90 */       if (interrupted) {
/*  91 */         Thread.currentThread().interrupt();
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
/*     */   @GwtIncompatible
/*     */   public static boolean awaitUninterruptibly(Condition condition, long timeout, TimeUnit unit) {
/* 105 */     boolean interrupted = false;
/*     */     try {
/* 107 */       long remainingNanos = unit.toNanos(timeout);
/* 108 */       long end = System.nanoTime() + remainingNanos;
/*     */       
/*     */       while (true) {
/*     */         try {
/* 112 */           return condition.await(remainingNanos, TimeUnit.NANOSECONDS);
/* 113 */         } catch (InterruptedException e) {
/* 114 */           interrupted = true;
/* 115 */           remainingNanos = end - System.nanoTime();
/*     */         } 
/*     */       } 
/*     */     } finally {
/* 119 */       if (interrupted) {
/* 120 */         Thread.currentThread().interrupt();
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   @GwtIncompatible
/*     */   public static void joinUninterruptibly(Thread toJoin) {
/* 128 */     boolean interrupted = false;
/*     */     
/*     */     while (true) {
/*     */       try {
/* 132 */         toJoin.join();
/*     */         return;
/* 134 */       } catch (InterruptedException e) {
/*     */ 
/*     */       
/*     */       } finally {
/*     */         
/* 139 */         if (interrupted) {
/* 140 */           Thread.currentThread().interrupt();
/*     */         }
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @GwtIncompatible
/*     */   public static void joinUninterruptibly(Thread toJoin, long timeout, TimeUnit unit) {
/* 152 */     Preconditions.checkNotNull(toJoin);
/* 153 */     boolean interrupted = false;
/*     */     try {
/* 155 */       long remainingNanos = unit.toNanos(timeout);
/* 156 */       long end = System.nanoTime() + remainingNanos;
/*     */       
/*     */       while (true) {
/*     */         try {
/* 160 */           TimeUnit.NANOSECONDS.timedJoin(toJoin, remainingNanos);
/*     */           return;
/* 162 */         } catch (InterruptedException e) {
/* 163 */           interrupted = true;
/* 164 */           remainingNanos = end - System.nanoTime();
/*     */         } 
/*     */       } 
/*     */     } finally {
/* 168 */       if (interrupted) {
/* 169 */         Thread.currentThread().interrupt();
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
/*     */   @CanIgnoreReturnValue
/*     */   public static <V> V getUninterruptibly(Future<V> future) throws ExecutionException {
/* 193 */     boolean interrupted = false;
/*     */     
/*     */     while (true) {
/*     */       try {
/* 197 */         return future.get();
/* 198 */       } catch (InterruptedException e) {
/*     */ 
/*     */       
/*     */       } finally {
/*     */         
/* 203 */         if (interrupted) {
/* 204 */           Thread.currentThread().interrupt();
/*     */         }
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
/*     */   @CanIgnoreReturnValue
/*     */   @GwtIncompatible
/*     */   public static <V> V getUninterruptibly(Future<V> future, long timeout, TimeUnit unit) throws ExecutionException, TimeoutException {
/* 232 */     boolean interrupted = false;
/*     */     try {
/* 234 */       long remainingNanos = unit.toNanos(timeout);
/* 235 */       long end = System.nanoTime() + remainingNanos;
/*     */ 
/*     */       
/*     */       while (true) {
/*     */         try {
/* 240 */           return future.get(remainingNanos, TimeUnit.NANOSECONDS);
/* 241 */         } catch (InterruptedException e) {
/* 242 */           interrupted = true;
/* 243 */           remainingNanos = end - System.nanoTime();
/*     */         } 
/*     */       } 
/*     */     } finally {
/* 247 */       if (interrupted) {
/* 248 */         Thread.currentThread().interrupt();
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   @GwtIncompatible
/*     */   public static <E> E takeUninterruptibly(BlockingQueue<E> queue) {
/* 256 */     boolean interrupted = false;
/*     */     
/*     */     while (true) {
/*     */       try {
/* 260 */         return queue.take();
/* 261 */       } catch (InterruptedException e) {
/*     */ 
/*     */       
/*     */       } finally {
/*     */         
/* 266 */         if (interrupted) {
/* 267 */           Thread.currentThread().interrupt();
/*     */         }
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
/*     */   @GwtIncompatible
/*     */   public static <E> void putUninterruptibly(BlockingQueue<E> queue, E element) {
/* 282 */     boolean interrupted = false;
/*     */     
/*     */     while (true) {
/*     */       try {
/* 286 */         queue.put(element);
/*     */         return;
/* 288 */       } catch (InterruptedException e) {
/*     */ 
/*     */       
/*     */       } finally {
/*     */         
/* 293 */         if (interrupted) {
/* 294 */           Thread.currentThread().interrupt();
/*     */         }
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @GwtIncompatible
/*     */   public static void sleepUninterruptibly(long sleepFor, TimeUnit unit) {
/* 304 */     boolean interrupted = false;
/*     */     try {
/* 306 */       long remainingNanos = unit.toNanos(sleepFor);
/* 307 */       long end = System.nanoTime() + remainingNanos;
/*     */       
/*     */       while (true) {
/*     */         try {
/* 311 */           TimeUnit.NANOSECONDS.sleep(remainingNanos);
/*     */           return;
/* 313 */         } catch (InterruptedException e) {
/* 314 */           interrupted = true;
/* 315 */           remainingNanos = end - System.nanoTime();
/*     */         } 
/*     */       } 
/*     */     } finally {
/* 319 */       if (interrupted) {
/* 320 */         Thread.currentThread().interrupt();
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
/*     */   @GwtIncompatible
/*     */   public static boolean tryAcquireUninterruptibly(Semaphore semaphore, long timeout, TimeUnit unit) {
/* 335 */     return tryAcquireUninterruptibly(semaphore, 1, timeout, unit);
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
/*     */   @GwtIncompatible
/*     */   public static boolean tryAcquireUninterruptibly(Semaphore semaphore, int permits, long timeout, TimeUnit unit) {
/* 348 */     boolean interrupted = false;
/*     */     try {
/* 350 */       long remainingNanos = unit.toNanos(timeout);
/* 351 */       long end = System.nanoTime() + remainingNanos;
/*     */ 
/*     */       
/*     */       while (true) {
/*     */         try {
/* 356 */           return semaphore.tryAcquire(permits, remainingNanos, TimeUnit.NANOSECONDS);
/* 357 */         } catch (InterruptedException e) {
/* 358 */           interrupted = true;
/* 359 */           remainingNanos = end - System.nanoTime();
/*     */         } 
/*     */       } 
/*     */     } finally {
/* 363 */       if (interrupted)
/* 364 */         Thread.currentThread().interrupt(); 
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\google\commo\\util\concurrent\Uninterruptibles.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */