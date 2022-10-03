/*     */ package org.springframework.core.type;
/*     */ 
/*     */ import java.lang.reflect.Method;
/*     */ import java.util.Collections;
/*     */ import java.util.LinkedHashSet;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import org.springframework.core.annotation.AnnotatedElementUtils;
/*     */ import org.springframework.core.annotation.AnnotationUtils;
/*     */ import org.springframework.core.annotation.MergedAnnotations;
/*     */ import org.springframework.core.annotation.RepeatableContainers;
/*     */ import org.springframework.lang.Nullable;
/*     */ import org.springframework.util.MultiValueMap;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class StandardAnnotationMetadata
/*     */   extends StandardClassMetadata
/*     */   implements AnnotationMetadata
/*     */ {
/*     */   private final MergedAnnotations mergedAnnotations;
/*     */   private final boolean nestedAnnotationsAsMap;
/*     */   @Nullable
/*     */   private Set<String> annotationTypes;
/*     */   
/*     */   @Deprecated
/*     */   public StandardAnnotationMetadata(Class<?> introspectedClass) {
/*  65 */     this(introspectedClass, false);
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
/*     */   @Deprecated
/*     */   public StandardAnnotationMetadata(Class<?> introspectedClass, boolean nestedAnnotationsAsMap) {
/*  85 */     super(introspectedClass);
/*  86 */     this.mergedAnnotations = MergedAnnotations.from(introspectedClass, MergedAnnotations.SearchStrategy.INHERITED_ANNOTATIONS, 
/*  87 */         RepeatableContainers.none());
/*  88 */     this.nestedAnnotationsAsMap = nestedAnnotationsAsMap;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public MergedAnnotations getAnnotations() {
/*  94 */     return this.mergedAnnotations;
/*     */   }
/*     */ 
/*     */   
/*     */   public Set<String> getAnnotationTypes() {
/*  99 */     Set<String> annotationTypes = this.annotationTypes;
/* 100 */     if (annotationTypes == null) {
/* 101 */       annotationTypes = Collections.unmodifiableSet(super.getAnnotationTypes());
/* 102 */       this.annotationTypes = annotationTypes;
/*     */     } 
/* 104 */     return annotationTypes;
/*     */   }
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public Map<String, Object> getAnnotationAttributes(String annotationName, boolean classValuesAsString) {
/* 110 */     if (this.nestedAnnotationsAsMap) {
/* 111 */       return super.getAnnotationAttributes(annotationName, classValuesAsString);
/*     */     }
/* 113 */     return (Map<String, Object>)AnnotatedElementUtils.getMergedAnnotationAttributes(
/* 114 */         getIntrospectedClass(), annotationName, classValuesAsString, false);
/*     */   }
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public MultiValueMap<String, Object> getAllAnnotationAttributes(String annotationName, boolean classValuesAsString) {
/* 120 */     if (this.nestedAnnotationsAsMap) {
/* 121 */       return super.getAllAnnotationAttributes(annotationName, classValuesAsString);
/*     */     }
/* 123 */     return AnnotatedElementUtils.getAllAnnotationAttributes(
/* 124 */         getIntrospectedClass(), annotationName, classValuesAsString, false);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean hasAnnotatedMethods(String annotationName) {
/* 129 */     if (AnnotationUtils.isCandidateClass(getIntrospectedClass(), annotationName)) {
/*     */       try {
/* 131 */         Method[] methods = ReflectionUtils.getDeclaredMethods(getIntrospectedClass());
/* 132 */         for (Method method : methods) {
/* 133 */           if (isAnnotatedMethod(method, annotationName)) {
/* 134 */             return true;
/*     */           }
/*     */         }
/*     */       
/* 138 */       } catch (Throwable ex) {
/* 139 */         throw new IllegalStateException("Failed to introspect annotated methods on " + getIntrospectedClass(), ex);
/*     */       } 
/*     */     }
/* 142 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Set<MethodMetadata> getAnnotatedMethods(String annotationName) {
/* 148 */     Set<MethodMetadata> annotatedMethods = null;
/* 149 */     if (AnnotationUtils.isCandidateClass(getIntrospectedClass(), annotationName)) {
/*     */       try {
/* 151 */         Method[] methods = ReflectionUtils.getDeclaredMethods(getIntrospectedClass());
/* 152 */         for (Method method : methods) {
/* 153 */           if (isAnnotatedMethod(method, annotationName)) {
/* 154 */             if (annotatedMethods == null) {
/* 155 */               annotatedMethods = new LinkedHashSet<>(4);
/*     */             }
/* 157 */             annotatedMethods.add(new StandardMethodMetadata(method, this.nestedAnnotationsAsMap));
/*     */           }
/*     */         
/*     */         } 
/* 161 */       } catch (Throwable ex) {
/* 162 */         throw new IllegalStateException("Failed to introspect annotated methods on " + getIntrospectedClass(), ex);
/*     */       } 
/*     */     }
/* 165 */     return (annotatedMethods != null) ? annotatedMethods : Collections.<MethodMetadata>emptySet();
/*     */   }
/*     */ 
/*     */   
/*     */   private static boolean isAnnotatedMethod(Method method, String annotationName) {
/* 170 */     return (!method.isBridge() && (method.getAnnotations()).length > 0 && 
/* 171 */       AnnotatedElementUtils.isAnnotated(method, annotationName));
/*     */   }
/*     */   
/*     */   static AnnotationMetadata from(Class<?> introspectedClass) {
/* 175 */     return new StandardAnnotationMetadata(introspectedClass, true);
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\springframework\core\type\StandardAnnotationMetadata.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */