/*     */ package org.springframework.objenesis;
/*     */ 
/*     */ import java.util.concurrent.ConcurrentHashMap;
/*     */ import org.springframework.objenesis.instantiator.ObjectInstantiator;
/*     */ import org.springframework.objenesis.strategy.InstantiatorStrategy;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ObjenesisBase
/*     */   implements Objenesis
/*     */ {
/*     */   protected final InstantiatorStrategy strategy;
/*     */   protected ConcurrentHashMap<String, ObjectInstantiator<?>> cache;
/*     */   
/*     */   public ObjenesisBase(InstantiatorStrategy strategy) {
/*  43 */     this(strategy, true);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ObjenesisBase(InstantiatorStrategy strategy, boolean useCache) {
/*  53 */     if (strategy == null) {
/*  54 */       throw new IllegalArgumentException("A strategy can't be null");
/*     */     }
/*  56 */     this.strategy = strategy;
/*  57 */     this.cache = useCache ? new ConcurrentHashMap<>() : null;
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/*  62 */     return getClass().getName() + " using " + this.strategy.getClass().getName() + ((this.cache == null) ? " without" : " with") + " caching";
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public <T> T newInstance(Class<T> clazz) {
/*  73 */     return (T)getInstantiatorOf(clazz).newInstance();
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
/*     */   public <T> ObjectInstantiator<T> getInstantiatorOf(Class<T> clazz) {
/*  86 */     if (clazz.isPrimitive()) {
/*  87 */       throw new IllegalArgumentException("Primitive types can't be instantiated in Java");
/*     */     }
/*  89 */     if (this.cache == null) {
/*  90 */       return this.strategy.newInstantiatorOf(clazz);
/*     */     }
/*  92 */     ObjectInstantiator<?> instantiator = this.cache.get(clazz.getName());
/*  93 */     if (instantiator == null) {
/*  94 */       ObjectInstantiator<?> newInstantiator = this.strategy.newInstantiatorOf(clazz);
/*  95 */       instantiator = this.cache.putIfAbsent(clazz.getName(), newInstantiator);
/*  96 */       if (instantiator == null) {
/*  97 */         instantiator = newInstantiator;
/*     */       }
/*     */     } 
/* 100 */     return (ObjectInstantiator)instantiator;
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\springframework\objenesis\ObjenesisBase.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */