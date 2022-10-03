/*     */ package com.google.common.collect;
/*     */ 
/*     */ import com.google.common.annotations.GwtCompatible;
/*     */ import com.google.common.annotations.GwtIncompatible;
/*     */ import com.google.common.annotations.VisibleForTesting;
/*     */ import com.google.common.base.Objects;
/*     */ import com.google.common.base.Preconditions;
/*     */ import com.google.errorprone.annotations.CanIgnoreReturnValue;
/*     */ import java.io.IOException;
/*     */ import java.io.ObjectInputStream;
/*     */ import java.io.ObjectOutputStream;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @GwtCompatible(serializable = true, emulated = true)
/*     */ public final class LinkedHashMultimap<K, V>
/*     */   extends LinkedHashMultimapGwtSerializationDependencies<K, V>
/*     */ {
/*     */   private static final int DEFAULT_KEY_CAPACITY = 16;
/*     */   private static final int DEFAULT_VALUE_SET_CAPACITY = 2;
/*     */   @VisibleForTesting
/*     */   static final double VALUE_SET_LOAD_FACTOR = 1.0D;
/*     */   
/*     */   public static <K, V> LinkedHashMultimap<K, V> create() {
/*  86 */     return new LinkedHashMultimap<>(16, 2);
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
/*     */   public static <K, V> LinkedHashMultimap<K, V> create(int expectedKeys, int expectedValuesPerKey) {
/*  99 */     return new LinkedHashMultimap<>(
/* 100 */         Maps.capacity(expectedKeys), Maps.capacity(expectedValuesPerKey));
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
/*     */   public static <K, V> LinkedHashMultimap<K, V> create(Multimap<? extends K, ? extends V> multimap) {
/* 113 */     LinkedHashMultimap<K, V> result = create(multimap.keySet().size(), 2);
/* 114 */     result.putAll(multimap);
/* 115 */     return result;
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
/*     */   private static <K, V> void succeedsInValueSet(ValueSetLink<K, V> pred, ValueSetLink<K, V> succ) {
/* 129 */     pred.setSuccessorInValueSet(succ);
/* 130 */     succ.setPredecessorInValueSet(pred);
/*     */   }
/*     */   
/*     */   private static <K, V> void succeedsInMultimap(ValueEntry<K, V> pred, ValueEntry<K, V> succ) {
/* 134 */     pred.setSuccessorInMultimap(succ);
/* 135 */     succ.setPredecessorInMultimap(pred);
/*     */   }
/*     */   
/*     */   private static <K, V> void deleteFromValueSet(ValueSetLink<K, V> entry) {
/* 139 */     succeedsInValueSet(entry.getPredecessorInValueSet(), entry.getSuccessorInValueSet());
/*     */   }
/*     */   
/*     */   private static <K, V> void deleteFromMultimap(ValueEntry<K, V> entry) {
/* 143 */     succeedsInMultimap(entry.getPredecessorInMultimap(), entry.getSuccessorInMultimap());
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @VisibleForTesting
/*     */   static final class ValueEntry<K, V>
/*     */     extends ImmutableEntry<K, V>
/*     */     implements ValueSetLink<K, V>
/*     */   {
/*     */     final int smearedValueHash;
/*     */ 
/*     */     
/*     */     ValueEntry<K, V> nextInValueBucket;
/*     */ 
/*     */     
/*     */     LinkedHashMultimap.ValueSetLink<K, V> predecessorInValueSet;
/*     */     
/*     */     LinkedHashMultimap.ValueSetLink<K, V> successorInValueSet;
/*     */     
/*     */     ValueEntry<K, V> predecessorInMultimap;
/*     */     
/*     */     ValueEntry<K, V> successorInMultimap;
/*     */ 
/*     */     
/*     */     ValueEntry(K key, V value, int smearedValueHash, ValueEntry<K, V> nextInValueBucket) {
/* 169 */       super(key, value);
/* 170 */       this.smearedValueHash = smearedValueHash;
/* 171 */       this.nextInValueBucket = nextInValueBucket;
/*     */     }
/*     */     
/*     */     boolean matchesValue(Object v, int smearedVHash) {
/* 175 */       return (this.smearedValueHash == smearedVHash && Objects.equal(getValue(), v));
/*     */     }
/*     */ 
/*     */     
/*     */     public LinkedHashMultimap.ValueSetLink<K, V> getPredecessorInValueSet() {
/* 180 */       return this.predecessorInValueSet;
/*     */     }
/*     */ 
/*     */     
/*     */     public LinkedHashMultimap.ValueSetLink<K, V> getSuccessorInValueSet() {
/* 185 */       return this.successorInValueSet;
/*     */     }
/*     */ 
/*     */     
/*     */     public void setPredecessorInValueSet(LinkedHashMultimap.ValueSetLink<K, V> entry) {
/* 190 */       this.predecessorInValueSet = entry;
/*     */     }
/*     */ 
/*     */     
/*     */     public void setSuccessorInValueSet(LinkedHashMultimap.ValueSetLink<K, V> entry) {
/* 195 */       this.successorInValueSet = entry;
/*     */     }
/*     */     
/*     */     public ValueEntry<K, V> getPredecessorInMultimap() {
/* 199 */       return this.predecessorInMultimap;
/*     */     }
/*     */     
/*     */     public ValueEntry<K, V> getSuccessorInMultimap() {
/* 203 */       return this.successorInMultimap;
/*     */     }
/*     */     
/*     */     public void setSuccessorInMultimap(ValueEntry<K, V> multimapSuccessor) {
/* 207 */       this.successorInMultimap = multimapSuccessor;
/*     */     }
/*     */     
/*     */     public void setPredecessorInMultimap(ValueEntry<K, V> multimapPredecessor) {
/* 211 */       this.predecessorInMultimap = multimapPredecessor;
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @VisibleForTesting
/* 219 */   transient int valueSetCapacity = 2;
/*     */   private transient ValueEntry<K, V> multimapHeaderEntry;
/*     */   
/*     */   private LinkedHashMultimap(int keyCapacity, int valueSetCapacity) {
/* 223 */     super(Platform.newLinkedHashMapWithExpectedSize(keyCapacity));
/* 224 */     CollectPreconditions.checkNonnegative(valueSetCapacity, "expectedValuesPerKey");
/*     */     
/* 226 */     this.valueSetCapacity = valueSetCapacity;
/* 227 */     this.multimapHeaderEntry = new ValueEntry<>(null, null, 0, null);
/* 228 */     succeedsInMultimap(this.multimapHeaderEntry, this.multimapHeaderEntry);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @GwtIncompatible
/*     */   private static final long serialVersionUID = 1L;
/*     */ 
/*     */ 
/*     */   
/*     */   Set<V> createCollection() {
/* 240 */     return Platform.newLinkedHashSetWithExpectedSize(this.valueSetCapacity);
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
/*     */   Collection<V> createCollection(K key) {
/* 254 */     return new ValueSet(key, this.valueSetCapacity);
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
/*     */   @CanIgnoreReturnValue
/*     */   public Set<V> replaceValues(K key, Iterable<? extends V> values) {
/* 267 */     return super.replaceValues(key, values);
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
/*     */   public Set<Map.Entry<K, V>> entries() {
/* 283 */     return super.entries();
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
/*     */   public Set<K> keySet() {
/* 298 */     return super.keySet();
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
/*     */   public Collection<V> values() {
/* 310 */     return super.values();
/*     */   }
/*     */ 
/*     */   
/*     */   @VisibleForTesting
/*     */   final class ValueSet
/*     */     extends Sets.ImprovedAbstractSet<V>
/*     */     implements ValueSetLink<K, V>
/*     */   {
/*     */     private final K key;
/*     */     
/*     */     @VisibleForTesting
/*     */     LinkedHashMultimap.ValueEntry<K, V>[] hashTable;
/* 323 */     private int size = 0;
/* 324 */     private int modCount = 0;
/*     */     
/*     */     private LinkedHashMultimap.ValueSetLink<K, V> firstEntry;
/*     */     
/*     */     private LinkedHashMultimap.ValueSetLink<K, V> lastEntry;
/*     */ 
/*     */     
/*     */     ValueSet(K key, int expectedValues) {
/* 332 */       this.key = key;
/* 333 */       this.firstEntry = this;
/* 334 */       this.lastEntry = this;
/*     */       
/* 336 */       int tableSize = Hashing.closedTableSize(expectedValues, 1.0D);
/*     */ 
/*     */       
/* 339 */       LinkedHashMultimap.ValueEntry[] arrayOfValueEntry = new LinkedHashMultimap.ValueEntry[tableSize];
/* 340 */       this.hashTable = (LinkedHashMultimap.ValueEntry<K, V>[])arrayOfValueEntry;
/*     */     }
/*     */     
/*     */     private int mask() {
/* 344 */       return this.hashTable.length - 1;
/*     */     }
/*     */ 
/*     */     
/*     */     public LinkedHashMultimap.ValueSetLink<K, V> getPredecessorInValueSet() {
/* 349 */       return this.lastEntry;
/*     */     }
/*     */ 
/*     */     
/*     */     public LinkedHashMultimap.ValueSetLink<K, V> getSuccessorInValueSet() {
/* 354 */       return this.firstEntry;
/*     */     }
/*     */ 
/*     */     
/*     */     public void setPredecessorInValueSet(LinkedHashMultimap.ValueSetLink<K, V> entry) {
/* 359 */       this.lastEntry = entry;
/*     */     }
/*     */ 
/*     */     
/*     */     public void setSuccessorInValueSet(LinkedHashMultimap.ValueSetLink<K, V> entry) {
/* 364 */       this.firstEntry = entry;
/*     */     }
/*     */ 
/*     */     
/*     */     public Iterator<V> iterator() {
/* 369 */       return new Iterator<V>() {
/* 370 */           LinkedHashMultimap.ValueSetLink<K, V> nextEntry = LinkedHashMultimap.ValueSet.this.firstEntry;
/*     */           LinkedHashMultimap.ValueEntry<K, V> toRemove;
/* 372 */           int expectedModCount = LinkedHashMultimap.ValueSet.this.modCount;
/*     */           
/*     */           private void checkForComodification() {
/* 375 */             if (LinkedHashMultimap.ValueSet.this.modCount != this.expectedModCount) {
/* 376 */               throw new ConcurrentModificationException();
/*     */             }
/*     */           }
/*     */ 
/*     */           
/*     */           public boolean hasNext() {
/* 382 */             checkForComodification();
/* 383 */             return (this.nextEntry != LinkedHashMultimap.ValueSet.this);
/*     */           }
/*     */ 
/*     */           
/*     */           public V next() {
/* 388 */             if (!hasNext()) {
/* 389 */               throw new NoSuchElementException();
/*     */             }
/* 391 */             LinkedHashMultimap.ValueEntry<K, V> entry = (LinkedHashMultimap.ValueEntry<K, V>)this.nextEntry;
/* 392 */             V result = entry.getValue();
/* 393 */             this.toRemove = entry;
/* 394 */             this.nextEntry = entry.getSuccessorInValueSet();
/* 395 */             return result;
/*     */           }
/*     */ 
/*     */           
/*     */           public void remove() {
/* 400 */             checkForComodification();
/* 401 */             CollectPreconditions.checkRemove((this.toRemove != null));
/* 402 */             LinkedHashMultimap.ValueSet.this.remove(this.toRemove.getValue());
/* 403 */             this.expectedModCount = LinkedHashMultimap.ValueSet.this.modCount;
/* 404 */             this.toRemove = null;
/*     */           }
/*     */         };
/*     */     }
/*     */ 
/*     */     
/*     */     public void forEach(Consumer<? super V> action) {
/* 411 */       Preconditions.checkNotNull(action);
/* 412 */       LinkedHashMultimap.ValueSetLink<K, V> entry = this.firstEntry;
/* 413 */       for (; entry != this; 
/* 414 */         entry = entry.getSuccessorInValueSet()) {
/* 415 */         action.accept((V)((LinkedHashMultimap.ValueEntry)entry).getValue());
/*     */       }
/*     */     }
/*     */ 
/*     */     
/*     */     public int size() {
/* 421 */       return this.size;
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean contains(Object o) {
/* 426 */       int smearedHash = Hashing.smearedHash(o);
/* 427 */       LinkedHashMultimap.ValueEntry<K, V> entry = this.hashTable[smearedHash & mask()];
/* 428 */       for (; entry != null; 
/* 429 */         entry = entry.nextInValueBucket) {
/* 430 */         if (entry.matchesValue(o, smearedHash)) {
/* 431 */           return true;
/*     */         }
/*     */       } 
/* 434 */       return false;
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean add(V value) {
/* 439 */       int smearedHash = Hashing.smearedHash(value);
/* 440 */       int bucket = smearedHash & mask();
/* 441 */       LinkedHashMultimap.ValueEntry<K, V> rowHead = this.hashTable[bucket];
/* 442 */       for (LinkedHashMultimap.ValueEntry<K, V> entry = rowHead; entry != null; entry = entry.nextInValueBucket) {
/* 443 */         if (entry.matchesValue(value, smearedHash)) {
/* 444 */           return false;
/*     */         }
/*     */       } 
/*     */       
/* 448 */       LinkedHashMultimap.ValueEntry<K, V> newEntry = new LinkedHashMultimap.ValueEntry<>(this.key, value, smearedHash, rowHead);
/* 449 */       LinkedHashMultimap.succeedsInValueSet(this.lastEntry, newEntry);
/* 450 */       LinkedHashMultimap.succeedsInValueSet(newEntry, this);
/* 451 */       LinkedHashMultimap.succeedsInMultimap(LinkedHashMultimap.this.multimapHeaderEntry.getPredecessorInMultimap(), newEntry);
/* 452 */       LinkedHashMultimap.succeedsInMultimap(newEntry, LinkedHashMultimap.this.multimapHeaderEntry);
/* 453 */       this.hashTable[bucket] = newEntry;
/* 454 */       this.size++;
/* 455 */       this.modCount++;
/* 456 */       rehashIfNecessary();
/* 457 */       return true;
/*     */     }
/*     */     
/*     */     private void rehashIfNecessary() {
/* 461 */       if (Hashing.needsResizing(this.size, this.hashTable.length, 1.0D)) {
/*     */         
/* 463 */         LinkedHashMultimap.ValueEntry[] arrayOfValueEntry = new LinkedHashMultimap.ValueEntry[this.hashTable.length * 2];
/* 464 */         this.hashTable = (LinkedHashMultimap.ValueEntry<K, V>[])arrayOfValueEntry;
/* 465 */         int mask = arrayOfValueEntry.length - 1;
/* 466 */         LinkedHashMultimap.ValueSetLink<K, V> entry = this.firstEntry;
/* 467 */         for (; entry != this; 
/* 468 */           entry = entry.getSuccessorInValueSet()) {
/* 469 */           LinkedHashMultimap.ValueEntry<K, V> valueEntry = (LinkedHashMultimap.ValueEntry<K, V>)entry;
/* 470 */           int bucket = valueEntry.smearedValueHash & mask;
/* 471 */           valueEntry.nextInValueBucket = arrayOfValueEntry[bucket];
/* 472 */           arrayOfValueEntry[bucket] = valueEntry;
/*     */         } 
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     @CanIgnoreReturnValue
/*     */     public boolean remove(Object o) {
/* 480 */       int smearedHash = Hashing.smearedHash(o);
/* 481 */       int bucket = smearedHash & mask();
/* 482 */       LinkedHashMultimap.ValueEntry<K, V> prev = null;
/* 483 */       LinkedHashMultimap.ValueEntry<K, V> entry = this.hashTable[bucket];
/* 484 */       for (; entry != null; 
/* 485 */         prev = entry, entry = entry.nextInValueBucket) {
/* 486 */         if (entry.matchesValue(o, smearedHash)) {
/* 487 */           if (prev == null) {
/*     */             
/* 489 */             this.hashTable[bucket] = entry.nextInValueBucket;
/*     */           } else {
/* 491 */             prev.nextInValueBucket = entry.nextInValueBucket;
/*     */           } 
/* 493 */           LinkedHashMultimap.deleteFromValueSet(entry);
/* 494 */           LinkedHashMultimap.deleteFromMultimap(entry);
/* 495 */           this.size--;
/* 496 */           this.modCount++;
/* 497 */           return true;
/*     */         } 
/*     */       } 
/* 500 */       return false;
/*     */     }
/*     */ 
/*     */     
/*     */     public void clear() {
/* 505 */       Arrays.fill((Object[])this.hashTable, (Object)null);
/* 506 */       this.size = 0;
/* 507 */       LinkedHashMultimap.ValueSetLink<K, V> entry = this.firstEntry;
/* 508 */       for (; entry != this; 
/* 509 */         entry = entry.getSuccessorInValueSet()) {
/* 510 */         LinkedHashMultimap.ValueEntry<K, V> valueEntry = (LinkedHashMultimap.ValueEntry<K, V>)entry;
/* 511 */         LinkedHashMultimap.deleteFromMultimap(valueEntry);
/*     */       } 
/* 513 */       LinkedHashMultimap.succeedsInValueSet(this, this);
/* 514 */       this.modCount++;
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   Iterator<Map.Entry<K, V>> entryIterator() {
/* 520 */     return new Iterator<Map.Entry<K, V>>() {
/* 521 */         LinkedHashMultimap.ValueEntry<K, V> nextEntry = LinkedHashMultimap.this.multimapHeaderEntry.successorInMultimap;
/*     */         
/*     */         LinkedHashMultimap.ValueEntry<K, V> toRemove;
/*     */         
/*     */         public boolean hasNext() {
/* 526 */           return (this.nextEntry != LinkedHashMultimap.this.multimapHeaderEntry);
/*     */         }
/*     */ 
/*     */         
/*     */         public Map.Entry<K, V> next() {
/* 531 */           if (!hasNext()) {
/* 532 */             throw new NoSuchElementException();
/*     */           }
/* 534 */           LinkedHashMultimap.ValueEntry<K, V> result = this.nextEntry;
/* 535 */           this.toRemove = result;
/* 536 */           this.nextEntry = this.nextEntry.successorInMultimap;
/* 537 */           return result;
/*     */         }
/*     */ 
/*     */         
/*     */         public void remove() {
/* 542 */           CollectPreconditions.checkRemove((this.toRemove != null));
/* 543 */           LinkedHashMultimap.this.remove(this.toRemove.getKey(), this.toRemove.getValue());
/* 544 */           this.toRemove = null;
/*     */         }
/*     */       };
/*     */   }
/*     */ 
/*     */   
/*     */   Spliterator<Map.Entry<K, V>> entrySpliterator() {
/* 551 */     return Spliterators.spliterator(entries(), 17);
/*     */   }
/*     */ 
/*     */   
/*     */   Iterator<V> valueIterator() {
/* 556 */     return Maps.valueIterator(entryIterator());
/*     */   }
/*     */ 
/*     */   
/*     */   Spliterator<V> valueSpliterator() {
/* 561 */     return CollectSpliterators.map(entrySpliterator(), Map.Entry::getValue);
/*     */   }
/*     */ 
/*     */   
/*     */   public void clear() {
/* 566 */     super.clear();
/* 567 */     succeedsInMultimap(this.multimapHeaderEntry, this.multimapHeaderEntry);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @GwtIncompatible
/*     */   private void writeObject(ObjectOutputStream stream) throws IOException {
/* 576 */     stream.defaultWriteObject();
/* 577 */     stream.writeInt(keySet().size());
/* 578 */     for (K key : keySet()) {
/* 579 */       stream.writeObject(key);
/*     */     }
/* 581 */     stream.writeInt(size());
/* 582 */     for (Map.Entry<K, V> entry : entries()) {
/* 583 */       stream.writeObject(entry.getKey());
/* 584 */       stream.writeObject(entry.getValue());
/*     */     } 
/*     */   }
/*     */   
/*     */   @GwtIncompatible
/*     */   private void readObject(ObjectInputStream stream) throws IOException, ClassNotFoundException {
/* 590 */     stream.defaultReadObject();
/* 591 */     this.multimapHeaderEntry = new ValueEntry<>(null, null, 0, null);
/* 592 */     succeedsInMultimap(this.multimapHeaderEntry, this.multimapHeaderEntry);
/* 593 */     this.valueSetCapacity = 2;
/* 594 */     int distinctKeys = stream.readInt();
/* 595 */     Map<K, Collection<V>> map = Platform.newLinkedHashMapWithExpectedSize(12);
/* 596 */     for (int i = 0; i < distinctKeys; i++) {
/*     */       
/* 598 */       K key = (K)stream.readObject();
/* 599 */       map.put(key, createCollection(key));
/*     */     } 
/* 601 */     int entries = stream.readInt();
/* 602 */     for (int j = 0; j < entries; j++) {
/*     */       
/* 604 */       K key = (K)stream.readObject();
/*     */       
/* 606 */       V value = (V)stream.readObject();
/* 607 */       ((Collection<V>)map.get(key)).add(value);
/*     */     } 
/* 609 */     setMap(map);
/*     */   }
/*     */   
/*     */   private static interface ValueSetLink<K, V> {
/*     */     ValueSetLink<K, V> getPredecessorInValueSet();
/*     */     
/*     */     ValueSetLink<K, V> getSuccessorInValueSet();
/*     */     
/*     */     void setPredecessorInValueSet(ValueSetLink<K, V> param1ValueSetLink);
/*     */     
/*     */     void setSuccessorInValueSet(ValueSetLink<K, V> param1ValueSetLink);
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\google\common\collect\LinkedHashMultimap.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */