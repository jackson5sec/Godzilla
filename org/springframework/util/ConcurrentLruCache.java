/*     */ package org.springframework.util;
/*     */ 
/*     */ import java.util.concurrent.ConcurrentHashMap;
/*     */ import java.util.concurrent.ConcurrentLinkedDeque;
/*     */ import java.util.concurrent.locks.ReadWriteLock;
/*     */ import java.util.concurrent.locks.ReentrantReadWriteLock;
/*     */ import java.util.function.Function;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ConcurrentLruCache<K, V>
/*     */ {
/*     */   private final int sizeLimit;
/*     */   private final Function<K, V> generator;
/*  45 */   private final ConcurrentHashMap<K, V> cache = new ConcurrentHashMap<>();
/*     */   
/*  47 */   private final ConcurrentLinkedDeque<K> queue = new ConcurrentLinkedDeque<>();
/*     */   
/*  49 */   private final ReadWriteLock lock = new ReentrantReadWriteLock();
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private volatile int size;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ConcurrentLruCache(int sizeLimit, Function<K, V> generator) {
/*  61 */     Assert.isTrue((sizeLimit >= 0), "Cache size limit must not be negative");
/*  62 */     Assert.notNull(generator, "Generator function must not be null");
/*  63 */     this.sizeLimit = sizeLimit;
/*  64 */     this.generator = generator;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public V get(K key) {
/*  75 */     if (this.sizeLimit == 0) {
/*  76 */       return this.generator.apply(key);
/*     */     }
/*     */     
/*  79 */     V cached = this.cache.get(key);
/*  80 */     if (cached != null) {
/*  81 */       if (this.size < this.sizeLimit) {
/*  82 */         return cached;
/*     */       }
/*  84 */       this.lock.readLock().lock();
/*     */       try {
/*  86 */         if (this.queue.removeLastOccurrence(key)) {
/*  87 */           this.queue.offer(key);
/*     */         }
/*  89 */         return cached;
/*     */       } finally {
/*     */         
/*  92 */         this.lock.readLock().unlock();
/*     */       } 
/*     */     } 
/*     */     
/*  96 */     this.lock.writeLock().lock();
/*     */     
/*     */     try {
/*  99 */       cached = this.cache.get(key);
/* 100 */       if (cached != null) {
/* 101 */         if (this.queue.removeLastOccurrence(key)) {
/* 102 */           this.queue.offer(key);
/*     */         }
/* 104 */         return cached;
/*     */       } 
/*     */       
/* 107 */       V value = this.generator.apply(key);
/* 108 */       if (this.size == this.sizeLimit) {
/* 109 */         K leastUsed = this.queue.poll();
/* 110 */         if (leastUsed != null) {
/* 111 */           this.cache.remove(leastUsed);
/*     */         }
/*     */       } 
/* 114 */       this.queue.offer(key);
/* 115 */       this.cache.put(key, value);
/* 116 */       this.size = this.cache.size();
/* 117 */       return value;
/*     */     } finally {
/*     */       
/* 120 */       this.lock.writeLock().unlock();
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean contains(K key) {
/* 131 */     return this.cache.containsKey(key);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean remove(K key) {
/* 141 */     this.lock.writeLock().lock();
/*     */     try {
/* 143 */       boolean wasPresent = (this.cache.remove(key) != null);
/* 144 */       this.queue.remove(key);
/* 145 */       this.size = this.cache.size();
/* 146 */       return wasPresent;
/*     */     } finally {
/*     */       
/* 149 */       this.lock.writeLock().unlock();
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void clear() {
/* 157 */     this.lock.writeLock().lock();
/*     */     try {
/* 159 */       this.cache.clear();
/* 160 */       this.queue.clear();
/* 161 */       this.size = 0;
/*     */     } finally {
/*     */       
/* 164 */       this.lock.writeLock().unlock();
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int size() {
/* 173 */     return this.size;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int sizeLimit() {
/* 182 */     return this.sizeLimit;
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\springframewor\\util\ConcurrentLruCache.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */