/*     */ package com.google.common.collect;
/*     */ 
/*     */ import com.google.common.annotations.Beta;
/*     */ import com.google.common.annotations.GwtCompatible;
/*     */ import com.google.common.annotations.GwtIncompatible;
/*     */ import com.google.common.base.Preconditions;
/*     */ import com.google.errorprone.annotations.CanIgnoreReturnValue;
/*     */ import java.util.ArrayDeque;
/*     */ import java.util.Collection;
/*     */ import java.util.Deque;
/*     */ import java.util.PriorityQueue;
/*     */ import java.util.Queue;
/*     */ import java.util.concurrent.ArrayBlockingQueue;
/*     */ import java.util.concurrent.BlockingQueue;
/*     */ import java.util.concurrent.ConcurrentLinkedQueue;
/*     */ import java.util.concurrent.LinkedBlockingDeque;
/*     */ import java.util.concurrent.LinkedBlockingQueue;
/*     */ import java.util.concurrent.PriorityBlockingQueue;
/*     */ import java.util.concurrent.SynchronousQueue;
/*     */ import java.util.concurrent.TimeUnit;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
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
/*     */ public final class Queues
/*     */ {
/*     */   @GwtIncompatible
/*     */   public static <E> ArrayBlockingQueue<E> newArrayBlockingQueue(int capacity) {
/*  55 */     return new ArrayBlockingQueue<>(capacity);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <E> ArrayDeque<E> newArrayDeque() {
/*  66 */     return new ArrayDeque<>();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <E> ArrayDeque<E> newArrayDeque(Iterable<? extends E> elements) {
/*  76 */     if (elements instanceof Collection) {
/*  77 */       return new ArrayDeque<>(Collections2.cast(elements));
/*     */     }
/*  79 */     ArrayDeque<E> deque = new ArrayDeque<>();
/*  80 */     Iterables.addAll(deque, elements);
/*  81 */     return deque;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @GwtIncompatible
/*     */   public static <E> ConcurrentLinkedQueue<E> newConcurrentLinkedQueue() {
/*  89 */     return new ConcurrentLinkedQueue<>();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @GwtIncompatible
/*     */   public static <E> ConcurrentLinkedQueue<E> newConcurrentLinkedQueue(Iterable<? extends E> elements) {
/*  99 */     if (elements instanceof Collection) {
/* 100 */       return new ConcurrentLinkedQueue<>(Collections2.cast(elements));
/*     */     }
/* 102 */     ConcurrentLinkedQueue<E> queue = new ConcurrentLinkedQueue<>();
/* 103 */     Iterables.addAll(queue, elements);
/* 104 */     return queue;
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
/*     */   public static <E> LinkedBlockingDeque<E> newLinkedBlockingDeque() {
/* 116 */     return new LinkedBlockingDeque<>();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @GwtIncompatible
/*     */   public static <E> LinkedBlockingDeque<E> newLinkedBlockingDeque(int capacity) {
/* 127 */     return new LinkedBlockingDeque<>(capacity);
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
/*     */   public static <E> LinkedBlockingDeque<E> newLinkedBlockingDeque(Iterable<? extends E> elements) {
/* 139 */     if (elements instanceof Collection) {
/* 140 */       return new LinkedBlockingDeque<>(Collections2.cast(elements));
/*     */     }
/* 142 */     LinkedBlockingDeque<E> deque = new LinkedBlockingDeque<>();
/* 143 */     Iterables.addAll(deque, elements);
/* 144 */     return deque;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @GwtIncompatible
/*     */   public static <E> LinkedBlockingQueue<E> newLinkedBlockingQueue() {
/* 152 */     return new LinkedBlockingQueue<>();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @GwtIncompatible
/*     */   public static <E> LinkedBlockingQueue<E> newLinkedBlockingQueue(int capacity) {
/* 162 */     return new LinkedBlockingQueue<>(capacity);
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
/*     */   public static <E> LinkedBlockingQueue<E> newLinkedBlockingQueue(Iterable<? extends E> elements) {
/* 175 */     if (elements instanceof Collection) {
/* 176 */       return new LinkedBlockingQueue<>(Collections2.cast(elements));
/*     */     }
/* 178 */     LinkedBlockingQueue<E> queue = new LinkedBlockingQueue<>();
/* 179 */     Iterables.addAll(queue, elements);
/* 180 */     return queue;
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
/*     */   public static <E extends Comparable> PriorityBlockingQueue<E> newPriorityBlockingQueue() {
/* 195 */     return new PriorityBlockingQueue<>();
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
/*     */   public static <E extends Comparable> PriorityBlockingQueue<E> newPriorityBlockingQueue(Iterable<? extends E> elements) {
/* 209 */     if (elements instanceof Collection) {
/* 210 */       return new PriorityBlockingQueue<>(Collections2.cast(elements));
/*     */     }
/* 212 */     PriorityBlockingQueue<E> queue = new PriorityBlockingQueue<>();
/* 213 */     Iterables.addAll(queue, elements);
/* 214 */     return queue;
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
/*     */   public static <E extends Comparable> PriorityQueue<E> newPriorityQueue() {
/* 226 */     return new PriorityQueue<>();
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
/*     */   public static <E extends Comparable> PriorityQueue<E> newPriorityQueue(Iterable<? extends E> elements) {
/* 239 */     if (elements instanceof Collection) {
/* 240 */       return new PriorityQueue<>(Collections2.cast(elements));
/*     */     }
/* 242 */     PriorityQueue<E> queue = new PriorityQueue<>();
/* 243 */     Iterables.addAll(queue, elements);
/* 244 */     return queue;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @GwtIncompatible
/*     */   public static <E> SynchronousQueue<E> newSynchronousQueue() {
/* 252 */     return new SynchronousQueue<>();
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
/*     */   @Beta
/*     */   @CanIgnoreReturnValue
/*     */   @GwtIncompatible
/*     */   public static <E> int drain(BlockingQueue<E> q, Collection<? super E> buffer, int numElements, long timeout, TimeUnit unit) throws InterruptedException {
/* 278 */     Preconditions.checkNotNull(buffer);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 284 */     long deadline = System.nanoTime() + unit.toNanos(timeout);
/* 285 */     int added = 0;
/* 286 */     while (added < numElements) {
/*     */ 
/*     */       
/* 289 */       added += q.drainTo(buffer, numElements - added);
/* 290 */       if (added < numElements) {
/* 291 */         E e = q.poll(deadline - System.nanoTime(), TimeUnit.NANOSECONDS);
/* 292 */         if (e == null) {
/*     */           break;
/*     */         }
/* 295 */         buffer.add(e);
/* 296 */         added++;
/*     */       } 
/*     */     } 
/* 299 */     return added;
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
/*     */   @Beta
/*     */   @CanIgnoreReturnValue
/*     */   @GwtIncompatible
/*     */   public static <E> int drainUninterruptibly(BlockingQueue<E> q, Collection<? super E> buffer, int numElements, long timeout, TimeUnit unit) {
/* 325 */     Preconditions.checkNotNull(buffer);
/* 326 */     long deadline = System.nanoTime() + unit.toNanos(timeout);
/* 327 */     int added = 0;
/* 328 */     boolean interrupted = false;
/*     */     try {
/* 330 */       while (added < numElements) {
/*     */ 
/*     */         
/* 333 */         added += q.drainTo(buffer, numElements - added);
/* 334 */         if (added < numElements) {
/*     */           E e;
/*     */           while (true) {
/*     */             try {
/* 338 */               e = q.poll(deadline - System.nanoTime(), TimeUnit.NANOSECONDS);
/*     */               break;
/* 340 */             } catch (InterruptedException ex) {
/* 341 */               interrupted = true;
/*     */             } 
/*     */           } 
/* 344 */           if (e == null) {
/*     */             break;
/*     */           }
/* 347 */           buffer.add(e);
/* 348 */           added++;
/*     */         } 
/*     */       } 
/*     */     } finally {
/* 352 */       if (interrupted) {
/* 353 */         Thread.currentThread().interrupt();
/*     */       }
/*     */     } 
/* 356 */     return added;
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
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <E> Queue<E> synchronizedQueue(Queue<E> queue) {
/* 389 */     return Synchronized.queue(queue, null);
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
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <E> Deque<E> synchronizedDeque(Deque<E> deque) {
/* 422 */     return Synchronized.deque(deque, null);
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\google\common\collect\Queues.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */