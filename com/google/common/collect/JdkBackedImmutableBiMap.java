/*     */ package com.google.common.collect;
/*     */ 
/*     */ import com.google.common.annotations.GwtCompatible;
/*     */ import com.google.common.annotations.VisibleForTesting;
/*     */ import com.google.errorprone.annotations.concurrent.LazyInit;
/*     */ import com.google.j2objc.annotations.RetainedWith;
/*     */ import java.util.Map;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
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
/*     */ final class JdkBackedImmutableBiMap<K, V>
/*     */   extends ImmutableBiMap<K, V>
/*     */ {
/*     */   private final transient ImmutableList<Map.Entry<K, V>> entries;
/*     */   private final Map<K, V> forwardDelegate;
/*     */   private final Map<V, K> backwardDelegate;
/*     */   @LazyInit
/*     */   @RetainedWith
/*     */   private transient JdkBackedImmutableBiMap<V, K> inverse;
/*     */   
/*     */   @VisibleForTesting
/*     */   static <K, V> ImmutableBiMap<K, V> create(int n, Map.Entry<K, V>[] entryArray) {
/*  34 */     Map<K, V> forwardDelegate = Maps.newHashMapWithExpectedSize(n);
/*  35 */     Map<V, K> backwardDelegate = Maps.newHashMapWithExpectedSize(n);
/*  36 */     for (int i = 0; i < n; i++) {
/*  37 */       Map.Entry<K, V> e = RegularImmutableMap.makeImmutable(entryArray[i]);
/*  38 */       entryArray[i] = e;
/*  39 */       V oldValue = forwardDelegate.putIfAbsent(e.getKey(), e.getValue());
/*  40 */       if (oldValue != null) {
/*  41 */         throw conflictException("key", (new StringBuilder()).append(e.getKey()).append("=").append(oldValue).toString(), entryArray[i]);
/*     */       }
/*  43 */       K oldKey = backwardDelegate.putIfAbsent(e.getValue(), e.getKey());
/*  44 */       if (oldKey != null) {
/*  45 */         throw conflictException("value", (new StringBuilder()).append(oldKey).append("=").append(e.getValue()).toString(), entryArray[i]);
/*     */       }
/*     */     } 
/*  48 */     ImmutableList<Map.Entry<K, V>> entryList = ImmutableList.asImmutableList((Object[])entryArray, n);
/*  49 */     return new JdkBackedImmutableBiMap<>(entryList, forwardDelegate, backwardDelegate);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private JdkBackedImmutableBiMap(ImmutableList<Map.Entry<K, V>> entries, Map<K, V> forwardDelegate, Map<V, K> backwardDelegate) {
/*  58 */     this.entries = entries;
/*  59 */     this.forwardDelegate = forwardDelegate;
/*  60 */     this.backwardDelegate = backwardDelegate;
/*     */   }
/*     */ 
/*     */   
/*     */   public int size() {
/*  65 */     return this.entries.size();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ImmutableBiMap<V, K> inverse() {
/*  72 */     JdkBackedImmutableBiMap<V, K> result = this.inverse;
/*  73 */     if (result == null) {
/*  74 */       this.inverse = result = new JdkBackedImmutableBiMap(new InverseEntries(), this.backwardDelegate, this.forwardDelegate);
/*     */ 
/*     */ 
/*     */       
/*  78 */       result.inverse = this;
/*     */     } 
/*  80 */     return result;
/*     */   }
/*     */   
/*     */   private final class InverseEntries extends ImmutableList<Map.Entry<V, K>> {
/*     */     private InverseEntries() {}
/*     */     
/*     */     public Map.Entry<V, K> get(int index) {
/*  87 */       Map.Entry<K, V> entry = JdkBackedImmutableBiMap.this.entries.get(index);
/*  88 */       return Maps.immutableEntry(entry.getValue(), entry.getKey());
/*     */     }
/*     */ 
/*     */     
/*     */     boolean isPartialView() {
/*  93 */       return false;
/*     */     }
/*     */ 
/*     */     
/*     */     public int size() {
/*  98 */       return JdkBackedImmutableBiMap.this.entries.size();
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public V get(Object key) {
/* 104 */     return this.forwardDelegate.get(key);
/*     */   }
/*     */ 
/*     */   
/*     */   ImmutableSet<Map.Entry<K, V>> createEntrySet() {
/* 109 */     return new ImmutableMapEntrySet.RegularEntrySet<>(this, this.entries);
/*     */   }
/*     */ 
/*     */   
/*     */   ImmutableSet<K> createKeySet() {
/* 114 */     return new ImmutableMapKeySet<>(this);
/*     */   }
/*     */ 
/*     */   
/*     */   boolean isPartialView() {
/* 119 */     return false;
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\google\common\collect\JdkBackedImmutableBiMap.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */