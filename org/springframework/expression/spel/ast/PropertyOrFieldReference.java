/*     */ package org.springframework.expression.spel.ast;
/*     */ 
/*     */ import java.lang.reflect.InvocationTargetException;
/*     */ import java.util.ArrayList;
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import org.springframework.asm.Label;
/*     */ import org.springframework.asm.MethodVisitor;
/*     */ import org.springframework.core.convert.TypeDescriptor;
/*     */ import org.springframework.expression.AccessException;
/*     */ import org.springframework.expression.EvaluationContext;
/*     */ import org.springframework.expression.EvaluationException;
/*     */ import org.springframework.expression.PropertyAccessor;
/*     */ import org.springframework.expression.TypedValue;
/*     */ import org.springframework.expression.spel.CodeFlow;
/*     */ import org.springframework.expression.spel.CompilablePropertyAccessor;
/*     */ import org.springframework.expression.spel.ExpressionState;
/*     */ import org.springframework.expression.spel.SpelEvaluationException;
/*     */ import org.springframework.expression.spel.SpelMessage;
/*     */ import org.springframework.expression.spel.support.ReflectivePropertyAccessor;
/*     */ import org.springframework.lang.Nullable;
/*     */ import org.springframework.util.Assert;
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
/*     */ public class PropertyOrFieldReference
/*     */   extends SpelNodeImpl
/*     */ {
/*     */   private final boolean nullSafe;
/*     */   private final String name;
/*     */   @Nullable
/*     */   private String originalPrimitiveExitTypeDescriptor;
/*     */   @Nullable
/*     */   private volatile PropertyAccessor cachedReadAccessor;
/*     */   @Nullable
/*     */   private volatile PropertyAccessor cachedWriteAccessor;
/*     */   
/*     */   public PropertyOrFieldReference(boolean nullSafe, String propertyOrFieldName, int startPos, int endPos) {
/*  68 */     super(startPos, endPos, new SpelNodeImpl[0]);
/*  69 */     this.nullSafe = nullSafe;
/*  70 */     this.name = propertyOrFieldName;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isNullSafe() {
/*  75 */     return this.nullSafe;
/*     */   }
/*     */   
/*     */   public String getName() {
/*  79 */     return this.name;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public ValueRef getValueRef(ExpressionState state) throws EvaluationException {
/*  85 */     return new AccessorLValue(this, state.getActiveContextObject(), state.getEvaluationContext(), state
/*  86 */         .getConfiguration().isAutoGrowNullReferences());
/*     */   }
/*     */ 
/*     */   
/*     */   public TypedValue getValueInternal(ExpressionState state) throws EvaluationException {
/*  91 */     TypedValue tv = getValueInternal(state.getActiveContextObject(), state.getEvaluationContext(), state
/*  92 */         .getConfiguration().isAutoGrowNullReferences());
/*  93 */     PropertyAccessor accessorToUse = this.cachedReadAccessor;
/*  94 */     if (accessorToUse instanceof CompilablePropertyAccessor) {
/*  95 */       CompilablePropertyAccessor accessor = (CompilablePropertyAccessor)accessorToUse;
/*  96 */       setExitTypeDescriptor(CodeFlow.toDescriptor(accessor.getPropertyType()));
/*     */     } 
/*  98 */     return tv;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private TypedValue getValueInternal(TypedValue contextObject, EvaluationContext evalContext, boolean isAutoGrowNullReferences) throws EvaluationException {
/* 104 */     TypedValue result = readProperty(contextObject, evalContext, this.name);
/*     */ 
/*     */     
/* 107 */     if (result.getValue() == null && isAutoGrowNullReferences && 
/* 108 */       nextChildIs(new Class[] { Indexer.class, PropertyOrFieldReference.class })) {
/* 109 */       TypeDescriptor resultDescriptor = result.getTypeDescriptor();
/* 110 */       Assert.state((resultDescriptor != null), "No result type");
/*     */       
/* 112 */       if (List.class == resultDescriptor.getType()) {
/* 113 */         if (isWritableProperty(this.name, contextObject, evalContext)) {
/* 114 */           List<?> newList = new ArrayList();
/* 115 */           writeProperty(contextObject, evalContext, this.name, newList);
/* 116 */           result = readProperty(contextObject, evalContext, this.name);
/*     */         }
/*     */       
/* 119 */       } else if (Map.class == resultDescriptor.getType()) {
/* 120 */         if (isWritableProperty(this.name, contextObject, evalContext)) {
/* 121 */           Map<?, ?> newMap = new HashMap<>();
/* 122 */           writeProperty(contextObject, evalContext, this.name, newMap);
/* 123 */           result = readProperty(contextObject, evalContext, this.name);
/*     */         } 
/*     */       } else {
/*     */ 
/*     */         
/*     */         try {
/* 129 */           if (isWritableProperty(this.name, contextObject, evalContext)) {
/* 130 */             Class<?> clazz = result.getTypeDescriptor().getType();
/* 131 */             Object newObject = ReflectionUtils.accessibleConstructor(clazz, new Class[0]).newInstance(new Object[0]);
/* 132 */             writeProperty(contextObject, evalContext, this.name, newObject);
/* 133 */             result = readProperty(contextObject, evalContext, this.name);
/*     */           }
/*     */         
/* 136 */         } catch (InvocationTargetException ex) {
/* 137 */           throw new SpelEvaluationException(getStartPosition(), ex.getTargetException(), SpelMessage.UNABLE_TO_DYNAMICALLY_CREATE_OBJECT, new Object[] { result
/* 138 */                 .getTypeDescriptor().getType() });
/*     */         }
/* 140 */         catch (Throwable ex) {
/* 141 */           throw new SpelEvaluationException(getStartPosition(), ex, SpelMessage.UNABLE_TO_DYNAMICALLY_CREATE_OBJECT, new Object[] { result
/* 142 */                 .getTypeDescriptor().getType() });
/*     */         } 
/*     */       } 
/*     */     } 
/* 146 */     return result;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setValue(ExpressionState state, @Nullable Object newValue) throws EvaluationException {
/* 151 */     writeProperty(state.getActiveContextObject(), state.getEvaluationContext(), this.name, newValue);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isWritable(ExpressionState state) throws EvaluationException {
/* 156 */     return isWritableProperty(this.name, state.getActiveContextObject(), state.getEvaluationContext());
/*     */   }
/*     */ 
/*     */   
/*     */   public String toStringAST() {
/* 161 */     return this.name;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private TypedValue readProperty(TypedValue contextObject, EvaluationContext evalContext, String name) throws EvaluationException {
/* 172 */     Object targetObject = contextObject.getValue();
/* 173 */     if (targetObject == null && this.nullSafe) {
/* 174 */       return TypedValue.NULL;
/*     */     }
/*     */     
/* 177 */     PropertyAccessor accessorToUse = this.cachedReadAccessor;
/* 178 */     if (accessorToUse != null) {
/* 179 */       if (evalContext.getPropertyAccessors().contains(accessorToUse)) {
/*     */         try {
/* 181 */           return accessorToUse.read(evalContext, contextObject.getValue(), name);
/*     */         }
/* 183 */         catch (Exception exception) {}
/*     */       }
/*     */ 
/*     */ 
/*     */       
/* 188 */       this.cachedReadAccessor = null;
/*     */     } 
/*     */ 
/*     */     
/* 192 */     List<PropertyAccessor> accessorsToTry = getPropertyAccessorsToTry(contextObject.getValue(), evalContext.getPropertyAccessors());
/*     */ 
/*     */ 
/*     */     
/*     */     try {
/* 197 */       for (PropertyAccessor accessor : accessorsToTry) {
/* 198 */         if (accessor.canRead(evalContext, contextObject.getValue(), name)) {
/* 199 */           if (accessor instanceof ReflectivePropertyAccessor) {
/* 200 */             accessor = ((ReflectivePropertyAccessor)accessor).createOptimalAccessor(evalContext, contextObject
/* 201 */                 .getValue(), name);
/*     */           }
/* 203 */           this.cachedReadAccessor = accessor;
/* 204 */           return accessor.read(evalContext, contextObject.getValue(), name);
/*     */         }
/*     */       
/*     */       } 
/* 208 */     } catch (Exception ex) {
/* 209 */       throw new SpelEvaluationException(ex, SpelMessage.EXCEPTION_DURING_PROPERTY_READ, new Object[] { name, ex.getMessage() });
/*     */     } 
/*     */     
/* 212 */     if (contextObject.getValue() == null) {
/* 213 */       throw new SpelEvaluationException(SpelMessage.PROPERTY_OR_FIELD_NOT_READABLE_ON_NULL, new Object[] { name });
/*     */     }
/*     */     
/* 216 */     throw new SpelEvaluationException(getStartPosition(), SpelMessage.PROPERTY_OR_FIELD_NOT_READABLE, new Object[] { name, 
/* 217 */           FormatHelper.formatClassNameForMessage(getObjectClass(contextObject.getValue())) });
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void writeProperty(TypedValue contextObject, EvaluationContext evalContext, String name, @Nullable Object newValue) throws EvaluationException {
/* 225 */     if (contextObject.getValue() == null && this.nullSafe) {
/*     */       return;
/*     */     }
/* 228 */     if (contextObject.getValue() == null) {
/* 229 */       throw new SpelEvaluationException(getStartPosition(), SpelMessage.PROPERTY_OR_FIELD_NOT_WRITABLE_ON_NULL, new Object[] { name });
/*     */     }
/*     */     
/* 232 */     PropertyAccessor accessorToUse = this.cachedWriteAccessor;
/* 233 */     if (accessorToUse != null) {
/* 234 */       if (evalContext.getPropertyAccessors().contains(accessorToUse)) {
/*     */         try {
/* 236 */           accessorToUse.write(evalContext, contextObject.getValue(), name, newValue);
/*     */           
/*     */           return;
/* 239 */         } catch (Exception exception) {}
/*     */       }
/*     */ 
/*     */ 
/*     */       
/* 244 */       this.cachedWriteAccessor = null;
/*     */     } 
/*     */ 
/*     */     
/* 248 */     List<PropertyAccessor> accessorsToTry = getPropertyAccessorsToTry(contextObject.getValue(), evalContext.getPropertyAccessors());
/*     */     try {
/* 250 */       for (PropertyAccessor accessor : accessorsToTry) {
/* 251 */         if (accessor.canWrite(evalContext, contextObject.getValue(), name)) {
/* 252 */           this.cachedWriteAccessor = accessor;
/* 253 */           accessor.write(evalContext, contextObject.getValue(), name, newValue);
/*     */           
/*     */           return;
/*     */         } 
/*     */       } 
/* 258 */     } catch (AccessException ex) {
/* 259 */       throw new SpelEvaluationException(getStartPosition(), ex, SpelMessage.EXCEPTION_DURING_PROPERTY_WRITE, new Object[] { name, ex
/* 260 */             .getMessage() });
/*     */     } 
/*     */     
/* 263 */     throw new SpelEvaluationException(getStartPosition(), SpelMessage.PROPERTY_OR_FIELD_NOT_WRITABLE, new Object[] { name, 
/* 264 */           FormatHelper.formatClassNameForMessage(getObjectClass(contextObject.getValue())) });
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isWritableProperty(String name, TypedValue contextObject, EvaluationContext evalContext) throws EvaluationException {
/* 270 */     Object value = contextObject.getValue();
/* 271 */     if (value != null) {
/*     */       
/* 273 */       List<PropertyAccessor> accessorsToTry = getPropertyAccessorsToTry(contextObject.getValue(), evalContext.getPropertyAccessors());
/* 274 */       for (PropertyAccessor accessor : accessorsToTry) {
/*     */         try {
/* 276 */           if (accessor.canWrite(evalContext, value, name)) {
/* 277 */             return true;
/*     */           }
/*     */         }
/* 280 */         catch (AccessException accessException) {}
/*     */       } 
/*     */     } 
/*     */ 
/*     */     
/* 285 */     return false;
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
/*     */   private List<PropertyAccessor> getPropertyAccessorsToTry(@Nullable Object contextObject, List<PropertyAccessor> propertyAccessors) {
/* 303 */     Class<?> targetType = (contextObject != null) ? contextObject.getClass() : null;
/*     */     
/* 305 */     List<PropertyAccessor> specificAccessors = new ArrayList<>();
/* 306 */     List<PropertyAccessor> generalAccessors = new ArrayList<>();
/* 307 */     for (PropertyAccessor resolver : propertyAccessors) {
/* 308 */       Class<?>[] targets = resolver.getSpecificTargetClasses();
/* 309 */       if (targets == null) {
/*     */         
/* 311 */         generalAccessors.add(resolver); continue;
/*     */       } 
/* 313 */       if (targetType != null) {
/* 314 */         for (Class<?> clazz : targets) {
/* 315 */           if (clazz == targetType) {
/* 316 */             specificAccessors.add(resolver);
/*     */             break;
/*     */           } 
/* 319 */           if (clazz.isAssignableFrom(targetType)) {
/* 320 */             generalAccessors.add(resolver);
/*     */           }
/*     */         } 
/*     */       }
/*     */     } 
/* 325 */     List<PropertyAccessor> resolvers = new ArrayList<>(specificAccessors);
/* 326 */     generalAccessors.removeAll(specificAccessors);
/* 327 */     resolvers.addAll(generalAccessors);
/* 328 */     return resolvers;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isCompilable() {
/* 333 */     PropertyAccessor accessorToUse = this.cachedReadAccessor;
/* 334 */     return (accessorToUse instanceof CompilablePropertyAccessor && ((CompilablePropertyAccessor)accessorToUse)
/* 335 */       .isCompilable());
/*     */   }
/*     */ 
/*     */   
/*     */   public void generateCode(MethodVisitor mv, CodeFlow cf) {
/* 340 */     PropertyAccessor accessorToUse = this.cachedReadAccessor;
/* 341 */     if (!(accessorToUse instanceof CompilablePropertyAccessor)) {
/* 342 */       throw new IllegalStateException("Property accessor is not compilable: " + accessorToUse);
/*     */     }
/*     */     
/* 345 */     Label skipIfNull = null;
/* 346 */     if (this.nullSafe) {
/* 347 */       mv.visitInsn(89);
/* 348 */       skipIfNull = new Label();
/* 349 */       Label continueLabel = new Label();
/* 350 */       mv.visitJumpInsn(199, continueLabel);
/* 351 */       CodeFlow.insertCheckCast(mv, this.exitTypeDescriptor);
/* 352 */       mv.visitJumpInsn(167, skipIfNull);
/* 353 */       mv.visitLabel(continueLabel);
/*     */     } 
/*     */     
/* 356 */     ((CompilablePropertyAccessor)accessorToUse).generateCode(this.name, mv, cf);
/* 357 */     cf.pushDescriptor(this.exitTypeDescriptor);
/*     */     
/* 359 */     if (this.originalPrimitiveExitTypeDescriptor != null)
/*     */     {
/*     */ 
/*     */       
/* 363 */       CodeFlow.insertBoxIfNecessary(mv, this.originalPrimitiveExitTypeDescriptor);
/*     */     }
/* 365 */     if (skipIfNull != null) {
/* 366 */       mv.visitLabel(skipIfNull);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   void setExitTypeDescriptor(String descriptor) {
/* 374 */     if (this.nullSafe && CodeFlow.isPrimitive(descriptor)) {
/* 375 */       this.originalPrimitiveExitTypeDescriptor = descriptor;
/* 376 */       this.exitTypeDescriptor = CodeFlow.toBoxedDescriptor(descriptor);
/*     */     } else {
/*     */       
/* 379 */       this.exitTypeDescriptor = descriptor;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private static class AccessorLValue
/*     */     implements ValueRef
/*     */   {
/*     */     private final PropertyOrFieldReference ref;
/*     */     
/*     */     private final TypedValue contextObject;
/*     */     
/*     */     private final EvaluationContext evalContext;
/*     */     
/*     */     private final boolean autoGrowNullReferences;
/*     */ 
/*     */     
/*     */     public AccessorLValue(PropertyOrFieldReference propertyOrFieldReference, TypedValue activeContextObject, EvaluationContext evalContext, boolean autoGrowNullReferences) {
/* 397 */       this.ref = propertyOrFieldReference;
/* 398 */       this.contextObject = activeContextObject;
/* 399 */       this.evalContext = evalContext;
/* 400 */       this.autoGrowNullReferences = autoGrowNullReferences;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public TypedValue getValue() {
/* 406 */       TypedValue value = this.ref.getValueInternal(this.contextObject, this.evalContext, this.autoGrowNullReferences);
/* 407 */       PropertyAccessor accessorToUse = this.ref.cachedReadAccessor;
/* 408 */       if (accessorToUse instanceof CompilablePropertyAccessor) {
/* 409 */         this.ref.setExitTypeDescriptor(CodeFlow.toDescriptor(((CompilablePropertyAccessor)accessorToUse).getPropertyType()));
/*     */       }
/* 411 */       return value;
/*     */     }
/*     */ 
/*     */     
/*     */     public void setValue(@Nullable Object newValue) {
/* 416 */       this.ref.writeProperty(this.contextObject, this.evalContext, this.ref.name, newValue);
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean isWritable() {
/* 421 */       return this.ref.isWritableProperty(this.ref.name, this.contextObject, this.evalContext);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\springframework\expression\spel\ast\PropertyOrFieldReference.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */