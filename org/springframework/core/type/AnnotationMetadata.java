/*     */ package org.springframework.core.type;
/*     */ 
/*     */ import java.util.Collections;
/*     */ import java.util.Set;
/*     */ import java.util.function.Supplier;
/*     */ import java.util.stream.Collectors;
/*     */ import org.springframework.core.annotation.MergedAnnotation;
/*     */ import org.springframework.core.annotation.MergedAnnotations;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public interface AnnotationMetadata
/*     */   extends ClassMetadata, AnnotatedTypeMetadata
/*     */ {
/*     */   default Set<String> getAnnotationTypes() {
/*  49 */     return (Set<String>)getAnnotations().stream()
/*  50 */       .filter(MergedAnnotation::isDirectlyPresent)
/*  51 */       .map(annotation -> annotation.getType().getName())
/*  52 */       .collect(Collectors.toCollection(java.util.LinkedHashSet::new));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   default Set<String> getMetaAnnotationTypes(String annotationName) {
/*  63 */     MergedAnnotation<?> annotation = getAnnotations().get(annotationName, MergedAnnotation::isDirectlyPresent);
/*  64 */     if (!annotation.isPresent()) {
/*  65 */       return Collections.emptySet();
/*     */     }
/*  67 */     return (Set<String>)MergedAnnotations.from(annotation.getType(), MergedAnnotations.SearchStrategy.INHERITED_ANNOTATIONS).stream()
/*  68 */       .map(mergedAnnotation -> mergedAnnotation.getType().getName())
/*  69 */       .collect(Collectors.toCollection(java.util.LinkedHashSet::new));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   default boolean hasAnnotation(String annotationName) {
/*  80 */     return getAnnotations().isDirectlyPresent(annotationName);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   default boolean hasMetaAnnotation(String metaAnnotationName) {
/*  91 */     return getAnnotations().get(metaAnnotationName, MergedAnnotation::isMetaPresent)
/*  92 */       .isPresent();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   default boolean hasAnnotatedMethods(String annotationName) {
/* 102 */     return !getAnnotatedMethods(annotationName).isEmpty();
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
/*     */   static AnnotationMetadata introspect(Class<?> type) {
/* 127 */     return StandardAnnotationMetadata.from(type);
/*     */   }
/*     */   
/*     */   Set<MethodMetadata> getAnnotatedMethods(String paramString);
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\springframework\core\type\AnnotationMetadata.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */