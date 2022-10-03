/*     */ package org.mozilla.javascript.commonjs.module;
/*     */ 
/*     */ import java.io.File;
/*     */ import java.net.URI;
/*     */ import java.net.URISyntaxException;
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ import java.util.concurrent.ConcurrentHashMap;
/*     */ import org.mozilla.javascript.BaseFunction;
/*     */ import org.mozilla.javascript.Context;
/*     */ import org.mozilla.javascript.Script;
/*     */ import org.mozilla.javascript.ScriptRuntime;
/*     */ import org.mozilla.javascript.Scriptable;
/*     */ import org.mozilla.javascript.ScriptableObject;
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
/*     */ public class Require
/*     */   extends BaseFunction
/*     */ {
/*     */   private static final long serialVersionUID = 1L;
/*     */   private final ModuleScriptProvider moduleScriptProvider;
/*     */   private final Scriptable nativeScope;
/*     */   private final Scriptable paths;
/*     */   private final boolean sandboxed;
/*     */   private final Script preExec;
/*     */   private final Script postExec;
/*  51 */   private String mainModuleId = null;
/*     */   
/*     */   private Scriptable mainExports;
/*     */   
/*  55 */   private final Map<String, Scriptable> exportedModuleInterfaces = new ConcurrentHashMap<String, Scriptable>();
/*     */   
/*  57 */   private final Object loadLock = new Object();
/*     */ 
/*     */ 
/*     */   
/*  61 */   private static final ThreadLocal<Map<String, Scriptable>> loadingModuleInterfaces = new ThreadLocal<Map<String, Scriptable>>();
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
/*     */   public Require(Context cx, Scriptable nativeScope, ModuleScriptProvider moduleScriptProvider, Script preExec, Script postExec, boolean sandboxed) {
/*  84 */     this.moduleScriptProvider = moduleScriptProvider;
/*  85 */     this.nativeScope = nativeScope;
/*  86 */     this.sandboxed = sandboxed;
/*  87 */     this.preExec = preExec;
/*  88 */     this.postExec = postExec;
/*  89 */     setPrototype(ScriptableObject.getFunctionPrototype(nativeScope));
/*  90 */     if (!sandboxed) {
/*  91 */       this.paths = cx.newArray(nativeScope, 0);
/*  92 */       defineReadOnlyProperty((ScriptableObject)this, "paths", this.paths);
/*     */     } else {
/*     */       
/*  95 */       this.paths = null;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Scriptable requireMain(Context cx, String mainModuleId) {
/*     */     ModuleScript moduleScript;
/* 117 */     if (this.mainModuleId != null) {
/* 118 */       if (!this.mainModuleId.equals(mainModuleId)) {
/* 119 */         throw new IllegalStateException("Main module already set to " + this.mainModuleId);
/*     */       }
/*     */       
/* 122 */       return this.mainExports;
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/*     */     try {
/* 128 */       moduleScript = this.moduleScriptProvider.getModuleScript(cx, mainModuleId, null, null, this.paths);
/*     */     }
/* 130 */     catch (RuntimeException x) {
/* 131 */       throw x;
/* 132 */     } catch (Exception x) {
/* 133 */       throw new RuntimeException(x);
/*     */     } 
/*     */     
/* 136 */     if (moduleScript != null) {
/* 137 */       this.mainExports = getExportedModuleInterface(cx, mainModuleId, (URI)null, (URI)null, true);
/*     */     }
/* 139 */     else if (!this.sandboxed) {
/*     */       
/* 141 */       URI mainUri = null;
/*     */ 
/*     */       
/*     */       try {
/* 145 */         mainUri = new URI(mainModuleId);
/* 146 */       } catch (URISyntaxException usx) {}
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 151 */       if (mainUri == null || !mainUri.isAbsolute()) {
/* 152 */         File file = new File(mainModuleId);
/* 153 */         if (!file.isFile()) {
/* 154 */           throw ScriptRuntime.throwError(cx, this.nativeScope, "Module \"" + mainModuleId + "\" not found.");
/*     */         }
/*     */         
/* 157 */         mainUri = file.toURI();
/*     */       } 
/* 159 */       this.mainExports = getExportedModuleInterface(cx, mainUri.toString(), mainUri, (URI)null, true);
/*     */     } 
/*     */ 
/*     */     
/* 163 */     this.mainModuleId = mainModuleId;
/* 164 */     return this.mainExports;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void install(Scriptable scope) {
/* 173 */     ScriptableObject.putProperty(scope, "require", this);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Object call(Context cx, Scriptable scope, Scriptable thisObj, Object[] args) {
/* 180 */     if (args == null || args.length < 1) {
/* 181 */       throw ScriptRuntime.throwError(cx, scope, "require() needs one argument");
/*     */     }
/*     */ 
/*     */     
/* 185 */     String id = (String)Context.jsToJava(args[0], String.class);
/* 186 */     URI uri = null;
/* 187 */     URI base = null;
/* 188 */     if (id.startsWith("./") || id.startsWith("../")) {
/* 189 */       if (!(thisObj instanceof ModuleScope)) {
/* 190 */         throw ScriptRuntime.throwError(cx, scope, "Can't resolve relative module ID \"" + id + "\" when require() is used outside of a module");
/*     */       }
/*     */ 
/*     */ 
/*     */       
/* 195 */       ModuleScope moduleScope = (ModuleScope)thisObj;
/* 196 */       base = moduleScope.getBase();
/* 197 */       URI current = moduleScope.getUri();
/* 198 */       uri = current.resolve(id);
/*     */       
/* 200 */       if (base == null) {
/*     */ 
/*     */         
/* 203 */         id = uri.toString();
/*     */       } else {
/*     */         
/* 206 */         id = base.relativize(current).resolve(id).toString();
/* 207 */         if (id.charAt(0) == '.') {
/*     */ 
/*     */           
/* 210 */           if (this.sandboxed) {
/* 211 */             throw ScriptRuntime.throwError(cx, scope, "Module \"" + id + "\" is not contained in sandbox.");
/*     */           }
/*     */           
/* 214 */           id = uri.toString();
/*     */         } 
/*     */       } 
/*     */     } 
/*     */     
/* 219 */     return getExportedModuleInterface(cx, id, uri, base, false);
/*     */   }
/*     */ 
/*     */   
/*     */   public Scriptable construct(Context cx, Scriptable scope, Object[] args) {
/* 224 */     throw ScriptRuntime.throwError(cx, scope, "require() can not be invoked as a constructor");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private Scriptable getExportedModuleInterface(Context cx, String id, URI uri, URI base, boolean isMain) {
/* 232 */     Scriptable exports = this.exportedModuleInterfaces.get(id);
/* 233 */     if (exports != null) {
/* 234 */       if (isMain) {
/* 235 */         throw new IllegalStateException("Attempt to set main module after it was loaded");
/*     */       }
/*     */       
/* 238 */       return exports;
/*     */     } 
/*     */ 
/*     */     
/* 242 */     Map<String, Scriptable> threadLoadingModules = loadingModuleInterfaces.get();
/*     */     
/* 244 */     if (threadLoadingModules != null) {
/* 245 */       exports = threadLoadingModules.get(id);
/* 246 */       if (exports != null) {
/* 247 */         return exports;
/*     */       }
/*     */     } 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 258 */     synchronized (this.loadLock) {
/*     */ 
/*     */       
/* 261 */       exports = this.exportedModuleInterfaces.get(id);
/* 262 */       if (exports != null) {
/* 263 */         return exports;
/*     */       }
/*     */       
/* 266 */       ModuleScript moduleScript = getModule(cx, id, uri, base);
/* 267 */       if (this.sandboxed && !moduleScript.isSandboxed()) {
/* 268 */         throw ScriptRuntime.throwError(cx, this.nativeScope, "Module \"" + id + "\" is not contained in sandbox.");
/*     */       }
/*     */       
/* 271 */       exports = cx.newObject(this.nativeScope);
/*     */       
/* 273 */       boolean outermostLocked = (threadLoadingModules == null);
/* 274 */       if (outermostLocked) {
/* 275 */         threadLoadingModules = new HashMap<String, Scriptable>();
/* 276 */         loadingModuleInterfaces.set(threadLoadingModules);
/*     */       } 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 286 */       threadLoadingModules.put(id, exports);
/*     */ 
/*     */       
/*     */       try {
/* 290 */         Scriptable newExports = executeModuleScript(cx, id, exports, moduleScript, isMain);
/*     */         
/* 292 */         if (exports != newExports) {
/* 293 */           threadLoadingModules.put(id, newExports);
/* 294 */           exports = newExports;
/*     */         }
/*     */       
/* 297 */       } catch (RuntimeException e) {
/*     */         
/* 299 */         threadLoadingModules.remove(id);
/* 300 */         throw e;
/*     */       } finally {
/*     */         
/* 303 */         if (outermostLocked) {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */           
/* 310 */           this.exportedModuleInterfaces.putAll(threadLoadingModules);
/* 311 */           loadingModuleInterfaces.set(null);
/*     */         } 
/*     */       } 
/*     */     } 
/* 315 */     return exports;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private Scriptable executeModuleScript(Context cx, String id, Scriptable exports, ModuleScript moduleScript, boolean isMain) {
/* 321 */     ScriptableObject moduleObject = (ScriptableObject)cx.newObject(this.nativeScope);
/*     */     
/* 323 */     URI uri = moduleScript.getUri();
/* 324 */     URI base = moduleScript.getBase();
/* 325 */     defineReadOnlyProperty(moduleObject, "id", id);
/* 326 */     if (!this.sandboxed) {
/* 327 */       defineReadOnlyProperty(moduleObject, "uri", uri.toString());
/*     */     }
/* 329 */     ModuleScope moduleScope = new ModuleScope(this.nativeScope, uri, base);
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 334 */     moduleScope.put("exports", (Scriptable)moduleScope, exports);
/* 335 */     moduleScope.put("module", (Scriptable)moduleScope, moduleObject);
/* 336 */     moduleObject.put("exports", (Scriptable)moduleObject, exports);
/* 337 */     install((Scriptable)moduleScope);
/* 338 */     if (isMain) {
/* 339 */       defineReadOnlyProperty((ScriptableObject)this, "main", moduleObject);
/*     */     }
/* 341 */     executeOptionalScript(this.preExec, cx, (Scriptable)moduleScope);
/* 342 */     moduleScript.getScript().exec(cx, (Scriptable)moduleScope);
/* 343 */     executeOptionalScript(this.postExec, cx, (Scriptable)moduleScope);
/* 344 */     return ScriptRuntime.toObject(cx, this.nativeScope, ScriptableObject.getProperty((Scriptable)moduleObject, "exports"));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static void executeOptionalScript(Script script, Context cx, Scriptable executionScope) {
/* 351 */     if (script != null) {
/* 352 */       script.exec(cx, executionScope);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   private static void defineReadOnlyProperty(ScriptableObject obj, String name, Object value) {
/* 358 */     ScriptableObject.putProperty((Scriptable)obj, name, value);
/* 359 */     obj.setAttributes(name, 5);
/*     */   }
/*     */ 
/*     */   
/*     */   private ModuleScript getModule(Context cx, String id, URI uri, URI base) {
/*     */     try {
/* 365 */       ModuleScript moduleScript = this.moduleScriptProvider.getModuleScript(cx, id, uri, base, this.paths);
/*     */       
/* 367 */       if (moduleScript == null) {
/* 368 */         throw ScriptRuntime.throwError(cx, this.nativeScope, "Module \"" + id + "\" not found.");
/*     */       }
/*     */       
/* 371 */       return moduleScript;
/*     */     }
/* 373 */     catch (RuntimeException e) {
/* 374 */       throw e;
/*     */     }
/* 376 */     catch (Exception e) {
/* 377 */       throw Context.throwAsScriptRuntimeEx(e);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public String getFunctionName() {
/* 383 */     return "require";
/*     */   }
/*     */ 
/*     */   
/*     */   public int getArity() {
/* 388 */     return 1;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getLength() {
/* 393 */     return 1;
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\mozilla\javascript\commonjs\module\Require.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */