/*     */ package org.springframework.core.annotation;
/*     */ 
/*     */ import java.lang.annotation.Annotation;
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
/*     */ @FunctionalInterface
/*     */ public interface AnnotationFilter
/*     */ {
/*  43 */   public static final AnnotationFilter PLAIN = packages(new String[] { "java.lang", "org.springframework.lang" });
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  49 */   public static final AnnotationFilter JAVA = packages(new String[] { "java", "javax" });
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  55 */   public static final AnnotationFilter ALL = new AnnotationFilter()
/*     */     {
/*     */       public boolean matches(Annotation annotation) {
/*  58 */         return true;
/*     */       }
/*     */       
/*     */       public boolean matches(Class<?> type) {
/*  62 */         return true;
/*     */       }
/*     */       
/*     */       public boolean matches(String typeName) {
/*  66 */         return true;
/*     */       }
/*     */       
/*     */       public String toString() {
/*  70 */         return "All annotations filtered";
/*     */       }
/*     */     };
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
/*  83 */   public static final AnnotationFilter NONE = new AnnotationFilter()
/*     */     {
/*     */       public boolean matches(Annotation annotation) {
/*  86 */         return false;
/*     */       }
/*     */       
/*     */       public boolean matches(Class<?> type) {
/*  90 */         return false;
/*     */       }
/*     */       
/*     */       public boolean matches(String typeName) {
/*  94 */         return false;
/*     */       }
/*     */       
/*     */       public String toString() {
/*  98 */         return "No annotation filtering";
/*     */       }
/*     */     };
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   default boolean matches(Annotation annotation) {
/* 109 */     return matches(annotation.annotationType());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   default boolean matches(Class<?> type) {
/* 118 */     return matches(type.getName());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   boolean matches(String paramString);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static AnnotationFilter packages(String... packages) {
/* 136 */     return new PackagesAnnotationFilter(packages);
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\springframework\core\annotation\AnnotationFilter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */