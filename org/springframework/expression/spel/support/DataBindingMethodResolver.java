/*    */ package org.springframework.expression.spel.support;
/*    */ 
/*    */ import java.lang.reflect.Method;
/*    */ import java.lang.reflect.Modifier;
/*    */ import java.util.List;
/*    */ import org.springframework.core.convert.TypeDescriptor;
/*    */ import org.springframework.expression.AccessException;
/*    */ import org.springframework.expression.EvaluationContext;
/*    */ import org.springframework.expression.MethodExecutor;
/*    */ import org.springframework.lang.Nullable;
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
/*    */ public final class DataBindingMethodResolver
/*    */   extends ReflectiveMethodResolver
/*    */ {
/*    */   @Nullable
/*    */   public MethodExecutor resolve(EvaluationContext context, Object targetObject, String name, List<TypeDescriptor> argumentTypes) throws AccessException {
/* 53 */     if (targetObject instanceof Class) {
/* 54 */       throw new IllegalArgumentException("DataBindingMethodResolver does not support Class targets");
/*    */     }
/* 56 */     return super.resolve(context, targetObject, name, argumentTypes);
/*    */   }
/*    */ 
/*    */   
/*    */   protected boolean isCandidateForInvocation(Method method, Class<?> targetClass) {
/* 61 */     if (Modifier.isStatic(method.getModifiers())) {
/* 62 */       return false;
/*    */     }
/* 64 */     Class<?> clazz = method.getDeclaringClass();
/* 65 */     return (clazz != Object.class && clazz != Class.class && !ClassLoader.class.isAssignableFrom(targetClass));
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static DataBindingMethodResolver forInstanceMethodInvocation() {
/* 73 */     return new DataBindingMethodResolver();
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\springframework\expression\spel\support\DataBindingMethodResolver.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */