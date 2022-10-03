/*     */ package org.mozilla.javascript.jdk13;
/*     */ 
/*     */ import java.lang.reflect.AccessibleObject;
/*     */ import java.lang.reflect.Constructor;
/*     */ import java.lang.reflect.InvocationHandler;
/*     */ import java.lang.reflect.InvocationTargetException;
/*     */ import java.lang.reflect.Member;
/*     */ import java.lang.reflect.Method;
/*     */ import java.lang.reflect.Proxy;
/*     */ import org.mozilla.javascript.Context;
/*     */ import org.mozilla.javascript.ContextFactory;
/*     */ import org.mozilla.javascript.InterfaceAdapter;
/*     */ import org.mozilla.javascript.Kit;
/*     */ import org.mozilla.javascript.Scriptable;
/*     */ import org.mozilla.javascript.VMBridge;
/*     */ 
/*     */ 
/*     */ public class VMBridge_jdk13
/*     */   extends VMBridge
/*     */ {
/*  21 */   private ThreadLocal<Object[]> contextLocal = new ThreadLocal();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected Object getThreadContextHelper() {
/*  35 */     Object[] storage = this.contextLocal.get();
/*  36 */     if (storage == null) {
/*  37 */       storage = new Object[1];
/*  38 */       this.contextLocal.set(storage);
/*     */     } 
/*  40 */     return storage;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected Context getContext(Object contextHelper) {
/*  46 */     Object[] storage = (Object[])contextHelper;
/*  47 */     return (Context)storage[0];
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected void setContext(Object contextHelper, Context cx) {
/*  53 */     Object[] storage = (Object[])contextHelper;
/*  54 */     storage[0] = cx;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected ClassLoader getCurrentThreadClassLoader() {
/*  60 */     return Thread.currentThread().getContextClassLoader();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected boolean tryToMakeAccessible(Object accessibleObject) {
/*  66 */     if (!(accessibleObject instanceof AccessibleObject)) {
/*  67 */       return false;
/*     */     }
/*  69 */     AccessibleObject accessible = (AccessibleObject)accessibleObject;
/*  70 */     if (accessible.isAccessible()) {
/*  71 */       return true;
/*     */     }
/*     */     try {
/*  74 */       accessible.setAccessible(true);
/*  75 */     } catch (Exception ex) {}
/*     */     
/*  77 */     return accessible.isAccessible();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected Object getInterfaceProxyHelper(ContextFactory cf, Class<?>[] interfaces) {
/*     */     Constructor<?> c;
/*  86 */     ClassLoader loader = interfaces[0].getClassLoader();
/*  87 */     Class<?> cl = Proxy.getProxyClass(loader, interfaces);
/*     */     
/*     */     try {
/*  90 */       c = cl.getConstructor(new Class[] { InvocationHandler.class });
/*  91 */     } catch (NoSuchMethodException ex) {
/*     */       
/*  93 */       throw Kit.initCause(new IllegalStateException(), ex);
/*     */     } 
/*  95 */     return c;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected Object newInterfaceProxy(Object proxyHelper, final ContextFactory cf, final InterfaceAdapter adapter, final Object target, final Scriptable topScope) {
/*     */     Object proxy;
/* 105 */     Constructor<?> c = (Constructor)proxyHelper;
/*     */     
/* 107 */     InvocationHandler handler = new InvocationHandler()
/*     */       {
/*     */ 
/*     */ 
/*     */ 
/*     */         
/*     */         public Object invoke(Object proxy, Method method, Object[] args)
/*     */         {
/* 115 */           if (method.getDeclaringClass() == Object.class) {
/* 116 */             String methodName = method.getName();
/* 117 */             if (methodName.equals("equals")) {
/* 118 */               Object other = args[0];
/*     */ 
/*     */ 
/*     */ 
/*     */               
/* 123 */               return Boolean.valueOf((proxy == other));
/*     */             } 
/* 125 */             if (methodName.equals("hashCode")) {
/* 126 */               return Integer.valueOf(target.hashCode());
/*     */             }
/* 128 */             if (methodName.equals("toString")) {
/* 129 */               return "Proxy[" + target.toString() + "]";
/*     */             }
/*     */           } 
/* 132 */           return adapter.invoke(cf, target, topScope, proxy, method, args);
/*     */         }
/*     */       };
/*     */     
/*     */     try {
/* 137 */       proxy = c.newInstance(new Object[] { handler });
/* 138 */     } catch (InvocationTargetException ex) {
/* 139 */       throw Context.throwAsScriptRuntimeEx(ex);
/* 140 */     } catch (IllegalAccessException ex) {
/*     */       
/* 142 */       throw Kit.initCause(new IllegalStateException(), ex);
/* 143 */     } catch (InstantiationException ex) {
/*     */       
/* 145 */       throw Kit.initCause(new IllegalStateException(), ex);
/*     */     } 
/* 147 */     return proxy;
/*     */   }
/*     */ 
/*     */   
/*     */   protected boolean isVarArgs(Member member) {
/* 152 */     return false;
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\mozilla\javascript\jdk13\VMBridge_jdk13.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */