/*     */ package com.google.common.reflect;
/*     */ 
/*     */ import com.google.common.annotations.Beta;
/*     */ import com.google.common.base.Preconditions;
/*     */ import com.google.common.collect.FluentIterable;
/*     */ import com.google.common.collect.ImmutableList;
/*     */ import com.google.common.collect.UnmodifiableIterator;
/*     */ import java.lang.annotation.Annotation;
/*     */ import java.lang.reflect.AnnotatedElement;
/*     */ import java.lang.reflect.AnnotatedType;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @Beta
/*     */ public final class Parameter
/*     */   implements AnnotatedElement
/*     */ {
/*     */   private final Invokable<?, ?> declaration;
/*     */   private final int position;
/*     */   private final TypeToken<?> type;
/*     */   private final ImmutableList<Annotation> annotations;
/*     */   private final AnnotatedType annotatedType;
/*     */   
/*     */   Parameter(Invokable<?, ?> declaration, int position, TypeToken<?> type, Annotation[] annotations, AnnotatedType annotatedType) {
/*  48 */     this.declaration = declaration;
/*  49 */     this.position = position;
/*  50 */     this.type = type;
/*  51 */     this.annotations = ImmutableList.copyOf((Object[])annotations);
/*  52 */     this.annotatedType = annotatedType;
/*     */   }
/*     */ 
/*     */   
/*     */   public TypeToken<?> getType() {
/*  57 */     return this.type;
/*     */   }
/*     */ 
/*     */   
/*     */   public Invokable<?, ?> getDeclaringInvokable() {
/*  62 */     return this.declaration;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isAnnotationPresent(Class<? extends Annotation> annotationType) {
/*  67 */     return (getAnnotation(annotationType) != null);
/*     */   }
/*     */ 
/*     */   
/*     */   public <A extends Annotation> A getAnnotation(Class<A> annotationType) {
/*  72 */     Preconditions.checkNotNull(annotationType);
/*  73 */     for (UnmodifiableIterator<Annotation> unmodifiableIterator = this.annotations.iterator(); unmodifiableIterator.hasNext(); ) { Annotation annotation = unmodifiableIterator.next();
/*  74 */       if (annotationType.isInstance(annotation)) {
/*  75 */         return annotationType.cast(annotation);
/*     */       } }
/*     */     
/*  78 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   public Annotation[] getAnnotations() {
/*  83 */     return getDeclaredAnnotations();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public <A extends Annotation> A[] getAnnotationsByType(Class<A> annotationType) {
/*  90 */     return getDeclaredAnnotationsByType(annotationType);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Annotation[] getDeclaredAnnotations() {
/*  97 */     return (Annotation[])this.annotations.toArray((Object[])new Annotation[this.annotations.size()]);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public <A extends Annotation> A getDeclaredAnnotation(Class<A> annotationType) {
/* 104 */     Preconditions.checkNotNull(annotationType);
/* 105 */     return (A)FluentIterable.from((Iterable)this.annotations).filter(annotationType).first().orNull();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public <A extends Annotation> A[] getDeclaredAnnotationsByType(Class<A> annotationType) {
/* 112 */     return (A[])FluentIterable.from((Iterable)this.annotations).filter(annotationType).toArray(annotationType);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public AnnotatedType getAnnotatedType() {
/* 118 */     return this.annotatedType;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean equals(Object obj) {
/* 123 */     if (obj instanceof Parameter) {
/* 124 */       Parameter that = (Parameter)obj;
/* 125 */       return (this.position == that.position && this.declaration.equals(that.declaration));
/*     */     } 
/* 127 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 132 */     return this.position;
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 137 */     return this.type + " arg" + this.position;
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\google\common\reflect\Parameter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */