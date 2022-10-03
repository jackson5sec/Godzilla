/*    */ package org.springframework.core;
/*    */ 
/*    */ import java.lang.annotation.Annotation;
/*    */ import java.lang.reflect.Method;
/*    */ import org.springframework.lang.Nullable;
/*    */ import org.springframework.util.ClassUtils;
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
/*    */ public abstract class KotlinDetector
/*    */ {
/*    */   @Nullable
/*    */   private static final Class<? extends Annotation> kotlinMetadata;
/*    */   private static final boolean kotlinReflectPresent;
/*    */   
/*    */   static {
/*    */     Class<?> metadata;
/* 42 */     ClassLoader classLoader = KotlinDetector.class.getClassLoader();
/*    */     try {
/* 44 */       metadata = ClassUtils.forName("kotlin.Metadata", classLoader);
/*    */     }
/* 46 */     catch (ClassNotFoundException ex) {
/*    */       
/* 48 */       metadata = null;
/*    */     } 
/* 50 */     kotlinMetadata = (Class)metadata;
/* 51 */     kotlinReflectPresent = ClassUtils.isPresent("kotlin.reflect.full.KClasses", classLoader);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static boolean isKotlinPresent() {
/* 59 */     return (kotlinMetadata != null);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static boolean isKotlinReflectPresent() {
/* 67 */     return kotlinReflectPresent;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static boolean isKotlinType(Class<?> clazz) {
/* 75 */     return (kotlinMetadata != null && clazz.getDeclaredAnnotation(kotlinMetadata) != null);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static boolean isSuspendingFunction(Method method) {
/* 83 */     if (isKotlinType(method.getDeclaringClass())) {
/* 84 */       Class<?>[] types = method.getParameterTypes();
/* 85 */       if (types.length > 0 && "kotlin.coroutines.Continuation".equals(types[types.length - 1].getName())) {
/* 86 */         return true;
/*    */       }
/*    */     } 
/* 89 */     return false;
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\springframework\core\KotlinDetector.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */