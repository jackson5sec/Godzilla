/*    */ package org.jetbrains.annotations;
/*    */ 
/*    */ import java.lang.annotation.Documented;
/*    */ import java.lang.annotation.ElementType;
/*    */ import java.lang.annotation.Retention;
/*    */ import java.lang.annotation.RetentionPolicy;
/*    */ import java.lang.annotation.Target;
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
/*    */ @Documented
/*    */ @Retention(RetentionPolicy.CLASS)
/*    */ @Target({ElementType.METHOD, ElementType.FIELD, ElementType.PARAMETER, ElementType.LOCAL_VARIABLE, ElementType.TYPE, ElementType.PACKAGE})
/*    */ public @interface Nls
/*    */ {
/*    */   Capitalization capitalization() default Capitalization.NotSpecified;
/*    */   
/*    */   public enum Capitalization
/*    */   {
/* 36 */     NotSpecified,
/*    */ 
/*    */ 
/*    */     
/* 40 */     Title,
/*    */ 
/*    */ 
/*    */     
/* 44 */     Sentence;
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\jetbrains\annotations\Nls.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */