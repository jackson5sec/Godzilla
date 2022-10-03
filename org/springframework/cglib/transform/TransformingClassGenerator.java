/*    */ package org.springframework.cglib.transform;
/*    */ 
/*    */ import org.springframework.asm.ClassVisitor;
/*    */ import org.springframework.cglib.core.ClassGenerator;
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
/*    */ public class TransformingClassGenerator
/*    */   implements ClassGenerator
/*    */ {
/*    */   private ClassGenerator gen;
/*    */   private ClassTransformer t;
/*    */   
/*    */   public TransformingClassGenerator(ClassGenerator gen, ClassTransformer t) {
/* 27 */     this.gen = gen;
/* 28 */     this.t = t;
/*    */   }
/*    */   
/*    */   public void generateClass(ClassVisitor v) throws Exception {
/* 32 */     this.t.setTarget(v);
/* 33 */     this.gen.generateClass(this.t);
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\springframework\cglib\transform\TransformingClassGenerator.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */