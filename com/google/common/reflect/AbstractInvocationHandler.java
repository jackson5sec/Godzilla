/*     */ package com.google.common.reflect;
/*     */ 
/*     */ import com.google.common.annotations.Beta;
/*     */ import java.lang.reflect.InvocationHandler;
/*     */ import java.lang.reflect.Method;
/*     */ import java.lang.reflect.Proxy;
/*     */ import java.util.Arrays;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @Beta
/*     */ public abstract class AbstractInvocationHandler
/*     */   implements InvocationHandler
/*     */ {
/*  44 */   private static final Object[] NO_ARGS = new Object[0];
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
/*  64 */     if (args == null) {
/*  65 */       args = NO_ARGS;
/*     */     }
/*  67 */     if (args.length == 0 && method.getName().equals("hashCode")) {
/*  68 */       return Integer.valueOf(hashCode());
/*     */     }
/*  70 */     if (args.length == 1 && method
/*  71 */       .getName().equals("equals") && method
/*  72 */       .getParameterTypes()[0] == Object.class) {
/*  73 */       Object arg = args[0];
/*  74 */       if (arg == null) {
/*  75 */         return Boolean.valueOf(false);
/*     */       }
/*  77 */       if (proxy == arg) {
/*  78 */         return Boolean.valueOf(true);
/*     */       }
/*  80 */       return Boolean.valueOf((isProxyOfSameInterfaces(arg, proxy.getClass()) && 
/*  81 */           equals(Proxy.getInvocationHandler(arg))));
/*     */     } 
/*  83 */     if (args.length == 0 && method.getName().equals("toString")) {
/*  84 */       return toString();
/*     */     }
/*  86 */     return handleInvocation(proxy, method, args);
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
/*     */   protected abstract Object handleInvocation(Object paramObject, Method paramMethod, Object[] paramArrayOfObject) throws Throwable;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean equals(Object obj) {
/* 113 */     return super.equals(obj);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 122 */     return super.hashCode();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String toString() {
/* 132 */     return super.toString();
/*     */   }
/*     */   
/*     */   private static boolean isProxyOfSameInterfaces(Object arg, Class<?> proxyClass) {
/* 136 */     return (proxyClass.isInstance(arg) || (
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 142 */       Proxy.isProxyClass(arg.getClass()) && 
/* 143 */       Arrays.equals((Object[])arg.getClass().getInterfaces(), (Object[])proxyClass.getInterfaces())));
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\google\common\reflect\AbstractInvocationHandler.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */