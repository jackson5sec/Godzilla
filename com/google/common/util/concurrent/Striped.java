/*     */ package com.google.common.util.concurrent;
/*     */ 
/*     */ import com.google.common.annotations.Beta;
/*     */ import com.google.common.annotations.GwtIncompatible;
/*     */ import com.google.common.annotations.VisibleForTesting;
/*     */ import com.google.common.base.MoreObjects;
/*     */ import com.google.common.base.Preconditions;
/*     */ import com.google.common.base.Supplier;
/*     */ import com.google.common.collect.ImmutableList;
/*     */ import com.google.common.collect.Iterables;
/*     */ import com.google.common.collect.MapMaker;
/*     */ import com.google.common.math.IntMath;
/*     */ import java.lang.ref.Reference;
/*     */ import java.lang.ref.ReferenceQueue;
/*     */ import java.lang.ref.WeakReference;
/*     */ import java.math.RoundingMode;
/*     */ import java.util.Arrays;
/*     */ import java.util.Collections;
/*     */ import java.util.List;
/*     */ import java.util.concurrent.ConcurrentMap;
/*     */ import java.util.concurrent.Semaphore;
/*     */ import java.util.concurrent.atomic.AtomicReferenceArray;
/*     */ import java.util.concurrent.locks.Condition;
/*     */ import java.util.concurrent.locks.Lock;
/*     */ import java.util.concurrent.locks.ReadWriteLock;
/*     */ import java.util.concurrent.locks.ReentrantLock;
/*     */ import java.util.concurrent.locks.ReentrantReadWriteLock;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
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
/*     */ public abstract class Striped<L>
/*     */ {
/*     */   private static final int LARGE_LAZY_CUTOFF = 1024;
/*     */   
/*     */   private Striped() {}
/*     */   
/*     */   public Iterable<L> bulkGet(Iterable<?> keys) {
/* 141 */     Object[] array = Iterables.toArray(keys, Object.class);
/* 142 */     if (array.length == 0) {
/* 143 */       return (Iterable<L>)ImmutableList.of();
/*     */     }
/* 145 */     int[] stripes = new int[array.length];
/* 146 */     for (int i = 0; i < array.length; i++) {
/* 147 */       stripes[i] = indexFor(array[i]);
/*     */     }
/* 149 */     Arrays.sort(stripes);
/*     */     
/* 151 */     int previousStripe = stripes[0];
/* 152 */     array[0] = getAt(previousStripe);
/* 153 */     for (int j = 1; j < array.length; j++) {
/* 154 */       int currentStripe = stripes[j];
/* 155 */       if (currentStripe == previousStripe) {
/* 156 */         array[j] = array[j - 1];
/*     */       } else {
/* 158 */         array[j] = getAt(currentStripe);
/* 159 */         previousStripe = currentStripe;
/*     */       } 
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 180 */     List<L> asList = Arrays.asList((L[])array);
/* 181 */     return Collections.unmodifiableList(asList);
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
/*     */   static <L> Striped<L> custom(int stripes, Supplier<L> supplier) {
/* 195 */     return new CompactStriped<>(stripes, supplier);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Striped<Lock> lock(int stripes) {
/* 206 */     return custom(stripes, new Supplier<Lock>()
/*     */         {
/*     */           
/*     */           public Lock get()
/*     */           {
/* 211 */             return new Striped.PaddedLock();
/*     */           }
/*     */         });
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Striped<Lock> lazyWeakLock(int stripes) {
/* 224 */     return lazy(stripes, new Supplier<Lock>()
/*     */         {
/*     */           
/*     */           public Lock get()
/*     */           {
/* 229 */             return new ReentrantLock(false);
/*     */           }
/*     */         });
/*     */   }
/*     */   
/*     */   private static <L> Striped<L> lazy(int stripes, Supplier<L> supplier) {
/* 235 */     return (stripes < 1024) ? new SmallLazyStriped<>(stripes, supplier) : new LargeLazyStriped<>(stripes, supplier);
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
/*     */   public static Striped<Semaphore> semaphore(int stripes, final int permits) {
/* 249 */     return custom(stripes, new Supplier<Semaphore>()
/*     */         {
/*     */           
/*     */           public Semaphore get()
/*     */           {
/* 254 */             return new Striped.PaddedSemaphore(permits);
/*     */           }
/*     */         });
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
/*     */   public static Striped<Semaphore> lazyWeakSemaphore(int stripes, final int permits) {
/* 268 */     return lazy(stripes, new Supplier<Semaphore>()
/*     */         {
/*     */           
/*     */           public Semaphore get()
/*     */           {
/* 273 */             return new Semaphore(permits, false);
/*     */           }
/*     */         });
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Striped<ReadWriteLock> readWriteLock(int stripes) {
/* 286 */     return custom(stripes, READ_WRITE_LOCK_SUPPLIER);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Striped<ReadWriteLock> lazyWeakReadWriteLock(int stripes) {
/* 297 */     return lazy(stripes, WEAK_SAFE_READ_WRITE_LOCK_SUPPLIER);
/*     */   }
/*     */   
/* 300 */   private static final Supplier<ReadWriteLock> READ_WRITE_LOCK_SUPPLIER = new Supplier<ReadWriteLock>()
/*     */     {
/*     */       public ReadWriteLock get()
/*     */       {
/* 304 */         return new ReentrantReadWriteLock();
/*     */       }
/*     */     };
/*     */   
/* 308 */   private static final Supplier<ReadWriteLock> WEAK_SAFE_READ_WRITE_LOCK_SUPPLIER = new Supplier<ReadWriteLock>()
/*     */     {
/*     */       public ReadWriteLock get()
/*     */       {
/* 312 */         return new Striped.WeakSafeReadWriteLock();
/*     */       }
/*     */     };
/*     */ 
/*     */ 
/*     */   
/*     */   private static final int ALL_SET = -1;
/*     */ 
/*     */ 
/*     */   
/*     */   private static final class WeakSafeReadWriteLock
/*     */     implements ReadWriteLock
/*     */   {
/* 325 */     private final ReadWriteLock delegate = new ReentrantReadWriteLock();
/*     */ 
/*     */ 
/*     */     
/*     */     public Lock readLock() {
/* 330 */       return new Striped.WeakSafeLock(this.delegate.readLock(), this);
/*     */     }
/*     */ 
/*     */     
/*     */     public Lock writeLock() {
/* 335 */       return new Striped.WeakSafeLock(this.delegate.writeLock(), this);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   private static final class WeakSafeLock
/*     */     extends ForwardingLock
/*     */   {
/*     */     private final Lock delegate;
/*     */     private final Striped.WeakSafeReadWriteLock strongReference;
/*     */     
/*     */     WeakSafeLock(Lock delegate, Striped.WeakSafeReadWriteLock strongReference) {
/* 347 */       this.delegate = delegate;
/* 348 */       this.strongReference = strongReference;
/*     */     }
/*     */ 
/*     */     
/*     */     Lock delegate() {
/* 353 */       return this.delegate;
/*     */     }
/*     */ 
/*     */     
/*     */     public Condition newCondition() {
/* 358 */       return new Striped.WeakSafeCondition(this.delegate.newCondition(), this.strongReference);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   private static final class WeakSafeCondition
/*     */     extends ForwardingCondition
/*     */   {
/*     */     private final Condition delegate;
/*     */     private final Striped.WeakSafeReadWriteLock strongReference;
/*     */     
/*     */     WeakSafeCondition(Condition delegate, Striped.WeakSafeReadWriteLock strongReference) {
/* 370 */       this.delegate = delegate;
/* 371 */       this.strongReference = strongReference;
/*     */     }
/*     */ 
/*     */     
/*     */     Condition delegate() {
/* 376 */       return this.delegate;
/*     */     }
/*     */   }
/*     */   
/*     */   private static abstract class PowerOfTwoStriped<L>
/*     */     extends Striped<L> {
/*     */     final int mask;
/*     */     
/*     */     PowerOfTwoStriped(int stripes) {
/* 385 */       Preconditions.checkArgument((stripes > 0), "Stripes must be positive");
/* 386 */       this.mask = (stripes > 1073741824) ? -1 : (Striped.ceilToPowerOfTwo(stripes) - 1);
/*     */     }
/*     */ 
/*     */     
/*     */     final int indexFor(Object key) {
/* 391 */       int hash = Striped.smear(key.hashCode());
/* 392 */       return hash & this.mask;
/*     */     }
/*     */ 
/*     */     
/*     */     public final L get(Object key) {
/* 397 */       return getAt(indexFor(key));
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static class CompactStriped<L>
/*     */     extends PowerOfTwoStriped<L>
/*     */   {
/*     */     private final Object[] array;
/*     */ 
/*     */     
/*     */     private CompactStriped(int stripes, Supplier<L> supplier) {
/* 410 */       super(stripes);
/* 411 */       Preconditions.checkArgument((stripes <= 1073741824), "Stripes must be <= 2^30)");
/*     */       
/* 413 */       this.array = new Object[this.mask + 1];
/* 414 */       for (int i = 0; i < this.array.length; i++) {
/* 415 */         this.array[i] = supplier.get();
/*     */       }
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public L getAt(int index) {
/* 422 */       return (L)this.array[index];
/*     */     }
/*     */ 
/*     */     
/*     */     public int size() {
/* 427 */       return this.array.length;
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   @VisibleForTesting
/*     */   static class SmallLazyStriped<L>
/*     */     extends PowerOfTwoStriped<L>
/*     */   {
/*     */     final AtomicReferenceArray<ArrayReference<? extends L>> locks;
/*     */     
/*     */     final Supplier<L> supplier;
/*     */     
/*     */     final int size;
/* 441 */     final ReferenceQueue<L> queue = new ReferenceQueue<>();
/*     */     
/*     */     SmallLazyStriped(int stripes, Supplier<L> supplier) {
/* 444 */       super(stripes);
/* 445 */       this.size = (this.mask == -1) ? Integer.MAX_VALUE : (this.mask + 1);
/* 446 */       this.locks = new AtomicReferenceArray<>(this.size);
/* 447 */       this.supplier = supplier;
/*     */     }
/*     */ 
/*     */     
/*     */     public L getAt(int index) {
/* 452 */       if (this.size != Integer.MAX_VALUE) {
/* 453 */         Preconditions.checkElementIndex(index, size());
/*     */       }
/* 455 */       ArrayReference<? extends L> existingRef = this.locks.get(index);
/* 456 */       L existing = (existingRef == null) ? null : existingRef.get();
/* 457 */       if (existing != null) {
/* 458 */         return existing;
/*     */       }
/* 460 */       L created = (L)this.supplier.get();
/* 461 */       ArrayReference<L> newRef = new ArrayReference<>(created, index, this.queue);
/* 462 */       while (!this.locks.compareAndSet(index, existingRef, newRef)) {
/*     */         
/* 464 */         existingRef = this.locks.get(index);
/* 465 */         existing = (existingRef == null) ? null : existingRef.get();
/* 466 */         if (existing != null) {
/* 467 */           return existing;
/*     */         }
/*     */       } 
/* 470 */       drainQueue();
/* 471 */       return created;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     private void drainQueue() {
/*     */       Reference<? extends L> ref;
/* 479 */       while ((ref = this.queue.poll()) != null) {
/*     */         
/* 481 */         ArrayReference<? extends L> arrayRef = (ArrayReference<? extends L>)ref;
/*     */ 
/*     */         
/* 484 */         this.locks.compareAndSet(arrayRef.index, arrayRef, null);
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public int size() {
/* 490 */       return this.size;
/*     */     }
/*     */     
/*     */     private static final class ArrayReference<L> extends WeakReference<L> {
/*     */       final int index;
/*     */       
/*     */       ArrayReference(L referent, int index, ReferenceQueue<L> queue) {
/* 497 */         super(referent, queue);
/* 498 */         this.index = index;
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   @VisibleForTesting
/*     */   static class LargeLazyStriped<L>
/*     */     extends PowerOfTwoStriped<L>
/*     */   {
/*     */     final ConcurrentMap<Integer, L> locks;
/*     */     
/*     */     final Supplier<L> supplier;
/*     */     
/*     */     final int size;
/*     */     
/*     */     LargeLazyStriped(int stripes, Supplier<L> supplier) {
/* 515 */       super(stripes);
/* 516 */       this.size = (this.mask == -1) ? Integer.MAX_VALUE : (this.mask + 1);
/* 517 */       this.supplier = supplier;
/* 518 */       this.locks = (new MapMaker()).weakValues().makeMap();
/*     */     }
/*     */ 
/*     */     
/*     */     public L getAt(int index) {
/* 523 */       if (this.size != Integer.MAX_VALUE) {
/* 524 */         Preconditions.checkElementIndex(index, size());
/*     */       }
/* 526 */       L existing = this.locks.get(Integer.valueOf(index));
/* 527 */       if (existing != null) {
/* 528 */         return existing;
/*     */       }
/* 530 */       L created = (L)this.supplier.get();
/* 531 */       existing = this.locks.putIfAbsent(Integer.valueOf(index), created);
/* 532 */       return (L)MoreObjects.firstNonNull(existing, created);
/*     */     }
/*     */ 
/*     */     
/*     */     public int size() {
/* 537 */       return this.size;
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static int ceilToPowerOfTwo(int x) {
/* 545 */     return 1 << IntMath.log2(x, RoundingMode.CEILING);
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
/*     */   private static int smear(int hashCode) {
/* 558 */     hashCode ^= hashCode >>> 20 ^ hashCode >>> 12;
/* 559 */     return hashCode ^ hashCode >>> 7 ^ hashCode >>> 4;
/*     */   }
/*     */   
/*     */   public abstract L get(Object paramObject);
/*     */   
/*     */   public abstract L getAt(int paramInt);
/*     */   
/*     */   abstract int indexFor(Object paramObject);
/*     */   
/*     */   public abstract int size();
/*     */   
/*     */   private static class PaddedLock extends ReentrantLock { long unused1;
/*     */     
/*     */     PaddedLock() {
/* 573 */       super(false);
/*     */     }
/*     */     
/*     */     long unused2;
/*     */     long unused3; }
/*     */   
/*     */   private static class PaddedSemaphore extends Semaphore { long unused1;
/*     */     long unused2;
/*     */     long unused3;
/*     */     
/*     */     PaddedSemaphore(int permits) {
/* 584 */       super(permits, false);
/*     */     } }
/*     */ 
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\google\commo\\util\concurrent\Striped.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */