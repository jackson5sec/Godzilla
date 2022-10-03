/*     */ package org.springframework.core.type.filter;
/*     */ 
/*     */ import java.lang.annotation.Annotation;
/*     */ import java.lang.annotation.Inherited;
/*     */ import org.springframework.core.annotation.AnnotationUtils;
/*     */ import org.springframework.core.type.AnnotationMetadata;
/*     */ import org.springframework.core.type.classreading.MetadataReader;
/*     */ import org.springframework.lang.Nullable;
/*     */ import org.springframework.util.ClassUtils;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class AnnotationTypeFilter
/*     */   extends AbstractTypeHierarchyTraversingFilter
/*     */ {
/*     */   private final Class<? extends Annotation> annotationType;
/*     */   private final boolean considerMetaAnnotations;
/*     */   
/*     */   public AnnotationTypeFilter(Class<? extends Annotation> annotationType) {
/*  61 */     this(annotationType, true, false);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public AnnotationTypeFilter(Class<? extends Annotation> annotationType, boolean considerMetaAnnotations) {
/*  71 */     this(annotationType, considerMetaAnnotations, false);
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
/*     */   public AnnotationTypeFilter(Class<? extends Annotation> annotationType, boolean considerMetaAnnotations, boolean considerInterfaces) {
/*  83 */     super(annotationType.isAnnotationPresent((Class)Inherited.class), considerInterfaces);
/*  84 */     this.annotationType = annotationType;
/*  85 */     this.considerMetaAnnotations = considerMetaAnnotations;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final Class<? extends Annotation> getAnnotationType() {
/*  94 */     return this.annotationType;
/*     */   }
/*     */ 
/*     */   
/*     */   protected boolean matchSelf(MetadataReader metadataReader) {
/*  99 */     AnnotationMetadata metadata = metadataReader.getAnnotationMetadata();
/* 100 */     return (metadata.hasAnnotation(this.annotationType.getName()) || (this.considerMetaAnnotations && metadata
/* 101 */       .hasMetaAnnotation(this.annotationType.getName())));
/*     */   }
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   protected Boolean matchSuperClass(String superClassName) {
/* 107 */     return hasAnnotation(superClassName);
/*     */   }
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   protected Boolean matchInterface(String interfaceName) {
/* 113 */     return hasAnnotation(interfaceName);
/*     */   }
/*     */   
/*     */   @Nullable
/*     */   protected Boolean hasAnnotation(String typeName) {
/* 118 */     if (Object.class.getName().equals(typeName)) {
/* 119 */       return Boolean.valueOf(false);
/*     */     }
/* 121 */     if (typeName.startsWith("java")) {
/* 122 */       if (!this.annotationType.getName().startsWith("java"))
/*     */       {
/*     */         
/* 125 */         return Boolean.valueOf(false);
/*     */       }
/*     */       try {
/* 128 */         Class<?> clazz = ClassUtils.forName(typeName, getClass().getClassLoader());
/* 129 */         return Boolean.valueOf(
/* 130 */             ((this.considerMetaAnnotations ? AnnotationUtils.getAnnotation(clazz, this.annotationType) : clazz.<Annotation>getAnnotation(this.annotationType)) != null));
/*     */       }
/* 132 */       catch (Throwable throwable) {}
/*     */     } 
/*     */ 
/*     */     
/* 136 */     return null;
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\springframework\core\type\filter\AnnotationTypeFilter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */