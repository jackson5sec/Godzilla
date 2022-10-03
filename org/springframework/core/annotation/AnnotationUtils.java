/*      */ package org.springframework.core.annotation;
/*      */ 
/*      */ import java.lang.annotation.Annotation;
/*      */ import java.lang.reflect.AnnotatedElement;
/*      */ import java.lang.reflect.Array;
/*      */ import java.lang.reflect.InvocationTargetException;
/*      */ import java.lang.reflect.Method;
/*      */ import java.lang.reflect.Modifier;
/*      */ import java.util.Collection;
/*      */ import java.util.Collections;
/*      */ import java.util.List;
/*      */ import java.util.Map;
/*      */ import java.util.NoSuchElementException;
/*      */ import java.util.Set;
/*      */ import org.springframework.core.BridgeMethodResolver;
/*      */ import org.springframework.lang.Nullable;
/*      */ import org.springframework.util.CollectionUtils;
/*      */ import org.springframework.util.ConcurrentReferenceHashMap;
/*      */ import org.springframework.util.ReflectionUtils;
/*      */ import org.springframework.util.StringUtils;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ public abstract class AnnotationUtils
/*      */ {
/*      */   public static final String VALUE = "value";
/*  113 */   private static final AnnotationFilter JAVA_LANG_ANNOTATION_FILTER = AnnotationFilter.packages(new String[] { "java.lang.annotation" });
/*      */   
/*  115 */   private static final Map<Class<? extends Annotation>, Map<String, DefaultValueHolder>> defaultValuesCache = (Map<Class<? extends Annotation>, Map<String, DefaultValueHolder>>)new ConcurrentReferenceHashMap();
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static boolean isCandidateClass(Class<?> clazz, Collection<Class<? extends Annotation>> annotationTypes) {
/*  132 */     for (Class<? extends Annotation> annotationType : annotationTypes) {
/*  133 */       if (isCandidateClass(clazz, annotationType)) {
/*  134 */         return true;
/*      */       }
/*      */     } 
/*  137 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static boolean isCandidateClass(Class<?> clazz, Class<? extends Annotation> annotationType) {
/*  152 */     return isCandidateClass(clazz, annotationType.getName());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static boolean isCandidateClass(Class<?> clazz, String annotationName) {
/*  167 */     if (annotationName.startsWith("java.")) {
/*  168 */       return true;
/*      */     }
/*  170 */     if (AnnotationsScanner.hasPlainJavaAnnotationsOnly(clazz)) {
/*  171 */       return false;
/*      */     }
/*  173 */     return true;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Nullable
/*      */   public static <A extends Annotation> A getAnnotation(Annotation annotation, Class<A> annotationType) {
/*  192 */     if (annotationType.isInstance(annotation)) {
/*  193 */       return synthesizeAnnotation((A)annotation, annotationType);
/*      */     }
/*      */     
/*  196 */     if (AnnotationsScanner.hasPlainJavaAnnotationsOnly(annotation)) {
/*  197 */       return null;
/*      */     }
/*      */     
/*  200 */     return (A)MergedAnnotations.from(annotation, new Annotation[] { annotation }, RepeatableContainers.none())
/*  201 */       .<Annotation>get(annotationType).withNonMergedAttributes()
/*  202 */       .synthesize(AnnotationUtils::isSingleLevelPresent).orElse(null);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Nullable
/*      */   public static <A extends Annotation> A getAnnotation(AnnotatedElement annotatedElement, Class<A> annotationType) {
/*  220 */     if (AnnotationFilter.PLAIN.matches(annotationType) || 
/*  221 */       AnnotationsScanner.hasPlainJavaAnnotationsOnly(annotatedElement)) {
/*  222 */       return annotatedElement.getAnnotation(annotationType);
/*      */     }
/*      */     
/*  225 */     return (A)MergedAnnotations.from(annotatedElement, MergedAnnotations.SearchStrategy.INHERITED_ANNOTATIONS, RepeatableContainers.none())
/*  226 */       .<Annotation>get(annotationType).withNonMergedAttributes()
/*  227 */       .synthesize(AnnotationUtils::isSingleLevelPresent).orElse(null);
/*      */   }
/*      */   
/*      */   private static <A extends Annotation> boolean isSingleLevelPresent(MergedAnnotation<A> mergedAnnotation) {
/*  231 */     int distance = mergedAnnotation.getDistance();
/*  232 */     return (distance == 0 || distance == 1);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Nullable
/*      */   public static <A extends Annotation> A getAnnotation(Method method, Class<A> annotationType) {
/*  251 */     Method resolvedMethod = BridgeMethodResolver.findBridgedMethod(method);
/*  252 */     return getAnnotation(resolvedMethod, annotationType);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Deprecated
/*      */   @Nullable
/*      */   public static Annotation[] getAnnotations(AnnotatedElement annotatedElement) {
/*      */     try {
/*  271 */       return synthesizeAnnotationArray(annotatedElement.getAnnotations(), annotatedElement);
/*      */     }
/*  273 */     catch (Throwable ex) {
/*  274 */       handleIntrospectionFailure(annotatedElement, ex);
/*  275 */       return null;
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Deprecated
/*      */   @Nullable
/*      */   public static Annotation[] getAnnotations(Method method) {
/*      */     try {
/*  296 */       return synthesizeAnnotationArray(BridgeMethodResolver.findBridgedMethod(method).getAnnotations(), method);
/*      */     }
/*  298 */     catch (Throwable ex) {
/*  299 */       handleIntrospectionFailure(method, ex);
/*  300 */       return null;
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Deprecated
/*      */   public static <A extends Annotation> Set<A> getRepeatableAnnotations(AnnotatedElement annotatedElement, Class<A> annotationType) {
/*  336 */     return getRepeatableAnnotations(annotatedElement, annotationType, null);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Deprecated
/*      */   public static <A extends Annotation> Set<A> getRepeatableAnnotations(AnnotatedElement annotatedElement, Class<A> annotationType, @Nullable Class<? extends Annotation> containerAnnotationType) {
/*  376 */     RepeatableContainers repeatableContainers = (containerAnnotationType != null) ? RepeatableContainers.of(annotationType, containerAnnotationType) : RepeatableContainers.standardRepeatables();
/*      */     
/*  378 */     return (Set<A>)MergedAnnotations.from(annotatedElement, MergedAnnotations.SearchStrategy.SUPERCLASS, repeatableContainers)
/*  379 */       .<A>stream(annotationType)
/*  380 */       .filter(MergedAnnotationPredicates.firstRunOf(MergedAnnotation::getAggregateIndex))
/*  381 */       .map(MergedAnnotation::withNonMergedAttributes)
/*  382 */       .collect(MergedAnnotationCollectors.toAnnotationSet());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Deprecated
/*      */   public static <A extends Annotation> Set<A> getDeclaredRepeatableAnnotations(AnnotatedElement annotatedElement, Class<A> annotationType) {
/*  418 */     return getDeclaredRepeatableAnnotations(annotatedElement, annotationType, null);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Deprecated
/*      */   public static <A extends Annotation> Set<A> getDeclaredRepeatableAnnotations(AnnotatedElement annotatedElement, Class<A> annotationType, @Nullable Class<? extends Annotation> containerAnnotationType) {
/*  458 */     RepeatableContainers repeatableContainers = (containerAnnotationType != null) ? RepeatableContainers.of(annotationType, containerAnnotationType) : RepeatableContainers.standardRepeatables();
/*      */     
/*  460 */     return (Set<A>)MergedAnnotations.from(annotatedElement, MergedAnnotations.SearchStrategy.DIRECT, repeatableContainers)
/*  461 */       .<A>stream(annotationType)
/*  462 */       .map(MergedAnnotation::withNonMergedAttributes)
/*  463 */       .collect(MergedAnnotationCollectors.toAnnotationSet());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Nullable
/*      */   public static <A extends Annotation> A findAnnotation(AnnotatedElement annotatedElement, @Nullable Class<A> annotationType) {
/*  486 */     if (annotationType == null) {
/*  487 */       return null;
/*      */     }
/*      */ 
/*      */     
/*  491 */     if (AnnotationFilter.PLAIN.matches(annotationType) || 
/*  492 */       AnnotationsScanner.hasPlainJavaAnnotationsOnly(annotatedElement)) {
/*  493 */       return annotatedElement.getDeclaredAnnotation(annotationType);
/*      */     }
/*      */ 
/*      */     
/*  497 */     return (A)MergedAnnotations.from(annotatedElement, MergedAnnotations.SearchStrategy.INHERITED_ANNOTATIONS, RepeatableContainers.none())
/*  498 */       .<Annotation>get(annotationType).withNonMergedAttributes()
/*  499 */       .synthesize(MergedAnnotation::isPresent).orElse(null);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Nullable
/*      */   public static <A extends Annotation> A findAnnotation(Method method, @Nullable Class<A> annotationType) {
/*  519 */     if (annotationType == null) {
/*  520 */       return null;
/*      */     }
/*      */ 
/*      */     
/*  524 */     if (AnnotationFilter.PLAIN.matches(annotationType) || 
/*  525 */       AnnotationsScanner.hasPlainJavaAnnotationsOnly(method)) {
/*  526 */       return method.getDeclaredAnnotation(annotationType);
/*      */     }
/*      */ 
/*      */     
/*  530 */     return (A)MergedAnnotations.from(method, MergedAnnotations.SearchStrategy.TYPE_HIERARCHY, RepeatableContainers.none())
/*  531 */       .<Annotation>get(annotationType).withNonMergedAttributes()
/*  532 */       .synthesize(MergedAnnotation::isPresent).orElse(null);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Nullable
/*      */   public static <A extends Annotation> A findAnnotation(Class<?> clazz, @Nullable Class<A> annotationType) {
/*  559 */     if (annotationType == null) {
/*  560 */       return null;
/*      */     }
/*      */ 
/*      */     
/*  564 */     if (AnnotationFilter.PLAIN.matches(annotationType) || 
/*  565 */       AnnotationsScanner.hasPlainJavaAnnotationsOnly(clazz)) {
/*  566 */       A annotation = clazz.getDeclaredAnnotation(annotationType);
/*  567 */       if (annotation != null) {
/*  568 */         return annotation;
/*      */       }
/*      */ 
/*      */       
/*  572 */       Class<?> superclass = clazz.getSuperclass();
/*  573 */       if (superclass == null || superclass == Object.class) {
/*  574 */         return null;
/*      */       }
/*  576 */       return findAnnotation(superclass, annotationType);
/*      */     } 
/*      */ 
/*      */     
/*  580 */     return (A)MergedAnnotations.from(clazz, MergedAnnotations.SearchStrategy.TYPE_HIERARCHY, RepeatableContainers.none())
/*  581 */       .<Annotation>get(annotationType).withNonMergedAttributes()
/*  582 */       .synthesize(MergedAnnotation::isPresent).orElse(null);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Deprecated
/*      */   @Nullable
/*      */   public static Class<?> findAnnotationDeclaringClass(Class<? extends Annotation> annotationType, @Nullable Class<?> clazz) {
/*  611 */     if (clazz == null) {
/*  612 */       return null;
/*      */     }
/*      */     
/*  615 */     return (Class)MergedAnnotations.from(clazz, MergedAnnotations.SearchStrategy.SUPERCLASS)
/*  616 */       .<Annotation>get(annotationType, MergedAnnotation::isDirectlyPresent)
/*  617 */       .getSource();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Deprecated
/*      */   @Nullable
/*      */   public static Class<?> findAnnotationDeclaringClassForTypes(List<Class<? extends Annotation>> annotationTypes, @Nullable Class<?> clazz) {
/*  648 */     if (clazz == null) {
/*  649 */       return null;
/*      */     }
/*      */     
/*  652 */     return MergedAnnotations.from(clazz, MergedAnnotations.SearchStrategy.SUPERCLASS)
/*  653 */       .stream()
/*  654 */       .filter(MergedAnnotationPredicates.<Annotation>typeIn(annotationTypes).and(MergedAnnotation::isDirectlyPresent))
/*  655 */       .map(MergedAnnotation::getSource)
/*  656 */       .findFirst().orElse(null);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static boolean isAnnotationDeclaredLocally(Class<? extends Annotation> annotationType, Class<?> clazz) {
/*  675 */     return MergedAnnotations.from(clazz).<Annotation>get(annotationType).isDirectlyPresent();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Deprecated
/*      */   public static boolean isAnnotationInherited(Class<? extends Annotation> annotationType, Class<?> clazz) {
/*  700 */     return 
/*      */ 
/*      */ 
/*      */       
/*  704 */       (((MergedAnnotation)MergedAnnotations.from(clazz, MergedAnnotations.SearchStrategy.INHERITED_ANNOTATIONS).<Annotation>stream(annotationType).filter(MergedAnnotation::isDirectlyPresent).findFirst().orElseGet(MergedAnnotation::missing)).getAggregateIndex() > 0);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Deprecated
/*      */   public static boolean isAnnotationMetaPresent(Class<? extends Annotation> annotationType, @Nullable Class<? extends Annotation> metaAnnotationType) {
/*  720 */     if (metaAnnotationType == null) {
/*  721 */       return false;
/*      */     }
/*      */     
/*  724 */     if (AnnotationFilter.PLAIN.matches(metaAnnotationType) || 
/*  725 */       AnnotationsScanner.hasPlainJavaAnnotationsOnly(annotationType)) {
/*  726 */       return annotationType.isAnnotationPresent(metaAnnotationType);
/*      */     }
/*      */     
/*  729 */     return MergedAnnotations.from(annotationType, MergedAnnotations.SearchStrategy.INHERITED_ANNOTATIONS, 
/*  730 */         RepeatableContainers.none()).isPresent(metaAnnotationType);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static boolean isInJavaLangAnnotationPackage(@Nullable Annotation annotation) {
/*  740 */     return (annotation != null && JAVA_LANG_ANNOTATION_FILTER.matches(annotation));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static boolean isInJavaLangAnnotationPackage(@Nullable String annotationType) {
/*  751 */     return (annotationType != null && JAVA_LANG_ANNOTATION_FILTER.matches(annotationType));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void validateAnnotation(Annotation annotation) {
/*  767 */     AttributeMethods.forAnnotationType(annotation.annotationType()).validate(annotation);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static Map<String, Object> getAnnotationAttributes(Annotation annotation) {
/*  786 */     return getAnnotationAttributes((AnnotatedElement)null, annotation);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static Map<String, Object> getAnnotationAttributes(Annotation annotation, boolean classValuesAsString) {
/*  806 */     return getAnnotationAttributes(annotation, classValuesAsString, false);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static AnnotationAttributes getAnnotationAttributes(Annotation annotation, boolean classValuesAsString, boolean nestedAnnotationsAsMap) {
/*  828 */     return getAnnotationAttributes(null, annotation, classValuesAsString, nestedAnnotationsAsMap);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static AnnotationAttributes getAnnotationAttributes(@Nullable AnnotatedElement annotatedElement, Annotation annotation) {
/*  847 */     return getAnnotationAttributes(annotatedElement, annotation, false, false);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static AnnotationAttributes getAnnotationAttributes(@Nullable AnnotatedElement annotatedElement, Annotation annotation, boolean classValuesAsString, boolean nestedAnnotationsAsMap) {
/*  872 */     MergedAnnotation.Adapt[] adaptations = MergedAnnotation.Adapt.values(classValuesAsString, nestedAnnotationsAsMap);
/*  873 */     return (AnnotationAttributes)MergedAnnotation.<Annotation>from(annotatedElement, annotation)
/*  874 */       .withNonMergedAttributes()
/*  875 */       .asMap(mergedAnnotation -> new AnnotationAttributes(mergedAnnotation.getType(), true), adaptations);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void registerDefaultValues(AnnotationAttributes attributes) {
/*  886 */     Class<? extends Annotation> annotationType = attributes.annotationType();
/*  887 */     if (annotationType != null && Modifier.isPublic(annotationType.getModifiers()) && 
/*  888 */       !AnnotationFilter.PLAIN.matches(annotationType)) {
/*  889 */       Map<String, DefaultValueHolder> defaultValues = getDefaultValues(annotationType);
/*  890 */       defaultValues.forEach(attributes::putIfAbsent);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private static Map<String, DefaultValueHolder> getDefaultValues(Class<? extends Annotation> annotationType) {
/*  897 */     return defaultValuesCache.computeIfAbsent(annotationType, AnnotationUtils::computeDefaultValues);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static Map<String, DefaultValueHolder> computeDefaultValues(Class<? extends Annotation> annotationType) {
/*  904 */     AttributeMethods methods = AttributeMethods.forAnnotationType(annotationType);
/*  905 */     if (!methods.hasDefaultValueMethod()) {
/*  906 */       return Collections.emptyMap();
/*      */     }
/*  908 */     Map<String, DefaultValueHolder> result = CollectionUtils.newLinkedHashMap(methods.size());
/*  909 */     if (!methods.hasNestedAnnotation()) {
/*      */       
/*  911 */       for (int i = 0; i < methods.size(); i++) {
/*  912 */         Method method = methods.get(i);
/*  913 */         Object defaultValue = method.getDefaultValue();
/*  914 */         if (defaultValue != null) {
/*  915 */           result.put(method.getName(), new DefaultValueHolder(defaultValue));
/*      */         }
/*      */       }
/*      */     
/*      */     }
/*      */     else {
/*      */       
/*  922 */       AnnotationAttributes attributes = (AnnotationAttributes)MergedAnnotation.<Annotation>of(annotationType).asMap(annotation -> new AnnotationAttributes(annotation.getType(), true), new MergedAnnotation.Adapt[] { MergedAnnotation.Adapt.ANNOTATION_TO_MAP });
/*      */       
/*  924 */       for (Map.Entry<String, Object> element : attributes.entrySet()) {
/*  925 */         result.put(element.getKey(), new DefaultValueHolder(element.getValue()));
/*      */       }
/*      */     } 
/*  928 */     return result;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void postProcessAnnotationAttributes(@Nullable Object annotatedElement, @Nullable AnnotationAttributes attributes, boolean classValuesAsString) {
/*  950 */     if (attributes == null) {
/*      */       return;
/*      */     }
/*  953 */     if (!attributes.validated) {
/*  954 */       Class<? extends Annotation> annotationType = attributes.annotationType();
/*  955 */       if (annotationType == null) {
/*      */         return;
/*      */       }
/*  958 */       AnnotationTypeMapping mapping = AnnotationTypeMappings.forAnnotationType(annotationType).get(0);
/*  959 */       for (int i = 0; i < mapping.getMirrorSets().size(); i++) {
/*  960 */         AnnotationTypeMapping.MirrorSets.MirrorSet mirrorSet = mapping.getMirrorSets().get(i);
/*  961 */         int resolved = mirrorSet.resolve(attributes.displayName, attributes, AnnotationUtils::getAttributeValueForMirrorResolution);
/*      */         
/*  963 */         if (resolved != -1) {
/*  964 */           Method attribute = mapping.getAttributes().get(resolved);
/*  965 */           Object value = attributes.get(attribute.getName());
/*  966 */           for (int j = 0; j < mirrorSet.size(); j++) {
/*  967 */             Method mirror = mirrorSet.get(j);
/*  968 */             if (mirror != attribute) {
/*  969 */               attributes.put(mirror.getName(), 
/*  970 */                   adaptValue(annotatedElement, value, classValuesAsString));
/*      */             }
/*      */           } 
/*      */         } 
/*      */       } 
/*      */     } 
/*  976 */     for (Map.Entry<String, Object> attributeEntry : attributes.entrySet()) {
/*  977 */       String attributeName = attributeEntry.getKey();
/*  978 */       Object value = attributeEntry.getValue();
/*  979 */       if (value instanceof DefaultValueHolder) {
/*  980 */         value = ((DefaultValueHolder)value).defaultValue;
/*  981 */         attributes.put(attributeName, 
/*  982 */             adaptValue(annotatedElement, value, classValuesAsString));
/*      */       } 
/*      */     } 
/*      */   }
/*      */   
/*      */   private static Object getAttributeValueForMirrorResolution(Method attribute, Object attributes) {
/*  988 */     Object result = ((AnnotationAttributes)attributes).get(attribute.getName());
/*  989 */     return (result instanceof DefaultValueHolder) ? ((DefaultValueHolder)result).defaultValue : result;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   @Nullable
/*      */   private static Object adaptValue(@Nullable Object annotatedElement, @Nullable Object value, boolean classValuesAsString) {
/*  996 */     if (classValuesAsString) {
/*  997 */       if (value instanceof Class) {
/*  998 */         return ((Class)value).getName();
/*      */       }
/* 1000 */       if (value instanceof Class[]) {
/* 1001 */         Class<?>[] classes = (Class[])value;
/* 1002 */         String[] names = new String[classes.length];
/* 1003 */         for (int i = 0; i < classes.length; i++) {
/* 1004 */           names[i] = classes[i].getName();
/*      */         }
/* 1006 */         return names;
/*      */       } 
/*      */     } 
/* 1009 */     if (value instanceof Annotation) {
/* 1010 */       Annotation annotation = (Annotation)value;
/* 1011 */       return MergedAnnotation.<Annotation>from(annotatedElement, annotation).synthesize();
/*      */     } 
/* 1013 */     if (value instanceof Annotation[]) {
/* 1014 */       Annotation[] annotations = (Annotation[])value;
/* 1015 */       Annotation[] synthesized = (Annotation[])Array.newInstance(annotations
/* 1016 */           .getClass().getComponentType(), annotations.length);
/* 1017 */       for (int i = 0; i < annotations.length; i++) {
/* 1018 */         synthesized[i] = MergedAnnotation.<Annotation>from(annotatedElement, annotations[i]).synthesize();
/*      */       }
/* 1020 */       return synthesized;
/*      */     } 
/* 1022 */     return value;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Nullable
/*      */   public static Object getValue(Annotation annotation) {
/* 1036 */     return getValue(annotation, "value");
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Nullable
/*      */   public static Object getValue(@Nullable Annotation annotation, @Nullable String attributeName) {
/* 1050 */     if (annotation == null || !StringUtils.hasText(attributeName)) {
/* 1051 */       return null;
/*      */     }
/*      */     try {
/* 1054 */       Method method = annotation.annotationType().getDeclaredMethod(attributeName, new Class[0]);
/* 1055 */       ReflectionUtils.makeAccessible(method);
/* 1056 */       return method.invoke(annotation, new Object[0]);
/*      */     }
/* 1058 */     catch (NoSuchMethodException ex) {
/* 1059 */       return null;
/*      */     }
/* 1061 */     catch (InvocationTargetException ex) {
/* 1062 */       rethrowAnnotationConfigurationException(ex.getTargetException());
/* 1063 */       throw new IllegalStateException("Could not obtain value for annotation attribute '" + attributeName + "' in " + annotation, ex);
/*      */     
/*      */     }
/* 1066 */     catch (Throwable ex) {
/* 1067 */       handleIntrospectionFailure(annotation.getClass(), ex);
/* 1068 */       return null;
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   static void rethrowAnnotationConfigurationException(Throwable ex) {
/* 1080 */     if (ex instanceof AnnotationConfigurationException) {
/* 1081 */       throw (AnnotationConfigurationException)ex;
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   static void handleIntrospectionFailure(@Nullable AnnotatedElement element, Throwable ex) {
/* 1101 */     rethrowAnnotationConfigurationException(ex);
/* 1102 */     IntrospectionFailureLogger logger = IntrospectionFailureLogger.INFO;
/* 1103 */     boolean meta = false;
/* 1104 */     if (element instanceof Class && Annotation.class.isAssignableFrom((Class)element)) {
/*      */       
/* 1106 */       logger = IntrospectionFailureLogger.DEBUG;
/* 1107 */       meta = true;
/*      */     } 
/* 1109 */     if (logger.isEnabled()) {
/* 1110 */       String message = meta ? "Failed to meta-introspect annotation " : "Failed to introspect annotations on ";
/*      */ 
/*      */       
/* 1113 */       logger.log(message + element + ": " + ex);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Nullable
/*      */   public static Object getDefaultValue(Annotation annotation) {
/* 1126 */     return getDefaultValue(annotation, "value");
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Nullable
/*      */   public static Object getDefaultValue(@Nullable Annotation annotation, @Nullable String attributeName) {
/* 1138 */     return (annotation != null) ? getDefaultValue(annotation.annotationType(), attributeName) : null;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Nullable
/*      */   public static Object getDefaultValue(Class<? extends Annotation> annotationType) {
/* 1150 */     return getDefaultValue(annotationType, "value");
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Nullable
/*      */   public static Object getDefaultValue(@Nullable Class<? extends Annotation> annotationType, @Nullable String attributeName) {
/* 1165 */     if (annotationType == null || !StringUtils.hasText(attributeName)) {
/* 1166 */       return null;
/*      */     }
/* 1168 */     return MergedAnnotation.<Annotation>of(annotationType).getDefaultValue(attributeName).orElse(null);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static <A extends Annotation> A synthesizeAnnotation(A annotation, @Nullable AnnotatedElement annotatedElement) {
/* 1191 */     if (annotation instanceof SynthesizedAnnotation || AnnotationFilter.PLAIN.matches((Annotation)annotation)) {
/* 1192 */       return annotation;
/*      */     }
/* 1194 */     return MergedAnnotation.<A>from(annotatedElement, annotation).synthesize();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static <A extends Annotation> A synthesizeAnnotation(Class<A> annotationType) {
/* 1213 */     return synthesizeAnnotation(Collections.emptyMap(), annotationType, null);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static <A extends Annotation> A synthesizeAnnotation(Map<String, Object> attributes, Class<A> annotationType, @Nullable AnnotatedElement annotatedElement) {
/*      */     try {
/* 1249 */       return MergedAnnotation.<A>of(annotatedElement, annotationType, attributes).synthesize();
/*      */     }
/* 1251 */     catch (NoSuchElementException|IllegalStateException ex) {
/* 1252 */       throw new IllegalArgumentException(ex);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   static Annotation[] synthesizeAnnotationArray(Annotation[] annotations, AnnotatedElement annotatedElement) {
/* 1274 */     if (AnnotationsScanner.hasPlainJavaAnnotationsOnly(annotatedElement)) {
/* 1275 */       return annotations;
/*      */     }
/* 1277 */     Annotation[] synthesized = (Annotation[])Array.newInstance(annotations
/* 1278 */         .getClass().getComponentType(), annotations.length);
/* 1279 */     for (int i = 0; i < annotations.length; i++) {
/* 1280 */       synthesized[i] = synthesizeAnnotation(annotations[i], annotatedElement);
/*      */     }
/* 1282 */     return synthesized;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void clearCache() {
/* 1290 */     AnnotationTypeMappings.clearCache();
/* 1291 */     AnnotationsScanner.clearCache();
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private static class DefaultValueHolder
/*      */   {
/*      */     final Object defaultValue;
/*      */ 
/*      */ 
/*      */     
/*      */     public DefaultValueHolder(Object defaultValue) {
/* 1303 */       this.defaultValue = defaultValue;
/*      */     }
/*      */ 
/*      */     
/*      */     public String toString() {
/* 1308 */       return "*" + this.defaultValue;
/*      */     }
/*      */   }
/*      */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\springframework\core\annotation\AnnotationUtils.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */