/*     */ package org.springframework.core.type.classreading;
/*     */ 
/*     */ import java.util.LinkedHashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import org.springframework.asm.AnnotationVisitor;
/*     */ import org.springframework.asm.MethodVisitor;
/*     */ import org.springframework.asm.Type;
/*     */ import org.springframework.core.annotation.AnnotationAttributes;
/*     */ import org.springframework.core.annotation.MergedAnnotations;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @Deprecated
/*     */ public class MethodMetadataReadingVisitor
/*     */   extends MethodVisitor
/*     */   implements MethodMetadata
/*     */ {
/*     */   protected final String methodName;
/*     */   protected final int access;
/*     */   protected final String declaringClassName;
/*     */   protected final String returnTypeName;
/*     */   @Nullable
/*     */   protected final ClassLoader classLoader;
/*     */   protected final Set<MethodMetadata> methodMetadataSet;
/*  67 */   protected final Map<String, Set<String>> metaAnnotationMap = new LinkedHashMap<>(4);
/*     */   
/*  69 */   protected final LinkedMultiValueMap<String, AnnotationAttributes> attributesMap = new LinkedMultiValueMap(3);
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public MethodMetadataReadingVisitor(String methodName, int access, String declaringClassName, String returnTypeName, @Nullable ClassLoader classLoader, Set<MethodMetadata> methodMetadataSet) {
/*  75 */     super(17432576);
/*  76 */     this.methodName = methodName;
/*  77 */     this.access = access;
/*  78 */     this.declaringClassName = declaringClassName;
/*  79 */     this.returnTypeName = returnTypeName;
/*  80 */     this.classLoader = classLoader;
/*  81 */     this.methodMetadataSet = methodMetadataSet;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public MergedAnnotations getAnnotations() {
/*  87 */     throw new UnsupportedOperationException();
/*     */   }
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public AnnotationVisitor visitAnnotation(String desc, boolean visible) {
/*  93 */     if (!visible) {
/*  94 */       return null;
/*     */     }
/*  96 */     this.methodMetadataSet.add(this);
/*  97 */     String className = Type.getType(desc).getClassName();
/*  98 */     return new AnnotationAttributesReadingVisitor(className, (MultiValueMap<String, AnnotationAttributes>)this.attributesMap, this.metaAnnotationMap, this.classLoader);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getMethodName() {
/* 105 */     return this.methodName;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isAbstract() {
/* 110 */     return ((this.access & 0x400) != 0);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isStatic() {
/* 115 */     return ((this.access & 0x8) != 0);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isFinal() {
/* 120 */     return ((this.access & 0x10) != 0);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isOverridable() {
/* 125 */     return (!isStatic() && !isFinal() && (this.access & 0x2) == 0);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isAnnotated(String annotationName) {
/* 130 */     return this.attributesMap.containsKey(annotationName);
/*     */   }
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public AnnotationAttributes getAnnotationAttributes(String annotationName, boolean classValuesAsString) {
/* 136 */     AnnotationAttributes raw = AnnotationReadingVisitorUtils.getMergedAnnotationAttributes(this.attributesMap, this.metaAnnotationMap, annotationName);
/*     */     
/* 138 */     if (raw == null) {
/* 139 */       return null;
/*     */     }
/* 141 */     return AnnotationReadingVisitorUtils.convertClassValues("method '" + 
/* 142 */         getMethodName() + "'", this.classLoader, raw, classValuesAsString);
/*     */   }
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public MultiValueMap<String, Object> getAllAnnotationAttributes(String annotationName, boolean classValuesAsString) {
/* 148 */     if (!this.attributesMap.containsKey(annotationName)) {
/* 149 */       return null;
/*     */     }
/* 151 */     LinkedMultiValueMap linkedMultiValueMap = new LinkedMultiValueMap();
/* 152 */     List<AnnotationAttributes> attributesList = this.attributesMap.get(annotationName);
/* 153 */     if (attributesList != null) {
/* 154 */       String annotatedElement = "method '" + getMethodName() + '\'';
/* 155 */       for (AnnotationAttributes annotationAttributes : attributesList) {
/* 156 */         AnnotationAttributes convertedAttributes = AnnotationReadingVisitorUtils.convertClassValues(annotatedElement, this.classLoader, annotationAttributes, classValuesAsString);
/*     */         
/* 158 */         convertedAttributes.forEach(linkedMultiValueMap::add);
/*     */       } 
/*     */     } 
/* 161 */     return (MultiValueMap<String, Object>)linkedMultiValueMap;
/*     */   }
/*     */ 
/*     */   
/*     */   public String getDeclaringClassName() {
/* 166 */     return this.declaringClassName;
/*     */   }
/*     */ 
/*     */   
/*     */   public String getReturnTypeName() {
/* 171 */     return this.returnTypeName;
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\springframework\core\type\classreading\MethodMetadataReadingVisitor.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */