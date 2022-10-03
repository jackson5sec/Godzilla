/*     */ package org.mozilla.javascript;
/*     */ 
/*     */ import java.lang.reflect.Member;
/*     */ import java.util.Iterator;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class VMBridge
/*     */ {
/*  18 */   static final VMBridge instance = makeInstance();
/*     */ 
/*     */   
/*     */   private static VMBridge makeInstance() {
/*  22 */     String[] classNames = { "org.mozilla.javascript.VMBridge_custom", "org.mozilla.javascript.jdk15.VMBridge_jdk15", "org.mozilla.javascript.jdk13.VMBridge_jdk13", "org.mozilla.javascript.jdk11.VMBridge_jdk11" };
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  28 */     for (int i = 0; i != classNames.length; i++) {
/*  29 */       String className = classNames[i];
/*  30 */       Class<?> cl = Kit.classOrNull(className);
/*  31 */       if (cl != null) {
/*  32 */         VMBridge bridge = (VMBridge)Kit.newInstanceOrNull(cl);
/*  33 */         if (bridge != null) {
/*  34 */           return bridge;
/*     */         }
/*     */       } 
/*     */     } 
/*  38 */     throw new IllegalStateException("Failed to create VMBridge instance");
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
/*     */   protected abstract Object getThreadContextHelper();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected abstract Context getContext(Object paramObject);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected abstract void setContext(Object paramObject, Context paramContext);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected abstract ClassLoader getCurrentThreadClassLoader();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected abstract boolean tryToMakeAccessible(Object paramObject);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected Object getInterfaceProxyHelper(ContextFactory cf, Class<?>[] interfaces) {
/* 102 */     throw Context.reportRuntimeError("VMBridge.getInterfaceProxyHelper is not supported");
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
/*     */   protected Object newInterfaceProxy(Object proxyHelper, ContextFactory cf, InterfaceAdapter adapter, Object target, Scriptable topScope) {
/* 122 */     throw Context.reportRuntimeError("VMBridge.newInterfaceProxy is not supported");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected abstract boolean isVarArgs(Member paramMember);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Iterator<?> getJavaIterator(Context cx, Scriptable scope, Object obj) {
/* 140 */     if (obj instanceof Wrapper) {
/* 141 */       Object unwrapped = ((Wrapper)obj).unwrap();
/* 142 */       Iterator<?> iterator = null;
/* 143 */       if (unwrapped instanceof Iterator)
/* 144 */         iterator = (Iterator)unwrapped; 
/* 145 */       return iterator;
/*     */     } 
/* 147 */     return null;
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\mozilla\javascript\VMBridge.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */