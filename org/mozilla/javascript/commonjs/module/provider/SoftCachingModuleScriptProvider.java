/*     */ package org.mozilla.javascript.commonjs.module.provider;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.ObjectInputStream;
/*     */ import java.io.ObjectOutputStream;
/*     */ import java.lang.ref.ReferenceQueue;
/*     */ import java.lang.ref.SoftReference;
/*     */ import java.net.URI;
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ import java.util.concurrent.ConcurrentHashMap;
/*     */ import java.util.concurrent.ConcurrentMap;
/*     */ import org.mozilla.javascript.Context;
/*     */ import org.mozilla.javascript.Script;
/*     */ import org.mozilla.javascript.Scriptable;
/*     */ import org.mozilla.javascript.commonjs.module.ModuleScript;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class SoftCachingModuleScriptProvider
/*     */   extends CachingModuleScriptProviderBase
/*     */ {
/*     */   private static final long serialVersionUID = 1L;
/*  36 */   private transient ReferenceQueue<Script> scriptRefQueue = new ReferenceQueue<Script>();
/*     */ 
/*     */   
/*  39 */   private transient ConcurrentMap<String, ScriptReference> scripts = new ConcurrentHashMap<String, ScriptReference>(16, 0.75F, getConcurrencyLevel());
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public SoftCachingModuleScriptProvider(ModuleSourceProvider moduleSourceProvider) {
/*  50 */     super(moduleSourceProvider);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ModuleScript getModuleScript(Context cx, String moduleId, URI uri, URI base, Scriptable paths) throws Exception {
/*     */     while (true) {
/*  61 */       ScriptReference ref = (ScriptReference)this.scriptRefQueue.poll();
/*  62 */       if (ref == null) {
/*     */         break;
/*     */       }
/*  65 */       this.scripts.remove(ref.getModuleId(), ref);
/*     */     } 
/*  67 */     return super.getModuleScript(cx, moduleId, uri, base, paths);
/*     */   }
/*     */ 
/*     */   
/*     */   protected CachingModuleScriptProviderBase.CachedModuleScript getLoadedModule(String moduleId) {
/*  72 */     ScriptReference scriptRef = this.scripts.get(moduleId);
/*  73 */     return (scriptRef != null) ? scriptRef.getCachedModuleScript() : null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void putLoadedModule(String moduleId, ModuleScript moduleScript, Object validator) {
/*  80 */     this.scripts.put(moduleId, new ScriptReference(moduleScript.getScript(), moduleId, moduleScript.getUri(), moduleScript.getBase(), validator, this.scriptRefQueue));
/*     */   }
/*     */ 
/*     */   
/*     */   private static class ScriptReference
/*     */     extends SoftReference<Script>
/*     */   {
/*     */     private final String moduleId;
/*     */     private final URI uri;
/*     */     private final URI base;
/*     */     private final Object validator;
/*     */     
/*     */     ScriptReference(Script script, String moduleId, URI uri, URI base, Object validator, ReferenceQueue<Script> refQueue) {
/*  93 */       super(script, refQueue);
/*  94 */       this.moduleId = moduleId;
/*  95 */       this.uri = uri;
/*  96 */       this.base = base;
/*  97 */       this.validator = validator;
/*     */     }
/*     */     
/*     */     CachingModuleScriptProviderBase.CachedModuleScript getCachedModuleScript() {
/* 101 */       Script script = get();
/* 102 */       if (script == null) {
/* 103 */         return null;
/*     */       }
/* 105 */       return new CachingModuleScriptProviderBase.CachedModuleScript(new ModuleScript(script, this.uri, this.base), this.validator);
/*     */     }
/*     */ 
/*     */     
/*     */     String getModuleId() {
/* 110 */       return this.moduleId;
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
/* 117 */     this.scriptRefQueue = new ReferenceQueue<Script>();
/* 118 */     this.scripts = new ConcurrentHashMap<String, ScriptReference>();
/* 119 */     Map<String, CachingModuleScriptProviderBase.CachedModuleScript> serScripts = (Map<String, CachingModuleScriptProviderBase.CachedModuleScript>)in.readObject();
/* 120 */     for (Map.Entry<String, CachingModuleScriptProviderBase.CachedModuleScript> entry : serScripts.entrySet()) {
/* 121 */       CachingModuleScriptProviderBase.CachedModuleScript cachedModuleScript = entry.getValue();
/* 122 */       putLoadedModule(entry.getKey(), cachedModuleScript.getModule(), cachedModuleScript.getValidator());
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private void writeObject(ObjectOutputStream out) throws IOException {
/* 128 */     Map<String, CachingModuleScriptProviderBase.CachedModuleScript> serScripts = new HashMap<String, CachingModuleScriptProviderBase.CachedModuleScript>();
/*     */     
/* 130 */     for (Map.Entry<String, ScriptReference> entry : this.scripts.entrySet()) {
/* 131 */       CachingModuleScriptProviderBase.CachedModuleScript cachedModuleScript = ((ScriptReference)entry.getValue()).getCachedModuleScript();
/*     */       
/* 133 */       if (cachedModuleScript != null) {
/* 134 */         serScripts.put(entry.getKey(), cachedModuleScript);
/*     */       }
/*     */     } 
/* 137 */     out.writeObject(serScripts);
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\mozilla\javascript\commonjs\module\provider\SoftCachingModuleScriptProvider.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */