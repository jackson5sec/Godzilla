/*     */ package org.springframework.core;
/*     */ 
/*     */ import java.lang.reflect.Method;
/*     */ import java.lang.reflect.Type;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import org.springframework.lang.Nullable;
/*     */ import org.springframework.util.ClassUtils;
/*     */ import org.springframework.util.ConcurrentReferenceHashMap;
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
/*     */ public final class BridgeMethodResolver
/*     */ {
/*  53 */   private static final Map<Method, Method> cache = (Map<Method, Method>)new ConcurrentReferenceHashMap();
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
/*     */   public static Method findBridgedMethod(Method bridgeMethod) {
/*  69 */     if (!bridgeMethod.isBridge()) {
/*  70 */       return bridgeMethod;
/*     */     }
/*  72 */     Method bridgedMethod = cache.get(bridgeMethod);
/*  73 */     if (bridgedMethod == null) {
/*     */       
/*  75 */       List<Method> candidateMethods = new ArrayList<>();
/*  76 */       ReflectionUtils.MethodFilter filter = candidateMethod -> isBridgedCandidateFor(candidateMethod, bridgeMethod);
/*     */       
/*  78 */       ReflectionUtils.doWithMethods(bridgeMethod.getDeclaringClass(), candidateMethods::add, filter);
/*  79 */       if (!candidateMethods.isEmpty())
/*     */       {
/*     */         
/*  82 */         bridgedMethod = (candidateMethods.size() == 1) ? candidateMethods.get(0) : searchCandidates(candidateMethods, bridgeMethod);
/*     */       }
/*  84 */       if (bridgedMethod == null)
/*     */       {
/*     */         
/*  87 */         bridgedMethod = bridgeMethod;
/*     */       }
/*  89 */       cache.put(bridgeMethod, bridgedMethod);
/*     */     } 
/*  91 */     return bridgedMethod;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static boolean isBridgedCandidateFor(Method candidateMethod, Method bridgeMethod) {
/* 101 */     return (!candidateMethod.isBridge() && !candidateMethod.equals(bridgeMethod) && candidateMethod
/* 102 */       .getName().equals(bridgeMethod.getName()) && candidateMethod
/* 103 */       .getParameterCount() == bridgeMethod.getParameterCount());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   private static Method searchCandidates(List<Method> candidateMethods, Method bridgeMethod) {
/* 114 */     if (candidateMethods.isEmpty()) {
/* 115 */       return null;
/*     */     }
/* 117 */     Method previousMethod = null;
/* 118 */     boolean sameSig = true;
/* 119 */     for (Method candidateMethod : candidateMethods) {
/* 120 */       if (isBridgeMethodFor(bridgeMethod, candidateMethod, bridgeMethod.getDeclaringClass())) {
/* 121 */         return candidateMethod;
/*     */       }
/* 123 */       if (previousMethod != null)
/*     */       {
/* 125 */         sameSig = (sameSig && Arrays.equals((Object[])candidateMethod.getGenericParameterTypes(), (Object[])previousMethod.getGenericParameterTypes()));
/*     */       }
/* 127 */       previousMethod = candidateMethod;
/*     */     } 
/* 129 */     return sameSig ? candidateMethods.get(0) : null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static boolean isBridgeMethodFor(Method bridgeMethod, Method candidateMethod, Class<?> declaringClass) {
/* 137 */     if (isResolvedTypeMatch(candidateMethod, bridgeMethod, declaringClass)) {
/* 138 */       return true;
/*     */     }
/* 140 */     Method method = findGenericDeclaration(bridgeMethod);
/* 141 */     return (method != null && isResolvedTypeMatch(method, candidateMethod, declaringClass));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static boolean isResolvedTypeMatch(Method genericMethod, Method candidateMethod, Class<?> declaringClass) {
/* 151 */     Type[] genericParameters = genericMethod.getGenericParameterTypes();
/* 152 */     if (genericParameters.length != candidateMethod.getParameterCount()) {
/* 153 */       return false;
/*     */     }
/* 155 */     Class<?>[] candidateParameters = candidateMethod.getParameterTypes();
/* 156 */     for (int i = 0; i < candidateParameters.length; i++) {
/* 157 */       ResolvableType genericParameter = ResolvableType.forMethodParameter(genericMethod, i, declaringClass);
/* 158 */       Class<?> candidateParameter = candidateParameters[i];
/* 159 */       if (candidateParameter.isArray())
/*     */       {
/* 161 */         if (!candidateParameter.getComponentType().equals(genericParameter.getComponentType().toClass())) {
/* 162 */           return false;
/*     */         }
/*     */       }
/*     */       
/* 166 */       if (!ClassUtils.resolvePrimitiveIfNecessary(candidateParameter).equals(ClassUtils.resolvePrimitiveIfNecessary(genericParameter.toClass()))) {
/* 167 */         return false;
/*     */       }
/*     */     } 
/* 170 */     return true;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   private static Method findGenericDeclaration(Method bridgeMethod) {
/* 181 */     Class<?> superclass = bridgeMethod.getDeclaringClass().getSuperclass();
/* 182 */     while (superclass != null && Object.class != superclass) {
/* 183 */       Method method = searchForMatch(superclass, bridgeMethod);
/* 184 */       if (method != null && !method.isBridge()) {
/* 185 */         return method;
/*     */       }
/* 187 */       superclass = superclass.getSuperclass();
/*     */     } 
/*     */     
/* 190 */     Class<?>[] interfaces = ClassUtils.getAllInterfacesForClass(bridgeMethod.getDeclaringClass());
/* 191 */     return searchInterfaces(interfaces, bridgeMethod);
/*     */   }
/*     */   
/*     */   @Nullable
/*     */   private static Method searchInterfaces(Class<?>[] interfaces, Method bridgeMethod) {
/* 196 */     for (Class<?> ifc : interfaces) {
/* 197 */       Method method = searchForMatch(ifc, bridgeMethod);
/* 198 */       if (method != null && !method.isBridge()) {
/* 199 */         return method;
/*     */       }
/*     */       
/* 202 */       method = searchInterfaces(ifc.getInterfaces(), bridgeMethod);
/* 203 */       if (method != null) {
/* 204 */         return method;
/*     */       }
/*     */     } 
/*     */     
/* 208 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   private static Method searchForMatch(Class<?> type, Method bridgeMethod) {
/*     */     try {
/* 219 */       return type.getDeclaredMethod(bridgeMethod.getName(), bridgeMethod.getParameterTypes());
/*     */     }
/* 221 */     catch (NoSuchMethodException ex) {
/* 222 */       return null;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static boolean isVisibilityBridgeMethodPair(Method bridgeMethod, Method bridgedMethod) {
/* 234 */     if (bridgeMethod == bridgedMethod) {
/* 235 */       return true;
/*     */     }
/* 237 */     return (bridgeMethod.getReturnType().equals(bridgedMethod.getReturnType()) && bridgeMethod
/* 238 */       .getParameterCount() == bridgedMethod.getParameterCount() && 
/* 239 */       Arrays.equals((Object[])bridgeMethod.getParameterTypes(), (Object[])bridgedMethod.getParameterTypes()));
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\springframework\core\BridgeMethodResolver.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */