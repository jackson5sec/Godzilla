/*    */ package org.springframework.objenesis.instantiator.sun;
/*    */ 
/*    */ import java.lang.reflect.Constructor;
/*    */ import java.lang.reflect.Method;
/*    */ import org.springframework.objenesis.ObjenesisException;
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
/*    */ class SunReflectionFactoryHelper
/*    */ {
/*    */   public static <T> Constructor<T> newConstructorForSerialization(Class<T> type, Constructor<?> constructor) {
/* 37 */     Class<?> reflectionFactoryClass = getReflectionFactoryClass();
/* 38 */     Object reflectionFactory = createReflectionFactory(reflectionFactoryClass);
/*    */     
/* 40 */     Method newConstructorForSerializationMethod = getNewConstructorForSerializationMethod(reflectionFactoryClass);
/*    */ 
/*    */     
/*    */     try {
/* 44 */       return (Constructor<T>)newConstructorForSerializationMethod.invoke(reflectionFactory, new Object[] { type, constructor });
/*    */     
/*    */     }
/* 47 */     catch (IllegalArgumentException|IllegalAccessException|java.lang.reflect.InvocationTargetException e) {
/* 48 */       throw new ObjenesisException(e);
/*    */     } 
/*    */   }
/*    */   
/*    */   private static Class<?> getReflectionFactoryClass() {
/*    */     try {
/* 54 */       return Class.forName("sun.reflect.ReflectionFactory");
/*    */     }
/* 56 */     catch (ClassNotFoundException e) {
/* 57 */       throw new ObjenesisException(e);
/*    */     } 
/*    */   }
/*    */   
/*    */   private static Object createReflectionFactory(Class<?> reflectionFactoryClass) {
/*    */     try {
/* 63 */       Method method = reflectionFactoryClass.getDeclaredMethod("getReflectionFactory", new Class[0]);
/*    */       
/* 65 */       return method.invoke(null, new Object[0]);
/*    */     }
/* 67 */     catch (NoSuchMethodException|IllegalAccessException|java.lang.reflect.InvocationTargetException|IllegalArgumentException e) {
/* 68 */       throw new ObjenesisException(e);
/*    */     } 
/*    */   }
/*    */   
/*    */   private static Method getNewConstructorForSerializationMethod(Class<?> reflectionFactoryClass) {
/*    */     try {
/* 74 */       return reflectionFactoryClass.getDeclaredMethod("newConstructorForSerialization", new Class[] { Class.class, Constructor.class });
/*    */     
/*    */     }
/* 77 */     catch (NoSuchMethodException e) {
/* 78 */       throw new ObjenesisException(e);
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\springframework\objenesis\instantiator\sun\SunReflectionFactoryHelper.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */