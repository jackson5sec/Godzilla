/*     */ package com.google.common.collect;
/*     */ 
/*     */ import com.google.common.annotations.GwtCompatible;
/*     */ import com.google.common.annotations.GwtIncompatible;
/*     */ import com.google.common.annotations.VisibleForTesting;
/*     */ import com.google.common.base.Preconditions;
/*     */ import com.google.errorprone.annotations.CanIgnoreReturnValue;
/*     */ import java.io.Serializable;
/*     */ import java.util.Map;
/*     */ import java.util.function.BiConsumer;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
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
/*     */ final class RegularImmutableMap<K, V>
/*     */   extends ImmutableMap<K, V>
/*     */ {
/*  43 */   static final ImmutableMap<Object, Object> EMPTY = new RegularImmutableMap((Map.Entry<K, V>[])ImmutableMap.EMPTY_ENTRY_ARRAY, null, 0);
/*     */ 
/*     */   
/*     */   @VisibleForTesting
/*     */   static final double MAX_LOAD_FACTOR = 1.2D;
/*     */ 
/*     */   
/*     */   @VisibleForTesting
/*     */   static final double HASH_FLOODING_FPP = 0.001D;
/*     */ 
/*     */   
/*     */   @VisibleForTesting
/*     */   static final int MAX_HASH_BUCKET_LENGTH = 8;
/*     */ 
/*     */   
/*     */   @VisibleForTesting
/*     */   final transient Map.Entry<K, V>[] entries;
/*     */ 
/*     */   
/*     */   private final transient ImmutableMapEntry<K, V>[] table;
/*     */ 
/*     */   
/*     */   private final transient int mask;
/*     */ 
/*     */   
/*     */   private static final long serialVersionUID = 0L;
/*     */ 
/*     */ 
/*     */   
/*     */   static <K, V> ImmutableMap<K, V> fromEntries(Map.Entry<K, V>... entries) {
/*  73 */     return fromEntryArray(entries.length, entries);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static <K, V> ImmutableMap<K, V> fromEntryArray(int n, Map.Entry<K, V>[] entryArray) {
/*     */     ImmutableMapEntry[] arrayOfImmutableMapEntry1;
/*  82 */     Preconditions.checkPositionIndex(n, entryArray.length);
/*  83 */     if (n == 0) {
/*  84 */       return (RegularImmutableMap)EMPTY;
/*     */     }
/*     */     
/*  87 */     if (n == entryArray.length) {
/*  88 */       Map.Entry<K, V>[] entries = entryArray;
/*     */     } else {
/*  90 */       arrayOfImmutableMapEntry1 = ImmutableMapEntry.createEntryArray(n);
/*     */     } 
/*  92 */     int tableSize = Hashing.closedTableSize(n, 1.2D);
/*  93 */     ImmutableMapEntry[] arrayOfImmutableMapEntry2 = (ImmutableMapEntry[])ImmutableMapEntry.createEntryArray(tableSize);
/*  94 */     int mask = tableSize - 1;
/*  95 */     for (int entryIndex = 0; entryIndex < n; entryIndex++) {
/*  96 */       Map.Entry<K, V> entry = entryArray[entryIndex];
/*  97 */       K key = entry.getKey();
/*  98 */       V value = entry.getValue();
/*  99 */       CollectPreconditions.checkEntryNotNull(key, value);
/* 100 */       int tableIndex = Hashing.smear(key.hashCode()) & mask;
/* 101 */       ImmutableMapEntry<K, V> existing = arrayOfImmutableMapEntry2[tableIndex];
/*     */ 
/*     */ 
/*     */       
/* 105 */       ImmutableMapEntry<K, V> newEntry = (existing == null) ? makeImmutable(entry, key, value) : new ImmutableMapEntry.NonTerminalImmutableMapEntry<>(key, value, existing);
/*     */       
/* 107 */       arrayOfImmutableMapEntry2[tableIndex] = newEntry;
/* 108 */       arrayOfImmutableMapEntry1[entryIndex] = newEntry;
/* 109 */       int bucketSize = checkNoConflictInKeyBucket(key, newEntry, existing);
/* 110 */       if (bucketSize > 8)
/*     */       {
/*     */         
/* 113 */         return JdkBackedImmutableMap.create(n, entryArray);
/*     */       }
/*     */     } 
/* 116 */     return new RegularImmutableMap<>((Map.Entry<K, V>[])arrayOfImmutableMapEntry1, (ImmutableMapEntry<K, V>[])arrayOfImmutableMapEntry2, mask);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   static <K, V> ImmutableMapEntry<K, V> makeImmutable(Map.Entry<K, V> entry, K key, V value) {
/* 122 */     boolean reusable = (entry instanceof ImmutableMapEntry && ((ImmutableMapEntry)entry).isReusable());
/* 123 */     return reusable ? (ImmutableMapEntry<K, V>)entry : new ImmutableMapEntry<>(key, value);
/*     */   }
/*     */ 
/*     */   
/*     */   static <K, V> ImmutableMapEntry<K, V> makeImmutable(Map.Entry<K, V> entry) {
/* 128 */     return makeImmutable(entry, entry.getKey(), entry.getValue());
/*     */   }
/*     */   
/*     */   private RegularImmutableMap(Map.Entry<K, V>[] entries, ImmutableMapEntry<K, V>[] table, int mask) {
/* 132 */     this.entries = entries;
/* 133 */     this.table = table;
/* 134 */     this.mask = mask;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @CanIgnoreReturnValue
/*     */   static int checkNoConflictInKeyBucket(Object key, Map.Entry<?, ?> entry, ImmutableMapEntry<?, ?> keyBucketHead) {
/* 144 */     int bucketSize = 0;
/* 145 */     for (; keyBucketHead != null; keyBucketHead = keyBucketHead.getNextInKeyBucket()) {
/* 146 */       checkNoConflict(!key.equals(keyBucketHead.getKey()), "key", entry, keyBucketHead);
/* 147 */       bucketSize++;
/*     */     } 
/* 149 */     return bucketSize;
/*     */   }
/*     */ 
/*     */   
/*     */   public V get(Object key) {
/* 154 */     return get(key, (ImmutableMapEntry<?, V>[])this.table, this.mask);
/*     */   }
/*     */ 
/*     */   
/*     */   static <V> V get(Object key, ImmutableMapEntry<?, V>[] keyTable, int mask) {
/* 159 */     if (key == null || keyTable == null) {
/* 160 */       return null;
/*     */     }
/* 162 */     int index = Hashing.smear(key.hashCode()) & mask;
/* 163 */     ImmutableMapEntry<?, V> entry = keyTable[index];
/* 164 */     for (; entry != null; 
/* 165 */       entry = entry.getNextInKeyBucket()) {
/* 166 */       Object candidateKey = entry.getKey();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 174 */       if (key.equals(candidateKey)) {
/* 175 */         return entry.getValue();
/*     */       }
/*     */     } 
/* 178 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   public void forEach(BiConsumer<? super K, ? super V> action) {
/* 183 */     Preconditions.checkNotNull(action);
/* 184 */     for (Map.Entry<K, V> entry : this.entries) {
/* 185 */       action.accept(entry.getKey(), entry.getValue());
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public int size() {
/* 191 */     return this.entries.length;
/*     */   }
/*     */ 
/*     */   
/*     */   boolean isPartialView() {
/* 196 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   ImmutableSet<Map.Entry<K, V>> createEntrySet() {
/* 201 */     return new ImmutableMapEntrySet.RegularEntrySet<>(this, this.entries);
/*     */   }
/*     */ 
/*     */   
/*     */   ImmutableSet<K> createKeySet() {
/* 206 */     return new KeySet<>(this);
/*     */   }
/*     */   
/*     */   @GwtCompatible(emulated = true)
/*     */   private static final class KeySet<K, V> extends IndexedImmutableSet<K> {
/*     */     private final RegularImmutableMap<K, V> map;
/*     */     
/*     */     KeySet(RegularImmutableMap<K, V> map) {
/* 214 */       this.map = map;
/*     */     }
/*     */ 
/*     */     
/*     */     K get(int index) {
/* 219 */       return this.map.entries[index].getKey();
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean contains(Object object) {
/* 224 */       return this.map.containsKey(object);
/*     */     }
/*     */ 
/*     */     
/*     */     boolean isPartialView() {
/* 229 */       return true;
/*     */     }
/*     */ 
/*     */     
/*     */     public int size() {
/* 234 */       return this.map.size();
/*     */     }
/*     */ 
/*     */     
/*     */     @GwtIncompatible
/*     */     Object writeReplace() {
/* 240 */       return new SerializedForm<>(this.map);
/*     */     }
/*     */     
/*     */     @GwtIncompatible
/*     */     private static class SerializedForm<K> implements Serializable {
/*     */       final ImmutableMap<K, ?> map;
/*     */       
/*     */       SerializedForm(ImmutableMap<K, ?> map) {
/* 248 */         this.map = map;
/*     */       }
/*     */       private static final long serialVersionUID = 0L;
/*     */       Object readResolve() {
/* 252 */         return this.map.keySet();
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   ImmutableCollection<V> createValues() {
/* 261 */     return new Values<>(this);
/*     */   }
/*     */   
/*     */   @GwtCompatible(emulated = true)
/*     */   private static final class Values<K, V> extends ImmutableList<V> {
/*     */     final RegularImmutableMap<K, V> map;
/*     */     
/*     */     Values(RegularImmutableMap<K, V> map) {
/* 269 */       this.map = map;
/*     */     }
/*     */ 
/*     */     
/*     */     public V get(int index) {
/* 274 */       return this.map.entries[index].getValue();
/*     */     }
/*     */ 
/*     */     
/*     */     public int size() {
/* 279 */       return this.map.size();
/*     */     }
/*     */ 
/*     */     
/*     */     boolean isPartialView() {
/* 284 */       return true;
/*     */     }
/*     */ 
/*     */     
/*     */     @GwtIncompatible
/*     */     Object writeReplace() {
/* 290 */       return new SerializedForm<>(this.map);
/*     */     }
/*     */     
/*     */     @GwtIncompatible
/*     */     private static class SerializedForm<V> implements Serializable { final ImmutableMap<?, V> map;
/*     */       private static final long serialVersionUID = 0L;
/*     */       
/*     */       SerializedForm(ImmutableMap<?, V> map) {
/* 298 */         this.map = map;
/*     */       }
/*     */       
/*     */       Object readResolve() {
/* 302 */         return this.map.values();
/*     */       } }
/*     */   
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\google\common\collect\RegularImmutableMap.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */