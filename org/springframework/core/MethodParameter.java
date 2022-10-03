/*     */ package org.springframework.core;
/*     */ 
/*     */ import java.lang.annotation.Annotation;
/*     */ import java.lang.reflect.AnnotatedElement;
/*     */ import java.lang.reflect.Constructor;
/*     */ import java.lang.reflect.Executable;
/*     */ import java.lang.reflect.Member;
/*     */ import java.lang.reflect.Method;
/*     */ import java.lang.reflect.Parameter;
/*     */ import java.lang.reflect.ParameterizedType;
/*     */ import java.lang.reflect.Type;
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ import java.util.Optional;
/*     */ import java.util.function.Predicate;
/*     */ import kotlin.Unit;
/*     */ import kotlin.reflect.KFunction;
/*     */ import kotlin.reflect.KParameter;
/*     */ import kotlin.reflect.jvm.ReflectJvmMapping;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class MethodParameter
/*     */ {
/*  63 */   private static final Annotation[] EMPTY_ANNOTATION_ARRAY = new Annotation[0];
/*     */ 
/*     */   
/*     */   private final Executable executable;
/*     */ 
/*     */   
/*     */   private final int parameterIndex;
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   private volatile Parameter parameter;
/*     */ 
/*     */   
/*     */   private int nestingLevel;
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   Map<Integer, Integer> typeIndexesPerLevel;
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   private volatile Class<?> containingClass;
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   private volatile Class<?> parameterType;
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   private volatile Type genericParameterType;
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   private volatile Annotation[] parameterAnnotations;
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   private volatile ParameterNameDiscoverer parameterNameDiscoverer;
/*     */   
/*     */   @Nullable
/*     */   private volatile String parameterName;
/*     */   
/*     */   @Nullable
/*     */   private volatile MethodParameter nestedMethodParameter;
/*     */ 
/*     */   
/*     */   public MethodParameter(Method method, int parameterIndex) {
/* 110 */     this(method, parameterIndex, 1);
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
/*     */   public MethodParameter(Method method, int parameterIndex, int nestingLevel) {
/* 124 */     Assert.notNull(method, "Method must not be null");
/* 125 */     this.executable = method;
/* 126 */     this.parameterIndex = validateIndex(method, parameterIndex);
/* 127 */     this.nestingLevel = nestingLevel;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public MethodParameter(Constructor<?> constructor, int parameterIndex) {
/* 136 */     this(constructor, parameterIndex, 1);
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
/*     */   public MethodParameter(Constructor<?> constructor, int parameterIndex, int nestingLevel) {
/* 148 */     Assert.notNull(constructor, "Constructor must not be null");
/* 149 */     this.executable = constructor;
/* 150 */     this.parameterIndex = validateIndex(constructor, parameterIndex);
/* 151 */     this.nestingLevel = nestingLevel;
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
/*     */   MethodParameter(Executable executable, int parameterIndex, @Nullable Class<?> containingClass) {
/* 163 */     Assert.notNull(executable, "Executable must not be null");
/* 164 */     this.executable = executable;
/* 165 */     this.parameterIndex = validateIndex(executable, parameterIndex);
/* 166 */     this.nestingLevel = 1;
/* 167 */     this.containingClass = containingClass;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public MethodParameter(MethodParameter original) {
/* 176 */     Assert.notNull(original, "Original must not be null");
/* 177 */     this.executable = original.executable;
/* 178 */     this.parameterIndex = original.parameterIndex;
/* 179 */     this.parameter = original.parameter;
/* 180 */     this.nestingLevel = original.nestingLevel;
/* 181 */     this.typeIndexesPerLevel = original.typeIndexesPerLevel;
/* 182 */     this.containingClass = original.containingClass;
/* 183 */     this.parameterType = original.parameterType;
/* 184 */     this.genericParameterType = original.genericParameterType;
/* 185 */     this.parameterAnnotations = original.parameterAnnotations;
/* 186 */     this.parameterNameDiscoverer = original.parameterNameDiscoverer;
/* 187 */     this.parameterName = original.parameterName;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public Method getMethod() {
/* 198 */     return (this.executable instanceof Method) ? (Method)this.executable : null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public Constructor<?> getConstructor() {
/* 208 */     return (this.executable instanceof Constructor) ? (Constructor)this.executable : null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Class<?> getDeclaringClass() {
/* 215 */     return this.executable.getDeclaringClass();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Member getMember() {
/* 223 */     return this.executable;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public AnnotatedElement getAnnotatedElement() {
/* 233 */     return this.executable;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Executable getExecutable() {
/* 242 */     return this.executable;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Parameter getParameter() {
/* 250 */     if (this.parameterIndex < 0) {
/* 251 */       throw new IllegalStateException("Cannot retrieve Parameter descriptor for method return type");
/*     */     }
/* 253 */     Parameter parameter = this.parameter;
/* 254 */     if (parameter == null) {
/* 255 */       parameter = getExecutable().getParameters()[this.parameterIndex];
/* 256 */       this.parameter = parameter;
/*     */     } 
/* 258 */     return parameter;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getParameterIndex() {
/* 266 */     return this.parameterIndex;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   public void increaseNestingLevel() {
/* 276 */     this.nestingLevel++;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   public void decreaseNestingLevel() {
/* 287 */     getTypeIndexesPerLevel().remove(Integer.valueOf(this.nestingLevel));
/* 288 */     this.nestingLevel--;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getNestingLevel() {
/* 297 */     return this.nestingLevel;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public MethodParameter withTypeIndex(int typeIndex) {
/* 307 */     return nested(this.nestingLevel, Integer.valueOf(typeIndex));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   public void setTypeIndexForCurrentLevel(int typeIndex) {
/* 319 */     getTypeIndexesPerLevel().put(Integer.valueOf(this.nestingLevel), Integer.valueOf(typeIndex));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public Integer getTypeIndexForCurrentLevel() {
/* 330 */     return getTypeIndexForLevel(this.nestingLevel);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public Integer getTypeIndexForLevel(int nestingLevel) {
/* 341 */     return getTypeIndexesPerLevel().get(Integer.valueOf(nestingLevel));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private Map<Integer, Integer> getTypeIndexesPerLevel() {
/* 348 */     if (this.typeIndexesPerLevel == null) {
/* 349 */       this.typeIndexesPerLevel = new HashMap<>(4);
/*     */     }
/* 351 */     return this.typeIndexesPerLevel;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public MethodParameter nested() {
/* 360 */     return nested(null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public MethodParameter nested(@Nullable Integer typeIndex) {
/* 370 */     MethodParameter nestedParam = this.nestedMethodParameter;
/* 371 */     if (nestedParam != null && typeIndex == null) {
/* 372 */       return nestedParam;
/*     */     }
/* 374 */     nestedParam = nested(this.nestingLevel + 1, typeIndex);
/* 375 */     if (typeIndex == null) {
/* 376 */       this.nestedMethodParameter = nestedParam;
/*     */     }
/* 378 */     return nestedParam;
/*     */   }
/*     */   
/*     */   private MethodParameter nested(int nestingLevel, @Nullable Integer typeIndex) {
/* 382 */     MethodParameter copy = clone();
/* 383 */     copy.nestingLevel = nestingLevel;
/* 384 */     if (this.typeIndexesPerLevel != null) {
/* 385 */       copy.typeIndexesPerLevel = new HashMap<>(this.typeIndexesPerLevel);
/*     */     }
/* 387 */     if (typeIndex != null) {
/* 388 */       copy.getTypeIndexesPerLevel().put(Integer.valueOf(copy.nestingLevel), typeIndex);
/*     */     }
/* 390 */     copy.parameterType = null;
/* 391 */     copy.genericParameterType = null;
/* 392 */     return copy;
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
/*     */   public boolean isOptional() {
/* 404 */     return (getParameterType() == Optional.class || hasNullableAnnotation() || (
/* 405 */       KotlinDetector.isKotlinReflectPresent() && 
/* 406 */       KotlinDetector.isKotlinType(getContainingClass()) && 
/* 407 */       KotlinDelegate.isOptional(this)));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean hasNullableAnnotation() {
/* 416 */     for (Annotation ann : getParameterAnnotations()) {
/* 417 */       if ("Nullable".equals(ann.annotationType().getSimpleName())) {
/* 418 */         return true;
/*     */       }
/*     */     } 
/* 421 */     return false;
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
/*     */   public MethodParameter nestedIfOptional() {
/* 433 */     return (getParameterType() == Optional.class) ? nested() : this;
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
/*     */   public MethodParameter withContainingClass(@Nullable Class<?> containingClass) {
/* 445 */     MethodParameter result = clone();
/* 446 */     result.containingClass = containingClass;
/* 447 */     result.parameterType = null;
/* 448 */     return result;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   void setContainingClass(Class<?> containingClass) {
/* 456 */     this.containingClass = containingClass;
/* 457 */     this.parameterType = null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Class<?> getContainingClass() {
/* 467 */     Class<?> containingClass = this.containingClass;
/* 468 */     return (containingClass != null) ? containingClass : getDeclaringClass();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   void setParameterType(@Nullable Class<?> parameterType) {
/* 476 */     this.parameterType = parameterType;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Class<?> getParameterType() {
/* 484 */     Class<?> paramType = this.parameterType;
/* 485 */     if (paramType != null) {
/* 486 */       return paramType;
/*     */     }
/* 488 */     if (getContainingClass() != getDeclaringClass()) {
/* 489 */       paramType = ResolvableType.forMethodParameter(this, (Type)null, 1).resolve();
/*     */     }
/* 491 */     if (paramType == null) {
/* 492 */       paramType = computeParameterType();
/*     */     }
/* 494 */     this.parameterType = paramType;
/* 495 */     return paramType;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Type getGenericParameterType() {
/* 504 */     Type paramType = this.genericParameterType;
/* 505 */     if (paramType == null) {
/* 506 */       if (this.parameterIndex < 0) {
/* 507 */         Method method = getMethod();
/*     */ 
/*     */         
/* 510 */         paramType = (method != null) ? ((KotlinDetector.isKotlinReflectPresent() && KotlinDetector.isKotlinType(getContainingClass())) ? KotlinDelegate.getGenericReturnType(method) : method.getGenericReturnType()) : void.class;
/*     */       } else {
/*     */         
/* 513 */         Type[] genericParameterTypes = this.executable.getGenericParameterTypes();
/* 514 */         int index = this.parameterIndex;
/* 515 */         if (this.executable instanceof Constructor && 
/* 516 */           ClassUtils.isInnerClass(this.executable.getDeclaringClass()) && genericParameterTypes.length == this.executable
/* 517 */           .getParameterCount() - 1)
/*     */         {
/*     */ 
/*     */           
/* 521 */           index = this.parameterIndex - 1;
/*     */         }
/*     */         
/* 524 */         paramType = (index >= 0 && index < genericParameterTypes.length) ? genericParameterTypes[index] : computeParameterType();
/*     */       } 
/* 526 */       this.genericParameterType = paramType;
/*     */     } 
/* 528 */     return paramType;
/*     */   }
/*     */   
/*     */   private Class<?> computeParameterType() {
/* 532 */     if (this.parameterIndex < 0) {
/* 533 */       Method method = getMethod();
/* 534 */       if (method == null) {
/* 535 */         return void.class;
/*     */       }
/* 537 */       if (KotlinDetector.isKotlinReflectPresent() && KotlinDetector.isKotlinType(getContainingClass())) {
/* 538 */         return KotlinDelegate.getReturnType(method);
/*     */       }
/* 540 */       return method.getReturnType();
/*     */     } 
/* 542 */     return this.executable.getParameterTypes()[this.parameterIndex];
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Class<?> getNestedParameterType() {
/* 552 */     if (this.nestingLevel > 1) {
/* 553 */       Type type = getGenericParameterType();
/* 554 */       for (int i = 2; i <= this.nestingLevel; i++) {
/* 555 */         if (type instanceof ParameterizedType) {
/* 556 */           Type[] args = ((ParameterizedType)type).getActualTypeArguments();
/* 557 */           Integer index = getTypeIndexForLevel(i);
/* 558 */           type = args[(index != null) ? index.intValue() : (args.length - 1)];
/*     */         } 
/*     */       } 
/*     */       
/* 562 */       if (type instanceof Class) {
/* 563 */         return (Class)type;
/*     */       }
/* 565 */       if (type instanceof ParameterizedType) {
/* 566 */         Type arg = ((ParameterizedType)type).getRawType();
/* 567 */         if (arg instanceof Class) {
/* 568 */           return (Class)arg;
/*     */         }
/*     */       } 
/* 571 */       return Object.class;
/*     */     } 
/*     */     
/* 574 */     return getParameterType();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Type getNestedGenericParameterType() {
/* 585 */     if (this.nestingLevel > 1) {
/* 586 */       Type type = getGenericParameterType();
/* 587 */       for (int i = 2; i <= this.nestingLevel; i++) {
/* 588 */         if (type instanceof ParameterizedType) {
/* 589 */           Type[] args = ((ParameterizedType)type).getActualTypeArguments();
/* 590 */           Integer index = getTypeIndexForLevel(i);
/* 591 */           type = args[(index != null) ? index.intValue() : (args.length - 1)];
/*     */         } 
/*     */       } 
/* 594 */       return type;
/*     */     } 
/*     */     
/* 597 */     return getGenericParameterType();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Annotation[] getMethodAnnotations() {
/* 605 */     return adaptAnnotationArray(getAnnotatedElement().getAnnotations());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public <A extends Annotation> A getMethodAnnotation(Class<A> annotationType) {
/* 615 */     A annotation = getAnnotatedElement().getAnnotation(annotationType);
/* 616 */     return (annotation != null) ? adaptAnnotation(annotation) : null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public <A extends Annotation> boolean hasMethodAnnotation(Class<A> annotationType) {
/* 626 */     return getAnnotatedElement().isAnnotationPresent(annotationType);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Annotation[] getParameterAnnotations() {
/* 633 */     Annotation[] paramAnns = this.parameterAnnotations;
/* 634 */     if (paramAnns == null) {
/* 635 */       Annotation[][] annotationArray = this.executable.getParameterAnnotations();
/* 636 */       int index = this.parameterIndex;
/* 637 */       if (this.executable instanceof Constructor && 
/* 638 */         ClassUtils.isInnerClass(this.executable.getDeclaringClass()) && annotationArray.length == this.executable
/* 639 */         .getParameterCount() - 1)
/*     */       {
/*     */         
/* 642 */         index = this.parameterIndex - 1;
/*     */       }
/*     */       
/* 645 */       paramAnns = (index >= 0 && index < annotationArray.length) ? adaptAnnotationArray(annotationArray[index]) : EMPTY_ANNOTATION_ARRAY;
/* 646 */       this.parameterAnnotations = paramAnns;
/*     */     } 
/* 648 */     return paramAnns;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean hasParameterAnnotations() {
/* 657 */     return ((getParameterAnnotations()).length != 0);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public <A extends Annotation> A getParameterAnnotation(Class<A> annotationType) {
/* 668 */     Annotation[] anns = getParameterAnnotations();
/* 669 */     for (Annotation ann : anns) {
/* 670 */       if (annotationType.isInstance(ann)) {
/* 671 */         return (A)ann;
/*     */       }
/*     */     } 
/* 674 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public <A extends Annotation> boolean hasParameterAnnotation(Class<A> annotationType) {
/* 683 */     return (getParameterAnnotation(annotationType) != null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void initParameterNameDiscovery(@Nullable ParameterNameDiscoverer parameterNameDiscoverer) {
/* 693 */     this.parameterNameDiscoverer = parameterNameDiscoverer;
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
/*     */   public String getParameterName() {
/* 705 */     if (this.parameterIndex < 0) {
/* 706 */       return null;
/*     */     }
/* 708 */     ParameterNameDiscoverer discoverer = this.parameterNameDiscoverer;
/* 709 */     if (discoverer != null) {
/* 710 */       String[] parameterNames = null;
/* 711 */       if (this.executable instanceof Method) {
/* 712 */         parameterNames = discoverer.getParameterNames((Method)this.executable);
/*     */       }
/* 714 */       else if (this.executable instanceof Constructor) {
/* 715 */         parameterNames = discoverer.getParameterNames((Constructor)this.executable);
/*     */       } 
/* 717 */       if (parameterNames != null) {
/* 718 */         this.parameterName = parameterNames[this.parameterIndex];
/*     */       }
/* 720 */       this.parameterNameDiscoverer = null;
/*     */     } 
/* 722 */     return this.parameterName;
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
/*     */   protected <A extends Annotation> A adaptAnnotation(A annotation) {
/* 735 */     return annotation;
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
/*     */   protected Annotation[] adaptAnnotationArray(Annotation[] annotations) {
/* 747 */     return annotations;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean equals(@Nullable Object other) {
/* 753 */     if (this == other) {
/* 754 */       return true;
/*     */     }
/* 756 */     if (!(other instanceof MethodParameter)) {
/* 757 */       return false;
/*     */     }
/* 759 */     MethodParameter otherParam = (MethodParameter)other;
/* 760 */     return (getContainingClass() == otherParam.getContainingClass() && 
/* 761 */       ObjectUtils.nullSafeEquals(this.typeIndexesPerLevel, otherParam.typeIndexesPerLevel) && this.nestingLevel == otherParam.nestingLevel && this.parameterIndex == otherParam.parameterIndex && this.executable
/*     */ 
/*     */       
/* 764 */       .equals(otherParam.executable));
/*     */   }
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 769 */     return 31 * this.executable.hashCode() + this.parameterIndex;
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 774 */     Method method = getMethod();
/* 775 */     return ((method != null) ? ("method '" + method.getName() + "'") : "constructor") + " parameter " + this.parameterIndex;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public MethodParameter clone() {
/* 781 */     return new MethodParameter(this);
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
/*     */   @Deprecated
/*     */   public static MethodParameter forMethodOrConstructor(Object methodOrConstructor, int parameterIndex) {
/* 795 */     if (!(methodOrConstructor instanceof Executable)) {
/* 796 */       throw new IllegalArgumentException("Given object [" + methodOrConstructor + "] is neither a Method nor a Constructor");
/*     */     }
/*     */     
/* 799 */     return forExecutable((Executable)methodOrConstructor, parameterIndex);
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
/*     */   public static MethodParameter forExecutable(Executable executable, int parameterIndex) {
/* 812 */     if (executable instanceof Method) {
/* 813 */       return new MethodParameter((Method)executable, parameterIndex);
/*     */     }
/* 815 */     if (executable instanceof Constructor) {
/* 816 */       return new MethodParameter((Constructor)executable, parameterIndex);
/*     */     }
/*     */     
/* 819 */     throw new IllegalArgumentException("Not a Method/Constructor: " + executable);
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
/*     */   public static MethodParameter forParameter(Parameter parameter) {
/* 832 */     return forExecutable(parameter.getDeclaringExecutable(), findParameterIndex(parameter));
/*     */   }
/*     */   
/*     */   protected static int findParameterIndex(Parameter parameter) {
/* 836 */     Executable executable = parameter.getDeclaringExecutable();
/* 837 */     Parameter[] allParams = executable.getParameters();
/*     */     int i;
/* 839 */     for (i = 0; i < allParams.length; i++) {
/* 840 */       if (parameter == allParams[i]) {
/* 841 */         return i;
/*     */       }
/*     */     } 
/*     */ 
/*     */     
/* 846 */     for (i = 0; i < allParams.length; i++) {
/* 847 */       if (parameter.equals(allParams[i])) {
/* 848 */         return i;
/*     */       }
/*     */     } 
/* 851 */     throw new IllegalArgumentException("Given parameter [" + parameter + "] does not match any parameter in the declaring executable");
/*     */   }
/*     */ 
/*     */   
/*     */   private static int validateIndex(Executable executable, int parameterIndex) {
/* 856 */     int count = executable.getParameterCount();
/* 857 */     Assert.isTrue((parameterIndex >= -1 && parameterIndex < count), () -> "Parameter index needs to be between -1 and " + (count - 1));
/*     */     
/* 859 */     return parameterIndex;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static class KotlinDelegate
/*     */   {
/*     */     public static boolean isOptional(MethodParameter param) {
/*     */       KFunction<?> function;
/*     */       Predicate<KParameter> predicate;
/* 874 */       Method method = param.getMethod();
/* 875 */       int index = param.getParameterIndex();
/* 876 */       if (method != null && index == -1) {
/* 877 */         function = ReflectJvmMapping.getKotlinFunction(method);
/* 878 */         return (function != null && function.getReturnType().isMarkedNullable());
/*     */       } 
/*     */ 
/*     */       
/* 882 */       if (method != null) {
/* 883 */         if (param.getParameterType().getName().equals("kotlin.coroutines.Continuation")) {
/* 884 */           return true;
/*     */         }
/* 886 */         function = ReflectJvmMapping.getKotlinFunction(method);
/* 887 */         predicate = (p -> KParameter.Kind.VALUE.equals(p.getKind()));
/*     */       } else {
/*     */         
/* 890 */         Constructor<?> ctor = param.getConstructor();
/* 891 */         Assert.state((ctor != null), "Neither method nor constructor found");
/* 892 */         function = ReflectJvmMapping.getKotlinFunction(ctor);
/* 893 */         predicate = (p -> (KParameter.Kind.VALUE.equals(p.getKind()) || KParameter.Kind.INSTANCE.equals(p.getKind())));
/*     */       } 
/*     */       
/* 896 */       if (function != null) {
/* 897 */         int i = 0;
/* 898 */         for (KParameter kParameter : function.getParameters()) {
/* 899 */           if (predicate.test(kParameter) && 
/* 900 */             index == i++) {
/* 901 */             return (kParameter.getType().isMarkedNullable() || kParameter.isOptional());
/*     */           }
/*     */         } 
/*     */       } 
/*     */       
/* 906 */       return false;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     private static Type getGenericReturnType(Method method) {
/*     */       try {
/* 915 */         KFunction<?> function = ReflectJvmMapping.getKotlinFunction(method);
/* 916 */         if (function != null && function.isSuspend()) {
/* 917 */           return ReflectJvmMapping.getJavaType(function.getReturnType());
/*     */         }
/*     */       }
/* 920 */       catch (UnsupportedOperationException unsupportedOperationException) {}
/*     */ 
/*     */       
/* 923 */       return method.getGenericReturnType();
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     private static Class<?> getReturnType(Method method) {
/*     */       try {
/* 932 */         KFunction<?> function = ReflectJvmMapping.getKotlinFunction(method);
/* 933 */         if (function != null && function.isSuspend()) {
/* 934 */           Type<void> paramType = ReflectJvmMapping.getJavaType(function.getReturnType());
/* 935 */           if (paramType == Unit.class) {
/* 936 */             paramType = void.class;
/*     */           }
/* 938 */           return ResolvableType.forType(paramType).resolve(method.getReturnType());
/*     */         }
/*     */       
/* 941 */       } catch (UnsupportedOperationException unsupportedOperationException) {}
/*     */ 
/*     */       
/* 944 */       return method.getReturnType();
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\springframework\core\MethodParameter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */