/*     */ package org.springframework.expression.spel.ast;
/*     */ 
/*     */ import java.lang.reflect.Method;
/*     */ import java.lang.reflect.Modifier;
/*     */ import java.lang.reflect.Proxy;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collections;
/*     */ import java.util.List;
/*     */ import java.util.StringJoiner;
/*     */ import org.springframework.asm.Label;
/*     */ import org.springframework.asm.MethodVisitor;
/*     */ import org.springframework.core.convert.TypeDescriptor;
/*     */ import org.springframework.expression.AccessException;
/*     */ import org.springframework.expression.EvaluationContext;
/*     */ import org.springframework.expression.EvaluationException;
/*     */ import org.springframework.expression.ExpressionInvocationTargetException;
/*     */ import org.springframework.expression.MethodExecutor;
/*     */ import org.springframework.expression.MethodResolver;
/*     */ import org.springframework.expression.TypedValue;
/*     */ import org.springframework.expression.spel.CodeFlow;
/*     */ import org.springframework.expression.spel.ExpressionState;
/*     */ import org.springframework.expression.spel.SpelEvaluationException;
/*     */ import org.springframework.expression.spel.SpelMessage;
/*     */ import org.springframework.expression.spel.support.ReflectiveMethodExecutor;
/*     */ import org.springframework.lang.Nullable;
/*     */ import org.springframework.util.Assert;
/*     */ import org.springframework.util.ObjectUtils;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class MethodReference
/*     */   extends SpelNodeImpl
/*     */ {
/*     */   private final String name;
/*     */   private final boolean nullSafe;
/*     */   @Nullable
/*     */   private String originalPrimitiveExitTypeDescriptor;
/*     */   @Nullable
/*     */   private volatile CachedMethodExecutor cachedExecutor;
/*     */   
/*     */   public MethodReference(boolean nullSafe, String methodName, int startPos, int endPos, SpelNodeImpl... arguments) {
/*  69 */     super(startPos, endPos, arguments);
/*  70 */     this.name = methodName;
/*  71 */     this.nullSafe = nullSafe;
/*     */   }
/*     */ 
/*     */   
/*     */   public final String getName() {
/*  76 */     return this.name;
/*     */   }
/*     */ 
/*     */   
/*     */   protected ValueRef getValueRef(ExpressionState state) throws EvaluationException {
/*  81 */     Object[] arguments = getArguments(state);
/*  82 */     if (state.getActiveContextObject().getValue() == null) {
/*  83 */       throwIfNotNullSafe(getArgumentTypes(arguments));
/*  84 */       return ValueRef.NullValueRef.INSTANCE;
/*     */     } 
/*  86 */     return new MethodValueRef(state, arguments);
/*     */   }
/*     */ 
/*     */   
/*     */   public TypedValue getValueInternal(ExpressionState state) throws EvaluationException {
/*  91 */     EvaluationContext evaluationContext = state.getEvaluationContext();
/*  92 */     Object value = state.getActiveContextObject().getValue();
/*  93 */     TypeDescriptor targetType = state.getActiveContextObject().getTypeDescriptor();
/*  94 */     Object[] arguments = getArguments(state);
/*  95 */     TypedValue result = getValueInternal(evaluationContext, value, targetType, arguments);
/*  96 */     updateExitTypeDescriptor();
/*  97 */     return result;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private TypedValue getValueInternal(EvaluationContext evaluationContext, @Nullable Object value, @Nullable TypeDescriptor targetType, Object[] arguments) {
/* 103 */     List<TypeDescriptor> argumentTypes = getArgumentTypes(arguments);
/* 104 */     if (value == null) {
/* 105 */       throwIfNotNullSafe(argumentTypes);
/* 106 */       return TypedValue.NULL;
/*     */     } 
/*     */     
/* 109 */     MethodExecutor executorToUse = getCachedExecutor(evaluationContext, value, targetType, argumentTypes);
/* 110 */     if (executorToUse != null) {
/*     */       try {
/* 112 */         return executorToUse.execute(evaluationContext, value, arguments);
/*     */       }
/* 114 */       catch (AccessException ex) {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 126 */         throwSimpleExceptionIfPossible(value, ex);
/*     */ 
/*     */ 
/*     */         
/* 130 */         this.cachedExecutor = null;
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/* 135 */     executorToUse = findAccessorForMethod(argumentTypes, value, evaluationContext);
/* 136 */     this.cachedExecutor = new CachedMethodExecutor(executorToUse, (value instanceof Class) ? (Class)value : null, targetType, argumentTypes);
/*     */     
/*     */     try {
/* 139 */       return executorToUse.execute(evaluationContext, value, arguments);
/*     */     }
/* 141 */     catch (AccessException ex) {
/*     */       
/* 143 */       throwSimpleExceptionIfPossible(value, ex);
/* 144 */       throw new SpelEvaluationException(getStartPosition(), ex, SpelMessage.EXCEPTION_DURING_METHOD_INVOCATION, new Object[] { this.name, value
/*     */             
/* 146 */             .getClass().getName(), ex.getMessage() });
/*     */     } 
/*     */   }
/*     */   
/*     */   private void throwIfNotNullSafe(List<TypeDescriptor> argumentTypes) {
/* 151 */     if (!this.nullSafe)
/* 152 */       throw new SpelEvaluationException(getStartPosition(), SpelMessage.METHOD_CALL_ON_NULL_OBJECT_NOT_ALLOWED, new Object[] {
/*     */             
/* 154 */             FormatHelper.formatMethodForMessage(this.name, argumentTypes)
/*     */           }); 
/*     */   }
/*     */   
/*     */   private Object[] getArguments(ExpressionState state) {
/* 159 */     Object[] arguments = new Object[getChildCount()];
/* 160 */     for (int i = 0; i < arguments.length; i++) {
/*     */ 
/*     */       
/* 163 */       try { state.pushActiveContextObject(state.getScopeRootContextObject());
/* 164 */         arguments[i] = this.children[i].getValueInternal(state).getValue();
/*     */ 
/*     */         
/* 167 */         state.popActiveContextObject(); } finally { state.popActiveContextObject(); }
/*     */     
/*     */     } 
/* 170 */     return arguments;
/*     */   }
/*     */   
/*     */   private List<TypeDescriptor> getArgumentTypes(Object... arguments) {
/* 174 */     List<TypeDescriptor> descriptors = new ArrayList<>(arguments.length);
/* 175 */     for (Object argument : arguments) {
/* 176 */       descriptors.add(TypeDescriptor.forObject(argument));
/*     */     }
/* 178 */     return Collections.unmodifiableList(descriptors);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   private MethodExecutor getCachedExecutor(EvaluationContext evaluationContext, Object value, @Nullable TypeDescriptor target, List<TypeDescriptor> argumentTypes) {
/* 185 */     List<MethodResolver> methodResolvers = evaluationContext.getMethodResolvers();
/* 186 */     if (methodResolvers.size() != 1 || !(methodResolvers.get(0) instanceof org.springframework.expression.spel.support.ReflectiveMethodResolver))
/*     */     {
/* 188 */       return null;
/*     */     }
/*     */     
/* 191 */     CachedMethodExecutor executorToCheck = this.cachedExecutor;
/* 192 */     if (executorToCheck != null && executorToCheck.isSuitable(value, target, argumentTypes)) {
/* 193 */       return executorToCheck.get();
/*     */     }
/* 195 */     this.cachedExecutor = null;
/* 196 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private MethodExecutor findAccessorForMethod(List<TypeDescriptor> argumentTypes, Object targetObject, EvaluationContext evaluationContext) throws SpelEvaluationException {
/* 202 */     AccessException accessException = null;
/* 203 */     List<MethodResolver> methodResolvers = evaluationContext.getMethodResolvers();
/* 204 */     for (MethodResolver methodResolver : methodResolvers) {
/*     */       try {
/* 206 */         MethodExecutor methodExecutor = methodResolver.resolve(evaluationContext, targetObject, this.name, argumentTypes);
/*     */         
/* 208 */         if (methodExecutor != null) {
/* 209 */           return methodExecutor;
/*     */         }
/*     */       }
/* 212 */       catch (AccessException ex) {
/* 213 */         accessException = ex;
/*     */         
/*     */         break;
/*     */       } 
/*     */     } 
/* 218 */     String method = FormatHelper.formatMethodForMessage(this.name, argumentTypes);
/* 219 */     String className = FormatHelper.formatClassNameForMessage((targetObject instanceof Class) ? (Class)targetObject : targetObject
/* 220 */         .getClass());
/* 221 */     if (accessException != null) {
/* 222 */       throw new SpelEvaluationException(
/* 223 */           getStartPosition(), accessException, SpelMessage.PROBLEM_LOCATING_METHOD, new Object[] { method, className });
/*     */     }
/*     */     
/* 226 */     throw new SpelEvaluationException(getStartPosition(), SpelMessage.METHOD_NOT_FOUND, new Object[] { method, className });
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void throwSimpleExceptionIfPossible(Object value, AccessException ex) {
/* 235 */     if (ex.getCause() instanceof java.lang.reflect.InvocationTargetException) {
/* 236 */       Throwable rootCause = ex.getCause().getCause();
/* 237 */       if (rootCause instanceof RuntimeException) {
/* 238 */         throw (RuntimeException)rootCause;
/*     */       }
/* 240 */       throw new ExpressionInvocationTargetException(getStartPosition(), "A problem occurred when trying to execute method '" + this.name + "' on object of type [" + value
/*     */           
/* 242 */           .getClass().getName() + "]", rootCause);
/*     */     } 
/*     */   }
/*     */   
/*     */   private void updateExitTypeDescriptor() {
/* 247 */     CachedMethodExecutor executorToCheck = this.cachedExecutor;
/* 248 */     if (executorToCheck != null && executorToCheck.get() instanceof ReflectiveMethodExecutor) {
/* 249 */       Method method = ((ReflectiveMethodExecutor)executorToCheck.get()).getMethod();
/* 250 */       String descriptor = CodeFlow.toDescriptor(method.getReturnType());
/* 251 */       if (this.nullSafe && CodeFlow.isPrimitive(descriptor)) {
/* 252 */         this.originalPrimitiveExitTypeDescriptor = descriptor;
/* 253 */         this.exitTypeDescriptor = CodeFlow.toBoxedDescriptor(descriptor);
/*     */       } else {
/*     */         
/* 256 */         this.exitTypeDescriptor = descriptor;
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public String toStringAST() {
/* 263 */     StringJoiner sj = new StringJoiner(",", "(", ")");
/* 264 */     for (int i = 0; i < getChildCount(); i++) {
/* 265 */       sj.add(getChild(i).toStringAST());
/*     */     }
/* 267 */     return this.name + sj.toString();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isCompilable() {
/* 276 */     CachedMethodExecutor executorToCheck = this.cachedExecutor;
/* 277 */     if (executorToCheck == null || executorToCheck.hasProxyTarget() || 
/* 278 */       !(executorToCheck.get() instanceof ReflectiveMethodExecutor)) {
/* 279 */       return false;
/*     */     }
/*     */     
/* 282 */     for (SpelNodeImpl child : this.children) {
/* 283 */       if (!child.isCompilable()) {
/* 284 */         return false;
/*     */       }
/*     */     } 
/*     */     
/* 288 */     ReflectiveMethodExecutor executor = (ReflectiveMethodExecutor)executorToCheck.get();
/* 289 */     if (executor.didArgumentConversionOccur()) {
/* 290 */       return false;
/*     */     }
/* 292 */     Class<?> clazz = executor.getMethod().getDeclaringClass();
/* 293 */     if (!Modifier.isPublic(clazz.getModifiers()) && executor.getPublicDeclaringClass() == null) {
/* 294 */       return false;
/*     */     }
/*     */     
/* 297 */     return true;
/*     */   }
/*     */   
/*     */   public void generateCode(MethodVisitor mv, CodeFlow cf) {
/*     */     String classDesc;
/* 302 */     CachedMethodExecutor executorToCheck = this.cachedExecutor;
/* 303 */     if (executorToCheck == null || !(executorToCheck.get() instanceof ReflectiveMethodExecutor)) {
/* 304 */       throw new IllegalStateException("No applicable cached executor found: " + executorToCheck);
/*     */     }
/*     */     
/* 307 */     ReflectiveMethodExecutor methodExecutor = (ReflectiveMethodExecutor)executorToCheck.get();
/* 308 */     Method method = methodExecutor.getMethod();
/* 309 */     boolean isStaticMethod = Modifier.isStatic(method.getModifiers());
/* 310 */     String descriptor = cf.lastDescriptor();
/*     */     
/* 312 */     Label skipIfNull = null;
/* 313 */     if (descriptor == null && !isStaticMethod)
/*     */     {
/* 315 */       cf.loadTarget(mv);
/*     */     }
/* 317 */     if ((descriptor != null || !isStaticMethod) && this.nullSafe) {
/* 318 */       mv.visitInsn(89);
/* 319 */       skipIfNull = new Label();
/* 320 */       Label continueLabel = new Label();
/* 321 */       mv.visitJumpInsn(199, continueLabel);
/* 322 */       CodeFlow.insertCheckCast(mv, this.exitTypeDescriptor);
/* 323 */       mv.visitJumpInsn(167, skipIfNull);
/* 324 */       mv.visitLabel(continueLabel);
/*     */     } 
/* 326 */     if (descriptor != null && isStaticMethod)
/*     */     {
/* 328 */       mv.visitInsn(87);
/*     */     }
/*     */     
/* 331 */     if (CodeFlow.isPrimitive(descriptor)) {
/* 332 */       CodeFlow.insertBoxIfNecessary(mv, descriptor.charAt(0));
/*     */     }
/*     */ 
/*     */     
/* 336 */     if (Modifier.isPublic(method.getDeclaringClass().getModifiers())) {
/* 337 */       classDesc = method.getDeclaringClass().getName().replace('.', '/');
/*     */     } else {
/*     */       
/* 340 */       Class<?> publicDeclaringClass = methodExecutor.getPublicDeclaringClass();
/* 341 */       Assert.state((publicDeclaringClass != null), "No public declaring class");
/* 342 */       classDesc = publicDeclaringClass.getName().replace('.', '/');
/*     */     } 
/*     */     
/* 345 */     if (!isStaticMethod && (descriptor == null || !descriptor.substring(1).equals(classDesc))) {
/* 346 */       CodeFlow.insertCheckCast(mv, "L" + classDesc);
/*     */     }
/*     */     
/* 349 */     generateCodeForArguments(mv, cf, method, this.children);
/* 350 */     mv.visitMethodInsn(isStaticMethod ? 184 : (method.isDefault() ? 185 : 182), classDesc, method
/* 351 */         .getName(), CodeFlow.createSignatureDescriptor(method), method
/* 352 */         .getDeclaringClass().isInterface());
/* 353 */     cf.pushDescriptor(this.exitTypeDescriptor);
/*     */     
/* 355 */     if (this.originalPrimitiveExitTypeDescriptor != null)
/*     */     {
/*     */       
/* 358 */       CodeFlow.insertBoxIfNecessary(mv, this.originalPrimitiveExitTypeDescriptor);
/*     */     }
/* 360 */     if (skipIfNull != null) {
/* 361 */       mv.visitLabel(skipIfNull);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   private class MethodValueRef
/*     */     implements ValueRef
/*     */   {
/*     */     private final EvaluationContext evaluationContext;
/*     */     
/*     */     @Nullable
/*     */     private final Object value;
/*     */     
/*     */     @Nullable
/*     */     private final TypeDescriptor targetType;
/*     */     private final Object[] arguments;
/*     */     
/*     */     public MethodValueRef(ExpressionState state, Object[] arguments) {
/* 379 */       this.evaluationContext = state.getEvaluationContext();
/* 380 */       this.value = state.getActiveContextObject().getValue();
/* 381 */       this.targetType = state.getActiveContextObject().getTypeDescriptor();
/* 382 */       this.arguments = arguments;
/*     */     }
/*     */ 
/*     */     
/*     */     public TypedValue getValue() {
/* 387 */       TypedValue result = MethodReference.this.getValueInternal(this.evaluationContext, this.value, this.targetType, this.arguments);
/*     */       
/* 389 */       MethodReference.this.updateExitTypeDescriptor();
/* 390 */       return result;
/*     */     }
/*     */ 
/*     */     
/*     */     public void setValue(@Nullable Object newValue) {
/* 395 */       throw new IllegalAccessError();
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean isWritable() {
/* 400 */       return false;
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static class CachedMethodExecutor
/*     */   {
/*     */     private final MethodExecutor methodExecutor;
/*     */     
/*     */     @Nullable
/*     */     private final Class<?> staticClass;
/*     */     
/*     */     @Nullable
/*     */     private final TypeDescriptor target;
/*     */     
/*     */     private final List<TypeDescriptor> argumentTypes;
/*     */ 
/*     */     
/*     */     public CachedMethodExecutor(MethodExecutor methodExecutor, @Nullable Class<?> staticClass, @Nullable TypeDescriptor target, List<TypeDescriptor> argumentTypes) {
/* 420 */       this.methodExecutor = methodExecutor;
/* 421 */       this.staticClass = staticClass;
/* 422 */       this.target = target;
/* 423 */       this.argumentTypes = argumentTypes;
/*     */     }
/*     */     
/*     */     public boolean isSuitable(Object value, @Nullable TypeDescriptor target, List<TypeDescriptor> argumentTypes) {
/* 427 */       return ((this.staticClass == null || this.staticClass == value) && 
/* 428 */         ObjectUtils.nullSafeEquals(this.target, target) && this.argumentTypes.equals(argumentTypes));
/*     */     }
/*     */     
/*     */     public boolean hasProxyTarget() {
/* 432 */       return (this.target != null && Proxy.isProxyClass(this.target.getType()));
/*     */     }
/*     */     
/*     */     public MethodExecutor get() {
/* 436 */       return this.methodExecutor;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\springframework\expression\spel\ast\MethodReference.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */