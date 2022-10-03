/*    */ package org.mozilla.javascript.commonjs.module.provider;
/*    */ 
/*    */ import java.util.Map;
/*    */ import java.util.concurrent.ConcurrentHashMap;
/*    */ import org.mozilla.javascript.commonjs.module.ModuleScript;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class StrongCachingModuleScriptProvider
/*    */   extends CachingModuleScriptProviderBase
/*    */ {
/*    */   private static final long serialVersionUID = 1L;
/* 24 */   private final Map<String, CachingModuleScriptProviderBase.CachedModuleScript> modules = new ConcurrentHashMap<String, CachingModuleScriptProviderBase.CachedModuleScript>(16, 0.75F, getConcurrencyLevel());
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public StrongCachingModuleScriptProvider(ModuleSourceProvider moduleSourceProvider) {
/* 34 */     super(moduleSourceProvider);
/*    */   }
/*    */ 
/*    */   
/*    */   protected CachingModuleScriptProviderBase.CachedModuleScript getLoadedModule(String moduleId) {
/* 39 */     return this.modules.get(moduleId);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   protected void putLoadedModule(String moduleId, ModuleScript moduleScript, Object validator) {
/* 45 */     this.modules.put(moduleId, new CachingModuleScriptProviderBase.CachedModuleScript(moduleScript, validator));
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\mozilla\javascript\commonjs\module\provider\StrongCachingModuleScriptProvider.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */