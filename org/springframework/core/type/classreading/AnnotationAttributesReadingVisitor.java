/*     */ package org.springframework.core.type.classreading;
/*     */ 
/*     */ import java.lang.annotation.Annotation;
/*     */ import java.lang.reflect.Modifier;
/*     */ import java.util.LinkedHashSet;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import org.springframework.core.annotation.AnnotationAttributes;
/*     */ import org.springframework.core.annotation.AnnotationUtils;
/*     */ import org.springframework.lang.Nullable;
/*     */ import org.springframework.util.MultiValueMap;
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
/*     */ 
/*     */ @Deprecated
/*     */ final class AnnotationAttributesReadingVisitor
/*     */   extends RecursiveAnnotationAttributesVisitor
/*     */ {
/*     */   private final MultiValueMap<String, AnnotationAttributes> attributesMap;
/*     */   private final Map<String, Set<String>> metaAnnotationMap;
/*     */   
/*     */   public AnnotationAttributesReadingVisitor(String annotationType, MultiValueMap<String, AnnotationAttributes> attributesMap, Map<String, Set<String>> metaAnnotationMap, @Nullable ClassLoader classLoader) {
/*  60 */     super(annotationType, new AnnotationAttributes(annotationType, classLoader), classLoader);
/*  61 */     this.attributesMap = attributesMap;
/*  62 */     this.metaAnnotationMap = metaAnnotationMap;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void visitEnd() {
/*  68 */     super.visitEnd();
/*     */     
/*  70 */     Class<? extends Annotation> annotationClass = this.attributes.annotationType();
/*  71 */     if (annotationClass != null) {
/*  72 */       List<AnnotationAttributes> attributeList = (List<AnnotationAttributes>)this.attributesMap.get(this.annotationType);
/*  73 */       if (attributeList == null) {
/*  74 */         this.attributesMap.add(this.annotationType, this.attributes);
/*     */       } else {
/*     */         
/*  77 */         attributeList.add(0, this.attributes);
/*     */       } 
/*  79 */       if (!AnnotationUtils.isInJavaLangAnnotationPackage(annotationClass.getName())) {
/*     */         try {
/*  81 */           Annotation[] metaAnnotations = annotationClass.getAnnotations();
/*  82 */           if (!ObjectUtils.isEmpty((Object[])metaAnnotations)) {
/*  83 */             Set<Annotation> visited = new LinkedHashSet<>();
/*  84 */             for (Annotation metaAnnotation : metaAnnotations) {
/*  85 */               recursivelyCollectMetaAnnotations(visited, metaAnnotation);
/*     */             }
/*  87 */             if (!visited.isEmpty()) {
/*  88 */               Set<String> metaAnnotationTypeNames = new LinkedHashSet<>(visited.size());
/*  89 */               for (Annotation ann : visited) {
/*  90 */                 metaAnnotationTypeNames.add(ann.annotationType().getName());
/*     */               }
/*  92 */               this.metaAnnotationMap.put(annotationClass.getName(), metaAnnotationTypeNames);
/*     */             }
/*     */           
/*     */           } 
/*  96 */         } catch (Throwable ex) {
/*  97 */           if (this.logger.isDebugEnabled()) {
/*  98 */             this.logger.debug("Failed to introspect meta-annotations on " + annotationClass + ": " + ex);
/*     */           }
/*     */         } 
/*     */       }
/*     */     } 
/*     */   }
/*     */   
/*     */   private void recursivelyCollectMetaAnnotations(Set<Annotation> visited, Annotation annotation) {
/* 106 */     Class<? extends Annotation> annotationType = annotation.annotationType();
/* 107 */     String annotationName = annotationType.getName();
/* 108 */     if (!AnnotationUtils.isInJavaLangAnnotationPackage(annotationName) && visited.add(annotation))
/*     */       
/*     */       try {
/*     */ 
/*     */         
/* 113 */         if (Modifier.isPublic(annotationType.getModifiers())) {
/* 114 */           this.attributesMap.add(annotationName, 
/* 115 */               AnnotationUtils.getAnnotationAttributes(annotation, false, true));
/*     */         }
/* 117 */         for (Annotation metaMetaAnnotation : annotationType.getAnnotations()) {
/* 118 */           recursivelyCollectMetaAnnotations(visited, metaMetaAnnotation);
/*     */         }
/*     */       }
/* 121 */       catch (Throwable ex) {
/* 122 */         if (this.logger.isDebugEnabled())
/* 123 */           this.logger.debug("Failed to introspect meta-annotations on " + annotation + ": " + ex); 
/*     */       }  
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\springframework\core\type\classreading\AnnotationAttributesReadingVisitor.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */