/*     */ package com.google.common.reflect;
/*     */ 
/*     */ import com.google.common.annotations.Beta;
/*     */ import com.google.common.base.Preconditions;
/*     */ import com.google.common.collect.ImmutableList;
/*     */ import com.google.errorprone.annotations.CanIgnoreReturnValue;
/*     */ import java.lang.annotation.Annotation;
/*     */ import java.lang.reflect.AnnotatedType;
/*     */ import java.lang.reflect.Constructor;
/*     */ import java.lang.reflect.GenericDeclaration;
/*     */ import java.lang.reflect.InvocationTargetException;
/*     */ import java.lang.reflect.Method;
/*     */ import java.lang.reflect.Modifier;
/*     */ import java.lang.reflect.Type;
/*     */ import java.lang.reflect.TypeVariable;
/*     */ import java.util.Arrays;
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
/*     */ @Beta
/*     */ public abstract class Invokable<T, R>
/*     */   extends Element
/*     */   implements GenericDeclaration
/*     */ {
/*     */   <M extends java.lang.reflect.AccessibleObject & java.lang.reflect.Member> Invokable(M member) {
/*  62 */     super(member);
/*     */   }
/*     */ 
/*     */   
/*     */   public static Invokable<?, Object> from(Method method) {
/*  67 */     return new MethodInvokable(method);
/*     */   }
/*     */ 
/*     */   
/*     */   public static <T> Invokable<T, T> from(Constructor<T> constructor) {
/*  72 */     return new ConstructorInvokable<>(constructor);
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
/*     */   @CanIgnoreReturnValue
/*     */   public final R invoke(T receiver, Object... args) throws InvocationTargetException, IllegalAccessException {
/* 102 */     return (R)invokeInternal(receiver, (Object[])Preconditions.checkNotNull(args));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final TypeToken<? extends R> getReturnType() {
/* 109 */     return (TypeToken)TypeToken.of(getGenericReturnType());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final ImmutableList<Parameter> getParameters() {
/* 118 */     Type[] parameterTypes = getGenericParameterTypes();
/* 119 */     Annotation[][] annotations = getParameterAnnotations();
/* 120 */     AnnotatedType[] annotatedTypes = getAnnotatedParameterTypes();
/* 121 */     ImmutableList.Builder<Parameter> builder = ImmutableList.builder();
/* 122 */     for (int i = 0; i < parameterTypes.length; i++) {
/* 123 */       builder.add(new Parameter(this, i, 
/*     */             
/* 125 */             TypeToken.of(parameterTypes[i]), annotations[i], annotatedTypes[i]));
/*     */     }
/* 127 */     return builder.build();
/*     */   }
/*     */ 
/*     */   
/*     */   public final ImmutableList<TypeToken<? extends Throwable>> getExceptionTypes() {
/* 132 */     ImmutableList.Builder<TypeToken<? extends Throwable>> builder = ImmutableList.builder();
/* 133 */     for (Type type : getGenericExceptionTypes()) {
/*     */ 
/*     */ 
/*     */       
/* 137 */       TypeToken<? extends Throwable> exceptionType = (TypeToken)TypeToken.of(type);
/* 138 */       builder.add(exceptionType);
/*     */     } 
/* 140 */     return builder.build();
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
/*     */   public final <R1 extends R> Invokable<T, R1> returning(Class<R1> returnType) {
/* 152 */     return returning(TypeToken.of(returnType));
/*     */   }
/*     */ 
/*     */   
/*     */   public final <R1 extends R> Invokable<T, R1> returning(TypeToken<R1> returnType) {
/* 157 */     if (!returnType.isSupertypeOf(getReturnType())) {
/* 158 */       throw new IllegalArgumentException("Invokable is known to return " + 
/* 159 */           getReturnType() + ", not " + returnType);
/*     */     }
/*     */     
/* 162 */     Invokable<T, R1> specialized = this;
/* 163 */     return specialized;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public final Class<? super T> getDeclaringClass() {
/* 169 */     return (Class)super.getDeclaringClass();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public TypeToken<T> getOwnerType() {
/* 177 */     return TypeToken.of((Class)getDeclaringClass());
/*     */   }
/*     */   
/*     */   public abstract boolean isOverridable();
/*     */   
/*     */   public abstract boolean isVarArgs();
/*     */   
/*     */   abstract Object invokeInternal(Object paramObject, Object[] paramArrayOfObject) throws InvocationTargetException, IllegalAccessException;
/*     */   
/*     */   abstract Type[] getGenericParameterTypes();
/*     */   
/*     */   abstract AnnotatedType[] getAnnotatedParameterTypes();
/*     */   
/*     */   abstract Type[] getGenericExceptionTypes();
/*     */   
/*     */   abstract Annotation[][] getParameterAnnotations();
/*     */   
/*     */   abstract Type getGenericReturnType();
/*     */   
/*     */   public abstract AnnotatedType getAnnotatedReturnType();
/*     */   
/*     */   static class MethodInvokable<T>
/*     */     extends Invokable<T, Object> {
/*     */     MethodInvokable(Method method) {
/* 201 */       super(method);
/* 202 */       this.method = method;
/*     */     }
/*     */     
/*     */     final Method method;
/*     */     
/*     */     final Object invokeInternal(Object receiver, Object[] args) throws InvocationTargetException, IllegalAccessException {
/* 208 */       return this.method.invoke(receiver, args);
/*     */     }
/*     */ 
/*     */     
/*     */     Type getGenericReturnType() {
/* 213 */       return this.method.getGenericReturnType();
/*     */     }
/*     */ 
/*     */     
/*     */     Type[] getGenericParameterTypes() {
/* 218 */       return this.method.getGenericParameterTypes();
/*     */     }
/*     */ 
/*     */     
/*     */     AnnotatedType[] getAnnotatedParameterTypes() {
/* 223 */       return this.method.getAnnotatedParameterTypes();
/*     */     }
/*     */ 
/*     */     
/*     */     public AnnotatedType getAnnotatedReturnType() {
/* 228 */       return this.method.getAnnotatedReturnType();
/*     */     }
/*     */ 
/*     */     
/*     */     Type[] getGenericExceptionTypes() {
/* 233 */       return this.method.getGenericExceptionTypes();
/*     */     }
/*     */ 
/*     */     
/*     */     final Annotation[][] getParameterAnnotations() {
/* 238 */       return this.method.getParameterAnnotations();
/*     */     }
/*     */ 
/*     */     
/*     */     public final TypeVariable<?>[] getTypeParameters() {
/* 243 */       return (TypeVariable<?>[])this.method.getTypeParameters();
/*     */     }
/*     */ 
/*     */     
/*     */     public final boolean isOverridable() {
/* 248 */       return (!isFinal() && 
/* 249 */         !isPrivate() && 
/* 250 */         !isStatic() && 
/* 251 */         !Modifier.isFinal(getDeclaringClass().getModifiers()));
/*     */     }
/*     */ 
/*     */     
/*     */     public final boolean isVarArgs() {
/* 256 */       return this.method.isVarArgs();
/*     */     }
/*     */   }
/*     */   
/*     */   static class ConstructorInvokable<T>
/*     */     extends Invokable<T, T> {
/*     */     final Constructor<?> constructor;
/*     */     
/*     */     ConstructorInvokable(Constructor<?> constructor) {
/* 265 */       super(constructor);
/* 266 */       this.constructor = constructor;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     final Object invokeInternal(Object receiver, Object[] args) throws InvocationTargetException, IllegalAccessException {
/*     */       try {
/* 273 */         return this.constructor.newInstance(args);
/* 274 */       } catch (InstantiationException e) {
/* 275 */         throw new RuntimeException(this.constructor + " failed.", e);
/*     */       } 
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     Type getGenericReturnType() {
/* 285 */       Class<?> declaringClass = getDeclaringClass();
/* 286 */       TypeVariable[] arrayOfTypeVariable = (TypeVariable[])declaringClass.getTypeParameters();
/* 287 */       if (arrayOfTypeVariable.length > 0) {
/* 288 */         return Types.newParameterizedType(declaringClass, (Type[])arrayOfTypeVariable);
/*     */       }
/* 290 */       return declaringClass;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     Type[] getGenericParameterTypes() {
/* 296 */       Type[] types = this.constructor.getGenericParameterTypes();
/* 297 */       if (types.length > 0 && mayNeedHiddenThis()) {
/* 298 */         Class<?>[] rawParamTypes = this.constructor.getParameterTypes();
/* 299 */         if (types.length == rawParamTypes.length && rawParamTypes[0] == 
/* 300 */           getDeclaringClass().getEnclosingClass())
/*     */         {
/* 302 */           return Arrays.<Type>copyOfRange(types, 1, types.length);
/*     */         }
/*     */       } 
/* 305 */       return types;
/*     */     }
/*     */ 
/*     */     
/*     */     AnnotatedType[] getAnnotatedParameterTypes() {
/* 310 */       return this.constructor.getAnnotatedParameterTypes();
/*     */     }
/*     */ 
/*     */     
/*     */     public AnnotatedType getAnnotatedReturnType() {
/* 315 */       return this.constructor.getAnnotatedReturnType();
/*     */     }
/*     */ 
/*     */     
/*     */     Type[] getGenericExceptionTypes() {
/* 320 */       return this.constructor.getGenericExceptionTypes();
/*     */     }
/*     */ 
/*     */     
/*     */     final Annotation[][] getParameterAnnotations() {
/* 325 */       return this.constructor.getParameterAnnotations();
/*     */     }
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
/*     */     public final TypeVariable<?>[] getTypeParameters() {
/* 339 */       TypeVariable[] arrayOfTypeVariable1 = (TypeVariable[])getDeclaringClass().getTypeParameters();
/* 340 */       TypeVariable[] arrayOfTypeVariable2 = (TypeVariable[])this.constructor.getTypeParameters();
/* 341 */       TypeVariable[] arrayOfTypeVariable3 = new TypeVariable[arrayOfTypeVariable1.length + arrayOfTypeVariable2.length];
/*     */       
/* 343 */       System.arraycopy(arrayOfTypeVariable1, 0, arrayOfTypeVariable3, 0, arrayOfTypeVariable1.length);
/* 344 */       System.arraycopy(arrayOfTypeVariable2, 0, arrayOfTypeVariable3, arrayOfTypeVariable1.length, arrayOfTypeVariable2.length);
/*     */       
/* 346 */       return (TypeVariable<?>[])arrayOfTypeVariable3;
/*     */     }
/*     */ 
/*     */     
/*     */     public final boolean isOverridable() {
/* 351 */       return false;
/*     */     }
/*     */ 
/*     */     
/*     */     public final boolean isVarArgs() {
/* 356 */       return this.constructor.isVarArgs();
/*     */     }
/*     */     
/*     */     private boolean mayNeedHiddenThis() {
/* 360 */       Class<?> declaringClass = this.constructor.getDeclaringClass();
/* 361 */       if (declaringClass.getEnclosingConstructor() != null)
/*     */       {
/* 363 */         return true;
/*     */       }
/* 365 */       Method enclosingMethod = declaringClass.getEnclosingMethod();
/* 366 */       if (enclosingMethod != null)
/*     */       {
/* 368 */         return !Modifier.isStatic(enclosingMethod.getModifiers());
/*     */       }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 376 */       return (declaringClass.getEnclosingClass() != null && 
/* 377 */         !Modifier.isStatic(declaringClass.getModifiers()));
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\google\common\reflect\Invokable.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */