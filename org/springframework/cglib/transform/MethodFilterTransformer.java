/*    */ package org.springframework.cglib.transform;
/*    */ 
/*    */ import org.springframework.asm.ClassVisitor;
/*    */ import org.springframework.asm.MethodVisitor;
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
/*    */ public class MethodFilterTransformer
/*    */   extends AbstractClassTransformer
/*    */ {
/*    */   private MethodFilter filter;
/*    */   private ClassTransformer pass;
/*    */   private ClassVisitor direct;
/*    */   
/*    */   public MethodFilterTransformer(MethodFilter filter, ClassTransformer pass) {
/* 26 */     this.filter = filter;
/* 27 */     this.pass = pass;
/* 28 */     super.setTarget(pass);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
/* 36 */     return (this.filter.accept(access, name, desc, signature, exceptions) ? this.pass : this.direct).visitMethod(access, name, desc, signature, exceptions);
/*    */   }
/*    */   
/*    */   public void setTarget(ClassVisitor target) {
/* 40 */     this.pass.setTarget(target);
/* 41 */     this.direct = target;
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\springframework\cglib\transform\MethodFilterTransformer.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */