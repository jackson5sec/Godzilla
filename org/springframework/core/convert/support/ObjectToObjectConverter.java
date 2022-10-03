/*     */ package org.springframework.core.convert.support;
/*     */ 
/*     */ import java.lang.reflect.Constructor;
/*     */ import java.lang.reflect.InvocationTargetException;
/*     */ import java.lang.reflect.Member;
/*     */ import java.lang.reflect.Method;
/*     */ import java.lang.reflect.Modifier;
/*     */ import java.util.Collections;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import org.springframework.core.convert.ConversionFailedException;
/*     */ import org.springframework.core.convert.TypeDescriptor;
/*     */ import org.springframework.core.convert.converter.ConditionalGenericConverter;
/*     */ import org.springframework.core.convert.converter.GenericConverter;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ final class ObjectToObjectConverter
/*     */   implements ConditionalGenericConverter
/*     */ {
/*  69 */   private static final Map<Class<?>, Member> conversionMemberCache = (Map<Class<?>, Member>)new ConcurrentReferenceHashMap(32);
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Set<GenericConverter.ConvertiblePair> getConvertibleTypes() {
/*  75 */     return Collections.singleton(new GenericConverter.ConvertiblePair(Object.class, Object.class));
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean matches(TypeDescriptor sourceType, TypeDescriptor targetType) {
/*  80 */     return (sourceType.getType() != targetType.getType() && 
/*  81 */       hasConversionMethodOrConstructor(targetType.getType(), sourceType.getType()));
/*     */   }
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public Object convert(@Nullable Object source, TypeDescriptor sourceType, TypeDescriptor targetType) {
/*  87 */     if (source == null) {
/*  88 */       return null;
/*     */     }
/*  90 */     Class<?> sourceClass = sourceType.getType();
/*  91 */     Class<?> targetClass = targetType.getType();
/*  92 */     Member member = getValidatedMember(targetClass, sourceClass);
/*     */     
/*     */     try {
/*  95 */       if (member instanceof Method) {
/*  96 */         Method method = (Method)member;
/*  97 */         ReflectionUtils.makeAccessible(method);
/*  98 */         if (!Modifier.isStatic(method.getModifiers())) {
/*  99 */           return method.invoke(source, new Object[0]);
/*     */         }
/*     */         
/* 102 */         return method.invoke(null, new Object[] { source });
/*     */       } 
/*     */       
/* 105 */       if (member instanceof Constructor) {
/* 106 */         Constructor<?> ctor = (Constructor)member;
/* 107 */         ReflectionUtils.makeAccessible(ctor);
/* 108 */         return ctor.newInstance(new Object[] { source });
/*     */       }
/*     */     
/* 111 */     } catch (InvocationTargetException ex) {
/* 112 */       throw new ConversionFailedException(sourceType, targetType, source, ex.getTargetException());
/*     */     }
/* 114 */     catch (Throwable ex) {
/* 115 */       throw new ConversionFailedException(sourceType, targetType, source, ex);
/*     */     } 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 121 */     throw new IllegalStateException(String.format("No to%3$s() method exists on %1$s, and no static valueOf/of/from(%1$s) method or %3$s(%1$s) constructor exists on %2$s.", new Object[] { sourceClass
/*     */             
/* 123 */             .getName(), targetClass.getName(), targetClass.getSimpleName() }));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   static boolean hasConversionMethodOrConstructor(Class<?> targetClass, Class<?> sourceClass) {
/* 129 */     return (getValidatedMember(targetClass, sourceClass) != null);
/*     */   }
/*     */   
/*     */   @Nullable
/*     */   private static Member getValidatedMember(Class<?> targetClass, Class<?> sourceClass) {
/* 134 */     Member<?> member = conversionMemberCache.get(targetClass);
/* 135 */     if (isApplicable(member, sourceClass)) {
/* 136 */       return member;
/*     */     }
/*     */     
/* 139 */     member = determineToMethod(targetClass, sourceClass);
/* 140 */     if (member == null) {
/* 141 */       member = determineFactoryMethod(targetClass, sourceClass);
/* 142 */       if (member == null) {
/* 143 */         member = determineFactoryConstructor(targetClass, sourceClass);
/* 144 */         if (member == null) {
/* 145 */           return null;
/*     */         }
/*     */       } 
/*     */     } 
/*     */     
/* 150 */     conversionMemberCache.put(targetClass, member);
/* 151 */     return member;
/*     */   }
/*     */   
/*     */   private static boolean isApplicable(Member member, Class<?> sourceClass) {
/* 155 */     if (member instanceof Method) {
/* 156 */       Method method = (Method)member;
/* 157 */       return !Modifier.isStatic(method.getModifiers()) ? 
/* 158 */         ClassUtils.isAssignable(method.getDeclaringClass(), sourceClass) : (
/* 159 */         (method.getParameterTypes()[0] == sourceClass));
/*     */     } 
/* 161 */     if (member instanceof Constructor) {
/* 162 */       Constructor<?> ctor = (Constructor)member;
/* 163 */       return (ctor.getParameterTypes()[0] == sourceClass);
/*     */     } 
/*     */     
/* 166 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   private static Method determineToMethod(Class<?> targetClass, Class<?> sourceClass) {
/* 172 */     if (String.class == targetClass || String.class == sourceClass)
/*     */     {
/* 174 */       return null;
/*     */     }
/*     */     
/* 177 */     Method method = ClassUtils.getMethodIfAvailable(sourceClass, "to" + targetClass.getSimpleName(), new Class[0]);
/* 178 */     return (method != null && !Modifier.isStatic(method.getModifiers()) && 
/* 179 */       ClassUtils.isAssignable(targetClass, method.getReturnType())) ? method : null;
/*     */   }
/*     */   
/*     */   @Nullable
/*     */   private static Method determineFactoryMethod(Class<?> targetClass, Class<?> sourceClass) {
/* 184 */     if (String.class == targetClass)
/*     */     {
/* 186 */       return null;
/*     */     }
/*     */     
/* 189 */     Method method = ClassUtils.getStaticMethod(targetClass, "valueOf", new Class[] { sourceClass });
/* 190 */     if (method == null) {
/* 191 */       method = ClassUtils.getStaticMethod(targetClass, "of", new Class[] { sourceClass });
/* 192 */       if (method == null) {
/* 193 */         method = ClassUtils.getStaticMethod(targetClass, "from", new Class[] { sourceClass });
/*     */       }
/*     */     } 
/* 196 */     return method;
/*     */   }
/*     */   
/*     */   @Nullable
/*     */   private static Constructor<?> determineFactoryConstructor(Class<?> targetClass, Class<?> sourceClass) {
/* 201 */     return ClassUtils.getConstructorIfAvailable(targetClass, new Class[] { sourceClass });
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\springframework\core\convert\support\ObjectToObjectConverter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */