/*     */ package javassist.scopedpool;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collections;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.WeakHashMap;
/*     */ import javassist.ClassPath;
/*     */ import javassist.ClassPool;
/*     */ import javassist.LoaderClassPath;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ScopedClassPoolRepositoryImpl
/*     */   implements ScopedClassPoolRepository
/*     */ {
/*  37 */   private static final ScopedClassPoolRepositoryImpl instance = new ScopedClassPoolRepositoryImpl();
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean prune = true;
/*     */ 
/*     */   
/*     */   boolean pruneWhenCached;
/*     */ 
/*     */   
/*  47 */   protected Map<ClassLoader, ScopedClassPool> registeredCLs = Collections.synchronizedMap(new WeakHashMap<>());
/*     */ 
/*     */   
/*     */   protected ClassPool classpool;
/*     */ 
/*     */   
/*  53 */   protected ScopedClassPoolFactory factory = new ScopedClassPoolFactoryImpl();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static ScopedClassPoolRepository getInstance() {
/*  61 */     return instance;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private ScopedClassPoolRepositoryImpl() {
/*  68 */     this.classpool = ClassPool.getDefault();
/*     */     
/*  70 */     ClassLoader cl = Thread.currentThread().getContextClassLoader();
/*  71 */     this.classpool.insertClassPath((ClassPath)new LoaderClassPath(cl));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isPrune() {
/*  81 */     return this.prune;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setPrune(boolean prune) {
/*  91 */     this.prune = prune;
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
/*     */   public ScopedClassPool createScopedClassPool(ClassLoader cl, ClassPool src) {
/* 103 */     return this.factory.create(cl, src, this);
/*     */   }
/*     */ 
/*     */   
/*     */   public ClassPool findClassPool(ClassLoader cl) {
/* 108 */     if (cl == null) {
/* 109 */       return registerClassLoader(ClassLoader.getSystemClassLoader());
/*     */     }
/* 111 */     return registerClassLoader(cl);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ClassPool registerClassLoader(ClassLoader ucl) {
/* 122 */     synchronized (this.registeredCLs) {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 128 */       if (this.registeredCLs.containsKey(ucl)) {
/* 129 */         return this.registeredCLs.get(ucl);
/*     */       }
/* 131 */       ScopedClassPool pool = createScopedClassPool(ucl, this.classpool);
/* 132 */       this.registeredCLs.put(ucl, pool);
/* 133 */       return pool;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Map<ClassLoader, ScopedClassPool> getRegisteredCLs() {
/* 142 */     clearUnregisteredClassLoaders();
/* 143 */     return this.registeredCLs;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void clearUnregisteredClassLoaders() {
/* 152 */     List<ClassLoader> toUnregister = null;
/* 153 */     synchronized (this.registeredCLs) {
/* 154 */       for (Map.Entry<ClassLoader, ScopedClassPool> reg : this.registeredCLs.entrySet()) {
/* 155 */         if (((ScopedClassPool)reg.getValue()).isUnloadedClassLoader()) {
/* 156 */           ClassLoader cl = ((ScopedClassPool)reg.getValue()).getClassLoader();
/* 157 */           if (cl != null) {
/* 158 */             if (toUnregister == null)
/* 159 */               toUnregister = new ArrayList<>(); 
/* 160 */             toUnregister.add(cl);
/*     */           } 
/* 162 */           this.registeredCLs.remove(reg.getKey());
/*     */         } 
/*     */       } 
/* 165 */       if (toUnregister != null)
/* 166 */         for (ClassLoader cl : toUnregister) {
/* 167 */           unregisterClassLoader(cl);
/*     */         } 
/*     */     } 
/*     */   }
/*     */   
/*     */   public void unregisterClassLoader(ClassLoader cl) {
/* 173 */     synchronized (this.registeredCLs) {
/* 174 */       ScopedClassPool pool = this.registeredCLs.remove(cl);
/* 175 */       if (pool != null) {
/* 176 */         pool.close();
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void insertDelegate(ScopedClassPoolRepository delegate) {}
/*     */ 
/*     */   
/*     */   public void setClassPoolFactory(ScopedClassPoolFactory factory) {
/* 186 */     this.factory = factory;
/*     */   }
/*     */ 
/*     */   
/*     */   public ScopedClassPoolFactory getClassPoolFactory() {
/* 191 */     return this.factory;
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\javassist\scopedpool\ScopedClassPoolRepositoryImpl.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */