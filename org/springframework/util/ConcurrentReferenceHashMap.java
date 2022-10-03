/*      */ package org.springframework.util;
/*      */ 
/*      */ import java.lang.ref.ReferenceQueue;
/*      */ import java.lang.ref.SoftReference;
/*      */ import java.lang.ref.WeakReference;
/*      */ import java.lang.reflect.Array;
/*      */ import java.util.AbstractMap;
/*      */ import java.util.AbstractSet;
/*      */ import java.util.Collections;
/*      */ import java.util.EnumSet;
/*      */ import java.util.HashSet;
/*      */ import java.util.Iterator;
/*      */ import java.util.Map;
/*      */ import java.util.NoSuchElementException;
/*      */ import java.util.Set;
/*      */ import java.util.concurrent.ConcurrentMap;
/*      */ import java.util.concurrent.atomic.AtomicInteger;
/*      */ import java.util.concurrent.locks.ReentrantLock;
/*      */ import org.springframework.lang.Nullable;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ public class ConcurrentReferenceHashMap<K, V>
/*      */   extends AbstractMap<K, V>
/*      */   implements ConcurrentMap<K, V>
/*      */ {
/*      */   private static final int DEFAULT_INITIAL_CAPACITY = 16;
/*      */   private static final float DEFAULT_LOAD_FACTOR = 0.75F;
/*      */   private static final int DEFAULT_CONCURRENCY_LEVEL = 16;
/*   71 */   private static final ReferenceType DEFAULT_REFERENCE_TYPE = ReferenceType.SOFT;
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static final int MAXIMUM_CONCURRENCY_LEVEL = 65536;
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static final int MAXIMUM_SEGMENT_SIZE = 1073741824;
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private final Segment[] segments;
/*      */ 
/*      */ 
/*      */   
/*      */   private final float loadFactor;
/*      */ 
/*      */ 
/*      */   
/*      */   private final ReferenceType referenceType;
/*      */ 
/*      */ 
/*      */   
/*      */   private final int shift;
/*      */ 
/*      */ 
/*      */   
/*      */   @Nullable
/*      */   private volatile Set<Map.Entry<K, V>> entrySet;
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public ConcurrentReferenceHashMap() {
/*  109 */     this(16, 0.75F, 16, DEFAULT_REFERENCE_TYPE);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public ConcurrentReferenceHashMap(int initialCapacity) {
/*  117 */     this(initialCapacity, 0.75F, 16, DEFAULT_REFERENCE_TYPE);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public ConcurrentReferenceHashMap(int initialCapacity, float loadFactor) {
/*  127 */     this(initialCapacity, loadFactor, 16, DEFAULT_REFERENCE_TYPE);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public ConcurrentReferenceHashMap(int initialCapacity, int concurrencyLevel) {
/*  137 */     this(initialCapacity, 0.75F, concurrencyLevel, DEFAULT_REFERENCE_TYPE);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public ConcurrentReferenceHashMap(int initialCapacity, ReferenceType referenceType) {
/*  146 */     this(initialCapacity, 0.75F, 16, referenceType);
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
/*      */   public ConcurrentReferenceHashMap(int initialCapacity, float loadFactor, int concurrencyLevel) {
/*  158 */     this(initialCapacity, loadFactor, concurrencyLevel, DEFAULT_REFERENCE_TYPE);
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
/*      */   public ConcurrentReferenceHashMap(int initialCapacity, float loadFactor, int concurrencyLevel, ReferenceType referenceType) {
/*  174 */     Assert.isTrue((initialCapacity >= 0), "Initial capacity must not be negative");
/*  175 */     Assert.isTrue((loadFactor > 0.0F), "Load factor must be positive");
/*  176 */     Assert.isTrue((concurrencyLevel > 0), "Concurrency level must be positive");
/*  177 */     Assert.notNull(referenceType, "Reference type must not be null");
/*  178 */     this.loadFactor = loadFactor;
/*  179 */     this.shift = calculateShift(concurrencyLevel, 65536);
/*  180 */     int size = 1 << this.shift;
/*  181 */     this.referenceType = referenceType;
/*  182 */     int roundedUpSegmentCapacity = (int)(((initialCapacity + size) - 1L) / size);
/*  183 */     int initialSize = 1 << calculateShift(roundedUpSegmentCapacity, 1073741824);
/*  184 */     Segment[] arrayOfSegment = (Segment[])Array.newInstance(Segment.class, size);
/*  185 */     int resizeThreshold = (int)(initialSize * getLoadFactor());
/*  186 */     for (int i = 0; i < arrayOfSegment.length; i++) {
/*  187 */       arrayOfSegment[i] = new Segment(initialSize, resizeThreshold);
/*      */     }
/*  189 */     this.segments = (Segment[])arrayOfSegment;
/*      */   }
/*      */ 
/*      */   
/*      */   protected final float getLoadFactor() {
/*  194 */     return this.loadFactor;
/*      */   }
/*      */   
/*      */   protected final int getSegmentsSize() {
/*  198 */     return this.segments.length;
/*      */   }
/*      */   
/*      */   protected final Segment getSegment(int index) {
/*  202 */     return this.segments[index];
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected ReferenceManager createReferenceManager() {
/*  211 */     return new ReferenceManager();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected int getHash(@Nullable Object o) {
/*  222 */     int hash = (o != null) ? o.hashCode() : 0;
/*  223 */     hash += hash << 15 ^ 0xFFFFCD7D;
/*  224 */     hash ^= hash >>> 10;
/*  225 */     hash += hash << 3;
/*  226 */     hash ^= hash >>> 6;
/*  227 */     hash += (hash << 2) + (hash << 14);
/*  228 */     hash ^= hash >>> 16;
/*  229 */     return hash;
/*      */   }
/*      */ 
/*      */   
/*      */   @Nullable
/*      */   public V get(@Nullable Object key) {
/*  235 */     Reference<K, V> ref = getReference(key, Restructure.WHEN_NECESSARY);
/*  236 */     Entry<K, V> entry = (ref != null) ? ref.get() : null;
/*  237 */     return (entry != null) ? entry.getValue() : null;
/*      */   }
/*      */ 
/*      */   
/*      */   @Nullable
/*      */   public V getOrDefault(@Nullable Object key, @Nullable V defaultValue) {
/*  243 */     Reference<K, V> ref = getReference(key, Restructure.WHEN_NECESSARY);
/*  244 */     Entry<K, V> entry = (ref != null) ? ref.get() : null;
/*  245 */     return (entry != null) ? entry.getValue() : defaultValue;
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean containsKey(@Nullable Object key) {
/*  250 */     Reference<K, V> ref = getReference(key, Restructure.WHEN_NECESSARY);
/*  251 */     Entry<K, V> entry = (ref != null) ? ref.get() : null;
/*  252 */     return (entry != null && ObjectUtils.nullSafeEquals(entry.getKey(), key));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Nullable
/*      */   protected final Reference<K, V> getReference(@Nullable Object key, Restructure restructure) {
/*  264 */     int hash = getHash(key);
/*  265 */     return getSegmentForHash(hash).getReference(key, hash, restructure);
/*      */   }
/*      */ 
/*      */   
/*      */   @Nullable
/*      */   public V put(@Nullable K key, @Nullable V value) {
/*  271 */     return put(key, value, true);
/*      */   }
/*      */ 
/*      */   
/*      */   @Nullable
/*      */   public V putIfAbsent(@Nullable K key, @Nullable V value) {
/*  277 */     return put(key, value, false);
/*      */   }
/*      */   
/*      */   @Nullable
/*      */   private V put(@Nullable K key, @Nullable final V value, final boolean overwriteExisting) {
/*  282 */     return doTask(key, new Task<V>(new TaskOption[] { TaskOption.RESTRUCTURE_BEFORE, TaskOption.RESIZE })
/*      */         {
/*      */           @Nullable
/*      */           protected V execute(@Nullable ConcurrentReferenceHashMap.Reference<K, V> ref, @Nullable ConcurrentReferenceHashMap.Entry<K, V> entry, @Nullable ConcurrentReferenceHashMap.Entries<V> entries) {
/*  286 */             if (entry != null) {
/*  287 */               V oldValue = entry.getValue();
/*  288 */               if (overwriteExisting) {
/*  289 */                 entry.setValue((V)value);
/*      */               }
/*  291 */               return oldValue;
/*      */             } 
/*  293 */             Assert.state((entries != null), "No entries segment");
/*  294 */             entries.add((V)value);
/*  295 */             return null;
/*      */           }
/*      */         });
/*      */   }
/*      */ 
/*      */   
/*      */   @Nullable
/*      */   public V remove(@Nullable Object key) {
/*  303 */     return doTask(key, new Task<V>(new TaskOption[] { TaskOption.RESTRUCTURE_AFTER, TaskOption.SKIP_IF_EMPTY })
/*      */         {
/*      */           @Nullable
/*      */           protected V execute(@Nullable ConcurrentReferenceHashMap.Reference<K, V> ref, @Nullable ConcurrentReferenceHashMap.Entry<K, V> entry) {
/*  307 */             if (entry != null) {
/*  308 */               if (ref != null) {
/*  309 */                 ref.release();
/*      */               }
/*  311 */               return entry.value;
/*      */             } 
/*  313 */             return null;
/*      */           }
/*      */         });
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean remove(@Nullable Object key, @Nullable final Object value) {
/*  320 */     Boolean result = doTask(key, new Task<Boolean>(new TaskOption[] { TaskOption.RESTRUCTURE_AFTER, TaskOption.SKIP_IF_EMPTY })
/*      */         {
/*      */           protected Boolean execute(@Nullable ConcurrentReferenceHashMap.Reference<K, V> ref, @Nullable ConcurrentReferenceHashMap.Entry<K, V> entry) {
/*  323 */             if (entry != null && ObjectUtils.nullSafeEquals(entry.getValue(), value)) {
/*  324 */               if (ref != null) {
/*  325 */                 ref.release();
/*      */               }
/*  327 */               return Boolean.valueOf(true);
/*      */             } 
/*  329 */             return Boolean.valueOf(false);
/*      */           }
/*      */         });
/*  332 */     return Boolean.TRUE.equals(result);
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean replace(@Nullable K key, @Nullable final V oldValue, @Nullable final V newValue) {
/*  337 */     Boolean result = doTask(key, new Task<Boolean>(new TaskOption[] { TaskOption.RESTRUCTURE_BEFORE, TaskOption.SKIP_IF_EMPTY })
/*      */         {
/*      */           protected Boolean execute(@Nullable ConcurrentReferenceHashMap.Reference<K, V> ref, @Nullable ConcurrentReferenceHashMap.Entry<K, V> entry) {
/*  340 */             if (entry != null && ObjectUtils.nullSafeEquals(entry.getValue(), oldValue)) {
/*  341 */               entry.setValue((V)newValue);
/*  342 */               return Boolean.valueOf(true);
/*      */             } 
/*  344 */             return Boolean.valueOf(false);
/*      */           }
/*      */         });
/*  347 */     return Boolean.TRUE.equals(result);
/*      */   }
/*      */ 
/*      */   
/*      */   @Nullable
/*      */   public V replace(@Nullable K key, @Nullable final V value) {
/*  353 */     return doTask(key, new Task<V>(new TaskOption[] { TaskOption.RESTRUCTURE_BEFORE, TaskOption.SKIP_IF_EMPTY })
/*      */         {
/*      */           @Nullable
/*      */           protected V execute(@Nullable ConcurrentReferenceHashMap.Reference<K, V> ref, @Nullable ConcurrentReferenceHashMap.Entry<K, V> entry) {
/*  357 */             if (entry != null) {
/*  358 */               V oldValue = entry.getValue();
/*  359 */               entry.setValue((V)value);
/*  360 */               return oldValue;
/*      */             } 
/*  362 */             return null;
/*      */           }
/*      */         });
/*      */   }
/*      */ 
/*      */   
/*      */   public void clear() {
/*  369 */     for (Segment segment : this.segments) {
/*  370 */       segment.clear();
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void purgeUnreferencedEntries() {
/*  381 */     for (Segment segment : this.segments) {
/*  382 */       segment.restructureIfNecessary(false);
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public int size() {
/*  389 */     int size = 0;
/*  390 */     for (Segment segment : this.segments) {
/*  391 */       size += segment.getCount();
/*      */     }
/*  393 */     return size;
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean isEmpty() {
/*  398 */     for (Segment segment : this.segments) {
/*  399 */       if (segment.getCount() > 0) {
/*  400 */         return false;
/*      */       }
/*      */     } 
/*  403 */     return true;
/*      */   }
/*      */ 
/*      */   
/*      */   public Set<Map.Entry<K, V>> entrySet() {
/*  408 */     Set<Map.Entry<K, V>> entrySet = this.entrySet;
/*  409 */     if (entrySet == null) {
/*  410 */       entrySet = new EntrySet();
/*  411 */       this.entrySet = entrySet;
/*      */     } 
/*  413 */     return entrySet;
/*      */   }
/*      */   
/*      */   @Nullable
/*      */   private <T> T doTask(@Nullable Object key, Task<T> task) {
/*  418 */     int hash = getHash(key);
/*  419 */     return getSegmentForHash(hash).doTask(hash, key, task);
/*      */   }
/*      */   
/*      */   private Segment getSegmentForHash(int hash) {
/*  423 */     return this.segments[hash >>> 32 - this.shift & this.segments.length - 1];
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected static int calculateShift(int minimumValue, int maximumValue) {
/*  434 */     int shift = 0;
/*  435 */     int value = 1;
/*  436 */     while (value < minimumValue && value < maximumValue) {
/*  437 */       value <<= 1;
/*  438 */       shift++;
/*      */     } 
/*  440 */     return shift;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public enum ReferenceType
/*      */   {
/*  450 */     SOFT,
/*      */ 
/*      */     
/*  453 */     WEAK;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected final class Segment
/*      */     extends ReentrantLock
/*      */   {
/*      */     private final ConcurrentReferenceHashMap<K, V>.ReferenceManager referenceManager;
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     private final int initialSize;
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     private volatile ConcurrentReferenceHashMap.Reference<K, V>[] references;
/*      */ 
/*      */ 
/*      */     
/*  477 */     private final AtomicInteger count = new AtomicInteger();
/*      */ 
/*      */ 
/*      */     
/*      */     private int resizeThreshold;
/*      */ 
/*      */ 
/*      */     
/*      */     public Segment(int initialSize, int resizeThreshold) {
/*  486 */       this.referenceManager = ConcurrentReferenceHashMap.this.createReferenceManager();
/*  487 */       this.initialSize = initialSize;
/*  488 */       this.references = createReferenceArray(initialSize);
/*  489 */       this.resizeThreshold = resizeThreshold;
/*      */     }
/*      */     
/*      */     @Nullable
/*      */     public ConcurrentReferenceHashMap.Reference<K, V> getReference(@Nullable Object key, int hash, ConcurrentReferenceHashMap.Restructure restructure) {
/*  494 */       if (restructure == ConcurrentReferenceHashMap.Restructure.WHEN_NECESSARY) {
/*  495 */         restructureIfNecessary(false);
/*      */       }
/*  497 */       if (this.count.get() == 0) {
/*  498 */         return null;
/*      */       }
/*      */       
/*  501 */       ConcurrentReferenceHashMap.Reference<K, V>[] references = this.references;
/*  502 */       int index = getIndex(hash, references);
/*  503 */       ConcurrentReferenceHashMap.Reference<K, V> head = references[index];
/*  504 */       return findInChain(head, key, hash);
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
/*      */     @Nullable
/*      */     public <T> T doTask(int hash, @Nullable Object key, ConcurrentReferenceHashMap<K, V>.Task<T> task) {
/*  517 */       boolean resize = task.hasOption(ConcurrentReferenceHashMap.TaskOption.RESIZE);
/*  518 */       if (task.hasOption(ConcurrentReferenceHashMap.TaskOption.RESTRUCTURE_BEFORE)) {
/*  519 */         restructureIfNecessary(resize);
/*      */       }
/*  521 */       if (task.hasOption(ConcurrentReferenceHashMap.TaskOption.SKIP_IF_EMPTY) && this.count.get() == 0) {
/*  522 */         return task.execute((ConcurrentReferenceHashMap.Reference<K, V>)null, (ConcurrentReferenceHashMap.Entry<K, V>)null, (ConcurrentReferenceHashMap.Entries<V>)null);
/*      */       }
/*  524 */       lock();
/*      */       try {
/*  526 */         int index = getIndex(hash, this.references);
/*  527 */         ConcurrentReferenceHashMap.Reference<K, V> head = this.references[index];
/*  528 */         ConcurrentReferenceHashMap.Reference<K, V> ref = findInChain(head, key, hash);
/*  529 */         ConcurrentReferenceHashMap.Entry<K, V> entry = (ref != null) ? ref.get() : null;
/*  530 */         ConcurrentReferenceHashMap.Entries<V> entries = value -> {
/*      */             ConcurrentReferenceHashMap.Entry<K, V> newEntry = new ConcurrentReferenceHashMap.Entry<>((K)key, (V)value);
/*      */             
/*      */             ConcurrentReferenceHashMap.Reference<K, V> newReference = this.referenceManager.createReference(newEntry, hash, head);
/*      */             this.references[index] = newReference;
/*      */             this.count.incrementAndGet();
/*      */           };
/*  537 */         return task.execute(ref, entry, entries);
/*      */       } finally {
/*      */         
/*  540 */         unlock();
/*  541 */         if (task.hasOption(ConcurrentReferenceHashMap.TaskOption.RESTRUCTURE_AFTER)) {
/*  542 */           restructureIfNecessary(resize);
/*      */         }
/*      */       } 
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public void clear() {
/*  551 */       if (this.count.get() == 0) {
/*      */         return;
/*      */       }
/*  554 */       lock();
/*      */       try {
/*  556 */         this.references = createReferenceArray(this.initialSize);
/*  557 */         this.resizeThreshold = (int)(this.references.length * ConcurrentReferenceHashMap.this.getLoadFactor());
/*  558 */         this.count.set(0);
/*      */       } finally {
/*      */         
/*  561 */         unlock();
/*      */       } 
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     protected final void restructureIfNecessary(boolean allowResize) {
/*  572 */       int currCount = this.count.get();
/*  573 */       boolean needsResize = (allowResize && currCount > 0 && currCount >= this.resizeThreshold);
/*  574 */       ConcurrentReferenceHashMap.Reference<K, V> ref = this.referenceManager.pollForPurge();
/*  575 */       if (ref != null || needsResize) {
/*  576 */         restructure(allowResize, ref);
/*      */       }
/*      */     }
/*      */ 
/*      */     
/*      */     private void restructure(boolean allowResize, @Nullable ConcurrentReferenceHashMap.Reference<K, V> ref) {
/*  582 */       lock();
/*      */       try {
/*  584 */         int countAfterRestructure = this.count.get();
/*  585 */         Set<ConcurrentReferenceHashMap.Reference<K, V>> toPurge = Collections.emptySet();
/*  586 */         if (ref != null) {
/*  587 */           toPurge = new HashSet<>();
/*  588 */           while (ref != null) {
/*  589 */             toPurge.add(ref);
/*  590 */             ref = this.referenceManager.pollForPurge();
/*      */           } 
/*      */         } 
/*  593 */         countAfterRestructure -= toPurge.size();
/*      */ 
/*      */ 
/*      */         
/*  597 */         boolean needsResize = (countAfterRestructure > 0 && countAfterRestructure >= this.resizeThreshold);
/*  598 */         boolean resizing = false;
/*  599 */         int restructureSize = this.references.length;
/*  600 */         if (allowResize && needsResize && restructureSize < 1073741824) {
/*  601 */           restructureSize <<= 1;
/*  602 */           resizing = true;
/*      */         } 
/*      */ 
/*      */ 
/*      */         
/*  607 */         ConcurrentReferenceHashMap.Reference<K, V>[] restructured = resizing ? createReferenceArray(restructureSize) : this.references;
/*      */ 
/*      */         
/*  610 */         for (int i = 0; i < this.references.length; i++) {
/*  611 */           ref = this.references[i];
/*  612 */           if (!resizing) {
/*  613 */             restructured[i] = null;
/*      */           }
/*  615 */           while (ref != null) {
/*  616 */             if (!toPurge.contains(ref)) {
/*  617 */               ConcurrentReferenceHashMap.Entry<K, V> entry = ref.get();
/*  618 */               if (entry != null) {
/*  619 */                 int index = getIndex(ref.getHash(), restructured);
/*  620 */                 restructured[index] = this.referenceManager.createReference(entry, ref
/*  621 */                     .getHash(), restructured[index]);
/*      */               } 
/*      */             } 
/*  624 */             ref = ref.getNext();
/*      */           } 
/*      */         } 
/*      */ 
/*      */         
/*  629 */         if (resizing) {
/*  630 */           this.references = restructured;
/*  631 */           this.resizeThreshold = (int)(this.references.length * ConcurrentReferenceHashMap.this.getLoadFactor());
/*      */         } 
/*  633 */         this.count.set(Math.max(countAfterRestructure, 0));
/*      */       } finally {
/*      */         
/*  636 */         unlock();
/*      */       } 
/*      */     }
/*      */     
/*      */     @Nullable
/*      */     private ConcurrentReferenceHashMap.Reference<K, V> findInChain(ConcurrentReferenceHashMap.Reference<K, V> ref, @Nullable Object key, int hash) {
/*  642 */       ConcurrentReferenceHashMap.Reference<K, V> currRef = ref;
/*  643 */       while (currRef != null) {
/*  644 */         if (currRef.getHash() == hash) {
/*  645 */           ConcurrentReferenceHashMap.Entry<K, V> entry = currRef.get();
/*  646 */           if (entry != null) {
/*  647 */             K entryKey = entry.getKey();
/*  648 */             if (ObjectUtils.nullSafeEquals(entryKey, key)) {
/*  649 */               return currRef;
/*      */             }
/*      */           } 
/*      */         } 
/*  653 */         currRef = currRef.getNext();
/*      */       } 
/*  655 */       return null;
/*      */     }
/*      */ 
/*      */     
/*      */     private ConcurrentReferenceHashMap.Reference<K, V>[] createReferenceArray(int size) {
/*  660 */       return (ConcurrentReferenceHashMap.Reference<K, V>[])new ConcurrentReferenceHashMap.Reference[size];
/*      */     }
/*      */     
/*      */     private int getIndex(int hash, ConcurrentReferenceHashMap.Reference<K, V>[] references) {
/*  664 */       return hash & references.length - 1;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public final int getSize() {
/*  671 */       return this.references.length;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public final int getCount() {
/*  678 */       return this.count.get();
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected static interface Reference<K, V>
/*      */   {
/*      */     @Nullable
/*      */     ConcurrentReferenceHashMap.Entry<K, V> get();
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     int getHash();
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     @Nullable
/*      */     Reference<K, V> getNext();
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     void release();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected static final class Entry<K, V>
/*      */     implements Map.Entry<K, V>
/*      */   {
/*      */     @Nullable
/*      */     private final K key;
/*      */ 
/*      */ 
/*      */     
/*      */     @Nullable
/*      */     private volatile V value;
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public Entry(@Nullable K key, @Nullable V value) {
/*  730 */       this.key = key;
/*  731 */       this.value = value;
/*      */     }
/*      */ 
/*      */     
/*      */     @Nullable
/*      */     public K getKey() {
/*  737 */       return this.key;
/*      */     }
/*      */ 
/*      */     
/*      */     @Nullable
/*      */     public V getValue() {
/*  743 */       return this.value;
/*      */     }
/*      */ 
/*      */     
/*      */     @Nullable
/*      */     public V setValue(@Nullable V value) {
/*  749 */       V previous = this.value;
/*  750 */       this.value = value;
/*  751 */       return previous;
/*      */     }
/*      */ 
/*      */     
/*      */     public String toString() {
/*  756 */       return (new StringBuilder()).append(this.key).append("=").append(this.value).toString();
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     public final boolean equals(@Nullable Object other) {
/*  762 */       if (this == other) {
/*  763 */         return true;
/*      */       }
/*  765 */       if (!(other instanceof Map.Entry)) {
/*  766 */         return false;
/*      */       }
/*  768 */       Map.Entry otherEntry = (Map.Entry)other;
/*  769 */       return (ObjectUtils.nullSafeEquals(getKey(), otherEntry.getKey()) && 
/*  770 */         ObjectUtils.nullSafeEquals(getValue(), otherEntry.getValue()));
/*      */     }
/*      */ 
/*      */     
/*      */     public final int hashCode() {
/*  775 */       return ObjectUtils.nullSafeHashCode(this.key) ^ ObjectUtils.nullSafeHashCode(this.value);
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private abstract class Task<T>
/*      */   {
/*      */     private final EnumSet<ConcurrentReferenceHashMap.TaskOption> options;
/*      */ 
/*      */ 
/*      */     
/*      */     public Task(ConcurrentReferenceHashMap.TaskOption... options) {
/*  788 */       this.options = (options.length == 0) ? EnumSet.<ConcurrentReferenceHashMap.TaskOption>noneOf(ConcurrentReferenceHashMap.TaskOption.class) : EnumSet.<ConcurrentReferenceHashMap.TaskOption>of(options[0], options);
/*      */     }
/*      */     
/*      */     public boolean hasOption(ConcurrentReferenceHashMap.TaskOption option) {
/*  792 */       return this.options.contains(option);
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
/*      */     @Nullable
/*      */     protected T execute(@Nullable ConcurrentReferenceHashMap.Reference<K, V> ref, @Nullable ConcurrentReferenceHashMap.Entry<K, V> entry, @Nullable ConcurrentReferenceHashMap.Entries<V> entries) {
/*  805 */       return execute(ref, entry);
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     @Nullable
/*      */     protected T execute(@Nullable ConcurrentReferenceHashMap.Reference<K, V> ref, @Nullable ConcurrentReferenceHashMap.Entry<K, V> entry) {
/*  817 */       return null;
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private enum TaskOption
/*      */   {
/*  827 */     RESTRUCTURE_BEFORE, RESTRUCTURE_AFTER, SKIP_IF_EMPTY, RESIZE;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static interface Entries<V>
/*      */   {
/*      */     void add(@Nullable V param1V);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private class EntrySet
/*      */     extends AbstractSet<Map.Entry<K, V>>
/*      */   {
/*      */     private EntrySet() {}
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public Iterator<Map.Entry<K, V>> iterator() {
/*  851 */       return new ConcurrentReferenceHashMap.EntryIterator();
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean contains(@Nullable Object o) {
/*  856 */       if (o instanceof Map.Entry) {
/*  857 */         Map.Entry<?, ?> entry = (Map.Entry<?, ?>)o;
/*  858 */         ConcurrentReferenceHashMap.Reference<K, V> ref = ConcurrentReferenceHashMap.this.getReference(entry.getKey(), ConcurrentReferenceHashMap.Restructure.NEVER);
/*  859 */         ConcurrentReferenceHashMap.Entry<K, V> otherEntry = (ref != null) ? ref.get() : null;
/*  860 */         if (otherEntry != null) {
/*  861 */           return ObjectUtils.nullSafeEquals(entry.getValue(), otherEntry.getValue());
/*      */         }
/*      */       } 
/*  864 */       return false;
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean remove(Object o) {
/*  869 */       if (o instanceof Map.Entry) {
/*  870 */         Map.Entry<?, ?> entry = (Map.Entry<?, ?>)o;
/*  871 */         return ConcurrentReferenceHashMap.this.remove(entry.getKey(), entry.getValue());
/*      */       } 
/*  873 */       return false;
/*      */     }
/*      */ 
/*      */     
/*      */     public int size() {
/*  878 */       return ConcurrentReferenceHashMap.this.size();
/*      */     }
/*      */ 
/*      */     
/*      */     public void clear() {
/*  883 */       ConcurrentReferenceHashMap.this.clear();
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private class EntryIterator
/*      */     implements Iterator<Map.Entry<K, V>>
/*      */   {
/*      */     private int segmentIndex;
/*      */     
/*      */     private int referenceIndex;
/*      */     
/*      */     @Nullable
/*      */     private ConcurrentReferenceHashMap.Reference<K, V>[] references;
/*      */     
/*      */     @Nullable
/*      */     private ConcurrentReferenceHashMap.Reference<K, V> reference;
/*      */     
/*      */     @Nullable
/*      */     private ConcurrentReferenceHashMap.Entry<K, V> next;
/*      */     
/*      */     @Nullable
/*      */     private ConcurrentReferenceHashMap.Entry<K, V> last;
/*      */ 
/*      */     
/*      */     public EntryIterator() {
/*  910 */       moveToNextSegment();
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean hasNext() {
/*  915 */       getNextIfNecessary();
/*  916 */       return (this.next != null);
/*      */     }
/*      */ 
/*      */     
/*      */     public ConcurrentReferenceHashMap.Entry<K, V> next() {
/*  921 */       getNextIfNecessary();
/*  922 */       if (this.next == null) {
/*  923 */         throw new NoSuchElementException();
/*      */       }
/*  925 */       this.last = this.next;
/*  926 */       this.next = null;
/*  927 */       return this.last;
/*      */     }
/*      */     
/*      */     private void getNextIfNecessary() {
/*  931 */       while (this.next == null) {
/*  932 */         moveToNextReference();
/*  933 */         if (this.reference == null) {
/*      */           return;
/*      */         }
/*  936 */         this.next = this.reference.get();
/*      */       } 
/*      */     }
/*      */     
/*      */     private void moveToNextReference() {
/*  941 */       if (this.reference != null) {
/*  942 */         this.reference = this.reference.getNext();
/*      */       }
/*  944 */       while (this.reference == null && this.references != null) {
/*  945 */         if (this.referenceIndex >= this.references.length) {
/*  946 */           moveToNextSegment();
/*  947 */           this.referenceIndex = 0;
/*      */           continue;
/*      */         } 
/*  950 */         this.reference = this.references[this.referenceIndex];
/*  951 */         this.referenceIndex++;
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     private void moveToNextSegment() {
/*  957 */       this.reference = null;
/*  958 */       this.references = null;
/*  959 */       if (this.segmentIndex < ConcurrentReferenceHashMap.this.segments.length) {
/*  960 */         this.references = (ConcurrentReferenceHashMap.this.segments[this.segmentIndex]).references;
/*  961 */         this.segmentIndex++;
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     public void remove() {
/*  967 */       Assert.state((this.last != null), "No element to remove");
/*  968 */       ConcurrentReferenceHashMap.this.remove(this.last.getKey());
/*  969 */       this.last = null;
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected enum Restructure
/*      */   {
/*  979 */     WHEN_NECESSARY, NEVER;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected class ReferenceManager
/*      */   {
/*  989 */     private final ReferenceQueue<ConcurrentReferenceHashMap.Entry<K, V>> queue = new ReferenceQueue<>();
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public ConcurrentReferenceHashMap.Reference<K, V> createReference(ConcurrentReferenceHashMap.Entry<K, V> entry, int hash, @Nullable ConcurrentReferenceHashMap.Reference<K, V> next) {
/*  999 */       if (ConcurrentReferenceHashMap.this.referenceType == ConcurrentReferenceHashMap.ReferenceType.WEAK) {
/* 1000 */         return new ConcurrentReferenceHashMap.WeakEntryReference<>(entry, hash, next, this.queue);
/*      */       }
/* 1002 */       return new ConcurrentReferenceHashMap.SoftEntryReference<>(entry, hash, next, this.queue);
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
/*      */     @Nullable
/*      */     public ConcurrentReferenceHashMap.Reference<K, V> pollForPurge() {
/* 1015 */       return (ConcurrentReferenceHashMap.Reference)this.queue.poll();
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private static final class SoftEntryReference<K, V>
/*      */     extends SoftReference<Entry<K, V>>
/*      */     implements Reference<K, V>
/*      */   {
/*      */     private final int hash;
/*      */ 
/*      */     
/*      */     @Nullable
/*      */     private final ConcurrentReferenceHashMap.Reference<K, V> nextReference;
/*      */ 
/*      */     
/*      */     public SoftEntryReference(ConcurrentReferenceHashMap.Entry<K, V> entry, int hash, @Nullable ConcurrentReferenceHashMap.Reference<K, V> next, ReferenceQueue<ConcurrentReferenceHashMap.Entry<K, V>> queue) {
/* 1033 */       super(entry, queue);
/* 1034 */       this.hash = hash;
/* 1035 */       this.nextReference = next;
/*      */     }
/*      */ 
/*      */     
/*      */     public int getHash() {
/* 1040 */       return this.hash;
/*      */     }
/*      */ 
/*      */     
/*      */     @Nullable
/*      */     public ConcurrentReferenceHashMap.Reference<K, V> getNext() {
/* 1046 */       return this.nextReference;
/*      */     }
/*      */ 
/*      */     
/*      */     public void release() {
/* 1051 */       enqueue();
/* 1052 */       clear();
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private static final class WeakEntryReference<K, V>
/*      */     extends WeakReference<Entry<K, V>>
/*      */     implements Reference<K, V>
/*      */   {
/*      */     private final int hash;
/*      */ 
/*      */     
/*      */     @Nullable
/*      */     private final ConcurrentReferenceHashMap.Reference<K, V> nextReference;
/*      */ 
/*      */     
/*      */     public WeakEntryReference(ConcurrentReferenceHashMap.Entry<K, V> entry, int hash, @Nullable ConcurrentReferenceHashMap.Reference<K, V> next, ReferenceQueue<ConcurrentReferenceHashMap.Entry<K, V>> queue) {
/* 1070 */       super(entry, queue);
/* 1071 */       this.hash = hash;
/* 1072 */       this.nextReference = next;
/*      */     }
/*      */ 
/*      */     
/*      */     public int getHash() {
/* 1077 */       return this.hash;
/*      */     }
/*      */ 
/*      */     
/*      */     @Nullable
/*      */     public ConcurrentReferenceHashMap.Reference<K, V> getNext() {
/* 1083 */       return this.nextReference;
/*      */     }
/*      */ 
/*      */     
/*      */     public void release() {
/* 1088 */       enqueue();
/* 1089 */       clear();
/*      */     }
/*      */   }
/*      */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\springframewor\\util\ConcurrentReferenceHashMap.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */