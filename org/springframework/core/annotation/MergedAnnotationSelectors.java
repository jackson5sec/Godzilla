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
/*     */ public abstract class MergedAnnotationSelectors
/*     */ {
/*  33 */   private static final MergedAnnotationSelector<?> NEAREST = new Nearest();
/*     */   
/*  35 */   private static final MergedAnnotationSelector<?> FIRST_DIRECTLY_DECLARED = new FirstDirectlyDeclared();
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
/*     */   public static <A extends Annotation> MergedAnnotationSelector<A> nearest() {
/*  48 */     return (MergedAnnotationSelector)NEAREST;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <A extends Annotation> MergedAnnotationSelector<A> firstDirectlyDeclared() {
/*  58 */     return (MergedAnnotationSelector)FIRST_DIRECTLY_DECLARED;
/*     */   }
/*     */ 
/*     */   
/*     */   private static class Nearest
/*     */     implements MergedAnnotationSelector<Annotation>
/*     */   {
/*     */     private Nearest() {}
/*     */ 
/*     */     
/*     */     public boolean isBestCandidate(MergedAnnotation<Annotation> annotation) {
/*  69 */       return (annotation.getDistance() == 0);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public MergedAnnotation<Annotation> select(MergedAnnotation<Annotation> existing, MergedAnnotation<Annotation> candidate) {
/*  76 */       if (candidate.getDistance() < existing.getDistance()) {
/*  77 */         return candidate;
/*     */       }
/*  79 */       return existing;
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static class FirstDirectlyDeclared
/*     */     implements MergedAnnotationSelector<Annotation>
/*     */   {
/*     */     private FirstDirectlyDeclared() {}
/*     */ 
/*     */ 
/*     */     
/*     */     public boolean isBestCandidate(MergedAnnotation<Annotation> annotation) {
/*  93 */       return (annotation.getDistance() == 0);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public MergedAnnotation<Annotation> select(MergedAnnotation<Annotation> existing, MergedAnnotation<Annotation> candidate) {
/* 100 */       if (existing.getDistance() > 0 && candidate.getDistance() == 0) {
/* 101 */         return candidate;
/*     */       }
/* 103 */       return existing;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\springframework\core\annotation\MergedAnnotationSelectors.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */