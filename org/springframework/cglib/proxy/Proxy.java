/*    */ package org.springframework.cglib.proxy;
/*    */ 
/*    */ import java.io.Serializable;
/*    */ import java.lang.reflect.Method;
/*    */ import org.springframework.cglib.core.CodeGenerationException;
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
/*    */ public class Proxy
/*    */   implements Serializable
/*    */ {
/*    */   protected InvocationHandler h;
/*    */   
/* 41 */   private static final CallbackFilter BAD_OBJECT_METHOD_FILTER = new CallbackFilter() {
/*    */       public int accept(Method method) {
/* 43 */         if (method.getDeclaringClass().getName().equals("java.lang.Object")) {
/* 44 */           String name = method.getName();
/* 45 */           if (!name.equals("hashCode") && 
/* 46 */             !name.equals("equals") && 
/* 47 */             !name.equals("toString")) {
/* 48 */             return 1;
/*    */           }
/*    */         } 
/* 51 */         return 0;
/*    */       }
/*    */     };
/*    */   
/*    */   protected Proxy(InvocationHandler h) {
/* 56 */     Enhancer.registerCallbacks(getClass(), new Callback[] { h, null });
/* 57 */     this.h = h;
/*    */   }
/*    */   
/*    */   private static class ProxyImpl
/*    */     extends Proxy {
/*    */     protected ProxyImpl(InvocationHandler h) {
/* 63 */       super(h);
/*    */     }
/*    */   }
/*    */   
/*    */   public static InvocationHandler getInvocationHandler(Object proxy) {
/* 68 */     if (!(proxy instanceof ProxyImpl)) {
/* 69 */       throw new IllegalArgumentException("Object is not a proxy");
/*    */     }
/* 71 */     return ((Proxy)proxy).h;
/*    */   }
/*    */   
/*    */   public static Class getProxyClass(ClassLoader loader, Class[] interfaces) {
/* 75 */     Enhancer e = new Enhancer();
/* 76 */     e.setSuperclass(ProxyImpl.class);
/* 77 */     e.setInterfaces(interfaces);
/* 78 */     e.setCallbackTypes(new Class[] { InvocationHandler.class, NoOp.class });
/*    */ 
/*    */ 
/*    */     
/* 82 */     e.setCallbackFilter(BAD_OBJECT_METHOD_FILTER);
/* 83 */     e.setUseFactory(false);
/* 84 */     return e.createClass();
/*    */   }
/*    */   
/*    */   public static boolean isProxyClass(Class cl) {
/* 88 */     return cl.getSuperclass().equals(ProxyImpl.class);
/*    */   }
/*    */   
/*    */   public static Object newProxyInstance(ClassLoader loader, Class[] interfaces, InvocationHandler h) {
/*    */     try {
/* 93 */       Class clazz = getProxyClass(loader, interfaces);
/* 94 */       return clazz.getConstructor(new Class[] { InvocationHandler.class }).newInstance(new Object[] { h });
/* 95 */     } catch (RuntimeException e) {
/* 96 */       throw e;
/* 97 */     } catch (Exception e) {
/* 98 */       throw new CodeGenerationException(e);
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\springframework\cglib\proxy\Proxy.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */