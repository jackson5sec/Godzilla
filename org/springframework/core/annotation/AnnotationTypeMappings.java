/*     */ package org.springframework.core.annotation;
/*     */ 
/*     */ import java.lang.annotation.Annotation;
/*     */ import java.util.ArrayDeque;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Deque;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import org.springframework.lang.Nullable;
/*     */ import org.springframework.util.ConcurrentReferenceHashMap;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ final class AnnotationTypeMappings
/*     */ {
/*  48 */   private static final IntrospectionFailureLogger failureLogger = IntrospectionFailureLogger.DEBUG;
/*     */   
/*  50 */   private static final Map<AnnotationFilter, Cache> standardRepeatablesCache = (Map<AnnotationFilter, Cache>)new ConcurrentReferenceHashMap();
/*     */   
/*  52 */   private static final Map<AnnotationFilter, Cache> noRepeatablesCache = (Map<AnnotationFilter, Cache>)new ConcurrentReferenceHashMap();
/*     */ 
/*     */   
/*     */   private final RepeatableContainers repeatableContainers;
/*     */ 
/*     */   
/*     */   private final AnnotationFilter filter;
/*     */ 
/*     */   
/*     */   private final List<AnnotationTypeMapping> mappings;
/*     */ 
/*     */   
/*     */   private AnnotationTypeMappings(RepeatableContainers repeatableContainers, AnnotationFilter filter, Class<? extends Annotation> annotationType) {
/*  65 */     this.repeatableContainers = repeatableContainers;
/*  66 */     this.filter = filter;
/*  67 */     this.mappings = new ArrayList<>();
/*  68 */     addAllMappings(annotationType);
/*  69 */     this.mappings.forEach(AnnotationTypeMapping::afterAllMappingsSet);
/*     */   }
/*     */ 
/*     */   
/*     */   private void addAllMappings(Class<? extends Annotation> annotationType) {
/*  74 */     Deque<AnnotationTypeMapping> queue = new ArrayDeque<>();
/*  75 */     addIfPossible(queue, null, annotationType, null);
/*  76 */     while (!queue.isEmpty()) {
/*  77 */       AnnotationTypeMapping mapping = queue.removeFirst();
/*  78 */       this.mappings.add(mapping);
/*  79 */       addMetaAnnotationsToQueue(queue, mapping);
/*     */     } 
/*     */   }
/*     */   
/*     */   private void addMetaAnnotationsToQueue(Deque<AnnotationTypeMapping> queue, AnnotationTypeMapping source) {
/*  84 */     Annotation[] metaAnnotations = AnnotationsScanner.getDeclaredAnnotations(source.getAnnotationType(), false);
/*  85 */     for (Annotation metaAnnotation : metaAnnotations) {
/*  86 */       if (isMappable(source, metaAnnotation)) {
/*     */ 
/*     */         
/*  89 */         Annotation[] repeatedAnnotations = this.repeatableContainers.findRepeatedAnnotations(metaAnnotation);
/*  90 */         if (repeatedAnnotations != null) {
/*  91 */           for (Annotation repeatedAnnotation : repeatedAnnotations) {
/*  92 */             if (isMappable(source, repeatedAnnotation))
/*     */             {
/*     */               
/*  95 */               addIfPossible(queue, source, repeatedAnnotation);
/*     */             }
/*     */           } 
/*     */         } else {
/*  99 */           addIfPossible(queue, source, metaAnnotation);
/*     */         } 
/*     */       } 
/*     */     } 
/*     */   }
/*     */   private void addIfPossible(Deque<AnnotationTypeMapping> queue, AnnotationTypeMapping source, Annotation ann) {
/* 105 */     addIfPossible(queue, source, ann.annotationType(), ann);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private void addIfPossible(Deque<AnnotationTypeMapping> queue, @Nullable AnnotationTypeMapping source, Class<? extends Annotation> annotationType, @Nullable Annotation ann) {
/*     */     try {
/* 112 */       queue.addLast(new AnnotationTypeMapping(source, annotationType, ann));
/*     */     }
/* 114 */     catch (Exception ex) {
/* 115 */       AnnotationUtils.rethrowAnnotationConfigurationException(ex);
/* 116 */       if (failureLogger.isEnabled()) {
/* 117 */         failureLogger.log("Failed to introspect meta-annotation " + annotationType.getName(), (source != null) ? source
/* 118 */             .getAnnotationType() : null, ex);
/*     */       }
/*     */     } 
/*     */   }
/*     */   
/*     */   private boolean isMappable(AnnotationTypeMapping source, @Nullable Annotation metaAnnotation) {
/* 124 */     return (metaAnnotation != null && !this.filter.matches(metaAnnotation) && 
/* 125 */       !AnnotationFilter.PLAIN.matches(source.getAnnotationType()) && 
/* 126 */       !isAlreadyMapped(source, metaAnnotation));
/*     */   }
/*     */   
/*     */   private boolean isAlreadyMapped(AnnotationTypeMapping source, Annotation metaAnnotation) {
/* 130 */     Class<? extends Annotation> annotationType = metaAnnotation.annotationType();
/* 131 */     AnnotationTypeMapping mapping = source;
/* 132 */     while (mapping != null) {
/* 133 */       if (mapping.getAnnotationType() == annotationType) {
/* 134 */         return true;
/*     */       }
/* 136 */       mapping = mapping.getSource();
/*     */     } 
/* 138 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   int size() {
/* 146 */     return this.mappings.size();
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
/*     */   AnnotationTypeMapping get(int index) {
/* 159 */     return this.mappings.get(index);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static AnnotationTypeMappings forAnnotationType(Class<? extends Annotation> annotationType) {
/* 169 */     return forAnnotationType(annotationType, AnnotationFilter.PLAIN);
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
/*     */   static AnnotationTypeMappings forAnnotationType(Class<? extends Annotation> annotationType, AnnotationFilter annotationFilter) {
/* 182 */     return forAnnotationType(annotationType, RepeatableContainers.standardRepeatables(), annotationFilter);
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
/*     */   static AnnotationTypeMappings forAnnotationType(Class<? extends Annotation> annotationType, RepeatableContainers repeatableContainers, AnnotationFilter annotationFilter) {
/* 197 */     if (repeatableContainers == RepeatableContainers.standardRepeatables()) {
/* 198 */       return ((Cache)standardRepeatablesCache.computeIfAbsent(annotationFilter, key -> new Cache(repeatableContainers, key)))
/* 199 */         .get(annotationType);
/*     */     }
/* 201 */     if (repeatableContainers == RepeatableContainers.none()) {
/* 202 */       return ((Cache)noRepeatablesCache.computeIfAbsent(annotationFilter, key -> new Cache(repeatableContainers, key)))
/* 203 */         .get(annotationType);
/*     */     }
/* 205 */     return new AnnotationTypeMappings(repeatableContainers, annotationFilter, annotationType);
/*     */   }
/*     */   
/*     */   static void clearCache() {
/* 209 */     standardRepeatablesCache.clear();
/* 210 */     noRepeatablesCache.clear();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static class Cache
/*     */   {
/*     */     private final RepeatableContainers repeatableContainers;
/*     */ 
/*     */ 
/*     */     
/*     */     private final AnnotationFilter filter;
/*     */ 
/*     */     
/*     */     private final Map<Class<? extends Annotation>, AnnotationTypeMappings> mappings;
/*     */ 
/*     */ 
/*     */     
/*     */     Cache(RepeatableContainers repeatableContainers, AnnotationFilter filter) {
/* 230 */       this.repeatableContainers = repeatableContainers;
/* 231 */       this.filter = filter;
/* 232 */       this.mappings = (Map<Class<? extends Annotation>, AnnotationTypeMappings>)new ConcurrentReferenceHashMap();
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     AnnotationTypeMappings get(Class<? extends Annotation> annotationType) {
/* 241 */       return this.mappings.computeIfAbsent(annotationType, this::createMappings);
/*     */     }
/*     */     
/*     */     AnnotationTypeMappings createMappings(Class<? extends Annotation> annotationType) {
/* 245 */       return new AnnotationTypeMappings(this.repeatableContainers, this.filter, annotationType);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\springframework\core\annotation\AnnotationTypeMappings.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */