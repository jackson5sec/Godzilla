/*     */ package com.google.common.collect;
/*     */ 
/*     */ import com.google.common.annotations.GwtIncompatible;
/*     */ import com.google.common.annotations.VisibleForTesting;
/*     */ import com.google.common.base.Objects;
/*     */ import com.google.common.base.Preconditions;
/*     */ import com.google.errorprone.annotations.CanIgnoreReturnValue;
/*     */ import java.io.IOException;
/*     */ import java.io.ObjectInputStream;
/*     */ import java.io.ObjectOutputStream;
/*     */ import java.io.Serializable;
/*     */ import java.util.AbstractMap;
/*     */ import java.util.Arrays;
/*     */ import java.util.Collection;
/*     */ import java.util.ConcurrentModificationException;
/*     */ import java.util.Iterator;
/*     */ import java.util.Map;
/*     */ import java.util.NoSuchElementException;
/*     */ import java.util.Set;
/*     */ import java.util.Spliterator;
/*     */ import java.util.Spliterators;
/*     */ import java.util.function.BiConsumer;
/*     */ import java.util.function.BiFunction;
/*     */ import java.util.function.Consumer;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
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
/*     */ class CompactHashMap<K, V>
/*     */   extends AbstractMap<K, V>
/*     */   implements Serializable
/*     */ {
/*     */   private static final int MAXIMUM_CAPACITY = 1073741824;
/*     */   static final float DEFAULT_LOAD_FACTOR = 1.0F;
/*     */   private static final long NEXT_MASK = 4294967295L;
/*     */   private static final long HASH_MASK = -4294967296L;
/*     */   static final int DEFAULT_SIZE = 3;
/*     */   static final int UNSET = -1;
/*     */   private transient int[] table;
/*     */   @VisibleForTesting
/*     */   transient long[] entries;
/*     */   @VisibleForTesting
/*     */   transient Object[] keys;
/*     */   @VisibleForTesting
/*     */   transient Object[] values;
/*     */   transient float loadFactor;
/*     */   transient int modCount;
/*     */   private transient int threshold;
/*     */   private transient int size;
/*     */   private transient Set<K> keySetView;
/*     */   private transient Set<Map.Entry<K, V>> entrySetView;
/*     */   private transient Collection<V> valuesView;
/*     */   
/*     */   public static <K, V> CompactHashMap<K, V> create() {
/*  87 */     return new CompactHashMap<>();
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
/*     */   public static <K, V> CompactHashMap<K, V> createWithExpectedSize(int expectedSize) {
/* 100 */     return new CompactHashMap<>(expectedSize);
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   CompactHashMap() {
/* 168 */     init(3, 1.0F);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   CompactHashMap(int capacity) {
/* 177 */     this(capacity, 1.0F);
/*     */   }
/*     */   
/*     */   CompactHashMap(int expectedSize, float loadFactor) {
/* 181 */     init(expectedSize, loadFactor);
/*     */   }
/*     */ 
/*     */   
/*     */   void init(int expectedSize, float loadFactor) {
/* 186 */     Preconditions.checkArgument((expectedSize >= 0), "Initial capacity must be non-negative");
/* 187 */     Preconditions.checkArgument((loadFactor > 0.0F), "Illegal load factor");
/* 188 */     int buckets = Hashing.closedTableSize(expectedSize, loadFactor);
/* 189 */     this.table = newTable(buckets);
/* 190 */     this.loadFactor = loadFactor;
/*     */     
/* 192 */     this.keys = new Object[expectedSize];
/* 193 */     this.values = new Object[expectedSize];
/*     */     
/* 195 */     this.entries = newEntries(expectedSize);
/* 196 */     this.threshold = Math.max(1, (int)(buckets * loadFactor));
/*     */   }
/*     */   
/*     */   private static int[] newTable(int size) {
/* 200 */     int[] array = new int[size];
/* 201 */     Arrays.fill(array, -1);
/* 202 */     return array;
/*     */   }
/*     */   
/*     */   private static long[] newEntries(int size) {
/* 206 */     long[] array = new long[size];
/* 207 */     Arrays.fill(array, -1L);
/* 208 */     return array;
/*     */   }
/*     */   
/*     */   private int hashTableMask() {
/* 212 */     return this.table.length - 1;
/*     */   }
/*     */   
/*     */   private static int getHash(long entry) {
/* 216 */     return (int)(entry >>> 32L);
/*     */   }
/*     */ 
/*     */   
/*     */   private static int getNext(long entry) {
/* 221 */     return (int)entry;
/*     */   }
/*     */ 
/*     */   
/*     */   private static long swapNext(long entry, int newNext) {
/* 226 */     return 0xFFFFFFFF00000000L & entry | 0xFFFFFFFFL & newNext;
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
/*     */   void accessEntry(int index) {}
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
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
/*     */   public V put(K key, V value) {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: getfield entries : [J
/*     */     //   4: astore_3
/*     */     //   5: aload_0
/*     */     //   6: getfield keys : [Ljava/lang/Object;
/*     */     //   9: astore #4
/*     */     //   11: aload_0
/*     */     //   12: getfield values : [Ljava/lang/Object;
/*     */     //   15: astore #5
/*     */     //   17: aload_1
/*     */     //   18: invokestatic smearedHash : (Ljava/lang/Object;)I
/*     */     //   21: istore #6
/*     */     //   23: iload #6
/*     */     //   25: aload_0
/*     */     //   26: invokespecial hashTableMask : ()I
/*     */     //   29: iand
/*     */     //   30: istore #7
/*     */     //   32: aload_0
/*     */     //   33: getfield size : I
/*     */     //   36: istore #8
/*     */     //   38: aload_0
/*     */     //   39: getfield table : [I
/*     */     //   42: iload #7
/*     */     //   44: iaload
/*     */     //   45: istore #9
/*     */     //   47: iload #9
/*     */     //   49: iconst_m1
/*     */     //   50: if_icmpne -> 65
/*     */     //   53: aload_0
/*     */     //   54: getfield table : [I
/*     */     //   57: iload #7
/*     */     //   59: iload #8
/*     */     //   61: iastore
/*     */     //   62: goto -> 143
/*     */     //   65: iload #9
/*     */     //   67: istore #10
/*     */     //   69: aload_3
/*     */     //   70: iload #9
/*     */     //   72: laload
/*     */     //   73: lstore #11
/*     */     //   75: lload #11
/*     */     //   77: invokestatic getHash : (J)I
/*     */     //   80: iload #6
/*     */     //   82: if_icmpne -> 119
/*     */     //   85: aload_1
/*     */     //   86: aload #4
/*     */     //   88: iload #9
/*     */     //   90: aaload
/*     */     //   91: invokestatic equal : (Ljava/lang/Object;Ljava/lang/Object;)Z
/*     */     //   94: ifeq -> 119
/*     */     //   97: aload #5
/*     */     //   99: iload #9
/*     */     //   101: aaload
/*     */     //   102: astore #13
/*     */     //   104: aload #5
/*     */     //   106: iload #9
/*     */     //   108: aload_2
/*     */     //   109: aastore
/*     */     //   110: aload_0
/*     */     //   111: iload #9
/*     */     //   113: invokevirtual accessEntry : (I)V
/*     */     //   116: aload #13
/*     */     //   118: areturn
/*     */     //   119: lload #11
/*     */     //   121: invokestatic getNext : (J)I
/*     */     //   124: istore #9
/*     */     //   126: iload #9
/*     */     //   128: iconst_m1
/*     */     //   129: if_icmpne -> 65
/*     */     //   132: aload_3
/*     */     //   133: iload #10
/*     */     //   135: lload #11
/*     */     //   137: iload #8
/*     */     //   139: invokestatic swapNext : (JI)J
/*     */     //   142: lastore
/*     */     //   143: iload #8
/*     */     //   145: ldc 2147483647
/*     */     //   147: if_icmpne -> 160
/*     */     //   150: new java/lang/IllegalStateException
/*     */     //   153: dup
/*     */     //   154: ldc 'Cannot contain more than Integer.MAX_VALUE elements!'
/*     */     //   156: invokespecial <init> : (Ljava/lang/String;)V
/*     */     //   159: athrow
/*     */     //   160: iload #8
/*     */     //   162: iconst_1
/*     */     //   163: iadd
/*     */     //   164: istore #10
/*     */     //   166: aload_0
/*     */     //   167: iload #10
/*     */     //   169: invokespecial resizeMeMaybe : (I)V
/*     */     //   172: aload_0
/*     */     //   173: iload #8
/*     */     //   175: aload_1
/*     */     //   176: aload_2
/*     */     //   177: iload #6
/*     */     //   179: invokevirtual insertEntry : (ILjava/lang/Object;Ljava/lang/Object;I)V
/*     */     //   182: aload_0
/*     */     //   183: iload #10
/*     */     //   185: putfield size : I
/*     */     //   188: iload #8
/*     */     //   190: aload_0
/*     */     //   191: getfield threshold : I
/*     */     //   194: if_icmplt -> 208
/*     */     //   197: aload_0
/*     */     //   198: iconst_2
/*     */     //   199: aload_0
/*     */     //   200: getfield table : [I
/*     */     //   203: arraylength
/*     */     //   204: imul
/*     */     //   205: invokespecial resizeTable : (I)V
/*     */     //   208: aload_0
/*     */     //   209: dup
/*     */     //   210: getfield modCount : I
/*     */     //   213: iconst_1
/*     */     //   214: iadd
/*     */     //   215: putfield modCount : I
/*     */     //   218: aconst_null
/*     */     //   219: areturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #240	-> 0
/*     */     //   #241	-> 5
/*     */     //   #242	-> 11
/*     */     //   #244	-> 17
/*     */     //   #245	-> 23
/*     */     //   #246	-> 32
/*     */     //   #247	-> 38
/*     */     //   #248	-> 47
/*     */     //   #249	-> 53
/*     */     //   #254	-> 65
/*     */     //   #255	-> 69
/*     */     //   #256	-> 75
/*     */     //   #259	-> 97
/*     */     //   #261	-> 104
/*     */     //   #262	-> 110
/*     */     //   #263	-> 116
/*     */     //   #265	-> 119
/*     */     //   #266	-> 126
/*     */     //   #267	-> 132
/*     */     //   #269	-> 143
/*     */     //   #270	-> 150
/*     */     //   #272	-> 160
/*     */     //   #273	-> 166
/*     */     //   #274	-> 172
/*     */     //   #275	-> 182
/*     */     //   #276	-> 188
/*     */     //   #277	-> 197
/*     */     //   #279	-> 208
/*     */     //   #280	-> 218
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   104	15	13	oldValue	Ljava/lang/Object;
/*     */     //   69	74	10	last	I
/*     */     //   75	68	11	entry	J
/*     */     //   0	220	0	this	Lcom/google/common/collect/CompactHashMap;
/*     */     //   0	220	1	key	Ljava/lang/Object;
/*     */     //   0	220	2	value	Ljava/lang/Object;
/*     */     //   5	215	3	entries	[J
/*     */     //   11	209	4	keys	[Ljava/lang/Object;
/*     */     //   17	203	5	values	[Ljava/lang/Object;
/*     */     //   23	197	6	hash	I
/*     */     //   32	188	7	tableIndex	I
/*     */     //   38	182	8	newEntryIndex	I
/*     */     //   47	173	9	next	I
/*     */     //   166	54	10	newSize	I
/*     */     // Local variable type table:
/*     */     //   start	length	slot	name	signature
/*     */     //   104	15	13	oldValue	TV;
/*     */     //   0	220	0	this	Lcom/google/common/collect/CompactHashMap<TK;TV;>;
/*     */     //   0	220	1	key	TK;
/*     */     //   0	220	2	value	TV;
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
/*     */   void insertEntry(int entryIndex, K key, V value, int hash) {
/* 287 */     this.entries[entryIndex] = hash << 32L | 0xFFFFFFFFL;
/* 288 */     this.keys[entryIndex] = key;
/* 289 */     this.values[entryIndex] = value;
/*     */   }
/*     */ 
/*     */   
/*     */   private void resizeMeMaybe(int newSize) {
/* 294 */     int entriesSize = this.entries.length;
/* 295 */     if (newSize > entriesSize) {
/* 296 */       int newCapacity = entriesSize + Math.max(1, entriesSize >>> 1);
/* 297 */       if (newCapacity < 0) {
/* 298 */         newCapacity = Integer.MAX_VALUE;
/*     */       }
/* 300 */       if (newCapacity != entriesSize) {
/* 301 */         resizeEntries(newCapacity);
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   void resizeEntries(int newCapacity) {
/* 311 */     this.keys = Arrays.copyOf(this.keys, newCapacity);
/* 312 */     this.values = Arrays.copyOf(this.values, newCapacity);
/* 313 */     long[] entries = this.entries;
/* 314 */     int oldCapacity = entries.length;
/* 315 */     entries = Arrays.copyOf(entries, newCapacity);
/* 316 */     if (newCapacity > oldCapacity) {
/* 317 */       Arrays.fill(entries, oldCapacity, newCapacity, -1L);
/*     */     }
/* 319 */     this.entries = entries;
/*     */   }
/*     */   
/*     */   private void resizeTable(int newCapacity) {
/* 323 */     int[] oldTable = this.table;
/* 324 */     int oldCapacity = oldTable.length;
/* 325 */     if (oldCapacity >= 1073741824) {
/* 326 */       this.threshold = Integer.MAX_VALUE;
/*     */       return;
/*     */     } 
/* 329 */     int newThreshold = 1 + (int)(newCapacity * this.loadFactor);
/* 330 */     int[] newTable = newTable(newCapacity);
/* 331 */     long[] entries = this.entries;
/*     */     
/* 333 */     int mask = newTable.length - 1;
/* 334 */     for (int i = 0; i < this.size; i++) {
/* 335 */       long oldEntry = entries[i];
/* 336 */       int hash = getHash(oldEntry);
/* 337 */       int tableIndex = hash & mask;
/* 338 */       int next = newTable[tableIndex];
/* 339 */       newTable[tableIndex] = i;
/* 340 */       entries[i] = hash << 32L | 0xFFFFFFFFL & next;
/*     */     } 
/*     */     
/* 343 */     this.threshold = newThreshold;
/* 344 */     this.table = newTable;
/*     */   }
/*     */   
/*     */   private int indexOf(Object key) {
/* 348 */     int hash = Hashing.smearedHash(key);
/* 349 */     int next = this.table[hash & hashTableMask()];
/* 350 */     while (next != -1) {
/* 351 */       long entry = this.entries[next];
/* 352 */       if (getHash(entry) == hash && Objects.equal(key, this.keys[next])) {
/* 353 */         return next;
/*     */       }
/* 355 */       next = getNext(entry);
/*     */     } 
/* 357 */     return -1;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean containsKey(Object key) {
/* 362 */     return (indexOf(key) != -1);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public V get(Object key) {
/* 368 */     int index = indexOf(key);
/* 369 */     accessEntry(index);
/* 370 */     return (index == -1) ? null : (V)this.values[index];
/*     */   }
/*     */ 
/*     */   
/*     */   @CanIgnoreReturnValue
/*     */   public V remove(Object key) {
/* 376 */     return remove(key, Hashing.smearedHash(key));
/*     */   }
/*     */   
/*     */   private V remove(Object key, int hash) {
/* 380 */     int tableIndex = hash & hashTableMask();
/* 381 */     int next = this.table[tableIndex];
/* 382 */     if (next == -1) {
/* 383 */       return null;
/*     */     }
/* 385 */     int last = -1;
/*     */     while (true) {
/* 387 */       if (getHash(this.entries[next]) == hash && 
/* 388 */         Objects.equal(key, this.keys[next])) {
/*     */ 
/*     */         
/* 391 */         V oldValue = (V)this.values[next];
/*     */         
/* 393 */         if (last == -1) {
/*     */           
/* 395 */           this.table[tableIndex] = getNext(this.entries[next]);
/*     */         } else {
/*     */           
/* 398 */           this.entries[last] = swapNext(this.entries[last], getNext(this.entries[next]));
/*     */         } 
/*     */         
/* 401 */         moveLastEntry(next);
/* 402 */         this.size--;
/* 403 */         this.modCount++;
/* 404 */         return oldValue;
/*     */       } 
/*     */       
/* 407 */       last = next;
/* 408 */       next = getNext(this.entries[next]);
/* 409 */       if (next == -1)
/* 410 */         return null; 
/*     */     } 
/*     */   }
/*     */   @CanIgnoreReturnValue
/*     */   private V removeEntry(int entryIndex) {
/* 415 */     return remove(this.keys[entryIndex], getHash(this.entries[entryIndex]));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   void moveLastEntry(int dstIndex) {
/* 422 */     int srcIndex = size() - 1;
/* 423 */     if (dstIndex < srcIndex) {
/*     */       
/* 425 */       this.keys[dstIndex] = this.keys[srcIndex];
/* 426 */       this.values[dstIndex] = this.values[srcIndex];
/* 427 */       this.keys[srcIndex] = null;
/* 428 */       this.values[srcIndex] = null;
/*     */ 
/*     */       
/* 431 */       long lastEntry = this.entries[srcIndex];
/* 432 */       this.entries[dstIndex] = lastEntry;
/* 433 */       this.entries[srcIndex] = -1L;
/*     */ 
/*     */ 
/*     */       
/* 437 */       int tableIndex = getHash(lastEntry) & hashTableMask();
/* 438 */       int lastNext = this.table[tableIndex];
/* 439 */       if (lastNext == srcIndex) {
/*     */         
/* 441 */         this.table[tableIndex] = dstIndex;
/*     */       } else {
/*     */         int previous;
/*     */         
/*     */         long entry;
/*     */         do {
/* 447 */           previous = lastNext;
/* 448 */           lastNext = getNext(entry = this.entries[lastNext]);
/* 449 */         } while (lastNext != srcIndex);
/*     */         
/* 451 */         this.entries[previous] = swapNext(entry, dstIndex);
/*     */       } 
/*     */     } else {
/* 454 */       this.keys[dstIndex] = null;
/* 455 */       this.values[dstIndex] = null;
/* 456 */       this.entries[dstIndex] = -1L;
/*     */     } 
/*     */   }
/*     */   
/*     */   int firstEntryIndex() {
/* 461 */     return isEmpty() ? -1 : 0;
/*     */   }
/*     */   
/*     */   int getSuccessor(int entryIndex) {
/* 465 */     return (entryIndex + 1 < this.size) ? (entryIndex + 1) : -1;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   int adjustAfterRemove(int indexBeforeRemove, int indexRemoved) {
/* 474 */     return indexBeforeRemove - 1;
/*     */   }
/*     */   
/*     */   private abstract class Itr<T> implements Iterator<T> {
/* 478 */     int expectedModCount = CompactHashMap.this.modCount;
/* 479 */     int currentIndex = CompactHashMap.this.firstEntryIndex();
/* 480 */     int indexToRemove = -1;
/*     */ 
/*     */     
/*     */     public boolean hasNext() {
/* 484 */       return (this.currentIndex >= 0);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public T next() {
/* 491 */       checkForConcurrentModification();
/* 492 */       if (!hasNext()) {
/* 493 */         throw new NoSuchElementException();
/*     */       }
/* 495 */       this.indexToRemove = this.currentIndex;
/* 496 */       T result = getOutput(this.currentIndex);
/* 497 */       this.currentIndex = CompactHashMap.this.getSuccessor(this.currentIndex);
/* 498 */       return result;
/*     */     }
/*     */ 
/*     */     
/*     */     public void remove() {
/* 503 */       checkForConcurrentModification();
/* 504 */       CollectPreconditions.checkRemove((this.indexToRemove >= 0));
/* 505 */       this.expectedModCount++;
/* 506 */       CompactHashMap.this.removeEntry(this.indexToRemove);
/* 507 */       this.currentIndex = CompactHashMap.this.adjustAfterRemove(this.currentIndex, this.indexToRemove);
/* 508 */       this.indexToRemove = -1;
/*     */     }
/*     */     private Itr() {}
/*     */     private void checkForConcurrentModification() {
/* 512 */       if (CompactHashMap.this.modCount != this.expectedModCount)
/* 513 */         throw new ConcurrentModificationException(); 
/*     */     }
/*     */     
/*     */     abstract T getOutput(int param1Int);
/*     */   }
/*     */   
/*     */   public void replaceAll(BiFunction<? super K, ? super V, ? extends V> function) {
/* 520 */     Preconditions.checkNotNull(function);
/* 521 */     for (int i = 0; i < this.size; i++) {
/* 522 */       this.values[i] = function.apply((K)this.keys[i], (V)this.values[i]);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Set<K> keySet() {
/* 530 */     return (this.keySetView == null) ? (this.keySetView = createKeySet()) : this.keySetView;
/*     */   }
/*     */   
/*     */   Set<K> createKeySet() {
/* 534 */     return new KeySetView();
/*     */   }
/*     */   
/*     */   class KeySetView
/*     */     extends Maps.KeySet<K, V> {
/*     */     KeySetView() {
/* 540 */       super(CompactHashMap.this);
/*     */     }
/*     */ 
/*     */     
/*     */     public Object[] toArray() {
/* 545 */       return ObjectArrays.copyAsObjectArray(CompactHashMap.this.keys, 0, CompactHashMap.this.size);
/*     */     }
/*     */ 
/*     */     
/*     */     public <T> T[] toArray(T[] a) {
/* 550 */       return ObjectArrays.toArrayImpl(CompactHashMap.this.keys, 0, CompactHashMap.this.size, a);
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean remove(Object o) {
/* 555 */       int index = CompactHashMap.this.indexOf(o);
/* 556 */       if (index == -1) {
/* 557 */         return false;
/*     */       }
/* 559 */       CompactHashMap.this.removeEntry(index);
/* 560 */       return true;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public Iterator<K> iterator() {
/* 566 */       return CompactHashMap.this.keySetIterator();
/*     */     }
/*     */ 
/*     */     
/*     */     public Spliterator<K> spliterator() {
/* 571 */       return Spliterators.spliterator(CompactHashMap.this.keys, 0, CompactHashMap.this.size, 17);
/*     */     }
/*     */ 
/*     */     
/*     */     public void forEach(Consumer<? super K> action) {
/* 576 */       Preconditions.checkNotNull(action);
/* 577 */       for (int i = 0; i < CompactHashMap.this.size; i++) {
/* 578 */         action.accept((K)CompactHashMap.this.keys[i]);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   Iterator<K> keySetIterator() {
/* 584 */     return new Itr<K>()
/*     */       {
/*     */         K getOutput(int entry)
/*     */         {
/* 588 */           return (K)CompactHashMap.this.keys[entry];
/*     */         }
/*     */       };
/*     */   }
/*     */ 
/*     */   
/*     */   public void forEach(BiConsumer<? super K, ? super V> action) {
/* 595 */     Preconditions.checkNotNull(action);
/* 596 */     for (int i = 0; i < this.size; i++) {
/* 597 */       action.accept((K)this.keys[i], (V)this.values[i]);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Set<Map.Entry<K, V>> entrySet() {
/* 605 */     return (this.entrySetView == null) ? (this.entrySetView = createEntrySet()) : this.entrySetView;
/*     */   }
/*     */   
/*     */   Set<Map.Entry<K, V>> createEntrySet() {
/* 609 */     return new EntrySetView();
/*     */   }
/*     */   
/*     */   class EntrySetView
/*     */     extends Maps.EntrySet<K, V>
/*     */   {
/*     */     Map<K, V> map() {
/* 616 */       return CompactHashMap.this;
/*     */     }
/*     */ 
/*     */     
/*     */     public Iterator<Map.Entry<K, V>> iterator() {
/* 621 */       return CompactHashMap.this.entrySetIterator();
/*     */     }
/*     */ 
/*     */     
/*     */     public Spliterator<Map.Entry<K, V>> spliterator() {
/* 626 */       return CollectSpliterators.indexed(CompactHashMap.this
/* 627 */           .size, 17, x$0 -> new CompactHashMap.MapEntry(x$0));
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean contains(Object o) {
/* 632 */       if (o instanceof Map.Entry) {
/* 633 */         Map.Entry<?, ?> entry = (Map.Entry<?, ?>)o;
/* 634 */         int index = CompactHashMap.this.indexOf(entry.getKey());
/* 635 */         return (index != -1 && Objects.equal(CompactHashMap.this.values[index], entry.getValue()));
/*     */       } 
/* 637 */       return false;
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean remove(Object o) {
/* 642 */       if (o instanceof Map.Entry) {
/* 643 */         Map.Entry<?, ?> entry = (Map.Entry<?, ?>)o;
/* 644 */         int index = CompactHashMap.this.indexOf(entry.getKey());
/* 645 */         if (index != -1 && Objects.equal(CompactHashMap.this.values[index], entry.getValue())) {
/* 646 */           CompactHashMap.this.removeEntry(index);
/* 647 */           return true;
/*     */         } 
/*     */       } 
/* 650 */       return false;
/*     */     }
/*     */   }
/*     */   
/*     */   Iterator<Map.Entry<K, V>> entrySetIterator() {
/* 655 */     return new Itr<Map.Entry<K, V>>()
/*     */       {
/*     */         Map.Entry<K, V> getOutput(int entry) {
/* 658 */           return new CompactHashMap.MapEntry(entry);
/*     */         }
/*     */       };
/*     */   }
/*     */   
/*     */   final class MapEntry
/*     */     extends AbstractMapEntry<K, V>
/*     */   {
/*     */     private final K key;
/*     */     private int lastKnownIndex;
/*     */     
/*     */     MapEntry(int index) {
/* 670 */       this.key = (K)CompactHashMap.this.keys[index];
/* 671 */       this.lastKnownIndex = index;
/*     */     }
/*     */ 
/*     */     
/*     */     public K getKey() {
/* 676 */       return this.key;
/*     */     }
/*     */     
/*     */     private void updateLastKnownIndex() {
/* 680 */       if (this.lastKnownIndex == -1 || this.lastKnownIndex >= CompactHashMap.this
/* 681 */         .size() || 
/* 682 */         !Objects.equal(this.key, CompactHashMap.this.keys[this.lastKnownIndex])) {
/* 683 */         this.lastKnownIndex = CompactHashMap.this.indexOf(this.key);
/*     */       }
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public V getValue() {
/* 690 */       updateLastKnownIndex();
/* 691 */       return (this.lastKnownIndex == -1) ? null : (V)CompactHashMap.this.values[this.lastKnownIndex];
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public V setValue(V value) {
/* 697 */       updateLastKnownIndex();
/* 698 */       if (this.lastKnownIndex == -1) {
/* 699 */         CompactHashMap.this.put(this.key, value);
/* 700 */         return null;
/*     */       } 
/* 702 */       V old = (V)CompactHashMap.this.values[this.lastKnownIndex];
/* 703 */       CompactHashMap.this.values[this.lastKnownIndex] = value;
/* 704 */       return old;
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public int size() {
/* 711 */     return this.size;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isEmpty() {
/* 716 */     return (this.size == 0);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean containsValue(Object value) {
/* 721 */     for (int i = 0; i < this.size; i++) {
/* 722 */       if (Objects.equal(value, this.values[i])) {
/* 723 */         return true;
/*     */       }
/*     */     } 
/* 726 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Collection<V> values() {
/* 733 */     return (this.valuesView == null) ? (this.valuesView = createValues()) : this.valuesView;
/*     */   }
/*     */   
/*     */   Collection<V> createValues() {
/* 737 */     return new ValuesView();
/*     */   }
/*     */   
/*     */   class ValuesView
/*     */     extends Maps.Values<K, V> {
/*     */     ValuesView() {
/* 743 */       super(CompactHashMap.this);
/*     */     }
/*     */ 
/*     */     
/*     */     public Iterator<V> iterator() {
/* 748 */       return CompactHashMap.this.valuesIterator();
/*     */     }
/*     */ 
/*     */     
/*     */     public void forEach(Consumer<? super V> action) {
/* 753 */       Preconditions.checkNotNull(action);
/* 754 */       for (int i = 0; i < CompactHashMap.this.size; i++) {
/* 755 */         action.accept((V)CompactHashMap.this.values[i]);
/*     */       }
/*     */     }
/*     */ 
/*     */     
/*     */     public Spliterator<V> spliterator() {
/* 761 */       return Spliterators.spliterator(CompactHashMap.this.values, 0, CompactHashMap.this.size, 16);
/*     */     }
/*     */ 
/*     */     
/*     */     public Object[] toArray() {
/* 766 */       return ObjectArrays.copyAsObjectArray(CompactHashMap.this.values, 0, CompactHashMap.this.size);
/*     */     }
/*     */ 
/*     */     
/*     */     public <T> T[] toArray(T[] a) {
/* 771 */       return ObjectArrays.toArrayImpl(CompactHashMap.this.values, 0, CompactHashMap.this.size, a);
/*     */     }
/*     */   }
/*     */   
/*     */   Iterator<V> valuesIterator() {
/* 776 */     return new Itr<V>()
/*     */       {
/*     */         V getOutput(int entry)
/*     */         {
/* 780 */           return (V)CompactHashMap.this.values[entry];
/*     */         }
/*     */       };
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void trimToSize() {
/* 790 */     int size = this.size;
/* 791 */     if (size < this.entries.length) {
/* 792 */       resizeEntries(size);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 798 */     int minimumTableSize = Math.max(1, Integer.highestOneBit((int)(size / this.loadFactor)));
/* 799 */     if (minimumTableSize < 1073741824) {
/* 800 */       double load = size / minimumTableSize;
/* 801 */       if (load > this.loadFactor) {
/* 802 */         minimumTableSize <<= 1;
/*     */       }
/*     */     } 
/*     */     
/* 806 */     if (minimumTableSize < this.table.length) {
/* 807 */       resizeTable(minimumTableSize);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public void clear() {
/* 813 */     this.modCount++;
/* 814 */     Arrays.fill(this.keys, 0, this.size, (Object)null);
/* 815 */     Arrays.fill(this.values, 0, this.size, (Object)null);
/* 816 */     Arrays.fill(this.table, -1);
/* 817 */     Arrays.fill(this.entries, -1L);
/* 818 */     this.size = 0;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void writeObject(ObjectOutputStream stream) throws IOException {
/* 826 */     stream.defaultWriteObject();
/* 827 */     stream.writeInt(this.size);
/* 828 */     for (int i = 0; i < this.size; i++) {
/* 829 */       stream.writeObject(this.keys[i]);
/* 830 */       stream.writeObject(this.values[i]);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private void readObject(ObjectInputStream stream) throws IOException, ClassNotFoundException {
/* 836 */     stream.defaultReadObject();
/* 837 */     init(3, 1.0F);
/* 838 */     int elementCount = stream.readInt();
/* 839 */     for (int i = elementCount; --i >= 0; ) {
/* 840 */       K key = (K)stream.readObject();
/* 841 */       V value = (V)stream.readObject();
/* 842 */       put(key, value);
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\google\common\collect\CompactHashMap.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */