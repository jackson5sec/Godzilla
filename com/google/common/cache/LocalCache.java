/*      */ package com.google.common.cache;
/*      */ 
/*      */ import com.google.common.annotations.GwtCompatible;
/*      */ import com.google.common.annotations.GwtIncompatible;
/*      */ import com.google.common.annotations.VisibleForTesting;
/*      */ import com.google.common.base.Equivalence;
/*      */ import com.google.common.base.Function;
/*      */ import com.google.common.base.Preconditions;
/*      */ import com.google.common.base.Stopwatch;
/*      */ import com.google.common.base.Ticker;
/*      */ import com.google.common.collect.AbstractSequentialIterator;
/*      */ import com.google.common.collect.ImmutableMap;
/*      */ import com.google.common.collect.ImmutableSet;
/*      */ import com.google.common.collect.Iterators;
/*      */ import com.google.common.collect.Maps;
/*      */ import com.google.common.collect.Sets;
/*      */ import com.google.common.primitives.Ints;
/*      */ import com.google.common.util.concurrent.ExecutionError;
/*      */ import com.google.common.util.concurrent.Futures;
/*      */ import com.google.common.util.concurrent.ListenableFuture;
/*      */ import com.google.common.util.concurrent.MoreExecutors;
/*      */ import com.google.common.util.concurrent.SettableFuture;
/*      */ import com.google.common.util.concurrent.UncheckedExecutionException;
/*      */ import com.google.common.util.concurrent.Uninterruptibles;
/*      */ import com.google.errorprone.annotations.concurrent.GuardedBy;
/*      */ import com.google.j2objc.annotations.Weak;
/*      */ import java.io.IOException;
/*      */ import java.io.ObjectInputStream;
/*      */ import java.io.Serializable;
/*      */ import java.lang.ref.Reference;
/*      */ import java.lang.ref.ReferenceQueue;
/*      */ import java.lang.ref.SoftReference;
/*      */ import java.lang.ref.WeakReference;
/*      */ import java.util.AbstractCollection;
/*      */ import java.util.AbstractMap;
/*      */ import java.util.AbstractQueue;
/*      */ import java.util.AbstractSet;
/*      */ import java.util.ArrayList;
/*      */ import java.util.Collection;
/*      */ import java.util.Iterator;
/*      */ import java.util.Map;
/*      */ import java.util.NoSuchElementException;
/*      */ import java.util.Queue;
/*      */ import java.util.Set;
/*      */ import java.util.concurrent.Callable;
/*      */ import java.util.concurrent.ConcurrentLinkedQueue;
/*      */ import java.util.concurrent.ConcurrentMap;
/*      */ import java.util.concurrent.ExecutionException;
/*      */ import java.util.concurrent.Future;
/*      */ import java.util.concurrent.TimeUnit;
/*      */ import java.util.concurrent.atomic.AtomicInteger;
/*      */ import java.util.concurrent.atomic.AtomicReferenceArray;
/*      */ import java.util.concurrent.locks.ReentrantLock;
/*      */ import java.util.function.BiFunction;
/*      */ import java.util.function.BiPredicate;
/*      */ import java.util.function.Function;
/*      */ import java.util.function.Predicate;
/*      */ import java.util.logging.Level;
/*      */ import java.util.logging.Logger;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ @GwtCompatible(emulated = true)
/*      */ class LocalCache<K, V>
/*      */   extends AbstractMap<K, V>
/*      */   implements ConcurrentMap<K, V>
/*      */ {
/*      */   static final int MAXIMUM_CAPACITY = 1073741824;
/*      */   static final int MAX_SEGMENTS = 65536;
/*      */   static final int CONTAINS_VALUE_RETRIES = 3;
/*      */   static final int DRAIN_THRESHOLD = 63;
/*      */   static final int DRAIN_MAX = 16;
/*  164 */   static final Logger logger = Logger.getLogger(LocalCache.class.getName());
/*      */ 
/*      */ 
/*      */   
/*      */   final int segmentMask;
/*      */ 
/*      */ 
/*      */   
/*      */   final int segmentShift;
/*      */ 
/*      */ 
/*      */   
/*      */   final Segment<K, V>[] segments;
/*      */ 
/*      */ 
/*      */   
/*      */   final int concurrencyLevel;
/*      */ 
/*      */ 
/*      */   
/*      */   final Equivalence<Object> keyEquivalence;
/*      */ 
/*      */ 
/*      */   
/*      */   final Equivalence<Object> valueEquivalence;
/*      */ 
/*      */ 
/*      */   
/*      */   final Strength keyStrength;
/*      */ 
/*      */ 
/*      */   
/*      */   final Strength valueStrength;
/*      */ 
/*      */ 
/*      */   
/*      */   final long maxWeight;
/*      */ 
/*      */ 
/*      */   
/*      */   final Weigher<K, V> weigher;
/*      */ 
/*      */ 
/*      */   
/*      */   final long expireAfterAccessNanos;
/*      */ 
/*      */ 
/*      */   
/*      */   final long expireAfterWriteNanos;
/*      */ 
/*      */ 
/*      */   
/*      */   final long refreshNanos;
/*      */ 
/*      */ 
/*      */   
/*      */   final Queue<RemovalNotification<K, V>> removalNotificationQueue;
/*      */ 
/*      */ 
/*      */   
/*      */   final RemovalListener<K, V> removalListener;
/*      */ 
/*      */   
/*      */   final Ticker ticker;
/*      */ 
/*      */   
/*      */   final EntryFactory entryFactory;
/*      */ 
/*      */   
/*      */   final AbstractCache.StatsCounter globalStatsCounter;
/*      */ 
/*      */   
/*      */   final CacheLoader<? super K, V> defaultLoader;
/*      */ 
/*      */ 
/*      */   
/*      */   LocalCache(CacheBuilder<? super K, ? super V> builder, CacheLoader<? super K, V> loader) {
/*  241 */     this.concurrencyLevel = Math.min(builder.getConcurrencyLevel(), 65536);
/*      */     
/*  243 */     this.keyStrength = builder.getKeyStrength();
/*  244 */     this.valueStrength = builder.getValueStrength();
/*      */     
/*  246 */     this.keyEquivalence = builder.getKeyEquivalence();
/*  247 */     this.valueEquivalence = builder.getValueEquivalence();
/*      */     
/*  249 */     this.maxWeight = builder.getMaximumWeight();
/*  250 */     this.weigher = builder.getWeigher();
/*  251 */     this.expireAfterAccessNanos = builder.getExpireAfterAccessNanos();
/*  252 */     this.expireAfterWriteNanos = builder.getExpireAfterWriteNanos();
/*  253 */     this.refreshNanos = builder.getRefreshNanos();
/*      */     
/*  255 */     this.removalListener = builder.getRemovalListener();
/*  256 */     this
/*      */       
/*  258 */       .removalNotificationQueue = (this.removalListener == CacheBuilder.NullListener.INSTANCE) ? discardingQueue() : new ConcurrentLinkedQueue<>();
/*      */ 
/*      */     
/*  261 */     this.ticker = builder.getTicker(recordsTime());
/*  262 */     this.entryFactory = EntryFactory.getFactory(this.keyStrength, usesAccessEntries(), usesWriteEntries());
/*  263 */     this.globalStatsCounter = (AbstractCache.StatsCounter)builder.getStatsCounterSupplier().get();
/*  264 */     this.defaultLoader = loader;
/*      */     
/*  266 */     int initialCapacity = Math.min(builder.getInitialCapacity(), 1073741824);
/*  267 */     if (evictsBySize() && !customWeigher()) {
/*  268 */       initialCapacity = (int)Math.min(initialCapacity, this.maxWeight);
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  276 */     int segmentShift = 0;
/*  277 */     int segmentCount = 1;
/*  278 */     while (segmentCount < this.concurrencyLevel && (!evictsBySize() || (segmentCount * 20) <= this.maxWeight)) {
/*  279 */       segmentShift++;
/*  280 */       segmentCount <<= 1;
/*      */     } 
/*  282 */     this.segmentShift = 32 - segmentShift;
/*  283 */     this.segmentMask = segmentCount - 1;
/*      */     
/*  285 */     this.segments = newSegmentArray(segmentCount);
/*      */     
/*  287 */     int segmentCapacity = initialCapacity / segmentCount;
/*  288 */     if (segmentCapacity * segmentCount < initialCapacity) {
/*  289 */       segmentCapacity++;
/*      */     }
/*      */     
/*  292 */     int segmentSize = 1;
/*  293 */     while (segmentSize < segmentCapacity) {
/*  294 */       segmentSize <<= 1;
/*      */     }
/*      */     
/*  297 */     if (evictsBySize()) {
/*      */       
/*  299 */       long maxSegmentWeight = this.maxWeight / segmentCount + 1L;
/*  300 */       long remainder = this.maxWeight % segmentCount;
/*  301 */       for (int i = 0; i < this.segments.length; i++) {
/*  302 */         if (i == remainder) {
/*  303 */           maxSegmentWeight--;
/*      */         }
/*  305 */         this.segments[i] = 
/*  306 */           createSegment(segmentSize, maxSegmentWeight, (AbstractCache.StatsCounter)builder.getStatsCounterSupplier().get());
/*      */       } 
/*      */     } else {
/*  309 */       for (int i = 0; i < this.segments.length; i++) {
/*  310 */         this.segments[i] = 
/*  311 */           createSegment(segmentSize, -1L, (AbstractCache.StatsCounter)builder.getStatsCounterSupplier().get());
/*      */       }
/*      */     } 
/*      */   }
/*      */   
/*      */   boolean evictsBySize() {
/*  317 */     return (this.maxWeight >= 0L);
/*      */   }
/*      */   
/*      */   boolean customWeigher() {
/*  321 */     return (this.weigher != CacheBuilder.OneWeigher.INSTANCE);
/*      */   }
/*      */   
/*      */   boolean expires() {
/*  325 */     return (expiresAfterWrite() || expiresAfterAccess());
/*      */   }
/*      */   
/*      */   boolean expiresAfterWrite() {
/*  329 */     return (this.expireAfterWriteNanos > 0L);
/*      */   }
/*      */   
/*      */   boolean expiresAfterAccess() {
/*  333 */     return (this.expireAfterAccessNanos > 0L);
/*      */   }
/*      */   
/*      */   boolean refreshes() {
/*  337 */     return (this.refreshNanos > 0L);
/*      */   }
/*      */   
/*      */   boolean usesAccessQueue() {
/*  341 */     return (expiresAfterAccess() || evictsBySize());
/*      */   }
/*      */   
/*      */   boolean usesWriteQueue() {
/*  345 */     return expiresAfterWrite();
/*      */   }
/*      */   
/*      */   boolean recordsWrite() {
/*  349 */     return (expiresAfterWrite() || refreshes());
/*      */   }
/*      */   
/*      */   boolean recordsAccess() {
/*  353 */     return expiresAfterAccess();
/*      */   }
/*      */   
/*      */   boolean recordsTime() {
/*  357 */     return (recordsWrite() || recordsAccess());
/*      */   }
/*      */   
/*      */   boolean usesWriteEntries() {
/*  361 */     return (usesWriteQueue() || recordsWrite());
/*      */   }
/*      */   
/*      */   boolean usesAccessEntries() {
/*  365 */     return (usesAccessQueue() || recordsAccess());
/*      */   }
/*      */   
/*      */   boolean usesKeyReferences() {
/*  369 */     return (this.keyStrength != Strength.STRONG);
/*      */   }
/*      */   
/*      */   boolean usesValueReferences() {
/*  373 */     return (this.valueStrength != Strength.STRONG);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   enum Strength
/*      */   {
/*  382 */     STRONG
/*      */     {
/*      */       <K, V> LocalCache.ValueReference<K, V> referenceValue(LocalCache.Segment<K, V> segment, ReferenceEntry<K, V> entry, V value, int weight)
/*      */       {
/*  386 */         return (weight == 1) ? new LocalCache.StrongValueReference<>(value) : new LocalCache.WeightedStrongValueReference<>(value, weight);
/*      */       }
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*      */       Equivalence<Object> defaultEquivalence() {
/*  393 */         return Equivalence.equals();
/*      */       }
/*      */     },
/*  396 */     SOFT
/*      */     {
/*      */       <K, V> LocalCache.ValueReference<K, V> referenceValue(LocalCache.Segment<K, V> segment, ReferenceEntry<K, V> entry, V value, int weight)
/*      */       {
/*  400 */         return (weight == 1) ? new LocalCache.SoftValueReference<>(segment.valueReferenceQueue, value, entry) : new LocalCache.WeightedSoftValueReference<>(segment.valueReferenceQueue, value, entry, weight);
/*      */       }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*      */       Equivalence<Object> defaultEquivalence() {
/*  408 */         return Equivalence.identity();
/*      */       }
/*      */     },
/*  411 */     WEAK
/*      */     {
/*      */       <K, V> LocalCache.ValueReference<K, V> referenceValue(LocalCache.Segment<K, V> segment, ReferenceEntry<K, V> entry, V value, int weight)
/*      */       {
/*  415 */         return (weight == 1) ? new LocalCache.WeakValueReference<>(segment.valueReferenceQueue, value, entry) : new LocalCache.WeightedWeakValueReference<>(segment.valueReferenceQueue, value, entry, weight);
/*      */       }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*      */       Equivalence<Object> defaultEquivalence() {
/*  423 */         return Equivalence.identity();
/*      */       }
/*      */     };
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     abstract <K, V> LocalCache.ValueReference<K, V> referenceValue(LocalCache.Segment<K, V> param1Segment, ReferenceEntry<K, V> param1ReferenceEntry, V param1V, int param1Int);
/*      */ 
/*      */ 
/*      */     
/*      */     abstract Equivalence<Object> defaultEquivalence();
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   enum EntryFactory
/*      */   {
/*  441 */     STRONG
/*      */     {
/*      */       <K, V> ReferenceEntry<K, V> newEntry(LocalCache.Segment<K, V> segment, K key, int hash, ReferenceEntry<K, V> next)
/*      */       {
/*  445 */         return new LocalCache.StrongEntry<>(key, hash, next);
/*      */       }
/*      */     },
/*  448 */     STRONG_ACCESS
/*      */     {
/*      */       <K, V> ReferenceEntry<K, V> newEntry(LocalCache.Segment<K, V> segment, K key, int hash, ReferenceEntry<K, V> next)
/*      */       {
/*  452 */         return new LocalCache.StrongAccessEntry<>(key, hash, next);
/*      */       }
/*      */ 
/*      */ 
/*      */       
/*      */       <K, V> ReferenceEntry<K, V> copyEntry(LocalCache.Segment<K, V> segment, ReferenceEntry<K, V> original, ReferenceEntry<K, V> newNext) {
/*  458 */         ReferenceEntry<K, V> newEntry = super.copyEntry(segment, original, newNext);
/*  459 */         copyAccessEntry(original, newEntry);
/*  460 */         return newEntry;
/*      */       }
/*      */     },
/*  463 */     STRONG_WRITE
/*      */     {
/*      */       <K, V> ReferenceEntry<K, V> newEntry(LocalCache.Segment<K, V> segment, K key, int hash, ReferenceEntry<K, V> next)
/*      */       {
/*  467 */         return new LocalCache.StrongWriteEntry<>(key, hash, next);
/*      */       }
/*      */ 
/*      */ 
/*      */       
/*      */       <K, V> ReferenceEntry<K, V> copyEntry(LocalCache.Segment<K, V> segment, ReferenceEntry<K, V> original, ReferenceEntry<K, V> newNext) {
/*  473 */         ReferenceEntry<K, V> newEntry = super.copyEntry(segment, original, newNext);
/*  474 */         copyWriteEntry(original, newEntry);
/*  475 */         return newEntry;
/*      */       }
/*      */     },
/*  478 */     STRONG_ACCESS_WRITE
/*      */     {
/*      */       <K, V> ReferenceEntry<K, V> newEntry(LocalCache.Segment<K, V> segment, K key, int hash, ReferenceEntry<K, V> next)
/*      */       {
/*  482 */         return new LocalCache.StrongAccessWriteEntry<>(key, hash, next);
/*      */       }
/*      */ 
/*      */ 
/*      */       
/*      */       <K, V> ReferenceEntry<K, V> copyEntry(LocalCache.Segment<K, V> segment, ReferenceEntry<K, V> original, ReferenceEntry<K, V> newNext) {
/*  488 */         ReferenceEntry<K, V> newEntry = super.copyEntry(segment, original, newNext);
/*  489 */         copyAccessEntry(original, newEntry);
/*  490 */         copyWriteEntry(original, newEntry);
/*  491 */         return newEntry;
/*      */       }
/*      */     },
/*  494 */     WEAK
/*      */     {
/*      */       <K, V> ReferenceEntry<K, V> newEntry(LocalCache.Segment<K, V> segment, K key, int hash, ReferenceEntry<K, V> next)
/*      */       {
/*  498 */         return new LocalCache.WeakEntry<>(segment.keyReferenceQueue, key, hash, next);
/*      */       }
/*      */     },
/*  501 */     WEAK_ACCESS
/*      */     {
/*      */       <K, V> ReferenceEntry<K, V> newEntry(LocalCache.Segment<K, V> segment, K key, int hash, ReferenceEntry<K, V> next)
/*      */       {
/*  505 */         return new LocalCache.WeakAccessEntry<>(segment.keyReferenceQueue, key, hash, next);
/*      */       }
/*      */ 
/*      */ 
/*      */       
/*      */       <K, V> ReferenceEntry<K, V> copyEntry(LocalCache.Segment<K, V> segment, ReferenceEntry<K, V> original, ReferenceEntry<K, V> newNext) {
/*  511 */         ReferenceEntry<K, V> newEntry = super.copyEntry(segment, original, newNext);
/*  512 */         copyAccessEntry(original, newEntry);
/*  513 */         return newEntry;
/*      */       }
/*      */     },
/*  516 */     WEAK_WRITE
/*      */     {
/*      */       <K, V> ReferenceEntry<K, V> newEntry(LocalCache.Segment<K, V> segment, K key, int hash, ReferenceEntry<K, V> next)
/*      */       {
/*  520 */         return new LocalCache.WeakWriteEntry<>(segment.keyReferenceQueue, key, hash, next);
/*      */       }
/*      */ 
/*      */ 
/*      */       
/*      */       <K, V> ReferenceEntry<K, V> copyEntry(LocalCache.Segment<K, V> segment, ReferenceEntry<K, V> original, ReferenceEntry<K, V> newNext) {
/*  526 */         ReferenceEntry<K, V> newEntry = super.copyEntry(segment, original, newNext);
/*  527 */         copyWriteEntry(original, newEntry);
/*  528 */         return newEntry;
/*      */       }
/*      */     },
/*  531 */     WEAK_ACCESS_WRITE
/*      */     {
/*      */       <K, V> ReferenceEntry<K, V> newEntry(LocalCache.Segment<K, V> segment, K key, int hash, ReferenceEntry<K, V> next)
/*      */       {
/*  535 */         return new LocalCache.WeakAccessWriteEntry<>(segment.keyReferenceQueue, key, hash, next);
/*      */       }
/*      */ 
/*      */ 
/*      */       
/*      */       <K, V> ReferenceEntry<K, V> copyEntry(LocalCache.Segment<K, V> segment, ReferenceEntry<K, V> original, ReferenceEntry<K, V> newNext) {
/*  541 */         ReferenceEntry<K, V> newEntry = super.copyEntry(segment, original, newNext);
/*  542 */         copyAccessEntry(original, newEntry);
/*  543 */         copyWriteEntry(original, newEntry);
/*  544 */         return newEntry;
/*      */       }
/*      */     };
/*      */ 
/*      */     
/*      */     static final int ACCESS_MASK = 1;
/*      */     
/*      */     static final int WRITE_MASK = 2;
/*      */     
/*      */     static final int WEAK_MASK = 4;
/*      */     
/*  555 */     static final EntryFactory[] factories = new EntryFactory[] { STRONG, STRONG_ACCESS, STRONG_WRITE, STRONG_ACCESS_WRITE, WEAK, WEAK_ACCESS, WEAK_WRITE, WEAK_ACCESS_WRITE };
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     static {
/*      */     
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     static EntryFactory getFactory(LocalCache.Strength keyStrength, boolean usesAccessQueue, boolean usesWriteQueue) {
/*  568 */       int flags = ((keyStrength == LocalCache.Strength.WEAK) ? 4 : 0) | (usesAccessQueue ? 1 : 0) | (usesWriteQueue ? 2 : 0);
/*      */ 
/*      */ 
/*      */       
/*  572 */       return factories[flags];
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     <K, V> ReferenceEntry<K, V> copyEntry(LocalCache.Segment<K, V> segment, ReferenceEntry<K, V> original, ReferenceEntry<K, V> newNext) {
/*  595 */       return newEntry(segment, original.getKey(), original.getHash(), newNext);
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     <K, V> void copyAccessEntry(ReferenceEntry<K, V> original, ReferenceEntry<K, V> newEntry) {
/*  602 */       newEntry.setAccessTime(original.getAccessTime());
/*      */       
/*  604 */       LocalCache.connectAccessOrder(original.getPreviousInAccessQueue(), newEntry);
/*  605 */       LocalCache.connectAccessOrder(newEntry, original.getNextInAccessQueue());
/*      */       
/*  607 */       LocalCache.nullifyAccessOrder(original);
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     <K, V> void copyWriteEntry(ReferenceEntry<K, V> original, ReferenceEntry<K, V> newEntry) {
/*  614 */       newEntry.setWriteTime(original.getWriteTime());
/*      */       
/*  616 */       LocalCache.connectWriteOrder(original.getPreviousInWriteQueue(), newEntry);
/*  617 */       LocalCache.connectWriteOrder(newEntry, original.getNextInWriteQueue());
/*      */       
/*  619 */       LocalCache.nullifyWriteOrder(original);
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     abstract <K, V> ReferenceEntry<K, V> newEntry(LocalCache.Segment<K, V> param1Segment, K param1K, int param1Int, ReferenceEntry<K, V> param1ReferenceEntry);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  680 */   static final ValueReference<Object, Object> UNSET = new ValueReference<Object, Object>()
/*      */     {
/*      */       public Object get()
/*      */       {
/*  684 */         return null;
/*      */       }
/*      */ 
/*      */       
/*      */       public int getWeight() {
/*  689 */         return 0;
/*      */       }
/*      */ 
/*      */       
/*      */       public ReferenceEntry<Object, Object> getEntry() {
/*  694 */         return null;
/*      */       }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*      */       public LocalCache.ValueReference<Object, Object> copyFor(ReferenceQueue<Object> queue, Object value, ReferenceEntry<Object, Object> entry) {
/*  702 */         return this;
/*      */       }
/*      */ 
/*      */       
/*      */       public boolean isLoading() {
/*  707 */         return false;
/*      */       }
/*      */ 
/*      */       
/*      */       public boolean isActive() {
/*  712 */         return false;
/*      */       }
/*      */ 
/*      */       
/*      */       public Object waitForValue() {
/*  717 */         return null;
/*      */       }
/*      */ 
/*      */ 
/*      */       
/*      */       public void notifyNewValue(Object newValue) {}
/*      */     };
/*      */ 
/*      */   
/*      */   static <K, V> ValueReference<K, V> unset() {
/*  727 */     return (ValueReference)UNSET;
/*      */   }
/*      */   
/*      */   private enum NullEntry implements ReferenceEntry<Object, Object> {
/*  731 */     INSTANCE;
/*      */ 
/*      */     
/*      */     public LocalCache.ValueReference<Object, Object> getValueReference() {
/*  735 */       return null;
/*      */     }
/*      */ 
/*      */     
/*      */     public void setValueReference(LocalCache.ValueReference<Object, Object> valueReference) {}
/*      */ 
/*      */     
/*      */     public ReferenceEntry<Object, Object> getNext() {
/*  743 */       return null;
/*      */     }
/*      */ 
/*      */     
/*      */     public int getHash() {
/*  748 */       return 0;
/*      */     }
/*      */ 
/*      */     
/*      */     public Object getKey() {
/*  753 */       return null;
/*      */     }
/*      */ 
/*      */     
/*      */     public long getAccessTime() {
/*  758 */       return 0L;
/*      */     }
/*      */ 
/*      */     
/*      */     public void setAccessTime(long time) {}
/*      */ 
/*      */     
/*      */     public ReferenceEntry<Object, Object> getNextInAccessQueue() {
/*  766 */       return this;
/*      */     }
/*      */ 
/*      */     
/*      */     public void setNextInAccessQueue(ReferenceEntry<Object, Object> next) {}
/*      */ 
/*      */     
/*      */     public ReferenceEntry<Object, Object> getPreviousInAccessQueue() {
/*  774 */       return this;
/*      */     }
/*      */ 
/*      */     
/*      */     public void setPreviousInAccessQueue(ReferenceEntry<Object, Object> previous) {}
/*      */ 
/*      */     
/*      */     public long getWriteTime() {
/*  782 */       return 0L;
/*      */     }
/*      */ 
/*      */     
/*      */     public void setWriteTime(long time) {}
/*      */ 
/*      */     
/*      */     public ReferenceEntry<Object, Object> getNextInWriteQueue() {
/*  790 */       return this;
/*      */     }
/*      */ 
/*      */     
/*      */     public void setNextInWriteQueue(ReferenceEntry<Object, Object> next) {}
/*      */ 
/*      */     
/*      */     public ReferenceEntry<Object, Object> getPreviousInWriteQueue() {
/*  798 */       return this;
/*      */     }
/*      */     
/*      */     public void setPreviousInWriteQueue(ReferenceEntry<Object, Object> previous) {}
/*      */   }
/*      */   
/*      */   static abstract class AbstractReferenceEntry<K, V>
/*      */     implements ReferenceEntry<K, V>
/*      */   {
/*      */     public LocalCache.ValueReference<K, V> getValueReference() {
/*  808 */       throw new UnsupportedOperationException();
/*      */     }
/*      */ 
/*      */     
/*      */     public void setValueReference(LocalCache.ValueReference<K, V> valueReference) {
/*  813 */       throw new UnsupportedOperationException();
/*      */     }
/*      */ 
/*      */     
/*      */     public ReferenceEntry<K, V> getNext() {
/*  818 */       throw new UnsupportedOperationException();
/*      */     }
/*      */ 
/*      */     
/*      */     public int getHash() {
/*  823 */       throw new UnsupportedOperationException();
/*      */     }
/*      */ 
/*      */     
/*      */     public K getKey() {
/*  828 */       throw new UnsupportedOperationException();
/*      */     }
/*      */ 
/*      */     
/*      */     public long getAccessTime() {
/*  833 */       throw new UnsupportedOperationException();
/*      */     }
/*      */ 
/*      */     
/*      */     public void setAccessTime(long time) {
/*  838 */       throw new UnsupportedOperationException();
/*      */     }
/*      */ 
/*      */     
/*      */     public ReferenceEntry<K, V> getNextInAccessQueue() {
/*  843 */       throw new UnsupportedOperationException();
/*      */     }
/*      */ 
/*      */     
/*      */     public void setNextInAccessQueue(ReferenceEntry<K, V> next) {
/*  848 */       throw new UnsupportedOperationException();
/*      */     }
/*      */ 
/*      */     
/*      */     public ReferenceEntry<K, V> getPreviousInAccessQueue() {
/*  853 */       throw new UnsupportedOperationException();
/*      */     }
/*      */ 
/*      */     
/*      */     public void setPreviousInAccessQueue(ReferenceEntry<K, V> previous) {
/*  858 */       throw new UnsupportedOperationException();
/*      */     }
/*      */ 
/*      */     
/*      */     public long getWriteTime() {
/*  863 */       throw new UnsupportedOperationException();
/*      */     }
/*      */ 
/*      */     
/*      */     public void setWriteTime(long time) {
/*  868 */       throw new UnsupportedOperationException();
/*      */     }
/*      */ 
/*      */     
/*      */     public ReferenceEntry<K, V> getNextInWriteQueue() {
/*  873 */       throw new UnsupportedOperationException();
/*      */     }
/*      */ 
/*      */     
/*      */     public void setNextInWriteQueue(ReferenceEntry<K, V> next) {
/*  878 */       throw new UnsupportedOperationException();
/*      */     }
/*      */ 
/*      */     
/*      */     public ReferenceEntry<K, V> getPreviousInWriteQueue() {
/*  883 */       throw new UnsupportedOperationException();
/*      */     }
/*      */ 
/*      */     
/*      */     public void setPreviousInWriteQueue(ReferenceEntry<K, V> previous) {
/*  888 */       throw new UnsupportedOperationException();
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   static <K, V> ReferenceEntry<K, V> nullEntry() {
/*  894 */     return NullEntry.INSTANCE;
/*      */   }
/*      */   
/*  897 */   static final Queue<?> DISCARDING_QUEUE = new AbstractQueue()
/*      */     {
/*      */       public boolean offer(Object o)
/*      */       {
/*  901 */         return true;
/*      */       }
/*      */ 
/*      */       
/*      */       public Object peek() {
/*  906 */         return null;
/*      */       }
/*      */ 
/*      */       
/*      */       public Object poll() {
/*  911 */         return null;
/*      */       }
/*      */ 
/*      */       
/*      */       public int size() {
/*  916 */         return 0;
/*      */       }
/*      */ 
/*      */       
/*      */       public Iterator<Object> iterator() {
/*  921 */         return (Iterator<Object>)ImmutableSet.of().iterator();
/*      */       }
/*      */     };
/*      */   Set<K> keySet; Collection<V> values;
/*      */   Set<Map.Entry<K, V>> entrySet;
/*      */   
/*      */   static <E> Queue<E> discardingQueue() {
/*  928 */     return (Queue)DISCARDING_QUEUE;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   static class StrongEntry<K, V>
/*      */     extends AbstractReferenceEntry<K, V>
/*      */   {
/*      */     final K key;
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     final int hash;
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     final ReferenceEntry<K, V> next;
/*      */ 
/*      */ 
/*      */     
/*      */     volatile LocalCache.ValueReference<K, V> valueReference;
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     StrongEntry(K key, int hash, ReferenceEntry<K, V> next) {
/*  958 */       this.valueReference = LocalCache.unset();
/*      */       this.key = key;
/*      */       this.hash = hash;
/*      */       this.next = next;
/*  962 */     } public K getKey() { return this.key; } public LocalCache.ValueReference<K, V> getValueReference() { return this.valueReference; }
/*      */ 
/*      */ 
/*      */     
/*      */     public void setValueReference(LocalCache.ValueReference<K, V> valueReference) {
/*  967 */       this.valueReference = valueReference;
/*      */     }
/*      */ 
/*      */     
/*      */     public int getHash() {
/*  972 */       return this.hash;
/*      */     }
/*      */ 
/*      */     
/*      */     public ReferenceEntry<K, V> getNext() {
/*  977 */       return this.next;
/*      */     } }
/*      */   static final class StrongAccessEntry<K, V> extends StrongEntry<K, V> { volatile long accessTime;
/*      */     ReferenceEntry<K, V> nextAccess;
/*      */     ReferenceEntry<K, V> previousAccess;
/*      */     
/*  983 */     StrongAccessEntry(K key, int hash, ReferenceEntry<K, V> next) { super(key, hash, next);
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*  988 */       this.accessTime = Long.MAX_VALUE;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 1001 */       this.nextAccess = LocalCache.nullEntry();
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 1014 */       this.previousAccess = LocalCache.nullEntry(); } public long getAccessTime() { return this.accessTime; }
/*      */     public void setAccessTime(long time) { this.accessTime = time; }
/*      */     public ReferenceEntry<K, V> getNextInAccessQueue() { return this.nextAccess; }
/*      */     public void setNextInAccessQueue(ReferenceEntry<K, V> next) { this.nextAccess = next; }
/* 1018 */     public ReferenceEntry<K, V> getPreviousInAccessQueue() { return this.previousAccess; }
/*      */ 
/*      */ 
/*      */     
/*      */     public void setPreviousInAccessQueue(ReferenceEntry<K, V> previous) {
/* 1023 */       this.previousAccess = previous;
/*      */     } }
/*      */   static final class StrongWriteEntry<K, V> extends StrongEntry<K, V> { volatile long writeTime;
/*      */     ReferenceEntry<K, V> nextWrite;
/*      */     ReferenceEntry<K, V> previousWrite;
/*      */     
/* 1029 */     StrongWriteEntry(K key, int hash, ReferenceEntry<K, V> next) { super(key, hash, next);
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 1034 */       this.writeTime = Long.MAX_VALUE;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 1047 */       this.nextWrite = LocalCache.nullEntry();
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 1060 */       this.previousWrite = LocalCache.nullEntry(); } public long getWriteTime() { return this.writeTime; }
/*      */     public void setWriteTime(long time) { this.writeTime = time; }
/*      */     public ReferenceEntry<K, V> getNextInWriteQueue() { return this.nextWrite; }
/*      */     public void setNextInWriteQueue(ReferenceEntry<K, V> next) { this.nextWrite = next; }
/* 1064 */     public ReferenceEntry<K, V> getPreviousInWriteQueue() { return this.previousWrite; }
/*      */ 
/*      */ 
/*      */     
/*      */     public void setPreviousInWriteQueue(ReferenceEntry<K, V> previous) {
/* 1069 */       this.previousWrite = previous;
/*      */     } }
/*      */   static final class StrongAccessWriteEntry<K, V> extends StrongEntry<K, V> { volatile long accessTime; ReferenceEntry<K, V> nextAccess; ReferenceEntry<K, V> previousAccess; volatile long writeTime;
/*      */     ReferenceEntry<K, V> nextWrite;
/*      */     ReferenceEntry<K, V> previousWrite;
/*      */     
/* 1075 */     StrongAccessWriteEntry(K key, int hash, ReferenceEntry<K, V> next) { super(key, hash, next);
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 1080 */       this.accessTime = Long.MAX_VALUE;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 1093 */       this.nextAccess = LocalCache.nullEntry();
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 1106 */       this.previousAccess = LocalCache.nullEntry();
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 1120 */       this.writeTime = Long.MAX_VALUE;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 1133 */       this.nextWrite = LocalCache.nullEntry();
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 1146 */       this.previousWrite = LocalCache.nullEntry(); } public long getAccessTime() { return this.accessTime; } public void setAccessTime(long time) { this.accessTime = time; } public ReferenceEntry<K, V> getNextInAccessQueue() { return this.nextAccess; } public void setNextInAccessQueue(ReferenceEntry<K, V> next) { this.nextAccess = next; } public ReferenceEntry<K, V> getPreviousInAccessQueue() { return this.previousAccess; } public void setPreviousInAccessQueue(ReferenceEntry<K, V> previous) { this.previousAccess = previous; } public long getWriteTime() { return this.writeTime; }
/*      */     public void setWriteTime(long time) { this.writeTime = time; }
/*      */     public ReferenceEntry<K, V> getNextInWriteQueue() { return this.nextWrite; }
/*      */     public void setNextInWriteQueue(ReferenceEntry<K, V> next) { this.nextWrite = next; }
/* 1150 */     public ReferenceEntry<K, V> getPreviousInWriteQueue() { return this.previousWrite; }
/*      */ 
/*      */ 
/*      */     
/*      */     public void setPreviousInWriteQueue(ReferenceEntry<K, V> previous) {
/* 1155 */       this.previousWrite = previous;
/*      */     } }
/*      */   
/*      */   static class WeakEntry<K, V> extends WeakReference<K> implements ReferenceEntry<K, V> { final int hash;
/*      */     final ReferenceEntry<K, V> next;
/*      */     volatile LocalCache.ValueReference<K, V> valueReference;
/*      */     
/* 1162 */     WeakEntry(ReferenceQueue<K> queue, K key, int hash, ReferenceEntry<K, V> next) { super(key, queue);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 1245 */       this.valueReference = LocalCache.unset(); this.hash = hash; this.next = next; }
/*      */     public K getKey() { return get(); }
/*      */     public long getAccessTime() { throw new UnsupportedOperationException(); }
/*      */     public void setAccessTime(long time) { throw new UnsupportedOperationException(); }
/* 1249 */     public ReferenceEntry<K, V> getNextInAccessQueue() { throw new UnsupportedOperationException(); } public void setNextInAccessQueue(ReferenceEntry<K, V> next) { throw new UnsupportedOperationException(); } public ReferenceEntry<K, V> getPreviousInAccessQueue() { throw new UnsupportedOperationException(); } public LocalCache.ValueReference<K, V> getValueReference() { return this.valueReference; }
/*      */     public void setPreviousInAccessQueue(ReferenceEntry<K, V> previous) { throw new UnsupportedOperationException(); }
/*      */     public long getWriteTime() { throw new UnsupportedOperationException(); }
/*      */     public void setWriteTime(long time) { throw new UnsupportedOperationException(); }
/*      */     public ReferenceEntry<K, V> getNextInWriteQueue() { throw new UnsupportedOperationException(); }
/* 1254 */     public void setNextInWriteQueue(ReferenceEntry<K, V> next) { throw new UnsupportedOperationException(); } public ReferenceEntry<K, V> getPreviousInWriteQueue() { throw new UnsupportedOperationException(); } public void setPreviousInWriteQueue(ReferenceEntry<K, V> previous) { throw new UnsupportedOperationException(); } public void setValueReference(LocalCache.ValueReference<K, V> valueReference) { this.valueReference = valueReference; }
/*      */ 
/*      */ 
/*      */     
/*      */     public int getHash() {
/* 1259 */       return this.hash;
/*      */     }
/*      */ 
/*      */     
/*      */     public ReferenceEntry<K, V> getNext() {
/* 1264 */       return this.next;
/*      */     } }
/*      */   static final class WeakAccessEntry<K, V> extends WeakEntry<K, V> { volatile long accessTime;
/*      */     ReferenceEntry<K, V> nextAccess;
/*      */     ReferenceEntry<K, V> previousAccess;
/*      */     
/* 1270 */     WeakAccessEntry(ReferenceQueue<K> queue, K key, int hash, ReferenceEntry<K, V> next) { super(queue, key, hash, next);
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 1275 */       this.accessTime = Long.MAX_VALUE;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 1288 */       this.nextAccess = LocalCache.nullEntry();
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 1301 */       this.previousAccess = LocalCache.nullEntry(); } public long getAccessTime() { return this.accessTime; }
/*      */     public void setAccessTime(long time) { this.accessTime = time; }
/*      */     public ReferenceEntry<K, V> getNextInAccessQueue() { return this.nextAccess; }
/*      */     public void setNextInAccessQueue(ReferenceEntry<K, V> next) { this.nextAccess = next; }
/* 1305 */     public ReferenceEntry<K, V> getPreviousInAccessQueue() { return this.previousAccess; }
/*      */ 
/*      */ 
/*      */     
/*      */     public void setPreviousInAccessQueue(ReferenceEntry<K, V> previous) {
/* 1310 */       this.previousAccess = previous;
/*      */     } }
/*      */   static final class WeakWriteEntry<K, V> extends WeakEntry<K, V> { volatile long writeTime;
/*      */     ReferenceEntry<K, V> nextWrite;
/*      */     ReferenceEntry<K, V> previousWrite;
/*      */     
/* 1316 */     WeakWriteEntry(ReferenceQueue<K> queue, K key, int hash, ReferenceEntry<K, V> next) { super(queue, key, hash, next);
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 1321 */       this.writeTime = Long.MAX_VALUE;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 1334 */       this.nextWrite = LocalCache.nullEntry();
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 1347 */       this.previousWrite = LocalCache.nullEntry(); } public long getWriteTime() { return this.writeTime; }
/*      */     public void setWriteTime(long time) { this.writeTime = time; }
/*      */     public ReferenceEntry<K, V> getNextInWriteQueue() { return this.nextWrite; }
/*      */     public void setNextInWriteQueue(ReferenceEntry<K, V> next) { this.nextWrite = next; }
/* 1351 */     public ReferenceEntry<K, V> getPreviousInWriteQueue() { return this.previousWrite; }
/*      */ 
/*      */ 
/*      */     
/*      */     public void setPreviousInWriteQueue(ReferenceEntry<K, V> previous) {
/* 1356 */       this.previousWrite = previous;
/*      */     } }
/*      */   static final class WeakAccessWriteEntry<K, V> extends WeakEntry<K, V> { volatile long accessTime; ReferenceEntry<K, V> nextAccess; ReferenceEntry<K, V> previousAccess;
/*      */     volatile long writeTime;
/*      */     ReferenceEntry<K, V> nextWrite;
/*      */     ReferenceEntry<K, V> previousWrite;
/*      */     
/* 1363 */     WeakAccessWriteEntry(ReferenceQueue<K> queue, K key, int hash, ReferenceEntry<K, V> next) { super(queue, key, hash, next);
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 1368 */       this.accessTime = Long.MAX_VALUE;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 1381 */       this.nextAccess = LocalCache.nullEntry();
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 1394 */       this.previousAccess = LocalCache.nullEntry();
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 1408 */       this.writeTime = Long.MAX_VALUE;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 1421 */       this.nextWrite = LocalCache.nullEntry();
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 1434 */       this.previousWrite = LocalCache.nullEntry(); } public long getAccessTime() { return this.accessTime; } public void setAccessTime(long time) { this.accessTime = time; } public ReferenceEntry<K, V> getNextInAccessQueue() { return this.nextAccess; } public void setNextInAccessQueue(ReferenceEntry<K, V> next) { this.nextAccess = next; } public ReferenceEntry<K, V> getPreviousInAccessQueue() { return this.previousAccess; } public void setPreviousInAccessQueue(ReferenceEntry<K, V> previous) { this.previousAccess = previous; } public long getWriteTime() { return this.writeTime; }
/*      */     public void setWriteTime(long time) { this.writeTime = time; }
/*      */     public ReferenceEntry<K, V> getNextInWriteQueue() { return this.nextWrite; }
/*      */     public void setNextInWriteQueue(ReferenceEntry<K, V> next) { this.nextWrite = next; }
/* 1438 */     public ReferenceEntry<K, V> getPreviousInWriteQueue() { return this.previousWrite; }
/*      */ 
/*      */ 
/*      */     
/*      */     public void setPreviousInWriteQueue(ReferenceEntry<K, V> previous) {
/* 1443 */       this.previousWrite = previous;
/*      */     } }
/*      */ 
/*      */   
/*      */   static class WeakValueReference<K, V>
/*      */     extends WeakReference<V> implements ValueReference<K, V> {
/*      */     final ReferenceEntry<K, V> entry;
/*      */     
/*      */     WeakValueReference(ReferenceQueue<V> queue, V referent, ReferenceEntry<K, V> entry) {
/* 1452 */       super(referent, queue);
/* 1453 */       this.entry = entry;
/*      */     }
/*      */ 
/*      */     
/*      */     public int getWeight() {
/* 1458 */       return 1;
/*      */     }
/*      */ 
/*      */     
/*      */     public ReferenceEntry<K, V> getEntry() {
/* 1463 */       return this.entry;
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     public void notifyNewValue(V newValue) {}
/*      */ 
/*      */     
/*      */     public LocalCache.ValueReference<K, V> copyFor(ReferenceQueue<V> queue, V value, ReferenceEntry<K, V> entry) {
/* 1472 */       return new WeakValueReference(queue, value, entry);
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean isLoading() {
/* 1477 */       return false;
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean isActive() {
/* 1482 */       return true;
/*      */     }
/*      */ 
/*      */     
/*      */     public V waitForValue() {
/* 1487 */       return get();
/*      */     }
/*      */   }
/*      */   
/*      */   static class SoftValueReference<K, V>
/*      */     extends SoftReference<V> implements ValueReference<K, V> {
/*      */     final ReferenceEntry<K, V> entry;
/*      */     
/*      */     SoftValueReference(ReferenceQueue<V> queue, V referent, ReferenceEntry<K, V> entry) {
/* 1496 */       super(referent, queue);
/* 1497 */       this.entry = entry;
/*      */     }
/*      */ 
/*      */     
/*      */     public int getWeight() {
/* 1502 */       return 1;
/*      */     }
/*      */ 
/*      */     
/*      */     public ReferenceEntry<K, V> getEntry() {
/* 1507 */       return this.entry;
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     public void notifyNewValue(V newValue) {}
/*      */ 
/*      */     
/*      */     public LocalCache.ValueReference<K, V> copyFor(ReferenceQueue<V> queue, V value, ReferenceEntry<K, V> entry) {
/* 1516 */       return new SoftValueReference(queue, value, entry);
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean isLoading() {
/* 1521 */       return false;
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean isActive() {
/* 1526 */       return true;
/*      */     }
/*      */ 
/*      */     
/*      */     public V waitForValue() {
/* 1531 */       return get();
/*      */     }
/*      */   }
/*      */   
/*      */   static class StrongValueReference<K, V>
/*      */     implements ValueReference<K, V> {
/*      */     final V referent;
/*      */     
/*      */     StrongValueReference(V referent) {
/* 1540 */       this.referent = referent;
/*      */     }
/*      */ 
/*      */     
/*      */     public V get() {
/* 1545 */       return this.referent;
/*      */     }
/*      */ 
/*      */     
/*      */     public int getWeight() {
/* 1550 */       return 1;
/*      */     }
/*      */ 
/*      */     
/*      */     public ReferenceEntry<K, V> getEntry() {
/* 1555 */       return null;
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     public LocalCache.ValueReference<K, V> copyFor(ReferenceQueue<V> queue, V value, ReferenceEntry<K, V> entry) {
/* 1561 */       return this;
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean isLoading() {
/* 1566 */       return false;
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean isActive() {
/* 1571 */       return true;
/*      */     }
/*      */ 
/*      */     
/*      */     public V waitForValue() {
/* 1576 */       return get();
/*      */     }
/*      */ 
/*      */     
/*      */     public void notifyNewValue(V newValue) {}
/*      */   }
/*      */   
/*      */   static final class WeightedWeakValueReference<K, V>
/*      */     extends WeakValueReference<K, V>
/*      */   {
/*      */     final int weight;
/*      */     
/*      */     WeightedWeakValueReference(ReferenceQueue<V> queue, V referent, ReferenceEntry<K, V> entry, int weight) {
/* 1589 */       super(queue, referent, entry);
/* 1590 */       this.weight = weight;
/*      */     }
/*      */ 
/*      */     
/*      */     public int getWeight() {
/* 1595 */       return this.weight;
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     public LocalCache.ValueReference<K, V> copyFor(ReferenceQueue<V> queue, V value, ReferenceEntry<K, V> entry) {
/* 1601 */       return new WeightedWeakValueReference(queue, value, entry, this.weight);
/*      */     }
/*      */   }
/*      */   
/*      */   static final class WeightedSoftValueReference<K, V>
/*      */     extends SoftValueReference<K, V>
/*      */   {
/*      */     final int weight;
/*      */     
/*      */     WeightedSoftValueReference(ReferenceQueue<V> queue, V referent, ReferenceEntry<K, V> entry, int weight) {
/* 1611 */       super(queue, referent, entry);
/* 1612 */       this.weight = weight;
/*      */     }
/*      */ 
/*      */     
/*      */     public int getWeight() {
/* 1617 */       return this.weight;
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     public LocalCache.ValueReference<K, V> copyFor(ReferenceQueue<V> queue, V value, ReferenceEntry<K, V> entry) {
/* 1623 */       return new WeightedSoftValueReference(queue, value, entry, this.weight);
/*      */     }
/*      */   }
/*      */   
/*      */   static final class WeightedStrongValueReference<K, V>
/*      */     extends StrongValueReference<K, V> {
/*      */     final int weight;
/*      */     
/*      */     WeightedStrongValueReference(V referent, int weight) {
/* 1632 */       super(referent);
/* 1633 */       this.weight = weight;
/*      */     }
/*      */ 
/*      */     
/*      */     public int getWeight() {
/* 1638 */       return this.weight;
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   static int rehash(int h) {
/* 1654 */     h += h << 15 ^ 0xFFFFCD7D;
/* 1655 */     h ^= h >>> 10;
/* 1656 */     h += h << 3;
/* 1657 */     h ^= h >>> 6;
/* 1658 */     h += (h << 2) + (h << 14);
/* 1659 */     return h ^ h >>> 16;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @VisibleForTesting
/*      */   ReferenceEntry<K, V> newEntry(K key, int hash, ReferenceEntry<K, V> next) {
/* 1667 */     Segment<K, V> segment = segmentFor(hash);
/* 1668 */     segment.lock();
/*      */     try {
/* 1670 */       return segment.newEntry(key, hash, next);
/*      */     } finally {
/* 1672 */       segment.unlock();
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @VisibleForTesting
/*      */   ReferenceEntry<K, V> copyEntry(ReferenceEntry<K, V> original, ReferenceEntry<K, V> newNext) {
/* 1682 */     int hash = original.getHash();
/* 1683 */     return segmentFor(hash).copyEntry(original, newNext);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @VisibleForTesting
/*      */   ValueReference<K, V> newValueReference(ReferenceEntry<K, V> entry, V value, int weight) {
/* 1692 */     int hash = entry.getHash();
/* 1693 */     return this.valueStrength.referenceValue(segmentFor(hash), entry, (V)Preconditions.checkNotNull(value), weight);
/*      */   }
/*      */   
/*      */   int hash(Object key) {
/* 1697 */     int h = this.keyEquivalence.hash(key);
/* 1698 */     return rehash(h);
/*      */   }
/*      */   
/*      */   void reclaimValue(ValueReference<K, V> valueReference) {
/* 1702 */     ReferenceEntry<K, V> entry = valueReference.getEntry();
/* 1703 */     int hash = entry.getHash();
/* 1704 */     segmentFor(hash).reclaimValue(entry.getKey(), hash, valueReference);
/*      */   }
/*      */   
/*      */   void reclaimKey(ReferenceEntry<K, V> entry) {
/* 1708 */     int hash = entry.getHash();
/* 1709 */     segmentFor(hash).reclaimKey(entry, hash);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @VisibleForTesting
/*      */   boolean isLive(ReferenceEntry<K, V> entry, long now) {
/* 1718 */     return (segmentFor(entry.getHash()).getLiveValue(entry, now) != null);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   Segment<K, V> segmentFor(int hash) {
/* 1729 */     return this.segments[hash >>> this.segmentShift & this.segmentMask];
/*      */   }
/*      */ 
/*      */   
/*      */   Segment<K, V> createSegment(int initialCapacity, long maxSegmentWeight, AbstractCache.StatsCounter statsCounter) {
/* 1734 */     return new Segment<>(this, initialCapacity, maxSegmentWeight, statsCounter);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   V getLiveValue(ReferenceEntry<K, V> entry, long now) {
/* 1745 */     if (entry.getKey() == null) {
/* 1746 */       return null;
/*      */     }
/* 1748 */     V value = (V)entry.getValueReference().get();
/* 1749 */     if (value == null) {
/* 1750 */       return null;
/*      */     }
/*      */     
/* 1753 */     if (isExpired(entry, now)) {
/* 1754 */       return null;
/*      */     }
/* 1756 */     return value;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   boolean isExpired(ReferenceEntry<K, V> entry, long now) {
/* 1763 */     Preconditions.checkNotNull(entry);
/* 1764 */     if (expiresAfterAccess() && now - entry.getAccessTime() >= this.expireAfterAccessNanos) {
/* 1765 */       return true;
/*      */     }
/* 1767 */     if (expiresAfterWrite() && now - entry.getWriteTime() >= this.expireAfterWriteNanos) {
/* 1768 */       return true;
/*      */     }
/* 1770 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   static <K, V> void connectAccessOrder(ReferenceEntry<K, V> previous, ReferenceEntry<K, V> next) {
/* 1777 */     previous.setNextInAccessQueue(next);
/* 1778 */     next.setPreviousInAccessQueue(previous);
/*      */   }
/*      */ 
/*      */   
/*      */   static <K, V> void nullifyAccessOrder(ReferenceEntry<K, V> nulled) {
/* 1783 */     ReferenceEntry<K, V> nullEntry = nullEntry();
/* 1784 */     nulled.setNextInAccessQueue(nullEntry);
/* 1785 */     nulled.setPreviousInAccessQueue(nullEntry);
/*      */   }
/*      */ 
/*      */   
/*      */   static <K, V> void connectWriteOrder(ReferenceEntry<K, V> previous, ReferenceEntry<K, V> next) {
/* 1790 */     previous.setNextInWriteQueue(next);
/* 1791 */     next.setPreviousInWriteQueue(previous);
/*      */   }
/*      */ 
/*      */   
/*      */   static <K, V> void nullifyWriteOrder(ReferenceEntry<K, V> nulled) {
/* 1796 */     ReferenceEntry<K, V> nullEntry = nullEntry();
/* 1797 */     nulled.setNextInWriteQueue(nullEntry);
/* 1798 */     nulled.setPreviousInWriteQueue(nullEntry);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   void processPendingNotifications() {
/*      */     RemovalNotification<K, V> notification;
/* 1808 */     while ((notification = this.removalNotificationQueue.poll()) != null) {
/*      */       try {
/* 1810 */         this.removalListener.onRemoval(notification);
/* 1811 */       } catch (Throwable e) {
/* 1812 */         logger.log(Level.WARNING, "Exception thrown by removal listener", e);
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   final Segment<K, V>[] newSegmentArray(int ssize) {
/* 1819 */     return (Segment<K, V>[])new Segment[ssize];
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   static class Segment<K, V>
/*      */     extends ReentrantLock
/*      */   {
/*      */     @Weak
/*      */     final LocalCache<K, V> map;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     volatile int count;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     @GuardedBy("this")
/*      */     long totalWeight;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     int modCount;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     int threshold;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     volatile AtomicReferenceArray<ReferenceEntry<K, V>> table;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     final long maxSegmentWeight;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     final ReferenceQueue<K> keyReferenceQueue;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     final ReferenceQueue<V> valueReferenceQueue;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     final Queue<ReferenceEntry<K, V>> recencyQueue;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1916 */     final AtomicInteger readCount = new AtomicInteger();
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     @GuardedBy("this")
/*      */     final Queue<ReferenceEntry<K, V>> writeQueue;
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     @GuardedBy("this")
/*      */     final Queue<ReferenceEntry<K, V>> accessQueue;
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     final AbstractCache.StatsCounter statsCounter;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     Segment(LocalCache<K, V> map, int initialCapacity, long maxSegmentWeight, AbstractCache.StatsCounter statsCounter) {
/* 1940 */       this.map = map;
/* 1941 */       this.maxSegmentWeight = maxSegmentWeight;
/* 1942 */       this.statsCounter = (AbstractCache.StatsCounter)Preconditions.checkNotNull(statsCounter);
/* 1943 */       initTable(newEntryArray(initialCapacity));
/*      */       
/* 1945 */       this.keyReferenceQueue = map.usesKeyReferences() ? new ReferenceQueue<>() : null;
/*      */       
/* 1947 */       this.valueReferenceQueue = map.usesValueReferences() ? new ReferenceQueue<>() : null;
/*      */       
/* 1949 */       this
/*      */ 
/*      */         
/* 1952 */         .recencyQueue = map.usesAccessQueue() ? new ConcurrentLinkedQueue<>() : LocalCache.<ReferenceEntry<K, V>>discardingQueue();
/*      */       
/* 1954 */       this
/*      */ 
/*      */         
/* 1957 */         .writeQueue = map.usesWriteQueue() ? new LocalCache.WriteQueue<>() : LocalCache.<ReferenceEntry<K, V>>discardingQueue();
/*      */       
/* 1959 */       this
/*      */ 
/*      */         
/* 1962 */         .accessQueue = map.usesAccessQueue() ? new LocalCache.AccessQueue<>() : LocalCache.<ReferenceEntry<K, V>>discardingQueue();
/*      */     }
/*      */     
/*      */     AtomicReferenceArray<ReferenceEntry<K, V>> newEntryArray(int size) {
/* 1966 */       return new AtomicReferenceArray<>(size);
/*      */     }
/*      */     
/*      */     void initTable(AtomicReferenceArray<ReferenceEntry<K, V>> newTable) {
/* 1970 */       this.threshold = newTable.length() * 3 / 4;
/* 1971 */       if (!this.map.customWeigher() && this.threshold == this.maxSegmentWeight)
/*      */       {
/* 1973 */         this.threshold++;
/*      */       }
/* 1975 */       this.table = newTable;
/*      */     }
/*      */     
/*      */     @GuardedBy("this")
/*      */     ReferenceEntry<K, V> newEntry(K key, int hash, ReferenceEntry<K, V> next) {
/* 1980 */       return this.map.entryFactory.newEntry(this, (K)Preconditions.checkNotNull(key), hash, next);
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     @GuardedBy("this")
/*      */     ReferenceEntry<K, V> copyEntry(ReferenceEntry<K, V> original, ReferenceEntry<K, V> newNext) {
/* 1989 */       if (original.getKey() == null)
/*      */       {
/* 1991 */         return null;
/*      */       }
/*      */       
/* 1994 */       LocalCache.ValueReference<K, V> valueReference = original.getValueReference();
/* 1995 */       V value = valueReference.get();
/* 1996 */       if (value == null && valueReference.isActive())
/*      */       {
/* 1998 */         return null;
/*      */       }
/*      */       
/* 2001 */       ReferenceEntry<K, V> newEntry = this.map.entryFactory.copyEntry(this, original, newNext);
/* 2002 */       newEntry.setValueReference(valueReference.copyFor(this.valueReferenceQueue, value, newEntry));
/* 2003 */       return newEntry;
/*      */     }
/*      */ 
/*      */     
/*      */     @GuardedBy("this")
/*      */     void setValue(ReferenceEntry<K, V> entry, K key, V value, long now) {
/* 2009 */       LocalCache.ValueReference<K, V> previous = entry.getValueReference();
/* 2010 */       int weight = this.map.weigher.weigh(key, value);
/* 2011 */       Preconditions.checkState((weight >= 0), "Weights must be non-negative");
/*      */ 
/*      */       
/* 2014 */       LocalCache.ValueReference<K, V> valueReference = this.map.valueStrength.referenceValue(this, entry, value, weight);
/* 2015 */       entry.setValueReference(valueReference);
/* 2016 */       recordWrite(entry, weight, now);
/* 2017 */       previous.notifyNewValue(value);
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     V get(K key, int hash, CacheLoader<? super K, V> loader) throws ExecutionException {
/* 2023 */       Preconditions.checkNotNull(key);
/* 2024 */       Preconditions.checkNotNull(loader);
/*      */       try {
/* 2026 */         if (this.count != 0) {
/*      */           
/* 2028 */           ReferenceEntry<K, V> e = getEntry(key, hash);
/* 2029 */           if (e != null) {
/* 2030 */             long now = this.map.ticker.read();
/* 2031 */             V value = getLiveValue(e, now);
/* 2032 */             if (value != null) {
/* 2033 */               recordRead(e, now);
/* 2034 */               this.statsCounter.recordHits(1);
/* 2035 */               return scheduleRefresh(e, key, hash, value, now, loader);
/*      */             } 
/* 2037 */             LocalCache.ValueReference<K, V> valueReference = e.getValueReference();
/* 2038 */             if (valueReference.isLoading()) {
/* 2039 */               return waitForLoadingValue(e, key, valueReference);
/*      */             }
/*      */           } 
/*      */         } 
/*      */ 
/*      */         
/* 2045 */         return lockedGetOrLoad(key, hash, loader);
/* 2046 */       } catch (ExecutionException ee) {
/* 2047 */         Throwable cause = ee.getCause();
/* 2048 */         if (cause instanceof Error)
/* 2049 */           throw new ExecutionError((Error)cause); 
/* 2050 */         if (cause instanceof RuntimeException) {
/* 2051 */           throw new UncheckedExecutionException(cause);
/*      */         }
/* 2053 */         throw ee;
/*      */       } finally {
/* 2055 */         postReadCleanup();
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     V get(Object key, int hash) {
/*      */       try {
/* 2062 */         if (this.count != 0) {
/* 2063 */           long now = this.map.ticker.read();
/* 2064 */           ReferenceEntry<K, V> e = getLiveEntry(key, hash, now);
/* 2065 */           if (e == null) {
/* 2066 */             return null;
/*      */           }
/*      */           
/* 2069 */           V value = (V)e.getValueReference().get();
/* 2070 */           if (value != null) {
/* 2071 */             recordRead(e, now);
/* 2072 */             return scheduleRefresh(e, e.getKey(), hash, value, now, this.map.defaultLoader);
/*      */           } 
/* 2074 */           tryDrainReferenceQueues();
/*      */         } 
/* 2076 */         return null;
/*      */       } finally {
/* 2078 */         postReadCleanup();
/*      */       } 
/*      */     }
/*      */     
/*      */     V lockedGetOrLoad(K key, int hash, CacheLoader<? super K, V> loader) throws ExecutionException {
/*      */       ReferenceEntry<K, V> e;
/* 2084 */       LocalCache.ValueReference<K, V> valueReference = null;
/* 2085 */       LocalCache.LoadingValueReference<K, V> loadingValueReference = null;
/* 2086 */       boolean createNewEntry = true;
/*      */       
/* 2088 */       lock();
/*      */       
/*      */       try {
/* 2091 */         long now = this.map.ticker.read();
/* 2092 */         preWriteCleanup(now);
/*      */         
/* 2094 */         int newCount = this.count - 1;
/* 2095 */         AtomicReferenceArray<ReferenceEntry<K, V>> table = this.table;
/* 2096 */         int index = hash & table.length() - 1;
/* 2097 */         ReferenceEntry<K, V> first = table.get(index);
/*      */         
/* 2099 */         for (e = first; e != null; e = e.getNext()) {
/* 2100 */           K entryKey = e.getKey();
/* 2101 */           if (e.getHash() == hash && entryKey != null && this.map.keyEquivalence
/*      */             
/* 2103 */             .equivalent(key, entryKey)) {
/* 2104 */             valueReference = e.getValueReference();
/* 2105 */             if (valueReference.isLoading()) {
/* 2106 */               createNewEntry = false; break;
/*      */             } 
/* 2108 */             V value = valueReference.get();
/* 2109 */             if (value == null) {
/* 2110 */               enqueueNotification(entryKey, hash, value, valueReference
/* 2111 */                   .getWeight(), RemovalCause.COLLECTED);
/* 2112 */             } else if (this.map.isExpired(e, now)) {
/*      */ 
/*      */               
/* 2115 */               enqueueNotification(entryKey, hash, value, valueReference
/* 2116 */                   .getWeight(), RemovalCause.EXPIRED);
/*      */             } else {
/* 2118 */               recordLockedRead(e, now);
/* 2119 */               this.statsCounter.recordHits(1);
/*      */               
/* 2121 */               return value;
/*      */             } 
/*      */ 
/*      */             
/* 2125 */             this.writeQueue.remove(e);
/* 2126 */             this.accessQueue.remove(e);
/* 2127 */             this.count = newCount;
/*      */             
/*      */             break;
/*      */           } 
/*      */         } 
/*      */         
/* 2133 */         if (createNewEntry) {
/* 2134 */           loadingValueReference = new LocalCache.LoadingValueReference<>();
/*      */           
/* 2136 */           if (e == null) {
/* 2137 */             e = newEntry(key, hash, first);
/* 2138 */             e.setValueReference(loadingValueReference);
/* 2139 */             table.set(index, e);
/*      */           } else {
/* 2141 */             e.setValueReference(loadingValueReference);
/*      */           } 
/*      */         } 
/*      */       } finally {
/* 2145 */         unlock();
/* 2146 */         postWriteCleanup();
/*      */       } 
/*      */       
/* 2149 */       if (createNewEntry) {
/*      */         
/*      */         try {
/*      */ 
/*      */ 
/*      */ 
/*      */         
/*      */         } finally {
/*      */           
/* 2158 */           this.statsCounter.recordMisses(1);
/*      */         } 
/*      */       }
/*      */       
/* 2162 */       return waitForLoadingValue(e, key, valueReference);
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     V waitForLoadingValue(ReferenceEntry<K, V> e, K key, LocalCache.ValueReference<K, V> valueReference) throws ExecutionException {
/* 2168 */       if (!valueReference.isLoading()) {
/* 2169 */         throw new AssertionError();
/*      */       }
/*      */       
/* 2172 */       Preconditions.checkState(!Thread.holdsLock(e), "Recursive load of: %s", key);
/*      */       
/*      */       try {
/* 2175 */         V value = valueReference.waitForValue();
/* 2176 */         if (value == null) {
/* 2177 */           throw new CacheLoader.InvalidCacheLoadException("CacheLoader returned null for key " + key + ".");
/*      */         }
/*      */         
/* 2180 */         long now = this.map.ticker.read();
/* 2181 */         recordRead(e, now);
/* 2182 */         return value;
/*      */       } finally {
/* 2184 */         this.statsCounter.recordMisses(1);
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     V compute(K key, int hash, BiFunction<? super K, ? super V, ? extends V> function) {
/* 2190 */       LocalCache.ValueReference<K, V> valueReference = null;
/* 2191 */       LocalCache.LoadingValueReference<K, V> loadingValueReference = null;
/* 2192 */       boolean createNewEntry = true;
/*      */ 
/*      */       
/* 2195 */       lock();
/*      */       
/*      */       try {
/* 2198 */         long now = this.map.ticker.read();
/* 2199 */         preWriteCleanup(now);
/*      */         
/* 2201 */         AtomicReferenceArray<ReferenceEntry<K, V>> table = this.table;
/* 2202 */         int index = hash & table.length() - 1;
/* 2203 */         ReferenceEntry<K, V> first = table.get(index);
/*      */         ReferenceEntry<K, V> e;
/* 2205 */         for (e = first; e != null; e = e.getNext()) {
/* 2206 */           K entryKey = e.getKey();
/* 2207 */           if (e.getHash() == hash && entryKey != null && this.map.keyEquivalence
/*      */             
/* 2209 */             .equivalent(key, entryKey)) {
/* 2210 */             valueReference = e.getValueReference();
/* 2211 */             if (this.map.isExpired(e, now))
/*      */             {
/*      */               
/* 2214 */               enqueueNotification(entryKey, hash, valueReference
/*      */ 
/*      */                   
/* 2217 */                   .get(), valueReference
/* 2218 */                   .getWeight(), RemovalCause.EXPIRED);
/*      */             }
/*      */ 
/*      */ 
/*      */             
/* 2223 */             this.writeQueue.remove(e);
/* 2224 */             this.accessQueue.remove(e);
/* 2225 */             createNewEntry = false;
/*      */ 
/*      */             
/*      */             break;
/*      */           } 
/*      */         } 
/*      */         
/* 2232 */         loadingValueReference = new LocalCache.LoadingValueReference<>(valueReference);
/*      */         
/* 2234 */         if (e == null) {
/* 2235 */           createNewEntry = true;
/* 2236 */           e = newEntry(key, hash, first);
/* 2237 */           e.setValueReference(loadingValueReference);
/* 2238 */           table.set(index, e);
/*      */         } else {
/* 2240 */           e.setValueReference(loadingValueReference);
/*      */         } 
/*      */         
/* 2243 */         V newValue = loadingValueReference.compute(key, function);
/* 2244 */         if (newValue != null) {
/* 2245 */           if (valueReference != null && newValue == valueReference.get()) {
/* 2246 */             loadingValueReference.set(newValue);
/* 2247 */             e.setValueReference(valueReference);
/* 2248 */             recordWrite(e, 0, now);
/* 2249 */             return newValue;
/*      */           } 
/*      */           try {
/* 2252 */             return getAndRecordStats(key, hash, loadingValueReference, 
/* 2253 */                 Futures.immediateFuture(newValue));
/* 2254 */           } catch (ExecutionException exception) {
/* 2255 */             throw new AssertionError("impossible; Futures.immediateFuture can't throw");
/*      */           } 
/* 2257 */         }  if (createNewEntry) {
/* 2258 */           removeLoadingValue(key, hash, loadingValueReference);
/* 2259 */           return null;
/*      */         } 
/* 2261 */         removeEntry(e, hash, RemovalCause.EXPLICIT);
/* 2262 */         return null;
/*      */       } finally {
/*      */         
/* 2265 */         unlock();
/* 2266 */         postWriteCleanup();
/*      */       } 
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     V loadSync(K key, int hash, LocalCache.LoadingValueReference<K, V> loadingValueReference, CacheLoader<? super K, V> loader) throws ExecutionException {
/* 2278 */       ListenableFuture<V> loadingFuture = loadingValueReference.loadFuture(key, loader);
/* 2279 */       return getAndRecordStats(key, hash, loadingValueReference, loadingFuture);
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     ListenableFuture<V> loadAsync(final K key, final int hash, final LocalCache.LoadingValueReference<K, V> loadingValueReference, CacheLoader<? super K, V> loader) {
/* 2287 */       final ListenableFuture<V> loadingFuture = loadingValueReference.loadFuture(key, loader);
/* 2288 */       loadingFuture.addListener(new Runnable()
/*      */           {
/*      */             public void run()
/*      */             {
/*      */               try {
/* 2293 */                 LocalCache.Segment.this.getAndRecordStats(key, hash, loadingValueReference, loadingFuture);
/* 2294 */               } catch (Throwable t) {
/* 2295 */                 LocalCache.logger.log(Level.WARNING, "Exception thrown during refresh", t);
/* 2296 */                 loadingValueReference.setException(t);
/*      */               }
/*      */             
/*      */             }
/* 2300 */           }MoreExecutors.directExecutor());
/* 2301 */       return loadingFuture;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     V getAndRecordStats(K key, int hash, LocalCache.LoadingValueReference<K, V> loadingValueReference, ListenableFuture<V> newValue) throws ExecutionException {
/* 2311 */       V value = null;
/*      */       try {
/* 2313 */         value = (V)Uninterruptibles.getUninterruptibly((Future)newValue);
/* 2314 */         if (value == null) {
/* 2315 */           throw new CacheLoader.InvalidCacheLoadException("CacheLoader returned null for key " + key + ".");
/*      */         }
/* 2317 */         this.statsCounter.recordLoadSuccess(loadingValueReference.elapsedNanos());
/* 2318 */         storeLoadedValue(key, hash, loadingValueReference, value);
/* 2319 */         return value;
/*      */       } finally {
/* 2321 */         if (value == null) {
/* 2322 */           this.statsCounter.recordLoadException(loadingValueReference.elapsedNanos());
/* 2323 */           removeLoadingValue(key, hash, loadingValueReference);
/*      */         } 
/*      */       } 
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     V scheduleRefresh(ReferenceEntry<K, V> entry, K key, int hash, V oldValue, long now, CacheLoader<? super K, V> loader) {
/* 2335 */       if (this.map.refreshes() && now - entry
/* 2336 */         .getWriteTime() > this.map.refreshNanos && 
/* 2337 */         !entry.getValueReference().isLoading()) {
/* 2338 */         V newValue = refresh(key, hash, loader, true);
/* 2339 */         if (newValue != null) {
/* 2340 */           return newValue;
/*      */         }
/*      */       } 
/* 2343 */       return oldValue;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     V refresh(K key, int hash, CacheLoader<? super K, V> loader, boolean checkTime) {
/* 2355 */       LocalCache.LoadingValueReference<K, V> loadingValueReference = insertLoadingValueReference(key, hash, checkTime);
/* 2356 */       if (loadingValueReference == null) {
/* 2357 */         return null;
/*      */       }
/*      */       
/* 2360 */       ListenableFuture<V> result = loadAsync(key, hash, loadingValueReference, loader);
/* 2361 */       if (result.isDone()) {
/*      */         try {
/* 2363 */           return (V)Uninterruptibles.getUninterruptibly((Future)result);
/* 2364 */         } catch (Throwable throwable) {}
/*      */       }
/*      */ 
/*      */       
/* 2368 */       return null;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     LocalCache.LoadingValueReference<K, V> insertLoadingValueReference(K key, int hash, boolean checkTime) {
/* 2378 */       ReferenceEntry<K, V> e = null;
/* 2379 */       lock();
/*      */       try {
/* 2381 */         long now = this.map.ticker.read();
/* 2382 */         preWriteCleanup(now);
/*      */         
/* 2384 */         AtomicReferenceArray<ReferenceEntry<K, V>> table = this.table;
/* 2385 */         int index = hash & table.length() - 1;
/* 2386 */         ReferenceEntry<K, V> first = table.get(index);
/*      */ 
/*      */         
/* 2389 */         for (e = first; e != null; e = e.getNext()) {
/* 2390 */           K entryKey = e.getKey();
/* 2391 */           if (e.getHash() == hash && entryKey != null && this.map.keyEquivalence
/*      */             
/* 2393 */             .equivalent(key, entryKey)) {
/*      */ 
/*      */             
/* 2396 */             LocalCache.ValueReference<K, V> valueReference = e.getValueReference();
/* 2397 */             if (valueReference.isLoading() || (checkTime && now - e
/* 2398 */               .getWriteTime() < this.map.refreshNanos))
/*      */             {
/*      */ 
/*      */               
/* 2402 */               return null;
/*      */             }
/*      */ 
/*      */             
/* 2406 */             this.modCount++;
/* 2407 */             LocalCache.LoadingValueReference<K, V> loadingValueReference1 = new LocalCache.LoadingValueReference<>(valueReference);
/*      */             
/* 2409 */             e.setValueReference(loadingValueReference1);
/* 2410 */             return loadingValueReference1;
/*      */           } 
/*      */         } 
/*      */         
/* 2414 */         this.modCount++;
/* 2415 */         LocalCache.LoadingValueReference<K, V> loadingValueReference = new LocalCache.LoadingValueReference<>();
/* 2416 */         e = newEntry(key, hash, first);
/* 2417 */         e.setValueReference(loadingValueReference);
/* 2418 */         table.set(index, e);
/* 2419 */         return loadingValueReference;
/*      */       } finally {
/* 2421 */         unlock();
/* 2422 */         postWriteCleanup();
/*      */       } 
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     void tryDrainReferenceQueues() {
/* 2430 */       if (tryLock()) {
/*      */         try {
/* 2432 */           drainReferenceQueues();
/*      */         } finally {
/* 2434 */           unlock();
/*      */         } 
/*      */       }
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     @GuardedBy("this")
/*      */     void drainReferenceQueues() {
/* 2445 */       if (this.map.usesKeyReferences()) {
/* 2446 */         drainKeyReferenceQueue();
/*      */       }
/* 2448 */       if (this.map.usesValueReferences()) {
/* 2449 */         drainValueReferenceQueue();
/*      */       }
/*      */     }
/*      */ 
/*      */     
/*      */     @GuardedBy("this")
/*      */     void drainKeyReferenceQueue() {
/* 2456 */       int i = 0; Reference<? extends K> ref;
/* 2457 */       while ((ref = this.keyReferenceQueue.poll()) != null) {
/*      */         
/* 2459 */         ReferenceEntry<K, V> entry = (ReferenceEntry)ref;
/* 2460 */         this.map.reclaimKey(entry);
/* 2461 */         if (++i == 16) {
/*      */           break;
/*      */         }
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     @GuardedBy("this")
/*      */     void drainValueReferenceQueue() {
/* 2470 */       int i = 0; Reference<? extends V> ref;
/* 2471 */       while ((ref = this.valueReferenceQueue.poll()) != null) {
/*      */         
/* 2473 */         LocalCache.ValueReference<K, V> valueReference = (LocalCache.ValueReference)ref;
/* 2474 */         this.map.reclaimValue(valueReference);
/* 2475 */         if (++i == 16) {
/*      */           break;
/*      */         }
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     void clearReferenceQueues() {
/* 2483 */       if (this.map.usesKeyReferences()) {
/* 2484 */         clearKeyReferenceQueue();
/*      */       }
/* 2486 */       if (this.map.usesValueReferences()) {
/* 2487 */         clearValueReferenceQueue();
/*      */       }
/*      */     }
/*      */     
/*      */     void clearKeyReferenceQueue() {
/* 2492 */       while (this.keyReferenceQueue.poll() != null);
/*      */     }
/*      */     
/*      */     void clearValueReferenceQueue() {
/* 2496 */       while (this.valueReferenceQueue.poll() != null);
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     void recordRead(ReferenceEntry<K, V> entry, long now) {
/* 2509 */       if (this.map.recordsAccess()) {
/* 2510 */         entry.setAccessTime(now);
/*      */       }
/* 2512 */       this.recencyQueue.add(entry);
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     @GuardedBy("this")
/*      */     void recordLockedRead(ReferenceEntry<K, V> entry, long now) {
/* 2524 */       if (this.map.recordsAccess()) {
/* 2525 */         entry.setAccessTime(now);
/*      */       }
/* 2527 */       this.accessQueue.add(entry);
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     @GuardedBy("this")
/*      */     void recordWrite(ReferenceEntry<K, V> entry, int weight, long now) {
/* 2537 */       drainRecencyQueue();
/* 2538 */       this.totalWeight += weight;
/*      */       
/* 2540 */       if (this.map.recordsAccess()) {
/* 2541 */         entry.setAccessTime(now);
/*      */       }
/* 2543 */       if (this.map.recordsWrite()) {
/* 2544 */         entry.setWriteTime(now);
/*      */       }
/* 2546 */       this.accessQueue.add(entry);
/* 2547 */       this.writeQueue.add(entry);
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     @GuardedBy("this")
/*      */     void drainRecencyQueue() {
/*      */       ReferenceEntry<K, V> e;
/* 2559 */       while ((e = this.recencyQueue.poll()) != null) {
/*      */ 
/*      */ 
/*      */ 
/*      */         
/* 2564 */         if (this.accessQueue.contains(e)) {
/* 2565 */           this.accessQueue.add(e);
/*      */         }
/*      */       } 
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     void tryExpireEntries(long now) {
/* 2574 */       if (tryLock()) {
/*      */         try {
/* 2576 */           expireEntries(now);
/*      */         } finally {
/* 2578 */           unlock();
/*      */         } 
/*      */       }
/*      */     }
/*      */ 
/*      */     
/*      */     @GuardedBy("this")
/*      */     void expireEntries(long now) {
/* 2586 */       drainRecencyQueue();
/*      */       
/*      */       ReferenceEntry<K, V> e;
/* 2589 */       while ((e = this.writeQueue.peek()) != null && this.map.isExpired(e, now)) {
/* 2590 */         if (!removeEntry(e, e.getHash(), RemovalCause.EXPIRED)) {
/* 2591 */           throw new AssertionError();
/*      */         }
/*      */       } 
/* 2594 */       while ((e = this.accessQueue.peek()) != null && this.map.isExpired(e, now)) {
/* 2595 */         if (!removeEntry(e, e.getHash(), RemovalCause.EXPIRED)) {
/* 2596 */           throw new AssertionError();
/*      */         }
/*      */       } 
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     @GuardedBy("this")
/*      */     void enqueueNotification(K key, int hash, V value, int weight, RemovalCause cause) {
/* 2606 */       this.totalWeight -= weight;
/* 2607 */       if (cause.wasEvicted()) {
/* 2608 */         this.statsCounter.recordEviction();
/*      */       }
/* 2610 */       if (this.map.removalNotificationQueue != LocalCache.DISCARDING_QUEUE) {
/* 2611 */         RemovalNotification<K, V> notification = RemovalNotification.create(key, value, cause);
/* 2612 */         this.map.removalNotificationQueue.offer(notification);
/*      */       } 
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     @GuardedBy("this")
/*      */     void evictEntries(ReferenceEntry<K, V> newest) {
/* 2624 */       if (!this.map.evictsBySize()) {
/*      */         return;
/*      */       }
/*      */       
/* 2628 */       drainRecencyQueue();
/*      */ 
/*      */ 
/*      */       
/* 2632 */       if (newest.getValueReference().getWeight() > this.maxSegmentWeight && 
/* 2633 */         !removeEntry(newest, newest.getHash(), RemovalCause.SIZE)) {
/* 2634 */         throw new AssertionError();
/*      */       }
/*      */ 
/*      */       
/* 2638 */       while (this.totalWeight > this.maxSegmentWeight) {
/* 2639 */         ReferenceEntry<K, V> e = getNextEvictable();
/* 2640 */         if (!removeEntry(e, e.getHash(), RemovalCause.SIZE)) {
/* 2641 */           throw new AssertionError();
/*      */         }
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     @GuardedBy("this")
/*      */     ReferenceEntry<K, V> getNextEvictable() {
/* 2649 */       for (ReferenceEntry<K, V> e : this.accessQueue) {
/* 2650 */         int weight = e.getValueReference().getWeight();
/* 2651 */         if (weight > 0) {
/* 2652 */           return e;
/*      */         }
/*      */       } 
/* 2655 */       throw new AssertionError();
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     ReferenceEntry<K, V> getFirst(int hash) {
/* 2661 */       AtomicReferenceArray<ReferenceEntry<K, V>> table = this.table;
/* 2662 */       return table.get(hash & table.length() - 1);
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     ReferenceEntry<K, V> getEntry(Object key, int hash) {
/* 2669 */       for (ReferenceEntry<K, V> e = getFirst(hash); e != null; e = e.getNext()) {
/* 2670 */         if (e.getHash() == hash) {
/*      */ 
/*      */ 
/*      */           
/* 2674 */           K entryKey = e.getKey();
/* 2675 */           if (entryKey == null) {
/* 2676 */             tryDrainReferenceQueues();
/*      */ 
/*      */           
/*      */           }
/* 2680 */           else if (this.map.keyEquivalence.equivalent(key, entryKey)) {
/* 2681 */             return e;
/*      */           } 
/*      */         } 
/*      */       } 
/* 2685 */       return null;
/*      */     }
/*      */ 
/*      */     
/*      */     ReferenceEntry<K, V> getLiveEntry(Object key, int hash, long now) {
/* 2690 */       ReferenceEntry<K, V> e = getEntry(key, hash);
/* 2691 */       if (e == null)
/* 2692 */         return null; 
/* 2693 */       if (this.map.isExpired(e, now)) {
/* 2694 */         tryExpireEntries(now);
/* 2695 */         return null;
/*      */       } 
/* 2697 */       return e;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     V getLiveValue(ReferenceEntry<K, V> entry, long now) {
/* 2705 */       if (entry.getKey() == null) {
/* 2706 */         tryDrainReferenceQueues();
/* 2707 */         return null;
/*      */       } 
/* 2709 */       V value = (V)entry.getValueReference().get();
/* 2710 */       if (value == null) {
/* 2711 */         tryDrainReferenceQueues();
/* 2712 */         return null;
/*      */       } 
/*      */       
/* 2715 */       if (this.map.isExpired(entry, now)) {
/* 2716 */         tryExpireEntries(now);
/* 2717 */         return null;
/*      */       } 
/* 2719 */       return value;
/*      */     }
/*      */     
/*      */     boolean containsKey(Object key, int hash) {
/*      */       try {
/* 2724 */         if (this.count != 0) {
/* 2725 */           long now = this.map.ticker.read();
/* 2726 */           ReferenceEntry<K, V> e = getLiveEntry(key, hash, now);
/* 2727 */           if (e == null) {
/* 2728 */             return false;
/*      */           }
/* 2730 */           return (e.getValueReference().get() != null);
/*      */         } 
/*      */         
/* 2733 */         return false;
/*      */       } finally {
/* 2735 */         postReadCleanup();
/*      */       } 
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     @VisibleForTesting
/*      */     boolean containsValue(Object value) {
/*      */       try {
/* 2746 */         if (this.count != 0) {
/* 2747 */           long now = this.map.ticker.read();
/* 2748 */           AtomicReferenceArray<ReferenceEntry<K, V>> table = this.table;
/* 2749 */           int length = table.length();
/* 2750 */           for (int i = 0; i < length; i++) {
/* 2751 */             for (ReferenceEntry<K, V> e = table.get(i); e != null; e = e.getNext()) {
/* 2752 */               V entryValue = getLiveValue(e, now);
/* 2753 */               if (entryValue != null)
/*      */               {
/*      */                 
/* 2756 */                 if (this.map.valueEquivalence.equivalent(value, entryValue)) {
/* 2757 */                   return true;
/*      */                 }
/*      */               }
/*      */             } 
/*      */           } 
/*      */         } 
/* 2763 */         return false;
/*      */       } finally {
/* 2765 */         postReadCleanup();
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     V put(K key, int hash, V value, boolean onlyIfAbsent) {
/* 2771 */       lock();
/*      */       try {
/* 2773 */         long now = this.map.ticker.read();
/* 2774 */         preWriteCleanup(now);
/*      */         
/* 2776 */         int newCount = this.count + 1;
/* 2777 */         if (newCount > this.threshold) {
/* 2778 */           expand();
/* 2779 */           newCount = this.count + 1;
/*      */         } 
/*      */         
/* 2782 */         AtomicReferenceArray<ReferenceEntry<K, V>> table = this.table;
/* 2783 */         int index = hash & table.length() - 1;
/* 2784 */         ReferenceEntry<K, V> first = table.get(index);
/*      */ 
/*      */         
/* 2787 */         for (ReferenceEntry<K, V> e = first; e != null; e = e.getNext()) {
/* 2788 */           K entryKey = e.getKey();
/* 2789 */           if (e.getHash() == hash && entryKey != null && this.map.keyEquivalence
/*      */             
/* 2791 */             .equivalent(key, entryKey)) {
/*      */ 
/*      */             
/* 2794 */             LocalCache.ValueReference<K, V> valueReference = e.getValueReference();
/* 2795 */             V entryValue = valueReference.get();
/*      */             
/* 2797 */             if (entryValue == null) {
/* 2798 */               this.modCount++;
/* 2799 */               if (valueReference.isActive()) {
/* 2800 */                 enqueueNotification(key, hash, entryValue, valueReference
/* 2801 */                     .getWeight(), RemovalCause.COLLECTED);
/* 2802 */                 setValue(e, key, value, now);
/* 2803 */                 newCount = this.count;
/*      */               } else {
/* 2805 */                 setValue(e, key, value, now);
/* 2806 */                 newCount = this.count + 1;
/*      */               } 
/* 2808 */               this.count = newCount;
/* 2809 */               evictEntries(e);
/* 2810 */               return null;
/* 2811 */             }  if (onlyIfAbsent) {
/*      */ 
/*      */ 
/*      */               
/* 2815 */               recordLockedRead(e, now);
/* 2816 */               return entryValue;
/*      */             } 
/*      */             
/* 2819 */             this.modCount++;
/* 2820 */             enqueueNotification(key, hash, entryValue, valueReference
/* 2821 */                 .getWeight(), RemovalCause.REPLACED);
/* 2822 */             setValue(e, key, value, now);
/* 2823 */             evictEntries(e);
/* 2824 */             return entryValue;
/*      */           } 
/*      */         } 
/*      */ 
/*      */ 
/*      */         
/* 2830 */         this.modCount++;
/* 2831 */         ReferenceEntry<K, V> newEntry = newEntry(key, hash, first);
/* 2832 */         setValue(newEntry, key, value, now);
/* 2833 */         table.set(index, newEntry);
/* 2834 */         newCount = this.count + 1;
/* 2835 */         this.count = newCount;
/* 2836 */         evictEntries(newEntry);
/* 2837 */         return null;
/*      */       } finally {
/* 2839 */         unlock();
/* 2840 */         postWriteCleanup();
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     @GuardedBy("this")
/*      */     void expand() {
/* 2847 */       AtomicReferenceArray<ReferenceEntry<K, V>> oldTable = this.table;
/* 2848 */       int oldCapacity = oldTable.length();
/* 2849 */       if (oldCapacity >= 1073741824) {
/*      */         return;
/*      */       }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 2863 */       int newCount = this.count;
/* 2864 */       AtomicReferenceArray<ReferenceEntry<K, V>> newTable = newEntryArray(oldCapacity << 1);
/* 2865 */       this.threshold = newTable.length() * 3 / 4;
/* 2866 */       int newMask = newTable.length() - 1;
/* 2867 */       for (int oldIndex = 0; oldIndex < oldCapacity; oldIndex++) {
/*      */ 
/*      */         
/* 2870 */         ReferenceEntry<K, V> head = oldTable.get(oldIndex);
/*      */         
/* 2872 */         if (head != null) {
/* 2873 */           ReferenceEntry<K, V> next = head.getNext();
/* 2874 */           int headIndex = head.getHash() & newMask;
/*      */ 
/*      */           
/* 2877 */           if (next == null) {
/* 2878 */             newTable.set(headIndex, head);
/*      */           
/*      */           }
/*      */           else {
/*      */             
/* 2883 */             ReferenceEntry<K, V> tail = head;
/* 2884 */             int tailIndex = headIndex; ReferenceEntry<K, V> e;
/* 2885 */             for (e = next; e != null; e = e.getNext()) {
/* 2886 */               int newIndex = e.getHash() & newMask;
/* 2887 */               if (newIndex != tailIndex) {
/*      */                 
/* 2889 */                 tailIndex = newIndex;
/* 2890 */                 tail = e;
/*      */               } 
/*      */             } 
/* 2893 */             newTable.set(tailIndex, tail);
/*      */ 
/*      */             
/* 2896 */             for (e = head; e != tail; e = e.getNext()) {
/* 2897 */               int newIndex = e.getHash() & newMask;
/* 2898 */               ReferenceEntry<K, V> newNext = newTable.get(newIndex);
/* 2899 */               ReferenceEntry<K, V> newFirst = copyEntry(e, newNext);
/* 2900 */               if (newFirst != null) {
/* 2901 */                 newTable.set(newIndex, newFirst);
/*      */               } else {
/* 2903 */                 removeCollectedEntry(e);
/* 2904 */                 newCount--;
/*      */               } 
/*      */             } 
/*      */           } 
/*      */         } 
/*      */       } 
/* 2910 */       this.table = newTable;
/* 2911 */       this.count = newCount;
/*      */     }
/*      */     
/*      */     boolean replace(K key, int hash, V oldValue, V newValue) {
/* 2915 */       lock();
/*      */       try {
/* 2917 */         long now = this.map.ticker.read();
/* 2918 */         preWriteCleanup(now);
/*      */         
/* 2920 */         AtomicReferenceArray<ReferenceEntry<K, V>> table = this.table;
/* 2921 */         int index = hash & table.length() - 1;
/* 2922 */         ReferenceEntry<K, V> first = table.get(index);
/*      */         
/* 2924 */         for (ReferenceEntry<K, V> e = first; e != null; e = e.getNext()) {
/* 2925 */           K entryKey = e.getKey();
/* 2926 */           if (e.getHash() == hash && entryKey != null && this.map.keyEquivalence
/*      */             
/* 2928 */             .equivalent(key, entryKey)) {
/* 2929 */             LocalCache.ValueReference<K, V> valueReference = e.getValueReference();
/* 2930 */             V entryValue = valueReference.get();
/* 2931 */             if (entryValue == null) {
/* 2932 */               if (valueReference.isActive()) {
/*      */                 
/* 2934 */                 int newCount = this.count - 1;
/* 2935 */                 this.modCount++;
/*      */                 
/* 2937 */                 ReferenceEntry<K, V> newFirst = removeValueFromChain(first, e, entryKey, hash, entryValue, valueReference, RemovalCause.COLLECTED);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */                 
/* 2945 */                 newCount = this.count - 1;
/* 2946 */                 table.set(index, newFirst);
/* 2947 */                 this.count = newCount;
/*      */               } 
/* 2949 */               return false;
/*      */             } 
/*      */             
/* 2952 */             if (this.map.valueEquivalence.equivalent(oldValue, entryValue)) {
/* 2953 */               this.modCount++;
/* 2954 */               enqueueNotification(key, hash, entryValue, valueReference
/* 2955 */                   .getWeight(), RemovalCause.REPLACED);
/* 2956 */               setValue(e, key, newValue, now);
/* 2957 */               evictEntries(e);
/* 2958 */               return true;
/*      */             } 
/*      */ 
/*      */             
/* 2962 */             recordLockedRead(e, now);
/* 2963 */             return false;
/*      */           } 
/*      */         } 
/*      */ 
/*      */         
/* 2968 */         return false;
/*      */       } finally {
/* 2970 */         unlock();
/* 2971 */         postWriteCleanup();
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     V replace(K key, int hash, V newValue) {
/* 2977 */       lock();
/*      */       try {
/* 2979 */         long now = this.map.ticker.read();
/* 2980 */         preWriteCleanup(now);
/*      */         
/* 2982 */         AtomicReferenceArray<ReferenceEntry<K, V>> table = this.table;
/* 2983 */         int index = hash & table.length() - 1;
/* 2984 */         ReferenceEntry<K, V> first = table.get(index);
/*      */         ReferenceEntry<K, V> e;
/* 2986 */         for (e = first; e != null; e = e.getNext()) {
/* 2987 */           K entryKey = e.getKey();
/* 2988 */           if (e.getHash() == hash && entryKey != null && this.map.keyEquivalence
/*      */             
/* 2990 */             .equivalent(key, entryKey)) {
/* 2991 */             LocalCache.ValueReference<K, V> valueReference = e.getValueReference();
/* 2992 */             V entryValue = valueReference.get();
/* 2993 */             if (entryValue == null) {
/* 2994 */               if (valueReference.isActive()) {
/*      */                 
/* 2996 */                 int newCount = this.count - 1;
/* 2997 */                 this.modCount++;
/*      */                 
/* 2999 */                 ReferenceEntry<K, V> newFirst = removeValueFromChain(first, e, entryKey, hash, entryValue, valueReference, RemovalCause.COLLECTED);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */                 
/* 3007 */                 newCount = this.count - 1;
/* 3008 */                 table.set(index, newFirst);
/* 3009 */                 this.count = newCount;
/*      */               } 
/* 3011 */               return null;
/*      */             } 
/*      */             
/* 3014 */             this.modCount++;
/* 3015 */             enqueueNotification(key, hash, entryValue, valueReference
/* 3016 */                 .getWeight(), RemovalCause.REPLACED);
/* 3017 */             setValue(e, key, newValue, now);
/* 3018 */             evictEntries(e);
/* 3019 */             return entryValue;
/*      */           } 
/*      */         } 
/*      */         
/* 3023 */         e = null; return (V)e;
/*      */       } finally {
/* 3025 */         unlock();
/* 3026 */         postWriteCleanup();
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     V remove(Object key, int hash) {
/* 3032 */       lock();
/*      */       try {
/* 3034 */         long now = this.map.ticker.read();
/* 3035 */         preWriteCleanup(now);
/*      */         
/* 3037 */         int newCount = this.count - 1;
/* 3038 */         AtomicReferenceArray<ReferenceEntry<K, V>> table = this.table;
/* 3039 */         int index = hash & table.length() - 1;
/* 3040 */         ReferenceEntry<K, V> first = table.get(index);
/*      */         ReferenceEntry<K, V> e;
/* 3042 */         for (e = first; e != null; e = e.getNext()) {
/* 3043 */           K entryKey = e.getKey();
/* 3044 */           if (e.getHash() == hash && entryKey != null && this.map.keyEquivalence
/*      */             
/* 3046 */             .equivalent(key, entryKey)) {
/* 3047 */             RemovalCause cause; LocalCache.ValueReference<K, V> valueReference = e.getValueReference();
/* 3048 */             V entryValue = valueReference.get();
/*      */ 
/*      */             
/* 3051 */             if (entryValue != null) {
/* 3052 */               cause = RemovalCause.EXPLICIT;
/* 3053 */             } else if (valueReference.isActive()) {
/* 3054 */               cause = RemovalCause.COLLECTED;
/*      */             } else {
/*      */               
/* 3057 */               return null;
/*      */             } 
/*      */             
/* 3060 */             this.modCount++;
/*      */             
/* 3062 */             ReferenceEntry<K, V> newFirst = removeValueFromChain(first, e, entryKey, hash, entryValue, valueReference, cause);
/* 3063 */             newCount = this.count - 1;
/* 3064 */             table.set(index, newFirst);
/* 3065 */             this.count = newCount;
/* 3066 */             return entryValue;
/*      */           } 
/*      */         } 
/*      */         
/* 3070 */         e = null; return (V)e;
/*      */       } finally {
/* 3072 */         unlock();
/* 3073 */         postWriteCleanup();
/*      */       } 
/*      */     }
/*      */     
/*      */     boolean remove(Object key, int hash, Object value) {
/* 3078 */       lock();
/*      */       try {
/* 3080 */         long now = this.map.ticker.read();
/* 3081 */         preWriteCleanup(now);
/*      */         
/* 3083 */         int newCount = this.count - 1;
/* 3084 */         AtomicReferenceArray<ReferenceEntry<K, V>> table = this.table;
/* 3085 */         int index = hash & table.length() - 1;
/* 3086 */         ReferenceEntry<K, V> first = table.get(index);
/*      */         
/* 3088 */         for (ReferenceEntry<K, V> e = first; e != null; e = e.getNext()) {
/* 3089 */           K entryKey = e.getKey();
/* 3090 */           if (e.getHash() == hash && entryKey != null && this.map.keyEquivalence
/*      */             
/* 3092 */             .equivalent(key, entryKey)) {
/* 3093 */             RemovalCause cause; LocalCache.ValueReference<K, V> valueReference = e.getValueReference();
/* 3094 */             V entryValue = valueReference.get();
/*      */ 
/*      */             
/* 3097 */             if (this.map.valueEquivalence.equivalent(value, entryValue)) {
/* 3098 */               cause = RemovalCause.EXPLICIT;
/* 3099 */             } else if (entryValue == null && valueReference.isActive()) {
/* 3100 */               cause = RemovalCause.COLLECTED;
/*      */             } else {
/*      */               
/* 3103 */               return false;
/*      */             } 
/*      */             
/* 3106 */             this.modCount++;
/*      */             
/* 3108 */             ReferenceEntry<K, V> newFirst = removeValueFromChain(first, e, entryKey, hash, entryValue, valueReference, cause);
/* 3109 */             newCount = this.count - 1;
/* 3110 */             table.set(index, newFirst);
/* 3111 */             this.count = newCount;
/* 3112 */             return (cause == RemovalCause.EXPLICIT);
/*      */           } 
/*      */         } 
/*      */         
/* 3116 */         return false;
/*      */       } finally {
/* 3118 */         unlock();
/* 3119 */         postWriteCleanup();
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     boolean storeLoadedValue(K key, int hash, LocalCache.LoadingValueReference<K, V> oldValueReference, V newValue) {
/* 3125 */       lock();
/*      */       try {
/* 3127 */         long now = this.map.ticker.read();
/* 3128 */         preWriteCleanup(now);
/*      */         
/* 3130 */         int newCount = this.count + 1;
/* 3131 */         if (newCount > this.threshold) {
/* 3132 */           expand();
/* 3133 */           newCount = this.count + 1;
/*      */         } 
/*      */         
/* 3136 */         AtomicReferenceArray<ReferenceEntry<K, V>> table = this.table;
/* 3137 */         int index = hash & table.length() - 1;
/* 3138 */         ReferenceEntry<K, V> first = table.get(index);
/*      */         
/* 3140 */         for (ReferenceEntry<K, V> e = first; e != null; e = e.getNext()) {
/* 3141 */           K entryKey = e.getKey();
/* 3142 */           if (e.getHash() == hash && entryKey != null && this.map.keyEquivalence
/*      */             
/* 3144 */             .equivalent(key, entryKey)) {
/* 3145 */             LocalCache.ValueReference<K, V> valueReference = e.getValueReference();
/* 3146 */             V entryValue = valueReference.get();
/*      */ 
/*      */             
/* 3149 */             if (oldValueReference == valueReference || (entryValue == null && valueReference != LocalCache.UNSET)) {
/*      */               
/* 3151 */               this.modCount++;
/* 3152 */               if (oldValueReference.isActive()) {
/* 3153 */                 RemovalCause cause = (entryValue == null) ? RemovalCause.COLLECTED : RemovalCause.REPLACED;
/*      */                 
/* 3155 */                 enqueueNotification(key, hash, entryValue, oldValueReference.getWeight(), cause);
/* 3156 */                 newCount--;
/*      */               } 
/* 3158 */               setValue(e, key, newValue, now);
/* 3159 */               this.count = newCount;
/* 3160 */               evictEntries(e);
/* 3161 */               return true;
/*      */             } 
/*      */ 
/*      */             
/* 3165 */             enqueueNotification(key, hash, newValue, 0, RemovalCause.REPLACED);
/* 3166 */             return false;
/*      */           } 
/*      */         } 
/*      */         
/* 3170 */         this.modCount++;
/* 3171 */         ReferenceEntry<K, V> newEntry = newEntry(key, hash, first);
/* 3172 */         setValue(newEntry, key, newValue, now);
/* 3173 */         table.set(index, newEntry);
/* 3174 */         this.count = newCount;
/* 3175 */         evictEntries(newEntry);
/* 3176 */         return true;
/*      */       } finally {
/* 3178 */         unlock();
/* 3179 */         postWriteCleanup();
/*      */       } 
/*      */     }
/*      */     
/*      */     void clear() {
/* 3184 */       if (this.count != 0) {
/* 3185 */         lock();
/*      */         try {
/* 3187 */           long now = this.map.ticker.read();
/* 3188 */           preWriteCleanup(now);
/*      */           
/* 3190 */           AtomicReferenceArray<ReferenceEntry<K, V>> table = this.table; int i;
/* 3191 */           for (i = 0; i < table.length(); i++) {
/* 3192 */             for (ReferenceEntry<K, V> e = table.get(i); e != null; e = e.getNext()) {
/*      */               
/* 3194 */               if (e.getValueReference().isActive()) {
/* 3195 */                 K key = e.getKey();
/* 3196 */                 V value = (V)e.getValueReference().get();
/* 3197 */                 RemovalCause cause = (key == null || value == null) ? RemovalCause.COLLECTED : RemovalCause.EXPLICIT;
/*      */                 
/* 3199 */                 enqueueNotification(key, e
/* 3200 */                     .getHash(), value, e.getValueReference().getWeight(), cause);
/*      */               } 
/*      */             } 
/*      */           } 
/* 3204 */           for (i = 0; i < table.length(); i++) {
/* 3205 */             table.set(i, null);
/*      */           }
/* 3207 */           clearReferenceQueues();
/* 3208 */           this.writeQueue.clear();
/* 3209 */           this.accessQueue.clear();
/* 3210 */           this.readCount.set(0);
/*      */           
/* 3212 */           this.modCount++;
/* 3213 */           this.count = 0;
/*      */         } finally {
/* 3215 */           unlock();
/* 3216 */           postWriteCleanup();
/*      */         } 
/*      */       } 
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     @GuardedBy("this")
/*      */     ReferenceEntry<K, V> removeValueFromChain(ReferenceEntry<K, V> first, ReferenceEntry<K, V> entry, K key, int hash, V value, LocalCache.ValueReference<K, V> valueReference, RemovalCause cause) {
/* 3231 */       enqueueNotification(key, hash, value, valueReference.getWeight(), cause);
/* 3232 */       this.writeQueue.remove(entry);
/* 3233 */       this.accessQueue.remove(entry);
/*      */       
/* 3235 */       if (valueReference.isLoading()) {
/* 3236 */         valueReference.notifyNewValue(null);
/* 3237 */         return first;
/*      */       } 
/* 3239 */       return removeEntryFromChain(first, entry);
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     @GuardedBy("this")
/*      */     ReferenceEntry<K, V> removeEntryFromChain(ReferenceEntry<K, V> first, ReferenceEntry<K, V> entry) {
/* 3247 */       int newCount = this.count;
/* 3248 */       ReferenceEntry<K, V> newFirst = entry.getNext();
/* 3249 */       for (ReferenceEntry<K, V> e = first; e != entry; e = e.getNext()) {
/* 3250 */         ReferenceEntry<K, V> next = copyEntry(e, newFirst);
/* 3251 */         if (next != null) {
/* 3252 */           newFirst = next;
/*      */         } else {
/* 3254 */           removeCollectedEntry(e);
/* 3255 */           newCount--;
/*      */         } 
/*      */       } 
/* 3258 */       this.count = newCount;
/* 3259 */       return newFirst;
/*      */     }
/*      */     
/*      */     @GuardedBy("this")
/*      */     void removeCollectedEntry(ReferenceEntry<K, V> entry) {
/* 3264 */       enqueueNotification(entry
/* 3265 */           .getKey(), entry
/* 3266 */           .getHash(), (V)entry
/* 3267 */           .getValueReference().get(), entry
/* 3268 */           .getValueReference().getWeight(), RemovalCause.COLLECTED);
/*      */       
/* 3270 */       this.writeQueue.remove(entry);
/* 3271 */       this.accessQueue.remove(entry);
/*      */     }
/*      */ 
/*      */     
/*      */     boolean reclaimKey(ReferenceEntry<K, V> entry, int hash) {
/* 3276 */       lock();
/*      */       try {
/* 3278 */         int newCount = this.count - 1;
/* 3279 */         AtomicReferenceArray<ReferenceEntry<K, V>> table = this.table;
/* 3280 */         int index = hash & table.length() - 1;
/* 3281 */         ReferenceEntry<K, V> first = table.get(index);
/*      */         
/* 3283 */         for (ReferenceEntry<K, V> e = first; e != null; e = e.getNext()) {
/* 3284 */           if (e == entry) {
/* 3285 */             this.modCount++;
/*      */             
/* 3287 */             ReferenceEntry<K, V> newFirst = removeValueFromChain(first, e, e
/*      */ 
/*      */                 
/* 3290 */                 .getKey(), hash, (V)e
/*      */                 
/* 3292 */                 .getValueReference().get(), e
/* 3293 */                 .getValueReference(), RemovalCause.COLLECTED);
/*      */             
/* 3295 */             newCount = this.count - 1;
/* 3296 */             table.set(index, newFirst);
/* 3297 */             this.count = newCount;
/* 3298 */             return true;
/*      */           } 
/*      */         } 
/*      */         
/* 3302 */         return false;
/*      */       } finally {
/* 3304 */         unlock();
/* 3305 */         postWriteCleanup();
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     boolean reclaimValue(K key, int hash, LocalCache.ValueReference<K, V> valueReference) {
/* 3311 */       lock();
/*      */       try {
/* 3313 */         int newCount = this.count - 1;
/* 3314 */         AtomicReferenceArray<ReferenceEntry<K, V>> table = this.table;
/* 3315 */         int index = hash & table.length() - 1;
/* 3316 */         ReferenceEntry<K, V> first = table.get(index);
/*      */         
/* 3318 */         for (ReferenceEntry<K, V> e = first; e != null; e = e.getNext()) {
/* 3319 */           K entryKey = e.getKey();
/* 3320 */           if (e.getHash() == hash && entryKey != null && this.map.keyEquivalence
/*      */             
/* 3322 */             .equivalent(key, entryKey)) {
/* 3323 */             LocalCache.ValueReference<K, V> v = e.getValueReference();
/* 3324 */             if (v == valueReference) {
/* 3325 */               this.modCount++;
/*      */               
/* 3327 */               ReferenceEntry<K, V> newFirst = removeValueFromChain(first, e, entryKey, hash, valueReference
/*      */ 
/*      */ 
/*      */ 
/*      */                   
/* 3332 */                   .get(), valueReference, RemovalCause.COLLECTED);
/*      */ 
/*      */               
/* 3335 */               newCount = this.count - 1;
/* 3336 */               table.set(index, newFirst);
/* 3337 */               this.count = newCount;
/* 3338 */               return true;
/*      */             } 
/* 3340 */             return false;
/*      */           } 
/*      */         } 
/*      */         
/* 3344 */         return false;
/*      */       } finally {
/* 3346 */         unlock();
/* 3347 */         if (!isHeldByCurrentThread()) {
/* 3348 */           postWriteCleanup();
/*      */         }
/*      */       } 
/*      */     }
/*      */     
/*      */     boolean removeLoadingValue(K key, int hash, LocalCache.LoadingValueReference<K, V> valueReference) {
/* 3354 */       lock();
/*      */       try {
/* 3356 */         AtomicReferenceArray<ReferenceEntry<K, V>> table = this.table;
/* 3357 */         int index = hash & table.length() - 1;
/* 3358 */         ReferenceEntry<K, V> first = table.get(index);
/*      */         
/* 3360 */         for (ReferenceEntry<K, V> e = first; e != null; e = e.getNext()) {
/* 3361 */           K entryKey = e.getKey();
/* 3362 */           if (e.getHash() == hash && entryKey != null && this.map.keyEquivalence
/*      */             
/* 3364 */             .equivalent(key, entryKey)) {
/* 3365 */             LocalCache.ValueReference<K, V> v = e.getValueReference();
/* 3366 */             if (v == valueReference) {
/* 3367 */               if (valueReference.isActive()) {
/* 3368 */                 e.setValueReference(valueReference.getOldValue());
/*      */               } else {
/* 3370 */                 ReferenceEntry<K, V> newFirst = removeEntryFromChain(first, e);
/* 3371 */                 table.set(index, newFirst);
/*      */               } 
/* 3373 */               return true;
/*      */             } 
/* 3375 */             return false;
/*      */           } 
/*      */         } 
/*      */         
/* 3379 */         return false;
/*      */       } finally {
/* 3381 */         unlock();
/* 3382 */         postWriteCleanup();
/*      */       } 
/*      */     }
/*      */     
/*      */     @VisibleForTesting
/*      */     @GuardedBy("this")
/*      */     boolean removeEntry(ReferenceEntry<K, V> entry, int hash, RemovalCause cause) {
/* 3389 */       int newCount = this.count - 1;
/* 3390 */       AtomicReferenceArray<ReferenceEntry<K, V>> table = this.table;
/* 3391 */       int index = hash & table.length() - 1;
/* 3392 */       ReferenceEntry<K, V> first = table.get(index);
/*      */       
/* 3394 */       for (ReferenceEntry<K, V> e = first; e != null; e = e.getNext()) {
/* 3395 */         if (e == entry) {
/* 3396 */           this.modCount++;
/*      */           
/* 3398 */           ReferenceEntry<K, V> newFirst = removeValueFromChain(first, e, e
/*      */ 
/*      */               
/* 3401 */               .getKey(), hash, (V)e
/*      */               
/* 3403 */               .getValueReference().get(), e
/* 3404 */               .getValueReference(), cause);
/*      */           
/* 3406 */           newCount = this.count - 1;
/* 3407 */           table.set(index, newFirst);
/* 3408 */           this.count = newCount;
/* 3409 */           return true;
/*      */         } 
/*      */       } 
/*      */       
/* 3413 */       return false;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     void postReadCleanup() {
/* 3421 */       if ((this.readCount.incrementAndGet() & 0x3F) == 0) {
/* 3422 */         cleanUp();
/*      */       }
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     @GuardedBy("this")
/*      */     void preWriteCleanup(long now) {
/* 3434 */       runLockedCleanup(now);
/*      */     }
/*      */ 
/*      */     
/*      */     void postWriteCleanup() {
/* 3439 */       runUnlockedCleanup();
/*      */     }
/*      */     
/*      */     void cleanUp() {
/* 3443 */       long now = this.map.ticker.read();
/* 3444 */       runLockedCleanup(now);
/* 3445 */       runUnlockedCleanup();
/*      */     }
/*      */     
/*      */     void runLockedCleanup(long now) {
/* 3449 */       if (tryLock()) {
/*      */         try {
/* 3451 */           drainReferenceQueues();
/* 3452 */           expireEntries(now);
/* 3453 */           this.readCount.set(0);
/*      */         } finally {
/* 3455 */           unlock();
/*      */         } 
/*      */       }
/*      */     }
/*      */ 
/*      */     
/*      */     void runUnlockedCleanup() {
/* 3462 */       if (!isHeldByCurrentThread()) {
/* 3463 */         this.map.processPendingNotifications();
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */   static class LoadingValueReference<K, V>
/*      */     implements ValueReference<K, V>
/*      */   {
/*      */     volatile LocalCache.ValueReference<K, V> oldValue;
/* 3472 */     final SettableFuture<V> futureValue = SettableFuture.create();
/* 3473 */     final Stopwatch stopwatch = Stopwatch.createUnstarted();
/*      */     
/*      */     public LoadingValueReference() {
/* 3476 */       this(null);
/*      */     }
/*      */     
/*      */     public LoadingValueReference(LocalCache.ValueReference<K, V> oldValue) {
/* 3480 */       this.oldValue = (oldValue == null) ? LocalCache.<K, V>unset() : oldValue;
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean isLoading() {
/* 3485 */       return true;
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean isActive() {
/* 3490 */       return this.oldValue.isActive();
/*      */     }
/*      */ 
/*      */     
/*      */     public int getWeight() {
/* 3495 */       return this.oldValue.getWeight();
/*      */     }
/*      */     
/*      */     public boolean set(V newValue) {
/* 3499 */       return this.futureValue.set(newValue);
/*      */     }
/*      */     
/*      */     public boolean setException(Throwable t) {
/* 3503 */       return this.futureValue.setException(t);
/*      */     }
/*      */     
/*      */     private ListenableFuture<V> fullyFailedFuture(Throwable t) {
/* 3507 */       return Futures.immediateFailedFuture(t);
/*      */     }
/*      */ 
/*      */     
/*      */     public void notifyNewValue(V newValue) {
/* 3512 */       if (newValue != null) {
/*      */ 
/*      */         
/* 3515 */         set(newValue);
/*      */       } else {
/*      */         
/* 3518 */         this.oldValue = LocalCache.unset();
/*      */       } 
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     public ListenableFuture<V> loadFuture(K key, CacheLoader<? super K, V> loader) {
/*      */       try {
/* 3526 */         this.stopwatch.start();
/* 3527 */         V previousValue = this.oldValue.get();
/* 3528 */         if (previousValue == null) {
/* 3529 */           V v = loader.load(key);
/* 3530 */           return set(v) ? (ListenableFuture<V>)this.futureValue : Futures.immediateFuture(v);
/*      */         } 
/* 3532 */         ListenableFuture<V> newValue = loader.reload(key, previousValue);
/* 3533 */         if (newValue == null) {
/* 3534 */           return Futures.immediateFuture(null);
/*      */         }
/*      */ 
/*      */         
/* 3538 */         return Futures.transform(newValue, new Function<V, V>()
/*      */             {
/*      */               
/*      */               public V apply(V newValue)
/*      */               {
/* 3543 */                 LocalCache.LoadingValueReference.this.set(newValue);
/* 3544 */                 return newValue;
/*      */               }
/*      */             }, 
/* 3547 */             MoreExecutors.directExecutor());
/* 3548 */       } catch (Throwable t) {
/* 3549 */         ListenableFuture<V> result = setException(t) ? (ListenableFuture<V>)this.futureValue : fullyFailedFuture(t);
/* 3550 */         if (t instanceof InterruptedException) {
/* 3551 */           Thread.currentThread().interrupt();
/*      */         }
/* 3553 */         return result;
/*      */       } 
/*      */     }
/*      */     public V compute(K key, BiFunction<? super K, ? super V, ? extends V> function) {
/*      */       V previousValue, newValue;
/* 3558 */       this.stopwatch.start();
/*      */       
/*      */       try {
/* 3561 */         previousValue = this.oldValue.waitForValue();
/* 3562 */       } catch (ExecutionException e) {
/* 3563 */         previousValue = null;
/*      */       } 
/*      */       
/*      */       try {
/* 3567 */         newValue = function.apply(key, previousValue);
/* 3568 */       } catch (Throwable th) {
/* 3569 */         setException(th);
/* 3570 */         throw th;
/*      */       } 
/* 3572 */       set(newValue);
/* 3573 */       return newValue;
/*      */     }
/*      */     
/*      */     public long elapsedNanos() {
/* 3577 */       return this.stopwatch.elapsed(TimeUnit.NANOSECONDS);
/*      */     }
/*      */ 
/*      */     
/*      */     public V waitForValue() throws ExecutionException {
/* 3582 */       return (V)Uninterruptibles.getUninterruptibly((Future)this.futureValue);
/*      */     }
/*      */ 
/*      */     
/*      */     public V get() {
/* 3587 */       return this.oldValue.get();
/*      */     }
/*      */     
/*      */     public LocalCache.ValueReference<K, V> getOldValue() {
/* 3591 */       return this.oldValue;
/*      */     }
/*      */ 
/*      */     
/*      */     public ReferenceEntry<K, V> getEntry() {
/* 3596 */       return null;
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     public LocalCache.ValueReference<K, V> copyFor(ReferenceQueue<V> queue, V value, ReferenceEntry<K, V> entry) {
/* 3602 */       return this;
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   static final class WriteQueue<K, V>
/*      */     extends AbstractQueue<ReferenceEntry<K, V>>
/*      */   {
/* 3620 */     final ReferenceEntry<K, V> head = new LocalCache.AbstractReferenceEntry<K, V>()
/*      */       {
/*      */         
/*      */         public long getWriteTime()
/*      */         {
/* 3625 */           return Long.MAX_VALUE;
/*      */         }
/*      */ 
/*      */         
/*      */         public void setWriteTime(long time) {}
/*      */         
/* 3631 */         ReferenceEntry<K, V> nextWrite = this;
/*      */ 
/*      */         
/*      */         public ReferenceEntry<K, V> getNextInWriteQueue() {
/* 3635 */           return this.nextWrite;
/*      */         }
/*      */ 
/*      */         
/*      */         public void setNextInWriteQueue(ReferenceEntry<K, V> next) {
/* 3640 */           this.nextWrite = next;
/*      */         }
/*      */         
/* 3643 */         ReferenceEntry<K, V> previousWrite = this;
/*      */ 
/*      */         
/*      */         public ReferenceEntry<K, V> getPreviousInWriteQueue() {
/* 3647 */           return this.previousWrite;
/*      */         }
/*      */ 
/*      */         
/*      */         public void setPreviousInWriteQueue(ReferenceEntry<K, V> previous) {
/* 3652 */           this.previousWrite = previous;
/*      */         }
/*      */       };
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public boolean offer(ReferenceEntry<K, V> entry) {
/* 3661 */       LocalCache.connectWriteOrder(entry.getPreviousInWriteQueue(), entry.getNextInWriteQueue());
/*      */ 
/*      */       
/* 3664 */       LocalCache.connectWriteOrder(this.head.getPreviousInWriteQueue(), entry);
/* 3665 */       LocalCache.connectWriteOrder(entry, this.head);
/*      */       
/* 3667 */       return true;
/*      */     }
/*      */ 
/*      */     
/*      */     public ReferenceEntry<K, V> peek() {
/* 3672 */       ReferenceEntry<K, V> next = this.head.getNextInWriteQueue();
/* 3673 */       return (next == this.head) ? null : next;
/*      */     }
/*      */ 
/*      */     
/*      */     public ReferenceEntry<K, V> poll() {
/* 3678 */       ReferenceEntry<K, V> next = this.head.getNextInWriteQueue();
/* 3679 */       if (next == this.head) {
/* 3680 */         return null;
/*      */       }
/*      */       
/* 3683 */       remove(next);
/* 3684 */       return next;
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     public boolean remove(Object o) {
/* 3690 */       ReferenceEntry<K, V> e = (ReferenceEntry<K, V>)o;
/* 3691 */       ReferenceEntry<K, V> previous = e.getPreviousInWriteQueue();
/* 3692 */       ReferenceEntry<K, V> next = e.getNextInWriteQueue();
/* 3693 */       LocalCache.connectWriteOrder(previous, next);
/* 3694 */       LocalCache.nullifyWriteOrder(e);
/*      */       
/* 3696 */       return (next != LocalCache.NullEntry.INSTANCE);
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     public boolean contains(Object o) {
/* 3702 */       ReferenceEntry<K, V> e = (ReferenceEntry<K, V>)o;
/* 3703 */       return (e.getNextInWriteQueue() != LocalCache.NullEntry.INSTANCE);
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean isEmpty() {
/* 3708 */       return (this.head.getNextInWriteQueue() == this.head);
/*      */     }
/*      */ 
/*      */     
/*      */     public int size() {
/* 3713 */       int size = 0;
/* 3714 */       ReferenceEntry<K, V> e = this.head.getNextInWriteQueue();
/* 3715 */       for (; e != this.head; 
/* 3716 */         e = e.getNextInWriteQueue()) {
/* 3717 */         size++;
/*      */       }
/* 3719 */       return size;
/*      */     }
/*      */ 
/*      */     
/*      */     public void clear() {
/* 3724 */       ReferenceEntry<K, V> e = this.head.getNextInWriteQueue();
/* 3725 */       while (e != this.head) {
/* 3726 */         ReferenceEntry<K, V> next = e.getNextInWriteQueue();
/* 3727 */         LocalCache.nullifyWriteOrder(e);
/* 3728 */         e = next;
/*      */       } 
/*      */       
/* 3731 */       this.head.setNextInWriteQueue(this.head);
/* 3732 */       this.head.setPreviousInWriteQueue(this.head);
/*      */     }
/*      */ 
/*      */     
/*      */     public Iterator<ReferenceEntry<K, V>> iterator() {
/* 3737 */       return (Iterator<ReferenceEntry<K, V>>)new AbstractSequentialIterator<ReferenceEntry<K, V>>(peek())
/*      */         {
/*      */           protected ReferenceEntry<K, V> computeNext(ReferenceEntry<K, V> previous) {
/* 3740 */             ReferenceEntry<K, V> next = previous.getNextInWriteQueue();
/* 3741 */             return (next == LocalCache.WriteQueue.this.head) ? null : next;
/*      */           }
/*      */         };
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   static final class AccessQueue<K, V>
/*      */     extends AbstractQueue<ReferenceEntry<K, V>>
/*      */   {
/* 3759 */     final ReferenceEntry<K, V> head = new LocalCache.AbstractReferenceEntry<K, V>()
/*      */       {
/*      */         
/*      */         public long getAccessTime()
/*      */         {
/* 3764 */           return Long.MAX_VALUE;
/*      */         }
/*      */ 
/*      */         
/*      */         public void setAccessTime(long time) {}
/*      */         
/* 3770 */         ReferenceEntry<K, V> nextAccess = this;
/*      */ 
/*      */         
/*      */         public ReferenceEntry<K, V> getNextInAccessQueue() {
/* 3774 */           return this.nextAccess;
/*      */         }
/*      */ 
/*      */         
/*      */         public void setNextInAccessQueue(ReferenceEntry<K, V> next) {
/* 3779 */           this.nextAccess = next;
/*      */         }
/*      */         
/* 3782 */         ReferenceEntry<K, V> previousAccess = this;
/*      */ 
/*      */         
/*      */         public ReferenceEntry<K, V> getPreviousInAccessQueue() {
/* 3786 */           return this.previousAccess;
/*      */         }
/*      */ 
/*      */         
/*      */         public void setPreviousInAccessQueue(ReferenceEntry<K, V> previous) {
/* 3791 */           this.previousAccess = previous;
/*      */         }
/*      */       };
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public boolean offer(ReferenceEntry<K, V> entry) {
/* 3800 */       LocalCache.connectAccessOrder(entry.getPreviousInAccessQueue(), entry.getNextInAccessQueue());
/*      */ 
/*      */       
/* 3803 */       LocalCache.connectAccessOrder(this.head.getPreviousInAccessQueue(), entry);
/* 3804 */       LocalCache.connectAccessOrder(entry, this.head);
/*      */       
/* 3806 */       return true;
/*      */     }
/*      */ 
/*      */     
/*      */     public ReferenceEntry<K, V> peek() {
/* 3811 */       ReferenceEntry<K, V> next = this.head.getNextInAccessQueue();
/* 3812 */       return (next == this.head) ? null : next;
/*      */     }
/*      */ 
/*      */     
/*      */     public ReferenceEntry<K, V> poll() {
/* 3817 */       ReferenceEntry<K, V> next = this.head.getNextInAccessQueue();
/* 3818 */       if (next == this.head) {
/* 3819 */         return null;
/*      */       }
/*      */       
/* 3822 */       remove(next);
/* 3823 */       return next;
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     public boolean remove(Object o) {
/* 3829 */       ReferenceEntry<K, V> e = (ReferenceEntry<K, V>)o;
/* 3830 */       ReferenceEntry<K, V> previous = e.getPreviousInAccessQueue();
/* 3831 */       ReferenceEntry<K, V> next = e.getNextInAccessQueue();
/* 3832 */       LocalCache.connectAccessOrder(previous, next);
/* 3833 */       LocalCache.nullifyAccessOrder(e);
/*      */       
/* 3835 */       return (next != LocalCache.NullEntry.INSTANCE);
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     public boolean contains(Object o) {
/* 3841 */       ReferenceEntry<K, V> e = (ReferenceEntry<K, V>)o;
/* 3842 */       return (e.getNextInAccessQueue() != LocalCache.NullEntry.INSTANCE);
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean isEmpty() {
/* 3847 */       return (this.head.getNextInAccessQueue() == this.head);
/*      */     }
/*      */ 
/*      */     
/*      */     public int size() {
/* 3852 */       int size = 0;
/* 3853 */       ReferenceEntry<K, V> e = this.head.getNextInAccessQueue();
/* 3854 */       for (; e != this.head; 
/* 3855 */         e = e.getNextInAccessQueue()) {
/* 3856 */         size++;
/*      */       }
/* 3858 */       return size;
/*      */     }
/*      */ 
/*      */     
/*      */     public void clear() {
/* 3863 */       ReferenceEntry<K, V> e = this.head.getNextInAccessQueue();
/* 3864 */       while (e != this.head) {
/* 3865 */         ReferenceEntry<K, V> next = e.getNextInAccessQueue();
/* 3866 */         LocalCache.nullifyAccessOrder(e);
/* 3867 */         e = next;
/*      */       } 
/*      */       
/* 3870 */       this.head.setNextInAccessQueue(this.head);
/* 3871 */       this.head.setPreviousInAccessQueue(this.head);
/*      */     }
/*      */ 
/*      */     
/*      */     public Iterator<ReferenceEntry<K, V>> iterator() {
/* 3876 */       return (Iterator<ReferenceEntry<K, V>>)new AbstractSequentialIterator<ReferenceEntry<K, V>>(peek())
/*      */         {
/*      */           protected ReferenceEntry<K, V> computeNext(ReferenceEntry<K, V> previous) {
/* 3879 */             ReferenceEntry<K, V> next = previous.getNextInAccessQueue();
/* 3880 */             return (next == LocalCache.AccessQueue.this.head) ? null : next;
/*      */           }
/*      */         };
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void cleanUp() {
/* 3889 */     for (Segment<?, ?> segment : this.segments) {
/* 3890 */       segment.cleanUp();
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean isEmpty() {
/* 3905 */     long sum = 0L;
/* 3906 */     Segment<K, V>[] segments = this.segments; int i;
/* 3907 */     for (i = 0; i < segments.length; i++) {
/* 3908 */       if ((segments[i]).count != 0) {
/* 3909 */         return false;
/*      */       }
/* 3911 */       sum += (segments[i]).modCount;
/*      */     } 
/*      */     
/* 3914 */     if (sum != 0L) {
/* 3915 */       for (i = 0; i < segments.length; i++) {
/* 3916 */         if ((segments[i]).count != 0) {
/* 3917 */           return false;
/*      */         }
/* 3919 */         sum -= (segments[i]).modCount;
/*      */       } 
/* 3921 */       if (sum != 0L) {
/* 3922 */         return false;
/*      */       }
/*      */     } 
/* 3925 */     return true;
/*      */   }
/*      */   
/*      */   long longSize() {
/* 3929 */     Segment<K, V>[] segments = this.segments;
/* 3930 */     long sum = 0L;
/* 3931 */     for (int i = 0; i < segments.length; i++) {
/* 3932 */       sum += Math.max(0, (segments[i]).count);
/*      */     }
/* 3934 */     return sum;
/*      */   }
/*      */ 
/*      */   
/*      */   public int size() {
/* 3939 */     return Ints.saturatedCast(longSize());
/*      */   }
/*      */ 
/*      */   
/*      */   public V get(Object key) {
/* 3944 */     if (key == null) {
/* 3945 */       return null;
/*      */     }
/* 3947 */     int hash = hash(key);
/* 3948 */     return segmentFor(hash).get(key, hash);
/*      */   }
/*      */   
/*      */   V get(K key, CacheLoader<? super K, V> loader) throws ExecutionException {
/* 3952 */     int hash = hash(Preconditions.checkNotNull(key));
/* 3953 */     return segmentFor(hash).get(key, hash, loader);
/*      */   }
/*      */   
/*      */   public V getIfPresent(Object key) {
/* 3957 */     int hash = hash(Preconditions.checkNotNull(key));
/* 3958 */     V value = segmentFor(hash).get(key, hash);
/* 3959 */     if (value == null) {
/* 3960 */       this.globalStatsCounter.recordMisses(1);
/*      */     } else {
/* 3962 */       this.globalStatsCounter.recordHits(1);
/*      */     } 
/* 3964 */     return value;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public V getOrDefault(Object key, V defaultValue) {
/* 3971 */     V result = get(key);
/* 3972 */     return (result != null) ? result : defaultValue;
/*      */   }
/*      */   
/*      */   V getOrLoad(K key) throws ExecutionException {
/* 3976 */     return get(key, this.defaultLoader);
/*      */   }
/*      */   
/*      */   ImmutableMap<K, V> getAllPresent(Iterable<?> keys) {
/* 3980 */     int hits = 0;
/* 3981 */     int misses = 0;
/*      */     
/* 3983 */     Map<K, V> result = Maps.newLinkedHashMap();
/* 3984 */     for (Object key : keys) {
/* 3985 */       V value = get(key);
/* 3986 */       if (value == null) {
/* 3987 */         misses++;
/*      */         
/*      */         continue;
/*      */       } 
/* 3991 */       K castKey = (K)key;
/* 3992 */       result.put(castKey, value);
/* 3993 */       hits++;
/*      */     } 
/*      */     
/* 3996 */     this.globalStatsCounter.recordHits(hits);
/* 3997 */     this.globalStatsCounter.recordMisses(misses);
/* 3998 */     return ImmutableMap.copyOf(result);
/*      */   }
/*      */   
/*      */   ImmutableMap<K, V> getAll(Iterable<? extends K> keys) throws ExecutionException {
/* 4002 */     int hits = 0;
/* 4003 */     int misses = 0;
/*      */     
/* 4005 */     Map<K, V> result = Maps.newLinkedHashMap();
/* 4006 */     Set<K> keysToLoad = Sets.newLinkedHashSet();
/* 4007 */     for (K key : keys) {
/* 4008 */       V value = get(key);
/* 4009 */       if (!result.containsKey(key)) {
/* 4010 */         result.put(key, value);
/* 4011 */         if (value == null) {
/* 4012 */           misses++;
/* 4013 */           keysToLoad.add(key); continue;
/*      */         } 
/* 4015 */         hits++;
/*      */       } 
/*      */     } 
/*      */ 
/*      */     
/*      */     try {
/* 4021 */       if (!keysToLoad.isEmpty()) {
/*      */         try {
/* 4023 */           Map<K, V> newEntries = loadAll(keysToLoad, this.defaultLoader);
/* 4024 */           for (K key : keysToLoad) {
/* 4025 */             V value = newEntries.get(key);
/* 4026 */             if (value == null) {
/* 4027 */               throw new CacheLoader.InvalidCacheLoadException("loadAll failed to return a value for " + key);
/*      */             }
/* 4029 */             result.put(key, value);
/*      */           } 
/* 4031 */         } catch (UnsupportedLoadingOperationException e) {
/*      */           
/* 4033 */           for (K key : keysToLoad) {
/* 4034 */             misses--;
/* 4035 */             result.put(key, get(key, this.defaultLoader));
/*      */           } 
/*      */         } 
/*      */       }
/* 4039 */       return ImmutableMap.copyOf(result);
/*      */     } finally {
/* 4041 */       this.globalStatsCounter.recordHits(hits);
/* 4042 */       this.globalStatsCounter.recordMisses(misses);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   Map<K, V> loadAll(Set<? extends K> keys, CacheLoader<? super K, V> loader) throws ExecutionException {
/*      */     Map<K, V> result;
/* 4053 */     Preconditions.checkNotNull(loader);
/* 4054 */     Preconditions.checkNotNull(keys);
/* 4055 */     Stopwatch stopwatch = Stopwatch.createStarted();
/*      */     
/* 4057 */     boolean success = false;
/*      */     
/*      */     try {
/* 4060 */       Map<K, V> map = (Map)loader.loadAll(keys);
/* 4061 */       result = map;
/* 4062 */       success = true;
/* 4063 */     } catch (UnsupportedLoadingOperationException e) {
/* 4064 */       success = true;
/* 4065 */       throw e;
/* 4066 */     } catch (InterruptedException e) {
/* 4067 */       Thread.currentThread().interrupt();
/* 4068 */       throw new ExecutionException(e);
/* 4069 */     } catch (RuntimeException e) {
/* 4070 */       throw new UncheckedExecutionException(e);
/* 4071 */     } catch (Exception e) {
/* 4072 */       throw new ExecutionException(e);
/* 4073 */     } catch (Error e) {
/* 4074 */       throw new ExecutionError(e);
/*      */     } finally {
/* 4076 */       if (!success) {
/* 4077 */         this.globalStatsCounter.recordLoadException(stopwatch.elapsed(TimeUnit.NANOSECONDS));
/*      */       }
/*      */     } 
/*      */     
/* 4081 */     if (result == null) {
/* 4082 */       this.globalStatsCounter.recordLoadException(stopwatch.elapsed(TimeUnit.NANOSECONDS));
/* 4083 */       throw new CacheLoader.InvalidCacheLoadException(loader + " returned null map from loadAll");
/*      */     } 
/*      */     
/* 4086 */     stopwatch.stop();
/*      */     
/* 4088 */     boolean nullsPresent = false;
/* 4089 */     for (Map.Entry<K, V> entry : result.entrySet()) {
/* 4090 */       K key = entry.getKey();
/* 4091 */       V value = entry.getValue();
/* 4092 */       if (key == null || value == null) {
/*      */         
/* 4094 */         nullsPresent = true; continue;
/*      */       } 
/* 4096 */       put(key, value);
/*      */     } 
/*      */ 
/*      */     
/* 4100 */     if (nullsPresent) {
/* 4101 */       this.globalStatsCounter.recordLoadException(stopwatch.elapsed(TimeUnit.NANOSECONDS));
/* 4102 */       throw new CacheLoader.InvalidCacheLoadException(loader + " returned null keys or values from loadAll");
/*      */     } 
/*      */ 
/*      */     
/* 4106 */     this.globalStatsCounter.recordLoadSuccess(stopwatch.elapsed(TimeUnit.NANOSECONDS));
/* 4107 */     return result;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   ReferenceEntry<K, V> getEntry(Object key) {
/* 4116 */     if (key == null) {
/* 4117 */       return null;
/*      */     }
/* 4119 */     int hash = hash(key);
/* 4120 */     return segmentFor(hash).getEntry(key, hash);
/*      */   }
/*      */   
/*      */   void refresh(K key) {
/* 4124 */     int hash = hash(Preconditions.checkNotNull(key));
/* 4125 */     segmentFor(hash).refresh(key, hash, this.defaultLoader, false);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean containsKey(Object key) {
/* 4131 */     if (key == null) {
/* 4132 */       return false;
/*      */     }
/* 4134 */     int hash = hash(key);
/* 4135 */     return segmentFor(hash).containsKey(key, hash);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean containsValue(Object value) {
/* 4141 */     if (value == null) {
/* 4142 */       return false;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 4150 */     long now = this.ticker.read();
/* 4151 */     Segment<K, V>[] segments = this.segments;
/* 4152 */     long last = -1L;
/* 4153 */     for (int i = 0; i < 3; i++) {
/* 4154 */       long sum = 0L;
/* 4155 */       for (Segment<K, V> segment : segments) {
/*      */         
/* 4157 */         int unused = segment.count;
/*      */         
/* 4159 */         AtomicReferenceArray<ReferenceEntry<K, V>> table = segment.table;
/* 4160 */         for (int j = 0; j < table.length(); j++) {
/* 4161 */           for (ReferenceEntry<K, V> e = table.get(j); e != null; e = e.getNext()) {
/* 4162 */             V v = segment.getLiveValue(e, now);
/* 4163 */             if (v != null && this.valueEquivalence.equivalent(value, v)) {
/* 4164 */               return true;
/*      */             }
/*      */           } 
/*      */         } 
/* 4168 */         sum += segment.modCount;
/*      */       } 
/* 4170 */       if (sum == last) {
/*      */         break;
/*      */       }
/* 4173 */       last = sum;
/*      */     } 
/* 4175 */     return false;
/*      */   }
/*      */ 
/*      */   
/*      */   public V put(K key, V value) {
/* 4180 */     Preconditions.checkNotNull(key);
/* 4181 */     Preconditions.checkNotNull(value);
/* 4182 */     int hash = hash(key);
/* 4183 */     return segmentFor(hash).put(key, hash, value, false);
/*      */   }
/*      */ 
/*      */   
/*      */   public V putIfAbsent(K key, V value) {
/* 4188 */     Preconditions.checkNotNull(key);
/* 4189 */     Preconditions.checkNotNull(value);
/* 4190 */     int hash = hash(key);
/* 4191 */     return segmentFor(hash).put(key, hash, value, true);
/*      */   }
/*      */ 
/*      */   
/*      */   public V compute(K key, BiFunction<? super K, ? super V, ? extends V> function) {
/* 4196 */     Preconditions.checkNotNull(key);
/* 4197 */     Preconditions.checkNotNull(function);
/* 4198 */     int hash = hash(key);
/* 4199 */     return segmentFor(hash).compute(key, hash, function);
/*      */   }
/*      */ 
/*      */   
/*      */   public V computeIfAbsent(K key, Function<? super K, ? extends V> function) {
/* 4204 */     Preconditions.checkNotNull(key);
/* 4205 */     Preconditions.checkNotNull(function);
/* 4206 */     return compute(key, (k, oldValue) -> (oldValue == null) ? function.apply(key) : oldValue);
/*      */   }
/*      */ 
/*      */   
/*      */   public V computeIfPresent(K key, BiFunction<? super K, ? super V, ? extends V> function) {
/* 4211 */     Preconditions.checkNotNull(key);
/* 4212 */     Preconditions.checkNotNull(function);
/* 4213 */     return compute(key, (k, oldValue) -> (oldValue == null) ? null : function.apply(k, oldValue));
/*      */   }
/*      */ 
/*      */   
/*      */   public V merge(K key, V newValue, BiFunction<? super V, ? super V, ? extends V> function) {
/* 4218 */     Preconditions.checkNotNull(key);
/* 4219 */     Preconditions.checkNotNull(newValue);
/* 4220 */     Preconditions.checkNotNull(function);
/* 4221 */     return compute(key, (k, oldValue) -> (oldValue == null) ? newValue : function.apply(oldValue, newValue));
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void putAll(Map<? extends K, ? extends V> m) {
/* 4227 */     for (Map.Entry<? extends K, ? extends V> e : m.entrySet()) {
/* 4228 */       put(e.getKey(), e.getValue());
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   public V remove(Object key) {
/* 4234 */     if (key == null) {
/* 4235 */       return null;
/*      */     }
/* 4237 */     int hash = hash(key);
/* 4238 */     return segmentFor(hash).remove(key, hash);
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean remove(Object key, Object value) {
/* 4243 */     if (key == null || value == null) {
/* 4244 */       return false;
/*      */     }
/* 4246 */     int hash = hash(key);
/* 4247 */     return segmentFor(hash).remove(key, hash, value);
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean replace(K key, V oldValue, V newValue) {
/* 4252 */     Preconditions.checkNotNull(key);
/* 4253 */     Preconditions.checkNotNull(newValue);
/* 4254 */     if (oldValue == null) {
/* 4255 */       return false;
/*      */     }
/* 4257 */     int hash = hash(key);
/* 4258 */     return segmentFor(hash).replace(key, hash, oldValue, newValue);
/*      */   }
/*      */ 
/*      */   
/*      */   public V replace(K key, V value) {
/* 4263 */     Preconditions.checkNotNull(key);
/* 4264 */     Preconditions.checkNotNull(value);
/* 4265 */     int hash = hash(key);
/* 4266 */     return segmentFor(hash).replace(key, hash, value);
/*      */   }
/*      */ 
/*      */   
/*      */   public void clear() {
/* 4271 */     for (Segment<K, V> segment : this.segments) {
/* 4272 */       segment.clear();
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   void invalidateAll(Iterable<?> keys) {
/* 4278 */     for (Object key : keys) {
/* 4279 */       remove(key);
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Set<K> keySet() {
/* 4288 */     Set<K> ks = this.keySet;
/* 4289 */     return (ks != null) ? ks : (this.keySet = new KeySet(this));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Collection<V> values() {
/* 4297 */     Collection<V> vs = this.values;
/* 4298 */     return (vs != null) ? vs : (this.values = new Values(this));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @GwtIncompatible
/*      */   public Set<Map.Entry<K, V>> entrySet() {
/* 4307 */     Set<Map.Entry<K, V>> es = this.entrySet;
/* 4308 */     return (es != null) ? es : (this.entrySet = new EntrySet(this));
/*      */   }
/*      */ 
/*      */   
/*      */   abstract class HashIterator<T>
/*      */     implements Iterator<T>
/*      */   {
/*      */     int nextSegmentIndex;
/*      */     int nextTableIndex;
/*      */     LocalCache.Segment<K, V> currentSegment;
/*      */     AtomicReferenceArray<ReferenceEntry<K, V>> currentTable;
/*      */     ReferenceEntry<K, V> nextEntry;
/*      */     LocalCache<K, V>.WriteThroughEntry nextExternal;
/*      */     LocalCache<K, V>.WriteThroughEntry lastReturned;
/*      */     
/*      */     HashIterator() {
/* 4324 */       this.nextSegmentIndex = LocalCache.this.segments.length - 1;
/* 4325 */       this.nextTableIndex = -1;
/* 4326 */       advance();
/*      */     }
/*      */ 
/*      */     
/*      */     public abstract T next();
/*      */     
/*      */     final void advance() {
/* 4333 */       this.nextExternal = null;
/*      */       
/* 4335 */       if (nextInChain()) {
/*      */         return;
/*      */       }
/*      */       
/* 4339 */       if (nextInTable()) {
/*      */         return;
/*      */       }
/*      */       
/* 4343 */       while (this.nextSegmentIndex >= 0) {
/* 4344 */         this.currentSegment = LocalCache.this.segments[this.nextSegmentIndex--];
/* 4345 */         if (this.currentSegment.count != 0) {
/* 4346 */           this.currentTable = this.currentSegment.table;
/* 4347 */           this.nextTableIndex = this.currentTable.length() - 1;
/* 4348 */           if (nextInTable()) {
/*      */             return;
/*      */           }
/*      */         } 
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     boolean nextInChain() {
/* 4357 */       if (this.nextEntry != null) {
/* 4358 */         for (this.nextEntry = this.nextEntry.getNext(); this.nextEntry != null; this.nextEntry = this.nextEntry.getNext()) {
/* 4359 */           if (advanceTo(this.nextEntry)) {
/* 4360 */             return true;
/*      */           }
/*      */         } 
/*      */       }
/* 4364 */       return false;
/*      */     }
/*      */ 
/*      */     
/*      */     boolean nextInTable() {
/* 4369 */       while (this.nextTableIndex >= 0) {
/* 4370 */         if ((this.nextEntry = this.currentTable.get(this.nextTableIndex--)) != null && (
/* 4371 */           advanceTo(this.nextEntry) || nextInChain())) {
/* 4372 */           return true;
/*      */         }
/*      */       } 
/*      */       
/* 4376 */       return false;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     boolean advanceTo(ReferenceEntry<K, V> entry) {
/*      */       try {
/* 4385 */         long now = LocalCache.this.ticker.read();
/* 4386 */         K key = entry.getKey();
/* 4387 */         V value = LocalCache.this.getLiveValue(entry, now);
/* 4388 */         if (value != null) {
/* 4389 */           this.nextExternal = new LocalCache.WriteThroughEntry(key, value);
/* 4390 */           return true;
/*      */         } 
/*      */         
/* 4393 */         return false;
/*      */       } finally {
/*      */         
/* 4396 */         this.currentSegment.postReadCleanup();
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean hasNext() {
/* 4402 */       return (this.nextExternal != null);
/*      */     }
/*      */     
/*      */     LocalCache<K, V>.WriteThroughEntry nextEntry() {
/* 4406 */       if (this.nextExternal == null) {
/* 4407 */         throw new NoSuchElementException();
/*      */       }
/* 4409 */       this.lastReturned = this.nextExternal;
/* 4410 */       advance();
/* 4411 */       return this.lastReturned;
/*      */     }
/*      */ 
/*      */     
/*      */     public void remove() {
/* 4416 */       Preconditions.checkState((this.lastReturned != null));
/* 4417 */       LocalCache.this.remove(this.lastReturned.getKey());
/* 4418 */       this.lastReturned = null;
/*      */     }
/*      */   }
/*      */   
/*      */   final class KeyIterator
/*      */     extends HashIterator<K>
/*      */   {
/*      */     public K next() {
/* 4426 */       return nextEntry().getKey();
/*      */     }
/*      */   }
/*      */   
/*      */   final class ValueIterator
/*      */     extends HashIterator<V>
/*      */   {
/*      */     public V next() {
/* 4434 */       return nextEntry().getValue();
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   final class WriteThroughEntry
/*      */     implements Map.Entry<K, V>
/*      */   {
/*      */     final K key;
/*      */     
/*      */     V value;
/*      */     
/*      */     WriteThroughEntry(K key, V value) {
/* 4447 */       this.key = key;
/* 4448 */       this.value = value;
/*      */     }
/*      */ 
/*      */     
/*      */     public K getKey() {
/* 4453 */       return this.key;
/*      */     }
/*      */ 
/*      */     
/*      */     public V getValue() {
/* 4458 */       return this.value;
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     public boolean equals(Object object) {
/* 4464 */       if (object instanceof Map.Entry) {
/* 4465 */         Map.Entry<?, ?> that = (Map.Entry<?, ?>)object;
/* 4466 */         return (this.key.equals(that.getKey()) && this.value.equals(that.getValue()));
/*      */       } 
/* 4468 */       return false;
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     public int hashCode() {
/* 4474 */       return this.key.hashCode() ^ this.value.hashCode();
/*      */     }
/*      */ 
/*      */     
/*      */     public V setValue(V newValue) {
/* 4479 */       V oldValue = LocalCache.this.put(this.key, newValue);
/* 4480 */       this.value = newValue;
/* 4481 */       return oldValue;
/*      */     }
/*      */ 
/*      */     
/*      */     public String toString() {
/* 4486 */       return (new StringBuilder()).append(getKey()).append("=").append(getValue()).toString();
/*      */     }
/*      */   }
/*      */   
/*      */   final class EntryIterator
/*      */     extends HashIterator<Map.Entry<K, V>>
/*      */   {
/*      */     public Map.Entry<K, V> next() {
/* 4494 */       return nextEntry();
/*      */     } }
/*      */   
/*      */   abstract class AbstractCacheSet<T> extends AbstractSet<T> {
/*      */     @Weak
/*      */     final ConcurrentMap<?, ?> map;
/*      */     
/*      */     AbstractCacheSet(ConcurrentMap<?, ?> map) {
/* 4502 */       this.map = map;
/*      */     }
/*      */ 
/*      */     
/*      */     public int size() {
/* 4507 */       return this.map.size();
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean isEmpty() {
/* 4512 */       return this.map.isEmpty();
/*      */     }
/*      */ 
/*      */     
/*      */     public void clear() {
/* 4517 */       this.map.clear();
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public Object[] toArray() {
/* 4525 */       return LocalCache.toArrayList(this).toArray();
/*      */     }
/*      */ 
/*      */     
/*      */     public <E> E[] toArray(E[] a) {
/* 4530 */       return (E[])LocalCache.toArrayList(this).toArray((Object[])a);
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   private static <E> ArrayList<E> toArrayList(Collection<E> c) {
/* 4536 */     ArrayList<E> result = new ArrayList<>(c.size());
/* 4537 */     Iterators.addAll(result, c.iterator());
/* 4538 */     return result;
/*      */   }
/*      */   
/*      */   boolean removeIf(BiPredicate<? super K, ? super V> filter) {
/* 4542 */     Preconditions.checkNotNull(filter);
/* 4543 */     boolean changed = false;
/* 4544 */     label17: for (K key : keySet()) {
/*      */       while (true) {
/* 4546 */         V value = get(key);
/* 4547 */         if (value != null) { if (!filter.test(key, value))
/*      */             continue label17; 
/* 4549 */           if (remove(key, value))
/* 4550 */             changed = true;  continue; }
/*      */         
/*      */         continue label17;
/*      */       } 
/*      */     } 
/* 4555 */     return changed;
/*      */   }
/*      */   
/*      */   final class KeySet
/*      */     extends AbstractCacheSet<K>
/*      */   {
/*      */     KeySet(ConcurrentMap<?, ?> map) {
/* 4562 */       super(map);
/*      */     }
/*      */ 
/*      */     
/*      */     public Iterator<K> iterator() {
/* 4567 */       return new LocalCache.KeyIterator();
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean contains(Object o) {
/* 4572 */       return this.map.containsKey(o);
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean remove(Object o) {
/* 4577 */       return (this.map.remove(o) != null);
/*      */     }
/*      */   }
/*      */   
/*      */   final class Values
/*      */     extends AbstractCollection<V> {
/*      */     private final ConcurrentMap<?, ?> map;
/*      */     
/*      */     Values(ConcurrentMap<?, ?> map) {
/* 4586 */       this.map = map;
/*      */     }
/*      */ 
/*      */     
/*      */     public int size() {
/* 4591 */       return this.map.size();
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean isEmpty() {
/* 4596 */       return this.map.isEmpty();
/*      */     }
/*      */ 
/*      */     
/*      */     public void clear() {
/* 4601 */       this.map.clear();
/*      */     }
/*      */ 
/*      */     
/*      */     public Iterator<V> iterator() {
/* 4606 */       return new LocalCache.ValueIterator();
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean removeIf(Predicate<? super V> filter) {
/* 4611 */       Preconditions.checkNotNull(filter);
/* 4612 */       return LocalCache.this.removeIf((k, v) -> filter.test(v));
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean contains(Object o) {
/* 4617 */       return this.map.containsValue(o);
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public Object[] toArray() {
/* 4625 */       return LocalCache.toArrayList(this).toArray();
/*      */     }
/*      */ 
/*      */     
/*      */     public <E> E[] toArray(E[] a) {
/* 4630 */       return (E[])LocalCache.toArrayList(this).toArray((Object[])a);
/*      */     }
/*      */   }
/*      */   
/*      */   final class EntrySet
/*      */     extends AbstractCacheSet<Map.Entry<K, V>>
/*      */   {
/*      */     EntrySet(ConcurrentMap<?, ?> map) {
/* 4638 */       super(map);
/*      */     }
/*      */ 
/*      */     
/*      */     public Iterator<Map.Entry<K, V>> iterator() {
/* 4643 */       return new LocalCache.EntryIterator();
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean removeIf(Predicate<? super Map.Entry<K, V>> filter) {
/* 4648 */       Preconditions.checkNotNull(filter);
/* 4649 */       return LocalCache.this.removeIf((k, v) -> filter.test(Maps.immutableEntry(k, v)));
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean contains(Object o) {
/* 4654 */       if (!(o instanceof Map.Entry)) {
/* 4655 */         return false;
/*      */       }
/* 4657 */       Map.Entry<?, ?> e = (Map.Entry<?, ?>)o;
/* 4658 */       Object key = e.getKey();
/* 4659 */       if (key == null) {
/* 4660 */         return false;
/*      */       }
/* 4662 */       V v = (V)LocalCache.this.get(key);
/*      */       
/* 4664 */       return (v != null && LocalCache.this.valueEquivalence.equivalent(e.getValue(), v));
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean remove(Object o) {
/* 4669 */       if (!(o instanceof Map.Entry)) {
/* 4670 */         return false;
/*      */       }
/* 4672 */       Map.Entry<?, ?> e = (Map.Entry<?, ?>)o;
/* 4673 */       Object key = e.getKey();
/* 4674 */       return (key != null && LocalCache.this.remove(key, e.getValue()));
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   static class ManualSerializationProxy<K, V>
/*      */     extends ForwardingCache<K, V>
/*      */     implements Serializable
/*      */   {
/*      */     private static final long serialVersionUID = 1L;
/*      */     
/*      */     final LocalCache.Strength keyStrength;
/*      */     
/*      */     final LocalCache.Strength valueStrength;
/*      */     
/*      */     final Equivalence<Object> keyEquivalence;
/*      */     
/*      */     final Equivalence<Object> valueEquivalence;
/*      */     
/*      */     final long expireAfterWriteNanos;
/*      */     
/*      */     final long expireAfterAccessNanos;
/*      */     
/*      */     final long maxWeight;
/*      */     
/*      */     final Weigher<K, V> weigher;
/*      */     
/*      */     final int concurrencyLevel;
/*      */     final RemovalListener<? super K, ? super V> removalListener;
/*      */     final Ticker ticker;
/*      */     final CacheLoader<? super K, V> loader;
/*      */     transient Cache<K, V> delegate;
/*      */     
/*      */     ManualSerializationProxy(LocalCache<K, V> cache) {
/* 4708 */       this(cache.keyStrength, cache.valueStrength, cache.keyEquivalence, cache.valueEquivalence, cache.expireAfterWriteNanos, cache.expireAfterAccessNanos, cache.maxWeight, cache.weigher, cache.concurrencyLevel, cache.removalListener, cache.ticker, cache.defaultLoader);
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     private ManualSerializationProxy(LocalCache.Strength keyStrength, LocalCache.Strength valueStrength, Equivalence<Object> keyEquivalence, Equivalence<Object> valueEquivalence, long expireAfterWriteNanos, long expireAfterAccessNanos, long maxWeight, Weigher<K, V> weigher, int concurrencyLevel, RemovalListener<? super K, ? super V> removalListener, Ticker ticker, CacheLoader<? super K, V> loader) {
/* 4736 */       this.keyStrength = keyStrength;
/* 4737 */       this.valueStrength = valueStrength;
/* 4738 */       this.keyEquivalence = keyEquivalence;
/* 4739 */       this.valueEquivalence = valueEquivalence;
/* 4740 */       this.expireAfterWriteNanos = expireAfterWriteNanos;
/* 4741 */       this.expireAfterAccessNanos = expireAfterAccessNanos;
/* 4742 */       this.maxWeight = maxWeight;
/* 4743 */       this.weigher = weigher;
/* 4744 */       this.concurrencyLevel = concurrencyLevel;
/* 4745 */       this.removalListener = removalListener;
/* 4746 */       this.ticker = (ticker == Ticker.systemTicker() || ticker == CacheBuilder.NULL_TICKER) ? null : ticker;
/* 4747 */       this.loader = loader;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     CacheBuilder<K, V> recreateCacheBuilder() {
/* 4758 */       CacheBuilder<K, V> builder = CacheBuilder.newBuilder().setKeyStrength(this.keyStrength).setValueStrength(this.valueStrength).keyEquivalence(this.keyEquivalence).valueEquivalence(this.valueEquivalence).concurrencyLevel(this.concurrencyLevel).removalListener(this.removalListener);
/* 4759 */       builder.strictParsing = false;
/* 4760 */       if (this.expireAfterWriteNanos > 0L) {
/* 4761 */         builder.expireAfterWrite(this.expireAfterWriteNanos, TimeUnit.NANOSECONDS);
/*      */       }
/* 4763 */       if (this.expireAfterAccessNanos > 0L) {
/* 4764 */         builder.expireAfterAccess(this.expireAfterAccessNanos, TimeUnit.NANOSECONDS);
/*      */       }
/* 4766 */       if (this.weigher != CacheBuilder.OneWeigher.INSTANCE) {
/* 4767 */         builder.weigher(this.weigher);
/* 4768 */         if (this.maxWeight != -1L) {
/* 4769 */           builder.maximumWeight(this.maxWeight);
/*      */         }
/*      */       }
/* 4772 */       else if (this.maxWeight != -1L) {
/* 4773 */         builder.maximumSize(this.maxWeight);
/*      */       } 
/*      */       
/* 4776 */       if (this.ticker != null) {
/* 4777 */         builder.ticker(this.ticker);
/*      */       }
/* 4779 */       return builder;
/*      */     }
/*      */     
/*      */     private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
/* 4783 */       in.defaultReadObject();
/* 4784 */       CacheBuilder<K, V> builder = recreateCacheBuilder();
/* 4785 */       this.delegate = builder.build();
/*      */     }
/*      */     
/*      */     private Object readResolve() {
/* 4789 */       return this.delegate;
/*      */     }
/*      */ 
/*      */     
/*      */     protected Cache<K, V> delegate() {
/* 4794 */       return this.delegate;
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   static final class LoadingSerializationProxy<K, V>
/*      */     extends ManualSerializationProxy<K, V>
/*      */     implements LoadingCache<K, V>, Serializable
/*      */   {
/*      */     private static final long serialVersionUID = 1L;
/*      */ 
/*      */     
/*      */     transient LoadingCache<K, V> autoDelegate;
/*      */ 
/*      */ 
/*      */     
/*      */     LoadingSerializationProxy(LocalCache<K, V> cache) {
/* 4813 */       super(cache);
/*      */     }
/*      */     
/*      */     private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
/* 4817 */       in.defaultReadObject();
/* 4818 */       CacheBuilder<K, V> builder = recreateCacheBuilder();
/* 4819 */       this.autoDelegate = builder.build(this.loader);
/*      */     }
/*      */ 
/*      */     
/*      */     public V get(K key) throws ExecutionException {
/* 4824 */       return this.autoDelegate.get(key);
/*      */     }
/*      */ 
/*      */     
/*      */     public V getUnchecked(K key) {
/* 4829 */       return this.autoDelegate.getUnchecked(key);
/*      */     }
/*      */ 
/*      */     
/*      */     public ImmutableMap<K, V> getAll(Iterable<? extends K> keys) throws ExecutionException {
/* 4834 */       return this.autoDelegate.getAll(keys);
/*      */     }
/*      */ 
/*      */     
/*      */     public final V apply(K key) {
/* 4839 */       return this.autoDelegate.apply(key);
/*      */     }
/*      */ 
/*      */     
/*      */     public void refresh(K key) {
/* 4844 */       this.autoDelegate.refresh(key);
/*      */     }
/*      */     
/*      */     private Object readResolve() {
/* 4848 */       return this.autoDelegate;
/*      */     } }
/*      */   
/*      */   static class LocalManualCache<K, V> implements Cache<K, V>, Serializable {
/*      */     final LocalCache<K, V> localCache;
/*      */     private static final long serialVersionUID = 1L;
/*      */     
/*      */     LocalManualCache(CacheBuilder<? super K, ? super V> builder) {
/* 4856 */       this(new LocalCache<>(builder, null));
/*      */     }
/*      */     
/*      */     private LocalManualCache(LocalCache<K, V> localCache) {
/* 4860 */       this.localCache = localCache;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public V getIfPresent(Object key) {
/* 4867 */       return this.localCache.getIfPresent(key);
/*      */     }
/*      */ 
/*      */     
/*      */     public V get(K key, final Callable<? extends V> valueLoader) throws ExecutionException {
/* 4872 */       Preconditions.checkNotNull(valueLoader);
/* 4873 */       return this.localCache.get(key, (CacheLoader)new CacheLoader<Object, V>()
/*      */           {
/*      */             
/*      */             public V load(Object key) throws Exception
/*      */             {
/* 4878 */               return valueLoader.call();
/*      */             }
/*      */           });
/*      */     }
/*      */ 
/*      */     
/*      */     public ImmutableMap<K, V> getAllPresent(Iterable<?> keys) {
/* 4885 */       return this.localCache.getAllPresent(keys);
/*      */     }
/*      */ 
/*      */     
/*      */     public void put(K key, V value) {
/* 4890 */       this.localCache.put(key, value);
/*      */     }
/*      */ 
/*      */     
/*      */     public void putAll(Map<? extends K, ? extends V> m) {
/* 4895 */       this.localCache.putAll(m);
/*      */     }
/*      */ 
/*      */     
/*      */     public void invalidate(Object key) {
/* 4900 */       Preconditions.checkNotNull(key);
/* 4901 */       this.localCache.remove(key);
/*      */     }
/*      */ 
/*      */     
/*      */     public void invalidateAll(Iterable<?> keys) {
/* 4906 */       this.localCache.invalidateAll(keys);
/*      */     }
/*      */ 
/*      */     
/*      */     public void invalidateAll() {
/* 4911 */       this.localCache.clear();
/*      */     }
/*      */ 
/*      */     
/*      */     public long size() {
/* 4916 */       return this.localCache.longSize();
/*      */     }
/*      */ 
/*      */     
/*      */     public ConcurrentMap<K, V> asMap() {
/* 4921 */       return this.localCache;
/*      */     }
/*      */ 
/*      */     
/*      */     public CacheStats stats() {
/* 4926 */       AbstractCache.SimpleStatsCounter aggregator = new AbstractCache.SimpleStatsCounter();
/* 4927 */       aggregator.incrementBy(this.localCache.globalStatsCounter);
/* 4928 */       for (LocalCache.Segment<K, V> segment : this.localCache.segments) {
/* 4929 */         aggregator.incrementBy(segment.statsCounter);
/*      */       }
/* 4931 */       return aggregator.snapshot();
/*      */     }
/*      */ 
/*      */     
/*      */     public void cleanUp() {
/* 4936 */       this.localCache.cleanUp();
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     Object writeReplace() {
/* 4944 */       return new LocalCache.ManualSerializationProxy<>(this.localCache);
/*      */     }
/*      */   }
/*      */   
/*      */   static class LocalLoadingCache<K, V>
/*      */     extends LocalManualCache<K, V> implements LoadingCache<K, V> {
/*      */     private static final long serialVersionUID = 1L;
/*      */     
/*      */     LocalLoadingCache(CacheBuilder<? super K, ? super V> builder, CacheLoader<? super K, V> loader) {
/* 4953 */       super(new LocalCache<>(builder, (CacheLoader<? super K, V>)Preconditions.checkNotNull(loader)));
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public V get(K key) throws ExecutionException {
/* 4960 */       return this.localCache.getOrLoad(key);
/*      */     }
/*      */ 
/*      */     
/*      */     public V getUnchecked(K key) {
/*      */       try {
/* 4966 */         return get(key);
/* 4967 */       } catch (ExecutionException e) {
/* 4968 */         throw new UncheckedExecutionException(e.getCause());
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     public ImmutableMap<K, V> getAll(Iterable<? extends K> keys) throws ExecutionException {
/* 4974 */       return this.localCache.getAll(keys);
/*      */     }
/*      */ 
/*      */     
/*      */     public void refresh(K key) {
/* 4979 */       this.localCache.refresh(key);
/*      */     }
/*      */ 
/*      */     
/*      */     public final V apply(K key) {
/* 4984 */       return getUnchecked(key);
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     Object writeReplace() {
/* 4993 */       return new LocalCache.LoadingSerializationProxy<>(this.localCache);
/*      */     }
/*      */   }
/*      */   
/*      */   static interface ValueReference<K, V> {
/*      */     V get();
/*      */     
/*      */     V waitForValue() throws ExecutionException;
/*      */     
/*      */     int getWeight();
/*      */     
/*      */     ReferenceEntry<K, V> getEntry();
/*      */     
/*      */     ValueReference<K, V> copyFor(ReferenceQueue<V> param1ReferenceQueue, V param1V, ReferenceEntry<K, V> param1ReferenceEntry);
/*      */     
/*      */     void notifyNewValue(V param1V);
/*      */     
/*      */     boolean isLoading();
/*      */     
/*      */     boolean isActive();
/*      */   }
/*      */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\google\common\cache\LocalCache.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */