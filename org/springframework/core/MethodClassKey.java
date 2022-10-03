/*    */ package org.springframework.core;
/*    */ 
/*    */ import java.lang.reflect.Method;
/*    */ import org.springframework.lang.Nullable;
/*    */ import org.springframework.util.ObjectUtils;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public final class MethodClassKey
/*    */   implements Comparable<MethodClassKey>
/*    */ {
/*    */   private final Method method;
/*    */   @Nullable
/*    */   private final Class<?> targetClass;
/*    */   
/*    */   public MethodClassKey(Method method, @Nullable Class<?> targetClass) {
/* 47 */     this.method = method;
/* 48 */     this.targetClass = targetClass;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public boolean equals(@Nullable Object other) {
/* 54 */     if (this == other) {
/* 55 */       return true;
/*    */     }
/* 57 */     if (!(other instanceof MethodClassKey)) {
/* 58 */       return false;
/*    */     }
/* 60 */     MethodClassKey otherKey = (MethodClassKey)other;
/* 61 */     return (this.method.equals(otherKey.method) && 
/* 62 */       ObjectUtils.nullSafeEquals(this.targetClass, otherKey.targetClass));
/*    */   }
/*    */ 
/*    */   
/*    */   public int hashCode() {
/* 67 */     return this.method.hashCode() + ((this.targetClass != null) ? (this.targetClass.hashCode() * 29) : 0);
/*    */   }
/*    */ 
/*    */   
/*    */   public String toString() {
/* 72 */     return this.method + ((this.targetClass != null) ? (" on " + this.targetClass) : "");
/*    */   }
/*    */ 
/*    */   
/*    */   public int compareTo(MethodClassKey other) {
/* 77 */     int result = this.method.getName().compareTo(other.method.getName());
/* 78 */     if (result == 0) {
/* 79 */       result = this.method.toString().compareTo(other.method.toString());
/* 80 */       if (result == 0 && this.targetClass != null && other.targetClass != null) {
/* 81 */         result = this.targetClass.getName().compareTo(other.targetClass.getName());
/*    */       }
/*    */     } 
/* 84 */     return result;
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\springframework\core\MethodClassKey.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */