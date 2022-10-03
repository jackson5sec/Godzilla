/*     */ package org.springframework.core.annotation;
/*     */ 
/*     */ import java.lang.annotation.Annotation;
/*     */ import java.lang.reflect.Array;
/*     */ import java.lang.reflect.Member;
/*     */ import java.lang.reflect.Method;
/*     */ import java.util.Collections;
/*     */ import java.util.HashMap;
/*     */ import java.util.LinkedHashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.NoSuchElementException;
/*     */ import java.util.Optional;
/*     */ import java.util.function.Function;
/*     */ import java.util.function.Predicate;
/*     */ import org.springframework.lang.Nullable;
/*     */ import org.springframework.util.Assert;
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
/*     */ 
/*     */ final class TypeMappedAnnotation<A extends Annotation>
/*     */   extends AbstractMergedAnnotation<A>
/*     */ {
/*     */   private static final Map<Class<?>, Object> EMPTY_ARRAYS;
/*     */   private final AnnotationTypeMapping mapping;
/*     */   @Nullable
/*     */   private final ClassLoader classLoader;
/*     */   @Nullable
/*     */   private final Object source;
/*     */   @Nullable
/*     */   private final Object rootAttributes;
/*     */   private final ValueExtractor valueExtractor;
/*     */   private final int aggregateIndex;
/*     */   private final boolean useMergedValues;
/*     */   @Nullable
/*     */   private final Predicate<String> attributeFilter;
/*     */   private final int[] resolvedRootMirrors;
/*     */   private final int[] resolvedMirrors;
/*     */   
/*     */   static {
/*  75 */     Map<Class<?>, Object> emptyArrays = new HashMap<>();
/*  76 */     emptyArrays.put(boolean.class, new boolean[0]);
/*  77 */     emptyArrays.put(byte.class, new byte[0]);
/*  78 */     emptyArrays.put(char.class, new char[0]);
/*  79 */     emptyArrays.put(double.class, new double[0]);
/*  80 */     emptyArrays.put(float.class, new float[0]);
/*  81 */     emptyArrays.put(int.class, new int[0]);
/*  82 */     emptyArrays.put(long.class, new long[0]);
/*  83 */     emptyArrays.put(short.class, new short[0]);
/*  84 */     emptyArrays.put(String.class, new String[0]);
/*  85 */     EMPTY_ARRAYS = Collections.unmodifiableMap(emptyArrays);
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
/*     */   private TypeMappedAnnotation(AnnotationTypeMapping mapping, @Nullable ClassLoader classLoader, @Nullable Object source, @Nullable Object rootAttributes, ValueExtractor valueExtractor, int aggregateIndex) {
/* 118 */     this(mapping, classLoader, source, rootAttributes, valueExtractor, aggregateIndex, null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private TypeMappedAnnotation(AnnotationTypeMapping mapping, @Nullable ClassLoader classLoader, @Nullable Object source, @Nullable Object rootAttributes, ValueExtractor valueExtractor, int aggregateIndex, @Nullable int[] resolvedRootMirrors) {
/* 125 */     this.mapping = mapping;
/* 126 */     this.classLoader = classLoader;
/* 127 */     this.source = source;
/* 128 */     this.rootAttributes = rootAttributes;
/* 129 */     this.valueExtractor = valueExtractor;
/* 130 */     this.aggregateIndex = aggregateIndex;
/* 131 */     this.useMergedValues = true;
/* 132 */     this.attributeFilter = null;
/* 133 */     this
/* 134 */       .resolvedRootMirrors = (resolvedRootMirrors != null) ? resolvedRootMirrors : mapping.getRoot().getMirrorSets().resolve(source, rootAttributes, this.valueExtractor);
/* 135 */     this
/* 136 */       .resolvedMirrors = (getDistance() == 0) ? this.resolvedRootMirrors : mapping.getMirrorSets().resolve(source, this, this::getValueForMirrorResolution);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private TypeMappedAnnotation(AnnotationTypeMapping mapping, @Nullable ClassLoader classLoader, @Nullable Object source, @Nullable Object rootAnnotation, ValueExtractor valueExtractor, int aggregateIndex, boolean useMergedValues, @Nullable Predicate<String> attributeFilter, int[] resolvedRootMirrors, int[] resolvedMirrors) {
/* 144 */     this.classLoader = classLoader;
/* 145 */     this.source = source;
/* 146 */     this.rootAttributes = rootAnnotation;
/* 147 */     this.valueExtractor = valueExtractor;
/* 148 */     this.mapping = mapping;
/* 149 */     this.aggregateIndex = aggregateIndex;
/* 150 */     this.useMergedValues = useMergedValues;
/* 151 */     this.attributeFilter = attributeFilter;
/* 152 */     this.resolvedRootMirrors = resolvedRootMirrors;
/* 153 */     this.resolvedMirrors = resolvedMirrors;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Class<A> getType() {
/* 160 */     return (Class)this.mapping.getAnnotationType();
/*     */   }
/*     */ 
/*     */   
/*     */   public List<Class<? extends Annotation>> getMetaTypes() {
/* 165 */     return this.mapping.getMetaTypes();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isPresent() {
/* 170 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getDistance() {
/* 175 */     return this.mapping.getDistance();
/*     */   }
/*     */ 
/*     */   
/*     */   public int getAggregateIndex() {
/* 180 */     return this.aggregateIndex;
/*     */   }
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public Object getSource() {
/* 186 */     return this.source;
/*     */   }
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public MergedAnnotation<?> getMetaSource() {
/* 192 */     AnnotationTypeMapping metaSourceMapping = this.mapping.getSource();
/* 193 */     if (metaSourceMapping == null) {
/* 194 */       return null;
/*     */     }
/* 196 */     return new TypeMappedAnnotation(metaSourceMapping, this.classLoader, this.source, this.rootAttributes, this.valueExtractor, this.aggregateIndex, this.resolvedRootMirrors);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public MergedAnnotation<?> getRoot() {
/* 202 */     if (getDistance() == 0) {
/* 203 */       return this;
/*     */     }
/* 205 */     AnnotationTypeMapping rootMapping = this.mapping.getRoot();
/* 206 */     return new TypeMappedAnnotation(rootMapping, this.classLoader, this.source, this.rootAttributes, this.valueExtractor, this.aggregateIndex, this.resolvedRootMirrors);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean hasDefaultValue(String attributeName) {
/* 212 */     int attributeIndex = getAttributeIndex(attributeName, true);
/* 213 */     Object value = getValue(attributeIndex, true, false);
/* 214 */     return (value == null || this.mapping.isEquivalentToDefaultValue(attributeIndex, value, this.valueExtractor));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public <T extends Annotation> MergedAnnotation<T> getAnnotation(String attributeName, Class<T> type) throws NoSuchElementException {
/* 222 */     int attributeIndex = getAttributeIndex(attributeName, true);
/* 223 */     Method attribute = this.mapping.getAttributes().get(attributeIndex);
/* 224 */     Assert.notNull(type, "Type must not be null");
/* 225 */     Assert.isAssignable(type, attribute.getReturnType(), () -> "Attribute " + attributeName + " type mismatch:");
/*     */     
/* 227 */     return (MergedAnnotation<T>)getRequiredValue(attributeIndex, attributeName);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public <T extends Annotation> MergedAnnotation<T>[] getAnnotationArray(String attributeName, Class<T> type) throws NoSuchElementException {
/* 235 */     int attributeIndex = getAttributeIndex(attributeName, true);
/* 236 */     Method attribute = this.mapping.getAttributes().get(attributeIndex);
/* 237 */     Class<?> componentType = attribute.getReturnType().getComponentType();
/* 238 */     Assert.notNull(type, "Type must not be null");
/* 239 */     Assert.notNull(componentType, () -> "Attribute " + attributeName + " is not an array");
/* 240 */     Assert.isAssignable(type, componentType, () -> "Attribute " + attributeName + " component type mismatch:");
/* 241 */     return (MergedAnnotation<T>[])getRequiredValue(attributeIndex, attributeName);
/*     */   }
/*     */ 
/*     */   
/*     */   public <T> Optional<T> getDefaultValue(String attributeName, Class<T> type) {
/* 246 */     int attributeIndex = getAttributeIndex(attributeName, false);
/* 247 */     if (attributeIndex == -1) {
/* 248 */       return Optional.empty();
/*     */     }
/* 250 */     Method attribute = this.mapping.getAttributes().get(attributeIndex);
/* 251 */     return Optional.ofNullable(adapt(attribute, attribute.getDefaultValue(), type));
/*     */   }
/*     */ 
/*     */   
/*     */   public MergedAnnotation<A> filterAttributes(Predicate<String> predicate) {
/* 256 */     if (this.attributeFilter != null) {
/* 257 */       predicate = this.attributeFilter.and(predicate);
/*     */     }
/* 259 */     return new TypeMappedAnnotation(this.mapping, this.classLoader, this.source, this.rootAttributes, this.valueExtractor, this.aggregateIndex, this.useMergedValues, predicate, this.resolvedRootMirrors, this.resolvedMirrors);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public MergedAnnotation<A> withNonMergedAttributes() {
/* 266 */     return new TypeMappedAnnotation(this.mapping, this.classLoader, this.source, this.rootAttributes, this.valueExtractor, this.aggregateIndex, false, this.attributeFilter, this.resolvedRootMirrors, this.resolvedMirrors);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Map<String, Object> asMap(MergedAnnotation.Adapt... adaptations) {
/* 273 */     return Collections.unmodifiableMap(asMap(mergedAnnotation -> new LinkedHashMap<>(), adaptations));
/*     */   }
/*     */ 
/*     */   
/*     */   public <T extends Map<String, Object>> T asMap(Function<MergedAnnotation<?>, T> factory, MergedAnnotation.Adapt... adaptations) {
/* 278 */     Map<String, Object> map = (Map)factory.apply(this);
/* 279 */     Assert.state((map != null), "Factory used to create MergedAnnotation Map must not return null");
/* 280 */     AttributeMethods attributes = this.mapping.getAttributes();
/* 281 */     for (int i = 0; i < attributes.size(); i++) {
/* 282 */       Method attribute = attributes.get(i);
/*     */       
/* 284 */       Object value = isFiltered(attribute.getName()) ? null : getValue(i, getTypeForMapOptions(attribute, adaptations));
/* 285 */       if (value != null) {
/* 286 */         map.put(attribute.getName(), 
/* 287 */             adaptValueForMapOptions(attribute, value, map.getClass(), factory, adaptations));
/*     */       }
/*     */     } 
/* 290 */     return (T)map;
/*     */   }
/*     */   
/*     */   private Class<?> getTypeForMapOptions(Method attribute, MergedAnnotation.Adapt[] adaptations) {
/* 294 */     Class<?> attributeType = attribute.getReturnType();
/* 295 */     Class<?> componentType = attributeType.isArray() ? attributeType.getComponentType() : attributeType;
/* 296 */     if (MergedAnnotation.Adapt.CLASS_TO_STRING.isIn(adaptations) && componentType == Class.class) {
/* 297 */       return attributeType.isArray() ? String[].class : String.class;
/*     */     }
/* 299 */     return Object.class;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private <T extends Map<String, Object>> Object adaptValueForMapOptions(Method attribute, Object value, Class<?> mapType, Function<MergedAnnotation<?>, T> factory, MergedAnnotation.Adapt[] adaptations) {
/* 305 */     if (value instanceof MergedAnnotation) {
/* 306 */       MergedAnnotation<?> annotation = (MergedAnnotation)value;
/* 307 */       return MergedAnnotation.Adapt.ANNOTATION_TO_MAP.isIn(adaptations) ? annotation
/* 308 */         .<T>asMap(factory, adaptations) : annotation.synthesize();
/*     */     } 
/* 310 */     if (value instanceof MergedAnnotation[]) {
/* 311 */       MergedAnnotation[] arrayOfMergedAnnotation = (MergedAnnotation[])value;
/* 312 */       if (MergedAnnotation.Adapt.ANNOTATION_TO_MAP.isIn(adaptations)) {
/* 313 */         Object object = Array.newInstance(mapType, arrayOfMergedAnnotation.length);
/* 314 */         for (int j = 0; j < arrayOfMergedAnnotation.length; j++) {
/* 315 */           Array.set(object, j, arrayOfMergedAnnotation[j].asMap(factory, adaptations));
/*     */         }
/* 317 */         return object;
/*     */       } 
/* 319 */       Object result = Array.newInstance(attribute
/* 320 */           .getReturnType().getComponentType(), arrayOfMergedAnnotation.length);
/* 321 */       for (int i = 0; i < arrayOfMergedAnnotation.length; i++) {
/* 322 */         Array.set(result, i, arrayOfMergedAnnotation[i].synthesize());
/*     */       }
/* 324 */       return result;
/*     */     } 
/* 326 */     return value;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected A createSynthesized() {
/* 332 */     if (getType().isInstance(this.rootAttributes) && !isSynthesizable()) {
/* 333 */       return (A)this.rootAttributes;
/*     */     }
/* 335 */     return SynthesizedMergedAnnotationInvocationHandler.createProxy(this, getType());
/*     */   }
/*     */ 
/*     */   
/*     */   private boolean isSynthesizable() {
/* 340 */     if (this.rootAttributes instanceof SynthesizedAnnotation) {
/* 341 */       return false;
/*     */     }
/* 343 */     return this.mapping.isSynthesizable();
/*     */   }
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   protected <T> T getAttributeValue(String attributeName, Class<T> type) {
/* 349 */     int attributeIndex = getAttributeIndex(attributeName, false);
/* 350 */     return (attributeIndex != -1) ? getValue(attributeIndex, type) : null;
/*     */   }
/*     */   
/*     */   private Object getRequiredValue(int attributeIndex, String attributeName) {
/* 354 */     Object value = getValue(attributeIndex, Object.class);
/* 355 */     if (value == null) {
/* 356 */       throw new NoSuchElementException("No element at attribute index " + attributeIndex + " for name " + attributeName);
/*     */     }
/*     */     
/* 359 */     return value;
/*     */   }
/*     */   
/*     */   @Nullable
/*     */   private <T> T getValue(int attributeIndex, Class<T> type) {
/* 364 */     Method attribute = this.mapping.getAttributes().get(attributeIndex);
/* 365 */     Object value = getValue(attributeIndex, true, false);
/* 366 */     if (value == null) {
/* 367 */       value = attribute.getDefaultValue();
/*     */     }
/* 369 */     return adapt(attribute, value, type);
/*     */   }
/*     */   
/*     */   @Nullable
/*     */   private Object getValue(int attributeIndex, boolean useConventionMapping, boolean forMirrorResolution) {
/* 374 */     AnnotationTypeMapping mapping = this.mapping;
/* 375 */     if (this.useMergedValues) {
/* 376 */       int mappedIndex = this.mapping.getAliasMapping(attributeIndex);
/* 377 */       if (mappedIndex == -1 && useConventionMapping) {
/* 378 */         mappedIndex = this.mapping.getConventionMapping(attributeIndex);
/*     */       }
/* 380 */       if (mappedIndex != -1) {
/* 381 */         mapping = mapping.getRoot();
/* 382 */         attributeIndex = mappedIndex;
/*     */       } 
/*     */     } 
/* 385 */     if (!forMirrorResolution)
/*     */     {
/* 387 */       attributeIndex = ((mapping.getDistance() != 0) ? this.resolvedMirrors : this.resolvedRootMirrors)[attributeIndex];
/*     */     }
/* 389 */     if (attributeIndex == -1) {
/* 390 */       return null;
/*     */     }
/* 392 */     if (mapping.getDistance() == 0) {
/* 393 */       Method attribute = mapping.getAttributes().get(attributeIndex);
/* 394 */       Object result = this.valueExtractor.extract(attribute, this.rootAttributes);
/* 395 */       return (result != null) ? result : attribute.getDefaultValue();
/*     */     } 
/* 397 */     return getValueFromMetaAnnotation(attributeIndex, forMirrorResolution);
/*     */   }
/*     */   
/*     */   @Nullable
/*     */   private Object getValueFromMetaAnnotation(int attributeIndex, boolean forMirrorResolution) {
/* 402 */     Object value = null;
/* 403 */     if (this.useMergedValues || forMirrorResolution) {
/* 404 */       value = this.mapping.getMappedAnnotationValue(attributeIndex, forMirrorResolution);
/*     */     }
/* 406 */     if (value == null) {
/* 407 */       Method attribute = this.mapping.getAttributes().get(attributeIndex);
/* 408 */       value = ReflectionUtils.invokeMethod(attribute, this.mapping.getAnnotation());
/*     */     } 
/* 410 */     return value;
/*     */   }
/*     */   
/*     */   @Nullable
/*     */   private Object getValueForMirrorResolution(Method attribute, Object annotation) {
/* 415 */     int attributeIndex = this.mapping.getAttributes().indexOf(attribute);
/* 416 */     boolean valueAttribute = "value".equals(attribute.getName());
/* 417 */     return getValue(attributeIndex, !valueAttribute, true);
/*     */   }
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   private <T> T adapt(Method attribute, @Nullable Object value, Class<T> type) {
/* 423 */     if (value == null) {
/* 424 */       return null;
/*     */     }
/* 426 */     value = adaptForAttribute(attribute, value);
/* 427 */     type = getAdaptType(attribute, type);
/* 428 */     if (value instanceof Class && type == String.class) {
/* 429 */       value = ((Class)value).getName();
/*     */     }
/* 431 */     else if (value instanceof String && type == Class.class) {
/* 432 */       value = ClassUtils.resolveClassName((String)value, getClassLoader());
/*     */     }
/* 434 */     else if (value instanceof Class[] && type == String[].class) {
/* 435 */       Class<?>[] classes = (Class[])value;
/* 436 */       String[] names = new String[classes.length];
/* 437 */       for (int i = 0; i < classes.length; i++) {
/* 438 */         names[i] = classes[i].getName();
/*     */       }
/* 440 */       value = names;
/*     */     }
/* 442 */     else if (value instanceof String[] && type == Class[].class) {
/* 443 */       String[] names = (String[])value;
/* 444 */       Class<?>[] classes = new Class[names.length];
/* 445 */       for (int i = 0; i < names.length; i++) {
/* 446 */         classes[i] = ClassUtils.resolveClassName(names[i], getClassLoader());
/*     */       }
/* 448 */       value = classes;
/*     */     }
/* 450 */     else if (value instanceof MergedAnnotation && type.isAnnotation()) {
/* 451 */       MergedAnnotation<?> annotation = (MergedAnnotation)value;
/* 452 */       value = annotation.synthesize();
/*     */     }
/* 454 */     else if (value instanceof MergedAnnotation[] && type.isArray() && type.getComponentType().isAnnotation()) {
/* 455 */       MergedAnnotation[] arrayOfMergedAnnotation = (MergedAnnotation[])value;
/* 456 */       Object array = Array.newInstance(type.getComponentType(), arrayOfMergedAnnotation.length);
/* 457 */       for (int i = 0; i < arrayOfMergedAnnotation.length; i++) {
/* 458 */         Array.set(array, i, arrayOfMergedAnnotation[i].synthesize());
/*     */       }
/* 460 */       value = array;
/*     */     } 
/* 462 */     if (!type.isInstance(value)) {
/* 463 */       throw new IllegalArgumentException("Unable to adapt value of type " + value
/* 464 */           .getClass().getName() + " to " + type.getName());
/*     */     }
/* 466 */     return (T)value;
/*     */   }
/*     */ 
/*     */   
/*     */   private Object adaptForAttribute(Method attribute, Object value) {
/* 471 */     Class<?> attributeType = ClassUtils.resolvePrimitiveIfNecessary(attribute.getReturnType());
/* 472 */     if (attributeType.isArray() && !value.getClass().isArray()) {
/* 473 */       Object array = Array.newInstance(value.getClass(), 1);
/* 474 */       Array.set(array, 0, value);
/* 475 */       return adaptForAttribute(attribute, array);
/*     */     } 
/* 477 */     if (attributeType.isAnnotation()) {
/* 478 */       return adaptToMergedAnnotation(value, (Class)attributeType);
/*     */     }
/* 480 */     if (attributeType.isArray() && attributeType.getComponentType().isAnnotation()) {
/* 481 */       MergedAnnotation[] arrayOfMergedAnnotation = new MergedAnnotation[Array.getLength(value)];
/* 482 */       for (int i = 0; i < arrayOfMergedAnnotation.length; i++) {
/* 483 */         arrayOfMergedAnnotation[i] = adaptToMergedAnnotation(Array.get(value, i), (Class)attributeType
/* 484 */             .getComponentType());
/*     */       }
/* 486 */       return arrayOfMergedAnnotation;
/*     */     } 
/* 488 */     if ((attributeType == Class.class && value instanceof String) || (attributeType == Class[].class && value instanceof String[]) || (attributeType == String.class && value instanceof Class) || (attributeType == String[].class && value instanceof Class[]))
/*     */     {
/*     */ 
/*     */       
/* 492 */       return value;
/*     */     }
/* 494 */     if (attributeType.isArray() && isEmptyObjectArray(value)) {
/* 495 */       return emptyArray(attributeType.getComponentType());
/*     */     }
/* 497 */     if (!attributeType.isInstance(value)) {
/* 498 */       throw new IllegalStateException("Attribute '" + attribute.getName() + "' in annotation " + 
/* 499 */           getType().getName() + " should be compatible with " + attributeType
/* 500 */           .getName() + " but a " + value.getClass().getName() + " value was returned");
/*     */     }
/*     */     
/* 503 */     return value;
/*     */   }
/*     */   
/*     */   private boolean isEmptyObjectArray(Object value) {
/* 507 */     return (value instanceof Object[] && ((Object[])value).length == 0);
/*     */   }
/*     */   
/*     */   private Object emptyArray(Class<?> componentType) {
/* 511 */     Object result = EMPTY_ARRAYS.get(componentType);
/* 512 */     if (result == null) {
/* 513 */       result = Array.newInstance(componentType, 0);
/*     */     }
/* 515 */     return result;
/*     */   }
/*     */   
/*     */   private MergedAnnotation<?> adaptToMergedAnnotation(Object value, Class<? extends Annotation> annotationType) {
/* 519 */     if (value instanceof MergedAnnotation) {
/* 520 */       return (MergedAnnotation)value;
/*     */     }
/* 522 */     AnnotationTypeMapping mapping = AnnotationTypeMappings.forAnnotationType(annotationType).get(0);
/* 523 */     return new TypeMappedAnnotation(mapping, null, this.source, value, 
/* 524 */         getValueExtractor(value), this.aggregateIndex);
/*     */   }
/*     */   
/*     */   private ValueExtractor getValueExtractor(Object value) {
/* 528 */     if (value instanceof Annotation) {
/* 529 */       return ReflectionUtils::invokeMethod;
/*     */     }
/* 531 */     if (value instanceof Map) {
/* 532 */       return TypeMappedAnnotation::extractFromMap;
/*     */     }
/* 534 */     return this.valueExtractor;
/*     */   }
/*     */ 
/*     */   
/*     */   private <T> Class<T> getAdaptType(Method attribute, Class<T> type) {
/* 539 */     if (type != Object.class) {
/* 540 */       return type;
/*     */     }
/* 542 */     Class<?> attributeType = attribute.getReturnType();
/* 543 */     if (attributeType.isAnnotation()) {
/* 544 */       return (Class)MergedAnnotation.class;
/*     */     }
/* 546 */     if (attributeType.isArray() && attributeType.getComponentType().isAnnotation()) {
/* 547 */       return (Class)MergedAnnotation[].class;
/*     */     }
/* 549 */     return ClassUtils.resolvePrimitiveIfNecessary(attributeType);
/*     */   }
/*     */   
/*     */   private int getAttributeIndex(String attributeName, boolean required) {
/* 553 */     Assert.hasText(attributeName, "Attribute name must not be null");
/* 554 */     int attributeIndex = isFiltered(attributeName) ? -1 : this.mapping.getAttributes().indexOf(attributeName);
/* 555 */     if (attributeIndex == -1 && required) {
/* 556 */       throw new NoSuchElementException("No attribute named '" + attributeName + "' present in merged annotation " + 
/* 557 */           getType().getName());
/*     */     }
/* 559 */     return attributeIndex;
/*     */   }
/*     */   
/*     */   private boolean isFiltered(String attributeName) {
/* 563 */     if (this.attributeFilter != null) {
/* 564 */       return !this.attributeFilter.test(attributeName);
/*     */     }
/* 566 */     return false;
/*     */   }
/*     */   
/*     */   @Nullable
/*     */   private ClassLoader getClassLoader() {
/* 571 */     if (this.classLoader != null) {
/* 572 */       return this.classLoader;
/*     */     }
/* 574 */     if (this.source != null) {
/* 575 */       if (this.source instanceof Class) {
/* 576 */         return ((Class)this.source).getClassLoader();
/*     */       }
/* 578 */       if (this.source instanceof Member) {
/* 579 */         ((Member)this.source).getDeclaringClass().getClassLoader();
/*     */       }
/*     */     } 
/* 582 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   static <A extends Annotation> MergedAnnotation<A> from(@Nullable Object source, A annotation) {
/* 587 */     Assert.notNull(annotation, "Annotation must not be null");
/* 588 */     AnnotationTypeMappings mappings = AnnotationTypeMappings.forAnnotationType(annotation.annotationType());
/* 589 */     return new TypeMappedAnnotation<>(mappings.get(0), null, source, annotation, ReflectionUtils::invokeMethod, 0);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static <A extends Annotation> MergedAnnotation<A> of(@Nullable ClassLoader classLoader, @Nullable Object source, Class<A> annotationType, @Nullable Map<String, ?> attributes) {
/* 596 */     Assert.notNull(annotationType, "Annotation type must not be null");
/* 597 */     AnnotationTypeMappings mappings = AnnotationTypeMappings.forAnnotationType(annotationType);
/* 598 */     return new TypeMappedAnnotation<>(mappings
/* 599 */         .get(0), classLoader, source, attributes, TypeMappedAnnotation::extractFromMap, 0);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   static <A extends Annotation> TypeMappedAnnotation<A> createIfPossible(AnnotationTypeMapping mapping, MergedAnnotation<?> annotation, IntrospectionFailureLogger logger) {
/* 606 */     if (annotation instanceof TypeMappedAnnotation) {
/* 607 */       TypeMappedAnnotation<?> typeMappedAnnotation = (TypeMappedAnnotation)annotation;
/* 608 */       return createIfPossible(mapping, typeMappedAnnotation.source, typeMappedAnnotation.rootAttributes, typeMappedAnnotation.valueExtractor, typeMappedAnnotation.aggregateIndex, logger);
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/* 613 */     return createIfPossible(mapping, annotation.getSource(), (Annotation)annotation.synthesize(), annotation
/* 614 */         .getAggregateIndex(), logger);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   static <A extends Annotation> TypeMappedAnnotation<A> createIfPossible(AnnotationTypeMapping mapping, @Nullable Object source, Annotation annotation, int aggregateIndex, IntrospectionFailureLogger logger) {
/* 622 */     return createIfPossible(mapping, source, annotation, ReflectionUtils::invokeMethod, aggregateIndex, logger);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   private static <A extends Annotation> TypeMappedAnnotation<A> createIfPossible(AnnotationTypeMapping mapping, @Nullable Object source, @Nullable Object rootAttribute, ValueExtractor valueExtractor, int aggregateIndex, IntrospectionFailureLogger logger) {
/*     */     try {
/* 632 */       return new TypeMappedAnnotation<>(mapping, null, source, rootAttribute, valueExtractor, aggregateIndex);
/*     */     
/*     */     }
/* 635 */     catch (Exception ex) {
/* 636 */       AnnotationUtils.rethrowAnnotationConfigurationException(ex);
/* 637 */       if (logger.isEnabled()) {
/* 638 */         String type = mapping.getAnnotationType().getName();
/*     */         
/* 640 */         String item = (mapping.getDistance() == 0) ? ("annotation " + type) : ("meta-annotation " + type + " from " + mapping.getRoot().getAnnotationType().getName());
/* 641 */         logger.log("Failed to introspect " + item, source, ex);
/*     */       } 
/* 643 */       return null;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   static Object extractFromMap(Method attribute, @Nullable Object map) {
/* 650 */     return (map != null) ? ((Map)map).get(attribute.getName()) : null;
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\springframework\core\annotation\TypeMappedAnnotation.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */