/*    */ package org.mozilla.javascript.commonjs.module.provider;
/*    */ 
/*    */ import java.net.URI;
/*    */ import java.util.LinkedList;
/*    */ import java.util.List;
/*    */ import org.mozilla.javascript.Context;
/*    */ import org.mozilla.javascript.Scriptable;
/*    */ import org.mozilla.javascript.commonjs.module.ModuleScript;
/*    */ import org.mozilla.javascript.commonjs.module.ModuleScriptProvider;
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
/*    */ 
/*    */ public class MultiModuleScriptProvider
/*    */   implements ModuleScriptProvider
/*    */ {
/*    */   private final ModuleScriptProvider[] providers;
/*    */   
/*    */   public MultiModuleScriptProvider(Iterable<? extends ModuleScriptProvider> providers) {
/* 31 */     List<ModuleScriptProvider> l = new LinkedList<ModuleScriptProvider>();
/* 32 */     for (ModuleScriptProvider provider : providers) {
/* 33 */       l.add(provider);
/*    */     }
/* 35 */     this.providers = l.<ModuleScriptProvider>toArray(new ModuleScriptProvider[l.size()]);
/*    */   }
/*    */ 
/*    */   
/*    */   public ModuleScript getModuleScript(Context cx, String moduleId, URI uri, URI base, Scriptable paths) throws Exception {
/* 40 */     for (ModuleScriptProvider provider : this.providers) {
/* 41 */       ModuleScript script = provider.getModuleScript(cx, moduleId, uri, base, paths);
/*    */       
/* 43 */       if (script != null) {
/* 44 */         return script;
/*    */       }
/*    */     } 
/* 47 */     return null;
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\mozilla\javascript\commonjs\module\provider\MultiModuleScriptProvider.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */