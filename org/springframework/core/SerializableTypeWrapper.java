/*     */ package org.springframework.core;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.ObjectInputStream;
/*     */ import java.io.Serializable;
/*     */ import java.lang.reflect.Field;
/*     */ import java.lang.reflect.GenericArrayType;
/*     */ import java.lang.reflect.InvocationHandler;
/*     */ import java.lang.reflect.InvocationTargetException;
/*     */ import java.lang.reflect.Method;
/*     */ import java.lang.reflect.ParameterizedType;
/*     */ import java.lang.reflect.Proxy;
/*     */ import java.lang.reflect.Type;
/*     */ import java.lang.reflect.TypeVariable;
/*     */ import java.lang.reflect.WildcardType;
/*     */ import org.springframework.lang.Nullable;
/*     */ import org.springframework.util.ConcurrentReferenceHashMap;
/*     */ import org.springframework.util.ObjectUtils;
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
/*     */ final class SerializableTypeWrapper
/*     */ {
/*  59 */   private static final Class<?>[] SUPPORTED_SERIALIZABLE_TYPES = new Class[] { GenericArrayType.class, ParameterizedType.class, TypeVariable.class, WildcardType.class };
/*     */ 
/*     */   
/*  62 */   static final ConcurrentReferenceHashMap<Type, Type> cache = new ConcurrentReferenceHashMap(256);
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
/*     */   public static Type forField(Field field) {
/*  74 */     return forTypeProvider(new FieldTypeProvider(field));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public static Type forMethodParameter(MethodParameter methodParameter) {
/*  83 */     return forTypeProvider(new MethodParameterTypeProvider(methodParameter));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <T extends Type> T unwrap(T type) {
/*  93 */     Type unwrapped = null;
/*  94 */     if (type instanceof SerializableTypeProxy) {
/*  95 */       unwrapped = ((SerializableTypeProxy)type).getTypeProvider().getType();
/*     */     }
/*  97 */     return (unwrapped != null) ? (T)unwrapped : type;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   static Type forTypeProvider(TypeProvider provider) {
/* 107 */     Type providedType = provider.getType();
/* 108 */     if (providedType == null || providedType instanceof Serializable)
/*     */     {
/* 110 */       return providedType;
/*     */     }
/* 112 */     if (NativeDetector.inNativeImage() || !Serializable.class.isAssignableFrom(Class.class))
/*     */     {
/*     */       
/* 115 */       return providedType;
/*     */     }
/*     */ 
/*     */     
/* 119 */     Type cached = (Type)cache.get(providedType);
/* 120 */     if (cached != null) {
/* 121 */       return cached;
/*     */     }
/* 123 */     for (Class<?> type : SUPPORTED_SERIALIZABLE_TYPES) {
/* 124 */       if (type.isInstance(providedType)) {
/* 125 */         ClassLoader classLoader = provider.getClass().getClassLoader();
/* 126 */         Class<?>[] interfaces = new Class[] { type, SerializableTypeProxy.class, Serializable.class };
/* 127 */         InvocationHandler handler = new TypeProxyInvocationHandler(provider);
/* 128 */         cached = (Type)Proxy.newProxyInstance(classLoader, interfaces, handler);
/* 129 */         cache.put(providedType, cached);
/* 130 */         return cached;
/*     */       } 
/*     */     } 
/* 133 */     throw new IllegalArgumentException("Unsupported Type class: " + providedType.getClass().getName());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static interface SerializableTypeProxy
/*     */   {
/*     */     SerializableTypeWrapper.TypeProvider getTypeProvider();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static interface TypeProvider
/*     */     extends Serializable
/*     */   {
/*     */     @Nullable
/*     */     Type getType();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     @Nullable
/*     */     default Object getSource() {
/* 167 */       return null;
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static class TypeProxyInvocationHandler
/*     */     implements InvocationHandler, Serializable
/*     */   {
/*     */     private final SerializableTypeWrapper.TypeProvider provider;
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public TypeProxyInvocationHandler(SerializableTypeWrapper.TypeProvider provider) {
/* 183 */       this.provider = provider;
/*     */     }
/*     */     
/*     */     @Nullable
/*     */     public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
/*     */       Object other;
/* 189 */       switch (method.getName()) {
/*     */         case "equals":
/* 191 */           other = args[0];
/*     */           
/* 193 */           if (other instanceof Type) {
/* 194 */             other = SerializableTypeWrapper.unwrap((Type)other);
/*     */           }
/* 196 */           return Boolean.valueOf(ObjectUtils.nullSafeEquals(this.provider.getType(), other));
/*     */         case "hashCode":
/* 198 */           return Integer.valueOf(ObjectUtils.nullSafeHashCode(this.provider.getType()));
/*     */         case "getTypeProvider":
/* 200 */           return this.provider;
/*     */       } 
/*     */       
/* 203 */       if (Type.class == method.getReturnType() && ObjectUtils.isEmpty(args)) {
/* 204 */         return SerializableTypeWrapper.forTypeProvider(new SerializableTypeWrapper.MethodInvokeTypeProvider(this.provider, method, -1));
/*     */       }
/* 206 */       if (Type[].class == method.getReturnType() && ObjectUtils.isEmpty(args)) {
/* 207 */         Type[] result = new Type[((Type[])method.invoke(this.provider.getType(), new Object[0])).length];
/* 208 */         for (int i = 0; i < result.length; i++) {
/* 209 */           result[i] = SerializableTypeWrapper.forTypeProvider(new SerializableTypeWrapper.MethodInvokeTypeProvider(this.provider, method, i));
/*     */         }
/* 211 */         return result;
/*     */       } 
/*     */       
/*     */       try {
/* 215 */         return method.invoke(this.provider.getType(), args);
/*     */       }
/* 217 */       catch (InvocationTargetException ex) {
/* 218 */         throw ex.getTargetException();
/*     */       } 
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   static class FieldTypeProvider
/*     */     implements TypeProvider
/*     */   {
/*     */     private final String fieldName;
/*     */ 
/*     */     
/*     */     private final Class<?> declaringClass;
/*     */     
/*     */     private transient Field field;
/*     */ 
/*     */     
/*     */     public FieldTypeProvider(Field field) {
/* 237 */       this.fieldName = field.getName();
/* 238 */       this.declaringClass = field.getDeclaringClass();
/* 239 */       this.field = field;
/*     */     }
/*     */ 
/*     */     
/*     */     public Type getType() {
/* 244 */       return this.field.getGenericType();
/*     */     }
/*     */ 
/*     */     
/*     */     public Object getSource() {
/* 249 */       return this.field;
/*     */     }
/*     */     
/*     */     private void readObject(ObjectInputStream inputStream) throws IOException, ClassNotFoundException {
/* 253 */       inputStream.defaultReadObject();
/*     */       try {
/* 255 */         this.field = this.declaringClass.getDeclaredField(this.fieldName);
/*     */       }
/* 257 */       catch (Throwable ex) {
/* 258 */         throw new IllegalStateException("Could not find original class structure", ex);
/*     */       } 
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   static class MethodParameterTypeProvider
/*     */     implements TypeProvider
/*     */   {
/*     */     @Nullable
/*     */     private final String methodName;
/*     */ 
/*     */     
/*     */     private final Class<?>[] parameterTypes;
/*     */     
/*     */     private final Class<?> declaringClass;
/*     */     
/*     */     private final int parameterIndex;
/*     */     
/*     */     private transient MethodParameter methodParameter;
/*     */ 
/*     */     
/*     */     public MethodParameterTypeProvider(MethodParameter methodParameter) {
/* 282 */       this.methodName = (methodParameter.getMethod() != null) ? methodParameter.getMethod().getName() : null;
/* 283 */       this.parameterTypes = methodParameter.getExecutable().getParameterTypes();
/* 284 */       this.declaringClass = methodParameter.getDeclaringClass();
/* 285 */       this.parameterIndex = methodParameter.getParameterIndex();
/* 286 */       this.methodParameter = methodParameter;
/*     */     }
/*     */ 
/*     */     
/*     */     public Type getType() {
/* 291 */       return this.methodParameter.getGenericParameterType();
/*     */     }
/*     */ 
/*     */     
/*     */     public Object getSource() {
/* 296 */       return this.methodParameter;
/*     */     }
/*     */     
/*     */     private void readObject(ObjectInputStream inputStream) throws IOException, ClassNotFoundException {
/* 300 */       inputStream.defaultReadObject();
/*     */       try {
/* 302 */         if (this.methodName != null) {
/* 303 */           this
/* 304 */             .methodParameter = new MethodParameter(this.declaringClass.getDeclaredMethod(this.methodName, this.parameterTypes), this.parameterIndex);
/*     */         } else {
/*     */           
/* 307 */           this
/* 308 */             .methodParameter = new MethodParameter(this.declaringClass.getDeclaredConstructor(this.parameterTypes), this.parameterIndex);
/*     */         }
/*     */       
/* 311 */       } catch (Throwable ex) {
/* 312 */         throw new IllegalStateException("Could not find original class structure", ex);
/*     */       } 
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   static class MethodInvokeTypeProvider
/*     */     implements TypeProvider
/*     */   {
/*     */     private final SerializableTypeWrapper.TypeProvider provider;
/*     */ 
/*     */     
/*     */     private final String methodName;
/*     */     
/*     */     private final Class<?> declaringClass;
/*     */     
/*     */     private final int index;
/*     */     
/*     */     private transient Method method;
/*     */     
/*     */     @Nullable
/*     */     private volatile transient Object result;
/*     */ 
/*     */     
/*     */     public MethodInvokeTypeProvider(SerializableTypeWrapper.TypeProvider provider, Method method, int index) {
/* 338 */       this.provider = provider;
/* 339 */       this.methodName = method.getName();
/* 340 */       this.declaringClass = method.getDeclaringClass();
/* 341 */       this.index = index;
/* 342 */       this.method = method;
/*     */     }
/*     */ 
/*     */     
/*     */     @Nullable
/*     */     public Type getType() {
/* 348 */       Object result = this.result;
/* 349 */       if (result == null) {
/*     */         
/* 351 */         result = ReflectionUtils.invokeMethod(this.method, this.provider.getType());
/*     */         
/* 353 */         this.result = result;
/*     */       } 
/* 355 */       return (result instanceof Type[]) ? ((Type[])result)[this.index] : (Type)result;
/*     */     }
/*     */ 
/*     */     
/*     */     @Nullable
/*     */     public Object getSource() {
/* 361 */       return null;
/*     */     }
/*     */     
/*     */     private void readObject(ObjectInputStream inputStream) throws IOException, ClassNotFoundException {
/* 365 */       inputStream.defaultReadObject();
/* 366 */       Method method = ReflectionUtils.findMethod(this.declaringClass, this.methodName);
/* 367 */       if (method == null) {
/* 368 */         throw new IllegalStateException("Cannot find method on deserialization: " + this.methodName);
/*     */       }
/* 370 */       if (method.getReturnType() != Type.class && method.getReturnType() != Type[].class) {
/* 371 */         throw new IllegalStateException("Invalid return type on deserialized method - needs to be Type or Type[]: " + method);
/*     */       }
/*     */       
/* 374 */       this.method = method;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\springframework\core\SerializableTypeWrapper.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */