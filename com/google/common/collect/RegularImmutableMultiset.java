/*     */ package com.google.common.collect;
/*     */ 
/*     */ import com.google.common.annotations.GwtCompatible;
/*     */ import com.google.common.annotations.VisibleForTesting;
/*     */ import com.google.common.base.Objects;
/*     */ import com.google.common.base.Preconditions;
/*     */ import com.google.common.primitives.Ints;
/*     */ import com.google.errorprone.annotations.concurrent.LazyInit;
/*     */ import java.util.Arrays;
/*     */ import java.util.Collection;
/*     */ import java.util.Set;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @GwtCompatible(emulated = true, serializable = true)
/*     */ class RegularImmutableMultiset<E>
/*     */   extends ImmutableMultiset<E>
/*     */ {
/*  38 */   static final ImmutableMultiset<Object> EMPTY = create(ImmutableList.of()); @VisibleForTesting
/*     */   static final double MAX_LOAD_FACTOR = 1.0D; @VisibleForTesting
/*     */   static final double HASH_FLOODING_FPP = 0.001D; @VisibleForTesting
/*  41 */   static final int MAX_HASH_BUCKET_LENGTH = 9; private final transient Multisets.ImmutableEntry<E>[] entries; static <E> ImmutableMultiset<E> create(Collection<? extends Multiset.Entry<? extends E>> entries) { int distinct = entries.size();
/*     */     
/*  43 */     Multisets.ImmutableEntry[] arrayOfImmutableEntry1 = new Multisets.ImmutableEntry[distinct];
/*  44 */     if (distinct == 0) {
/*  45 */       return new RegularImmutableMultiset<>((Multisets.ImmutableEntry<E>[])arrayOfImmutableEntry1, null, 0, 0, ImmutableSet.of());
/*     */     }
/*  47 */     int tableSize = Hashing.closedTableSize(distinct, 1.0D);
/*  48 */     int mask = tableSize - 1;
/*     */     
/*  50 */     Multisets.ImmutableEntry[] arrayOfImmutableEntry2 = new Multisets.ImmutableEntry[tableSize];
/*     */     
/*  52 */     int index = 0;
/*  53 */     int hashCode = 0;
/*  54 */     long size = 0L;
/*  55 */     for (Multiset.Entry<? extends E> entry : entries) {
/*  56 */       Multisets.ImmutableEntry<E> newEntry; E element = (E)Preconditions.checkNotNull(entry.getElement());
/*  57 */       int count = entry.getCount();
/*  58 */       int hash = element.hashCode();
/*  59 */       int bucket = Hashing.smear(hash) & mask;
/*  60 */       Multisets.ImmutableEntry<E> bucketHead = arrayOfImmutableEntry2[bucket];
/*     */       
/*  62 */       if (bucketHead == null) {
/*  63 */         boolean canReuseEntry = (entry instanceof Multisets.ImmutableEntry && !(entry instanceof NonTerminalEntry));
/*     */         
/*  65 */         newEntry = canReuseEntry ? (Multisets.ImmutableEntry)entry : new Multisets.ImmutableEntry<>(element, count);
/*     */       
/*     */       }
/*     */       else {
/*     */         
/*  70 */         newEntry = new NonTerminalEntry<>(element, count, bucketHead);
/*     */       } 
/*  72 */       hashCode += hash ^ count;
/*  73 */       arrayOfImmutableEntry1[index++] = newEntry;
/*  74 */       arrayOfImmutableEntry2[bucket] = newEntry;
/*  75 */       size += count;
/*     */     } 
/*     */     
/*  78 */     return hashFloodingDetected((Multisets.ImmutableEntry<?>[])arrayOfImmutableEntry2) ? 
/*  79 */       JdkBackedImmutableMultiset.<E>create(ImmutableList.asImmutableList((Object[])arrayOfImmutableEntry1)) : new RegularImmutableMultiset<>((Multisets.ImmutableEntry<E>[])arrayOfImmutableEntry1, (Multisets.ImmutableEntry<E>[])arrayOfImmutableEntry2, 
/*     */         
/*  81 */         Ints.saturatedCast(size), hashCode, null); }
/*     */    private final transient Multisets.ImmutableEntry<E>[] hashTable; private final transient int size; private final transient int hashCode; @LazyInit
/*     */   private transient ImmutableSet<E> elementSet;
/*     */   private static boolean hashFloodingDetected(Multisets.ImmutableEntry<?>[] hashTable) {
/*  85 */     for (int i = 0; i < hashTable.length; i++) {
/*  86 */       int bucketLength = 0;
/*  87 */       Multisets.ImmutableEntry<?> entry = hashTable[i];
/*  88 */       for (; entry != null; 
/*  89 */         entry = entry.nextInBucket()) {
/*  90 */         bucketLength++;
/*  91 */         if (bucketLength > 9) {
/*  92 */           return true;
/*     */         }
/*     */       } 
/*     */     } 
/*  96 */     return false;
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
/*     */   private RegularImmutableMultiset(Multisets.ImmutableEntry<E>[] entries, Multisets.ImmutableEntry<E>[] hashTable, int size, int hashCode, ImmutableSet<E> elementSet) {
/* 131 */     this.entries = entries;
/* 132 */     this.hashTable = hashTable;
/* 133 */     this.size = size;
/* 134 */     this.hashCode = hashCode;
/* 135 */     this.elementSet = elementSet;
/*     */   }
/*     */   
/*     */   private static final class NonTerminalEntry<E> extends Multisets.ImmutableEntry<E> {
/*     */     private final Multisets.ImmutableEntry<E> nextInBucket;
/*     */     
/*     */     NonTerminalEntry(E element, int count, Multisets.ImmutableEntry<E> nextInBucket) {
/* 142 */       super(element, count);
/* 143 */       this.nextInBucket = nextInBucket;
/*     */     }
/*     */ 
/*     */     
/*     */     public Multisets.ImmutableEntry<E> nextInBucket() {
/* 148 */       return this.nextInBucket;
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   boolean isPartialView() {
/* 154 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public int count(Object element) {
/* 159 */     Multisets.ImmutableEntry<E>[] hashTable = this.hashTable;
/* 160 */     if (element == null || hashTable == null) {
/* 161 */       return 0;
/*     */     }
/* 163 */     int hash = Hashing.smearedHash(element);
/* 164 */     int mask = hashTable.length - 1;
/* 165 */     Multisets.ImmutableEntry<E> entry = hashTable[hash & mask];
/* 166 */     for (; entry != null; 
/* 167 */       entry = entry.nextInBucket()) {
/* 168 */       if (Objects.equal(element, entry.getElement())) {
/* 169 */         return entry.getCount();
/*     */       }
/*     */     } 
/* 172 */     return 0;
/*     */   }
/*     */ 
/*     */   
/*     */   public int size() {
/* 177 */     return this.size;
/*     */   }
/*     */ 
/*     */   
/*     */   public ImmutableSet<E> elementSet() {
/* 182 */     ImmutableSet<E> result = this.elementSet;
/* 183 */     return (result == null) ? (this.elementSet = new ImmutableMultiset.ElementSet<>(Arrays.asList((Multiset.Entry[])this.entries), this)) : result;
/*     */   }
/*     */ 
/*     */   
/*     */   Multiset.Entry<E> getEntry(int index) {
/* 188 */     return this.entries[index];
/*     */   }
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 193 */     return this.hashCode;
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\google\common\collect\RegularImmutableMultiset.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */