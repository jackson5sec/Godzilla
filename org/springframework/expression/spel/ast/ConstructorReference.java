/*     */ package org.springframework.expression.spel.ast;
/*     */ 
/*     */ import java.lang.reflect.Array;
/*     */ import java.lang.reflect.Constructor;
/*     */ import java.lang.reflect.Modifier;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import org.springframework.asm.MethodVisitor;
/*     */ import org.springframework.core.convert.TypeDescriptor;
/*     */ import org.springframework.expression.AccessException;
/*     */ import org.springframework.expression.ConstructorExecutor;
/*     */ import org.springframework.expression.ConstructorResolver;
/*     */ import org.springframework.expression.EvaluationContext;
/*     */ import org.springframework.expression.EvaluationException;
/*     */ import org.springframework.expression.TypeConverter;
/*     */ import org.springframework.expression.TypedValue;
/*     */ import org.springframework.expression.common.ExpressionUtils;
/*     */ import org.springframework.expression.spel.CodeFlow;
/*     */ import org.springframework.expression.spel.ExpressionState;
/*     */ import org.springframework.expression.spel.SpelEvaluationException;
/*     */ import org.springframework.expression.spel.SpelMessage;
/*     */ import org.springframework.expression.spel.SpelNode;
/*     */ import org.springframework.expression.spel.support.ReflectiveConstructorExecutor;
/*     */ import org.springframework.lang.Nullable;
/*     */ import org.springframework.util.Assert;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ConstructorReference
/*     */   extends SpelNodeImpl
/*     */ {
/*     */   private final boolean isArrayConstructor;
/*     */   @Nullable
/*     */   private SpelNodeImpl[] dimensions;
/*     */   @Nullable
/*     */   private volatile ConstructorExecutor cachedExecutor;
/*     */   
/*     */   public ConstructorReference(int startPos, int endPos, SpelNodeImpl... arguments) {
/*  76 */     super(startPos, endPos, arguments);
/*  77 */     this.isArrayConstructor = false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ConstructorReference(int startPos, int endPos, SpelNodeImpl[] dimensions, SpelNodeImpl... arguments) {
/*  85 */     super(startPos, endPos, arguments);
/*  86 */     this.isArrayConstructor = true;
/*  87 */     this.dimensions = dimensions;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public TypedValue getValueInternal(ExpressionState state) throws EvaluationException {
/*  96 */     if (this.isArrayConstructor) {
/*  97 */       return createArray(state);
/*     */     }
/*     */     
/* 100 */     return createNewInstance(state);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private TypedValue createNewInstance(ExpressionState state) throws EvaluationException {
/* 111 */     Object[] arguments = new Object[getChildCount() - 1];
/* 112 */     List<TypeDescriptor> argumentTypes = new ArrayList<>(getChildCount() - 1);
/* 113 */     for (int i = 0; i < arguments.length; i++) {
/* 114 */       TypedValue childValue = this.children[i + 1].getValueInternal(state);
/* 115 */       Object value = childValue.getValue();
/* 116 */       arguments[i] = value;
/* 117 */       argumentTypes.add(TypeDescriptor.forObject(value));
/*     */     } 
/*     */     
/* 120 */     ConstructorExecutor executorToUse = this.cachedExecutor;
/* 121 */     if (executorToUse != null) {
/*     */       try {
/* 123 */         return executorToUse.execute(state.getEvaluationContext(), arguments);
/*     */       }
/* 125 */       catch (AccessException ex) {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 136 */         if (ex.getCause() instanceof java.lang.reflect.InvocationTargetException) {
/*     */           
/* 138 */           Throwable rootCause = ex.getCause().getCause();
/* 139 */           if (rootCause instanceof RuntimeException) {
/* 140 */             throw (RuntimeException)rootCause;
/*     */           }
/*     */           
/* 143 */           String str = (String)this.children[0].getValueInternal(state).getValue();
/* 144 */           throw new SpelEvaluationException(getStartPosition(), rootCause, SpelMessage.CONSTRUCTOR_INVOCATION_PROBLEM, new Object[] { str, 
/*     */                 
/* 146 */                 FormatHelper.formatMethodForMessage("", argumentTypes) });
/*     */         } 
/*     */ 
/*     */ 
/*     */         
/* 151 */         this.cachedExecutor = null;
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/* 156 */     String typeName = (String)this.children[0].getValueInternal(state).getValue();
/* 157 */     Assert.state((typeName != null), "No type name");
/* 158 */     executorToUse = findExecutorForConstructor(typeName, argumentTypes, state);
/*     */     try {
/* 160 */       this.cachedExecutor = executorToUse;
/* 161 */       if (executorToUse instanceof ReflectiveConstructorExecutor) {
/* 162 */         this.exitTypeDescriptor = CodeFlow.toDescriptor(((ReflectiveConstructorExecutor)executorToUse)
/* 163 */             .getConstructor().getDeclaringClass());
/*     */       }
/*     */       
/* 166 */       return executorToUse.execute(state.getEvaluationContext(), arguments);
/*     */     }
/* 168 */     catch (AccessException ex) {
/* 169 */       throw new SpelEvaluationException(getStartPosition(), ex, SpelMessage.CONSTRUCTOR_INVOCATION_PROBLEM, new Object[] { typeName, 
/*     */             
/* 171 */             FormatHelper.formatMethodForMessage("", argumentTypes) });
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
/*     */ 
/*     */ 
/*     */   
/*     */   private ConstructorExecutor findExecutorForConstructor(String typeName, List<TypeDescriptor> argumentTypes, ExpressionState state) throws SpelEvaluationException {
/* 187 */     EvaluationContext evalContext = state.getEvaluationContext();
/* 188 */     List<ConstructorResolver> ctorResolvers = evalContext.getConstructorResolvers();
/* 189 */     for (ConstructorResolver ctorResolver : ctorResolvers) {
/*     */       try {
/* 191 */         ConstructorExecutor ce = ctorResolver.resolve(state.getEvaluationContext(), typeName, argumentTypes);
/* 192 */         if (ce != null) {
/* 193 */           return ce;
/*     */         }
/*     */       }
/* 196 */       catch (AccessException ex) {
/* 197 */         throw new SpelEvaluationException(getStartPosition(), ex, SpelMessage.CONSTRUCTOR_INVOCATION_PROBLEM, new Object[] { typeName, 
/*     */               
/* 199 */               FormatHelper.formatMethodForMessage("", argumentTypes) });
/*     */       } 
/*     */     } 
/* 202 */     throw new SpelEvaluationException(getStartPosition(), SpelMessage.CONSTRUCTOR_NOT_FOUND, new Object[] { typeName, 
/* 203 */           FormatHelper.formatMethodForMessage("", argumentTypes) });
/*     */   }
/*     */ 
/*     */   
/*     */   public String toStringAST() {
/* 208 */     StringBuilder sb = new StringBuilder("new ");
/* 209 */     int index = 0;
/* 210 */     sb.append(getChild(index++).toStringAST());
/* 211 */     sb.append('(');
/* 212 */     for (int i = index; i < getChildCount(); i++) {
/* 213 */       if (i > index) {
/* 214 */         sb.append(',');
/*     */       }
/* 216 */       sb.append(getChild(i).toStringAST());
/*     */     } 
/* 218 */     sb.append(')');
/* 219 */     return sb.toString();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private TypedValue createArray(ExpressionState state) throws EvaluationException {
/*     */     Class<?> componentType;
/* 230 */     Object intendedArrayType = getChild(0).getValue(state);
/* 231 */     if (!(intendedArrayType instanceof String)) {
/* 232 */       throw new SpelEvaluationException(getChild(0).getStartPosition(), SpelMessage.TYPE_NAME_EXPECTED_FOR_ARRAY_CONSTRUCTION, new Object[] {
/*     */             
/* 234 */             FormatHelper.formatClassNameForMessage((intendedArrayType != null) ? intendedArrayType
/* 235 */               .getClass() : null)
/*     */           });
/*     */     }
/* 238 */     String type = (String)intendedArrayType;
/*     */     
/* 240 */     TypeCode arrayTypeCode = TypeCode.forName(type);
/* 241 */     if (arrayTypeCode == TypeCode.OBJECT) {
/* 242 */       componentType = state.findType(type);
/*     */     } else {
/*     */       
/* 245 */       componentType = arrayTypeCode.getType();
/*     */     } 
/*     */     
/* 248 */     Object newArray = null;
/* 249 */     if (!hasInitializer()) {
/*     */       
/* 251 */       if (this.dimensions != null) {
/* 252 */         for (SpelNodeImpl dimension : this.dimensions) {
/* 253 */           if (dimension == null) {
/* 254 */             throw new SpelEvaluationException(getStartPosition(), SpelMessage.MISSING_ARRAY_DIMENSION, new Object[0]);
/*     */           }
/*     */         } 
/* 257 */         TypeConverter typeConverter = state.getEvaluationContext().getTypeConverter();
/* 258 */         if (this.dimensions.length == 1)
/*     */         {
/* 260 */           TypedValue o = this.dimensions[0].getTypedValue(state);
/* 261 */           int arraySize = ExpressionUtils.toInt(typeConverter, o);
/* 262 */           newArray = Array.newInstance(componentType, arraySize);
/*     */         }
/*     */         else
/*     */         {
/* 266 */           int[] dims = new int[this.dimensions.length];
/* 267 */           for (int d = 0; d < this.dimensions.length; d++) {
/* 268 */             TypedValue o = this.dimensions[d].getTypedValue(state);
/* 269 */             dims[d] = ExpressionUtils.toInt(typeConverter, o);
/*     */           } 
/* 271 */           newArray = Array.newInstance(componentType, dims);
/*     */         }
/*     */       
/*     */       } 
/*     */     } else {
/*     */       
/* 277 */       if (this.dimensions == null || this.dimensions.length > 1)
/*     */       {
/*     */         
/* 280 */         throw new SpelEvaluationException(getStartPosition(), SpelMessage.MULTIDIM_ARRAY_INITIALIZER_NOT_SUPPORTED, new Object[0]);
/*     */       }
/*     */       
/* 283 */       TypeConverter typeConverter = state.getEvaluationContext().getTypeConverter();
/* 284 */       InlineList initializer = (InlineList)getChild(1);
/*     */       
/* 286 */       if (this.dimensions[0] != null) {
/* 287 */         TypedValue dValue = this.dimensions[0].getTypedValue(state);
/* 288 */         int i = ExpressionUtils.toInt(typeConverter, dValue);
/* 289 */         if (i != initializer.getChildCount()) {
/* 290 */           throw new SpelEvaluationException(getStartPosition(), SpelMessage.INITIALIZER_LENGTH_INCORRECT, new Object[0]);
/*     */         }
/*     */       } 
/*     */       
/* 294 */       int arraySize = initializer.getChildCount();
/* 295 */       newArray = Array.newInstance(componentType, arraySize);
/* 296 */       if (arrayTypeCode == TypeCode.OBJECT) {
/* 297 */         populateReferenceTypeArray(state, newArray, typeConverter, initializer, componentType);
/*     */       }
/* 299 */       else if (arrayTypeCode == TypeCode.BOOLEAN) {
/* 300 */         populateBooleanArray(state, newArray, typeConverter, initializer);
/*     */       }
/* 302 */       else if (arrayTypeCode == TypeCode.BYTE) {
/* 303 */         populateByteArray(state, newArray, typeConverter, initializer);
/*     */       }
/* 305 */       else if (arrayTypeCode == TypeCode.CHAR) {
/* 306 */         populateCharArray(state, newArray, typeConverter, initializer);
/*     */       }
/* 308 */       else if (arrayTypeCode == TypeCode.DOUBLE) {
/* 309 */         populateDoubleArray(state, newArray, typeConverter, initializer);
/*     */       }
/* 311 */       else if (arrayTypeCode == TypeCode.FLOAT) {
/* 312 */         populateFloatArray(state, newArray, typeConverter, initializer);
/*     */       }
/* 314 */       else if (arrayTypeCode == TypeCode.INT) {
/* 315 */         populateIntArray(state, newArray, typeConverter, initializer);
/*     */       }
/* 317 */       else if (arrayTypeCode == TypeCode.LONG) {
/* 318 */         populateLongArray(state, newArray, typeConverter, initializer);
/*     */       }
/* 320 */       else if (arrayTypeCode == TypeCode.SHORT) {
/* 321 */         populateShortArray(state, newArray, typeConverter, initializer);
/*     */       } else {
/*     */         
/* 324 */         throw new IllegalStateException(arrayTypeCode.name());
/*     */       } 
/*     */     } 
/* 327 */     return new TypedValue(newArray);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private void populateReferenceTypeArray(ExpressionState state, Object newArray, TypeConverter typeConverter, InlineList initializer, Class<?> componentType) {
/* 333 */     TypeDescriptor toTypeDescriptor = TypeDescriptor.valueOf(componentType);
/* 334 */     Object[] newObjectArray = (Object[])newArray;
/* 335 */     for (int i = 0; i < newObjectArray.length; i++) {
/* 336 */       SpelNode elementNode = initializer.getChild(i);
/* 337 */       Object arrayEntry = elementNode.getValue(state);
/* 338 */       newObjectArray[i] = typeConverter.convertValue(arrayEntry, 
/* 339 */           TypeDescriptor.forObject(arrayEntry), toTypeDescriptor);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private void populateByteArray(ExpressionState state, Object newArray, TypeConverter typeConverter, InlineList initializer) {
/* 346 */     byte[] newByteArray = (byte[])newArray;
/* 347 */     for (int i = 0; i < newByteArray.length; i++) {
/* 348 */       TypedValue typedValue = initializer.getChild(i).getTypedValue(state);
/* 349 */       newByteArray[i] = ExpressionUtils.toByte(typeConverter, typedValue);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private void populateFloatArray(ExpressionState state, Object newArray, TypeConverter typeConverter, InlineList initializer) {
/* 356 */     float[] newFloatArray = (float[])newArray;
/* 357 */     for (int i = 0; i < newFloatArray.length; i++) {
/* 358 */       TypedValue typedValue = initializer.getChild(i).getTypedValue(state);
/* 359 */       newFloatArray[i] = ExpressionUtils.toFloat(typeConverter, typedValue);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private void populateDoubleArray(ExpressionState state, Object newArray, TypeConverter typeConverter, InlineList initializer) {
/* 366 */     double[] newDoubleArray = (double[])newArray;
/* 367 */     for (int i = 0; i < newDoubleArray.length; i++) {
/* 368 */       TypedValue typedValue = initializer.getChild(i).getTypedValue(state);
/* 369 */       newDoubleArray[i] = ExpressionUtils.toDouble(typeConverter, typedValue);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private void populateShortArray(ExpressionState state, Object newArray, TypeConverter typeConverter, InlineList initializer) {
/* 376 */     short[] newShortArray = (short[])newArray;
/* 377 */     for (int i = 0; i < newShortArray.length; i++) {
/* 378 */       TypedValue typedValue = initializer.getChild(i).getTypedValue(state);
/* 379 */       newShortArray[i] = ExpressionUtils.toShort(typeConverter, typedValue);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private void populateLongArray(ExpressionState state, Object newArray, TypeConverter typeConverter, InlineList initializer) {
/* 386 */     long[] newLongArray = (long[])newArray;
/* 387 */     for (int i = 0; i < newLongArray.length; i++) {
/* 388 */       TypedValue typedValue = initializer.getChild(i).getTypedValue(state);
/* 389 */       newLongArray[i] = ExpressionUtils.toLong(typeConverter, typedValue);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private void populateCharArray(ExpressionState state, Object newArray, TypeConverter typeConverter, InlineList initializer) {
/* 396 */     char[] newCharArray = (char[])newArray;
/* 397 */     for (int i = 0; i < newCharArray.length; i++) {
/* 398 */       TypedValue typedValue = initializer.getChild(i).getTypedValue(state);
/* 399 */       newCharArray[i] = ExpressionUtils.toChar(typeConverter, typedValue);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private void populateBooleanArray(ExpressionState state, Object newArray, TypeConverter typeConverter, InlineList initializer) {
/* 406 */     boolean[] newBooleanArray = (boolean[])newArray;
/* 407 */     for (int i = 0; i < newBooleanArray.length; i++) {
/* 408 */       TypedValue typedValue = initializer.getChild(i).getTypedValue(state);
/* 409 */       newBooleanArray[i] = ExpressionUtils.toBoolean(typeConverter, typedValue);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private void populateIntArray(ExpressionState state, Object newArray, TypeConverter typeConverter, InlineList initializer) {
/* 416 */     int[] newIntArray = (int[])newArray;
/* 417 */     for (int i = 0; i < newIntArray.length; i++) {
/* 418 */       TypedValue typedValue = initializer.getChild(i).getTypedValue(state);
/* 419 */       newIntArray[i] = ExpressionUtils.toInt(typeConverter, typedValue);
/*     */     } 
/*     */   }
/*     */   
/*     */   private boolean hasInitializer() {
/* 424 */     return (getChildCount() > 1);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isCompilable() {
/* 429 */     if (!(this.cachedExecutor instanceof ReflectiveConstructorExecutor) || this.exitTypeDescriptor == null)
/*     */     {
/* 431 */       return false;
/*     */     }
/*     */     
/* 434 */     if (getChildCount() > 1) {
/* 435 */       for (int c = 1, max = getChildCount(); c < max; c++) {
/* 436 */         if (!this.children[c].isCompilable()) {
/* 437 */           return false;
/*     */         }
/*     */       } 
/*     */     }
/*     */     
/* 442 */     ReflectiveConstructorExecutor executor = (ReflectiveConstructorExecutor)this.cachedExecutor;
/* 443 */     if (executor == null) {
/* 444 */       return false;
/*     */     }
/* 446 */     Constructor<?> constructor = executor.getConstructor();
/* 447 */     return (Modifier.isPublic(constructor.getModifiers()) && 
/* 448 */       Modifier.isPublic(constructor.getDeclaringClass().getModifiers()));
/*     */   }
/*     */ 
/*     */   
/*     */   public void generateCode(MethodVisitor mv, CodeFlow cf) {
/* 453 */     ReflectiveConstructorExecutor executor = (ReflectiveConstructorExecutor)this.cachedExecutor;
/* 454 */     Assert.state((executor != null), "No cached executor");
/*     */     
/* 456 */     Constructor<?> constructor = executor.getConstructor();
/* 457 */     String classDesc = constructor.getDeclaringClass().getName().replace('.', '/');
/* 458 */     mv.visitTypeInsn(187, classDesc);
/* 459 */     mv.visitInsn(89);
/*     */ 
/*     */     
/* 462 */     SpelNodeImpl[] arguments = new SpelNodeImpl[this.children.length - 1];
/* 463 */     System.arraycopy(this.children, 1, arguments, 0, this.children.length - 1);
/* 464 */     generateCodeForArguments(mv, cf, constructor, arguments);
/* 465 */     mv.visitMethodInsn(183, classDesc, "<init>", CodeFlow.createSignatureDescriptor(constructor), false);
/* 466 */     cf.pushDescriptor(this.exitTypeDescriptor);
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\springframework\expression\spel\ast\ConstructorReference.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */