/*    */ package org.springframework.cglib.transform;
/*    */ 
/*    */ import org.springframework.asm.ClassReader;
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
/*    */ public class TransformingClassLoader
/*    */   extends AbstractClassLoader
/*    */ {
/*    */   private ClassTransformerFactory t;
/*    */   
/*    */   public TransformingClassLoader(ClassLoader parent, ClassFilter filter, ClassTransformerFactory t) {
/* 26 */     super(parent, parent, filter);
/* 27 */     this.t = t;
/*    */   }
/*    */   
/*    */   protected ClassGenerator getGenerator(ClassReader r) {
/* 31 */     ClassTransformer t2 = this.t.newInstance();
/* 32 */     return new TransformingClassGenerator(super.getGenerator(r), t2);
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\springframework\cglib\transform\TransformingClassLoader.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */