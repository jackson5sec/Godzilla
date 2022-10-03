/*     */ package org.mozilla.javascript;
/*     */ 
/*     */ import java.security.AccessController;
/*     */ import java.security.PrivilegedAction;
/*     */ import org.mozilla.javascript.xml.XMLLib;
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
/*     */ public class ContextFactory
/*     */ {
/*     */   private static volatile boolean hasCustomGlobal;
/* 113 */   private static ContextFactory global = new ContextFactory();
/*     */   
/*     */   private volatile boolean sealed;
/*     */   
/* 117 */   private final Object listenersLock = new Object();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private volatile Object listeners;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean disabledListening;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private ClassLoader applicationClassLoader;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static ContextFactory getGlobal() {
/* 147 */     return global;
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
/*     */   public static boolean hasExplicitGlobal() {
/* 161 */     return hasCustomGlobal;
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
/*     */   public static synchronized void initGlobal(ContextFactory factory) {
/* 173 */     if (factory == null) {
/* 174 */       throw new IllegalArgumentException();
/*     */     }
/* 176 */     if (hasCustomGlobal) {
/* 177 */       throw new IllegalStateException();
/*     */     }
/* 179 */     hasCustomGlobal = true;
/* 180 */     global = factory;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static synchronized GlobalSetter getGlobalSetter() {
/* 189 */     if (hasCustomGlobal) {
/* 190 */       throw new IllegalStateException();
/*     */     }
/* 192 */     hasCustomGlobal = true;
/*     */     class GlobalSetterImpl implements GlobalSetter {
/*     */       public void setContextFactoryGlobal(ContextFactory factory) {
/* 195 */         ContextFactory.global = (factory == null) ? new ContextFactory() : factory;
/*     */       }
/*     */       public ContextFactory getContextFactoryGlobal() {
/* 198 */         return ContextFactory.global;
/*     */       }
/*     */     };
/* 201 */     return new GlobalSetterImpl();
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
/*     */   protected Context makeContext() {
/* 215 */     return new Context(this);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected boolean hasFeature(Context cx, int featureIndex) {
/*     */     int version;
/* 226 */     switch (featureIndex) {
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
/*     */       case 1:
/* 239 */         version = cx.getLanguageVersion();
/* 240 */         return (version == 100 || version == 110 || version == 120);
/*     */ 
/*     */ 
/*     */       
/*     */       case 2:
/* 245 */         return false;
/*     */       
/*     */       case 3:
/* 248 */         return true;
/*     */       
/*     */       case 4:
/* 251 */         version = cx.getLanguageVersion();
/* 252 */         return (version == 120);
/*     */       
/*     */       case 5:
/* 255 */         return true;
/*     */       
/*     */       case 6:
/* 258 */         version = cx.getLanguageVersion();
/* 259 */         return (version == 0 || version >= 160);
/*     */ 
/*     */       
/*     */       case 7:
/* 263 */         return false;
/*     */       
/*     */       case 8:
/* 266 */         return false;
/*     */       
/*     */       case 9:
/* 269 */         return false;
/*     */       
/*     */       case 10:
/* 272 */         return false;
/*     */       
/*     */       case 11:
/* 275 */         return false;
/*     */       
/*     */       case 12:
/* 278 */         return false;
/*     */       
/*     */       case 13:
/* 281 */         return false;
/*     */       
/*     */       case 14:
/* 284 */         return true;
/*     */     } 
/*     */     
/* 287 */     throw new IllegalArgumentException(String.valueOf(featureIndex));
/*     */   }
/*     */   
/*     */   private boolean isDom3Present() {
/* 291 */     Class<?> nodeClass = Kit.classOrNull("org.w3c.dom.Node");
/* 292 */     if (nodeClass == null) return false;
/*     */ 
/*     */     
/*     */     try {
/* 296 */       nodeClass.getMethod("getUserData", new Class[] { String.class });
/* 297 */       return true;
/* 298 */     } catch (NoSuchMethodException e) {
/* 299 */       return false;
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
/*     */ 
/*     */ 
/*     */   
/*     */   protected XMLLib.Factory getE4xImplementationFactory() {
/* 323 */     if (isDom3Present()) {
/* 324 */       return XMLLib.Factory.create("org.mozilla.javascript.xmlimpl.XMLLibImpl");
/*     */     }
/*     */ 
/*     */     
/* 328 */     return null;
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
/*     */   protected GeneratedClassLoader createClassLoader(final ClassLoader parent) {
/* 343 */     return AccessController.<GeneratedClassLoader>doPrivileged((PrivilegedAction)new PrivilegedAction<DefiningClassLoader>() {
/*     */           public DefiningClassLoader run() {
/* 345 */             return new DefiningClassLoader(parent);
/*     */           }
/*     */         });
/*     */   }
/*     */   
/*     */   public static interface GlobalSetter
/*     */   {
/*     */     void setContextFactoryGlobal(ContextFactory param1ContextFactory);
/*     */     
/*     */     ContextFactory getContextFactoryGlobal();
/*     */   }
/*     */   
/*     */   public final ClassLoader getApplicationClassLoader() {
/* 358 */     return this.applicationClassLoader;
/*     */   }
/*     */   
/*     */   public static interface Listener {
/*     */     void contextCreated(Context param1Context);
/*     */     
/*     */     void contextReleased(Context param1Context);
/*     */   }
/*     */   
/*     */   public final void initApplicationClassLoader(ClassLoader loader) {
/* 368 */     if (loader == null)
/* 369 */       throw new IllegalArgumentException("loader is null"); 
/* 370 */     if (!Kit.testIfCanLoadRhinoClasses(loader)) {
/* 371 */       throw new IllegalArgumentException("Loader can not resolve Rhino classes");
/*     */     }
/*     */     
/* 374 */     if (this.applicationClassLoader != null) {
/* 375 */       throw new IllegalStateException("applicationClassLoader can only be set once");
/*     */     }
/* 377 */     checkNotSealed();
/*     */     
/* 379 */     this.applicationClassLoader = loader;
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
/*     */   protected Object doTopCall(Callable callable, Context cx, Scriptable scope, Scriptable thisObj, Object[] args) {
/* 393 */     Object result = callable.call(cx, scope, thisObj, args);
/* 394 */     return (result instanceof ConsString) ? result.toString() : result;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void observeInstructionCount(Context cx, int instructionCount) {}
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void onContextCreated(Context cx) {
/* 408 */     Object listeners = this.listeners;
/* 409 */     for (int i = 0;; i++) {
/* 410 */       Listener l = (Listener)Kit.getListener(listeners, i);
/* 411 */       if (l == null)
/*     */         break; 
/* 413 */       l.contextCreated(cx);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   protected void onContextReleased(Context cx) {
/* 419 */     Object listeners = this.listeners;
/* 420 */     for (int i = 0;; i++) {
/* 421 */       Listener l = (Listener)Kit.getListener(listeners, i);
/* 422 */       if (l == null)
/*     */         break; 
/* 424 */       l.contextReleased(cx);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public final void addListener(Listener listener) {
/* 430 */     checkNotSealed();
/* 431 */     synchronized (this.listenersLock) {
/* 432 */       if (this.disabledListening) {
/* 433 */         throw new IllegalStateException();
/*     */       }
/* 435 */       this.listeners = Kit.addListener(this.listeners, listener);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public final void removeListener(Listener listener) {
/* 441 */     checkNotSealed();
/* 442 */     synchronized (this.listenersLock) {
/* 443 */       if (this.disabledListening) {
/* 444 */         throw new IllegalStateException();
/*     */       }
/* 446 */       this.listeners = Kit.removeListener(this.listeners, listener);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   final void disableContextListening() {
/* 456 */     checkNotSealed();
/* 457 */     synchronized (this.listenersLock) {
/* 458 */       this.disabledListening = true;
/* 459 */       this.listeners = null;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final boolean isSealed() {
/* 469 */     return this.sealed;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final void seal() {
/* 479 */     checkNotSealed();
/* 480 */     this.sealed = true;
/*     */   }
/*     */ 
/*     */   
/*     */   protected final void checkNotSealed() {
/* 485 */     if (this.sealed) throw new IllegalStateException();
/*     */   
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
/*     */   public final Object call(ContextAction action) {
/* 503 */     return Context.call(this, action);
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
/*     */   public Context enterContext() {
/* 547 */     return enterContext(null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   public final Context enter() {
/* 557 */     return enterContext(null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   public final void exit() {
/* 566 */     Context.exit();
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
/*     */   public final Context enterContext(Context cx) {
/* 586 */     return Context.enter(cx, this);
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\mozilla\javascript\ContextFactory.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */