/*     */ package org.springframework.util;
/*     */ 
/*     */ import java.io.Serializable;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collection;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import org.springframework.lang.Nullable;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class MultiValueMapAdapter<K, V>
/*     */   implements MultiValueMap<K, V>, Serializable
/*     */ {
/*     */   private final Map<K, List<V>> targetMap;
/*     */   
/*     */   public MultiValueMapAdapter(Map<K, List<V>> targetMap) {
/*  50 */     Assert.notNull(targetMap, "'targetMap' must not be null");
/*  51 */     this.targetMap = targetMap;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public V getFirst(K key) {
/*  60 */     List<V> values = this.targetMap.get(key);
/*  61 */     return (values != null && !values.isEmpty()) ? values.get(0) : null;
/*     */   }
/*     */ 
/*     */   
/*     */   public void add(K key, @Nullable V value) {
/*  66 */     List<V> values = this.targetMap.computeIfAbsent(key, k -> new ArrayList(1));
/*  67 */     values.add(value);
/*     */   }
/*     */ 
/*     */   
/*     */   public void addAll(K key, List<? extends V> values) {
/*  72 */     List<V> currentValues = this.targetMap.computeIfAbsent(key, k -> new ArrayList(1));
/*  73 */     currentValues.addAll(values);
/*     */   }
/*     */ 
/*     */   
/*     */   public void addAll(MultiValueMap<K, V> values) {
/*  78 */     for (Map.Entry<K, List<V>> entry : values.entrySet()) {
/*  79 */       addAll(entry.getKey(), entry.getValue());
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public void set(K key, @Nullable V value) {
/*  85 */     List<V> values = new ArrayList<>(1);
/*  86 */     values.add(value);
/*  87 */     this.targetMap.put(key, values);
/*     */   }
/*     */ 
/*     */   
/*     */   public void setAll(Map<K, V> values) {
/*  92 */     values.forEach(this::set);
/*     */   }
/*     */ 
/*     */   
/*     */   public Map<K, V> toSingleValueMap() {
/*  97 */     Map<K, V> singleValueMap = CollectionUtils.newLinkedHashMap(this.targetMap.size());
/*  98 */     this.targetMap.forEach((key, values) -> {
/*     */           if (values != null && !values.isEmpty()) {
/*     */             singleValueMap.put(key, values.get(0));
/*     */           }
/*     */         });
/* 103 */     return singleValueMap;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int size() {
/* 111 */     return this.targetMap.size();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isEmpty() {
/* 116 */     return this.targetMap.isEmpty();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean containsKey(Object key) {
/* 121 */     return this.targetMap.containsKey(key);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean containsValue(Object value) {
/* 126 */     return this.targetMap.containsValue(value);
/*     */   }
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public List<V> get(Object key) {
/* 132 */     return this.targetMap.get(key);
/*     */   }
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public List<V> put(K key, List<V> value) {
/* 138 */     return this.targetMap.put(key, value);
/*     */   }
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public List<V> remove(Object key) {
/* 144 */     return this.targetMap.remove(key);
/*     */   }
/*     */ 
/*     */   
/*     */   public void putAll(Map<? extends K, ? extends List<V>> map) {
/* 149 */     this.targetMap.putAll(map);
/*     */   }
/*     */ 
/*     */   
/*     */   public void clear() {
/* 154 */     this.targetMap.clear();
/*     */   }
/*     */ 
/*     */   
/*     */   public Set<K> keySet() {
/* 159 */     return this.targetMap.keySet();
/*     */   }
/*     */ 
/*     */   
/*     */   public Collection<List<V>> values() {
/* 164 */     return this.targetMap.values();
/*     */   }
/*     */ 
/*     */   
/*     */   public Set<Map.Entry<K, List<V>>> entrySet() {
/* 169 */     return this.targetMap.entrySet();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean equals(@Nullable Object other) {
/* 174 */     return (this == other || this.targetMap.equals(other));
/*     */   }
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 179 */     return this.targetMap.hashCode();
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 184 */     return this.targetMap.toString();
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\springframewor\\util\MultiValueMapAdapter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */