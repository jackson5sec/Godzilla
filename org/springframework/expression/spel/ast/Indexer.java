/*     */ package org.springframework.expression.spel.ast;
/*     */ 
/*     */ import java.lang.reflect.Constructor;
/*     */ import java.lang.reflect.Field;
/*     */ import java.lang.reflect.Member;
/*     */ import java.lang.reflect.Method;
/*     */ import java.lang.reflect.Modifier;
/*     */ import java.util.Collection;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.StringJoiner;
/*     */ import org.springframework.asm.MethodVisitor;
/*     */ import org.springframework.core.convert.TypeDescriptor;
/*     */ import org.springframework.expression.AccessException;
/*     */ import org.springframework.expression.EvaluationContext;
/*     */ import org.springframework.expression.EvaluationException;
/*     */ import org.springframework.expression.PropertyAccessor;
/*     */ import org.springframework.expression.TypeConverter;
/*     */ import org.springframework.expression.TypedValue;
/*     */ import org.springframework.expression.spel.CodeFlow;
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
/*     */ public class Indexer
/*     */   extends SpelNodeImpl
/*     */ {
/*     */   @Nullable
/*     */   private String cachedReadName;
/*     */   @Nullable
/*     */   private Class<?> cachedReadTargetType;
/*     */   @Nullable
/*     */   private PropertyAccessor cachedReadAccessor;
/*     */   @Nullable
/*     */   private String cachedWriteName;
/*     */   @Nullable
/*     */   private Class<?> cachedWriteTargetType;
/*     */   @Nullable
/*     */   private PropertyAccessor cachedWriteAccessor;
/*     */   @Nullable
/*     */   private IndexedType indexedType;
/*     */   
/*     */   private enum IndexedType
/*     */   {
/*  59 */     ARRAY, LIST, MAP, STRING, OBJECT;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Indexer(int startPos, int endPos, SpelNodeImpl expr) {
/*  95 */     super(startPos, endPos, new SpelNodeImpl[] { expr });
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public TypedValue getValueInternal(ExpressionState state) throws EvaluationException {
/* 101 */     return getValueRef(state).getValue();
/*     */   }
/*     */ 
/*     */   
/*     */   public void setValue(ExpressionState state, @Nullable Object newValue) throws EvaluationException {
/* 106 */     getValueRef(state).setValue(newValue);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isWritable(ExpressionState expressionState) throws SpelEvaluationException {
/* 111 */     return true;
/*     */   }
/*     */   
/*     */   protected ValueRef getValueRef(ExpressionState state) throws EvaluationException {
/*     */     TypedValue indexValue;
/*     */     Object index;
/* 117 */     TypedValue context = state.getActiveContextObject();
/* 118 */     Object target = context.getValue();
/* 119 */     TypeDescriptor targetDescriptor = context.getTypeDescriptor();
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 124 */     if (target instanceof Map && this.children[0] instanceof PropertyOrFieldReference) {
/* 125 */       PropertyOrFieldReference reference = (PropertyOrFieldReference)this.children[0];
/* 126 */       index = reference.getName();
/* 127 */       indexValue = new TypedValue(index);
/*     */     } else {
/*     */ 
/*     */       
/*     */       try {
/*     */         
/* 133 */         state.pushActiveContextObject(state.getRootContextObject());
/* 134 */         indexValue = this.children[0].getValueInternal(state);
/* 135 */         index = indexValue.getValue();
/* 136 */         Assert.state((index != null), "No index");
/*     */       } finally {
/*     */         
/* 139 */         state.popActiveContextObject();
/*     */       } 
/*     */     } 
/*     */ 
/*     */     
/* 144 */     if (target == null) {
/* 145 */       throw new SpelEvaluationException(getStartPosition(), SpelMessage.CANNOT_INDEX_INTO_NULL_VALUE, new Object[0]);
/*     */     }
/*     */     
/* 148 */     Assert.state((targetDescriptor != null), "No type descriptor");
/*     */ 
/*     */     
/* 151 */     if (target instanceof Map) {
/* 152 */       Object key = index;
/* 153 */       if (targetDescriptor.getMapKeyTypeDescriptor() != null) {
/* 154 */         key = state.convertValue(key, targetDescriptor.getMapKeyTypeDescriptor());
/*     */       }
/* 156 */       this.indexedType = IndexedType.MAP;
/* 157 */       return new MapIndexingValueRef(state.getTypeConverter(), (Map)target, key, targetDescriptor);
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/* 162 */     if (target.getClass().isArray() || target instanceof Collection || target instanceof String) {
/* 163 */       int idx = ((Integer)state.convertValue(index, TypeDescriptor.valueOf(Integer.class))).intValue();
/* 164 */       if (target.getClass().isArray()) {
/* 165 */         this.indexedType = IndexedType.ARRAY;
/* 166 */         return new ArrayIndexingValueRef(state.getTypeConverter(), target, idx, targetDescriptor);
/*     */       } 
/* 168 */       if (target instanceof Collection) {
/* 169 */         if (target instanceof List) {
/* 170 */           this.indexedType = IndexedType.LIST;
/*     */         }
/* 172 */         return new CollectionIndexingValueRef((Collection)target, idx, targetDescriptor, state
/* 173 */             .getTypeConverter(), state.getConfiguration().isAutoGrowCollections(), state
/* 174 */             .getConfiguration().getMaximumAutoGrowSize());
/*     */       } 
/*     */       
/* 177 */       this.indexedType = IndexedType.STRING;
/* 178 */       return new StringIndexingLValue((String)target, idx, targetDescriptor);
/*     */     } 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 184 */     TypeDescriptor valueType = indexValue.getTypeDescriptor();
/* 185 */     if (valueType != null && String.class == valueType.getType()) {
/* 186 */       this.indexedType = IndexedType.OBJECT;
/* 187 */       return new PropertyIndexingValueRef(target, (String)index, state
/* 188 */           .getEvaluationContext(), targetDescriptor);
/*     */     } 
/*     */     
/* 191 */     throw new SpelEvaluationException(
/* 192 */         getStartPosition(), SpelMessage.INDEXING_NOT_SUPPORTED_FOR_TYPE, new Object[] { targetDescriptor });
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isCompilable() {
/* 197 */     if (this.indexedType == IndexedType.ARRAY) {
/* 198 */       return (this.exitTypeDescriptor != null);
/*     */     }
/* 200 */     if (this.indexedType == IndexedType.LIST) {
/* 201 */       return this.children[0].isCompilable();
/*     */     }
/* 203 */     if (this.indexedType == IndexedType.MAP) {
/* 204 */       return (this.children[0] instanceof PropertyOrFieldReference || this.children[0].isCompilable());
/*     */     }
/* 206 */     if (this.indexedType == IndexedType.OBJECT)
/*     */     {
/* 208 */       return (this.cachedReadAccessor != null && this.cachedReadAccessor instanceof ReflectivePropertyAccessor.OptimalPropertyAccessor && 
/*     */         
/* 210 */         getChild(0) instanceof StringLiteral);
/*     */     }
/* 212 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public void generateCode(MethodVisitor mv, CodeFlow cf) {
/* 217 */     String descriptor = cf.lastDescriptor();
/* 218 */     if (descriptor == null)
/*     */     {
/* 220 */       cf.loadTarget(mv);
/*     */     }
/*     */     
/* 223 */     if (this.indexedType == IndexedType.ARRAY) {
/*     */       int insn;
/* 225 */       if ("D".equals(this.exitTypeDescriptor)) {
/* 226 */         mv.visitTypeInsn(192, "[D");
/* 227 */         insn = 49;
/*     */       }
/* 229 */       else if ("F".equals(this.exitTypeDescriptor)) {
/* 230 */         mv.visitTypeInsn(192, "[F");
/* 231 */         insn = 48;
/*     */       }
/* 233 */       else if ("J".equals(this.exitTypeDescriptor)) {
/* 234 */         mv.visitTypeInsn(192, "[J");
/* 235 */         insn = 47;
/*     */       }
/* 237 */       else if ("I".equals(this.exitTypeDescriptor)) {
/* 238 */         mv.visitTypeInsn(192, "[I");
/* 239 */         insn = 46;
/*     */       }
/* 241 */       else if ("S".equals(this.exitTypeDescriptor)) {
/* 242 */         mv.visitTypeInsn(192, "[S");
/* 243 */         insn = 53;
/*     */       }
/* 245 */       else if ("B".equals(this.exitTypeDescriptor)) {
/* 246 */         mv.visitTypeInsn(192, "[B");
/* 247 */         insn = 51;
/*     */       }
/* 249 */       else if ("C".equals(this.exitTypeDescriptor)) {
/* 250 */         mv.visitTypeInsn(192, "[C");
/* 251 */         insn = 52;
/*     */       } else {
/*     */         
/* 254 */         mv.visitTypeInsn(192, "[" + this.exitTypeDescriptor + (
/* 255 */             CodeFlow.isPrimitiveArray(this.exitTypeDescriptor) ? "" : ";"));
/*     */         
/* 257 */         insn = 50;
/*     */       } 
/* 259 */       SpelNodeImpl index = this.children[0];
/* 260 */       cf.enterCompilationScope();
/* 261 */       index.generateCode(mv, cf);
/* 262 */       cf.exitCompilationScope();
/* 263 */       mv.visitInsn(insn);
/*     */     
/*     */     }
/* 266 */     else if (this.indexedType == IndexedType.LIST) {
/* 267 */       mv.visitTypeInsn(192, "java/util/List");
/* 268 */       cf.enterCompilationScope();
/* 269 */       this.children[0].generateCode(mv, cf);
/* 270 */       cf.exitCompilationScope();
/* 271 */       mv.visitMethodInsn(185, "java/util/List", "get", "(I)Ljava/lang/Object;", true);
/*     */     
/*     */     }
/* 274 */     else if (this.indexedType == IndexedType.MAP) {
/* 275 */       mv.visitTypeInsn(192, "java/util/Map");
/*     */ 
/*     */       
/* 278 */       if (this.children[0] instanceof PropertyOrFieldReference) {
/* 279 */         PropertyOrFieldReference reference = (PropertyOrFieldReference)this.children[0];
/* 280 */         String mapKeyName = reference.getName();
/* 281 */         mv.visitLdcInsn(mapKeyName);
/*     */       } else {
/*     */         
/* 284 */         cf.enterCompilationScope();
/* 285 */         this.children[0].generateCode(mv, cf);
/* 286 */         cf.exitCompilationScope();
/*     */       } 
/* 288 */       mv.visitMethodInsn(185, "java/util/Map", "get", "(Ljava/lang/Object;)Ljava/lang/Object;", true);
/*     */ 
/*     */     
/*     */     }
/* 292 */     else if (this.indexedType == IndexedType.OBJECT) {
/* 293 */       ReflectivePropertyAccessor.OptimalPropertyAccessor accessor = (ReflectivePropertyAccessor.OptimalPropertyAccessor)this.cachedReadAccessor;
/*     */       
/* 295 */       Assert.state((accessor != null), "No cached read accessor");
/* 296 */       Member member = accessor.member;
/* 297 */       boolean isStatic = Modifier.isStatic(member.getModifiers());
/* 298 */       String classDesc = member.getDeclaringClass().getName().replace('.', '/');
/*     */       
/* 300 */       if (!isStatic) {
/* 301 */         if (descriptor == null) {
/* 302 */           cf.loadTarget(mv);
/*     */         }
/* 304 */         if (descriptor == null || !classDesc.equals(descriptor.substring(1))) {
/* 305 */           mv.visitTypeInsn(192, classDesc);
/*     */         }
/*     */       } 
/*     */       
/* 309 */       if (member instanceof Method) {
/* 310 */         mv.visitMethodInsn(isStatic ? 184 : 182, classDesc, member.getName(), 
/* 311 */             CodeFlow.createSignatureDescriptor((Method)member), false);
/*     */       } else {
/*     */         
/* 314 */         mv.visitFieldInsn(isStatic ? 178 : 180, classDesc, member.getName(), 
/* 315 */             CodeFlow.toJvmDescriptor(((Field)member).getType()));
/*     */       } 
/*     */     } 
/*     */     
/* 319 */     cf.pushDescriptor(this.exitTypeDescriptor);
/*     */   }
/*     */ 
/*     */   
/*     */   public String toStringAST() {
/* 324 */     StringJoiner sj = new StringJoiner(",", "[", "]");
/* 325 */     for (int i = 0; i < getChildCount(); i++) {
/* 326 */       sj.add(getChild(i).toStringAST());
/*     */     }
/* 328 */     return sj.toString();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void setArrayElement(TypeConverter converter, Object ctx, int idx, @Nullable Object newValue, Class<?> arrayComponentType) throws EvaluationException {
/* 335 */     if (arrayComponentType == boolean.class) {
/* 336 */       boolean[] array = (boolean[])ctx;
/* 337 */       checkAccess(array.length, idx);
/* 338 */       array[idx] = ((Boolean)convertValue(converter, newValue, (Class)Boolean.class)).booleanValue();
/*     */     }
/* 340 */     else if (arrayComponentType == byte.class) {
/* 341 */       byte[] array = (byte[])ctx;
/* 342 */       checkAccess(array.length, idx);
/* 343 */       array[idx] = ((Byte)convertValue(converter, newValue, (Class)Byte.class)).byteValue();
/*     */     }
/* 345 */     else if (arrayComponentType == char.class) {
/* 346 */       char[] array = (char[])ctx;
/* 347 */       checkAccess(array.length, idx);
/* 348 */       array[idx] = ((Character)convertValue(converter, newValue, (Class)Character.class)).charValue();
/*     */     }
/* 350 */     else if (arrayComponentType == double.class) {
/* 351 */       double[] array = (double[])ctx;
/* 352 */       checkAccess(array.length, idx);
/* 353 */       array[idx] = ((Double)convertValue(converter, newValue, (Class)Double.class)).doubleValue();
/*     */     }
/* 355 */     else if (arrayComponentType == float.class) {
/* 356 */       float[] array = (float[])ctx;
/* 357 */       checkAccess(array.length, idx);
/* 358 */       array[idx] = ((Float)convertValue(converter, newValue, (Class)Float.class)).floatValue();
/*     */     }
/* 360 */     else if (arrayComponentType == int.class) {
/* 361 */       int[] array = (int[])ctx;
/* 362 */       checkAccess(array.length, idx);
/* 363 */       array[idx] = ((Integer)convertValue(converter, newValue, (Class)Integer.class)).intValue();
/*     */     }
/* 365 */     else if (arrayComponentType == long.class) {
/* 366 */       long[] array = (long[])ctx;
/* 367 */       checkAccess(array.length, idx);
/* 368 */       array[idx] = ((Long)convertValue(converter, newValue, (Class)Long.class)).longValue();
/*     */     }
/* 370 */     else if (arrayComponentType == short.class) {
/* 371 */       short[] array = (short[])ctx;
/* 372 */       checkAccess(array.length, idx);
/* 373 */       array[idx] = ((Short)convertValue(converter, newValue, (Class)Short.class)).shortValue();
/*     */     } else {
/*     */       
/* 376 */       Object[] array = (Object[])ctx;
/* 377 */       checkAccess(array.length, idx);
/* 378 */       array[idx] = convertValue(converter, newValue, arrayComponentType);
/*     */     } 
/*     */   }
/*     */   
/*     */   private Object accessArrayElement(Object ctx, int idx) throws SpelEvaluationException {
/* 383 */     Class<?> arrayComponentType = ctx.getClass().getComponentType();
/* 384 */     if (arrayComponentType == boolean.class) {
/* 385 */       boolean[] arrayOfBoolean = (boolean[])ctx;
/* 386 */       checkAccess(arrayOfBoolean.length, idx);
/* 387 */       this.exitTypeDescriptor = "Z";
/* 388 */       return Boolean.valueOf(arrayOfBoolean[idx]);
/*     */     } 
/* 390 */     if (arrayComponentType == byte.class) {
/* 391 */       byte[] arrayOfByte = (byte[])ctx;
/* 392 */       checkAccess(arrayOfByte.length, idx);
/* 393 */       this.exitTypeDescriptor = "B";
/* 394 */       return Byte.valueOf(arrayOfByte[idx]);
/*     */     } 
/* 396 */     if (arrayComponentType == char.class) {
/* 397 */       char[] arrayOfChar = (char[])ctx;
/* 398 */       checkAccess(arrayOfChar.length, idx);
/* 399 */       this.exitTypeDescriptor = "C";
/* 400 */       return Character.valueOf(arrayOfChar[idx]);
/*     */     } 
/* 402 */     if (arrayComponentType == double.class) {
/* 403 */       double[] arrayOfDouble = (double[])ctx;
/* 404 */       checkAccess(arrayOfDouble.length, idx);
/* 405 */       this.exitTypeDescriptor = "D";
/* 406 */       return Double.valueOf(arrayOfDouble[idx]);
/*     */     } 
/* 408 */     if (arrayComponentType == float.class) {
/* 409 */       float[] arrayOfFloat = (float[])ctx;
/* 410 */       checkAccess(arrayOfFloat.length, idx);
/* 411 */       this.exitTypeDescriptor = "F";
/* 412 */       return Float.valueOf(arrayOfFloat[idx]);
/*     */     } 
/* 414 */     if (arrayComponentType == int.class) {
/* 415 */       int[] arrayOfInt = (int[])ctx;
/* 416 */       checkAccess(arrayOfInt.length, idx);
/* 417 */       this.exitTypeDescriptor = "I";
/* 418 */       return Integer.valueOf(arrayOfInt[idx]);
/*     */     } 
/* 420 */     if (arrayComponentType == long.class) {
/* 421 */       long[] arrayOfLong = (long[])ctx;
/* 422 */       checkAccess(arrayOfLong.length, idx);
/* 423 */       this.exitTypeDescriptor = "J";
/* 424 */       return Long.valueOf(arrayOfLong[idx]);
/*     */     } 
/* 426 */     if (arrayComponentType == short.class) {
/* 427 */       short[] arrayOfShort = (short[])ctx;
/* 428 */       checkAccess(arrayOfShort.length, idx);
/* 429 */       this.exitTypeDescriptor = "S";
/* 430 */       return Short.valueOf(arrayOfShort[idx]);
/*     */     } 
/*     */     
/* 433 */     Object[] array = (Object[])ctx;
/* 434 */     checkAccess(array.length, idx);
/* 435 */     Object retValue = array[idx];
/* 436 */     this.exitTypeDescriptor = CodeFlow.toDescriptor(arrayComponentType);
/* 437 */     return retValue;
/*     */   }
/*     */ 
/*     */   
/*     */   private void checkAccess(int arrayLength, int index) throws SpelEvaluationException {
/* 442 */     if (index >= arrayLength) {
/* 443 */       throw new SpelEvaluationException(getStartPosition(), SpelMessage.ARRAY_INDEX_OUT_OF_BOUNDS, new Object[] {
/* 444 */             Integer.valueOf(arrayLength), Integer.valueOf(index)
/*     */           });
/*     */     }
/*     */   }
/*     */   
/*     */   private <T> T convertValue(TypeConverter converter, @Nullable Object value, Class<T> targetType) {
/* 450 */     T result = (T)converter.convertValue(value, 
/* 451 */         TypeDescriptor.forObject(value), TypeDescriptor.valueOf(targetType));
/* 452 */     if (result == null) {
/* 453 */       throw new IllegalStateException("Null conversion result for index [" + value + "]");
/*     */     }
/* 455 */     return result;
/*     */   }
/*     */ 
/*     */   
/*     */   private class ArrayIndexingValueRef
/*     */     implements ValueRef
/*     */   {
/*     */     private final TypeConverter typeConverter;
/*     */     
/*     */     private final Object array;
/*     */     
/*     */     private final int index;
/*     */     private final TypeDescriptor typeDescriptor;
/*     */     
/*     */     ArrayIndexingValueRef(TypeConverter typeConverter, Object array, int index, TypeDescriptor typeDescriptor) {
/* 470 */       this.typeConverter = typeConverter;
/* 471 */       this.array = array;
/* 472 */       this.index = index;
/* 473 */       this.typeDescriptor = typeDescriptor;
/*     */     }
/*     */ 
/*     */     
/*     */     public TypedValue getValue() {
/* 478 */       Object arrayElement = Indexer.this.accessArrayElement(this.array, this.index);
/* 479 */       return new TypedValue(arrayElement, this.typeDescriptor.elementTypeDescriptor(arrayElement));
/*     */     }
/*     */ 
/*     */     
/*     */     public void setValue(@Nullable Object newValue) {
/* 484 */       TypeDescriptor elementType = this.typeDescriptor.getElementTypeDescriptor();
/* 485 */       Assert.state((elementType != null), "No element type");
/* 486 */       Indexer.this.setArrayElement(this.typeConverter, this.array, this.index, newValue, elementType.getType());
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean isWritable() {
/* 491 */       return true;
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private class MapIndexingValueRef
/*     */     implements ValueRef
/*     */   {
/*     */     private final TypeConverter typeConverter;
/*     */     
/*     */     private final Map map;
/*     */     
/*     */     @Nullable
/*     */     private final Object key;
/*     */     
/*     */     private final TypeDescriptor mapEntryDescriptor;
/*     */ 
/*     */     
/*     */     public MapIndexingValueRef(TypeConverter typeConverter, @Nullable Map map, Object key, TypeDescriptor mapEntryDescriptor) {
/* 511 */       this.typeConverter = typeConverter;
/* 512 */       this.map = map;
/* 513 */       this.key = key;
/* 514 */       this.mapEntryDescriptor = mapEntryDescriptor;
/*     */     }
/*     */ 
/*     */     
/*     */     public TypedValue getValue() {
/* 519 */       Object value = this.map.get(this.key);
/* 520 */       Indexer.this.exitTypeDescriptor = CodeFlow.toDescriptor(Object.class);
/* 521 */       return new TypedValue(value, this.mapEntryDescriptor.getMapValueTypeDescriptor(value));
/*     */     }
/*     */ 
/*     */     
/*     */     public void setValue(@Nullable Object newValue) {
/* 526 */       if (this.mapEntryDescriptor.getMapValueTypeDescriptor() != null) {
/* 527 */         newValue = this.typeConverter.convertValue(newValue, TypeDescriptor.forObject(newValue), this.mapEntryDescriptor
/* 528 */             .getMapValueTypeDescriptor());
/*     */       }
/* 530 */       this.map.put(this.key, newValue);
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean isWritable() {
/* 535 */       return true;
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   private class PropertyIndexingValueRef
/*     */     implements ValueRef
/*     */   {
/*     */     private final Object targetObject;
/*     */     
/*     */     private final String name;
/*     */     
/*     */     private final EvaluationContext evaluationContext;
/*     */     
/*     */     private final TypeDescriptor targetObjectTypeDescriptor;
/*     */ 
/*     */     
/*     */     public PropertyIndexingValueRef(Object targetObject, String value, EvaluationContext evaluationContext, TypeDescriptor targetObjectTypeDescriptor) {
/* 553 */       this.targetObject = targetObject;
/* 554 */       this.name = value;
/* 555 */       this.evaluationContext = evaluationContext;
/* 556 */       this.targetObjectTypeDescriptor = targetObjectTypeDescriptor;
/*     */     }
/*     */ 
/*     */     
/*     */     public TypedValue getValue() {
/* 561 */       Class<?> targetObjectRuntimeClass = Indexer.this.getObjectClass(this.targetObject);
/*     */       try {
/* 563 */         if (Indexer.this.cachedReadName != null && Indexer.this.cachedReadName.equals(this.name) && Indexer.this
/* 564 */           .cachedReadTargetType != null && Indexer.this
/* 565 */           .cachedReadTargetType.equals(targetObjectRuntimeClass)) {
/*     */           
/* 567 */           PropertyAccessor accessor = Indexer.this.cachedReadAccessor;
/* 568 */           Assert.state((accessor != null), "No cached read accessor");
/* 569 */           return accessor.read(this.evaluationContext, this.targetObject, this.name);
/*     */         } 
/* 571 */         List<PropertyAccessor> accessorsToTry = AstUtils.getPropertyAccessorsToTry(targetObjectRuntimeClass, this.evaluationContext
/* 572 */             .getPropertyAccessors());
/* 573 */         for (PropertyAccessor accessor : accessorsToTry) {
/* 574 */           if (accessor.canRead(this.evaluationContext, this.targetObject, this.name)) {
/* 575 */             if (accessor instanceof ReflectivePropertyAccessor) {
/* 576 */               accessor = ((ReflectivePropertyAccessor)accessor).createOptimalAccessor(this.evaluationContext, this.targetObject, this.name);
/*     */             }
/*     */             
/* 579 */             Indexer.this.cachedReadAccessor = accessor;
/* 580 */             Indexer.this.cachedReadName = this.name;
/* 581 */             Indexer.this.cachedReadTargetType = targetObjectRuntimeClass;
/* 582 */             if (accessor instanceof ReflectivePropertyAccessor.OptimalPropertyAccessor) {
/* 583 */               ReflectivePropertyAccessor.OptimalPropertyAccessor optimalAccessor = (ReflectivePropertyAccessor.OptimalPropertyAccessor)accessor;
/*     */               
/* 585 */               Member member = optimalAccessor.member;
/* 586 */               Indexer.this.exitTypeDescriptor = CodeFlow.toDescriptor((member instanceof Method) ? ((Method)member)
/* 587 */                   .getReturnType() : ((Field)member).getType());
/*     */             } 
/* 589 */             return accessor.read(this.evaluationContext, this.targetObject, this.name);
/*     */           }
/*     */         
/*     */         } 
/* 593 */       } catch (AccessException ex) {
/* 594 */         throw new SpelEvaluationException(Indexer.this.getStartPosition(), ex, SpelMessage.INDEXING_NOT_SUPPORTED_FOR_TYPE, new Object[] { this.targetObjectTypeDescriptor
/* 595 */               .toString() });
/*     */       } 
/* 597 */       throw new SpelEvaluationException(Indexer.this.getStartPosition(), SpelMessage.INDEXING_NOT_SUPPORTED_FOR_TYPE, new Object[] { this.targetObjectTypeDescriptor
/* 598 */             .toString() });
/*     */     }
/*     */ 
/*     */     
/*     */     public void setValue(@Nullable Object newValue) {
/* 603 */       Class<?> contextObjectClass = Indexer.this.getObjectClass(this.targetObject);
/*     */       try {
/* 605 */         if (Indexer.this.cachedWriteName != null && Indexer.this.cachedWriteName.equals(this.name) && Indexer.this
/* 606 */           .cachedWriteTargetType != null && Indexer.this
/* 607 */           .cachedWriteTargetType.equals(contextObjectClass)) {
/*     */           
/* 609 */           PropertyAccessor accessor = Indexer.this.cachedWriteAccessor;
/* 610 */           Assert.state((accessor != null), "No cached write accessor");
/* 611 */           accessor.write(this.evaluationContext, this.targetObject, this.name, newValue);
/*     */           return;
/*     */         } 
/* 614 */         List<PropertyAccessor> accessorsToTry = AstUtils.getPropertyAccessorsToTry(contextObjectClass, this.evaluationContext
/* 615 */             .getPropertyAccessors());
/* 616 */         for (PropertyAccessor accessor : accessorsToTry) {
/* 617 */           if (accessor.canWrite(this.evaluationContext, this.targetObject, this.name)) {
/* 618 */             Indexer.this.cachedWriteName = this.name;
/* 619 */             Indexer.this.cachedWriteTargetType = contextObjectClass;
/* 620 */             Indexer.this.cachedWriteAccessor = accessor;
/* 621 */             accessor.write(this.evaluationContext, this.targetObject, this.name, newValue);
/*     */             
/*     */             return;
/*     */           } 
/*     */         } 
/* 626 */       } catch (AccessException ex) {
/* 627 */         throw new SpelEvaluationException(Indexer.this.getStartPosition(), ex, SpelMessage.EXCEPTION_DURING_PROPERTY_WRITE, new Object[] { this.name, ex
/* 628 */               .getMessage() });
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean isWritable() {
/* 634 */       return true;
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private class CollectionIndexingValueRef
/*     */     implements ValueRef
/*     */   {
/*     */     private final Collection collection;
/*     */     
/*     */     private final int index;
/*     */     
/*     */     private final TypeDescriptor collectionEntryDescriptor;
/*     */     
/*     */     private final TypeConverter typeConverter;
/*     */     
/*     */     private final boolean growCollection;
/*     */     
/*     */     private final int maximumSize;
/*     */ 
/*     */     
/*     */     public CollectionIndexingValueRef(Collection collection, int index, TypeDescriptor collectionEntryDescriptor, TypeConverter typeConverter, boolean growCollection, int maximumSize) {
/* 657 */       this.collection = collection;
/* 658 */       this.index = index;
/* 659 */       this.collectionEntryDescriptor = collectionEntryDescriptor;
/* 660 */       this.typeConverter = typeConverter;
/* 661 */       this.growCollection = growCollection;
/* 662 */       this.maximumSize = maximumSize;
/*     */     }
/*     */ 
/*     */     
/*     */     public TypedValue getValue() {
/* 667 */       growCollectionIfNecessary();
/* 668 */       if (this.collection instanceof List) {
/* 669 */         Object o = ((List)this.collection).get(this.index);
/* 670 */         Indexer.this.exitTypeDescriptor = CodeFlow.toDescriptor(Object.class);
/* 671 */         return new TypedValue(o, this.collectionEntryDescriptor.elementTypeDescriptor(o));
/*     */       } 
/* 673 */       int pos = 0;
/* 674 */       for (Object o : this.collection) {
/* 675 */         if (pos == this.index) {
/* 676 */           return new TypedValue(o, this.collectionEntryDescriptor.elementTypeDescriptor(o));
/*     */         }
/* 678 */         pos++;
/*     */       } 
/* 680 */       throw new IllegalStateException("Failed to find indexed element " + this.index + ": " + this.collection);
/*     */     }
/*     */ 
/*     */     
/*     */     public void setValue(@Nullable Object newValue) {
/* 685 */       growCollectionIfNecessary();
/* 686 */       if (this.collection instanceof List) {
/* 687 */         List<Object> list = (List)this.collection;
/* 688 */         if (this.collectionEntryDescriptor.getElementTypeDescriptor() != null) {
/* 689 */           newValue = this.typeConverter.convertValue(newValue, TypeDescriptor.forObject(newValue), this.collectionEntryDescriptor
/* 690 */               .getElementTypeDescriptor());
/*     */         }
/* 692 */         list.set(this.index, newValue);
/*     */       } else {
/*     */         
/* 695 */         throw new SpelEvaluationException(Indexer.this.getStartPosition(), SpelMessage.INDEXING_NOT_SUPPORTED_FOR_TYPE, new Object[] { this.collectionEntryDescriptor
/* 696 */               .toString() });
/*     */       } 
/*     */     }
/*     */     
/*     */     private void growCollectionIfNecessary() {
/* 701 */       if (this.index >= this.collection.size()) {
/* 702 */         if (!this.growCollection)
/* 703 */           throw new SpelEvaluationException(Indexer.this.getStartPosition(), SpelMessage.COLLECTION_INDEX_OUT_OF_BOUNDS, new Object[] {
/* 704 */                 Integer.valueOf(this.collection.size()), Integer.valueOf(this.index)
/*     */               }); 
/* 706 */         if (this.index >= this.maximumSize) {
/* 707 */           throw new SpelEvaluationException(Indexer.this.getStartPosition(), SpelMessage.UNABLE_TO_GROW_COLLECTION, new Object[0]);
/*     */         }
/* 709 */         if (this.collectionEntryDescriptor.getElementTypeDescriptor() == null) {
/* 710 */           throw new SpelEvaluationException(Indexer.this
/* 711 */               .getStartPosition(), SpelMessage.UNABLE_TO_GROW_COLLECTION_UNKNOWN_ELEMENT_TYPE, new Object[0]);
/*     */         }
/* 713 */         TypeDescriptor elementType = this.collectionEntryDescriptor.getElementTypeDescriptor();
/*     */         try {
/* 715 */           Constructor<?> ctor = getDefaultConstructor(elementType.getType());
/* 716 */           int newElements = this.index - this.collection.size();
/* 717 */           while (newElements >= 0)
/*     */           {
/* 719 */             this.collection.add((ctor != null) ? (T)ctor.newInstance(new Object[0]) : null);
/* 720 */             newElements--;
/*     */           }
/*     */         
/* 723 */         } catch (Throwable ex) {
/* 724 */           throw new SpelEvaluationException(Indexer.this.getStartPosition(), ex, SpelMessage.UNABLE_TO_GROW_COLLECTION, new Object[0]);
/*     */         } 
/*     */       } 
/*     */     }
/*     */     
/*     */     @Nullable
/*     */     private Constructor<?> getDefaultConstructor(Class<?> type) {
/*     */       try {
/* 732 */         return ReflectionUtils.accessibleConstructor(type, new Class[0]);
/*     */       }
/* 734 */       catch (Throwable ex) {
/* 735 */         return null;
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean isWritable() {
/* 741 */       return true;
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   private class StringIndexingLValue
/*     */     implements ValueRef
/*     */   {
/*     */     private final String target;
/*     */     
/*     */     private final int index;
/*     */     private final TypeDescriptor typeDescriptor;
/*     */     
/*     */     public StringIndexingLValue(String target, int index, TypeDescriptor typeDescriptor) {
/* 755 */       this.target = target;
/* 756 */       this.index = index;
/* 757 */       this.typeDescriptor = typeDescriptor;
/*     */     }
/*     */ 
/*     */     
/*     */     public TypedValue getValue() {
/* 762 */       if (this.index >= this.target.length())
/* 763 */         throw new SpelEvaluationException(Indexer.this.getStartPosition(), SpelMessage.STRING_INDEX_OUT_OF_BOUNDS, new Object[] {
/* 764 */               Integer.valueOf(this.target.length()), Integer.valueOf(this.index)
/*     */             }); 
/* 766 */       return new TypedValue(String.valueOf(this.target.charAt(this.index)));
/*     */     }
/*     */ 
/*     */     
/*     */     public void setValue(@Nullable Object newValue) {
/* 771 */       throw new SpelEvaluationException(Indexer.this.getStartPosition(), SpelMessage.INDEXING_NOT_SUPPORTED_FOR_TYPE, new Object[] { this.typeDescriptor
/* 772 */             .toString() });
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean isWritable() {
/* 777 */       return true;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\springframework\expression\spel\ast\Indexer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */