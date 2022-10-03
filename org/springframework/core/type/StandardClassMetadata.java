/*     */ package org.springframework.core.type;
/*     */ 
/*     */ import java.lang.reflect.Modifier;
/*     */ import java.util.LinkedHashSet;
/*     */ import org.springframework.lang.Nullable;
/*     */ import org.springframework.util.Assert;
/*     */ import org.springframework.util.StringUtils;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class StandardClassMetadata
/*     */   implements ClassMetadata
/*     */ {
/*     */   private final Class<?> introspectedClass;
/*     */   
/*     */   @Deprecated
/*     */   public StandardClassMetadata(Class<?> introspectedClass) {
/*  46 */     Assert.notNull(introspectedClass, "Class must not be null");
/*  47 */     this.introspectedClass = introspectedClass;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final Class<?> getIntrospectedClass() {
/*  54 */     return this.introspectedClass;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String getClassName() {
/*  60 */     return this.introspectedClass.getName();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isInterface() {
/*  65 */     return this.introspectedClass.isInterface();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isAnnotation() {
/*  70 */     return this.introspectedClass.isAnnotation();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isAbstract() {
/*  75 */     return Modifier.isAbstract(this.introspectedClass.getModifiers());
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isFinal() {
/*  80 */     return Modifier.isFinal(this.introspectedClass.getModifiers());
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isIndependent() {
/*  85 */     return (!hasEnclosingClass() || (this.introspectedClass
/*  86 */       .getDeclaringClass() != null && 
/*  87 */       Modifier.isStatic(this.introspectedClass.getModifiers())));
/*     */   }
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public String getEnclosingClassName() {
/*  93 */     Class<?> enclosingClass = this.introspectedClass.getEnclosingClass();
/*  94 */     return (enclosingClass != null) ? enclosingClass.getName() : null;
/*     */   }
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public String getSuperClassName() {
/* 100 */     Class<?> superClass = this.introspectedClass.getSuperclass();
/* 101 */     return (superClass != null) ? superClass.getName() : null;
/*     */   }
/*     */ 
/*     */   
/*     */   public String[] getInterfaceNames() {
/* 106 */     Class<?>[] ifcs = this.introspectedClass.getInterfaces();
/* 107 */     String[] ifcNames = new String[ifcs.length];
/* 108 */     for (int i = 0; i < ifcs.length; i++) {
/* 109 */       ifcNames[i] = ifcs[i].getName();
/*     */     }
/* 111 */     return ifcNames;
/*     */   }
/*     */ 
/*     */   
/*     */   public String[] getMemberClassNames() {
/* 116 */     LinkedHashSet<String> memberClassNames = new LinkedHashSet<>(4);
/* 117 */     for (Class<?> nestedClass : this.introspectedClass.getDeclaredClasses()) {
/* 118 */       memberClassNames.add(nestedClass.getName());
/*     */     }
/* 120 */     return StringUtils.toStringArray(memberClassNames);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean equals(@Nullable Object obj) {
/* 125 */     return (this == obj || (obj instanceof StandardClassMetadata && 
/* 126 */       getIntrospectedClass().equals(((StandardClassMetadata)obj).getIntrospectedClass())));
/*     */   }
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 131 */     return getIntrospectedClass().hashCode();
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 136 */     return getClassName();
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\springframework\core\type\StandardClassMetadata.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */