/*     */ package org.springframework.util;
/*     */ 
/*     */ import java.lang.reflect.InvocationTargetException;
/*     */ import java.lang.reflect.Method;
/*     */ import java.lang.reflect.Modifier;
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
/*     */ public class MethodInvoker
/*     */ {
/*  41 */   private static final Object[] EMPTY_ARGUMENTS = new Object[0];
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   protected Class<?> targetClass;
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   private Object targetObject;
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   private String targetMethod;
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   private String staticMethod;
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   private Object[] arguments;
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   private Method methodObject;
/*     */ 
/*     */ 
/*     */   
/*     */   public void setTargetClass(@Nullable Class<?> targetClass) {
/*  72 */     this.targetClass = targetClass;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public Class<?> getTargetClass() {
/*  80 */     return this.targetClass;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setTargetObject(@Nullable Object targetObject) {
/*  91 */     this.targetObject = targetObject;
/*  92 */     if (targetObject != null) {
/*  93 */       this.targetClass = targetObject.getClass();
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public Object getTargetObject() {
/* 102 */     return this.targetObject;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setTargetMethod(@Nullable String targetMethod) {
/* 113 */     this.targetMethod = targetMethod;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public String getTargetMethod() {
/* 121 */     return this.targetMethod;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setStaticMethod(String staticMethod) {
/* 132 */     this.staticMethod = staticMethod;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setArguments(Object... arguments) {
/* 140 */     this.arguments = arguments;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Object[] getArguments() {
/* 147 */     return (this.arguments != null) ? this.arguments : EMPTY_ARGUMENTS;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void prepare() throws ClassNotFoundException, NoSuchMethodException {
/* 158 */     if (this.staticMethod != null) {
/* 159 */       int lastDotIndex = this.staticMethod.lastIndexOf('.');
/* 160 */       if (lastDotIndex == -1 || lastDotIndex == this.staticMethod.length()) {
/* 161 */         throw new IllegalArgumentException("staticMethod must be a fully qualified class plus method name: e.g. 'example.MyExampleClass.myExampleMethod'");
/*     */       }
/*     */ 
/*     */       
/* 165 */       String className = this.staticMethod.substring(0, lastDotIndex);
/* 166 */       String methodName = this.staticMethod.substring(lastDotIndex + 1);
/* 167 */       this.targetClass = resolveClassName(className);
/* 168 */       this.targetMethod = methodName;
/*     */     } 
/*     */     
/* 171 */     Class<?> targetClass = getTargetClass();
/* 172 */     String targetMethod = getTargetMethod();
/* 173 */     Assert.notNull(targetClass, "Either 'targetClass' or 'targetObject' is required");
/* 174 */     Assert.notNull(targetMethod, "Property 'targetMethod' is required");
/*     */     
/* 176 */     Object[] arguments = getArguments();
/* 177 */     Class<?>[] argTypes = new Class[arguments.length];
/* 178 */     for (int i = 0; i < arguments.length; i++) {
/* 179 */       argTypes[i] = (arguments[i] != null) ? arguments[i].getClass() : Object.class;
/*     */     }
/*     */ 
/*     */     
/*     */     try {
/* 184 */       this.methodObject = targetClass.getMethod(targetMethod, argTypes);
/*     */     }
/* 186 */     catch (NoSuchMethodException ex) {
/*     */       
/* 188 */       this.methodObject = findMatchingMethod();
/* 189 */       if (this.methodObject == null) {
/* 190 */         throw ex;
/*     */       }
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
/*     */   
/*     */   protected Class<?> resolveClassName(String className) throws ClassNotFoundException {
/* 204 */     return ClassUtils.forName(className, ClassUtils.getDefaultClassLoader());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   protected Method findMatchingMethod() {
/* 216 */     String targetMethod = getTargetMethod();
/* 217 */     Object[] arguments = getArguments();
/* 218 */     int argCount = arguments.length;
/*     */     
/* 220 */     Class<?> targetClass = getTargetClass();
/* 221 */     Assert.state((targetClass != null), "No target class set");
/* 222 */     Method[] candidates = ReflectionUtils.getAllDeclaredMethods(targetClass);
/* 223 */     int minTypeDiffWeight = Integer.MAX_VALUE;
/* 224 */     Method matchingMethod = null;
/*     */     
/* 226 */     for (Method candidate : candidates) {
/* 227 */       if (candidate.getName().equals(targetMethod) && 
/* 228 */         candidate.getParameterCount() == argCount) {
/* 229 */         Class<?>[] paramTypes = candidate.getParameterTypes();
/* 230 */         int typeDiffWeight = getTypeDifferenceWeight(paramTypes, arguments);
/* 231 */         if (typeDiffWeight < minTypeDiffWeight) {
/* 232 */           minTypeDiffWeight = typeDiffWeight;
/* 233 */           matchingMethod = candidate;
/*     */         } 
/*     */       } 
/*     */     } 
/*     */ 
/*     */     
/* 239 */     return matchingMethod;
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
/*     */   public Method getPreparedMethod() throws IllegalStateException {
/* 251 */     if (this.methodObject == null) {
/* 252 */       throw new IllegalStateException("prepare() must be called prior to invoke() on MethodInvoker");
/*     */     }
/* 254 */     return this.methodObject;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isPrepared() {
/* 262 */     return (this.methodObject != null);
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
/*     */   public Object invoke() throws InvocationTargetException, IllegalAccessException {
/* 277 */     Object targetObject = getTargetObject();
/* 278 */     Method preparedMethod = getPreparedMethod();
/* 279 */     if (targetObject == null && !Modifier.isStatic(preparedMethod.getModifiers())) {
/* 280 */       throw new IllegalArgumentException("Target method must not be non-static without a target");
/*     */     }
/* 282 */     ReflectionUtils.makeAccessible(preparedMethod);
/* 283 */     return preparedMethod.invoke(targetObject, getArguments());
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
/*     */   public static int getTypeDifferenceWeight(Class<?>[] paramTypes, Object[] args) {
/* 308 */     int result = 0;
/* 309 */     for (int i = 0; i < paramTypes.length; i++) {
/* 310 */       if (!ClassUtils.isAssignableValue(paramTypes[i], args[i])) {
/* 311 */         return Integer.MAX_VALUE;
/*     */       }
/* 313 */       if (args[i] != null) {
/* 314 */         Class<?> paramType = paramTypes[i];
/* 315 */         Class<?> superClass = args[i].getClass().getSuperclass();
/* 316 */         while (superClass != null) {
/* 317 */           if (paramType.equals(superClass)) {
/* 318 */             result += 2;
/* 319 */             superClass = null; continue;
/*     */           } 
/* 321 */           if (ClassUtils.isAssignable(paramType, superClass)) {
/* 322 */             result += 2;
/* 323 */             superClass = superClass.getSuperclass();
/*     */             continue;
/*     */           } 
/* 326 */           superClass = null;
/*     */         } 
/*     */         
/* 329 */         if (paramType.isInterface()) {
/* 330 */           result++;
/*     */         }
/*     */       } 
/*     */     } 
/* 334 */     return result;
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\springframewor\\util\MethodInvoker.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */