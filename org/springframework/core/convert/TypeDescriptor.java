/*     */ package org.springframework.core.convert;
/*     */ 
/*     */ import java.io.Serializable;
/*     */ import java.lang.annotation.Annotation;
/*     */ import java.lang.reflect.AnnotatedElement;
/*     */ import java.lang.reflect.Field;
/*     */ import java.util.Arrays;
/*     */ import java.util.Collection;
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ import java.util.stream.Stream;
/*     */ import org.springframework.core.MethodParameter;
/*     */ import org.springframework.core.ResolvableType;
/*     */ import org.springframework.core.annotation.AnnotatedElementUtils;
/*     */ import org.springframework.lang.Nullable;
/*     */ import org.springframework.util.Assert;
/*     */ import org.springframework.util.ClassUtils;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ public class TypeDescriptor
/*     */   implements Serializable
/*     */ {
/*  55 */   private static final Annotation[] EMPTY_ANNOTATION_ARRAY = new Annotation[0];
/*     */   
/*  57 */   private static final Map<Class<?>, TypeDescriptor> commonTypesCache = new HashMap<>(32);
/*     */   
/*  59 */   private static final Class<?>[] CACHED_COMMON_TYPES = new Class[] { boolean.class, Boolean.class, byte.class, Byte.class, char.class, Character.class, double.class, Double.class, float.class, Float.class, int.class, Integer.class, long.class, Long.class, short.class, Short.class, String.class, Object.class };
/*     */   private final Class<?> type;
/*     */   private final ResolvableType resolvableType;
/*     */   private final AnnotatedElementAdapter annotatedElement;
/*     */   
/*     */   static {
/*  65 */     for (Class<?> preCachedClass : CACHED_COMMON_TYPES) {
/*  66 */       commonTypesCache.put(preCachedClass, valueOf(preCachedClass));
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
/*     */ 
/*     */ 
/*     */   
/*     */   public TypeDescriptor(MethodParameter methodParameter) {
/*  85 */     this.resolvableType = ResolvableType.forMethodParameter(methodParameter);
/*  86 */     this.type = this.resolvableType.resolve(methodParameter.getNestedParameterType());
/*  87 */     this
/*  88 */       .annotatedElement = new AnnotatedElementAdapter((methodParameter.getParameterIndex() == -1) ? methodParameter.getMethodAnnotations() : methodParameter.getParameterAnnotations());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public TypeDescriptor(Field field) {
/*  97 */     this.resolvableType = ResolvableType.forField(field);
/*  98 */     this.type = this.resolvableType.resolve(field.getType());
/*  99 */     this.annotatedElement = new AnnotatedElementAdapter(field.getAnnotations());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public TypeDescriptor(Property property) {
/* 109 */     Assert.notNull(property, "Property must not be null");
/* 110 */     this.resolvableType = ResolvableType.forMethodParameter(property.getMethodParameter());
/* 111 */     this.type = this.resolvableType.resolve(property.getType());
/* 112 */     this.annotatedElement = new AnnotatedElementAdapter(property.getAnnotations());
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
/*     */   public TypeDescriptor(ResolvableType resolvableType, @Nullable Class<?> type, @Nullable Annotation[] annotations) {
/* 126 */     this.resolvableType = resolvableType;
/* 127 */     this.type = (type != null) ? type : resolvableType.toClass();
/* 128 */     this.annotatedElement = new AnnotatedElementAdapter(annotations);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Class<?> getObjectType() {
/* 139 */     return ClassUtils.resolvePrimitiveIfNecessary(getType());
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
/*     */   public Class<?> getType() {
/* 151 */     return this.type;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ResolvableType getResolvableType() {
/* 159 */     return this.resolvableType;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Object getSource() {
/* 170 */     return this.resolvableType.getSource();
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
/*     */   public TypeDescriptor narrow(@Nullable Object value) {
/* 190 */     if (value == null) {
/* 191 */       return this;
/*     */     }
/* 193 */     ResolvableType narrowed = ResolvableType.forType(value.getClass(), getResolvableType());
/* 194 */     return new TypeDescriptor(narrowed, value.getClass(), getAnnotations());
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
/*     */   public TypeDescriptor upcast(@Nullable Class<?> superType) {
/* 207 */     if (superType == null) {
/* 208 */       return null;
/*     */     }
/* 210 */     Assert.isAssignable(superType, getType());
/* 211 */     return new TypeDescriptor(getResolvableType().as(superType), superType, getAnnotations());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getName() {
/* 218 */     return ClassUtils.getQualifiedName(getType());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isPrimitive() {
/* 225 */     return getType().isPrimitive();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Annotation[] getAnnotations() {
/* 233 */     return this.annotatedElement.getAnnotations();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean hasAnnotation(Class<? extends Annotation> annotationType) {
/* 244 */     if (this.annotatedElement.isEmpty())
/*     */     {
/*     */       
/* 247 */       return false;
/*     */     }
/* 249 */     return AnnotatedElementUtils.isAnnotated(this.annotatedElement, annotationType);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public <T extends Annotation> T getAnnotation(Class<T> annotationType) {
/* 260 */     if (this.annotatedElement.isEmpty())
/*     */     {
/*     */       
/* 263 */       return null;
/*     */     }
/* 265 */     return (T)AnnotatedElementUtils.getMergedAnnotation(this.annotatedElement, annotationType);
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
/*     */   public boolean isAssignableTo(TypeDescriptor typeDescriptor) {
/* 283 */     boolean typesAssignable = typeDescriptor.getObjectType().isAssignableFrom(getObjectType());
/* 284 */     if (!typesAssignable) {
/* 285 */       return false;
/*     */     }
/* 287 */     if (isArray() && typeDescriptor.isArray()) {
/* 288 */       return isNestedAssignable(getElementTypeDescriptor(), typeDescriptor.getElementTypeDescriptor());
/*     */     }
/* 290 */     if (isCollection() && typeDescriptor.isCollection()) {
/* 291 */       return isNestedAssignable(getElementTypeDescriptor(), typeDescriptor.getElementTypeDescriptor());
/*     */     }
/* 293 */     if (isMap() && typeDescriptor.isMap()) {
/* 294 */       return (isNestedAssignable(getMapKeyTypeDescriptor(), typeDescriptor.getMapKeyTypeDescriptor()) && 
/* 295 */         isNestedAssignable(getMapValueTypeDescriptor(), typeDescriptor.getMapValueTypeDescriptor()));
/*     */     }
/*     */     
/* 298 */     return true;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean isNestedAssignable(@Nullable TypeDescriptor nestedTypeDescriptor, @Nullable TypeDescriptor otherNestedTypeDescriptor) {
/* 305 */     return (nestedTypeDescriptor == null || otherNestedTypeDescriptor == null || nestedTypeDescriptor
/* 306 */       .isAssignableTo(otherNestedTypeDescriptor));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isCollection() {
/* 313 */     return Collection.class.isAssignableFrom(getType());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isArray() {
/* 320 */     return getType().isArray();
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
/*     */   @Nullable
/*     */   public TypeDescriptor getElementTypeDescriptor() {
/* 334 */     if (getResolvableType().isArray()) {
/* 335 */       return new TypeDescriptor(getResolvableType().getComponentType(), null, getAnnotations());
/*     */     }
/* 337 */     if (Stream.class.isAssignableFrom(getType())) {
/* 338 */       return getRelatedIfResolvable(this, getResolvableType().as(Stream.class).getGeneric(new int[] { 0 }));
/*     */     }
/* 340 */     return getRelatedIfResolvable(this, getResolvableType().asCollection().getGeneric(new int[] { 0 }));
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
/*     */   @Nullable
/*     */   public TypeDescriptor elementTypeDescriptor(Object element) {
/* 362 */     return narrow(element, getElementTypeDescriptor());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isMap() {
/* 369 */     return Map.class.isAssignableFrom(getType());
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
/*     */   public TypeDescriptor getMapKeyTypeDescriptor() {
/* 382 */     Assert.state(isMap(), "Not a [java.util.Map]");
/* 383 */     return getRelatedIfResolvable(this, getResolvableType().asMap().getGeneric(new int[] { 0 }));
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
/*     */   @Nullable
/*     */   public TypeDescriptor getMapKeyTypeDescriptor(Object mapKey) {
/* 405 */     return narrow(mapKey, getMapKeyTypeDescriptor());
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
/*     */   @Nullable
/*     */   public TypeDescriptor getMapValueTypeDescriptor() {
/* 419 */     Assert.state(isMap(), "Not a [java.util.Map]");
/* 420 */     return getRelatedIfResolvable(this, getResolvableType().asMap().getGeneric(new int[] { 1 }));
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
/*     */   @Nullable
/*     */   public TypeDescriptor getMapValueTypeDescriptor(Object mapValue) {
/* 442 */     return narrow(mapValue, getMapValueTypeDescriptor());
/*     */   }
/*     */   
/*     */   @Nullable
/*     */   private TypeDescriptor narrow(@Nullable Object value, @Nullable TypeDescriptor typeDescriptor) {
/* 447 */     if (typeDescriptor != null) {
/* 448 */       return typeDescriptor.narrow(value);
/*     */     }
/* 450 */     if (value != null) {
/* 451 */       return narrow(value);
/*     */     }
/* 453 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean equals(@Nullable Object other) {
/* 458 */     if (this == other) {
/* 459 */       return true;
/*     */     }
/* 461 */     if (!(other instanceof TypeDescriptor)) {
/* 462 */       return false;
/*     */     }
/* 464 */     TypeDescriptor otherDesc = (TypeDescriptor)other;
/* 465 */     if (getType() != otherDesc.getType()) {
/* 466 */       return false;
/*     */     }
/* 468 */     if (!annotationsMatch(otherDesc)) {
/* 469 */       return false;
/*     */     }
/* 471 */     if (isCollection() || isArray()) {
/* 472 */       return ObjectUtils.nullSafeEquals(getElementTypeDescriptor(), otherDesc.getElementTypeDescriptor());
/*     */     }
/* 474 */     if (isMap()) {
/* 475 */       return (ObjectUtils.nullSafeEquals(getMapKeyTypeDescriptor(), otherDesc.getMapKeyTypeDescriptor()) && 
/* 476 */         ObjectUtils.nullSafeEquals(getMapValueTypeDescriptor(), otherDesc.getMapValueTypeDescriptor()));
/*     */     }
/*     */     
/* 479 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   private boolean annotationsMatch(TypeDescriptor otherDesc) {
/* 484 */     Annotation[] anns = getAnnotations();
/* 485 */     Annotation[] otherAnns = otherDesc.getAnnotations();
/* 486 */     if (anns == otherAnns) {
/* 487 */       return true;
/*     */     }
/* 489 */     if (anns.length != otherAnns.length) {
/* 490 */       return false;
/*     */     }
/* 492 */     if (anns.length > 0) {
/* 493 */       for (int i = 0; i < anns.length; i++) {
/* 494 */         if (!annotationEquals(anns[i], otherAnns[i])) {
/* 495 */           return false;
/*     */         }
/*     */       } 
/*     */     }
/* 499 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   private boolean annotationEquals(Annotation ann, Annotation otherAnn) {
/* 504 */     return (ann == otherAnn || (ann.getClass() == otherAnn.getClass() && ann.equals(otherAnn)));
/*     */   }
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 509 */     return getType().hashCode();
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 514 */     StringBuilder builder = new StringBuilder();
/* 515 */     for (Annotation ann : getAnnotations()) {
/* 516 */       builder.append('@').append(ann.annotationType().getName()).append(' ');
/*     */     }
/* 518 */     builder.append(getResolvableType());
/* 519 */     return builder.toString();
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
/*     */   public static TypeDescriptor forObject(@Nullable Object source) {
/* 534 */     return (source != null) ? valueOf(source.getClass()) : null;
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
/*     */   public static TypeDescriptor valueOf(@Nullable Class<?> type) {
/* 548 */     if (type == null) {
/* 549 */       type = Object.class;
/*     */     }
/* 551 */     TypeDescriptor desc = commonTypesCache.get(type);
/* 552 */     return (desc != null) ? desc : new TypeDescriptor(ResolvableType.forClass(type), null, null);
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
/*     */   public static TypeDescriptor collection(Class<?> collectionType, @Nullable TypeDescriptor elementTypeDescriptor) {
/* 568 */     Assert.notNull(collectionType, "Collection type must not be null");
/* 569 */     if (!Collection.class.isAssignableFrom(collectionType)) {
/* 570 */       throw new IllegalArgumentException("Collection type must be a [java.util.Collection]");
/*     */     }
/* 572 */     ResolvableType element = (elementTypeDescriptor != null) ? elementTypeDescriptor.resolvableType : null;
/* 573 */     return new TypeDescriptor(ResolvableType.forClassWithGenerics(collectionType, new ResolvableType[] { element }), null, null);
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
/*     */   public static TypeDescriptor map(Class<?> mapType, @Nullable TypeDescriptor keyTypeDescriptor, @Nullable TypeDescriptor valueTypeDescriptor) {
/* 593 */     Assert.notNull(mapType, "Map type must not be null");
/* 594 */     if (!Map.class.isAssignableFrom(mapType)) {
/* 595 */       throw new IllegalArgumentException("Map type must be a [java.util.Map]");
/*     */     }
/* 597 */     ResolvableType key = (keyTypeDescriptor != null) ? keyTypeDescriptor.resolvableType : null;
/* 598 */     ResolvableType value = (valueTypeDescriptor != null) ? valueTypeDescriptor.resolvableType : null;
/* 599 */     return new TypeDescriptor(ResolvableType.forClassWithGenerics(mapType, new ResolvableType[] { key, value }), null, null);
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
/*     */   public static TypeDescriptor array(@Nullable TypeDescriptor elementTypeDescriptor) {
/* 614 */     if (elementTypeDescriptor == null) {
/* 615 */       return null;
/*     */     }
/* 617 */     return new TypeDescriptor(ResolvableType.forArrayComponent(elementTypeDescriptor.resolvableType), null, elementTypeDescriptor
/* 618 */         .getAnnotations());
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
/*     */   @Nullable
/*     */   public static TypeDescriptor nested(MethodParameter methodParameter, int nestingLevel) {
/* 645 */     if (methodParameter.getNestingLevel() != 1) {
/* 646 */       throw new IllegalArgumentException("MethodParameter nesting level must be 1: use the nestingLevel parameter to specify the desired nestingLevel for nested type traversal");
/*     */     }
/*     */     
/* 649 */     return nested(new TypeDescriptor(methodParameter), nestingLevel);
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
/*     */   @Nullable
/*     */   public static TypeDescriptor nested(Field field, int nestingLevel) {
/* 675 */     return nested(new TypeDescriptor(field), nestingLevel);
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
/*     */   @Nullable
/*     */   public static TypeDescriptor nested(Property property, int nestingLevel) {
/* 701 */     return nested(new TypeDescriptor(property), nestingLevel);
/*     */   }
/*     */   
/*     */   @Nullable
/*     */   private static TypeDescriptor nested(TypeDescriptor typeDescriptor, int nestingLevel) {
/* 706 */     ResolvableType nested = typeDescriptor.resolvableType;
/* 707 */     for (int i = 0; i < nestingLevel; i++) {
/* 708 */       if (Object.class != nested.getType())
/*     */       {
/*     */ 
/*     */ 
/*     */         
/* 713 */         nested = nested.getNested(2);
/*     */       }
/*     */     } 
/* 716 */     if (nested == ResolvableType.NONE) {
/* 717 */       return null;
/*     */     }
/* 719 */     return getRelatedIfResolvable(typeDescriptor, nested);
/*     */   }
/*     */   
/*     */   @Nullable
/*     */   private static TypeDescriptor getRelatedIfResolvable(TypeDescriptor source, ResolvableType type) {
/* 724 */     if (type.resolve() == null) {
/* 725 */       return null;
/*     */     }
/* 727 */     return new TypeDescriptor(type, null, source.getAnnotations());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private class AnnotatedElementAdapter
/*     */     implements AnnotatedElement, Serializable
/*     */   {
/*     */     @Nullable
/*     */     private final Annotation[] annotations;
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public AnnotatedElementAdapter(Annotation[] annotations) {
/* 743 */       this.annotations = annotations;
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean isAnnotationPresent(Class<? extends Annotation> annotationClass) {
/* 748 */       for (Annotation annotation : getAnnotations()) {
/* 749 */         if (annotation.annotationType() == annotationClass) {
/* 750 */           return true;
/*     */         }
/*     */       } 
/* 753 */       return false;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     @Nullable
/*     */     public <T extends Annotation> T getAnnotation(Class<T> annotationClass) {
/* 760 */       for (Annotation annotation : getAnnotations()) {
/* 761 */         if (annotation.annotationType() == annotationClass) {
/* 762 */           return (T)annotation;
/*     */         }
/*     */       } 
/* 765 */       return null;
/*     */     }
/*     */ 
/*     */     
/*     */     public Annotation[] getAnnotations() {
/* 770 */       return (this.annotations != null) ? (Annotation[])this.annotations.clone() : TypeDescriptor.EMPTY_ANNOTATION_ARRAY;
/*     */     }
/*     */ 
/*     */     
/*     */     public Annotation[] getDeclaredAnnotations() {
/* 775 */       return getAnnotations();
/*     */     }
/*     */     
/*     */     public boolean isEmpty() {
/* 779 */       return ObjectUtils.isEmpty((Object[])this.annotations);
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean equals(@Nullable Object other) {
/* 784 */       return (this == other || (other instanceof AnnotatedElementAdapter && 
/* 785 */         Arrays.equals((Object[])this.annotations, (Object[])((AnnotatedElementAdapter)other).annotations)));
/*     */     }
/*     */ 
/*     */     
/*     */     public int hashCode() {
/* 790 */       return Arrays.hashCode((Object[])this.annotations);
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString() {
/* 795 */       return TypeDescriptor.this.toString();
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\springframework\core\convert\TypeDescriptor.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */