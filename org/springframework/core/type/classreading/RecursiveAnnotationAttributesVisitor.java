/*    */ package org.springframework.core.type.classreading;
/*    */ 
/*    */ import org.springframework.core.annotation.AnnotationAttributes;
/*    */ import org.springframework.core.annotation.AnnotationUtils;
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
/*    */ @Deprecated
/*    */ class RecursiveAnnotationAttributesVisitor
/*    */   extends AbstractRecursiveAnnotationVisitor
/*    */ {
/*    */   protected final String annotationType;
/*    */   
/*    */   public RecursiveAnnotationAttributesVisitor(String annotationType, AnnotationAttributes attributes, @Nullable ClassLoader classLoader) {
/* 43 */     super(classLoader, attributes);
/* 44 */     this.annotationType = annotationType;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public void visitEnd() {
/* 50 */     AnnotationUtils.registerDefaultValues(this.attributes);
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\springframework\core\type\classreading\RecursiveAnnotationAttributesVisitor.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */