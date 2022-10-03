/*    */ package org.springframework.core.annotation;
/*    */ 
/*    */ import java.lang.annotation.Annotation;
/*    */ import org.springframework.lang.Nullable;
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
/*    */ 
/*    */ @FunctionalInterface
/*    */ interface AnnotationsProcessor<C, R>
/*    */ {
/*    */   @Nullable
/*    */   default R doWithAggregate(C context, int aggregateIndex) {
/* 45 */     return null;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   @Nullable
/*    */   R doWithAnnotations(C paramC, int paramInt, @Nullable Object paramObject, Annotation[] paramArrayOfAnnotation);
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   @Nullable
/*    */   default R finish(@Nullable R result) {
/* 69 */     return result;
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\springframework\core\annotation\AnnotationsProcessor.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */