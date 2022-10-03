/*     */ package com.google.common.collect;
/*     */ 
/*     */ import com.google.common.annotations.GwtIncompatible;
/*     */ import com.google.common.annotations.VisibleForTesting;
/*     */ import com.google.common.base.Preconditions;
/*     */ import java.util.Arrays;
/*     */ import java.util.Collection;
/*     */ import java.util.Map;
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
/*     */ @GwtIncompatible
/*     */ class CompactLinkedHashMap<K, V>
/*     */   extends CompactHashMap<K, V>
/*     */ {
/*     */   private static final int ENDPOINT = -2;
/*     */   @VisibleForTesting
/*     */   transient long[] links;
/*     */   private transient int firstEntry;
/*     */   private transient int lastEntry;
/*     */   private final boolean accessOrder;
/*     */   
/*     */   public static <K, V> CompactLinkedHashMap<K, V> create() {
/*  58 */     return new CompactLinkedHashMap<>();
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
/*     */   public static <K, V> CompactLinkedHashMap<K, V> createWithExpectedSize(int expectedSize) {
/*  71 */     return new CompactLinkedHashMap<>(expectedSize);
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
/*     */   CompactLinkedHashMap() {
/*  96 */     this(3);
/*     */   }
/*     */   
/*     */   CompactLinkedHashMap(int expectedSize) {
/* 100 */     this(expectedSize, 1.0F, false);
/*     */   }
/*     */   
/*     */   CompactLinkedHashMap(int expectedSize, float loadFactor, boolean accessOrder) {
/* 104 */     super(expectedSize, loadFactor);
/* 105 */     this.accessOrder = accessOrder;
/*     */   }
/*     */ 
/*     */   
/*     */   void init(int expectedSize, float loadFactor) {
/* 110 */     super.init(expectedSize, loadFactor);
/* 111 */     this.firstEntry = -2;
/* 112 */     this.lastEntry = -2;
/* 113 */     this.links = new long[expectedSize];
/* 114 */     Arrays.fill(this.links, -1L);
/*     */   }
/*     */   
/*     */   private int getPredecessor(int entry) {
/* 118 */     return (int)(this.links[entry] >>> 32L);
/*     */   }
/*     */ 
/*     */   
/*     */   int getSuccessor(int entry) {
/* 123 */     return (int)this.links[entry];
/*     */   }
/*     */   
/*     */   private void setSuccessor(int entry, int succ) {
/* 127 */     long succMask = 4294967295L;
/* 128 */     this.links[entry] = this.links[entry] & (succMask ^ 0xFFFFFFFFFFFFFFFFL) | succ & succMask;
/*     */   }
/*     */   
/*     */   private void setPredecessor(int entry, int pred) {
/* 132 */     long predMask = -4294967296L;
/* 133 */     this.links[entry] = this.links[entry] & (predMask ^ 0xFFFFFFFFFFFFFFFFL) | pred << 32L;
/*     */   }
/*     */   
/*     */   private void setSucceeds(int pred, int succ) {
/* 137 */     if (pred == -2) {
/* 138 */       this.firstEntry = succ;
/*     */     } else {
/* 140 */       setSuccessor(pred, succ);
/*     */     } 
/* 142 */     if (succ == -2) {
/* 143 */       this.lastEntry = pred;
/*     */     } else {
/* 145 */       setPredecessor(succ, pred);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   void insertEntry(int entryIndex, K key, V value, int hash) {
/* 151 */     super.insertEntry(entryIndex, key, value, hash);
/* 152 */     setSucceeds(this.lastEntry, entryIndex);
/* 153 */     setSucceeds(entryIndex, -2);
/*     */   }
/*     */ 
/*     */   
/*     */   void accessEntry(int index) {
/* 158 */     if (this.accessOrder) {
/*     */       
/* 160 */       setSucceeds(getPredecessor(index), getSuccessor(index));
/*     */       
/* 162 */       setSucceeds(this.lastEntry, index);
/* 163 */       setSucceeds(index, -2);
/* 164 */       this.modCount++;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   void moveLastEntry(int dstIndex) {
/* 170 */     int srcIndex = size() - 1;
/* 171 */     setSucceeds(getPredecessor(dstIndex), getSuccessor(dstIndex));
/* 172 */     if (dstIndex < srcIndex) {
/* 173 */       setSucceeds(getPredecessor(srcIndex), dstIndex);
/* 174 */       setSucceeds(dstIndex, getSuccessor(srcIndex));
/*     */     } 
/* 176 */     super.moveLastEntry(dstIndex);
/*     */   }
/*     */ 
/*     */   
/*     */   void resizeEntries(int newCapacity) {
/* 181 */     super.resizeEntries(newCapacity);
/* 182 */     this.links = Arrays.copyOf(this.links, newCapacity);
/*     */   }
/*     */ 
/*     */   
/*     */   int firstEntryIndex() {
/* 187 */     return this.firstEntry;
/*     */   }
/*     */ 
/*     */   
/*     */   int adjustAfterRemove(int indexBeforeRemove, int indexRemoved) {
/* 192 */     return (indexBeforeRemove >= size()) ? indexRemoved : indexBeforeRemove;
/*     */   }
/*     */ 
/*     */   
/*     */   public void forEach(BiConsumer<? super K, ? super V> action) {
/* 197 */     Preconditions.checkNotNull(action);
/* 198 */     for (int i = this.firstEntry; i != -2; i = getSuccessor(i)) {
/* 199 */       action.accept((K)this.keys[i], (V)this.values[i]);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   Set<Map.Entry<K, V>> createEntrySet() {
/*     */     class EntrySetImpl
/*     */       extends CompactHashMap<K, V>.EntrySetView
/*     */     {
/*     */       public Spliterator<Map.Entry<K, V>> spliterator() {
/* 209 */         return Spliterators.spliterator(this, 17);
/*     */       }
/*     */     };
/* 212 */     return new EntrySetImpl();
/*     */   }
/*     */ 
/*     */   
/*     */   Set<K> createKeySet() {
/*     */     class KeySetImpl
/*     */       extends CompactHashMap<K, V>.KeySetView
/*     */     {
/*     */       public Object[] toArray() {
/* 221 */         return ObjectArrays.toArrayImpl(this);
/*     */       }
/*     */ 
/*     */       
/*     */       public <T> T[] toArray(T[] a) {
/* 226 */         return ObjectArrays.toArrayImpl(this, a);
/*     */       }
/*     */ 
/*     */       
/*     */       public Spliterator<K> spliterator() {
/* 231 */         return Spliterators.spliterator(this, 17);
/*     */       }
/*     */ 
/*     */       
/*     */       public void forEach(Consumer<? super K> action) {
/* 236 */         Preconditions.checkNotNull(action);
/* 237 */         for (int i = CompactLinkedHashMap.this.firstEntry; i != -2; i = CompactLinkedHashMap.this.getSuccessor(i)) {
/* 238 */           action.accept((K)CompactLinkedHashMap.this.keys[i]);
/*     */         }
/*     */       }
/*     */     };
/* 242 */     return new KeySetImpl();
/*     */   }
/*     */ 
/*     */   
/*     */   Collection<V> createValues() {
/*     */     class ValuesImpl
/*     */       extends CompactHashMap<K, V>.ValuesView
/*     */     {
/*     */       public Object[] toArray() {
/* 251 */         return ObjectArrays.toArrayImpl(this);
/*     */       }
/*     */ 
/*     */       
/*     */       public <T> T[] toArray(T[] a) {
/* 256 */         return ObjectArrays.toArrayImpl(this, a);
/*     */       }
/*     */ 
/*     */       
/*     */       public Spliterator<V> spliterator() {
/* 261 */         return Spliterators.spliterator(this, 16);
/*     */       }
/*     */ 
/*     */       
/*     */       public void forEach(Consumer<? super V> action) {
/* 266 */         Preconditions.checkNotNull(action);
/* 267 */         for (int i = CompactLinkedHashMap.this.firstEntry; i != -2; i = CompactLinkedHashMap.this.getSuccessor(i)) {
/* 268 */           action.accept((V)CompactLinkedHashMap.this.values[i]);
/*     */         }
/*     */       }
/*     */     };
/* 272 */     return new ValuesImpl();
/*     */   }
/*     */ 
/*     */   
/*     */   public void clear() {
/* 277 */     super.clear();
/* 278 */     this.firstEntry = -2;
/* 279 */     this.lastEntry = -2;
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\google\common\collect\CompactLinkedHashMap.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */