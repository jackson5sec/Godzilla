/*     */ package org.springframework.core;
/*     */ 
/*     */ import java.lang.reflect.Method;
/*     */ import java.lang.reflect.Proxy;
/*     */ import java.util.Iterator;
/*     */ import java.util.LinkedHashMap;
/*     */ import java.util.LinkedHashSet;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import org.springframework.lang.Nullable;
/*     */ import org.springframework.util.ClassUtils;
/*     */ import org.springframework.util.ReflectionUtils;
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
/*     */ public final class MethodIntrospector
/*     */ {
/*     */   public static <T> Map<Method, T> selectMethods(Class<?> targetType, MetadataLookup<T> metadataLookup) {
/*  59 */     Map<Method, T> methodMap = new LinkedHashMap<>();
/*  60 */     Set<Class<?>> handlerTypes = new LinkedHashSet<>();
/*  61 */     Class<?> specificHandlerType = null;
/*     */     
/*  63 */     if (!Proxy.isProxyClass(targetType)) {
/*  64 */       specificHandlerType = ClassUtils.getUserClass(targetType);
/*  65 */       handlerTypes.add(specificHandlerType);
/*     */     } 
/*  67 */     handlerTypes.addAll(ClassUtils.getAllInterfacesForClassAsSet(targetType));
/*     */     
/*  69 */     for (Iterator<Class<?>> iterator = handlerTypes.iterator(); iterator.hasNext(); ) { Class<?> currentHandlerType = iterator.next();
/*  70 */       Class<?> targetClass = (specificHandlerType != null) ? specificHandlerType : currentHandlerType;
/*     */       
/*  72 */       ReflectionUtils.doWithMethods(currentHandlerType, method -> { Method specificMethod = ClassUtils.getMostSpecificMethod(method, targetClass); T result = metadataLookup.inspect(specificMethod); if (result != null) { Method bridgedMethod = BridgeMethodResolver.findBridgedMethod(specificMethod); if (bridgedMethod == specificMethod || metadataLookup.inspect(bridgedMethod) == null) methodMap.put(specificMethod, result);  }  }ReflectionUtils.USER_DECLARED_METHODS); }
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
/*  84 */     return methodMap;
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
/*     */   public static Set<Method> selectMethods(Class<?> targetType, ReflectionUtils.MethodFilter methodFilter) {
/*  96 */     return selectMethods(targetType, method -> methodFilter.matches(method) ? Boolean.TRUE : null)
/*  97 */       .keySet();
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
/*     */ 
/*     */   
/*     */   public static Method selectInvocableMethod(Method method, Class<?> targetType) {
/* 114 */     if (method.getDeclaringClass().isAssignableFrom(targetType)) {
/* 115 */       return method;
/*     */     }
/*     */     try {
/* 118 */       String methodName = method.getName();
/* 119 */       Class<?>[] parameterTypes = method.getParameterTypes();
/* 120 */       for (Class<?> ifc : targetType.getInterfaces()) {
/*     */         try {
/* 122 */           return ifc.getMethod(methodName, parameterTypes);
/*     */         }
/* 124 */         catch (NoSuchMethodException noSuchMethodException) {}
/*     */       } 
/*     */ 
/*     */ 
/*     */       
/* 129 */       return targetType.getMethod(methodName, parameterTypes);
/*     */     }
/* 131 */     catch (NoSuchMethodException ex) {
/* 132 */       throw new IllegalStateException(String.format("Need to invoke method '%s' declared on target class '%s', but not found in any interface(s) of the exposed proxy type. Either pull the method up to an interface or switch to CGLIB proxies by enforcing proxy-target-class mode in your configuration.", new Object[] { method
/*     */ 
/*     */ 
/*     */ 
/*     */               
/* 137 */               .getName(), method.getDeclaringClass().getSimpleName() }));
/*     */     } 
/*     */   }
/*     */   
/*     */   @FunctionalInterface
/*     */   public static interface MetadataLookup<T> {
/*     */     @Nullable
/*     */     T inspect(Method param1Method);
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\springframework\core\MethodIntrospector.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */