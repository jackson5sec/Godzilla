/*    */ package org.springframework.objenesis.instantiator.util;
/*    */ 
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
/*    */ 
/*    */ 
/*    */ public final class ClassUtils
/*    */ {
/*    */   public static String classNameToInternalClassName(String className) {
/* 37 */     return className.replace('.', '/');
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static String classNameToResource(String className) {
/* 48 */     return classNameToInternalClassName(className) + ".class";
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
/*    */   public static <T> Class<T> getExistingClass(ClassLoader classLoader, String className) {
/*    */     try {
/* 62 */       return (Class)Class.forName(className, true, classLoader);
/*    */     }
/* 64 */     catch (ClassNotFoundException e) {
/* 65 */       return null;
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   public static <T> T newInstance(Class<T> clazz) {
/*    */     try {
/* 72 */       return clazz.newInstance();
/* 73 */     } catch (InstantiationException|IllegalAccessException e) {
/* 74 */       throw new ObjenesisException(e);
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\springframework\objenesis\instantiato\\util\ClassUtils.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */