/*     */ package com.google.common.collect;
/*     */ 
/*     */ import com.google.common.annotations.GwtCompatible;
/*     */ import com.google.common.annotations.GwtIncompatible;
/*     */ import com.google.common.base.Objects;
/*     */ import com.google.common.base.Preconditions;
/*     */ import com.google.errorprone.annotations.CanIgnoreReturnValue;
/*     */ import com.google.j2objc.annotations.RetainedWith;
/*     */ import java.io.IOException;
/*     */ import java.io.ObjectInputStream;
/*     */ import java.io.ObjectOutputStream;
/*     */ import java.io.Serializable;
/*     */ import java.util.Arrays;
/*     */ import java.util.Collection;
/*     */ import java.util.ConcurrentModificationException;
/*     */ import java.util.Iterator;
/*     */ import java.util.Map;
/*     */ import java.util.NoSuchElementException;
/*     */ import java.util.Set;
/*     */ import java.util.function.BiConsumer;
/*     */ import java.util.function.BiFunction;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
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
/*     */ public final class HashBiMap<K, V>
/*     */   extends Maps.IteratorBasedAbstractMap<K, V>
/*     */   implements BiMap<K, V>, Serializable
/*     */ {
/*     */   private static final double LOAD_FACTOR = 1.0D;
/*     */   private transient BiEntry<K, V>[] hashTableKToV;
/*     */   private transient BiEntry<K, V>[] hashTableVToK;
/*     */   private transient BiEntry<K, V> firstInKeyInsertionOrder;
/*     */   private transient BiEntry<K, V> lastInKeyInsertionOrder;
/*     */   private transient int size;
/*     */   private transient int mask;
/*     */   private transient int modCount;
/*     */   @RetainedWith
/*     */   private transient BiMap<V, K> inverse;
/*     */   @GwtIncompatible
/*     */   private static final long serialVersionUID = 0L;
/*     */   
/*     */   public static <K, V> HashBiMap<K, V> create() {
/*  64 */     return create(16);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <K, V> HashBiMap<K, V> create(int expectedSize) {
/*  74 */     return new HashBiMap<>(expectedSize);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <K, V> HashBiMap<K, V> create(Map<? extends K, ? extends V> map) {
/*  82 */     HashBiMap<K, V> bimap = create(map.size());
/*  83 */     bimap.putAll(map);
/*  84 */     return bimap;
/*     */   }
/*     */   
/*     */   private static final class BiEntry<K, V>
/*     */     extends ImmutableEntry<K, V>
/*     */   {
/*     */     final int keyHash;
/*     */     final int valueHash;
/*     */     BiEntry<K, V> nextInKToVBucket;
/*     */     BiEntry<K, V> nextInVToKBucket;
/*     */     BiEntry<K, V> nextInKeyInsertionOrder;
/*     */     BiEntry<K, V> prevInKeyInsertionOrder;
/*     */     
/*     */     BiEntry(K key, int keyHash, V value, int valueHash) {
/*  98 */       super(key, value);
/*  99 */       this.keyHash = keyHash;
/* 100 */       this.valueHash = valueHash;
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
/*     */   private HashBiMap(int expectedSize) {
/* 115 */     init(expectedSize);
/*     */   }
/*     */   
/*     */   private void init(int expectedSize) {
/* 119 */     CollectPreconditions.checkNonnegative(expectedSize, "expectedSize");
/* 120 */     int tableSize = Hashing.closedTableSize(expectedSize, 1.0D);
/* 121 */     this.hashTableKToV = createTable(tableSize);
/* 122 */     this.hashTableVToK = createTable(tableSize);
/* 123 */     this.firstInKeyInsertionOrder = null;
/* 124 */     this.lastInKeyInsertionOrder = null;
/* 125 */     this.size = 0;
/* 126 */     this.mask = tableSize - 1;
/* 127 */     this.modCount = 0;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void delete(BiEntry<K, V> entry) {
/* 135 */     int keyBucket = entry.keyHash & this.mask;
/* 136 */     BiEntry<K, V> prevBucketEntry = null;
/* 137 */     BiEntry<K, V> bucketEntry = this.hashTableKToV[keyBucket];
/*     */     
/* 139 */     for (;; bucketEntry = bucketEntry.nextInKToVBucket) {
/* 140 */       if (bucketEntry == entry) {
/* 141 */         if (prevBucketEntry == null) {
/* 142 */           this.hashTableKToV[keyBucket] = entry.nextInKToVBucket; break;
/*     */         } 
/* 144 */         prevBucketEntry.nextInKToVBucket = entry.nextInKToVBucket;
/*     */         
/*     */         break;
/*     */       } 
/* 148 */       prevBucketEntry = bucketEntry;
/*     */     } 
/*     */     
/* 151 */     int valueBucket = entry.valueHash & this.mask;
/* 152 */     prevBucketEntry = null;
/* 153 */     BiEntry<K, V> biEntry1 = this.hashTableVToK[valueBucket];
/*     */     
/* 155 */     for (;; biEntry1 = biEntry1.nextInVToKBucket) {
/* 156 */       if (biEntry1 == entry) {
/* 157 */         if (prevBucketEntry == null) {
/* 158 */           this.hashTableVToK[valueBucket] = entry.nextInVToKBucket; break;
/*     */         } 
/* 160 */         prevBucketEntry.nextInVToKBucket = entry.nextInVToKBucket;
/*     */         
/*     */         break;
/*     */       } 
/* 164 */       prevBucketEntry = biEntry1;
/*     */     } 
/*     */     
/* 167 */     if (entry.prevInKeyInsertionOrder == null) {
/* 168 */       this.firstInKeyInsertionOrder = entry.nextInKeyInsertionOrder;
/*     */     } else {
/* 170 */       entry.prevInKeyInsertionOrder.nextInKeyInsertionOrder = entry.nextInKeyInsertionOrder;
/*     */     } 
/*     */     
/* 173 */     if (entry.nextInKeyInsertionOrder == null) {
/* 174 */       this.lastInKeyInsertionOrder = entry.prevInKeyInsertionOrder;
/*     */     } else {
/* 176 */       entry.nextInKeyInsertionOrder.prevInKeyInsertionOrder = entry.prevInKeyInsertionOrder;
/*     */     } 
/*     */     
/* 179 */     this.size--;
/* 180 */     this.modCount++;
/*     */   }
/*     */   
/*     */   private void insert(BiEntry<K, V> entry, BiEntry<K, V> oldEntryForKey) {
/* 184 */     int keyBucket = entry.keyHash & this.mask;
/* 185 */     entry.nextInKToVBucket = this.hashTableKToV[keyBucket];
/* 186 */     this.hashTableKToV[keyBucket] = entry;
/*     */     
/* 188 */     int valueBucket = entry.valueHash & this.mask;
/* 189 */     entry.nextInVToKBucket = this.hashTableVToK[valueBucket];
/* 190 */     this.hashTableVToK[valueBucket] = entry;
/*     */     
/* 192 */     if (oldEntryForKey == null) {
/* 193 */       entry.prevInKeyInsertionOrder = this.lastInKeyInsertionOrder;
/* 194 */       entry.nextInKeyInsertionOrder = null;
/* 195 */       if (this.lastInKeyInsertionOrder == null) {
/* 196 */         this.firstInKeyInsertionOrder = entry;
/*     */       } else {
/* 198 */         this.lastInKeyInsertionOrder.nextInKeyInsertionOrder = entry;
/*     */       } 
/* 200 */       this.lastInKeyInsertionOrder = entry;
/*     */     } else {
/* 202 */       entry.prevInKeyInsertionOrder = oldEntryForKey.prevInKeyInsertionOrder;
/* 203 */       if (entry.prevInKeyInsertionOrder == null) {
/* 204 */         this.firstInKeyInsertionOrder = entry;
/*     */       } else {
/* 206 */         entry.prevInKeyInsertionOrder.nextInKeyInsertionOrder = entry;
/*     */       } 
/* 208 */       entry.nextInKeyInsertionOrder = oldEntryForKey.nextInKeyInsertionOrder;
/* 209 */       if (entry.nextInKeyInsertionOrder == null) {
/* 210 */         this.lastInKeyInsertionOrder = entry;
/*     */       } else {
/* 212 */         entry.nextInKeyInsertionOrder.prevInKeyInsertionOrder = entry;
/*     */       } 
/*     */     } 
/*     */     
/* 216 */     this.size++;
/* 217 */     this.modCount++;
/*     */   }
/*     */   
/*     */   private BiEntry<K, V> seekByKey(Object key, int keyHash) {
/* 221 */     BiEntry<K, V> entry = this.hashTableKToV[keyHash & this.mask];
/* 222 */     for (; entry != null; 
/* 223 */       entry = entry.nextInKToVBucket) {
/* 224 */       if (keyHash == entry.keyHash && Objects.equal(key, entry.key)) {
/* 225 */         return entry;
/*     */       }
/*     */     } 
/* 228 */     return null;
/*     */   }
/*     */   
/*     */   private BiEntry<K, V> seekByValue(Object value, int valueHash) {
/* 232 */     BiEntry<K, V> entry = this.hashTableVToK[valueHash & this.mask];
/* 233 */     for (; entry != null; 
/* 234 */       entry = entry.nextInVToKBucket) {
/* 235 */       if (valueHash == entry.valueHash && Objects.equal(value, entry.value)) {
/* 236 */         return entry;
/*     */       }
/*     */     } 
/* 239 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean containsKey(Object key) {
/* 244 */     return (seekByKey(key, Hashing.smearedHash(key)) != null);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean containsValue(Object value) {
/* 249 */     return (seekByValue(value, Hashing.smearedHash(value)) != null);
/*     */   }
/*     */ 
/*     */   
/*     */   public V get(Object key) {
/* 254 */     return Maps.valueOrNull(seekByKey(key, Hashing.smearedHash(key)));
/*     */   }
/*     */ 
/*     */   
/*     */   @CanIgnoreReturnValue
/*     */   public V put(K key, V value) {
/* 260 */     return put(key, value, false);
/*     */   }
/*     */   
/*     */   private V put(K key, V value, boolean force) {
/* 264 */     int keyHash = Hashing.smearedHash(key);
/* 265 */     int valueHash = Hashing.smearedHash(value);
/*     */     
/* 267 */     BiEntry<K, V> oldEntryForKey = seekByKey(key, keyHash);
/* 268 */     if (oldEntryForKey != null && valueHash == oldEntryForKey.valueHash && 
/*     */       
/* 270 */       Objects.equal(value, oldEntryForKey.value)) {
/* 271 */       return value;
/*     */     }
/*     */     
/* 274 */     BiEntry<K, V> oldEntryForValue = seekByValue(value, valueHash);
/* 275 */     if (oldEntryForValue != null) {
/* 276 */       if (force) {
/* 277 */         delete(oldEntryForValue);
/*     */       } else {
/* 279 */         throw new IllegalArgumentException("value already present: " + value);
/*     */       } 
/*     */     }
/*     */     
/* 283 */     BiEntry<K, V> newEntry = new BiEntry<>(key, keyHash, value, valueHash);
/* 284 */     if (oldEntryForKey != null) {
/* 285 */       delete(oldEntryForKey);
/* 286 */       insert(newEntry, oldEntryForKey);
/* 287 */       oldEntryForKey.prevInKeyInsertionOrder = null;
/* 288 */       oldEntryForKey.nextInKeyInsertionOrder = null;
/* 289 */       return oldEntryForKey.value;
/*     */     } 
/* 291 */     insert(newEntry, (BiEntry<K, V>)null);
/* 292 */     rehashIfNecessary();
/* 293 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @CanIgnoreReturnValue
/*     */   public V forcePut(K key, V value) {
/* 301 */     return put(key, value, true);
/*     */   }
/*     */   
/*     */   private K putInverse(V value, K key, boolean force) {
/* 305 */     int valueHash = Hashing.smearedHash(value);
/* 306 */     int keyHash = Hashing.smearedHash(key);
/*     */     
/* 308 */     BiEntry<K, V> oldEntryForValue = seekByValue(value, valueHash);
/* 309 */     BiEntry<K, V> oldEntryForKey = seekByKey(key, keyHash);
/* 310 */     if (oldEntryForValue != null && keyHash == oldEntryForValue.keyHash && 
/*     */       
/* 312 */       Objects.equal(key, oldEntryForValue.key))
/* 313 */       return key; 
/* 314 */     if (oldEntryForKey != null && !force) {
/* 315 */       throw new IllegalArgumentException("key already present: " + key);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 325 */     if (oldEntryForValue != null) {
/* 326 */       delete(oldEntryForValue);
/*     */     }
/*     */     
/* 329 */     if (oldEntryForKey != null) {
/* 330 */       delete(oldEntryForKey);
/*     */     }
/*     */     
/* 333 */     BiEntry<K, V> newEntry = new BiEntry<>(key, keyHash, value, valueHash);
/* 334 */     insert(newEntry, oldEntryForKey);
/*     */     
/* 336 */     if (oldEntryForKey != null) {
/* 337 */       oldEntryForKey.prevInKeyInsertionOrder = null;
/* 338 */       oldEntryForKey.nextInKeyInsertionOrder = null;
/*     */     } 
/* 340 */     if (oldEntryForValue != null) {
/* 341 */       oldEntryForValue.prevInKeyInsertionOrder = null;
/* 342 */       oldEntryForValue.nextInKeyInsertionOrder = null;
/*     */     } 
/* 344 */     rehashIfNecessary();
/* 345 */     return Maps.keyOrNull(oldEntryForValue);
/*     */   }
/*     */   
/*     */   private void rehashIfNecessary() {
/* 349 */     BiEntry<K, V>[] oldKToV = this.hashTableKToV;
/* 350 */     if (Hashing.needsResizing(this.size, oldKToV.length, 1.0D)) {
/* 351 */       int newTableSize = oldKToV.length * 2;
/*     */       
/* 353 */       this.hashTableKToV = createTable(newTableSize);
/* 354 */       this.hashTableVToK = createTable(newTableSize);
/* 355 */       this.mask = newTableSize - 1;
/* 356 */       this.size = 0;
/*     */       
/* 358 */       BiEntry<K, V> entry = this.firstInKeyInsertionOrder;
/* 359 */       for (; entry != null; 
/* 360 */         entry = entry.nextInKeyInsertionOrder) {
/* 361 */         insert(entry, entry);
/*     */       }
/* 363 */       this.modCount++;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private BiEntry<K, V>[] createTable(int length) {
/* 369 */     return (BiEntry<K, V>[])new BiEntry[length];
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @CanIgnoreReturnValue
/*     */   public V remove(Object key) {
/* 376 */     BiEntry<K, V> entry = seekByKey(key, Hashing.smearedHash(key));
/* 377 */     if (entry == null) {
/* 378 */       return null;
/*     */     }
/* 380 */     delete(entry);
/* 381 */     entry.prevInKeyInsertionOrder = null;
/* 382 */     entry.nextInKeyInsertionOrder = null;
/* 383 */     return entry.value;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void clear() {
/* 389 */     this.size = 0;
/* 390 */     Arrays.fill((Object[])this.hashTableKToV, (Object)null);
/* 391 */     Arrays.fill((Object[])this.hashTableVToK, (Object)null);
/* 392 */     this.firstInKeyInsertionOrder = null;
/* 393 */     this.lastInKeyInsertionOrder = null;
/* 394 */     this.modCount++;
/*     */   }
/*     */ 
/*     */   
/*     */   public int size() {
/* 399 */     return this.size;
/*     */   }
/*     */   
/*     */   abstract class Itr<T> implements Iterator<T> {
/* 403 */     HashBiMap.BiEntry<K, V> next = HashBiMap.this.firstInKeyInsertionOrder;
/* 404 */     HashBiMap.BiEntry<K, V> toRemove = null;
/* 405 */     int expectedModCount = HashBiMap.this.modCount;
/* 406 */     int remaining = HashBiMap.this.size();
/*     */ 
/*     */     
/*     */     public boolean hasNext() {
/* 410 */       if (HashBiMap.this.modCount != this.expectedModCount) {
/* 411 */         throw new ConcurrentModificationException();
/*     */       }
/* 413 */       return (this.next != null && this.remaining > 0);
/*     */     }
/*     */ 
/*     */     
/*     */     public T next() {
/* 418 */       if (!hasNext()) {
/* 419 */         throw new NoSuchElementException();
/*     */       }
/*     */       
/* 422 */       HashBiMap.BiEntry<K, V> entry = this.next;
/* 423 */       this.next = entry.nextInKeyInsertionOrder;
/* 424 */       this.toRemove = entry;
/* 425 */       this.remaining--;
/* 426 */       return output(entry);
/*     */     }
/*     */ 
/*     */     
/*     */     public void remove() {
/* 431 */       if (HashBiMap.this.modCount != this.expectedModCount) {
/* 432 */         throw new ConcurrentModificationException();
/*     */       }
/* 434 */       CollectPreconditions.checkRemove((this.toRemove != null));
/* 435 */       HashBiMap.this.delete(this.toRemove);
/* 436 */       this.expectedModCount = HashBiMap.this.modCount;
/* 437 */       this.toRemove = null;
/*     */     }
/*     */ 
/*     */     
/*     */     abstract T output(HashBiMap.BiEntry<K, V> param1BiEntry);
/*     */   }
/*     */   
/*     */   public Set<K> keySet() {
/* 445 */     return new KeySet();
/*     */   }
/*     */   
/*     */   private final class KeySet
/*     */     extends Maps.KeySet<K, V> {
/*     */     KeySet() {
/* 451 */       super(HashBiMap.this);
/*     */     }
/*     */ 
/*     */     
/*     */     public Iterator<K> iterator() {
/* 456 */       return new HashBiMap<K, V>.Itr<K>()
/*     */         {
/*     */           K output(HashBiMap.BiEntry<K, V> entry) {
/* 459 */             return entry.key;
/*     */           }
/*     */         };
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean remove(Object o) {
/* 466 */       HashBiMap.BiEntry<K, V> entry = HashBiMap.this.seekByKey(o, Hashing.smearedHash(o));
/* 467 */       if (entry == null) {
/* 468 */         return false;
/*     */       }
/* 470 */       HashBiMap.this.delete(entry);
/* 471 */       entry.prevInKeyInsertionOrder = null;
/* 472 */       entry.nextInKeyInsertionOrder = null;
/* 473 */       return true;
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Set<V> values() {
/* 480 */     return inverse().keySet();
/*     */   }
/*     */ 
/*     */   
/*     */   Iterator<Map.Entry<K, V>> entryIterator() {
/* 485 */     return new Itr<Map.Entry<K, V>>()
/*     */       {
/*     */         Map.Entry<K, V> output(HashBiMap.BiEntry<K, V> entry) {
/* 488 */           return new MapEntry(entry);
/*     */         }
/*     */ 
/*     */ 
/*     */         
/*     */         class MapEntry
/*     */           extends AbstractMapEntry<K, V>
/*     */         {
/*     */           HashBiMap.BiEntry<K, V> delegate;
/*     */ 
/*     */           
/*     */           public K getKey() {
/* 500 */             return this.delegate.key;
/*     */           }
/*     */ 
/*     */           
/*     */           public V getValue() {
/* 505 */             return this.delegate.value;
/*     */           }
/*     */ 
/*     */           
/*     */           public V setValue(V value) {
/* 510 */             V oldValue = this.delegate.value;
/* 511 */             int valueHash = Hashing.smearedHash(value);
/* 512 */             if (valueHash == this.delegate.valueHash && Objects.equal(value, oldValue)) {
/* 513 */               return value;
/*     */             }
/* 515 */             Preconditions.checkArgument((HashBiMap.this.seekByValue(value, valueHash) == null), "value already present: %s", value);
/* 516 */             HashBiMap.this.delete(this.delegate);
/* 517 */             HashBiMap.BiEntry<K, V> newEntry = new HashBiMap.BiEntry<>(this.delegate.key, this.delegate.keyHash, value, valueHash);
/* 518 */             HashBiMap.this.insert(newEntry, this.delegate);
/* 519 */             this.delegate.prevInKeyInsertionOrder = null;
/* 520 */             this.delegate.nextInKeyInsertionOrder = null;
/* 521 */             HashBiMap.null.this.expectedModCount = HashBiMap.this.modCount;
/* 522 */             if (HashBiMap.null.this.toRemove == this.delegate) {
/* 523 */               HashBiMap.null.this.toRemove = newEntry;
/*     */             }
/* 525 */             this.delegate = newEntry;
/* 526 */             return oldValue;
/*     */           }
/*     */         }
/*     */       };
/*     */   }
/*     */ 
/*     */   
/*     */   public void forEach(BiConsumer<? super K, ? super V> action) {
/* 534 */     Preconditions.checkNotNull(action);
/* 535 */     BiEntry<K, V> entry = this.firstInKeyInsertionOrder;
/* 536 */     for (; entry != null; 
/* 537 */       entry = entry.nextInKeyInsertionOrder) {
/* 538 */       action.accept(entry.key, entry.value);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public void replaceAll(BiFunction<? super K, ? super V, ? extends V> function) {
/* 544 */     Preconditions.checkNotNull(function);
/* 545 */     BiEntry<K, V> oldFirst = this.firstInKeyInsertionOrder;
/* 546 */     clear();
/* 547 */     for (BiEntry<K, V> entry = oldFirst; entry != null; entry = entry.nextInKeyInsertionOrder) {
/* 548 */       put(entry.key, function.apply(entry.key, entry.value));
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public BiMap<V, K> inverse() {
/* 556 */     BiMap<V, K> result = this.inverse;
/* 557 */     return (result == null) ? (this.inverse = new Inverse()) : result;
/*     */   }
/*     */   
/*     */   private final class Inverse extends Maps.IteratorBasedAbstractMap<V, K> implements BiMap<V, K>, Serializable { private Inverse() {}
/*     */     
/*     */     BiMap<K, V> forward() {
/* 563 */       return HashBiMap.this;
/*     */     }
/*     */ 
/*     */     
/*     */     public int size() {
/* 568 */       return HashBiMap.this.size;
/*     */     }
/*     */ 
/*     */     
/*     */     public void clear() {
/* 573 */       forward().clear();
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean containsKey(Object value) {
/* 578 */       return forward().containsValue(value);
/*     */     }
/*     */ 
/*     */     
/*     */     public K get(Object value) {
/* 583 */       return Maps.keyOrNull(HashBiMap.this.seekByValue(value, Hashing.smearedHash(value)));
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     @CanIgnoreReturnValue
/*     */     public K put(V value, K key) {
/* 590 */       return HashBiMap.this.putInverse(value, key, false);
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public K forcePut(V value, K key) {
/* 596 */       return HashBiMap.this.putInverse(value, key, true);
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public K remove(Object value) {
/* 602 */       HashBiMap.BiEntry<K, V> entry = HashBiMap.this.seekByValue(value, Hashing.smearedHash(value));
/* 603 */       if (entry == null) {
/* 604 */         return null;
/*     */       }
/* 606 */       HashBiMap.this.delete(entry);
/* 607 */       entry.prevInKeyInsertionOrder = null;
/* 608 */       entry.nextInKeyInsertionOrder = null;
/* 609 */       return entry.key;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public BiMap<K, V> inverse() {
/* 615 */       return forward();
/*     */     }
/*     */ 
/*     */     
/*     */     public Set<V> keySet() {
/* 620 */       return new InverseKeySet();
/*     */     }
/*     */     
/*     */     private final class InverseKeySet
/*     */       extends Maps.KeySet<V, K> {
/*     */       InverseKeySet() {
/* 626 */         super(HashBiMap.Inverse.this);
/*     */       }
/*     */ 
/*     */       
/*     */       public boolean remove(Object o) {
/* 631 */         HashBiMap.BiEntry<K, V> entry = HashBiMap.this.seekByValue(o, Hashing.smearedHash(o));
/* 632 */         if (entry == null) {
/* 633 */           return false;
/*     */         }
/* 635 */         HashBiMap.this.delete(entry);
/* 636 */         return true;
/*     */       }
/*     */ 
/*     */ 
/*     */       
/*     */       public Iterator<V> iterator() {
/* 642 */         return new HashBiMap<K, V>.Itr<V>()
/*     */           {
/*     */             V output(HashBiMap.BiEntry<K, V> entry) {
/* 645 */               return entry.value;
/*     */             }
/*     */           };
/*     */       }
/*     */     }
/*     */ 
/*     */     
/*     */     public Set<K> values() {
/* 653 */       return forward().keySet();
/*     */     }
/*     */ 
/*     */     
/*     */     Iterator<Map.Entry<V, K>> entryIterator() {
/* 658 */       return new HashBiMap<K, V>.Itr<Map.Entry<V, K>>()
/*     */         {
/*     */           Map.Entry<V, K> output(HashBiMap.BiEntry<K, V> entry) {
/* 661 */             return new InverseEntry(entry);
/*     */           }
/*     */ 
/*     */ 
/*     */           
/*     */           class InverseEntry
/*     */             extends AbstractMapEntry<V, K>
/*     */           {
/*     */             HashBiMap.BiEntry<K, V> delegate;
/*     */ 
/*     */             
/*     */             public V getKey() {
/* 673 */               return this.delegate.value;
/*     */             }
/*     */ 
/*     */             
/*     */             public K getValue() {
/* 678 */               return this.delegate.key;
/*     */             }
/*     */ 
/*     */             
/*     */             public K setValue(K key) {
/* 683 */               K oldKey = this.delegate.key;
/* 684 */               int keyHash = Hashing.smearedHash(key);
/* 685 */               if (keyHash == this.delegate.keyHash && Objects.equal(key, oldKey)) {
/* 686 */                 return key;
/*     */               }
/* 688 */               Preconditions.checkArgument((HashBiMap.this.seekByKey(key, keyHash) == null), "value already present: %s", key);
/* 689 */               HashBiMap.this.delete(this.delegate);
/* 690 */               HashBiMap.BiEntry<K, V> newEntry = new HashBiMap.BiEntry<>(key, keyHash, this.delegate.value, this.delegate.valueHash);
/*     */               
/* 692 */               this.delegate = newEntry;
/* 693 */               HashBiMap.this.insert(newEntry, (HashBiMap.BiEntry<K, V>)null);
/* 694 */               HashBiMap.Inverse.null.this.expectedModCount = HashBiMap.this.modCount;
/* 695 */               return oldKey;
/*     */             }
/*     */           }
/*     */         };
/*     */     }
/*     */ 
/*     */     
/*     */     public void forEach(BiConsumer<? super V, ? super K> action) {
/* 703 */       Preconditions.checkNotNull(action);
/* 704 */       HashBiMap.this.forEach((k, v) -> action.accept(v, k));
/*     */     }
/*     */ 
/*     */     
/*     */     public void replaceAll(BiFunction<? super V, ? super K, ? extends K> function) {
/* 709 */       Preconditions.checkNotNull(function);
/* 710 */       HashBiMap.BiEntry<K, V> oldFirst = HashBiMap.this.firstInKeyInsertionOrder;
/* 711 */       clear();
/* 712 */       for (HashBiMap.BiEntry<K, V> entry = oldFirst; entry != null; entry = entry.nextInKeyInsertionOrder) {
/* 713 */         put(entry.value, function.apply(entry.value, entry.key));
/*     */       }
/*     */     }
/*     */     
/*     */     Object writeReplace() {
/* 718 */       return new HashBiMap.InverseSerializedForm<>(HashBiMap.this);
/*     */     } }
/*     */ 
/*     */   
/*     */   private static final class InverseSerializedForm<K, V> implements Serializable {
/*     */     private final HashBiMap<K, V> bimap;
/*     */     
/*     */     InverseSerializedForm(HashBiMap<K, V> bimap) {
/* 726 */       this.bimap = bimap;
/*     */     }
/*     */     
/*     */     Object readResolve() {
/* 730 */       return this.bimap.inverse();
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @GwtIncompatible
/*     */   private void writeObject(ObjectOutputStream stream) throws IOException {
/* 739 */     stream.defaultWriteObject();
/* 740 */     Serialization.writeMap(this, stream);
/*     */   }
/*     */   
/*     */   @GwtIncompatible
/*     */   private void readObject(ObjectInputStream stream) throws IOException, ClassNotFoundException {
/* 745 */     stream.defaultReadObject();
/* 746 */     int size = Serialization.readCount(stream);
/* 747 */     init(16);
/* 748 */     Serialization.populateMap(this, stream, size);
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\google\common\collect\HashBiMap.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */