/*    */ package org.springframework.core.annotation;
/*    */ 
/*    */ import java.util.Arrays;
/*    */ import org.springframework.lang.Nullable;
/*    */ import org.springframework.util.Assert;
/*    */ import org.springframework.util.StringUtils;
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
/*    */ final class PackagesAnnotationFilter
/*    */   implements AnnotationFilter
/*    */ {
/*    */   private final String[] prefixes;
/*    */   private final int hashCode;
/*    */   
/*    */   PackagesAnnotationFilter(String... packages) {
/* 40 */     Assert.notNull(packages, "Packages array must not be null");
/* 41 */     this.prefixes = new String[packages.length];
/* 42 */     for (int i = 0; i < packages.length; i++) {
/* 43 */       String pkg = packages[i];
/* 44 */       Assert.hasText(pkg, "Packages array must not have empty elements");
/* 45 */       this.prefixes[i] = pkg + ".";
/*    */     } 
/* 47 */     Arrays.sort((Object[])this.prefixes);
/* 48 */     this.hashCode = Arrays.hashCode((Object[])this.prefixes);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public boolean matches(String annotationType) {
/* 54 */     for (String prefix : this.prefixes) {
/* 55 */       if (annotationType.startsWith(prefix)) {
/* 56 */         return true;
/*    */       }
/*    */     } 
/* 59 */     return false;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public boolean equals(@Nullable Object other) {
/* 65 */     if (this == other) {
/* 66 */       return true;
/*    */     }
/* 68 */     if (other == null || getClass() != other.getClass()) {
/* 69 */       return false;
/*    */     }
/* 71 */     return Arrays.equals((Object[])this.prefixes, (Object[])((PackagesAnnotationFilter)other).prefixes);
/*    */   }
/*    */ 
/*    */   
/*    */   public int hashCode() {
/* 76 */     return this.hashCode;
/*    */   }
/*    */ 
/*    */   
/*    */   public String toString() {
/* 81 */     return "Packages annotation filter: " + 
/* 82 */       StringUtils.arrayToCommaDelimitedString((Object[])this.prefixes);
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\springframework\core\annotation\PackagesAnnotationFilter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */