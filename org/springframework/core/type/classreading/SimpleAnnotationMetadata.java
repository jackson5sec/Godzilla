/*     */ package org.springframework.core.type.classreading;
/*     */ 
/*     */ import java.util.Collections;
/*     */ import java.util.LinkedHashSet;
/*     */ import java.util.Set;
/*     */ import org.springframework.core.annotation.MergedAnnotations;
/*     */ import org.springframework.core.type.AnnotationMetadata;
/*     */ import org.springframework.core.type.MethodMetadata;
/*     */ import org.springframework.lang.Nullable;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ final class SimpleAnnotationMetadata
/*     */   implements AnnotationMetadata
/*     */ {
/*     */   private final String className;
/*     */   private final int access;
/*     */   @Nullable
/*     */   private final String enclosingClassName;
/*     */   @Nullable
/*     */   private final String superClassName;
/*     */   private final boolean independentInnerClass;
/*     */   private final String[] interfaceNames;
/*     */   private final String[] memberClassNames;
/*     */   private final MethodMetadata[] annotatedMethods;
/*     */   private final MergedAnnotations annotations;
/*     */   @Nullable
/*     */   private Set<String> annotationTypes;
/*     */   
/*     */   SimpleAnnotationMetadata(String className, int access, @Nullable String enclosingClassName, @Nullable String superClassName, boolean independentInnerClass, String[] interfaceNames, String[] memberClassNames, MethodMetadata[] annotatedMethods, MergedAnnotations annotations) {
/*  67 */     this.className = className;
/*  68 */     this.access = access;
/*  69 */     this.enclosingClassName = enclosingClassName;
/*  70 */     this.superClassName = superClassName;
/*  71 */     this.independentInnerClass = independentInnerClass;
/*  72 */     this.interfaceNames = interfaceNames;
/*  73 */     this.memberClassNames = memberClassNames;
/*  74 */     this.annotatedMethods = annotatedMethods;
/*  75 */     this.annotations = annotations;
/*     */   }
/*     */ 
/*     */   
/*     */   public String getClassName() {
/*  80 */     return this.className;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isInterface() {
/*  85 */     return ((this.access & 0x200) != 0);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isAnnotation() {
/*  90 */     return ((this.access & 0x2000) != 0);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isAbstract() {
/*  95 */     return ((this.access & 0x400) != 0);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isFinal() {
/* 100 */     return ((this.access & 0x10) != 0);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isIndependent() {
/* 105 */     return (this.enclosingClassName == null || this.independentInnerClass);
/*     */   }
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public String getEnclosingClassName() {
/* 111 */     return this.enclosingClassName;
/*     */   }
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public String getSuperClassName() {
/* 117 */     return this.superClassName;
/*     */   }
/*     */ 
/*     */   
/*     */   public String[] getInterfaceNames() {
/* 122 */     return (String[])this.interfaceNames.clone();
/*     */   }
/*     */ 
/*     */   
/*     */   public String[] getMemberClassNames() {
/* 127 */     return (String[])this.memberClassNames.clone();
/*     */   }
/*     */ 
/*     */   
/*     */   public Set<String> getAnnotationTypes() {
/* 132 */     Set<String> annotationTypes = this.annotationTypes;
/* 133 */     if (annotationTypes == null) {
/* 134 */       annotationTypes = Collections.unmodifiableSet(super
/* 135 */           .getAnnotationTypes());
/* 136 */       this.annotationTypes = annotationTypes;
/*     */     } 
/* 138 */     return annotationTypes;
/*     */   }
/*     */ 
/*     */   
/*     */   public Set<MethodMetadata> getAnnotatedMethods(String annotationName) {
/* 143 */     Set<MethodMetadata> annotatedMethods = null;
/* 144 */     for (MethodMetadata annotatedMethod : this.annotatedMethods) {
/* 145 */       if (annotatedMethod.isAnnotated(annotationName)) {
/* 146 */         if (annotatedMethods == null) {
/* 147 */           annotatedMethods = new LinkedHashSet<>(4);
/*     */         }
/* 149 */         annotatedMethods.add(annotatedMethod);
/*     */       } 
/*     */     } 
/* 152 */     return (annotatedMethods != null) ? annotatedMethods : Collections.<MethodMetadata>emptySet();
/*     */   }
/*     */ 
/*     */   
/*     */   public MergedAnnotations getAnnotations() {
/* 157 */     return this.annotations;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean equals(@Nullable Object obj) {
/* 162 */     return (this == obj || (obj instanceof SimpleAnnotationMetadata && this.className
/* 163 */       .equals(((SimpleAnnotationMetadata)obj).className)));
/*     */   }
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 168 */     return this.className.hashCode();
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 173 */     return this.className;
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\springframework\core\type\classreading\SimpleAnnotationMetadata.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */