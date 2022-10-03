/*     */ package org.springframework.core.annotation;
/*     */ 
/*     */ import java.lang.annotation.Annotation;
/*     */ import java.lang.reflect.AnnotatedElement;
/*     */ import java.util.Collections;
/*     */ import java.util.Comparator;
/*     */ import java.util.Set;
/*     */ import java.util.function.Predicate;
/*     */ import java.util.function.Supplier;
/*     */ import java.util.stream.Collectors;
/*     */ import org.springframework.lang.Nullable;
/*     */ import org.springframework.util.MultiValueMap;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class AnnotatedElementUtils
/*     */ {
/*     */   public static AnnotatedElement forAnnotations(Annotation... annotations) {
/* 102 */     return new AnnotatedElementForAnnotations(annotations);
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
/*     */   public static Set<String> getMetaAnnotationTypes(AnnotatedElement element, Class<? extends Annotation> annotationType) {
/* 122 */     return getMetaAnnotationTypes(element, element.getAnnotation((Class)annotationType));
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
/*     */   public static Set<String> getMetaAnnotationTypes(AnnotatedElement element, String annotationName) {
/* 140 */     for (Annotation annotation : element.getAnnotations()) {
/* 141 */       if (annotation.annotationType().getName().equals(annotationName)) {
/* 142 */         return getMetaAnnotationTypes(element, annotation);
/*     */       }
/*     */     } 
/* 145 */     return Collections.emptySet();
/*     */   }
/*     */   
/*     */   private static Set<String> getMetaAnnotationTypes(AnnotatedElement element, @Nullable Annotation annotation) {
/* 149 */     if (annotation == null) {
/* 150 */       return Collections.emptySet();
/*     */     }
/* 152 */     return (Set<String>)getAnnotations(annotation.annotationType()).stream()
/* 153 */       .map(mergedAnnotation -> mergedAnnotation.getType().getName())
/* 154 */       .collect(Collectors.toCollection(java.util.LinkedHashSet::new));
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
/*     */   public static boolean hasMetaAnnotationTypes(AnnotatedElement element, Class<? extends Annotation> annotationType) {
/* 170 */     return getAnnotations(element).<Annotation>stream(annotationType).anyMatch(MergedAnnotation::isMetaPresent);
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
/*     */   public static boolean hasMetaAnnotationTypes(AnnotatedElement element, String annotationName) {
/* 186 */     return getAnnotations(element).<Annotation>stream(annotationName).anyMatch(MergedAnnotation::isMetaPresent);
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
/*     */   public static boolean isAnnotated(AnnotatedElement element, Class<? extends Annotation> annotationType) {
/* 205 */     if (AnnotationFilter.PLAIN.matches(annotationType) || 
/* 206 */       AnnotationsScanner.hasPlainJavaAnnotationsOnly(element)) {
/* 207 */       return element.isAnnotationPresent(annotationType);
/*     */     }
/*     */     
/* 210 */     return getAnnotations(element).isPresent(annotationType);
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
/*     */   public static boolean isAnnotated(AnnotatedElement element, String annotationName) {
/* 226 */     return getAnnotations(element).isPresent(annotationName);
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
/*     */   @Nullable
/*     */   public static AnnotationAttributes getMergedAnnotationAttributes(AnnotatedElement element, Class<? extends Annotation> annotationType) {
/* 251 */     MergedAnnotation<?> mergedAnnotation = getAnnotations(element).get(annotationType, (Predicate<? super MergedAnnotation<?>>)null, MergedAnnotationSelectors.firstDirectlyDeclared());
/* 252 */     return getAnnotationAttributes(mergedAnnotation, false, false);
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
/*     */   @Nullable
/*     */   public static AnnotationAttributes getMergedAnnotationAttributes(AnnotatedElement element, String annotationName) {
/* 277 */     return getMergedAnnotationAttributes(element, annotationName, false, false);
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
/*     */   @Nullable
/*     */   public static AnnotationAttributes getMergedAnnotationAttributes(AnnotatedElement element, String annotationName, boolean classValuesAsString, boolean nestedAnnotationsAsMap) {
/* 311 */     MergedAnnotation<?> mergedAnnotation = getAnnotations(element).get(annotationName, (Predicate<? super MergedAnnotation<?>>)null, MergedAnnotationSelectors.firstDirectlyDeclared());
/* 312 */     return getAnnotationAttributes(mergedAnnotation, classValuesAsString, nestedAnnotationsAsMap);
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
/*     */   @Nullable
/*     */   public static <A extends Annotation> A getMergedAnnotation(AnnotatedElement element, Class<A> annotationType) {
/* 332 */     if (AnnotationFilter.PLAIN.matches(annotationType) || 
/* 333 */       AnnotationsScanner.hasPlainJavaAnnotationsOnly(element)) {
/* 334 */       return element.getDeclaredAnnotation(annotationType);
/*     */     }
/*     */     
/* 337 */     return (A)getAnnotations(element)
/* 338 */       .<Annotation>get(annotationType, (Predicate<? super MergedAnnotation<Annotation>>)null, MergedAnnotationSelectors.firstDirectlyDeclared())
/* 339 */       .synthesize(MergedAnnotation::isPresent).orElse(null);
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
/*     */   public static <A extends Annotation> Set<A> getAllMergedAnnotations(AnnotatedElement element, Class<A> annotationType) {
/* 365 */     return (Set<A>)getAnnotations(element).<A>stream(annotationType)
/* 366 */       .collect(MergedAnnotationCollectors.toAnnotationSet());
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
/*     */   public static Set<Annotation> getAllMergedAnnotations(AnnotatedElement element, Set<Class<? extends Annotation>> annotationTypes) {
/* 390 */     return (Set<Annotation>)getAnnotations(element).stream()
/* 391 */       .filter((Predicate)MergedAnnotationPredicates.typeIn(annotationTypes))
/* 392 */       .collect(MergedAnnotationCollectors.toAnnotationSet());
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
/*     */   public static <A extends Annotation> Set<A> getMergedRepeatableAnnotations(AnnotatedElement element, Class<A> annotationType) {
/* 422 */     return getMergedRepeatableAnnotations(element, annotationType, null);
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
/*     */   public static <A extends Annotation> Set<A> getMergedRepeatableAnnotations(AnnotatedElement element, Class<A> annotationType, @Nullable Class<? extends Annotation> containerType) {
/* 455 */     return (Set<A>)getRepeatableAnnotations(element, containerType, annotationType)
/* 456 */       .<A>stream(annotationType)
/* 457 */       .collect(MergedAnnotationCollectors.toAnnotationSet());
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
/*     */   @Nullable
/*     */   public static MultiValueMap<String, Object> getAllAnnotationAttributes(AnnotatedElement element, String annotationName) {
/* 478 */     return getAllAnnotationAttributes(element, annotationName, false, false);
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
/*     */   @Nullable
/*     */   public static MultiValueMap<String, Object> getAllAnnotationAttributes(AnnotatedElement element, String annotationName, boolean classValuesAsString, boolean nestedAnnotationsAsMap) {
/* 503 */     MergedAnnotation.Adapt[] adaptations = MergedAnnotation.Adapt.values(classValuesAsString, nestedAnnotationsAsMap);
/* 504 */     return (MultiValueMap<String, Object>)getAnnotations(element).<Annotation>stream(annotationName)
/* 505 */       .filter(MergedAnnotationPredicates.unique(MergedAnnotation::getMetaTypes))
/* 506 */       .map(MergedAnnotation::withNonMergedAttributes)
/* 507 */       .collect(MergedAnnotationCollectors.toMultiValueMap(AnnotatedElementUtils::nullIfEmpty, adaptations));
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
/*     */   public static boolean hasAnnotation(AnnotatedElement element, Class<? extends Annotation> annotationType) {
/* 526 */     if (AnnotationFilter.PLAIN.matches(annotationType) || 
/* 527 */       AnnotationsScanner.hasPlainJavaAnnotationsOnly(element)) {
/* 528 */       return element.isAnnotationPresent(annotationType);
/*     */     }
/*     */     
/* 531 */     return findAnnotations(element).isPresent(annotationType);
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
/*     */   @Nullable
/*     */   public static AnnotationAttributes findMergedAnnotationAttributes(AnnotatedElement element, Class<? extends Annotation> annotationType, boolean classValuesAsString, boolean nestedAnnotationsAsMap) {
/* 566 */     MergedAnnotation<?> mergedAnnotation = findAnnotations(element).get(annotationType, (Predicate<? super MergedAnnotation<?>>)null, MergedAnnotationSelectors.firstDirectlyDeclared());
/* 567 */     return getAnnotationAttributes(mergedAnnotation, classValuesAsString, nestedAnnotationsAsMap);
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
/*     */   @Nullable
/*     */   public static AnnotationAttributes findMergedAnnotationAttributes(AnnotatedElement element, String annotationName, boolean classValuesAsString, boolean nestedAnnotationsAsMap) {
/* 602 */     MergedAnnotation<?> mergedAnnotation = findAnnotations(element).get(annotationName, (Predicate<? super MergedAnnotation<?>>)null, MergedAnnotationSelectors.firstDirectlyDeclared());
/* 603 */     return getAnnotationAttributes(mergedAnnotation, classValuesAsString, nestedAnnotationsAsMap);
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
/*     */   @Nullable
/*     */   public static <A extends Annotation> A findMergedAnnotation(AnnotatedElement element, Class<A> annotationType) {
/* 627 */     if (AnnotationFilter.PLAIN.matches(annotationType) || 
/* 628 */       AnnotationsScanner.hasPlainJavaAnnotationsOnly(element)) {
/* 629 */       return element.getDeclaredAnnotation(annotationType);
/*     */     }
/*     */     
/* 632 */     return (A)findAnnotations(element)
/* 633 */       .<Annotation>get(annotationType, (Predicate<? super MergedAnnotation<Annotation>>)null, MergedAnnotationSelectors.firstDirectlyDeclared())
/* 634 */       .synthesize(MergedAnnotation::isPresent).orElse(null);
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
/*     */   public static <A extends Annotation> Set<A> findAllMergedAnnotations(AnnotatedElement element, Class<A> annotationType) {
/* 657 */     return (Set<A>)findAnnotations(element).<A>stream(annotationType)
/* 658 */       .sorted(highAggregateIndexesFirst())
/* 659 */       .collect(MergedAnnotationCollectors.toAnnotationSet());
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
/*     */   public static Set<Annotation> findAllMergedAnnotations(AnnotatedElement element, Set<Class<? extends Annotation>> annotationTypes) {
/* 681 */     return (Set<Annotation>)findAnnotations(element).stream()
/* 682 */       .filter((Predicate)MergedAnnotationPredicates.typeIn(annotationTypes))
/* 683 */       .sorted(highAggregateIndexesFirst())
/* 684 */       .collect(MergedAnnotationCollectors.toAnnotationSet());
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
/*     */   public static <A extends Annotation> Set<A> findMergedRepeatableAnnotations(AnnotatedElement element, Class<A> annotationType) {
/* 714 */     return findMergedRepeatableAnnotations(element, annotationType, null);
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
/*     */   public static <A extends Annotation> Set<A> findMergedRepeatableAnnotations(AnnotatedElement element, Class<A> annotationType, @Nullable Class<? extends Annotation> containerType) {
/* 746 */     return (Set<A>)findRepeatableAnnotations(element, containerType, annotationType)
/* 747 */       .<A>stream(annotationType)
/* 748 */       .sorted(highAggregateIndexesFirst())
/* 749 */       .collect(MergedAnnotationCollectors.toAnnotationSet());
/*     */   }
/*     */   
/*     */   private static MergedAnnotations getAnnotations(AnnotatedElement element) {
/* 753 */     return MergedAnnotations.from(element, MergedAnnotations.SearchStrategy.INHERITED_ANNOTATIONS, RepeatableContainers.none());
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static MergedAnnotations getRepeatableAnnotations(AnnotatedElement element, @Nullable Class<? extends Annotation> containerType, Class<? extends Annotation> annotationType) {
/* 759 */     RepeatableContainers repeatableContainers = RepeatableContainers.of(annotationType, containerType);
/* 760 */     return MergedAnnotations.from(element, MergedAnnotations.SearchStrategy.INHERITED_ANNOTATIONS, repeatableContainers);
/*     */   }
/*     */   
/*     */   private static MergedAnnotations findAnnotations(AnnotatedElement element) {
/* 764 */     return MergedAnnotations.from(element, MergedAnnotations.SearchStrategy.TYPE_HIERARCHY, RepeatableContainers.none());
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static MergedAnnotations findRepeatableAnnotations(AnnotatedElement element, @Nullable Class<? extends Annotation> containerType, Class<? extends Annotation> annotationType) {
/* 770 */     RepeatableContainers repeatableContainers = RepeatableContainers.of(annotationType, containerType);
/* 771 */     return MergedAnnotations.from(element, MergedAnnotations.SearchStrategy.TYPE_HIERARCHY, repeatableContainers);
/*     */   }
/*     */   
/*     */   @Nullable
/*     */   private static MultiValueMap<String, Object> nullIfEmpty(MultiValueMap<String, Object> map) {
/* 776 */     return map.isEmpty() ? null : map;
/*     */   }
/*     */   
/*     */   private static <A extends Annotation> Comparator<MergedAnnotation<A>> highAggregateIndexesFirst() {
/* 780 */     return Comparator.<MergedAnnotation<A>>comparingInt(MergedAnnotation::getAggregateIndex)
/* 781 */       .reversed();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   private static AnnotationAttributes getAnnotationAttributes(MergedAnnotation<?> annotation, boolean classValuesAsString, boolean nestedAnnotationsAsMap) {
/* 788 */     if (!annotation.isPresent()) {
/* 789 */       return null;
/*     */     }
/* 791 */     return annotation.asAnnotationAttributes(
/* 792 */         MergedAnnotation.Adapt.values(classValuesAsString, nestedAnnotationsAsMap));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static class AnnotatedElementForAnnotations
/*     */     implements AnnotatedElement
/*     */   {
/*     */     private final Annotation[] annotations;
/*     */ 
/*     */     
/*     */     AnnotatedElementForAnnotations(Annotation... annotations) {
/* 804 */       this.annotations = annotations;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     @Nullable
/*     */     public <T extends Annotation> T getAnnotation(Class<T> annotationClass) {
/* 811 */       for (Annotation annotation : this.annotations) {
/* 812 */         if (annotation.annotationType() == annotationClass) {
/* 813 */           return (T)annotation;
/*     */         }
/*     */       } 
/* 816 */       return null;
/*     */     }
/*     */ 
/*     */     
/*     */     public Annotation[] getAnnotations() {
/* 821 */       return (Annotation[])this.annotations.clone();
/*     */     }
/*     */ 
/*     */     
/*     */     public Annotation[] getDeclaredAnnotations() {
/* 826 */       return (Annotation[])this.annotations.clone();
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\springframework\core\annotation\AnnotatedElementUtils.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */