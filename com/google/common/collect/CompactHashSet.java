/*     */ package com.google.common.collect;
/*     */ 
/*     */ import com.google.common.annotations.GwtIncompatible;
/*     */ import com.google.common.base.Objects;
/*     */ import com.google.common.base.Preconditions;
/*     */ import com.google.errorprone.annotations.CanIgnoreReturnValue;
/*     */ import java.io.IOException;
/*     */ import java.io.ObjectInputStream;
/*     */ import java.io.ObjectOutputStream;
/*     */ import java.io.Serializable;
/*     */ import java.util.AbstractSet;
/*     */ import java.util.Arrays;
/*     */ import java.util.Collection;
/*     */ import java.util.Collections;
/*     */ import java.util.ConcurrentModificationException;
/*     */ import java.util.Iterator;
/*     */ import java.util.NoSuchElementException;
/*     */ import java.util.Spliterator;
/*     */ import java.util.Spliterators;
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
/*     */ class CompactHashSet<E>
/*     */   extends AbstractSet<E>
/*     */   implements Serializable
/*     */ {
/*     */   private static final int MAXIMUM_CAPACITY = 1073741824;
/*     */   private static final float DEFAULT_LOAD_FACTOR = 1.0F;
/*     */   private static final long NEXT_MASK = 4294967295L;
/*     */   private static final long HASH_MASK = -4294967296L;
/*     */   private static final int DEFAULT_SIZE = 3;
/*     */   static final int UNSET = -1;
/*     */   private transient int[] table;
/*     */   private transient long[] entries;
/*     */   transient Object[] elements;
/*     */   transient float loadFactor;
/*     */   transient int modCount;
/*     */   private transient int threshold;
/*     */   private transient int size;
/*     */   
/*     */   public static <E> CompactHashSet<E> create() {
/*  76 */     return new CompactHashSet<>();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <E> CompactHashSet<E> create(Collection<? extends E> collection) {
/*  87 */     CompactHashSet<E> set = createWithExpectedSize(collection.size());
/*  88 */     set.addAll(collection);
/*  89 */     return set;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <E> CompactHashSet<E> create(E... elements) {
/* 100 */     CompactHashSet<E> set = createWithExpectedSize(elements.length);
/* 101 */     Collections.addAll(set, elements);
/* 102 */     return set;
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
/*     */   public static <E> CompactHashSet<E> createWithExpectedSize(int expectedSize) {
/* 115 */     return new CompactHashSet<>(expectedSize);
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
/*     */   CompactHashSet() {
/* 173 */     init(3, 1.0F);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   CompactHashSet(int expectedSize) {
/* 182 */     init(expectedSize, 1.0F);
/*     */   }
/*     */ 
/*     */   
/*     */   void init(int expectedSize, float loadFactor) {
/* 187 */     Preconditions.checkArgument((expectedSize >= 0), "Initial capacity must be non-negative");
/* 188 */     Preconditions.checkArgument((loadFactor > 0.0F), "Illegal load factor");
/* 189 */     int buckets = Hashing.closedTableSize(expectedSize, loadFactor);
/* 190 */     this.table = newTable(buckets);
/* 191 */     this.loadFactor = loadFactor;
/* 192 */     this.elements = new Object[expectedSize];
/* 193 */     this.entries = newEntries(expectedSize);
/* 194 */     this.threshold = Math.max(1, (int)(buckets * loadFactor));
/*     */   }
/*     */   
/*     */   private static int[] newTable(int size) {
/* 198 */     int[] array = new int[size];
/* 199 */     Arrays.fill(array, -1);
/* 200 */     return array;
/*     */   }
/*     */   
/*     */   private static long[] newEntries(int size) {
/* 204 */     long[] array = new long[size];
/* 205 */     Arrays.fill(array, -1L);
/* 206 */     return array;
/*     */   }
/*     */   
/*     */   private static int getHash(long entry) {
/* 210 */     return (int)(entry >>> 32L);
/*     */   }
/*     */ 
/*     */   
/*     */   private static int getNext(long entry) {
/* 215 */     return (int)entry;
/*     */   }
/*     */ 
/*     */   
/*     */   private static long swapNext(long entry, int newNext) {
/* 220 */     return 0xFFFFFFFF00000000L & entry | 0xFFFFFFFFL & newNext;
/*     */   }
/*     */   
/*     */   private int hashTableMask() {
/* 224 */     return this.table.length - 1;
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
/*     */   @CanIgnoreReturnValue
/*     */   public boolean add(E object) {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: getfield entries : [J
/*     */     //   4: astore_2
/*     */     //   5: aload_0
/*     */     //   6: getfield elements : [Ljava/lang/Object;
/*     */     //   9: astore_3
/*     */     //   10: aload_1
/*     */     //   11: invokestatic smearedHash : (Ljava/lang/Object;)I
/*     */     //   14: istore #4
/*     */     //   16: iload #4
/*     */     //   18: aload_0
/*     */     //   19: invokespecial hashTableMask : ()I
/*     */     //   22: iand
/*     */     //   23: istore #5
/*     */     //   25: aload_0
/*     */     //   26: getfield size : I
/*     */     //   29: istore #6
/*     */     //   31: aload_0
/*     */     //   32: getfield table : [I
/*     */     //   35: iload #5
/*     */     //   37: iaload
/*     */     //   38: istore #7
/*     */     //   40: iload #7
/*     */     //   42: iconst_m1
/*     */     //   43: if_icmpne -> 58
/*     */     //   46: aload_0
/*     */     //   47: getfield table : [I
/*     */     //   50: iload #5
/*     */     //   52: iload #6
/*     */     //   54: iastore
/*     */     //   55: goto -> 115
/*     */     //   58: iload #7
/*     */     //   60: istore #8
/*     */     //   62: aload_2
/*     */     //   63: iload #7
/*     */     //   65: laload
/*     */     //   66: lstore #9
/*     */     //   68: lload #9
/*     */     //   70: invokestatic getHash : (J)I
/*     */     //   73: iload #4
/*     */     //   75: if_icmpne -> 91
/*     */     //   78: aload_1
/*     */     //   79: aload_3
/*     */     //   80: iload #7
/*     */     //   82: aaload
/*     */     //   83: invokestatic equal : (Ljava/lang/Object;Ljava/lang/Object;)Z
/*     */     //   86: ifeq -> 91
/*     */     //   89: iconst_0
/*     */     //   90: ireturn
/*     */     //   91: lload #9
/*     */     //   93: invokestatic getNext : (J)I
/*     */     //   96: istore #7
/*     */     //   98: iload #7
/*     */     //   100: iconst_m1
/*     */     //   101: if_icmpne -> 58
/*     */     //   104: aload_2
/*     */     //   105: iload #8
/*     */     //   107: lload #9
/*     */     //   109: iload #6
/*     */     //   111: invokestatic swapNext : (JI)J
/*     */     //   114: lastore
/*     */     //   115: iload #6
/*     */     //   117: ldc 2147483647
/*     */     //   119: if_icmpne -> 132
/*     */     //   122: new java/lang/IllegalStateException
/*     */     //   125: dup
/*     */     //   126: ldc 'Cannot contain more than Integer.MAX_VALUE elements!'
/*     */     //   128: invokespecial <init> : (Ljava/lang/String;)V
/*     */     //   131: athrow
/*     */     //   132: iload #6
/*     */     //   134: iconst_1
/*     */     //   135: iadd
/*     */     //   136: istore #8
/*     */     //   138: aload_0
/*     */     //   139: iload #8
/*     */     //   141: invokespecial resizeMeMaybe : (I)V
/*     */     //   144: aload_0
/*     */     //   145: iload #6
/*     */     //   147: aload_1
/*     */     //   148: iload #4
/*     */     //   150: invokevirtual insertEntry : (ILjava/lang/Object;I)V
/*     */     //   153: aload_0
/*     */     //   154: iload #8
/*     */     //   156: putfield size : I
/*     */     //   159: iload #6
/*     */     //   161: aload_0
/*     */     //   162: getfield threshold : I
/*     */     //   165: if_icmplt -> 179
/*     */     //   168: aload_0
/*     */     //   169: iconst_2
/*     */     //   170: aload_0
/*     */     //   171: getfield table : [I
/*     */     //   174: arraylength
/*     */     //   175: imul
/*     */     //   176: invokespecial resizeTable : (I)V
/*     */     //   179: aload_0
/*     */     //   180: dup
/*     */     //   181: getfield modCount : I
/*     */     //   184: iconst_1
/*     */     //   185: iadd
/*     */     //   186: putfield modCount : I
/*     */     //   189: iconst_1
/*     */     //   190: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #230	-> 0
/*     */     //   #231	-> 5
/*     */     //   #232	-> 10
/*     */     //   #233	-> 16
/*     */     //   #234	-> 25
/*     */     //   #235	-> 31
/*     */     //   #236	-> 40
/*     */     //   #237	-> 46
/*     */     //   #242	-> 58
/*     */     //   #243	-> 62
/*     */     //   #244	-> 68
/*     */     //   #245	-> 89
/*     */     //   #247	-> 91
/*     */     //   #248	-> 98
/*     */     //   #249	-> 104
/*     */     //   #251	-> 115
/*     */     //   #252	-> 122
/*     */     //   #254	-> 132
/*     */     //   #255	-> 138
/*     */     //   #256	-> 144
/*     */     //   #257	-> 153
/*     */     //   #258	-> 159
/*     */     //   #259	-> 168
/*     */     //   #261	-> 179
/*     */     //   #262	-> 189
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   62	53	8	last	I
/*     */     //   68	47	9	entry	J
/*     */     //   0	191	0	this	Lcom/google/common/collect/CompactHashSet;
/*     */     //   0	191	1	object	Ljava/lang/Object;
/*     */     //   5	186	2	entries	[J
/*     */     //   10	181	3	elements	[Ljava/lang/Object;
/*     */     //   16	175	4	hash	I
/*     */     //   25	166	5	tableIndex	I
/*     */     //   31	160	6	newEntryIndex	I
/*     */     //   40	151	7	next	I
/*     */     //   138	53	8	newSize	I
/*     */     // Local variable type table:
/*     */     //   start	length	slot	name	signature
/*     */     //   0	191	0	this	Lcom/google/common/collect/CompactHashSet<TE;>;
/*     */     //   0	191	1	object	TE;
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
/*     */   void insertEntry(int entryIndex, E object, int hash) {
/* 269 */     this.entries[entryIndex] = hash << 32L | 0xFFFFFFFFL;
/* 270 */     this.elements[entryIndex] = object;
/*     */   }
/*     */ 
/*     */   
/*     */   private void resizeMeMaybe(int newSize) {
/* 275 */     int entriesSize = this.entries.length;
/* 276 */     if (newSize > entriesSize) {
/* 277 */       int newCapacity = entriesSize + Math.max(1, entriesSize >>> 1);
/* 278 */       if (newCapacity < 0) {
/* 279 */         newCapacity = Integer.MAX_VALUE;
/*     */       }
/* 281 */       if (newCapacity != entriesSize) {
/* 282 */         resizeEntries(newCapacity);
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   void resizeEntries(int newCapacity) {
/* 292 */     this.elements = Arrays.copyOf(this.elements, newCapacity);
/* 293 */     long[] entries = this.entries;
/* 294 */     int oldSize = entries.length;
/* 295 */     entries = Arrays.copyOf(entries, newCapacity);
/* 296 */     if (newCapacity > oldSize) {
/* 297 */       Arrays.fill(entries, oldSize, newCapacity, -1L);
/*     */     }
/* 299 */     this.entries = entries;
/*     */   }
/*     */   
/*     */   private void resizeTable(int newCapacity) {
/* 303 */     int[] oldTable = this.table;
/* 304 */     int oldCapacity = oldTable.length;
/* 305 */     if (oldCapacity >= 1073741824) {
/* 306 */       this.threshold = Integer.MAX_VALUE;
/*     */       return;
/*     */     } 
/* 309 */     int newThreshold = 1 + (int)(newCapacity * this.loadFactor);
/* 310 */     int[] newTable = newTable(newCapacity);
/* 311 */     long[] entries = this.entries;
/*     */     
/* 313 */     int mask = newTable.length - 1;
/* 314 */     for (int i = 0; i < this.size; i++) {
/* 315 */       long oldEntry = entries[i];
/* 316 */       int hash = getHash(oldEntry);
/* 317 */       int tableIndex = hash & mask;
/* 318 */       int next = newTable[tableIndex];
/* 319 */       newTable[tableIndex] = i;
/* 320 */       entries[i] = hash << 32L | 0xFFFFFFFFL & next;
/*     */     } 
/*     */     
/* 323 */     this.threshold = newThreshold;
/* 324 */     this.table = newTable;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean contains(Object object) {
/* 329 */     int hash = Hashing.smearedHash(object);
/* 330 */     int next = this.table[hash & hashTableMask()];
/* 331 */     while (next != -1) {
/* 332 */       long entry = this.entries[next];
/* 333 */       if (getHash(entry) == hash && Objects.equal(object, this.elements[next])) {
/* 334 */         return true;
/*     */       }
/* 336 */       next = getNext(entry);
/*     */     } 
/* 338 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   @CanIgnoreReturnValue
/*     */   public boolean remove(Object object) {
/* 344 */     return remove(object, Hashing.smearedHash(object));
/*     */   }
/*     */   
/*     */   @CanIgnoreReturnValue
/*     */   private boolean remove(Object object, int hash) {
/* 349 */     int tableIndex = hash & hashTableMask();
/* 350 */     int next = this.table[tableIndex];
/* 351 */     if (next == -1) {
/* 352 */       return false;
/*     */     }
/* 354 */     int last = -1;
/*     */     while (true) {
/* 356 */       if (getHash(this.entries[next]) == hash && Objects.equal(object, this.elements[next])) {
/* 357 */         if (last == -1) {
/*     */           
/* 359 */           this.table[tableIndex] = getNext(this.entries[next]);
/*     */         } else {
/*     */           
/* 362 */           this.entries[last] = swapNext(this.entries[last], getNext(this.entries[next]));
/*     */         } 
/*     */         
/* 365 */         moveEntry(next);
/* 366 */         this.size--;
/* 367 */         this.modCount++;
/* 368 */         return true;
/*     */       } 
/* 370 */       last = next;
/* 371 */       next = getNext(this.entries[next]);
/* 372 */       if (next == -1) {
/* 373 */         return false;
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   void moveEntry(int dstIndex) {
/* 380 */     int srcIndex = size() - 1;
/* 381 */     if (dstIndex < srcIndex) {
/*     */       
/* 383 */       this.elements[dstIndex] = this.elements[srcIndex];
/* 384 */       this.elements[srcIndex] = null;
/*     */ 
/*     */       
/* 387 */       long lastEntry = this.entries[srcIndex];
/* 388 */       this.entries[dstIndex] = lastEntry;
/* 389 */       this.entries[srcIndex] = -1L;
/*     */ 
/*     */ 
/*     */       
/* 393 */       int tableIndex = getHash(lastEntry) & hashTableMask();
/* 394 */       int lastNext = this.table[tableIndex];
/* 395 */       if (lastNext == srcIndex) {
/*     */         
/* 397 */         this.table[tableIndex] = dstIndex;
/*     */       } else {
/*     */         int previous;
/*     */         
/*     */         long entry;
/*     */         do {
/* 403 */           previous = lastNext;
/* 404 */           lastNext = getNext(entry = this.entries[lastNext]);
/* 405 */         } while (lastNext != srcIndex);
/*     */         
/* 407 */         this.entries[previous] = swapNext(entry, dstIndex);
/*     */       } 
/*     */     } else {
/* 410 */       this.elements[dstIndex] = null;
/* 411 */       this.entries[dstIndex] = -1L;
/*     */     } 
/*     */   }
/*     */   
/*     */   int firstEntryIndex() {
/* 416 */     return isEmpty() ? -1 : 0;
/*     */   }
/*     */   
/*     */   int getSuccessor(int entryIndex) {
/* 420 */     return (entryIndex + 1 < this.size) ? (entryIndex + 1) : -1;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   int adjustAfterRemove(int indexBeforeRemove, int indexRemoved) {
/* 429 */     return indexBeforeRemove - 1;
/*     */   }
/*     */ 
/*     */   
/*     */   public Iterator<E> iterator() {
/* 434 */     return new Iterator<E>() {
/* 435 */         int expectedModCount = CompactHashSet.this.modCount;
/* 436 */         int index = CompactHashSet.this.firstEntryIndex();
/* 437 */         int indexToRemove = -1;
/*     */ 
/*     */         
/*     */         public boolean hasNext() {
/* 441 */           return (this.index >= 0);
/*     */         }
/*     */ 
/*     */ 
/*     */         
/*     */         public E next() {
/* 447 */           checkForConcurrentModification();
/* 448 */           if (!hasNext()) {
/* 449 */             throw new NoSuchElementException();
/*     */           }
/* 451 */           this.indexToRemove = this.index;
/* 452 */           E result = (E)CompactHashSet.this.elements[this.index];
/* 453 */           this.index = CompactHashSet.this.getSuccessor(this.index);
/* 454 */           return result;
/*     */         }
/*     */ 
/*     */         
/*     */         public void remove() {
/* 459 */           checkForConcurrentModification();
/* 460 */           CollectPreconditions.checkRemove((this.indexToRemove >= 0));
/* 461 */           this.expectedModCount++;
/* 462 */           CompactHashSet.this.remove(CompactHashSet.this.elements[this.indexToRemove], CompactHashSet.getHash(CompactHashSet.this.entries[this.indexToRemove]));
/* 463 */           this.index = CompactHashSet.this.adjustAfterRemove(this.index, this.indexToRemove);
/* 464 */           this.indexToRemove = -1;
/*     */         }
/*     */         
/*     */         private void checkForConcurrentModification() {
/* 468 */           if (CompactHashSet.this.modCount != this.expectedModCount) {
/* 469 */             throw new ConcurrentModificationException();
/*     */           }
/*     */         }
/*     */       };
/*     */   }
/*     */ 
/*     */   
/*     */   public Spliterator<E> spliterator() {
/* 477 */     return Spliterators.spliterator(this.elements, 0, this.size, 17);
/*     */   }
/*     */ 
/*     */   
/*     */   public void forEach(Consumer<? super E> action) {
/* 482 */     Preconditions.checkNotNull(action);
/* 483 */     for (int i = 0; i < this.size; i++) {
/* 484 */       action.accept((E)this.elements[i]);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public int size() {
/* 490 */     return this.size;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isEmpty() {
/* 495 */     return (this.size == 0);
/*     */   }
/*     */ 
/*     */   
/*     */   public Object[] toArray() {
/* 500 */     return Arrays.copyOf(this.elements, this.size);
/*     */   }
/*     */ 
/*     */   
/*     */   @CanIgnoreReturnValue
/*     */   public <T> T[] toArray(T[] a) {
/* 506 */     return ObjectArrays.toArrayImpl(this.elements, 0, this.size, a);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void trimToSize() {
/* 514 */     int size = this.size;
/* 515 */     if (size < this.entries.length) {
/* 516 */       resizeEntries(size);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 522 */     int minimumTableSize = Math.max(1, Integer.highestOneBit((int)(size / this.loadFactor)));
/* 523 */     if (minimumTableSize < 1073741824) {
/* 524 */       double load = size / minimumTableSize;
/* 525 */       if (load > this.loadFactor) {
/* 526 */         minimumTableSize <<= 1;
/*     */       }
/*     */     } 
/*     */     
/* 530 */     if (minimumTableSize < this.table.length) {
/* 531 */       resizeTable(minimumTableSize);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public void clear() {
/* 537 */     this.modCount++;
/* 538 */     Arrays.fill(this.elements, 0, this.size, (Object)null);
/* 539 */     Arrays.fill(this.table, -1);
/* 540 */     Arrays.fill(this.entries, -1L);
/* 541 */     this.size = 0;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void writeObject(ObjectOutputStream stream) throws IOException {
/* 549 */     stream.defaultWriteObject();
/* 550 */     stream.writeInt(this.size);
/* 551 */     for (E e : this) {
/* 552 */       stream.writeObject(e);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   private void readObject(ObjectInputStream stream) throws IOException, ClassNotFoundException {
/* 558 */     stream.defaultReadObject();
/* 559 */     init(3, 1.0F);
/* 560 */     int elementCount = stream.readInt();
/* 561 */     for (int i = elementCount; --i >= 0; ) {
/* 562 */       E element = (E)stream.readObject();
/* 563 */       add(element);
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\google\common\collect\CompactHashSet.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */