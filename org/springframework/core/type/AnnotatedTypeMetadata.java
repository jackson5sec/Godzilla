/*     */ package org.springframework.core.type;
/*     */ 
/*     */ import java.lang.annotation.Annotation;
/*     */ import java.util.Map;
/*     */ import org.springframework.core.annotation.MergedAnnotation;
/*     */ import org.springframework.core.annotation.MergedAnnotationCollectors;
/*     */ import org.springframework.core.annotation.MergedAnnotationPredicates;
/*     */ import org.springframework.core.annotation.MergedAnnotationSelectors;
/*     */ import org.springframework.core.annotation.MergedAnnotations;
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
/*     */ public interface AnnotatedTypeMetadata
/*     */ {
/*     */   MergedAnnotations getAnnotations();
/*     */   
/*     */   default boolean isAnnotated(String annotationName) {
/*  66 */     return getAnnotations().isPresent(annotationName);
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
/*     */   default Map<String, Object> getAnnotationAttributes(String annotationName) {
/*  81 */     return getAnnotationAttributes(annotationName, false);
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
/*     */   default Map<String, Object> getAnnotationAttributes(String annotationName, boolean classValuesAsString) {
/* 101 */     MergedAnnotation<Annotation> annotation = getAnnotations().get(annotationName, null, 
/* 102 */         MergedAnnotationSelectors.firstDirectlyDeclared());
/* 103 */     if (!annotation.isPresent()) {
/* 104 */       return null;
/*     */     }
/* 106 */     return (Map<String, Object>)annotation.asAnnotationAttributes(MergedAnnotation.Adapt.values(classValuesAsString, true));
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
/*     */   @Nullable
/*     */   default MultiValueMap<String, Object> getAllAnnotationAttributes(String annotationName) {
/* 122 */     return getAllAnnotationAttributes(annotationName, false);
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
/*     */   @Nullable
/*     */   default MultiValueMap<String, Object> getAllAnnotationAttributes(String annotationName, boolean classValuesAsString) {
/* 141 */     MergedAnnotation.Adapt[] adaptations = MergedAnnotation.Adapt.values(classValuesAsString, true);
/* 142 */     return (MultiValueMap<String, Object>)getAnnotations().stream(annotationName)
/* 143 */       .filter(MergedAnnotationPredicates.unique(MergedAnnotation::getMetaTypes))
/* 144 */       .map(MergedAnnotation::withNonMergedAttributes)
/* 145 */       .collect(MergedAnnotationCollectors.toMultiValueMap(map -> map.isEmpty() ? null : map, adaptations));
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\springframework\core\type\AnnotatedTypeMetadata.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */