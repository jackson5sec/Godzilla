/*     */ package com.google.common.cache;
/*     */ 
/*     */ import com.google.common.annotations.GwtIncompatible;
/*     */ import com.google.common.base.Preconditions;
/*     */ import com.google.common.collect.ForwardingObject;
/*     */ import com.google.common.collect.ImmutableMap;
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
/*     */ @GwtIncompatible
/*     */ public abstract class ForwardingCache<K, V>
/*     */   extends ForwardingObject
/*     */   implements Cache<K, V>
/*     */ {
/*     */   public V getIfPresent(Object key) {
/*  47 */     return delegate().getIfPresent(key);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public V get(K key, Callable<? extends V> valueLoader) throws ExecutionException {
/*  53 */     return delegate().get(key, valueLoader);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public ImmutableMap<K, V> getAllPresent(Iterable<?> keys) {
/*  59 */     return delegate().getAllPresent(keys);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void put(K key, V value) {
/*  65 */     delegate().put(key, value);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void putAll(Map<? extends K, ? extends V> m) {
/*  71 */     delegate().putAll(m);
/*     */   }
/*     */ 
/*     */   
/*     */   public void invalidate(Object key) {
/*  76 */     delegate().invalidate(key);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void invalidateAll(Iterable<?> keys) {
/*  82 */     delegate().invalidateAll(keys);
/*     */   }
/*     */ 
/*     */   
/*     */   public void invalidateAll() {
/*  87 */     delegate().invalidateAll();
/*     */   }
/*     */ 
/*     */   
/*     */   public long size() {
/*  92 */     return delegate().size();
/*     */   }
/*     */ 
/*     */   
/*     */   public CacheStats stats() {
/*  97 */     return delegate().stats();
/*     */   }
/*     */ 
/*     */   
/*     */   public ConcurrentMap<K, V> asMap() {
/* 102 */     return delegate().asMap();
/*     */   }
/*     */ 
/*     */   
/*     */   public void cleanUp() {
/* 107 */     delegate().cleanUp();
/*     */   }
/*     */ 
/*     */   
/*     */   protected abstract Cache<K, V> delegate();
/*     */ 
/*     */   
/*     */   public static abstract class SimpleForwardingCache<K, V>
/*     */     extends ForwardingCache<K, V>
/*     */   {
/*     */     private final Cache<K, V> delegate;
/*     */     
/*     */     protected SimpleForwardingCache(Cache<K, V> delegate) {
/* 120 */       this.delegate = (Cache<K, V>)Preconditions.checkNotNull(delegate);
/*     */     }
/*     */ 
/*     */     
/*     */     protected final Cache<K, V> delegate() {
/* 125 */       return this.delegate;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\google\common\cache\ForwardingCache.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */