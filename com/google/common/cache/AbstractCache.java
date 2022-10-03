/*     */ package com.google.common.cache;
/*     */ 
/*     */ import com.google.common.annotations.GwtCompatible;
/*     */ import com.google.common.collect.ImmutableMap;
/*     */ import com.google.common.collect.Maps;
/*     */ import java.util.Map;
/*     */ import java.util.concurrent.Callable;
/*     */ import java.util.concurrent.ConcurrentMap;
/*     */ import java.util.concurrent.ExecutionException;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @GwtCompatible
/*     */ public abstract class AbstractCache<K, V>
/*     */   implements Cache<K, V>
/*     */ {
/*     */   public V get(K key, Callable<? extends V> valueLoader) throws ExecutionException {
/*  49 */     throw new UnsupportedOperationException();
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
/*     */   public ImmutableMap<K, V> getAllPresent(Iterable<?> keys) {
/*  63 */     Map<K, V> result = Maps.newLinkedHashMap();
/*  64 */     for (Object key : keys) {
/*  65 */       if (!result.containsKey(key)) {
/*     */         
/*  67 */         K castKey = (K)key;
/*  68 */         V value = getIfPresent(key);
/*  69 */         if (value != null) {
/*  70 */           result.put(castKey, value);
/*     */         }
/*     */       } 
/*     */     } 
/*  74 */     return ImmutableMap.copyOf(result);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void put(K key, V value) {
/*  80 */     throw new UnsupportedOperationException();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void putAll(Map<? extends K, ? extends V> m) {
/*  86 */     for (Map.Entry<? extends K, ? extends V> entry : m.entrySet()) {
/*  87 */       put(entry.getKey(), entry.getValue());
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public void cleanUp() {}
/*     */ 
/*     */   
/*     */   public long size() {
/*  96 */     throw new UnsupportedOperationException();
/*     */   }
/*     */ 
/*     */   
/*     */   public void invalidate(Object key) {
/* 101 */     throw new UnsupportedOperationException();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void invalidateAll(Iterable<?> keys) {
/* 107 */     for (Object key : keys) {
/* 108 */       invalidate(key);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public void invalidateAll() {
/* 114 */     throw new UnsupportedOperationException();
/*     */   }
/*     */ 
/*     */   
/*     */   public CacheStats stats() {
/* 119 */     throw new UnsupportedOperationException();
/*     */   }
/*     */ 
/*     */   
/*     */   public ConcurrentMap<K, V> asMap() {
/* 124 */     throw new UnsupportedOperationException();
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static final class SimpleStatsCounter
/*     */     implements StatsCounter
/*     */   {
/* 197 */     private final LongAddable hitCount = LongAddables.create();
/* 198 */     private final LongAddable missCount = LongAddables.create();
/* 199 */     private final LongAddable loadSuccessCount = LongAddables.create();
/* 200 */     private final LongAddable loadExceptionCount = LongAddables.create();
/* 201 */     private final LongAddable totalLoadTime = LongAddables.create();
/* 202 */     private final LongAddable evictionCount = LongAddables.create();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public void recordHits(int count) {
/* 210 */       this.hitCount.add(count);
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public void recordMisses(int count) {
/* 216 */       this.missCount.add(count);
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public void recordLoadSuccess(long loadTime) {
/* 222 */       this.loadSuccessCount.increment();
/* 223 */       this.totalLoadTime.add(loadTime);
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public void recordLoadException(long loadTime) {
/* 229 */       this.loadExceptionCount.increment();
/* 230 */       this.totalLoadTime.add(loadTime);
/*     */     }
/*     */ 
/*     */     
/*     */     public void recordEviction() {
/* 235 */       this.evictionCount.increment();
/*     */     }
/*     */ 
/*     */     
/*     */     public CacheStats snapshot() {
/* 240 */       return new CacheStats(this.hitCount
/* 241 */           .sum(), this.missCount
/* 242 */           .sum(), this.loadSuccessCount
/* 243 */           .sum(), this.loadExceptionCount
/* 244 */           .sum(), this.totalLoadTime
/* 245 */           .sum(), this.evictionCount
/* 246 */           .sum());
/*     */     }
/*     */ 
/*     */     
/*     */     public void incrementBy(AbstractCache.StatsCounter other) {
/* 251 */       CacheStats otherStats = other.snapshot();
/* 252 */       this.hitCount.add(otherStats.hitCount());
/* 253 */       this.missCount.add(otherStats.missCount());
/* 254 */       this.loadSuccessCount.add(otherStats.loadSuccessCount());
/* 255 */       this.loadExceptionCount.add(otherStats.loadExceptionCount());
/* 256 */       this.totalLoadTime.add(otherStats.totalLoadTime());
/* 257 */       this.evictionCount.add(otherStats.evictionCount());
/*     */     }
/*     */   }
/*     */   
/*     */   public static interface StatsCounter {
/*     */     void recordHits(int param1Int);
/*     */     
/*     */     void recordMisses(int param1Int);
/*     */     
/*     */     void recordLoadSuccess(long param1Long);
/*     */     
/*     */     void recordLoadException(long param1Long);
/*     */     
/*     */     void recordEviction();
/*     */     
/*     */     CacheStats snapshot();
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\google\common\cache\AbstractCache.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */