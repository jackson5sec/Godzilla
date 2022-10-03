/*     */ package org.springframework.core;
/*     */ 
/*     */ import java.lang.reflect.Method;
/*     */ import java.lang.reflect.ParameterizedType;
/*     */ import java.lang.reflect.Type;
/*     */ import java.lang.reflect.TypeVariable;
/*     */ import java.util.Collections;
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ import org.springframework.lang.Nullable;
/*     */ import org.springframework.util.Assert;
/*     */ import org.springframework.util.ConcurrentReferenceHashMap;
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
/*     */ public final class GenericTypeResolver
/*     */ {
/*  48 */   private static final Map<Class<?>, Map<TypeVariable, Type>> typeVariableCache = (Map<Class<?>, Map<TypeVariable, Type>>)new ConcurrentReferenceHashMap();
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
/*     */   @Deprecated
/*     */   public static Class<?> resolveParameterType(MethodParameter methodParameter, Class<?> implementationClass) {
/*  64 */     Assert.notNull(methodParameter, "MethodParameter must not be null");
/*  65 */     Assert.notNull(implementationClass, "Class must not be null");
/*  66 */     methodParameter.setContainingClass(implementationClass);
/*  67 */     return methodParameter.getParameterType();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Class<?> resolveReturnType(Method method, Class<?> clazz) {
/*  78 */     Assert.notNull(method, "Method must not be null");
/*  79 */     Assert.notNull(clazz, "Class must not be null");
/*  80 */     return ResolvableType.forMethodReturnType(method, clazz).resolve(method.getReturnType());
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
/*     */   @Nullable
/*     */   public static Class<?> resolveReturnTypeArgument(Method method, Class<?> genericIfc) {
/*  94 */     Assert.notNull(method, "Method must not be null");
/*  95 */     ResolvableType resolvableType = ResolvableType.forMethodReturnType(method).as(genericIfc);
/*  96 */     if (!resolvableType.hasGenerics() || resolvableType.getType() instanceof java.lang.reflect.WildcardType) {
/*  97 */       return null;
/*     */     }
/*  99 */     return getSingleGeneric(resolvableType);
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
/*     */   @Nullable
/*     */   public static Class<?> resolveTypeArgument(Class<?> clazz, Class<?> genericIfc) {
/* 112 */     ResolvableType resolvableType = ResolvableType.forClass(clazz).as(genericIfc);
/* 113 */     if (!resolvableType.hasGenerics()) {
/* 114 */       return null;
/*     */     }
/* 116 */     return getSingleGeneric(resolvableType);
/*     */   }
/*     */   
/*     */   @Nullable
/*     */   private static Class<?> getSingleGeneric(ResolvableType resolvableType) {
/* 121 */     Assert.isTrue(((resolvableType.getGenerics()).length == 1), () -> "Expected 1 type argument on generic interface [" + resolvableType + "] but found " + (resolvableType.getGenerics()).length);
/*     */ 
/*     */     
/* 124 */     return resolvableType.getGeneric(new int[0]).resolve();
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
/*     */   @Nullable
/*     */   public static Class<?>[] resolveTypeArguments(Class<?> clazz, Class<?> genericIfc) {
/* 139 */     ResolvableType type = ResolvableType.forClass(clazz).as(genericIfc);
/* 140 */     if (!type.hasGenerics() || type.isEntirelyUnresolvable()) {
/* 141 */       return null;
/*     */     }
/* 143 */     return type.resolveGenerics(Object.class);
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
/*     */   public static Type resolveType(Type genericType, @Nullable Class<?> contextClass) {
/* 156 */     if (contextClass != null) {
/* 157 */       if (genericType instanceof TypeVariable) {
/* 158 */         ResolvableType resolvedTypeVariable = resolveVariable((TypeVariable)genericType, 
/* 159 */             ResolvableType.forClass(contextClass));
/* 160 */         if (resolvedTypeVariable != ResolvableType.NONE) {
/* 161 */           Class<?> resolved = resolvedTypeVariable.resolve();
/* 162 */           if (resolved != null) {
/* 163 */             return resolved;
/*     */           }
/*     */         }
/*     */       
/* 167 */       } else if (genericType instanceof ParameterizedType) {
/* 168 */         ResolvableType resolvedType = ResolvableType.forType(genericType);
/* 169 */         if (resolvedType.hasUnresolvableGenerics()) {
/* 170 */           ParameterizedType parameterizedType = (ParameterizedType)genericType;
/* 171 */           Class<?>[] generics = new Class[(parameterizedType.getActualTypeArguments()).length];
/* 172 */           Type[] typeArguments = parameterizedType.getActualTypeArguments();
/* 173 */           ResolvableType contextType = ResolvableType.forClass(contextClass);
/* 174 */           for (int i = 0; i < typeArguments.length; i++) {
/* 175 */             Type typeArgument = typeArguments[i];
/* 176 */             if (typeArgument instanceof TypeVariable) {
/* 177 */               ResolvableType resolvedTypeArgument = resolveVariable((TypeVariable)typeArgument, contextType);
/*     */               
/* 179 */               if (resolvedTypeArgument != ResolvableType.NONE) {
/* 180 */                 generics[i] = resolvedTypeArgument.resolve();
/*     */               } else {
/*     */                 
/* 183 */                 generics[i] = ResolvableType.forType(typeArgument).resolve();
/*     */               } 
/*     */             } else {
/*     */               
/* 187 */               generics[i] = ResolvableType.forType(typeArgument).resolve();
/*     */             } 
/*     */           } 
/* 190 */           Class<?> rawClass = resolvedType.getRawClass();
/* 191 */           if (rawClass != null) {
/* 192 */             return ResolvableType.forClassWithGenerics(rawClass, generics).getType();
/*     */           }
/*     */         } 
/*     */       } 
/*     */     }
/* 197 */     return genericType;
/*     */   }
/*     */ 
/*     */   
/*     */   private static ResolvableType resolveVariable(TypeVariable<?> typeVariable, ResolvableType contextType) {
/* 202 */     if (contextType.hasGenerics()) {
/* 203 */       ResolvableType resolvedType = ResolvableType.forType(typeVariable, contextType);
/* 204 */       if (resolvedType.resolve() != null) {
/* 205 */         return resolvedType;
/*     */       }
/*     */     } 
/*     */     
/* 209 */     ResolvableType superType = contextType.getSuperType();
/* 210 */     if (superType != ResolvableType.NONE) {
/* 211 */       ResolvableType resolvedType = resolveVariable(typeVariable, superType);
/* 212 */       if (resolvedType.resolve() != null) {
/* 213 */         return resolvedType;
/*     */       }
/*     */     } 
/* 216 */     for (ResolvableType ifc : contextType.getInterfaces()) {
/* 217 */       ResolvableType resolvedType = resolveVariable(typeVariable, ifc);
/* 218 */       if (resolvedType.resolve() != null) {
/* 219 */         return resolvedType;
/*     */       }
/*     */     } 
/* 222 */     return ResolvableType.NONE;
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
/*     */   public static Class<?> resolveType(Type genericType, Map<TypeVariable, Type> map) {
/* 234 */     return ResolvableType.forType(genericType, new TypeVariableMapVariableResolver(map)).toClass();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Map<TypeVariable, Type> getTypeVariableMap(Class<?> clazz) {
/* 245 */     Map<TypeVariable, Type> typeVariableMap = typeVariableCache.get(clazz);
/* 246 */     if (typeVariableMap == null) {
/* 247 */       typeVariableMap = new HashMap<>();
/* 248 */       buildTypeVariableMap(ResolvableType.forClass(clazz), typeVariableMap);
/* 249 */       typeVariableCache.put(clazz, Collections.unmodifiableMap(typeVariableMap));
/*     */     } 
/* 251 */     return typeVariableMap;
/*     */   }
/*     */ 
/*     */   
/*     */   private static void buildTypeVariableMap(ResolvableType type, Map<TypeVariable, Type> typeVariableMap) {
/* 256 */     if (type != ResolvableType.NONE) {
/* 257 */       Class<?> resolved = type.resolve();
/* 258 */       if (resolved != null && type.getType() instanceof ParameterizedType) {
/* 259 */         TypeVariable[] arrayOfTypeVariable = (TypeVariable[])resolved.getTypeParameters();
/* 260 */         for (int i = 0; i < arrayOfTypeVariable.length; i++) {
/* 261 */           ResolvableType generic = type.getGeneric(new int[] { i });
/* 262 */           while (generic.getType() instanceof TypeVariable) {
/* 263 */             generic = generic.resolveType();
/*     */           }
/* 265 */           if (generic != ResolvableType.NONE) {
/* 266 */             typeVariableMap.put(arrayOfTypeVariable[i], generic.getType());
/*     */           }
/*     */         } 
/*     */       } 
/* 270 */       buildTypeVariableMap(type.getSuperType(), typeVariableMap);
/* 271 */       for (ResolvableType interfaceType : type.getInterfaces()) {
/* 272 */         buildTypeVariableMap(interfaceType, typeVariableMap);
/*     */       }
/* 274 */       if (resolved != null && resolved.isMemberClass()) {
/* 275 */         buildTypeVariableMap(ResolvableType.forClass(resolved.getEnclosingClass()), typeVariableMap);
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private static class TypeVariableMapVariableResolver
/*     */     implements ResolvableType.VariableResolver
/*     */   {
/*     */     private final Map<TypeVariable, Type> typeVariableMap;
/*     */     
/*     */     public TypeVariableMapVariableResolver(Map<TypeVariable, Type> typeVariableMap) {
/* 287 */       this.typeVariableMap = typeVariableMap;
/*     */     }
/*     */ 
/*     */     
/*     */     @Nullable
/*     */     public ResolvableType resolveVariable(TypeVariable<?> variable) {
/* 293 */       Type type = this.typeVariableMap.get(variable);
/* 294 */       return (type != null) ? ResolvableType.forType(type) : null;
/*     */     }
/*     */ 
/*     */     
/*     */     public Object getSource() {
/* 299 */       return this.typeVariableMap;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\springframework\core\GenericTypeResolver.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */