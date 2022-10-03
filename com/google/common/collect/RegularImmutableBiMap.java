/*     */ package com.google.common.collect;
/*     */ 
/*     */ import com.google.common.annotations.GwtCompatible;
/*     */ import com.google.common.annotations.VisibleForTesting;
/*     */ import com.google.common.base.Preconditions;
/*     */ import com.google.errorprone.annotations.CanIgnoreReturnValue;
/*     */ import com.google.errorprone.annotations.concurrent.LazyInit;
/*     */ import com.google.j2objc.annotations.RetainedWith;
/*     */ import java.io.Serializable;
/*     */ import java.util.Iterator;
/*     */ import java.util.Map;
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
/*     */ @GwtCompatible(serializable = true, emulated = true)
/*     */ class RegularImmutableBiMap<K, V>
/*     */   extends ImmutableBiMap<K, V>
/*     */ {
/*  45 */   static final RegularImmutableBiMap<Object, Object> EMPTY = new RegularImmutableBiMap(null, null, (Map.Entry<K, V>[])ImmutableMap.EMPTY_ENTRY_ARRAY, 0, 0);
/*     */   static final double MAX_LOAD_FACTOR = 1.2D;
/*     */   private final transient ImmutableMapEntry<K, V>[] keyTable;
/*     */   private final transient ImmutableMapEntry<K, V>[] valueTable;
/*     */   @VisibleForTesting
/*     */   final transient Map.Entry<K, V>[] entries;
/*     */   private final transient int mask;
/*     */   private final transient int hashCode;
/*     */   @LazyInit
/*     */   @RetainedWith
/*     */   private transient ImmutableBiMap<V, K> inverse;
/*     */   
/*     */   static <K, V> ImmutableBiMap<K, V> fromEntries(Map.Entry<K, V>... entries) {
/*  58 */     return fromEntryArray(entries.length, entries);
/*     */   }
/*     */   static <K, V> ImmutableBiMap<K, V> fromEntryArray(int n, Map.Entry<K, V>[] entryArray) {
/*     */     ImmutableMapEntry[] arrayOfImmutableMapEntry3;
/*  62 */     Preconditions.checkPositionIndex(n, entryArray.length);
/*  63 */     int tableSize = Hashing.closedTableSize(n, 1.2D);
/*  64 */     int mask = tableSize - 1;
/*  65 */     ImmutableMapEntry[] arrayOfImmutableMapEntry1 = (ImmutableMapEntry[])ImmutableMapEntry.createEntryArray(tableSize);
/*  66 */     ImmutableMapEntry[] arrayOfImmutableMapEntry2 = (ImmutableMapEntry[])ImmutableMapEntry.createEntryArray(tableSize);
/*     */     
/*  68 */     if (n == entryArray.length) {
/*  69 */       Map.Entry<K, V>[] entries = entryArray;
/*     */     } else {
/*  71 */       arrayOfImmutableMapEntry3 = ImmutableMapEntry.createEntryArray(n);
/*     */     } 
/*  73 */     int hashCode = 0;
/*     */     
/*  75 */     for (int i = 0; i < n; i++) {
/*     */       
/*  77 */       Map.Entry<K, V> entry = entryArray[i];
/*  78 */       K key = entry.getKey();
/*  79 */       V value = entry.getValue();
/*  80 */       CollectPreconditions.checkEntryNotNull(key, value);
/*  81 */       int keyHash = key.hashCode();
/*  82 */       int valueHash = value.hashCode();
/*  83 */       int keyBucket = Hashing.smear(keyHash) & mask;
/*  84 */       int valueBucket = Hashing.smear(valueHash) & mask;
/*     */       
/*  86 */       ImmutableMapEntry<K, V> nextInKeyBucket = arrayOfImmutableMapEntry1[keyBucket];
/*  87 */       int keyBucketLength = RegularImmutableMap.checkNoConflictInKeyBucket(key, entry, nextInKeyBucket);
/*  88 */       ImmutableMapEntry<K, V> nextInValueBucket = arrayOfImmutableMapEntry2[valueBucket];
/*  89 */       int valueBucketLength = checkNoConflictInValueBucket(value, entry, nextInValueBucket);
/*  90 */       if (keyBucketLength > 8 || valueBucketLength > 8)
/*     */       {
/*  92 */         return JdkBackedImmutableBiMap.create(n, entryArray);
/*     */       }
/*     */ 
/*     */       
/*  96 */       ImmutableMapEntry<K, V> newEntry = (nextInValueBucket == null && nextInKeyBucket == null) ? RegularImmutableMap.<K, V>makeImmutable(entry, key, value) : new ImmutableMapEntry.NonTerminalImmutableBiMapEntry<>(key, value, nextInKeyBucket, nextInValueBucket);
/*     */ 
/*     */       
/*  99 */       arrayOfImmutableMapEntry1[keyBucket] = newEntry;
/* 100 */       arrayOfImmutableMapEntry2[valueBucket] = newEntry;
/* 101 */       arrayOfImmutableMapEntry3[i] = newEntry;
/* 102 */       hashCode += keyHash ^ valueHash;
/*     */     } 
/* 104 */     return new RegularImmutableBiMap<>((ImmutableMapEntry<K, V>[])arrayOfImmutableMapEntry1, (ImmutableMapEntry<K, V>[])arrayOfImmutableMapEntry2, (Map.Entry<K, V>[])arrayOfImmutableMapEntry3, mask, hashCode);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private RegularImmutableBiMap(ImmutableMapEntry<K, V>[] keyTable, ImmutableMapEntry<K, V>[] valueTable, Map.Entry<K, V>[] entries, int mask, int hashCode) {
/* 113 */     this.keyTable = keyTable;
/* 114 */     this.valueTable = valueTable;
/* 115 */     this.entries = entries;
/* 116 */     this.mask = mask;
/* 117 */     this.hashCode = hashCode;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @CanIgnoreReturnValue
/*     */   private static int checkNoConflictInValueBucket(Object value, Map.Entry<?, ?> entry, ImmutableMapEntry<?, ?> valueBucketHead) {
/* 129 */     int bucketSize = 0;
/* 130 */     for (; valueBucketHead != null; valueBucketHead = valueBucketHead.getNextInValueBucket()) {
/* 131 */       checkNoConflict(!value.equals(valueBucketHead.getValue()), "value", entry, valueBucketHead);
/* 132 */       bucketSize++;
/*     */     } 
/* 134 */     return bucketSize;
/*     */   }
/*     */ 
/*     */   
/*     */   public V get(Object key) {
/* 139 */     return (this.keyTable == null) ? null : RegularImmutableMap.<V>get(key, (ImmutableMapEntry<?, V>[])this.keyTable, this.mask);
/*     */   }
/*     */ 
/*     */   
/*     */   ImmutableSet<Map.Entry<K, V>> createEntrySet() {
/* 144 */     return isEmpty() ? 
/* 145 */       ImmutableSet.<Map.Entry<K, V>>of() : new ImmutableMapEntrySet.RegularEntrySet<>(this, this.entries);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   ImmutableSet<K> createKeySet() {
/* 151 */     return new ImmutableMapKeySet<>(this);
/*     */   }
/*     */ 
/*     */   
/*     */   public void forEach(BiConsumer<? super K, ? super V> action) {
/* 156 */     Preconditions.checkNotNull(action);
/* 157 */     for (Map.Entry<K, V> entry : this.entries) {
/* 158 */       action.accept(entry.getKey(), entry.getValue());
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   boolean isHashCodeFast() {
/* 164 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 169 */     return this.hashCode;
/*     */   }
/*     */ 
/*     */   
/*     */   boolean isPartialView() {
/* 174 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public int size() {
/* 179 */     return this.entries.length;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ImmutableBiMap<V, K> inverse() {
/* 186 */     if (isEmpty()) {
/* 187 */       return ImmutableBiMap.of();
/*     */     }
/* 189 */     ImmutableBiMap<V, K> result = this.inverse;
/* 190 */     return (result == null) ? (this.inverse = new Inverse()) : result;
/*     */   }
/*     */   
/*     */   private final class Inverse extends ImmutableBiMap<V, K> {
/*     */     private Inverse() {}
/*     */     
/*     */     public int size() {
/* 197 */       return inverse().size();
/*     */     }
/*     */ 
/*     */     
/*     */     public ImmutableBiMap<K, V> inverse() {
/* 202 */       return RegularImmutableBiMap.this;
/*     */     }
/*     */ 
/*     */     
/*     */     public void forEach(BiConsumer<? super V, ? super K> action) {
/* 207 */       Preconditions.checkNotNull(action);
/* 208 */       RegularImmutableBiMap.this.forEach((k, v) -> action.accept(v, k));
/*     */     }
/*     */ 
/*     */     
/*     */     public K get(Object value) {
/* 213 */       if (value == null || RegularImmutableBiMap.this.valueTable == null) {
/* 214 */         return null;
/*     */       }
/* 216 */       int bucket = Hashing.smear(value.hashCode()) & RegularImmutableBiMap.this.mask;
/* 217 */       ImmutableMapEntry<K, V> entry = RegularImmutableBiMap.this.valueTable[bucket];
/* 218 */       for (; entry != null; 
/* 219 */         entry = entry.getNextInValueBucket()) {
/* 220 */         if (value.equals(entry.getValue())) {
/* 221 */           return entry.getKey();
/*     */         }
/*     */       } 
/* 224 */       return null;
/*     */     }
/*     */ 
/*     */     
/*     */     ImmutableSet<V> createKeySet() {
/* 229 */       return new ImmutableMapKeySet<>(this);
/*     */     }
/*     */ 
/*     */     
/*     */     ImmutableSet<Map.Entry<V, K>> createEntrySet() {
/* 234 */       return new InverseEntrySet();
/*     */     }
/*     */     
/*     */     final class InverseEntrySet
/*     */       extends ImmutableMapEntrySet<V, K>
/*     */     {
/*     */       ImmutableMap<V, K> map() {
/* 241 */         return RegularImmutableBiMap.Inverse.this;
/*     */       }
/*     */ 
/*     */       
/*     */       boolean isHashCodeFast() {
/* 246 */         return true;
/*     */       }
/*     */ 
/*     */       
/*     */       public int hashCode() {
/* 251 */         return RegularImmutableBiMap.this.hashCode;
/*     */       }
/*     */ 
/*     */       
/*     */       public UnmodifiableIterator<Map.Entry<V, K>> iterator() {
/* 256 */         return asList().iterator();
/*     */       }
/*     */ 
/*     */       
/*     */       public void forEach(Consumer<? super Map.Entry<V, K>> action) {
/* 261 */         asList().forEach(action);
/*     */       }
/*     */ 
/*     */       
/*     */       ImmutableList<Map.Entry<V, K>> createAsList() {
/* 266 */         return new ImmutableAsList<Map.Entry<V, K>>()
/*     */           {
/*     */             public Map.Entry<V, K> get(int index) {
/* 269 */               Map.Entry<K, V> entry = RegularImmutableBiMap.this.entries[index];
/* 270 */               return Maps.immutableEntry(entry.getValue(), entry.getKey());
/*     */             }
/*     */ 
/*     */             
/*     */             ImmutableCollection<Map.Entry<V, K>> delegateCollection() {
/* 275 */               return RegularImmutableBiMap.Inverse.InverseEntrySet.this;
/*     */             }
/*     */           };
/*     */       }
/*     */     }
/*     */ 
/*     */     
/*     */     boolean isPartialView() {
/* 283 */       return false;
/*     */     }
/*     */ 
/*     */     
/*     */     Object writeReplace() {
/* 288 */       return new RegularImmutableBiMap.InverseSerializedForm<>(RegularImmutableBiMap.this);
/*     */     } }
/*     */   
/*     */   private static class InverseSerializedForm<K, V> implements Serializable {
/*     */     private final ImmutableBiMap<K, V> forward;
/*     */     private static final long serialVersionUID = 1L;
/*     */     
/*     */     InverseSerializedForm(ImmutableBiMap<K, V> forward) {
/* 296 */       this.forward = forward;
/*     */     }
/*     */     
/*     */     Object readResolve() {
/* 300 */       return this.forward.inverse();
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\google\common\collect\RegularImmutableBiMap.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */