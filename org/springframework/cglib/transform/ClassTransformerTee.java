/*    */ package org.springframework.cglib.transform;
/*    */ 
/*    */ import org.springframework.asm.ClassVisitor;
/*    */ import org.springframework.cglib.core.Constants;
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
/*    */ public class ClassTransformerTee
/*    */   extends ClassTransformer
/*    */ {
/*    */   private ClassVisitor branch;
/*    */   
/*    */   public ClassTransformerTee(ClassVisitor branch) {
/* 25 */     super(Constants.ASM_API);
/* 26 */     this.branch = branch;
/*    */   }
/*    */   
/*    */   public void setTarget(ClassVisitor target) {
/* 30 */     this.cv = new ClassVisitorTee(this.branch, target);
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\springframework\cglib\transform\ClassTransformerTee.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */