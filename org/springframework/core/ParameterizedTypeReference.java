/*     */ package org.springframework.core;
/*     */ 
/*     */ import java.lang.reflect.ParameterizedType;
/*     */ import java.lang.reflect.Type;
/*     */ import org.springframework.lang.Nullable;
/*     */ import org.springframework.util.Assert;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class ParameterizedTypeReference<T>
/*     */ {
/*     */   private final Type type;
/*     */   
/*     */   protected ParameterizedTypeReference() {
/*  51 */     Class<?> parameterizedTypeReferenceSubclass = findParameterizedTypeReferenceSubclass(getClass());
/*  52 */     Type type = parameterizedTypeReferenceSubclass.getGenericSuperclass();
/*  53 */     Assert.isInstanceOf(ParameterizedType.class, type, "Type must be a parameterized type");
/*  54 */     ParameterizedType parameterizedType = (ParameterizedType)type;
/*  55 */     Type[] actualTypeArguments = parameterizedType.getActualTypeArguments();
/*  56 */     Assert.isTrue((actualTypeArguments.length == 1), "Number of type arguments must be 1");
/*  57 */     this.type = actualTypeArguments[0];
/*     */   }
/*     */   
/*     */   private ParameterizedTypeReference(Type type) {
/*  61 */     this.type = type;
/*     */   }
/*     */ 
/*     */   
/*     */   public Type getType() {
/*  66 */     return this.type;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean equals(@Nullable Object other) {
/*  71 */     return (this == other || (other instanceof ParameterizedTypeReference && this.type
/*  72 */       .equals(((ParameterizedTypeReference)other).type)));
/*     */   }
/*     */ 
/*     */   
/*     */   public int hashCode() {
/*  77 */     return this.type.hashCode();
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/*  82 */     return "ParameterizedTypeReference<" + this.type + ">";
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
/*     */   public static <T> ParameterizedTypeReference<T> forType(Type type) {
/*  95 */     return new ParameterizedTypeReference<T>(type) {  }
/*     */       ;
/*     */   }
/*     */   
/*     */   private static Class<?> findParameterizedTypeReferenceSubclass(Class<?> child) {
/* 100 */     Class<?> parent = child.getSuperclass();
/* 101 */     if (Object.class == parent) {
/* 102 */       throw new IllegalStateException("Expected ParameterizedTypeReference superclass");
/*     */     }
/* 104 */     if (ParameterizedTypeReference.class == parent) {
/* 105 */       return child;
/*     */     }
/*     */     
/* 108 */     return findParameterizedTypeReferenceSubclass(parent);
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\springframework\core\ParameterizedTypeReference.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */