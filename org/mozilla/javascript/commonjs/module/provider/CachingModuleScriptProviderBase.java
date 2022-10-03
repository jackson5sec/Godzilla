/*     */ package org.mozilla.javascript.commonjs.module.provider;
/*     */ 
/*     */ import java.io.Reader;
/*     */ import java.io.Serializable;
/*     */ import java.net.URI;
/*     */ import org.mozilla.javascript.Context;
/*     */ import org.mozilla.javascript.Scriptable;
/*     */ import org.mozilla.javascript.commonjs.module.ModuleScript;
/*     */ import org.mozilla.javascript.commonjs.module.ModuleScriptProvider;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class CachingModuleScriptProviderBase
/*     */   implements ModuleScriptProvider, Serializable
/*     */ {
/*     */   private static final long serialVersionUID = 1L;
/*  31 */   private static final int loadConcurrencyLevel = Runtime.getRuntime().availableProcessors() * 8;
/*     */   private static final int loadLockShift;
/*     */   private static final int loadLockMask;
/*     */   private static final int loadLockCount;
/*     */   
/*     */   static {
/*  37 */     int sshift = 0;
/*  38 */     int ssize = 1;
/*  39 */     while (ssize < loadConcurrencyLevel) {
/*  40 */       sshift++;
/*  41 */       ssize <<= 1;
/*     */     } 
/*  43 */     loadLockShift = 32 - sshift;
/*  44 */     loadLockMask = ssize - 1;
/*  45 */     loadLockCount = ssize;
/*     */   }
/*  47 */   private final Object[] loadLocks = new Object[loadLockCount]; protected CachingModuleScriptProviderBase(ModuleSourceProvider moduleSourceProvider) {
/*  48 */     for (int i = 0; i < this.loadLocks.length; i++) {
/*  49 */       this.loadLocks[i] = new Object();
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  61 */     this.moduleSourceProvider = moduleSourceProvider;
/*     */   }
/*     */   
/*     */   private final ModuleSourceProvider moduleSourceProvider;
/*     */   
/*     */   public ModuleScript getModuleScript(Context cx, String moduleId, URI moduleUri, URI baseUri, Scriptable paths) throws Exception {
/*  67 */     CachedModuleScript cachedModule1 = getLoadedModule(moduleId);
/*  68 */     Object validator1 = getValidator(cachedModule1);
/*  69 */     ModuleSource moduleSource = (moduleUri == null) ? this.moduleSourceProvider.loadSource(moduleId, paths, validator1) : this.moduleSourceProvider.loadSource(moduleUri, baseUri, validator1);
/*     */ 
/*     */     
/*  72 */     if (moduleSource == ModuleSourceProvider.NOT_MODIFIED) {
/*  73 */       return cachedModule1.getModule();
/*     */     }
/*  75 */     if (moduleSource == null) {
/*  76 */       return null;
/*     */     }
/*  78 */     Reader reader = moduleSource.getReader();
/*     */     try {
/*  80 */       int idHash = moduleId.hashCode();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     }
/*     */     finally {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/*  99 */       reader.close();
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
/*     */ 
/*     */   
/*     */   public static class CachedModuleScript
/*     */   {
/*     */     private final ModuleScript moduleScript;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     private final Object validator;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public CachedModuleScript(ModuleScript moduleScript, Object validator) {
/* 137 */       this.moduleScript = moduleScript;
/* 138 */       this.validator = validator;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     ModuleScript getModule() {
/* 146 */       return this.moduleScript;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     Object getValidator() {
/* 154 */       return this.validator;
/*     */     }
/*     */   }
/*     */   
/*     */   private static Object getValidator(CachedModuleScript cachedModule) {
/* 159 */     return (cachedModule == null) ? null : cachedModule.getValidator();
/*     */   }
/*     */   
/*     */   private static boolean equal(Object o1, Object o2) {
/* 163 */     return (o1 == null) ? ((o2 == null)) : o1.equals(o2);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected static int getConcurrencyLevel() {
/* 171 */     return loadLockCount;
/*     */   }
/*     */   
/*     */   protected abstract void putLoadedModule(String paramString, ModuleScript paramModuleScript, Object paramObject);
/*     */   
/*     */   protected abstract CachedModuleScript getLoadedModule(String paramString);
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\mozilla\javascript\commonjs\module\provider\CachingModuleScriptProviderBase.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */