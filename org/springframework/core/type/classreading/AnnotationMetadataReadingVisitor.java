/*     */ package org.springframework.core.type.classreading;
/*     */ 
/*     */ import java.util.Collection;
/*     */ import java.util.Collections;
/*     */ import java.util.LinkedHashMap;
/*     */ import java.util.LinkedHashSet;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import org.springframework.asm.AnnotationVisitor;
/*     */ import org.springframework.asm.Attribute;
/*     */ import org.springframework.asm.FieldVisitor;
/*     */ import org.springframework.asm.MethodVisitor;
/*     */ import org.springframework.asm.Type;
/*     */ import org.springframework.core.annotation.AnnotationAttributes;
/*     */ import org.springframework.core.annotation.AnnotationUtils;
/*     */ import org.springframework.core.annotation.MergedAnnotations;
/*     */ import org.springframework.core.type.AnnotationMetadata;
/*     */ import org.springframework.core.type.MethodMetadata;
/*     */ import org.springframework.lang.Nullable;
/*     */ import org.springframework.util.LinkedMultiValueMap;
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
/*     */ @Deprecated
/*     */ public class AnnotationMetadataReadingVisitor
/*     */   extends ClassMetadataReadingVisitor
/*     */   implements AnnotationMetadata
/*     */ {
/*     */   @Nullable
/*     */   protected final ClassLoader classLoader;
/*  62 */   protected final Set<String> annotationSet = new LinkedHashSet<>(4);
/*     */   
/*  64 */   protected final Map<String, Set<String>> metaAnnotationMap = new LinkedHashMap<>(4);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  71 */   protected final LinkedMultiValueMap<String, AnnotationAttributes> attributesMap = new LinkedMultiValueMap(3);
/*     */   
/*  73 */   protected final Set<MethodMetadata> methodMetadataSet = new LinkedHashSet<>(4);
/*     */ 
/*     */   
/*     */   public AnnotationMetadataReadingVisitor(@Nullable ClassLoader classLoader) {
/*  77 */     this.classLoader = classLoader;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public MergedAnnotations getAnnotations() {
/*  83 */     throw new UnsupportedOperationException();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
/*  90 */     if ((access & 0x40) != 0) {
/*  91 */       return super.visitMethod(access, name, desc, signature, exceptions);
/*     */     }
/*  93 */     return new MethodMetadataReadingVisitor(name, access, getClassName(), 
/*  94 */         Type.getReturnType(desc).getClassName(), this.classLoader, this.methodMetadataSet);
/*     */   }
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public AnnotationVisitor visitAnnotation(String desc, boolean visible) {
/* 100 */     if (!visible) {
/* 101 */       return null;
/*     */     }
/* 103 */     String className = Type.getType(desc).getClassName();
/* 104 */     if (AnnotationUtils.isInJavaLangAnnotationPackage(className)) {
/* 105 */       return null;
/*     */     }
/* 107 */     this.annotationSet.add(className);
/* 108 */     return new AnnotationAttributesReadingVisitor(className, (MultiValueMap<String, AnnotationAttributes>)this.attributesMap, this.metaAnnotationMap, this.classLoader);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Set<String> getAnnotationTypes() {
/* 115 */     return this.annotationSet;
/*     */   }
/*     */ 
/*     */   
/*     */   public Set<String> getMetaAnnotationTypes(String annotationName) {
/* 120 */     Set<String> metaAnnotationTypes = this.metaAnnotationMap.get(annotationName);
/* 121 */     return (metaAnnotationTypes != null) ? metaAnnotationTypes : Collections.<String>emptySet();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean hasMetaAnnotation(String metaAnnotationType) {
/* 126 */     if (AnnotationUtils.isInJavaLangAnnotationPackage(metaAnnotationType)) {
/* 127 */       return false;
/*     */     }
/* 129 */     Collection<Set<String>> allMetaTypes = this.metaAnnotationMap.values();
/* 130 */     for (Set<String> metaTypes : allMetaTypes) {
/* 131 */       if (metaTypes.contains(metaAnnotationType)) {
/* 132 */         return true;
/*     */       }
/*     */     } 
/* 135 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isAnnotated(String annotationName) {
/* 140 */     return (!AnnotationUtils.isInJavaLangAnnotationPackage(annotationName) && this.attributesMap
/* 141 */       .containsKey(annotationName));
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean hasAnnotation(String annotationName) {
/* 146 */     return getAnnotationTypes().contains(annotationName);
/*     */   }
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public AnnotationAttributes getAnnotationAttributes(String annotationName, boolean classValuesAsString) {
/* 152 */     AnnotationAttributes raw = AnnotationReadingVisitorUtils.getMergedAnnotationAttributes(this.attributesMap, this.metaAnnotationMap, annotationName);
/*     */     
/* 154 */     if (raw == null) {
/* 155 */       return null;
/*     */     }
/* 157 */     return AnnotationReadingVisitorUtils.convertClassValues("class '" + 
/* 158 */         getClassName() + "'", this.classLoader, raw, classValuesAsString);
/*     */   }
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public MultiValueMap<String, Object> getAllAnnotationAttributes(String annotationName, boolean classValuesAsString) {
/* 164 */     LinkedMultiValueMap linkedMultiValueMap = new LinkedMultiValueMap();
/* 165 */     List<AnnotationAttributes> attributes = this.attributesMap.get(annotationName);
/* 166 */     if (attributes == null) {
/* 167 */       return null;
/*     */     }
/* 169 */     String annotatedElement = "class '" + getClassName() + "'";
/* 170 */     for (AnnotationAttributes raw : attributes) {
/* 171 */       for (Map.Entry<String, Object> entry : (Iterable<Map.Entry<String, Object>>)AnnotationReadingVisitorUtils.convertClassValues(annotatedElement, this.classLoader, raw, classValuesAsString)
/* 172 */         .entrySet()) {
/* 173 */         linkedMultiValueMap.add(entry.getKey(), entry.getValue());
/*     */       }
/*     */     } 
/* 176 */     return (MultiValueMap<String, Object>)linkedMultiValueMap;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean hasAnnotatedMethods(String annotationName) {
/* 181 */     for (MethodMetadata methodMetadata : this.methodMetadataSet) {
/* 182 */       if (methodMetadata.isAnnotated(annotationName)) {
/* 183 */         return true;
/*     */       }
/*     */     } 
/* 186 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public Set<MethodMetadata> getAnnotatedMethods(String annotationName) {
/* 191 */     Set<MethodMetadata> annotatedMethods = new LinkedHashSet<>(4);
/* 192 */     for (MethodMetadata methodMetadata : this.methodMetadataSet) {
/* 193 */       if (methodMetadata.isAnnotated(annotationName)) {
/* 194 */         annotatedMethods.add(methodMetadata);
/*     */       }
/*     */     } 
/* 197 */     return annotatedMethods;
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\springframework\core\type\classreading\AnnotationMetadataReadingVisitor.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */