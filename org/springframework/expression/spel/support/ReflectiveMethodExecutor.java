/*     */ package org.springframework.expression.spel.support;
/*     */ 
/*     */ import java.lang.reflect.Method;
/*     */ import java.lang.reflect.Modifier;
/*     */ import org.springframework.core.MethodParameter;
/*     */ import org.springframework.core.convert.TypeDescriptor;
/*     */ import org.springframework.expression.AccessException;
/*     */ import org.springframework.expression.EvaluationContext;
/*     */ import org.springframework.expression.MethodExecutor;
/*     */ import org.springframework.expression.TypedValue;
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
/*     */ public class ReflectiveMethodExecutor
/*     */   implements MethodExecutor
/*     */ {
/*     */   private final Method originalMethod;
/*     */   private final Method methodToInvoke;
/*     */   @Nullable
/*     */   private final Integer varargsPosition;
/*     */   private boolean computedPublicDeclaringClass = false;
/*     */   @Nullable
/*     */   private Class<?> publicDeclaringClass;
/*     */   private boolean argumentConversionOccurred = false;
/*     */   
/*     */   public ReflectiveMethodExecutor(Method method) {
/*  61 */     this.originalMethod = method;
/*  62 */     this.methodToInvoke = ClassUtils.getInterfaceMethodIfPossible(method);
/*  63 */     if (method.isVarArgs()) {
/*  64 */       this.varargsPosition = Integer.valueOf(method.getParameterCount() - 1);
/*     */     } else {
/*     */       
/*  67 */       this.varargsPosition = null;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final Method getMethod() {
/*  76 */     return this.originalMethod;
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
/*     */   public Class<?> getPublicDeclaringClass() {
/*  89 */     if (!this.computedPublicDeclaringClass) {
/*  90 */       this
/*  91 */         .publicDeclaringClass = discoverPublicDeclaringClass(this.originalMethod, this.originalMethod.getDeclaringClass());
/*  92 */       this.computedPublicDeclaringClass = true;
/*     */     } 
/*  94 */     return this.publicDeclaringClass;
/*     */   }
/*     */   
/*     */   @Nullable
/*     */   private Class<?> discoverPublicDeclaringClass(Method method, Class<?> clazz) {
/*  99 */     if (Modifier.isPublic(clazz.getModifiers())) {
/*     */       try {
/* 101 */         clazz.getDeclaredMethod(method.getName(), method.getParameterTypes());
/* 102 */         return clazz;
/*     */       }
/* 104 */       catch (NoSuchMethodException noSuchMethodException) {}
/*     */     }
/*     */ 
/*     */     
/* 108 */     if (clazz.getSuperclass() != null) {
/* 109 */       return discoverPublicDeclaringClass(method, clazz.getSuperclass());
/*     */     }
/* 111 */     return null;
/*     */   }
/*     */   
/*     */   public boolean didArgumentConversionOccur() {
/* 115 */     return this.argumentConversionOccurred;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public TypedValue execute(EvaluationContext context, Object target, Object... arguments) throws AccessException {
/*     */     try {
/* 122 */       this.argumentConversionOccurred = ReflectionHelper.convertArguments(context
/* 123 */           .getTypeConverter(), arguments, this.originalMethod, this.varargsPosition);
/* 124 */       if (this.originalMethod.isVarArgs()) {
/* 125 */         arguments = ReflectionHelper.setupArgumentsForVarargsInvocation(this.originalMethod
/* 126 */             .getParameterTypes(), arguments);
/*     */       }
/* 128 */       ReflectionUtils.makeAccessible(this.methodToInvoke);
/* 129 */       Object value = this.methodToInvoke.invoke(target, arguments);
/* 130 */       return new TypedValue(value, (new TypeDescriptor(new MethodParameter(this.originalMethod, -1))).narrow(value));
/*     */     }
/* 132 */     catch (Exception ex) {
/* 133 */       throw new AccessException("Problem invoking method: " + this.methodToInvoke, ex);
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\springframework\expression\spel\support\ReflectiveMethodExecutor.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */