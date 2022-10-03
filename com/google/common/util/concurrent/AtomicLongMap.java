/*     */ package com.google.common.util.concurrent;
/*     */ 
/*     */ import com.google.common.annotations.Beta;
/*     */ import com.google.common.annotations.GwtCompatible;
/*     */ import com.google.common.base.Preconditions;
/*     */ import com.google.errorprone.annotations.CanIgnoreReturnValue;
/*     */ import java.io.Serializable;
/*     */ import java.util.Collections;
/*     */ import java.util.Map;
/*     */ import java.util.concurrent.ConcurrentHashMap;
/*     */ import java.util.concurrent.atomic.AtomicBoolean;
/*     */ import java.util.concurrent.atomic.AtomicLong;
/*     */ import java.util.function.LongBinaryOperator;
/*     */ import java.util.function.LongUnaryOperator;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
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
/*     */ public final class AtomicLongMap<K>
/*     */   implements Serializable
/*     */ {
/*     */   private final ConcurrentHashMap<K, Long> map;
/*     */   private transient Map<K, Long> asMap;
/*     */   
/*     */   private AtomicLongMap(ConcurrentHashMap<K, Long> map) {
/*  62 */     this.map = (ConcurrentHashMap<K, Long>)Preconditions.checkNotNull(map);
/*     */   }
/*     */ 
/*     */   
/*     */   public static <K> AtomicLongMap<K> create() {
/*  67 */     return new AtomicLongMap<>(new ConcurrentHashMap<>());
/*     */   }
/*     */ 
/*     */   
/*     */   public static <K> AtomicLongMap<K> create(Map<? extends K, ? extends Long> m) {
/*  72 */     AtomicLongMap<K> result = create();
/*  73 */     result.putAll(m);
/*  74 */     return result;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public long get(K key) {
/*  82 */     return ((Long)this.map.getOrDefault(key, Long.valueOf(0L))).longValue();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @CanIgnoreReturnValue
/*     */   public long incrementAndGet(K key) {
/*  90 */     return addAndGet(key, 1L);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @CanIgnoreReturnValue
/*     */   public long decrementAndGet(K key) {
/*  98 */     return addAndGet(key, -1L);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @CanIgnoreReturnValue
/*     */   public long addAndGet(K key, long delta) {
/* 107 */     return accumulateAndGet(key, delta, Long::sum);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @CanIgnoreReturnValue
/*     */   public long getAndIncrement(K key) {
/* 115 */     return getAndAdd(key, 1L);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @CanIgnoreReturnValue
/*     */   public long getAndDecrement(K key) {
/* 123 */     return getAndAdd(key, -1L);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @CanIgnoreReturnValue
/*     */   public long getAndAdd(K key, long delta) {
/* 132 */     return getAndAccumulate(key, delta, Long::sum);
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
/*     */   public long updateAndGet(K key, LongUnaryOperator updaterFunction) {
/* 144 */     Preconditions.checkNotNull(updaterFunction);
/* 145 */     return ((Long)this.map.compute(key, (k, value) -> Long.valueOf(updaterFunction.applyAsLong((value == null) ? 0L : value.longValue())))).longValue();
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
/*     */   @CanIgnoreReturnValue
/*     */   public long getAndUpdate(K key, LongUnaryOperator updaterFunction) {
/* 158 */     Preconditions.checkNotNull(updaterFunction);
/* 159 */     AtomicLong holder = new AtomicLong();
/* 160 */     this.map.compute(key, (k, value) -> {
/*     */           long oldValue = (value == null) ? 0L : value.longValue();
/*     */           
/*     */           holder.set(oldValue);
/*     */           
/*     */           return Long.valueOf(updaterFunction.applyAsLong(oldValue));
/*     */         });
/* 167 */     return holder.get();
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
/*     */   @CanIgnoreReturnValue
/*     */   public long accumulateAndGet(K key, long x, LongBinaryOperator accumulatorFunction) {
/* 180 */     Preconditions.checkNotNull(accumulatorFunction);
/* 181 */     return updateAndGet(key, oldValue -> accumulatorFunction.applyAsLong(oldValue, x));
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
/*     */   @CanIgnoreReturnValue
/*     */   public long getAndAccumulate(K key, long x, LongBinaryOperator accumulatorFunction) {
/* 194 */     Preconditions.checkNotNull(accumulatorFunction);
/* 195 */     return getAndUpdate(key, oldValue -> accumulatorFunction.applyAsLong(oldValue, x));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @CanIgnoreReturnValue
/*     */   public long put(K key, long newValue) {
/* 204 */     return getAndUpdate(key, x -> newValue);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void putAll(Map<? extends K, ? extends Long> m) {
/* 214 */     m.forEach(this::put);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @CanIgnoreReturnValue
/*     */   public long remove(K key) {
/* 223 */     Long result = this.map.remove(key);
/* 224 */     return (result == null) ? 0L : result.longValue();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   boolean remove(K key, long value) {
/* 232 */     return this.map.remove(key, Long.valueOf(value));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Beta
/*     */   @CanIgnoreReturnValue
/*     */   public boolean removeIfZero(K key) {
/* 243 */     return remove(key, 0L);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void removeAllZeros() {
/* 253 */     this.map.values().removeIf(x -> (x.longValue() == 0L));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public long sum() {
/* 262 */     return this.map.values().stream().mapToLong(Long::longValue).sum();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Map<K, Long> asMap() {
/* 269 */     Map<K, Long> result = this.asMap;
/* 270 */     return (result == null) ? (this.asMap = createAsMap()) : result;
/*     */   }
/*     */   
/*     */   private Map<K, Long> createAsMap() {
/* 274 */     return Collections.unmodifiableMap(this.map);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean containsKey(Object key) {
/* 279 */     return this.map.containsKey(key);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int size() {
/* 287 */     return this.map.size();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isEmpty() {
/* 292 */     return this.map.isEmpty();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void clear() {
/* 302 */     this.map.clear();
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 307 */     return this.map.toString();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   long putIfAbsent(K key, long newValue) {
/* 316 */     AtomicBoolean noValue = new AtomicBoolean(false);
/*     */     
/* 318 */     Long result = this.map.compute(key, (k, oldValue) -> {
/*     */           if (oldValue == null || oldValue.longValue() == 0L) {
/*     */             noValue.set(true);
/*     */             
/*     */             return Long.valueOf(newValue);
/*     */           } 
/*     */           
/*     */           return oldValue;
/*     */         });
/*     */     
/* 328 */     return noValue.get() ? 0L : result.longValue();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   boolean replace(K key, long expectedOldValue, long newValue) {
/* 339 */     if (expectedOldValue == 0L) {
/* 340 */       return (putIfAbsent(key, newValue) == 0L);
/*     */     }
/* 342 */     return this.map.replace(key, Long.valueOf(expectedOldValue), Long.valueOf(newValue));
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\google\commo\\util\concurrent\AtomicLongMap.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */