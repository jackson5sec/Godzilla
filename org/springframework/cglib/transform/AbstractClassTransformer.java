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
/*    */ public abstract class AbstractClassTransformer
/*    */   extends ClassTransformer
/*    */ {
/*    */   protected AbstractClassTransformer() {
/* 23 */     super(Constants.ASM_API);
/*    */   }
/*    */   
/*    */   public void setTarget(ClassVisitor target) {
/* 27 */     this.cv = target;
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\springframework\cglib\transform\AbstractClassTransformer.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */