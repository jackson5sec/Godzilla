/*     */ package org.springframework.core.type.classreading;
/*     */ 
/*     */ import org.springframework.core.annotation.MergedAnnotations;
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
/*     */ final class SimpleMethodMetadata
/*     */   implements MethodMetadata
/*     */ {
/*     */   private final String methodName;
/*     */   private final int access;
/*     */   private final String declaringClassName;
/*     */   private final String returnTypeName;
/*     */   private final Object source;
/*     */   private final MergedAnnotations annotations;
/*     */   
/*     */   SimpleMethodMetadata(String methodName, int access, String declaringClassName, String returnTypeName, Object source, MergedAnnotations annotations) {
/*  50 */     this.methodName = methodName;
/*  51 */     this.access = access;
/*  52 */     this.declaringClassName = declaringClassName;
/*  53 */     this.returnTypeName = returnTypeName;
/*  54 */     this.source = source;
/*  55 */     this.annotations = annotations;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String getMethodName() {
/*  61 */     return this.methodName;
/*     */   }
/*     */ 
/*     */   
/*     */   public String getDeclaringClassName() {
/*  66 */     return this.declaringClassName;
/*     */   }
/*     */ 
/*     */   
/*     */   public String getReturnTypeName() {
/*  71 */     return this.returnTypeName;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isAbstract() {
/*  76 */     return ((this.access & 0x400) != 0);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isStatic() {
/*  81 */     return ((this.access & 0x8) != 0);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isFinal() {
/*  86 */     return ((this.access & 0x10) != 0);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isOverridable() {
/*  91 */     return (!isStatic() && !isFinal() && !isPrivate());
/*     */   }
/*     */   
/*     */   private boolean isPrivate() {
/*  95 */     return ((this.access & 0x2) != 0);
/*     */   }
/*     */ 
/*     */   
/*     */   public MergedAnnotations getAnnotations() {
/* 100 */     return this.annotations;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean equals(@Nullable Object obj) {
/* 105 */     return (this == obj || (obj instanceof SimpleMethodMetadata && this.source
/* 106 */       .equals(((SimpleMethodMetadata)obj).source)));
/*     */   }
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 111 */     return this.source.hashCode();
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 116 */     return this.source.toString();
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\springframework\core\type\classreading\SimpleMethodMetadata.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */