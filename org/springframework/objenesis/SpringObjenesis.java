/*     */ package org.springframework.objenesis;
/*     */ 
/*     */ import org.springframework.core.SpringProperties;
/*     */ import org.springframework.objenesis.instantiator.ObjectInstantiator;
/*     */ import org.springframework.objenesis.strategy.InstantiatorStrategy;
/*     */ import org.springframework.objenesis.strategy.StdInstantiatorStrategy;
/*     */ import org.springframework.util.ConcurrentReferenceHashMap;
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
/*     */ public class SpringObjenesis
/*     */   implements Objenesis
/*     */ {
/*     */   public static final String IGNORE_OBJENESIS_PROPERTY_NAME = "spring.objenesis.ignore";
/*     */   private final InstantiatorStrategy strategy;
/*  49 */   private final ConcurrentReferenceHashMap<Class<?>, ObjectInstantiator<?>> cache = new ConcurrentReferenceHashMap();
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private volatile Boolean worthTrying;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public SpringObjenesis() {
/*  60 */     this(null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public SpringObjenesis(InstantiatorStrategy strategy) {
/*  69 */     this.strategy = (strategy != null) ? strategy : (InstantiatorStrategy)new StdInstantiatorStrategy();
/*     */ 
/*     */     
/*  72 */     if (SpringProperties.getFlag("spring.objenesis.ignore")) {
/*  73 */       this.worthTrying = Boolean.FALSE;
/*     */     }
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
/*     */   public boolean isWorthTrying() {
/*  86 */     return (this.worthTrying != Boolean.FALSE);
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
/*     */   public <T> T newInstance(Class<T> clazz, boolean useCache) {
/*  99 */     if (!useCache) {
/* 100 */       return (T)newInstantiatorOf(clazz).newInstance();
/*     */     }
/* 102 */     return (T)getInstantiatorOf(clazz).newInstance();
/*     */   }
/*     */   
/*     */   public <T> T newInstance(Class<T> clazz) {
/* 106 */     return (T)getInstantiatorOf(clazz).newInstance();
/*     */   }
/*     */ 
/*     */   
/*     */   public <T> ObjectInstantiator<T> getInstantiatorOf(Class<T> clazz) {
/* 111 */     ObjectInstantiator<?> instantiator = (ObjectInstantiator)this.cache.get(clazz);
/* 112 */     if (instantiator == null) {
/* 113 */       ObjectInstantiator<T> newInstantiator = newInstantiatorOf(clazz);
/* 114 */       instantiator = (ObjectInstantiator)this.cache.putIfAbsent(clazz, newInstantiator);
/* 115 */       if (instantiator == null) {
/* 116 */         instantiator = newInstantiator;
/*     */       }
/*     */     } 
/* 119 */     return (ObjectInstantiator)instantiator;
/*     */   }
/*     */   
/*     */   protected <T> ObjectInstantiator<T> newInstantiatorOf(Class<T> clazz) {
/* 123 */     Boolean currentWorthTrying = this.worthTrying;
/*     */     try {
/* 125 */       ObjectInstantiator<T> instantiator = this.strategy.newInstantiatorOf(clazz);
/* 126 */       if (currentWorthTrying == null) {
/* 127 */         this.worthTrying = Boolean.TRUE;
/*     */       }
/* 129 */       return instantiator;
/*     */     }
/* 131 */     catch (ObjenesisException ex) {
/* 132 */       if (currentWorthTrying == null) {
/* 133 */         Throwable cause = ex.getCause();
/* 134 */         if (cause instanceof ClassNotFoundException || cause instanceof IllegalAccessException)
/*     */         {
/*     */ 
/*     */           
/* 138 */           this.worthTrying = Boolean.FALSE;
/*     */         }
/*     */       } 
/* 141 */       throw ex;
/*     */     }
/* 143 */     catch (NoClassDefFoundError err) {
/*     */ 
/*     */       
/* 146 */       if (currentWorthTrying == null) {
/* 147 */         this.worthTrying = Boolean.FALSE;
/*     */       }
/* 149 */       throw new ObjenesisException(err);
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\springframework\objenesis\SpringObjenesis.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */