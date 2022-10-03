/*    */ package org.springframework.core.annotation;
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
/*    */ 
/*    */ @FunctionalInterface
/*    */ public interface MergedAnnotationSelector<A extends java.lang.annotation.Annotation>
/*    */ {
/*    */   default boolean isBestCandidate(MergedAnnotation<A> annotation) {
/* 40 */     return false;
/*    */   }
/*    */   
/*    */   MergedAnnotation<A> select(MergedAnnotation<A> paramMergedAnnotation1, MergedAnnotation<A> paramMergedAnnotation2);
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\springframework\core\annotation\MergedAnnotationSelector.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */