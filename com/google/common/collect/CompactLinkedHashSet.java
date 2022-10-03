/*     */ package com.google.common.collect;
/*     */ 
/*     */ import com.google.common.annotations.GwtIncompatible;
/*     */ import com.google.common.base.Preconditions;
/*     */ import java.util.Arrays;
/*     */ import java.util.Collection;
/*     */ import java.util.Collections;
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
/*     */ @GwtIncompatible
/*     */ class CompactLinkedHashSet<E>
/*     */   extends CompactHashSet<E>
/*     */ {
/*     */   private static final int ENDPOINT = -2;
/*     */   private transient int[] predecessor;
/*     */   private transient int[] successor;
/*     */   private transient int firstEntry;
/*     */   private transient int lastEntry;
/*     */   
/*     */   public static <E> CompactLinkedHashSet<E> create() {
/*  56 */     return new CompactLinkedHashSet<>();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <E> CompactLinkedHashSet<E> create(Collection<? extends E> collection) {
/*  67 */     CompactLinkedHashSet<E> set = createWithExpectedSize(collection.size());
/*  68 */     set.addAll(collection);
/*  69 */     return set;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <E> CompactLinkedHashSet<E> create(E... elements) {
/*  80 */     CompactLinkedHashSet<E> set = createWithExpectedSize(elements.length);
/*  81 */     Collections.addAll(set, elements);
/*  82 */     return set;
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
/*     */   public static <E> CompactLinkedHashSet<E> createWithExpectedSize(int expectedSize) {
/*  95 */     return new CompactLinkedHashSet<>(expectedSize);
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
/*     */   CompactLinkedHashSet() {}
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   CompactLinkedHashSet(int expectedSize) {
/* 124 */     super(expectedSize);
/*     */   }
/*     */ 
/*     */   
/*     */   void init(int expectedSize, float loadFactor) {
/* 129 */     super.init(expectedSize, loadFactor);
/* 130 */     this.predecessor = new int[expectedSize];
/* 131 */     this.successor = new int[expectedSize];
/*     */     
/* 133 */     Arrays.fill(this.predecessor, -1);
/* 134 */     Arrays.fill(this.successor, -1);
/* 135 */     this.firstEntry = -2;
/* 136 */     this.lastEntry = -2;
/*     */   }
/*     */   
/*     */   private void succeeds(int pred, int succ) {
/* 140 */     if (pred == -2) {
/* 141 */       this.firstEntry = succ;
/*     */     } else {
/* 143 */       this.successor[pred] = succ;
/*     */     } 
/*     */     
/* 146 */     if (succ == -2) {
/* 147 */       this.lastEntry = pred;
/*     */     } else {
/* 149 */       this.predecessor[succ] = pred;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   void insertEntry(int entryIndex, E object, int hash) {
/* 155 */     super.insertEntry(entryIndex, object, hash);
/* 156 */     succeeds(this.lastEntry, entryIndex);
/* 157 */     succeeds(entryIndex, -2);
/*     */   }
/*     */ 
/*     */   
/*     */   void moveEntry(int dstIndex) {
/* 162 */     int srcIndex = size() - 1;
/* 163 */     super.moveEntry(dstIndex);
/*     */     
/* 165 */     succeeds(this.predecessor[dstIndex], this.successor[dstIndex]);
/* 166 */     if (srcIndex != dstIndex) {
/* 167 */       succeeds(this.predecessor[srcIndex], dstIndex);
/* 168 */       succeeds(dstIndex, this.successor[srcIndex]);
/*     */     } 
/* 170 */     this.predecessor[srcIndex] = -1;
/* 171 */     this.successor[srcIndex] = -1;
/*     */   }
/*     */ 
/*     */   
/*     */   public void clear() {
/* 176 */     super.clear();
/* 177 */     this.firstEntry = -2;
/* 178 */     this.lastEntry = -2;
/* 179 */     Arrays.fill(this.predecessor, -1);
/* 180 */     Arrays.fill(this.successor, -1);
/*     */   }
/*     */ 
/*     */   
/*     */   void resizeEntries(int newCapacity) {
/* 185 */     super.resizeEntries(newCapacity);
/* 186 */     int oldCapacity = this.predecessor.length;
/* 187 */     this.predecessor = Arrays.copyOf(this.predecessor, newCapacity);
/* 188 */     this.successor = Arrays.copyOf(this.successor, newCapacity);
/*     */     
/* 190 */     if (oldCapacity < newCapacity) {
/* 191 */       Arrays.fill(this.predecessor, oldCapacity, newCapacity, -1);
/* 192 */       Arrays.fill(this.successor, oldCapacity, newCapacity, -1);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public Object[] toArray() {
/* 198 */     return ObjectArrays.toArrayImpl(this);
/*     */   }
/*     */ 
/*     */   
/*     */   public <T> T[] toArray(T[] a) {
/* 203 */     return ObjectArrays.toArrayImpl(this, a);
/*     */   }
/*     */ 
/*     */   
/*     */   int firstEntryIndex() {
/* 208 */     return this.firstEntry;
/*     */   }
/*     */ 
/*     */   
/*     */   int adjustAfterRemove(int indexBeforeRemove, int indexRemoved) {
/* 213 */     return (indexBeforeRemove == size()) ? indexRemoved : indexBeforeRemove;
/*     */   }
/*     */ 
/*     */   
/*     */   int getSuccessor(int entryIndex) {
/* 218 */     return this.successor[entryIndex];
/*     */   }
/*     */ 
/*     */   
/*     */   public Spliterator<E> spliterator() {
/* 223 */     return Spliterators.spliterator(this, 17);
/*     */   }
/*     */ 
/*     */   
/*     */   public void forEach(Consumer<? super E> action) {
/* 228 */     Preconditions.checkNotNull(action);
/* 229 */     for (int i = this.firstEntry; i != -2; i = this.successor[i])
/* 230 */       action.accept((E)this.elements[i]); 
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\google\common\collect\CompactLinkedHashSet.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */