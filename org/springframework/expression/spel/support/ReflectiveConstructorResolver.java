/*     */ package org.springframework.expression.spel.support;
/*     */ 
/*     */ import java.lang.reflect.Constructor;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.Comparator;
/*     */ import java.util.List;
/*     */ import org.springframework.core.MethodParameter;
/*     */ import org.springframework.core.convert.TypeDescriptor;
/*     */ import org.springframework.expression.AccessException;
/*     */ import org.springframework.expression.ConstructorExecutor;
/*     */ import org.springframework.expression.ConstructorResolver;
/*     */ import org.springframework.expression.EvaluationContext;
/*     */ import org.springframework.expression.EvaluationException;
/*     */ import org.springframework.expression.TypeConverter;
/*     */ import org.springframework.lang.Nullable;
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
/*     */ public class ReflectiveConstructorResolver
/*     */   implements ConstructorResolver
/*     */ {
/*     */   @Nullable
/*     */   public ConstructorExecutor resolve(EvaluationContext context, String typeName, List<TypeDescriptor> argumentTypes) throws AccessException {
/*     */     try {
/*  59 */       TypeConverter typeConverter = context.getTypeConverter();
/*  60 */       Class<?> type = context.getTypeLocator().findType(typeName);
/*  61 */       Constructor[] arrayOfConstructor = (Constructor[])type.getConstructors();
/*     */       
/*  63 */       Arrays.sort(arrayOfConstructor, Comparator.comparingInt(Constructor::getParameterCount));
/*     */       
/*  65 */       Constructor<?> closeMatch = null;
/*  66 */       Constructor<?> matchRequiringConversion = null;
/*     */       
/*  68 */       for (Constructor<?> ctor : arrayOfConstructor) {
/*  69 */         int paramCount = ctor.getParameterCount();
/*  70 */         List<TypeDescriptor> paramDescriptors = new ArrayList<>(paramCount);
/*  71 */         for (int i = 0; i < paramCount; i++) {
/*  72 */           paramDescriptors.add(new TypeDescriptor(new MethodParameter(ctor, i)));
/*     */         }
/*  74 */         ReflectionHelper.ArgumentsMatchInfo matchInfo = null;
/*  75 */         if (ctor.isVarArgs() && argumentTypes.size() >= paramCount - 1) {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */           
/*  82 */           matchInfo = ReflectionHelper.compareArgumentsVarargs(paramDescriptors, argumentTypes, typeConverter);
/*     */         }
/*  84 */         else if (paramCount == argumentTypes.size()) {
/*     */           
/*  86 */           matchInfo = ReflectionHelper.compareArguments(paramDescriptors, argumentTypes, typeConverter);
/*     */         } 
/*  88 */         if (matchInfo != null) {
/*  89 */           if (matchInfo.isExactMatch()) {
/*  90 */             return new ReflectiveConstructorExecutor(ctor);
/*     */           }
/*  92 */           if (matchInfo.isCloseMatch()) {
/*  93 */             closeMatch = ctor;
/*     */           }
/*  95 */           else if (matchInfo.isMatchRequiringConversion()) {
/*  96 */             matchRequiringConversion = ctor;
/*     */           } 
/*     */         } 
/*     */       } 
/*     */       
/* 101 */       if (closeMatch != null) {
/* 102 */         return new ReflectiveConstructorExecutor(closeMatch);
/*     */       }
/* 104 */       if (matchRequiringConversion != null) {
/* 105 */         return new ReflectiveConstructorExecutor(matchRequiringConversion);
/*     */       }
/*     */       
/* 108 */       return null;
/*     */     
/*     */     }
/* 111 */     catch (EvaluationException ex) {
/* 112 */       throw new AccessException("Failed to resolve constructor", ex);
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\springframework\expression\spel\support\ReflectiveConstructorResolver.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */