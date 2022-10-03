/*     */ package org.mozilla.javascript;
/*     */ 
/*     */ import java.lang.reflect.Method;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class InterfaceAdapter
/*     */ {
/*     */   private final Object proxyHelper;
/*     */   
/*     */   static Object create(Context cx, Class<?> cl, ScriptableObject object) {
/*  29 */     if (!cl.isInterface()) throw new IllegalArgumentException();
/*     */     
/*  31 */     Scriptable topScope = ScriptRuntime.getTopCallScope(cx);
/*  32 */     ClassCache cache = ClassCache.get(topScope);
/*     */     
/*  34 */     InterfaceAdapter adapter = (InterfaceAdapter)cache.getInterfaceAdapter(cl);
/*  35 */     ContextFactory cf = cx.getFactory();
/*  36 */     if (adapter == null) {
/*  37 */       Method[] methods = cl.getMethods();
/*  38 */       if (object instanceof Callable) {
/*     */ 
/*     */ 
/*     */ 
/*     */         
/*  43 */         int length = methods.length;
/*  44 */         if (length == 0) {
/*  45 */           throw Context.reportRuntimeError1("msg.no.empty.interface.conversion", cl.getName());
/*     */         }
/*     */         
/*  48 */         if (length > 1) {
/*  49 */           String methodName = methods[0].getName();
/*  50 */           for (int i = 1; i < length; i++) {
/*  51 */             if (!methodName.equals(methods[i].getName())) {
/*  52 */               throw Context.reportRuntimeError1("msg.no.function.interface.conversion", cl.getName());
/*     */             }
/*     */           } 
/*     */         } 
/*     */       } 
/*     */ 
/*     */       
/*  59 */       adapter = new InterfaceAdapter(cf, cl);
/*  60 */       cache.cacheInterfaceAdapter(cl, adapter);
/*     */     } 
/*  62 */     return VMBridge.instance.newInterfaceProxy(adapter.proxyHelper, cf, adapter, object, topScope);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private InterfaceAdapter(ContextFactory cf, Class<?> cl) {
/*  68 */     this.proxyHelper = VMBridge.instance.getInterfaceProxyHelper(cf, new Class[] { cl });
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
/*     */   public Object invoke(ContextFactory cf, final Object target, final Scriptable topScope, final Object thisObject, final Method method, final Object[] args) {
/*  80 */     ContextAction action = new ContextAction()
/*     */       {
/*     */         public Object run(Context cx) {
/*  83 */           return InterfaceAdapter.this.invokeImpl(cx, target, topScope, thisObject, method, args);
/*     */         }
/*     */       };
/*  86 */     return cf.call(action);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   Object invokeImpl(Context cx, Object target, Scriptable topScope, Object thisObject, Method method, Object[] args) {
/*     */     Callable function;
/*  97 */     if (target instanceof Callable) {
/*  98 */       function = (Callable)target;
/*     */     } else {
/* 100 */       Scriptable s = (Scriptable)target;
/* 101 */       String methodName = method.getName();
/* 102 */       Object value = ScriptableObject.getProperty(s, methodName);
/* 103 */       if (value == ScriptableObject.NOT_FOUND) {
/*     */ 
/*     */ 
/*     */         
/* 107 */         Context.reportWarning(ScriptRuntime.getMessage1("msg.undefined.function.interface", methodName));
/*     */         
/* 109 */         Class<?> resultType = method.getReturnType();
/* 110 */         if (resultType == void.class) {
/* 111 */           return null;
/*     */         }
/* 113 */         return Context.jsToJava(null, resultType);
/*     */       } 
/*     */       
/* 116 */       if (!(value instanceof Callable)) {
/* 117 */         throw Context.reportRuntimeError1("msg.not.function.interface", methodName);
/*     */       }
/*     */       
/* 120 */       function = (Callable)value;
/*     */     } 
/* 122 */     WrapFactory wf = cx.getWrapFactory();
/* 123 */     if (args == null) {
/* 124 */       args = ScriptRuntime.emptyArgs;
/*     */     } else {
/* 126 */       for (int i = 0, N = args.length; i != N; i++) {
/* 127 */         Object arg = args[i];
/*     */         
/* 129 */         if (!(arg instanceof String) && !(arg instanceof Number) && !(arg instanceof Boolean))
/*     */         {
/* 131 */           args[i] = wf.wrap(cx, topScope, arg, null);
/*     */         }
/*     */       } 
/*     */     } 
/* 135 */     Scriptable thisObj = wf.wrapAsJavaObject(cx, topScope, thisObject, null);
/*     */     
/* 137 */     Object result = function.call(cx, topScope, thisObj, args);
/* 138 */     Class<?> javaResultType = method.getReturnType();
/* 139 */     if (javaResultType == void.class) {
/* 140 */       result = null;
/*     */     } else {
/* 142 */       result = Context.jsToJava(result, javaResultType);
/*     */     } 
/* 144 */     return result;
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\mozilla\javascript\InterfaceAdapter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */