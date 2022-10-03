/*    */ package org.springframework.objenesis.instantiator.basic;
/*    */ 
/*    */ import java.lang.reflect.Constructor;
/*    */ import org.springframework.objenesis.ObjenesisException;
/*    */ import org.springframework.objenesis.instantiator.ObjectInstantiator;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public abstract class DelegatingToExoticInstantiator<T>
/*    */   implements ObjectInstantiator<T>
/*    */ {
/*    */   private final ObjectInstantiator<T> wrapped;
/*    */   
/*    */   protected DelegatingToExoticInstantiator(String className, Class<T> type) {
/* 50 */     Class<ObjectInstantiator<T>> clazz = instantiatorClass(className);
/* 51 */     Constructor<ObjectInstantiator<T>> constructor = instantiatorConstructor(className, clazz);
/* 52 */     this.wrapped = instantiator(className, type, constructor);
/*    */   }
/*    */   
/*    */   private ObjectInstantiator<T> instantiator(String className, Class<T> type, Constructor<ObjectInstantiator<T>> constructor) {
/*    */     try {
/* 57 */       return constructor.newInstance(new Object[] { type });
/* 58 */     } catch (InstantiationException|IllegalAccessException|java.lang.reflect.InvocationTargetException e) {
/* 59 */       throw new RuntimeException("Failed to call constructor of " + className, e);
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   private Class<ObjectInstantiator<T>> instantiatorClass(String className) {
/*    */     try {
/* 66 */       Class<ObjectInstantiator<T>> clazz = (Class)Class.forName(className);
/* 67 */       return clazz;
/* 68 */     } catch (ClassNotFoundException e) {
/* 69 */       throw new ObjenesisException(getClass().getSimpleName() + " now requires objenesis-exotic to be in the classpath", e);
/*    */     } 
/*    */   }
/*    */   
/*    */   private Constructor<ObjectInstantiator<T>> instantiatorConstructor(String className, Class<ObjectInstantiator<T>> clazz) {
/*    */     try {
/* 75 */       return clazz.getConstructor(new Class[] { Class.class });
/* 76 */     } catch (NoSuchMethodException e) {
/* 77 */       throw new ObjenesisException("Try to find constructor taking a Class<T> in parameter on " + className + " but can't find it", e);
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   public T newInstance() {
/* 83 */     return (T)this.wrapped.newInstance();
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\springframework\objenesis\instantiator\basic\DelegatingToExoticInstantiator.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */