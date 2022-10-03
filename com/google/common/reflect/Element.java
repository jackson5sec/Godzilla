/*     */ package com.google.common.reflect;
/*     */ 
/*     */ import com.google.common.base.Preconditions;
/*     */ import java.lang.annotation.Annotation;
/*     */ import java.lang.reflect.AccessibleObject;
/*     */ import java.lang.reflect.Member;
/*     */ import java.lang.reflect.Modifier;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ class Element
/*     */   extends AccessibleObject
/*     */   implements Member
/*     */ {
/*     */   private final AccessibleObject accessibleObject;
/*     */   private final Member member;
/*     */   
/*     */   <M extends AccessibleObject & Member> Element(M member) {
/*  40 */     Preconditions.checkNotNull(member);
/*  41 */     this.accessibleObject = (AccessibleObject)member;
/*  42 */     this.member = (Member)member;
/*     */   }
/*     */   
/*     */   public TypeToken<?> getOwnerType() {
/*  46 */     return TypeToken.of(getDeclaringClass());
/*     */   }
/*     */ 
/*     */   
/*     */   public final boolean isAnnotationPresent(Class<? extends Annotation> annotationClass) {
/*  51 */     return this.accessibleObject.isAnnotationPresent(annotationClass);
/*     */   }
/*     */ 
/*     */   
/*     */   public final <A extends Annotation> A getAnnotation(Class<A> annotationClass) {
/*  56 */     return this.accessibleObject.getAnnotation(annotationClass);
/*     */   }
/*     */ 
/*     */   
/*     */   public final Annotation[] getAnnotations() {
/*  61 */     return this.accessibleObject.getAnnotations();
/*     */   }
/*     */ 
/*     */   
/*     */   public final Annotation[] getDeclaredAnnotations() {
/*  66 */     return this.accessibleObject.getDeclaredAnnotations();
/*     */   }
/*     */ 
/*     */   
/*     */   public final void setAccessible(boolean flag) throws SecurityException {
/*  71 */     this.accessibleObject.setAccessible(flag);
/*     */   }
/*     */ 
/*     */   
/*     */   public final boolean isAccessible() {
/*  76 */     return this.accessibleObject.isAccessible();
/*     */   }
/*     */ 
/*     */   
/*     */   public Class<?> getDeclaringClass() {
/*  81 */     return this.member.getDeclaringClass();
/*     */   }
/*     */ 
/*     */   
/*     */   public final String getName() {
/*  86 */     return this.member.getName();
/*     */   }
/*     */ 
/*     */   
/*     */   public final int getModifiers() {
/*  91 */     return this.member.getModifiers();
/*     */   }
/*     */ 
/*     */   
/*     */   public final boolean isSynthetic() {
/*  96 */     return this.member.isSynthetic();
/*     */   }
/*     */ 
/*     */   
/*     */   public final boolean isPublic() {
/* 101 */     return Modifier.isPublic(getModifiers());
/*     */   }
/*     */ 
/*     */   
/*     */   public final boolean isProtected() {
/* 106 */     return Modifier.isProtected(getModifiers());
/*     */   }
/*     */ 
/*     */   
/*     */   public final boolean isPackagePrivate() {
/* 111 */     return (!isPrivate() && !isPublic() && !isProtected());
/*     */   }
/*     */ 
/*     */   
/*     */   public final boolean isPrivate() {
/* 116 */     return Modifier.isPrivate(getModifiers());
/*     */   }
/*     */ 
/*     */   
/*     */   public final boolean isStatic() {
/* 121 */     return Modifier.isStatic(getModifiers());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final boolean isFinal() {
/* 132 */     return Modifier.isFinal(getModifiers());
/*     */   }
/*     */ 
/*     */   
/*     */   public final boolean isAbstract() {
/* 137 */     return Modifier.isAbstract(getModifiers());
/*     */   }
/*     */ 
/*     */   
/*     */   public final boolean isNative() {
/* 142 */     return Modifier.isNative(getModifiers());
/*     */   }
/*     */ 
/*     */   
/*     */   public final boolean isSynchronized() {
/* 147 */     return Modifier.isSynchronized(getModifiers());
/*     */   }
/*     */ 
/*     */   
/*     */   final boolean isVolatile() {
/* 152 */     return Modifier.isVolatile(getModifiers());
/*     */   }
/*     */ 
/*     */   
/*     */   final boolean isTransient() {
/* 157 */     return Modifier.isTransient(getModifiers());
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean equals(Object obj) {
/* 162 */     if (obj instanceof Element) {
/* 163 */       Element that = (Element)obj;
/* 164 */       return (getOwnerType().equals(that.getOwnerType()) && this.member.equals(that.member));
/*     */     } 
/* 166 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 171 */     return this.member.hashCode();
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 176 */     return this.member.toString();
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\google\common\reflect\Element.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */