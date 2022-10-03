/*    */ package com.google.common.reflect;
/*    */ 
/*    */ import com.google.common.annotations.Beta;
/*    */ import com.google.common.base.Preconditions;
/*    */ import java.lang.reflect.InvocationHandler;
/*    */ import java.lang.reflect.Proxy;
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
/*    */ @Beta
/*    */ public final class Reflection
/*    */ {
/*    */   public static String getPackageName(Class<?> clazz) {
/* 38 */     return getPackageName(clazz.getName());
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static String getPackageName(String classFullName) {
/* 47 */     int lastDot = classFullName.lastIndexOf('.');
/* 48 */     return (lastDot < 0) ? "" : classFullName.substring(0, lastDot);
/*    */   }
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
/*    */   public static void initialize(Class<?>... classes) {
/* 63 */     for (Class<?> clazz : classes) {
/*    */       try {
/* 65 */         Class.forName(clazz.getName(), true, clazz.getClassLoader());
/* 66 */       } catch (ClassNotFoundException e) {
/* 67 */         throw new AssertionError(e);
/*    */       } 
/*    */     } 
/*    */   }
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
/*    */   public static <T> T newProxy(Class<T> interfaceType, InvocationHandler handler) {
/* 82 */     Preconditions.checkNotNull(handler);
/* 83 */     Preconditions.checkArgument(interfaceType.isInterface(), "%s is not an interface", interfaceType);
/*    */     
/* 85 */     Object object = Proxy.newProxyInstance(interfaceType
/* 86 */         .getClassLoader(), new Class[] { interfaceType }, handler);
/* 87 */     return interfaceType.cast(object);
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\google\common\reflect\Reflection.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */