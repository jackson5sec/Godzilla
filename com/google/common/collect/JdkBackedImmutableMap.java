/*    */ package com.google.common.collect;
/*    */ 
/*    */ import com.google.common.annotations.GwtCompatible;
/*    */ import com.google.common.base.Preconditions;
/*    */ import java.util.Map;
/*    */ import java.util.function.BiConsumer;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ @GwtCompatible(emulated = true)
/*    */ final class JdkBackedImmutableMap<K, V>
/*    */   extends ImmutableMap<K, V>
/*    */ {
/*    */   private final transient Map<K, V> delegateMap;
/*    */   private final transient ImmutableList<Map.Entry<K, V>> entries;
/*    */   
/*    */   static <K, V> ImmutableMap<K, V> create(int n, Map.Entry<K, V>[] entryArray) {
/* 39 */     Map<K, V> delegateMap = Maps.newHashMapWithExpectedSize(n);
/* 40 */     for (int i = 0; i < n; i++) {
/* 41 */       entryArray[i] = RegularImmutableMap.makeImmutable(entryArray[i]);
/* 42 */       V oldValue = delegateMap.putIfAbsent(entryArray[i].getKey(), entryArray[i].getValue());
/* 43 */       if (oldValue != null) {
/* 44 */         throw conflictException("key", entryArray[i], (new StringBuilder()).append(entryArray[i].getKey()).append("=").append(oldValue).toString());
/*    */       }
/*    */     } 
/* 47 */     return new JdkBackedImmutableMap<>(delegateMap, ImmutableList.asImmutableList((Object[])entryArray, n));
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   JdkBackedImmutableMap(Map<K, V> delegateMap, ImmutableList<Map.Entry<K, V>> entries) {
/* 54 */     this.delegateMap = delegateMap;
/* 55 */     this.entries = entries;
/*    */   }
/*    */ 
/*    */   
/*    */   public int size() {
/* 60 */     return this.entries.size();
/*    */   }
/*    */ 
/*    */   
/*    */   public V get(Object key) {
/* 65 */     return this.delegateMap.get(key);
/*    */   }
/*    */ 
/*    */   
/*    */   ImmutableSet<Map.Entry<K, V>> createEntrySet() {
/* 70 */     return new ImmutableMapEntrySet.RegularEntrySet<>(this, this.entries);
/*    */   }
/*    */ 
/*    */   
/*    */   public void forEach(BiConsumer<? super K, ? super V> action) {
/* 75 */     Preconditions.checkNotNull(action);
/* 76 */     this.entries.forEach(e -> action.accept(e.getKey(), e.getValue()));
/*    */   }
/*    */ 
/*    */   
/*    */   ImmutableSet<K> createKeySet() {
/* 81 */     return new ImmutableMapKeySet<>(this);
/*    */   }
/*    */ 
/*    */   
/*    */   ImmutableCollection<V> createValues() {
/* 86 */     return new ImmutableMapValues<>(this);
/*    */   }
/*    */ 
/*    */   
/*    */   boolean isPartialView() {
/* 91 */     return false;
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\google\common\collect\JdkBackedImmutableMap.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */