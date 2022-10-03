/*    */ package org.springframework.cglib.transform.impl;
/*    */ 
/*    */ import org.springframework.cglib.core.ClassGenerator;
/*    */ import org.springframework.cglib.core.DefaultGeneratorStrategy;
/*    */ import org.springframework.cglib.core.TypeUtils;
/*    */ import org.springframework.cglib.transform.ClassTransformer;
/*    */ import org.springframework.cglib.transform.MethodFilter;
/*    */ import org.springframework.cglib.transform.MethodFilterTransformer;
/*    */ import org.springframework.cglib.transform.TransformingClassGenerator;
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
/*    */ public class UndeclaredThrowableStrategy
/*    */   extends DefaultGeneratorStrategy
/*    */ {
/*    */   private Class wrapper;
/*    */   
/*    */   public UndeclaredThrowableStrategy(Class wrapper) {
/* 46 */     this.wrapper = wrapper;
/*    */   }
/*    */   
/* 49 */   private static final MethodFilter TRANSFORM_FILTER = new MethodFilter() {
/*    */       public boolean accept(int access, String name, String desc, String signature, String[] exceptions) {
/* 51 */         return (!TypeUtils.isPrivate(access) && name.indexOf('$') < 0);
/*    */       }
/*    */     };
/*    */   
/*    */   protected ClassGenerator transform(ClassGenerator cg) throws Exception {
/* 56 */     UndeclaredThrowableTransformer undeclaredThrowableTransformer = new UndeclaredThrowableTransformer(this.wrapper);
/* 57 */     MethodFilterTransformer methodFilterTransformer = new MethodFilterTransformer(TRANSFORM_FILTER, (ClassTransformer)undeclaredThrowableTransformer);
/* 58 */     return (ClassGenerator)new TransformingClassGenerator(cg, (ClassTransformer)methodFilterTransformer);
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\springframework\cglib\transform\impl\UndeclaredThrowableStrategy.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */