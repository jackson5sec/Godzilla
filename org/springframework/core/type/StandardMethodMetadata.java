/*     */ package org.springframework.core.type;
/*     */ 
/*     */ import java.lang.reflect.Method;
/*     */ import java.lang.reflect.Modifier;
/*     */ import java.util.Map;
/*     */ import org.springframework.core.annotation.AnnotatedElementUtils;
/*     */ import org.springframework.core.annotation.MergedAnnotations;
/*     */ import org.springframework.core.annotation.RepeatableContainers;
/*     */ import org.springframework.lang.Nullable;
/*     */ import org.springframework.util.Assert;
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
/*     */ public class StandardMethodMetadata
/*     */   implements MethodMetadata
/*     */ {
/*     */   private final Method introspectedMethod;
/*     */   private final boolean nestedAnnotationsAsMap;
/*     */   private final MergedAnnotations mergedAnnotations;
/*     */   
/*     */   @Deprecated
/*     */   public StandardMethodMetadata(Method introspectedMethod) {
/*  58 */     this(introspectedMethod, false);
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
/*     */   @Deprecated
/*     */   public StandardMethodMetadata(Method introspectedMethod, boolean nestedAnnotationsAsMap) {
/*  75 */     Assert.notNull(introspectedMethod, "Method must not be null");
/*  76 */     this.introspectedMethod = introspectedMethod;
/*  77 */     this.nestedAnnotationsAsMap = nestedAnnotationsAsMap;
/*  78 */     this.mergedAnnotations = MergedAnnotations.from(introspectedMethod, MergedAnnotations.SearchStrategy.DIRECT, 
/*  79 */         RepeatableContainers.none());
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public MergedAnnotations getAnnotations() {
/*  85 */     return this.mergedAnnotations;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final Method getIntrospectedMethod() {
/*  92 */     return this.introspectedMethod;
/*     */   }
/*     */ 
/*     */   
/*     */   public String getMethodName() {
/*  97 */     return this.introspectedMethod.getName();
/*     */   }
/*     */ 
/*     */   
/*     */   public String getDeclaringClassName() {
/* 102 */     return this.introspectedMethod.getDeclaringClass().getName();
/*     */   }
/*     */ 
/*     */   
/*     */   public String getReturnTypeName() {
/* 107 */     return this.introspectedMethod.getReturnType().getName();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isAbstract() {
/* 112 */     return Modifier.isAbstract(this.introspectedMethod.getModifiers());
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isStatic() {
/* 117 */     return Modifier.isStatic(this.introspectedMethod.getModifiers());
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isFinal() {
/* 122 */     return Modifier.isFinal(this.introspectedMethod.getModifiers());
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isOverridable() {
/* 127 */     return (!isStatic() && !isFinal() && !isPrivate());
/*     */   }
/*     */   
/*     */   private boolean isPrivate() {
/* 131 */     return Modifier.isPrivate(this.introspectedMethod.getModifiers());
/*     */   }
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public Map<String, Object> getAnnotationAttributes(String annotationName, boolean classValuesAsString) {
/* 137 */     if (this.nestedAnnotationsAsMap) {
/* 138 */       return super.getAnnotationAttributes(annotationName, classValuesAsString);
/*     */     }
/* 140 */     return (Map<String, Object>)AnnotatedElementUtils.getMergedAnnotationAttributes(this.introspectedMethod, annotationName, classValuesAsString, false);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public MultiValueMap<String, Object> getAllAnnotationAttributes(String annotationName, boolean classValuesAsString) {
/* 147 */     if (this.nestedAnnotationsAsMap) {
/* 148 */       return super.getAllAnnotationAttributes(annotationName, classValuesAsString);
/*     */     }
/* 150 */     return AnnotatedElementUtils.getAllAnnotationAttributes(this.introspectedMethod, annotationName, classValuesAsString, false);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean equals(@Nullable Object obj) {
/* 156 */     return (this == obj || (obj instanceof StandardMethodMetadata && this.introspectedMethod
/* 157 */       .equals(((StandardMethodMetadata)obj).introspectedMethod)));
/*     */   }
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 162 */     return this.introspectedMethod.hashCode();
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 167 */     return this.introspectedMethod.toString();
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\springframework\core\type\StandardMethodMetadata.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */