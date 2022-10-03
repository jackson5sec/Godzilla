/*     */ package org.mozilla.javascript;
/*     */ 
/*     */ import java.io.Serializable;
/*     */ import java.util.Map;
/*     */ import java.util.concurrent.ConcurrentHashMap;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ClassCache
/*     */   implements Serializable
/*     */ {
/*     */   private static final long serialVersionUID = -8866246036237312215L;
/*  24 */   private static final Object AKEY = "ClassCache";
/*     */ 
/*     */   
/*     */   private volatile boolean cachingIsEnabled = true;
/*     */ 
/*     */   
/*     */   private transient Map<Class<?>, JavaMembers> classTable;
/*     */ 
/*     */   
/*     */   private transient Map<JavaAdapter.JavaAdapterSignature, Class<?>> classAdapterCache;
/*     */ 
/*     */   
/*     */   private transient Map<Class<?>, Object> interfaceAdapterCache;
/*     */ 
/*     */   
/*     */   private int generatedClassSerial;
/*     */ 
/*     */   
/*     */   private Scriptable associatedScope;
/*     */ 
/*     */ 
/*     */   
/*     */   public static ClassCache get(Scriptable scope) {
/*  47 */     ClassCache cache = (ClassCache)ScriptableObject.getTopScopeValue(scope, AKEY);
/*     */     
/*  49 */     if (cache == null) {
/*  50 */       throw new RuntimeException("Can't find top level scope for ClassCache.get");
/*     */     }
/*     */     
/*  53 */     return cache;
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
/*     */   public boolean associate(ScriptableObject topScope) {
/*  69 */     if (topScope.getParentScope() != null)
/*     */     {
/*  71 */       throw new IllegalArgumentException();
/*     */     }
/*  73 */     if (this == topScope.associateValue(AKEY, this)) {
/*  74 */       this.associatedScope = topScope;
/*  75 */       return true;
/*     */     } 
/*  77 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public synchronized void clearCaches() {
/*  85 */     this.classTable = null;
/*  86 */     this.classAdapterCache = null;
/*  87 */     this.interfaceAdapterCache = null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final boolean isCachingEnabled() {
/*  96 */     return this.cachingIsEnabled;
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
/*     */   public synchronized void setCachingEnabled(boolean enabled) {
/* 119 */     if (enabled == this.cachingIsEnabled)
/*     */       return; 
/* 121 */     if (!enabled)
/* 122 */       clearCaches(); 
/* 123 */     this.cachingIsEnabled = enabled;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   Map<Class<?>, JavaMembers> getClassCacheMap() {
/* 130 */     if (this.classTable == null)
/*     */     {
/*     */       
/* 133 */       this.classTable = new ConcurrentHashMap<Class<?>, JavaMembers>(16, 0.75F, 1);
/*     */     }
/* 135 */     return this.classTable;
/*     */   }
/*     */ 
/*     */   
/*     */   Map<JavaAdapter.JavaAdapterSignature, Class<?>> getInterfaceAdapterCacheMap() {
/* 140 */     if (this.classAdapterCache == null) {
/* 141 */       this.classAdapterCache = new ConcurrentHashMap<JavaAdapter.JavaAdapterSignature, Class<?>>(16, 0.75F, 1);
/*     */     }
/* 143 */     return this.classAdapterCache;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   public boolean isInvokerOptimizationEnabled() {
/* 154 */     return false;
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
/*     */   @Deprecated
/*     */   public synchronized void setInvokerOptimizationEnabled(boolean enabled) {}
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final synchronized int newClassSerialNumber() {
/* 177 */     return ++this.generatedClassSerial;
/*     */   }
/*     */ 
/*     */   
/*     */   Object getInterfaceAdapter(Class<?> cl) {
/* 182 */     return (this.interfaceAdapterCache == null) ? null : this.interfaceAdapterCache.get(cl);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   synchronized void cacheInterfaceAdapter(Class<?> cl, Object iadapter) {
/* 189 */     if (this.cachingIsEnabled) {
/* 190 */       if (this.interfaceAdapterCache == null) {
/* 191 */         this.interfaceAdapterCache = new ConcurrentHashMap<Class<?>, Object>(16, 0.75F, 1);
/*     */       }
/* 193 */       this.interfaceAdapterCache.put(cl, iadapter);
/*     */     } 
/*     */   }
/*     */   
/*     */   Scriptable getAssociatedScope() {
/* 198 */     return this.associatedScope;
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\mozilla\javascript\ClassCache.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */