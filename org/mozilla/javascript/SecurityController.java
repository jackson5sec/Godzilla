/*     */ package org.mozilla.javascript;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class SecurityController
/*     */ {
/*     */   private static SecurityController global;
/*     */   
/*     */   static SecurityController global() {
/*  42 */     return global;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static boolean hasGlobal() {
/*  51 */     return (global != null);
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
/*     */   public static void initGlobal(SecurityController controller) {
/*  68 */     if (controller == null) throw new IllegalArgumentException(); 
/*  69 */     if (global != null) {
/*  70 */       throw new SecurityException("Cannot overwrite already installed global SecurityController");
/*     */     }
/*  72 */     global = controller;
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
/*     */   public abstract GeneratedClassLoader createClassLoader(ClassLoader paramClassLoader, Object paramObject);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static GeneratedClassLoader createLoader(ClassLoader parent, Object staticDomain) {
/*     */     GeneratedClassLoader loader;
/* 103 */     Context cx = Context.getContext();
/* 104 */     if (parent == null) {
/* 105 */       parent = cx.getApplicationClassLoader();
/*     */     }
/* 107 */     SecurityController sc = cx.getSecurityController();
/*     */     
/* 109 */     if (sc == null) {
/* 110 */       loader = cx.createClassLoader(parent);
/*     */     } else {
/* 112 */       Object dynamicDomain = sc.getDynamicSecurityDomain(staticDomain);
/* 113 */       loader = sc.createClassLoader(parent, dynamicDomain);
/*     */     } 
/* 115 */     return loader;
/*     */   }
/*     */   
/*     */   public static Class<?> getStaticSecurityDomainClass() {
/* 119 */     SecurityController sc = Context.getContext().getSecurityController();
/* 120 */     return (sc == null) ? null : sc.getStaticSecurityDomainClassInternal();
/*     */   }
/*     */ 
/*     */   
/*     */   public Class<?> getStaticSecurityDomainClassInternal() {
/* 125 */     return null;
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
/*     */   public abstract Object getDynamicSecurityDomain(Object paramObject);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Object callWithDomain(Object securityDomain, Context cx, final Callable callable, Scriptable scope, final Scriptable thisObj, final Object[] args) {
/* 156 */     return execWithDomain(cx, scope, new Script()
/*     */         {
/*     */           public Object exec(Context cx, Scriptable scope)
/*     */           {
/* 160 */             return callable.call(cx, scope, thisObj, args);
/*     */           }
/*     */         }securityDomain);
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
/*     */   public Object execWithDomain(Context cx, Scriptable scope, Script script, Object securityDomain) {
/* 175 */     throw new IllegalStateException("callWithDomain should be overridden");
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\mozilla\javascript\SecurityController.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */